package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
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

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        // "support@ulegalize.com";
        lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(),
                "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname,
                DriveType.openstack, "", "", verifyUser);

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

    @Test
    public void test_F_totalHonoraireByDossierId() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);

        lawfirmToken.setEnumRights(new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        mvc.perform(get("/v2/compta/dossier/" + dossier.getIdDoss() + "/total")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalHonoraire", greaterThan(BigDecimal.ONE.doubleValue())))
                .andExpect(status().isOk());
    }

    @Test
    public void test_G_totalThirdPartyByDossierId() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);

        tFrais.getRefCompte().setAccountTypeId(EnumAccountType.ACCOUNT_TIERS);
        testEntityManager.persist(tFrais.getRefCompte());

        lawfirmToken.setEnumRights(new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        mvc.perform(get("/v2/compta/dossier/" + dossier.getIdDoss() + "/tiers/total")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.montant", greaterThan(BigDecimal.ONE.doubleValue())))
                .andExpect(status().isOk());
    }

    @Test
    void test_H_deactivateCompta() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);

        lawfirmToken.setEnumRights(new ArrayList<>());
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        mvc.perform(put("/v2/compta/deactivate/" + tFrais.getIdFrais())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
