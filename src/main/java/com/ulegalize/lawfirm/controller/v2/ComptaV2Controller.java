package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.ComptaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public List<ComptaDTO> getCompta(@RequestParam int offset, @RequestParam int limit,
                                     @RequestParam(required = false) Long dossierId,
                                     @RequestParam(required = false) Boolean isDebours,
                                     @RequestParam(required = false) Boolean isFraiCollaboration,
                                     @RequestParam(required = false) Boolean honoraire,
                                     @RequestParam(required = false) Boolean tiers) {
        log.debug("getCompta(offset: {} , limit {} and dossierId {} , debours {}, frais collaboration {}", offset, limit, dossierId, isDebours, isFraiCollaboration);
        log.debug("getCompta(offset: {} , limit {} and dossierId {} , debours {}, frais collaboration {}, honoraire {}, tiers {})", offset, limit, dossierId, isDebours, isFraiCollaboration, honoraire, tiers);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        List<ComptaDTO> allCompta;

        if (dossierId != null && (isFraiCollaboration != null || isDebours != null || honoraire != null || tiers != null)) {
            allCompta = comptaService.getAllComptaByDossierId(limit, offset, dossierId, lawfirmToken.getVcKey(), isDebours, isFraiCollaboration, honoraire, tiers);
        } else {
            allCompta = comptaService.getAllCompta(limit, offset, lawfirmToken.getVcKey());
        }

        return allCompta;

    }

    @GetMapping(value = "/count")
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).COMPTABILITE_LECTURE.name())")
    public Long countFraisAdmin(@RequestParam(required = false) Long dossierId,
                                @RequestParam(required = false) Boolean isDebours,
                                @RequestParam(required = false) Boolean isFraiCollaboration,
                                @RequestParam(required = false) Boolean honoraire,
                                @RequestParam(required = false) Boolean tiers
    ) {
        log.debug("countFraisAdmin({}, debours {}, frais collaboration {}, honoraire {}, tiers {})", dossierId, isDebours, isFraiCollaboration, honoraire, tiers);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        if (dossierId != null && (isFraiCollaboration != null || isDebours != null || honoraire != null || tiers != null)) {
            return comptaService.countAllComptaByVcKey(dossierId, lawfirmToken.getVcKey(), isDebours, isFraiCollaboration, honoraire, tiers);
        } else {
            return comptaService.countAllCompta(lawfirmToken.getVcKey());
        }

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
}
