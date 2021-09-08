package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TFactureTypeRepository extends JpaRepository<TFactureType, Integer>, JpaSpecificationExecutor<TFactureType> {

}