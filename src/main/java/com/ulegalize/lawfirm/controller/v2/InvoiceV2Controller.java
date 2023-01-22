package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.*;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.InvoiceService;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/v2/invoices")
@Slf4j
public class InvoiceV2Controller {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long invoiceId,
                                                     @RequestParam(required = false) String vcKey) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getInvoiceById(invoiceId: {} for vckey {}", invoiceId, lawfirmToken.getVcKey());

        return ResponseEntity.ok()
                .body(invoiceService.getInvoiceById(invoiceId, lawfirmToken.getVcKey()));
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Page<InvoiceDTO>> getInvoices(@RequestParam int offset, @RequestParam int limit,
                                                        @RequestParam(required = false) Long dossierId,
                                                        @RequestParam(required = false) Integer searchEcheance,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime searchDate,
                                                        @RequestParam(required = false) String searchNomenclature,
                                                        @RequestParam(required = false) String searchClient,
                                                        @RequestParam(required = false) String vcKey,
                                                        @RequestParam(required = false) Boolean sortFacture

    ) {
        log.debug("getInvoices(offset: {} , limit {} and dossierId {}", offset, limit, dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        Page<InvoiceDTO> allInvoices;

        if (dossierId != null) {
            allInvoices = invoiceService.getAllInvoicesByDossierId(limit, offset, dossierId, lawfirmToken.getVcKey());
        } else {
            ZonedDateTime zonedDateTimeSearch = searchDate != null ? CalendarEventsUtil.convertToZoneDateTimeViaInstant(searchDate) : null;
            allInvoices = invoiceService.getAllInvoices(limit, offset, lawfirmToken.getVcKey(), searchEcheance, zonedDateTimeSearch, searchNomenclature, searchClient, sortFacture);
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        return responseBuilder
                .body(allInvoices);

    }

    @GetMapping(value = "/dossier/{dossierId}/total")
    public ResponseEntity<InvoiceDTO> totalInvoiceByDossierId(@PathVariable Long dossierId) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("totalInvoice for vckey {} and dossierId {}", lawfirmToken.getVcKey(), dossierId);

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());
        InvoiceDTO invoiceDTO = invoiceService.totalInvoiceByDossierId(dossierId);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        return responseBuilder
                .body(invoiceDTO);

    }

    @GetMapping
    public List<ItemLongDto> getInvoicesBySearchCriteria(@RequestParam(required = false) String searchCriteria, @RequestParam(required = false) Long dossierId) {

        log.debug("Entering getInvoicesBySearchCriteria searchCriteria {} and id dossier {}", searchCriteria, dossierId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getContacts(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return invoiceService.getInvoicesBySearchCriteria(lawfirmToken.getVcKey(), searchCriteria, dossierId);
    }

    @DeleteMapping("/{invoiceId}")
    public Long deleteInvoice(@PathVariable Long invoiceId) {

        log.debug("Entering deleteInvoice( invoiceId {})", invoiceId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("deleteInvoice(vckey: {}, user id: {})", lawfirmToken.getVcKey(), lawfirmToken.getUserId());

        return invoiceService.deleteInvoiceById(invoiceId);

    }

    @GetMapping("/default")
    public InvoiceDTO getDefaultInvoice() {
        log.debug("getDefaultInvoice()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.getDefaultInvoice(lawfirmToken.getUserId(), lawfirmToken.getVcKey(), lawfirmToken.getLanguage());

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).FACTURE_ECRITURE.name())")
    public Long createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        log.debug("createInvoice({})", invoiceDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.createInvoice(invoiceDTO, lawfirmToken.getVcKey());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).FACTURE_ECRITURE.name())")
    public InvoiceDTO updateInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        log.debug("updateInvoice({})", invoiceDTO);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.updateInvoice(invoiceDTO, lawfirmToken.getVcKey());

    }

    @PutMapping(value = "/validate/{invoiceId}")
    @PreAuthorize("hasAnyAuthority(T(com.ulegalize.security.EnumRights).ADMINISTRATEUR.name(), T(com.ulegalize.security.EnumRights).FACTURE_VALIDATION.name())")
    public InvoiceDTO validateInvoice(@PathVariable Long invoiceId) {
        log.debug("Entering validateInvoice controller with invoiceId = ({})", invoiceId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.validateInvoice(invoiceId, lawfirmToken.getVcKey());

    }

    @GetMapping("/{invoiceId}/prestations/{dossierId}")
    public List<PrestationSummary> getPrestationByDossierId(@PathVariable Long invoiceId,
                                                            @PathVariable Long dossierId,
                                                            @RequestParam(required = false) Boolean filterInvoicePrestation) {
        log.debug("getPrestationByDossierId(invoiceId {}, dossier id {})", invoiceId, dossierId);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.getPrestationByDossierId(invoiceId, dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey(), filterInvoicePrestation);
    }

    @GetMapping("/{invoiceId}/fraisAdmin/{dossierId}")
    public List<FraisAdminDTO> getFraisAdminByDossierId(@PathVariable Long invoiceId, @PathVariable Long dossierId, Boolean filterInvoiceFraisAdmin) {
        log.debug("getFraisAdminByDossierId(invoiceId {}, dossier id {})", invoiceId, dossierId);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.getFraisAdminByDossierId(invoiceId, dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey(), filterInvoiceFraisAdmin);
    }

    @GetMapping("/{invoiceId}/debours/{dossierId}")
    public List<ComptaDTO> getDeboursByDossierId(@PathVariable Long invoiceId, @PathVariable Long dossierId, Boolean filterInvoiceDebours) {
        log.debug("getDeboursByDossierId(invoiceId {}, dossier id {})", invoiceId, dossierId);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.getDeboursByDossierId(invoiceId, dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey(), filterInvoiceDebours);
    }

    @GetMapping("/{invoiceId}/fraisCollaborat/{dossierId}")
    public List<ComptaDTO> getFraisCollabByDossierId(@PathVariable Long invoiceId, @PathVariable Long dossierId, Boolean filterInvoiceFraisCollab) {
        log.debug("getFraisCollabByDossierId(invoiceId {}, dossier id {})", invoiceId, dossierId);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.getFraisCollabByDossierId(invoiceId, dossierId, lawfirmToken.getUserId(), lawfirmToken.getVcKey(), filterInvoiceFraisCollab);
    }

    @GetMapping("/vat/{vat}")
    public Long countInvoiceDetailsByVat(@PathVariable BigDecimal vat) {
        log.debug("countInvoiceDetailsByVat( vat id {})", vat);

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.countInvoiceDetailsByVat(vat);
    }

    @GetMapping("/count/active")
    public Long countAllActiveInvoiceByVcKey() {
        log.debug("countInvoiceDetailsByVat()");

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Lawfirm connected vc{} user {}", lawfirmToken.getVcKey(), lawfirmToken.getUsername());

        return invoiceService.countAllActiveByVcKey(lawfirmToken.getVcKey());
    }


}
