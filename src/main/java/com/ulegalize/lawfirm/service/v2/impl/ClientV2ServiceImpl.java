package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToClientEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToContactSummaryConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.model.entity.VirtualcabClient;
import com.ulegalize.lawfirm.repository.ClientRepository;
import com.ulegalize.lawfirm.repository.DossierRepository;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import com.ulegalize.lawfirm.service.v2.ClientV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ClientV2ServiceImpl implements ClientV2Service {

    private final ClientRepository clientRepository;
    private final DossierRepository dossierRepository;
    private final LawfirmRepository lawfirmRepository;
    private final EntityToContactSummaryConverter entityToContactSummaryConverter;
    private final DTOToClientEntityConverter dtoToClientEntityConverter;

    public ClientV2ServiceImpl(ClientRepository clientRepository, DossierRepository dossierRepository, LawfirmRepository lawfirmRepository, EntityToContactSummaryConverter entityToContactSummaryConverter, DTOToClientEntityConverter dtoToClientEntityConverter) {
        this.clientRepository = clientRepository;
        this.dossierRepository = dossierRepository;
        this.lawfirmRepository = lawfirmRepository;

        this.entityToContactSummaryConverter = entityToContactSummaryConverter;
        this.dtoToClientEntityConverter = dtoToClientEntityConverter;
    }

    @Override
    public List<ContactSummary> getAllCientByVcKey(String searchCriteria) throws LawfirmBusinessException {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllCientByVcKey searchCriteria {} vcKey {} user id {}", searchCriteria, lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        List<TClients> lawfirmClientOptional = clientRepository.findByUserIdOrVcKey(Collections.singletonList(lawfirmToken.getVcKey()), lawfirmToken.getUserId());

        List<ContactSummary> contactSummaries = entityToContactSummaryConverter.convertToList(lawfirmClientOptional);
        return searchCriteria != null && !searchCriteria.isEmpty() ? contactSummaries.stream().filter(contact -> contact.getFullName().toLowerCase().contains(searchCriteria.toLowerCase())).collect(Collectors.toList()) : contactSummaries;

    }

    @Override
    public List<ContactSummary> getAllContactsByIds(List<Long> clientIds) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllCientByVcKey searchCriteria {} vcKey {} user id {}", clientIds, lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        List<TClients> lawfirmClientOptional = clientRepository.findByIds(clientIds, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return entityToContactSummaryConverter.convertToList(lawfirmClientOptional);
    }

    @Override
    public ContactSummary getAllCientByVcKeyAndEmail(String email) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllCientByVcKeyAndEmail email {} and vcKey {} user id {}", email, lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        List<TClients> clientsList = clientRepository.findByUserIdOrVcKeyAAndF_email(Collections.singletonList(lawfirmToken.getVcKey()), lawfirmToken.getUserId(), email);

        if (clientsList == null || clientsList.isEmpty()) {
            log.debug("client list is empty");
            return null;
        }
        Optional<TClients> clientsOptional = clientsList.stream().findFirst();
        log.debug("client found {}", clientsOptional.get());
        return clientsOptional.map(tClients -> entityToContactSummaryConverter.apply(tClients)).orElse(null);

    }

    @Override
    public ContactSummary getCientById(Long clientId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getCientById clientId {} and vcKey {} user id {}", clientId, lawfirmToken.getVcKey(), lawfirmToken.getUserId());
        Optional<TClients> clientsOptional = clientRepository.findById_clientAndUserIdOrVcKey(clientId, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        if (!clientsOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }
        return entityToContactSummaryConverter.apply(clientsOptional.get());

    }

    @Override
    public ContactSummary updateContact(ContactSummary contactSummary) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering updateContact vcKey {} user id {} , contact {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId(), contactSummary);

        if (contactSummary == null) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        if (contactSummary.getId() == null) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        Optional<TClients> clientsOptional = clientRepository.findById(contactSummary.getId());

        if (clientsOptional.isEmpty()) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        contactSummary.setVcKey(lawfirmToken.getVcKey());
        TClients clients = dtoToClientEntityConverter.apply(contactSummary, clientsOptional.get());

        clients.setUser_upd(lawfirmToken.getUsername());
        clientRepository.save(clients);

        return contactSummary;
    }

    @Override
    public ContactSummary createContact(ContactSummary contactSummary) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering createContact vcKey {} user id {} , contact {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId(), contactSummary);

        if (contactSummary == null) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        TClients clients = dtoToClientEntityConverter.apply(contactSummary, new TClients());
        clients.setUser_upd(lawfirmToken.getUsername());
        if (clients.getVirtualcabClientList() == null) {
            clients.setVirtualcabClientList(new ArrayList<>());
        }

        Optional<LawfirmEntity> optionalLawfirmEntity = lawfirmRepository.findLawfirmByVckey(contactSummary.getVcKey());

        if (optionalLawfirmEntity.isPresent()) {
            VirtualcabClient virtualcabClient = new VirtualcabClient();
            virtualcabClient.setTClients(clients);
            virtualcabClient.setLawfirm(optionalLawfirmEntity.get());
            virtualcabClient.setCreUser(lawfirmToken.getUsername());
            clients.getVirtualcabClientList().add(virtualcabClient);

            clientRepository.save(clients);
        }
        return entityToContactSummaryConverter.apply(clients);
    }

    @Override
    public Long contAllCientByVcKey() {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("contAllCientByVcKey vcKey {} user id {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return clientRepository.countByUserIdOrVcKey(lawfirmToken.getVcKey(), lawfirmToken.getUserId());
    }

    @Override
    public Long countClientsByName(String vcKey, Long userId, String searchCriteria) {
        return clientRepository.countByUserIdOrVcKeyWithPagination(vcKey, userId, searchCriteria);
    }

    @Override
    public List<ContactSummary> getAllClientByVcKeyWithPagination(int offset, int limit, String searchCriteria) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllClientByVcKeyWithPagination searchCriteria {} and vcKey {} user id {}", searchCriteria, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        searchCriteria = searchCriteria != null && !searchCriteria.isEmpty() ? searchCriteria : "%";

        Pageable pageable = new OffsetBasedPageRequest(limit, offset, Sort.by(Sort.Direction.ASC, "f_nom"));
        List<TClients> lawfirmClientOptional = clientRepository.findByUserIdOrVcKeyWithPagination(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), searchCriteria, pageable);

        return entityToContactSummaryConverter.convertToList(lawfirmClientOptional);
    }

    @Override
    public Long deleteClient(Long clientId) {
        Optional<TClients> clientsOptional = clientRepository.findById(clientId);

        if (!clientsOptional.isPresent()) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        // check if it's used
        Long nbClient = dossierRepository.countByClient_cabAndOrClient_adv(clientId, clientId, clientId);

        if (nbClient > 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Client is linked to a dossier");
        }

        clientRepository.delete(clientsOptional.get());
        return clientId;
    }
}
