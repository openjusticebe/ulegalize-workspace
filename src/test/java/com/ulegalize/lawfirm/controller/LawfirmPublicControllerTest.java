package com.ulegalize.lawfirm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LawfirmPublicControllerTest extends EntityTest {
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
        lawfirm = createLawfirm("MYLAW");
        createLawfirmWebsiteEntity(lawfirm);

    }

    @Test
    public void test_A_getLawfirm() throws Exception {
        mvc.perform(get("/public/lawfirms/" + lawfirm.getVckey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vckey", equalTo(lawfirm.getVckey())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_B_getFilterLawfirm() throws Exception {
        mvc.perform(get("/public/lawfirms?search=" + lawfirm.getAlias())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].vckey", equalTo(lawfirm.getVckey())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_C_getFilterLawfirm_thenError() throws Exception {
        mvc.perform(get("/public/lawfirms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void test_D_getLawyers() throws Exception {
        String lawyerAlias = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();

        mvc.perform(get("/public/lawfirms/" + lawfirm.getAlias() + "/lawyers/" + lawyerAlias)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName", equalTo(lawfirm.getLawfirmUsers().get(0).getUser().getFullname())))
                .andExpect(status().isOk());
    }
}
