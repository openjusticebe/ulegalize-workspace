package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.lawfirm.model.entity.converter.RefTransactionTypeConverter;
import com.ulegalize.lawfirm.model.entity.converter.TTypeConverter;
import com.ulegalize.lawfirm.model.enumeration.EnumRefTransaction;
import com.ulegalize.lawfirm.model.enumeration.EnumTType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_frais")
public class TFrais implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_frais", nullable = false)
    @Getter
    @Setter
    private Long idFrais;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "id_client")
    @Getter
    @Setter
    private Long idClient;

    @Column(name = "id_compte", nullable = false)
    @Getter
    @Setter
    private Integer idCompte;

    @Column(name = "id_doss")
    @Getter
    @Setter
    private Long idDoss;

    @Column(name = "id_facture")
    @Getter
    @Setter
    private Long idFacture;

    @Column(name = "id_type", nullable = false)
    @Convert(converter = TTypeConverter.class)
    @Getter
    @Setter
    private EnumTType idType;

    @Column(name = "id_poste", nullable = false)
    @Getter
    @Setter
    private Integer idPoste = 0;

    @Column(name = "id_transaction", nullable = false)
    @Convert(converter = RefTransactionTypeConverter.class)
    @Getter
    @Setter
    private EnumRefTransaction idTransaction;

    @Column(name = "montant", nullable = false)
    @Getter
    @Setter
    private BigDecimal montant = BigDecimal.ZERO;

    @Column(name = "ref", nullable = false)
    @Getter
    @Setter
    private String ref = "";

    @Column(name = "ratio", nullable = false)
    @Getter
    @Setter
    private BigDecimal ratio = new BigDecimal("100.00");

    @Column(name = "grid_id")
    @Getter
    @Setter
    private Integer gridId;

    @Column(name = "date_value", nullable = false)
    @Getter
    @Setter
    private LocalDate dateValue;

    @Column(name = "memo", nullable = false)
    @Getter
    @Setter
    private String memo = "";

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd = "";

    @Column(name = "date_upd", nullable = false)
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    /**
     * deprecated
     */
    @Column(name = "tva")
    @Getter
    @Setter
    private Integer tva = 0;

    @Column(name = "montantht", nullable = false)
    @Getter
    @Setter
    private BigDecimal montantht = BigDecimal.ZERO;

    @Column(name = "mapping_migration")
    @Getter
    @Setter
    private String mappingMigration;

    @ManyToOne
    @JoinColumn(name = "id_poste", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private RefPoste refPoste;
    @ManyToOne
    @JoinColumn(name = "id_compte", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private RefCompte refCompte;

    @ManyToOne
    @JoinColumn(name = "id_client", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter
    @Setter
    private TClients tClients;

    @ManyToOne
    @JoinColumn(name = "id_doss", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter
    @Setter
    private TDossiers tDossiers;

    @ManyToOne
    @JoinColumn(name = "id_facture", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter
    @Setter
    private TFactures tFactures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFrais tFrais = (TFrais) o;
        return Objects.equals(idFrais, tFrais.idFrais) && Objects.equals(vcKey, tFrais.vcKey) && Objects.equals(idClient, tFrais.idClient) && Objects.equals(idCompte, tFrais.idCompte) && Objects.equals(idDoss, tFrais.idDoss) && Objects.equals(idFacture, tFrais.idFacture) && idType == tFrais.idType && Objects.equals(idPoste, tFrais.idPoste) && idTransaction == tFrais.idTransaction && Objects.equals(montant, tFrais.montant) && Objects.equals(ref, tFrais.ref) && Objects.equals(ratio, tFrais.ratio) && Objects.equals(gridId, tFrais.gridId) && Objects.equals(dateValue, tFrais.dateValue) && Objects.equals(memo, tFrais.memo) && Objects.equals(userUpd, tFrais.userUpd) && Objects.equals(dateUpd, tFrais.dateUpd) && Objects.equals(tva, tFrais.tva) && Objects.equals(montantht, tFrais.montantht) && Objects.equals(mappingMigration, tFrais.mappingMigration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFrais, vcKey, idClient, idCompte, idDoss, idFacture, idType, idPoste, idTransaction, montant, ref, ratio, gridId, dateValue, memo, userUpd, dateUpd, tva, montantht, mappingMigration);
    }

    @Override
    public String toString() {
        return "TFrais{" +
                "idFrais=" + idFrais +
                ", vcKey='" + vcKey + '\'' +
                ", idClient=" + idClient +
                ", idCompte=" + idCompte +
                ", idDoss=" + idDoss +
                ", idFacture=" + idFacture +
                ", idType=" + idType +
                ", idPoste=" + idPoste +
                ", idTransaction=" + idTransaction +
                ", montant=" + montant +
                ", ref='" + ref + '\'' +
                ", ratio=" + ratio +
                ", gridId=" + gridId +
                ", dateValue=" + dateValue +
                ", memo='" + memo + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", tva=" + tva +
                ", montantht=" + montantht +
                ", mappingMigration='" + mappingMigration + '\'' +
                '}';
    }
}