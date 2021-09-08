package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.InvoiceDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.utils.SuperConverter;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.DossiersUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityToInvoiceConverter implements SuperConverter<TFactures, InvoiceDTO> {

    @Override
    public InvoiceDTO apply(TFactures factures) {

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(factures.getIdFacture());
        invoiceDTO.setDossierId(factures.getIdDoss());
        if (factures.getTDossiers() != null) {
            invoiceDTO.setDossier(DossiersUtils.getDossierLabelItem(
                    factures.getTDossiers().getYear_doss(),
                    factures.getTDossiers().getNum_doss()
            ));
            invoiceDTO.setDossierItem(new ItemLongDto(
                    factures.getIdDoss(),
                    DossiersUtils.getDossierLabelItem(
                            factures.getTDossiers().getYear_doss(),
                            factures.getTDossiers().getNum_doss())
            ));
            invoiceDTO.setDossierYear(factures.getTDossiers().getYear_doss());
            invoiceDTO.setDossierNumber(DossiersUtils.getDossierNum(factures.getTDossiers().getNum_doss()));
        }
        invoiceDTO.setPosteId(factures.getIdPoste());
        if (factures.getPoste() != null) {
            invoiceDTO.setPosteItem(new ItemDto(
                    factures.getIdPoste(),
                    factures.getPoste().getRefPoste()));
        }
        invoiceDTO.setClientId(factures.getIdTiers());
        if (factures.getTClients() != null) {
            String fullname = ClientsUtils.getFullname(factures.getTClients().getF_nom(), factures.getTClients().getF_prenom(), factures.getTClients().getF_company());

            invoiceDTO.setClientItem(new ItemLongDto(
                    factures.getIdTiers(), fullname
            ));
        }

        invoiceDTO.setVcKey(factures.getVcKey());
        invoiceDTO.setNumFacture(factures.getNumFacture());
        invoiceDTO.setYearFacture(factures.getYearFacture());
        invoiceDTO.setReference(factures.getFactureRef());

        if (factures.getIdFactureType() != null) {
            invoiceDTO.setTypeId(factures.getIdFactureType().getId());
            invoiceDTO.setTypeItem(new ItemLongDto(
                    factures.getIdFactureType().getId(),
                    factures.getIdFactureType().getDescription()
            ));
        }
        invoiceDTO.setMontant(factures.getMontant());
        invoiceDTO.setValid(factures.getValid());
        invoiceDTO.setDateValue(factures.getDateValue());
        invoiceDTO.setDateEcheance(factures.getDateEcheance());
        if (factures.getTFactureEcheance() != null) {
            invoiceDTO.setEcheanceId(factures.getIdEcheance());
            invoiceDTO.setEcheanceItem(new ItemDto(
                    factures.getTFactureEcheance().getID(),
                    factures.getTFactureEcheance().getDESCRIPTION()));
        }

        return invoiceDTO;
    }

}
