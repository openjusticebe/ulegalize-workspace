package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.LawfirmConfigDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.TVirtualcabConfig;
import com.ulegalize.lawfirm.repository.TVirtualcabConfigRepository;
import com.ulegalize.lawfirm.service.v2.LawfirmConfigV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class LawfirmConfigV2ServiceImpl implements LawfirmConfigV2Service {
    private final TVirtualcabConfigRepository lawfirmConfigRepository;

    public LawfirmConfigV2ServiceImpl(TVirtualcabConfigRepository lawfirmConfigRepository) {
        this.lawfirmConfigRepository = lawfirmConfigRepository;
    }

    @Override
    public List<LawfirmConfigDTO> getLawfirmConfigInfoByVcKey(String vcKey) {
        log.info("Entering getLawfirmConfigInfoByVcKey vckey {}", vcKey);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<TVirtualcabConfig> lawfirmConfigList = lawfirmConfigRepository.findAllByVcKey(vcKey);
        // List<LawfirmConfigDTO> lawfirmConfigDTOList =
        if (lawfirmConfigList.size() == 0 || lawfirmConfigList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "lawfirmConfig is not found");
        }
        List<LawfirmConfigDTO> DTOList = lawfirmConfigList.stream().map(lawfirmConfigEntity -> {
            log.debug("LawfirmConfig");
            return new LawfirmConfigDTO(lawfirmConfigEntity.getVcKey(), lawfirmConfigEntity.getDescription(),
                    lawfirmConfigEntity.getParameter());
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return DTOList;
    }

    @Override
    public void addLawfirmConfigByVcKey(LawfirmConfigDTO lawfirmConfigDTO, String vcKey) {
        log.debug("Entering addLawfirmInfoByVcKey");
        log.debug(lawfirmConfigDTO.getDescription());

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        // List<TVirtualcabConfig> oldLawfirmConfigList = lawfirmConfigRepository
        // .findAllByVcKey(lawfirmConfigDTO.getVcKey());

        TVirtualcabConfig newLawfirmConfig = new TVirtualcabConfig();

        newLawfirmConfig.setVcKey(lawfirmToken.getVcKey());
        newLawfirmConfig.setParameter(lawfirmConfigDTO.getParameter());
        newLawfirmConfig.setDescription(lawfirmConfigDTO.getDescription());

        lawfirmConfigRepository.save(newLawfirmConfig);

        log.debug("addLawfirmInfoByVcKey vckey {}", lawfirmToken.getVcKey());
        // return getLawfirmConfigInfoByVcKey(lawfirmToken.getVcKey());
    }

    @Override
    public void removeLawfirmConfig(String lawfirmConfigDescription, String vcKey) {
        log.debug("Entering removeLawfirmConfig");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        TVirtualcabConfig cabConfigEntity = lawfirmConfigRepository.findLawfirmConfigInfoByVcKeyAndDescription(vcKey,
                lawfirmConfigDescription);

        lawfirmConfigRepository.delete(cabConfigEntity);

        log.debug("removeLawfirmConfig vckey {}", lawfirmToken.getVcKey());
    }
}
