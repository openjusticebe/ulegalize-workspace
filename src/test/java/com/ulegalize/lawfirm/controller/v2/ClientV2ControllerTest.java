package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.DossierContact;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.model.entity.TDossiers;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientV2ControllerTest extends EntityTest {
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
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getclients() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        mvc.perform(get("/v2/clients")
                        .param("vcKey", lawfirm.getVckey())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_getclientById() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

        mvc.perform(get("/v2/clients/" + dossierContactClient.get().getClients().getId_client())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(dossierContactClient.get().getClients().getId_client().intValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getAllContactsPagination() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

        mvc.perform(get("/v2/clients/list")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vcKey", lawfirm.getVckey())
                        .param("offset", String.valueOf(0))
                        .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$[0].firstname", equalTo(dossierContactClient.get().getClients().getF_prenom())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_getAllContactsPagination() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();
        mvc.perform(get("/v2/clients/list")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", String.valueOf(0))
                        .param("vcKey", lawfirm.getVckey())
                        .param("limit", String.valueOf(10))
                        .param("searchCriteria", dossierContactClient.get().getClients().getF_nom()))
                .andExpect(jsonPath("$[0].firstname", equalTo(dossierContactClient.get().getClients().getF_prenom())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_E_getAllContactsPagination_noResult() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        mvc.perform(get("/v2/clients/list")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vcKey", lawfirm.getVckey())
                        .param("offset", String.valueOf(0))
                        .param("limit", String.valueOf(10))
                        .param("searchCriteria", "bye bye"))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_countContactsList() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        // 2 clients created for the dossirer
        mvc.perform(get("/v2/clients/count")
                        .param("vcKey", lawfirm.getVckey())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(2)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_G_countClientsByName() throws Exception {

        TClients tClients = createClient(lawfirm);
        mvc.perform(get("/v2/clients/clients/count")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", "nme"))
                .andExpect(jsonPath("$", is(1)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_H_countClientsByNameNotFounded() throws Exception {
        TClients tClients = createClient(lawfirm);
        mvc.perform(get("/v2/clients/clients/count")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", "Bye Bye"))
                .andExpect(jsonPath("$", is(0)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_I_countClientsByNameNotFounded() throws Exception {
        TClients tClients = createClient(lawfirm);
        mvc.perform(get("/v2/clients/clients/count")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", "Bye Bye"))
                .andExpect(jsonPath("$", is(0)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_J_getAllContactsByIds_oneResult() throws Exception {
        TClients tClients = createClient(lawfirm);

        // workaround to avoid [] into the String
        String clientIds = String.valueOf(List.of(tClients.getId_client())).substring(1, String.valueOf(List.of(tClients.getId_client())).length() - 1);
        mvc.perform(get("/v2/clients/ids")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("clientIds", clientIds))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_K_getAllContactsByIds_twoResult() throws Exception {
        TClients tClients = createClient(lawfirm);
        TClients tClients2 = createClient(lawfirm);
        List<Long> id_client = List.of(tClients.getId_client(), tClients2.getId_client());

        // workaround to avoid [] into the String
        String clientIds = String.valueOf(id_client).substring(1, String.valueOf(id_client).length() - 1);
        mvc.perform(get("/v2/clients/ids")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("clientIds", clientIds))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
    }
}
