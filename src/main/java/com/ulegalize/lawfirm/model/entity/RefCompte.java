package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.enumeration.EnumAccountType;
import com.ulegalize.lawfirm.model.entity.converter.EnumAccountTypeConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ref_compte")
public class RefCompte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte", nullable = false)
    @Getter
    @Setter
    private Integer idCompte;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "compte_num", nullable = false)
    @Getter
    @Setter
    private String compteNum;

    @Column(name = "compte_ref", nullable = false)
    @Getter
    @Setter
    private String compteRef;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private Date dateUpd;

    @Column(name = "is_countable", nullable = false)
    @Getter
    @Setter
    private Boolean countable;

    @Column(name = "is_archived", nullable = false)
    @Getter
    @Setter
    private Boolean archived;

    @Column(name = "account_type_id", nullable = false)
    @Convert(converter = EnumAccountTypeConverter.class)
    @Getter
    @Setter
    private EnumAccountType accountTypeId;

    @Override
    public String toString() {
        return "RefCompte{" +
                "idCompte=" + idCompte +
                ", vcKey='" + vcKey + '\'' +
                ", compteNum='" + compteNum + '\'' +
                ", compteRef='" + compteRef + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", countable=" + countable +
                ", archived=" + archived +
                ", accountTypeId=" + accountTypeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefCompte refCompte = (RefCompte) o;
        return Objects.equals(idCompte, refCompte.idCompte) && Objects.equals(vcKey, refCompte.vcKey) && Objects.equals(compteNum, refCompte.compteNum) && Objects.equals(compteRef, refCompte.compteRef) && Objects.equals(userUpd, refCompte.userUpd) && Objects.equals(dateUpd, refCompte.dateUpd) && Objects.equals(countable, refCompte.countable) && Objects.equals(archived, refCompte.archived) && accountTypeId == refCompte.accountTypeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompte, vcKey, compteNum, compteRef, userUpd, dateUpd, countable, archived, accountTypeId);
    }
}
