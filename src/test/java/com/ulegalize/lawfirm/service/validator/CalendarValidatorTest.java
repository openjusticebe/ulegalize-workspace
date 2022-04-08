package com.ulegalize.lawfirm.service.validator;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.EntityTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class CalendarValidatorTest extends EntityTest {

    @Autowired
    private CalendarValidator calendarValidator;

    @Test
    void test_A_validate_exception_entityNull() {
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = null;

        assertThrows(ResponseStatusException.class, () -> {
            calendarValidator.validate(lawfirmCalendarEventDTO);
        });
    }

    @Test
    void test_B_validate_exception_startDate_null() {
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();

        assertThrows(ResponseStatusException.class, () -> {
            calendarValidator.validate(lawfirmCalendarEventDTO);
        });
    }

    @Test
    void test_C_validate_exception_eventType_null() {
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();

        lawfirmCalendarEventDTO.setStart(new Date());

        // Default is EnumCalendarEventType.PERM
        lawfirmCalendarEventDTO.setEventType(null);

        assertThrows(ResponseStatusException.class, () -> {
            calendarValidator.validate(lawfirmCalendarEventDTO);
        });
    }

    @Test
    void test_D_validate_exception_eventType_end_Null() {
        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();

        lawfirmCalendarEventDTO.setStart(new Date());

        lawfirmCalendarEventDTO.setEventType(EnumCalendarEventType.PERM);

        assertThrows(ResponseStatusException.class, () -> {
            calendarValidator.validate(lawfirmCalendarEventDTO);
        });
    }

    @Test
    void test_E_validate_exception_endDate_Before_startDate() {
        Date dateEndTest = new Date();
        dateEndTest.setHours(dateEndTest.getHours() - 1);

        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();

        lawfirmCalendarEventDTO.setStart(new Date());

        lawfirmCalendarEventDTO.setEventType(EnumCalendarEventType.PERM);

        lawfirmCalendarEventDTO.setEnd(dateEndTest);

        assertThrows(ResponseStatusException.class, () -> {
            calendarValidator.validate(lawfirmCalendarEventDTO);
        });
    }

    @Test
    void test_F_validate_exception_emailNotValid() {
        String participant1 = "test@test.com";
        String participant2 = "test2@@test.com";

        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = new LawfirmCalendarEventDTO();

        lawfirmCalendarEventDTO.setStart(new Date());

        lawfirmCalendarEventDTO.setEventType(EnumCalendarEventType.PERM);

        lawfirmCalendarEventDTO.setEnd(new Date());

        lawfirmCalendarEventDTO.setParticipantsEmail(new ArrayList<>());
        lawfirmCalendarEventDTO.getParticipantsEmail().add(participant1);
        lawfirmCalendarEventDTO.getParticipantsEmail().add(participant2);

        assertThrows(ResponseStatusException.class, () -> {
            calendarValidator.validate(lawfirmCalendarEventDTO);
        });
    }
}