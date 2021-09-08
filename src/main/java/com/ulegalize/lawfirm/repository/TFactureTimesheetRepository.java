package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactureTimesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TFactureTimesheetRepository extends JpaRepository<TFactureTimesheet, Integer>, JpaSpecificationExecutor<TFactureTimesheet> {
    @Query("delete  from TFactureTimesheet t" +
            " where t.tFactures.idFacture = :factureId")
    void deleteAllByTFacturesIdFacture(Long factureId);
}