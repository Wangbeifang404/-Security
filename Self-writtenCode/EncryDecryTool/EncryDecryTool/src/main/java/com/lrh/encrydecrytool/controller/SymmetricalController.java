package com.lrh.encrydecrytool.controller;

import com.lrh.encrydecrytool.Common.CodingRequest;
import com.lrh.encrydecrytool.Common.Result;

import com.lrh.encrydecrytool.service.SymmetricalService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Slf4j
@RestController
@RequestMapping("/symmetrical")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SymmetricalController {
    @Autowired
    private SymmetricalService symmetricalService;

    @GetMapping("/key/AES")
    public Result<String> getAESKey() throws NoSuchAlgorithmException {
        log.info("生成AES密钥");
        return Result.success(symmetricalService.generateAESKey());
    }

    @GetMapping("/key/RC6")
    public Result<String> getRC6Key(@RequestParam(required = false) Integer keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        log.info("生成RC6密钥");
        return Result.success(symmetricalService.generateRC6Key(keySize == null ? 0 : keySize));
    }

    @GetMapping("/key/SM4")
    public Result<String> getSM4Key() throws NoSuchAlgorithmException,
            NoSuchProviderException {
        log.info("生成RC4密钥");
        return Result.success(symmetricalService.generateSM4Key());
    }

    @PostMapping("/encode/AES")
    public Result<String> encodeAES(@RequestBody CodingRequest request, @RequestParam String key)
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException{
        log.info("AES加密");
        return Result.success(symmetricalService.encodeAES(request.getInput(), key));
    }

    @PostMapping("/encode/SM4")
    public Result<String> encodeSM4(@RequestBody CodingRequest request, @RequestParam String key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        log.info("SM4加密");
        return Result.success(symmetricalService.encodeSM4(request.getInput(), key));
    }

    @PostMapping("/encode/RC6")
    public Result<String> encodeRC6(@RequestBody CodingRequest request, @RequestParam String key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        log.info("RC6加密");
        return Result.success(symmetricalService.encodeRC6(request.getInput(), key));
    }

    @PostMapping("/decode/AES")
    public Result<String> decodeAES(@RequestBody CodingRequest request, @RequestParam String key) throws
            InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        log.info("AES解密");
        return Result.success(symmetricalService.decodeAES(request.getInput(), key));
    }

    @PostMapping("/decode/SM4")
    public Result<String> decodeSM4(@RequestBody CodingRequest request, @RequestParam String key) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        log.info("SM4解密");
        return Result.success(symmetricalService.decodeSM4(request.getInput(), key));
    }

    @PostMapping("/decode/RC6")
    public Result<String> decodeRC6(@RequestBody CodingRequest request, @RequestParam String key) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        log.info("RC6解密");
        return Result.success(symmetricalService.decodeRC6(request.getInput(), key));
    }


}
