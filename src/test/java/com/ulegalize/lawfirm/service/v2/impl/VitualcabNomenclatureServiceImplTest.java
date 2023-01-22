package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TNomenclatureConfig;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import com.ulegalize.lawfirm.repository.TNomenclatureConfigRepository;
import com.ulegalize.lawfirm.repository.TVirtualcabNomenclatureRepository;
import com.ulegalize.lawfirm.service.v2.VirtualcabNomenclatureService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class VitualcabNomenclatureServiceImplTest extends EntityTest {

    @Autowired
    private VirtualcabNomenclatureService virtualCabNomenclatureService;
    @Autowired
    private TVirtualcabNomenclatureRepository tVirtualcabNomenclatureRepository;
    private UsernamePasswordAuthenticationToken authentication;
    private LawfirmEntity lawfirm;
    @Autowired
    private TNomenclatureConfigRepository tNomenclatureConfigRepository;

    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void test_A_saveVirtualcabNomenclature() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();

        Long id = virtualCabNomenclatureService.saveVirtualcabNomenclature(virtualcabNomenclatureDTO);
        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByIdAndLawfirmEntity(id, lawfirm);
        assertTrue(virtualcabNomenclatureOptional.isPresent());
    }

    @Test
    void test_B_saveVirtualcabNomenclature_dto_is_null() {
        assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.saveVirtualcabNomenclature(null));
    }

    @Test
    void test_C_getAllVirtualcabNomenclature() {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "nomenclature1");
        createVirtualcabNomenclature(lawfirm, "nomenclature2");
        createVirtualcabNomenclature(lawfirm, "nomenclature3");

        LawfirmEntity lawfirmEntity2 = createLawfirm("TEST1");
        createVirtualcabNomenclature(lawfirmEntity2, "newNomenclature");

        List<ItemLongDto> virtualcabNomenclatureDTOS = virtualCabNomenclatureService.getAllVirtualcabNomenclature();

        assertNotNull(virtualcabNomenclatureDTOS);

        assertEquals(3, virtualcabNomenclatureDTOS.size());
        assertEquals(virtualcabNomenclatureDTOS.get(0).getLabel(), nomenclature1.getName());
    }

    @Test
    void test_D_checkVirtualcabNomenclature() {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "nomenclature1");

        VirtualcabNomenclatureDTO dto = virtualCabNomenclatureService.getVirtualcabNomenclature(nomenclature1.getName());
        assertNotNull(dto);
        assertEquals(dto.getId(), nomenclature1.getId());
    }

    @Test
    void test_E_checkVirtualcabNomenclature() {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        nomenclature1.setName(nomenclature1.getName().toLowerCase());

        VirtualcabNomenclatureDTO dto = virtualCabNomenclatureService.getVirtualcabNomenclature("Nomenclature1");
        assertNull(dto);
    }

    @Test
    void test_F_checkVirtualcabNomenclature() {
        assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.getVirtualcabNomenclature(null));
    }

    @Test
    void test_G_updateVirtualcabNomenclature() {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTOOld = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTOOld.setId(nomenclature1.getId());
        virtualcabNomenclatureDTOOld.setName("New Nomenclature");
        virtualcabNomenclatureDTOOld.setDrivePath("New Drive Path");
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTONew = virtualCabNomenclatureService.updateVirtualcabNomenclature(virtualcabNomenclatureDTOOld);

        assertNotNull(virtualcabNomenclatureDTONew);
        assertEquals(virtualcabNomenclatureDTONew.getId(), virtualcabNomenclatureDTOOld.getId());
        assertEquals(virtualcabNomenclatureDTONew.getName(), virtualcabNomenclatureDTOOld.getName());
        assertEquals(virtualcabNomenclatureDTONew.getDrivePath(), virtualcabNomenclatureDTOOld.getDrivePath());
    }

    @Test
    void test_H_updateVirtualcabNomenclature_mustReturnException() {
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTOOld = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTOOld.setId(nomenclature1.getId());
        virtualcabNomenclatureDTOOld.setName(null);
        virtualcabNomenclatureDTOOld.setDrivePath("New Drive Path");

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.updateVirtualcabNomenclature(virtualcabNomenclatureDTOOld));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO name is null or empty"));
    }

    @Test
    void test_I_updateVirtualcabNomenclature_mustReturnException() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTOOld = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTOOld.setId(5L);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.updateVirtualcabNomenclature(virtualcabNomenclatureDTOOld));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclature does not exist"));
    }

    @Test
    void test_J_deleteVirtualcabNomenclature() {
        TVirtualcabNomenclature nomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        createVirtualcabNomenclature(lawfirm, "Nomenclature2");
        virtualCabNomenclatureService.deleteVirtualcabNomenclature(nomenclature.getId());
        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findById(nomenclature.getId());
        assertTrue(virtualcabNomenclatureOptional.isEmpty());
    }

    // TODO
    @Test
    void test_K_deleteVirtualcabNomenclature_with_nomenclatureConfig() {
        TVirtualcabNomenclature nomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        createVirtualcabNomenclature(lawfirm, "Nomenclature2");
        TNomenclatureConfig nomenclatureConfig1 = createNomenclatureConfig(nomenclature, "NomenclatureConfig1");
        TNomenclatureConfig nomenclatureConfig2 = createNomenclatureConfig(nomenclature, "NomenclatureConfig2");
        nomenclature.addTNomenclatureConfig(nomenclatureConfig1);
        nomenclature.addTNomenclatureConfig(nomenclatureConfig2);
        testEntityManager.persist(nomenclature);

        virtualCabNomenclatureService.deleteVirtualcabNomenclature(nomenclature.getId());

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findById(nomenclature.getId());

        Optional<TNomenclatureConfig> nomenclatureConfigOptional1 = tNomenclatureConfigRepository.findById(nomenclatureConfig1.getId());
        Optional<TNomenclatureConfig> nomenclatureConfigOptional2 = tNomenclatureConfigRepository.findById(nomenclatureConfig2.getId());

        assertTrue(virtualcabNomenclatureOptional.isEmpty());
        assertTrue(nomenclatureConfigOptional1.isEmpty());
        assertTrue(nomenclatureConfigOptional2.isEmpty());
    }

    @Test
    void test_L_getAllVirtualcabNomenclatureWithPagination() {
        createVirtualcabNomenclature(lawfirm, "nomenclature1");
        createVirtualcabNomenclature(lawfirm, "nomenclature2");
        createVirtualcabNomenclature(lawfirm, "nomenclature3");
        createVirtualcabNomenclature(lawfirm, "nomenclature4");
        createVirtualcabNomenclature(lawfirm, "nomenclature5");
        createVirtualcabNomenclature(lawfirm, "nomenclature6");

        LawfirmEntity lawfirmEntity2 = createLawfirm("TEST1");
        createVirtualcabNomenclature(lawfirmEntity2, "newNomenclature");

        Page<VirtualcabNomenclatureDTO> nomenclatureWithPagination = virtualCabNomenclatureService.getAllVirtualcabNomenclatureWithPagination(5, 0);
        assertNotNull(nomenclatureWithPagination.get());
        assertEquals(nomenclatureWithPagination.getTotalElements(), 6);
        assertEquals(nomenclatureWithPagination.getContent().get(0).getName(), "nomenclature1");
    }

    @Test
    void test_M_deleteVirtualcabNomenclature_withException() {
        TVirtualcabNomenclature nomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.deleteVirtualcabNomenclature(nomenclature.getId()));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Virtualcab Nomenclature cannot be deleted as it is the latest"));
    }

    @Test
    void getAllVirtualcabNomenclatureByVckey() {
        LawfirmEntity lawfirmEntityToFind = createLawfirm("NEWVCKEY");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntityToFind, "new Nomenclature 1");
        createVirtualcabNomenclature(lawfirmEntityToFind, "new Nomenclature 2");

        List<ItemLongDto> nomenclatures = virtualCabNomenclatureService.getAllVirtualcabNomenclatureByVckey(lawfirmEntityToFind.getVckey());
        assertNotNull(nomenclatures);
        assertEquals(nomenclatures.size(), 2);
        assertEquals(nomenclatures.get(0).label, virtualcabNomenclature.getName());

    }

    @Test
    void getAllVirtualcabNomenclatureByVckey_with_vcKey_is_null_must_return_exception() {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.getAllVirtualcabNomenclatureByVckey(null));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("vcKey to find is null"));
    }

    @Test
    void getAllVirtualcabNomenclatureByVckey_with_vcKey_is_empty_must_return_exception() {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.getAllVirtualcabNomenclatureByVckey(""));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("vcKey to find is null"));
    }

    @Test
    void getAllVirtualcabNomenclatureByVckey_with_lawfirm_error_must_return_exception() {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualCabNomenclatureService.getAllVirtualcabNomenclatureByVckey("VCKEYNOTEXIST"));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("vcKey to find doesn't exist"));
    }

    @Test
    void getAllVirtualcabNomenclatureByVckey_with_empty_list() {
        LawfirmEntity lawfirmEntityToFind = createLawfirm("NEWVCKEY");

        List<ItemLongDto> nomenclatures = virtualCabNomenclatureService.getAllVirtualcabNomenclatureByVckey(lawfirmEntityToFind.getVckey());
        assertNotNull(nomenclatures);
        assertEquals(nomenclatures.size(), 0);
    }
}