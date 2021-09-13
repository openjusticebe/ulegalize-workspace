package com.ulegalize.lawfirm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.*;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
public abstract class EntityTest extends ConfigureTest {
    protected static String USER = "BEST";
    protected static String EMAIL = "my@gmail.com";
    @Autowired
    protected TestEntityManager testEntityManager;

    protected ObjectMapper objectMapper;

    protected LawfirmEntity createLawfirm() {
        LawfirmEntity lawfirmEntity = new LawfirmEntity();
        lawfirmEntity.setVckey("MYLAW");
        lawfirmEntity.setName("MYLAW name");
        lawfirmEntity.setAlias("MYLAW");
        lawfirmEntity.setEmail("MYLAW@");
        lawfirmEntity.setCity("my city");
        lawfirmEntity.setLicenseId(1);
        lawfirmEntity.setDriveType(DriveType.openstack);
        lawfirmEntity.setDropboxToken("");
        lawfirmEntity.setCompanyNumber("0837777");
        lawfirmEntity.setUserUpd(USER);
        lawfirmEntity.setStartInvoiceNumber(10);
        lawfirmEntity.setNotification(true);

        lawfirmEntity.setLawfirmUsers(new ArrayList<>());
        createLawfirmUsers(lawfirmEntity, EMAIL);

        testEntityManager.persist(lawfirmEntity);

        return lawfirmEntity;
    }

    protected LawfirmUsers createLawfirmUsers(LawfirmEntity lawfirmEntity, String email) {
        LawfirmUsers lawfirmUsers = new LawfirmUsers();
        Date from = DateUtils.addDays(new Date(), -1);
        Date to = DateUtils.addYears(new Date(), 5);
        lawfirmUsers.setValidFrom(from);
        lawfirmUsers.setValidTo(to);
        lawfirmUsers.setIdRole(EnumRole.AVOCAT);
        lawfirmUsers.setLawfirm(lawfirmEntity);
        lawfirmUsers.setUser_upd(USER);
        lawfirmUsers.setPublic(true);
        lawfirmUsers.setSelected(true);
        lawfirmEntity.getLawfirmUsers().add(lawfirmUsers);

        TUsers user = createUser(email);
        lawfirmUsers.setUser(user);
        user.setLawfirmUsers(new ArrayList<>());
        user.getLawfirmUsers().add(lawfirmUsers);

        return lawfirmUsers;
    }

    protected TUsers createUser(String email) {
        TUsers user = new TUsers();
        user.setEmail(email);
        user.setFullname("my name");
        user.setAvatar(new byte[0]);
        user.setInitiales("me");
        user.setIdUser("me" + String.valueOf(UUID.randomUUID()).substring(0, 4));
        user.setHashkey("");
        user.setIdValid(EnumValid.VERIFIED);
        user.setLoginCount(0L);
        user.setUserpass("");
        user.setAliasPublic("my alias");
        user.setSpecialities("");
        user.setNotification(true);

        testEntityManager.persist(user);

        return user;

    }

