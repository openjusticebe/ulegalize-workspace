package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "t_langues")
public class TLangues implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_lg", insertable = false, nullable = false)
    private String idLg;

    @Column(name = "lg_desc", nullable = false)
    private String lgDesc;


}