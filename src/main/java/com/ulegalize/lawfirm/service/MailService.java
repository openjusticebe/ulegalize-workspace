package com.ulegalize.lawfirm.service;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.kafka.producer.mail.IMailProducer;
import com.ulegalize.mail.transparency.EnumMailTemplate;
import com.ulegalize.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class MailService {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private IMailProducer mailProducer;

    /**
     * no visio / meeting attachement and no ics attached
     *
     * @param type
     * @param context
     * @param language
     */
    public void sendMailWithoutMeetingAndIcs(EnumMailTemplate type, Map<String, Object> context, String language) {
        sendMail(type, context, language, null, null);
    }

    public void sendEvent(String eventId, EnumMailTemplate type, Map<String, Object> context, String language, ZonedDateTime start, ZonedDateTime end, boolean roomAttached, boolean isModerator, String urlRoom, List<String> attendeesEmail) {
        log.debug("Entering Event has to be sent");
        String location = (String) context.get("location");
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
        log.debug("language {}", enumLanguage);

        String subject = subjectConfiguration(type, context, enumLanguage);

        sendEvent(eventId, location, start, end, type, enumLanguage,
                type.getName() + language,
                subject,
                context,
                roomAttached,
                isModerator,
                urlRoom,
                attendeesEmail);
    }

    public void sendMail(EnumMailTemplate type, Map<String, Object> context, String language, ZonedDateTime start, ZonedDateTime end) {
        log.debug("Entering Email has to be sent");
        String location = (String) context.get("location");
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
        log.debug("language {}", enumLanguage);

        String subject = subjectConfiguration(type, context, enumLanguage);

        sendEmail(location, start, end, type, enumLanguage,
                type.getName() + language,
                subject,
                context);
    }

    public String subjectConfiguration(EnumMailTemplate type, Map<String, Object> context, EnumLanguage enumLanguage) {
        String emailTo = (String) context.get("to");

        log.info("Sending mail {} and to {} ", type, emailTo);

        String appointmentType = (String) context.get("appointment_type");

        String subjectFr = "";
        String subjectEn = "";
        String subjectNl = "";
        String subjectDe = "";

        if (appointmentType != null) {
            subjectFr = appointmentType + " - ";
            subjectEn = appointmentType + " - ";
            subjectNl = appointmentType + " - ";
            subjectDe = appointmentType + " - ";
        }

        subjectFr += type.getSubjectFr();
        subjectEn += type.getSubjectEn();
        subjectNl += type.getSubjectNl();
        subjectDe += type.getSubjectDe();

        String subject;

        switch (type) {
            case MAILAPPOINTMENT_ADDED_NOTIFICATION:

                subject = context.get("title") != null && !((String) context.get("title")).isEmpty() ? (String) context.get("title") : Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl, subjectNl);
                break;
            case MAILAPPOINTMENT_CANCEL_NOTIFICATION:
            case MAILAPPOINTMENTCONFIRMEDTEMPLATE:
            case MAILNEWAPPOINTMENTREQUESTTEMPLATE: {
            }
            case MAILAPPOINTMENTREGISTEREDTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl, subjectDe);
                break;
            }
            case MAILSHAREDFOLDERUSERTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl, subjectDe) + (String) context.get("dossier");

                break;
            }
            case MAILSHAREDUSERSECURITYTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl, subjectDe) + (String) context.get("vckey");

                break;
            }
            case MAILVERIFYTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl, subjectDe);

                break;
            }
            case MAILWORKSPACEASSOCIATION: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl, subjectDe);
                break;
            }

            default:
                subject = "";
                break;
        }

        return subject;

    }

    private void sendEmail(String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context) {
        mailProducer.sendEmail(location, start, end, enumMailTemplate, enumLanguage, template, subject, context);
    }

    private void sendEvent(String eventId, String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context, boolean roomAttached, boolean isModerator, String urlRoom, List<String> attendeesEmail) {
        mailProducer.sendEvent(eventId, location, start, end, enumMailTemplate, enumLanguage, template, subject, context, roomAttached, isModerator, urlRoom, attendeesEmail);
    }
}
