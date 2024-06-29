package com.apiDataProcessor.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class utils {
    public static String hashString(String str) {
        if (str == null || str.isBlank()) {
            return "";
        }
        return Hashing.sha256().hashString(str, StandardCharsets.UTF_8).toString();
    }

    public static String basicAuthHeader(String username, String password) {
        if (isEmpty(username) || isEmpty(password)) {
            return "";
        }
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public static boolean validateBasicAuth(String token, String username, String password) {
        if (isEmpty(token) || isEmpty(username) || isEmpty(password)) {
            return false;
        }
        return token.equals(basicAuthHeader(username, password));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }
}
