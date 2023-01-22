package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import lombok.extern.slf4j.Slf4j;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class VirtualcabNomenclatureControllerTest extends EntityTest {
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
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @Test
    void test_A_saveVirtualcabNomenclature() throws Exception {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();

        mvc.perform(post("/v2/vcnomenclature")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(virtualcabNomenclatureDTO)))
                .andExpect(jsonPath("$").exists())
                .andExpect(status().isOk());
    }

    @Test
    void test_B_getVirtualcabNomenclatureList() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        createVirtualcabNomenclature(lawfirm, "Nomenclature2");

        mvc.perform(get("/v2/vcnomenclature/list")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value", equalTo(virtualcabNomenclature.getId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    void test_C_getVirtualcabNomenclatureWithPagination() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        createVirtualcabNomenclature(lawfirm, "Nomenclature2");
        createVirtualcabNomenclature(lawfirm, "Nomenclature3");
        createVirtualcabNomenclature(lawfirm, "Nomenclature2");

        mvc.perform(get("/v2/vcnomenclature/vcnomenclatures")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", String.valueOf(0))
                        .param("limit", String.valueOf(5)))
                .andExpect(jsonPath("$.content[0].id", equalTo(virtualcabNomenclature.getId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    void test_D_getVirtualcabNomenclatureById() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");

        mvc.perform(get("/v2/vcnomenclature/" + virtualcabNomenclature.getId())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(virtualcabNomenclature.getId().intValue())))
                .andExpect(status().isOk());
    }

    @Test
    void test_E_updateVirtualcabNomenclature() throws Exception {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "nomenclature1");
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setId(nomenclature1.getId());

        mvc.perform(put("/v2/vcnomenclature")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(virtualcabNomenclatureDTO)))
                .andExpect(jsonPath("$").exists())
                .andExpect(status().isOk());
    }

    @Test
    void deleteVirtualcabNomenclature() throws Exception {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "nomenclature1");
        createVirtualcabNomenclature(lawfirm, "nomenclature1");

        mvc.perform(delete("/v2/vcnomenclature/" + nomenclature1.getId())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(status().isOk());
    }
}