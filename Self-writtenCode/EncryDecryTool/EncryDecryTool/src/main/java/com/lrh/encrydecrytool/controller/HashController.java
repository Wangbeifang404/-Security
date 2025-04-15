package com.lrh.encrydecrytool.controller;

import com.lrh.encrydecrytool.Common.CodingRequest;
import com.lrh.encrydecrytool.Common.Result;
import com.lrh.encrydecrytool.service.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/hash")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HashController {
    @Autowired
    private HashService hashService;

    @PostMapping("/SHA1")
    public Result<String> SHA1(@RequestBody CodingRequest request) throws NoSuchAlgorithmException {
        log.info("SHA-1加密");
        return  Result.success(hashService.SHA1(request.getInput()));
    }

    @PostMapping("/SHA256")
    public Result<String> SHA256(@RequestBody CodingRequest request) throws NoSuchAlgorithmException {
        log.info("SHA-256加密");
        return  Result.success(hashService.SHA256(request.getInput()));
    }

    // bitLength支持标准变体：224/256/384/512
    @PostMapping("/SHA3")
    public Result<String> SHA3(@RequestBody CodingRequest request, @RequestParam int bitLength) throws NoSuchAlgorithmException {
        log.info("SHA-3加密");
        return Result.success(hashService.SHA3(request.getInput(), bitLength));
    }

    @PostMapping("/RIPEMD160")
    public Result<String> RIPEMD160(@RequestBody CodingRequest request){
        log.info("RIPEMD160加密");
        return Result.success(hashService.RIPEMD160(request.getInput()));
    }

    //为HMac生成密钥
    @GetMapping("/secretKey")
    public Result<String> getSecretKey(@RequestParam int length){
        log.info("生成密钥");
        return Result.success(hashService.generateKey(length));
    }

    @PostMapping("/HMacSHA1")
    public Result<String> HMacSHA1(@RequestBody CodingRequest request, @RequestParam String secret) throws NoSuchAlgorithmException {
        log.info("HMacSHA1");
        return Result.success(hashService.HMacSHA1(request.getInput(), secret));
    }


    @PostMapping("/HMacSHA256")
    public Result<String> HMacSHA256(@RequestBody CodingRequest request, @RequestParam String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("HMacSHA256");
        return Result.success(hashService.HMacSHA256(request.getInput(), secret));
    }

    @PostMapping("/PBKDF2")
    public Result<String> PBKDF2(@RequestBody CodingRequest request) {
        log.info("PBKDF2");
        return Result.success(hashService.PBKDF2(request.getInput()));
    }
}
