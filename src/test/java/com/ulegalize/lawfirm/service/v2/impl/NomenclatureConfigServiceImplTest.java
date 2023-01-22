package com.ulegalize.lawfirm.service.v2.impl;

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
import com.ulegalize.lawfirm.service.v2.NomenclatureConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class NomenclatureConfigServiceImplTest extends EntityTest {

    @Autowired
    TNomenclatureConfigRepository nomenclatureConfigRepository;
    @Autowired
    private NomenclatureConfigService nomenclatureConfigService;
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

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void test_A_getNomenclatureConfigInfoByVcNomenclature() {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        createNomenclatureConfig(virtualcabNomenclature, "Config1");
        createNomenclatureConfig(virtualcabNomenclature, "Config2");
        createNomenclatureConfig(virtualcabNomenclature, "Config3");

        List<NomenclatureConfigDTO> dtos = nomenclatureConfigService.getNomenclatureConfigInfoByVcNomenclature(virtualcabNomenclature.getId(), lawfirm.getVckey());
        assertNotNull(dtos);
        assertEquals(3, dtos.size());
        assertEquals(dtos.get(0).getVcNomenclature(), virtualcabNomenclature.getId());
    }

    @Test
    void test_B_getNomenclatureConfigInfoByVcNomenclature() {
        LawfirmEntity lawfirmEntity2 = createLawfirm("Test2");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        TVirtualcabNomenclature virtualcabNomenclature2 = createVirtualcabNomenclature(lawfirmEntity2, "Nomenclature2");
        createNomenclatureConfig(virtualcabNomenclature, "Config1");
        createNomenclatureConfig(virtualcabNomenclature, "Config2");
        createNomenclatureConfig(virtualcabNomenclature, "Config3");
        createNomenclatureConfig(virtualcabNomenclature2, "ConfigOther");

        List<NomenclatureConfigDTO> dtos = nomenclatureConfigService.getNomenclatureConfigInfoByVcNomenclature(virtualcabNomenclature.getId(), lawfirm.getVckey());
        assertNotNull(dtos);
        assertEquals(3, dtos.size());
        assertEquals(dtos.get(0).getVcNomenclature(), virtualcabNomenclature.getId());
    }

    @Test
    void test_C_addNomenclatureConfigByVcNomenclature() {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);

        nomenclatureConfigService.addNomenclatureConfigByVcNomenclature(nomenclatureConfigDTO);

        Optional<TNomenclatureConfig> nomenclatureConfigOptional = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfigDTO.getDescription());

        assertFalse(nomenclatureConfigOptional.isEmpty());
        assertEquals(nomenclatureConfigOptional.get().getLabel(), nomenclatureConfigDTO.getDescription());
    }

    @Test
    void test_D_addNomenclatureConfigByVcNomenclature_with_exception_validator() {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setDescription("");

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigService.addNomenclatureConfigByVcNomenclature(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO description is null or empty"));

        Optional<TNomenclatureConfig> nomenclatureConfigOptional = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfigDTO.getDescription());

        assertTrue(nomenclatureConfigOptional.isEmpty());
    }

    @Test
    void test_E_removeNomenclatureConfig() {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        TNomenclatureConfig nomenclatureConfig = createNomenclatureConfig(virtualcabNomenclature, "nomenclatureConfig");

        Optional<TNomenclatureConfig> nomenclatureConfigOptional = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfig.getLabel());
        assertTrue(nomenclatureConfigOptional.isPresent());

        nomenclatureConfigService.removeNomenclatureConfig(nomenclatureConfig.getLabel(), virtualcabNomenclature.getId());
        Optional<TNomenclatureConfig> nomenclatureConfigOptional2 = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclature, nomenclatureConfig.getLabel());
        assertTrue(nomenclatureConfigOptional2.isEmpty());
    }

    @Test
    void test_F_getNomenclatureConfig() {
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        TNomenclatureConfig nomenclatureConfig = createNomenclatureConfig(virtualcabNomenclature, "nomenclatureConfig");

        NomenclatureConfigDTO nomenclatureConfigDTO = nomenclatureConfigService.getNomenclatureConfig(nomenclatureConfig.getVcNomenclature().getId(), nomenclatureConfig.getLabel());

        assertNotNull(nomenclatureConfigDTO);
        assertEquals(nomenclatureConfig.getVcNomenclature().getId(), nomenclatureConfigDTO.getVcNomenclature());
        assertEquals(nomenclatureConfig.getLabel(), nomenclatureConfigDTO.getDescription());

    }
}