package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.LawfirmUserDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.springframework.stereotype.Component;

@Component
public class EntityToLawfirmUserDTOConverter implements SuperConverter<LawfirmUsers, LawfirmUserDTO> {

    @Override
    public LawfirmUserDTO apply(LawfirmUsers lawfirmUsers) {

        LawfirmUserDTO lawfirmUserDTO = new LawfirmUserDTO();

        lawfirmUserDTO.setId(lawfirmUsers.getUser().getId());
        lawfirmUserDTO.setPublic(lawfirmUsers.isPublic());
        lawfirmUserDTO.setActive(lawfirmUsers.isActive());
        lawfirmUserDTO.setSelected(lawfirmUsers.isSelected());
        lawfirmUserDTO.setPrestataire(lawfirmUsers.isPrestataire());
        lawfirmUserDTO.setIdRole(lawfirmUsers.getIdRole());
        lawfirmUserDTO.setLawyerAlias(lawfirmUsers.getUser().getAliasPublic());
        lawfirmUserDTO.setLawyerFullname(lawfirmUsers.getUser().getFullname());
        lawfirmUserDTO.setCouthoraire(lawfirmUsers.getCouthoraire());
        lawfirmUserDTO.setUseSelfCouthoraire(lawfirmUsers.isUseSelfCouthoraire());
        lawfirmUserDTO.setValidFrom(lawfirmUsers.getValidFrom());
        lawfirmUserDTO.setValidTo(lawfirmUsers.getValidTo());
        lawfirmUserDTO.setEmail(lawfirmUsers.getUser().getEmail());

        return lawfirmUserDTO;
    }

}
