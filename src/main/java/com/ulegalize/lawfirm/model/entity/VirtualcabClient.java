package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_virtualcab_client")
public class VirtualcabClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @JoinColumn(name = "vc_key")
    @Getter
    @Setter
    private LawfirmEntity lawfirm;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "client_id")
    @Getter
    @Setter
    private TClients tClients;

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
    public String toString() {
        return "VirtualcabClient{" +
                "id=" + id + '\'' +
                "creUser=" + creUser + '\'' +
                "creDate=" + creDate + '\'' +
                "updUser=" + updUser + '\'' +
                "updDate=" + updDate + '\'' +
                '}';
    }
}
