package com.ulegalize.lawfirm.utils;

import com.ulegalize.enumeration.EnumContextTemplate;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumTypeTemplate;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumMesureType;
import org.apache.commons.text.StringEscapeUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultLawfirm {

    public static List<TTimesheetType> getTimesheetType(String vcKey, EnumLanguage enumLanguage) {
        List<String> descriptions = new ArrayList<>();
        List<TTimesheetType> timesheetTypeList = new ArrayList<>();

        if (EnumLanguage.EN.equals(enumLanguage)) {
            descriptions.add("Mail");
            descriptions.add("Meeting");
            descriptions.add("Audience delivered");
            descriptions.add("Audience default");
            descriptions.add("Moot hearing");
            descriptions.add("Procedure");
            descriptions.add("Note");
            descriptions.add("Search");
            descriptions.add("Study of the file");
            descriptions.add("Request");
            descriptions.add("Remedies");
            descriptions.add("Conclusions");
            descriptions.add("Mail");
            descriptions.add("Telephone interview");
            descriptions.add("Displacement");
            descriptions.add("Quote");
            descriptions.add("Expertise");
            descriptions.add("Photocopy");
            descriptions.add("Folder archiving");
            descriptions.add("Open folder");
            descriptions.add("Follow-up of the file");
            descriptions.add("Mail");
        } else if (EnumLanguage.NL.equals(enumLanguage)) {
            descriptions.add("Mail");
            descriptions.add("Vergadering");
            descriptions.add("Publiek geleverd");
            descriptions.add("doelgroep standaard");
            descriptions.add("Moot hoorzitting");
            descriptions.add("Procedure");
            descriptions.add("Opmerking");
            descriptions.add("Zoeken");
            descriptions.add("Studie van het bestand");
            descriptions.add("Verzoek");
            descriptions.add("Remedies");
            descriptions.add("Conclusies");
            descriptions.add("Mail");
            descriptions.add("Telefoongesprek");
            descriptions.add("Verplaatsing");
            descriptions.add("Citaat");
            descriptions.add("Expertise");
            descriptions.add("Fotokopie");
            descriptions.add("Maparchivering");
            descriptions.add("Open map");
            descriptions.add("Opvolging van het bestand");
            descriptions.add("Mail");
        } else if (EnumLanguage.DE.equals(enumLanguage)) {
            descriptions.add("Mail");
            descriptions.add("Treffen");
            descriptions.add("Publikum geliefert");
            descriptions.add("Publikumsstandard");
            descriptions.add("Strittige Anhörung");
            descriptions.add("Verfahren");
            descriptions.add("Hinweis");
            descriptions.add("Suche");
            descriptions.add("Untersuchung der Datei");
            descriptions.add("Anfrage");
            descriptions.add("Abhilfe");
            descriptions.add("Schlussfolgerungen");
            descriptions.add("Mail");
            descriptions.add("Telefoninterview");
            descriptions.add("Verschiebung");
            descriptions.add("Zitat");
            descriptions.add("Expertise");
            descriptions.add("Fotokopie");
            descriptions.add("Ordnerarchivierung");
            descriptions.add("Ordner öffnen");
            descriptions.add("Nachverfolgung der Datei");
            descriptions.add("Mail");
        } else {
            descriptions.add("Courrier");
            descriptions.add("Réunion");
            descriptions.add("Audience remise");
            descriptions.add("Audience défaut");
            descriptions.add("Audience plaidoirie");
            descriptions.add("Démarche");
            descriptions.add("Note");
            descriptions.add("Recherche");
            descriptions.add("Étude du dossier");
            descriptions.add("Requête");
            descriptions.add("Recours");
            descriptions.add("Conclusions");
            descriptions.add("Entretien téléphonique");
            descriptions.add("Déplacement");
            descriptions.add("Citation");
            descriptions.add("Expertise");
            descriptions.add("Photocopie");
            descriptions.add("Archivage dossier");
            descriptions.add("Ouverture dossier");
            descriptions.add("Suivi du dossier");
            descriptions.add("Mail");
        }

        for (String description : descriptions) {
            TTimesheetType tTimesheetType = new TTimesheetType();
            tTimesheetType.setVcKey(vcKey);
            tTimesheetType.setDescription(description);
            tTimesheetType.setDateUpd(new Date());
            tTimesheetType.setUserUpd("ulegalize");
            tTimesheetType.setArchived(false);

            timesheetTypeList.add(tTimesheetType);
        }

        return timesheetTypeList;
    }

    public static List<TDebourType> getDebourType(String vcKey, EnumLanguage enumLanguage) {
        List<TDebourType> tDebourTypes = new ArrayList<>();

        if (EnumLanguage.EN.equals(enumLanguage)) {
            TDebourType tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.2"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Photocopy(ies)");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Typed page");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Typed letter");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("8"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("emails");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("25"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("registered mail");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.KM.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.7"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Travel costs outside Brussels");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("50"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("File opening and archiving");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("15"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("displacement in Bxl");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);
        } else if (EnumLanguage.NL.equals(enumLanguage)) {
            TDebourType tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.2"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Fotokopie(s)");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("getypte pagina");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("getypte brief");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("8"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("e-mails");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("25"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("aangetekende brief");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.KM.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.7"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Reiskosten buiten Brussel");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("50"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("File opening and archiving");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("15"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("verplaatsing in Bxl");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);
        } else if (EnumLanguage.DE.equals(enumLanguage)) {
            TDebourType tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.2"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Fotokopie(n)");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("getippte Seite");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Getippter Brief");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("8"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("E-Mails");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("25"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Einschreiben");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.KM.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.7"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Reisekosten außerhalb von Brüssel");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("50"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Öffnen und Archivieren von Dateien");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("15"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Reisen in Brüssel");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);
        } else {
            TDebourType tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.2"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Photocopie(s)");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Page dactylographiée");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("10"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Lettre dactylographiée");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("8"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("courriels");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.PAGE.getId());
            tDebourType.setPricePerUnit(new BigDecimal("25"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("courrier recommandé");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.KM.getId());
            tDebourType.setPricePerUnit(new BigDecimal("0.7"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Frais de déplacement hors Bruxelles");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("50"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("Ouverture et archivage dossier");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);

            tDebourType = new TDebourType();
            tDebourType.setIdMesureType(EnumMesureType.FORFAIT.getId());
            tDebourType.setPricePerUnit(new BigDecimal("15"));
            tDebourType.setVcKey(vcKey);
            tDebourType.setDescription("déplacement dans Bxl");
            tDebourType.setUserUpd("ulegalize");
            tDebourType.setArchived(false);
            tDebourType.setDateUpd(new Date());

            tDebourTypes.add(tDebourType);
        }

        return tDebourTypes;
    }

    public static List<RefPoste> getRefPostes(String vcKey, EnumLanguage enumLanguage) {
        List<RefPoste> refPosteList = new ArrayList<>();

        if (EnumLanguage.EN.equals(enumLanguage)) {
            RefPoste refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Fees");
            refPoste.setHonoraires(true);
            refPoste.setFacturable(true);
            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Social contributions");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Books, magazines");

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Conferences, colloquia");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Bailiff's fees");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Registry voucher");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Administration costs");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Stamps");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Recommended");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Collaboration costs");
            refPoste.setFraisCollaboration(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Secretary");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Social Secretariat");
            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Telephone");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Restaurant");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Public transport");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Taxi");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Office - rent");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Advance payments");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Office - supplies");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Office - material to amortize");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Mastercard");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("rents");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("civil party reimbursement");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Trainee salary");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("translation costs");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("customer refund");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("opponent payment");

            refPosteList.add(refPoste);

        } else if (EnumLanguage.NL.equals(enumLanguage)) {
            RefPoste refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Kosten");
            refPoste.setHonoraires(true);
            refPoste.setFacturable(true);
            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Sociale bijdragen");
            refPoste.setFraisCollaboration(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Boeken, tijdschriften");

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Conferenties, colloquia");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Kosten van de deurtrueder");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Registervoucher");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Administratiekosten");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Postzegels");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Aanbevolen");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Samenwerkingskosten");
            refPoste.setFraisCollaboration(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Secretaris");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Sociaal secretariaat");
            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Telefoon");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Restaurant");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Openbaar vervoer");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Taxi");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Kantoor - huur");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Vooruitbetalingen");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Kantoorbenodigdheden");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Kantoor - af te schrijven materiaal");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Mastercard");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("huurt");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("terugbetaling door burgerlijke partij");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Salaris stagiair");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("vertaalkosten");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("terugbetaling van de klant");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("tegenbetaling");

            refPosteList.add(refPoste);

        } else if (EnumLanguage.DE.equals(enumLanguage)) {
            RefPoste refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Honorare");
            refPoste.setHonoraires(true);
            refPoste.setFacturable(true);
            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Sozialbeiträge");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Bücher, Zeitschriften");

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Konferenzen, Symposien");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Gerichtsvollziehergebühren");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Registrierungsbeleg");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Verwaltungsgebühren");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Briefmarken");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Empfohlen");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Gebühren für die Zusammenarbeit");
            refPoste.setFraisCollaboration(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Sekretär");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Sozialsekretär");
            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Telefon");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Restaurant");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("öffentlicher Verkehr");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Taxi");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Büromiete");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Vorauszahlungen");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Büro - Lieferungen");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Abzuschreibende Bürogeräte");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Mastercard");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Mieten");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("zivilrechtliche Erstattung");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Azubi Gehalt");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Übersetzungskosten");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Kundenrückerstattung");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Zahlung des Gegners");

            refPosteList.add(refPoste);

        } else {
            RefPoste refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Honoraires");
            refPoste.setHonoraires(true);
            refPoste.setFacturable(true);
            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Cotisations sociales");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Livres, revues");

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Conférences, colloques");
            refPoste.setArchived(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Frais d'huissier");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Bon de Greffe");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);

            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Frais d'administration");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Timbres");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Recommandés");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Frais de collaboration");
            refPoste.setFraisCollaboration(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Secrétaire");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Secretariat social");
            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Telephone");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Restaurant");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Transports en commun");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Taxi");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Bureau - loyer");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Versements anticipés");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Bureau - fournitures");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Bureau - matériel à amortir");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Mastercard");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("loyers");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("remboursement partie civile");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("Salaire stagiaire");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("frais traduction");
            refPoste.setFraisProcedure(true);

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("remboursement client");

            refPosteList.add(refPoste);
            refPoste = getRefPoste(vcKey);
            refPoste.setRefPoste("paiement adversaire");

            refPosteList.add(refPoste);

        }

        return refPosteList;
    }

    public static List<TTemplates> getTemplates(String vcKey, EnumLanguage enumLanguage) {
        List<TTemplates> tTemplates = new ArrayList<>();

        TTemplates templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName("Facture temporaire comptant");
        templates.setContext(EnumContextTemplate.FACTURE);
        templates.setSubcontext("FT-1-N");
        templates.setFormat("D");
        templates.setDescription("Facture temporaire comptant");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        String template = StringEscapeUtils.escapeHtml4("<p>&nbsp;</p>\n<p>En cas de non-paiement d'une facture &agrave;\n" +
                "l\n" +
                "'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l'&eacute;ch\n" +
                "&eacute;ance\n" +
                "portent de plein droit un int&eacute;r\n" +
                "&ecirc;t\n" +
                "de 1% par mois.</p>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);

        templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName("Facture comptant");
        templates.setSubcontext("FV-1-N");
        templates.setContext(EnumContextTemplate.FACTURE);
        templates.setFormat("D");
        templates.setDescription("Facture comptant");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        template = StringEscapeUtils.escapeHtml4("<p>&nbsp;</p>\n<p>En cas de non-paiement d'une facture &agrave;\n" +
                "l\n" +
                "'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l'&eacute;ch\n" +
                "&eacute;ance\n" +
                "portent de plein droit un int&eacute;r\n" +
                "&ecirc;t\n" +
                "de 1% par mois.</p>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);

        templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName(" ");
        templates.setSubcontext("HEADER");
        templates.setContext(EnumContextTemplate.CABINET);
        templates.setFormat("D");
        templates.setDescription("Cabinet header");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        template = StringEscapeUtils.escapeHtml4("<table>\n<tbody>\n<tr>\n<td rowspan=\"2\" width=\"12%\">{_Cabinet_Logo}</td>\n" +
                "<td style=\"font-weight: bold;\" align=\"left\">{_Cabinet_NomComplet}</td>\n</tr>\n<tr>\n" +
                "<td style=\"color: #ccc; font-weight: bold;\" align=\"left\">{_Cabinet_ObjetSocial}</td>\n</tr>\n</tbody>\n" +
                "</table>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);

        templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName("Facture temporaire échéance");
        templates.setSubcontext("FT-3-N");
        templates.setContext(EnumContextTemplate.FACTURE);
        templates.setFormat("D");
        templates.setDescription("Facture temporaire échéance");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        template = StringEscapeUtils.escapeHtml4("<p>&nbsp;</p>\n<p>En cas de non-paiement d'une facture &agrave;\n" +
                "l\n" +
                "'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l'&eacute;ch\n" +
                "&eacute;ance\n" +
                "portent de plein droit un int&eacute;r\n" +
                "&ecirc;t\n" +
                "de 1% par mois.</p>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);

        templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName("Facture temporaire payée");
        templates.setSubcontext("FT-2-N");
        templates.setContext(EnumContextTemplate.FACTURE);
        templates.setFormat("D");
        templates.setDescription("Facture temporaire payée");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        template = StringEscapeUtils.escapeHtml4("<p>&nbsp;</p>\n<p>Facture acquitt&eacute;e le {_Facture_DateEcheance}</p>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);


        templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName("Facture échéance");
        templates.setSubcontext("FV-3-N");
        templates.setContext(EnumContextTemplate.FACTURE);
        templates.setFormat("D");
        templates.setDescription("Facture échéance");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        template = StringEscapeUtils.escapeHtml4("<p>&nbsp;</p>\n<p>En cas de non-paiement d'une facture &agrave;\n" +
                "l\n" +
                "'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l'&eacute;ch\n" +
                "&eacute;ance\n" +
                "portent de plein droit un int&eacute;r\n" +
                "&ecirc;t\n" +
                "de 1% par mois.</p>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);

        tTemplates.add(templates);

        templates = new TTemplates();
        templates.setVcKey(vcKey);
        templates.setName("Facture payée");
        templates.setSubcontext("FV-2-N");
        templates.setContext(EnumContextTemplate.FACTURE);
        templates.setFormat("D");
        templates.setDescription("Facture payée");
        templates.setType(EnumTypeTemplate.S);
        templates.setUserUpd("ulegalize");
        templates.setDateUpd(LocalDateTime.now());
        template = StringEscapeUtils.escapeHtml4("<p>&nbsp;</p>\n<p>Facture acquitt&eacute;e le {_Facture_DateEcheance}</p>");
        templates.setTemplate(template.getBytes(StandardCharsets.UTF_8));

        tTemplates.add(templates);
        return tTemplates;
    }

    public static List<TVcGroupment> getVcGroupment(String vcKey) {
        List<TVcGroupment> groupmentList = new ArrayList<>();
        TVcGroupment tVcGroupment = new TVcGroupment();
        tVcGroupment.setVcKey(vcKey);
        tVcGroupment.setGroupmentId(1);
        tVcGroupment.setCreUser("ulegalize");
        groupmentList.add(tVcGroupment);
        tVcGroupment = new TVcGroupment();
        tVcGroupment.setVcKey(vcKey);
        tVcGroupment.setGroupmentId(2);
        tVcGroupment.setCreUser("ulegalize");
        groupmentList.add(tVcGroupment);
        tVcGroupment = new TVcGroupment();
        tVcGroupment.setVcKey(vcKey);
        tVcGroupment.setGroupmentId(3);
        tVcGroupment.setCreUser("ulegalize");
        groupmentList.add(tVcGroupment);

        return groupmentList;
    }

    public static RefPoste getRefPoste(String vcKey) {
        RefPoste refPoste = new RefPoste();
        refPoste.setVcKey(vcKey);
        refPoste.setFraisProcedure(false);
        refPoste.setHonoraires(false);
        refPoste.setFraisCollaboration(false);
        refPoste.setFacturable(false);
        refPoste.setArchived(false);
        refPoste.setUserUpd("ulegalize");
        refPoste.setDateUpd(new Date());

        return refPoste;
    }
}
