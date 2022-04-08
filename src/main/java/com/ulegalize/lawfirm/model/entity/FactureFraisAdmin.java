package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_facture_frais_admin")
public class FactureFraisAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "BIGINT", name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long ID;

    @Column(columnDefinition = "INTEGER", name = "DEBOURS_ID", nullable = false)
    @Getter
    @Setter
    private Long deboursId;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "CRE_DATE")
    @Getter
    @Setter
    private Date creDate;

    @Column(name = "UPD_DATE")
    @Getter
    @Setter
    private Date updDate;

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;

    @ManyToOne
    @JoinColumn(name = "DEBOURS_ID", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TDebour tDebour;
    @ManyToOne
    @JoinColumn(name = "FACTURE_ID", nullable = false, updatable = true)
    @Getter
    @Setter
    private TFactures tFactures;

    @Override
    public String toString() {
        return "FactureFraisAdmin{" +
                "ID=" + ID + '\'' +
                "deboursId=" + deboursId + '\'' +
                "creUser=" + creUser + '\'' +
                "creDate=" + creDate + '\'' +
                "updDate=" + updDate + '\'' +
                "updUser=" + updUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactureFraisAdmin that = (FactureFraisAdmin) o;
        return Objects.equals(ID, that.ID) && Objects.equals(deboursId, that.deboursId) && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate) && Objects.equals(updDate, that.updDate) && Objects.equals(updUser, that.updUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, deboursId, creUser, creDate, updDate, updUser);
    }
}
