package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_dossiers_vc_tags")
public class TDossiersVcTags implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dossier", nullable = false)
    @Getter
    @Setter
    private TDossiers tDossiers;

    @ManyToOne
    @JoinColumn(name = "id_vc_tags", nullable = false)
    @Getter
    @Setter
    private TVirtualCabTags tVirtualCabTags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDossiersVcTags that = (TDossiersVcTags) o;
        return Objects.equals(id, that.id) && Objects.equals(tDossiers, that.tDossiers) && Objects.equals(tVirtualCabTags, that.tVirtualCabTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tDossiers, tVirtualCabTags);
    }

    @Override
    public String toString() {
        return "TDossiersVcTags{" +
                "id=" + id +
                ", tDossiers=" + tDossiers +
                ", tVirtualCabTags=" + tVirtualCabTags +
                '}';
    }
}