    protected TDossiers createDossier(LawfirmEntity lawfirmEntity, EnumVCOwner enumVCOwner) {
        TDossiers tDossiers = new TDossiers();
        tDossiers.setYear_doss(String.valueOf(LocalDate.now().getYear()));
        tDossiers.setNum_doss(6789L);
        tDossiers.setDoss_type(EnumDossierType.DC.getDossType());
//        tDossiers.setClient_adv();
//        tDossiers.setClient_cab(createClient(lawfirmEntity));
        tDossiers.setVc_key(lawfirmEntity.getVckey());
        tDossiers.setDate_open(new Date());
        tDossiers.setDate_close(null);
        TClients client = createClient(lawfirmEntity);
        TClients clientAdverse = createClient(lawfirmEntity);

        // link client-dossier
        DossierContact dossierContact = new DossierContact();
        dossierContact.setDossiers(tDossiers);
        dossierContact.setClients(client);
        dossierContact.setContactTypeId(EnumDossierContactType.CLIENT);
        dossierContact.setCreUser(USER);
        tDossiers.getDossierContactList().add(dossierContact);

        // link client-dossier
        DossierContact dossierContactAdv = new DossierContact();
        dossierContactAdv.setDossiers(tDossiers);
        dossierContactAdv.setClients(clientAdverse);
        dossierContactAdv.setContactTypeId(EnumDossierContactType.OPPOSING);
        dossierContactAdv.setCreUser(USER);
        tDossiers.getDossierContactList().add(dossierContactAdv);


        TMatiereRubriques tMatiereRubriques = createTMatiereRubriques();
        tDossiers.setTMatiereRubriques(tMatiereRubriques);
        tDossiers.setKeywords("");
        tDossiers.setCouthoraire(100);
        tDossiers.setMemo("");
        tDossiers.setNote("");
        tDossiers.setUserUpd(USER);
        tDossiers.setSuccess_fee_perc(100);
        tDossiers.setSuccess_fee_montant(BigDecimal.ZERO);
        tDossiers.setId_user_resp(lawfirmEntity.getLawfirmUsers().get(0).getUser().getId());

        testEntityManager.persist(tDossiers);
        TDossierRights tDossierRights = new TDossierRights();
        tDossierRights.setDossierId(tDossiers.getIdDoss());
        tDossierRights.setVcUserId(lawfirmEntity.getLawfirmUsers().get(0).getId());
        tDossierRights.setVcOwner(enumVCOwner);
        tDossierRights.setRIGHTS("ACCESS");
        tDossierRights.setTDossiers(tDossiers);
        tDossierRights.setCreUser(USER);
        tDossierRights.setLawfirmUsers(lawfirmEntity.getLawfirmUsers().get(0));

        testEntityManager.persist(tDossierRights);

        tDossiers.setDossierRightsList(new ArrayList<>());
        tDossiers.getDossierRightsList().add(tDossierRights);
        testEntityManager.persist(tDossiers);

        return tDossiers;
    }

    protected TMatiereRubriques createTMatiereRubriques() {
        TMatiereRubriques matiereRubriques = new TMatiereRubriques();
        matiereRubriques.setMatiereRubriqueDesc("matiere desc");
        TMatieres tMatieres = new TMatieres();
        tMatieres.setIdMatiere(getRandomNumberUsingInts(0, 10000));
        tMatieres.setMatiereDesc("Maatiere");

        testEntityManager.persist(tMatieres);
        matiereRubriques.setIdMatiere(tMatieres.getIdMatiere());
        matiereRubriques.setMatieres(tMatieres);

        testEntityManager.persist(matiereRubriques);

        return matiereRubriques;
    }

    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    protected TClients createClient(LawfirmEntity lawfirmEntity) {
        TClients clients = new TClients();
        clients.setClient_type(EnumClientType.NATURAL_PERSON);
        clients.setF_nom("client nme");
        clients.setF_email("client@business.co,");
        clients.setId_lg("fr");
        if (clients.getVirtualcabClientList() == null) {
            clients.setVirtualcabClientList(new ArrayList<>());
        }

        VirtualcabClient virtualcabClient = new VirtualcabClient();
        virtualcabClient.setTClients(clients);
        virtualcabClient.setLawfirm(lawfirmEntity);
        virtualcabClient.setCreUser(USER);
        clients.getVirtualcabClientList().add(virtualcabClient);

        testEntityManager.persist(clients);

        return clients;
    }

    protected LawfirmWebsiteEntity createLawfirmWebsiteEntity(LawfirmEntity lawfirmEntity) {
        LawfirmWebsiteEntity lawfirmWebsiteEntity = new LawfirmWebsiteEntity();
        lawfirmWebsiteEntity.setVcKey(lawfirmEntity.getVckey());
        lawfirmWebsiteEntity.setTitle("title");
        lawfirmWebsiteEntity.setActive(true);

        lawfirmEntity.setLawfirmWebsite(lawfirmWebsiteEntity);

        testEntityManager.persist(lawfirmEntity);

        return lawfirmWebsiteEntity;
    }

