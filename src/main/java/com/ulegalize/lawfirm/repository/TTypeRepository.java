package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TTypeRepository extends JpaRepository<TType, Integer>, JpaSpecificationExecutor<TType> {

}