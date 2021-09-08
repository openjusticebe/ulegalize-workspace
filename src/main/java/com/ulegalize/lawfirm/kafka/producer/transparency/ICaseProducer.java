package com.ulegalize.lawfirm.kafka.producer.transparency;

import com.ulegalize.dto.CaseCreationDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.lawfirm.model.LawfirmToken;
import org.springframework.web.server.ResponseStatusException;

public interface ICaseProducer {

    public void createCaseMessage(CaseCreationDTO message, LawfirmToken lawfirmToken);

    public void createLawfirmMessage(LawfirmToken lawfirmToken, String newVcKey, String userEmail, String language, long userId);

    public void createShareCases(LawfirmToken lawfirmToken, UpdateShareRequestDTO updateShareRequest) throws ResponseStatusException;


}