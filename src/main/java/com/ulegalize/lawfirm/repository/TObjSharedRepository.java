package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TObjShared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TObjSharedRepository extends JpaRepository<TObjShared, Long>, JpaSpecificationExecutor<TObjShared> {
    Optional<TObjShared> findByVcKeyAndObj(String vcKey, String obj);

    Optional<TObjShared> findByVcKeyAndObjAndSize(String vcKey, String obj, Long size);
}