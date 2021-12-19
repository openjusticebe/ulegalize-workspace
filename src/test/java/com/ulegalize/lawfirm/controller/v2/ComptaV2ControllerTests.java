package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TFrais;
import com.ulegalize.security.EnumRights;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ComptaV2ControllerTests extends EntityTest {
    LawfirmToken lawfirmToken;
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

    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        // "support@ulegalize.com";
        lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(),
                "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname,
                DriveType.openstack, "");

        lawfirmToken.getEnumRights().add(EnumRights.ADMINISTRATEUR);
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
    }

    @Test
    public void test_A_getComptaById() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);

        mvc.perform(get("/v2/compta")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fraisId", String.valueOf(tFrais.getIdFrais())))
                .andExpect(jsonPath("$.id", equalTo(tFrais.getIdFrais().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_B_getGrids() throws Exception {
        lawfirmToken.getEnumRights().add(EnumRights.ADMINISTRATEUR);
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        mvc.perform(get("/v2/compta/grids")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value", equalTo(tFrais.getGridId())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_C_getGrids() throws Exception {
        lawfirmToken.setEnumRights(new ArrayList<>());
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        mvc.perform(get("/v2/compta/grids")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_D_getDefaultCompta() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);

        lawfirmToken.setEnumRights(new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        mvc.perform(get("/v2/compta/default")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vcKey", equalTo(lawfirmToken.getVcKey())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_E_createcompta_throwError() throws Exception {
        lawfirmToken.setEnumRights(new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        mvc.perform(post("/v2/compta")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
