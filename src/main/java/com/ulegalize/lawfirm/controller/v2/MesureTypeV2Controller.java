package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.enumeration.EnumMesureType;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/mesureType")
@Slf4j
public class MesureTypeV2Controller {

    @GetMapping
    public List<ItemDto> getAllMesureType() {
        log.debug("getAllMesureType()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

        return Arrays.stream(EnumMesureType.values())
                .map(enumMesureType -> new ItemDto(enumMesureType.getId(), Utils.getLabel(enumLanguage,
                        enumMesureType.name(), com.ulegalize.lawfirm.utils.Utils.PACKAGENAME)))
                .collect(Collectors.toList());
    }
}
