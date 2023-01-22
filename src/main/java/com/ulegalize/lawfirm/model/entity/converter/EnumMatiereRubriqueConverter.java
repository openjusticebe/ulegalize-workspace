package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.lawfirm.model.enumeration.EnumMatiereRubrique;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EnumMatiereRubriqueConverter implements AttributeConverter<EnumMatiereRubrique, Long> {

    @Override
    public Long convertToDatabaseColumn(EnumMatiereRubrique value) {

        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumMatiereRubrique convertToEntityAttribute(Long value) {

        if (value == null) {
            return null;
        }

        return EnumMatiereRubrique.fromId(value);
    }
}
