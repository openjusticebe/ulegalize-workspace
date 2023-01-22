package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToVirtualcabNomenclatureConverter;
import com.ulegalize.lawfirm.model.converter.EntityToVirtualcabNomenclatureDTOConverter;
import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import com.ulegalize.lawfirm.repository.TVirtualcabNomenclatureRepository;
import com.ulegalize.lawfirm.service.v2.VirtualcabNomenclatureService;
import com.ulegalize.lawfirm.service.validator.VirtualcabNomenclatureValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class VitualcabNomenclatureServiceImpl implements VirtualcabNomenclatureService {
    private final TVirtualcabNomenclatureRepository tVirtualcabNomenclatureRepository;
    private final LawfirmRepository lawfirmRepository;
    private final VirtualcabNomenclatureValidator virtualcabNomenclatureValidator;
    private final DTOToVirtualcabNomenclatureConverter dtoToVirtualcabNomenclatureConverter;
    private final EntityToVirtualcabNomenclatureDTOConverter entityToVirtualcabNomenclatureDTOConverter;

    public VitualcabNomenclatureServiceImpl(TVirtualcabNomenclatureRepository tVirtualcabNomenclatureRepository, LawfirmRepository lawfirmRepository, VirtualcabNomenclatureValidator virtualcabNomenclatureValidator, DTOToVirtualcabNomenclatureConverter dtoToVirtualcabNomenclatureConverter, EntityToVirtualcabNomenclatureDTOConverter entityToVirtualcabNomenclatureDTOConverter) {
        this.tVirtualcabNomenclatureRepository = tVirtualcabNomenclatureRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.virtualcabNomenclatureValidator = virtualcabNomenclatureValidator;
        this.dtoToVirtualcabNomenclatureConverter = dtoToVirtualcabNomenclatureConverter;
        this.entityToVirtualcabNomenclatureDTOConverter = entityToVirtualcabNomenclatureDTOConverter;
    }

    @Override
    public Long saveVirtualcabNomenclature(VirtualcabNomenclatureDTO nomenclatureDTO) {
        log.info("Entering saveVirtualcabNomenclature() with VirtualcabNomenclatureDTO {}", nomenclatureDTO);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        virtualcabNomenclatureValidator.validate(nomenclatureDTO);

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOld = tVirtualcabNomenclatureRepository.findByLawfirmEntityAndAndName(lawfirmEntityOptional.get(), nomenclatureDTO.getName());

        if (virtualcabNomenclatureOld.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualcabNomenclature is not unique !" + virtualcabNomenclatureOld);
        }

        TVirtualcabNomenclature virtualcabNomenclature = new TVirtualcabNomenclature();
        virtualcabNomenclature.setLawfirmEntity(lawfirmEntityOptional.get());

        virtualcabNomenclature = dtoToVirtualcabNomenclatureConverter.apply(nomenclatureDTO, virtualcabNomenclature);
        virtualcabNomenclature.setCreUser(lawfirmToken.getUsername());

        tVirtualcabNomenclatureRepository.save(virtualcabNomenclature);

        log.info("Leaving saveVirtualcabNomenclature()");
        return virtualcabNomenclature.getId();
    }

    @Override
    public List<ItemLongDto> getAllVirtualcabNomenclature() {
        log.info("Entering getAllVirtualcabNomenclature()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        return tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntityOptional.get());
    }

    @Override
    public VirtualcabNomenclatureDTO getVirtualcabNomenclature(String name) {
        log.info("Entering getAllVirtualcabNomenclature()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        if (name == null || name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualcabNomenclature name is null or empty");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByLawfirmEntityAndAndName(lawfirmEntityOptional.get(), name);

        log.info("Leaving getAllVirtualcabNomenclature()");
        return virtualcabNomenclatureOptional.isEmpty() ? null : entityToVirtualcabNomenclatureDTOConverter.apply(virtualcabNomenclatureOptional.get());
    }

    @Override
    public VirtualcabNomenclatureDTO getVirtualcabNomenclatureById(Long id) {
        log.info("Entering getVirtualcabNomenclatureById()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualcabNomenclature id is null");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByIdAndLawfirmEntity(id, lawfirmEntityOptional.get());

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualcabNomenclature doesn't exist");
        }
        log.info("Leaving getVirtualcabNomenclatureById()");
        return entityToVirtualcabNomenclatureDTOConverter.apply(virtualcabNomenclatureOptional.get());
    }

    @Override
    public VirtualcabNomenclatureDTO updateVirtualcabNomenclature(VirtualcabNomenclatureDTO virtualcabNomenclatureDTO) {
        log.info("Entering updateVirtualcabNomenclature() with virtualcabNomenclatureDTO {}", virtualcabNomenclatureDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByIdAndLawfirmEntity(virtualcabNomenclatureDTO.getId(), lawfirmEntityOptional.get());

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualcabNomenclature does not exist" + virtualcabNomenclatureOptional);
        }
        virtualcabNomenclatureValidator.validate(virtualcabNomenclatureDTO);

        TVirtualcabNomenclature virtualcabNomenclature = dtoToVirtualcabNomenclatureConverter.apply(virtualcabNomenclatureDTO, virtualcabNomenclatureOptional.get());

        tVirtualcabNomenclatureRepository.save(virtualcabNomenclature);

        log.info("Leaving updateVirtualcabNomenclature()");

        return entityToVirtualcabNomenclatureDTOConverter.apply(virtualcabNomenclature);
    }

    @Override
    public Long deleteVirtualcabNomenclature(Long idVcNomenclature) {
        log.info("Entering deleteVirtualcabNomenclature() with idVcNomenclature {}", idVcNomenclature);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByIdAndLawfirmEntity(idVcNomenclature, lawfirmEntityOptional.get());

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VirtualcabNomenclature does not exist" + virtualcabNomenclatureOptional);
        }

        List<ItemLongDto> virtualcabNomenclatureOld = tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntityOptional.get());

        if (virtualcabNomenclatureOld.size() == 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtualcab Nomenclature cannot be deleted as it is the latest" + virtualcabNomenclatureOld.size());
        }

        tVirtualcabNomenclatureRepository.delete(virtualcabNomenclatureOptional.get());
        log.info("Leaving deleteVirtualcabNomenclature()");
        return idVcNomenclature;
    }

    @Override
    public Page<VirtualcabNomenclatureDTO> getAllVirtualcabNomenclatureWithPagination(int limit, int offset) {
        log.info("Entering getVirtualcabNomenclatureWithPagination(with limit {} and offset {})", limit, offset);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }
        Pageable pageable = new OffsetBasedPageRequest(limit, offset);

        Page<TVirtualcabNomenclature> virtualcabNomenclaturePage = tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntityOptional.get(), pageable);

        List<VirtualcabNomenclatureDTO> virtualcabNomenclatureDTOList = entityToVirtualcabNomenclatureDTOConverter.convertToList(virtualcabNomenclaturePage.getContent());

        return new PageImpl<>(virtualcabNomenclatureDTOList, Pageable.unpaged(), virtualcabNomenclaturePage.getTotalElements());
    }

    @Override
    public List<ItemLongDto> getAllVirtualcabNomenclatureByVckey(String vcKey) {
        log.info("Entering getAllVirtualcabNomenclatureByVckey()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        if (vcKey == null || vcKey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vcKey to find is null");
        }
        Optional<LawfirmEntity> lawfirmEntityOptionalToChange = lawfirmRepository.findLawfirmByVckey(vcKey);

        if (lawfirmEntityOptionalToChange.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vcKey to find doesn't exist");
        }

        return tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntityOptionalToChange.get());
    }
}
