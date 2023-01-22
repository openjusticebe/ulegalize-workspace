package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumRole;
import com.ulegalize.enumeration.EnumSecurityAppGroups;
import com.ulegalize.lawfirm.kafka.producer.payment.ILawfirmProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPrivateConverter;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmUserDTOConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TSecurityGroupUsersRepository;
import com.ulegalize.lawfirm.rest.AuthApi;
import com.ulegalize.lawfirm.service.LawfirmUserService;
import com.ulegalize.lawfirm.service.v2.cache.CacheService;
import com.ulegalize.lawfirm.service.v2.cache.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class LawfirmUserServiceImpl implements LawfirmUserService {
    private final TSecurityGroupUsersRepository tSecurityGroupUsersRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final LawfirmRepository lawfirmRepository;
    private final ILawfirmProducer lawfirmProducer;
    private final EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter;
    private final EntityToLawfirmPrivateConverter entityToLawfirmPrivateConverter;
    private final CacheService cacheService;
    private final AuthApi authApi;

    public LawfirmUserServiceImpl(TSecurityGroupUsersRepository tSecurityGroupUsersRepository, LawfirmUserRepository lawfirmUserRepository, LawfirmRepository lawfirmRepository, ILawfirmProducer lawfirmProducer,
                                  EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter, EntityToLawfirmPrivateConverter entityToLawfirmPrivateConverter, CacheService cacheService, AuthApi authApi) {
        this.tSecurityGroupUsersRepository = tSecurityGroupUsersRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.lawfirmProducer = lawfirmProducer;
        this.entityToLawfirmUserDTOConverter = entityToLawfirmUserDTOConverter;
        this.entityToLawfirmPrivateConverter = entityToLawfirmPrivateConverter;
        this.cacheService = cacheService;
        this.authApi = authApi;
    }

    @Override
    public List<LawfirmDTO> getLawfirmsByUserId(Long userId) throws ResponseStatusException {
        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByUserId(userId);

        return lawfirmUsersList.stream().map(lawfirmUsers -> {
            LawfirmDTO lawfirmDTO = new LawfirmDTO();
            lawfirmDTO.setVckey(lawfirmUsers.getLawfirm().getVckey());
            return lawfirmDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LawfirmUserDTO> getLawfirmUsers(String vcKey) {
        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKey(vcKey);

        if (lawfirmUsersList == null) {
            return new ArrayList<>();
        }
        return entityToLawfirmUserDTOConverter.convertToList(lawfirmUsersList);
    }

    @Override
    public LawfirmUserDTO updateIsPublicLawfirmUser(String vcKey, Long userId, String isPublic) {

        log.info("Entering updateIsPublicLawfirmUser with userId {} and vcKey {}", userId, vcKey);
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        LawfirmUserDTO lawfirmUserDTO;
        if (lawfirmUsersOptional.isPresent()) {
            lawfirmUsersOptional.get().setPublic(Boolean.parseBoolean(isPublic));
            lawfirmUserRepository.save(lawfirmUsersOptional.get());
            lawfirmUserDTO = entityToLawfirmUserDTOConverter.apply(lawfirmUsersOptional.get());
            TUsers user = lawfirmUsersOptional.get().getUser();
            user.setSpecialities(user.getSpecialities() != null ? user.getSpecialities() : "");
        } else {
            log.warn("LawfirmUser does not exist {}", userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "LawfirmUser does not exist");
        }

        log.debug("LawfirmUser {} updated successfully : isPublic set to {} ", userId, lawfirmUserDTO.isPublic());
        return lawfirmUserDTO;
    }

    @Override
    public String switchLawfirm(Long userId, String newVcKeySelected) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering switchLawfirm user id {} and new vckey {}", userId, newVcKeySelected);

        Optional<LawfirmEntity> optionalLawfirm = lawfirmRepository.findLawfirmByVckey(newVcKeySelected);

        if (optionalLawfirm.isEmpty()) {
            log.warn("Lawfirm {} does not exist", newVcKeySelected);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lawfirm " + newVcKeySelected + " does not exist");
        }

        List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByUserId(userId);

        var refUser = new Object() {
            String lawyerEmail;
        };
        boolean anyMatch = lawfirmUsersList.stream().anyMatch(lawfirmUsers -> lawfirmUsers.getLawfirm().getVckey().equalsIgnoreCase(newVcKeySelected));

        log.info("Found matching between list and new vc key {}", anyMatch);
        if (anyMatch) {
            lawfirmUsersList.forEach(lawfirmUsers -> {
                lawfirmUsers.setSelected(false);

                if (lawfirmUsers.getLawfirm().getVckey().equalsIgnoreCase(newVcKeySelected)) {
                    lawfirmUsers.setSelected(true);
                    refUser.lawyerEmail = lawfirmUsers.getUser().getEmail();
                    authApi.updateAppMetaData(lawfirmToken.getAuth0UserId(), newVcKeySelected);
                }
            });

            lawfirmUserRepository.saveAll(lawfirmUsersList);

            LawfirmDTO lawfirmDTO = entityToLawfirmPrivateConverter.apply(optionalLawfirm.get());

            lawfirmDTO.setEmail(refUser.lawyerEmail);
            lawfirmDTO.setVckey(newVcKeySelected);
            lawfirmProducer.switchLawfirm(lawfirmDTO, lawfirmToken);
        }
        cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);

        return newVcKeySelected;

    }

    @Override
    public SecurityGroupUserDTO getLawfirmUserByUserId(Long userId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering getLawfirmUserByUserId user id {} and new vckey {}", userId, lawfirmToken.getVcKey());
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), userId);

        SecurityGroupUserDTO securityGroupUserDTO = new SecurityGroupUserDTO();

        if (lawfirmUsersOptional.isPresent()) {
            securityGroupUserDTO.setId(userId);
            securityGroupUserDTO.setFunctionId(lawfirmUsersOptional.get().getIdRole().getIdRole());
            securityGroupUserDTO.setEmail(lawfirmUsersOptional.get().getUser().getEmail());
            securityGroupUserDTO.setUserEmailItem(new ItemStringDto(
                    lawfirmUsersOptional.get().getUser().getEmail(),
                    lawfirmUsersOptional.get().getUser().getEmail()
            ));

        }

        return securityGroupUserDTO;

    }

    @Override
    public LawyerDTO updateRoleLawfirmUser(LawyerDTO lawyerDTO) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering updateRoleLawfirmUser user id {} and new vckey {}", lawyerDTO, lawfirmToken.getVcKey());
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), lawyerDTO.getId());

        if (lawfirmUsersOptional.isPresent()) {
            EnumRole enumRole = EnumRole.fromId(lawyerDTO.getFunctionId());
            lawfirmUsersOptional.get().setIdRole(enumRole);

            log.debug("User used to active {} and become active {}", lawfirmUsersOptional.get().isActive(), lawyerDTO.isActive());
            if (lawfirmUsersOptional.get().isActive() != lawyerDTO.isActive()) {
                log.info("there is change for the active user");
                // check if it's active
                if (!lawyerDTO.isActive()) {
                    List<LawfirmUsers> lawfirmUsersList = lawfirmUserRepository.findLawfirmUsersByVcKeyAndActive(lawfirmToken.getVcKey());

                    if (lawfirmUsersList.size() == 1) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only one user is remaining into vckey " + lawfirmToken.getVcKey());
                    }


                    List<TSecurityGroupRights> tSecurityGroupRightsList = tSecurityGroupUsersRepository.findByIdUserAndVckeyAndEqual(lawyerDTO.getId(), lawfirmToken.getVcKey(), EnumSecurityAppGroups.ADMIN);

                    // if it's an admin
                    // check if there is at least more than 1 admin
                    if (!CollectionUtils.isEmpty(tSecurityGroupRightsList)) {
                        log.debug("Security group right is not empty {}", tSecurityGroupRightsList.size());
                        List<TSecurityGroupUsers> securityGroupUsersList = tSecurityGroupUsersRepository.findByVckeyAndRight(lawfirmToken.getVcKey(), EnumSecurityAppGroups.ADMIN);

                        log.debug("Security group user (active) size {}", securityGroupUsersList.size());

                        if (!CollectionUtils.isEmpty(securityGroupUsersList) && securityGroupUsersList.size() == 1) {
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only one user is remaining into the ADMIN vckey " + lawfirmToken.getVcKey());
                        }

                    }

                }
                lawfirmUsersOptional.get().setActive(lawyerDTO.isActive());
                lawfirmUserRepository.save(lawfirmUsersOptional.get());
            }

            lawfirmUserRepository.save(lawfirmUsersOptional.get());
            cacheService.evictCaches(CacheUtils.CACHE_USER_PROFILE);
            return lawyerDTO;
        } else {
            log.warn("LawfirmUser does not exist {}", lawyerDTO.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "LawfirmUser does not exist");
        }

    }


}
