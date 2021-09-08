package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TProceduresType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TProceduresTypeRepository extends JpaRepository<TProceduresType, Integer>, JpaSpecificationExecutor<TProceduresType> {

}