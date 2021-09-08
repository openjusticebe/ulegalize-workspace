package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.dto.ModelDTO;
import com.ulegalize.lawfirm.model.entity.TTemplates;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityToTemplateConverter implements SuperConverter<TTemplates, ModelDTO> {

    @Override
    public ModelDTO apply(TTemplates entity) {
        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setId(entity.getId());
        modelDTO.setName(entity.getName());
        modelDTO.setContext(entity.getContext());
        modelDTO.setContextItem(new ItemStringDto(entity.getContext().name(), entity.getContext().name()));
        modelDTO.setFormat(entity.getFormat());
        String format = "";
        if (entity.getFormat().equalsIgnoreCase("D")) {
            format = "Document";
        } else {
            format = "Couriel";

        }
        modelDTO.setFormatItem(new ItemStringDto(entity.getFormat(), format));
        modelDTO.setDescription(entity.getDescription());
        modelDTO.setType(entity.getType());
        String template = new String(entity.getTemplate());
        template = StringEscapeUtils.unescapeHtml4(template);
        modelDTO.setTemplate(template);
        modelDTO.setTemplateItem(new ItemStringDto(template, template));

        return modelDTO;
    }

}
