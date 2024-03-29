package com.ulegalize.lawfirm.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CalendarEventsUtil {

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
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date convertToDateViaInstant(ZonedDateTime dateToConvert) {
        return dateToConvert != null ? java.util.Date
                .from(dateToConvert.toInstant()) : null;
    }

    public static Date convertZoneToDateViaInstant(ZonedDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.toInstant());
    }

}
