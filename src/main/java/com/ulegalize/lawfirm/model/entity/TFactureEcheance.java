package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_facture_echeance")
@Entity
@Data
public class TFactureEcheance implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "DESCRIPTION", nullable = false)
    private String DESCRIPTION;


}