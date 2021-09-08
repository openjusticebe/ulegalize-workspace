package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumVCOwner;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class VcOwnerConverter implements AttributeConverter<EnumVCOwner, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumVCOwner value) {
        if (value == null) {
            return null;
        }

        return value.getId();
    }

    @Override
    public EnumVCOwner convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return EnumVCOwner.fromId(value);
    }
}
