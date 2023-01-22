package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_auth0")
public class Auth0Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "access_token", nullable = false, length = 1000)
    @Getter
    @Setter
    private String accessToken;


    @Column(name = "expire_in", nullable = false)
    @Getter
    @Setter
    private Integer expireIn;

    @Column(name = "cre_date", nullable = false)
    @Getter
    @Setter
    @UpdateTimestamp
    // GMT
    private ZonedDateTime creDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auth0Entity that = (Auth0Entity) o;
        return Objects.equals(id, that.id) && Objects.equals(accessToken, that.accessToken) && Objects.equals(expireIn, that.expireIn) && Objects.equals(creDate, that.creDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken, expireIn, creDate);
    }

    @Override
    public String toString() {
        return "Auth0Entity{" +
                "id=" + id +
                ", accessToken='" + accessToken + '\'' +
                ", expireIn=" + expireIn +
                ", creDate=" + creDate +
                '}';
    }
}
