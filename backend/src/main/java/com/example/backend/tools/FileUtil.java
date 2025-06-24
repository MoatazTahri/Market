package com.example.backend.tools;

import java.util.UUID;

public class FileUtil {
    public static String codeFileName(String fileName) {
        String extension = "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            extension = fileName.substring(lastDotIndex + 1);
        }
        String code = UUID.randomUUID().toString();
        return code + "." + extension;
    }
}
