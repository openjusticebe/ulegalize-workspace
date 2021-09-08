package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.PrestationTypeDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TTimesheetType;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TTimesheetTypeRepository;
import com.ulegalize.lawfirm.service.v2.PrestationTypeService;
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
public class PrestationTypeServiceImpl implements PrestationTypeService {
    private final TTimesheetTypeRepository tTimesheetTypeRepository;
    private final LawfirmUserRepository lawfirmUserRepository;

    public PrestationTypeServiceImpl(TTimesheetTypeRepository tTimesheetTypeRepository, LawfirmUserRepository lawfirmUserRepository) {
        this.tTimesheetTypeRepository = tTimesheetTypeRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
    }

    @Override
    public List<PrestationTypeDTO> getAllPrestationsType(String vcKey, Long userId) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            return tTimesheetTypeRepository.findAllItemByVcKey(vcKey);
        }

        return new ArrayList<>();
    }

    @Override
    public PrestationTypeDTO updatePrestationsType(String vcKey, Long userId, Integer prestationTypeId, PrestationTypeDTO prestationTypeDTO) {
        log.debug("Update presations type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<TTimesheetType> optionalTTimesheetType = tTimesheetTypeRepository.findById(prestationTypeId);
            if (!optionalTTimesheetType.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation type is not found");
            }


            optionalTTimesheetType.get().setDescription(prestationTypeDTO.getDescription());
            optionalTTimesheetType.get().setArchived(prestationTypeDTO.isArchived());
            optionalTTimesheetType.get().setUserUpd(lawfirmToken.getUsername());

            tTimesheetTypeRepository.save(optionalTTimesheetType.get());

            return prestationTypeDTO;
        }

        return null;
    }

    @Override
    public Integer deletePrestationsType(String vcKey, Long userId, Integer prestationTypeId) {
        log.debug("Update presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<TTimesheetType> optionalTTimesheetType = tTimesheetTypeRepository.findById(prestationTypeId);
            if (!optionalTTimesheetType.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation type is not found");
            }

            tTimesheetTypeRepository.delete(optionalTTimesheetType.get());

            return prestationTypeId;
        }

        return null;
    }

    @Override
    public Integer createPrestationsType(String vcKey, Long userId, PrestationTypeDTO prestationTypeDTO) {
        log.debug("Update presations type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            TTimesheetType tTimesheetType = new TTimesheetType();

            tTimesheetType.setDescription(prestationTypeDTO.getDescription());
            tTimesheetType.setArchived(false);
            tTimesheetType.setUserUpd(lawfirmToken.getUsername());
            tTimesheetType.setDateUpd(new Date());
            tTimesheetType.setVcKey(vcKey);

            TTimesheetType save = tTimesheetTypeRepository.save(tTimesheetType);
            return save.getIdTs();
        }

        return null;
    }

    @Override
    public PrestationTypeDTO getPrestationsTypeById(String vcKey, Long userId, Integer prestationsType) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            return tTimesheetTypeRepository.findDTOById(vcKey, prestationsType);
        }

        return null;
    }
}
