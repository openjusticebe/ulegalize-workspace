package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.PrestationSummary;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PrestationService {
    Page<PrestationSummary> getAllPrestations(int limit, int offset, Long userId, String vcKey, String searchCriteriaNomenclature, Integer searchCriteriaIdTsType);

    Page<PrestationSummary> getAllPrestationsByDossierId(int limit, int offset, Long dossierId, Long userId, String vcKey);

    List<PrestationSummary> getPrestationsByDossierId(Long dossierId, Long userId, String vcKey);

    Long countAllPrestationByVcKey(Long dossierId, Long userId, String vcKey);

    PrestationSummary getDefaultPrestations(Long dossierId, Long userId, String vcKey);

    Long savePrestation(PrestationSummary prestationSummary);

    PrestationSummary getPrestationById(Long userId, String vcKey, Long prestationId);

    Long deletePrestation(Long userId, String vcKey, Long prestationId);
}
