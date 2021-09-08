package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_matiere_rubriques")
public class TMatiereRubriques implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matiere_rubrique", insertable = false, nullable = false)
    @Getter
    @Setter
    private Long idMatiereRubrique;

    @Column(name = "id_matiere", nullable = false)
    @Getter
    @Setter
    private Integer idMatiere;

    @Column(name = "matiere_rubrique_desc", nullable = false)
    @Getter
    @Setter
    private String matiereRubriqueDesc;

    @ManyToOne
    @JoinColumn(name = "id_matiere", insertable = false, updatable = false)
    @Getter
    @Setter
    private TMatieres matieres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMatiereRubriques that = (TMatiereRubriques) o;
        return Objects.equals(idMatiereRubrique, that.idMatiereRubrique) && Objects.equals(idMatiere, that.idMatiere) && Objects.equals(matiereRubriqueDesc, that.matiereRubriqueDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMatiereRubrique, idMatiere, matiereRubriqueDesc);
    }

    @Override
    public String toString() {
        return "TMatiereRubriques{" +
                "idMatiereRubrique=" + idMatiereRubrique +
                ", idMatiere=" + idMatiere +
                ", matiereRubriqueDesc='" + matiereRubriqueDesc + '\'' +
                '}';
    }
}