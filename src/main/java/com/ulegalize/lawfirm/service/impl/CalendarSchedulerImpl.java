package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TCalendarParticipants;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumSlackUrl;
import com.ulegalize.lawfirm.repository.TCalendarEventRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.rest.v2.SlackApi;
import com.ulegalize.lawfirm.service.CalendarScheduler;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CalendarSchedulerImpl implements CalendarScheduler {
    private static final int SLOT_EVENT = 30;
    private final TUsersRepository usersRepository;
    private final TCalendarEventRepository tCalendarEventRepository;
    private final SlackApi slackApi;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    public CalendarSchedulerImpl(TUsersRepository usersRepository,
                                 TCalendarEventRepository tCalendarEventRepository,
                                 SlackApi slackApi) {
        this.usersRepository = usersRepository;
        this.tCalendarEventRepository = tCalendarEventRepository;
        this.slackApi = slackApi;
    }


    @Override
    @Scheduled(cron = "#{getSchedulerValue}", zone = "Europe/Brussels")
    public void executeInfo() {
        executeInfoScheduler(203L);
    }

    @Override
    public void executeInfoScheduler(Long userId) {
        log.info("Entering executeInfo ");
        if (!activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")
                && !activeProfile.equalsIgnoreCase("test")) {
            log.info("inside scheduler ");

            // info
            Optional<TUsers> usersOptional = usersRepository.findById(userId);
            if (usersOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            Date dt = new Date();

            LocalDateTime startimeTemp = LocalDateTime.from(dt.toInstant().atZone(ZoneId.systemDefault())).plusDays(14);
            LocalDateTime endtimeTemp = LocalDateTime.from(dt.toInstant().atZone(ZoneId.systemDefault())).plusDays(28);
            LocalDateTime startime = LocalDateTime.of(startimeTemp.getYear(), startimeTemp.getMonth(), startimeTemp.getDayOfMonth(), startimeTemp.getHour(), 0);
            LocalDateTime endtime = LocalDateTime.of(endtimeTemp.getYear(), endtimeTemp.getMonth(), endtimeTemp.getDayOfMonth(), endtimeTemp.getHour(), 0);


            log.debug("Start date {}", startime);
            log.debug("End date {}", endtime);

            List<LocalDateTime> slots = getSlots(startime, endtime, SLOT_EVENT);
            // create next 2 weeks
            //create a new RDV event
            for (LocalDateTime slot : slots) {
                if (!slot.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                        && !slot.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                        // 9 to 19h
                        && (slot.getHour() >= 9 && slot.getHour() <= 18)) {
                    Date start = CalendarEventsUtil.convertToDateViaInstant(slot);
                    Date end = CalendarEventsUtil.convertToDateViaInstant(slot.plusMinutes(SLOT_EVENT));


                    Optional<TCalendarEvent> calendarEventOptional = tCalendarEventRepository.findByUserIdAndStartAndEndAndEventType(usersOptional.get().getId(), start, end, EnumCalendarEventType.PERM);

                    if (!calendarEventOptional.isPresent()) {
                        TCalendarEvent calendarEvent = new TCalendarEvent();
                        calendarEvent.setEventType(EnumCalendarEventType.PERM);
                        calendarEvent.setTitle("Evènement du " + start);
                        calendarEvent.setCreationDate(new Date());
                        calendarEvent.setCreationUser("schedul");
                        calendarEvent.setNote("");
                        calendarEvent.setTUsers(usersOptional.get());
                        calendarEvent.setStart(start);
                        calendarEvent.setEnd(end);

                        TCalendarParticipants tCalendarParticipants = new TCalendarParticipants();
                        tCalendarParticipants.setTCalendarEvent(calendarEvent);
                        tCalendarParticipants.setUserEmail(usersOptional.get().getEmail());
                        tCalendarParticipants.setCreUser("schedul");

                        calendarEvent.setTCalendarParticipants(new ArrayList<>());
                        calendarEvent.getTCalendarParticipants().add(tCalendarParticipants);

                        log.debug("New event {}", calendarEvent);

                        tCalendarEventRepository.save(calendarEvent);
                    }
                }
            }

            log.info("Date created for the next 14 days");

//            delete old event
            LocalDateTime deletedStart = LocalDateTime.from(dt.toInstant().atZone(ZoneId.systemDefault())).minusDays(10);

            Date start = CalendarEventsUtil.convertToDateViaInstant(deletedStart);
            log.info("Event to delete {}", start);

            List<TCalendarEvent> tCalendarEvents = tCalendarEventRepository.findByUserIdAndStartAndEndAndEventType(usersOptional.get().getId(), start, EnumCalendarEventType.PERM);
            tCalendarEventRepository.deleteAll(tCalendarEvents);
            log.info("Event deleted");
            slackApi.sendSensitiveNotification("Calendar event created from " + start, endtime, EnumSlackUrl.CALENDAR_SCHEDULER);

        }

        log.info("Leaving executeInfo ");
    }

    private List<LocalDateTime> getSlots(LocalDateTime start, LocalDateTime end, int slotMinutes) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime startime = start;
        while (startime.isBefore(end)) {
            slots.add(startime);
            // Prepare for the next loop.
            startime = startime.plusMinutes(slotMinutes);
        }

        return slots;

    }
}