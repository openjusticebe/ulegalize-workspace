package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToCalendarConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.repository.CalendarEventRepository;
import com.ulegalize.lawfirm.repository.ClientRepository;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.rest.LawfirmTransparencyApi;
import com.ulegalize.lawfirm.service.CalendarService;
import com.ulegalize.lawfirm.service.MailService;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import com.ulegalize.lawfirm.utils.EmailUtils;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CalendarServiceImpl implements CalendarService {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${app.portal.url}")
    private String portalUrl;

    private CalendarEventRepository calendarEventRepository;
    private TUsersRepository userRepository;
    private ClientRepository clientRepository;
    private LawfirmRepository lawfirmRepository;
    private LawfirmTransparencyApi lawfirmTransparencyApi;

    private EntityToCalendarConverter entityToCalendarConverter;
    private MailService mailService;

    public CalendarServiceImpl(CalendarEventRepository calendarEventRepository,
                               EntityToCalendarConverter entityToCalendarConverter,
                               TUsersRepository userRepository,
                               ClientRepository clientRepository,
                               LawfirmRepository lawfirmRepository,
                               LawfirmTransparencyApi lawfirmTransparencyApi, MailService mailService) {
        this.calendarEventRepository = calendarEventRepository;
        this.entityToCalendarConverter = entityToCalendarConverter;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.lawfirmRepository = lawfirmRepository;
        this.mailService = mailService;
        this.lawfirmTransparencyApi = lawfirmTransparencyApi;
    }


    @Override
    public List<LawfirmCalendarEventDTO> findAllByUserId(Long userId) throws LawfirmBusinessException {

        List<TCalendarEvent> calEvents = calendarEventRepository.findCalendarEventsByUserId(userId);

        log.debug("Found {} events ", calEvents.size());

        return entityToCalendarConverter.convertToList(calEvents, EnumLanguage.FR.getShortCode());
    }

    @Override
    public LawfirmCalendarEventDTO approveLawfirmCalendarEvent(LawfirmToken lawfirmToken, Long eventId) throws
            ResponseStatusException {
        log.debug("Entering approveLawfirmCalendarEvent(lawfirmToken : {}", lawfirmToken);

        Optional<LawfirmEntity> lawfirm = lawfirmRepository.findById(lawfirmToken.getVcKey().toUpperCase());
        if (!lawfirm.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lawfirm with key: {" + lawfirmToken.getVcKey() + "} not found.");
        }

        Optional<TCalendarEvent> eventEntityOptional = calendarEventRepository.findById(eventId);
        if (!eventEntityOptional.isPresent()) {
            log.warn("Event id {} not found ", eventId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event not found");
        }
        if (eventEntityOptional.get().getContact() == null || eventEntityOptional.get().getContact().getF_email() == null || eventEntityOptional.get().getContact().getF_email().isEmpty()) {
            String email = eventEntityOptional.get().getContact() != null ? eventEntityOptional.get().getContact().getF_email() : "";
            log.warn("event {} Client email {} not found ", eventEntityOptional.get().getId(), email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client email not found ");
        }

        // remove permanence at this date and for this user
        Long userId = eventEntityOptional.get().getTUsers() != null ? eventEntityOptional.get().getTUsers().getId() : null;

        log.debug("remove the PERM for the event from {} to {} for user id {}", eventEntityOptional.get().getStart(), eventEntityOptional.get().getEnd(), userId);
        List<TCalendarEvent> calendarEventList = calendarEventRepository.getCalendarEventByStartEndUserIdType(eventEntityOptional.get().getStart(), eventEntityOptional.get().getEnd(), userId, EnumCalendarEventType.PERM);
        // remove the first
        if (calendarEventList != null && !calendarEventList.isEmpty()) {
            log.info("calendar event to remove id {}", calendarEventList.get(0).getId());
            calendarEventRepository.delete(calendarEventList.get(0));
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

        LawfirmCalendarEventDTO lawfirmCalendarEventDTO = entityToCalendarConverter.apply(savedEvent, EnumLanguage.FR.getShortCode());
        if (!activeProfile.equalsIgnoreCase("integrationtest")) {

            // create cas juridique
            createCasLawfirm(lawfirmCalendarEventDTO, lawfirmToken.getToken());

            String language = lawyer.getUser().getLanguage() != null ? lawyer.getUser().getLanguage().toLowerCase() : EnumLanguage.FR.getShortCode();
            mailService.sendMail(EnumMailTemplate.MAILAPPOINTMENTCONFIRMEDTEMPLATE,
                    EmailUtils.prepareContextForAppointmentConfirmedEmail(language, savedEvent, lawyer, portalUrl, lawfirmToken.getClientFrom()),
                    language,
                    CalendarEventsUtil.convertToZoneDateTimeViaInstant(savedEvent.getStart()),
                    CalendarEventsUtil.convertToZoneDateTimeViaInstant(savedEvent.getEnd()));
        }
        return lawfirmCalendarEventDTO;
    }

    private void createCasLawfirm(LawfirmCalendarEventDTO lawfirmCalendarEventDTO, String token) throws
            ResponseStatusException {
        lawfirmTransparencyApi.createCasFromAgenda(lawfirmCalendarEventDTO, token);
    }
}
