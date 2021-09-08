package com.ulegalize.lawfirm.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "t_vc_groupment")
@Data
@Entity
public class TVcGroupment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "VC_KEY", nullable = false)
    private String vcKey;

    @Column(name = "GROUPMENT_ID", nullable = false)
    private Integer groupmentId;

    @Column(name = "CRE_USER", nullable = false)
    private String creUser;

    @Column(name = "CRE_DATE")
    @CreationTimestamp
    private LocalDateTime creDate;

    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    private String updUser;


}