package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TRolesRepository extends JpaRepository<TRoles, Integer>, JpaSpecificationExecutor<TRoles> {

}