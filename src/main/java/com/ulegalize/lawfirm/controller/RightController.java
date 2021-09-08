package com.ulegalize.lawfirm.controller;

import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.AffaireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("/v1/right")
@Slf4j
public class RightController {
    @Autowired
    private AffaireService affaireService;

    @GetMapping(value = "/affaire/{id}")
    public Boolean getRightByAffaireId(@PathVariable Long id) throws LawfirmBusinessException {
        log.debug("getRightByAffaireId(id: {}", id);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return affaireService.hasRightAffaire(lawfirmToken.getVcKey(), id);

    }
}
