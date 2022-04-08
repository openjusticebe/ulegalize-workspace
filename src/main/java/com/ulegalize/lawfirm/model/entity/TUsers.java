package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.lawfirm.model.entity.converter.EnumValidConverter;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "t_users")
public class TUsers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "id_user", nullable = false)
    @Getter
    @Setter
    private String idUser;

    @JsonIgnore
    @Column(name = "userpass", nullable = false)
    @Getter
    @Setter
    private String userpass;

    @JsonIgnore
    @Column(name = "hashkey", nullable = false)
    @Getter
    @Setter
    private String hashkey;

    @Column(name = "email", nullable = false)
    @Getter
    @Setter
    private String email;

    @Column(name = "fullname", nullable = false)
    @Getter
    @Setter
    private String fullname;

    @Column(name = "alias_public")
    @Getter
    @Setter
    private String aliasPublic;

    @Column(name = "initiales", nullable = false)
    @Getter
    @Setter
    private String initiales;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime dateUpd;

    @Column(name = "is_valid", nullable = false)
    @Getter
    @Setter
    private Boolean valid = Boolean.TRUE;

    @Column(name = "avatar", nullable = false)
    @Lob
    @Getter
    @Setter
    private byte[] avatar;

    @Column(name = "login_date")
    @Getter
    @Setter
    private LocalDateTime loginDate;

    @Column(name = "login_count", nullable = false)
    @Getter
    @Setter
    private Long loginCount;

    @Column(name = "id_valid", nullable = false)
    @Convert(converter = EnumValidConverter.class)
    @Getter
    @Setter
    private EnumValid idValid;

    @Column(name = "busystatus", nullable = false)
    @Getter
    @Setter
    private Integer busystatus = 0;

    @JsonIgnore
    @Column(name = "stripe_id")
    @Getter
    @Setter
    private String stripeId;

    @JsonIgnore
    @Column(name = "stripe_email")
    @Getter
    @Setter
    private String stripeEmail;

    @Column(name = "parrainage")
    @Getter
    @Setter
    private String parrainage;

    @Column(name = "mon_lien_parrainage")
    @Getter
    @Setter
    private String monLienParrainage;

    @Column(name = "language", nullable = false)
    @Getter
    @Setter
    private String language = "fr";

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;

    @Column(name = "color")
    @Getter
    @Setter
    private String color;

    @Column(name = "biography")
    @Getter
    @Setter
    private String biography;

    @Column(name = "specialities")
    @Getter
    @Setter
    private String specialities;

    @Column(name = "is_notification")
    @Getter
    @Setter
    private Boolean notification = Boolean.TRUE;

    @Column(name = "client_from")
    @Getter
    @Setter
    private String clientFrom;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LawfirmUsers> lawfirmUsers;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<TSecurityGroupUsers> securityGroupUsers;

    @OneToMany(mappedBy = "tUsers")
    private Set<TTimesheet> tTimesheetList;

    @OneToMany(mappedBy = "tUsers")
    private Set<TCalendarEvent> tCalendarEvents;

    @JsonIgnore
    public List<LawfirmUsers> getLawfirmUsers() {
        return lawfirmUsers;
    }

    public void setLawfirmUsers(List<LawfirmUsers> lawfirmUsers) {
        this.lawfirmUsers = lawfirmUsers;
    }

    @JsonIgnore
    public Set<TSecurityGroupUsers> getSecurityGroupUsers() {
        return securityGroupUsers;
    }

    public void setSecurityGroupUsers(Set<TSecurityGroupUsers> securityGroupUsers) {
        this.securityGroupUsers = securityGroupUsers;
    }

    @JsonIgnore
    public Set<TTimesheet> getTTimesheetList() {
        return tTimesheetList;
    }

    public void setTTimesheetList(Set<TTimesheet> tTimesheetList) {
        this.tTimesheetList = tTimesheetList;
    }

    @JsonIgnore
    public Set<TCalendarEvent> getTCalendarEvents() {
        return tCalendarEvents;
    }

    public void setTCalendarEvents(Set<TCalendarEvent> tCalendarEvents) {
        this.tCalendarEvents = tCalendarEvents;
    }

    @Override
    public String toString() {
        return "TUsers{" +
                "id=" + id +
                ", idUser='" + idUser + '\'' +
                ", userpass='" + userpass + '\'' +
                ", hashkey='" + hashkey + '\'' +
                ", email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                ", aliasPublic='" + aliasPublic + '\'' +
                ", initiales='" + initiales + '\'' +
                ", dateUpd=" + dateUpd +
                ", valid=" + valid +
                ", loginDate=" + loginDate +
                ", loginCount=" + loginCount +
                ", idValid=" + idValid +
                ", busystatus=" + busystatus +
                ", stripeId='" + stripeId + '\'' +
                ", stripeEmail='" + stripeEmail + '\'' +
                ", parrainage='" + parrainage + '\'' +
                ", monLienParrainage='" + monLienParrainage + '\'' +
                ", language='" + language + '\'' +
                ", updUser='" + updUser + '\'' +
                ", color='" + color + '\'' +
                ", biography='" + biography + '\'' +
                ", specialities='" + specialities + '\'' +
                ", notification='" + notification + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TUsers tUsers = (TUsers) o;
        return Objects.equals(id, tUsers.id) && Objects.equals(idUser, tUsers.idUser) && Objects.equals(userpass, tUsers.userpass) && Objects.equals(hashkey, tUsers.hashkey) && Objects.equals(email, tUsers.email) && Objects.equals(fullname, tUsers.fullname) && Objects.equals(aliasPublic, tUsers.aliasPublic) && Objects.equals(initiales, tUsers.initiales) && Objects.equals(dateUpd, tUsers.dateUpd) && Objects.equals(valid, tUsers.valid) && Arrays.equals(avatar, tUsers.avatar) && Objects.equals(loginDate, tUsers.loginDate) && Objects.equals(loginCount, tUsers.loginCount) && idValid == tUsers.idValid && Objects.equals(busystatus, tUsers.busystatus) && Objects.equals(stripeId, tUsers.stripeId) && Objects.equals(stripeEmail, tUsers.stripeEmail) && Objects.equals(parrainage, tUsers.parrainage) && Objects.equals(monLienParrainage, tUsers.monLienParrainage) && Objects.equals(language, tUsers.language) && Objects.equals(updUser, tUsers.updUser) && Objects.equals(color, tUsers.color) && Objects.equals(biography, tUsers.biography) && Objects.equals(specialities, tUsers.specialities) && Objects.equals(notification, tUsers.notification);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, idUser, userpass, hashkey, email, fullname, aliasPublic, initiales, dateUpd, valid, loginDate, loginCount, idValid, busystatus, stripeId, stripeEmail, parrainage, monLienParrainage, language, updUser, color, biography, specialities, notification);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}