package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.FactureFraisAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactureFraisAdminRepository extends JpaRepository<FactureFraisAdmin, Long>, JpaSpecificationExecutor<FactureFraisAdmin> {

    @Query("select t from FactureFraisAdmin t" +
            " where t.deboursId = :deboursId" +
            " and t.ID <> ifnull( :id, 0)")
    List<FactureFraisAdmin> findByFraisIdAndNotId(Long id, Long deboursId);
}