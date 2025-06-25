package com.example.backend.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilTest {

    @Test
    public void shouldCodeFileName() {
        String codedFileName = FileUtil.codeFileName("tes sq sq . sqs-qs -qs t.txt");
        assertTrue(codedFileName.matches("[a-zA-Z0-9\\-]+\\.txt")); // contains only letters, numbers, and "-"
    }
}