    protected TCalendarEvent createTCalendarEvent(LawfirmEntity lawfirmEntity, EnumCalendarEventType calendarEventType, Date start, Date end) {
        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TCalendarEvent calendarEvent = new TCalendarEvent();
        calendarEvent.setDossier(dossier);
        TClients client = createClient(lawfirmEntity);
        calendarEvent.setContact(client);
        calendarEvent.setApproved(false);
        calendarEvent.setEventType(calendarEventType);
        calendarEvent.setStart(start);
        calendarEvent.setEnd(end);
        calendarEvent.setTUsers(lawfirmEntity.getLawfirmUsers().get(0).getUser());

        calendarEvent.setCreationUser(USER);

        TCalendarParticipants tCalendarParticipants = new TCalendarParticipants();
        tCalendarParticipants.setTCalendarEvent(calendarEvent);
        tCalendarParticipants.setUserEmail(lawfirmEntity.getLawfirmUsers().get(0).getUser().getEmail());
        tCalendarParticipants.setCreUser(USER);
        calendarEvent.setTCalendarParticipants(new ArrayList<>());
        calendarEvent.getTCalendarParticipants().add(tCalendarParticipants);

        testEntityManager.persist(calendarEvent);

        TUsers tUsers = lawfirmEntity.getLawfirmUsers().get(0).getUser();
        tUsers.setTCalendarEvents(new HashSet<>());
        tUsers.getTCalendarEvents().add(calendarEvent);

        testEntityManager.persist(tUsers);

        return calendarEvent;
    }

    protected TFrais createTFrais(LawfirmEntity lawfirm) {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TClients client = createClient(lawfirm);

        TFrais tFrais = new TFrais();
        tFrais.setIdClient(client.getId_client());
        tFrais.setIdDoss(dossier.getIdDoss());
        tFrais.setVcKey(lawfirm.getVckey());
        RefCompte refCompte = createRefCompte(lawfirm);
        tFrais.setIdCompte(refCompte.getIdCompte());
        tFrais.setIdType(EnumTType.ENTREE);
        RefPoste refPoste = createRefPoste(lawfirm);
        tFrais.setIdPoste(refPoste.getIdPoste());
        tFrais.setRefPoste(refPoste);
        tFrais.setIdTransaction(EnumRefTransaction.fromId(1));
        tFrais.setMontant(BigDecimal.valueOf(121));
        tFrais.setMontantht(BigDecimal.valueOf(100));
        tFrais.setRatio(BigDecimal.valueOf(121));
        tFrais.setRef("");
        tFrais.setDateValue(LocalDate.now());
        tFrais.setTva(21);
        TGrid grids = createGrids();
        tFrais.setGridId(grids.getID());
        tFrais.setDateUpd(LocalDateTime.now());


        testEntityManager.persist(tFrais);

        return tFrais;
    }

    protected TDebour createTDebour(LawfirmEntity lawfirm) {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TDebour tDebour = new TDebour();
        tDebour.setUnit(4);
        tDebour.setIdDoss(dossier.getIdDoss());
        tDebour.setDateAction(ZonedDateTime.now());
        tDebour.setPricePerUnit(BigDecimal.valueOf(40));
        TDebourType tDebourType = createTDebourType(lawfirm);
        tDebour.setIdMesureType(tDebourType.getIdMesureType());

        tDebour.setIdDebourType(tDebourType.getIdDebourType());
        tDebour.setDateUpd(LocalDateTime.now());
        tDebour.setUserUpd(USER);


        testEntityManager.persist(tDebour);

        return tDebour;
    }

    protected TDebourType createTDebourType(LawfirmEntity lawfirm) {
        TDebourType tDebourType = new TDebourType();
        TMesureType tMesureType = createTMesureType();
        tDebourType.setIdMesureType(tMesureType.getIdMesureType());
        tDebourType.setPricePerUnit(BigDecimal.valueOf(40));
        tDebourType.setVcKey(lawfirm.getVckey());
        tDebourType.setDescription("debour type desc");
        tDebourType.setUserUpd(USER);
        tDebourType.setArchived(false);
        tDebourType.setDateUpd(new Date());
        testEntityManager.persist(tDebourType);

        return tDebourType;
    }

    protected TMesureType createTMesureType() {
        TMesureType tMesureType = new TMesureType();
        tMesureType.setIdMesureType(1);
        tMesureType.setDescription("mesure type desc");
        testEntityManager.persist(tMesureType);

        return tMesureType;
    }

