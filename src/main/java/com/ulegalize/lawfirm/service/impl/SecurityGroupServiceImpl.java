package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToSecurityGroupConverter;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.service.SecurityGroupService;
import com.ulegalize.lawfirm.service.v2.ClientV2Service;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import com.ulegalize.lawfirm.service.v2.cache.CacheService;
import com.ulegalize.lawfirm.service.v2.cache.CacheUtils;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import com.ulegalize.security.EnumRights;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private final TUsersRepository tUsersRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final LawfirmRepository lawfirmRepository;
    private final TDossierRightsRepository dossierRightsRepository;
    private final DossierRepository dossierRepository;
    private final TSecurityGroupUsersRepository tSecurityGroupUsersRepository;
    private final TSecurityGroupRightsRepository tSecurityGroupRightsRepository;
    private final TSecurityGroupsRepository tSecurityGroupsRepository;
    private final TStripeSubscribersRepository tStripeSubscribersRepository;
    private final TFirstTimeRepository tFirstTimeRepository;
    private final EntityToUserConverter entityToUserConverter;
    private final EntityToSecurityGroupConverter entityToSecurityGroupConverter;
    private final UserV2Service userV2Service;
    private final DossierV2Service dossierV2Service;
    private final MailService mailService;
    private final ClientV2Service clientV2Service;
    private final CacheService cacheService;

    @Autowired
    public SecurityGroupServiceImpl(TUsersRepository tUsersRepository,
                                    LawfirmUserRepository lawfirmUserRepository,
                                    LawfirmRepository lawfirmRepository, TDossierRightsRepository dossierRightsRepository,
                                    DossierRepository dossierRepository, TSecurityGroupUsersRepository tSecurityGroupUsersRepository,
                                    TSecurityGroupRightsRepository tSecurityGroupRightsRepository, TSecurityGroupsRepository tSecurityGroupsRepository,
                                    TStripeSubscribersRepository tStripeSubscribersRepository,
                                    TFirstTimeRepository tFirstTimeRepository, EntityToUserConverter entityToUserConverter,
                                    EntityToSecurityGroupConverter entityToSecurityGroupConverter, UserV2Service userV2Service, DossierV2Service dossierV2Service, MailService mailService, ClientV2Service clientV2Service, CacheService cacheService) {
        this.tUsersRepository = tUsersRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.dossierRightsRepository = dossierRightsRepository;
        this.dossierRepository = dossierRepository;
        this.tSecurityGroupUsersRepository = tSecurityGroupUsersRepository;
        this.tSecurityGroupRightsRepository = tSecurityGroupRightsRepository;
        this.tSecurityGroupsRepository = tSecurityGroupsRepository;
        this.tStripeSubscribersRepository = tStripeSubscribersRepository;
        this.tFirstTimeRepository = tFirstTimeRepository;
        this.entityToUserConverter = entityToUserConverter;
        this.entityToSecurityGroupConverter = entityToSecurityGroupConverter;
        this.userV2Service = userV2Service;
        this.dossierV2Service = dossierV2Service;
        this.mailService = mailService;
        this.clientV2Service = clientV2Service;
        this.cacheService = cacheService;
    }

    @Override
    @Cacheable(value = "userProfile", key = "#userEmail")
    public ProfileDTO getUserProfile(String clientFrom, String userEmail, boolean withSecurity, boolean emailVerified) {
        log.debug("Entering getUserProfile email {}", userEmail);
        return profile(clientFrom, userEmail, withSecurity, emailVerified);

    }

    @Override
    public ProfileDTO getSimpleUserProfile(String email, boolean emailVerified) {
        log.debug("Entering getSimpleUserProfile email {}", email);
        return profile(null, email, false, emailVerified);
    }

    @Override
    public ProfileDTO getProfileForRegistry(String email, boolean emailVerified) {
        log.debug("Entering getProfileForRegistry email {} and emailVerified {}", email, emailVerified);
        // fullProfile true, but for signup if the user does not exists -> throw exception
        return profile(null, email, true, emailVerified);
    }

    private ProfileDTO profile(String clientFrom, String email, boolean fullProfile, boolean emailVerified) {

        Optional<LawyerDTO> lawyerDTOOptional = tUsersRepository.findDTOByEmail(email);

        if (lawyerDTOOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is unknown");
        }
        String vcKey = "";
        String dropboxToken = "";
        String onedriveToken = "";
        ZonedDateTime onedriveExpToken = null;
        String onedriveRefreshToken = "";
        Boolean temporaryVc = false;

        // if NOT verified check auth0
        boolean verified = emailVerified;

        // check first avogest db
        // if verified it's ok nothing to do
        if (lawyerDTOOptional.get().getEnumValid().equals(EnumValid.VERIFIED)) {
            verified = true;
        }

        String currency = EnumRefCurrency.EUR.getSymbol();
        DriveType driveType = DriveType.openstack;
        List<EnumRights> roleIdList = new ArrayList<>();

        if (fullProfile) {

            List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByUserIdAndIsSelected(lawyerDTOOptional.get().getId(), true);

            if (lawfirmUsersList == null || lawfirmUsersList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No User registred for a virtual cab");
            }
            // get the first to be sure
            LawfirmEntity lawfirmEntity = lawfirmUsersList.get(0).getLawfirm();

            vcKey = lawfirmEntity.getVckey();

//            if(!lawfirmEntity.getclientFrom().equalsIgnore(clientFrom)){
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Client from must be the same");
//            }

            dropboxToken = lawfirmEntity.getDropboxToken();
            onedriveToken = lawfirmEntity.getOnedriveToken();
            onedriveExpToken = lawfirmEntity.getExpireToken();
            onedriveRefreshToken = lawfirmEntity.getRefreshToken();
            temporaryVc = lawfirmEntity.getTemporaryVc();
            currency = lawfirmEntity.getCurrency().getSymbol();
            driveType = lawfirmEntity.getDriveType();

            roleIdList = tSecurityGroupUsersRepository.findByIdUserAndVckey(lawyerDTOOptional.get().getId(), lawfirmEntity.getVckey(), List.of(EnumSecurityAppGroups.ADMIN, EnumSecurityAppGroups.OTHER, EnumSecurityAppGroups.USER));

            if (roleIdList == null || roleIdList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the rights");
            }

//            roleIdList = securityGroupRights.stream()
//                    .map(TSecurityGroupRights::getIdRight).collect(Collectors.toList());
        }

        // only go inside with admin interface
        // This authorization is manage into auth0
        if (clientFrom != null && clientFrom.equalsIgnoreCase("admin")) {
            roleIdList.add(EnumRights.SUPER_ADMIN);
        }

        return new ProfileDTO(lawyerDTOOptional.get().getId(), lawyerDTOOptional.get().getFullName(), lawyerDTOOptional.get().getEmail(), null, vcKey,
                temporaryVc,
                lawyerDTOOptional.get().getLanguage(),
                currency,
                lawyerDTOOptional.get().getId(),
                lawyerDTOOptional.get().getIdUser(),
                roleIdList.stream().map(EnumRights::getId).collect(Collectors.toList()),
                driveType, dropboxToken,
                onedriveToken,
                onedriveRefreshToken,
                onedriveExpToken,
                verified,
                lawyerDTOOptional.get().getClientFrom());

    }

    @Override
    public List<LawyerDTO> getFullUserResponsableList(String vcKey) {
        log.info("Entering getUserResponsableByVcKey vckey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKey(vcKey);

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
        return lawfirmUsersList.stream().map(lawfirmUsers -> {
            LawyerDTO lawyerDTO = entityToUserConverter.apply(lawfirmUsers.getUser(), false);
            lawyerDTO.setFunctionId(lawfirmUsers.getIdRole().getIdRole());
            lawyerDTO.setFunctionIdItem(new ItemDto(lawfirmUsers.getIdRole().getIdRole(),
                    Utils.getLabel(enumLanguage,
                            lawfirmUsers.getIdRole().name(), null
                    )));
            lawyerDTO.setActive(lawfirmUsers.isActive());
            List<TSecurityGroupUsers> securityGroupUsersList = tSecurityGroupUsersRepository.findByIdAndVckey(lawfirmUsers.getUser().getId(), vcKey);

            securityGroupUsersList.forEach(groupUsers -> {
                if (!groupUsers.getTSecurityGroups().getTSecAppGroupId().equals(EnumSecurityAppGroups.SUPER_ADMIN)) {
                    ItemLongDto securityGroupDTOItem = new ItemLongDto(groupUsers.getTSecurityGroups().getId(), groupUsers.getTSecurityGroups().getDescription());
                    lawyerDTO.getSecurityGroupDTOList().add(securityGroupDTOItem);
                }
            });

            return lawyerDTO;
        }).collect(Collectors.toList());

    }

    @Override
    public List<SecurityGroupDTO> getSecurityGroup(String vcKey) {
        log.info("Entreing getSecurityGroup {}", vcKey);
        List<TSecurityGroups> tSecurityGroupsList = tSecurityGroupsRepository.findAllByVcKey(vcKey);

        return tSecurityGroupsList.stream()
                .map(groups -> {
                    if (!groups.getTSecAppGroupId().equals(EnumSecurityAppGroups.SUPER_ADMIN)) {
                        SecurityGroupDTO securityGroupDTO = new SecurityGroupDTO();
                        securityGroupDTO.setId(groups.getId());
                        securityGroupDTO.setDescription(groups.getDescription());
                        securityGroupDTO.setAppGroup(groups.getTSecAppGroupId());
                        int size = groups.getTSecurityGroupUsersList() != null ? groups.getTSecurityGroupUsersList().size() : 0;
                        securityGroupDTO.setNbUsers(size);
                        return securityGroupDTO;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Integer createUserSecurity(Long userId, String vcKey, SecurityGroupUserDTO securityGroupUserDTO) throws ResponseStatusException {
        log.info("Entering createUserSecurity userId {}", userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (securityGroupUserDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
        if (securityGroupUserDTO.getFunctionId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "function does not exist");
        }
        if (securityGroupUserDTO.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user id does not exist");
        }
        if (securityGroupUserDTO.getSecurityGroupId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security group id does not exist");
        }

        TUsers user;
        Optional<TUsers> usersOptional;
        // create user
        // check if the user is known
        if (securityGroupUserDTO.getUserId() != null) {
            usersOptional = tUsersRepository.findById(securityGroupUserDTO.getUserId());
        } else {
            usersOptional = tUsersRepository.findByEmail(securityGroupUserDTO.getEmail().toLowerCase().trim());
        }

        // NOT EXIST -> new user
        if (usersOptional.isEmpty()) {
            EnumLanguage language = EnumLanguage.fromshortCode(lawfirmToken.getLanguage());
            user = userV2Service.createUsers(securityGroupUserDTO.getEmail(), lawfirmToken.getClientFrom(), language, true);

            log.debug("user created");
        } else {
            user = usersOptional.get();
            // check if this user exist in the cab
            List<TSecurityGroupUsers> securityGroupUsersList = tSecurityGroupUsersRepository.findByIdAndVckey(user.getId(), vcKey);
            if (securityGroupUsersList != null && !securityGroupUsersList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user is already a member to this vckey (with security group)");
            }

            // check if this client is already a member without security right
            Optional<LawfirmUsers> optionalLawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, user.getId());

            if (optionalLawfirmUsers.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "user is already a member to this vckey (without right)");
            }
        }

        // add this new member to vc key
        Optional<TStripeSubscribers> stripeSubscribers = tStripeSubscribersRepository.findByIdUser(user.getId());

        if (stripeSubscribers.isEmpty()) {
            // init stripe user
            TStripeSubscribers tStripeSubscribers = new TStripeSubscribers();
            tStripeSubscribers.setIdUser(user.getId());
            tStripeSubscribers.setActivesub(1);
            tStripeSubscribers.setPlan("basic_plan");
            tStripeSubscribers.setValidFrom(LocalDate.now());
            tStripeSubscribers.setValidTo(LocalDate.now().plusYears(5));
            tStripeSubscribersRepository.save(tStripeSubscribers);

            TFirstTime tFirstTime = new TFirstTime();
            tFirstTime.setUserId(user.getId());
            tFirstTime.setACTIVATED(1);
            tFirstTime.setCreUser(lawfirmToken.getUsername());

            tFirstTimeRepository.save(tFirstTime);
        }

        // add security group
        Optional<TSecurityGroups> securityGroupsOptional = tSecurityGroupsRepository.findById(securityGroupUserDTO.getSecurityGroupId());

        if (securityGroupsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security group not found");
        }

        TSecurityGroupUsers tSecurityGroupUsers = new TSecurityGroupUsers();

        tSecurityGroupUsers.setUser(user);
        tSecurityGroupUsers.setTSecurityGroups(securityGroupsOptional.get());
        tSecurityGroupUsersRepository.save(tSecurityGroupUsers);


        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BIG ISSUE MOTHERFUCKER");
        }
        LawfirmUsers lawfirmUsers = new LawfirmUsers();
        lawfirmUsers.setUser(user);
        lawfirmUsers.setLawfirm(lawfirmEntityOptional.get());
        lawfirmUsers.setActive(true);
        lawfirmUsers.setIdRole(EnumRole.fromId(securityGroupUserDTO.getFunctionId()));
        lawfirmUsers.setCouthoraire(0);
        Date from = DateUtils.addDays(new Date(), -1);
        Date to = DateUtils.addYears(new Date(), 5);
        lawfirmUsers.setValidFrom(from);
        lawfirmUsers.setValidTo(to);
        // fill in the selected , could be check with a select on "LawfirmUsers" based on user id
        lawfirmUsers.setSelected(true);
        lawfirmUsers.setPrestataire(true);
        lawfirmUsers.setUser_upd(lawfirmToken.getUsername());
        lawfirmUsers.setDate_upd(LocalDateTime.now());

        lawfirmUserRepository.save(lawfirmUsers);


        //add to t_client
        ContactSummary contactSummary = new ContactSummary();
        contactSummary.setVcKey(vcKey);
        contactSummary.setFullName(user.getFullname());
        contactSummary.setLastname(user.getFullname());
        contactSummary.setFirstname(user.getFullname());
        contactSummary.setEmail(user.getEmail());
        contactSummary.setLanguage(user.getLanguage());
        contactSummary.setUserId(user.getId());

        clientV2Service.createContact(contactSummary);


        // all dossier
        if (securityGroupUserDTO.isShareDossier()) {

            List<TDossiers> dossiersList = dossierRepository.findAllByVCKey(vcKey);
//
            dossiersList.forEach(dossiers -> {
                ShareAffaireDTO shareAffaireDTO = new ShareAffaireDTO();
                shareAffaireDTO.setUserIdSelected(new ArrayList<>());
                shareAffaireDTO.getUserIdSelected().add(lawfirmUsers.getUser().getId());
                shareAffaireDTO.setUserId(lawfirmUsers.getUser().getId());
                shareAffaireDTO.setAffaireId(dossiers.getIdDoss());
                shareAffaireDTO.setVcKey(lawfirmUsers.getLawfirm().getVckey());
                shareAffaireDTO.setNomenclature(dossiers.getNomenclature());
                dossierV2Service.addShareFolderUser(shareAffaireDTO, false);
            });

        }
        String language = lawfirmUsers.getUser().getLanguage() != null ? lawfirmUsers.getUser().getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();

        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILSHAREDUSERSECURITYTEMPLATE,
                EmailUtils.prepareContextForSharedUserSecurity(
                        lawfirmUsers.getUser().getEmail(),
                        vcKey,
                        lawfirmToken.getClientFrom()),
                language
        );


        return null;
    }

    @Override
    public Long deleteSecurityUsersGroup(Long userId) {
        log.debug("Entering deleteSecurityUsersGroup user id {}", userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), userId);

        if (lawfirmUsersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lawfirm User not found");
        }

        List<TSecurityGroupUsers> tSecurityGroupUsers = tSecurityGroupUsersRepository.findByIdAndVckey(userId, lawfirmToken.getVcKey());

        // if there is at least one active user in the group ADMIN
        List<TSecurityGroupUsers> securityGroupUsersList = tSecurityGroupUsersRepository.findByVckeyAndRight(lawfirmToken.getVcKey(), EnumSecurityAppGroups.ADMIN);

        log.debug("Security group user (active) size {}", securityGroupUsersList.size());

        if (!CollectionUtils.isEmpty(securityGroupUsersList) && securityGroupUsersList.size() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only one user is remaining into the ADMIN vckey " + lawfirmToken.getVcKey());
        }


        tSecurityGroupUsersRepository.deleteAllById(tSecurityGroupUsers.stream().map(TSecurityGroupUsers::getId).collect(Collectors.toList()));

        log.debug("Security group user deleted {}", userId);
        log.info("dossier right start deleting {}", userId);


        dossierRightsRepository.deleteAllByVcUserId(lawfirmUsersOptional.get().getId());


        log.debug("dossier right deleted {}", userId);
        log.info("lawfirm User start deleting {}", userId);

        lawfirmUserRepository.deleteByLawfirmUsersId(lawfirmUsersOptional.get().getId());
        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        log.info("Bye bye delete {}", userId);

        return userId;
    }

    @Override
    public boolean existSecurityGroupByName(String securityGroupName) {
        log.debug("Entering getSecurityGroupById securityGroupId {}", securityGroupName);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (securityGroupName == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Security group name not found");
        }

        log.debug("Security group Vc key {}", lawfirmToken.getVcKey());
        List<TSecurityGroups> tSecurityGroupsList = tSecurityGroupsRepository.findAllByVcKeyAndDescription(lawfirmToken.getVcKey(), securityGroupName.trim());

        return tSecurityGroupsList != null && !tSecurityGroupsList.isEmpty();
    }

    @Override
    public Long createSecurityGroup(Long userId, String newName) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (existSecurityGroupByName(newName)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create Security group, it already exist");
        }

        TSecurityGroups tSecurityGroups = new TSecurityGroups();
        tSecurityGroups.setDescription(newName);
        tSecurityGroups.setVcKey(lawfirmToken.getVcKey());
        tSecurityGroups.setUserUpd(lawfirmToken.getUsername());
        tSecurityGroups.setDateUpd(LocalDateTime.now());
        tSecurityGroups.setTSecAppGroupId(EnumSecurityAppGroups.OTHER);

        TSecurityGroups saved = tSecurityGroupsRepository.save(tSecurityGroups);

        return saved.getId();
    }

    @Override
    public Long deleteSecurityGroup(Long securityGroupId) {
        log.debug("Entering deleteSecurityGroup securityGroup id {}", securityGroupId);
        Optional<TSecurityGroups> securityGroupsOptional = tSecurityGroupsRepository.findById(securityGroupId);

        if (securityGroupsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security group not found");
        }

        log.debug("Security group user list");

        if (securityGroupsOptional.get().getTSecurityGroupUsersList() != null && !securityGroupsOptional.get().getTSecurityGroupUsersList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete Security group, Users exist");
        }
        log.info("Security group start deleting {}", securityGroupId);

        tSecurityGroupsRepository.delete(securityGroupsOptional.get());

        log.info("Bye bye delete {}", securityGroupId);

        return securityGroupId;
    }

    @Override
    public List<SecurityGroupUserDTO> getSecurityUserGroupBySecurityGroupId(Long securityGroupId) {
        log.info("Entering getSecurityUserGroupBySecurityGroupId securityGroupId {}", securityGroupId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TSecurityGroupUsers> securityGroupUsersList = tSecurityGroupUsersRepository.findByTSecurityGroupsIdAndVckey(securityGroupId, lawfirmToken.getVcKey());

        return entityToSecurityGroupConverter.convertToList(securityGroupUsersList);
    }

    @Override
    public List<LawyerDTO> getOutSecurityUserGroupBySecurityGroupId(Long securityGroupId) {
        log.info("Entering getOutSecurityUserGroupBySecurityGroupId securityGroupId {}", securityGroupId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<LawyerDTO> lawyerDTOList = new ArrayList<>();

        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKey(lawfirmToken.getVcKey());

        List<TSecurityGroupUsers> securityGroupUsersList = tSecurityGroupUsersRepository.findByTSecurityGroupsIdAndVckey(securityGroupId, lawfirmToken.getVcKey());

        // remove user already in the security group from lawfirmUsersList
        lawfirmUsersList.forEach(lawfirmUsers -> {
            boolean exist = false;
            for (TSecurityGroupUsers securityGroupUsers : securityGroupUsersList) {
                if (lawfirmUsers.getUser().getId().equals(securityGroupUsers.getUser().getId())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                log.debug("This user {} does not exist in the group", lawfirmUsers.getUser().getEmail());
                lawyerDTOList.add(entityToUserConverter.apply(lawfirmUsers.getUser(), false));
            }
        });

        return lawyerDTOList;

    }

    @Override
    public Long addUserSecurity(Long securityGroupId, Long userId) {
        log.info("Entering addUserSecurity securityGroupId {} and userId {}", securityGroupId, userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), userId);

        if (lawfirmUsersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BIG ISSUE MOTHERF...");
        }
        // add security group
        Optional<TSecurityGroups> securityGroupsOptional = tSecurityGroupsRepository.findById(securityGroupId);

        if (securityGroupsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security group not found");
        }
        if (securityGroupsOptional.get().getTSecurityGroupUsersList() != null && !securityGroupsOptional.get().getTSecurityGroupUsersList().isEmpty()) {
            boolean anyMatch = securityGroupsOptional.get().getTSecurityGroupUsersList().stream().anyMatch(tSecurityGroupUsers -> tSecurityGroupUsers.getUser().getId().equals(userId));
            if (anyMatch) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Security user group already exis");
            }
        }
        TSecurityGroupUsers tSecurityGroupUsers = new TSecurityGroupUsers();

        tSecurityGroupUsers.setUser(lawfirmUsersOptional.get().getUser());
        tSecurityGroupUsers.setTSecurityGroups(securityGroupsOptional.get());

        tSecurityGroupUsersRepository.save(tSecurityGroupUsers);

        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        return tSecurityGroupUsers.getId();
    }

    @Override
    public Long deleteSecurityGroupById(Long securityGroupUserId) {
        log.info("Entering deleteSecurityGroupById securityGroupUserId {}", securityGroupUserId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<TSecurityGroupUsers> securityGroupUsersOptional = tSecurityGroupUsersRepository.findById(securityGroupUserId);

        if (securityGroupUsersOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security user group not found");
        }

        List<TSecurityGroupUsers> securityGroupUsers = tSecurityGroupUsersRepository.findBySecGroupUserIdAndVckey(securityGroupUsersOptional.get().getTSecurityGroups().getId(), lawfirmToken.getVcKey(), EnumSecurityAppGroups.ADMIN);

        // if it's admin and remove the last user throw exception
        if (securityGroupUsers != null && securityGroupUsers.size() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Security user group cannot be deleted because it's only 1 remaining");
        }

        tSecurityGroupUsersRepository.delete(securityGroupUsersOptional.get());
        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        return securityGroupUserId;
    }

    @Override
    public List<ItemLongDto> getSecurityRightGroupBySecurityGroupId(Long securityGroupId) {
        log.info("Entering getSecurityRightGroupBySecurityGroupId securityGroupUserId {}", securityGroupId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TSecurityGroupRights> securityGroupRights = tSecurityGroupRightsRepository.findByTSecurityGroups_Id(securityGroupId);
        return securityGroupRights.stream()
                .map(securityGroupRight -> new ItemLongDto(securityGroupRight.getId(), Utils.getLabel(EnumLanguage.fromshortCode(lawfirmToken.getLanguage()), securityGroupRight.getIdRight().name(), null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getOutSecurityRightGroupBySecurityGroupId(Long securityGroupId) {
        log.info("Entering getOutSecurityRightGroupBySecurityGroupId securityGroupUserId {}", securityGroupId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<TSecurityGroupRights> securityGroupRights = tSecurityGroupRightsRepository.findByTSecurityGroups_Id(securityGroupId);
        List<ItemDto> rightOut = new ArrayList<>();

        EnumRights.allNonAdmin().forEach(enumRights -> {
            boolean exist = false;
            for (TSecurityGroupRights tSecurityGroupRights : securityGroupRights) {
                if (enumRights.getId() == tSecurityGroupRights.getIdRight().getId()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                String label = Utils.getLabel(EnumLanguage.fromshortCode(lawfirmToken.getLanguage()), enumRights.name(), null);
                log.debug("This right {} does not exist in the security group", label);
                rightOut.add(new ItemDto(enumRights.getId(), label));
            }
        });
        return rightOut;
    }

    @Override
    public Long addRightSecurity(Long securityGroupId, Integer rightId) {
        log.info("Entering addRightSecurity securityGroupId {} and rightId {}", securityGroupId, rightId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        if (!lawfirmUsersOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BIG ISSUE MOTHERF...");
        }
        EnumRights enumRights = EnumRights.fromId(rightId);

        if (enumRights == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "right not found");
        }
        // add security group
        Optional<TSecurityGroups> securityGroupsOptional = tSecurityGroupsRepository.findById(securityGroupId);

        if (!securityGroupsOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security group not found");
        }
        List<TSecurityGroupRights> securityGroupRights = tSecurityGroupRightsRepository.findByIdRightAndTSecurityGroups_Id(enumRights, securityGroupId);

        if (securityGroupRights != null && !securityGroupRights.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Security right group already exis");
        }

        TSecurityGroupRights tSecurityGroupRights = new TSecurityGroupRights();

        tSecurityGroupRights.setIdRight(enumRights);
        tSecurityGroupRights.setTSecurityGroups(securityGroupsOptional.get());

        tSecurityGroupRightsRepository.save(tSecurityGroupRights);

        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        return tSecurityGroupRights.getId();
    }

    @Override
    public Long deleteSecurityGroupRightById(Long securityGroupRightId) {
        log.info("Entering deleteSecurityGroupRightById securityGroupRightId {}", securityGroupRightId);
        Optional<TSecurityGroupRights> groupRightsOptional = tSecurityGroupRightsRepository.findById(securityGroupRightId);

        if (groupRightsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Security right group not found");
        }

        tSecurityGroupRightsRepository.deleteById(groupRightsOptional.get().getId());
        cacheService.evictCaches("userProfile");

        return securityGroupRightId;
    }
}
