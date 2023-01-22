package com.ulegalize.lawfirm.model.dto;

import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedWorkspaceDTO {

    private String vcKeyRecipient;
    private String message;
    // 0 = request ; 1 = reception
    private boolean typeAssociation;
    private ZonedDateTime creationDate;
    private EnumStatusAssociation status;
}