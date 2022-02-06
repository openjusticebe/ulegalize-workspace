package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TCalendarParticipants;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import com.ulegalize.utils.DossiersUtils;
import com.ulegalize.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityToCalendarConverter implements SuperTriConverter<TCalendarEvent, String, LawfirmCalendarEventDTO> {
    @Autowired
    private EntityToDossierConverter entityToDossierConverter;
    @Autowired
    private EntityToContactSummaryConverter entityToContactSummaryConverter;

    @Override
    public LawfirmCalendarEventDTO apply(TCalendarEvent entity, String language) {

        LawfirmCalendarEventDTO event = new LawfirmCalendarEventDTO();
        if (entity.getDossier() != null) {
            DossierDTO ds = entityToDossierConverter.apply(entity.getDossier(), EnumLanguage.fromshortCode(language));
            event.setDossier(ds);
            event.setDossierId(ds.getId());
            event.setDossierItem(new ItemLongDto(ds.getId(), DossiersUtils.getDossierLabelItem(entity.getDossier().getYear_doss(), entity.getDossier().getNum_doss())));
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

        if (entity.getEventType() != null) {
            EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
            ItemEventDto itemStringDto = new ItemEventDto(entity.getEventType().getCode(),
                    Utils.getLabel(enumLanguage,
                            entity.getEventType().getLabelFr(),
                            entity.getEventType().getLabelEn(),
                            entity.getEventType().getLabelNl()), entity.getEventType().getColor());
            event.setEventTypeItem(itemStringDto);
        }
        event.setApproved(entity.isApproved());
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
