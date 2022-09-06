package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.dto.ItemPartieDTO;
import com.ulegalize.dto.ShareAffaireDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.TDossierRightsRepository;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class DossierV2ServiceTests extends EntityTest {

    @Autowired
    private DossierV2Service dossierV2Service;
    @Autowired
    private TDossierRightsRepository dossierRightsRepository;
    @Autowired
    private TUsersRepository usersRepository;
    @Autowired
    private EntityToDossierConverter entityToDossierConverter;
    private LawfirmEntity lawfirm;

    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    public void test_A_getAffairesByVcUserIdAndSearchCriteria_numDossier_67() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, vcKey, null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        String searchCriteria = "67";
        Long vcUserId = lawfirm.getLawfirmUsers().get(0).getUser().getId();

        List<ItemLongDto> itemLongDtos = dossierV2Service.getAffairesByVcUserIdAndSearchCriteria(
                vcKey, vcUserId, searchCriteria);

        assertNotNull(itemLongDtos);
        assertEquals(1, itemLongDtos.size());
        assertTrue(itemLongDtos.get(0).getLabel().toLowerCase().contains(searchCriteria.toLowerCase()));

    }

    @Test
    public void test_B_getAffairesByVcUserIdAndSearchCriteria_yearDossier_2020() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, vcKey, null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long vcUserId = lawfirm.getLawfirmUsers().get(0).getUser().getId();

        List<ItemLongDto> itemLongDtos = dossierV2Service.getAffairesByVcUserIdAndSearchCriteria(
                vcKey, vcUserId, searchCriteria);

        assertNotNull(itemLongDtos);
        assertEquals(1, itemLongDtos.size());
        assertTrue(itemLongDtos.get(0).getLabel().toLowerCase().contains(searchCriteria.toLowerCase()));

    }

    @Test
    public void test_C_getAllAffaires_zero() {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        String vcKey = lawfirm.getVckey();
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean searchArchived = false;
        Page<DossierDTO> allAffaires = dossierV2Service.getAllAffaires(10, 0, userId, vcKey, List.of(EnumVCOwner.OWNER_VC), "", null, null, false, null, searchArchived, null);

        assertNotNull(allAffaires);
        assertEquals(1, allAffaires.getTotalPages());
        assertTrue(allAffaires.getContent().get(0).getBalance().setScale(2).equals(BigDecimal.ZERO.setScale(2)));
    }

    @Test
    public void test_D_getAllAffaires_notZero() {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        createTTimesheet(lawfirm, dossier);

        String vcKey = lawfirm.getVckey();
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean searchArchived = false;
        Page<DossierDTO> allAffaires = dossierV2Service.getAllAffaires(10, 0, userId, vcKey, List.of(EnumVCOwner.OWNER_VC), "", null, null, true, null, searchArchived, null);

        assertNotNull(allAffaires);
        assertEquals(1, allAffaires.getTotalPages());
        assertFalse(allAffaires.getContent().get(0).getBalance().equals(BigDecimal.ZERO));
    }

    @Test
    public void test_E_updateAffaire_client() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TClients newClient = createClient(lawfirm);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);

        dossierDTO.setIdClient(newClient.getId_client());

        DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey);

        assertNotNull(updateAffaire);
        assertEquals(newClient.getId_client(), updateAffaire.getIdClient());

        // double check
        TDossiers tDossiersnew = testEntityManager.find(TDossiers.class, dossier.getIdDoss());
        Optional<DossierContact> dossierContactClient = tDossiersnew.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();
        assertEquals(dossierContactClient.get().getClients().getId_client(), updateAffaire.getIdClient());
    }

    @Test
    public void test_F_updateAffaire_party_exception() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDoss_type(EnumDossierType.MD.getDossType());
        testEntityManager.persist(dossier);

        TClients newClient = createClient(lawfirm);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);

        // clientList is empty
        dossierDTO.setClientList(new ArrayList<>());
        assertThrows(ResponseStatusException.class, () -> {
            DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey);
        });
    }

    @Test
    public void test_G_updateAffaire_party() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDoss_type(EnumDossierType.MD.getDossType());
        testEntityManager.persist(dossier);

        TClients newClient = createClient(lawfirm);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);

        dossierDTO.getClientList().get(0).setValue(newClient.getId_client());

        DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey);

        assertNotNull(updateAffaire);
        assertEquals(newClient.getId_client(), updateAffaire.getClientList().get(0).getValue());

        // double check
        TDossiers tDossiersnew = testEntityManager.find(TDossiers.class, dossier.getIdDoss());
        Optional<DossierContact> dossierContactClient = tDossiersnew.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.PARTY)).findFirst();
        assertEquals(dossierContactClient.get().getClients().getId_client(), updateAffaire.getClientList().get(0).getValue());
    }

    @Test
    public void test_H_addShareFolderUser_NotMd() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "new@gmail.com");
        Long newUserId = lawfirmUsers.getUser().getId();

        ShareAffaireDTO shareAffaireDTO = new ShareAffaireDTO();
        shareAffaireDTO.setUserIdSelected(new ArrayList<>());
        shareAffaireDTO.getUserIdSelected().add(newUserId);
        shareAffaireDTO.setUserId(newUserId);
        shareAffaireDTO.setAffaireId(dossier.getIdDoss());
        shareAffaireDTO.setVcKey(vcKey);
        shareAffaireDTO.setAllMembers(false);
        dossierV2Service.addShareFolderUser(shareAffaireDTO, false);

        List<ShareAffaireDTO> tDossiersnew = dossierRightsRepository.findShareUserByAffaireId(dossier.getIdDoss());

        assertEquals(2, tDossiersnew.size());

    }

    @Test
    public void test_I_inviteConseil_withnewVcKey() {
        createTSequences();
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        ItemPartieDTO itemPartieDTO = new ItemPartieDTO();
        itemPartieDTO.setEmail("unknownUser@gmail.com");
        itemPartieDTO.setLitigant(false);

        String tDossiersnew = dossierV2Service.inviteConseil(dossier.getIdDoss(), itemPartieDTO);

        Optional<TUsers> newInvitedUser = usersRepository.findByEmail("unknownUser@gmail.com");

        assertTrue(newInvitedUser.isPresent());
        assertEquals(EnumValid.VERIFIED, newInvitedUser.get().getIdValid());

    }
}
