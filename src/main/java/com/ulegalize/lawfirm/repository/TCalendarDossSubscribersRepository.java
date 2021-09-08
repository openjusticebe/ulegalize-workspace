package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TCalendarDossSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCalendarDossSubscribersRepository extends JpaRepository<TCalendarDossSubscribers, Integer>, JpaSpecificationExecutor<TCalendarDossSubscribers> {

}