package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.NomenclatureConfigDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TNomenclatureConfig;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import com.ulegalize.lawfirm.repository.TNomenclatureConfigRepository;
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
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class NomenclatureConfigControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    private UsernamePasswordAuthenticationToken authentication;

    private LawfirmEntity lawfirm;

    @Autowired
    private TNomenclatureConfigRepository nomenclatureConfigRepository;

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
    void test_A_getNomenclatureConfigInfoByVcNomenclature() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        TNomenclatureConfig nomenclatureConfig = createNomenclatureConfig(virtualcabNomenclature, "Config1");
        createNomenclatureConfig(virtualcabNomenclature, "Config2");
        createNomenclatureConfig(virtualcabNomenclature, "Config3");

        mvc.perform(get("/v2/nomenclatureConfig/" + virtualcabNomenclature.getId())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].description", equalTo(nomenclatureConfig.getLabel())))
                .andExpect(status().isOk());
    }

    @Test
    void test_B_addNomenclatureConfig() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);

        mvc.perform(post("/v2/nomenclatureConfig")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nomenclatureConfigDTO)))
                .andExpect(status().isOk());

        Optional<TNomenclatureConfig> nomenclatureConfigOptional = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfigDTO.getDescription());
        assertFalse(nomenclatureConfigOptional.isEmpty());
        assertEquals(nomenclatureConfigOptional.get().getLabel(), nomenclatureConfigDTO.getDescription());
    }

    @Test
    void test_C_removeNomenclatureConfig() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        TNomenclatureConfig nomenclatureConfig = createNomenclatureConfig(virtualcabNomenclature, "nomenclatureConfig");

        Optional<TNomenclatureConfig> nomenclatureConfigOptional = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfig.getLabel());
        assertTrue(nomenclatureConfigOptional.isPresent());

        mvc.perform(delete("/v2/nomenclatureConfig/" + virtualcabNomenclature.getId() + "/" + nomenclatureConfig.getLabel())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<TNomenclatureConfig> nomenclatureConfigOptional2 = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfig.getLabel());
        assertTrue(nomenclatureConfigOptional2.isEmpty());
    }

    @Test
    void test_D_getNomenclatureConfig() throws Exception {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        TNomenclatureConfig nomenclatureConfig = createNomenclatureConfig(virtualcabNomenclature, "nomenclatureConfig");

        mvc.perform(get("/v2/nomenclatureConfig?vcNomenclatureId=" + virtualcabNomenclature.getId() + "&nomenclatureConfig=" + nomenclatureConfig.getLabel())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", equalTo(nomenclatureConfig.getLabel())))
                .andExpect(status().isOk());
    }
}