package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_obj_shared")
public class TObjShared implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "obj", nullable = false)
    @Getter
    @Setter
    private String obj;

    @Column(name = "vc_key")
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @Column(name = "size")
    @Getter
    @Setter
    private Long size;

    @OneToMany(mappedBy = "objShared")
    @Getter
    @Setter
    private List<TObjSharedWith> objSharedWithList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TObjShared that = (TObjShared) o;
        return Objects.equals(id, that.id) && Objects.equals(obj, that.obj) && Objects.equals(vcKey, that.vcKey) && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, obj, vcKey, userUpd, dateUpd, size);
    }

    @Override
    public String toString() {
        return "TObjShared{" +
                "id=" + id +
                ", obj='" + obj + '\'' +
                ", vcKey='" + vcKey + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", size=" + size +
                '}';
    }
}