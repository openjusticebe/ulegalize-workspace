package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.dto.NomenclatureConfigDTO;
import com.ulegalize.lawfirm.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class NomenclatureConfigValidator {
    public void validate(NomenclatureConfigDTO nomenclatureConfigDTO) {
        log.debug("Entering validate() with nomenclatureConfigDTO {} ", nomenclatureConfigDTO);

        if (nomenclatureConfigDTO == null) {
            log.warn("NomenclatureConfigDTO is null");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NomenclatureConfigDTO is null");
        }

        if (nomenclatureConfigDTO.getVcNomenclature() == null) {
            log.warn("VcNomenclature NomenclatureConfigDTO is null");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "VcNomenclature NomenclatureConfigDTO is null");
        }

        if (nomenclatureConfigDTO.getDescription() == null || nomenclatureConfigDTO.getDescription().isEmpty()) {
            log.warn("NomenclatureConfigDTO description is null or empty");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NomenclatureConfigDTO description is null or empty");
        }

        if (!Utils.validNomenclature(nomenclatureConfigDTO.getDescription())) {
            log.warn("NomenclatureConfigDTO description format is incorrect {}", nomenclatureConfigDTO.getDescription());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NomenclatureConfigDTO description format is incorrect");
        }

        if (nomenclatureConfigDTO.getParameter() == null || nomenclatureConfigDTO.getParameter().isEmpty()) {
            log.warn("NomenclatureConfigDTO parameter is null or empty");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NomenclatureConfigDTO parameter is null or empty");
        }

        log.debug("Leaving validate()");

    }
}
