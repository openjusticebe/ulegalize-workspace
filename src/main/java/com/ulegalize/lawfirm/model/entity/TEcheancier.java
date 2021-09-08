package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "t_echeancier")
@Data
public class TEcheancier implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_echeance", nullable = false)
    private Long idEcheance;

    @Column(name = "vc_key", nullable = false)
    private String vcKey;

    @Column(name = "id_doss", nullable = false)
    private Long idDoss = 0L;

    @Column(name = "date_rappel", nullable = false)
    private LocalDate dateRappel;

    @Column(name = "hour_rappel", nullable = false)
    private LocalTime hourRappel;

    @Column(name = "comment", nullable = false)
    private String comment = "";

    @Column(name = "user_upd", nullable = false)
    private String userUpd = "";

    @Column(name = "date_upd", nullable = false)
    private LocalDateTime dateUpd;

    @Column(name = "id_assignee", nullable = false)
    private Long idAssignee;

    @Column(name = "id_owner", nullable = false)
    private Long idOwner = 0L;

    /**
     * 0 = Public 1 = Private
     */
    @Column(name = "mode", nullable = false)
    private Integer mode = 0;


}