package com.ulegalize.lawfirm.model.enumeration;

public enum EnumFactureType {
    SELL(1, "Facture vente", "FV"),
    CREDIT(2, "Note de crédit", "NC"),
    TEMP(3, "Facture temporaire", "FT");

    private long id;
    private String description;
    private String code;

    EnumFactureType(long id, String description, String code) {
        this.id = id;
        this.description = description;
        this.code = code;

    }

    public static EnumFactureType fromId(Long id) {
        for (EnumFactureType clientType : values()) {
            if (clientType.getId() == id)
                return clientType;
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
}
