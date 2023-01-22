package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_nomenclature_config")
public class TNomenclatureConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vc_nomenclature", nullable = false)
    @Getter
    @Setter
    private TVirtualcabNomenclature vcNomenclature;

    @Column(name = "label")
    @Getter
    @Setter
    private String label;

    @Column(name = "parameter")
    @Getter
    @Setter
    private String parameter;

    @Column(name = "cre_user")
    @Getter
    @Setter
    private String creUser;

    @Column(name = "cre_date")
    @CreationTimestamp
    @Getter
    @Setter
    private ZonedDateTime creDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TNomenclatureConfig that = (TNomenclatureConfig) o;
        return id.equals(that.id) && vcNomenclature.equals(that.vcNomenclature) && label.equals(that.label) && parameter.equals(that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vcNomenclature, label, parameter);
    }
}
