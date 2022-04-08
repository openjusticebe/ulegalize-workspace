package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "t_facture_timesheet")
@Entity
public class TFactureTimesheet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long ID;

    @Column(name = "TS_ID", nullable = false)
    @Getter
    @Setter
    private Long tsId;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "CRE_DATE")
    @CreationTimestamp
    @Getter
    @Setter
    private LocalDateTime creDate;

    @Column(name = "UPD_DATE")
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;

    @ManyToOne
    @JoinColumn(name = "TS_ID", insertable = false, nullable = false, updatable = false)
    @Getter
    @Setter
    private TTimesheet tTimesheet;
    @ManyToOne
    @JoinColumn(name = "FACTURE_ID", nullable = false, updatable = true)
    @Getter
    @Setter
    private TFactures tFactures;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFactureTimesheet that = (TFactureTimesheet) o;
        return Objects.equals(ID, that.ID) && Objects.equals(tsId, that.tsId) && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate) && Objects.equals(updDate, that.updDate) && Objects.equals(updUser, that.updUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, tsId, creUser, creDate, updDate, updUser);
    }

    @Override
    public String toString() {
        return "TFactureTimesheet{" +
                "ID=" + ID +
                ", tsId=" + tsId +
                ", creUser='" + creUser + '\'' +
                ", creDate=" + creDate +
                ", updDate=" + updDate +
                ", updUser='" + updUser + '\'' +
                '}';
    }
}