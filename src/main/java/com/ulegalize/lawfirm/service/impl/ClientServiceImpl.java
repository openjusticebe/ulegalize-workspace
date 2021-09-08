package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.converter.EntityToContactSummaryConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.repository.ClientRepository;
import com.ulegalize.lawfirm.repository.LawfirmUserRepository;
import com.ulegalize.lawfirm.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private LawfirmUserRepository lawfirmUserRepository;
    private EntityToContactSummaryConverter entityToContactSummaryConverter;

    public ClientServiceImpl(ClientRepository clientRepository, LawfirmUserRepository lawfirmUserRepository, EntityToContactSummaryConverter entityToContactSummaryConverter) {
        this.clientRepository = clientRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;

        this.entityToContactSummaryConverter = entityToContactSummaryConverter;
    }

    @Override
    public List<ContactSummary> getAllCientByUserId(Long userId, String searchCriteria) throws LawfirmBusinessException {
        log.debug("getAllCientByUserId user id {}", userId);
        List<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByUserId(userId);

        List<String> vcKeys = lawfirmUsers.stream().map(lawfirmUsers1 -> lawfirmUsers1.getLawfirm().getVckey()).collect(Collectors.toList());
        log.debug("Law firm list {} user id {}", vcKeys, userId);

        List<TClients> lawfirmClientOptional = clientRepository.findByUserIdOrVcKey(vcKeys, userId);

        List<ContactSummary> contactSummaries = entityToContactSummaryConverter.convertToList(lawfirmClientOptional);
        return searchCriteria != null && !searchCriteria.isEmpty() ? contactSummaries.stream().filter(contact -> contact.getFullName().toLowerCase().contains(searchCriteria.toLowerCase())).collect(Collectors.toList()) : contactSummaries;

    }
}
