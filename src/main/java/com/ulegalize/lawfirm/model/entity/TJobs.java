package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "t_jobs")
@Entity
@Data
public class TJobs implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Long ID;

    @Column(name = "NAME", nullable = false)
    private String NAME;

    /**
     * 0= failed 1=success
     */
    @Column(name = "STATUS", nullable = false)
    private Integer STATUS;

    @Column(name = "DATE_START", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "DATE_END", nullable = false)
    private LocalDateTime dateEnd;

    @Column(name = "LOG")
    private String LOG;


}