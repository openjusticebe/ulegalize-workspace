package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.service.v2.VirtualcabNomenclatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/vcnomenclature")
@Slf4j
public class VirtualcabNomenclatureController {

    @Autowired
    private VirtualcabNomenclatureService virtualCabNomenclatureService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long saveVirtualcabNomenclature(@RequestBody VirtualcabNomenclatureDTO virtualcabNomenclatureDTO) {
        log.debug("saveVirtualcabNomenclature({})", virtualcabNomenclatureDTO);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.saveVirtualcabNomenclature(virtualcabNomenclatureDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public VirtualcabNomenclatureDTO updateVirtualcabNomenclature(@RequestBody VirtualcabNomenclatureDTO virtualcabNomenclatureDTO) {
        log.debug("updateVirtualcabNomenclature({})", virtualcabNomenclatureDTO);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.updateVirtualcabNomenclature(virtualcabNomenclatureDTO);
    }

    @DeleteMapping(value = "/{idVcNomenclature}")
    public Long deleteVirtualcabNomenclature(@PathVariable Long idVcNomenclature) {
        log.debug("deleteVirtualcabNomenclature({})", idVcNomenclature);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.deleteVirtualcabNomenclature(idVcNomenclature);
    }

    @GetMapping(value = "/list")
    public List<ItemLongDto> getVirtualcabNomenclatureList() {
        log.debug("Entering getVirtualcabNomenclatureList");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.getAllVirtualcabNomenclature();
    }

    @GetMapping
    public VirtualcabNomenclatureDTO getVirtualcabNomenclature(@RequestParam String vcNomenclatureName) {
        log.debug("Entering getVirtualcabNomenclature");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.getVirtualcabNomenclature(vcNomenclatureName);
    }

    @GetMapping(value = "{id}")
    public VirtualcabNomenclatureDTO getVirtualcabNomenclatureById(@PathVariable Long id) {
        log.debug("Entering getVirtualcabNomenclatureById");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.getVirtualcabNomenclatureById(id);
    }

    @GetMapping(value = "/vcnomenclatures")
    public Page<VirtualcabNomenclatureDTO> getVirtualcabNomenclatureWithPagination(
            @RequestParam int offset,
            @RequestParam int limit
    ) {
        log.debug("getVirtualcabNomenclatureWithPagination() with offset {} and limit {}", offset, limit);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.getAllVirtualcabNomenclatureWithPagination(limit, offset);
    }
}
