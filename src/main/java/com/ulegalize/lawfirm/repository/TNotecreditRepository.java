package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TNotecredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TNotecreditRepository extends JpaRepository<TNotecredit, Long>, JpaSpecificationExecutor<TNotecredit> {

}