package com.ulegalize.lawfirm.model.enumeration;

import lombok.Getter;

public enum EnumMatiere {
    DROIT_FAMILLE_ET_PERSONNES(1L),
    DROIT_BIENS(2L),
    ASSURANCES(3L),
    DROIT_CONSTRUCTION(4L),
    DROIT_JUDICIAIRE(5L),
    SOCIETES(6L),
    DROIT_COMMERCIAL(7L),
    DROIT_TRANSPORTS(8L),
    DROITS_INTELLECTUELS(9L),
    DROIT_SOCIAL(10L),
    DROIT_FISCAL(11L),
    DROIT_PENAL(12L),
    DROIT_PUBLIC(13L),
    DROIT_HUMANITAIRE(14L),
    DROIT_INTERNATIONAL(15L),
    DROIT_EUROPEEN(16L),
    DROIT_TECHNOLOGIES(17L),
    MEDIATION(18L),
    DROIT_MEDICAL(19L),
    DROIT_MEDIAS(20L),
    DROIT_SPORT(21L),
    SPECIFIER(22L);

    @Getter
    private final Long id;

    EnumMatiere(Long id) {
        this.id = id;
    }

    public static EnumMatiere fromId(Long id) {
        for (EnumMatiere enumMatiere : values()) {
            if (enumMatiere.getId().equals(id)) {
                return enumMatiere;
            }
        }
        return null;
    }

}
