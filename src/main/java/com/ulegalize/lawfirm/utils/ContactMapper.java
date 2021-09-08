package com.ulegalize.lawfirm.utils;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.utils.ClientsUtils;

public class ContactMapper {

	public static ContactSummary toContactSummary(TClients client) {
        ContactSummary ds = new ContactSummary();
        ds.setId(client.getId_client());
        ds.setEmail(client.getF_email());

        //natural person
        if (client.getClient_type().equals(EnumClientType.NATURAL_PERSON)) {
            String fullname = ClientsUtils.getFullname(client.getF_nom(), client.getF_prenom(), client.getF_company());

            ds.setFullName(fullname);
        } else {
            //company or collegue
            ds.setFullName(client.getF_company());
        }

        return ds;
    }
}
