package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_facture_statut")
@Entity
public class TFactureStatut implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "id_facture_statut", nullable = false)
    private Integer idFactureStatut;

    @Column(name = "description", nullable = false)
    private String description;


}