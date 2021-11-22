package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.model.entity.converter.VcOwnerConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Table(name = "t_dossier_rights")
@Entity
@IdClass(TDossierRightsPk.class)
public class TDossierRights implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DOSSIER_ID", nullable = false)
    @Getter
    @Setter
    private long dossierId;

    @Id
    @Column(name = "VC_USER_ID", nullable = false)
    @Getter
    @Setter
    private long vcUserId;

    @Column(name = "RIGHTS", nullable = false)
    @Getter
    @Setter
    private String RIGHTS;

    /**
     * 0: not in the same vc , 1: owner of vc, 2 not owner but same vc
     */
    @Column(name = "VC_OWNER", nullable = false)
    @Convert(converter = VcOwnerConverter.class)
    @Getter
    @Setter
    private EnumVCOwner vcOwner = EnumVCOwner.NOT_SAME_VC;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "CRE_DATE")
    @CreationTimestamp
    @Getter
    @Setter
    private ZonedDateTime creDate;

    @Column(name = "UPD_DATE")
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;

    @Column(name = "LAST_ACCESS_DATE")
    @UpdateTimestamp
    @Getter
    @Setter
    private ZonedDateTime lastAccessDate = ZonedDateTime.now();

    //bi-directional many-to-one association to TDossiers
    @ManyToOne
    @JoinColumn(name = "DOSSIER_ID", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TDossiers tDossiers;
    //bi-directional many-to-one association to LawfirmUsers
    @ManyToOne
    @JoinColumn(name = "VC_USER_ID", insertable = false, nullable = false, updatable = false)
    @JsonIgnore
    @Getter
    @Setter
    private LawfirmUsers lawfirmUsers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDossierRights that = (TDossierRights) o;
        return dossierId == that.dossierId && vcUserId == that.vcUserId && Objects.equals(RIGHTS, that.RIGHTS) && vcOwner == that.vcOwner && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate) && Objects.equals(updDate, that.updDate) && Objects.equals(updUser, that.updUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dossierId, vcUserId, RIGHTS, vcOwner, creUser, creDate, updDate, updUser);
    }

    @Override
    public String toString() {
        return "TDossierRights{" +
                "dossierId=" + dossierId +
                ", vcUserId=" + vcUserId +
                ", RIGHTS='" + RIGHTS + '\'' +
                ", vcOwner=" + vcOwner +
                ", creUser='" + creUser + '\'' +
                ", creDate=" + creDate +
                ", updDate=" + updDate +
                ", updUser='" + updUser + '\'' +
                '}';
    }
}