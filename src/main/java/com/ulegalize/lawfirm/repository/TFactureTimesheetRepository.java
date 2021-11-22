package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactureTimesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TFactureTimesheetRepository extends JpaRepository<TFactureTimesheet, Integer>, JpaSpecificationExecutor<TFactureTimesheet> {
    @Query("select t from TFactureTimesheet t" +
            " where t.tsId = :tsId" +
            " and t.ID <> ifnull(:id, 0)")
    List<TFactureTimesheet> findByTsIdAndNotId(Integer id, Long tsId);
}