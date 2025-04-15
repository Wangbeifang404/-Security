package com.lrh.encrydecrytool.service;

public interface CodingService {
    String base64Encode(String input);
    String base64Decode(String input);
    String utf8Encode(String input);
    String utf8Decode(String input);
}
