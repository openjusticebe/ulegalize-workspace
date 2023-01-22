package com.ulegalize.lawfirm.controller.v1.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.MessageDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
class BackAdminV2ControllerTest extends EntityTest {

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
        // "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true,
                new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(),
                EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED));

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null,
                lawfirmToken.getAuthorities());

    }

    @Test
    void getLawfirmList() throws Exception {

        mvc.perform(get("/v1/backAdmin/lawfirm").with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", lawfirm.getVckey()))
                .andExpect(status().isOk());

    }

    @Test
    void testCreateMessage() throws Exception {
        ZonedDateTime now = ZonedDateTime.now();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageFr("Salut");
        messageDTO.setMessageEn("Hello");
        messageDTO.setMessageDe("Hallo");
        messageDTO.setMessageNl("Goeiedag");
        messageDTO.setDateTo(now);

        mvc.perform(post("/v1/backAdmin/message")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getVirtualcabNomenclatureListAdmin() throws Exception {
        LawfirmEntity lawfirmEntityToFind = createLawfirm("NEWVCKEY");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntityToFind, "new Nomenclature 1");
        createVirtualcabNomenclature(lawfirmEntityToFind, "new Nomenclature 2");

        mvc.perform(get("/v1/backAdmin/virtualcabNomenclature/vc/" + lawfirmEntityToFind.getVckey()).with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vcKey", lawfirmEntityToFind.getVckey()))
                .andExpect(jsonPath("$[0].label", equalTo(virtualcabNomenclature.getName())))
                .andExpect(status().isOk());
    }

    @Test
    void getVirtualcabNomenclatureListAdmin_with_vcKey_is_empty_must_return_exception() throws Exception {
        mvc.perform(get("/v1/backAdmin/virtualcabNomenclature/vc/" + "").with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vcKey", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVirtualcabNomenclatureListAdmin_with_lawfirm_error_must_return_exception() throws Exception {
        mvc.perform(get("/v1/backAdmin/virtualcabNomenclature/vc/" + "VCKEYNOTEXIST").with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vcKey", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVirtualcabNomenclatureListAdmin_with_empty_list() throws Exception {
        LawfirmEntity lawfirmEntityToFind = createLawfirm("NEWVCKEY");

        mvc.perform(get("/v1/backAdmin/virtualcabNomenclature/vc/" + lawfirmEntityToFind.getVckey()).with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("vcKey", lawfirmEntityToFind.getVckey()))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }
}
