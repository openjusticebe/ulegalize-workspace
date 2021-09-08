package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_security_group_users")
public class TSecurityGroupUsers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "id_user")
    @Getter
    @Setter
    private TUsers user;

    @ManyToOne
    @JoinColumn(name = "t_sec_groups_id")
    @Getter
    @Setter
    private TSecurityGroups tSecurityGroups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSecurityGroupUsers that = (TSecurityGroupUsers) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(tSecurityGroups, that.tSecurityGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, tSecurityGroups);
    }
}