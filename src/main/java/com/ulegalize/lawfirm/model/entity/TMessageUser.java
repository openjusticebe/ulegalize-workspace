package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "t_message_user")
public class TMessageUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "user_id", nullable = false)
    @Getter
    @Setter
    private Long userId;

    @Column(name = "IS_VALID", nullable = false)
    @Getter
    @Setter
    private Boolean valid;

    @Column(name = "DATE_TO", nullable = false)
    @Getter
    @Setter
    private LocalDateTime dateTo;

    @Column(name = "CRE_USER", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "CRE_DATE")
    @Getter
    @Setter
    private LocalDateTime creDate;

    @ManyToOne
    @JoinColumn(name = "message_id")
    @Getter
    @Setter
    private TMessage tMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMessageUser that = (TMessageUser) o;
        return Objects.equals(userId, that.userId) && Objects.equals(valid, that.valid) && Objects.equals(dateTo, that.dateTo) && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, valid, dateTo, creUser, creDate);
    }

    @Override
    public String toString() {
        return "TMessageUser{" +
                "userId='" + userId + '\'' +
                ", valid='" + valid + '\'' +
                ", dateTo=" + dateTo +
                ", creUser='" + creUser + '\'' +
                ", creDate=" + creDate +
                '}';
    }
}