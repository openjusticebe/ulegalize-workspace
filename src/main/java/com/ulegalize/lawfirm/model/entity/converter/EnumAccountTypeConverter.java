package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumAccountType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumAccountTypeConverter implements AttributeConverter<EnumAccountType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumAccountType value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumAccountType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return EnumAccountType.fromId(value);
    }
}
