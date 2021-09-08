package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.ModelDTO;
import net.minidev.json.JSONObject;

import java.util.List;

public interface TemplateV2Service {
    List<ModelDTO> getModelsList();

    Long updateModels(ModelDTO modelDTO);

    Long createModels(ModelDTO modelDTO);

    Long deleteModels(Long modelsId);

    JSONObject getTemplatDataByDossier(Long dossierId);

    JSONObject getTemplatData();
}
