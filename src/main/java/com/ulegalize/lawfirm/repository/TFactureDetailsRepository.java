package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactureDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TFactureDetailsRepository extends JpaRepository<TFactureDetails, Integer>, JpaSpecificationExecutor<TFactureDetails> {

}