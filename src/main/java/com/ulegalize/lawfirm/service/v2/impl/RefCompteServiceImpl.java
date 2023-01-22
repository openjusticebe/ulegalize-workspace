package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.BankAccountDTO;
import com.ulegalize.enumeration.EnumAccountType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.RefCompte;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.RefCompteRepository;
import com.ulegalize.lawfirm.service.v2.RefCompteService;
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
public class RefCompteServiceImpl implements RefCompteService {
    private final RefCompteRepository refCompteRepository;
    private final LawfirmUserRepository lawfirmUserRepository;

    public RefCompteServiceImpl(RefCompteRepository refCompteRepository, LawfirmUserRepository lawfirmUserRepository) {
        this.refCompteRepository = refCompteRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
    }

    @Override
    public List<BankAccountDTO> getAllBankAccount(String vcKey, Long userId, String language) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {
            EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
            return refCompteRepository.findAllItemByVcKey(vcKey, enumLanguage.getShortCode());
        }

        return new ArrayList<>();
    }

    @Override
    public BankAccountDTO updateBankAccount(String vcKey, Long userId, Integer compteId, BankAccountDTO bankAccountDTO) {
        log.debug("Update presations type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<RefCompte> optionalRefPoste = refCompteRepository.findById(compteId);
            if (!optionalRefPoste.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account  is not found");
            }

            EnumAccountType enumAccountType = EnumAccountType.fromId(bankAccountDTO.getAccountTypeId());
            if (enumAccountType == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account type is not found");
            }
            optionalRefPoste.get().setCompteNum(bankAccountDTO.getAccountNumber());
            optionalRefPoste.get().setCompteRef(bankAccountDTO.getAccountRef());

            optionalRefPoste.get().setAccountTypeId(enumAccountType);
            optionalRefPoste.get().setArchived(bankAccountDTO.isArchived());
            optionalRefPoste.get().setUserUpd(lawfirmToken.getUsername());

            refCompteRepository.save(optionalRefPoste.get());

            return bankAccountDTO;
        }

        return null;
    }

    @Override
    public Integer deleteBankAccount(String vcKey, Long userId, Integer compteId) {
        log.debug("Update presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<RefCompte> optionalRefCompte = refCompteRepository.findById(compteId);
            if (!optionalRefCompte.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accounting type is not found");
            }

            refCompteRepository.delete(optionalRefCompte.get());

            return compteId;
        }

        return null;
    }

    @Override
    public Integer createBankAccount(String vcKey, Long userId, BankAccountDTO bankAccountDTO) {
        log.debug("create debours type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            RefCompte refCompte = new RefCompte();
            refCompte.setVcKey(vcKey);
            refCompte.setCompteRef(bankAccountDTO.getAccountRef());
            refCompte.setCompteNum(bankAccountDTO.getAccountNumber());

            EnumAccountType enumAccountType = EnumAccountType.fromId(bankAccountDTO.getAccountTypeId());
            if (enumAccountType == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account type is not found");
            }
            refCompte.setAccountTypeId(enumAccountType);
            refCompte.setArchived(false);
            refCompte.setUserUpd(lawfirmToken.getUsername());
            refCompte.setDateUpd(new Date());
            refCompte.setCountable(false);

            RefCompte save = refCompteRepository.save(refCompte);
            return save.getIdCompte();
        }

        return null;
    }

    @Override
    public BankAccountDTO getBankAccountById(String vcKey, Long userId, Integer compteId, String language) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {
            EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);

            return refCompteRepository.findDTOById(vcKey, compteId, enumLanguage.getShortCode());
        }

        return null;
    }
}
