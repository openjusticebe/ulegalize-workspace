package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_procedures_type")
@Data
public class TProceduresType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "proc_type", nullable = false)
    private Integer procType;

    @Column(name = "vc_key", nullable = false)
    private String vcKey;

    @Column(name = "type_desc", nullable = false)
    private String typeDesc = "";


}