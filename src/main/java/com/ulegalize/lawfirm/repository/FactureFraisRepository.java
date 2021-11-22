package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.FactureFraisDebours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactureFraisRepository extends JpaRepository<FactureFraisDebours, Long>, JpaSpecificationExecutor<FactureFraisDebours> {


    @Query("select t from FactureFraisDebours t" +
            " where t.fraisId = :fraisId" +
            " and t.ID <> ifnull( :id, 0)")
    List<FactureFraisDebours> findByFraisIdAndNotId(Long id, Long fraisId);
}