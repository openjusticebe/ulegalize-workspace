package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumDossierContactType;
import com.ulegalize.enumeration.EnumDossierType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.model.entity.DossierContact;
import com.ulegalize.lawfirm.model.entity.TDossierRights;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.DossiersUtils;
import com.ulegalize.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EntityToDossierConverter implements SuperTriConverter<TDossiers, EnumLanguage, DossierDTO> {

    @Override
    public DossierDTO apply(TDossiers dossier, EnumLanguage enumLanguage) {

        DossierDTO dossierDTO = new DossierDTO();
        dossierDTO.setId(dossier.getIdDoss());
        dossierDTO.setNum(dossier.getNum_doss());
        dossierDTO.setNote(dossier.getNote());
        dossierDTO.setIsDigital(dossier.getIsDigital());

        if (dossier.getDossierRightsList() != null) {
            Optional<TDossierRights> dossierRightsOptional = dossier.getDossierRightsList().stream().filter(tDossierRights -> tDossierRights.getVcOwner().equals(EnumVCOwner.OWNER_VC)).findFirst();
            // get the vc key owner
            dossierRightsOptional.ifPresent(dossierRights -> dossierDTO.setVckeyOwner(dossierRights.getLawfirmUsers().getLawfirm().getVckey()));

        }
        if (dossier.getDate_open() != null) {
            dossierDTO.setOpenDossier(dossier.getDate_open());
        }
        if (dossier.getDate_close() != null) {
            dossierDTO.setCloseDossier(dossier.getDate_close());
        }
        EnumDossierType enumDossierType = EnumDossierType.fromdossType(dossier.getDoss_type());
        dossierDTO.setType(enumDossierType);
        String label = Utils.getLabel(enumLanguage, enumDossierType.getLabelFr(), enumDossierType.getLabelEn(), enumDossierType.getLabelNl(), enumDossierType.getLabelNl());

        dossierDTO.setTypeItem(new ItemStringDto(enumDossierType.getDossType(), label));
        dossierDTO.setMemo(dossier.getMemo());

        if (!CollectionUtils.isEmpty(dossier.getDossierContactList())) {
            String clientName = "";
            String opposingName = "";
            if (!dossier.getDoss_type().equalsIgnoreCase(EnumDossierType.MD.getDossType())) {
                // client
                Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

                if (dossierContactClient.isPresent()) {
                    dossierDTO.setIdClient(dossierContactClient.get().getClients().getId_client());
                    dossierDTO.setEmailClient(dossierContactClient.get().getClients().getF_email());
                    dossierDTO.setFirstnameClient(dossierContactClient.get().getClients().getF_prenom());
                    dossierDTO.setLastnameClient(dossierContactClient.get().getClients().getF_nom());
                    dossierDTO.setBirthdateClient(dossierContactClient.get().getClients().getBirthdate());

                    String fullname = ClientsUtils.getFullname(dossierContactClient.get().getClients().getF_nom(), dossierContactClient.get().getClients().getF_prenom(), dossierContactClient.get().getClients().getF_company());
                    String email = dossierContactClient.get().getClients().getF_email();

                    dossierDTO.setClient(new ItemLongDto(dossierContactClient.get().getClients().getId_client(), fullname, email));
                    clientName = dossierContactClient.get().getClients().getF_nom();
                }
                // adverse
                Optional<DossierContact> dossierContactOpposing = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.OPPOSING)).findAny();

                if (dossierContactOpposing.isPresent()) {
                    dossierDTO.setIdAdverseClient(dossierContactOpposing.get().getClients().getId_client());
                    String fullname = ClientsUtils.getFullname(dossierContactOpposing.get().getClients().getF_nom(), dossierContactOpposing.get().getClients().getF_prenom(), dossierContactOpposing.get().getClients().getF_company());

                    dossierDTO.setAdverseFirstnameClient(dossierContactOpposing.get().getClients().getF_prenom());
                    dossierDTO.setAdverseLastnameClient(dossierContactOpposing.get().getClients().getF_nom());
                    dossierDTO.setAdverseClient(new ItemLongDto(dossierContactOpposing.get().getClients().getId_client(), fullname));

                    opposingName = dossierContactOpposing.get().getClients().getF_nom();

                }
                dossierDTO.setLabel(DossiersUtils.getDossierLabelItem(dossier.getYear_doss(), dossier.getNum_doss())
                        + " " + clientName + "/" + opposingName); //2019 / 0012 blahaz/azklk

            } else {
                dossierDTO.setClientList(dossier.getDossierContactList().stream().map(dossierContact -> {
                    String fullname = ClientsUtils.getFullname(dossierContact.getClients().getF_nom(), dossierContact.getClients().getF_prenom(), dossierContact.getClients().getF_company());
                    String email = dossierContact.getClients().getF_email();
                    return new ItemClientDto(dossierContact.getClients().getId_client(), fullname, email, EnumDossierContactType.PARTY);
                }).collect(Collectors.toList()));
                clientName = dossierDTO.getClientList().stream().map(Item::getLabel).collect(Collectors.joining(", "));
                int maxLength = Math.min(clientName.length(), 45);

                clientName = clientName.substring(0, maxLength);

                dossierDTO.setLabel(DossiersUtils.getDossierLabelItem(dossier.getYear_doss(), dossier.getNum_doss()) + " " + clientName
                ); //2019 / 0012 parties

            }

        }

        dossierDTO.setFullnameDossier(DossiersUtils.getDossierLabelItem(dossier.getYear_doss(), dossier.getNum_doss()));

        dossierDTO.setYear(Long.parseLong(dossier.getYear_doss()));

        dossierDTO.setSuccess_fee_perc(dossier.getSuccess_fee_perc());
        dossierDTO.setSuccess_fee_montant(dossier.getSuccess_fee_montant());
        dossierDTO.setCouthoraire(dossier.getCouthoraire());
        dossierDTO.setQuality(dossier.getClientQuality());
        dossierDTO.setKeywords(dossier.getKeywords());
        dossierDTO.setIdUserResponsible(dossier.getId_user_resp());
        dossierDTO.setMemo(dossier.getMemo());
//        ,round(ifnull(presta.cout,0) + ifnull(justice.cout,0) + ifnull(debour.cout,0) + ifnull(collabo.cout,0) - ifnull(hono.cout,0),2) as totcout

        if (dossier.getEnumMatiereRubrique() != null) {
            dossierDTO.setId_matiere_rubrique(dossier.getEnumMatiereRubrique().getId());
            dossierDTO.setMatiere_rubrique(new ItemLongDto(dossier.getEnumMatiereRubrique().getMatiereId(), Utils.getLabel(enumLanguage,
                    dossier.getEnumMatiereRubrique().getDescriptionFr(),
                    dossier.getEnumMatiereRubrique().getDescriptionEn(),
                    dossier.getEnumMatiereRubrique().getDescriptionNl(),
                    dossier.getEnumMatiereRubrique().getDescriptionDe()
            )));
        }


        if (dossier.getOpposingCounsel() != null) {
            dossierDTO.setConseilIdAdverseClient(dossier.getOpposingCounsel().getId_client());
            String fullname = ClientsUtils.getFullname(dossier.getOpposingCounsel().getF_nom(), dossier.getOpposingCounsel().getF_prenom(), dossier.getOpposingCounsel().getF_company());

            dossierDTO.setConseilAdverseClient(new ItemLongDto(dossier.getOpposingCounsel().getId_client(), fullname));
        }

        return dossierDTO;
    }

}
