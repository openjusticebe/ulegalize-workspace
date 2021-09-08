package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_roles")
@Entity
public class TRoles implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "id_role", nullable = false)
    private Integer idRole = 0;

    @Column(name = "role_desc", nullable = false)
    private String roleDesc;


}