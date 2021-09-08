package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_clients_type")
@Data
public class TClientsType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "id_ct", nullable = false)
    private String idCt = "";

    @Column(name = "ref_ct", nullable = false)
    private String refCt = "";


}