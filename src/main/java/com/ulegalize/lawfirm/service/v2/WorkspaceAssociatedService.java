package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.lawfirm.model.dto.AssociatedWorkspaceDTO;
import org.springframework.data.domain.Page;

public interface WorkspaceAssociatedService {

    Boolean validateAssociation(Long id, String vcKeyRecipient, String hashkey, Boolean status);

    Boolean createAssociation(AssociatedWorkspaceDTO associatedWorkspaceDTO);

    Page<AssociatedWorkspaceDTO> getAllAssociatedWorkspace(int limit, int offset, String vcKey, Long userId, Boolean searchCriteriaType);

    Boolean updateAssociation(String vcKeySender, Long userId, String vcKeyRecipient, Boolean typeAssociation, Boolean status);
}
