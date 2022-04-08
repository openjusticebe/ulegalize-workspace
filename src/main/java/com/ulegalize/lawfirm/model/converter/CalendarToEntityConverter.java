package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.dto.LawfirmCalendarEventDTO;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.springframework.stereotype.Component;

@Component
public class CalendarToEntityConverter implements SuperTriConverter<LawfirmCalendarEventDTO, TCalendarEvent, TCalendarEvent> {

    @Override
    public TCalendarEvent apply(LawfirmCalendarEventDTO event, TCalendarEvent entity) {

        entity.setId(event.getId());
        if (event.getDossier() != null) {
            TDossiers dossier = new TDossiers();
            dossier.setIdDoss(event.getDossier().getId());
            entity.setDossier(dossier);
        }

        if (event.getDossierId() != null) {
            TDossiers dossier = new TDossiers();
            dossier.setIdDoss(event.getDossierId());
            entity.setDossier(dossier);
        }

        entity.setNote(event.getNote());
        entity.setTitle(event.getTitle());
        entity.setLocation(event.getLocation());
        entity.setStart(event.getStart());
        entity.setEnd(event.getEnd());
        entity.setEventType(event.getEventType());
        entity.setApproved(event.isApproved());
        entity.setPathFile(event.getPathFile());
        entity.setMicroText(event.getMicroText());
        entity.setAudioText(event.getAudioText());
        entity.setSpeechToTextActivated(event.isSpeechToTextActivated());
        entity.setUrlRoom(event.getUrlRoom());

        return entity;
    }

}
