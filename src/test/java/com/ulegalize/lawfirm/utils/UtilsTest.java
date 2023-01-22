package com.ulegalize.lawfirm.utils;

import com.ulegalize.enumeration.EnumLanguage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

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

    @Test
    void test_local() {
        assertEquals(Locale.FRENCH, new Locale(EnumLanguage.FR.getShortCode()));
    }

    @Test
    void test_date_locale() {
        Locale loc = Locale.forLanguageTag(EnumLanguage.DE.getShortCode());
        ZonedDateTime zonedDateTime = CalendarEventsUtil.convertToZoneDateTimeViaInstant(new Date());

        String format = zonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(loc));

        assertNotEquals("", format);
        System.out.println("DE " + format);

        Locale locfr = Locale.forLanguageTag(EnumLanguage.FR.getShortCode());
        format = zonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locfr));
        System.out.println("FR " + format);
        assertNotEquals("", format);
    }

    @Test
    void test_A_validNomenclature_valid() {
        assertTrue(Utils.validNomenclature("WORK-1234"));
    }

    @Test
    void test_B_validNomenclature_valid() {
        assertTrue(Utils.validNomenclature(LocalDate.now().getYear() + "-0005"));
    }

    @Test
    void test_D_validNomenclature_maxLength_not_valid() {
        // < 4
        assertFalse(Utils.validNomenclature("0001/test"));
    }
}