package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.lawfirm.model.enumeration.EnumSequenceType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SequenceTypeConverter implements AttributeConverter<EnumSequenceType, String> {
    @Override
    public String convertToDatabaseColumn(EnumSequenceType value) {
        if (value == null) {
            return null;
        }

        return value.name();
    }

    @Override
    public EnumSequenceType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return EnumSequenceType.fromType(value);
    }
}
