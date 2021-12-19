package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.dto.CaseCreationDTO;
import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.enumeration.EnumDossierType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.kafka.producer.payment.IPaymentProducer;
import com.ulegalize.lawfirm.kafka.producer.transparency.ICaseProducer;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.LawyerDuty;
import com.ulegalize.lawfirm.model.LawyerDutyRequest;
import com.ulegalize.lawfirm.model.converter.CalendarToEntityConverter;
import com.ulegalize.lawfirm.model.converter.EntityToCalendarConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.*;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.service.v2.CalendarV2Service;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import com.ulegalize.lawfirm.utils.DriveUtils;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CalendarV2ServiceImpl implements CalendarV2Service {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${app.portal.url}")
    private String portalUrl;

    private final CalendarEventRepository calendarEventRepository;
    private final LawfirmUserRepository lawfirmUserRepository;
    private final TUsersRepository userRepository;
    private final ClientRepository clientRepository;
    private final LawfirmRepository lawfirmRepository;
    private final ICaseProducer caseProducer;
    private final IPaymentProducer paymentProducer;
    private final DossierRepository dossierRepository;

    private final EntityToCalendarConverter entityToCalendarConverter;
    private final CalendarToEntityConverter calendarToEntityConverter;
    private final MailService mailService;

    public CalendarV2ServiceImpl(CalendarEventRepository calendarEventRepository,
                                 LawfirmUserRepository lawfirmUserRepository,
                                 EntityToCalendarConverter entityToCalendarConverter,
                                 TUsersRepository userRepository,
                                 ClientRepository clientRepository,
                                 LawfirmRepository lawfirmRepository,
                                 ICaseProducer caseProducer,
                                 IPaymentProducer paymentProducer, DossierRepository dossierRepository,
                                 CalendarToEntityConverter calendarToEntityConverter, MailService mailService) {
        this.calendarEventRepository = calendarEventRepository;
        this.lawfirmUserRepository = lawfirmUserRepository;
        this.entityToCalendarConverter = entityToCalendarConverter;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.caseProducer = caseProducer;
        this.paymentProducer = paymentProducer;
        this.dossierRepository = dossierRepository;
        this.calendarToEntityConverter = calendarToEntityConverter;
        this.mailService = mailService;
    }


    @Override
    public List<LawfirmCalendarEventDTO> findAllByUserId(Long userId, Long dossierId, Date start, Date end, List<String> eventTypesSelected) throws LawfirmBusinessException {
        log.debug("Entering findAllByUserId with user id {} , start {}, end {} and event types  {}", userId, start, end, eventTypesSelected);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TCalendarEvent> calEvents;

        // check if it's in the same vc
        if (userId != 0) {
            Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), userId);

            if (lawfirmUsers.isEmpty()) {
                log.warn("Lawfirm {} is not present for this user id {}", lawfirmToken.getVcKey(), userId);
                return new ArrayList<>();
            }
        }
        List<EnumCalendarEventType> enumCalendarEventTypeList = eventTypesSelected.stream().map(EnumCalendarEventType::fromCode).collect(Collectors.toList());
        // start date and end date must be midnight in order to have all day
        if (dossierId != null) {
            calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDateAndDossierId(userId, dossierId, lawfirmToken.getVcKey(), start, end, enumCalendarEventTypeList);
        } else {
            calEvents = calendarEventRepository.findCalendarEventsByUserIdAndDate(userId, lawfirmToken.getVcKey(), start, end, enumCalendarEventTypeList);
        }
        log.debug("Found {} events ", calEvents.size());

        return entityToCalendarConverter.convertToList(calEvents, lawfirmToken.getLanguage());
    }

    @Override
    public LawfirmCalendarEventDTO approveLawfirmCalendarEvent(Long eventId) throws ResponseStatusException {
        log.debug("Entering approveLawfirmCalendarEvent(eventId : {}", eventId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<LawfirmEntity> lawfirm = lawfirmRepository.findById(lawfirmToken.getVcKey().toUpperCase());
        if (lawfirm.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lawfirm with key: {" + lawfirmToken.getVcKey() + "} not found.");
        }

        Optional<TCalendarEvent> eventEntityOptional = calendarEventRepository.findById(eventId);
        if (eventEntityOptional.isEmpty()) {
            log.warn("Event id {} not found ", eventId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not found");
        }
        if (eventEntityOptional.get().getContact() == null || eventEntityOptional.get().getContact().getF_email() == null || eventEntityOptional.get().getContact().getF_email().isEmpty()) {
            String email = eventEntityOptional.get().getContact() != null ? eventEntityOptional.get().getContact().getF_email() : "";
            log.warn("event {} Client email {} not found ", eventEntityOptional.get().getId(), email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client email not found ");
        }
        if (eventEntityOptional.get().getTUsers() == null && eventEntityOptional.get().getVcKey() == null) {
            log.warn("event {} user nor vckey found ", eventEntityOptional.get().getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "event {} user nor vckey found");
        }

        // remove permanence at this date and for this user
        Long userId = eventEntityOptional.get().getTUsers() != null ? eventEntityOptional.get().getTUsers().getId() : null;

        log.debug("remove the PERM for the event from {} to {} for user id {}", eventEntityOptional.get().getStart(), eventEntityOptional.get().getEnd(), userId);
        List<TCalendarEvent> calendarEventList = calendarEventRepository.getCalendarEventByStartEndUserIdType(eventEntityOptional.get().getStart(), eventEntityOptional.get().getEnd(), userId, EnumCalendarEventType.PERM);
        // remove the first
        if (calendarEventList != null && !calendarEventList.isEmpty()) {
            log.info("calendar event to remove id {}", calendarEventList.stream().map(TCalendarEvent::getId).collect(Collectors.toList()));
            calendarEventRepository.deleteAll(calendarEventList);
        }

        TCalendarEvent eventEntity = eventEntityOptional.get();

        eventEntity.setApproved(true);
        eventEntity.setUpdateDate(new Date());
        eventEntity.setUpdateUser(lawfirmToken.getUsername());

        TCalendarEvent savedEvent = calendarEventRepository.save(eventEntity);

        LawfirmUsers lawyer = lawfirm.get().getLawfirmUsers().stream()
                .filter(user -> eventEntity.getTUsers() != null && user.getUser().getId().equals(eventEntity.getTUsers().getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while filtering user"));

        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = entityToCalendarConverter.apply(savedEvent, lawfirmToken.getLanguage());
        if (!activeProfile.equalsIgnoreCase("integrationtest")) {

            // create cas juridique
            createCasLawfirm(lawfirmCalendarEventDTO, lawfirmToken);

            String language = lawyer.getUser().getLanguage() != null ? lawyer.getUser().getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();

            // mail to contact
            mailService.sendMail(EnumMailTemplate.MAILAPPOINTMENTCONFIRMEDTEMPLATE,
                    EmailUtils.prepareContextForAppointmentConfirmedEmail(language, savedEvent.getContact().getF_email(), savedEvent, lawyer, portalUrl, lawfirmToken.getClientFrom()),
                    language,
                    CalendarEventsUtil.convertToZoneDateTimeViaInstant(savedEvent.getStart()),
                    CalendarEventsUtil.convertToZoneDateTimeViaInstant(savedEvent.getEnd())
                    , true, false, eventEntity.getRoomName());

            // mail to mediator
            String emailTo;
            if (eventEntityOptional.get().getTUsers() != null) {
                emailTo = eventEntityOptional.get().getTUsers().getEmail();
            } else {
                Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(savedEvent.getVcKey());

                emailTo = lawfirmDTOOptional.get().getEmail();
            }
            mailService.sendMail(EnumMailTemplate.MAILAPPOINTMENTCONFIRMEDTEMPLATE,
                    EmailUtils.prepareContextForAppointmentConfirmedEmail(language, emailTo, savedEvent, lawyer, portalUrl, lawfirmToken.getClientFrom()),
                    language,
                    CalendarEventsUtil.convertToZoneDateTimeViaInstant(savedEvent.getStart()),
                    CalendarEventsUtil.convertToZoneDateTimeViaInstant(savedEvent.getEnd())
                    , true, true, eventEntity.getRoomName());
        }
        return lawfirmCalendarEventDTO;
    }

    @Override
    public void createLawfirmCalendarEvent(LawfirmCalendarEventDTO calendarEvent) {
        log.debug("Entering createLawfirmCalendarEvent {} ", calendarEvent);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // BR when creating
        commonRuleEvent(calendarEvent);

        createOrUpdateCalendarEvent(calendarEvent, lawfirmToken.getVcKey());
    }

    @Override
    public void updateLawfirmCalendarEvent(Long eventId, LawfirmCalendarEventDTO calendarEvent) {
        log.debug("Entering saveLawfirmCalendarEvent {} ", eventId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<TCalendarEvent> eventEntityOptional = calendarEventRepository.findById(eventId);
        if (eventEntityOptional.isEmpty()) {
            log.warn("Event id {} not found ", eventId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not found");
        }

        // BR when updating
        commonRuleEvent(calendarEvent);

        createOrUpdateCalendarEvent(calendarEvent, lawfirmToken.getVcKey());
    }

    @Override
    public Long deleteEvent(Long eventId) {
        Optional<TCalendarEvent> eventEntityOptional = calendarEventRepository.findById(eventId);
        if (eventEntityOptional.isEmpty()) {
            log.warn("Event id {} not found ", eventId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not found");
        }
        calendarEventRepository.delete(eventEntityOptional.get());

        return eventId;
    }

    @Override
    public Page<LawfirmCalendarEventDTO> countUnapprovedLawfirmCalendar(Long userId, EnumCalendarEventType eventType) {
        log.debug("Entering findAllByUserId with user id {} ", userId);
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userIdToSearch;
        // check if it's in the same vc
        ;
        if (userId != null) {
            userIdToSearch = userId;
        } else {
            log.debug("User {} is present into vckey ", lawfirmToken.getUserId());
            userIdToSearch = lawfirmToken.getUserId();
        }

        Optional<LawfirmUsers> lawfirmUsers = lawfirmUserRepository.findLawfirmUsersByVcKeyAndUserId(lawfirmToken.getVcKey(), userIdToSearch);

        if (lawfirmUsers.isPresent()) {
            log.debug("User {} is present into vckey {}", userIdToSearch, lawfirmToken.getVcKey());
            Page<TCalendarEvent> calEvents = calendarEventRepository.findByUserAndVcKey(userIdToSearch, lawfirmToken.getVcKey(), eventType, Pageable.unpaged());
            log.debug("Found {} events ", calEvents);
            List<LawfirmCalendarEventDTO> calendarEventDTOS = entityToCalendarConverter.convertToList(calEvents.getContent(), lawfirmToken.getLanguage());

            return new PageImpl<>(calendarEventDTOS, Pageable.unpaged(), calEvents.getTotalElements());
        } else {
            log.warn("Lawfirm {} is not present for this user id {}", lawfirmToken.getVcKey(), userId);
            return Page.empty();
        }
    }

    @Override
    public LawyerDuty newLawyerAppointment(String lawyerAlias, LawyerDutyRequest appointment) throws
            LawfirmBusinessException {
        log.debug("newLawyerAppointment(lawyer alias : {}", lawyerAlias);
        Optional<TUsers> userOptional = userRepository.findPublicUserByAliasPublic(lawyerAlias);

        if (userOptional.isEmpty()) {
            throw new LawfirmBusinessException("No lawyer found for this alias");
        }

        //search for an event matching start/end/lawyer
        List<TCalendarEvent> events = calendarEventRepository.findCalendarEventsByUserId(userOptional.get().getId());
        log.debug("Event from db {}", events);

        LocalDateTime startLocal = CalendarEventsUtil.convertToLocalDateTimeViaInstant(appointment.getStart());
        LocalDateTime endLocal = CalendarEventsUtil.convertToLocalDateTimeViaInstant(appointment.getEnd());

        Optional<TCalendarEvent> eventOptional = events.stream().filter(e -> {
            LocalDateTime start = CalendarEventsUtil.convertToLocalDateTimeViaInstant(e.getStart());
            LocalDateTime end = CalendarEventsUtil.convertToLocalDateTimeViaInstant(e.getEnd());

            return start.equals(startLocal)
                    && end.equals(endLocal)
                    && EnumCalendarEventType.PERM.equals(e.getEventType());
        }).findFirst();

        if (!eventOptional.isPresent()) {
            log.warn("Event is null with start {}, end {}, user id {} , cal type {}", appointment.getStart(), appointment.getEnd(), userOptional.get().getId(), EnumCalendarEventType.PERM);
            throw new LawfirmBusinessException("");
        }
        TCalendarEvent event = eventOptional.get();
//        log.debug("Found an event {} " , event);

        //create a new RDV event
        TCalendarEvent appointmentEvent = new TCalendarEvent();
        appointmentEvent.setEventType(EnumCalendarEventType.RDV);
        appointmentEvent.setTitle(EnumCalendarEventType.RDV.name() + " " + appointment.getFirstName() + " " + appointment.getLastName());
        appointmentEvent.setCreationDate(new Date());
        appointmentEvent.setCreationUser(appointment.getLastName());
        appointmentEvent.setNote(appointment.getNote());
        appointmentEvent.setTUsers(userOptional.get());
        appointmentEvent.setStart(appointment.getStart());
        appointmentEvent.setEnd(appointment.getEnd());

        TCalendarParticipants tCalendarParticipants = new TCalendarParticipants();
        tCalendarParticipants.setTCalendarEvent(appointmentEvent);
        tCalendarParticipants.setUserEmail(userOptional.get().getEmail());
        tCalendarParticipants.setCreUser(appointment.getLastName());

        appointmentEvent.setTCalendarParticipants(new ArrayList<>());
        appointmentEvent.getTCalendarParticipants().add(tCalendarParticipants);

        log.debug("New event {}", appointmentEvent);

        //search for an existing contact
        // 1: in lawfirm or lawyer contacts
        // 2: otherwise create it

        //1 : search for the client in destination lawfirm
        List<TClients> tClientsList = clientRepository.findByAliasPublic(lawyerAlias, appointment.getEmail());
        TClients lawfirmClient = null;
        //create a new contact to link to the appointment
        if (tClientsList == null || tClientsList.isEmpty()) {
            //3: create one in all lawfirm linked to user
            lawfirmClient = new TClients();
            lawfirmClient.setF_nom(appointment.getLastName());
            lawfirmClient.setF_prenom(appointment.getFirstName());
            lawfirmClient.setF_email(appointment.getEmail());
            lawfirmClient.setF_tel(appointment.getPhone());
            lawfirmClient.setClient_type(EnumClientType.NATURAL_PERSON);
            lawfirmClient.setId_lg(EnumLanguage.FR.getShortCode());
//            lawfirmClient.setVc_key(null);
            // link to user id
            lawfirmClient.setUser_id(userOptional.get().getId());
            // use the last one to send an email

            if (lawfirmClient.getVirtualcabClientList() == null) {
                lawfirmClient.setVirtualcabClientList(new ArrayList<>());
            }

            for (LawfirmUsers lawfirmUsers : userOptional.get().getLawfirmUsers()) {
                VirtualcabClient virtualcabClient = new VirtualcabClient();
                virtualcabClient.setTClients(lawfirmClient);
                virtualcabClient.setLawfirm(lawfirmUsers.getLawfirm());
                virtualcabClient.setCreUser("public");
                lawfirmClient.getVirtualcabClientList().add(virtualcabClient);
            }

            lawfirmClient = clientRepository.save(lawfirmClient);

        } else {
            lawfirmClient = tClientsList.get(0);
        }

        Optional<LawfirmUsers> lawfirmUsers = userOptional.get().getLawfirmUsers().stream().findFirst();

        // use last client we should have a contact not link to a lawfirm
        appointmentEvent.setContact(lawfirmClient);

        calendarEventRepository.save(appointmentEvent);

        if (!activeProfile.equalsIgnoreCase("integrationtest") && lawfirmUsers.isPresent()) {
            String language = userOptional.get().getLanguage() != null ? userOptional.get().getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();
            mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILAPPOINTMENTREGISTEREDTEMPLATE, EmailUtils.prepareContextForRegisteredAppointmentEmail(language, appointment, appointmentEvent.getEventType(), lawfirmUsers.get(), portalUrl, "workspace"), language);
            mailService.sendMailWithoutMeetingAndIcs(EnumMailTemplate.MAILNEWAPPOINTMENTREQUESTTEMPLATE, EmailUtils.prepareContextForNewAppointmentEmail(language, appointment, appointmentEvent.getEventType(), lawfirmUsers.get(), portalUrl, "workspace"), language);
        }
        Long userId = event.getTUsers() != null ? event.getTUsers().getId() : null;

        return new LawyerDuty(event.getId(), event.getStart(), event.getEnd(), event.getNote(), userId);
    }

    /**
     * common rules for updatinf and creating an event
     *
     * @param calendarEvent
     */
    private void commonRuleEvent(LawfirmCalendarEventDTO calendarEvent) {
        if (calendarEvent.getStart() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "start date cannot be empty");
        }

        if (calendarEvent.getEventType() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event type cannot be before start date");
        }

        if (!calendarEvent.getEventType().equals(EnumCalendarEventType.TASK)) {
            if (calendarEvent.getEnd() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "end date cannot be empty");
            }
            if (calendarEvent.getEnd().before(calendarEvent.getStart())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "End date cannot be before start date");
            }
        }
    }

    /**
     * create or update a calendar event
     *
     * @param calendarEvent
     * @param vcKey
     */
    private void createOrUpdateCalendarEvent(LawfirmCalendarEventDTO calendarEvent, String vcKey) {
        LawfirmToken lawfirmToken = (LawfirmToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Entering createOrUpdateCalendarEvent username {} ", lawfirmToken.getUsername());
        ZonedDateTime startime = CalendarEventsUtil.convertToZoneDateTimeViaInstant(calendarEvent.getStart());
        ZonedDateTime endtime = CalendarEventsUtil.convertToZoneDateTimeViaInstant(calendarEvent.getEnd());

        log.debug("startime {} ", startime);
        log.debug("endtime {} ", endtime);
        log.debug("calendar type {} ", calendarEvent.getEventType());

        switch (calendarEvent.getEventType()) {
            case PERM:
                // create by slot of 1/2h
                List<ZonedDateTime> slots = getSlots(startime, endtime, CalendarEventsUtil.SLOT_EVENT);

                createEventBySlot(lawfirmToken.getUsername(), lawfirmToken.getLanguage(), vcKey, slots, calendarEvent, CalendarEventsUtil.SLOT_EVENT, lawfirmToken.getClientFrom());

                break;
            case RDV:
                createEvent(lawfirmToken.getUsername(), lawfirmToken.getLanguage(), vcKey, startime, endtime, calendarEvent, calendarEvent.isApproved(), lawfirmToken.getClientFrom());
                break;
            case AUD:
            case OTH:
                createEvent(lawfirmToken.getUsername(), lawfirmToken.getLanguage(), vcKey, startime, endtime, calendarEvent, true, lawfirmToken.getClientFrom());
                break;
            case TASK:
                createEvent(lawfirmToken.getUsername(), lawfirmToken.getLanguage(), vcKey, startime, null, calendarEvent, true, lawfirmToken.getClientFrom());

                break;
            case RECORD:
                // this the storing folder
                calendarEvent.setPathFile(DriveUtils.TMP_PATH + "/" + calendarEvent.getPathFile());
                createEvent(lawfirmToken.getUsername(), lawfirmToken.getLanguage(), vcKey, startime, null, calendarEvent, true, lawfirmToken.getClientFrom());

                paymentProducer.sendPayment(lawfirmToken, calendarEvent);

                break;
            default:
                break;
        }
    }

    private void createCasLawfirm(LawfirmCalendarEventDTO lawfirmCalendarEventDTO, LawfirmToken lawfirmToken) throws ResponseStatusException {
        CaseCreationDTO caseCreationDTO = new CaseCreationDTO();
        caseCreationDTO.setDossier(lawfirmCalendarEventDTO.getDossier());
        caseCreationDTO.setNote(lawfirmCalendarEventDTO.getNote());
        caseCreationDTO.setContactSummaryList(new ArrayList<>());
        caseCreationDTO.getContactSummaryList().add(lawfirmCalendarEventDTO.getContact());
        if (lawfirmCalendarEventDTO.getDossier() != null && lawfirmCalendarEventDTO.getDossier().getType() != null) {
            caseCreationDTO.setAssistanceJuridique(lawfirmCalendarEventDTO.getDossier().getType().equals(EnumDossierType.BA));
        }
        caseProducer.createCaseMessage(caseCreationDTO, lawfirmToken);
    }

    /**
     * @param username
     * @param vcKey
     * @param calendarEvent
     * @param slotMinutes   in minutes
     * @param clientFrom
     */
    private void createEventBySlot(String username, String language, String vcKey, List<ZonedDateTime> slots, LawfirmCalendarEventDTO calendarEvent, int slotMinutes, String clientFrom) {

        for (ZonedDateTime slot : slots) {
            createEvent(username, language, vcKey, slot, slot.plusMinutes(slotMinutes), calendarEvent, true, clientFrom);
        }
    }

    /**
     * @param username
     * @param vcKey
     * @param startDate
     * @param endDate
     * @param calendarEvent
     * @param approved
     * @param clientFrom
     */
    private void createEvent(String username, String language, String vcKey, ZonedDateTime startDate, ZonedDateTime endDate, LawfirmCalendarEventDTO calendarEvent, boolean approved, String clientFrom) {
        log.debug("Entering createEvent String username {}, LocalDateTime startDate {}, LocalDateTime endDate {}, LawfirmCalendarEvent calendarEvent {}, boolean approved {}", username, startDate, endDate, calendarEvent, approved);

        // recalculate the start and end date
        calendarEvent.setStart(CalendarEventsUtil.convertToDateViaInstant(startDate));

        if (endDate != null) {
            calendarEvent.setEnd(CalendarEventsUtil.convertToDateViaInstant(endDate));
        } else {
            calendarEvent.setEnd(CalendarEventsUtil.convertToDateViaInstant(startDate));

        }
        TCalendarEvent entity = null;

        boolean calendarCreated = false;
        if (calendarEvent.getId() != null) {
            Optional<TCalendarEvent> tCalendarEvent = calendarEventRepository.findById(calendarEvent.getId());
            if (tCalendarEvent.isPresent()) {
                log.debug("calendar {} is present", tCalendarEvent.get().getId());
                entity = calendarToEntityConverter.apply(calendarEvent, tCalendarEvent.get());
                calendarCreated = true;
            }
        }

        if (!calendarCreated) {
            log.debug("calendar new event");
            entity = calendarToEntityConverter.apply(calendarEvent, new TCalendarEvent());

            entity.setCreationUser(username);
        }

        if (calendarEvent.getDossierId() != null) {
            Optional<TDossiers> dossiersOptional = dossierRepository.findById(calendarEvent.getDossierId());
            dossiersOptional.ifPresent(entity::setDossier);
        }
        if (calendarEvent.getContactId() != null) {
            Optional<TClients> clientsOptional = clientRepository.findById(calendarEvent.getContactId());
            clientsOptional.ifPresent(entity::setContact);
        }
        if (calendarEvent.getUserId() != null && calendarEvent.getUserId() != 0) {
            Optional<TUsers> usersOptional = userRepository.findById(calendarEvent.getUserId());
            usersOptional.ifPresent(entity::setTUsers);
        } else {
            // if it's empty create for vkey
            entity.setVcKey(vcKey);
        }
        entity.setNote(calendarEvent.getNote());
        entity.setUpdateUser(username);
        entity.setApproved(approved); //all events created here are obviously approved

        if (!calendarCreated) {
            log.debug("Room name starting");

            String roomName = entity.getTUsers() != null ? String.valueOf(entity.getTUsers().getId()) : entity.getVcKey();

            // if iit's null generate a room at random
            if (roomName == null) {
                log.debug("room generated automatically ");
                roomName = RandomStringUtils.randomAlphabetic(8);
            }
            log.debug("room name generated {} ", roomName);

            entity.setRoomName(roomName);
        }

        log.debug("Calendar Participants starting");

        // remove duplicate email
        List<String> participantEmailList = calendarEvent.getParticipantsEmail() != null ? calendarEvent.getParticipantsEmail().stream()
                .distinct()
                .collect(Collectors.toList()) : new ArrayList<>();

        log.info("Participants email list {}", participantEmailList);

        // removed cancel
        List<String> emailRemoved = new ArrayList<>();

        // First in the DB list
        if (!CollectionUtils.isEmpty(entity.getTCalendarParticipants())) {
            for (TCalendarParticipants calendarParticipants : entity.getTCalendarParticipants()) {
                // list from UI (user selected)
                boolean participantFoundInUI = participantEmailList.stream().anyMatch(email -> email.equalsIgnoreCase(calendarParticipants.getUserEmail()));

                log.debug("Participant email {} is found in db {}", calendarParticipants.getUserEmail(), participantFoundInUI);

                // if email from db is not found in UI , it's a removed email
                if (!participantFoundInUI) {
                    emailRemoved.add(calendarParticipants.getUserEmail());
                }
            }
        } else if (entity.getTCalendarParticipants() == null) {
            entity.setTCalendarParticipants(new ArrayList<>());
        }

        // added cancel
        List<String> emailAdded = new ArrayList<>();

        // First in UI list
        if (!participantEmailList.isEmpty()) {
            for (String email : participantEmailList) {
                // list from UI (user selected)
                boolean participantFoundInDb = entity.getTCalendarParticipants() != null && entity.getTCalendarParticipants().stream().anyMatch(tCalendarParticipants -> tCalendarParticipants.getUserEmail().equalsIgnoreCase(email));

                log.debug("Participant email {} is found in db {}", email, participantFoundInDb);
                // if email is not found in db , it's a new one
                if (!participantFoundInDb) {
                    emailAdded.add(email);
                }
            }
        }

        if (!participantEmailList.isEmpty()) {
            List<TCalendarParticipants> participantsList = entity.getTCalendarParticipants();
            for (String email : emailAdded) {
                TCalendarParticipants tCalendarParticipants = new TCalendarParticipants();
                tCalendarParticipants.setTCalendarEvent(entity);
                tCalendarParticipants.setUserEmail(email);
                tCalendarParticipants.setCreUser(username);

                participantsList.add(tCalendarParticipants);
            }
            entity.getTCalendarParticipants().addAll(participantsList);

            for (String email : emailRemoved) {
                List<TCalendarParticipants> tCalendarParticipantsToRemoved = entity.getTCalendarParticipants().stream()
                        .filter(cal -> cal.getUserEmail().equalsIgnoreCase(email))
                        .collect(Collectors.toList());

                entity.getTCalendarParticipants().removeAll(tCalendarParticipantsToRemoved);
            }
        } else {
            entity.getTCalendarParticipants().clear();
        }

        TCalendarEvent savedEvent = calendarEventRepository.save(entity);

        calendarEvent.setId(savedEvent.getId());

        // send email cancellation and enroll email
        log.debug("Send email cancellation and enroll email");

        log.debug("start Notification added email {}", emailAdded);
        String emailContact = "";
        String phoneContact = "";

        if (savedEvent.getVcKey() != null && !savedEvent.getVcKey().isEmpty()) {
            log.debug("Vc key {} contact person", savedEvent.getVcKey());
            Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(savedEvent.getVcKey());
            if (lawfirmDTOOptional.isPresent()) {
                emailContact = lawfirmDTOOptional.get().getEmail();
                phoneContact = lawfirmDTOOptional.get().getPhoneNumber();
            }
        } else {
            emailContact = savedEvent.getTUsers().getEmail();
        }

        boolean roomAttached = calendarEvent.getEventType().equals(EnumCalendarEventType.RDV);

//        // send notification to the lawyer
        mailService.sendMail(EnumMailTemplate.MAILAPPOINTMENT_ADDED_NOTIFICATION,
                EmailUtils.prepareContextNotificationEmail(language, savedEvent, CalendarEventsUtil.convertToDateViaInstant(startDate), CalendarEventsUtil.convertToDateViaInstant(endDate), emailContact, phoneContact, portalUrl, emailContact, clientFrom),
                language,
                startDate,
                endDate, roomAttached, true, savedEvent.getRoomName());


        for (String email : emailAdded) {
            // send notification to the participants
            mailService.sendMail(EnumMailTemplate.MAILAPPOINTMENT_ADDED_NOTIFICATION,
                    EmailUtils.prepareContextNotificationEmail(language, savedEvent, CalendarEventsUtil.convertToDateViaInstant(startDate), CalendarEventsUtil.convertToDateViaInstant(endDate), emailContact, phoneContact, portalUrl, email, clientFrom),
                    language,
                    startDate,
                    endDate, roomAttached, false, savedEvent.getRoomName());
        }
        log.debug("start Notification cancel email {}", emailRemoved);

        for (String email : emailRemoved) {
            // send notification to the participants
            mailService.sendMailWithoutMeeting(EnumMailTemplate.MAILAPPOINTMENT_CANCEL_NOTIFICATION,
                    EmailUtils.prepareContextNotificationEmail(language, savedEvent, CalendarEventsUtil.convertToDateViaInstant(startDate), CalendarEventsUtil.convertToDateViaInstant(endDate), emailContact, phoneContact, portalUrl, email, clientFrom),
                    language,
                    startDate,
                    endDate);
        }
        log.info("End Notification");

    }

    /**
     * create slots for event
     *
     * @param start
     * @param end
     * @param slotMinutes
     * @return
     */
    private List<ZonedDateTime> getSlots(ZonedDateTime start, ZonedDateTime end, int slotMinutes) {
        List<ZonedDateTime> slots = new ArrayList<>();
        ZonedDateTime startime = start;
        while (startime.isBefore(end)) {
            slots.add(startime);
            // Prepare for the next loop.
            startime = startime.plusMinutes(slotMinutes);
        }

        return slots;

    }
}
