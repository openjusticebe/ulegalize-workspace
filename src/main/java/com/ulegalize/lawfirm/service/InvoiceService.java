package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public interface InvoiceService {
    public Page<InvoiceDTO> getAllInvoices(int limit, int offset, String vcKey, Integer searchEcheance, ZonedDateTime searchDate, String searchYearDossier, Long searchNumberDossier, String searchClient);

    Page<InvoiceDTO> getAllInvoicesByDossierId(int limit, int offset, Long dossierId, String vcKey);

    List<ItemLongDto> getInvoicesBySearchCriteria(String vcKey, String searchCriteria);

    InvoiceDTO getDefaultInvoice(Long userId, String vcKey);

    InvoiceDTO getInvoiceById(Long invoiceId, String vcKey) throws ResponseStatusException;

    Long createInvoice(InvoiceDTO invoiceDTO, String vcKey);

    InvoiceDTO updateInvoice(InvoiceDTO invoiceDTO, String vcKey);

    List<PrestationSummary> getPrestationByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation);

    Long countInvoiceDetailsByVat(BigDecimal vat);

    InvoiceDTO validateInvoice(Long invoiceId, String vcKey);

    ByteArrayResource downloadInvoice(Long id);

    Long deleteInvoiceById(Long invoiceId);

    InvoiceDTO totalInvoiceByDossierId(Long dossierId);

    List<FraisAdminDTO> getFraisAdminByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation);

    List<ComptaDTO> getDeboursByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation);

    List<ComptaDTO> getFraisCollabByDossierId(Long invoiceId, Long dossierId, Long userId, String vcKey, Boolean filterInvoicePrestation);
}
