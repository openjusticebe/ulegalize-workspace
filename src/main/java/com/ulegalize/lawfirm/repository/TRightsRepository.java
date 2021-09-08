package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TRightsRepository extends JpaRepository<TRights, Integer>, JpaSpecificationExecutor<TRights> {

}