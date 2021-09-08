package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_facture_details")
public class TFactureDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    @Column(name = "htva", nullable = false)
    @Getter
    @Setter
    private BigDecimal htva;

    @Column(name = "tva", nullable = false)
    @Getter
    @Setter
    private BigDecimal tva;

    @Column(name = "ttc", nullable = false)
    @Getter
    @Setter
    private BigDecimal ttc;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @ManyToOne
    @JoinColumn(name = "id_facture", nullable = false, updatable = false)
    @Getter
    @Setter
    private TFactures tFactures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFactureDetails that = (TFactureDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(htva, that.htva) && Objects.equals(tva, that.tva) && Objects.equals(ttc, that.ttc) && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, htva, tva, ttc, userUpd, dateUpd);
    }

    @Override
    public String toString() {
        return "TFactureDetails{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", htva=" + htva +
                ", tva=" + tva +
                ", ttc=" + ttc +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                '}';
    }
}