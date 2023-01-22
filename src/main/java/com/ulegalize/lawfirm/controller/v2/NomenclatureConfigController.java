package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.NomenclatureConfigDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.service.v2.NomenclatureConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/nomenclatureConfig")
@Slf4j
public class NomenclatureConfigController {

    @Autowired
    private LawfirmV2Service lawfirmV2Service;
    @Autowired
    private NomenclatureConfigService nomenclatureConfigService;

    @GetMapping(value = "/{vcNomenclatureId}")
    public List<NomenclatureConfigDTO> getNomenclatureConfigInfo(@PathVariable Long vcNomenclatureId,
                                                                 @RequestParam(required = false) String vcKey) {
        log.debug("getLawfirmConfigInfo() with vcNomenclatureId {}", vcNomenclatureId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        if (vcKey != null && !vcKey.isEmpty()) {
            return nomenclatureConfigService.getNomenclatureConfigInfoByVcNomenclature(vcNomenclatureId, vcKey);
        } else {
            return nomenclatureConfigService.getNomenclatureConfigInfoByVcNomenclature(vcNomenclatureId, lawfirmToken.getVcKey());
        }
    }

    @GetMapping
    public NomenclatureConfigDTO getNomenclatureConfig(@RequestParam Long vcNomenclatureId, @RequestParam String nomenclatureConfig) {
        log.debug("getNomenclatureConfig()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return nomenclatureConfigService.getNomenclatureConfig(vcNomenclatureId, nomenclatureConfig);
    }

    @PostMapping
    public Long addNomenclatureConfig(@RequestBody NomenclatureConfigDTO nomenclatureConfigDTO) {
        log.debug("addNomenclatureConfig()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.debug(nomenclatureConfigDTO.getDescription() + " " + lawfirmToken.getVcKey() + " "
                + nomenclatureConfigDTO.getParameter());
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return nomenclatureConfigService.addNomenclatureConfigByVcNomenclature(nomenclatureConfigDTO);
    }

    @DeleteMapping(path = "/{vcNomenclatureId}/{nomenclatureConfigLabel}")
    public void removeNomenclatureConfig(@PathVariable Long vcNomenclatureId, @PathVariable String nomenclatureConfigLabel) {
        log.debug("(Backend) removeNomenclatureConfig() {}", nomenclatureConfigLabel);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        nomenclatureConfigService.removeNomenclatureConfig(nomenclatureConfigLabel, vcNomenclatureId);
    }

}
