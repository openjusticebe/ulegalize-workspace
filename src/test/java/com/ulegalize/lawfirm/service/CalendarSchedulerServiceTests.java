package com.ulegalize.lawfirm.service;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class CalendarSchedulerServiceTests extends EntityTest {

    @Autowired
    private CalendarScheduler calendarScheduler;

    @Test
    public void test_A_executeInfo() throws LawfirmBusinessException {
        LawfirmEntity lawfirm = createLawfirm();
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newUserForsecurity@gmail.com");
        Date start = new Date();
        Date end = new Date();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, start, end);
        calendarScheduler.executeInfoScheduler(lawfirmUsers.getUser().getId());

    }

}
