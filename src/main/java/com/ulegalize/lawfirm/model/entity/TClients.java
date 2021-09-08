package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.lawfirm.model.entity.converter.ClientTypeConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_CLIENTS")
public class TClients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id_client;

    @Getter
    @Setter
    private String vc_key;
    @Getter
    @Setter
    private Long user_id;

    @Getter
    @Setter
    private String id_lg;
    @Getter
    @Setter
    private String id_title = "";
    @Getter
    @Setter
    private String f_nom;
    @Getter
    @Setter
    private String f_prenom;
    @Getter
    @Setter
    private String f_rue = "";
    @Getter
    @Setter
    private String f_num = "";
    @Getter
    @Setter
    private String f_ville = "";
    @Getter
    @Setter
    private String f_cp = "";
    @Getter
    @Setter
    private String f_tel = "";
    @Getter
    @Setter
    private String f_gsm = "";
    @Getter
    @Setter
    private String f_fax = "";
    @Getter
    @Setter
    private String f_email = "";
    @Getter
    @Setter
    private String f_nn = "";
    @Getter
    @Setter
    private String f_noe = "";
    @Getter
    @Setter
    private Date birthdate;
    @Getter
    @Setter
    private String iban;
    @Getter
    @Setter
    private String bic;
    @Getter
    @Setter
    private String user_upd = "";

    @Column(name = "date_upd")
    @UpdateTimestamp
//    @CreationTimestamp
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @Getter
    @Setter
    private String f_tva = "";

    @Column(columnDefinition = "SMALLINT", name = "client_type")
    @Convert(converter = ClientTypeConverter.class)
    @Getter
    @Setter
    private EnumClientType client_type;
    @Getter
    @Setter
    private String f_company = "";
    @Getter
    @Setter
    private String id_country_alpha3 = "BEL";

    @OneToMany(mappedBy = "tClients")
    @Getter
    @Setter
    private List<TFactures> tFacturesList;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tClients", fetch = FetchType.EAGER)
    @JsonIgnore
    @Getter
    @Setter
    private List<VirtualcabClient> virtualcabClientList;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "clients")
    @JsonIgnore
    @Getter
    @Setter
    private List<DossierContact> dossierContactList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TClients tClients = (TClients) o;
        return Objects.equals(id_client, tClients.id_client) && Objects.equals(vc_key, tClients.vc_key) && Objects.equals(user_id, tClients.user_id) && Objects.equals(id_lg, tClients.id_lg) && Objects.equals(id_title, tClients.id_title) && Objects.equals(f_nom, tClients.f_nom) && Objects.equals(f_prenom, tClients.f_prenom) && Objects.equals(f_rue, tClients.f_rue) && Objects.equals(f_num, tClients.f_num) && Objects.equals(f_ville, tClients.f_ville) && Objects.equals(f_cp, tClients.f_cp) && Objects.equals(f_tel, tClients.f_tel) && Objects.equals(f_gsm, tClients.f_gsm) && Objects.equals(f_fax, tClients.f_fax) && Objects.equals(f_email, tClients.f_email) && Objects.equals(f_nn, tClients.f_nn) && Objects.equals(f_noe, tClients.f_noe) && Objects.equals(birthdate, tClients.birthdate) && Objects.equals(user_upd, tClients.user_upd) && Objects.equals(dateUpd, tClients.dateUpd) && Objects.equals(f_tva, tClients.f_tva) && client_type == tClients.client_type && Objects.equals(f_company, tClients.f_company) && Objects.equals(id_country_alpha3, tClients.id_country_alpha3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_client, vc_key, user_id, id_lg, id_title, f_nom, f_prenom, f_rue, f_num, f_ville, f_cp, f_tel, f_gsm, f_fax, f_email, f_nn, f_noe, birthdate, user_upd, dateUpd, f_tva, client_type, f_company, id_country_alpha3);
    }

    @Override
    public String toString() {
        return "TClients{" +
                "id_client=" + id_client +
                ", vc_key='" + vc_key + '\'' +
                ", user_id=" + user_id +
                ", id_lg='" + id_lg + '\'' +
                ", id_title='" + id_title + '\'' +
                ", f_nom='" + f_nom + '\'' +
                ", f_prenom='" + f_prenom + '\'' +
                ", f_rue='" + f_rue + '\'' +
                ", f_num='" + f_num + '\'' +
                ", f_ville='" + f_ville + '\'' +
                ", f_cp='" + f_cp + '\'' +
                ", f_tel='" + f_tel + '\'' +
                ", f_gsm='" + f_gsm + '\'' +
                ", f_fax='" + f_fax + '\'' +
                ", f_email='" + f_email + '\'' +
                ", f_nn='" + f_nn + '\'' +
                ", f_noe='" + f_noe + '\'' +
                ", birthdate=" + birthdate +
                ", user_upd='" + user_upd + '\'' +
                ", dateUpd=" + dateUpd +
                ", f_tva='" + f_tva + '\'' +
                ", client_type=" + client_type +
                ", f_company='" + f_company + '\'' +
                ", id_country_alpha3='" + id_country_alpha3 + '\'' +
                '}';
    }
}
