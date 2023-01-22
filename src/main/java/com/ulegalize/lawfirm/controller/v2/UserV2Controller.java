package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v2/user")
@Slf4j
public class UserV2Controller {
    @Autowired
    private SecurityGroupService securityGroupService;
    @Autowired
    private UserV2Service userV2Service;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @GetMapping()
    public ProfileDTO getUserInfo() {
        log.debug("getUserInfo()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProfileDTO userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), true, lawfirmToken.isVerified());
        log.info("Lawfirm connected {}", userProfile);

        return userProfile;
    }

    @PostMapping(value = "/language", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String changeLanguage(@RequestBody String language) {
        log.debug("changeLanguage({})", language);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        userV2Service.changeLanguage(lawfirmToken.getUserId(), language);

        return language;
    }

    @PutMapping(value = "/password", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String changePwd(@RequestBody String newPwd) {
        log.debug("changePwd()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getAuth0UserId());

        if (lawfirmToken.getAuth0UserId().startsWith("auth")) {
            userV2Service.changePwd(lawfirmToken.getAuth0UserId(), newPwd);
        }
        return "ok";
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<ProfileDTO> getUserSelectedOnlyForTest(@PathVariable String userEmail) {
        if (activeProfile.equalsIgnoreCase("test")) {
            log.debug("getUserSelected({})", userEmail);
            LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            ProfileDTO userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), userEmail, false, lawfirmToken.isVerified());
            log.info("Lawfirm connected {}", userProfile);

            return ResponseEntity.ok(userProfile);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized REST");
        }
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<String> deleteUserOnlyForTest(@PathVariable Long userId) {
        if (activeProfile.equalsIgnoreCase("dev")
                || activeProfile.equalsIgnoreCase("test")) {
            log.debug("deleteUser({})", userId);

            userV2Service.deleteUser(userId);

            return ResponseEntity.ok("ok");
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized REST");
        }

    }

    @GetMapping(value = "/lawyer/{userId}")
    public LawyerDTO getUserById(@PathVariable Long userId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering getUserById() with idUser {} and vckey {}", userId, lawfirmToken.getVcKey());

        return userV2Service.getLawfirmUserById(userId);
    }

}
