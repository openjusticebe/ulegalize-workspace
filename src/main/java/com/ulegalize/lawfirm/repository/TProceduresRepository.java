package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TProcedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TProceduresRepository extends JpaRepository<TProcedures, Long>, JpaSpecificationExecutor<TProcedures> {

}