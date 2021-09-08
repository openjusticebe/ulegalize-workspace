package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TCalendarVcSubscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TCalendarVcSubscribersRepository extends JpaRepository<TCalendarVcSubscribers, Integer>, JpaSpecificationExecutor<TCalendarVcSubscribers> {

}