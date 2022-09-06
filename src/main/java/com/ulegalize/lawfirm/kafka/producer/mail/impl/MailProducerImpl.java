package com.ulegalize.lawfirm.kafka.producer.mail.impl;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.kafka.producer.mail.IMailProducer;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import com.ulegalize.mail.transparency.KafkaMailObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class MailProducerImpl implements IMailProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${tpd.sendMail-topic-name}")
    private String topicName;
    @Value("${tpd.sendEvent-topic-name}")
    private String topicSendEventName;

    public MailProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendEvent(String eventId, String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context, boolean roomAttached, boolean isModerator, String urlRoom, List<String> attendeesEmail) {
        log.debug("Entering producer sendEmail enumLanguage {} and template {}", enumLanguage, template);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
//                && !activeProfile.equalsIgnoreCase("dev")
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaMailObject messageKafka = new KafkaMailObject();
                messageKafka.setEnumMailTemplate(enumMailTemplate);
                messageKafka.setEnumLanguage(enumLanguage);
                messageKafka.setTemplate(template);
                messageKafka.setSubject(subject);
                messageKafka.setContext(context);
                messageKafka.setLocation(location);
                messageKafka.setStart(start);
                messageKafka.setEnd(end);
                messageKafka.setRoomAttached(roomAttached);
                messageKafka.setIsModerator(isModerator);
                messageKafka.setUrlRoom(urlRoom);
                messageKafka.setAttendeesEmail(attendeesEmail);
                messageKafka.setEventId(eventId);

                kafkaTemplate.send(topicSendEventName, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createFolders ", e);
            }
        }
    }

    @Override
    public void sendEmail(String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context) {
        log.debug("Entering producer sendEmail enumLanguage {} and template {}", enumLanguage, template);
        if (!activeProfile.equalsIgnoreCase("integrationtest")
                /*&& !activeProfile.equalsIgnoreCase("dev")*/
                && !activeProfile.equalsIgnoreCase("devDocker")) {
            try {
                KafkaMailObject messageKafka = new KafkaMailObject();
                messageKafka.setEnumMailTemplate(enumMailTemplate);
                messageKafka.setEnumLanguage(enumLanguage);
                messageKafka.setTemplate(template);
                messageKafka.setSubject(subject);
                messageKafka.setContext(context);
                messageKafka.setLocation(location);
                messageKafka.setStart(start);
                messageKafka.setEnd(end);

                kafkaTemplate.send(topicName, messageKafka);

                log.info("All messages received");
            } catch (Exception e) {
                log.error("Error while createFolders ", e);
            }
        }
    }

}