package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "t_calendar_vc_subscribers")
@Data
@Entity
public class TCalendarVcSubscribers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "VC_KEY", nullable = false)
    private String vcKey;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "CRE_DATE")
    private LocalDateTime creDate;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;


}