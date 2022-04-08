package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.stream.Collectors;

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
    @ApiIgnore
    public ProfileDTO getUserInfo() {
        log.debug("getUserInfo()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LawfirmToken userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), lawfirmToken.getUserEmail(), lawfirmToken.getToken(), true);
        log.info("Lawfirm connected {}", userProfile);

        return new ProfileDTO(userProfile.getUserId(), userProfile.getUsername(), userProfile.getUserEmail(), null,
                userProfile.getVcKey(), userProfile.getTemporary(),
                lawfirmToken.getLanguage(),
                userProfile.getSymbolCurrency(),
                userProfile.getUserId(),
                userProfile.getEnumRights().stream().map(EnumRights::getId).collect(Collectors.toList()),
                userProfile.getDriveType(), userProfile.getDropboxToken(), userProfile.isVerified());
    }

    @PostMapping(value = "/language", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ApiIgnore
    public String changeLanguage(@RequestBody String language) {
        log.debug("changeLanguage({})", language);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        userV2Service.changeLanguage(lawfirmToken.getUserId(), language);

        return language;
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<ProfileDTO> getUserSelectedOnlyForTest(@PathVariable String userEmail) {
        if (activeProfile.equalsIgnoreCase("test")) {
            log.debug("getUserSelected({})", userEmail);
            LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            LawfirmToken userProfile = securityGroupService.getUserProfile(lawfirmToken.getClientFrom(), userEmail, lawfirmToken.getToken(), false);
            log.info("Lawfirm connected {}", userProfile);

            return ResponseEntity.ok(new ProfileDTO(userProfile.getUserId(), userProfile.getUsername(), userProfile.getUserEmail(), null,
                    userProfile.getVcKey(), userProfile.getTemporary(),
                    lawfirmToken.getLanguage(),
                    userProfile.getSymbolCurrency(),
                    userProfile.getUserId(),
                    userProfile.getEnumRights().stream().map(EnumRights::getId).collect(Collectors.toList()),
                    userProfile.getDriveType(), userProfile.getDropboxToken(), userProfile.isVerified()));
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

}
