package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TSecurityAppGroupRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TSecurityAppGroupRightsRepository extends JpaRepository<TSecurityAppGroupRights, Long>, JpaSpecificationExecutor<TSecurityAppGroupRights> {

}