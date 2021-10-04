package com.ulegalize.lawfirm.kafka.producer.mail;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.mail.transparency.EnumMailTemplate;

import java.time.ZonedDateTime;
import java.util.Map;

public interface IMailProducer {
    public void sendEmail(String organizer, String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context, boolean roomAttached, boolean isModerator, String roomName);

}