package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.v2.ClientV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v2/clients")
@Slf4j
public class ClientV2Controller {

    @Autowired
    ClientV2Service clientService;


    @RequestMapping(method = RequestMethod.GET, path = "")
    public ResponseEntity<List<ContactSummary>> getAllContacts(@RequestParam(required = false) String searchCriteria,
                                                               @RequestParam(required = false) Long dossierId,
                                                               @RequestParam(required = false, defaultValue = "false") Boolean withEmail) throws LawfirmBusinessException {
        log.debug("Entering getAllContacts()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllContacts(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity.ok().body(clientService.getAllCientByVcKey(searchCriteria, dossierId, withEmail));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/ids")
    public ResponseEntity<List<ContactSummary>> getAllContactsByIds(@RequestParam(value = "clientIds") List<Long> clientIds) throws LawfirmBusinessException {
        log.debug("Entering getAllContactsByIds({})", clientIds);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllContactsByIds(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity.ok().body(clientService.getAllContactsByIds(clientIds));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/email/{email}")
    public ResponseEntity<ContactSummary> getAllCientByVcKeyAndEmail(@PathVariable String email) throws LawfirmBusinessException {
        log.debug("Entering getAllCientByVcKeyAndEmail({})", email);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllCientByVcKeyAndEmail(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(clientService.getAllCientByVcKeyAndEmail(email));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/list")
    public ResponseEntity<List<ContactSummary>> getAllContactsPagination(@RequestParam int offset,
                                                                         @RequestParam int limit,
                                                                         @RequestParam(required = false) String searchCriteria) throws LawfirmBusinessException {
        log.debug("Entering getAllContactsPagination()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getAllContactsPagination(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity.ok()
                .body(clientService.getAllClientByVcKeyWithPagination(offset, limit, searchCriteria));
    }

    @GetMapping(path = "/count")
    public ResponseEntity<Long> countContactsList(@RequestParam String vcKey) {
        log.debug("Entering countContactsList()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("countContactsList(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(clientService.contAllCientByVcKey());
    }

    //TODO replace the explicit rest pathparam by a security/jwt param
    @GetMapping(value = "/{clientId}")
    public ResponseEntity<ContactSummary> getContactById(@PathVariable Long clientId) throws LawfirmBusinessException {
        log.debug("Entering getContactById({})", clientId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getContactById(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.SECONDS))
                .body(clientService.getCientById(clientId));
    }

    @GetMapping(value = "/dossier/{dossierId}")
    public ResponseEntity<List<ContactSummary>> getContactByDossierId(@PathVariable Long dossierId) throws LawfirmBusinessException {
        log.debug("Entering getContactByDossierId({})", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getContactByDossierId(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.SECONDS))
                .body(clientService.getContactByDossierId(dossierId));
    }

    @PutMapping
    public ContactSummary updateContactById(@RequestBody ContactSummary contactSummary) throws LawfirmBusinessException {
        log.debug("Entering updateContactById({})", contactSummary);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("updateContactById(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return clientService.updateContact(contactSummary);
    }

    @PostMapping
    public ContactSummary createContact(@RequestBody ContactSummary contactSummary) throws LawfirmBusinessException {
        log.debug("Entering createContact({})", contactSummary);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("createContact(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return clientService.createContact(contactSummary);
    }

    @GetMapping("/clients/count")
    public Long countClientsByName(@RequestParam(required = false) String searchCriteria) throws LawfirmBusinessException {

        log.debug("Entering countClientsByName( search criteria {})", searchCriteria);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("countClientsByName(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return clientService.countClientsByName(lawfirmToken.getVcKey(), lawfirmToken.getUserId(), searchCriteria);

    }

    @DeleteMapping("/{clientId}")
    public Long deleteClient(@PathVariable Long clientId) {

        log.debug("Entering deleteClient( clientId {})", clientId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("deleteClient(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return clientService.deleteClient(clientId);
    }

    @GetMapping(value = "/dossier/all")
    public ResponseEntity<List<ContactSummary>> getClientsByDossierId(@RequestParam Long dossierId, @RequestParam String dossierType) {
        log.debug("Entering getClientsByDossierId({})", dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getClientsByDossierId(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.SECONDS))
                .body(clientService.findTClientsByIdDoss(dossierId, dossierType));
    }
}
