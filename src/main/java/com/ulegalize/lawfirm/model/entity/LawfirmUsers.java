package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ulegalize.enumeration.EnumRole;
import com.ulegalize.lawfirm.model.entity.converter.EnumRoleConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "T_VIRTUALCAB_USERS")
public class LawfirmUsers {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "is_public")
    @Getter
    @Setter
    private boolean isPublic;

    @Column(name = "is_selected")
    @Getter
    @Setter
    private boolean isSelected;
    @Column(name = "is_active")
    @Getter
    @Setter
    private boolean isActive;
    @Column(name = "couthoraire")
    @Getter
    @Setter
    private Integer couthoraire;
    @Column(name = "use_self_couthoraire")
    @Getter
    @Setter
    private boolean useSelfCouthoraire;
    @Column(name = "is_prestataire")
    @Getter
    @Setter
    private boolean isPrestataire;

    @ManyToOne
    @JoinColumn(name = "VC_KEY")
    @JsonIgnore
    @Getter
    @Setter
    private LawfirmEntity lawfirm;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_user")
    @Getter
    @Setter
    private TUsers user;

    @Column(name = "VALID_FROM")
    @Getter
    @Setter
    private Date validFrom;

    @Column(name = "VALID_TO")
    @Getter
    @Setter
    private Date validTo;

    @Column(name = "id_role")
    @Convert(converter = EnumRoleConverter.class)
    @Getter
    @Setter
    private EnumRole idRole;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String user_upd;

    @Column(name = "date_upd")
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime date_upd;

    @Transient
    @JsonProperty("lawyer-alias")
    @Getter
    @Setter
    private String lawyerAlias;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawfirmUsers that = (LawfirmUsers) o;
        return isPublic == that.isPublic && isSelected == that.isSelected && isActive == that.isActive && useSelfCouthoraire == that.useSelfCouthoraire && isPrestataire == that.isPrestataire && Objects.equals(id, that.id) && Objects.equals(couthoraire, that.couthoraire) && Objects.equals(validFrom, that.validFrom) && Objects.equals(validTo, that.validTo) && idRole == that.idRole && Objects.equals(user_upd, that.user_upd) && Objects.equals(date_upd, that.date_upd) && Objects.equals(lawyerAlias, that.lawyerAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isPublic, isSelected, isActive, couthoraire, useSelfCouthoraire, isPrestataire, validFrom, validTo, idRole, user_upd, date_upd, lawyerAlias);
    }
}
