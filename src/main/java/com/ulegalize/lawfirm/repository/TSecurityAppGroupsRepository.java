package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TSecurityAppGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TSecurityAppGroupsRepository extends JpaRepository<TSecurityAppGroups, Integer>, JpaSpecificationExecutor<TSecurityAppGroups> {

}