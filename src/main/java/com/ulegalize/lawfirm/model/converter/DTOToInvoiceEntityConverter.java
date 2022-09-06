package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.InvoiceDTO;
import com.ulegalize.enumeration.EnumFactureType;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DTOToInvoiceEntityConverter implements SuperTriConverter<InvoiceDTO, TFactures, TFactures> {

    @Override
    public TFactures apply(InvoiceDTO dto, TFactures tFactures) {

        tFactures.setIdFacture(dto.getId());
        tFactures.setVcKey(dto.getVcKey());
        tFactures.setIdTiers(dto.getClientId());
        tFactures.setIdDoss(dto.getDossierId());
        tFactures.setIdFactureType(EnumFactureType.fromId(dto.getTypeId()));
        tFactures.setIdPoste(dto.getPosteId());
        tFactures.setDateValue(dto.getDateValue());
        tFactures.setDateEcheance(dto.getDateEcheance());
        tFactures.setIdEcheance(dto.getEcheanceId());
        tFactures.setYearFacture(dto.getYearFacture());
        tFactures.setNumFacture(dto.getNumFacture());
        tFactures.setFactureRef(dto.getReference());
        tFactures.setValid(dto.getValid() != null ? dto.getValid() : false);
        tFactures.setMontant(dto.getMontant() != null ? dto.getMontant() : BigDecimal.ZERO);
        tFactures.setRef("test ref");
        tFactures.setMemo("test memo");
        tFactures.setNumFactTemp(dto.getNumFacture());
        tFactures.setSecondTax(BigDecimal.ZERO);

        return tFactures;

    }
}
