package com.ulegalize.lawfirm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void generateHashkey() {
        String hashKey = Utils.generateHashkey();

        assertNotEquals("", hashKey);
        assertTrue(hashKey.length() < 11);
        assertTrue(hashKey.length() > 0);
    }
}