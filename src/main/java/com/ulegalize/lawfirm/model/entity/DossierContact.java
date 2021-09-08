package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.enumeration.EnumDossierContactType;
import com.ulegalize.lawfirm.model.entity.converter.DossierContactTypeConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_dossier_contact")
public class DossierContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_type_id", nullable = false)
    @Convert(converter = DossierContactTypeConverter.class)
    @Getter
    @Setter
    private EnumDossierContactType contactTypeId;

    @Column(name = "cre_user", nullable = false)
    private String creUser;

    @Column(name = "cre_date", nullable = false)
    @CreationTimestamp
    private Date creDate;

    @Column(name = "upd_user")
    private String updUser;

    @Column(name = "upd_date")
    @UpdateTimestamp
    private Date updDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @Getter
    @Setter
    private TClients clients;

    @ManyToOne
    @JoinColumn(name = "dossier_id", nullable = false)
    @Getter
    @Setter
    private TDossiers dossiers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DossierContact that = (DossierContact) o;
        return Objects.equals(id, that.id) && (contactTypeId == that.contactTypeId) && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate) && Objects.equals(updUser, that.updUser) && Objects.equals(updDate, that.updDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactTypeId, creUser, creDate, updUser, updDate);
    }

    @Override
    public String toString() {
        return "DossierContact{" +
                "id=" + id + '\'' +
                "contactType=" + contactTypeId + '\'' +
                "creUser=" + creUser + '\'' +
                "creDate=" + creDate + '\'' +
                "updUser=" + updUser + '\'' +
                "updDate=" + updDate + '\'' +
                '}';
    }
}