    protected TGrid createGrids() {
        TGrid tGrid = new TGrid();
        tGrid.setID(150);
        tGrid.setDESCRIPTION("new test grid");
        tGrid.setCreDate(LocalDateTime.now());
        tGrid.setCreUser(USER);

        testEntityManager.persist(tGrid);

        return tGrid;
    }

    protected RefCompte createRefCompte(LawfirmEntity lawfirmEntity) {

        RefCompte refCompte = new RefCompte();
        refCompte.setVcKey(lawfirmEntity.getVckey());
        refCompte.setCompteNum("BE1234567");
        refCompte.setCompteRef("Compte honoraire");
        refCompte.setUserUpd(USER);
        refCompte.setCountable(true);
        refCompte.setArchived(false);
        refCompte.setAccountTypeId(EnumAccountType.PRO_ACCOUNT);
        refCompte.setDateUpd(new Date());


        testEntityManager.persist(refCompte);

        return refCompte;
    }

    protected RefPoste createRefPoste(LawfirmEntity lawfirmEntity) {

        RefPoste refPoste = new RefPoste();
        refPoste.setVcKey(lawfirmEntity.getVckey());
        refPoste.setRefPoste("yes");
        refPoste.setUserUpd(USER);
        refPoste.setFraisCollaboration(false);
        refPoste.setFraisProcedure(false);
        refPoste.setHonoraires(true);
        refPoste.setFacturable(false);
        refPoste.setArchived(false);
        refPoste.setDateUpd(new Date());


        testEntityManager.persist(refPoste);

        return refPoste;
    }

    protected TSecurityGroupUsers createTSecurityGroupUsers(LawfirmEntity lawfirm, TSecurityGroups tSecurityGroups) {
        TSecurityGroupUsers tSecurityGroupUsers = new TSecurityGroupUsers();

        tSecurityGroupUsers.setUser(lawfirm.getLawfirmUsers().get(0).getUser());
        tSecurityGroupUsers.setTSecurityGroups(tSecurityGroups);
        testEntityManager.persist(tSecurityGroupUsers);

        tSecurityGroups.setTSecurityGroupUsersList(new ArrayList<>());
        tSecurityGroups.getTSecurityGroupUsersList().add(tSecurityGroupUsers);

        return tSecurityGroupUsers;
    }

    protected TSecurityGroupRights createTSecurityGroupRights(TSecurityGroups tSecurityGroups) {
        TSecurityGroupRights tSecurityGroupRights = new TSecurityGroupRights();
        tSecurityGroupRights.setIdRight(EnumRights.ADMINISTRATEUR);
        tSecurityGroupRights.setTSecurityGroups(tSecurityGroups);

        testEntityManager.persist(tSecurityGroupRights);

        tSecurityGroups.setTSecurityGroupRightsList(new ArrayList<>());
        tSecurityGroups.getTSecurityGroupRightsList().add(tSecurityGroupRights);

        testEntityManager.persist(tSecurityGroups);

        return tSecurityGroupRights;
    }

    protected TSecurityGroups createTSecurityGroups(LawfirmEntity lawfirm, boolean withUser) {
        TSecurityGroups tSecurityGroups = new TSecurityGroups();
        tSecurityGroups.setVcKey(lawfirm.getVckey());
        tSecurityGroups.setDescription("admin");
        tSecurityGroups.setTSecAppGroupId(EnumSecurityAppGroups.ADMIN);
        tSecurityGroups.setUserUpd(USER);
        tSecurityGroups.setDateUpd(LocalDateTime.now());

        createTSecurityGroupRights(tSecurityGroups);

        testEntityManager.persist(tSecurityGroups);
        if (withUser) {
            createTSecurityGroupUsers(lawfirm, tSecurityGroups);
        }

        return tSecurityGroups;
    }

