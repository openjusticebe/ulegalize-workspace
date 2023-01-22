package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefTransaction;
import com.ulegalize.lawfirm.model.entity.TFrais;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.DossiersUtils;
import com.ulegalize.utils.Utils;
import org.springframework.stereotype.Component;

@Component
public class EntityToComptaDTOConverter implements SuperTriConverter<TFrais, EnumLanguage, ComptaDTO> {

    @Override
    public ComptaDTO apply(TFrais entityTFrais, EnumLanguage enumLanguage) {
        ComptaDTO compta = new ComptaDTO();
        compta.setId(entityTFrais.getIdFrais());
        compta.setVcKey(entityTFrais.getVcKey());
        compta.setGridId(entityTFrais.getGridId());
        compta.setIdCompte(entityTFrais.getIdCompte());
        if (entityTFrais.getRefCompte() != null) {
            compta.setCompte(new ItemDto(entityTFrais.getRefCompte().getIdCompte(), entityTFrais.getRefCompte().getCompteNum()));
        }
        compta.setIdDoss(entityTFrais.getIdDoss());
        if (entityTFrais.getTDossiers() != null) {
            compta.setIdDossierItem(new ItemLongDto(entityTFrais.getIdDoss(),
                    DossiersUtils.getDossierLabelItem(entityTFrais.getTDossiers().getNomenclature())));
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
        String label = Utils.getLabel(enumLanguage, entityTFrais.getIdType().name(), null);
        compta.setIdTypeItem(new ItemDto(entityTFrais.getIdType().getIdType(), label));
        compta.setMontantHt(entityTFrais.getMontantht());
        compta.setRatio(entityTFrais.getRatio());
        compta.setDateValue(entityTFrais.getDateValue());
        if (entityTFrais.getRefPoste() != null) {
            compta.setPoste(new ItemDto(entityTFrais.getRefPoste().getIdPoste(), entityTFrais.getRefPoste().getRefPoste()));
        }


        EnumRefTransaction enumRefTransaction = EnumRefTransaction.fromId(entityTFrais.getIdTransaction().getId());

        if (entityTFrais.getIdTransaction() != null) {
            compta.setTransactionTypeItem(new ItemDto(entityTFrais.getIdTransaction().getId(), Utils.getLabel(EnumLanguage.fromshortCode(enumLanguage.getShortCode()), enumRefTransaction.name(), null)));
        }

        return compta;
    }
}
