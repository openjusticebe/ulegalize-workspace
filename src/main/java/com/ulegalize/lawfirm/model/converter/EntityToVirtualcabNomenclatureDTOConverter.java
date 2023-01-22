package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.springframework.stereotype.Component;

@Component
public class EntityToVirtualcabNomenclatureDTOConverter implements SuperConverter<TVirtualcabNomenclature, VirtualcabNomenclatureDTO> {

    @Override
    public VirtualcabNomenclatureDTO apply(TVirtualcabNomenclature virtualcabNomenclature) {
        VirtualcabNomenclatureDTO virtualcabNomenclatureDTO = new VirtualcabNomenclatureDTO();
        virtualcabNomenclatureDTO.setName(virtualcabNomenclature.getName());
        virtualcabNomenclatureDTO.setDrivePath(virtualcabNomenclature.getDrivePath());
        virtualcabNomenclatureDTO.setId(virtualcabNomenclature.getId());

        return virtualcabNomenclatureDTO;
    }
}
