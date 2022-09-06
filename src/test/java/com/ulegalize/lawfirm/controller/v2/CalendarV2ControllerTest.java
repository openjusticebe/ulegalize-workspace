package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CalendarV2ControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    private UsernamePasswordAuthenticationToken authentication;

    private LawfirmEntity lawfirm;

    @Autowired
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setupAuthenticate() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }


    @WithMockUser(value = "spring")
    @Test
    public void test_A_getCalendarEvents() throws Exception {
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, new Date(), new Date());
        List<EnumCalendarEventType> enumCalendarEventTypeList = Arrays.asList(EnumCalendarEventType.values());

        // workaround to avoid [] into the String
        String eventType = String.valueOf(enumCalendarEventTypeList).substring(1, String.valueOf(enumCalendarEventTypeList).length() - 1);
        mvc.perform(get("/v2/calendar/events")
                .with(authentication(authentication))
                .param("start", ZonedDateTime.now().minusDays(2).toString())
                .param("end", ZonedDateTime.now().plusDays(5).toString())
                .param("userId", String.valueOf(userId))
                .param("eventTypesSelected", eventType))
                .andExpect(jsonPath("$[0].eventType", is(tCalendarEvent.getEventType().getCode())))
                .andExpect(status().isOk());
    }

}
