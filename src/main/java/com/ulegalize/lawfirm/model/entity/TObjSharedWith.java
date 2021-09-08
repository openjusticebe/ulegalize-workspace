package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_obj_shared_with")
public class TObjSharedWith implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = true, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "obj_id", nullable = false)
    @Getter
    @Setter
    private Long objId;

    @Column(name = "from_userid", nullable = false)
    @Getter
    @Setter
    private Long fromUserid;

    @Column(name = "to_userid", nullable = false)
    @Getter
    @Setter
    private Long toUserid;

    @Column(name = "user_right", nullable = false)
    @Getter
    @Setter
    private Integer userRight;

    @Column(name = "msg", nullable = false)
    @Getter
    @Setter
    private String msg;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd = "";

    @Column(name = "date_upd", nullable = false)
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    /**
     * IP address of the client which triggered the action
     */
    @Column(name = "remote_addr")
    @Getter
    @Setter
    private String remoteAddr;


    @ManyToOne
    @JoinColumn(name = "obj_id", insertable = false, updatable = false)
    @Getter
    @Setter
    private TObjShared objShared;
    @ManyToOne
    @JoinColumn(name = "to_userid", insertable = false, updatable = false)
    @Getter
    @Setter
    private TUsers toUsers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TObjSharedWith that = (TObjSharedWith) o;
        return Objects.equals(id, that.id) && Objects.equals(objId, that.objId) && Objects.equals(fromUserid, that.fromUserid) && Objects.equals(toUserid, that.toUserid) && Objects.equals(userRight, that.userRight) && Objects.equals(msg, that.msg) && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd) && Objects.equals(remoteAddr, that.remoteAddr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, objId, fromUserid, toUserid, userRight, msg, userUpd, dateUpd, remoteAddr);
    }

    @Override
    public String toString() {
        return "TObjSharedWith{" +
                "id=" + id +
                ", objId=" + objId +
                ", fromUserid=" + fromUserid +
                ", toUserid=" + toUserid +
                ", userRight=" + userRight +
                ", msg='" + msg + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", remoteAddr='" + remoteAddr + '\'' +
                '}';
    }
}