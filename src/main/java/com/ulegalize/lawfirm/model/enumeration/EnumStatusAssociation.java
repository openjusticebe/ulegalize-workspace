package com.ulegalize.lawfirm.model.enumeration;

import lombok.Getter;

public enum EnumStatusAssociation {
    PENDING("PENDING"),
    REFUSED("REFUSED"),
    ACCEPTED("ACCEPTED");

    @Getter
    private final String code;

    EnumStatusAssociation(String code) {
        this.code = code;
    }
}
