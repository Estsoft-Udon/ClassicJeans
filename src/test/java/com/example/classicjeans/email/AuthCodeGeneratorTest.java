package com.example.classicjeans.email;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthCodeGeneratorTest {

    @Test
    void testGenerateCodeLength() {
        int length = 10;
        String code = AuthCodeGenerator.generateCode(length);

        // 길이가 올바른지 확인
        assertEquals(length, code.length(), "Code length should be " + length);
    }

    @Test
    void testGenerateCodeCharacterSet() {
        int length = 20;
        String code = AuthCodeGenerator.generateCode(length);

        // 코드 내의 모든 문자가 유효한 문자 집합에 속하는지 확인
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            assertTrue(validChars.indexOf(c) >= 0, "Invalid character found: " + c);
        }
    }

    @Test
    void testGenerateCodeRandomness() {
        // 동일한 길이로 두 개의 코드를 생성하고, 서로 다른 코드가 나오는지 확인
        int length = 8;
        String code1 = AuthCodeGenerator.generateCode(length);
        String code2 = AuthCodeGenerator.generateCode(length);

        assertNotEquals(code1, code2, "Generated codes should be different.");
    }
}
