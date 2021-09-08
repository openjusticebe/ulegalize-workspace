package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_first_time")
public class TFirstTime implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(columnDefinition = "VARCHAR", name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "ACTIVATED", nullable = false)
    private Integer ACTIVATED;

    @Column(name = "COUNT_CLICK", nullable = false)
    private Integer countClick = 0;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;

    @Column(name = "CRE_DATE")
    private LocalDateTime creDate;

    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    private String updUser;


}