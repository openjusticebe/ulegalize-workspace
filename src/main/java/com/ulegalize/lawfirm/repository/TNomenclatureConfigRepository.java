package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TNomenclatureConfig;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TNomenclatureConfigRepository extends JpaRepository<TNomenclatureConfig, Long>, JpaSpecificationExecutor<TNomenclatureConfig> {

    List<TNomenclatureConfig> findAllByVcNomenclature(TVirtualcabNomenclature virtualcabNomenclature);

    Optional<TNomenclatureConfig> findByVcNomenclatureAndLabel(TVirtualcabNomenclature virtualcabNomenclature, String label);
}
