package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ModelDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToTemplateEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToTemplateConverter;
import com.ulegalize.lawfirm.model.entity.TTemplates;
import com.ulegalize.lawfirm.repository.TTemplatesRepository;
import com.ulegalize.lawfirm.service.v2.TemplateV2Service;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class TemplateV2ServiceImpl implements TemplateV2Service {
    private final TTemplatesRepository tTemplatesRepository;
    private final EntityToTemplateConverter entityToTemplateConverter;
    private final DTOToTemplateEntityConverter dtoToTemplateEntityConverter;

    public TemplateV2ServiceImpl(TTemplatesRepository tTemplatesRepository, EntityToTemplateConverter entityToTemplateConverter, DTOToTemplateEntityConverter dtoToTemplateEntityConverter) {
        this.tTemplatesRepository = tTemplatesRepository;
        this.entityToTemplateConverter = entityToTemplateConverter;
        this.dtoToTemplateEntityConverter = dtoToTemplateEntityConverter;
    }

    @Override
    public List<ModelDTO> getModelsList() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering getModelsList vckey {}", lawfirmToken.getVcKey());

        List<TTemplates> tTemplatesList = tTemplatesRepository.findAllByVcKeyOrderByTypeDescNameAsc(lawfirmToken.getVcKey());
        return entityToTemplateConverter.convertToList(tTemplatesList);
    }

    @Override
    public Long updateModels(ModelDTO modelDTO) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering updateModelsList templateId {} and vc key {}", modelDTO, lawfirmToken.getVcKey());

        commonRuleTemplate(modelDTO);

        if (modelDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template not found");
        }

        Optional<TTemplates> tTemplatesOptional = tTemplatesRepository.findByIdAndVcKey(modelDTO.getId(), lawfirmToken.getVcKey());

        if (!tTemplatesOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template not found");
        }
        TTemplates tTemplates = dtoToTemplateEntityConverter.apply(modelDTO, tTemplatesOptional.get());
        tTemplates.setUserUpd(lawfirmToken.getUsername());
        tTemplatesRepository.save(tTemplates);
        return tTemplates.getId();
    }

    @Override
    public Long createModels(ModelDTO modelDTO) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering createModels templateId {} and vc key {}", modelDTO, lawfirmToken.getVcKey());

        commonRuleTemplate(modelDTO);

        TTemplates tTemplates = dtoToTemplateEntityConverter.apply(modelDTO, new TTemplates());
        tTemplates.setVcKey(lawfirmToken.getVcKey());
        tTemplates.setUserUpd(lawfirmToken.getUsername());
        tTemplates.setDateUpd(LocalDateTime.now());
        tTemplatesRepository.save(tTemplates);

        return tTemplates.getId();
    }

    @Override
    public Long deleteModels(Long modelsId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering deleteModels templateId {} and vc key {}", modelsId, lawfirmToken.getVcKey());

        if (modelsId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template not found");
        }
        Optional<TTemplates> tTemplatesOptional = tTemplatesRepository.findByIdAndVcKey(modelsId, lawfirmToken.getVcKey());

        if (!tTemplatesOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template not found");
        }
        tTemplatesRepository.delete(tTemplatesOptional.get());
        return modelsId;
    }

    @Override
    public JSONObject getTemplatDataByDossier(Long dossierId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering getTemplatDataByDossier dossierId {} and vckey {}", dossierId, lawfirmToken.getVcKey());

        return tTemplatesRepository.getTemplatDataByDossier(dossierId, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

    }

    @Override
    public JSONObject getTemplatData() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering getTemplatData vckey {}", lawfirmToken.getVcKey());

        return tTemplatesRepository.getTemplatData(lawfirmToken.getVcKey(), lawfirmToken.getUserId());

    }

    private void commonRuleTemplate(ModelDTO modelDTO) throws ResponseStatusException {
        if (modelDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template not found");
        }
        if (modelDTO.getFormat() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template format not found");
        }
        if (modelDTO.getContext() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template context not found");
        }
        if (modelDTO.getName() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template name not found");
        }
        if (modelDTO.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "template description not found");
        }
    }
}
