package com.ulegalize.lawfirm.model.enumeration;

import lombok.Getter;

public enum EnumMesureType {
    PAGE(1, "Page", "Page", "Bladzijde"),
    KM(2, "Km", "Km", "Km"),
    FORFAIT(3, "Forfait", "Flat rate", "Vast bedrag");

    @Getter
    private Integer id;
    @Getter
    private String descriptionFr;
    @Getter
    private String descriptionEn;
    @Getter
    private String descriptionNl;

    EnumMesureType(Integer id, String descriptionFr, String descriptionEn, String descriptionNl) {
        this.id = id;
        this.descriptionFr = descriptionFr;
        this.descriptionEn = descriptionEn;
        this.descriptionNl = descriptionNl;
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
