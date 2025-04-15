package com.lrh.encrydecrytool.controller;

import com.lrh.encrydecrytool.Common.CodingRequest;
import com.lrh.encrydecrytool.Common.Result;
import com.lrh.encrydecrytool.service.PublicKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

@Slf4j
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicKeyController {
    @Autowired
    private PublicKeyService publicKeyService;


    @PostMapping("/encode/RSA1024")
    public Result<String> encodeRSA1024(@RequestBody CodingRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        log.info("RSA1024加密");
        return Result.success(publicKeyService.RSA1024Encrypt(request.getInput()));
    }

    @PostMapping("/decode/RSA1024")
    public Result<String> decodeRSA1024(@RequestBody CodingRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        log.info("RSA1024解密");
        return Result.success(publicKeyService.RSA1024Decrypt(request.getInput()));
    }

    @PostMapping("/encode/ECC160")
    public Result<String> encodeECC160(@RequestBody CodingRequest request) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        log.info("ECC160加密");
        return Result.success(publicKeyService.ECC160Encrypt(request.getInput()));
    }

    @PostMapping("/decode/ECC160")
    public Result<String> decodeECC160(@RequestBody CodingRequest request) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        log.info("ECC160解密");
        return Result.success(publicKeyService.ECC160Decrypt(request.getInput()));
    }

    @PostMapping("/sign/RSASHA1")
    public Result<String> signRSASHA1(@RequestBody CodingRequest request) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("RSASHA1签名");
        return Result.success(publicKeyService.signRSASHA1(request.getInput()));
    }

    @PostMapping("/verify/RSASHA1")
    public Result<String> verifyRSASHA1(@RequestBody CodingRequest request, @RequestParam String signature) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("验证RSASHA1签名");
        if(publicKeyService.verifyRSASHA1(request.getInput(),signature)){
           return Result.success("success");
       }else {
           return Result.success("fail");
       }
    }

    @PostMapping("sign/ECDSA")
    public Result<String> signECDSA(@RequestBody CodingRequest request) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("ECDSA签名");
        return Result.success(publicKeyService.signECDSA(request.getInput()));
    }

    @PostMapping("verify/ECDSA")
    public Result<String> verifyECDSA(@RequestBody CodingRequest request, @RequestParam String signature) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("验证ECDSA签名");
        if(publicKeyService.verifyECDSA(request.getInput(),signature)){
            return Result.success("success");
        }else {
            return Result.success("fail");
        }
    }
}
