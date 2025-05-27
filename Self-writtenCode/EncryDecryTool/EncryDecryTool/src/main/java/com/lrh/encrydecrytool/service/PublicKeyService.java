package com.lrh.encrydecrytool.service;

import com.lrh.encrydecrytool.Common.CodingRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * 非对称加密相关服务接口，支持RSA和ECC算法的加密解密及数字签名功能。
 */
public interface PublicKeyService {

    /**
     * 使用RSA 1024位密钥加密明文。
     *
     * @param plainText 明文字符串，不能为空且非空字符串
     * @return 加密后的密文（Base64编码）
     * @throws NoSuchAlgorithmException 如果算法不存在
     * @throws NoSuchPaddingException 如果填充机制不存在
     * @throws IllegalBlockSizeException 如果数据块大小非法
     * @throws BadPaddingException 如果填充错误
     * @throws InvalidKeyException 如果密钥无效
     */
    String rsa1024Encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

    /**
     * 使用RSA 1024位密钥解密密文。
     *
     * @param encryptedText 密文字符串（Base64编码），不能为空且非空字符串
     * @return 解密后的明文
     * @throws NoSuchAlgorithmException 如果算法不存在
     * @throws NoSuchPaddingException 如果填充机制不存在
     * @throws InvalidKeyException 如果密钥无效
     * @throws IllegalBlockSizeException 如果数据块大小非法
     * @throws BadPaddingException 如果填充错误
     */
    String rsa1024Decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    /**
     * 使用ECC 160位密钥加密输入数据。
     *
     * @param input 明文字符串，不能为空且非空字符串
     * @return 加密后的密文（Base64编码）
     * @throws NoSuchAlgorithmException 如果算法不存在
     * @throws NoSuchProviderException 如果安全提供者不存在
     * @throws NoSuchPaddingException 如果填充机制不存在
     * @throws IllegalBlockSizeException 如果数据块大小非法
     * @throws BadPaddingException 如果填充错误
     * @throws InvalidKeyException 如果密钥无效
     */
    String ecc160Encrypt(String input) throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException;

    /**
     * 使用ECC 160位密钥解密输入数据。
     *
     * @param input 密文字符串（Base64编码），不能为空且非空字符串
     * @return 解密后的明文
     * @throws NoSuchAlgorithmException 如果算法不存在
     * @throws NoSuchProviderException 如果安全提供者不存在
     * @throws NoSuchPaddingException 如果填充机制不存在
     * @throws IllegalBlockSizeException 如果数据块大小非法
     * @throws BadPaddingException 如果填充错误
     * @throws InvalidKeyException 如果密钥无效
     */
    String ecc160Decrypt(String input) throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, NoSuchAlgorithmException;

    /**
     * 使用RSA和SHA-1算法对输入数据进行数字签名。
     *
     * @param input 待签名数据，不能为空且非空字符串
     * @return 签名值（Base64编码）
     * @throws SignatureException 签名异常
     * @throws InvalidKeyException 密钥无效异常
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    String signRsaSha1(String input) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * 验证RSA SHA-1数字签名。
     *
     * @param input 原始数据，不能为空且非空字符串
     * @param signature 签名值（Base64编码），不能为空且非空字符串
     * @return 验证结果，true为通过，false为失败
     * @throws InvalidKeyException 密钥无效异常
     * @throws SignatureException 签名异常
     * @throws NoSuchAlgorithmException 算法不存在异常
     */
    boolean verifyRsaSha1(String input, String signature) throws InvalidKeyException,
            SignatureException, NoSuchAlgorithmException;

    /**
     * 使用ECDSA算法对输入数据进行数字签名。
     *
     * @param input 待签名数据，不能为空且非空字符串
     * @return 签名值（Base64编码）
     * @throws InvalidKeyException 密钥无效异常
     * @throws NoSuchAlgorithmException 算法不存在异常
     * @throws SignatureException 签名异常
     */
    String signEcdsa(String input) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException;

    /**
     * 验证ECDSA数字签名。
     *
     * @param input 原始数据，不能为空且非空字符串
     * @param signature 签名值（Base64编码），不能为空且非空字符串
     * @return 验证结果，true为通过，false为失败
     * @throws NoSuchAlgorithmException 算法不存在异常
     * @throws SignatureException 签名异常
     * @throws InvalidKeyException 密钥无效异常
     */
    boolean verifyEcdsa(String input, String signature) throws NoSuchAlgorithmException,
            SignatureException, InvalidKeyException;
}
