package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumDossierContactType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DossierContactTypeConverter implements AttributeConverter<EnumDossierContactType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumDossierContactType value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumDossierContactType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return EnumDossierContactType.fromId(value);
    }
}
