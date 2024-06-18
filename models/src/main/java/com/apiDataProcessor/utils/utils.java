package com.apiDataProcessor.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class utils {
    public static String hashString(String str) {
        if (str == null || str.isBlank()) {
            return "";
        }
        return Hashing.sha256().hashString(str, StandardCharsets.UTF_8).toString();
    }
}
