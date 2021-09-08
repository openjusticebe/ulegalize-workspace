package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_message_user")
public class TMessageUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "user_id", nullable = false)
    private String userId;

    @Column(name = "IS_VALID", nullable = false)
    private String valid;

    @Column(name = "DATE_TO", nullable = false)
    private LocalDate dateTo;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;

    @Column(name = "CRE_DATE")
    private LocalDateTime creDate;


}