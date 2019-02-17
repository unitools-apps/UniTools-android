package com.github.ali77gh.unitools.core;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

public class StringCoder {

    public static String Encode(String input) {
        byte[] data = input.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
    }

    public static String Decode(String base64) {
        byte[] data2 = Base64.decode(base64, Base64.DEFAULT);
        return new String(data2, StandardCharsets.UTF_8);
    }
}