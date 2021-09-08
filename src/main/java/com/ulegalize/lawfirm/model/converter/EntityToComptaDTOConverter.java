package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.TFrais;
import com.ulegalize.lawfirm.utils.SuperConverter;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.DossiersUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityToComptaDTOConverter implements SuperConverter<TFrais, ComptaDTO> {

    @Override
    public ComptaDTO apply(TFrais entityTFrais) {
        ComptaDTO compta = new ComptaDTO();
        compta.setId(entityTFrais.getIdFrais());
        compta.setVcKey(entityTFrais.getVcKey());
        compta.setGridId(entityTFrais.getGridId());
        compta.setIdCompte(entityTFrais.getIdCompte());
        compta.setIdDoss(entityTFrais.getIdDoss());
        if (entityTFrais.getTDossiers() != null) {
            compta.setIdDossierItem(new ItemLongDto(entityTFrais.getIdDoss(),
                    DossiersUtils.getDossierLabelItem(entityTFrais.getTDossiers().getYear_doss(),
                            entityTFrais.getTDossiers().getNum_doss())));
        }
        compta.setIdFacture(entityTFrais.getIdFacture());
        if (entityTFrais.getTFactures() != null) {
            compta.setIdFactureItem(new ItemLongDto(entityTFrais.getIdFacture(),
                    String.valueOf(entityTFrais.getTFactures().getFactureRef())));
        }
        compta.setIdUser(entityTFrais.getIdClient());
        if (entityTFrais.getIdClient() != null && entityTFrais.getTClients() != null) {
            String fullname = ClientsUtils.getFullname(entityTFrais.getTClients().getF_nom(), entityTFrais.getTClients().getF_prenom(), entityTFrais.getTClients().getF_company());
            compta.setIdUserItem(new ItemLongDto(entityTFrais.getIdClient(), fullname));
            compta.setTiersFullname(fullname);
        }
        compta.setMontant(entityTFrais.getMontant());
        compta.setMemo(entityTFrais.getMemo());
        compta.setIdPost(entityTFrais.getIdPoste());
        compta.setIdTransaction(entityTFrais.getIdTransaction().getId());
        compta.setIdType(entityTFrais.getIdType().getIdType());
        compta.setIdTypeItem(new ItemDto(entityTFrais.getIdType().getIdType(), entityTFrais.getIdType().getDescriptionFr()));
        compta.setMontantHt(entityTFrais.getMontantht());
        compta.setRatio(entityTFrais.getRatio());
        compta.setDateValue(entityTFrais.getDateValue());
        if (entityTFrais.getRefPoste() != null) {
            compta.setPoste(new ItemDto(entityTFrais.getRefPoste().getIdPoste(), entityTFrais.getRefPoste().getRefPoste()));
        }
        return compta;
    }
}
