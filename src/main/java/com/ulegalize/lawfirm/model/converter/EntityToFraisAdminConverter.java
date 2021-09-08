package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.TDebour;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.springframework.stereotype.Component;

@Component
public class EntityToFraisAdminConverter implements SuperConverter<TDebour, FraisAdminDTO> {

    @Override
    public FraisAdminDTO apply(TDebour tDebour) {

        FraisAdminDTO fraisAdminDTO = new FraisAdminDTO();
        fraisAdminDTO.setId(tDebour.getIdDebour());
        fraisAdminDTO.setType(tDebour.getIdDebourType());
        fraisAdminDTO.setIdDebourType(tDebour.getIdDebourType());
        if (tDebour.getTDebourType() != null) {
            fraisAdminDTO.setIdDebourTypeItem(new ItemLongDto(tDebour.getTDebourType().getIdDebourType(), tDebour.getTDebourType().getDescription()));
        }
        fraisAdminDTO.setIdDoss(tDebour.getIdDoss());
        fraisAdminDTO.setPricePerUnit(tDebour.getPricePerUnit());
        fraisAdminDTO.setUnit(tDebour.getUnit());
        fraisAdminDTO.setIdMesureType(tDebour.getTMesureType().getIdMesureType());
        fraisAdminDTO.setIdMesureTypeItem(new ItemDto(tDebour.getTMesureType().getIdMesureType(), tDebour.getTMesureType().getDescription()));
        fraisAdminDTO.setMesureDescription(tDebour.getTMesureType().getDescription());
        fraisAdminDTO.setDateAction(tDebour.getDateAction());
        fraisAdminDTO.setComment(tDebour.getComment());

        return fraisAdminDTO;
    }

}
