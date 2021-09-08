package com.ulegalize.lawfirm.model.entity;

import com.ulegalize.enumeration.EnumContextTemplate;
import com.ulegalize.enumeration.EnumTypeTemplate;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_templates")
@Data
public class TTemplates implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Long id;

    @Column(name = "vc_key", nullable = false)
    private String vcKey;

    /**
     * S=system U=user
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EnumTypeTemplate type = EnumTypeTemplate.U;

    /**
     * D=Doc M=Mail
     */
    @Column(name = "format", nullable = false)
    private String format = "D";

    @Enumerated(EnumType.STRING)
    @Column(name = "context", nullable = false)
    private EnumContextTemplate context = EnumContextTemplate.DOSSIER;

    /**
     * (mail title)
     */
    @Column(name = "subcontext", nullable = false)
    private String subcontext = "";

    /**
     * (mail libelle)
     */
    @Column(name = "name", nullable = false)
    private String name = "";

    /**
     * (body/message)
     */
    @Column(name = "template", nullable = false)
    @Lob
    private byte[] template;

    /**
     * sujet
     */
    @Column(name = "title", nullable = false)
    private String title = "";

    @Column(name = "description", nullable = false)
    private String description = " ";

    @Column(name = "is_archived", nullable = false)
    private Boolean archived = Boolean.FALSE;

    @Column(name = "user_upd", nullable = false)
    private String userUpd;

    @Column(name = "date_upd", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dateUpd;


}