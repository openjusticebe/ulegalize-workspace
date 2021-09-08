package com.ulegalize.lawfirm.controller;

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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LawyerControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    private UsernamePasswordAuthenticationToken authentication;

    private LawfirmEntity lawfirm;

    @Autowired
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setupAuthenticate() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        lawfirm = createLawfirm();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }


    @WithMockUser(value = "spring")
    @Test
    public void test_G_getLawyerByUserId() throws Exception {
        mvc.perform(get("/lawyer")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_H_getCalendarEvents() throws Exception {
        mvc.perform(get("/lawyer/calendar/events")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_I_postCalendarEvents() throws Exception {
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();
        lawfirmCalendarEventDTO.setEventType(EnumCalendarEventType.OTH);
        lawfirmCalendarEventDTO.setStart(new Date());
        lawfirmCalendarEventDTO.setEnd(new Date());

        mvc.perform(post("/lawyer/calendar/events")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(lawfirmCalendarEventDTO)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_J_putSaveLawfirmCalendarEvent() throws Exception {
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();
        lawfirmCalendarEventDTO.setEventType(EnumCalendarEventType.OTH);
        lawfirmCalendarEventDTO.setStart(new Date());
        lawfirmCalendarEventDTO.setEnd(new Date());

        mvc.perform(put("/lawyer/calendar/events/1")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(lawfirmCalendarEventDTO)))
                .andExpect(status().is2xxSuccessful());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_K_deleteLawfirmCalendarEvent() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.OTH, new Date(), new Date());

        mvc.perform(delete("/lawyer/calendar/events/" + tCalendarEvent.getId())
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_L_approveLawfirmCalendarEvent() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.OTH, new Date(), new Date());


        mvc.perform(put("/lawyer/calendar/events/" + tCalendarEvent.getId() + "/approve")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_L_approveLawfirmCalendarEvent_error() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.OTH, new Date(), new Date());
        tCalendarEvent.getContact().setF_email("");
        testEntityManager.persist(tCalendarEvent.getContact());

        mvc.perform(put("/lawyer/calendar/events/" + tCalendarEvent.getId() + "/approve")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_L_approveLawfirmCalendarEvent_noContact() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.OTH, new Date(), new Date());
        // add contact to be approved
        tCalendarEvent.setContact(null);
        testEntityManager.persist(tCalendarEvent);

        mvc.perform(put("/lawyer/calendar/events/" + tCalendarEvent.getId() + "/approve")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
