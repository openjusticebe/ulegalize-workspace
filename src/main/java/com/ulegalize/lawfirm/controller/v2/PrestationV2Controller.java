package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.PrestationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v2/prestations")
@Slf4j
public class PrestationV2Controller {
    @Autowired
    private PrestationService prestationService;

    @GetMapping
    public ResponseEntity<List<PrestationSummary>> getPrestations(@RequestParam int offset,
                                                                  @RequestParam int limit,
                                                                  @RequestParam(required = false) Long dossierId,
                                                                  @RequestParam(required = false) String vcKey) throws LawfirmBusinessException {
        log.debug("getPrestations(offset: {} , limit {} and dossier id {}", offset, limit, dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        List<PrestationSummary> prestationSummaryList;

        if (dossierId != null) {
            prestationSummaryList = prestationService.getAllPrestationsByDossierId(limit, offset, dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey());
        } else {
            prestationSummaryList = prestationService.getAllPrestations(limit, offset, lawfirmToken.getUserId(), lawfirmToken.getVcKey());
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(prestationSummaryList);
    }

    @GetMapping(value = "/default")
    @ApiIgnore
    public ResponseEntity<PrestationSummary> defaultPrestations() {
        log.debug("defaultPrestations()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return ResponseEntity
                .ok()
                .body(prestationService.getDefaultPrestations(lawfirmToken.getUserId(), lawfirmToken.getVcKey()));
    }

    @GetMapping(value = "/{prestationId}")
    @ApiIgnore
    public ResponseEntity<PrestationSummary> getPrestationById(@PathVariable Long prestationId) {
        log.debug("defaultPrestations()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(prestationService.getPrestationById(lawfirmToken.getUserId(), lawfirmToken.getVcKey(), prestationId));
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Long> countPrestation(@RequestParam Long dossierId, @RequestParam(required = false) String vcKey) {
        log.debug("countPrestation({})", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(prestationService.countAllPrestationByVcKey(dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey()));

    }

    @DeleteMapping(value = "/{prestationId}")
    @ApiIgnore
    public Long deletePrestation(@PathVariable Long prestationId) {
        log.debug("defaultPrestations()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return prestationService.deletePrestation(lawfirmToken.getUserId(), lawfirmToken.getVcKey(), prestationId);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiIgnore
    public Long savePrestation(@RequestBody PrestationSummary prestationSummary) {
        log.debug("savePrestation({})", prestationSummary);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return prestationService.savePrestation(prestationSummary);
    }

}
