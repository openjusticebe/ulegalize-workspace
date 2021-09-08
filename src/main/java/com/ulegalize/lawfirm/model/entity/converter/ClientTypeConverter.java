package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumClientType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ClientTypeConverter implements AttributeConverter<EnumClientType, Long> {
    @Override
    public Long convertToDatabaseColumn(EnumClientType value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumClientType convertToEntityAttribute(Long value) {
        if (value == null) {
            return null;
        }

        return EnumClientType.fromId(value);
    }
}
