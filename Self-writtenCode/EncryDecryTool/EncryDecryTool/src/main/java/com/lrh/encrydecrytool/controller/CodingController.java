package com.lrh.encrydecrytool.controller;

import com.lrh.encrydecrytool.Common.CodingRequest;
import com.lrh.encrydecrytool.Common.Result;
import com.lrh.encrydecrytool.service.CodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coding")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CodingController {
    @Autowired
    private CodingService codingService;

    //base64 加密
    @PostMapping("/encode/base64")
    public Result<String> base64Encode(@RequestBody CodingRequest input) {
        log.info("base64编码");

        return Result.success(codingService.base64Encode(input.getInput()));
    }

    @PostMapping("/decode/base64")
    public Result<String> base64Decode(@RequestBody CodingRequest input) {
        log.info("base64解码");
        return Result.success(codingService.base64Decode(input.getInput()));
    }

    @PostMapping("/encode/utf8")
    public Result<String> utf8Encode(@RequestBody CodingRequest input) {
        log.info("utf-8编码");
        return Result.success(codingService.utf8Encode(input.getInput()));
    }

    @PostMapping("/decode/utf8")
    public Result<String> utf8Decode(@RequestBody CodingRequest input) {
        log.info("utf-8解码");
        return Result.success(codingService.utf8Decode(input.getInput()));
    }
}
