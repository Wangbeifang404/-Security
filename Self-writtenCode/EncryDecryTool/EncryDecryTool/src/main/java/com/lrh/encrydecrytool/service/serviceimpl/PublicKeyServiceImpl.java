package com.lrh.encrydecrytool.service.serviceimpl;


import com.lrh.encrydecrytool.service.PublicKeyService;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;



@Service
@Slf4j
public class PublicKeyServiceImpl implements PublicKeyService {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    //RSA1024密钥初始化
    private final PrivateKey privateKey_RSA1024;
    public final PublicKey publicKey_RSA1024;

   {
       KeyPairGenerator keyGen = null;
       try {
           keyGen = KeyPairGenerator.getInstance("RSA");
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException(e);
       }
       keyGen.initialize(1024, new SecureRandom());
        KeyPair keys = keyGen.generateKeyPair();
        publicKey_RSA1024 = keys.getPublic();
        privateKey_RSA1024 = keys.getPrivate();

    }
    @Override
    public String RSA1024Encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey_RSA1024);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Override
    public String RSA1024Decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey_RSA1024);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }


    //ECC60密钥初始化
    private final PrivateKey privateKey_ECC160;
    private final PublicKey publicKey_ECC160;
    {
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp160r1");
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("EC", "BC");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        try {
            keyGen.initialize(ecSpec, new SecureRandom());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        KeyPair keys = keyGen.generateKeyPair();
        privateKey_ECC160 = keys.getPrivate();
        publicKey_ECC160 = keys.getPublic();
    }

    @Override
    public String ECC160Encrypt(String plainText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = plainText.getBytes();
        Cipher cipher = Cipher.getInstance("EC", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey_ECC160);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    @Override
    public String ECC160Decrypt(String encryptedText) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        byte[] cipherText = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance("EC", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey_ECC160);
        return  new String(cipher.doFinal(cipherText));
    }

    //RSASHA1密钥初始化
    private final PrivateKey privateKey_RSA_SHA1;
    private final PublicKey publicKey_RSA_SHA1;
    {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.initialize(2048);
        KeyPair keys = keyGen.generateKeyPair();
        privateKey_RSA_SHA1 = keys.getPrivate();
        publicKey_RSA_SHA1 = keys.getPublic();
    }

    @Override
    public String signRSASHA1(String data) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey_RSA_SHA1);
        signature.update(data.getBytes());
        byte[] digitalSignature = signature.sign();
        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    @Override
    public boolean verifyRSASHA1(String data,String signatureStr) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey_RSA_SHA1);
        signature.update(data.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);
    }


    //ECDSA密钥初始化
    private final PrivateKey privateKey_ECDSA;
    private final PublicKey publicKey_ECDSA;
    {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        try {
            keyGen.initialize(ecSpec, new SecureRandom());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        KeyPair keys = keyGen.generateKeyPair();
        privateKey_ECDSA = keys.getPrivate();
        publicKey_ECDSA = keys.getPublic();

    }

    @Override
    public String signECDSA(String data) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initSign(privateKey_ECDSA);
        ecdsa.update(data.getBytes());
        byte[] signature = ecdsa.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    @Override
    public boolean verifyECDSA(String data, String signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature ecdsa = Signature.getInstance("SHA256withECDSA");
        ecdsa.initVerify(publicKey_ECDSA);
        ecdsa.update(data.getBytes());
        return ecdsa.verify(Base64.getDecoder().decode(signature));
    }
}
