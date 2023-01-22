package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TCalendarParticipants;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import com.ulegalize.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class EntityToCalendarConverter implements SuperTriConverter<TCalendarEvent, String, LawfirmCalendarEventDTO> {
    @Autowired
    private EntityToDossierConverter entityToDossierConverter;

    @Override
    public LawfirmCalendarEventDTO apply(TCalendarEvent entity, String language) {

        LawfirmCalendarEventDTO event = new LawfirmCalendarEventDTO();
        if (entity.getDossier() != null) {
            DossierDTO dossierDTO = entityToDossierConverter.apply(entity.getDossier(), EnumLanguage.fromshortCode(language));
            event.setDossier(dossierDTO);
            event.setDossierId(dossierDTO.getId());
            event.setDossierItem(new ItemLongDto(dossierDTO.getId(), dossierDTO.getLabel()));
        }

        event.setId(entity.getId());
        event.setTitle(entity.getTitle());
        event.setNote(entity.getNote());
        event.setLocation(entity.getLocation());
        event.setStart(entity.getStart());
        event.setEnd(entity.getEnd());
        event.setEventType(entity.getEventType());
        event.setPathFile(entity.getPathFile());
        event.setMicroText(entity.getMicroText());
        event.setAudioText(entity.getAudioText());
        event.setSpeechToTextActivated(entity.isSpeechToTextActivated());
        LocalDateTime startLocal = CalendarEventsUtil.convertToLocalDateTimeViaInstant(entity.getStart());
        LocalDateTime endLocal = CalendarEventsUtil.convertToLocalDateTimeViaInstant(entity.getEnd());
        if (DAYS.between(startLocal, endLocal) > 0) {
            event.setAllDay(true);
        }

        if (entity.getEventType() != null) {
            EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
            ItemEventDto itemStringDto = new ItemEventDto(entity.getEventType().getCode(),
                    Utils.getLabel(enumLanguage,
                            entity.getEventType().name(), null),
                    entity.getEventType().getColor());
            event.setEventTypeItem(itemStringDto);
        }
        event.setApproved(entity.isApproved());
        // check if it exists
        event.setRoomAttached(entity.getUrlRoom() != null && !entity.getUrlRoom().isEmpty());
        event.setUrlRoom(entity.getUrlRoom());
        if (entity.getTUsers() != null) {
            event.setUserId(entity.getTUsers().getId());
            String fullname = entity.getTUsers().getFullname() != null && !entity.getTUsers().getFullname().trim().isEmpty() ? entity.getTUsers().getFullname() : entity.getTUsers().getEmail();

            event.setUserItem(new ItemLongDto(entity.getTUsers().getId(), fullname));
        }
        if (entity.getTCalendarParticipants() != null) {
            event.setParticipantsEmail(entity.getTCalendarParticipants().stream().map(TCalendarParticipants::getUserEmail).collect(Collectors.toList()));
            event.setParticipantsEmailItem(entity.getTCalendarParticipants().stream()
                    .map(calendarParticipant -> new ItemStringDto(calendarParticipant.getUserEmail(), calendarParticipant.getUserEmail()))
                    .collect(Collectors.toList()));
        }
        event.setColor(entity.getEventType().getColor());

        return event;
    }

}
