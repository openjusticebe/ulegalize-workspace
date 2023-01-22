package com.ulegalize.lawfirm.controller.v1.admin;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumRole;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.Auth0User;
import com.ulegalize.lawfirm.model.dto.MessageDTO;
import com.ulegalize.lawfirm.model.dto.NomenclatureDTO;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.rest.AuthApi;
import com.ulegalize.lawfirm.service.LawfirmService;
import com.ulegalize.lawfirm.service.MessageService;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.service.v2.VirtualcabNomenclatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/backAdmin")
@Slf4j
public class BackAdminV2Controller {

    private final DossierV2Service dossierV2Service;
    private final LawfirmService lawfirmService;

    private final LawfirmV2Service lawfirmV2Service;
    private final SecurityGroupService securityGroupService;
    private final UserV2Service userV2Service;

    private final MessageService messageService;

    private final AuthApi authApi;

    private final VirtualcabNomenclatureService virtualCabNomenclatureService;

    public BackAdminV2Controller(DossierV2Service dossierV2Service, LawfirmService lawfirmService, LawfirmV2Service lawfirmV2Service,
                                 SecurityGroupService securityGroupService, UserV2Service userV2Service, MessageService messageService, AuthApi authApi, VirtualcabNomenclatureService virtualCabNomenclatureService) {
        this.dossierV2Service = dossierV2Service;
        this.lawfirmService = lawfirmService;
        this.lawfirmV2Service = lawfirmV2Service;
        this.securityGroupService = securityGroupService;
        this.userV2Service = userV2Service;
        this.messageService = messageService;
        this.authApi = authApi;
        this.virtualCabNomenclatureService = virtualCabNomenclatureService;
    }

    @GetMapping(value = "/affaires/vc/{vcKey}/user/{userId}")
    public List<DossierDTO> getDossierByVckeyAndUserId(@PathVariable String vcKey, @PathVariable Long userId) {
        log.debug("getDossierByVckeyAndUserId({}, {})", vcKey, userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return dossierV2Service.findAllByVCKeyAndUser(vcKey.toUpperCase(), userId);
    }

    @GetMapping(value = "/affaires/vc/{vcKey}")
    public List<DossierDTO> getDossierByVckey(@PathVariable String vcKey) {
        log.debug("getDossierByVckey({})", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return dossierV2Service.findAllByVCKey(vcKey.toUpperCase());
    }

    @GetMapping(value = "/affaires/{dossierId}/share/vc/{vcKey}")
    public List<ShareAffaireDTO> getShareDossierByVckey(@PathVariable Long dossierId, @PathVariable String vcKey) {
        log.debug("getShareDossierByVckey({} , {})", dossierId, vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return dossierV2Service.getSharedUserByAffaireId(dossierId, vcKey.toUpperCase());
    }

    @PostMapping(value = "/affaires/share")
    public String addShareDossierByVckey(@RequestBody ShareAffaireDTO shareAffaireDTO) {
        log.debug("getShareDossierByVckey({})", shareAffaireDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        if (shareAffaireDTO.getUserId() != null) {
            shareAffaireDTO.setUserIdSelected(new ArrayList<>());
            shareAffaireDTO.getUserIdSelected().add(shareAffaireDTO.getUserId());
        }
        dossierV2Service.addShareFolderUser(shareAffaireDTO, false);

        return "ok";
    }

    @PostMapping(value = "/affaires/nomenclature")
    public String changeNomenclature(@RequestBody NomenclatureDTO nomenclatureDTO) {
        log.debug("changeNomenclature({})", nomenclatureDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        dossierV2Service.changeNomenclature(nomenclatureDTO);

        return "ok";
    }

    @GetMapping(value = "/lawfirm")
    public List<LawfirmDTO> getLawfirmList(@RequestParam(required = false) String searchCriteria) {
        log.debug("getLawfirmList()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());
        return lawfirmService.getLawfirmList(searchCriteria);
    }

    @PostMapping(value = "/vc/{vcKey}/user/{userId}")
    public List<LawfirmUserDTO> addNewUserToVCKey(@PathVariable Long userId,
                                                  @PathVariable String vcKey) {
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
    public List<LawyerDTO> geUserByVcKey(@PathVariable String vcKey) {
        log.debug("geUserByVcKey({})", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.getLawfirmUsers(vcKey);

    }

    @GetMapping(value = "/lawfirm/users/vc/{vcKey}")
    public List<LawfirmUserDTO> geLawfirmUserByVcKey(@PathVariable String vcKey) {
        log.debug("geUserByVcKey({})", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.geLawfirmUserByVcKey(vcKey);

    }

    @GetMapping(value = "/users")
    public List<LawyerDTO> geUsers() {
        log.debug("geUsers()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.findValid();

    }

    @GetMapping(value = "/vc/total")
    public Long getTotalWorkspace() {
        log.debug("getTotalWorkspace()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return lawfirmV2Service.getTotalWorkspace();

    }

    @GetMapping(value = "/user/total")
    public Long getTotalUser() {
        log.debug("getTotalUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.findTotalUser();

    }

    @GetMapping(value = "/user/active/nb")
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).SUPER_ADMIN.name())")
    public ResponseEntity<Integer> getActivelUser() {
        log.debug("getActivelUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        List<Auth0User> activeUsers = authApi.getActiveUsers();
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(activeUsers != null ? activeUsers.size() : 0);
    }

    @GetMapping(value = "/user/active")
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).SUPER_ADMIN.name())")
    public ResponseEntity<List<Auth0User>> getActivelUserList() {
        log.debug("getActivelUserList()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS))
                .body(authApi.getActiveUsers());
    }

    @GetMapping(value = "/user/total/by")
    public Map<String, Long> getTotalUserBy() {
        log.debug("getTotalUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.findTotalUserBy();

    }

    @GetMapping(value = "/user/total/new")
    public Long getNewTotalUserWeek() {
        log.debug("getTotalUser()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());

        return userV2Service.getNewTotalUserWeek();

    }

    @PostMapping(value = "/message")
    public Long createMessage(@RequestBody MessageDTO messageDTO) {
        log.debug("createMessage({})", messageDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected user {}", lawfirmToken.getUsername());
        return messageService.createMessage(messageDTO);
    }

    @GetMapping(value = "/virtualcabNomenclature/vc/{vcKey}")
    public List<ItemLongDto> getVirtualcabNomenclatureListAdmin(@PathVariable String vcKey) {
        log.debug("Entering getVirtualcabNomenclatureListAdmin with vckey {}", vcKey);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return virtualCabNomenclatureService.getAllVirtualcabNomenclatureByVckey(vcKey);
    }

}
