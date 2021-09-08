package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.AccountingTypeDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.RefPoste;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.RefPosteRepository;
import com.ulegalize.lawfirm.service.v2.RefPosteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class RefPosteServiceImpl implements RefPosteService {
    private final RefPosteRepository refPosteRepository;
    private final LawfirmUserRepository lawfirmUserRepository;

    public RefPosteServiceImpl(RefPosteRepository refPosteRepository, LawfirmUserRepository lawfirmUserRepository) {
        this.refPosteRepository = refPosteRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
    }

    @Override
    public List<AccountingTypeDTO> getAllRefPoste(String vcKey, Long userId) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            return refPosteRepository.findAllItemByVcKey(vcKey);
        }

        return new ArrayList<>();
    }

    @Override
    public AccountingTypeDTO updateRefPoste(String vcKey, Long userId, Integer refPosteId, AccountingTypeDTO accountingTypeDTO) {
        log.debug("Update presations type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<RefPoste> optionalRefPoste = refPosteRepository.findById(refPosteId);
            if (!optionalRefPoste.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accounting type is not found");
            }

            optionalRefPoste.get().setRefPoste(accountingTypeDTO.getDescription());
            optionalRefPoste.get().setFraisProcedure(accountingTypeDTO.isFraisProcedure());
            optionalRefPoste.get().setHonoraires(accountingTypeDTO.isHonoraires());
            optionalRefPoste.get().setFraisCollaboration(accountingTypeDTO.isFraisCollaboration());
            optionalRefPoste.get().setFacturable(accountingTypeDTO.isFacturable());
            optionalRefPoste.get().setArchived(accountingTypeDTO.isArchived());
            optionalRefPoste.get().setUserUpd(lawfirmToken.getUsername());

            refPosteRepository.save(optionalRefPoste.get());

            return accountingTypeDTO;
        }

        return null;
    }

    @Override
    public Integer deleteRefPoste(String vcKey, Long userId, Integer refPosteId) {
        log.debug("Update presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<RefPoste> optionalRefPoste = refPosteRepository.findById(refPosteId);
            if (!optionalRefPoste.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accounting type is not found");
            }

            refPosteRepository.delete(optionalRefPoste.get());

            return refPosteId;
        }

        return null;
    }

    @Override
    public Integer createRefPoste(String vcKey, Long userId, AccountingTypeDTO accountingTypeDTO) {
        log.debug("create debours type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            RefPoste refPoste = new RefPoste();
            refPoste.setVcKey(vcKey);
            refPoste.setRefPoste(accountingTypeDTO.getDescription());
            refPoste.setFraisProcedure(accountingTypeDTO.isFraisProcedure());
            refPoste.setHonoraires(accountingTypeDTO.isHonoraires());
            refPoste.setFraisCollaboration(accountingTypeDTO.isFraisCollaboration());
            refPoste.setFacturable(accountingTypeDTO.isFacturable());
            refPoste.setArchived(false);
            refPoste.setUserUpd(lawfirmToken.getUsername());
            refPoste.setDateUpd(new Date());

            RefPoste save = refPosteRepository.save(refPoste);
            return save.getIdPoste();
        }

        return null;
    }

    @Override
    public AccountingTypeDTO getRefPosteById(String vcKey, Long userId, Integer refPosteId) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            return refPosteRepository.findDTOById(vcKey, refPosteId);
        }

        return null;
    }
}