    protected TMatieres createMatieres() {

        TMatieres matieres = new TMatieres();
        matieres.setIdMatiere(100);
        matieres.setMatiereDesc("civil right");

        testEntityManager.persist(matieres);

        TMatiereRubriques matiereRubriques = new TMatiereRubriques();
        matiereRubriques.setIdMatiere(matieres.getIdMatiere());
        matiereRubriques.setMatiereRubriqueDesc("civil right private");
        matiereRubriques.setMatieres(matieres);
        testEntityManager.persist(matiereRubriques);

        TMatiereRubriques rubriques2 = new TMatiereRubriques();
        rubriques2.setIdMatiere(matieres.getIdMatiere());
        rubriques2.setMatiereRubriqueDesc("civil right public");
        rubriques2.setMatieres(matieres);

        testEntityManager.persist(rubriques2);

        matieres.setMatieresRubriquesList(new ArrayList<>());
        matieres.getMatieresRubriquesList().add(matiereRubriques);
        matieres.getMatieresRubriquesList().add(rubriques2);

        return matieres;
    }

    protected TLangues createLanguage() {

        TLangues tLangues = new TLangues();
        tLangues.setIdLg("111");
        tLangues.setLgDesc("french");

        testEntityManager.persist(tLangues);

        return tLangues;
    }

    protected TCountries createTCountries() {

        TCountries tCountries = new TCountries();
        tCountries.setCode(999);
        tCountries.setAlpha2("AA");
        tCountries.setAlpha3("AAA");
        tCountries.setNomEnGb("AAA en");
        tCountries.setNomFrFr("AAA f");

        testEntityManager.persist(tCountries);

        return tCountries;
    }

    protected TSequences createTSequences() {

        TSequences sequences = new TSequences();
        sequences.setSequenceNumber(999L);
        sequences.setSequenceType(EnumSequenceType.TEMP_VC);

        testEntityManager.persist(sequences);

        return sequences;
    }

    protected TTimesheetType createTTimesheetType(LawfirmEntity lawfirm) {

        TTimesheetType tTimesheetType = new TTimesheetType();
        tTimesheetType.setVcKey(lawfirm.getVckey());
        tTimesheetType.setDescription("timesheet type");
        tTimesheetType.setDateUpd(new Date());
        tTimesheetType.setUserUpd(USER);
        tTimesheetType.setArchived(false);

        testEntityManager.persist(tTimesheetType);

        return tTimesheetType;
    }

    protected TTimesheet createTTimesheet(LawfirmEntity lawfirm, TDossiers dossier) {
        log.info("dossier {}", dossier.getIdDoss());

        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);
        TTimesheet tTimesheet = new TTimesheet();
        tTimesheet.setIdDoss(dossier.getIdDoss());
        tTimesheet.setIdGest(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        tTimesheet.setTsType(tTimesheetType.getIdTs());
        tTimesheet.setDateAction(ZonedDateTime.now());
        tTimesheet.setDateUpd(LocalDateTime.now());
        tTimesheet.setVat(new BigDecimal(21));
        tTimesheet.setCouthoraire(145);
        tTimesheet.setForfait(true);
        tTimesheet.setForfaitHt(new BigDecimal(100));

        testEntityManager.persist(tTimesheet);
        tTimesheet.setTTimesheetType(tTimesheetType);
        tTimesheet.setTDossiers(dossier);
        dossier.setTTimesheetList(new ArrayList<>());
        dossier.getTTimesheetList().add(tTimesheet);

        return tTimesheet;
    }

