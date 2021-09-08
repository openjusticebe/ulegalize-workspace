package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DTOToTimesheetEntityConverter implements SuperTriConverter<PrestationSummary, TTimesheet, TTimesheet> {

    @Override
    public TTimesheet apply(PrestationSummary prestationSummary, TTimesheet entity) {

        entity.setIdTs(prestationSummary.getId());
        entity.setIdDoss(prestationSummary.getDossierId());
        entity.setIdGest(prestationSummary.getIdGest());
        entity.setTsType(prestationSummary.getTsType());
        entity.setCouthoraire(prestationSummary.getCouthoraire());
        entity.setDateAction(prestationSummary.getDateAction());
        entity.setDh(prestationSummary.getDh() != null ? prestationSummary.getDh() : BigDecimal.ZERO);
        entity.setDm(prestationSummary.getDm() != null ? prestationSummary.getDm() : BigDecimal.ZERO);
        entity.setComment(prestationSummary.getComment());
        entity.setVat(prestationSummary.getVat());
        entity.setForfait(prestationSummary.isForfait());
        entity.setForfaitHt(prestationSummary.getForfaitHt());

        return entity;
    }

}
