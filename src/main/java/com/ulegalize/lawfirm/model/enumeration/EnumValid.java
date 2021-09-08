package com.ulegalize.lawfirm.model.enumeration;

public enum EnumValid {
    PENDING(-1),
    UNVERIFIED(1),
    VERIFIED(2);

    private int id;

    EnumValid(int id) {
        this.id = id;
    }

    public static EnumValid fromId(int id) {
        for (EnumValid clientType : values()) {
            if (clientType.getId() == id)
                return clientType;
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
