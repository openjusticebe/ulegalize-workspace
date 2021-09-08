package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ref_poste")
public class RefPoste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poste", nullable = false)
    @Getter
    @Setter
    private Integer idPoste;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "ref_poste", nullable = false)
    @Getter
    @Setter
    private String refPoste;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private Date dateUpd;

    @Column(name = "is_frais_procedure", nullable = false)
    @Getter
    @Setter
    private Boolean fraisProcedure;

    @Column(name = "is_honoraires", nullable = false)
    @Getter
    @Setter
    private Boolean honoraires;

    @Column(name = "is_frais_collaboration", nullable = false)
    @Getter
    @Setter
    private Boolean fraisCollaboration;

    @Column(name = "is_facturable", nullable = false)
    @Getter
    @Setter
    private Boolean facturable;

    @Column(name = "is_archived", nullable = false)
    @Getter
    @Setter
    private Boolean archived;

    @Column(name = "mapping_migration")
    @Getter
    @Setter
    private String mappingMigration;

    @Override
    public String toString() {
        return "RefPoste{" +
                "idPoste=" + idPoste +
                ", vcKey='" + vcKey + '\'' +
                ", refPoste='" + refPoste + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", fraisProcedure=" + fraisProcedure +
                ", honoraires=" + honoraires +
                ", fraisCollaboration=" + fraisCollaboration +
                ", facturable=" + facturable +
                ", archived=" + archived +
                ", mappingMigration='" + mappingMigration + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefPoste refPoste1 = (RefPoste) o;
        return Objects.equals(idPoste, refPoste1.idPoste) && Objects.equals(vcKey, refPoste1.vcKey) && Objects.equals(refPoste, refPoste1.refPoste) && Objects.equals(userUpd, refPoste1.userUpd) && Objects.equals(dateUpd, refPoste1.dateUpd) && Objects.equals(fraisProcedure, refPoste1.fraisProcedure) && Objects.equals(honoraires, refPoste1.honoraires) && Objects.equals(fraisCollaboration, refPoste1.fraisCollaboration) && Objects.equals(facturable, refPoste1.facturable) && Objects.equals(archived, refPoste1.archived) && Objects.equals(mappingMigration, refPoste1.mappingMigration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPoste, vcKey, refPoste, userUpd, dateUpd, fraisProcedure, honoraires, fraisCollaboration, facturable, archived, mappingMigration);
    }
}
