package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualCabTags;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.VirtualcabTagsRepository;
import com.ulegalize.lawfirm.service.v2.VirtualcabTagsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class VirtualCabTagsServiceImpl implements VirtualcabTagsService {

    private final VirtualcabTagsRepository virtualcabTagsRepository;

    private final LawfirmRepository lawfirmRepository;

    public VirtualCabTagsServiceImpl(VirtualcabTagsRepository virtualcabTagsRepository, LawfirmRepository lawfirmRepository) {
        this.virtualcabTagsRepository = virtualcabTagsRepository;
        this.lawfirmRepository = lawfirmRepository;
    }

    @Override
    public List<ItemLongDto> getTagsByVckey(String searchCriteria) {

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirmEntityOptional = lawfirmRepository.findLawfirmByVckey(lawfirmToken.getVcKey());

        if (lawfirmEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lawfirm doesn't exist");
        }

        searchCriteria = searchCriteria != null && !searchCriteria.isEmpty() ? searchCriteria : "%";

        return virtualcabTagsRepository.findTVirtualCabTagsBySearchCriteria(lawfirmToken.getVcKey(), searchCriteria);
    }

    @Override
    public Optional<TVirtualCabTags> findTVirtualCabTagsByLabel(String label) {

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<TVirtualCabTags> tVirtualCabTagsOptional = virtualcabTagsRepository.findTVirtualCabTagsByLabel(label, lawfirmToken.getVcKey());

        if (tVirtualCabTagsOptional.isEmpty()) {
            log.error("Tags {} does not exist !", label);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tags with label {} not found");
        }

        return tVirtualCabTagsOptional;
    }
}
