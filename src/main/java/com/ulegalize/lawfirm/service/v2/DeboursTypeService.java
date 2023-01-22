package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.FraisAdminDTO;

import java.util.List;

public interface DeboursTypeService {
    List<FraisAdminDTO> getAllDeboursType(String vcKey, Long userId);

    FraisAdminDTO updateDeboursType(String vcKey, Long userId, Long deboursTypeId, FraisAdminDTO FraisAdminDTO);

    Long deleteDeboursType(String vcKey, Long userId, Long deboursTypeId);

    Long createDeboursType(String vcKey, Long userId, FraisAdminDTO prestationTypeDTO);

    FraisAdminDTO getDeboursTypeById(String vcKey, Long userId, Long deboursTypeId);
}
