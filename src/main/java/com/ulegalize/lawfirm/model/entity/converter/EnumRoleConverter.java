package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.enumeration.EnumRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumRoleConverter implements AttributeConverter<EnumRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumRole enumRole) {
        return enumRole.getIdRole();
    }

    @Override
    public EnumRole convertToEntityAttribute(Integer dbData) {
        return EnumRole.fromId(dbData);
    }

}
