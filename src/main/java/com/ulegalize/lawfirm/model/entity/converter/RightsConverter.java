package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.security.EnumRights;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RightsConverter implements AttributeConverter<EnumRights, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EnumRights attribute) {
        return attribute.getId();
    }

    @Override
    public EnumRights convertToEntityAttribute(Integer dbId) {
        return EnumRights.fromId(dbId);
    }
}
