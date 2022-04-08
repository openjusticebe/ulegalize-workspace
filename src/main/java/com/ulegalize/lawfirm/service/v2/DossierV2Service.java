package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumVCOwner;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DossierV2Service {
    DossierDTO getDossierById(Long id_doss);

    /**
     * @param limit
     * @param offset
     * @param userId
     * @param vcKey
     * @param enumVCOwner
     * @param searchCriteriaClient
     * @param searchCriteriaYear
     * @param searchCriteriaNumber
     * @param searchCriteriaBalance
     * @param searchCriteriaInitiale
     * @param searchArchived
     * @param sortOpenDate           0 is desc , 1 asc , null order by last_access_date
     * @return
     */
    public Page<DossierDTO> getAllAffaires(int limit, int offset, Long userId, String vcKey, List<EnumVCOwner> enumVCOwner, String searchCriteriaClient, String searchCriteriaYear, Long searchCriteriaNumber, Boolean searchCriteriaBalance, String searchCriteriaInitiale, Boolean searchArchived, Boolean sortOpenDate);

    Long saveAffaire(DossierDTO dossierDTO, String vcKey);

    Long saveAffaireAndCreateCase(DossierDTO dossierDTO, String vcKey);

    Long saveAffaireAndAttachToCase(String caseId, DossierDTO dossierDTO, String vcKey);

    DossierDTO getDefaultDossier(String vcKey, Long userId);

    DossierDTO updateAffaire(DossierDTO dossierDTO, Long userId, String username, String vcKey);

    List<ItemLongDto> getAffairesByVcUserIdAndSearchCriteria(String vcKey, Long userId, String searchCriteria);

    FinanceDTO getFinanceDossierById(Long dossierId);

    Long countAffairesByClientId(Long clientId);

    List<ShareAffaireDTO> getSharedUserByAffaireId(Long affaireid, String vcKey);

    void addShareFolderUser(ShareAffaireDTO shareAffaireDTO, boolean isSendMail);

    void deleteShareFolderUser(ShareAffaireDTO shareAffaireDTO);

    void switchDossierDigital(Long dossierId, Long userId, String vcKey);

    String inviteConseil(Long dossierId, ItemPartieDTO partieDTO);

    String addShareAllFolderUser();

    List<DossierDTO> findAllByVCKey(String vcKey, Long userId);

}
