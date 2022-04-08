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

@Table(name = "t_debour")
@Entity
public class TDebour implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER", insertable = false, name = "id_debour", nullable = false)
    @Getter
    @Setter
    private Long idDebour;

    @Column(name = "id_doss", nullable = false)
    @Getter
    @Setter
    private Long idDoss;

    @Column(columnDefinition = "SMALLINT", name = "id_debour_type", nullable = false)
    @Getter
    @Setter
    private Long idDebourType;

    @Column(columnDefinition = "SMALLINT", name = "unit", nullable = false)
    @Getter
    @Setter
    private Integer unit;

    @Column(columnDefinition = "DOUBLE", name = "price_per_unit", nullable = false)
    @Getter
    @Setter
    private BigDecimal pricePerUnit;

    @Column(columnDefinition = "TINYINT", name = "id_mesure_type", nullable = false)
    @Getter
    @Setter
    private Integer idMesureType;

    @Column(name = "comment")
    @Getter
    @Setter
    private String comment;

    @Column(name = "date_action", nullable = false)
    @Getter
    @Setter
    private ZonedDateTime dateAction;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @Column(name = "mapping_migration")
    @Getter
    @Setter
    private String mappingMigration;

    //bi-directional many-to-one association to TDossiers
    @ManyToOne
    @JoinColumn(name = "id_doss", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TDossiers tDossiers;
    //bi-directional many-to-one association to TDossiers
    @ManyToOne
    @JoinColumn(name = "id_debour_type", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TDebourType tDebourType;
    @ManyToOne
    @JoinColumn(name = "id_mesure_type", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TMesureType tMesureType;

    @OneToMany(mappedBy = "tDebour")
    @Getter
    @Setter
    private List<FactureFraisAdmin> factureFraisAdmins;

    @Override
    public String toString() {
        return "TDebour{" +
                "idDebour=" + idDebour +
                ", idDoss=" + idDoss +
                ", idDebourType=" + idDebourType +
                ", unit=" + unit +
                ", pricePerUnit=" + pricePerUnit +
                ", idMesureType=" + idMesureType +
                ", comment='" + comment + '\'' +
                ", dateAction=" + dateAction +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", mappingMigration='" + mappingMigration + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDebour tDebour = (TDebour) o;
        return Objects.equals(idDebour, tDebour.idDebour) && Objects.equals(idDoss, tDebour.idDoss) && Objects.equals(idDebourType, tDebour.idDebourType) && Objects.equals(unit, tDebour.unit) && Objects.equals(pricePerUnit, tDebour.pricePerUnit) && Objects.equals(idMesureType, tDebour.idMesureType) && Objects.equals(comment, tDebour.comment) && Objects.equals(dateAction, tDebour.dateAction) && Objects.equals(userUpd, tDebour.userUpd) && Objects.equals(dateUpd, tDebour.dateUpd) && Objects.equals(mappingMigration, tDebour.mappingMigration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDebour, idDoss, idDebourType, unit, pricePerUnit, idMesureType, comment, dateAction, userUpd, dateUpd, mappingMigration);
    }
}