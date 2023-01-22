package com.ulegalize.lawfirm.model.enumeration;

import lombok.Getter;

public enum EnumMesureType {
    PAGE(1),
    KM(2),
    FORFAIT(3);

    @Getter
    private final Integer id;

    EnumMesureType(Integer id) {
        this.id = id;
    }

    public static EnumMesureType fromId(Integer id) {
        for (EnumMesureType enumMesureType : values()) {
            if (enumMesureType.getId().equals(id)) {
                return enumMesureType;
            }
        }
        return null;
    }

}
