package com.ulegalize.lawfirm.service.v2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToCalendarConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.service.v2.CalendarV2Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class CalendarV2ServiceImplTest extends EntityTest {

    @Autowired
    private CalendarV2Service calendarV2Service;
    private LawfirmEntity lawfirm;

    @Autowired
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    public void test_A_approveLawfirmCalendarEvent() {
        Date start = new Date();
        Date end = new Date();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, start, end);
        TCalendarEvent tCalendarEventToApproved = createTCalendarEvent(lawfirm, EnumCalendarEventType.RDV, start, end);

        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = calendarV2Service.approveLawfirmCalendarEvent(tCalendarEventToApproved.getId());
        assertTrue(lawfirmCalendarEventDTO.isApproved());

        TCalendarEvent tCalendarEvent1 = testEntityManager.find(TCalendarEvent.class, tCalendarEvent.getId());
        assertNull(tCalendarEvent1);
        TCalendarEvent tCalendarEventUpdated = testEntityManager.find(TCalendarEvent.class, tCalendarEventToApproved.getId());
        assertTrue(tCalendarEventUpdated.isApproved());

    }

    @Test
    void test_B_updateLawfirmCalendarEvent_rdv_not_approve() {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.RDV, new Date(), new Date());

        EntityToCalendarConverter entityToCalendarConverter = new EntityToCalendarConverter();
        tCalendarEvent.setDossier(null);
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = entityToCalendarConverter.apply(tCalendarEvent, "fr");

        calendarV2Service.updateLawfirmCalendarEvent(tCalendarEvent.getId(), lawfirmCalendarEventDTO);

        TCalendarEvent tCalendarEventUpdated = testEntityManager.find(TCalendarEvent.class, tCalendarEvent.getId());

        assertEquals(tCalendarEventUpdated.getEventType(), EnumCalendarEventType.RDV);
        assertFalse(tCalendarEventUpdated.isApproved());
    }

    @Test
    void test_C_updateLawfirmCalendarEvent_eventType_match() {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.RDV, new Date(), new Date());

        EntityToCalendarConverter entityToCalendarConverter = new EntityToCalendarConverter();
        tCalendarEvent.setDossier(null);
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = entityToCalendarConverter.apply(tCalendarEvent, "fr");

        calendarV2Service.updateLawfirmCalendarEvent(tCalendarEvent.getId(), lawfirmCalendarEventDTO);

        assertEquals(lawfirmCalendarEventDTO.getEventType(), tCalendarEvent.getEventType());
    }

    @Test
    void test_D_updateLawfirmCalendarEvent_eventType_notMatch() {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.RDV, new Date(), new Date());

        EntityToCalendarConverter entityToCalendarConverter = new EntityToCalendarConverter();
        tCalendarEvent.setDossier(null);
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = entityToCalendarConverter.apply(tCalendarEvent, "fr");
        lawfirmCalendarEventDTO.setEventType(EnumCalendarEventType.PERM);

        assertNotEquals(lawfirmCalendarEventDTO.getEventType(), tCalendarEvent.getEventType());

        assertThrows(ResponseStatusException.class, () ->
                calendarV2Service.updateLawfirmCalendarEvent(tCalendarEvent.getId(), lawfirmCalendarEventDTO));

    }
}
