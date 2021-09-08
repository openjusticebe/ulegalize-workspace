package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TSecurityGroupRights;
import com.ulegalize.security.EnumRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TSecurityGroupRightsRepository extends JpaRepository<TSecurityGroupRights, Long>, JpaSpecificationExecutor<TSecurityGroupRights> {
    @Query("select t from TSecurityGroupRights t where t.tSecurityGroups.id = :securityGroupId")
    List<TSecurityGroupRights> findByTSecurityGroups_Id(Long securityGroupId);

    @Query("select t from TSecurityGroupRights t " +
            " where t.idRight = :right and t.tSecurityGroups.id = :securityGroupId")
    List<TSecurityGroupRights> findByIdRightAndTSecurityGroups_Id(EnumRights right, Long securityGroupId);

    @Query(value = "delete from TSecurityGroupRights l where l.id = ?1")
    @Modifying
    void deleteById(Long id);
}