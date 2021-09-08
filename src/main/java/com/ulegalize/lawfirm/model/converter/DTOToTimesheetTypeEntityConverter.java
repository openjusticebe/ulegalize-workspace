package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.PrestationTypeDTO;
import com.ulegalize.lawfirm.model.entity.TTimesheetType;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class DTOToTimesheetTypeEntityConverter implements SuperTriConverter<PrestationTypeDTO, TTimesheetType, TTimesheetType> {

    @Override
    public TTimesheetType apply(PrestationTypeDTO prestationTypeDTO, TTimesheetType entity) {

        entity.setIdTs(prestationTypeDTO.getIdTs());
        entity.setDescription(prestationTypeDTO.getDescription());
        entity.setArchived(prestationTypeDTO.isArchived());

        return entity;
    }

}
