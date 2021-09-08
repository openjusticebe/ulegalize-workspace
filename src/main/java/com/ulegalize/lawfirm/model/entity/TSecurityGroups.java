package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.enumeration.EnumSecurityAppGroups;
import com.ulegalize.lawfirm.model.entity.converter.EnumSecurityAppGroupConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Unique description per VC
 */
@Table(name = "t_security_groups")
@Entity
public class TSecurityGroups implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    /**
     * when is set the group is an application predefined group and cannot be modified
     */
    @Column(name = "t_sec_app_group_id", nullable = false)
    @Convert(converter = EnumSecurityAppGroupConverter.class)
    @Getter
    @Setter
    private EnumSecurityAppGroups tSecAppGroupId;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tSecurityGroups")
    @JsonIgnore
    @Getter
    @Setter
    private List<TSecurityGroupRights> tSecurityGroupRightsList;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "tSecurityGroups")
    @JsonIgnore
    @Getter
    @Setter
    private List<TSecurityGroupUsers> tSecurityGroupUsersList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TSecurityGroups that = (TSecurityGroups) o;
        return Objects.equals(id, that.id) && Objects.equals(vcKey, that.vcKey) && Objects.equals(description, that.description) && tSecAppGroupId == that.tSecAppGroupId && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vcKey, description, tSecAppGroupId, userUpd, dateUpd);
    }

    @Override
    public String toString() {
        return "TSecurityGroups{" +
                "id=" + id +
                ", vcKey='" + vcKey + '\'' +
                ", description='" + description + '\'' +
                ", tSecAppGroupId=" + tSecAppGroupId +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                '}';
    }
}