package com.ulegalize.lawfirm.model.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "t_stripe_subscribers")
@Data
@Entity
public class TStripeSubscribers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "plan", nullable = false)
    private String plan;

    @Column(name = "activesub", nullable = false)
    private Integer activesub;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dateUpd;


}