package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TSecurityGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TSecurityGroupsRepository extends JpaRepository<TSecurityGroups, Long>, JpaSpecificationExecutor<TSecurityGroups> {
    List<TSecurityGroups> findAllByVcKey(String vcKey);

    List<TSecurityGroups> findAllByVcKeyAndDescription(String vKey, String securityGroupName);


}