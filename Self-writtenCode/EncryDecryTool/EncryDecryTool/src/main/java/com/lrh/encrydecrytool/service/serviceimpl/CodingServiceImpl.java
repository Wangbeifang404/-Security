package com.lrh.encrydecrytool.service.serviceimpl;

import com.lrh.encrydecrytool.service.CodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

@Slf4j
@Service
public class CodingServiceImpl implements CodingService {

    @Override
    public String base64Encode(String input) {
    Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String base64Decode(String input) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(input));
    }

    @Override
    public String utf8Encode(String input) {
        byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);
        return HexFormat.of().formatHex(utf8Bytes);
    }

    @Override
    public String utf8Decode(String input) {
        byte[] bytes = HexFormat.of().parseHex(input);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
