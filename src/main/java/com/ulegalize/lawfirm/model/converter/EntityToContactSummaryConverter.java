package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.enumeration.EnumTitle;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.utils.SuperConverter;
import com.ulegalize.utils.ClientsUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityToContactSummaryConverter implements SuperConverter<TClients, ContactSummary> {

    @Override
    public ContactSummary apply(TClients entity) {
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
            contactSummary.setTitleItem(new ItemStringDto(entity.getId_title(), enumTitle.getTitle()));
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
        if (entity.getVirtualcabClientList() != null) {
            contactSummary.setVcKey(entity.getVirtualcabClientList().get(0).getLawfirm().getVckey());
        }
        contactSummary.setUserId(entity.getUser_id());
        contactSummary.setIban(entity.getIban());
        contactSummary.setBic(entity.getBic());
        //natural person
        if (entity.getClient_type() != null && entity.getClient_type().equals(EnumClientType.NATURAL_PERSON)) {
            String fullname = ClientsUtils.getFullname(entity.getF_nom(), entity.getF_prenom(), entity.getF_company());

            contactSummary.setFullName(fullname);
        } else {
            //company or collegue
            contactSummary.setFullName(entity.getF_company());
        }

        return contactSummary;

    }
}
