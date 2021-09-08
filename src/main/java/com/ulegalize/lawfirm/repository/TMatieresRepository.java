package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.model.entity.TMatieres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TMatieresRepository extends JpaRepository<TMatieres, Integer>, JpaSpecificationExecutor<TMatieres> {
    @Query("select new com.ulegalize.dto.ItemDto(m.idMatiere, concat(m.matiereDesc, " +
            " CASE WHEN mr.matiereRubriqueDesc = '' THEN '' ELSE ' - ' END ,  mr.matiereRubriqueDesc) )" +
            " from TMatieres m join m.matieresRubriquesList mr order by m.matiereDesc , mr.matiereRubriqueDesc")
    public List<ItemDto> findAllOrderByMatiereDesc();
}