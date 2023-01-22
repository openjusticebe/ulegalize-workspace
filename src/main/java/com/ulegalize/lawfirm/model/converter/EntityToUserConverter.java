package com.ulegalize.lawfirm.model.converter;

import com.ulegalize.LawyerDuty;
import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.CalendarEventRepository;
import com.ulegalize.lawfirm.utils.CalendarEventsUtil;
import com.ulegalize.lawfirm.utils.SuperTriConverter;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EntityToUserConverter implements SuperTriConverter<TUsers, Boolean, LawyerDTO> {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Override
    public LawyerDTO apply(TUsers entity, Boolean wihtCalendarEvents) {
        LawyerDTO lawyer = new LawyerDTO();
        lawyer.setId(entity.getId());
        lawyer.setAlias(entity.getAliasPublic());
        lawyer.setFullName(entity.getFullname());
        lawyer.setInitials(entity.getInitiales());
        lawyer.setColor(entity.getColor());
        String biography = StringEscapeUtils.unescapeHtml4(entity.getBiography());
        lawyer.setBiography(biography);
        String specialities = StringEscapeUtils.unescapeHtml4(entity.getSpecialities());
        lawyer.setSpecialities(specialities);
        lawyer.setPicture(entity.getAvatar());
        lawyer.setEmail(entity.getEmail());
        lawyer.setUserEmailItem(new ItemStringDto(entity.getEmail(), entity.getEmail()));
        lawyer.setValid(entity.getValid());
        lawyer.setIdUser(entity.getIdUser());
        lawyer.setNotification(entity.getNotification());
        lawyer.setLanguage(entity.getLanguage());

        if (wihtCalendarEvents) {
            Date start = CalendarEventsUtil.convertToDateViaInstant(LocalDateTime.now().minusDays(1));
            List<TCalendarEvent> events = calendarEventRepository.findCalendarEventsByUserId(entity.getId(), EnumCalendarEventType.PERM, start);

            lawyer.setDuties(new ArrayList<>());
            events.stream().forEach(event -> {
                if (event.getEventType().equals(EnumCalendarEventType.PERM)) {
                    Long userId = event.getTUsers() != null ? event.getTUsers().getId() : null;
                    LawyerDuty duty = new LawyerDuty(event.getId(), event.getStart(), event.getEnd(), event.getNote(), userId);
                    lawyer.getDuties().add(duty);
                }
            });
        }
        return lawyer;
    }
}
