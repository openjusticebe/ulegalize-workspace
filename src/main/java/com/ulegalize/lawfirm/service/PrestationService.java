package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.PrestationSummary;

import java.util.List;

public interface PrestationService {
    public List<PrestationSummary> getAllPrestations(int limit, int offset, Long userId, String vcKey);

    List<PrestationSummary> getAllPrestationsByDossierId(int limit, int offset, Long dossierId, Long userId, String vcKey);

    List<PrestationSummary> getPrestationsByDossierId(Long dossierId, Long userId, String vcKey);

    Long countAllPrestationByVcKey(Long dossierId, Long userId, String vcKey);

    PrestationSummary getDefaultPrestations(Long userId, String vcKey);

    Long savePrestation(PrestationSummary prestationSummary);

    PrestationSummary getPrestationById(Long userId, String vcKey, Long prestationId);

    Long deletePrestation(Long userId, String vcKey, Long prestationId);
}
