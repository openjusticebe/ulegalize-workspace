package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class CalendarValidator {

    public void validate(LawfirmCalendarEventDTO lawfirmCalendarEventDTO) throws ResponseStatusException {
        log.debug("Entering validate() with lawfirmCalendarEventDTO {} ", lawfirmCalendarEventDTO);
        if (lawfirmCalendarEventDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event Entity is null");
        }
        if (lawfirmCalendarEventDTO.getStart() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "start date cannot be empty");
        }

        if (lawfirmCalendarEventDTO.getEventType() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event type is null");
        }

        if (!lawfirmCalendarEventDTO.getEventType().equals(EnumCalendarEventType.TASK)) {
            if (lawfirmCalendarEventDTO.getEnd() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "end date cannot be empty");
            }
            if (lawfirmCalendarEventDTO.getEnd().before(lawfirmCalendarEventDTO.getStart())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "End date cannot be before start date");
            }
        }
        if (lawfirmCalendarEventDTO.getParticipantsEmail() != null) {
            for (String email : lawfirmCalendarEventDTO.getParticipantsEmail()) {
                if (!Utils.validateEmail(email)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant's email is invalid ! : ");
                }
            }
        }
        log.debug("Leaving validate()");
    }
}
