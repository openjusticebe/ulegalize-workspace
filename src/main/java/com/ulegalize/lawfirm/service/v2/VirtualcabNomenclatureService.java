package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.dto.VirtualcabNomenclatureDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VirtualcabNomenclatureService {
    Long saveVirtualcabNomenclature(VirtualcabNomenclatureDTO nomenclatureDTO);

    List<ItemLongDto> getAllVirtualcabNomenclature();

    VirtualcabNomenclatureDTO getVirtualcabNomenclature(String name);

    VirtualcabNomenclatureDTO getVirtualcabNomenclatureById(Long id);

    VirtualcabNomenclatureDTO updateVirtualcabNomenclature(VirtualcabNomenclatureDTO virtualcabNomenclatureDTO);

    Long deleteVirtualcabNomenclature(Long idVcNomenclature);

    Page<VirtualcabNomenclatureDTO> getAllVirtualcabNomenclatureWithPagination(int limit, int offset);

    List<ItemLongDto> getAllVirtualcabNomenclatureByVckey(String vcKey);
}
