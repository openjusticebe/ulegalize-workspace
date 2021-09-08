package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "t_virtualcab_vat")
@Entity
public class TVirtualcabVat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long ID;

    @Column(name = "VC_KEY", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "VAT")
    @Getter
    @Setter
    private BigDecimal VAT;

    @Column(name = "SECOND_TAX")
    @Getter
    @Setter
    private BigDecimal secondTax;

    @Column(name = "SECOND_TAX_LABEL")
    @Getter
    @Setter
    private String secondTaxLabel;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "CRE_DATE")
    @Getter
    @Setter
    private LocalDateTime creDate;

    @Column(name = "UPD_DATE")
    @Getter
    @Setter
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;

    @Column(name = "is_default", nullable = false)
    @Getter
    @Setter
    private Boolean isDefault = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "VC_KEY", insertable = false, updatable = false)
    @JsonIgnore
    @Getter
    @Setter
    private LawfirmEntity lawfirm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TVirtualcabVat that = (TVirtualcabVat) o;
        return Objects.equals(ID, that.ID) && Objects.equals(vcKey, that.vcKey) && Objects.equals(VAT, that.VAT) && Objects.equals(secondTax, that.secondTax) && Objects.equals(secondTaxLabel, that.secondTaxLabel) && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate) && Objects.equals(updDate, that.updDate) && Objects.equals(updUser, that.updUser) && Objects.equals(isDefault, that.isDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, vcKey, VAT, secondTax, secondTaxLabel, creUser, creDate, updDate, updUser, isDefault);
    }

    @Override
    public String toString() {
        return "TVirtualcabVat{" +
                "ID=" + ID +
                ", vcKey='" + vcKey + '\'' +
                ", VAT=" + VAT +
                ", secondTax=" + secondTax +
                ", secondTaxLabel='" + secondTaxLabel + '\'' +
                ", creUser='" + creUser + '\'' +
                ", creDate=" + creDate +
                ", updDate=" + updDate +
                ", updUser='" + updUser + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}