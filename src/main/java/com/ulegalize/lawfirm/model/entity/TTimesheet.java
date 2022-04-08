package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_timesheet")
public class TTimesheet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_ts", nullable = false)
    @Getter
    @Setter
    private Long idTs;

    @Column(name = "id_doss", nullable = false)
    @Getter
    @Setter
    private Long idDoss = 0L;

    @Column(name = "id_gest", nullable = false)
    @Getter
    @Setter
    private Long idGest;

    @Column(columnDefinition = "SMALLINT", name = "ts_type", nullable = false)
    @Getter
    @Setter
    private Integer tsType = 0;

    @Column(name = "couthoraire")
    @Getter
    @Setter
    private Integer couthoraire = 0;

    @Column(name = "date_action", nullable = false)
    @Getter
    @Setter
    private ZonedDateTime dateAction;

    @Column(columnDefinition = "CHAR(2)", name = "dh", nullable = false)
    @Getter
    @Setter
    private BigDecimal dh = BigDecimal.ZERO;

    @Column(columnDefinition = "CHAR(2)", name = "dm", nullable = false)
    @Getter
    @Setter
    private BigDecimal dm = BigDecimal.ZERO;

    @Column(name = "comment")
    @Getter
    @Setter
    private String comment;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd = "";

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @Column(name = "mapping_migration")
    @Getter
    @Setter
    private String mappingMigration;

    @Column(name = "vat", nullable = false)
    @Getter
    @Setter
    private BigDecimal vat;

    @Column(columnDefinition = "INTEGER", name = "is_forfait", nullable = false)
    @Getter
    @Setter
    private Boolean forfait = Boolean.FALSE;

    @Column(name = "forfait_ht")
    @Getter
    @Setter
    private BigDecimal forfaitHt;

    //bi-directional many-to-one association to TDossiers
    @ManyToOne
    @JoinColumn(name = "id_doss", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TDossiers tDossiers;
    //bi-directional many-to-one association to TDossiers
    @ManyToOne
    @JoinColumn(name = "ts_type", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TTimesheetType tTimesheetType;
    @ManyToOne
    @JoinColumn(name = "id_gest", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TUsers tUsers;

    @OneToMany(mappedBy = "tTimesheet")
    @Getter
    @Setter
    private List<TFactureTimesheet> tFactureTimesheetList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTimesheet that = (TTimesheet) o;
        return Objects.equals(idTs, that.idTs) && Objects.equals(idDoss, that.idDoss) && Objects.equals(idGest, that.idGest) && Objects.equals(tsType, that.tsType) && Objects.equals(couthoraire, that.couthoraire) && Objects.equals(dateAction, that.dateAction) && Objects.equals(dh, that.dh) && Objects.equals(dm, that.dm) && Objects.equals(comment, that.comment) && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd) && Objects.equals(mappingMigration, that.mappingMigration) && Objects.equals(vat, that.vat) && Objects.equals(forfait, that.forfait) && Objects.equals(forfaitHt, that.forfaitHt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTs, idDoss, idGest, tsType, couthoraire, dateAction, dh, dm, comment, userUpd, dateUpd, mappingMigration, vat, forfait, forfaitHt);
    }
}