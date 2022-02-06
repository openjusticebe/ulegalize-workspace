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
        sendMail(type, context, language, null, null, false, false, null);
    }

    /**
     * no meeting
     *
     * @param type
     * @param context
     * @param language
     * @param start
     * @param end
     */
    public void sendMailWithoutMeeting(EnumMailTemplate type, Map<String, Object> context, String language, ZonedDateTime start, ZonedDateTime end) {
        sendMail(type, context, language, start, end, false, false, null);
    }

    public void sendMail(EnumMailTemplate type, Map<String, Object> context, String language, ZonedDateTime start, ZonedDateTime end, boolean roomAttached, boolean isModerator, String roomName) {
        if (activeProfile.equalsIgnoreCase("integrationtest")
                || activeProfile.equalsIgnoreCase("dev")
                || activeProfile.equalsIgnoreCase("devDocker")) {
            return;
        }
        String emailTo = (String) context.get("to");
        String location = (String) context.get("location");
        String organizer = null;
        String subject = null;

        log.info("Sending mail {} and to {} ", type, emailTo);
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
        log.debug("language {}", enumLanguage);
        String appointmentType = (String) context.get("appointment_type");

        String subjectFr = "";
        String subjectEn = "";
        String subjectNl = "";

        if (appointmentType != null) {
            subjectFr = appointmentType + " - ";
            subjectEn = appointmentType + " - ";
            subjectNl = appointmentType + " - ";
        }

        subjectFr += type.getSubjectFr();
        subjectEn += type.getSubjectEn();
        subjectNl += type.getSubjectNl();


        switch (type) {
            case MAILAPPOINTMENT_ADDED_NOTIFICATION:
                organizer = (String) context.get("lawyer_email");

                subject = context.get("title") != null && !((String) context.get("title")).isEmpty() ? (String) context.get("title") : Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl);
                break;
            case MAILAPPOINTMENT_CANCEL_NOTIFICATION:
            case MAILAPPOINTMENTCONFIRMEDTEMPLATE:
            case MAILNEWAPPOINTMENTREQUESTTEMPLATE: {
                organizer = (String) context.get("lawyer_email");
            }
            case MAILAPPOINTMENTREGISTEREDTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl);
                break;
            }
            case MAILSHAREDFOLDERUSERTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl) + (String) context.get("dossier");

                break;
            }
            case MAILSHAREDUSERSECURITYTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl) + (String) context.get("vckey");

                break;
            }
            case MAILVERIFYTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl);

                break;
            }
            default:
                break;
        }
        sendMailWithoutMeetingAndIcs(organizer, location, start, end, type, enumLanguage,
                type.getName() + language,
                subject,
                context,
                roomAttached, isModerator, roomName);

    }

    private void sendMailWithoutMeetingAndIcs(String organizer, String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context, boolean roomAttached, boolean isModerator, String roomName) {
        mailProducer.sendEmail(organizer, location, start, end, enumMailTemplate, enumLanguage, template, subject, context, roomAttached, isModerator, roomName );
    }
}
