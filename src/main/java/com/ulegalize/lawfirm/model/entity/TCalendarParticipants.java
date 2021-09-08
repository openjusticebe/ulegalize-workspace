package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_calendar_participants")
public class TCalendarParticipants implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", insertable = false, nullable = false)
    private Integer ID;

    @Column(name = "USER_EMAIL", nullable = false)
    @Getter
    @Setter
    private String userEmail;

    @Column(name = "CRE_DATE")
    @CreationTimestamp
    @Getter
    @Setter
    private LocalDateTime creDate;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @ManyToOne
    @JoinColumn(name = "CALENDAR_ID", nullable = false)
    @Getter
    @Setter
    private TCalendarEvent tCalendarEvent;

    @Override
    public String toString() {
        return "TCalendarUsers{" +
                "ID=" + ID +
                ", creDate=" + creDate +
                ", creUser='" + creUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCalendarParticipants that = (TCalendarParticipants) o;
        return Objects.equals(ID, that.ID) && Objects.equals(creDate, that.creDate) && Objects.equals(creUser, that.creUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, creDate, creUser);
    }
}