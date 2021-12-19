package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LawfirmV2ControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    private UsernamePasswordAuthenticationToken authentication;

    private LawfirmEntity lawfirm;
    @Autowired
    private EntityToUserConverter entityToUserConverter;

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
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_updateRoleLawfirmUser() throws Exception {
        LawyerDTO lawyerDTO = entityToUserConverter.apply(lawfirm.getLawfirmUsers().get(0).getUser(), false);
        lawyerDTO.setFunctionId(EnumRole.ASSISTANT.getIdRole());

        mvc.perform(put("/v2/lawfirm/users/role")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lawyerDTO)))
                .andExpect(jsonPath("$.functionId", is(lawyerDTO.getFunctionId())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_updateRoleLawfirmUser_not() throws Exception {
        LawyerDTO lawyerDTO = entityToUserConverter.apply(lawfirm.getLawfirmUsers().get(0).getUser(), false);
        lawyerDTO.setFunctionId(EnumRole.ASSISTANT.getIdRole());
        EnumRole enumBeforeUpdate = lawfirm.getLawfirmUsers().get(0).getIdRole();

        mvc.perform(put("/v2/lawfirm/users/role")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lawyerDTO)))
                .andExpect(jsonPath("$.functionId", not(enumBeforeUpdate.getIdRole())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getLawfirmUsers() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, new Date(), new Date());

        mvc.perform(get("/v2/lawfirm/users")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].public", is(lawfirm.getLawfirmUsers().get(0).isPublic())))
                .andExpect(status().isOk());
    }
}
