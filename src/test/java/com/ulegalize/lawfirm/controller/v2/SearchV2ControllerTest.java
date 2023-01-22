package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class SearchV2ControllerTest extends EntityTest {
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
    public void setAuthentication() {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        lawfirmToken.getEnumRights().add(EnumRights.ADMINISTRATEUR);
        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
    }

    @Test
    public void test_A_getRefCompte() throws Exception {

        RefCompte refCompte = createRefCompte(lawfirm);

        mvc.perform(get("/v2/search/tiers")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value", equalTo(refCompte.getIdCompte())))
                .andExpect(status().isOk());

    }

    @Test
    public void test_B_getTimesheetTypes() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);

        mvc.perform(get("/v2/search/timesheetTypes")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value", equalTo(tTimesheet.getTTimesheetType().getIdTs())))
                .andExpect(status().isOk());

    }

    @Test
    public void test_C_getDeboursTypes() throws Exception {

        TDebourType tDebourType = createTDebourType(lawfirm);

        mvc.perform(get("/v2/search/deboursTypes")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value", equalTo(tDebourType.getIdDebourType().intValue())))
                .andExpect(status().isOk());

    }

    @Test
    public void test_D_getFacturesTypes() throws Exception {
        mvc.perform(get("/v2/search/facturesTypes")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("isCreated", Boolean.FALSE.toString()))
                .andExpect(jsonPath("$", hasSize(EnumFactureType.values().length)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_D_getFacturesTypes_created() throws Exception {
        mvc.perform(get("/v2/search/facturesTypes")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("isCreated", Boolean.TRUE.toString()))
                // temporary not accepted
                .andExpect(jsonPath("$", hasSize(EnumFactureType.values().length - 2)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_E_getFactureEcheances() throws Exception {

        TFactureEcheance tFactureEcheance = createFactureEcheance(1);

        mvc.perform(get("/v2/search/factureEcheances")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value", equalTo(tFactureEcheance.getID())))
                .andExpect(status().isOk());

    }

    @Test
    public void test_F_getAccountType() throws Exception {

        mvc.perform(get("/v2/search/accountType")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(EnumAccountType.values().length)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_G_types() throws Exception {

        mvc.perform(get("/v2/search/types")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(EnumTType.values().length)))
                .andExpect(status().isOk());

    }

    @Test
    public void test_H_userFull() throws Exception {
        TUsers user = createUser("new@email.com");

        mvc.perform(get("/v2/search/users/full")
                        .with(authentication(authentication))
                        .param("search", "new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].label", is(user.getEmail())))
                .andExpect(status().isOk());

    }

    @Test
    void test_H_getCalendarEventType() throws Exception {
        mvc.perform(get("/v2/search/calendarEventType")
                        .with(authentication(authentication))
                        .param("search", "new")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(EnumCalendarEventType.values().length)))
                .andExpect(status().isOk());
    }

    @Test
    void test_I_getCalendarEventType_without_authentication_must_return_4xxClientError() throws Exception {
        mvc.perform(get("/v2/search/calendarEventType")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test_J_getMatieres() throws Exception {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.MD.getDossType(), EnumDossierType.MD.name());

        mvc.perform(get("/v2/search/matieres")
                        .with(authentication(authentication))
                        .param("tDossiersType", tDossiersType.getDossType())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_K_refTransaction() throws Exception {

        mvc.perform(get("/v2/search/refTransaction")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_L_getDossierContactType() throws Exception {

        mvc.perform(get("/v2/search/dossierContactType")
                        .with(authentication(authentication))
                        .param("typeDossierValue", "DC")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }

}
