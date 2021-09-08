package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TDebourType;
import com.ulegalize.lawfirm.model.entity.TMesureType;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.TDebourTypeRepository;
import com.ulegalize.lawfirm.repository.TMesureTypeRepository;
import com.ulegalize.lawfirm.service.v2.DeboursTypeService;
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
public class DeboursTypeServiceImpl implements DeboursTypeService {
    private final TDebourTypeRepository tDebourTypeRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final TMesureTypeRepository tMesureTypeRepository;

    public DeboursTypeServiceImpl(TDebourTypeRepository tDebourTypeRepository, LawfirmUserRepository lawfirmUserRepository, TMesureTypeRepository tMesureTypeRepository) {
        this.tDebourTypeRepository = tDebourTypeRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.tMesureTypeRepository = tMesureTypeRepository;
    }

    @Override
    public List<FraisAdminDTO> getAllDeboursType(String vcKey, Long userId) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            return tDebourTypeRepository.findAllItemByVcKey(vcKey);
        }

        return new ArrayList<>();
    }

    @Override
    public FraisAdminDTO updateDeboursType(String vcKey, Long userId, Long deboursTypeId, FraisAdminDTO fraisAdminDTO) {
        log.debug("Update presations type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<TDebourType> optionalTDebourType = tDebourTypeRepository.findById(deboursTypeId);
            if (!optionalTDebourType.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation type is not found");
            }


            optionalTDebourType.get().setDescription(fraisAdminDTO.getDebourDescription());
            optionalTDebourType.get().setIdMesureType(fraisAdminDTO.getIdMesureType());
            optionalTDebourType.get().setPricePerUnit(fraisAdminDTO.getPricePerUnit());
            optionalTDebourType.get().setArchived(fraisAdminDTO.isArchived());
            optionalTDebourType.get().setUserUpd(lawfirmToken.getUsername());

            tDebourTypeRepository.save(optionalTDebourType.get());

            return fraisAdminDTO;
        }

        return null;
    }

    @Override
    public Long deleteDeboursType(String vcKey, Long userId, Long deboursTypeId) {
        log.debug("Update presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<TDebourType> optionalTDebourType = tDebourTypeRepository.findById(deboursTypeId);
            if (!optionalTDebourType.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestation type is not found");
            }

            tDebourTypeRepository.delete(optionalTDebourType.get());

            return deboursTypeId;
        }

        return null;
    }

    @Override
    public Long createDeboursType(String vcKey, Long userId, FraisAdminDTO fraisAdminDTO) {
        log.debug("create debours type with vcKey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            Optional<TMesureType> optionalTMesureType = tMesureTypeRepository.findById(fraisAdminDTO.getIdMesureType());
            if (!optionalTMesureType.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesure type is not found");
            }
            TDebourType tDebourType = new TDebourType();

            tDebourType.setDescription(fraisAdminDTO.getDebourDescription());
            tDebourType.setIdMesureType(fraisAdminDTO.getIdMesureType());
            tDebourType.setArchived(false);
            tDebourType.setPricePerUnit(fraisAdminDTO.getPricePerUnit());
            tDebourType.setUserUpd(lawfirmToken.getUsername());
            tDebourType.setDateUpd(new Date());
            tDebourType.setVcKey(vcKey);

            TDebourType save = tDebourTypeRepository.save(tDebourType);
            return save.getIdDebourType();
        }

        return null;
    }

    @Override
    public FraisAdminDTO getDeboursTypeById(String vcKey, Long userId, Long deboursTypeId) {
        log.debug("Get all presations type with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);
        if (lawfirmUsers.isPresent()) {

            return tDebourTypeRepository.findDTOById(vcKey, deboursTypeId);
        }

        return null;
    }
}
