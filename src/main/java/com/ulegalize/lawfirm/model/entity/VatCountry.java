package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_vat_country")
public class VatCountry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "vat", nullable = false)
    @Getter
    @Setter
    private BigDecimal vat;

    @Column(name = "id_country_alpha2", nullable = false)
    @Getter
    @Setter
    private String idCountryAlpha2;


    @Column(name = "is_default", nullable = false)
    @Getter
    @Setter
    private Boolean isDefault = Boolean.FALSE;

    @Column(name = "upd_user")
    @Getter
    @Setter
    private String updUser;

    @Column(name = "upd_date")
    @Getter
    @Setter
    private Date updDate;


    @Override
    public String toString() {
        return "VatCountry{" +
                "id=" + id + '\'' +
                "vat=" + vat + '\'' +
                "idCountryAlpha2=" + idCountryAlpha2 + '\'' +
                "updUser=" + updUser + '\'' +
                "updDate=" + updDate + '\'' +
                '}';
    }
}
