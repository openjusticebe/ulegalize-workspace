package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_delegate")
@Entity
@Data
@IdClass(TDelegatePk.class)
public class TDelegate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Id
    @Column(name = "vc_key", nullable = false)
    private String vcKey = "";

    @Column(name = "order", nullable = false)
    private Integer order = 0;


}