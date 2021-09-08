package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_dossiers_type")
public class TDossiersType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "doss_type", insertable = false, nullable = false)
    private String dossType = "";

    @Column(name = "type_desc", nullable = false)
    private String typeDesc = "";


}