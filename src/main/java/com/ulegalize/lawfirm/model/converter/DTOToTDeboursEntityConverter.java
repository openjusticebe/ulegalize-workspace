package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.lawfirm.model.entity.TDebour;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class DTOToTDeboursEntityConverter implements SuperTriConverter<FraisAdminDTO, TDebour, TDebour> {

    @Override
    public TDebour apply(FraisAdminDTO fraisAdminDTO, TDebour entity) {

        entity.setIdDebour(fraisAdminDTO.getId());
        entity.setIdDoss(fraisAdminDTO.getIdDoss());
        entity.setComment(fraisAdminDTO.getComment());
        entity.setIdDebourType(fraisAdminDTO.getIdDebourType());
        entity.setIdMesureType(fraisAdminDTO.getIdMesureType());
        entity.setDateAction(fraisAdminDTO.getDateAction());
        entity.setPricePerUnit(fraisAdminDTO.getPricePerUnit());
        entity.setUnit(fraisAdminDTO.getUnit());

        return entity;
    }

}
