package com.ulegalize.lawfirm.rest;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.dto.UpdateShareRequestDTO;
import com.ulegalize.lawfirm.exception.RestException;
import org.springframework.web.server.ResponseStatusException;

public interface LawfirmTransparencyApi {
    void createCasFromAgenda(LawfirmCalendarEventDTO lawfirmCalendarEventDTO, String token) throws ResponseStatusException;

    void createShareCases(String internalToken, UpdateShareRequestDTO updateShareRequest) throws RestException;
}
