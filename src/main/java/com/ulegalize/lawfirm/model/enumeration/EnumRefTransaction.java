package com.ulegalize.lawfirm.model.enumeration;

import lombok.Getter;

public enum EnumRefTransaction {
    VIREMENT(1, "Virement"),
    CREDIT(2, "Carte de crédit"),
    CASH(3, "Espèce"),
    BANCONTACT(4, "Bancontact"),
    CHEQUE(5, "Chèque"),
    NA(6, "NA");

    @Getter
    private Integer id;
    @Getter
    private String description;

    EnumRefTransaction(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static EnumRefTransaction fromId(Integer id) {
        for (EnumRefTransaction enumRefTransaction : values()) {
            if (enumRefTransaction.getId().equals(id)) {
                return enumRefTransaction;
            }
        }
        return null;
    }
}
