package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.lawfirm.model.entity.converter.EnumStatusAssociationConverter;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_workspace_associated")
public class TWorkspaceAssociated implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_sender", nullable = false)
    @Getter
    @Setter
    private LawfirmEntity lawfirmSender;

    @ManyToOne
    @JoinColumn(name = "id_recipient", nullable = false)
    @Getter
    @Setter
    private LawfirmEntity lawfirmRecipient;

    @Column(name = "status")
    @Convert(converter = EnumStatusAssociationConverter.class)
    @Getter
    @Setter
    private EnumStatusAssociation status = EnumStatusAssociation.PENDING;

    @Column(name = "message")
    @Getter
    @Setter
    private String message;

    @Column(name = "creation_date")
    @Getter
    @Setter
    private ZonedDateTime creationDate;

    @Column(name = "hashkey")
    @Getter
    @Setter
    private String hashkey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TWorkspaceAssociated that = (TWorkspaceAssociated) o;
        return Objects.equals(id, that.id) && Objects.equals(lawfirmSender, that.lawfirmSender) && Objects.equals(lawfirmRecipient, that.lawfirmRecipient) && status == that.status && Objects.equals(message, that.message) && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lawfirmSender, lawfirmRecipient, status, message, creationDate);
    }
}
