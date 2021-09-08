package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_notecredit")
@Data
public class TNotecredit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notecredit", insertable = false, nullable = false)
    private Long idNotecredit;

    @Column(name = "id_facture", nullable = false)
    private Long idFacture;

    @Column(name = "year_nc", nullable = false)
    private Integer yearNc;

    @Column(name = "num_nc", nullable = false)
    private Integer numNc;

    @Column(name = "is_valid", nullable = false)
    private Boolean valid = Boolean.FALSE;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant = BigDecimal.ZERO;

    @Column(name = "ref", nullable = false)
    private String ref;

    @Column(name = "date_value", nullable = false)
    private LocalDate dateValue;

    @Column(name = "memo", nullable = false)
    private String memo = "";

    @Column(name = "user_upd", nullable = false)
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    private LocalDateTime dateUpd;


}