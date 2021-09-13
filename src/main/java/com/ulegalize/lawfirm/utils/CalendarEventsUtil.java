package com.ulegalize.lawfirm.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CalendarEventsUtil {
    public static int SLOT_EVENT = 30;

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static ZonedDateTime convertToZoneDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert
                .toInstant()
                .atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime convertToZoneDateTimeViaInstant(ZonedDateTime dateToConvert) {
        return dateToConvert
                .toInstant()
                .atZone(ZoneId.systemDefault());
    }

    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date convertToDateViaInstant(ZonedDateTime dateToConvert) {
        return Date
                .from(dateToConvert.toInstant());
    }

    public static Date convertZoneToDateViaInstant(ZonedDateTime dateToConvert) {
        return Date
                .from(dateToConvert.toInstant());
    }

}
