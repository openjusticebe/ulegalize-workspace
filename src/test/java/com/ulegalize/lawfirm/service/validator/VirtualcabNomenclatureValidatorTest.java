package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.utils.VirtualcabNomenclatureUtils;
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
class VirtualcabNomenclatureValidatorTest extends EntityTest {

    @Autowired
    private VirtualcabNomenclatureValidator virtualcabNomenclatureValidator;

    @Test
    void test_A_validate_dto_is_null() {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(null));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO is null"));
    }

    @Test
    void test_B_validate_nameDto_is_null() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setName(null);
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO name is null or empty"));
    }

    @Test
    void test_C_validate_nameDto_is_empty() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setName("");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO name is null or empty"));
    }

    @Test
    void test_D_validate_drivePath_is_null() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setDrivePath(null);
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO DrivePath is null or empty"));
    }

    @Test
    void test_D_validate_drivePath_is_empty() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setDrivePath("");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO DrivePath is null or empty"));
    }

    @Test
    void test_E_validate_nameFormat_incorrect() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setName("Nomenclature /");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Nomenclature format is incorrect"));
    }

    @Test
    void test_F_validate_nameFormat_incorrect() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setName("Nomenclature ;");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Nomenclature format is incorrect"));
    }

    @Test
    void test_G_validate_nameFormat_with_more_100chars() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setName("Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature " +
                "Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature " +
                "Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature " +
                "Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature " +
                "Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature Nomenclature ");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Nomenclature name must have less than 100 characters"));
    }

    @Test
    void test_H_validate_drivePathFormat_with_more_255chars() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setDrivePath("drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath " +
                "drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath drivePath ");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("VirtualcabNomenclatureDTO DrivePath must have less than 255 characters"));
    }

    @Test
    void test_I_validate_drivePathFormat_with_erreur() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setDrivePath(VirtualcabNomenclatureUtils.VIRTUALNOMENCLATUREYEAR + "/{num}/" + VirtualcabNomenclatureUtils.VIRTUALNOMENCLATURENOMENCLATURE + "/test/{work}");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Nomenclature drivePath format is incorrect"));
    }

    @Test
    void test_J_validate_drivePathFormat_with_erreur() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setDrivePath("test/{work}");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Nomenclature drivePath format is incorrect"));
    }

    @Test
    void test_K_validate_drivePathFormat_with_erreur() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setDrivePath("test/{work}/{num}");
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Nomenclature drivePath format is incorrect"));
    }

    @Test
    void test_K_validate_without_error() {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = createVirtualcabNomenclatureDTO();

        assertDoesNotThrow(() -> virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO));
    }
}