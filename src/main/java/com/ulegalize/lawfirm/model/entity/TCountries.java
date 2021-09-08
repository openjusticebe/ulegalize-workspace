package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Table(name = "t_countries")
@Entity
public class TCountries implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    @Getter
    @Setter
    private Integer id;

    @Column(name = "code", nullable = false)
    @Getter
    @Setter
    private Integer code;

    @Column(name = "alpha2", nullable = false)
    @Getter
    @Setter
    private String alpha2;

    @Column(name = "alpha3", nullable = false)
    @Getter
    @Setter
    private String alpha3;

    @Column(name = "nom_en_gb", nullable = false)
    @Getter
    @Setter
    private String nomEnGb;

    @Column(name = "nom_fr_fr", nullable = false)
    @Getter
    @Setter
    private String nomFrFr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCountries that = (TCountries) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(alpha2, that.alpha2) && Objects.equals(alpha3, that.alpha3) && Objects.equals(nomEnGb, that.nomEnGb) && Objects.equals(nomFrFr, that.nomFrFr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, alpha2, alpha3, nomEnGb, nomFrFr);
    }
}