package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class DTOToUserEntityConverter implements SuperTriConverter<LawyerDTO, TUsers, TUsers> {

    @Override
    public TUsers apply(LawyerDTO lawyerDTO, TUsers entity) {
        entity.setFullname(lawyerDTO.getFullName());
        entity.setInitiales(lawyerDTO.getInitials());
        entity.setColor(lawyerDTO.getColor());
        String biography = StringEscapeUtils.escapeHtml4(lawyerDTO.getBiography());
        entity.setBiography(biography);
        String specialities = StringEscapeUtils.escapeHtml4(lawyerDTO.getSpecialities());
        entity.setSpecialities(specialities);
        entity.setAliasPublic(lawyerDTO.getAlias());
        entity.setIdUser(lawyerDTO.getIdUser());
        entity.setNotification(lawyerDTO.isNotification());

        return entity;
    }
}
