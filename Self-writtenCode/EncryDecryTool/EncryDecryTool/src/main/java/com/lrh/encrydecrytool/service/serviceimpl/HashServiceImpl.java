package com.lrh.encrydecrytool.service.serviceimpl;

import com.lrh.encrydecrytool.service.HashService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;


import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HexFormat;

import static org.bouncycastle.internal.asn1.cms.CMSObjectIdentifiers.data;

@Slf4j
@Service
public class HashServiceImpl implements HashService {

    @Override
    public String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    public String SHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = String.format("%02x", 0xFF & b);
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    public String SHA3(String input,int bitLength) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-"+bitLength);
        byte[] hashBytes = md.digest(input.getBytes());
        return HexFormat.of().formatHex(hashBytes);
    }

    @Override
    public String RIPEMD160(String input) {
        // 将字符串转换为字节数组（使用UTF-8编码）
        byte[] inputBytes = input.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // 初始化RIPEMD160摘要算法
        RIPEMD160Digest digest = new RIPEMD160Digest();

        // 更新摘要数据
        digest.update(inputBytes, 0, inputBytes.length);

        // 创建接收哈希值的字节数组
        byte[] result = new byte[digest.getDigestSize()];

        // 生成最终哈希值
        digest.doFinal(result, 0);

        // 将字节数组转换为十六进制字符串
        return Hex.toHexString(result);
    }

    @Override
    public String HMacSHA1(String input, String key) throws NoSuchAlgorithmException {
        if (input == null || key == null) {
            throw new IllegalArgumentException("输入参数不能为空");
        }
        try {
            // 2. 解码 Base64 密钥
            log.info(key);
            byte[] keyBytes = Base64.getUrlDecoder().decode(key);

            // 3. 初始化 HMAC
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac hmac = Mac.getInstance("HmacSHA1");
            hmac.init(secretKey);

            // 4. 计算哈希
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            byte[] hmacBytes = hmac.doFinal(inputBytes);

            // 5. 转换为十六进制
            return bytesToHex(hmacBytes);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的HMAC密钥", e);
        }
    }

    @Override
    public String generateKey(int length) {
        // 参数校验
        if (length < 16) {
            throw new IllegalArgumentException("密钥长度至少16字节");
        }

        // 创建加密强随机数生成器
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法获取安全随机源", e);
        }

        // 生成随机字节
        byte[] keyBytes = new byte[length];
        secureRandom.nextBytes(keyBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
    }

    @Override
    public String HMacSHA256(String input, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secret);

        // 初始化HMAC-SHA256
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);

        // 计算签名
        byte[] rawHmac = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));

        // 返回Base64编码结果
        return Base64.getEncoder().encodeToString(rawHmac);
    }


    // 安全参数（可根据需求调整）
    private final int ITERATIONS = 100_000; // 迭代次数
    private final int SALT_LENGTH = 16;     // 盐值长度（字节）
    private final int KEY_LENGTH = 256;      // 密钥长度（位）
    private final String ALGORITHM = "PBKDF2WithHmacSHA256";
    @Override
    public String PBKDF2(String input) {

        try {
            // 1. 生成随机盐值
            byte[] salt = generateSalt();

            // 2. 创建密钥规范
            PBEKeySpec spec = new PBEKeySpec(
                    input.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            // 3. 执行哈希计算
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // 4. 组合盐值和哈希结果
            return formatResult(salt, hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("PBKDF2哈希生成失败", e);
        }
    }


    //生成加密安全的随机盐值
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

   // 格式化输出结果（盐值+哈希）
    private String formatResult(byte[] salt, byte[] hash) {
        return String.join("$",
                Base64.getEncoder().encodeToString(salt),
                String.valueOf(ITERATIONS),
                Base64.getEncoder().encodeToString(hash)
        );
    }
    private  String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            int v = b & 0xFF;
            if (v < 16) {
                hexString.append('0');
            }
            hexString.append(Integer.toHexString(v));
        }
        return hexString.toString();
    }
}
