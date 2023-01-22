package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.dto.DossierContactDTO;
import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.EnumDossierType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class DossierValidator {

    public void commonRuleDossier(DossierDTO dossierDTO) throws ResponseStatusException {
        log.debug("Entering commonRuleDossier() with dossierDTO {} ", dossierDTO);
        if (dossierDTO.getOpenDossier() == null) {
            log.warn("No open date dossier {}", dossierDTO.getOpenDossier());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No open date dossier");
        }

        if (dossierDTO.getType() == null) {
            log.warn("Type of dossier {} is not found", dossierDTO.getType());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type of dossier is not found");
        }

        if (dossierDTO.getId_matiere_rubrique() == null) {
            log.warn("Id matiere rubrique {} is not found", dossierDTO.getId_matiere_rubrique());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id matiere rubrique is not found");
        }

        if (CollectionUtils.isEmpty(dossierDTO.getDossierContactDTO())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DossierContactDTO list can't be empty");
        }

        int numberOfClientType = 0;

        for (DossierContactDTO dossierContactDTO : dossierDTO.getDossierContactDTO()) {

            if (dossierContactDTO.getClient() != null && dossierContactDTO.getClientType() != null) {
                if (dossierContactDTO.getClientType().getValue().equals(1)) {
                    numberOfClientType++;
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No client type or client in dossierContactDTO");
            }
        }

        if (!dossierDTO.getType().name().equals(EnumDossierType.MD.name())) {
            if (numberOfClientType == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "At least one party type client is needed");
            }
        }

        if (!CollectionUtils.isEmpty(dossierDTO.getTagsList())) {
            for (ItemLongDto itemLongDto : dossierDTO.getTagsList()) {

                if (itemLongDto.getLabel().length() > 50) {
                    log.warn("Label size for {} is too long, should be under 50 characters", itemLongDto.getLabel());
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Label size too long");
                }
            }
        }


        log.debug("Leaving commonRuleDossier()");
    }
}
