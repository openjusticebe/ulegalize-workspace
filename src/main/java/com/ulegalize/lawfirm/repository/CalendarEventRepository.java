package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<TCalendarEvent, Long> {


    @Query(value = "SELECT e" +
            " from TCalendarEvent e" +
            " join e.tUsers user " +
            " where user.id = ?1")
    List<TCalendarEvent> findCalendarEventsByUserId(Long userId);

    @Query(value = "SELECT e" +
            " FROM TCalendarEvent e" +
            " join e.tUsers user " +
            " where e.start = ?1 AND e.end = ?2 " +
            "AND user.id = ?3 AND e.eventType = ?4")
    List<TCalendarEvent> getCalendarEventByStartEndUserIdType(Date start, Date end, Long userId, EnumCalendarEventType type);


	/*
	V2
	 */

    @Query(value = "SELECT DISTINCT e" +
            " from TCalendarEvent e " +
            " left join e.tUsers user " +
            " left join e.tCalendarParticipants participant" +
            " where ( user.id = :userId or e.vcKey = :vcKey or participant.userEmail = :userEmail)" +
            " and e.eventType in :eventTypesSelected" +
            " and e.start >= :start" +
            " and e.end <= :end ")
    List<TCalendarEvent> findCalendarEventsByUserIdAndDate(Long userId, String vcKey, Date start, Date end, List<EnumCalendarEventType> eventTypesSelected, String userEmail);

    @Query(value = "SELECT DISTINCT e" +
            " from TCalendarEvent e " +
            " join e.dossier dossier " +
            " left join e.tUsers user " +
            " left join e.tCalendarParticipants participant" +
            " where ( user.id = :userId or e.vcKey = :vcKey or participant.userEmail = :userEmail)" +
            " and dossier.idDoss = :dossierId" +
            " and e.eventType in :eventTypesSelected" +
            " and e.start >= :start" +
            " and e.end <= :end ")
    List<TCalendarEvent> findCalendarEventsByUserIdAndDateAndDossierId(Long userId, Long dossierId, String vcKey, Date start, Date end, List<EnumCalendarEventType> eventTypesSelected, String userEmail);

    @Query(
            value = "SELECT e" +
                    " from TCalendarEvent e" +
                    " join e.tUsers user " +
                    " where (user.id = ?1 or e.vcKey = ?2) " +
                    " and e.approved = false " +
                    " and e.eventType = ?3",
            countQuery = "SELECT count(e)" +
                    " from TCalendarEvent e" +
                    " join e.tUsers user " +
                    " where (user.id = ?1 or e.vcKey = ?2) " +
                    " and e.approved = false " +
                    " and e.eventType = ?3")
    Page<TCalendarEvent> findByUserAndVcKey(Long userIdToSearch, String vcKey, EnumCalendarEventType eventType, Pageable unpaged);
}
