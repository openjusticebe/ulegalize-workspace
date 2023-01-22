package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.PrestationTypeDTO;

import java.util.List;

public interface PrestationTypeService {
    List<PrestationTypeDTO> getAllPrestationsType(String vcKey, Long userId);

    PrestationTypeDTO updatePrestationsType(String vcKey, Long userId, Integer prestationTypeId, PrestationTypeDTO prestationTypeDTO);

    Integer deletePrestationsType(String vcKey, Long userId, Integer prestationTypeId);

    Integer createPrestationsType(String vcKey, Long userId, PrestationTypeDTO prestationTypeDTO);

    PrestationTypeDTO getPrestationsTypeById(String vcKey, Long userId, Integer prestationsType);
}
