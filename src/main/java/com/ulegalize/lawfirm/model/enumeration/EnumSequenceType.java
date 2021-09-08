package com.ulegalize.lawfirm.model.enumeration;

public enum EnumSequenceType {
    TEMP_VC;

    EnumSequenceType() {
    }

    public static EnumSequenceType fromType(String name) {
        for (EnumSequenceType sequenceType : values()) {
            if (sequenceType.name().equalsIgnoreCase(name))
                return sequenceType;
        }
        return null;
    }

}
