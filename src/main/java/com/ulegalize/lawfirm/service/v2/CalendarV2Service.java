package com.ulegalize.lawfirm.service.v2;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawyerDuty;
import com.ulegalize.lawfirm.model.LawyerDutyRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

public interface CalendarV2Service {
    List<LawfirmCalendarEventDTO> findAllByUserId(Long userId, Long dossierId, Date start, Date end, List<String> eventTypesSelected) throws LawfirmBusinessException;

    LawfirmCalendarEventDTO approveLawfirmCalendarEvent(Long eventId) throws ResponseStatusException;

    void createLawfirmCalendarEvent(LawfirmCalendarEventDTO calendarEvent);

    void updateLawfirmCalendarEvent(Long eventId, LawfirmCalendarEventDTO calendarEvent);

    Long deleteEvent(Long eventId);

    Page<LawfirmCalendarEventDTO> countUnapprovedLawfirmCalendar(Long userId, EnumCalendarEventType rdv);

    /**
     * use from public site
     *
     * @param lawyerAlias
     * @param appointment
     * @return
     * @throws LawfirmBusinessException
     */
    LawyerDuty newLawyerAppointment(String lawyerAlias, LawyerDutyRequest appointment) throws LawfirmBusinessException;
}
