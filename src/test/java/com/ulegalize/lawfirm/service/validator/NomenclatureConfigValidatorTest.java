package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.dto.NomenclatureConfigDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class NomenclatureConfigValidatorTest extends EntityTest {

    @Autowired
    private NomenclatureConfigValidator nomenclatureConfigValidator;

    @Test
    void test_A_validate_dto_is_null() {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(null));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO is null"));
    }

    @Test
    void test_B_validate_vcNomenclature_is_null() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setVcNomenclature(null);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VcNomenclature NomenclatureConfigDTO is null"));
    }

    @Test
    void test_C_validate_label_is_null() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setDescription(null);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO description is null or empty"));
    }

    @Test
    void test_D_validate_label_is_Empty() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setDescription("");

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO description is null or empty"));
    }

    @Test
    void test_E_validate_parameter_is_null() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setParameter(null);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO parameter is null or empty"));
    }

    @Test
    void test_F_validate_parameter_is_Empty() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setParameter("");

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO parameter is null or empty"));
    }

    @Test
    void test_G_validate_does_not_throw() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);

        assertDoesNotThrow(() -> nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
    }

    @Test
    void test_H_validate_descriptionFormat_incorrect() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        NomenclatureConfigDTO nomenclatureConfigDTO = createNomenclatureConfigDto(virtualcabNomenclature);
        nomenclatureConfigDTO.setDescription("/test");

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                nomenclatureConfigValidator.validate(nomenclatureConfigDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("NomenclatureConfigDTO description format is incorrect"));
    }
}