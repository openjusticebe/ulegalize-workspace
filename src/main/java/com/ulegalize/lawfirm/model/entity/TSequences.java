package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.lawfirm.model.entity.converter.SequenceTypeConverter;
import com.ulegalize.lawfirm.model.enumeration.EnumSequenceType;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_sequences")
@Data
public class TSequences implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @JsonIgnore
    @Column(name = "sequence_type")
    @Convert(converter = SequenceTypeConverter.class)
    private EnumSequenceType sequenceType;

    @Column(name = "upd_date")
    @UpdateTimestamp
    private LocalDateTime updDate;

    @Column(name = "upd_user")
    private String updUser;

}