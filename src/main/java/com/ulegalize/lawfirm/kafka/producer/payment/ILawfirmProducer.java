package com.ulegalize.lawfirm.kafka.producer.payment;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface ILawfirmProducer {

    public void updateLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken);

    public void switchLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken);

    public void updateNotificationLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken);

}