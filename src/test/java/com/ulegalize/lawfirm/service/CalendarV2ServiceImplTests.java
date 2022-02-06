package com.ulegalize.lawfirm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
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

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class CalendarV2ServiceImplTests extends EntityTest {

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
        lawfirm = createLawfirm();
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

}
