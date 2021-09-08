package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TMesureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TMesureTypeRepository extends JpaRepository<TMesureType, Integer>, JpaSpecificationExecutor<TMesureType> {

}