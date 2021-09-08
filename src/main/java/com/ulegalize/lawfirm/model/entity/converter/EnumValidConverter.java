package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.lawfirm.model.enumeration.EnumValid;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumValidConverter implements AttributeConverter<EnumValid, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumValid enumValid) {
        return enumValid.getId();
    }

    @Override
    public EnumValid convertToEntityAttribute(Integer dbData) {
        return EnumValid.fromId(dbData);
    }

}
