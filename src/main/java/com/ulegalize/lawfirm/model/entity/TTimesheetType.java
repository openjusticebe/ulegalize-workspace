package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_timesheet_type")
public class TTimesheetType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SMALLINT", insertable = false, name = "id_ts", nullable = false)
    @Getter
    @Setter
    private Integer idTs;

    @Column(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "description")
    @Getter
    @Setter
    private String description;

    @Column(name = "user_upd", nullable = false)
    @Getter
    @Setter
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @Getter
    @Setter
    private Date dateUpd;

    @Column(name = "archived", nullable = false)
    @Getter
    @Setter
    private Boolean archived = false;

    @Column(name = "mapping_migration")
    @Getter
    @Setter
    private String mappingMigration;

    @OneToMany(mappedBy = "tTimesheetType")
    @Getter
    @Setter
    private List<TTimesheet> tTimesheetList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TTimesheetType that = (TTimesheetType) o;
        return Objects.equals(idTs, that.idTs) && Objects.equals(vcKey, that.vcKey) && Objects.equals(description, that.description) && Objects.equals(userUpd, that.userUpd) && Objects.equals(dateUpd, that.dateUpd) && Objects.equals(archived, that.archived) && Objects.equals(mappingMigration, that.mappingMigration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTs, vcKey, description, userUpd, dateUpd, archived, mappingMigration);
    }

    @Override
    public String toString() {
        return "TTimesheetType{" +
                "idTs=" + idTs +
                ", vcKey='" + vcKey + '\'' +
                ", description='" + description + '\'' +
                ", userUpd='" + userUpd + '\'' +
                ", dateUpd=" + dateUpd +
                ", archived=" + archived +
                ", mappingMigration='" + mappingMigration + '\'' +
                '}';
    }
}