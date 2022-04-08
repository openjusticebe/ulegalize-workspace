package com.ulegalize.lawfirm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.LawfirmWebsiteEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LawfirmControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    private LawfirmEntity lawfirm;
    private UsernamePasswordAuthenticationToken authentication;

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
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }
    @WithMockUser(value = "spring")
    @Test
    public void test_A_GetLawfirm() throws Exception {
        mvc.perform(get("/lawfirm/" + lawfirm.getVckey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @WithMockUser(value = "spring")
    @Test
    public void test_C_getLawfirmWebsite() throws Exception {
        mvc.perform(get("/lawfirm/" + lawfirm.getVckey() + "/website")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.active", is(notNullValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_saveLawfirmWebsite() throws Exception {
        LawfirmWebsiteEntity lawfirmWebsiteEntity = createLawfirmWebsiteEntity(lawfirm);

        mvc.perform(put("/lawfirm/" + lawfirm.getVckey() + "/website")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", lawfirmWebsiteEntity.getTitle())
                .param("vcKey", lawfirmWebsiteEntity.getVcKey()))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_E_activateLawfirmWebsite() throws Exception {
        LawfirmWebsiteEntity lawfirmWebsiteEntity = createLawfirmWebsiteEntity(lawfirm);

        mvc.perform(put("/lawfirm/" + lawfirm.getVckey() + "/website/activate")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(lawfirmWebsiteEntity)))
                .param("active", String.valueOf(lawfirmWebsiteEntity.isActive())))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_getLawfirmLawyers() throws Exception {
        mvc.perform(get("/lawfirm/" + lawfirm.getVckey() + "/lawyers")
                .contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$[0].lawfirm.vckey", is(lawfirm.getVckey())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_getLawfirmLawyers_withcalendar() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, new Date(), new Date());
        mvc.perform(get("/lawfirm/" + lawfirm.getVckey() + "/lawyers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_G_activateLawfirmLawyer() throws Exception {
        LawfirmWebsiteEntity lawfirmWebsiteEntity = createLawfirmWebsiteEntity(lawfirm);
        List<LawfirmUsers> lawfirmUsers = lawfirm.getLawfirmUsers();
        boolean isPublic = lawfirmUsers.get(0).isPublic();
        mvc.perform(post("/lawfirm/" + lawfirm.getVckey() + "/lawyers/" + lawfirmUsers.get(0).getUser().getId() + "/activate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.public", is(!isPublic)))
                .andExpect(status().isOk());
    }

}
