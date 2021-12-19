package com.ulegalize.lawfirm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.DefaultLawfirmDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
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
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LoginV2ControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private LawfirmV2Service lawfirmV2Service;
    @Autowired
    private LawfirmRepository lawfirmRepository;

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


    @WithMockUser(value = "spring")
    @Test
    public void test_A_validateTempKey_containtempvckey() throws Exception {
        createTSequences();
        createVatCountry();

        String tempVc = lawfirmV2Service.createTempVc("myuser@gmail.com", "workspace");
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        // get the last created

        lawfirm = lawfirmEntityList.get(lawfirmEntityList.size() - 1);

        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        defaultLawfirmDTO.setVcKey(lawfirm.getVckey() + "a44");
        defaultLawfirmDTO.setLanguage(EnumLanguage.FR.getShortCode());
        LawfirmDTO lawfirmDTO = new LawfirmDTO();
        lawfirmDTO.setCountryCode("BE");
        lawfirmDTO.setCurrency(EnumRefCurrency.EUR);
        defaultLawfirmDTO.setLawfirmDTO(lawfirmDTO);
        mvc.perform(post("/v2/login/validate/user/")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(defaultLawfirmDTO)))
                .andExpect(jsonPath("$.temporaryVc", is(false)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_validateTempKey() throws Exception {
        createTSequences();
        createVatCountry();

        String tempVc = lawfirmV2Service.createTempVc("myuser@gmail.com", "workspace");
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        // get the last created

        lawfirm = lawfirmEntityList.get(lawfirmEntityList.size() - 1);

        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        defaultLawfirmDTO.setVcKey("NEWIBFF");
        defaultLawfirmDTO.setLanguage(EnumLanguage.FR.getShortCode());
        LawfirmDTO lawfirmDTO = new LawfirmDTO();
        lawfirmDTO.setCountryCode("BE");
        lawfirmDTO.setCurrency(EnumRefCurrency.EUR);
        defaultLawfirmDTO.setLawfirmDTO(lawfirmDTO);
        mvc.perform(post("/v2/login/validate/user/")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(defaultLawfirmDTO)))
                .andExpect(jsonPath("$.temporaryVc", is(false)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_validateTempKey_same() throws Exception {
        createTSequences();
        createVatCountry();

        String tempVc = lawfirmV2Service.createTempVc("myuser@gmail.com", "workspace");
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        // get the last created

        lawfirm = lawfirmEntityList.get(lawfirmEntityList.size() - 1);

        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        defaultLawfirmDTO.setVcKey(lawfirm.getVckey());
        defaultLawfirmDTO.setLanguage(EnumLanguage.FR.getShortCode());
        LawfirmDTO lawfirmDTO = new LawfirmDTO();
        lawfirmDTO.setCountryCode("BE");
        lawfirmDTO.setCurrency(EnumRefCurrency.EUR);
        defaultLawfirmDTO.setLawfirmDTO(lawfirmDTO);
        mvc.perform(post("/v2/login/validate/user/")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(defaultLawfirmDTO)))
                .andExpect(jsonPath("$.temporaryVc", is(false)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_validateTempKey_error_numberTemp() throws Exception {
        createTSequences();
        createVatCountry();

        String tempVc = lawfirmV2Service.createTempVc("myuser@gmail.com", "workspace");
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        // get the last created

        lawfirm = lawfirmEntityList.get(lawfirmEntityList.size() - 1);

        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        defaultLawfirmDTO.setVcKey(lawfirm.getVckey() + "55");
        defaultLawfirmDTO.setLanguage(EnumLanguage.FR.getShortCode());
        LawfirmDTO lawfirmDTO = new LawfirmDTO();
        lawfirmDTO.setCountryCode("BE");
        lawfirmDTO.setCurrency(EnumRefCurrency.EUR);
        defaultLawfirmDTO.setLawfirmDTO(lawfirmDTO);
        mvc.perform(post("/v2/login/validate/user/")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(defaultLawfirmDTO)))
                .andExpect(status().isForbidden());
    }

}
