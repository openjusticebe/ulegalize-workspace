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

    public void sendMail(EnumMailTemplate type, Map<String, Object> context, String language) {
        sendMail(type, context, language, null, null);
    }

    public void sendMail(EnumMailTemplate type, Map<String, Object> context, String language, ZonedDateTime start, ZonedDateTime end) {
        if (activeProfile.equalsIgnoreCase("integrationtest")
//				|| activeProfile.equalsIgnoreCase("dev")
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

        switch (type) {
            case MAILAPPOINTMENT_ADDED_NOTIFICATION:
            case MAILAPPOINTMENT_CANCEL_NOTIFICATION:
            case MAILAPPOINTMENTCONFIRMEDTEMPLATE:
            case MAILNEWAPPOINTMENTREQUESTTEMPLATE: {
                organizer = (String) context.get("lawyer_email");
            }
            case MAILAPPOINTMENTREGISTEREDTEMPLATE: {
                String subjectFr = (String) context.get("appointment_type") + " - " + type.getSubjectFr();
                String subjectEn = (String) context.get("appointment_type") + " - " + type.getSubjectEn();
                String subjectNl = (String) context.get("appointment_type") + " - " + type.getSubjectNl();
                subject = Utils.getLabel(enumLanguage, subjectFr, subjectEn, subjectNl);
                break;
            }
            case MAILSHAREDFOLDERUSERTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, type.getSubjectFr(), type.getSubjectEn(), type.getSubjectNl()) + (String) context.get("dossier");

                break;
            }
            case MAILSHAREDUSERSECURITYTEMPLATE: {
                subject = Utils.getLabel(enumLanguage, type.getSubjectFr(), type.getSubjectEn(), type.getSubjectNl()) + (String) context.get("vckey");

                break;
            }
            default:
                break;
        }
        sendMail(organizer, location, start, end, type, enumLanguage,
                type.getName() + language,
                subject,
                context);

    }

    private void sendMail(String organizer, String location, ZonedDateTime start, ZonedDateTime end, EnumMailTemplate enumMailTemplate, EnumLanguage enumLanguage, String template, String subject, Map<String, Object> context) {
        mailProducer.sendEmail(organizer, location, start, end, enumMailTemplate, enumLanguage, template, subject, context);
    }
}
