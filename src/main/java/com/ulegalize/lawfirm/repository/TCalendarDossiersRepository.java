package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TCalendarDossiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCalendarDossiersRepository extends JpaRepository<TCalendarDossiers, Integer>, JpaSpecificationExecutor<TCalendarDossiers> {

}