package com.ulegalize.lawfirm.model.entity.converter;

import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumStatusAssociationConverter implements AttributeConverter<EnumStatusAssociation, String> {

    @Override
    public String convertToDatabaseColumn(EnumStatusAssociation enumStatusAssociation) {
        if (enumStatusAssociation == null) {
            return null;
        }
        return enumStatusAssociation.getCode();
    }

    @Override
    public EnumStatusAssociation convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumStatusAssociation.valueOf(dbData);
    }

}
