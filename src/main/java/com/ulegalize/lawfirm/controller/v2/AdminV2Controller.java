package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.*;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@ApiIgnore
@RequestMapping("/v2/admin")
@Slf4j
public class AdminV2Controller {
    @Autowired
    private LawfirmV2Service lawfirmV2Service;
    @Autowired
    private LawfirmConfigV2Service lawfirmConfigV2Service;
    @Autowired
    private LawfirmVatV2Service lawfirmVatV2Service;
    @Autowired
    private PrestationTypeService prestationTypeService;
    @Autowired
    private DeboursTypeService deboursTypeService;
    @Autowired
    private RefPosteService refPosteService;
    @Autowired
    private RefCompteService refCompteService;
    @Autowired
    private TemplateV2Service templateV2Service;
    @Autowired
    private SecurityGroupService securityGroupService;
    @Autowired
    private DossierV2Service dossierV2Service;

    // VirtualCab
    @GetMapping(value = "/lawfirm")
    public LawfirmDTO getLawfirmInfo() {
        log.debug("getLawfirmInfo()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmV2Service.getLawfirmInfoByVcKey(lawfirmToken.getVcKey());
    }

    @PutMapping(value = "/lawfirm")
    public LawfirmDTO updateLawfirmInfo(@RequestBody LawfirmDTO lawfirmDTO) {
        log.debug("updateLawfirmInfo({})", lawfirmDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmV2Service.updateLawfirmInfoByVcKey(lawfirmDTO);
    }

    @PostMapping("/lawfirm/logo")
    public String uploadImageVirtualcab(@RequestParam("files") MultipartFile file) throws IOException {
        log.debug("Entering uploadLogo()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("uploadLogo by (vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return lawfirmV2Service.uploadImageVirtualcab(file.getBytes());
    }

    // VirtualCabConfig
    @GetMapping(value = "/lawfirmConfig")
    public List<LawfirmConfigDTO> getLawfirmConfigInfo() {
        log.debug("getLawfirmConfigInfo()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        List<LawfirmConfigDTO> listDTO = lawfirmConfigV2Service.getLawfirmConfigInfoByVcKey(lawfirmToken.getVcKey());
        return listDTO;
    }

    @PutMapping(value = "/lawfirmConfig")
    public void addLawfirmConfigInfo(@RequestBody LawfirmConfigDTO lawfirmConfigDTO) {
        log.debug("addLawfirmConfigInfo()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.debug(lawfirmConfigDTO.getDescription() + " " + lawfirmToken.getVcKey() + " "
                + lawfirmConfigDTO.getParameter());
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        lawfirmConfigV2Service.addLawfirmConfigByVcKey(lawfirmConfigDTO, lawfirmToken.getVcKey());
    }

    @DeleteMapping(path = "/lawfirmConfigDelete/{lawfirmConfigDescription}")
    public void removeLawfirmConfigInfo(@PathVariable String lawfirmConfigDescription) {
        log.debug("(Backend) removeLawfirmConfigInfo() {}", lawfirmConfigDescription);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        lawfirmConfigV2Service.removeLawfirmConfig(lawfirmConfigDescription, lawfirmToken.getVcKey());
    }

    // Prestation
    @GetMapping(value = "/prestation/type")
    public List<PrestationTypeDTO> getPrestationsType() {
        log.debug("getPrestationsType()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return prestationTypeService.getAllPrestationsType(lawfirmToken.getVcKey(), lawfirmToken.getUserId());
    }

    @PutMapping(value = "/prestation/type/{prestationTypeId}")
    public PrestationTypeDTO updatePrestationsType(@PathVariable Integer prestationTypeId,
            @RequestBody PrestationTypeDTO prestationTypeDTO) {
        log.debug("updatePrestationsType({})", prestationTypeId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return prestationTypeService.updatePrestationsType(lawfirmToken.getVcKey(), lawfirmToken.getUserId(),
                prestationTypeId, prestationTypeDTO);
    }

    @PostMapping(value = "/prestation/type")
    public PrestationTypeDTO createPrestationsType(@RequestBody PrestationTypeDTO prestationTypeDTO) {
        log.debug("createPrestationsType({})", prestationTypeDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Integer prestationsType = prestationTypeService.createPrestationsType(lawfirmToken.getVcKey(),
                lawfirmToken.getUserId(), prestationTypeDTO);

        return prestationTypeService.getPrestationsTypeById(lawfirmToken.getVcKey(), lawfirmToken.getUserId(),
                prestationsType);
    }

    @DeleteMapping(value = "/prestation/type/{prestationTypeId}")
    public Integer deletePrestationsType(@PathVariable Integer prestationTypeId) {
        log.debug("deletePrestationsType({})", prestationTypeId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return prestationTypeService.deletePrestationsType(lawfirmToken.getVcKey(), lawfirmToken.getUserId(),
                prestationTypeId);
    }

    @GetMapping(value = "/frais/type")
    public List<FraisAdminDTO> getDeboursType() {
        log.debug("getDeboursType()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return deboursTypeService.getAllDeboursType(lawfirmToken.getVcKey(), lawfirmToken.getUserId());
    }

    @PutMapping(value = "/frais/type/{deboursTypeId}")
    public FraisAdminDTO updateDeboursType(@PathVariable Long deboursTypeId, @RequestBody FraisAdminDTO fraisAdminDTO) {
        log.debug("updateDeboursType({})", deboursTypeId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return deboursTypeService.updateDeboursType(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), deboursTypeId,
                fraisAdminDTO);
    }

    @PostMapping(value = "/frais/type")
    public FraisAdminDTO createDeboursType(@RequestBody FraisAdminDTO fraisAdminDTO) {
        log.debug("createDeboursType({})", fraisAdminDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Long deboursTypeId = deboursTypeService.createDeboursType(lawfirmToken.getVcKey(), lawfirmToken.getUserId(),
                fraisAdminDTO);

        return deboursTypeService.getDeboursTypeById(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), deboursTypeId);
    }

    @DeleteMapping(value = "/frais/type/{deboursTypeId}")
    public Long deleteDeboursType(@PathVariable Long deboursTypeId) {
        log.debug("deleteDeboursType({})", deboursTypeId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return deboursTypeService.deleteDeboursType(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), deboursTypeId);
    }

    @GetMapping(value = "/accounting/type")
    public List<AccountingTypeDTO> getAccountingType() {
        log.debug("getAccountingType()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return refPosteService.getAllRefPoste(lawfirmToken.getVcKey(), lawfirmToken.getUserId());
    }

    @PutMapping(value = "/accounting/type/{accountingTypeId}")
    public AccountingTypeDTO updateRefPoste(@PathVariable Integer accountingTypeId,
            @RequestBody AccountingTypeDTO accountingTypeDTO) {
        log.debug("updateRefPoste({})", accountingTypeId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return refPosteService.updateRefPoste(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), accountingTypeId,
                accountingTypeDTO);
    }

    @PostMapping(value = "/accounting/type")
    public AccountingTypeDTO createRefPoste(@RequestBody AccountingTypeDTO accountingTypeDTO) {
        log.debug("createRefPoste({})", accountingTypeDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Integer accountingTypeId = refPosteService.createRefPoste(lawfirmToken.getVcKey(), lawfirmToken.getUserId(),
                accountingTypeDTO);

        return refPosteService.getRefPosteById(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), accountingTypeId);
    }

    @DeleteMapping(value = "/accounting/type/{accountingTypeId}")
    public Integer deleteRefPoste(@PathVariable Integer accountingTypeId) {
        log.debug("deleteRefPoste({})", accountingTypeId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return refPosteService.deleteRefPoste(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), accountingTypeId);
    }

    @GetMapping(value = "/bankaccount/type")
    public List<BankAccountDTO> getBankAccount() {
        log.debug("getBankAccount()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return refCompteService.getAllBankAccount(lawfirmToken.getVcKey(), lawfirmToken.getUserId());
    }

    @PutMapping(value = "/bankaccount/type/{compteId}")
    public BankAccountDTO updateBankAccount(@PathVariable Integer compteId,
            @RequestBody BankAccountDTO bankAccountDTO) {
        log.debug("updateBankAccount({})", compteId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return refCompteService.updateBankAccount(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), compteId,
                bankAccountDTO);
    }

    @PostMapping(value = "/bankaccount/type")
    public BankAccountDTO createBankAccount(@RequestBody BankAccountDTO accountingTypeDTO) {
        log.debug("createBankAccount({})", accountingTypeDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Integer compteId = refCompteService.createBankAccount(lawfirmToken.getVcKey(), lawfirmToken.getUserId(),
                accountingTypeDTO);

        return refCompteService.getBankAccountById(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), compteId);
    }

    @DeleteMapping(value = "/bankaccount/type/{compteId}")
    public Integer deleteBankAccount(@PathVariable Integer compteId) {
        log.debug("deleteBankAccount({})", compteId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return refCompteService.deleteBankAccount(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), compteId);
    }

    @GetMapping(path = "/security/users/full")
    public List<LawyerDTO> getFullUserResponsableList() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.debug("getFullUserResponsableList( for vckey {}", lawfirmToken.getVcKey());

        return securityGroupService.getFullUserResponsableList(lawfirmToken.getVcKey());
    }

    @GetMapping(path = "/security/securityGroup")
    public List<SecurityGroupDTO> getSecurityGroup() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.debug("getSecurityGroup() for language {}", lawfirmToken.getLanguage());

        return securityGroupService.getSecurityGroup();
    }

    @GetMapping(path = "/security/securityGroup/exists")
    public Boolean getSecurityGroupByName(@RequestParam String securityGroupName) {
        log.debug("getSecurityGroup() by description {}", securityGroupName);

        return securityGroupService.existSecurityGroupByName(securityGroupName);
    }

    @DeleteMapping(path = "/security/securityUserGroup/{userId}")
    public Long deleteSecurityUserGroupByUser(@PathVariable Long userId) {
        log.debug("deleteSecurityUserGroupByUser() for userId {}", userId);

        return securityGroupService.deleteSecurityUsersGroup(userId);
    }

    @DeleteMapping(path = "/security/securityGroup/{securityGroupId}")
    public Long deleteSecurityGroup(@PathVariable Long securityGroupId) {
        log.debug("deleteSecurityGroup() for securityGroupId {}", securityGroupId);

        return securityGroupService.deleteSecurityGroup(securityGroupId);
    }

    @DeleteMapping(path = "/security/{securityGroupUserId}/user")
    public Long deleteSecurityUserGroup(@PathVariable Long securityGroupUserId) {
        log.debug("deleteSecurityGroup() for securityGroupUserId {}", securityGroupUserId);

        return securityGroupService.deleteSecurityGroupById(securityGroupUserId);
    }

    @PostMapping(value = "/security/securityGroup")
    public Long createSecurityGroup(@RequestBody String newName) {
        log.debug("createSecurityGroup({})", newName);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return securityGroupService.createSecurityGroup(lawfirmToken.getUserId(), newName);
    }

    @PostMapping(value = "/security/securityUserGroup")
    public Integer createUserSecurity(@RequestBody SecurityGroupUserDTO securityGroupUserDTO) throws RestException {
        log.debug("createUserSecurity({})", securityGroupUserDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return securityGroupService.createUserSecurity(lawfirmToken.getUserId(), securityGroupUserDTO);
    }

    @PostMapping(value = "/security/{securityGroupId}/user")
    public Long addUserSecurity(@PathVariable Long securityGroupId, @RequestBody String userId) {
        log.debug("addUserSecurity({}, {})", securityGroupId, userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return securityGroupService.addUserSecurity(securityGroupId, Long.valueOf(userId));
    }

    @GetMapping(path = "/security/securityUserGroup/{securityGroupId}")
    public List<SecurityGroupUserDTO> getSecurityUserGroupBySecurityGroupId(@PathVariable Long securityGroupId) {
        log.debug("getSecurityUserGroupBySecurityGroupId() by securityGroupId {}", securityGroupId);

        return securityGroupService.getSecurityUserGroupBySecurityGroupId(securityGroupId);
    }

    @GetMapping(path = "/security/securityUserGroup/out/{securityGroupId}")
    public List<LawyerDTO> getOutSecurityUserGroupBySecurityGroupId(@PathVariable Long securityGroupId) {
        log.debug("getSecurityUserGroupBySecurityGroupId() by securityGroupId {}", securityGroupId);

        return securityGroupService.getOutSecurityUserGroupBySecurityGroupId(securityGroupId);
    }

    @GetMapping(path = "/security/securityRightGroup/{securityGroupId}")
    public List<ItemLongDto> getSecurityRightGroupBySecurityGroupId(@PathVariable Long securityGroupId) {
        log.debug("getSecurityRightGroupBySecurityGroupId() by securityGroupId {}", securityGroupId);

        return securityGroupService.getSecurityRightGroupBySecurityGroupId(securityGroupId);
    }

    @GetMapping(path = "/security/securityRightGroup/out/{securityGroupId}")
    public List<ItemDto> getOutSecurityRightGroupBySecurityGroupId(@PathVariable Long securityGroupId) {
        log.debug("getOutSecurityRightGroupBySecurityGroupId() by securityGroupId {}", securityGroupId);

        return securityGroupService.getOutSecurityRightGroupBySecurityGroupId(securityGroupId);
    }

    @PostMapping(value = "/security/{securityGroupId}/right")
    public Long addRightSecurity(@PathVariable Long securityGroupId, @RequestBody String rightId) {
        log.debug("addRightSecurity({}, {})", securityGroupId, rightId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return securityGroupService.addRightSecurity(securityGroupId, Integer.valueOf(rightId));
    }

    @DeleteMapping(path = "/security/{securityGroupRightId}/right")
    public Long deleteSecurityRightGroup(@PathVariable Long securityGroupRightId) {
        log.debug("deleteSecurityRightGroup() for securityGroupRightId {}", securityGroupRightId);

        return securityGroupService.deleteSecurityGroupRightById(securityGroupRightId);
    }

    @PostMapping(value = "/vat")
    public Long createVat(@RequestBody String newVat) {
        log.debug("createVat({})", newVat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmVatV2Service.createVat(new BigDecimal(newVat));
    }

    @DeleteMapping(value = "/vat/{vat}")
    public Long deleteVat(@PathVariable BigDecimal vat) {
        log.debug("deleteVat({})", vat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmVatV2Service.deleteVat(vat);
    }

    @PutMapping(value = "/vat/{vat}")
    public Long changeDefaultVat(@PathVariable BigDecimal vat) {
        log.debug("changeDefaultVat({})", vat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return lawfirmVatV2Service.changeDefaultVat(vat);
    }

    @GetMapping(path = "/vat/exist/{vat}")
    public Boolean existVirtualCabVatByVat(@PathVariable BigDecimal vat) {
        log.debug("existVirtualCabVatByVat({})", vat);

        return lawfirmVatV2Service.existVirtualCabVatByVat(vat);
    }

    @GetMapping(path = "/vat/nb")
    public Long countVirtualCabVatByVcKey() {
        log.debug("countVirtualCabVatByVcKey()");

        return lawfirmVatV2Service.countVirtualCabVatByVcKey();
    }

    @GetMapping(path = "/models")
    public List<ModelDTO> getModelsList() {
        log.debug("getModelsList()");

        return templateV2Service.getModelsList();
    }

    @PutMapping(path = "/models")
    public Long updateModels(@RequestBody ModelDTO modelDTO) {
        log.debug("updateModels({})", modelDTO);

        return templateV2Service.updateModels(modelDTO);
    }

    @PostMapping(path = "/models")
    public Long createModels(@RequestBody ModelDTO modelDTO) {
        log.debug("createModels({})", modelDTO);

        return templateV2Service.createModels(modelDTO);
    }

    @DeleteMapping(path = "/models/{modelsId}")
    public Long deleteModels(@PathVariable Long modelsId) {
        log.debug("deleteModels({})", modelsId);

        return templateV2Service.deleteModels(modelsId);
    }

    @GetMapping(path = "/templateData/{dossierId}")
    public JSONObject getTemplatDataByDossier(@PathVariable Long dossierId) {
        log.debug("getTemplatDataByDossier({})", dossierId);

        return templateV2Service.getTemplatDataByDossier(dossierId);
    }

    @GetMapping(path = "/templateData")
    public JSONObject getTemplatData() {
        log.debug("getTemplatData()");

        return templateV2Service.getTemplatData();
    }

    @PostMapping(value = "/users/share/global")
    @ApiIgnore
    public String adminAddShareUser() throws RestException {
        log.debug("adminAddShareUser()");

        return dossierV2Service.addShareAllFolderUser();
    }
}
