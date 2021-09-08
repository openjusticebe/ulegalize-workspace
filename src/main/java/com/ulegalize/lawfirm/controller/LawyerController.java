package com.ulegalize.lawfirm.controller;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.exception.RestException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.*;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.CalendarEventRepository;
import com.ulegalize.lawfirm.repository.ClientRepository;
import com.ulegalize.lawfirm.repository.DossierRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.service.CalendarService;
import com.ulegalize.lawfirm.service.LawyerService;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@ApiIgnore
@RequestMapping("/lawyer")
@Slf4j
public class LawyerController {
    // in minutes
    private static int SLOT_EVENT = 30;
    @Autowired
    EntityToLawfirmPrivateConverter entityToLawfirmPrivateConverter;
    @Autowired
    EntityToCalendarConverter entityToCalendarConverter;
    @Autowired
    CalendarToEntityConverter calendarToEntityConverter;
    @Autowired
    EntityToDossierConverter entityToDossierConverter;
    @Autowired
    EntityToUserConverter entityToUserConverter;

    @Autowired
    private CalendarEventRepository calendarEventRepository;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private DossierRepository dossierRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LawyerService lawyerService;
    @Autowired
    private TUsersRepository usersRepository;

    //TODO replace the explicit rest pathparam by a security/jwt param
    @GetMapping()
    public LawyerDTO getLawyerByUserId() throws LawfirmBusinessException {

        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.debug("getLawfirmByUser(user email: {})", lawfirmToken.getUserEmail());

        return lawyerService.getLayerByEmail(lawfirmToken.getUserEmail());
    }


