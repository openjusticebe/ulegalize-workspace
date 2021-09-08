package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_facture_type")
@Entity
public class TFactureType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_facture_type", insertable = false, nullable = false)
    private Integer idFactureType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "acronyme", nullable = false)
    private String acronyme;


}