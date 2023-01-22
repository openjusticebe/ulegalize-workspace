package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.v2.VirtualcabTagsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/vctags")
@Slf4j
public class VirtualcabTagsController {

    @Autowired
    private VirtualcabTagsService virtualcabTagsService;

    @GetMapping(value = "/list")
    public List<ItemLongDto> getVirtualcabTagsList(@RequestParam(required = false) String searchCriteria) {
        log.debug("Entering getVirtualcabTagsList");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualcabTagsService.getTagsByVckey(searchCriteria);
    }
}
