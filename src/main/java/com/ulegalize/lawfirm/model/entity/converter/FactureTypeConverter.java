package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumFactureType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FactureTypeConverter implements AttributeConverter<EnumFactureType, Long> {
    @Override
    public Long convertToDatabaseColumn(EnumFactureType value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumFactureType convertToEntityAttribute(Long value) {
        if (value == null) {
            return null;
        }

        return EnumFactureType.fromId(value);
    }
}
