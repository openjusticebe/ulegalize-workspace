package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_grid")
@Data
public class TGrid implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "DESCRIPTION", nullable = false)
    private String DESCRIPTION;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;

    @Column(name = "CRE_DATE")
    private LocalDateTime creDate;

    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    private String updUser;


}