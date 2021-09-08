package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table(name = "t_schema_version")
@Entity
public class TSchemaVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Long id;

    @Column(name = "patch_number", nullable = false)
    private String patchNumber;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date_applied", nullable = false)
    private LocalDateTime dateApplied;


}