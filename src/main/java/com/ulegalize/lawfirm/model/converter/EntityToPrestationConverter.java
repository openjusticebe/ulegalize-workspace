package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ItemBigDecimalDto;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import com.ulegalize.lawfirm.utils.SuperConverter;
import com.ulegalize.utils.PrestationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityToPrestationConverter implements SuperConverter<TTimesheet, PrestationSummary> {

    @Override
    public PrestationSummary apply(TTimesheet timesheet) {

        PrestationSummary prestationSummary = new PrestationSummary();
        prestationSummary.setId(timesheet.getIdTs());
        prestationSummary.setDossierId(timesheet.getIdDoss());
        prestationSummary.setDossier(timesheet.getTDossiers().getYear_doss() + "/" + StringUtils.leftPad(timesheet.getTDossiers().getNum_doss().toString(), 4, "0"));
        prestationSummary.setNumDossier(timesheet.getTDossiers().getNum_doss());
        prestationSummary.setYearDossier(timesheet.getTDossiers().getYear_doss());
        prestationSummary.setIdGest(timesheet.getIdGest());
        if (timesheet.getTUsers() != null) {
            prestationSummary.setIdGestItem(new ItemLongDto(timesheet.getIdGest(), timesheet.getTUsers().getEmail()));
        }
        prestationSummary.setTsType(timesheet.getTsType());
        if (timesheet.getTTimesheetType() != null) {
            prestationSummary.setTsTypeItem(new ItemDto(timesheet.getTTimesheetType().getIdTs(), timesheet.getTTimesheetType().getDescription()));
            prestationSummary.setTsTypeDescription(timesheet.getTTimesheetType().getDescription());
        }
        prestationSummary.setCouthoraire(timesheet.getCouthoraire());
        prestationSummary.setDateAction(timesheet.getDateAction());
        prestationSummary.setDh(timesheet.getDh());
        prestationSummary.setDm(timesheet.getDm());
        prestationSummary.setComment(timesheet.getComment());
        prestationSummary.setVat(timesheet.getVat());
        if (timesheet.getVat() != null) {
            prestationSummary.setVatItem(new ItemBigDecimalDto(timesheet.getVat(), timesheet.getVat().toString()));
        }
        prestationSummary.setTotalTTC(PrestationUtils.calculateVAT(timesheet.getForfait(), timesheet.getDm(), timesheet.getDh(), timesheet.getCouthoraire(), timesheet.getForfaitHt(), timesheet.getVat()));
        prestationSummary.setTotalHt(PrestationUtils.calculateHVAT(timesheet.getForfait(), timesheet.getDm(), timesheet.getDh(), timesheet.getCouthoraire(), timesheet.getForfaitHt()));
        prestationSummary.setForfait(timesheet.getForfait());
        prestationSummary.setForfaitHt(timesheet.getForfaitHt());

        return prestationSummary;
    }

}
