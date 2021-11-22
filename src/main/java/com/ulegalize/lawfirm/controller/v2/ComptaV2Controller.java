package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.InvoiceDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.ComptaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@ApiIgnore
@RequestMapping("/v2/compta")
@Slf4j
public class ComptaV2Controller {
    @Autowired
    private ComptaService comptaService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).COMPTABILITE_LECTURE.name())")
    public ComptaDTO getComptaById(@RequestParam Long fraisId) {
        log.debug("getComptaById({})", fraisId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return comptaService.getComptaById(fraisId, lawfirmToken.getVcKey());
    }

    @GetMapping("/default")
    public ComptaDTO getDefaultCompta() {
        log.debug("getDefaultCompta()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return comptaService.getDefaultCompta(lawfirmToken.getUserId(), lawfirmToken.getVcKey());
    }

    @GetMapping("/tva/default")
    public ItemBigDecimalDto getTvaDefaultCompta() {
        log.debug("getDefaultCompta()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return comptaService.getTvaDefaultCompta(lawfirmToken.getUserId(), lawfirmToken.getVcKey());
    }

    @GetMapping(value = "/grids")
    public List<ItemDto> getGrids() {
        log.debug("getGrids()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return comptaService.getGridList();
    }


    @GetMapping(value = "/list")
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).COMPTABILITE_LECTURE.name())")
    public Page<ComptaDTO> getCompta(@RequestParam int offset, @RequestParam int limit,
                                     @RequestParam(required = false) Long dossierId,
                                     @RequestParam(required = false) Boolean isDebours,
                                     @RequestParam(required = false) Boolean isFraiCollaboration,
                                     @RequestParam(required = false) Boolean honoraire,
                                     @RequestParam(required = false) Boolean tiers,
                                     @RequestParam(required = false) String searchCriteriaClient,
                                     @RequestParam(required = false) String searchCriteriaYear,
                                     @RequestParam(required = false) Long searchCriteriaNumber,
                                     @RequestParam(required = false) String searchCriteriaPoste
    ) {
        log.debug("getCompta(offset: {} , limit {} and dossierId {} , debours {}, frais collaboration {}", offset, limit, dossierId, isDebours, isFraiCollaboration);
        log.debug("getCompta(offset: {} , limit {} and dossierId {} , debours {}, frais collaboration {}, honoraire {}, tiers {})", offset, limit, dossierId, isDebours, isFraiCollaboration, honoraire, tiers);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        Page<ComptaDTO> allCompta;

        if (dossierId != null && (isFraiCollaboration != null || isDebours != null || honoraire != null || tiers != null)) {
            allCompta = comptaService.getAllComptaByDossierId(limit, offset, dossierId, lawfirmToken.getVcKey(), isDebours, isFraiCollaboration, honoraire, tiers);
        } else {
            allCompta = comptaService.getAllCompta(limit, offset, lawfirmToken.getVcKey(), searchCriteriaClient, searchCriteriaYear, searchCriteriaNumber, searchCriteriaPoste);
        }

        return allCompta;

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).COMPTABILITE_ECRITURE.name())")
    public ComptaDTO updateCompta(@RequestBody ComptaDTO comptaDTO) {
        log.debug("updateCompta({})", comptaDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return comptaService.updateCompta(comptaDTO, lawfirmToken.getVcKey());

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).COMPTABILITE_ECRITURE.name())")
    public ComptaDTO createCompta(@RequestBody ComptaDTO comptaDTO) {
        log.debug("updateCompta({})", comptaDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Long comptaId = comptaService.createCompta(comptaDTO, lawfirmToken.getVcKey());
        return comptaService.getComptaById(comptaId, lawfirmToken.getVcKey());
    }


    @GetMapping(value = "/dossier/{dossierId}/total")
    public ResponseEntity<InvoiceDTO> totalHonoraireByDossierId(@PathVariable Long dossierId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("totalHonoraireByDossierId for vckey {} and dossierId {}", lawfirmToken.getVcKey(), dossierId);

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        InvoiceDTO invoiceDTO = comptaService.totalHonoraireByDossierId(dossierId);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        return responseBuilder
                .body(invoiceDTO);

    }
}
