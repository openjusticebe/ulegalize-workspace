package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.AccountingTypeDTO;

import java.util.List;

public interface RefPosteService {
    public List<AccountingTypeDTO> getAllRefPoste(String vcKey, Long userId);

    AccountingTypeDTO updateRefPoste(String vcKey, Long userId, Integer refPosteId, AccountingTypeDTO accountingTypeDTO);

    Integer deleteRefPoste(String vcKey, Long userId, Integer refPosteId);

    Integer createRefPoste(String vcKey, Long userId, AccountingTypeDTO accountingTypeDTO);

    AccountingTypeDTO getRefPosteById(String vcKey, Long userId, Integer refPosteId);
}
