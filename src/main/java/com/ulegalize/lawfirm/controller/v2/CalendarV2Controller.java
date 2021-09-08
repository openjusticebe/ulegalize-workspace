package com.ulegalize.lawfirm.controller.v2;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.service.v2.CalendarV2Service;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@ApiIgnore
@RequestMapping("/v2/calendar")
@Slf4j
public class CalendarV2Controller {
    @Autowired
    private CalendarV2Service calendarV2Service;

    @GetMapping(value = "/events")
    public List<LawfirmCalendarEventDTO> getLawfirmCalendar(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end,
                                                            @RequestParam(required = false) Long dossierId,
                                                            @RequestParam Long userId,
                                                            @RequestParam(value = "eventTypesSelected") List<String> eventTypesSelected) throws LawfirmBusinessException {

        log.debug("getLawfirmCalendar(userId: {}, start: {}, end: {} )", userId, start, end);

        return calendarV2Service.findAllByUserId(userId, dossierId, CalendarEventsUtil.convertZoneToDateViaInstant(start), CalendarEventsUtil.convertZoneToDateViaInstant(end), eventTypesSelected);
    }

    @GetMapping(value = "/events/unapproved")
    public ResponseEntity<Page<LawfirmCalendarEventDTO>> countUnapprovedLawfirmCalendar(@RequestParam(required = false) Long userId,
                                                                                        @RequestParam String vcKey
    ) throws LawfirmBusinessException {

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("countUnapprovedLawfirmCalendar( userId {})", userId);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        if (vcKey.equalsIgnoreCase(lawfirmToken.getVcKey())) {
            responseBuilder
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));
        }
        return responseBuilder
                .body(calendarV2Service.countUnapprovedLawfirmCalendar(userId, EnumCalendarEventType.RDV));
    }

    @PostMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LawfirmCalendarEventDTO createLawfirmCalendarEvent(@RequestBody LawfirmCalendarEventDTO calendarEvent) {

        log.debug("createLawfirmCalendarEvent()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("createLawfirmCalendarEvent(userid: {}, calendarEvent: {} )", lawfirmToken.getUserId(), calendarEvent);

        log.info("User logged {}", lawfirmToken);
        calendarV2Service.createLawfirmCalendarEvent(calendarEvent);

        return calendarEvent;
    }

    @PutMapping(value = "/events/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LawfirmCalendarEventDTO saveLawfirmCalendarEvent(@PathVariable Long eventId, @RequestBody LawfirmCalendarEventDTO calendarEvent) {

        log.debug("saveLawfirmCalendarEvent()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("saveLawfirmCalendarEvent(user id: {}, calendarEvent: {} )", lawfirmToken.getUserId(), calendarEvent);
        calendarV2Service.updateLawfirmCalendarEvent(eventId, calendarEvent);

        return calendarEvent;

    }

    @PutMapping(value = "/events/{eventId}/approved", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LawfirmCalendarEventDTO approvedLawfirmCalendarEvent(@PathVariable Long eventId) {

        log.debug("approvedLawfirmCalendarEvent()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("saveLawfirmCalendarEvent(user id: {} )", lawfirmToken.getUserId());
        return calendarV2Service.approveLawfirmCalendarEvent(eventId);

    }

    @DeleteMapping(value = "/events/{eventId}")
    public Long deleteLawfirmCalendarEvent(@PathVariable Long eventId) {

        log.debug("deleteLawfirmCalendarEvent({})", eventId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("saveLawfirmCalendarEvent(user id: {}, vckey: {} )", lawfirmToken.getUserId(), lawfirmToken.getVcKey());
        return calendarV2Service.deleteEvent(eventId);
    }


}
