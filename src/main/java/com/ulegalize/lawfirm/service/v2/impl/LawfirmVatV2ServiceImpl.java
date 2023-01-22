package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.TVirtualcabVat;
import com.ulegalize.lawfirm.repository.TFacturesRepository;
import com.ulegalize.lawfirm.repository.TVirtualcabVatRepository;
import com.ulegalize.lawfirm.service.v2.LawfirmVatV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class LawfirmVatV2ServiceImpl implements LawfirmVatV2Service {
    private final TVirtualcabVatRepository tVirtualcabVatRepository;
    private final TFacturesRepository tFacturesRepository;

    public LawfirmVatV2ServiceImpl(TVirtualcabVatRepository tVirtualcabVatRepository, TFacturesRepository tFacturesRepository) {
        this.tVirtualcabVatRepository = tVirtualcabVatRepository;
        this.tFacturesRepository = tFacturesRepository;
    }

    @Override
    public Long changeDefaultVat(BigDecimal vat) {
        log.info("Entering changeDefaultVat {}", vat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // change all to default false
        Optional<TVirtualcabVat> virtualcabVatDefaultOptional = tVirtualcabVatRepository.findByVcKeyAndIsDefaultAndVATIsNotNull(lawfirmToken.getVcKey(), true);

        if (!virtualcabVatDefaultOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "virtual vat by default not found");
        }
        virtualcabVatDefaultOptional.get().setIsDefault(false);
        tVirtualcabVatRepository.save(virtualcabVatDefaultOptional.get());

        log.debug("virtual cab vat {} default is set to FALSE", lawfirmToken.getVcKey());
        // switch
        Optional<TVirtualcabVat> virtualcabVatOptional = tVirtualcabVatRepository.findAllByVcKeyAndVAT(lawfirmToken.getVcKey(), vat);

        if (!virtualcabVatOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "virtual vat not found");
        }

        virtualcabVatOptional.get().setIsDefault(true);
        tVirtualcabVatRepository.save(virtualcabVatOptional.get());
        log.debug("virtual cab vat {} AND {} is set to TRUE", lawfirmToken.getVcKey(), virtualcabVatOptional.get().getVAT());

        return virtualcabVatOptional.get().getID();
    }

    @Override
    public Long deleteVat(BigDecimal vat) {
        log.info("Entering deleteVat with vat {}", vat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // chek if it's used in t_factures
        Long vatNb = tFacturesRepository.countAllByVcKeyAndVat(lawfirmToken.getVcKey(), vat);
        log.debug("Number of invoices {} found in the vckey {}", vatNb, lawfirmToken.getVcKey());

        if (vatNb != null && vatNb > 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Number of invoices greather than 0");
        }
        // change all to default false
        Optional<TVirtualcabVat> virtualcabVatOptional = tVirtualcabVatRepository.findAllByVcKeyAndVAT(lawfirmToken.getVcKey(), vat);

        if (virtualcabVatOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "virtual vat not founded ");
        }

        if (virtualcabVatOptional.get().getIsDefault()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This VAT cannot be removed as it is selected by default.");
        }

        Long nbVat = countVirtualCabVatByVcKey();

        if (nbVat != null && nbVat == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "virtual vat has only one vat remaining");
        }

        tVirtualcabVatRepository.delete(virtualcabVatOptional.get());

        log.info("Leaving deleteVat()");
        return virtualcabVatOptional.get().getID();
    }

    @Override
    public Long createVat(BigDecimal newVat) {
        log.info("Entering createVat {}", newVat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // change all to default false
        boolean alreadyExist = existVirtualCabVatByVat(newVat);

        if (alreadyExist) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "virtual vat founded");
        }

        TVirtualcabVat tVirtualcabVat = new TVirtualcabVat();
        tVirtualcabVat.setVcKey(lawfirmToken.getVcKey());
        tVirtualcabVat.setVAT(newVat);
        tVirtualcabVat.setCreUser(lawfirmToken.getUsername());

        tVirtualcabVatRepository.save(tVirtualcabVat);
        return tVirtualcabVat.getID();
    }

    @Override
    public boolean existVirtualCabVatByVat(BigDecimal newVat) {
        log.info("Entering createVat {}", newVat);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // change all to default false
        Optional<TVirtualcabVat> virtualcabVatOptional = tVirtualcabVatRepository.findAllByVcKeyAndVAT(lawfirmToken.getVcKey(), newVat);

        return virtualcabVatOptional.isPresent();
    }

    @Override
    public Long countVirtualCabVatByVcKey() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering countVirtualCabVatByVcKey {}", lawfirmToken.getVcKey());

        Long nbVat = tVirtualcabVatRepository.countAllByVcKey(lawfirmToken.getVcKey());
        log.debug("Number of vat {} in vckey {}", nbVat, lawfirmToken.getVcKey());
        return nbVat;
    }
}
