package com.ulegalize.lawfirm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TDossiers;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AffaireControllerTest extends EntityTest {
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
    public void test_B_updateShare() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        // new
        String email = "mynew@gmail.com";
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, email);

        testEntityManager.persist(lawfirm);

        UpdateShareRequestDTO updateShareRequest = new UpdateShareRequestDTO();
        updateShareRequest.setId_doss(dossier.getIdDoss());
        updateShareRequest.setVc_key(lawfirm.getVckey());
        updateShareRequest.setAll_member(false);
        updateShareRequest.setVcu_id(lawfirmUsers.getId());
        mvc.perform(post("/affaire/share")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateShareRequest)))
                .andExpect(jsonPath("$.emails[0]", is(email)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getDossierById() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        mvc.perform(get("/affaire/" + dossier.getIdDoss())
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(dossier.getIdDoss().intValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_switchDossier() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        // new
        String email = "mynew@gmail.com";
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, email);

        testEntityManager.persist(lawfirm);

        DossierDTO dossierSummary = new DossierDTO();
        dossierSummary.setId(dossier.getIdDoss());
        dossierSummary.setIsDigital(true);

        mvc.perform(post("/affaire/" + dossier.getIdDoss() + "/digital")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dossierSummary)))
                .andExpect(jsonPath("$.isDigital", is(true)))
                .andExpect(status().isOk());
    }
}
