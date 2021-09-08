package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumClientType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumClientTypeConverter implements AttributeConverter<EnumClientType, Long> {

    @Override
    public Long convertToDatabaseColumn(EnumClientType enumClientType) {
        return enumClientType.getId();
    }

    @Override
    public EnumClientType convertToEntityAttribute(Long dbData) {
        return EnumClientType.fromId(dbData);
    }

}
