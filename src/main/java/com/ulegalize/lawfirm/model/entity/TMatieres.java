package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Table(name = "t_matieres")
@Entity
public class TMatieres implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_matiere", insertable = false, nullable = false)
    @Getter
    @Setter
    private Integer idMatiere;

    @Column(name = "matiere_desc", nullable = false)
    @Getter
    @Setter
    private String matiereDesc;

    @OneToMany(mappedBy = "matieres")
    @Getter
    @Setter
    private List<TMatiereRubriques> matieresRubriquesList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMatieres tMatieres = (TMatieres) o;
        return Objects.equals(idMatiere, tMatieres.idMatiere) && Objects.equals(matiereDesc, tMatieres.matiereDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMatiere, matiereDesc);
    }
}