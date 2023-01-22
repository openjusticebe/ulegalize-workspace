package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.lawfirm.model.dto.AssociatedWorkspaceDTO;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class EntityToAssociatedWorkspaceDTOConverter implements SuperTriConverter<TWorkspaceAssociated, String, AssociatedWorkspaceDTO> {

    @Override
    public AssociatedWorkspaceDTO apply(TWorkspaceAssociated tWorkspaceAssociated, String vckey) {
        AssociatedWorkspaceDTO associatedWorkspaceDTO = new AssociatedWorkspaceDTO();

        // If vckey = idSender then set vckeyRecipient with idRecipient else if vckey = idRecipient ther set vckeyRecipient with idSender
        associatedWorkspaceDTO.setVcKeyRecipient(vckey.equalsIgnoreCase(tWorkspaceAssociated.getLawfirmSender().getVckey()) ? tWorkspaceAssociated.getLawfirmRecipient().getVckey() : tWorkspaceAssociated.getLawfirmSender().getVckey());
        associatedWorkspaceDTO.setMessage(tWorkspaceAssociated.getMessage());
        associatedWorkspaceDTO.setCreationDate(tWorkspaceAssociated.getCreationDate());
        associatedWorkspaceDTO.setStatus(tWorkspaceAssociated.getStatus());
        // If vckey = idSender then type = false ==> it's a request else it's a reception (type = true)
        associatedWorkspaceDTO.setTypeAssociation(!vckey.equalsIgnoreCase(tWorkspaceAssociated.getLawfirmSender().getVckey()));

        return associatedWorkspaceDTO;
    }
}