    @RequestMapping(method = RequestMethod.GET, path = "/calendar/events")
    public List<LawfirmCalendarEventDTO> getLawfirmCalendar(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) throws LawfirmBusinessException {

        log.debug("getLawfirmCalendar()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("getLawfirmCalendar(userId: {}, start: {}, end: {} )", lawfirmToken.getUserId(), start, end);

        return calendarService.findAllByUserId(lawfirmToken.getUserId());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/calendar/events")
    public List<LawfirmCalendarEventDTO> createLawfirmCalendarEvent(@RequestBody LawfirmCalendarEventDTO calendarEvent) throws LawfirmBusinessException {

        log.debug("createLawfirmCalendarEvent()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("createLawfirmCalendarEvent(userid: {}, calendarEvent: {} )", lawfirmToken.getUserId(), calendarEvent);

        log.info("User logged {}", lawfirmToken);

        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime startime = CalendarEventsUtil.convertToLocalDateTimeViaInstant(calendarEvent.getStart());
        LocalDateTime endtime = CalendarEventsUtil.convertToLocalDateTimeViaInstant(calendarEvent.getEnd());
        // create slot by use case
        switch (calendarEvent.getEventType()) {
            case PERM:
                // create by slot of 1/2h
                slots = getSlots(startime, endtime, SLOT_EVENT);

                createEventBySlot(lawfirmToken.getUsername(), slots, calendarEvent, SLOT_EVENT);
                break;
            case RDV:
            case AUD:
            case OTH:
                createEvent(lawfirmToken.getUsername(), startime, endtime, calendarEvent, true);
                break;
            default:
                break;
        }

        return calendarService.findAllByUserId(lawfirmToken.getUserId());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/calendar/events/{eventId}")
    public List<LawfirmCalendarEventDTO> saveLawfirmCalendarEvent(@PathVariable Long eventId, @RequestBody LawfirmCalendarEventDTO calendarEvent) throws LawfirmBusinessException {

        log.debug("saveLawfirmCalendarEvent()");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("saveLawfirmCalendarEvent(user id: {}, calendarEvent: {} )", lawfirmToken.getUserId(), calendarEvent);

        if (calendarEvent.getEnd().before(calendarEvent.getStart())) {
            throw new LawfirmBusinessException("End date cannot be before start date");
        }

        LocalDateTime startime = CalendarEventsUtil.convertToLocalDateTimeViaInstant(calendarEvent.getStart());
        LocalDateTime endtime = CalendarEventsUtil.convertToLocalDateTimeViaInstant(calendarEvent.getEnd());

        switch (calendarEvent.getEventType()) {
            case PERM:

                // create by slot of 1/2h
                List<LocalDateTime> slots = getSlots(startime, endtime, SLOT_EVENT);

                createEventBySlot(lawfirmToken.getUsername(), slots, calendarEvent, SLOT_EVENT);

                break;
            case RDV:
            case AUD:
            case OTH:
                createEvent(lawfirmToken.getUsername(), startime, endtime, calendarEvent, true);
                break;
            default:
                break;
        }

        return calendarService.findAllByUserId(lawfirmToken.getUserId());
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/calendar/events/{eventId}")
    public ResponseEntity<?> deleteLawfirmCalendarEvent(@PathVariable Long eventId) throws LawfirmBusinessException {
        calendarEventRepository.deleteById(eventId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/calendar/events/{eventId}/approve")
    public List<LawfirmCalendarEventDTO> approveLawfirmCalendarEvent(@PathVariable Long eventId) throws LawfirmBusinessException, RestException {
        log.info("Entering approveLawfirmCalendarEvent ");
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Entering approveLawfirmCalendarEvent /{}/calendar/events/{}/approve", lawfirmToken.getUserId(), eventId);

        calendarService.approveLawfirmCalendarEvent(lawfirmToken, eventId);

        return calendarService.findAllByUserId(lawfirmToken.getUserId());
    }

    /**
     * @param username
     * @param calendarEvent
     * @param slotMinutes   in minutes
     */
    private void createEventBySlot(String username, List<LocalDateTime> slots, LawfirmCalendarEventDTO calendarEvent, int slotMinutes) {

        for (LocalDateTime slot : slots) {
            createEvent(username, slot, slot.plusMinutes(slotMinutes), calendarEvent, true);
        }
    }

    /**
     * @param username
     * @param startDate
     * @param endDate
     * @param calendarEvent
     */
    private void createEvent(String username, LocalDateTime startDate, LocalDateTime endDate, LawfirmCalendarEventDTO calendarEvent, boolean approved) {
        log.debug("Entering createEvent String username {}, LocalDateTime startDate {}, LocalDateTime endDate {}, LawfirmCalendarEvent calendarEvent {}, boolean approved {}", username, startDate, endDate, calendarEvent, approved);

        // recalculate the start and end date
        calendarEvent.setStart(CalendarEventsUtil.convertToDateViaInstant(startDate));
        calendarEvent.setEnd(CalendarEventsUtil.convertToDateViaInstant(endDate));

        TCalendarEvent entity = calendarToEntityConverter.apply(calendarEvent, new TCalendarEvent());

        if (calendarEvent.getId() != null) {
            Optional<TCalendarEvent> tCalendarEvent = calendarEventRepository.findById(calendarEvent.getId());

            if (tCalendarEvent.isPresent()) {
                log.debug("calendar {} is present", tCalendarEvent.get().getId());
                entity = calendarToEntityConverter.apply(calendarEvent, tCalendarEvent.get());
            }
        }

        if (calendarEvent.getDossier() != null) {
            Optional<TDossiers> dossiersOptional = dossierRepository.findById(calendarEvent.getDossier().getId());
            dossiersOptional.ifPresent(entity::setDossier);
        }
        if (calendarEvent.getContact() != null) {
            Optional<TClients> clientsOptional = clientRepository.findById(calendarEvent.getContact().getId());
            clientsOptional.ifPresent(entity::setContact);
        }
        if (calendarEvent.getUserId() != null) {
            Optional<TUsers> usersOptional = usersRepository.findById(calendarEvent.getUserId());
            usersOptional.ifPresent(entity::setTUsers);
        }
        entity.setCreationUser(username);
        entity.setApproved(approved); //all events created here are obviously approved

        if (calendarEvent.getParticipantsEmail() != null && !calendarEvent.getParticipantsEmail().isEmpty()) {
            if (entity.getTCalendarParticipants() == null) {
                entity.setTCalendarParticipants(new ArrayList<>());
            }

            entity.getTCalendarParticipants().clear();

            for (String email : calendarEvent.getParticipantsEmail()) {
                TCalendarParticipants tCalendarParticipants = new TCalendarParticipants();
                tCalendarParticipants.setTCalendarEvent(entity);
                tCalendarParticipants.setUserEmail(email);
                tCalendarParticipants.setCreUser(username);

                entity.getTCalendarParticipants().add(tCalendarParticipants);
            }
        }


        TCalendarEvent savedEvent = calendarEventRepository.save(entity);
    }

    /**
     * create slots for event
     *
     * @param start beginning of the event
     * @param end   end of the event
     * @return
     */
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
