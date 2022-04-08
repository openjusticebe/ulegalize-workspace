package com.ulegalize.lawfirm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    @Test
    void generateHashkey() {
        String hashKey = Utils.generateHashkey();

        assertNotEquals("", hashKey);
        assertTrue(hashKey.length() < 11);
        assertTrue(hashKey.length() > 0);
    }

    @Test
    void test_regex_ok() {
        assertTrue(com.ulegalize.utils.Utils.validateEmail("test@test.com"));
    }

    @Test
    void test_regex_nok2() {
        assertFalse(com.ulegalize.utils.Utils.validateEmail("test@@test.com"));
    }
}