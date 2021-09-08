package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.security.EnumRights;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_security_group_rights")
public class TSecurityGroupRights implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "id_right", nullable = false)
    @Getter
    @Setter
    private EnumRights idRight;

    @ManyToOne
    @JoinColumn(name = "t_sec_groups_id")
    @Getter
    @Setter
    private TSecurityGroups tSecurityGroups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSecurityGroupRights that = (TSecurityGroupRights) o;
        return Objects.equals(id, that.id) && idRight == that.idRight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idRight);
    }

    @Override
    public String toString() {
        return "TSecurityGroupRights{" +
                "id=" + id +
                ", idRight=" + idRight +
                '}';
    }
}