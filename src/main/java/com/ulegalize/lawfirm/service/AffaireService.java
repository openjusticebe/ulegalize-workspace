package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.UpdateShareResponse;
import org.springframework.web.server.ResponseStatusException;

public interface AffaireService {
    DossierDTO getDossierById(Long id_doss);

    UpdateShareResponse updateShare(LawfirmToken lawfirmToken, UpdateShareRequestDTO updateShareRequest) throws RestException, ResponseStatusException;

    public boolean hasRightAffaire(String vckey, Long affaireId);

    void switchDossierDigital(DossierDTO dossierSummary, Long userId, String vcKey);
}
