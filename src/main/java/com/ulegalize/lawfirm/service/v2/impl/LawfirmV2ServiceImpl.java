package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.kafka.producer.payment.ILawfirmProducer;
import com.ulegalize.lawfirm.model.DefaultLawfirmDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPublicConverter;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumSequenceType;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.service.SearchService;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.service.v2.cache.CacheService;
import com.ulegalize.lawfirm.service.v2.cache.CacheUtils;
import com.ulegalize.lawfirm.utils.DefaultLawfirm;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.lawfirm.utils.Utils;
import com.ulegalize.lawfirm.utils.VirtualcabNomenclatureUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LawfirmV2ServiceImpl implements LawfirmV2Service {

    @Value("${name-temporary-vckey}")
    private String TEMP_VCKEY;

    private final MailService mailService;

    private final LawfirmRepository lawfirmRepository;
    private final TUsersRepository tUsersRepository;
    private final TSequenceRepository tSequenceRepository;
    private final TStripeSubscribersRepository tStripeSubscribersRepository;
    private final TVcGroupmentRepository tVcGroupmentRepository;
    private final TTemplatesRepository tTemplatesRepository;
    private final RefPosteRepository refPosteRepository;
    private final TDebourTypeRepository tDebourTypeRepository;
    private final TTimesheetTypeRepository timesheetTypeRepository;
    private final TSecurityGroupsRepository tSecurityGroupsRepository;
    private final TDossierRightsRepository tDossierRightsRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final UserV2Service userV2Service;
    private final ILawfirmProducer lawfirmProducer;
    private final EntityToLawfirmPublicConverter entityToLawfirmPublicConverter;

    private final EntityToUserConverter entityToUserConverter;
    private final SearchService searchService;
    private final CacheService cacheService;

    public LawfirmV2ServiceImpl(LawfirmRepository lawfirmRepository,
                                TUsersRepository tUsersRepository,
                                TSequenceRepository tSequenceRepository,
                                TStripeSubscribersRepository tStripeSubscribersRepository,
                                TVcGroupmentRepository tVcGroupmentRepository,
                                TTemplatesRepository tTemplatesRepository,
                                RefPosteRepository refPosteRepository,
                                TDebourTypeRepository tDebourTypeRepository,
                                TTimesheetTypeRepository timesheetTypeRepository,
                                TSecurityGroupsRepository tSecurityGroupsRepository,
                                TDossierRightsRepository tDossierRightsRepository, LawfirmUserRepository lawfirmUserRepository,
                                UserV2Service userV2Service, ILawfirmProducer lawfirmProducer,
                                EntityToLawfirmPublicConverter entityToLawfirmPublicConverter, SearchService searchService, MailService mailService,
                                EntityToUserConverter entityToUserConverter, CacheService cacheService) {
        this.lawfirmRepository = lawfirmRepository;
        this.tUsersRepository = tUsersRepository;
        this.tSequenceRepository = tSequenceRepository;
        this.tStripeSubscribersRepository = tStripeSubscribersRepository;
        this.tVcGroupmentRepository = tVcGroupmentRepository;
        this.tTemplatesRepository = tTemplatesRepository;
        this.refPosteRepository = refPosteRepository;
        this.tDebourTypeRepository = tDebourTypeRepository;
        this.timesheetTypeRepository = timesheetTypeRepository;
        this.tSecurityGroupsRepository = tSecurityGroupsRepository;
        this.tDossierRightsRepository = tDossierRightsRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.userV2Service = userV2Service;
        this.lawfirmProducer = lawfirmProducer;
        this.entityToLawfirmPublicConverter = entityToLawfirmPublicConverter;
        this.searchService = searchService;
        this.mailService = mailService;
        this.entityToUserConverter = entityToUserConverter;
        this.cacheService = cacheService;
    }


    @Override
    @Transactional
    public String createTempVc(String userEmail, String clientFrom, boolean isEmailVerified) throws ResponseStatusException {
        log.info("Entering createTempVc {}", userEmail);
        Optional<TSequences> tSequences = tSequenceRepository.maxSequenceById(EnumSequenceType.TEMP_VC);

        if (tSequences.isPresent()) {
            Long sequenceNumber = tSequences.get().getSequenceNumber();

            log.info("Sequence number for the new vc ke {}", sequenceNumber);

            tSequences.get().setSequenceNumber(sequenceNumber + 1);

            // save the new sequence for the next vc
            tSequenceRepository.save(tSequences.get());

            String tempVc = TEMP_VCKEY + sequenceNumber;
            // create the new vc
            createSingleVcKey(userEmail, tempVc, clientFrom, false, EnumLanguage.FR, "BE", isEmailVerified);

            log.debug("LEAVING createTempVc TEMP vckey {}", tempVc);
            // switch selected vcKey
            return tempVc;

        }
        log.debug("No sequence found");

        return null;
    }

    @Override
    @Transactional
    public Boolean deleteTempVcKey(String vcKey) {
        log.info("Entering deleteTempVcKey vcKey: {} ", vcKey);

        List<TSecurityGroups> tSecurityGroups = tSecurityGroupsRepository.findAllByVcKey(vcKey);
        if (tSecurityGroups != null) {
            tSecurityGroupsRepository.deleteAll(tSecurityGroups);
        }
        // no delete for t_virtualcab_users because it's an update during validation of temporary vckey
        lawfirmRepository.deleteById(vcKey);

        log.info("old temp vc: {} has been deleted", vcKey);

        return true;

    }

    @Override
    @Transactional
    public ProfileDTO validateVc(String newVcKey, Long userId, String userEmail) {
        log.debug("Entering validateVc vcKey {} and user id {}", newVcKey, userId);

        // verify the user
        Optional<TUsers> usersOptional = tUsersRepository.findByEmail(userEmail);

        if (usersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is unknown");
        }

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(newVcKey);

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is unknown");
        }

        lawfirmEntityOptional.get().setTemporaryVc(false);

        lawfirmRepository.save(lawfirmEntityOptional.get());
        log.debug("Lawfirm is not temporary {} anymore", newVcKey);

        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(newVcKey, usersOptional.get().getId());

        if (lawfirmUsers.isPresent()) {
            lawfirmUsers.get().setSelected(true);

            lawfirmUserRepository.save(lawfirmUsers.get());
        }

        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        // send to payment module the info for invoice
        LawfirmDTO lawfirmDTO = entityToLawfirmPublicConverter.apply(lawfirmEntityOptional.get(), false);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // the vckey here is not yet configured in AUTH0
        // so update the vcKey into the token
        lawfirmToken.setVcKey(newVcKey);

        lawfirmProducer.updateLawfirm(lawfirmDTO, lawfirmToken);

        return new ProfileDTO(usersOptional.get().getId(), usersOptional.get().getFullname(), usersOptional.get().getEmail(),
                "", lawfirmEntityOptional.get().getVckey(),
                false,
                usersOptional.get().getLanguage(),
                lawfirmEntityOptional.get().getCurrency().getSymbol(),
                usersOptional.get().getId(),
                usersOptional.get().getIdUser(),
                null,
                lawfirmEntityOptional.get().getDriveType(),
                lawfirmEntityOptional.get().getDropboxToken(),
                lawfirmEntityOptional.get().getOnedriveToken(),
                lawfirmEntityOptional.get().getRefreshToken(),
                lawfirmEntityOptional.get().getExpireToken(),
                usersOptional.get().getIdValid().equals(EnumValid.VERIFIED),
                lawfirmEntityOptional.get().getClientFrom());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NumberFormatException.class)
    public ProfileDTO updateTempVcKey(ProfileDTO userProfile, DefaultLawfirmDTO defaultLawfirmDTO) {
        log.debug("Entering updateTempVcKey old vckey {} , new vckey {}", userProfile, defaultLawfirmDTO);

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(defaultLawfirmDTO.getLanguage());

        if (enumLanguage == null) {
            log.warn("Language is empty for this vckey (not found) {}", userProfile.getVcKeySelected());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Language is empty for this vckey");
        }

        Optional<LawfirmEntity> entityOptional = lawfirmRepository.findById(userProfile.getVcKeySelected());

        if (entityOptional.isEmpty()) {
            log.warn("Something wrong with this vckey (not found) {}", userProfile.getVcKeySelected());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something wrong with this vckey ");
        }

        if (defaultLawfirmDTO.getLawfirmDTO() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm must be filled in");
        }

        if (defaultLawfirmDTO.getVcKey() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm must be filled in");
        }

        defaultLawfirmDTO.setVcKey(defaultLawfirmDTO.getVcKey().toUpperCase());

        // default value
        List<ItemVatDTO> itemVatDTOList;
        EnumRefCurrency currency = EnumRefCurrency.EUR;
        String countryCode = Utils.countryIso2Code(defaultLawfirmDTO.getLawfirmDTO().getCountryCode(), enumLanguage.getShortCode());

        if (defaultLawfirmDTO.getLawfirmDTO().getCurrency() != null) {
            currency = defaultLawfirmDTO.getLawfirmDTO().getCurrency();
        }
        // check if it exists and if it's null getdefault
        if (defaultLawfirmDTO.getItemVatDTOList() == null || defaultLawfirmDTO.getItemVatDTOList().size() == 0) {
            log.warn("Vat list must be greather than 0 with this vckey {}", userProfile.getVcKeySelected());

            itemVatDTOList = searchService.getDefaultVatsByCountryCode(countryCode);
        } else {
            itemVatDTOList = defaultLawfirmDTO.getItemVatDTOList();
        }

        if (itemVatDTOList == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vat list must be greather than 0");
        }
        // if at least one default
        boolean anyMatch = itemVatDTOList.stream().anyMatch(ItemVatDTO::getIsDefault);

        if (!anyMatch) {
            log.warn("Vat list must be have at least one default  with this vckey {}", userProfile.getVcKeySelected());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vat list must be have at least one default ");
        }
        log.info("Old vc key {} update with thenew one {}", userProfile.getVcKeySelected(), defaultLawfirmDTO.getVcKey());
        if (!defaultLawfirmDTO.getVcKey().equalsIgnoreCase((userProfile.getVcKeySelected()))) {

            // 1. create a new one
            Optional<TUsers> tUsersOptional = tUsersRepository.findByEmail(userProfile.getEmail());

            if (tUsersOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something wrong with this user");
            }

            // check if the new vckey exist
            Optional<LawfirmEntity> optionalLawfirm = lawfirmRepository.findLawfirmByVckey(defaultLawfirmDTO.getVcKey());

            if (optionalLawfirm.isPresent()) {
                log.warn("Lawfirm with the name {} already exists", defaultLawfirmDTO.getVcKey());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lawfirm with the name " + defaultLawfirmDTO.getVcKey() + " already exists");
            }

            // verify TEMP_VCKEY is in the new name
            if (defaultLawfirmDTO.getVcKey().contains(TEMP_VCKEY)) {
                // check the number after ULEGAL
                String restvalue = defaultLawfirmDTO.getVcKey().replace(TEMP_VCKEY, "");
                String restOriginalVcKey = userProfile.getVcKeySelected().replace(TEMP_VCKEY, "");

                try {
                    int numberRestValue = Integer.parseInt(restvalue);
                    int numberRestOriginal = Integer.parseInt(restOriginalVcKey);

                    // restValue cannot be > restoOriginalVcKey
                    if (numberRestValue > numberRestOriginal) {
                        log.warn("Lawfirm with the name {} cannot have a sequence number {} > the original temp number {}", defaultLawfirmDTO.getVcKey(), numberRestValue, numberRestOriginal);
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lawfirm with the name " + defaultLawfirmDTO.getVcKey() + " cannot have a sequence number " + numberRestValue + " > the original temp number " + numberRestOriginal);
                    }
                } catch (NumberFormatException ex) {
                    log.warn("Value are not number, everything it's ok");
                }
            }

            tUsersOptional.get().setLanguage(enumLanguage.getShortCode());

            tUsersRepository.save(tUsersOptional.get());

            LawfirmEntity newLawfirm = new LawfirmEntity();
            newLawfirm.setVckey(defaultLawfirmDTO.getVcKey());
            newLawfirm.setName(defaultLawfirmDTO.getVcKey());
            newLawfirm.setAlias(defaultLawfirmDTO.getVcKey().toLowerCase());
            newLawfirm.setCouthoraire(entityOptional.get().getCouthoraire());
            // check if the email has been fill in
            if (defaultLawfirmDTO.getLawfirmDTO().getEmail() != null) {
                newLawfirm.setEmail(defaultLawfirmDTO.getLawfirmDTO().getEmail().toLowerCase());
            } else {
                newLawfirm.setEmail(entityOptional.get().getEmail());
            }
            log.debug("New lawfirm email {}", newLawfirm.getEmail());

            newLawfirm.setCity(entityOptional.get().getCity());
            newLawfirm.setCountryCode(countryCode);
            newLawfirm.setCurrency(currency);
            newLawfirm.setLicenseId(entityOptional.get().getLicenseId());
            newLawfirm.setDriveType(entityOptional.get().getDriveType());
            newLawfirm.setDropboxToken(entityOptional.get().getDropboxToken());
            newLawfirm.setTemporaryVc(entityOptional.get().getTemporaryVc());
            newLawfirm.setClientFrom(entityOptional.get().getClientFrom());
            if (defaultLawfirmDTO.getLawfirmDTO().getAbbreviation() != null) {
                newLawfirm.setAbbreviation(defaultLawfirmDTO.getLawfirmDTO().getAbbreviation());
            }
            if (defaultLawfirmDTO.getLawfirmDTO().getObjetsocial() != null) {
                newLawfirm.setObjetsocial(defaultLawfirmDTO.getLawfirmDTO().getObjetsocial());
            }
            if (defaultLawfirmDTO.getLawfirmDTO().getNumentreprise() != null) {
                newLawfirm.setCompanyNumber(defaultLawfirmDTO.getLawfirmDTO().getNumentreprise());
            }
            newLawfirm.setUserUpd(entityOptional.get().getUserUpd());
            newLawfirm.setStartInvoiceNumber(entityOptional.get().getStartInvoiceNumber());
            newLawfirm.setCreUser(userProfile.getUserLoginId());

            newLawfirm.setLawfirmUsers(new ArrayList<>());
            newLawfirm.setTVirtualcabVatList(entityOptional.get().getTVirtualcabVatList());

            newLawfirm.getLawfirmUsers().addAll(entityOptional.get().getLawfirmUsers());
            newLawfirm.getLawfirmUsers().forEach(lawfirmUsers -> lawfirmUsers.setLawfirm(newLawfirm));

            LawfirmWebsiteEntity lawfirmWebsiteEntity = new LawfirmWebsiteEntity();
            lawfirmWebsiteEntity.setVcKey(defaultLawfirmDTO.getVcKey());
            lawfirmWebsiteEntity.setAbout(entityOptional.get().getLawfirmWebsite().getAbout());
            lawfirmWebsiteEntity.setAcceptAppointment(entityOptional.get().getLawfirmWebsite().isAcceptAppointment());
            lawfirmWebsiteEntity.setActive(entityOptional.get().getLawfirmWebsite().isActive());
            lawfirmWebsiteEntity.setIntro(entityOptional.get().getLawfirmWebsite().getIntro());
            lawfirmWebsiteEntity.setPhilosophy(entityOptional.get().getLawfirmWebsite().getPhilosophy());
            lawfirmWebsiteEntity.setTitle(entityOptional.get().getLawfirmWebsite().getTitle());
            lawfirmWebsiteEntity.setUpdDate(entityOptional.get().getLawfirmWebsite().getUpdDate());
            lawfirmWebsiteEntity.setUpdUser(entityOptional.get().getLawfirmWebsite().getUpdUser());
            newLawfirm.setLawfirmWebsite(lawfirmWebsiteEntity);

            procedureDefinitiveEntitiesForCreation(newLawfirm, enumLanguage, itemVatDTOList);

            log.debug("Leaving copyTempVcKey old vckey {} , new vckey {}", userProfile, defaultLawfirmDTO);

            // 2. update all entities

            List<TSecurityGroups> securityGroups = tSecurityGroupsRepository.findAllByVcKey(userProfile.getVcKeySelected());
            securityGroups.forEach(groupment -> groupment.setVcKey(defaultLawfirmDTO.getVcKey()));
            tSecurityGroupsRepository.saveAll(securityGroups);
//
            // get vc user id from old vckey
            for (LawfirmUsers lawfirmUsers : entityOptional.get().getLawfirmUsers()) {
                List<TDossierRights> tDossierRights = tDossierRightsRepository.findAllByVcUserId(lawfirmUsers.getId());
                if (tDossierRights != null && !tDossierRights.isEmpty()) {
                    log.debug(" dossier right is existing and will be changed");
                    // get the same user from old virtual_cab
                    Optional<LawfirmUsers> lawfirmUsersOptional = newLawfirm.getLawfirmUsers().stream().filter(lawfirmUsers1 ->
                            lawfirmUsers1.getUser().getId().equals(lawfirmUsers.getUser().getId())
                    ).findAny();
                    log.debug("New Lawfirm user {} will update dossier right ", lawfirmUsersOptional);
                    if (lawfirmUsersOptional.isPresent()) {
                        tDossierRights.forEach(dossierRights -> dossierRights.setVcUserId(lawfirmUsersOptional.get().getId()));
                        tDossierRightsRepository.saveAll(tDossierRights);
                    }
                }
            }

            // 3. delete the old one
            deleteTempVcKey(userProfile.getVcKeySelected());
        } else {
            if (defaultLawfirmDTO.getLawfirmDTO().getAbbreviation() != null) {
                entityOptional.get().setAbbreviation(defaultLawfirmDTO.getLawfirmDTO().getAbbreviation());
            }
            if (defaultLawfirmDTO.getLawfirmDTO().getObjetsocial() != null) {
                entityOptional.get().setObjetsocial(defaultLawfirmDTO.getLawfirmDTO().getObjetsocial());
            }
            if (defaultLawfirmDTO.getLawfirmDTO().getNumentreprise() != null) {
                entityOptional.get().setCompanyNumber(defaultLawfirmDTO.getLawfirmDTO().getNumentreprise());
            }
            entityOptional.get().setEmail(defaultLawfirmDTO.getLawfirmDTO().getEmail());
            entityOptional.get().setCurrency(currency);
            entityOptional.get().setCountryCode(countryCode);
            entityOptional.get().setUserUpd(entityOptional.get().getUserUpd());

            if (entityOptional.get().getTVirtualcabVatList() == null) {
                entityOptional.get().setTVirtualcabVatList(new ArrayList<>());

            }

            procedureDefinitiveEntitiesForCreation(entityOptional.get(), enumLanguage, itemVatDTOList);

        }

        return validateVc(defaultLawfirmDTO.getVcKey(), userProfile.getUserId(), userProfile.getEmail());
    }

    @Override
    @Transactional
    public LawfirmDTO getLawfirmInfoByVcKey(String vckey) {
        log.debug("Entering getLawfirmInfoByVcKey");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getLawfirmInfoByVcKey vckey {}", lawfirmToken.getVcKey());

        Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(vckey);

        if (lawfirmDTOOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is not found");
        }

        return lawfirmDTOOptional.get();
    }

    @Override
    @Transactional
    public LawfirmDTO updateLawfirmInfoByVcKey(LawfirmDTO lawfirmDTO) {
        log.debug("Entering updateLawfirmInfoByVcKey");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is not found");
        }

        LawfirmEntity oldLawfirmInfo = lawfirmEntityOptional.get();
        //GeneralTab
        oldLawfirmInfo.setVckey(lawfirmToken.getVcKey());
        oldLawfirmInfo.setAbbreviation(lawfirmDTO.getAbbreviation());
        oldLawfirmInfo.setObjetsocial(lawfirmDTO.getObjetsocial());
        oldLawfirmInfo.setCompanyNumber(lawfirmDTO.getNumentreprise());
        oldLawfirmInfo.setEmail(lawfirmDTO.getEmail());
        oldLawfirmInfo.setCurrency(lawfirmDTO.getCurrency());
        //AddressTab
        oldLawfirmInfo.setStreet(lawfirmDTO.getStreet());
        oldLawfirmInfo.setCity(lawfirmDTO.getCity());
        oldLawfirmInfo.setPostalCode(lawfirmDTO.getPostalCode());
        oldLawfirmInfo.setCountryCode(lawfirmDTO.getCountryCode());
        //ContactTab
        oldLawfirmInfo.setFax(lawfirmDTO.getFax());
        oldLawfirmInfo.setWebsite(lawfirmDTO.getWebsite());
        oldLawfirmInfo.setPhoneNumber(lawfirmDTO.getPhoneNumber());
        //ExtraTab
        oldLawfirmInfo.setCouthoraire(lawfirmDTO.getCouthoraire());
        oldLawfirmInfo.setLogo(lawfirmDTO.getLogo());


        if (lawfirmDTO.getStartInvoiceNumber() != null && lawfirmDTO.getStartInvoiceNumber() >= oldLawfirmInfo.getStartInvoiceNumber()) {
            oldLawfirmInfo.setStartInvoiceNumber(lawfirmDTO.getStartInvoiceNumber());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invoice number cannot be lower than the one previously entered");
        }

        if (lawfirmDTO.getStartDossierNumber() != null && lawfirmDTO.getStartDossierNumber() >= oldLawfirmInfo.getStartDossierNumber()) {
            oldLawfirmInfo.setStartDossierNumber(lawfirmDTO.getStartDossierNumber());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Dossier number cannot be lower than the one previously entered");
        }

        if (lawfirmDTO.getIsNotification() != null && oldLawfirmInfo.getNotification() != null && !lawfirmDTO.getIsNotification().equals(oldLawfirmInfo.getNotification())) {
            oldLawfirmInfo.setNotification(lawfirmDTO.getIsNotification());
            log.debug("notification updated {}", oldLawfirmInfo.getNotification());

            log.debug("Starting lawfirmProducer method updateNotificationLawfirm");

            lawfirmProducer.updateNotificationLawfirm(lawfirmDTO, lawfirmToken);

            log.debug("producer updateNotificationLawfirm sent vckey {}", lawfirmToken.getVcKey());
        }


        lawfirmRepository.save(oldLawfirmInfo);

        log.debug("updateLawfirmInfoByVcKey vckey {}", lawfirmToken.getVcKey());

        lawfirmDTO.setLawyers(new ArrayList<LawyerDTO>());

        oldLawfirmInfo.getLawfirmUsers().stream().forEach(user -> {
            LawyerDTO l = entityToUserConverter.apply(user.getUser(), false);
            l.setSelected(user.isSelected());

            lawfirmDTO.getLawyers().add(l);
        });
        lawfirmProducer.updateLawfirm(lawfirmDTO, lawfirmToken);

        log.debug("producer updateLawfirm sent vckey {}", lawfirmToken.getVcKey());

        return getLawfirmInfoByVcKey(lawfirmToken.getVcKey());
    }

    @Override
    public LawfirmDTO updateLawfirmDriveInfo(LawfirmDTO lawfirmDTO) {
        log.debug("Entering updateLawfirmDriveInfo");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirm is not found");
        }
        LawfirmEntity oldLawfirmInfo = lawfirmEntityOptional.get();
        // if it's different remove cache
        if (lawfirmDTO.getDriveType() != null && !lawfirmDTO.getDriveType().equals(oldLawfirmInfo.getDriveType())) {
            oldLawfirmInfo.setDriveType(lawfirmDTO.getDriveType());

            cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);
        }
        lawfirmRepository.save(oldLawfirmInfo);

        LawfirmDriveDTO lawfirmDriveDTO = new LawfirmDriveDTO();
        lawfirmDriveDTO.setDriveType(oldLawfirmInfo.getDriveType());
        lawfirmDriveDTO.setDropboxToken(oldLawfirmInfo.getDropboxToken());
        lawfirmDriveDTO.setOnedriveToken(oldLawfirmInfo.getOnedriveToken());
        lawfirmDriveDTO.setRefreshToken(oldLawfirmInfo.getRefreshToken());
        lawfirmDriveDTO.setExpireToken(oldLawfirmInfo.getExpireToken());

        lawfirmProducer.updateLawfirmDrive(lawfirmDriveDTO, lawfirmToken);
        log.debug("producer updateLawfirmDrive sent vckey {}", lawfirmToken.getVcKey());

        return getLawfirmInfoByVcKey(lawfirmToken.getVcKey());
    }

    @Override
    @Transactional
    public List<LawfirmDTO> searchLawfirmInfoByVcKey(String name) {
        log.debug("Entering searchLawfirmInfoByVcKey {}", name);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("connected {} and user id {}, searchLawfirmInfoByVcKey vckey {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId(), name);

        List<LawfirmEntity> lawfirmEntities = lawfirmRepository.searchLawfirmDTOByVckey(name.toUpperCase(), lawfirmToken.getVcKey(), EnumStatusAssociation.ACCEPTED);

        return entityToLawfirmPublicConverter.convertToList(lawfirmEntities, false);
    }

    @Override
    @Transactional
    public List<LawfirmDTO> searchLawfirmInfoByVcKeyAndStatusAssociation(String name) {

        log.debug("Entering searchLawfirmInfoByVcKeyAndStatusAssociation {}", name);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("connected {} and user id {}, searchLawfirmInfoByVcKey vckey {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId(), name);

        List<LawfirmEntity> lawfirmEntities = lawfirmRepository.searchLawfirmDTOByVckeyAndStatusAssociation(name.toUpperCase(), lawfirmToken.getVcKey().toUpperCase(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

        return entityToLawfirmPublicConverter.convertToList(lawfirmEntities, false);
    }

    @Override
    @Transactional
    public String uploadImageVirtualcab(byte[] bytes) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering uploadImageVirtualcab {}", lawfirmToken.getVcKey());
        Optional<LawfirmEntity> optionalLawfirmEntity = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());
        if (optionalLawfirmEntity.isEmpty()) {
            log.error("Unknown lawfirm {} ", lawfirmToken.getVcKey());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown lawfirm " + lawfirmToken.getVcKey());
        }
        int imgSize = bytes.length;

        // max size image > 1Mb
        if (imgSize > 1000000) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image size to heavy > 1Mb " + lawfirmToken.getVcKey());
        }
        optionalLawfirmEntity.get().setLogo(bytes);

        lawfirmRepository.save(optionalLawfirmEntity.get());
        log.info("Lawfirm logo avatar updated {}", optionalLawfirmEntity.get().getVckey());

        return optionalLawfirmEntity.get().getVckey();
    }

    @Override
    @Transactional
    public LawfirmDriveDTO updateToken(LawfirmDriveDTO lawfirmDriveDTO) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("updateToken by vckey {}", lawfirmToken.getVcKey());

        if (lawfirmDriveDTO.getDriveType() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "drive type not found");
        }
        if (lawfirmDriveDTO.getDriveType().equals(DriveType.dropbox)
                && (lawfirmDriveDTO.getDropboxToken() == null || lawfirmDriveDTO.getDropboxToken().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dropbox token not found");
        }
        if (lawfirmDriveDTO.getDriveType().equals(DriveType.onedrive)
                && (lawfirmDriveDTO.getOnedriveToken() == null || lawfirmDriveDTO.getOnedriveToken().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Onedrive token not found");
        }

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        lawfirmEntityOptional.ifPresent(lawfirm -> {
            lawfirm.setDriveType(lawfirmDriveDTO.getDriveType());
            if (lawfirmDriveDTO.getDriveType().equals(DriveType.dropbox)) {
                lawfirm.setDropboxToken(lawfirmDriveDTO.getDropboxToken());
            } else if (lawfirmDriveDTO.getDriveType().equals(DriveType.onedrive)) {
                lawfirm.setOnedriveToken(lawfirmDriveDTO.getOnedriveToken());
                lawfirm.setRefreshToken(lawfirmDriveDTO.getRefreshToken());
                if (lawfirmDriveDTO.getExpireToken() != null) {
                    lawfirm.setExpireToken(lawfirmDriveDTO.getExpireToken());
                } else {
                    // 3600 seconds is the default . To have security I fill in 3500
                    lawfirm.setExpireToken(ZonedDateTime.now());
                }
            }
            cacheService.evictCaches("userProfile");

            lawfirmProducer.updateLawfirmDrive(lawfirmDriveDTO, lawfirmToken);
        });
        return lawfirmDriveDTO;
    }

    @Override
    @Transactional
    public String registerUser(String userEmail, String clientFrom, boolean isVerified) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering registerUser userEmail {} and clientFrom {} and isEmailVerified {}", userEmail, clientFrom, isVerified);

        String tempVc = createTempVc(userEmail, lawfirmToken.getClientFrom(), isVerified);

        String language = lawfirmToken.getLanguage() != null ? lawfirmToken.getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();

        Optional<TUsers> usersOptional = tUsersRepository.findByEmail(userEmail);
        // send email in order verify user
        usersOptional.ifPresent(
                tUsers -> {
                    if (!tUsers.getIdValid().equals(EnumValid.VERIFIED)) {
                        log.debug("User not verified {}", tUsers.getId());

                        mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILVERIFYTEMPLATE,
                                EmailUtils.prepareContextVerifyUser(
                                        tUsers.getEmail(),
                                        tUsers.getEmail(),
                                        tUsers.getHashkey(),
                                        tUsers.getLanguage(),
                                        lawfirmToken.getClientFrom()),
                                language
                        );
                    }
                });

        return tempVc;

    }

    @Override
    public Long getTotalWorkspace() {
        log.debug("Entering getTotalWorkspace");

        return lawfirmRepository.count();
    }

    @Override
    public void createSingleVcKey(String userEmail, String tempVcKey, String clientFrom, boolean fullLawfirm, EnumLanguage enumLanguage, String countryCode, boolean emailVerified) {
        log.debug("Entering createSingleVcKey email {} and  {}", userEmail, tempVcKey);
        TUsers user;
        // check if the user is known
        Optional<TUsers> usersOptional = tUsersRepository.findByEmail(userEmail);

        // NOT EXIST -> new user
        if (usersOptional.isEmpty()) {
            user = userV2Service.createUsers(userEmail, clientFrom, EnumLanguage.FR, emailVerified);

            log.debug("user created");
        } else {
            user = usersOptional.get();
        }

        createVcKey(tempVcKey, user.getId(), countryCode, user, fullLawfirm, enumLanguage);

    }


    private void createVcKey(String vcKey, Long userId, String countryCode, TUsers userEntity, boolean fullLawfirm, EnumLanguage enumLanguage) {
        log.debug("Entering createVcKey vcKey {} and user id {}", vcKey, userId);

        procedureCreation(vcKey, userEntity, countryCode, fullLawfirm, enumLanguage);

        log.debug("Temp vc key {} created", vcKey);

        Optional<TStripeSubscribers> stripeSubscribers = tStripeSubscribersRepository.findByIdUser(userId);

        if (stripeSubscribers.isEmpty()) {
            // init stripe user
            TStripeSubscribers tStripeSubscribers = new TStripeSubscribers();
            tStripeSubscribers.setIdUser(userId);
            tStripeSubscribers.setActivesub(1);
            tStripeSubscribers.setPlan("basic_plan");
            tStripeSubscribers.setValidFrom(LocalDate.now());
            tStripeSubscribers.setValidTo(LocalDate.now().plusYears(5));
            tStripeSubscribersRepository.save(tStripeSubscribers);
        }

        // switch selected to the new one
        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByUserId(userId);

        for (LawfirmUsers lawfirmUsers : lawfirmUsersList) {
            lawfirmUsers.setSelected(false);
            log.debug("The lawfirm user vckey  {}", lawfirmUsers.getLawfirm().getVckey());

            if (lawfirmUsers.getLawfirm().getVckey().equalsIgnoreCase(vcKey)) {
                log.debug("The vc {} is selected ", vcKey);
                lawfirmUsers.setSelected(true);
            }
        }
        log.debug("Lawfirm user found {}", lawfirmUsersList);

        lawfirmUserRepository.saveAll(lawfirmUsersList);

        log.debug("stripe subscribe {} created", userId);
    }

    private void procedureDefinitiveEntitiesForCreation(LawfirmEntity lawfirmEntity, EnumLanguage enumLanguage, List<ItemVatDTO> itemVatDTOS) {
        log.debug("Entering procedureDefinitiveEntitiesForCreation for vckey {}", lawfirmEntity.getVckey());

        if (lawfirmEntity.getTVirtualcabVatList() == null) {
            lawfirmEntity.setTVirtualcabVatList(new ArrayList<>());
        } else {
            lawfirmEntity.getTVirtualcabVatList().clear();
        }

        for (ItemVatDTO itemVatDTO : itemVatDTOS) {
            TVirtualcabVat tVirtualcabVat = new TVirtualcabVat();
            tVirtualcabVat.setVcKey(lawfirmEntity.getVckey());
            tVirtualcabVat.setVAT(itemVatDTO.getValue());
            tVirtualcabVat.setCreUser("ulegalize");
            tVirtualcabVat.setIsDefault(itemVatDTO.getIsDefault());
            lawfirmEntity.getTVirtualcabVatList().add(tVirtualcabVat);
        }

        log.debug("vat added for vckey {}", lawfirmEntity.getVckey());

        createDefaultVirtualcabNomenclature(lawfirmEntity);
        log.info("nomenclature added for vckey {}", lawfirmEntity.getVckey());

        // save
        lawfirmRepository.save(lawfirmEntity);

        log.info("Vckey saved {}", lawfirmEntity.getVckey());

        List<TTimesheetType> timesheetTypeList = DefaultLawfirm.getTimesheetType(lawfirmEntity.getVckey(), enumLanguage);
        timesheetTypeRepository.saveAll(timesheetTypeList);

        List<TDebourType> debourTypeList = DefaultLawfirm.getDebourType(lawfirmEntity.getVckey(), enumLanguage);
        tDebourTypeRepository.saveAll(debourTypeList);

        List<RefPoste> refPosteList1 = DefaultLawfirm.getRefPostes(lawfirmEntity.getVckey(), enumLanguage);
        refPosteRepository.saveAll(refPosteList1);

        List<TTemplates> tTemplates = DefaultLawfirm.getTemplates(lawfirmEntity.getVckey(), enumLanguage);
        tTemplatesRepository.saveAll(tTemplates);

        List<TVcGroupment> groupmentList = DefaultLawfirm.getVcGroupment(lawfirmEntity.getVckey());
        tVcGroupmentRepository.saveAll(groupmentList);
        log.debug("Leaving procedureDefinitiveEntitiesForCreation for vckey {}", lawfirmEntity.getVckey());

    }

    private void procedureCreation(String vcKey, TUsers userEntity, String countryCode, boolean fullLawfirm, EnumLanguage enumLanguage) {
        LawfirmEntity newLawfirm = new LawfirmEntity();
        newLawfirm.setVckey(vcKey);
        newLawfirm.setName(vcKey);
        newLawfirm.setAlias(vcKey.toLowerCase());
        newLawfirm.setEmail(userEntity.getEmail());
        newLawfirm.setCity("");
        newLawfirm.setLicenseId(1);
        newLawfirm.setDriveType(DriveType.openstack);
        newLawfirm.setDropboxToken("");
        newLawfirm.setCompanyNumber("");
        newLawfirm.setTemporaryVc(true);
        newLawfirm.setCountryCode(countryCode);
        newLawfirm.setUserUpd("ulegalize");
        newLawfirm.setCouthoraire(145);
        newLawfirm.setObjetsocial("");
        newLawfirm.setStartInvoiceNumber(1);
        newLawfirm.setCreUser(userEntity.getIdUser());
        newLawfirm.setClientFrom(userEntity.getClientFrom());

        newLawfirm.setLawfirmUsers(new ArrayList<>());

        createLawfirmUsers(newLawfirm, userEntity);

        createTSecurityGroups(newLawfirm);

        LawfirmWebsiteEntity lawfirmWebsiteEntity = new LawfirmWebsiteEntity();
        lawfirmWebsiteEntity.setVcKey(vcKey);
        lawfirmWebsiteEntity.setAcceptAppointment(false);
        lawfirmWebsiteEntity.setActive(false);
        lawfirmWebsiteEntity.setUpdDate(new Date());
        lawfirmWebsiteEntity.setUpdUser("ulegalize");
        newLawfirm.setLawfirmWebsite(lawfirmWebsiteEntity);

        if (fullLawfirm) {
            // save is here
            List<ItemVatDTO> itemVatDTOS = searchService.getDefaultVatsByCountryCode(countryCode);
            procedureDefinitiveEntitiesForCreation(newLawfirm, enumLanguage, itemVatDTOS);
        } else {
            lawfirmRepository.save(newLawfirm);
        }

    }

    private void createLawfirmUsers(LawfirmEntity lawfirmEntity, TUsers userEntity) {
        LawfirmUsers lawfirmUsers = new LawfirmUsers();
        Date from = DateUtils.addDays(new Date(), -1);
        Date to = DateUtils.addYears(new Date(), 5);
        lawfirmUsers.setValidFrom(from);
        lawfirmUsers.setValidTo(to);
        lawfirmUsers.setIdRole(EnumRole.AVOCAT);
        lawfirmUsers.setActive(true);
        lawfirmUsers.setPrestataire(true);
        lawfirmUsers.setLawfirm(lawfirmEntity);
        lawfirmUsers.setUser_upd("ulegalize");
        lawfirmUsers.setPublic(false);
        lawfirmUsers.setCouthoraire(0);
        lawfirmUsers.setSelected(true);
        lawfirmEntity.getLawfirmUsers().add(lawfirmUsers);

        lawfirmUsers.setUser(userEntity);
        userEntity.setLawfirmUsers(new ArrayList<>());
        userEntity.getLawfirmUsers().add(lawfirmUsers);

    }

    private void createTSecurityGroups(LawfirmEntity lawfirm) {
        TSecurityGroups tSecurityGroups = new TSecurityGroups();
        tSecurityGroups.setVcKey(lawfirm.getVckey());
        tSecurityGroups.setDescription("admin");
        tSecurityGroups.setTSecAppGroupId(EnumSecurityAppGroups.ADMIN);
        tSecurityGroups.setUserUpd("ulegalize");
        tSecurityGroups.setDateUpd(LocalDateTime.now());


        createTSecurityGroupRights(tSecurityGroups);

        createTSecurityGroupUsers(lawfirm, tSecurityGroups);

        tSecurityGroupsRepository.save(tSecurityGroups);
    }

    private void createTSecurityGroupRights(TSecurityGroups tSecurityGroups) {
        TSecurityGroupRights tSecurityGroupRights = new TSecurityGroupRights();
        tSecurityGroupRights.setIdRight(EnumRights.ADMINISTRATEUR);
        tSecurityGroupRights.setTSecurityGroups(tSecurityGroups);

        tSecurityGroups.setTSecurityGroupRightsList(new ArrayList<>());
        tSecurityGroups.getTSecurityGroupRightsList().add(tSecurityGroupRights);

    }


    private void createTSecurityGroupUsers(LawfirmEntity lawfirm, TSecurityGroups tSecurityGroups) {
        TSecurityGroupUsers tSecurityGroupUsers = new TSecurityGroupUsers();

        tSecurityGroupUsers.setUser(lawfirm.getLawfirmUsers().get(0).getUser());
        tSecurityGroupUsers.setTSecurityGroups(tSecurityGroups);

        tSecurityGroups.setTSecurityGroupUsersList(new ArrayList<>());
        tSecurityGroups.getTSecurityGroupUsersList().add(tSecurityGroupUsers);
    }

    @Override
    public void createDefaultVirtualcabNomenclature(LawfirmEntity lawfirmEntity) {
        log.debug("Entering createDefaultVirtualcabNomenclature for vckey {}", lawfirmEntity.getVckey());

        TVirtualcabNomenclature virtualcabNomenclature = new TVirtualcabNomenclature();
        virtualcabNomenclature.setName(String.valueOf(ZonedDateTime.now().getYear()));
        virtualcabNomenclature.setDrivePath(VirtualcabNomenclatureUtils.VIRTUALNOMENCLATUREYEAR + "/" + VirtualcabNomenclatureUtils.VIRTUALNOMENCLATURENOMENCLATURE);
        virtualcabNomenclature.setLawfirmEntity(lawfirmEntity);
        virtualcabNomenclature.setCreUser(lawfirmEntity.getCreUser());
        if (lawfirmEntity.getVirtualcabNomenclatureList() == null) {
            lawfirmEntity.setVirtualcabNomenclatureList(new ArrayList<>());
        }
        lawfirmEntity.getVirtualcabNomenclatureList().add(virtualcabNomenclature);
        log.debug("Leaving createDefaultVirtualcabNomenclature for vckey {}", lawfirmEntity.getVckey());
    }

}
