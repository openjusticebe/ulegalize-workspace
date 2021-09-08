package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TCalendarParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalendarParticipantsRepository extends JpaRepository<TCalendarParticipants, Long> {


    @Query(value = "SELECT e" +
            " from TCalendarParticipants e" +
            " join e.tCalendarEvent cal " +
            " where cal.id = ?1")
    List<TCalendarParticipants> findByCalendarEventId(Long calendarEventId);
}
