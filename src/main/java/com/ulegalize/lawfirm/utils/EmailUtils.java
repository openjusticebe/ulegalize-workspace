package com.ulegalize.lawfirm.utils;

import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.model.LawyerDutyRequest;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TCalendarEvent;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.utils.DossiersUtils;
import com.ulegalize.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailUtils {
    private static String DOMAIN_URL;
    private static String PORTAL_URL;

    public static Map<String, Object> prepareContextForRegisteredAppointmentEmail(String language, LawyerDutyRequest appointment, EnumCalendarEventType eventType, LawfirmUsers lawyer, String portalUrl, String clientFrom) {
        Map<String, Object> model = communPrepareContext(clientFrom, portalUrl, appointment.getEmail());
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
        model.put("portalUrlLogin", portalUrl + "login");

        model.put("fullname", appointment.getFirstName() + " " + appointment.getLastName());
        model.put("lawyer_fullname", lawyer.getUser().getFullname());

        model.put("lawfirm_email", lawyer.getLawfirm().getEmail());
        model.put("lawfirm_phone", lawyer.getLawfirm().getPhoneNumber());

        model.put("appointment_date", formatDateToString(appointment.getStart()));
        model.put("appointment_time", formatTimeToString(appointment.getStart()));
        model.put("appointment_note", appointment.getNote());
        model.put("appointment_email", appointment.getEmail());
        model.put("appointment_phone", appointment.getPhone());
        model.put("appointment_type", Utils.getLabel(enumLanguage, eventType.getLabelFr(), eventType.getLabelEn(), eventType.getLabelNl(), eventType.getLabelNl()));
        return model;
    }

    public static Map<String, Object> prepareContextForSharedFolderUser(String emails, String dossier, String userDetails, String vcKey, String clientFrom) {
        Map<String, Object> model = communPrepareContext(clientFrom, "", emails);
        model.put("user_details", userDetails);
        model.put("vckey", vcKey);
        model.put("dossier", dossier);

        return model;
    }

    public static Map<String, Object> prepareContextForSharedUserSecurity(String emails, String vcKey, String clientFrom) {
        Map<String, Object> model = communPrepareContext(clientFrom, "", emails);
        model.put("vckey", vcKey);

        return model;
    }

    public static Map<String, Object> prepareContextVerifyUser(String emailTo, String email, String hash, String language, String clientFrom) {
        Map<String, Object> model = communPrepareContext(clientFrom, "", emailTo);

        String url = "https://" + clientFrom + "." + DOMAIN_URL;

        String verifyUrl = url + "/verify" + "?email=" + email + "&key=" + hash + "&language=" + language;
        model.put("verifyUrl", verifyUrl);

        return model;
    }

    public static Map<String, Object> prepareContextCreateAssociation(String emailTo, TWorkspaceAssociated tWorkspaceAssociated, String associateWorkspaceAcceptUrl, String associateWorkspaceDeclineUrl, String clientFrom) {
        Map<String, Object> model = communPrepareContext(clientFrom, "", emailTo);

        String url = "https://" + clientFrom + "." + DOMAIN_URL;
        String associateworkspace = url + "/associateworkspace";
        associateWorkspaceAcceptUrl = associateworkspace + associateWorkspaceAcceptUrl;
        associateWorkspaceDeclineUrl = associateworkspace + associateWorkspaceDeclineUrl;

        model.put("associateWorkspaceAcceptUrl", associateWorkspaceAcceptUrl);
        model.put("associateWorkspaceDeclineUrl", associateWorkspaceDeclineUrl);

        model.put("message", tWorkspaceAssociated.getMessage());
        model.put("virtualCab", tWorkspaceAssociated.getLawfirmSender().getVckey());

        return model;
    }

    public static Map<String, Object> prepareContextWelcomeEmail(String emailTo, String clientFrom) {
        return communPrepareContext(clientFrom, "", emailTo);
    }

    public static Map<String, Object> prepareContextSupportEmail(String emailTo, String clientFrom, String appointementUl) {
        Map<String, Object> model = communPrepareContext(clientFrom, "", emailTo);
        model.put("appointementUrl", appointementUl);
        return model;

    }

    public static Map<String, Object> prepareContextReminderEmail(String emailTo, String clientFrom) {
        return communPrepareContext(clientFrom, "", emailTo);
    }

    public static Map<String, Object> prepareContextForNewAppointmentEmail(String language, LawyerDutyRequest appointment, EnumCalendarEventType eventType, LawfirmUsers lawyer, String portalUrl, String clientFrom) {

        Map<String, Object> model = communPrepareContext(clientFrom, portalUrl, lawyer.getUser().getEmail());
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);

        model.put("fullname", appointment.getFirstName() + " " + appointment.getLastName());

        model.put("lawyer_fullname", lawyer.getUser().getFullname());
        model.put("lawyer_email", lawyer.getUser().getEmail());

        model.put("appointment_date", formatDateToString(appointment.getStart()));
        model.put("appointment_time", formatTimeToString(appointment.getStart()));
        model.put("appointment_note", appointment.getNote());
        model.put("appointment_email", appointment.getEmail());
        model.put("appointment_phone", appointment.getPhone());
        model.put("appointment_type", Utils.getLabel(enumLanguage, eventType.getLabelFr(), eventType.getLabelEn(), eventType.getLabelNl(), eventType.getLabelNl()));

        return model;
    }

    public static Map<String, Object> prepareContextForAppointmentConfirmedEmail(String language, String emailTo, TCalendarEvent appointment, LawfirmUsers lawyer, String portalUrl, String clientFrom) {

        Map<String, Object> model = communPrepareContext(clientFrom, portalUrl, emailTo);
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);
        String location = appointment.getLocation() != null ? appointment.getLocation() : null;

        model.put("location", location);

        model.put("lawyer_fullname", lawyer.getUser().getFullname());

        model.put("lawfirm_email", lawyer.getLawfirm().getEmail());
        model.put("lawfirm_phone", lawyer.getLawfirm().getPhoneNumber());

        model.put("appointment_date", formatDateToString(appointment.getStart()));
        model.put("appointment_time", formatTimeToString(appointment.getStart()));
        model.put("appointment_time_end", formatTimeToString(appointment.getEnd()));
        model.put("appointment_note", appointment.getNote());
        model.put("appointment_type", Utils.getLabel(enumLanguage, appointment.getEventType().getLabelFr(), appointment.getEventType().getLabelEn(), appointment.getEventType().getLabelNl(), appointment.getEventType().getLabelNl()));

        return model;
    }

    public static Map<String, Object> prepareContextNotificationEmail(String language, TCalendarEvent appointment, Date startDate, Date endDate, String emailContact, String phoneContact, String portalUrl, String emailTo, String clientFrom) {

        Map<String, Object> model = communPrepareContext(clientFrom, portalUrl, emailTo);

        String dossierReference = appointment.getDossier() != null ? DossiersUtils.getDossierLabelItem(appointment.getDossier().getYear_doss(), appointment.getDossier().getNum_doss()) : null;
        String location = appointment.getLocation() != null ? appointment.getLocation() : null;
        String note = appointment.getNote() != null ? appointment.getNote() : null;

        model.put("lawfirm_email", emailContact);
        model.put("lawfirm_phone", phoneContact);
        model.put("lawyer_email", emailContact);
        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(language);

        model.put("appointment_type", Utils.getLabel(enumLanguage, appointment.getEventType().getLabelFr(), appointment.getEventType().getLabelEn(), appointment.getEventType().getLabelNl(), appointment.getEventType().getLabelNl()));
        model.put("appointment_date", formatDateToString(startDate));
        model.put("appointment_time", formatTimeToString(startDate));
        model.put("appointment_time_end", endDate != null ? formatTimeToString(endDate) : null);

        model.put("title", appointment.getTitle());
        model.put("dossier_reference", dossierReference);
        model.put("location", location);
        model.put("description", note);

        return model;
    }

    private static Map<String, Object> communPrepareContext(String clientFrom, String portalUrl, String emailTo) {

        Map<String, Object> model = new HashMap<>();

        // value by default
        model.put("portalUrl", portalUrl);
        model.put("clientFromImage", PORTAL_URL + "images/" + clientFrom + ".png");
        model.put("lawfirmUrl", "https://" + clientFrom + "." + DOMAIN_URL);

        model.put("clientFrom", clientFrom);

        model.put("to", emailTo);

        return model;
    }

    private static String formatDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        return formatter.format(date);
    }

    private static String formatTimeToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    @Value("${app.domain.url}")
    public void setDomain(String name) {
        EmailUtils.DOMAIN_URL = name;
    }

    @Value("${app.portal.url}")
    public void setPortalUrl(String name) {
        EmailUtils.PORTAL_URL = name;
    }
}
