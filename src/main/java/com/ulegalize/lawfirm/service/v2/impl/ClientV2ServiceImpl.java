package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.enumeration.EnumDossierContactType;
import com.ulegalize.enumeration.EnumDossierType;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.kafka.producer.transparency.IClientProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.DTOToClientEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToContactSummaryConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.model.entity.VirtualcabClient;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.service.v2.ClientV2Service;
import com.ulegalize.lawfirm.service.validator.ContactValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Slf4j
@Transactional
public class ClientV2ServiceImpl implements ClientV2Service {

    private final ClientRepository clientRepository;
    private final DossierRepository dossierRepository;
    private final LawfirmRepository lawfirmRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final EntityToContactSummaryConverter entityToContactSummaryConverter;
    private final DTOToClientEntityConverter dtoToClientEntityConverter;
    private final ContactValidator contactValidator;
    private final IClientProducer clientProducer;

    public ClientV2ServiceImpl(ClientRepository clientRepository, DossierRepository dossierRepository, LawfirmRepository lawfirmRepository, LawfirmUserRepository lawfirmUserRepository, EntityToContactSummaryConverter entityToContactSummaryConverter, DTOToClientEntityConverter dtoToClientEntityConverter, ContactValidator contactValidator, IClientProducer clientProducer) {
        this.clientRepository = clientRepository;
        this.dossierRepository = dossierRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;

        this.entityToContactSummaryConverter = entityToContactSummaryConverter;
        this.dtoToClientEntityConverter = dtoToClientEntityConverter;
        this.contactValidator = contactValidator;
        this.clientProducer = clientProducer;
    }

    @Override
    public List<ContactSummary> getAllCientByVcKey(String searchCriteria, Long dossierId, Boolean withEmail) throws LawfirmBusinessException {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllCientByVcKey searchCriteria '{}' vcKey {} user id {} and dossierId {}", searchCriteria, lawfirmToken.getVcKey(), lawfirmToken.getUserId(), dossierId);
        List<TClients> lawfirmClientOptional = clientRepository.findByUserIdOrVcKeyAndDossierId(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), searchCriteria, dossierId, withEmail);

        return entityToContactSummaryConverter.convertToList(lawfirmClientOptional, lawfirmToken.getLanguage());
    }

    @Override
    public List<ContactSummary> getAllContactsByIds(List<Long> clientIds) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllContactsByIds searchCriteria {} vcKey {} user id {}", clientIds, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        List<TClients> lawfirmClientOptional = clientRepository.findByIds(clientIds, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        List<TClients> lawfirmClientOptionalWithoutDuplicates = new ArrayList<>(
                new LinkedHashSet<>(lawfirmClientOptional));

        return entityToContactSummaryConverter.convertToList(lawfirmClientOptionalWithoutDuplicates, lawfirmToken.getLanguage());
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
        return clientsOptional.map(tClients -> entityToContactSummaryConverter.apply(tClients, lawfirmToken.getLanguage())).orElse(null);

    }

    @Override
    public ContactSummary getCientById(Long clientId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getCientById clientId {} and vcKey {} user id {}", clientId, lawfirmToken.getVcKey(), lawfirmToken.getUserId());


        Optional<TClients> clientsOptional = clientRepository.findById_clientAndUserIdOrVcKey(clientId, lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        if (clientsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }
        return entityToContactSummaryConverter.apply(clientsOptional.get(), lawfirmToken.getLanguage());

    }

    @Override
    public ContactSummary updateContact(ContactSummary contactSummary) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering updateContact vcKey {} user id {} , contact {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId(), contactSummary);

        contactValidator.validate(contactSummary);

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

        // Update client in Transparency
        if (contactSummary.getEmail() != null && !contactSummary.getEmail().isEmpty()) {
            log.debug("update User (email : {}) in Transparancy ", contactSummary.getEmail());
            clientProducer.updateClient(contactSummary, lawfirmToken);
        } else {
            log.warn("Client doesn't have an email.");
        }

        return contactSummary;
    }

    @Override
    public ContactSummary createContact(ContactSummary contactSummary) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering createContact vcKey {} user id {} , contact {}", lawfirmToken.getVcKey(), lawfirmToken.getUserId(), contactSummary);

        contactValidator.validate(contactSummary);

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
        return entityToContactSummaryConverter.apply(clients, lawfirmToken.getLanguage());
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

        return entityToContactSummaryConverter.convertToList(lawfirmClientOptional, lawfirmToken.getLanguage());
    }

    @Override
    public Long deleteClient(Long clientId) {
        Optional<TClients> clientsOptional = clientRepository.findById(clientId);

        if (clientsOptional.isEmpty()) {
            log.warn("client is not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client is not found");
        }

        // check if it's used
        Long nbClient = dossierRepository.countByClient_cabAndOrClient_adv(List.of(clientId));

        if (nbClient > 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Client is linked to a dossier");
        }

        clientRepository.delete(clientsOptional.get());
        return clientId;
    }

    @Override
    public List<ContactSummary> getContactByDossierId(Long dossierId) {
        log.debug("Entering getContactByDossierId {}", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LawfirmUsers> optionalLawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        if (optionalLawfirmUsers.isPresent()) {
            List<TClients> clientsList = clientRepository.findByIdDossAndDossierContactType(dossierId, optionalLawfirmUsers.get().getId(), EnumDossierContactType.CLIENT);

            return entityToContactSummaryConverter.convertToList(clientsList, lawfirmToken.getLanguage());
        }
        return null;
    }

    @Override
    public List<ContactSummary> findTClientsByIdDoss(Long dossierId, String dossierType) {
        log.debug("Entering findTClientsByIdDoss {}", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (dossierId != null) {
            List<TClients> clientsList;

            if (dossierType.equals(EnumDossierType.DC.name())) {
                clientsList = clientRepository.findTClientsByIdDoss(dossierId, EnumDossierContactType.CLIENT);
                return entityToContactSummaryConverter.convertToList(clientsList, lawfirmToken.getLanguage());
            }

        }
        return null;
    }
}
