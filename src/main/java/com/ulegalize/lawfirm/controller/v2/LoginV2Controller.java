package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.lawfirm.kafka.producer.transparency.ICaseProducer;
import com.ulegalize.lawfirm.model.DefaultLawfirmDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.SlackApi;
import com.ulegalize.lawfirm.service.EmailService;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.utils.DriveUtils;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/login")
@Slf4j
public class LoginV2Controller {
    @Autowired
    private DriveFactory driveFactory;
    @Autowired
    private LawfirmV2Service lawfirmV2Service;
    @Autowired
    private SecurityGroupService securityGroupService;
    @Autowired
    private SlackApi slackApi;
    @Autowired
    private ICaseProducer caseProducer;
    @Autowired
    private UserV2Service userV2Service;
    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/user")
    @ApiIgnore
    public String registerUser() {
        log.debug("registerUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());
        try {
            LawfirmToken userProfile = securityGroupService.getSimpleUserProfile(lawfirmToken.getUserEmail(), lawfirmToken.getToken(), lawfirmToken.isVerified());

            log.debug("registerUser with KNOWN user profile({})", userProfile);

            // this means the user is known
            return EnumValid.VERIFIED.name();
        } catch (ResponseStatusException rse) {
            log.debug("registerUser with UNKNOWN user profile({})", lawfirmToken.getUserEmail());

            return lawfirmV2Service.registerUser(lawfirmToken.getUserEmail(), lawfirmToken.getClientFrom());
        } finally {
            slackApi.sendSensitiveNotification("A new user '" + lawfirmToken.getClientFrom() + "' create his lawfirm. :ok_hand: ", lawfirmToken.getUserEmail(), EnumSlackUrl.NEW_ARRIVAL);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ProfileDTO> getUser() {
        log.debug("getUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LawfirmToken userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), lawfirmToken.getToken(), true, lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        return ResponseEntity.ok()
                .body(new ProfileDTO(userProfile.getUserId(), userProfile.getFullname(), userProfile.getUserEmail(), null, userProfile.getVcKey(),
                        userProfile.getTemporary(), lawfirmToken.getLanguage(), userProfile.getSymbolCurrency(),
                        userProfile.getUserId(),
                        userProfile.getEnumRights().stream().map(EnumRights::getId).collect(Collectors.toList()),
                        userProfile.getDriveType(), userProfile.getDropboxToken(), userProfile.isVerified()));

    }

    @GetMapping(value = "/user/api")
    public ResponseEntity<ProfileDTO> getApiUser() {
        log.debug("getUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LawfirmToken userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), lawfirmToken.getToken(), true, lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS))
                .body(new ProfileDTO(userProfile.getUserId(), userProfile.getFullname(), userProfile.getUserEmail(), null, userProfile.getVcKey(),
                        userProfile.getTemporary(), lawfirmToken.getLanguage(), userProfile.getSymbolCurrency(),
                        userProfile.getUserId(),
                        userProfile.getEnumRights().stream().map(EnumRights::getId).collect(Collectors.toList()),
                        userProfile.getDriveType(), userProfile.getDropboxToken(), userProfile.isVerified()));

    }

    @GetMapping(value = "/light/user")
    public ProfileDTO getSimpleUser() {
        log.debug("getSimpleUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LawfirmToken userProfile = securityGroupService.getSimpleUserProfile(lawfirmToken.getUserEmail(), lawfirmToken.getToken(), lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        return new ProfileDTO(userProfile.getUserId(), userProfile.getFullname(), userProfile.getUserEmail(), null, userProfile.getVcKey(),
                userProfile.getTemporary(), lawfirmToken.getLanguage(), userProfile.getSymbolCurrency(),
                userProfile.getUserId(),
                userProfile.getEnumRights().stream().map(EnumRights::getId).collect(Collectors.toList()),
                userProfile.getDriveType(), userProfile.getDropboxToken(), userProfile.isVerified());
    }

    @PostMapping(value = "/validate/user")
    public ProfileDTO validateSignup(@RequestBody DefaultLawfirmDTO defaultLawfirmDTO) {
        log.debug("validateSignup({})", defaultLawfirmDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LawfirmToken userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), lawfirmToken.getToken(), true, lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        // check if the new cab is different than the old
        ProfileDTO profileDTO = lawfirmV2Service.updateTempVcKey(userProfile, defaultLawfirmDTO);

        // transparence
        try {
            caseProducer.createLawfirmMessage(lawfirmToken, defaultLawfirmDTO.getVcKey(), lawfirmToken.getUserEmail(), lawfirmToken.getLanguage(), profileDTO.getUserId());

        } catch (Exception e) {
            log.error("Error while calling transparency {}", defaultLawfirmDTO.getVcKey(), e);
            slackApi.sendSensitiveNotification("ResponseStatusException:LoginV2Controller->validateSignup transparency issue", defaultLawfirmDTO.getVcKey(), EnumSlackUrl.SENSITIVE);
        }
        try {
            // drive
            List<String> paths = List.of(DriveUtils.DOSSIER_PATH, DriveUtils.POSTIN_PATH, DriveUtils.INVOICE_PATH, DriveUtils.TEMPLATE_PATH);
            IDriveProducer driveApi = driveFactory.getDriveProducer(lawfirmToken.getDriveType());
            driveApi.createContainer(lawfirmToken, defaultLawfirmDTO.getVcKey(), paths);
        } catch (Exception e) {
            log.error("Error while calling drive {}", defaultLawfirmDTO.getVcKey(), e);
            slackApi.sendSensitiveNotification("ResponseStatusException:LoginV2Controller->validateSignup  createContainer tDrive issue", defaultLawfirmDTO.getVcKey(), EnumSlackUrl.SENSITIVE);
        }
        try {
            emailService.registeredUser(defaultLawfirmDTO.getVcKey(), userProfile);
        } catch (RuntimeException e) {
            log.error("Error while calling emails {}", defaultLawfirmDTO.getVcKey(), e);
            slackApi.sendSensitiveNotification("ResponseStatusException:LoginV2Controller->validateSignup  registeredUser email issue", defaultLawfirmDTO.getVcKey(), EnumSlackUrl.SENSITIVE);
        }

        return profileDTO;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/verifyUser")
    public Boolean verifyUser(@RequestParam("email") String email, @RequestParam("key") String hashkey) {
        log.debug("verifyUser()");

        return userV2Service.verifyUser(email, hashkey);
    }
}
