package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumTType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TTypeConverter implements AttributeConverter<EnumTType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumTType value) {
        if (value == null) {
            return null;
        }

        return value.getIdType();
    }

    @Override
    public EnumTType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return EnumTType.fromId(value);
    }
}
