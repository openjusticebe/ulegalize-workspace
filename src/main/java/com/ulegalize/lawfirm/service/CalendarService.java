package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface CalendarService {
    public List<LawfirmCalendarEventDTO> findAllByUserId(Long userId) throws LawfirmBusinessException;

    public LawfirmCalendarEventDTO approveLawfirmCalendarEvent(LawfirmToken lawfirmToken, Long eventId) throws ResponseStatusException;
}
