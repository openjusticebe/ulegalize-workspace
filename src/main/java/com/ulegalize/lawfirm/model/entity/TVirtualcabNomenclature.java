package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_virtualcab_nomenclature")
public class TVirtualcabNomenclature implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vc_key", nullable = false)
    @Getter
    @Setter
    private LawfirmEntity lawfirmEntity;

    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column(name = "drive_path")
    @Getter
    @Setter
    private String drivePath;

    @Column(name = "cre_user")
    @Getter
    @Setter
    private String creUser;

    @Column(name = "cre_date")
    @CreationTimestamp
    @Getter
    @Setter
    private ZonedDateTime creDate;

    @OneToMany(mappedBy = "vcNomenclature", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<TNomenclatureConfig> nomenclatureConfigs;


    public void addTNomenclatureConfig(TNomenclatureConfig tNomenclatureConfig) {
        this.nomenclatureConfigs.add(tNomenclatureConfig);
        tNomenclatureConfig.setVcNomenclature(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TVirtualcabNomenclature that = (TVirtualcabNomenclature) o;
        return id.equals(that.id) && lawfirmEntity.equals(that.lawfirmEntity) && name.equals(that.name) && drivePath.equals(that.drivePath) && creUser.equals(that.creUser) && creDate.equals(that.creDate) && Objects.equals(nomenclatureConfigs, that.nomenclatureConfigs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lawfirmEntity, name, drivePath, creUser, creDate, nomenclatureConfigs);
    }
}
