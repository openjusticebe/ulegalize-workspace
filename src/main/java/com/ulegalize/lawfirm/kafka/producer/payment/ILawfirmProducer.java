package com.ulegalize.lawfirm.kafka.producer.payment;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.dto.LawfirmDriveDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface ILawfirmProducer {

    void updateLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken);

    void updateLawfirmDrive(LawfirmDriveDTO message, LawfirmToken lawfirmToken);

    void switchLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken);

    void updateNotificationLawfirm(LawfirmDTO message, LawfirmToken lawfirmToken);

}