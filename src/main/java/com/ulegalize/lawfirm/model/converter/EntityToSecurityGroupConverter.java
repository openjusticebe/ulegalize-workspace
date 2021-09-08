package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.SecurityGroupUserDTO;
import com.ulegalize.lawfirm.model.entity.TSecurityGroupUsers;
import com.ulegalize.lawfirm.utils.SuperConverter;
import org.springframework.stereotype.Component;

@Component
public class EntityToSecurityGroupConverter implements SuperConverter<TSecurityGroupUsers, SecurityGroupUserDTO> {

    @Override
    public SecurityGroupUserDTO apply(TSecurityGroupUsers entity) {
        SecurityGroupUserDTO securityGroupUserDTO = new SecurityGroupUserDTO();
        securityGroupUserDTO.setId(entity.getId());
        securityGroupUserDTO.setSecurityGroupId(entity.getTSecurityGroups().getId());
        securityGroupUserDTO.setFullName(entity.getUser().getFullname());
        securityGroupUserDTO.setPicture(entity.getUser().getAvatar());
        securityGroupUserDTO.setEmail(entity.getUser().getEmail());
        securityGroupUserDTO.setValid(entity.getUser().getValid());

        return securityGroupUserDTO;
    }
}
