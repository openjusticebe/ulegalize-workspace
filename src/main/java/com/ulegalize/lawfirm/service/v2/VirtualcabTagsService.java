package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.TVirtualCabTags;

import java.util.List;
import java.util.Optional;

public interface VirtualcabTagsService {

    List<ItemLongDto> getTagsByVckey(String searchCriteria);

    Optional<TVirtualCabTags> findTVirtualCabTagsByLabel(String label);
}
