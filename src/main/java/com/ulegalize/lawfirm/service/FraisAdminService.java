package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.FraisAdminDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface FraisAdminService {
    public FraisAdminDTO createFraisAdmin(FraisAdminDTO fraisAdminDTO, Long userId) throws ResponseStatusException;

    List<FraisAdminDTO> getAllFraisByDossierId(int limit, int offset, Long dossierId, Long userId, String vcKey);

    List<FraisAdminDTO> getAllFrais(int limit, int offset, Long userId, String vcKey);

    Long countAllFraisByVcKey(Long dossierId, Long userId, String vcKey);

    FraisAdminDTO getFraisMatiere(Long idDebourType);

    FraisAdminDTO getDefaultFrais(Long userId, String vcKey);

    Long saveFrais(FraisAdminDTO fraisAdminDTO);

    FraisAdminDTO getFraisById(Long userId, String vcKey, Long fraisAdminId);

    Long deleteFrais(Long userId, String vcKey, Long fraisAdminId);
}
