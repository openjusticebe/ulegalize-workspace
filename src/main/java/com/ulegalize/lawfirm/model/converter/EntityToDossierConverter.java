package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumDossierType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.model.entity.TDossierRights;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TDossiersVcTags;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.DossiersUtils;
import com.ulegalize.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EntityToDossierConverter implements SuperTriConverter<TDossiers, EnumLanguage, DossierDTO> {

    private static final int maxLengthNumber = 90;
    private static final int minLengthNumber = 0;

    @Override
    public DossierDTO apply(TDossiers dossier, EnumLanguage enumLanguage) {

        DossierDTO dossierDTO = new DossierDTO();
        dossierDTO.setId(dossier.getIdDoss());
        dossierDTO.setNum(dossier.getNum_doss());
        dossierDTO.setNomenclature(dossier.getNomenclature());
        dossierDTO.setNote(dossier.getNote());
        dossierDTO.setIsDigital(dossier.getIsDigital());
        dossierDTO.setDrivePath(dossier.getDrivePath());

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
        String label = Utils.getLabel(enumLanguage, enumDossierType.name(), null);

        dossierDTO.setTypeItem(new ItemStringDto(enumDossierType.getDossType(), label));
        dossierDTO.setMemo(dossier.getMemo());

        if (!CollectionUtils.isEmpty(dossier.getDossierContactList())) {
            String clientName = "";

            if (!dossier.getDoss_type().equalsIgnoreCase(EnumDossierType.MD.getDossType())) {

                dossierDTO.setDossierContactDTO(dossier.getDossierContactList().stream().map(dossierContact -> {
                    String fullname = ClientsUtils.getFullname(dossierContact.getClients().getF_nom(), dossierContact.getClients().getF_prenom(), dossierContact.getClients().getF_company());
                    String email = dossierContact.getClients().getF_email();
                    ItemLongDto client = new ItemLongDto(dossierContact.getClients().getId_client(), fullname, email);
                    ItemDto clientType = new ItemDto(dossierContact.getContactTypeId().getId(), Utils.getLabel(enumLanguage,
                            dossierContact.getContactTypeId().name(), null));
                    DossierContactDTO dossierContactDTO = new DossierContactDTO();
                    dossierContactDTO.setClient(client);
                    dossierContactDTO.setClientType(clientType);
                    return dossierContactDTO;
                }).collect(Collectors.toList()));

                clientName = dossierDTO.getDossierContactDTO().stream().map(dto -> dto.getClient().getLabel()).collect(Collectors.joining(", "));
                int maxLength = Math.min(clientName.length(), maxLengthNumber);

                clientName = clientName.substring(minLengthNumber, maxLength);

                dossierDTO.setLabel(DossiersUtils.getDossierLabelItem(dossier.getNomenclature()) + " " + clientName
                ); //2019 / 0012 parties

               /* dossierDTO.setLabel(DossiersUtils.getDossierLabelItem(dossier.getNomenclature())
                        + " " + clientName + "/" + opposingName); //2019 / 0012 blahaz/azklk*/

            } else {
                dossierDTO.setDossierContactDTO(dossier.getDossierContactList().stream().map(dossierContact -> {
                    String fullname = ClientsUtils.getFullname(dossierContact.getClients().getF_nom(), dossierContact.getClients().getF_prenom(), dossierContact.getClients().getF_company());
                    String email = dossierContact.getClients().getF_email();
                    ItemLongDto client = new ItemLongDto(dossierContact.getClients().getId_client(), fullname, email);
                    ItemDto clientType = new ItemDto(dossierContact.getContactTypeId().getId(), Utils.getLabel(enumLanguage,
                            dossierContact.getContactTypeId().name(), null));
                    DossierContactDTO dossierContactDTO = new DossierContactDTO();
                    dossierContactDTO.setClient(client);
                    dossierContactDTO.setClientType(clientType);
                    return dossierContactDTO;
                }).collect(Collectors.toList()));

                clientName = dossierDTO.getDossierContactDTO().stream().map(dto -> dto.getClient().getLabel()).collect(Collectors.joining(", "));
                int maxLength = Math.min(clientName.length(), maxLengthNumber);

                clientName = clientName.substring(minLengthNumber, maxLength);

                dossierDTO.setLabel(DossiersUtils.getDossierLabelItem(dossier.getNomenclature()) + " " + clientName
                ); //2019 / 0012 parties

            }

        }

        if (!CollectionUtils.isEmpty(dossier.getTDossiersVcTags())) {

            List<ItemLongDto> itemLongDtoList = new ArrayList<>();

            for (TDossiersVcTags tDossiersVcTags : dossier.getTDossiersVcTags()) {
                ItemLongDto itemLongDto = new ItemLongDto(tDossiersVcTags.getTVirtualCabTags().getId(), tDossiersVcTags.getTVirtualCabTags().getLabel());
                itemLongDtoList.add(itemLongDto);
            }

            dossierDTO.setTagsList(itemLongDtoList);
        }

        dossierDTO.setFullnameDossier(DossiersUtils.getDossierLabelItem(dossier.getNomenclature()));

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
                    dossier.getEnumMatiereRubrique().name(), com.ulegalize.lawfirm.utils.Utils.PACKAGENAME
            )));
        }

        return dossierDTO;
    }

}
