package com.ulegalize.lawfirm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_security_app_group_rights")
public class TSecurityAppGroupRights implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Long id;

    @Column(name = "t_sec_app_groups_id", nullable = false)
    private Integer tSecAppGroupsId;

    @Column(name = "id_right", nullable = false)
    private Long idRight;


}