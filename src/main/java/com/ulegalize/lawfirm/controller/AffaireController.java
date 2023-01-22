package com.ulegalize.lawfirm.controller;

import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.UpdateShareResponse;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.service.AffaireService;
import com.ulegalize.lawfirm.service.FraisAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/affaire")
@Slf4j
public class AffaireController {

    @Autowired
    AffaireService affaireService;
    @Autowired
    FraisAdminService fraisAdminService;
    @Autowired
    EntityToDossierConverter entityToDossierConverter;

    //TODO replace the explicit rest pathparam by a security/jwt param
    @RequestMapping(method = RequestMethod.POST, path = "/share", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UpdateShareResponse updateShare(@RequestBody UpdateShareRequestDTO updateShareRequest) throws RestException {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("updateShare(updateShare: {})", updateShareRequest);

        return affaireService.updateShare(lawfirmToken, updateShareRequest);
    }

    //TODO replace the explicit rest pathparam by a security/jwt param
    @RequestMapping(method = RequestMethod.GET, path = "/{dossierId}")
    public DossierDTO getDossierById(@PathVariable Long dossierId) throws LawfirmBusinessException {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getDossierById(dossierId: {}) for vckey {}", dossierId, lawfirmToken.getVcKey());

        return affaireService.getDossierById(dossierId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{dossierId}/fraisadmin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FraisAdminDTO> createFraisAdmin(@PathVariable Long dossierId, @RequestBody FraisAdminDTO fraisAdminDTO) throws LawfirmBusinessException {
        log.debug("createFraisAdmin(fraisAdminDTO: {}", fraisAdminDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        FraisAdminDTO fraisAdmin = fraisAdminService.createFraisAdmin(fraisAdminDTO, lawfirmToken.getUserId());

        return ResponseEntity.ok(fraisAdmin);

    }

    @RequestMapping(method = RequestMethod.POST, path = "/{dossierId}/digital", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DossierDTO> updateDossierIsDigital(@PathVariable Long dossierId, @RequestBody DossierDTO dossierSummary) {
        log.debug("updateDossierIsDigital(dossierSummary: {}", dossierSummary);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        affaireService.switchDossierDigital(dossierSummary, lawfirmToken.getUserId(), lawfirmToken.getVcKey());

        return ResponseEntity.ok(dossierSummary);

    }


}
