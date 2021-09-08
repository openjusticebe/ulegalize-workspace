package com.ulegalize.lawfirm.repo;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.repository.CalendarEventRepository;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class CalendarEventRepositoryTests extends EntityTest {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Test
    public void test_A_findCalendarEventsByUserIdAndDate() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDate(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey(),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM));

        assertNotNull(calEvents);

        assertEquals(1, calEvents.size());
        assertEquals(tCalendarEvent.getId(), calEvents.get(0).getId());
        assertEquals(tCalendarEvent.getEventType(), calEvents.get(0).getEventType());
    }

    @Test
    public void test_B_findCalendarEventsByUserIdAndDateAndDossierId() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.PERM, new Date(), new Date());

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDateAndDossierId(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                tCalendarEvent.getDossier().getIdDoss(),
                lawfirmEntity.getVckey(),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(2)),
                CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().plusDays(2)),
                Collections.singletonList(EnumCalendarEventType.PERM));

        assertNotNull(calEvents);

        assertEquals(1, calEvents.size());
        assertEquals(tCalendarEvent.getId(), calEvents.get(0).getId());
        assertEquals(tCalendarEvent.getEventType(), calEvents.get(0).getEventType());
    }

    @Test
    public void test_C_countByUserAndVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.RDV, new Date(), new Date());

        Page<TCalendarEvent> resultCount = calendarEventRepository.findByUserAndVcKey(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey(),
                EnumCalendarEventType.RDV, Pageable.unpaged());

        assertNotNull(resultCount);

        assertEquals(1, resultCount.getTotalElements());
    }

    @Test
    public void test_D_countByUserAndVcKey_zero() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirmEntity, EnumCalendarEventType.RDV, new Date(), new Date());

        Page<TCalendarEvent> resultCount = calendarEventRepository.findByUserAndVcKey(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey(),
                EnumCalendarEventType.PERM, Pageable.unpaged());

        assertNotNull(resultCount);

        assertEquals(0, resultCount.getTotalElements());
    }
}
