package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.NomenclatureConfigDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TNomenclatureConfig;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.TNomenclatureConfigRepository;
import com.ulegalize.lawfirm.repository.TVirtualcabNomenclatureRepository;
import com.ulegalize.lawfirm.service.v2.NomenclatureConfigService;
import com.ulegalize.lawfirm.service.validator.NomenclatureConfigValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class NomenclatureConfigServiceImpl implements NomenclatureConfigService {
    private final TNomenclatureConfigRepository nomenclatureConfigRepository;
    private final TVirtualcabNomenclatureRepository virtualcabNomenclatureRepository;
    private final LawfirmRepository lawfirmRepository;
    private final NomenclatureConfigValidator nomenclatureConfigValidator;

    public NomenclatureConfigServiceImpl(TNomenclatureConfigRepository nomenclatureConfigRepository, TVirtualcabNomenclatureRepository virtualcabNomenclatureRepository, LawfirmRepository lawfirmRepository, NomenclatureConfigValidator nomenclatureConfigValidator) {
        this.nomenclatureConfigRepository = nomenclatureConfigRepository;
        this.virtualcabNomenclatureRepository = virtualcabNomenclatureRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.nomenclatureConfigValidator = nomenclatureConfigValidator;
    }

    @Override
    public List<NomenclatureConfigDTO> getNomenclatureConfigInfoByVcNomenclature(Long idVcNomenclature, String vckey) {
        log.info("Entering getNomenclatureConfigInfoByVcNomenclature idVcNomenclature {}", idVcNomenclature);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntity = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional;
        if (lawfirmEntity.get().getVckey().equals(vckey)) {
            virtualcabNomenclatureOptional = virtualcabNomenclatureRepository.findByIdAndLawfirmEntity(idVcNomenclature, lawfirmEntity.get());
        } else {
            Optional<LawfirmEntity> lawfirmToFind = lawfirmRepository.findLawfirmByVckey(vckey);

            if (lawfirmToFind.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
            }

            virtualcabNomenclatureOptional = virtualcabNomenclatureRepository.findByIdAndLawfirmEntity(idVcNomenclature, lawfirmToFind.get());
        }

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtualcab Nomenclature doesn't exist");
        }

        List<TNomenclatureConfig> nomenclatureConfigList = nomenclatureConfigRepository.findAllByVcNomenclature(virtualcabNomenclatureOptional.get());
        if (nomenclatureConfigList.size() == 0) {
            log.debug("nomenclatureConfigList is empty");
            return new ArrayList<>();
        }
        List<NomenclatureConfigDTO> DTOList = nomenclatureConfigList.stream().map(nomenclatureConfig -> {
            log.debug("nomenclatureConfig {}", nomenclatureConfig);
            return new NomenclatureConfigDTO(nomenclatureConfig.getVcNomenclature().getId(), nomenclatureConfig.getVcNomenclature().getLawfirmEntity().getVckey(), nomenclatureConfig.getLabel(),
                    nomenclatureConfig.getParameter());
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return DTOList;
    }

    @Override
    public Long addNomenclatureConfigByVcNomenclature(NomenclatureConfigDTO nomenclatureConfigDTO) {
        log.debug("Entering addNomenclatureConfigByVcNomenclature() with nomenclatureConfigDTO {}", nomenclatureConfigDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntity = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = virtualcabNomenclatureRepository.findByIdAndLawfirmEntity(nomenclatureConfigDTO.getVcNomenclature(), lawfirmEntity.get());

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtualcab Nomenclature doesn't exist");
        }

        nomenclatureConfigValidator.validate(nomenclatureConfigDTO);

        Optional<TNomenclatureConfig> tNomenclatureConfigOld = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclatureOptional.get(), nomenclatureConfigDTO.getDescription());
        if (tNomenclatureConfigOld.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NomenclatureConfig is not unique");
        }

        TNomenclatureConfig nomenclatureConfig = new TNomenclatureConfig();
        nomenclatureConfig.setVcNomenclature(virtualcabNomenclatureOptional.get());
        nomenclatureConfig.setParameter(nomenclatureConfigDTO.getParameter());
        nomenclatureConfig.setLabel(nomenclatureConfigDTO.getDescription());
        nomenclatureConfig.setCreUser(lawfirmToken.getUsername());

        nomenclatureConfigRepository.save(nomenclatureConfig);

        log.debug("Leaving addNomenclatureConfigByVcNomenclature() with nomenclatureConfig id {}", nomenclatureConfig);

        return nomenclatureConfig.getId();
    }

    @Override
    public void removeNomenclatureConfig(String nomenclatureConfigLabel, Long vcNomenclatureId) {
        log.debug("Entering removeNomenclatureConfig() with nomenclatureConfigLabel {}", nomenclatureConfigLabel);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Optional<LawfirmEntity> lawfirmEntity = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = virtualcabNomenclatureRepository.findByIdAndLawfirmEntity(vcNomenclatureId, lawfirmEntity.get());

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtualcab Nomenclature doesn't exist");
        }

        Optional<TNomenclatureConfig> nomenclatureConfig = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclatureOptional.get(), nomenclatureConfigLabel);

        if (nomenclatureConfig.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nomenclatureConfig doesn't exist");
        }
        nomenclatureConfigRepository.delete(nomenclatureConfig.get());

        log.debug("Leaving removeNomenclatureConfig()");
    }

    @Override
    public NomenclatureConfigDTO getNomenclatureConfig(Long vcNomenclatureId, String nomenclatureConfigName) {
        log.info("Entering getNomenclatureConfig vcNomenclatureId {} and nomenclatureConfig {}", vcNomenclatureId, nomenclatureConfigName);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntity = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = virtualcabNomenclatureRepository.findByIdAndLawfirmEntity(vcNomenclatureId, lawfirmEntity.get());

        if (virtualcabNomenclatureOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Virtualcab Nomenclature doesn't exist");
        }

        Optional<TNomenclatureConfig> nomenclatureConfig = nomenclatureConfigRepository.findByVcNomenclatureAndLabel(virtualcabNomenclatureOptional.get(), nomenclatureConfigName);
        if (nomenclatureConfig.isEmpty()) {
            log.debug("Leaving getNomenclatureConfig() with nomenclatureConfig = null");
            return null;
        }

        NomenclatureConfigDTO nomenclatureConfigDTO = new NomenclatureConfigDTO();
        nomenclatureConfigDTO.setDescription(nomenclatureConfig.get().getLabel());
        nomenclatureConfigDTO.setVcNomenclature(nomenclatureConfig.get().getVcNomenclature().getId());
        nomenclatureConfigDTO.setParameter(nomenclatureConfig.get().getParameter());
        nomenclatureConfigDTO.setVcKey(lawfirmEntity.get().getVckey());

        log.debug("Leaving getNomenclatureConfig()");
        return nomenclatureConfigDTO;
    }
}
