package com.ulegalize.lawfirm.kafka.producer.mail;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.mail.transparency.EnumMailTemplate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface IMailProducer {
    void sendEvent(String eventId, String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context, boolean roomAttached, boolean isModerator, String urlRoom, List<String> attendeesEmail);

    void sendEmail(String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context);

}