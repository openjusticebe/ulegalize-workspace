package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactures;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class InvoiceV2ControllerTest extends EntityTest {
    LawfirmToken lawfirmToken;
    @Autowired
    private MockMvc mockMvc;

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
        lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getInvoicesBySearchCriteria_noParam() throws Exception{

        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].label", containsStringIgnoringCase(String.valueOf(tFactures.getYearFacture()))))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_getInvoicesBySearchCriteria_withParma_founded() throws Exception {

        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", String.valueOf(LocalDate.now().getYear())))
                .andExpect(jsonPath("$[0].label", containsStringIgnoringCase(String.valueOf(tFactures.getYearFacture()))))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getInvoicesBySearchCriteria_withParma_notFounded() throws Exception {

        mockMvc.perform(get("/v2/invoices")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", "bala bala"))
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_getDefaultInvoice() throws Exception {

        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/default")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vcKey", equalTo(tFactures.getVcKey())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_E_getInvoiceById() throws Exception {
        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/" + tFactures.getIdFacture())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("invoiceId", String.valueOf(tFactures.getIdFacture())))
                .andExpect(jsonPath("$.id", equalTo(tFactures.getIdFacture().intValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_totalByDossier() throws Exception {
        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/dossier/" + tFactures.getIdDoss() + "/total")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("$.montant", tFactures.getMontant().toString()))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_H_getPrestationByDossierId() throws Exception {
        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/" + tFactures.getIdFacture() + "/prestations/" + tFactures.getIdDoss())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("$[0].id", tFactures.getTFactureTimesheetList().get(0).getTsId().toString()))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_I_getFraisAdminByDossierId() throws Exception {
        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/" + tFactures.getIdFacture() + "/fraisAdmin/" + tFactures.getIdDoss())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("$[0].id", tFactures.getFraisAdminList().get(0).getID().toString()))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_J_getDeboursByDossierId() throws Exception {
        TFactures tFactures = createFacture(lawfirm);
        // to link debours
        tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste().setFraisProcedure(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());


        mockMvc.perform(get("/v2/invoices/" + tFactures.getIdFacture() + "/debours/" + tFactures.getIdDoss())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("$[0].id", tFactures.getFraisDeboursList().get(0).getID().toString()))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_K_getFraisCollabByDossierId() throws Exception {
        TFactures tFactures = createFacture(lawfirm);
        // to link debours
        tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste().setFraisCollaboration(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());


        mockMvc.perform(get("/v2/invoices/" + tFactures.getIdFacture() + "/fraisCollaborat/" + tFactures.getIdDoss())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("$[0].id", tFactures.getFraisCollaborationArrayList().get(0).getID().toString()))
                .andExpect(status().isOk());
    }

}
