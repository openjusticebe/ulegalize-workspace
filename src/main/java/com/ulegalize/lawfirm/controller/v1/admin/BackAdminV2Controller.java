package com.ulegalize.lawfirm.controller.v1.admin;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumRole;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.service.LawfirmService;
import com.ulegalize.lawfirm.service.LawfirmUserService;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/v1/backAdmin")
@Slf4j
public class BackAdminV2Controller {

    private final DossierV2Service dossierV2Service;
    private final LawfirmService lawfirmService;
    private final LawfirmUserService lawfirmUserService;
    private final SecurityGroupService securityGroupService;
    private final UserV2Service userV2Service;

    public BackAdminV2Controller(DossierV2Service dossierV2Service, LawfirmService lawfirmService, LawfirmUserService lawfirmUserService, SecurityGroupService securityGroupService, UserV2Service userV2Service) {
        this.dossierV2Service = dossierV2Service;
        this.lawfirmService = lawfirmService;
        this.lawfirmUserService = lawfirmUserService;
        this.securityGroupService = securityGroupService;
        this.userV2Service = userV2Service;
    }

    @GetMapping(value = "/affaires/vc/{vcKey}/user/{userId}")
    @ApiIgnore
    public List<DossierDTO> getDossierByVckey(@PathVariable String vcKey, @PathVariable Long userId) {
        log.debug("getDossierByVckey({}, {})", vcKey, userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return dossierV2Service.findAllByVCKey(vcKey.toUpperCase(), userId);
    }

    @GetMapping(value = "/affaires/{dossierId}/share/vc/{vcKey}")
    @ApiIgnore
    public List<ShareAffaireDTO> getShareDossierByVckey(@PathVariable Long dossierId, @PathVariable String vcKey) {
        log.debug("getShareDossierByVckey({} , {})", dossierId, vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return dossierV2Service.getSharedUserByAffaireId(dossierId, vcKey.toUpperCase());
    }

    @PostMapping(value = "/affaires/share")
    @ApiIgnore
    public String addShareDossierByVckey(@RequestBody ShareAffaireDTO shareAffaireDTO) {
        log.debug("getShareDossierByVckey({})", shareAffaireDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        dossierV2Service.addShareFolderUser(shareAffaireDTO, false);

        return "ok";
    }

    @GetMapping(value = "/vc")
    @ApiIgnore
    public List<LawfirmDTO> getLawfirmList() {
        log.debug("getLawfirmList()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());
        return lawfirmService.getLawfirmList();
    }

    @PostMapping(value = "/vc/{vcKey}/user/{userId}")
    @ApiIgnore
    public List<LawfirmUserDTO> addNewUserToVCKey(@PathVariable Long userId,
                                                  @PathVariable String vcKey) throws RestException {
        log.debug("addNewUserToVCKey({} , {})", userId, vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());
        TUsers tUsers = userV2Service.findById(userId);

        SecurityGroupUserDTO securityGroupUserDTO = new SecurityGroupUserDTO();
        securityGroupUserDTO.setFunctionId(EnumRole.AVOCAT.getIdRole());

        securityGroupUserDTO.setEmail(tUsers.getEmail());
        securityGroupUserDTO.setUserId(tUsers.getId());
        List<SecurityGroupDTO> securityGroupDTOS = securityGroupService.getSecurityGroup(vcKey);

        // get the first
        securityGroupUserDTO.setSecurityGroupId(securityGroupDTOS.get(0).getId());

        securityGroupService.createUserSecurity(userId, vcKey, securityGroupUserDTO);

        return userV2Service.geLawfirmUserByVcKey(vcKey);
    }

    @GetMapping(value = "/users/vc/{vcKey}")
    @ApiIgnore
    public List<LawyerDTO> geUserByVcKey(@PathVariable String vcKey) throws RestException {
        log.debug("geUserByVcKey({})", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.getLawfirmUsers(vcKey);

    }

    @GetMapping(value = "/lawfirm/users/vc/{vcKey}")
    @ApiIgnore
    public List<LawfirmUserDTO> geLawfirmUserByVcKey(@PathVariable String vcKey) throws RestException {
        log.debug("geUserByVcKey({})", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.geLawfirmUserByVcKey(vcKey);

    }

    @GetMapping(value = "/users")
    @ApiIgnore
    public List<LawyerDTO> geUsers() throws RestException {
        log.debug("geUsers()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.findValid();

    }

}
