package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "t_ref_currency")
@Entity
public class TRefCurrency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "code", nullable = false)
    private String code;

    @Column(name = "symbol", nullable = false)
    private String symbol;


}