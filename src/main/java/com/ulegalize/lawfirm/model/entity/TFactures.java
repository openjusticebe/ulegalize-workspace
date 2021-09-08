package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.lawfirm.model.entity.converter.FactureTypeConverter;
import com.ulegalize.lawfirm.model.enumeration.EnumFactureType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Table(name = "t_factures")
@Entity
public class TFactures implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facture", insertable = true, nullable = false)
    @Getter
    @Setter
    private Long idFacture;

    @Column(name = "year_facture", nullable = false)
    @Getter
    @Setter
    private Integer yearFacture;

    @Column(name = "num_facture", nullable = false)
    @Getter
    @Setter
    private Integer numFacture;

    @Column(name = "facture_ref", nullable = false)
    @Getter
    @Setter
    private String factureRef;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "id_tiers", nullable = false)
    @Getter
    @Setter
    private Long idTiers;

    @Column(name = "id_doss")
    @Getter
    @Setter
    private Long idDoss;

    @Column(name = "id_facture_type", nullable = false)
    @Convert(converter = FactureTypeConverter.class)
    @Getter
    @Setter
    private EnumFactureType idFactureType;

    @Column(name = "id_poste", nullable = false)
    @Getter
    @Setter
    private Integer idPoste;

    @Column(name = "is_valid", nullable = false)
    @Getter
    @Setter
    private Boolean valid = Boolean.FALSE;

    @Column(name = "montant", nullable = false)
    @Getter
    @Setter
    private BigDecimal montant = BigDecimal.ZERO;

    @Column(name = "ref", nullable = false)
    @Getter
    @Setter
    private String ref;

    @Column(name = "date_value", nullable = false)
    @Getter
    @Setter
    private ZonedDateTime dateValue;

    @Column(name = "date_echeance", nullable = false)
    @Getter
    @Setter
    private ZonedDateTime dateEcheance;

    @Column(name = "memo", nullable = false)
    @Getter
    @Setter
    private String memo = "";

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @Column(name = "id_echeance", nullable = false)
    @Getter
    @Setter
    private Integer idEcheance = 1;

    @Column(name = "num_fact_temp")
    @Getter
    @Setter
    private Integer numFactTemp;

    @Column(name = "second_tax")
    @Getter
    @Setter
    private BigDecimal secondTax;

    //bi-directional many-to-one association to TDossiers
    @ManyToOne
    @JoinColumn(name = "id_doss", insertable = false, nullable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter
    @Setter
    private TDossiers tDossiers;

    @ManyToOne
    @JoinColumn(name = "id_poste", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private RefPoste poste;

    @ManyToOne
    @JoinColumn(name = "id_tiers", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TClients tClients;

    @ManyToOne
    @JoinColumn(name = "id_echeance", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TFactureEcheance tFactureEcheance;

    @OneToMany(mappedBy = "tFactures")
    @Getter
    @Setter
    private Set<TFrais> tFraisList;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "tFactures")
    @Getter
    @Setter
    private List<TFactureDetails> tFactureDetailsList = new ArrayList<>();

    public void addTFactureDetails(TFactureDetails tFactureDetails) {
        this.tFactureDetailsList.add(tFactureDetails);
        tFactureDetails.setTFactures(this);
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "tFactures")
    @Getter
    @Setter
    private List<TFactureTimesheet> tFactureTimesheetList = new ArrayList<>();

    public void addTFactureTimesheet(TFactureTimesheet tFactureTimesheet) {
        this.tFactureTimesheetList.add(tFactureTimesheet);
        tFactureTimesheet.setTFactures(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFactures tFactures = (TFactures) o;
        return Objects.equals(idFacture, tFactures.idFacture) && Objects.equals(yearFacture, tFactures.yearFacture) && Objects.equals(numFacture, tFactures.numFacture) && Objects.equals(factureRef, tFactures.factureRef) && Objects.equals(vcKey, tFactures.vcKey) && Objects.equals(idTiers, tFactures.idTiers) && Objects.equals(idDoss, tFactures.idDoss) && idFactureType == tFactures.idFactureType && Objects.equals(idPoste, tFactures.idPoste) && Objects.equals(valid, tFactures.valid) && Objects.equals(montant, tFactures.montant) && Objects.equals(ref, tFactures.ref) && Objects.equals(dateValue, tFactures.dateValue) && Objects.equals(dateEcheance, tFactures.dateEcheance) && Objects.equals(memo, tFactures.memo) && Objects.equals(userUpd, tFactures.userUpd) && Objects.equals(dateUpd, tFactures.dateUpd) && Objects.equals(idEcheance, tFactures.idEcheance) && Objects.equals(numFactTemp, tFactures.numFactTemp) && Objects.equals(secondTax, tFactures.secondTax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFacture, yearFacture, numFacture, factureRef, vcKey, idTiers, idDoss, idFactureType, idPoste, valid, montant, ref, dateValue, dateEcheance, memo, userUpd, dateUpd, idEcheance, numFactTemp, secondTax);
    }

    @Override
    public String toString() {
        return "TFactures{" +
                "idFacture=" + idFacture +
                ", yearFacture=" + yearFacture +
                ", numFacture=" + numFacture +
                ", factureRef='" + factureRef + '\'' +
                ", vcKey='" + vcKey + '\'' +
                ", idTiers=" + idTiers +
                ", idDoss=" + idDoss +
                ", idFactureType=" + idFactureType +
                ", idPoste=" + idPoste +
                ", valid=" + valid +
                ", montant=" + montant +
                ", ref='" + ref + '\'' +
                ", dateValue=" + dateValue +
                ", dateEcheance=" + dateEcheance +
                ", memo='" + memo + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", idEcheance=" + idEcheance +
                ", numFactTemp=" + numFactTemp +
                ", secondTax=" + secondTax +
                '}';
    }
}