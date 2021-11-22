package com.ulegalize.lawfirm.kafka.producer.transparency;

import com.ulegalize.dto.CaseCreationDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;

public interface IAffaireProducer {

    public void attachAffaire(CaseCreationDTO message, LawfirmToken lawfirmToken);
}