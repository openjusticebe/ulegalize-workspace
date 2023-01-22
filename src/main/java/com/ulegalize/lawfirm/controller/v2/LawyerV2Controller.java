package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.v2.LawyerV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v2/lawyer")
@Slf4j
public class LawyerV2Controller {
    @Autowired
    private LawyerV2Service lawyerV2Service;

    @GetMapping(value = "")
    public LawyerDTO getLawyer() {
        log.debug("GET /v2/lawyer getLawyer()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("2.8.5 {}", lawfirmToken);

        return lawyerV2Service.getLawyer();
    }

    @PutMapping(value = "")
    public Long updateLawyer(@RequestBody LawyerDTO lawyerDTO) {
        log.debug("PUT /v2/lawyer updateLawyer({})", lawyerDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawyerV2Service.updateLawyer(lawyerDTO);
    }

    @PostMapping("/logo")
    public Long uploadLogo(@RequestParam("files") MultipartFile file) throws IOException {
        log.debug("Entering uploadLogo()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("uploadLogo by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return lawyerV2Service.updateLawyerLogo(file.getBytes());
    }
}
