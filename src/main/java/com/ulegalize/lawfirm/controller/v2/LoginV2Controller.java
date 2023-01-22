package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.lawfirm.kafka.producer.transparency.ICaseProducer;
import com.ulegalize.lawfirm.model.DefaultLawfirmDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import com.ulegalize.lawfirm.rest.AuthApi;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.rest.v2.SlackApi;
import com.ulegalize.lawfirm.service.EmailService;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.service.v2.cache.CacheService;
import com.ulegalize.lawfirm.service.v2.cache.CacheUtils;
import com.ulegalize.lawfirm.utils.DriveUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v2/login")
@Slf4j
@AllArgsConstructor
public class LoginV2Controller {
    private final DriveFactory driveFactory;
    private final LawfirmV2Service lawfirmV2Service;
    private final SecurityGroupService securityGroupService;
    private final SlackApi slackApi;
    private final ICaseProducer caseProducer;
    private final UserV2Service userV2Service;
    private final EmailService emailService;
    private final AuthApi authApi;
    private final CacheService cacheService;


    @PostMapping(value = "/user")
    public String registerUser() {
        log.debug("registerUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String tempVcKey = null;
        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());
        try {
            ProfileDTO userProfile = securityGroupService.getProfileForRegistry(lawfirmToken.getUserEmail(), lawfirmToken.isVerified());

            tempVcKey = userProfile.getVcKeySelected();
            log.debug("registerUser with KNOWN user profile({})", userProfile);

            // this means the user is known
            return EnumValid.VERIFIED.name();
        } catch (ResponseStatusException rse) {
            log.debug("registerUser with UNKNOWN user profile({})", lawfirmToken.getUserEmail());

            tempVcKey = lawfirmV2Service.registerUser(lawfirmToken.getUserEmail(), lawfirmToken.getClientFrom(), lawfirmToken.isVerified());

            slackApi.sendSensitiveNotification("A new user '" + lawfirmToken.getClientFrom() + "' create his lawfirm. :ok_hand: ", lawfirmToken.getUserEmail(), EnumSlackUrl.NEW_ARRIVAL);

            return EnumValid.UNVERIFIED.name();
        } finally {
            cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

            authApi.updateAppMetaData(lawfirmToken.getAuth0UserId(), tempVcKey);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ProfileDTO> getUser() throws ExecutionException, InterruptedException {
        log.debug("getUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProfileDTO userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), true, lawfirmToken.isVerified());

        log.info("Lawfirm connected {}", userProfile);

        return ResponseEntity.ok()
                .body(userProfile);

    }

    @GetMapping(value = "/user/api")
    public ResponseEntity<ProfileDTO> getApiUser() {
        log.debug("getUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProfileDTO userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), true, lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS))
                .body(userProfile);

    }

    @GetMapping(value = "/light/user")
    public ProfileDTO getSimpleUser() {
        log.debug("getSimpleUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProfileDTO userProfile = securityGroupService.getSimpleUserProfile(lawfirmToken.getUserEmail(), lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        return userProfile;
    }

    @PostMapping(value = "/validate/user")
    public ProfileDTO validateSignup(@RequestBody DefaultLawfirmDTO defaultLawfirmDTO) {
        log.debug("validateSignup({})", defaultLawfirmDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProfileDTO userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), true, lawfirmToken.isVerified());
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
        } finally {
            authApi.updateAppMetaData(lawfirmToken.getAuth0UserId(), defaultLawfirmDTO.getVcKey());
        }

        return profileDTO;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/verifyUser")
    public Boolean verifyUser(@RequestParam("email") String email, @RequestParam("key") String hashkey) {
        log.debug("verifyUser()");

        return userV2Service.verifyUser(email, hashkey);
    }
}