    protected TFactures createFacture(LawfirmEntity lawfirm) {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));

        TFactures tFactures = new TFactures();
        tFactures.setYearFacture(LocalDate.now().getYear());
        tFactures.setNumFacture(1);
        tFactures.setFactureRef("FV" + "-" + LocalDate.now().getYear() + "-" + "00" + tFactures.getNumFacture());
        tFactures.setVcKey(lawfirm.getVckey());
        tFactures.setDateValue(ZonedDateTime.now());
        tFactures.setMemo("memo test");
        tFactures.setMontant(new BigDecimal(500));
        tFactures.setIdFactureType(EnumFactureType.SELL);
        tFactures.setRef("ref test");
        tFactures.setDateEcheance(ZonedDateTime.now().plusDays(20));
        tFactures.setDateUpd(LocalDateTime.now());
        TClients client = createClient(lawfirm);
        tFactures.setIdTiers(client.getId_client());
        tFactures.setTClients(client);

        RefPoste refPoste = createRefPoste(lawfirm);
        refPoste.setRefPoste("Honoraires");
        refPoste.setFacturable(true);

        tFactures.setIdPoste(refPoste.getIdPoste());
        tFactures.setUserUpd(lawfirm.getUserUpd());

        TFactureEcheance tFactureEcheance = createFactureEcheance();
        tFactures.setIdEcheance(tFactureEcheance.getID());

        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        testEntityManager.persist(tDossiers);
        tFactures.setIdDoss(tDossiers.getIdDoss());
        tFactures.setTDossiers(tDossiers);

        createTFactureDetails(tFactures, virtualcabVat);

        TTimesheet tTimesheet = createTTimesheet(lawfirm, tDossiers);
        TFactureTimesheet tFactureTimesheet = new TFactureTimesheet();
        tFactureTimesheet.setTsId(tTimesheet.getIdTs());
        tFactureTimesheet.setCreUser(USER);
        tFactures.addTFactureTimesheet(tFactureTimesheet);
        tFactureTimesheet.setTFactures(tFactures);

        testEntityManager.persist(tFactures);

        return tFactures;
    }

    protected TFactureDetails createTFactureDetails(TFactures tFactures, TVirtualcabVat virtualcabVat) {
        TFactureDetails factureDetails = new TFactureDetails();
        factureDetails.setDescription(" new details");
        factureDetails.setHtva(BigDecimal.valueOf(100));
        factureDetails.setTtc(BigDecimal.valueOf(121));
        factureDetails.setTva(virtualcabVat.getVAT());
        factureDetails.setDateUpd(LocalDateTime.now());
        factureDetails.setUserUpd(USER);

        tFactures.addTFactureDetails(factureDetails);

        return factureDetails;

    }

    protected TFactureEcheance createFactureEcheance() {
        TFactureEcheance tFactureEcheance = new TFactureEcheance();
        tFactureEcheance.setID(1);
        tFactureEcheance.setDESCRIPTION("Comptant");
        testEntityManager.persist(tFactureEcheance);
        return tFactureEcheance;
    }

    protected TFactureType createFactureTypes() {
        TFactureType tFactureType = new TFactureType();
        tFactureType.setIdFactureType(1);
        tFactureType.setDescription("Facture Vente");
        tFactureType.setAcronyme("FV");
        testEntityManager.persist(tFactureType);
        return tFactureType;
    }

    protected TVirtualcabVat createVirtualcabVat(LawfirmEntity lawfirm, BigDecimal vat) {
        TVirtualcabVat tVirtualcabVat = new TVirtualcabVat();
        tVirtualcabVat.setVcKey(lawfirm.getVckey());
        tVirtualcabVat.setVAT(vat);
        tVirtualcabVat.setCreUser(USER);
        tVirtualcabVat.setIsDefault(true);

        testEntityManager.persist(tVirtualcabVat);

        lawfirm.setTVirtualcabVatList(new ArrayList<>());
        lawfirm.getTVirtualcabVatList().add(tVirtualcabVat);
        return tVirtualcabVat;
    }

    protected TTemplates creatTTemplates(LawfirmEntity lawfirm) {
        TTemplates entity = new TTemplates();
        entity.setVcKey(lawfirm.getVckey());
        entity.setUserUpd(USER);
        entity.setDateUpd(LocalDateTime.now());
        entity.setName("temp name");
        entity.setContext(EnumContextTemplate.DOSSIER);
        entity.setFormat("D");
        entity.setDescription("descr templat3");
        entity.setType(EnumTypeTemplate.U);
        String template = StringEscapeUtils.escapeHtml4("t4est");
        entity.setTemplate(template.getBytes(StandardCharsets.UTF_8));
        testEntityManager.persist(entity);

        return entity;
    }

    protected TVirtualcabConfig createTVirtualcabConfig(LawfirmEntity lawfirm) {
        TVirtualcabConfig entity = new TVirtualcabConfig();


        entity.setVcKey(lawfirm.getVckey());
        entity.setParameter("CASEFOLDER");
        entity.setDescription("description");
        testEntityManager.persist(entity);

        return entity;
    }

    protected VatCountry createVatCountry() {
        VatCountry entity = new VatCountry();

        entity.setVat(new BigDecimal("21"));
        entity.setIsDefault(true);
        entity.setIdCountryAlpha2("BE");
        testEntityManager.persist(entity);

        return entity;
    }
}
