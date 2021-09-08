package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TSchemaVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TSchemaVersionRepository extends JpaRepository<TSchemaVersion, Long>, JpaSpecificationExecutor<TSchemaVersion> {

}