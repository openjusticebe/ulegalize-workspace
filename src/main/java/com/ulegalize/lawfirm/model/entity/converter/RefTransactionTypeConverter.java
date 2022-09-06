package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumRefTransaction;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RefTransactionTypeConverter implements AttributeConverter<EnumRefTransaction, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumRefTransaction value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumRefTransaction convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return EnumRefTransaction.fromId(value);
    }
}
