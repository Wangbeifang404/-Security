package com.lrh.encrydecrytool.service;

import com.lrh.encrydecrytool.Common.CodingRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public interface PublicKeyService {
    String RSA1024Encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException ;

    String RSA1024Decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    String ECC160Encrypt(String input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException;

    String ECC160Decrypt(String input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, NoSuchAlgorithmException;

    String signRSASHA1(String input) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException;

    boolean verifyRSASHA1(String input, String signature) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException;

    String signECDSA(String input) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException;

    boolean verifyECDSA(String input, String signature) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException;
}
