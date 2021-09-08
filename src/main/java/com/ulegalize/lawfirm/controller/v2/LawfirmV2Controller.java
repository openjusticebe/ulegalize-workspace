package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.lawfirm.kafka.producer.transparency.ICaseProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.SlackApi;
import com.ulegalize.lawfirm.service.LawfirmUserService;
import com.ulegalize.lawfirm.service.LawfirmWebsiteService;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.utils.DriveUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v2/lawfirm")
@Slf4j
public class LawfirmV2Controller {
    @Autowired
    private LawfirmUserService lawfirmUserService;

    @Autowired
    private LawfirmWebsiteService lawfirmWebsiteService;

    @Autowired
    private LawfirmV2Service lawfirmV2Service;

    @Autowired
    private DriveFactory driveFactory;
    @Autowired
    private ICaseProducer caseProducer;

    @Autowired
    private SlackApi slackApi;

    @PostMapping(value = "/switch")
    public String switchLawfirm(@RequestBody String vcKeySelected) {
        log.debug("switchLawfirm({})", vcKeySelected);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmUserService.switchLawfirm(lawfirmToken.getUserId(), vcKeySelected);
    }

    @GetMapping(value = "/users")
    @ApiIgnore
    public List<LawfirmUserDTO> getLawfirmUsers() {
        log.debug("getLawfirmUsers()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmUserService.getLawfirmUsers(lawfirmToken.getVcKey());
    }

    @PutMapping(value = "/user/{userId}/public")
    @ApiIgnore
    public LawfirmUserDTO updateIsPublicLawfirmUser(@PathVariable Long userId, @RequestBody String isPublic) {
        log.debug("updateIsActiveLawfirmUser with id {}", userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmUserService.updateIsPublicLawfirmUser(lawfirmToken.getVcKey(), userId, isPublic);
    }

    @PutMapping(value = "/user/{userId}/active")
    @ApiIgnore
    public LawfirmUserDTO updateIsActiveLawfirmUser(@PathVariable Long userId, @RequestBody String isActive) {
        log.debug("updateIsActiveLawfirmUser with id {}", userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmUserService.updateIsActiveLawfirmUser(lawfirmToken.getVcKey(), userId, isActive);
    }


    @GetMapping(value = "/users/list")
    public ResponseEntity<List<LawfirmDTO>> getLawfirmList(@RequestParam(required = false) Long userId) {
        log.debug("getLawfirmList()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (userId != null && userId.equals(lawfirmToken.getUserId())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(lawfirmUserService.getLawfirmsByUserId(lawfirmToken.getUserId()));
    }

    @PutMapping(value = "/users/role")
    @ApiIgnore
    public LawyerDTO updateRoleLawfirmUser(@RequestBody LawyerDTO lawyerDTO) {
        log.debug("updateRoleLawfirmUser({})", lawyerDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmUserService.updateRoleLawfirmUser(lawyerDTO);
    }

    @GetMapping(value = "/users/{userId}")
    @ApiIgnore
    public SecurityGroupUserDTO getLawfirmUserByUserId(@PathVariable Long userId) {
        log.debug("getLawfirmUserByUserId({})", userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmUserService.getLawfirmUserByUserId(userId);
    }

    @GetMapping
    @ApiIgnore
    public LawfirmDTO getLawfirmByName(@RequestParam String name) {
        log.debug("getLawfirmList({})", name);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmV2Service.getLawfirmInfoByVcKey(name);
    }

    @GetMapping(value = "/search")
    @ApiIgnore
    public List<LawfirmDTO> searchLawfirmByName(@RequestParam String name) {
        log.debug("searchLawfirmByName({})", name);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmV2Service.searchLawfirmInfoByVcKey(name);
    }


    @PostMapping
    @ApiIgnore
    public ProfileDTO createLawfirm(@RequestBody String newVcKey, @RequestParam String countryCode) {
        log.debug("createLawfirm({})", newVcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

        lawfirmV2Service.createSingleVcKey(lawfirmToken.getUserEmail(), newVcKey, lawfirmToken.getClientFrom(), true, enumLanguage, countryCode);

        ProfileDTO profileDTO = lawfirmV2Service.validateVc(newVcKey, lawfirmToken.getUserId(), lawfirmToken.getUserEmail());

        // transparence
        try {
            caseProducer.createLawfirmMessage(lawfirmToken, newVcKey, lawfirmToken.getUserEmail(), lawfirmToken.getLanguage(), lawfirmToken.getUserId());

        } catch (Exception e) {
            log.error("Error while calling transparency {}", newVcKey, e);
            slackApi.sendSensitiveNotification("ResponseStatusException:LawfirmV2Controller->createLawfirm transparency issue", newVcKey, EnumSlackUrl.SENSITIVE);
        }
        try {
            // drive
            List<String> paths = List.of(DriveUtils.DOSSIER_PATH, DriveUtils.POSTIN_PATH, DriveUtils.INVOICE_PATH, DriveUtils.TEMPLATE_PATH);
            IDriveProducer driveApi = driveFactory.getDriveProducer(lawfirmToken.getDriveType());
            driveApi.createContainer(lawfirmToken, newVcKey, paths);
        } catch (Exception e) {
            log.error("Error while calling drive {}", newVcKey, e);
            slackApi.sendSensitiveNotification("ResponseStatusException:LawfirmV2Controller->createLawfirm createContainer Drive issue", newVcKey, EnumSlackUrl.SENSITIVE);
        }

        return profileDTO;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/website")
    @ApiIgnore
    public LawfirmWebsiteDTO getLawfirmWebsites() {
        log.debug("getLawfirmWebsite()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmWebsiteService.getLawfirmWebsites(lawfirmToken.getVcKey());
    }

    @PutMapping(value = "/website")
    @ApiIgnore
    public LawfirmWebsiteDTO updateLawfirmWebsite(@RequestBody LawfirmWebsiteDTO lawfirmWebsiteDTO) {
        log.debug("updateLawfirmWebsite()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmWebsiteService.updateLawfirmWebsite(lawfirmToken.getVcKey(), lawfirmWebsiteDTO);
    }


    @PutMapping(value = "/updateToken")
    @ApiIgnore
    public LawfirmDriveDTO updateToken(@RequestBody LawfirmDriveDTO lawfirmDriveDTO) {
        log.debug("Entering updateToken {}", lawfirmDriveDTO);
        return lawfirmV2Service.updateToken(lawfirmDriveDTO);
    }

}
