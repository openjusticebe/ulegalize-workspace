package com.ulegalize.lawfirm.kafka.producer.transparency;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface IClientProducer {

    void updateClient(ContactSummary message, LawfirmToken lawfirmToken);
}