package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_rights")
@Data
public class TRights implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_right", insertable = false, nullable = false)
    private Integer idRight = 0;

    @Column(name = "right_desc", nullable = false)
    private String rightDesc;


}