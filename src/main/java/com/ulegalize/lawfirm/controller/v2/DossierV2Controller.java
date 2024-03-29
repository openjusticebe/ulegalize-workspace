package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.dto.CaseDTO;
import com.ulegalize.lawfirm.service.FraisAdminService;
import com.ulegalize.lawfirm.service.LawfirmUserService;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v2/affaires")
@Slf4j
public class DossierV2Controller {

    @Autowired
    LawfirmUserService lawfirmUserService;
    @Autowired
    DossierV2Service dossierV2Service;
    @Autowired
    FraisAdminService fraisAdminService;
    @Autowired
    EntityToDossierConverter entityToDossierConverter;

    @GetMapping(value = "/finance/{dossierId}")
    public ResponseEntity<FinanceDTO> getFinanceDossierById(@PathVariable Long dossierId,
                                                            @RequestParam(required = false) String vcKey) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getFinanceDossierById(dossierId: {}) for vckey {}", dossierId, lawfirmToken.getVcKey());

        return ResponseEntity.ok()
                .body(dossierV2Service.getFinanceDossierById(dossierId));
    }

    @GetMapping(value = "/{dossierId}")
    public ResponseEntity<DossierDTO> getDossierById(@PathVariable Long dossierId,
                                                     @RequestParam(required = false) String vcKey) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getDossierById(dossierId: {}) for vckey {}", dossierId, lawfirmToken.getVcKey());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equals(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(20, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(dossierV2Service.getDossierById(dossierId));
    }

    @GetMapping(value = "/default")
    public DossierDTO getDefaultDossier() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getDossier by default for vckey {}", lawfirmToken.getVcKey());


        return dossierV2Service.getDefaultDossier(lawfirmToken.getVcKey(), lawfirmToken.getUserId());
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Page<DossierDTO>> getAffaires(@RequestParam int offset, @RequestParam int limit,
                                                        @RequestParam(required = false) String vcKey,
                                                        @RequestParam(required = false) String searchCriteriaClient,
                                                        @RequestParam(required = false) String searchCriteriaYear,
                                                        @RequestParam(required = false) String searchCriteriaNomenclature,
                                                        @RequestParam(required = false) String searchCriteriaInitiale,
                                                        @RequestParam(required = false) Boolean searchCriteriaBalance,
                                                        @RequestParam(required = false) Boolean searchArchived,
                                                        @RequestParam(required = false) Boolean sortOpenDate,
                                                        @RequestParam(required = false) Boolean sortNomenclature) throws ExecutionException, InterruptedException {
        log.debug("getAffaires(offset: {} , limit {}", offset, limit);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());


        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(20, TimeUnit.SECONDS));
        }

        return responseBuilder
                .body(dossierV2Service.getAllAffaires(limit, offset, lawfirmToken.getUserId(), lawfirmToken.getVcKey(), lawfirmToken.getLanguage(), List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC),
                        searchCriteriaClient, searchCriteriaYear, searchCriteriaNomenclature, searchCriteriaBalance, searchCriteriaInitiale, searchArchived, sortOpenDate));
    }

    @GetMapping(value = "/shared/list")
    public ResponseEntity<Page<DossierDTO>> getSharedAffaires(@RequestParam int offset, @RequestParam int limit,
                                                              @RequestParam(required = false) String vcKey,
                                                              @RequestParam(required = false) String searchCriteriaClient,
                                                              @RequestParam(required = false) String searchCriteriaYear,
                                                              @RequestParam(required = false) String searchCriteriaNomenclature,
                                                              @RequestParam(required = false) Boolean searchArchived) {
        log.debug("getSharedAffaires(offset: {} , limit {}", offset, limit);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(dossierV2Service.getAllAffaires(limit, offset, lawfirmToken.getUserId(), lawfirmToken.getVcKey(), lawfirmToken.getLanguage(), List.of(EnumVCOwner.NOT_SAME_VC), searchCriteriaClient, searchCriteriaYear, searchCriteriaNomenclature, null, null, searchArchived, null));
    }

    @GetMapping(value = "/shared/list/count")
    public ResponseEntity<Long> countSharedAffaires(@RequestParam(required = false) String vcKey) {
        log.debug("countSharedAffaires()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey != null && vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(dossierV2Service.countSharedAffaires(lawfirmToken.getUserId(), lawfirmToken.getVcKey(), List.of(EnumVCOwner.NOT_SAME_VC)));
    }

    @GetMapping
    public List<ItemLongDto> getAffairesByVcUserIdAndSearchCriteria(@RequestParam(required = false) String searchCriteria, @RequestParam(required = false) boolean digital) {

        log.debug("Entering getAffairesByVcUserIdAndSearchCriteria()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getContacts(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return dossierV2Service.getAffairesByVcUserIdAndSearchCriteria(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), searchCriteria, digital);

    }

    @GetMapping("/clients/count/{clientId}")
    public Long countAffairesByClientId(@PathVariable Long clientId) throws LawfirmBusinessException {

        log.debug("Entering countAffairesByClientId( clientId {})", clientId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("countAffairesByClientId(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return dossierV2Service.countAffairesByClientId(clientId);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DossierDTO saveAffaire(@RequestBody DossierDTO dossierDTO) {
        log.debug("saveAffaire({})", dossierDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Long dossiersId = dossierV2Service.saveAffaireAndCreateCase(dossierDTO, lawfirmToken.getVcKey());

        return dossierV2Service.getDossierById(dossiersId);
    }

    @PostMapping(value = "/attach", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DossierDTO saveAffaireAndAttachToCase(@RequestBody CaseDTO casesModal,
                                                 @RequestParam int responsableId,
                                                 @RequestParam String virtualcabNomenclatureLabel,
                                                 @RequestParam int virtualcabNomenclatureId
    ) throws LawfirmBusinessException {
        log.debug("saveAffaireAndAttachToCase()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        Long dossiersId = dossierV2Service.saveAffaireAndAttachToCase(casesModal, responsableId, virtualcabNomenclatureLabel, virtualcabNomenclatureId, lawfirmToken.getVcKey());

        return dossierV2Service.getDossierById(dossiersId);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DossierDTO updateAffaire(@RequestBody DossierDTO dossierDTO) {
        log.debug("updateAffaire({})", dossierDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return dossierV2Service.updateAffaire(dossierDTO, lawfirmToken.getUserId(), lawfirmToken.getUsername(), lawfirmToken.getVcKey(), false);

    }

    @GetMapping("/share/{affaireid}")
    public List<ShareAffaireDTO> getSharedUserByAffaireId(@PathVariable Long affaireid) {

        log.debug("Entering getSharedUserByAffaireId(affaireid {})", affaireid);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getSharedUserByAffaireId(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return dossierV2Service.getSharedUserByAffaireId(affaireid, lawfirmToken.getVcKey());
    }

    @PostMapping(value = "/users/share", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addShareUser(@RequestBody ShareAffaireDTO shareAffaireDTO, @RequestParam Boolean isSendMail) throws RestException {
        log.debug("addShareUser({})", shareAffaireDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        dossierV2Service.addShareFolderUser(shareAffaireDTO, isSendMail);

        return shareAffaireDTO.getAffaireId();
    }

    @PutMapping(value = "/users/share", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long deleteShareUser(@RequestBody ShareAffaireDTO shareAffaireDTO) throws RestException {
        log.debug("deleteShareUser({})", shareAffaireDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        dossierV2Service.deleteShareFolderUser(shareAffaireDTO);

        return shareAffaireDTO.getAffaireId();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/digital", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DossierDTO> updateDossierIsDigital(@RequestBody Long dossierId) {
        log.debug("updateDossierIsDigital(dossierId: {}", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        dossierV2Service.switchDossierDigital(dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey());

        return ResponseEntity.ok(dossierV2Service.getDossierById(dossierId));

    }

    @PostMapping(value = "/parties/{dossierId}/invite", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String inviteConseil(@PathVariable Long dossierId, @RequestBody ItemPartieDTO partieDTO) throws RestException {
        log.debug("inviteConseil({})", partieDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return dossierV2Service.inviteConseil(dossierId, partieDTO);
    }

    @PostMapping(value = "/contact", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Boolean addNewContactToDossier(@RequestBody DossierDTO dossierDTO, @RequestParam(value = "newContactIdList") List<Long> newContactIdList) throws RestException {
        log.debug("addNewClientToDossier({})", dossierDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return dossierV2Service.addNewContactToDossier(dossierDTO, newContactIdList);
    }

}
