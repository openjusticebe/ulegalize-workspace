package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "t_mesure_type")
public class TMesureType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "id_mesure_type", nullable = false)
    @Getter
    @Setter
    private Integer idMesureType;

    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    @OneToMany(mappedBy = "tMesureType")
    @Getter
    @Setter
    private Set<TDebour> tDebourList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMesureType that = (TMesureType) o;
        return Objects.equals(idMesureType, that.idMesureType) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMesureType, description);
    }

    @Override
    public String toString() {
        return "TMesureType{" +
                "idMesureType=" + idMesureType +
                ", description='" + description + '\'' +
                '}';
    }
}