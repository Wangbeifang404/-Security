package com.lrh.encrydecrytool.service.serviceimpl;

import com.lrh.encrydecrytool.service.SymmetricalService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

@Slf4j
@Service
public class SymmetricalServiceImpl implements SymmetricalService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public String generateSM4Key() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGen = KeyGenerator.getInstance("SM4", "BC");
        //128位
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    @Override
    public String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        SecretKey key = kg.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    @Override
    public String generateRC6Key(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        //keySize只支持128，256，192，默认128
        if (keySize != 128 || keySize != 256 || keySize != 192) {
            keySize = 128;
        }
        KeyGenerator keyGen = KeyGenerator.getInstance("RC6", "BC");
        keyGen.init(keySize);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    @Override
    public String encodeAES(String input, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 解码密钥
        byte[] keyBytes= Base64.getDecoder().decode(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        // 生成随机IV
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        // 执行加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        //组合iv和密文
        byte[] encrypted = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @Override
    public String encodeSM4(String plainText, String base64Key) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 解码密钥
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "SM4");

        // 生成随机IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 初始化加密器
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // 执行加密
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        // 组合IV和密文
        byte[] encryptedData = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @Override
    public String encodeRC6(String plainText, String base64Key) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 解码密钥
        log.info(base64Key);
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "RC6");

        // 生成随机IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 初始化加密器
        Cipher cipher = Cipher.getInstance("RC6/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // 执行加密
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        // 组合IV和密文
        byte[] encryptedData = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @Override
    public String decodeAES(String input, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 解码密钥和数据
        byte[] keyBytes= Base64.getDecoder().decode(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] encryptedBytes = Base64.getDecoder().decode(input);
        // 分离IV和密文
        byte[] iv = new byte[16];
        byte[] cipherText = new byte[encryptedBytes.length - iv.length];
        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        System.arraycopy(encryptedBytes, iv.length, cipherText, 0, cipherText.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        // 执行解密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec, ivSpec);
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }

    @Override
    public String decodeSM4(String encryptedData, String base64Key) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        // 密钥解码
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "SM4");

        // 解码加密数据
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // 分离IV和密文（前16字节为IV）
        byte[] iv = new byte[16];
        byte[] cipherText = new byte[encryptedBytes.length - iv.length];
        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        System.arraycopy(encryptedBytes, iv.length, cipherText, 0, cipherText.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 初始化解密器
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // 执行解密
        byte[] plainTextBytes = cipher.doFinal(cipherText);
        return new String(plainTextBytes);
    }

    @Override
    public String decodeRC6(String encryptedData, String base64Key) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 解码密钥和数据
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "RC6");
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // 分离IV和密文
        byte[] iv = new byte[16];
        byte[] cipherText = new byte[encryptedBytes.length - 16];
        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        System.arraycopy(encryptedBytes, iv.length, cipherText, 0, cipherText.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 初始化解密器
        Cipher cipher = Cipher.getInstance("RC6/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // 执行解密
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }
}
