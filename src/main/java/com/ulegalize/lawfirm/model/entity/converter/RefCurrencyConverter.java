package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumRefCurrency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RefCurrencyConverter implements AttributeConverter<EnumRefCurrency, String> {
    @Override
    public String convertToDatabaseColumn(EnumRefCurrency value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    @Override
    public EnumRefCurrency convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return EnumRefCurrency.fromCode(code);
    }
}
