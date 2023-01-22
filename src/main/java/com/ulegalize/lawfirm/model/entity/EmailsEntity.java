package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_emails")
public class EmailsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "vc_key", nullable = false, insertable = false, updatable = false)
    @Getter
    @Setter
    private String vcKey;


    @Column(name = "welcome_sent", nullable = false)
    @Getter
    @Setter
    private boolean welcomeSent;

    @Column(name = "support_sent", nullable = false)
    @Getter
    @Setter
    private boolean supportSent;

    @Column(name = "reminder_sent", nullable = false)
    @Getter
    @Setter
    private boolean reminderSent;
    @Column(name = "reminder_date", nullable = false)
    @Getter
    @Setter
    private ZonedDateTime reminderDate;

    @Column(name = "cre_user", nullable = false)
    @Getter
    @Setter
    private String creUser;

    @Column(name = "cre_date", nullable = false)
    @Getter
    @Setter
    @CreationTimestamp
    private Date creDate;

    @Column(name = "upd_user")
    @Getter
    @Setter
    private String updUser;

    @Column(name = "upd_date")
    @Getter
    @Setter
    @UpdateTimestamp
    private Date updDate;

    @ManyToOne
    @JoinColumn(name = "VC_KEY", nullable = false)
    @Getter
    @Setter
    private LawfirmEntity lawfirm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailsEntity that = (EmailsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(vcKey, that.vcKey) && Objects.equals(welcomeSent, that.welcomeSent) && Objects.equals(supportSent, that.supportSent) && Objects.equals(reminderSent, that.reminderSent) && Objects.equals(creUser, that.creUser) && Objects.equals(creDate, that.creDate) && Objects.equals(updUser, that.updUser) && Objects.equals(updDate, that.updDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vcKey, welcomeSent, supportSent, reminderSent, creUser, creDate, updUser, updDate);
    }

    @Override
    public String toString() {
        return "EmailsEntity{" +
                "id=" + id + '\'' +
                "vcKey=" + vcKey + '\'' +
                "welcomeSent=" + welcomeSent + '\'' +
                "supportSent=" + supportSent + '\'' +
                "reminderSent=" + reminderSent + '\'' +
                "creUser=" + creUser + '\'' +
                "creDate=" + creDate + '\'' +
                "updUser=" + updUser + '\'' +
                "updDate=" + updDate + '\'' +
                '}';
    }
}
