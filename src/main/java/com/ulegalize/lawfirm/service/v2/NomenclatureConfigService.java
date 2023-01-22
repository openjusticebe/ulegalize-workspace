package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.NomenclatureConfigDTO;

import java.util.List;

public interface NomenclatureConfigService {
    List<NomenclatureConfigDTO> getNomenclatureConfigInfoByVcNomenclature(Long idVcNomenclature, String vckey);

    Long addNomenclatureConfigByVcNomenclature(NomenclatureConfigDTO nomenclatureConfigDTO);

    void removeNomenclatureConfig(String nomenclatureConfigLabel, Long vcNomenclatureId);

    NomenclatureConfigDTO getNomenclatureConfig(Long vcNomenclatureId, String nomenclatureConfig);
}
