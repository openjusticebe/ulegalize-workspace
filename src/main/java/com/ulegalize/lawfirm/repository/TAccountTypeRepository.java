package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TAccountTypeRepository extends JpaRepository<TAccountType, Integer>, JpaSpecificationExecutor<TAccountType> {

}