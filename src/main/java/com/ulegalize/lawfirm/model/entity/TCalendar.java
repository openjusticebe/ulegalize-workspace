package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_calendar")
public class TCalendar implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALENDAR", insertable = false, nullable = false)
    @Getter
    @Setter
    private Integer idCalendar;

    @Column(name = "EVENT_DESCRIPTION", nullable = false)
    @Getter
    @Setter
    private String eventDescription;

    @Column(name = "VALID_FROM", nullable = false)
    @Getter
    @Setter
    private LocalDateTime validFrom;

    @Column(name = "VALID_TO", nullable = false)
    @Getter
    @Setter
    private LocalDateTime validTo;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "CRE_DATE")
    @Getter
    @Setter
    private LocalDateTime creDate;

    @Column(name = "UPD_DATE")
    @Getter
    @Setter
    private LocalDateTime updDate;

    @Column(name = "UPD_USER")
    @Getter
    @Setter
    private String updUser;

    @Column(name = "IS_SENT", nullable = false)
    @Getter
    @Setter
    private Integer sent = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCalendar tCalendar = (TCalendar) o;
        return Objects.equals(idCalendar, tCalendar.idCalendar) && Objects.equals(eventDescription, tCalendar.eventDescription) && Objects.equals(validFrom, tCalendar.validFrom) && Objects.equals(validTo, tCalendar.validTo) && Objects.equals(creUser, tCalendar.creUser) && Objects.equals(creDate, tCalendar.creDate) && Objects.equals(updDate, tCalendar.updDate) && Objects.equals(updUser, tCalendar.updUser) && Objects.equals(sent, tCalendar.sent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCalendar, eventDescription, validFrom, validTo, creUser, creDate, updDate, updUser, sent);
    }
}