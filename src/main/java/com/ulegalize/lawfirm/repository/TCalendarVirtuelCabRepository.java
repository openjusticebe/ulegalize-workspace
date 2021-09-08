package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TCalendarVirtuelCab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCalendarVirtuelCabRepository extends JpaRepository<TCalendarVirtuelCab, Integer>, JpaSpecificationExecutor<TCalendarVirtuelCab> {

}