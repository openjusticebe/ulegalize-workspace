package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.enumeration.EnumDossierContactType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.UpdateShareResponse;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.ClientRepository;
import com.ulegalize.lawfirm.repository.DossierRepository;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TDossierRightsRepository;
import com.ulegalize.lawfirm.rest.LawfirmTransparencyApi;
import com.ulegalize.lawfirm.service.AffaireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AffaireServiceImpl implements AffaireService {

    private final EntityToDossierConverter entityToDossierConverter;
    private final LawfirmTransparencyApi lawfirmTransparencyApi;
    private final DossierRepository dossierRepository;
    private final TDossierRightsRepository tDossierRightsRepository;
    private final DossierRepository dossierRightsRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final ClientRepository clientRepository;

    public AffaireServiceImpl(EntityToDossierConverter entityToDossierConverter,
                              LawfirmTransparencyApi lawfirmTransparencyApi,
                              DossierRepository dossierRepository,
                              TDossierRightsRepository tDossierRightsRepository,
                              DossierRepository dossierRightsRepository,
                              LawfirmUserRepository lawfirmUserRepository,
                              ClientRepository clientRepository) {
        this.entityToDossierConverter = entityToDossierConverter;
        this.lawfirmTransparencyApi = lawfirmTransparencyApi;
        this.dossierRepository = dossierRepository;
        this.tDossierRightsRepository = tDossierRightsRepository;
        this.dossierRightsRepository = dossierRightsRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public DossierDTO getDossierById(Long id_doss) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String vcKey = lawfirmToken.getVcKey();
        log.debug("getDossierById -> Vckey {}", vcKey);
        Long userId = lawfirmToken.getUserId();
        log.debug("getDossierById -> userId {}", userId);

        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            Optional<TDossiers> tDossiers = dossierRepository.findByIdDoss(id_doss, lawfirmUsers.get().getId(), List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC));

            if (tDossiers.isPresent()) {
                EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
                log.info("Dossier is present id doss {} and vc user id {}", id_doss, lawfirmUsers.get().getId());
                return entityToDossierConverter.apply(tDossiers.get(), enumLanguage);
            }
        } else {
            log.warn("Lawfirm user is not present vckey {} and user id {}", vcKey, userId);
        }
        return null;
    }

    @Override
    public UpdateShareResponse updateShare(LawfirmToken lawfirmToken, UpdateShareRequestDTO updateShareRequest) throws RestException {
//        dossierRepository.
        UpdateShareResponse updateShareResponse = new UpdateShareResponse();
        List<String> emails = new ArrayList<>();

        Optional<TDossiers> dossiersOptional = dossierRepository.findById(updateShareRequest.getId_doss());

        if (!dossiersOptional.isPresent()) {
            log.warn("Dossier number {} not found", updateShareRequest.getId_doss());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dossier number: {" + updateShareRequest.getId_doss() + "} not found.");
        }
        Long countClient = clientRepository.countByVcKeyAndClientTypeAndFCompany(updateShareRequest.getVc_key(), EnumClientType.COLLEGUE, lawfirmToken.getVcKey());

        if (countClient == 0) {
            TClients tClients = new TClients();

            // duplicate client for the share dossier
            // create a client to invoice the cab in charge
            Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), lawfirmToken.getUserId());

            if (!lawfirmUsersOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lawfirm with key: {" + lawfirmToken.getVcKey() + "} not found.");
            }
            tClients.setF_nom(lawfirmUsersOptional.get().getUser().getFullname());
            tClients.setF_prenom(lawfirmUsersOptional.get().getUser().getFullname());
            tClients.setF_email(lawfirmUsersOptional.get().getUser().getEmail());
            tClients.setClient_type(EnumClientType.COLLEGUE);
            Optional<DossierContact> dossierContactClient = dossiersOptional.get().getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

            tClients.setId_lg(dossierContactClient.get().getClients().getId_lg());
            tClients.setF_company(lawfirmToken.getVcKey());
            tClients.setVc_key(updateShareRequest.getVc_key());
            tClients.setUser_upd(lawfirmToken.getUsername());

            clientRepository.save(tClients);
        }

        if (dossiersOptional.isPresent()) {
            // VC_OWNER 0, 1 or 2 .
            // 0: not in the same vc , 1: owner of vc, 2 not owner but same vc
            EnumVCOwner vcOwner = EnumVCOwner.NOT_SAME_VC;

            // all members
            if (updateShareRequest.getAll_member() != null && updateShareRequest.getAll_member()) {
                Long count = tDossierRightsRepository.countByDossierIdAndVcOwnerAndVcKey(updateShareRequest.getId_doss(), updateShareRequest.getVc_key());
                // if count > 0 , same vc key -> 2 . if count == 0 , 0
                if (count != null && count > 0) {
                    vcOwner = EnumVCOwner.NOT_OWNER_VC;
                }

                List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findByVcKeyAndDossier_NotExist(updateShareRequest.getVc_key(), updateShareRequest.getId_doss());

                for (LawfirmUsers lawfirmUsers : lawfirmUsersList) {
                    TDossierRights tDossierRights = new TDossierRights();
                    tDossierRights.setDossierId(updateShareRequest.getId_doss());
                    tDossierRights.setVcUserId(lawfirmUsers.getId());
                    tDossierRights.setVcOwner(vcOwner);
                    tDossierRights.setRIGHTS("ACCESS");
                    tDossierRights.setCreUser(lawfirmToken.getUsername());
                    tDossierRightsRepository.save(tDossierRights);

                    emails.add(lawfirmUsers.getUser().getEmail());
                }
            }
            // single member
            else {
                Optional<LawfirmUsers> usersOptional = lawfirmUserRepository.findByVcIdAndDossier_NotExist(updateShareRequest.getVcu_id(), updateShareRequest.getId_doss());

                // if not present , already registred
                if (usersOptional.isPresent()) {
                    emails.add(usersOptional.get().getUser().getEmail());
                    // vc from query (vc id) or user selected == vc connected
                    Long count = tDossierRightsRepository.countByDossierIdAndVcOwnerAndVcKey(updateShareRequest.getId_doss(), updateShareRequest.getVc_key());
                    // if count > 0 , same vc key -> 2 . if count == 0 , 0
                    if (count != null && count > 0) {
                        vcOwner = EnumVCOwner.NOT_OWNER_VC;
                    }
                    TDossierRights tDossierRights = new TDossierRights();
//                    tDossierRights.setTDossiers(dossiersOptional.get());
//                    tDossierRights.setLawfirmUsers(usersOptional.get());
                    tDossierRights.setDossierId(updateShareRequest.getId_doss());
                    tDossierRights.setVcUserId(updateShareRequest.getVcu_id());
                    tDossierRights.setVcOwner(vcOwner);
                    tDossierRights.setRIGHTS("ACCESS");
                    tDossierRights.setCreUser(lawfirmToken.getUsername());
                    tDossierRightsRepository.save(tDossierRights);

                }

            }
            // create case if it's not in the same vc
            if (vcOwner.equals(EnumVCOwner.NOT_SAME_VC)) {
                lawfirmTransparencyApi.createShareCases(lawfirmToken.getToken(), updateShareRequest);
            }
        }
        updateShareResponse.setEmails(emails);
        updateShareResponse.setNumDoss(dossiersOptional.get().getNum_doss());
        updateShareResponse.setYearDoss(dossiersOptional.get().getYear_doss());

        return updateShareResponse;
    }

    @Override
    public boolean hasRightAffaire(String vckey, Long affaireId) {
        log.info("Entering hasRightAffaire vckey {} and affaire id {}", vckey, affaireId);

        Long aLong = tDossierRightsRepository.countByDossierIdAndVcOwnerAndVcKey(affaireId, vckey);
        log.debug("count affaire {}", aLong);

        return aLong != null && aLong > 0;
    }


    @Override
    public void switchDossierDigital(DossierDTO dossierSummary, Long userId, String vcKey) {
        log.debug("Entering switchDossierDigital user id {} and vckey {} ", userId, vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);


        if (lawfirmUsers.isPresent()) {
            Optional<TDossiers> dossiersOptional = dossierRightsRepository.findAuthorizedByIdDoss(dossierSummary.getId(), lawfirmUsers.get().getId());

            if (dossiersOptional.isPresent()) {
                dossiersOptional.get().setIsDigital(dossierSummary.getIsDigital());
                dossierRightsRepository.save(dossiersOptional.get());

                log.debug("dossier {} updated with digital {}", dossierSummary.getId(), dossierSummary.getIsDigital());
            }
        }
    }

}
