package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ref_transaction")
public class RefTransaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "id_transaction", nullable = false)
    @Getter
    @Setter
    private Integer idTransaction = 0;
    @Column(name = "ref_transaction", nullable = false)
    @Getter
    @Setter
    private String refTransaction = "";

    @Override
    public String toString() {
        return "RefTransaction{" +
                "idTransaction=" + idTransaction +
                ", refTransaction='" + refTransaction + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefTransaction that = (RefTransaction) o;
        return Objects.equals(idTransaction, that.idTransaction) && Objects.equals(refTransaction, that.refTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaction, refTransaction);
    }


}