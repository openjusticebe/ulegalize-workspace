package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TCalendarParticipants;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class CalendarEventRepositoryTests extends EntityTest {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Test
    public void test_A_findCalendarEventsByUserIdAndDate() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());
        TCalendarParticipants tCalendarParticipants = tCalendarEvent.getTCalendarParticipants().get(0);

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDate(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey(),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                tCalendarParticipants.getUserEmail());

        assertNotNull(calEvents);

        assertEquals(1, calEvents.size());
        assertEquals(tCalendarEvent.getId(), calEvents.get(0).getId());
        assertEquals(tCalendarEvent.getEventType(), calEvents.get(0).getEventType());
    }

    @Test
    public void test_B_findCalendarEventsByUserIdAndDateAndDossierId() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDateAndDossierId(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                tCalendarEvent.getDossier().getIdDoss(),
                lawfirmEntity.getVckey(),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                "");

        assertNotNull(calEvents);

        assertEquals(1, calEvents.size());
        assertEquals(tCalendarEvent.getId(), calEvents.get(0).getId());
        assertEquals(tCalendarEvent.getEventType(), calEvents.get(0).getEventType());
    }

    @Test
    public void test_C_countByUserAndVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.RDV, new Date(), new Date());

        Page<TCalendarEvent> resultCount = calendarEventRepository.findByUserAndVcKey(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey(),
                EnumCalendarEventType.RDV, Pageable.unpaged());

        assertNotNull(resultCount);

        assertEquals(1, resultCount.getTotalElements());
    }

    @Test
    public void test_D_countByUserAndVcKey_zero() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.RDV, new Date(), new Date());

        Page<TCalendarEvent> resultCount = calendarEventRepository.findByUserAndVcKey(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey(),
                EnumCalendarEventType.PERM, Pageable.unpaged());

        assertNotNull(resultCount);

        assertEquals(0, resultCount.getTotalElements());
    }


    @Test
    public void test_E_findCalendarEventsByUserIdAndDate_participants() {
        String emailParticipant1 = "test@test.com";
        String emailParticipant2 = "test2@test.com";

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        // create 2 events with same participant
        // user id 1
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());
        // user id 2
        TCalendarEvent tCalendarEvent2 = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        createParticipantToEvent(tCalendarEvent, emailParticipant1);
        createParticipantToEvent(tCalendarEvent2, emailParticipant1);
        createParticipantToEvent(tCalendarEvent2, emailParticipant2);

        testEntityManager.merge(tCalendarEvent);
        testEntityManager.merge(tCalendarEvent2);

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDate(0L,
                "",
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                emailParticipant1);

        assertNotNull(calEvents);

        assertEquals(2, calEvents.size());

        List<TCalendarEvent> calEvents2 = calendarEventRepository.findCalendarEventsByUserIdAndDate(0L,
                "",
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                emailParticipant2);

        assertNotNull(calEvents2);

        assertEquals(1, calEvents2.size());

        List<TCalendarEvent> calEvents3 = calendarEventRepository.findCalendarEventsByUserIdAndDate(
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                "",
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                "");

        assertNotNull(calEvents3);

        assertEquals(2, calEvents3.size());
    }

    @Test
    public void test_F_findCalendarEventsByUserIdAndDateAndDossierId_participants() {
        String emailParticipant1 = "test@test.com";
        String emailParticipant2 = "test2@test.com";

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        // create 2 events with same participant
        // user id 1
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());
        // user id 2
        TCalendarEvent tCalendarEvent2 = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        createParticipantToEvent(tCalendarEvent, emailParticipant1);
        createParticipantToEvent(tCalendarEvent2, emailParticipant1);
        createParticipantToEvent(tCalendarEvent2, emailParticipant2);

        testEntityManager.merge(tCalendarEvent);
        testEntityManager.merge(tCalendarEvent2);

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDateAndDossierId(0L,
                tCalendarEvent.getDossier().getIdDoss(),
                "",
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                emailParticipant1);

        assertNotNull(calEvents);

        assertEquals(1, calEvents.size());

        List<TCalendarEvent> calEvents2 = calendarEventRepository.findCalendarEventsByUserIdAndDateAndDossierId(0L,
                tCalendarEvent2.getDossier().getIdDoss(),
                "",
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                emailParticipant2);

        assertNotNull(calEvents2);

        assertEquals(1, calEvents2.size());

        List<TCalendarEvent> calEvents3 = calendarEventRepository.findCalendarEventsByUserIdAndDateAndDossierId(
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                tCalendarEvent.getDossier().getIdDoss(),
                "",
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM),
                "");

        assertNotNull(calEvents3);

        assertEquals(1, calEvents3.size());
    }


    @Test
    public void test_G_findCalendarEventsByUserId() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        Date start = CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(1));
        List<TCalendarEvent> tCalendarEvents = calendarEventRepository.findCalendarEventsByUserId(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                EnumCalendarEventType.PERM, start);

        assertNotNull(tCalendarEvents);

        assertEquals(1, tCalendarEvents.size());
    }

    @Test
    public void test_H_findCalendarEventsByUserId_zero() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.RDV, new Date(), new Date());

        Date start = CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(1));
        List<TCalendarEvent> tCalendarEvents = calendarEventRepository.findCalendarEventsByUserId(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                EnumCalendarEventType.PERM, start);

        assertNotNull(tCalendarEvents);

        assertEquals(0, tCalendarEvents.size());
    }

    @Test
    public void test_I_findCalendarEventsByUserId_zero() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        Date start = CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(1));
        List<TCalendarEvent> tCalendarEvents = calendarEventRepository.findCalendarEventsByUserId(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                EnumCalendarEventType.PERM, start);

        assertNotNull(tCalendarEvents);

        assertEquals(0, tCalendarEvents.size());
    }

}
