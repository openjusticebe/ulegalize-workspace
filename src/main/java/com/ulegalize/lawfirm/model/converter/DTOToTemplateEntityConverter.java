package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ModelDTO;
import com.ulegalize.enumeration.EnumTypeTemplate;
import com.ulegalize.lawfirm.model.entity.TTemplates;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DTOToTemplateEntityConverter implements SuperTriConverter<ModelDTO, TTemplates, TTemplates> {

    @Override
    public TTemplates apply(ModelDTO modelDTO, TTemplates entity) {
        entity.setId(modelDTO.getId());
        entity.setName(modelDTO.getName());
        entity.setContext(modelDTO.getContext());
        entity.setFormat(modelDTO.getFormat());
        entity.setDescription(modelDTO.getDescription());
        entity.setType(modelDTO.getType() != null ? modelDTO.getType() : EnumTypeTemplate.U);
        String template = StringEscapeUtils.escapeHtml4(modelDTO.getTemplate());
        entity.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        return entity;
    }

}
