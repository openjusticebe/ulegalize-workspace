package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.DossierDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AffaireV2ControllerTest extends EntityTest {
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
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_saveAffaire() throws Exception {
        DossierDTO dossierDTO = new DossierDTO();
        dossierDTO.setNote("note");
        dossierDTO.setOpenDossier(new Date());
        dossierDTO.setType(EnumDossierType.DC);
        dossierDTO.setMemo("");
        TClients client = createClient(lawfirm);
        dossierDTO.setIdClient(client.getId_client());

        dossierDTO.setSuccess_fee_perc(0);
        dossierDTO.setSuccess_fee_montant(BigDecimal.valueOf(100));
        dossierDTO.setCouthoraire(0);
        dossierDTO.setQuality("");
        TUsers user = createUser("mine@mine.vok");
        dossierDTO.setIdUserResponsible(user.getId());

        TMatiereRubriques tMatiereRubriques = createTMatiereRubriques();
        dossierDTO.setId_matiere_rubrique(tMatiereRubriques.getIdMatiereRubrique());
        TClients clientAdv = createClient(lawfirm);

        dossierDTO.setIdAdverseClient(clientAdv.getId_client());

        mvc.perform(post("/v2/affaires")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dossierDTO)))
                .andExpect(jsonPath("$.note", is(dossierDTO.getNote())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getAffairesByVcUserIdAndSearchCriteria_noparam() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].label", containsStringIgnoringCase(dossier.getYear_doss())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_getAffairesByVcUserIdAndSearchCriteria_withParamFouned() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("searchCriteria", String.valueOf(LocalDate.now().getYear())))
                .andExpect(jsonPath("$[0].label", containsStringIgnoringCase(dossier.getYear_doss())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getAffairesByVcUserIdAndSearchCriteria_withParamNotFouned() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("searchCriteria", "any criteria ..."))
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_getAllAffairesPagination() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires/list")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", String.valueOf(0))
                .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$.content[0].id", equalTo(dossier.getIdDoss().intValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_E_getAllSharedAffairesPagination_notexist() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires/shared/list")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", String.valueOf(0))
                .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_getAllSharedAffairesPagination() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.NOT_SAME_VC);

        mvc.perform(get("/v2/affaires/shared/list")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", String.valueOf(0))
                .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$.content[0].id", equalTo(dossier.getIdDoss().intValue())))
                .andExpect(status().isOk());
    }

}
