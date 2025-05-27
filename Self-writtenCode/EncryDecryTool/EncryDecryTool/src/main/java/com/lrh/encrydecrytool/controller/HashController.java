package com.lrh.encrydecrytool.controller;

import com.lrh.encrydecrytool.Common.CodingRequest;
import com.lrh.encrydecrytool.Common.Result;
import com.lrh.encrydecrytool.service.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/hash")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HashController {

    private final HashService hashService;

    @Autowired
    public HashController(HashService hashService) {
        this.hashService = hashService;
    }

    /**
     * SHA-1 哈希
     */
    @PostMapping("/SHA1")
    public Result<String> sha1(@Valid @RequestBody CodingRequest request) throws NoSuchAlgorithmException {
        log.info("SHA-1加密");
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        return Result.success(hashService.SHA1(input));
    }

    /**
     * SHA-256 哈希
     */
    @PostMapping("/SHA256")
    public Result<String> sha256(@Valid @RequestBody CodingRequest request) throws NoSuchAlgorithmException {
        log.info("SHA-256加密");
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        return Result.success(hashService.SHA256(input));
    }

    /**
     * SHA-3 哈希，bitLength 支持 224, 256, 384, 512
     */
    @PostMapping("/SHA3")
    public Result<String> sha3(
            @Valid @RequestBody CodingRequest request,
            @RequestParam @Min(224) int bitLength) throws NoSuchAlgorithmException {
        log.info("SHA-3加密，bitLength={}", bitLength);
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        if (bitLength != 224 && bitLength != 256 && bitLength != 384 && bitLength != 512) {
            return Result.error("bitLength 必须是 224, 256, 384 或 512");
        }
        return Result.success(hashService.SHA3(input, bitLength));
    }

    /**
     * RIPEMD160 哈希
     */
    @PostMapping("/RIPEMD160")
    public Result<String> ripemd160(@Valid @RequestBody CodingRequest request) {
        log.info("RIPEMD160加密");
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        return Result.success(hashService.RIPEMD160(input));
    }

    /**
     * 生成安全随机密钥，长度必须大于0
     */
    @GetMapping("/secretKey")
    public Result<String> getSecretKey(@RequestParam @Min(1) int length) {
        log.info("生成密钥，长度={}", length);
        try {
            return Result.success(hashService.generateKey(length));
        } catch (IllegalArgumentException e) {
            return Result.error("密钥长度非法: " + e.getMessage());
        }
    }

    /**
     * HMac-SHA1 计算
     */
    @PostMapping("/HMacSHA1")
    public Result<String> hmacSha1(
            @Valid @RequestBody CodingRequest request,
            @RequestParam String secret) throws NoSuchAlgorithmException {
        log.info("HMacSHA1计算");
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        if (secret == null || secret.isEmpty()) {
            return Result.error("秘钥不能为空");
        }
        return Result.success(hashService.HMacSHA1(input, secret));
    }

    /**
     * HMac-SHA256 计算
     */
    @PostMapping("/HMacSHA256")
    public Result<String> hmacSha256(
            @Valid @RequestBody CodingRequest request,
            @RequestParam String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("HMacSHA256计算");
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        if (secret == null || secret.isEmpty()) {
            return Result.error("秘钥不能为空");
        }
        return Result.success(hashService.HMacSHA256(input, secret));
    }

    /**
     * PBKDF2 密码哈希
     */
    @PostMapping("/PBKDF2")
    public Result<String> pbkdf2(@Valid @RequestBody CodingRequest request) {
        log.info("PBKDF2计算");
        String input = request.getInput();
        if (input == null || input.isEmpty()) {
            return Result.error("输入不能为空");
        }
        return Result.success(hashService.PBKDF2(input));
    }
}
