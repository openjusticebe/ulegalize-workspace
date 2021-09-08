package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumRole;
import com.ulegalize.lawfirm.kafka.producer.payment.ILawfirmProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmPublicConverter;
import com.ulegalize.lawfirm.model.converter.EntityToLawfirmUserDTOConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.service.LawfirmUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class LawfirmUserServiceImpl implements LawfirmUserService {
    private final LawfirmUserRepository lawfirmUserRepository;
    private final LawfirmRepository lawfirmRepository;
    private final ILawfirmProducer lawfirmProducer;
    private final EntityToLawfirmPublicConverter entityToLawfirmPublicConverter;
    private final EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter;

    public LawfirmUserServiceImpl(LawfirmUserRepository lawfirmUserRepository, LawfirmRepository lawfirmRepository, ILawfirmProducer lawfirmProducer, EntityToLawfirmPublicConverter entityToLawfirmPublicConverter, EntityToLawfirmUserDTOConverter entityToLawfirmUserDTOConverter) {
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.lawfirmProducer = lawfirmProducer;
        this.entityToLawfirmPublicConverter = entityToLawfirmPublicConverter;
        this.entityToLawfirmUserDTOConverter = entityToLawfirmUserDTOConverter;
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
        } else {
            log.warn("LawfirmUser does not exist {}", userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "LawfirmUser does not exist");
        }

        log.debug("LawfirmUser {} updated successfully : isPublic set to {} ", userId, lawfirmUserDTO.isPublic());
        return lawfirmUserDTO;
    }

    @Override
    public LawfirmUserDTO updateIsActiveLawfirmUser(String vcKey, Long userId, String isActive) {

        log.info("Entering updateIsActiveLawfirmUser with userId {} and vcKey {}", userId, vcKey);
        Optional<LawfirmUsers> lawfirmUsersOptional = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        LawfirmUserDTO lawfirmUserDTO;
        if (lawfirmUsersOptional.isPresent()) {
            lawfirmUsersOptional.get().setActive(Boolean.parseBoolean(isActive));
            lawfirmUserRepository.save(lawfirmUsersOptional.get());
            lawfirmUserDTO = entityToLawfirmUserDTOConverter.apply(lawfirmUsersOptional.get());
        } else {
            log.warn("LawfirmUser does not exist {}", userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "LawfirmUser does not exist");
        }

        log.debug("LawfirmUser {} updated successfully : isActive set to {}", userId, lawfirmUserDTO.isActive());
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
                }
            });

            lawfirmUserRepository.saveAll(lawfirmUsersList);

            LawfirmDTO lawfirmDTO = new LawfirmDTO();
            lawfirmDTO.setEmail(refUser.lawyerEmail);
            lawfirmDTO.setVckey(newVcKeySelected);
            lawfirmProducer.switchLawfirm(lawfirmDTO, lawfirmToken);
        }
        return newVcKeySelected;

    }

    @Override
    public List<LawfirmDTO> getLawfirms() {
        List<LawfirmEntity> lawfirmEntityList = lawfirmRepository.findAll();
        return entityToLawfirmPublicConverter.convertToList(lawfirmEntityList, false);

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

            lawfirmUserRepository.save(lawfirmUsersOptional.get());
            return lawyerDTO;
        }

        return null;
    }


}
