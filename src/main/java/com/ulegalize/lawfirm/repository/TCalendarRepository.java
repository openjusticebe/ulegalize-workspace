package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCalendarRepository extends JpaRepository<TCalendar, Integer>, JpaSpecificationExecutor<TCalendar> {

}