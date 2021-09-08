package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactures;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

    @Before
    public void setupAuthenticate() {
        lawfirm = createLawfirm();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

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
    public void test_A_getInvoicesBySearchCriteria_withParma_founded() throws Exception{

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
    public void test_A_getInvoicesBySearchCriteria_withParma_notFounded() throws Exception {

        mockMvc.perform(get("/v2/invoices")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("searchCriteria", "bala bala"))
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getDefaultInvoice() throws Exception {

        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/default")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vcKey", equalTo(tFactures.getVcKey())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getInvoiceById() throws Exception {
        TFactures tFactures = createFacture(lawfirm);

        mockMvc.perform(get("/v2/invoices/" + tFactures.getIdFacture())
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("invoiceId", String.valueOf(tFactures.getIdFacture())))
                .andExpect(jsonPath("$.id", equalTo(tFactures.getIdFacture().intValue())))
                .andExpect(status().isOk());
    }

}
