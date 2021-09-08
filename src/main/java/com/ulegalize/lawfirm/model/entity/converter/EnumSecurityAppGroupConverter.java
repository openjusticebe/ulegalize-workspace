package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumSecurityAppGroups;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumSecurityAppGroupConverter implements AttributeConverter<EnumSecurityAppGroups, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumSecurityAppGroups enumSecurityAppGroups) {
        return enumSecurityAppGroups.getId();
    }

    @Override
    public EnumSecurityAppGroups convertToEntityAttribute(Integer dbData) {
        return EnumSecurityAppGroups.fromId(dbData);
    }

}
