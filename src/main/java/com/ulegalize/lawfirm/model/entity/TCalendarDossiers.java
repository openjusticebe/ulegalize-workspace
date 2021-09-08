package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_calendar_dossiers")
@Data
public class TCalendarDossiers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "CALENDAR_ID", nullable = false)
    private Integer calendarId;

    @Column(name = "DOSSIER_ID")
    private Integer dossierId;

    @Column(name = "CRE_DATE")
    private LocalDateTime creDate;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;


}