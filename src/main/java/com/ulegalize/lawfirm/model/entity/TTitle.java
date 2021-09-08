package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_title")
@Entity
@Data
public class TTitle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_title", insertable = false, nullable = false)
    private String idTitle = "";

    @Column(name = "title", nullable = false)
    private String title = "";


}