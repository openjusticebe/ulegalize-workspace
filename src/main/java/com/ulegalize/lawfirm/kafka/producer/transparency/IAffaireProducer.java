package com.ulegalize.lawfirm.kafka.producer.transparency;

import com.ulegalize.dto.CaseCreationDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface IAffaireProducer {

    void attachAffaire(CaseCreationDTO message, LawfirmToken lawfirmToken);

    void updateAffaire(CaseCreationDTO message, LawfirmToken lawfirmToken);
}