package com.ulegalize.lawfirm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawyerDutyRequest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LawyerPublicControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

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
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        lawfirm = createLawfirm("MYLAW");
    }

    @Test
    public void test_A_getLawyer() throws Exception {
        String aliasPublic = lawfirm.getLawfirmUsers().get(0).getUser().getAliasPublic();

        mvc.perform(get("/public/lawyers/" + aliasPublic)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(lawfirm.getLawfirmUsers().get(0).getUser().getId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_B_name_getFilterLawyer() throws Exception {
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        mvc.perform(get("/public/lawyers?name=" + fullname)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(lawfirm.getLawfirmUsers().get(0).getUser().getId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_C_name_getFilterLawyer_thenEmpty() throws Exception {
        mvc.perform(get("/public/lawyers?name=Vaffa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_D_all_getFilterLawyer() throws Exception {
        mvc.perform(get("/public/lawyers?search=all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_E_newLawyerAppointment() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, new Date(), new Date());
        String aliasPublic = lawfirm.getLawfirmUsers().get(0).getUser().getAliasPublic();
        LawyerDutyRequest appointment = new LawyerDutyRequest();
        appointment.setFirstName("first appointment");
        appointment.setLastName("last name");
        appointment.setNote("note appointment");
        appointment.setEmail("newrdv@");
        appointment.setStart(tCalendarEvent.getStart());
        appointment.setEnd(tCalendarEvent.getEnd());

        mvc.perform(post("/public/lawyers/" + aliasPublic + "/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(appointment)))
                .andExpect(jsonPath("$.remark", not("")))
                .andExpect(status().isOk());
    }

}
