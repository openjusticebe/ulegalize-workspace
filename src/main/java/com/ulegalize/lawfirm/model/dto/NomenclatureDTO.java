package com.ulegalize.lawfirm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NomenclatureDTO {
    Long dossierIdToRename;
    String newDossierName;
    Long idNomenclature;
    String vcKey;
}
