package com.ulegalize.lawfirm.controller.v2.dpa;

import com.ulegalize.enumeration.EnumValid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dpa")
@Slf4j
public class DPAV2Controller {

    @GetMapping(value = "/callback")
    public String callbackDPa(@RequestParam String code) {
        log.debug("callbackDPa()");

        log.info("code connected {}", code);

        // this means the user is known
        return EnumValid.VERIFIED.name();
    }


}
