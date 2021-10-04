package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ComptaService {
    public ComptaDTO getComptaById(Long fraisId, String vcKey) throws ResponseStatusException;

    List<ItemDto> getGridList();

    Page<ComptaDTO> getAllComptaByDossierId(int limit, int offset, Long dossierId, String vcKey, Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers);

    Page<ComptaDTO> getAllCompta(int limit, int offset, String vcKey, String searchCriteriaClient, String searchCriteriaYear, Long searchCriteriaNumber, String searchCriteriaPoste);

    ComptaDTO updateCompta(ComptaDTO comptaDTO, String vcKey);

    ComptaDTO getDefaultCompta(Long userId, String vcKey);

    Long createCompta(ComptaDTO comptaDTO, String vcKey);

    ItemBigDecimalDto getTvaDefaultCompta(Long userId, String vcKey);

}
