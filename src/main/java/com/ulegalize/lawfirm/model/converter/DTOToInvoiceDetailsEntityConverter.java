package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.InvoiceDetailsDTO;
import com.ulegalize.lawfirm.model.entity.TFactureDetails;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class DTOToInvoiceDetailsEntityConverter implements SuperTriConverter<InvoiceDetailsDTO, TFactureDetails, TFactureDetails> {

    @Override
    public TFactureDetails apply(InvoiceDetailsDTO dto, TFactureDetails tFactureDetails) {

        if (dto.getId() != null) {
            tFactureDetails.setId(dto.getId());
        }
        tFactureDetails.setDescription(dto.getDescription());
        tFactureDetails.setTva(dto.getTva());
        tFactureDetails.setHtva(dto.getMontantHt());
        tFactureDetails.setTtc(dto.getMontant());

        return tFactureDetails;
    }
}
