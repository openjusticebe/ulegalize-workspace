package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

public class TDossierRightsPk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private long dossierId;

    @Getter
    @Setter
    private long vcUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDossierRightsPk that = (TDossierRightsPk) o;
        return dossierId == that.dossierId && vcUserId == that.vcUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dossierId, vcUserId);
    }

}