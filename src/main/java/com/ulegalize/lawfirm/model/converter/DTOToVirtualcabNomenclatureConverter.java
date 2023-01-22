package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class DTOToVirtualcabNomenclatureConverter implements SuperTriConverter<VirtualcabNomenclatureDTO, TVirtualcabNomenclature, TVirtualcabNomenclature> {

    @Override
    public TVirtualcabNomenclature apply(VirtualcabNomenclatureDTO virtualcabNomenclatureDTO, TVirtualcabNomenclature virtualcabNomenclature) {
        virtualcabNomenclature.setName(virtualcabNomenclatureDTO.getName());
        virtualcabNomenclature.setDrivePath(virtualcabNomenclatureDTO.getDrivePath());
        return virtualcabNomenclature;
    }
}
