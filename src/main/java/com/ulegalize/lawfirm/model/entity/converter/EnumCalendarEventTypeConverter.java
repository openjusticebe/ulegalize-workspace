package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumCalendarEventType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumCalendarEventTypeConverter implements AttributeConverter<EnumCalendarEventType, String> {
    @Override
    public String convertToDatabaseColumn(EnumCalendarEventType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    @Override
    public EnumCalendarEventType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return EnumCalendarEventType.fromCode(value);
    }
}
