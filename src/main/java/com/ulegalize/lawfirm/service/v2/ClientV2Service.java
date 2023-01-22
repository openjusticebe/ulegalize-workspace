package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;

import java.util.List;

public interface ClientV2Service {
    List<ContactSummary> getAllCientByVcKey(String searchCriteria, Long dossierId, Boolean withEmail) throws LawfirmBusinessException;

    List<ContactSummary> getAllContactsByIds(List<Long> clientIds);

    ContactSummary getAllCientByVcKeyAndEmail(String email) throws LawfirmBusinessException;

    ContactSummary getCientById(Long clientId);

    ContactSummary updateContact(ContactSummary contactSummary);

    ContactSummary createContact(ContactSummary contactSummary);

    Long contAllCientByVcKey();

    Long countClientsByName(String vcKey, Long userId, String searchCriteria);

    List<ContactSummary> getAllClientByVcKeyWithPagination(int offset, int limit, String searchCriteria);

    Long deleteClient(Long clientId);

    List<ContactSummary> getContactByDossierId(Long dossierId);

    List<ContactSummary> findTClientsByIdDoss(Long dossierId, String dossierType);
}
