package com.lrh.encrydecrytool.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface HashService {
    String SHA1(String input) throws NoSuchAlgorithmException;

    String SHA256(String input) throws NoSuchAlgorithmException;

    String SHA3(String input, int bitLength) throws NoSuchAlgorithmException;

    String RIPEMD160(String input);

    String HMacSHA1(String input, String key) throws NoSuchAlgorithmException;

    String generateKey(int length);

    String HMacSHA256(String input, String secret) throws NoSuchAlgorithmException, InvalidKeyException;


    String PBKDF2(String input);
}
