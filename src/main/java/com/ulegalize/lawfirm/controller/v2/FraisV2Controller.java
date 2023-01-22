package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.FraisAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/frais")
@Slf4j
public class FraisV2Controller {
    @Autowired
    private FraisAdminService fraisAdminService;

    @GetMapping
    public List<FraisAdminDTO> getFraisAdmin(@RequestParam int offset, @RequestParam int limit, @RequestParam(required = false) Long dossierId) {
        log.debug("getFraisAdmin(offset: {} , limit {} and dossierId {}", offset, limit, dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        List<FraisAdminDTO> allFrais;

        if (dossierId != null) {
            allFrais = fraisAdminService.getAllFraisByDossierId(limit, offset, dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey());
        } else {
            allFrais = fraisAdminService.getAllFrais(limit, offset, lawfirmToken.getUserId(), lawfirmToken.getVcKey());
        }

        return allFrais;

    }

    @GetMapping(value = "/count")
    public Long countFraisAdmin(@RequestParam Long dossierId) {
        log.debug("countFraisAdmin({})", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return fraisAdminService.countAllFraisByVcKey(dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey());

    }

    @GetMapping(value = "/matieres/{idDebourType}")
    public FraisAdminDTO getFraisMatiere(@PathVariable Long idDebourType) {
        log.debug("getFraisMatiere({})", idDebourType);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return fraisAdminService.getFraisMatiere(idDebourType);
    }

    @GetMapping(value = "/default")
    public FraisAdminDTO defaultFrais() {
        log.debug("defaultFrais()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return fraisAdminService.getDefaultFrais(lawfirmToken.getUserId(), lawfirmToken.getVcKey());
    }

    @GetMapping(value = "/{fraisAdminId}")
    public FraisAdminDTO getFraisById(@PathVariable Long fraisAdminId) {
        log.debug("getFraisById({})", fraisAdminId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return fraisAdminService.getFraisById(lawfirmToken.getUserId(), lawfirmToken.getVcKey(), fraisAdminId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long saveFrais(@RequestBody FraisAdminDTO fraisAdminDTO) {
        log.debug("saveFrais({})", fraisAdminDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return fraisAdminService.saveFrais(fraisAdminDTO);
    }

    @DeleteMapping(value = "/{fraisAdminId}")
    public Long deleteFrais(@PathVariable Long fraisAdminId) {
        log.debug("deleteFrais({})", fraisAdminId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return fraisAdminService.deleteFrais(lawfirmToken.getUserId(), lawfirmToken.getVcKey(), fraisAdminId);

    }

}
