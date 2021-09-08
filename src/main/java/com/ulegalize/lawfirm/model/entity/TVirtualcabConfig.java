package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_virtualcab_config")
@Data
@IdClass(TVirtualcabConfigPk.class)
public class TVirtualcabConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "vc_key", insertable = false, nullable = false)
    private String vcKey;

    @Id
    @Column(name = "parameter", insertable = false, nullable = false)
    private String parameter;

    @Id
    @Column(name = "description", insertable = false, nullable = false)
    private String description;


}