package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ContactSummary;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;

import java.util.List;

public interface ClientService {
    public List<ContactSummary> getAllCientByUserId(Long userId, String searchCriteria) throws LawfirmBusinessException;
}
