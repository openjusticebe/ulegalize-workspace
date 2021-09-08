package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TCalendarEventRepository extends JpaRepository<TCalendarEvent, Long>, JpaSpecificationExecutor<TCalendarEvent> {
    @Query("select e from TCalendarEvent e where e.tUsers.id = :userId and e.start = :start and e.end = :end and e.eventType = :type")
    Optional<TCalendarEvent> findByUserIdAndStartAndEndAndEventType(Long userId, Date start, Date end, EnumCalendarEventType type);

    @Query("select e from TCalendarEvent e where e.tUsers.id = :userId and e.start < :start and e.eventType = :type")
    List<TCalendarEvent> findByUserIdAndStartAndEndAndEventType(Long userId, Date start, EnumCalendarEventType type);
}