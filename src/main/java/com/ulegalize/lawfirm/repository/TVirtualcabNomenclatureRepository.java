package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TVirtualcabNomenclatureRepository extends JpaRepository<TVirtualcabNomenclature, Long>, JpaSpecificationExecutor<TVirtualcabNomenclature> {

    Page<TVirtualcabNomenclature> findAllByLawfirmEntityOrderByName(LawfirmEntity lawfirmEntity, Pageable pageable);

    @Query(value = "select new com.ulegalize.dto.ItemLongDto(vn.id, vn.name)" +
            " from TVirtualcabNomenclature vn" +
            " where vn.lawfirmEntity = :lawfirmEntity" +
            " order by vn.name")
    List<ItemLongDto> findAllByLawfirmEntityOrderByName(LawfirmEntity lawfirmEntity);

    Optional<TVirtualcabNomenclature> findByIdAndLawfirmEntity(Long id, LawfirmEntity lawfirmEntity);

    Optional<TVirtualcabNomenclature> findByLawfirmEntityAndAndName(LawfirmEntity lawfirmEntity, String name);
}
