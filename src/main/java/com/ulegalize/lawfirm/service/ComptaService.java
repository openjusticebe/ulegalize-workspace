package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.InvoiceDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ComptaService {
    ComptaDTO getComptaById(Long fraisId, String vcKey, String language) throws ResponseStatusException;

    List<ItemDto> getGridList();

    Page<ComptaDTO> getAllComptaByDossierId(int limit, int offset, Long dossierId, String vcKey, Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers, String language);

    Page<ComptaDTO> getAllCompta(int limit, int offset, String vcKey, String searchCriteriaClient, String searchCriteriaNomenclature, String searchCriteriaPoste, Integer typeId, Integer searchCriteriaCompte, String language);

    ComptaDTO updateCompta(ComptaDTO comptaDTO, String vcKey);

    ComptaDTO getDefaultCompta(Long userId, String vcKey, String language);

    Long createCompta(ComptaDTO comptaDTO, String vcKey);

    ItemBigDecimalDto getTvaDefaultCompta(Long userId, String vcKey);

    InvoiceDTO totalHonoraireByDossierId(Long dossierId);

    ComptaDTO totalThirdPartyByDossierId(Long dossierId);

    void deactivateCompta(Long fraisId);
}
