package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="T_VIRTUALCAB_WEBSITE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LawfirmWebsiteEntity {

    @Id
    @Column(name = "VC_KEY")
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "TITLE")
    @Getter
    @Setter
    private String title;

    @Column(name = "INTRO")
    @Getter
    @Setter
    private String intro;

    @Column(name = "PHILOSOPHY")
    @Getter
    @Setter
    private String philosophy;

    @Column(name = "ABOUT")
    @Getter
    @Setter
    private String about;

    @Column(name = "ACTIVE")
    @Getter
    @Setter
    private boolean active;

    @Column(name = "ACCEPT_APPOINTMENT")
    @Getter
    @Setter
    private boolean acceptAppointment;

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;
    @Column(name = "UPD_DATE")
    @Getter
    @Setter
    @UpdateTimestamp
    private Date updDate;
    @OneToOne
    @JoinColumn(name = "VC_KEY", referencedColumnName = "`key`")
    @Getter
    @Setter
    @JsonIgnore
    private LawfirmEntity lawfirmEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawfirmWebsiteEntity that = (LawfirmWebsiteEntity) o;
        return active == that.active && acceptAppointment == that.acceptAppointment && Objects.equals(vcKey, that.vcKey) && Objects.equals(title, that.title) && Objects.equals(intro, that.intro) && Objects.equals(philosophy, that.philosophy) && Objects.equals(about, that.about) && Objects.equals(updUser, that.updUser) && Objects.equals(updDate, that.updDate);
    }

    @Override
    public String toString() {
        return "LawfirmWebsiteEntity{" +
                "vcKey='" + vcKey + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", philosophy='" + philosophy + '\'' +
                ", about='" + about + '\'' +
                ", active=" + active +
                ", acceptAppointment=" + acceptAppointment +
                ", updUser='" + updUser + '\'' +
                ", updDate=" + updDate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(vcKey, title, intro, philosophy, about, active, acceptAppointment, updUser, updDate);
    }
}
