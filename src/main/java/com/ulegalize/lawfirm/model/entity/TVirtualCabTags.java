package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "t_virtualcab_tags")
public class TVirtualCabTags implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vckey", nullable = false)
    @Getter
    @Setter
    private LawfirmEntity lawfirmEntity;

    @OneToMany(mappedBy = "tVirtualCabTags", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<TDossiersVcTags> tDossiersVcTags;

    @Override
    public String toString() {
        return "TVirtualCabTags{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", lawfirmEntity=" + lawfirmEntity +
                ", tDossiersVcTags=" + tDossiersVcTags +
                '}';
    }
}
