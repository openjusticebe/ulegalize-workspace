package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "t_procedures")
public class TProcedures implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proc", insertable = false, nullable = false)
    private Long idProc;

    @Column(name = "id_doss", nullable = false)
    private Long idDoss = 0L;

    @Column(name = "proc_type", nullable = false)
    private Integer procType = 0;

    @Column(name = "date_open", nullable = false)
    private LocalDate dateOpen;

    @Column(name = "date_close", nullable = false)
    private LocalDate dateClose;

    @Column(name = "user_upd", nullable = false)
    private String userUpd = "";

    @Column(name = "date_upd", nullable = false)
    private LocalDate dateUpd;


}