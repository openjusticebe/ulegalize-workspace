package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.kafka.producer.drive.IDriveProducer;
import com.ulegalize.lawfirm.kafka.producer.transparency.IAffaireProducer;
import com.ulegalize.lawfirm.kafka.producer.transparency.ICaseProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.rest.DriveFactory;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.service.PrestationService;
import com.ulegalize.lawfirm.service.v2.ClientV2Service;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.service.v2.ObjSharedV2Service;
import com.ulegalize.lawfirm.utils.DriveUtils;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import com.ulegalize.utils.DossiersUtils;
import com.ulegalize.utils.PrestationUtils;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DossierV2ServiceImpl implements DossierV2Service {

    private final EntityToDossierConverter entityToDossierConverter;
    private final DossierRepository dossierRepository;
    private final TDossierRightsRepository tDossierRightsRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final LawfirmRepository lawfirmRepository;
    private final ClientRepository clientRepository;
    private final VirtualcabClientRepository virtualcabClientRepository;
    private final TMatiereRubriquesRepository tMatiereRubriquesRepository;
    private final TVirtualcabConfigRepository tVirtualcabConfigRepository;
    private final PrestationService prestationService;
    private final TFraisRepository tFraisRepository;
    private final TDebourRepository tDebourRepository;
    private final TUsersRepository tUsersRepository;
    private final TFacturesRepository tFacturesRepository;
    private final ObjSharedV2Service objSharedV2Service;
    private final TObjSharedRepository tObjSharedRepository;
    private final TObjSharedWithRepository tObjSharedWithRepository;
    private final MailService mailService;
    private final DriveFactory driveFactory;
    private final LawfirmV2Service lawfirmV2Service;
    private final ClientV2Service clientV2Service;

    // api
    private final ICaseProducer caseProducer;
    private final IAffaireProducer affaireProducer;

    private String REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    //Compile regular expression to get the pattern
    private Pattern pattern = Pattern.compile(REGEX);

    public DossierV2ServiceImpl(EntityToDossierConverter entityToDossierConverter,
                                DossierRepository dossierRepository,
                                TDossierRightsRepository tDossierRightsRepository,
                                LawfirmUserRepository lawfirmUserRepository,
                                LawfirmRepository lawfirmRepository, ClientRepository clientRepository,
                                VirtualcabClientRepository virtualcabClientRepository, TMatiereRubriquesRepository tMatiereRubriquesRepository,
                                TVirtualcabConfigRepository tVirtualcabConfigRepository,
                                PrestationService prestationService,
                                TFraisRepository tFraisRepository,
                                TDebourRepository tDebourRepository,
                                TUsersRepository tUsersRepository, TFacturesRepository tFacturesRepository, ObjSharedV2Service objSharedV2Service, TObjSharedRepository tObjSharedRepository,
                                TObjSharedWithRepository tObjSharedWithRepository, MailService mailService, DriveFactory driveFactory, LawfirmV2Service lawfirmV2Service,
                                ClientV2Service clientV2Service, ICaseProducer caseProducer, IAffaireProducer affaireProducer) {
        this.entityToDossierConverter = entityToDossierConverter;
        this.dossierRepository = dossierRepository;
        this.tDossierRightsRepository = tDossierRightsRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.clientRepository = clientRepository;
        this.virtualcabClientRepository = virtualcabClientRepository;
        this.tMatiereRubriquesRepository = tMatiereRubriquesRepository;
        this.tVirtualcabConfigRepository = tVirtualcabConfigRepository;
        this.prestationService = prestationService;
        this.tFraisRepository = tFraisRepository;
        this.tDebourRepository = tDebourRepository;
        this.tUsersRepository = tUsersRepository;
        this.tFacturesRepository = tFacturesRepository;
        this.objSharedV2Service = objSharedV2Service;
        this.tObjSharedRepository = tObjSharedRepository;
        this.tObjSharedWithRepository = tObjSharedWithRepository;
        this.mailService = mailService;
        this.driveFactory = driveFactory;
        this.lawfirmV2Service = lawfirmV2Service;
        this.clientV2Service = clientV2Service;
        this.caseProducer = caseProducer;
        this.affaireProducer = affaireProducer;
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
            // update last access date
            Optional<TDossierRights> optionalTDossierRights = tDossierRightsRepository.findAllByVcUserIdAndDossierId(lawfirmUsers.get().getId(), id_doss);

            optionalTDossierRights.ifPresent(dossierRepository -> {
                log.debug("Dossier right exists ");
                dossierRepository.setLastAccessDate(ZonedDateTime.now());

                tDossierRightsRepository.save(dossierRepository);
                log.debug("Dossier right saved ");
            });

            Optional<TDossiers> tDossiers = dossierRepository.findByIdDoss(id_doss, lawfirmUsers.get().getId(), List.of(EnumVCOwner.NOT_SAME_VC, EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC));

            if (tDossiers.isPresent()) {
                log.info("Dossier is present id doss {} and vc user id {}", id_doss, lawfirmUsers.get().getId());

                EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
                return entityToDossierConverter.apply(tDossiers.get(), enumLanguage);
            }
        } else {
            log.warn("Lawfirm user is not present vckey {} and user id {}", vcKey, userId);
        }
        return null;
    }


    @Override
    public Page<DossierDTO> getAllAffaires(int limit, int offset, Long userId, String vcKey, List<EnumVCOwner> enumVCOwner,
                                           String searchCriteriaClient, String searchCriteriaYear, Long searchCriteriaNumber, Boolean searchCriteriaBalance, String searchCriteriaInitiale, Boolean searchArchived) {
        log.debug("Get all Affaires with limit {} and offset {} , user id {} and vckey {}", limit, offset, userId, vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);
            LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());


            // if it's 0 , transform to null like this it will query on all result
            Long numberDossier = searchCriteriaNumber != null && searchCriteriaNumber == 0 ? null : searchCriteriaNumber;

            searchCriteriaClient = searchCriteriaClient != null && !searchCriteriaClient.isEmpty() ? searchCriteriaClient : "%";


            Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "id_doss"));

            Page<IDossierDTO> tDossiersList = null;
            String initiales = searchCriteriaInitiale != null && !searchCriteriaInitiale.isEmpty() ? searchCriteriaInitiale.toUpperCase() : "%";
            List<Integer> integers = enumVCOwner.stream().map(EnumVCOwner::getId).collect(Collectors.toList());

            log.debug("Search based on owner {} , client {}, year {}, number {}, initiale {}, balance {} and archived {}", integers,
                    searchCriteriaClient, searchCriteriaYear, numberDossier, initiales, searchCriteriaBalance, searchArchived);

            tDossiersList = dossierRepository.findByVcUserIdAllWithPagination(lawfirmUsers.get().getId(), integers,
                    searchCriteriaClient, searchCriteriaYear, numberDossier, initiales, searchCriteriaBalance, searchArchived,
                    pageable);

            Optional<EnumVCOwner> enumVCOwner2 = enumVCOwner.stream().filter(enumVCOwner1 -> enumVCOwner1.equals(EnumVCOwner.NOT_SAME_VC)).findFirst();

            List<DossierDTO> dossierDTOS = tDossiersList.getContent().stream().map(dossier -> {
                DossierDTO dossierDTO = new DossierDTO(dossier.getId(), dossier.getYear(), dossier.getNum(), dossier.getInitiales(),
                        dossier.getFirstnameClient(), dossier.getLastnameClient(), dossier.getCompanyClient(), dossier.getIdClient(),
                        dossier.getAdverseFirstnameClient(), dossier.getAdverseLastnameClient(), dossier.getAdverseCompanyClient(), dossier.getAdverseIdClient(),
                        dossier.getBalance(), dossier.getVckeyOwner(), enumVCOwner2, dossier.getCloseDossier(),
                        dossier.getType(),
                        dossier.getLastAccessDate()

                );
                dossierDTO.setTypeItem(new ItemStringDto(dossier.getType().getDossType(),
                        Utils.getLabel(enumLanguage,
                                dossier.getType().getLabelFr(),
                                dossier.getType().getLabelEn(),
                                dossier.getType().getLabelNl())));

                return dossierDTO;
            }).collect(Collectors.toList());

            log.debug("Result affaire list total {}", tDossiersList.getTotalElements());

            return new PageImpl<>(dossierDTOS, Pageable.unpaged(), tDossiersList.getTotalElements());
        }

        return null;
    }

    private FinanceDTO getBalancePerDossier(Long dossierId, Long userId, String vcKey, Long vcUserId) {
        FinanceDTO financeDTO = new FinanceDTO();

//        BigDecimal prestations = BigDecimal.ZERO;
        List<PrestationSummary> prestationsByDossierId = prestationService.getPrestationsByDossierId(dossierId, userId, vcKey);

        //populate
        BigDecimal prestations = prestationsByDossierId.stream()
                .map(prestation -> PrestationUtils.calculateHVAT(prestation.isForfait(), prestation.getDm(), prestation.getDh(), prestation.getCouthoraire(), prestation.getForfaitHt()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        financeDTO.setPrestations(prestations.setScale(2, RoundingMode.HALF_UP));
        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllDeboursByVcKey(dossierId, vcKey);
        financeDTO.setDebours(countAllJusticeByVcKey.setScale(2, RoundingMode.HALF_UP));
        BigDecimal allDebours = tDebourRepository.sumByDossierId(dossierId, vcUserId);
        financeDTO.setFraisAdmin(allDebours.setScale(2, RoundingMode.HALF_UP));
        BigDecimal allCollabByVcKey = tFraisRepository.sumAllCollabByVcKey(dossierId, vcKey);
        financeDTO.setCollaboration(allCollabByVcKey.setScale(2, RoundingMode.HALF_UP));
        BigDecimal allHonoByVcKey = tFraisRepository.sumAllHonoHtvaByVcKey(dossierId, vcKey);
        financeDTO.setHonoraires(allHonoByVcKey.setScale(2, RoundingMode.HALF_UP));
        BigDecimal allAccountTiers = tFraisRepository.sumAllTiersByVcKey(dossierId, vcKey);
        financeDTO.setTiersAccount(allAccountTiers.setScale(2, RoundingMode.HALF_UP));

        BigDecimal totalHonoraire = prestations.add(countAllJusticeByVcKey).add(allDebours).add(allCollabByVcKey);
        financeDTO.setTotalHonoraire(totalHonoraire.setScale(2, RoundingMode.HALF_UP));

        BigDecimal totalInvoice = tFacturesRepository.sumHtvaInvoiceByVcKey(vcKey, dossierId);
        financeDTO.setTotalInvoice(totalInvoice.setScale(2, RoundingMode.HALF_UP));

        financeDTO.setBalance(totalHonoraire.subtract(totalInvoice));

        return financeDTO;
    }

    @Override
    public Long saveAffaire(DossierDTO dossierDTO, String vcKey) {
        log.debug("Entering saveAffaire dossier {}", dossierDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("saveAffaire -> Vckey {}", vcKey);
        Long userId = lawfirmToken.getUserId();
        log.debug("saveAffaire -> userId {}", userId);
        String username = lawfirmToken.getUsername();
        log.debug("saveAffaire -> username {}", username);

        TDossiers tDossiers = new TDossiers();

        commonRuleDossier(dossierDTO);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dossierDTO.getOpenDossier());
        int yearInt = cal.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        tDossiers.setYear_doss(year);
        Long maxDossier = dossierRepository.getMaxDossierByVckeyAndYear(vcKey, year);
        tDossiers.setNum_doss(maxDossier);
        tDossiers.setDoss_type(dossierDTO.getType().getDossType());

        Integer coutHoraire = dossierDTO.getCouthoraire();
        if (dossierDTO.getCouthoraire() == null || dossierDTO.getCouthoraire() == 0) {
            Optional<LawfirmUsers> optionalLawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), lawfirmToken.getUserId());

            if (optionalLawfirmUsers.isPresent()) {
                coutHoraire = optionalLawfirmUsers.get().getCouthoraire();
            }
        }

        Optional<TClients> client = Optional.empty();
        DossierContact dossierContact = null;
        switch (dossierDTO.getType()) {
            case BA:
            case DC:
            case DF:
                client = clientRepository.findById(dossierDTO.getIdClient());
                if (client.isEmpty()) {
                    log.warn("client {} is not found", dossierDTO.getIdClient());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
                }
                dossierContact = new DossierContact();
                dossierContact.setDossiers(tDossiers);
                dossierContact.setContactTypeId(EnumDossierContactType.CLIENT);
                dossierContact.setClients(client.get());
                dossierContact.setCreUser(username);
                tDossiers.getDossierContactList().add(dossierContact);

                client = clientRepository.findById(dossierDTO.getIdAdverseClient());

                if (client.isEmpty()) {
                    log.warn("client adv {} is not found", dossierDTO.getIdAdverseClient());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client adv is not found");
                }

                dossierContact = new DossierContact();
                dossierContact.setDossiers(tDossiers);
                dossierContact.setContactTypeId(EnumDossierContactType.OPPOSING);
                dossierContact.setClients(client.get());
                dossierContact.setCreUser(username);
                tDossiers.getDossierContactList().add(dossierContact);
                break;
            case MD:
                for (ItemClientDto dossierContact1 : dossierDTO.getClientList()) {
                    dossierContact = new DossierContact();
                    log.info(" Contact type {}", dossierContact1.getType());
                    client = clientRepository.findById(dossierContact1.getValue());

                    if (client.isEmpty()) {
                        log.warn("client partie {} is not found", dossierContact1.getValue());
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client adv is not found");
                    }
                    if (dossierContact1.getType().equals(EnumDossierContactType.PARTY)) {
                        dossierContact = new DossierContact();
                        dossierContact.setDossiers(tDossiers);
                        dossierContact.setContactTypeId(EnumDossierContactType.PARTY);

                        dossierContact.setClients(client.get());
                        dossierContact.setCreUser(username);
                        tDossiers.getDossierContactList().add(dossierContact);
                    }

                }
                break;
        }

        tDossiers.setVc_key(vcKey);
        tDossiers.setUserUpd(username);

        tDossiers.setDate_open(dossierDTO.getOpenDossier());
        tDossiers.setDate_close(null);
        Optional<TMatiereRubriques> rubriques = tMatiereRubriquesRepository.findById(dossierDTO.getId_matiere_rubrique());

        if (rubriques.isEmpty()) {
            log.warn("rubriques {} is not found", dossierDTO.getId_matiere_rubrique());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rubrique is not found");
        }

        tDossiers.setTMatiereRubriques(rubriques.get());
        tDossiers.setKeywords(dossierDTO.getKeywords() != null ? dossierDTO.getKeywords() : "");
        tDossiers.setMemo(dossierDTO.getMemo() != null ? dossierDTO.getMemo() : "");
        tDossiers.setCouthoraire(coutHoraire);
        tDossiers.setNote(dossierDTO.getNote() != null ? dossierDTO.getNote() : "");
        tDossiers.setSuccess_fee_montant(dossierDTO.getSuccess_fee_montant() != null ? dossierDTO.getSuccess_fee_montant() : BigDecimal.ZERO);
        tDossiers.setSuccess_fee_perc(dossierDTO.getSuccess_fee_perc());
        tDossiers.setId_user_resp(dossierDTO.getIdUserResponsible());
        if (dossierDTO.getConseilIdAdverseClient() != null) {
            Optional<TClients> clientOpposent = clientRepository.findById(dossierDTO.getConseilIdAdverseClient());
            if (clientOpposent.isEmpty()) {
                log.warn("client adv {} is not found", dossierDTO.getIdAdverseClient());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client opposent is not found");
            }
            tDossiers.setOpposingCounsel(clientOpposent.get());
        } else {
            tDossiers.setOpposingCounsel(null);
        }

        tDossiers.setClientQuality(dossierDTO.getQuality());
        // be sure that the transparency is created
        tDossiers.setIsDigital(false);
//
        dossierRepository.save(tDossiers);

        createTDossierRight(tDossiers, userId, vcKey, username);

        // get path in order to create folder within drive
        List<String> paths = getPaths(vcKey, tDossiers.getYear_doss(), StringUtils.leftPad(tDossiers.getNum_doss().toString(), 4, "0"));

        IDriveProducer driveProducer = driveFactory.getDriveProducer(lawfirmToken.getDriveType());
        driveProducer.createFolders(lawfirmToken, vcKey, paths);

        return tDossiers.getIdDoss();
    }

    @Override
    public Long saveAffaireAndCreateCase(DossierDTO dossierDTO, String vcKey) {
        log.debug("entering saveAffaireAndCreateCase {}", dossierDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long saveAffaire = saveAffaire(dossierDTO, vcKey);

        DossierDTO newDossierDto = getDossierById(saveAffaire);

        createCasLawfirm(newDossierDto, lawfirmToken);

        return saveAffaire;
    }

    @Override
    public Long saveAffaireAndAttachToCase(String caseId, DossierDTO dossierDTO, String vcKey) {
        log.debug("entering saveAffaireAndAttachToCase {}", dossierDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long saveAffaire = saveAffaire(dossierDTO, vcKey);
        DossierDTO newDossierDto = getDossierById(saveAffaire);

        attachAffaire(caseId, newDossierDto, lawfirmToken);

        return saveAffaire;
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = ResponseStatusException.class)
    public void createCasLawfirm(DossierDTO dossierDTO, LawfirmToken lawfirmToken) throws ResponseStatusException {
        log.debug("entering createCasLawfirm {}", dossierDTO);
        List<ContactSummary> contactSummaryList = new ArrayList<>();
        switch (dossierDTO.getType()) {
            case BA:
            case DC:
            case DF:
                ContactSummary contactSummary = clientV2Service.getCientById(dossierDTO.getIdClient());
                contactSummaryList.add(contactSummary);
                break;
            case MD:
                for (ItemClientDto dossierContact1 : dossierDTO.getClientList()) {
                    log.info(" Contact type {}", dossierContact1.getType());
                    try {
                        ContactSummary contactSummary1 = clientV2Service.getCientById(dossierContact1.getValue());
                        if (contactSummary1.getEmail() != null && !contactSummary1.getEmail().isEmpty()) {
                            contactSummaryList.add(contactSummary1);
                        }
                    } catch (ResponseStatusException rs) {
                        log.warn("Client does not exist {}", rs.getMessage());
                    }
                }
                break;
        }

        // check there is at least one contact
        // check if the email's contact exist for transparency
        if (!CollectionUtils.isEmpty(contactSummaryList)) {
            boolean anyMatch = contactSummaryList.stream().anyMatch(contactSummary -> {
                if (contactSummary.getEmail() != null) {
                    Matcher matcher = pattern.matcher(contactSummary.getEmail().toLowerCase());
                    return matcher.matches();
                } else {
                    return false;
                }
            });
            // Check if any emails are valid
            if (anyMatch) {
                CaseCreationDTO caseCreationDTO = new CaseCreationDTO();
                caseCreationDTO.setDossier(dossierDTO);
                caseCreationDTO.setContactSummaryList(new ArrayList<>());
                caseCreationDTO.getContactSummaryList().addAll(contactSummaryList);

                caseProducer.createCaseMessage(caseCreationDTO, lawfirmToken);
            } else {
                log.warn("The contact email has not been fill in.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No contact with email for transparency");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = ResponseStatusException.class)
    public void attachAffaire(String caseId, DossierDTO dossierDTO, LawfirmToken lawfirmToken) throws
            ResponseStatusException {
        log.debug("entering createCasLawfirm {}", dossierDTO);
        List<ContactSummary> contactSummaryList = new ArrayList<>();
        switch (dossierDTO.getType()) {
            case BA:
            case DC:
            case DF:
                ContactSummary contactSummary = clientV2Service.getCientById(dossierDTO.getIdClient());
                contactSummaryList.add(contactSummary);
                break;
            case MD:
                for (ItemClientDto dossierContact1 : dossierDTO.getClientList()) {
                    log.info(" Contact type {}", dossierContact1.getType());
                    try {
                        ContactSummary contactSummary1 = clientV2Service.getCientById(dossierContact1.getValue());
                        if (contactSummary1.getEmail() != null && !contactSummary1.getEmail().isEmpty()) {
                            contactSummaryList.add(contactSummary1);
                        }
                    } catch (ResponseStatusException rs) {
                        log.warn("Client does not exist {}", rs.getMessage());
                    }
                }
                break;
        }

        if (!CollectionUtils.isEmpty(contactSummaryList)) {
            CaseCreationDTO caseCreationDTO = new CaseCreationDTO();
            caseCreationDTO.setDossier(dossierDTO);
            caseCreationDTO.setContactSummaryList(new ArrayList<>());
            caseCreationDTO.getContactSummaryList().addAll(contactSummaryList);
            caseCreationDTO.setCaseId(caseId);

            affaireProducer.attachAffaire(caseCreationDTO, lawfirmToken);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No contact with email for transparency");
        }
    }

    private void createTDossierRight(TDossiers dossiers, Long userId, String vcKey, String username) {
        TDossierRights tDossierRights = new TDossierRights();
        tDossierRights.setDossierId(dossiers.getIdDoss());

        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lawfirm with key: {" + vcKey + "} not found.");
        }
        tDossierRights.setVcUserId(lawfirmUsersOptional.get().getId());
        tDossierRights.setVcOwner(EnumVCOwner.OWNER_VC);
        tDossierRights.setRIGHTS("ACCESS");
        tDossierRights.setTDossiers(dossiers);
        tDossierRights.setCreUser(username);

        tDossierRightsRepository.save(tDossierRights);
    }

    public List<String> getPaths(String vcKey, String year, String number) {
        List<TVirtualcabConfig> virtualcabConfigs = tVirtualcabConfigRepository.findAllByVcKey(vcKey);

        List<String> folders = virtualcabConfigs.stream()
                .map(tVirtualcabConfig -> ",dossiers," + year + "," + number + "," + tVirtualcabConfig.getDescription() + ",")
                .collect(Collectors.toList());

        // create at least the folder
        folders.add(",dossiers," + year + "," + number + ",");

        return folders;
    }

    @Override
    public DossierDTO getDefaultDossier(String vcKey, Long userId) {
        log.debug("Entering getDefaultDossier vc {} and userid {}", vcKey, userId);

        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm does not exist for user id");
        }

        DossierDTO dossierDTO = new DossierDTO();

        dossierDTO.setCouthoraire(lawfirmUsersOptional.get().isUseSelfCouthoraire() ? lawfirmUsersOptional.get().getCouthoraire() : lawfirmUsersOptional.get().getLawfirm().getCouthoraire());
        dossierDTO.setSuccess_fee_perc(100);
        dossierDTO.setSuccess_fee_montant(BigDecimal.ZERO);
        dossierDTO.setOpenDossier(new Date());
        dossierDTO.setType(EnumDossierType.DC);
        dossierDTO.setClientList(new ArrayList<>());
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
        String label = Utils.getLabel(enumLanguage, EnumDossierType.DC.getLabelFr(), EnumDossierType.DC.getLabelEn(), EnumDossierType.DC.getLabelNl());

        dossierDTO.setTypeItem(new ItemStringDto(EnumDossierType.DC.getDossType(), label));
        dossierDTO.setIdUserResponsible(userId);

        return dossierDTO;
    }

    @Override
    public DossierDTO updateAffaire(DossierDTO dossierDTO, Long userId, String username, String vcKey) {
        log.debug("updateAffaire -> Vckey {}", vcKey);
        log.debug("updateAffaire -> userId {}", userId);
        log.debug("updateAffaire -> username {}", username);

        commonRuleDossier(dossierDTO);

        if (dossierDTO.getId() == null) {
            log.warn("Dossier is not filled in {}", dossierDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Dossier is not filled in");
        }
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            Optional<TDossiers> tDossiersOptional = dossierRepository.findByIdDoss(dossierDTO.getId(), lawfirmUsers.get().getId(), List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC));

            if (tDossiersOptional.isEmpty()) {
                log.warn("Dossier does not exist {}", dossierDTO.getId());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Dossier does not exist");
            }

            TDossiers tDossiers = tDossiersOptional.get();
            tDossiers.setDoss_type(dossierDTO.getType().getDossType());


            DossierContact dossierContact = null;
            switch (dossierDTO.getType()) {
                case BA:
                case DC:

                    Optional<TClients> clientAdverse = clientRepository.findById(dossierDTO.getIdAdverseClient());
                    if (clientAdverse.isEmpty()) {
                        log.warn("client adv {} is not found", dossierDTO.getIdAdverseClient());
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client adv is not found");
                    }
                    // link client-dossier

                    Optional<TClients> client = clientRepository.findById(dossierDTO.getIdClient());
                    if (client.isEmpty()) {
                        log.warn("client {} is not found", dossierDTO.getIdClient());
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
                    }
                    for (DossierContact dossierContact1 : tDossiers.getDossierContactList()) {
                        log.info(" Contact type {}", dossierContact1.getContactTypeId());
                        switch (dossierContact1.getContactTypeId()) {
                            case CLIENT:
                                if (!dossierContact1.getClients().getId_client().equals(client.get().getId_client())) {
                                    log.debug("Client registered has been changed old {} with the new {}", dossierContact1.getClients().getId_client(), client.get().getId_client());
                                    dossierContact1.setClients(client.get());
                                    dossierContact1.setUpdUser(username);
                                }

                                break;
                            case OPPOSING:
                                if (!dossierContact1.getClients().getId_client().equals(clientAdverse.get().getId_client())) {
                                    log.debug("Client registered has been changed old {} with the new {}", dossierContact1.getClients().getId_client(), clientAdverse.get().getId_client());
                                    dossierContact1.setClients(clientAdverse.get());
                                    dossierContact1.setUpdUser(username);
                                }
                                break;
                        }
                    }
                    break;
                case MD:
                    tDossiers.getDossierContactList().clear();

                    for (ItemClientDto dossierContact1 : dossierDTO.getClientList()) {
                        // Mediation is always party client
                        if (dossierContact1.getType().equals(EnumDossierContactType.PARTY)) {
                            log.info(" Contact type {}", dossierContact1.getType());
                            client = clientRepository.findById(dossierContact1.getValue());

                            if (client.isEmpty()) {
                                log.warn("client partie {} is not found", dossierContact1.getValue());
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client adv is not found");
                            }
                            dossierContact = new DossierContact();
                            dossierContact.setDossiers(tDossiers);
                            dossierContact.setContactTypeId(EnumDossierContactType.PARTY);

                            dossierContact.setClients(client.get());
                            dossierContact.setCreUser(username);
                            dossierContact.setUpdUser(username);
                            tDossiers.getDossierContactList().add(dossierContact);
                        }

                    }
                    break;
            }

            tDossiers.setDate_open(dossierDTO.getOpenDossier());
            tDossiers.setDate_close(dossierDTO.getCloseDossier());

            Optional<TMatiereRubriques> rubriques = tMatiereRubriquesRepository.findById(dossierDTO.getId_matiere_rubrique());

            if (rubriques.isEmpty()) {
                log.warn("rubriques {} is not found", dossierDTO.getId_matiere_rubrique());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "rubrique is not found");
            }

            tDossiers.setTMatiereRubriques(rubriques.get());
            tDossiers.setKeywords(dossierDTO.getKeywords() != null ? dossierDTO.getKeywords() : "");
            tDossiers.setMemo(dossierDTO.getMemo() != null ? dossierDTO.getMemo() : "");
            tDossiers.setCouthoraire(dossierDTO.getCouthoraire());
            tDossiers.setNote(dossierDTO.getNote() != null ? dossierDTO.getNote() : "");
            tDossiers.setSuccess_fee_montant(dossierDTO.getSuccess_fee_montant() != null ? dossierDTO.getSuccess_fee_montant() : BigDecimal.ZERO);
            tDossiers.setSuccess_fee_perc(dossierDTO.getSuccess_fee_perc());
            tDossiers.setId_user_resp(dossierDTO.getIdUserResponsible());
            // check if the responsable user is in the vckey
            Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, dossierDTO.getIdUserResponsible());
            if (lawfirmUsersOptional.isPresent()) {
                tDossiers.setId_user_resp(dossierDTO.getIdUserResponsible());
            }
            if (dossierDTO.getConseilIdAdverseClient() != null) {
                Optional<TClients> clientOpposent = clientRepository.findById(dossierDTO.getConseilIdAdverseClient());
                if (clientOpposent.isEmpty()) {
                    log.warn("client adv {} is not found", dossierDTO.getIdAdverseClient());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client opposent is not found");
                }
                tDossiers.setOpposingCounsel(clientOpposent.get());
            } else {
                tDossiers.setOpposingCounsel(null);
            }

            tDossiers.setClientQuality(dossierDTO.getQuality());
            tDossiers.setUserUpd(username);

            dossierRepository.save(tDossiers);

            log.debug("Dossier id {} saved", tDossiers.getIdDoss());
            return getDossierById(tDossiers.getIdDoss());
        } else {
            log.warn("Lawfirm user is not present vckey {} and user id {}", vcKey, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lawfirm user is not present ");
        }
    }

    private void commonRuleDossier(DossierDTO dossierDTO) throws ResponseStatusException {
        if (dossierDTO.getOpenDossier() == null) {
            log.warn("No open date dossier {}", dossierDTO.getOpenDossier());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No open date dossier");
        }
        if (dossierDTO.getType() == null) {
            log.warn("Type of dossier {} is not found", dossierDTO.getType());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type of dossier is not found");
        }
        // cabinet must have a client and adverse
        if (!dossierDTO.getType().equals(EnumDossierType.MD)) {
            log.debug("Rule {} adverse client {} ", dossierDTO.getType(), dossierDTO.getIdAdverseClient());
            if (dossierDTO.getIdAdverseClient() == null) {
                log.warn("client adv {} is not found", dossierDTO.getIdAdverseClient());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client adv is not found");
            }
            log.debug("Rule {} client {} ", dossierDTO.getType(), dossierDTO.getIdClient());
            if (dossierDTO.getIdClient() == null) {
                log.warn("client {} is not found", dossierDTO.getIdClient());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
            }
        } else if (dossierDTO.getType().equals(EnumDossierType.MD)) {
            if (CollectionUtils.isEmpty(dossierDTO.getClientList())) {
                log.warn("client {} is not found", dossierDTO.getIdClient());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client list is not found");
            }
        }

    }

    @Override
    public List<ItemLongDto> getAffairesByVcUserIdAndSearchCriteria(String vcKey, Long userId, String
            searchCriteria) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("Get Affaires by vcKey {} and userId {} and Search Criteria {}", vcKey, userId, searchCriteria);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm does not exist for user id");
        }

        log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);


        List<TDossiers> dossierListByVcUserId = dossierRepository.findAffairesByVcUserId(lawfirmUsers.get().getId());
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
        List<DossierDTO> dossierDTOList = entityToDossierConverter.convertToList(dossierListByVcUserId, enumLanguage);

        List<DossierDTO> resultList = dossierDTOList;
        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            long number = 0;
            String year = "%";
            boolean bothYearAndNumber = false;

            bothYearAndNumber = searchCriteria.contains("/");
            // remove all non digit and /
            //year = searchCriteria.replaceAll("[^\\d/]", "");
            year = StringUtils.substringBefore(searchCriteria, "/");
            try {
                if (bothYearAndNumber) {
                    year = StringUtils.substringBefore(searchCriteria, "/");
                    number = Long.valueOf(StringUtils.substringAfter(searchCriteria, "/"));
                } else {
                    number = Long.valueOf(searchCriteria);
                }
            } catch (NumberFormatException nf) {
                log.warn("Number of dossier is wrong {}", searchCriteria);
            }

            String finalYear = year;
            long finalNumber = number;

            log.info("****************");
            log.info("Search criteria {}", searchCriteria);
            log.info("Number {}", number);
            log.info("year {}", year);
            log.info("bothYearAndNumber {}", bothYearAndNumber);
            log.info("****************");

            resultList = !bothYearAndNumber ? dossierDTOList.stream()
                    .filter(dossier
                            -> dossier.getYear().toString().toLowerCase().contains(finalYear.toLowerCase())
                            || dossier.getNum().toString().toLowerCase().contains(String.valueOf(finalNumber)))
                    .collect(Collectors.toList()) :
                    dossierDTOList.stream()
                            .filter(dossier
                                    -> dossier.getYear().toString().toLowerCase().contains(finalYear.toLowerCase())
                                    && dossier.getNum().toString().toLowerCase().contains(String.valueOf(finalNumber)))
                            .collect(Collectors.toList());

        }

        return resultList.stream()
                .map(dossier -> {
                            if (dossier.getVckeyOwner() != null && !dossier.getVckeyOwner().equalsIgnoreCase(vcKey)) {
                                return new ItemLongDto(dossier.getId(), dossier.getLabel(), dossier.getVckeyOwner());
                            } else {
                                return new ItemLongDto(dossier.getId(), dossier.getLabel());
                            }

                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public FinanceDTO getFinanceDossierById(Long dossierId) {
        log.debug("getFinanceDossierById and dossierId {}", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String vcKey = lawfirmToken.getVcKey();
        log.debug("getFinanceDossierById -> Vckey {}", vcKey);
        Long userId = lawfirmToken.getUserId();
        log.debug("getFinanceDossierById -> userId {}", userId);
        String username = lawfirmToken.getUsername();
        log.debug("getFinanceDossierById -> username {}", username);

        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Optional<TDossiers> tDossiersOptional = dossierRepository.findAuthorizedByIdDoss(dossierId, lawfirmUsers.get().getId());

            if (!tDossiersOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dossier does not exist for user id");
            }

            log.debug("Result affaire list check the balance");
            EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());

            DossierDTO dossierDTO = entityToDossierConverter.apply(tDossiersOptional.get(), enumLanguage);

            // define the balance
            // prestation
            return getBalancePerDossier(dossierDTO.getId(), userId, vcKey, lawfirmUsers.get().getId());

        }

        return null;
    }

    @Override
    public Long countAffairesByClientId(Long clientId) {
        log.debug("countAffairesByClientId and clientId {}", clientId);

        Long aLong = dossierRepository.countByClient_cabAndOrClient_adv(clientId, clientId, clientId);
        log.debug("countAffairesByClientId dossier number {}", aLong);
        return aLong;
    }

    @Override
    public List<ShareAffaireDTO> getSharedUserByAffaireId(Long affaireid, String vcKey) {
        log.debug("Enrering getSharedUserByAffaireId and affaireid {}", affaireid);
        return tDossierRightsRepository.findShareUserByAffaireId(affaireid);
    }

    @Override
    public void addShareFolderUser(ShareAffaireDTO shareAffaireDTO, boolean isSendMail) {
        log.debug("Enrering addShareUser shareAffaireDTO {}", shareAffaireDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> emails = new ArrayList<>();

        if (shareAffaireDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "share file must be filled in");
        }
        if (shareAffaireDTO.getAffaireId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "affaire id must be filled in");
        }
        if (shareAffaireDTO.getVcKey() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vc key must be filled in");
        }
        Optional<TDossiers> dossiersOptional = dossierRepository.findById(shareAffaireDTO.getAffaireId());

        if (dossiersOptional.isEmpty()) {
            log.warn("Dossier number {} not found", shareAffaireDTO.getAffaireId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dossier number: {" + shareAffaireDTO.getAffaireId() + "} not found.");
        }

        Long countClient = clientRepository.countByVcKeyAndClientTypeAndFCompany(shareAffaireDTO.getVcKey(), EnumClientType.COLLEGUE, lawfirmToken.getVcKey());

        if (countClient == 0) {
            TClients tClients = new TClients();

            // duplicate client for the share dossier
            // create a client to invoice the cab in charge
            Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), lawfirmToken.getUserId());

            if (lawfirmUsersOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lawfirm with key: {" + lawfirmToken.getVcKey() + "} not found.");
            }
            tClients.setF_nom(lawfirmUsersOptional.get().getUser().getFullname());
            tClients.setF_prenom(lawfirmUsersOptional.get().getUser().getFullname());
            tClients.setF_email(lawfirmUsersOptional.get().getUser().getEmail());
            tClients.setClient_type(EnumClientType.COLLEGUE);
            Optional<DossierContact> dossierContactClient = dossiersOptional.get().getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

            if (dossierContactClient.isPresent()) {
                tClients.setId_lg(dossierContactClient.get().getClients().getId_lg());
            } else {
                tClients.setId_lg(EnumLanguage.FR.getShortCode());
            }
            tClients.setF_company(lawfirmToken.getVcKey());
//            lawfirmClient.setVc_key(shareAffaireDTO.getVcKey());
            tClients.setUser_upd(lawfirmToken.getUsername());
            if (tClients.getVirtualcabClientList() == null) {
                tClients.setVirtualcabClientList(new ArrayList<>());
            }

            Optional<LawfirmEntity> optionalLawfirmEntity = lawfirmRepository.findLawfirmByVckey(shareAffaireDTO.getVcKey());

            if (optionalLawfirmEntity.isPresent()) {
                VirtualcabClient virtualcabClient = new VirtualcabClient();
                virtualcabClient.setTClients(tClients);
                virtualcabClient.setLawfirm(optionalLawfirmEntity.get());
                virtualcabClient.setCreUser(lawfirmToken.getUsername());
                tClients.getVirtualcabClientList().add(virtualcabClient);

                clientRepository.save(tClients);
            }
        }

        for (DossierContact dossierContact : dossiersOptional.get().getDossierContactList()) {
            log.debug("Client Opposing exists for dossier id {}", dossiersOptional.get().getIdDoss());
            TClients clients = dossierContact.getClients();

            clients.setUser_upd(lawfirmToken.getUsername());
            if (clients.getVirtualcabClientList() == null) {
                clients.setVirtualcabClientList(new ArrayList<>());
            }

            Optional<LawfirmEntity> optionalLawfirmEntityAdv = lawfirmRepository.findLawfirmByVckey(shareAffaireDTO.getVcKey());

            if (optionalLawfirmEntityAdv.isPresent()) {
                Optional<VirtualcabClient> virtualcabClientOptional = virtualcabClientRepository.findByLawfirmAAndTClients(optionalLawfirmEntityAdv.get().getVckey(), clients.getId_client());
                if (virtualcabClientOptional.isEmpty()) {
                    log.warn("Virtualcab client is not present {} , {}", optionalLawfirmEntityAdv.get().getVckey(), clients.getId_client());
                    VirtualcabClient virtualcabClient = new VirtualcabClient();
                    virtualcabClient.setTClients(clients);
                    virtualcabClient.setLawfirm(optionalLawfirmEntityAdv.get());
                    virtualcabClient.setCreUser(lawfirmToken.getUsername());
                    clients.getVirtualcabClientList().add(virtualcabClient);
                }
                clientRepository.save(clients);
            }
        }
        // VC_OWNER 0, 1 or 2 .
        // 0: not in the same vc , 1: owner of vc, 2 not owner but same vc
        EnumVCOwner vcOwner = EnumVCOwner.NOT_SAME_VC;

        // all members
        if (shareAffaireDTO.isAllMembers()) {
            Long count = tDossierRightsRepository.countByDossierIdAndVcOwnerAndVcKey(shareAffaireDTO.getAffaireId(), shareAffaireDTO.getVcKey());
            // if count > 0 , same vc key -> 2 . if count == 0 , 0
            if (count != null && count > 0) {
                vcOwner = EnumVCOwner.NOT_OWNER_VC;
            }

            List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findByVcKeyAndDossier_NotExist(shareAffaireDTO.getVcKey(), shareAffaireDTO.getAffaireId());

            emails.addAll(createShareDossier(lawfirmToken, lawfirmUsersList, vcOwner, shareAffaireDTO.getAffaireId()));

        }
        // single member
        else {
            List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findByVcAndUserIdAndDossier_NotExist(shareAffaireDTO.getVcKey(), shareAffaireDTO.getUserIdSelected(), shareAffaireDTO.getAffaireId());

            // if not present , already registred
            if (lawfirmUsersList != null) {
                // vc from query (vc id) or user selected == vc connected
                Long count = tDossierRightsRepository.countByDossierIdAndVcOwnerAndVcKey(shareAffaireDTO.getAffaireId(), shareAffaireDTO.getVcKey());
                // if count > 0 , same vc key -> 2 . if count == 0 , 0
                if (count != null && count > 0) {
                    vcOwner = EnumVCOwner.NOT_OWNER_VC;
                }

                emails.addAll(createShareDossier(lawfirmToken, lawfirmUsersList, vcOwner, shareAffaireDTO.getAffaireId()));
            }
        }

        log.info("start share folder");
        // share drive
        String objPath = getObjShared(dossiersOptional.get().getYear_doss(), dossiersOptional.get().getNum_doss());
        objSharedV2Service.shareFolder(objPath, lawfirmToken.getVcKey(), lawfirmToken.getUsername(), lawfirmToken.getUserId(), 0L, emails, "All dossier has been shared", 0, lawfirmToken.getClientFrom());

        // create case if it's not in the same vc
        if (vcOwner.equals(EnumVCOwner.NOT_SAME_VC)) {
            UpdateShareRequestDTO updateShareRequest = new UpdateShareRequestDTO();
            updateShareRequest.setVc_key(shareAffaireDTO.getVcKey());
            updateShareRequest.setId_doss(shareAffaireDTO.getAffaireId());
            caseProducer.createShareCases(lawfirmToken, updateShareRequest);
        }

        // email
        if (isSendMail) {
            emails.forEach(email -> {
                log.debug("Email {} to be sent", email);
                Optional<TUsers> usersOptional = tUsersRepository.findByEmail(email);
                String language = usersOptional.get().getLanguage() != null ? usersOptional.get().getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();
                mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILSHAREDFOLDERUSERTEMPLATE, EmailUtils.prepareContextForSharedFolderUser(email, DossiersUtils.getDossierLabelItem(dossiersOptional.get().getYear_doss(), dossiersOptional.get().getNum_doss()), usersOptional.get().getFullname(), lawfirmToken.getVcKey(), lawfirmToken.getClientFrom()), language);
            });
        }
    }

    private List<String> createShareDossier(LawfirmToken
                                                    lawfirmToken, List<LawfirmUsers> lawfirmUsersList, EnumVCOwner vcOwner, Long affaireId) {
        log.debug("Entering createShareDossier {} , vcOwner {}, affaireId {}, username {}", lawfirmUsersList, vcOwner, affaireId, lawfirmToken.getUsername());
        List<String> emails = new ArrayList<>();

        for (LawfirmUsers lawfirmUsers : lawfirmUsersList) {
            TDossierRights tDossierRights = new TDossierRights();
            tDossierRights.setDossierId(affaireId);
            tDossierRights.setVcUserId(lawfirmUsers.getId());
            tDossierRights.setVcOwner(vcOwner);
            tDossierRights.setRIGHTS("ACCESS");
            tDossierRights.setCreUser(lawfirmToken.getUsername());
            tDossierRightsRepository.save(tDossierRights);

            emails.add(lawfirmUsers.getUser().getEmail());
        }

        log.debug("emails to register {}", emails);


        List<ShareAffaireDTO> shareAffaireDTOS = tDossierRightsRepository.findShareUserByAffaireId(affaireId);

        caseProducer.shareUserToDossier(lawfirmToken, shareAffaireDTOS);

        log.info("Share user sent to transparency");


        return emails;
    }

    @Override
    public void deleteShareFolderUser(ShareAffaireDTO shareAffaireDTO) {
        log.debug("Enrering deleteShareUser shareAffaireDTO {}", shareAffaireDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (shareAffaireDTO.getAffaireId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object has no affaire id selected");
        }

        Optional<TDossiers> dossiersOptional = dossierRepository.findByIdDossAndVcKey(shareAffaireDTO.getAffaireId(), shareAffaireDTO.getVcKey());

        if (dossiersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier does not exist");
        }

        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(shareAffaireDTO.getVcKey(), shareAffaireDTO.getUserId());

        Long countOwner = tDossierRightsRepository.countByDossierIdAndVcOwnerAndvcUserId(shareAffaireDTO.getAffaireId(), lawfirmUsersOptional.get().getId());

        if (countOwner > 0 || shareAffaireDTO.getEnumVCOwner().equals(EnumVCOwner.OWNER_VC)) {
            log.warn("VC key {} and this user {} cannot be deleted", shareAffaireDTO.getVcKey(), shareAffaireDTO.getUserId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This user cannot be deleted");
        }

        Long client = tFacturesRepository.countAllByVcKeyAndClient(lawfirmToken.getVcKey());

        if (client == 0) {
            log.debug("no client found");
            List<VirtualcabClient> virtualcabClientOptional = virtualcabClientRepository.findByLawfirm(lawfirmToken.getVcKey());
            virtualcabClientRepository.deleteAll(virtualcabClientOptional);
        }


        tDossierRightsRepository.deleteAllByVcUserIdAndDossierId(shareAffaireDTO.getAffaireId(), lawfirmUsersOptional.get().getId());

        String objPath = getObjShared(dossiersOptional.get().getYear_doss(), dossiersOptional.get().getNum_doss());
        log.debug("objPath {} to delete", objPath);

        Optional<TObjShared> optionalTObjShared = tObjSharedRepository.findByVcKeyAndObj(shareAffaireDTO.getVcKey(), objPath);

        optionalTObjShared.ifPresent(objShared -> {
            log.debug("objShared found");
            Optional<TUsers> usersOptional = tUsersRepository.findById(shareAffaireDTO.getUserId());

            usersOptional.ifPresent(user -> {
                log.debug("Start delete tObjSharedWith");

                tObjSharedWithRepository.deleteByObjAndUserTo(objShared.getId(), user.getId());
                log.debug("end delete tObjSharedWith");
                List<TObjSharedWith> sharedWithList = tObjSharedWithRepository.findByObjId(objShared.getId());
                if (sharedWithList == null || sharedWithList.isEmpty()) {
                    log.debug("Start delete tObjShared");
                    tObjSharedRepository.delete(objShared);
                    log.debug("end delete tObjShared");
                }
            });
        });

        List<ShareAffaireDTO> shareAffaireDTOS = tDossierRightsRepository.findShareUserByAffaireId(shareAffaireDTO.getAffaireId());
        caseProducer.shareUserToDossier(lawfirmToken, shareAffaireDTOS);


    }

    @Override
    public void switchDossierDigital(Long dossierId, Long userId, String vcKey) {
        log.debug("Entering switchDossierDigital dossier id {},  user id {} and vckey {} ", dossierId, userId, vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);


        if (lawfirmUsers.isPresent()) {
            Optional<TDossiers> dossiersOptional = dossierRepository.findAuthorizedByIdDoss(dossierId, lawfirmUsers.get().getId());

            if (dossiersOptional.isPresent()) {
                dossiersOptional.get().setIsDigital(true);
                dossierRepository.save(dossiersOptional.get());

                log.debug("dossier {} updated with digital true", dossierId);
            }
        }
    }

    @Override
    public String inviteConseil(Long dossierId, ItemPartieDTO partieDTO) {
        log.debug("Entering inviteConseil dossier id {},  partieDTO {} ", dossierId, partieDTO);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!partieDTO.isLitigant()) {
            Optional<TUsers> tUsersOptional = tUsersRepository.findByEmail(partieDTO.getEmail());

            boolean createVcKey = false;
            String vckey = "";

            // check if user exists
            if (tUsersOptional.isPresent()) {
                log.debug("User exists {}", partieDTO.getEmail());
                // check if it's in the same vc key
                Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), tUsersOptional.get().getId());

                if (lawfirmUsersOptional.isPresent()) {
                    log.debug("Lawfirm User id exists {}", lawfirmUsersOptional.get().getId());
                    vckey = lawfirmUsersOptional.get().getLawfirm().getVckey();
                } else {
                    List<LawfirmUsers> lawfirmUsersByUserId = lawfirmUserRepository.findLawfirmUsersByUserIdAndIsSelected(tUsersOptional.get().getId(), true);

                    if (lawfirmUsersByUserId != null && !lawfirmUsersByUserId.isEmpty()) {
                        log.debug("Lawfirm User select id exists {}", lawfirmUsersByUserId.get(0).getId());
                        vckey = lawfirmUsersByUserId.get(0).getLawfirm().getVckey();
                    } else {
                        createVcKey = true;
                    }
                }
            } else {
                createVcKey = true;
            }

            log.info("Create a temp vc key {}", createVcKey);

            if (createVcKey) {
                lawfirmV2Service.createTempVc(partieDTO.getEmail(), lawfirmToken.getClientFrom());
                Optional<TUsers> usersOptional = tUsersRepository.findByEmail(partieDTO.getEmail());

                // must be present because it has been created
                if (usersOptional.isPresent()) {
                    usersOptional.get().setIdValid(EnumValid.VERIFIED);
                    usersOptional.get().setHashkey("");

                    tUsersRepository.save(usersOptional.get());
                    Optional<LawfirmUsers> optionalLawfirmUsers = usersOptional.get().getLawfirmUsers().stream().findFirst();
                    vckey = optionalLawfirmUsers.get().getLawfirm().getVckey();
                }
                log.info("Create temp vc key  {}", vckey);
            }

            Optional<TUsers> usersOptional = tUsersRepository.findByEmail(partieDTO.getEmail());

            caseProducer.createLawfirmMessage(lawfirmToken, vckey, partieDTO.getEmail(), lawfirmToken.getLanguage(), usersOptional.get().getId());
            log.info("Leaving  inviteConseil {} and vckey {} ", dossierId, vckey);

            return vckey;
        } else {
            log.warn("New Partie cannot be a litigant {}", partieDTO);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "New Partie cannot be a litigant");
        }
    }

    private String getObjShared(String year_doss, Long num_doss) {
        return DriveUtils.DOSSIER_PATH + year_doss + "/" + DossiersUtils.getDossierNum(num_doss) + "/";
    }

}