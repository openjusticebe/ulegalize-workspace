package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ComptaService {
    public ComptaDTO getComptaById(Long fraisId, String vcKey) throws ResponseStatusException;

    List<ItemDto> getGridList();

    List<ComptaDTO> getAllComptaByDossierId(int limit, int offset, Long dossierId, String vcKey, Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers);

    List<ComptaDTO> getAllCompta(int limit, int offset, String vcKey);

    Long countAllComptaByVcKey(Long dossierId, String vcKey, Boolean isDebours, Boolean isFraiCollaboration, Boolean honoraire, Boolean tiers);

    Long countAllCompta(String vcKey);

    ComptaDTO updateCompta(ComptaDTO comptaDTO, String vcKey);

    ComptaDTO getDefaultCompta(Long userId, String vcKey);

    Long createCompta(ComptaDTO comptaDTO, String vcKey);

    ItemBigDecimalDto getTvaDefaultCompta(Long userId, String vcKey);

}
