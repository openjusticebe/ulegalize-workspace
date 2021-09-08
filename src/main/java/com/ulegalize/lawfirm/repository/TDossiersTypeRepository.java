package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TDossiersType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TDossiersTypeRepository extends JpaRepository<TDossiersType, String>, JpaSpecificationExecutor<TDossiersType> {

}