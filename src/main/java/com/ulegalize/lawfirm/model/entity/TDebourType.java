package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "t_debour_type")
public class TDebourType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_debour_type", nullable = false)
    @Getter
    @Setter
    private Long idDebourType;

    @Column(name = "id_mesure_type", nullable = false)
    @Getter
    @Setter
    private Integer idMesureType;

    @Column(name = "price_per_unit", nullable = false)
    @Getter
    @Setter
    private BigDecimal pricePerUnit;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private Date dateUpd;

    @Column(name = "archived", nullable = false)
    @Getter
    @Setter
    private Boolean archived = false;

    @Column(name = "mapping_migration")
    @Getter
    @Setter
    private String mappingMigration;

    @OneToMany(mappedBy = "tDebourType")
    @Getter
    @Setter
    private Set<TDebour> tDebourList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDebourType that = (TDebourType) o;
        return Objects.equals(idDebourType, that.idDebourType) && Objects.equals(idMesureType, that.idMesureType) && Objects.equals(pricePerUnit, that.pricePerUnit) && Objects.equals(vcKey, that.vcKey) && Objects.equals(description, that.description) && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd) && Objects.equals(archived, that.archived) && Objects.equals(mappingMigration, that.mappingMigration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDebourType, idMesureType, pricePerUnit, vcKey, description, userUpd, dateUpd, archived, mappingMigration);
    }

    @Override
    public String toString() {
        return "TDebourType{" +
                "idDebourType=" + idDebourType +
                ", idMesureType=" + idMesureType +
                ", pricePerUnit=" + pricePerUnit +
                ", vcKey='" + vcKey + '\'' +
                ", description='" + description + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", archived=" + archived +
                ", mappingMigration='" + mappingMigration + '\'' +
                '}';
    }
}