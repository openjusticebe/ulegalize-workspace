package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_calendar_doss_subscribers")
public class TCalendarDossSubscribers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "DOSSIER_ID")
    private Integer dossierId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "CRE_DATE")
    private LocalDateTime creDate;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;


}