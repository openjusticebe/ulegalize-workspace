package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DTOToClientEntityConverter implements SuperTriConverter<ContactSummary, TClients, TClients> {

    @Override
    public TClients apply(ContactSummary dto, TClients entity) {
        TClients clients = entity;
        clients.setId_client(dto.getId());
        clients.setF_email(dto.getEmail() != null ? dto.getEmail() : "");
        clients.setF_prenom(dto.getFirstname());
        clients.setF_nom(dto.getLastname());
        clients.setF_tel(dto.getTel());

        clients.setF_company(dto.getCompany());

        EnumClientType enumClientType = EnumClientType.fromId(dto.getType());
        clients.setClient_type(enumClientType);
        clients.setId_title(dto.getTitle() != null ? dto.getTitle().getIdTitle() : null);
        clients.setId_lg(dto.getLanguage());

        clients.setF_rue(dto.getAddress() != null ? dto.getAddress() : "");
        clients.setF_cp(dto.getCp() != null ? dto.getCp() : "");
        clients.setF_ville(dto.getCity() != null ? dto.getCity() : "");
        clients.setF_tel(dto.getTel() != null ? dto.getTel() : "");
        clients.setF_gsm(dto.getMobile());
        clients.setF_fax(dto.getFax());
        clients.setF_nn(dto.getNrnat() != null ? dto.getNrnat() : "");
        clients.setF_noe(dto.getEtr() != null ? dto.getEtr() : "");
        clients.setF_tva(dto.getTva());
        clients.setBirthdate(dto.getBirthdate());
        clients.setId_country_alpha3(dto.getCountry());
        clients.setIban(dto.getIban());
        clients.setBic(dto.getBic());
//        if (!StringUtils.isEmpty(dto.getVcKey())) {
//            clients.setVc_key(dto.getVcKey());
//        }
        if (!StringUtils.isEmpty(dto.getUserId())) {
            clients.setUser_id(dto.getUserId());
        }

        return clients;

    }
}
