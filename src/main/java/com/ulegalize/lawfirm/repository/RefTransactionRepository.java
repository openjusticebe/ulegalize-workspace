package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.RefTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RefTransactionRepository extends JpaRepository<RefTransaction, Integer>, JpaSpecificationExecutor<RefTransaction> {

}