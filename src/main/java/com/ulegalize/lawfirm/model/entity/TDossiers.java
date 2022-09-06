package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.lawfirm.model.entity.converter.EnumMatiereRubriqueConverter;
import com.ulegalize.lawfirm.model.enumeration.EnumMatiereRubrique;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_dossiers")
public class TDossiers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DOSS", nullable = false)
    @Getter
    @Setter
    private Long idDoss;

    @Getter
    @Setter
    private String vc_key;
    @Getter
    @Setter
    private String year_doss;
    @Column(columnDefinition = "INTEGER", name = "num_doss")
    @Getter
    @Setter
    private Long num_doss;
    @Getter
    @Setter
    private String doss_type;
    @Getter
    @Setter
    private Date date_open;
    @Getter
    @Setter
    private Date date_close;
    @Getter
    @Setter
    private String keywords;
    @Getter
    @Setter
    private String note;
    @Getter
    @Setter
    private String memo;
    @Getter
    @Setter
    private Integer success_fee_perc;
    @Getter
    @Setter
    private BigDecimal success_fee_montant;
    @Getter
    @Setter
    private Integer couthoraire;
    @Getter
    @Setter
    private Long id_user_resp;
    @Column(name = "is_digital", nullable = false)
    @Getter
    @Setter
    private Boolean isDigital = Boolean.FALSE;
    @Column(name = "client_quality", nullable = true)
    @Getter
    @Setter
    private String clientQuality;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime dateUpd;
    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @ManyToOne
    @JoinColumn(name = "opposing_counsel")
    @Getter
    @Setter
    private TClients opposingCounsel;

    @Column(name = "id_matiere_rubrique", nullable = false)
    @Convert(converter = EnumMatiereRubriqueConverter.class)
    @Getter
    @Setter
    private EnumMatiereRubrique enumMatiereRubrique;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dossier_id")
    @JsonIgnore
    @Getter
    @Setter
    private List<TDossierRights> dossierRightsList;

    @OneToMany(mappedBy = "tDossiers")
    @JsonIgnore
    @Getter
    @Setter
    private List<TFactures> facturesList;

    @OneToMany(mappedBy = "tDossiers")
    @JsonIgnore
    @Getter
    @Setter
    private List<TTimesheet> tTimesheetList;

    @OneToMany(mappedBy = "tDossiers")
    @JsonIgnore
    @Getter
    @Setter
    private List<TDebour> tDebourList;

    @OneToMany(mappedBy = "tDossiers")
    @JsonIgnore
    @Getter
    @Setter
    private List<TFrais> tFraisList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "dossiers")
    @JsonIgnore
    @Getter
    @Setter
    private List<DossierContact> dossierContactList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDossiers tDossiers = (TDossiers) o;
        return Objects.equals(idDoss, tDossiers.idDoss) && Objects.equals(vc_key, tDossiers.vc_key) && Objects.equals(year_doss, tDossiers.year_doss) && Objects.equals(num_doss, tDossiers.num_doss) && Objects.equals(doss_type, tDossiers.doss_type) && Objects.equals(date_open, tDossiers.date_open) && Objects.equals(date_close, tDossiers.date_close) && Objects.equals(keywords, tDossiers.keywords) && Objects.equals(note, tDossiers.note) && Objects.equals(memo, tDossiers.memo) && Objects.equals(success_fee_perc, tDossiers.success_fee_perc) && Objects.equals(success_fee_montant, tDossiers.success_fee_montant) && Objects.equals(couthoraire, tDossiers.couthoraire) && Objects.equals(id_user_resp, tDossiers.id_user_resp) && Objects.equals(isDigital, tDossiers.isDigital) && Objects.equals(opposingCounsel, tDossiers.opposingCounsel) && Objects.equals(clientQuality, tDossiers.clientQuality) && Objects.equals(dateUpd, tDossiers.dateUpd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDoss, vc_key, year_doss, num_doss, doss_type, date_open, date_close, keywords, note, memo, success_fee_perc, success_fee_montant, couthoraire, id_user_resp, isDigital, opposingCounsel, clientQuality, dateUpd);
    }

    @Override
    public String toString() {
        return "TDossiers{" +
                "idDoss=" + idDoss +
                ", vc_key='" + vc_key + '\'' +
                ", year_doss='" + year_doss + '\'' +
                ", num_doss=" + num_doss +
                ", doss_type='" + doss_type + '\'' +
                ", date_open=" + date_open +
                ", date_close=" + date_close +
                ", keywords='" + keywords + '\'' +
                ", note='" + note + '\'' +
                ", memo='" + memo + '\'' +
                ", success_fee_perc=" + success_fee_perc +
                ", success_fee_montant=" + success_fee_montant +
                ", couthoraire=" + couthoraire +
                ", id_user_resp=" + id_user_resp +
                ", isDigital=" + isDigital +
                ", opposingCounsel=" + opposingCounsel +
                ", clientQuality='" + clientQuality + '\'' +
                ", dateUpd=" + dateUpd +
                '}';
    }
}