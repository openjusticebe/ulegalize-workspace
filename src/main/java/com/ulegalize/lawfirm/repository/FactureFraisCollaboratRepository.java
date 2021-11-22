package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.FactureFraisCollaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactureFraisCollaboratRepository extends JpaRepository<FactureFraisCollaboration, Long>, JpaSpecificationExecutor<FactureFraisCollaboration> {


    @Query("select t from FactureFraisCollaboration t" +
            " where t.fraisId = :fraisId" +
            " and t.ID <> ifnull( :id, 0)")
    List<FactureFraisCollaboration> findByFraisIdAndNotId(Long id, Long fraisId);
}