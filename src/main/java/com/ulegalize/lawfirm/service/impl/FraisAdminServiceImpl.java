package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToTDeboursEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToFraisAdminConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TDebour;
import com.ulegalize.lawfirm.model.enumeration.EnumMesureType;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import com.ulegalize.lawfirm.repository.TDebourRepository;
import com.ulegalize.lawfirm.repository.TDebourTypeRepository;
import com.ulegalize.lawfirm.service.FraisAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FraisAdminServiceImpl implements FraisAdminService {
    private final TDebourRepository tDebourRepository;
    private final EntityToFraisAdminConverter entityToFraisAdminConverter;
    private final DTOToTDeboursEntityConverter dtoToTDeboursEntityConverter;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final TDebourTypeRepository tDebourTypeRepository;

    public FraisAdminServiceImpl(TDebourRepository tDebourRepository, EntityToFraisAdminConverter entityToFraisAdminConverter, DTOToTDeboursEntityConverter dtoToTDeboursEntityConverter, LawfirmUserRepository lawfirmUserRepository, TDebourTypeRepository tDebourTypeRepository) {
        this.tDebourRepository = tDebourRepository;
        this.entityToFraisAdminConverter = entityToFraisAdminConverter;
        this.dtoToTDeboursEntityConverter = dtoToTDeboursEntityConverter;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.tDebourTypeRepository = tDebourTypeRepository;
    }

    @Override
    public FraisAdminDTO createFraisAdmin(FraisAdminDTO fraisAdminDTO, Long userId) throws ResponseStatusException {
        log.debug("Entering createFraisAdmin fraisAdminDTO {} and user id {} ", fraisAdminDTO, userId);

        if (fraisAdminDTO.getIdDoss() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dossier not attached");
        }
        TDebour tDebour = new TDebour();
        tDebour.setDateAction(ZonedDateTime.now());
        tDebour.setIdDoss(fraisAdminDTO.getIdDoss());
        tDebour.setPricePerUnit(fraisAdminDTO.getPricePerUnit());
        tDebour.setUserUpd(String.valueOf(userId));
        tDebour.setIdDebourType(fraisAdminDTO.getType());
        tDebour.setUnit(1);
        // forfait
        tDebour.setIdMesureType(EnumMesureType.FORFAIT.getId());

        tDebourRepository.save(tDebour);
        return null;
    }

    @Override
    public List<FraisAdminDTO> getAllFraisByDossierId(int limit, int offset, Long dossierId, Long userId, String vcKey) {
        log.debug("getAllFraisByDossierId with user {} limit {} and offset {} and dossierId {}", vcKey, limit, offset, dossierId);

        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "idDebour"));
            List<TDebour> pagination = tDebourRepository.findByDossierIdWithPagination(dossierId, lawfirmUsers.get().getId(), pageable);

            return entityToFraisAdminConverter.convertToList(pagination);

        }

        return new ArrayList<>();
    }

    @Override
    public List<FraisAdminDTO> getAllFrais(int limit, int offset, Long userId, String vcKey) {
        log.debug("Get all frais with user {} limit {} and offset {} ", vcKey, limit, offset);

        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.DESC, "idDebour"));
            List<TDebour> pagination = tDebourRepository.findAllWithPagination(lawfirmUsers.get().getId(), pageable);

            return entityToFraisAdminConverter.convertToList(pagination);

        }

        return new ArrayList<>();
    }

    @Override
    public Long countAllFraisByVcKey(Long dossierId, Long userId, String vcKey) {
        log.debug("countAllFraisByVcKey vckey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);
            return tDebourRepository.countByDossierIdWithPagination(dossierId, lawfirmUsers.get().getId());

        }

        return 0L;
    }

    @Override
    public FraisAdminDTO getFraisMatiere(Long idDebourType) {
        return tDebourTypeRepository.getFraisMatiere(idDebourType, false);
    }

    @Override
    public FraisAdminDTO getDefaultFrais(Long userId, String vcKey) {
        log.debug("getDefaultPrestations with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm is present {}", vcKey);

            FraisAdminDTO fraisAdminDTO = new FraisAdminDTO();

            List<ItemLongDto> debourTypeList = tDebourTypeRepository.findAllByVcKeyAndArchived(vcKey, false);
            if (debourTypeList != null) {
                fraisAdminDTO.setIdDebourType(debourTypeList.get(0).getValue());
                fraisAdminDTO = getFraisMatiere(debourTypeList.get(0).getValue());
            }
            fraisAdminDTO.setUnit(0);
            fraisAdminDTO.setIdUserResponsible(userId);
            fraisAdminDTO.setDateAction(ZonedDateTime.now());

            return fraisAdminDTO;
        }

        return null;
    }

    @Override
    public Long saveFrais(FraisAdminDTO fraisAdminDTO) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String vcKey = lawfirmToken.getVcKey();
        log.debug("savePrestation -> Vckey {}", vcKey);
        Long userId = lawfirmToken.getUserId();
        log.debug("savePrestation -> userId {}", userId);
        String username = lawfirmToken.getUsername();
        log.debug("savePrestation -> username {}", username);
        if (fraisAdminDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais not found");
        }
        if (fraisAdminDTO.getDateAction() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais action date not found");
        }
        if (fraisAdminDTO.getIdDoss() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais dossier not found");
        }
        if (fraisAdminDTO.getUnit() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais unit not found");
        }
        if (fraisAdminDTO.getPricePerUnit() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais price unit not found");
        }
        if (fraisAdminDTO.getIdDebourType() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais type not found");
        }
        if (fraisAdminDTO.getIdMesureType() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Frais mesure not found");
        }

        TDebour tDebour = new TDebour();
        if (fraisAdminDTO.getId() != null) {
            Optional<TDebour> tDebourOptional = tDebourRepository.findById(fraisAdminDTO.getId());

            if (tDebourOptional.isPresent()) {
                tDebour = tDebourOptional.get();
            }
        }
        TDebour tTimesheetToCreate = dtoToTDeboursEntityConverter.apply(fraisAdminDTO, tDebour);
        tTimesheetToCreate.setUserUpd(username);

        TDebour saved = tDebourRepository.save(tTimesheetToCreate);
        return saved.getIdDebour();
    }

    @Override
    public FraisAdminDTO getFraisById(Long userId, String vcKey, Long fraisAdminId) {
        log.debug("getDefaultPrestations with vcKey {}", vcKey);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm is present {}", vcKey);

            Optional<TDebour> optionalTDebour = tDebourRepository.findById(fraisAdminId);
            if (optionalTDebour.isPresent()) {
                return entityToFraisAdminConverter.apply(optionalTDebour.get());
            }
        }

        return null;
    }

    @Override
    public Long deleteFrais(Long userId, String vcKey, Long fraisAdminId) {
        log.debug("delete frais with user {} vcKey {} , fraisAdminId {}", userId, vcKey, fraisAdminId);
        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(vcKey, userId);

        if (lawfirmUsers.isPresent()) {
            log.debug("Law firm list {} user id {}", lawfirmUsers.get().getId(), userId);

            Optional<TDebour> debourOptional = tDebourRepository.findById(fraisAdminId);

            if (!debourOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Debours is not found");
            }

            tDebourRepository.delete(debourOptional.get());

            return fraisAdminId;
        }

        return null;
    }
}
