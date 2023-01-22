package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.utils.Utils;
import com.ulegalize.lawfirm.utils.VirtualcabNomenclatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class VirtualcabNomenclatureValidator {
    public void validate(VirtualcabNomenclatureDTO virtualcabNomenclatureDTO) {
        log.debug("Entering validate() with VirtualcabNomenclatureDTO {} ", virtualcabNomenclatureDTO);

        if (virtualcabNomenclatureDTO == null) {
            log.warn("VirtualcabNomenclatureDTO is null");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "VirtualcabNomenclatureDTO is null");
        }

        if (virtualcabNomenclatureDTO.getName() == null || virtualcabNomenclatureDTO.getName().isEmpty()) {
            log.warn("VirtualcabNomenclatureDTO name is null or empty");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "VirtualcabNomenclatureDTO name is null or empty");
        }

        if (!Utils.validNomenclature(virtualcabNomenclatureDTO.getName())) {
            log.warn("Nomenclature format is incorrect {}", virtualcabNomenclatureDTO.getName());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomenclature format is incorrect");
        }

        if (virtualcabNomenclatureDTO.getName().length() > 100) {
            log.warn("Nomenclature name must have less than 100 characters");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomenclature name must have less than 100 characters");
        }

        if (virtualcabNomenclatureDTO.getDrivePath() == null || virtualcabNomenclatureDTO.getDrivePath().isEmpty()) {
            log.warn("VirtualcabNomenclatureDTO DrivePath is null or empty");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "VirtualcabNomenclatureDTO DrivePath is null or empty");
        }

        if (virtualcabNomenclatureDTO.getDrivePath().length() > 255) {
            log.warn("VirtualcabNomenclatureDTO DrivePath must have less than 255 characters");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "VirtualcabNomenclatureDTO DrivePath must have less than 255 characters");
        }

        String drivePath = virtualcabNomenclatureDTO.getDrivePath().replaceAll("\\" + VirtualcabNomenclatureUtils.VIRTUALNOMENCLATUREYEAR, "");
        drivePath = drivePath.replaceAll("\\" + VirtualcabNomenclatureUtils.VIRTUALNOMENCLATURENUM, "");
        drivePath = drivePath.replaceAll("\\" + VirtualcabNomenclatureUtils.VIRTUALNOMENCLATURENOMENCLATURE, "");

        if (drivePath.contains("{") || drivePath.contains("}")) {
            log.warn("Nomenclature drivePath format is incorrect {}", virtualcabNomenclatureDTO.getDrivePath());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomenclature drivePath format is incorrect");
        }

        if (virtualcabNomenclatureDTO.getDrivePath().length() > 255) {
            log.warn("VirtualcabNomenclatureDTO DrivePath is null or empty");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "VirtualcabNomenclatureDTO DrivePath is null or empty");
        }
        log.debug("Leaving validate()");
    }
}
