package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.DefaultLawfirmDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.security.EnumRights;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void test_A_validateTempKey_containtempvckey_change() throws Exception {
        createTSequences();
        createVatCountry();

        String tempVc = lawfirmV2Service.createTempVc("myuser@gmail.com", "workspace");
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        // get the last created

        lawfirm = lawfirmEntityList.get(lawfirmEntityList.size() - 1);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        user.setIdValid(EnumValid.VERIFIED);

        testEntityManager.persist(user);
        Long userId = user.getId();
        String fullname = user.getFullname();
        String usermail = user.getEmail();
        boolean verifyUser = user.getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        // HERE IS THE CHANGE
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
    public void test_B_validateTempKey_complete_change() throws Exception {
        createTSequences();
        createVatCountry();

        String tempVc = lawfirmV2Service.createTempVc("myuser@gmail.com", "workspace");
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        // get the last created

        lawfirm = lawfirmEntityList.get(lawfirmEntityList.size() - 1);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        user.setIdValid(EnumValid.VERIFIED);

        testEntityManager.persist(user);
        Long userId = user.getId();
        String fullname = user.getFullname();
        String usermail = user.getEmail();
        boolean verifyUser = user.getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        // HERE IS THE CHANGE
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

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        user.setIdValid(EnumValid.VERIFIED);

        testEntityManager.persist(user);
        Long userId = user.getId();
        String fullname = user.getFullname();
        String usermail = user.getEmail();
        boolean verifyUser = user.getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        DefaultLawfirmDTO defaultLawfirmDTO = new DefaultLawfirmDTO();
        // HERE IS THE CHANGE (SAME TEMP VCKEY)
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
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

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

    @WithMockUser(value = "spring")
    @Test
    public void test_E_verifyUser_true() throws Exception {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        mvc.perform(get("/v2/login/verifyUser?email=" + user.getEmail() + "&key=" + user.getHashkey())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_verifyUser_false() throws Exception {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        mvc.perform(get("/v2/login/verifyUser?email=" + user.getEmail() + "&key=test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_G_verifyUser_exception() throws Exception {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        mvc.perform(get("/v2/login/verifyUser?email=&key=test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_H_registerUser() throws Exception {
        createTSequences();
        String newEmail = "test@test.com";

        LawfirmToken lawfirmToken = new LawfirmToken(0L, newEmail, newEmail, "NO", "", true, Collections.singletonList(EnumRights.ADMINISTRATEUR), "", true,
                EnumLanguage.FR.getShortCode(),
                EnumRefCurrency.EUR.getSymbol(), newEmail, DriveType.openstack, "", false);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        mvc.perform(post("/v2/login/user")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(EnumValid.UNVERIFIED.name()))
                );
    }
}
