package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumTitle;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import com.ulegalize.utils.ClientsUtils;
import com.ulegalize.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class EntityToContactSummaryConverter implements SuperTriConverter<TClients, String, ContactSummary> {

    @Override
    public ContactSummary apply(TClients entity, String language) {
        ContactSummary contactSummary = new ContactSummary();
        contactSummary.setId(entity.getId_client());
        contactSummary.setEmail(entity.getF_email());
        contactSummary.setFirstname(entity.getF_prenom());
        contactSummary.setLastname(entity.getF_nom());
        contactSummary.setPhone(entity.getF_tel());

        contactSummary.setCompany(entity.getF_company());
        contactSummary.setType(entity.getClient_type().getId());
        EnumTitle enumTitle = EnumTitle.fromId(entity.getId_title());
        if (enumTitle != null) {
            contactSummary.setTitle(enumTitle);
            contactSummary.setTitleItem(new ItemStringDto(entity.getId_title(), Utils.getLabel(EnumLanguage.fromshortCode(language), enumTitle.name(), null)));
        }
        contactSummary.setLanguage(entity.getId_lg());
        contactSummary.setAddress(entity.getF_rue());
        contactSummary.setCp(entity.getF_cp());
        contactSummary.setCity(entity.getF_ville());
        contactSummary.setTel(entity.getF_tel());
        contactSummary.setMobile(entity.getF_gsm());
        contactSummary.setFax(entity.getF_fax());
        contactSummary.setNrnat(entity.getF_nn());
        contactSummary.setEtr(entity.getF_noe());
        contactSummary.setTva(entity.getF_tva());
        contactSummary.setBirthdate(entity.getBirthdate());
        contactSummary.setCountry(entity.getId_country_alpha3());
        if (!CollectionUtils.isEmpty(entity.getVirtualcabClientList())) {
            contactSummary.setVcKey(entity.getVirtualcabClientList().get(0).getLawfirm().getVckey());
        }
        contactSummary.setUserId(entity.getUser_id());
        contactSummary.setIban(entity.getIban());
        contactSummary.setBic(entity.getBic());
        contactSummary.setJob(entity.getJob());
        //natural person
        if (entity.getClient_type() != null && entity.getClient_type().equals(EnumClientType.NATURAL_PERSON)) {
            String fullname = ClientsUtils.getEmailFullname(entity.getF_email(), entity.getF_nom(), entity.getF_prenom(), entity.getF_company());

            contactSummary.setFullName(fullname);
        } else {
            //company or collegue
            contactSummary.setFullName(entity.getF_company() != null ? entity.getF_company() : "");
        }

        return contactSummary;

    }
}
