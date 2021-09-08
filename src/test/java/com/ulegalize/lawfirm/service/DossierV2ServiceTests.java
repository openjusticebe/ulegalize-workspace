package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.dto.ShareAffaireDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.DossierContactRepository;
import com.ulegalize.lawfirm.repository.TDossierRightsRepository;
import com.ulegalize.lawfirm.service.v2.DossierV2Service;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
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

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class DossierV2ServiceTests extends EntityTest {

    @Autowired
    private DossierV2Service dossierV2Service;
    @Autowired
    private TDossierRightsRepository dossierRightsRepository;
    @Autowired
    private EntityToDossierConverter entityToDossierConverter;
    @Autowired
    private DossierContactRepository dossierContactRepository;

    @Test
    public void test_A_getAffairesByVcUserIdAndSearchCriteria_numDossier_67() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        String email = "my@gmail.com";
        String fullname = lawfirmEntity.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirmEntity.getVckey();
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, vcKey, null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        String searchCriteria = "67";
        Long vcUserId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        List<ItemLongDto> itemLongDtos = dossierV2Service.getAffairesByVcUserIdAndSearchCriteria(
                vcKey, vcUserId, searchCriteria);

        assertNotNull(itemLongDtos);
        assertEquals(1, itemLongDtos.size());
        assertTrue(itemLongDtos.get(0).getLabel().toLowerCase().contains(searchCriteria.toLowerCase()));

    }

    @Test
    public void test_B_getAffairesByVcUserIdAndSearchCriteria_yearDossier_2020() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        String email = "my@gmail.com";
        String fullname = lawfirmEntity.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirmEntity.getVckey();
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, vcKey, null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long vcUserId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        List<ItemLongDto> itemLongDtos = dossierV2Service.getAffairesByVcUserIdAndSearchCriteria(
                vcKey, vcUserId, searchCriteria);

        assertNotNull(itemLongDtos);
        assertEquals(1, itemLongDtos.size());
        assertTrue(itemLongDtos.get(0).getLabel().toLowerCase().contains(searchCriteria.toLowerCase()));

    }

    @Test
    public void test_C_getAllAffaires_zero() {

        LawfirmEntity lawfirmEntity = createLawfirm();
        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        String vcKey = lawfirmEntity.getVckey();
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();
        boolean searchArchived = false;
        Page<DossierDTO> allAffaires = dossierV2Service.getAllAffaires(10, 0, userId, vcKey, List.of(EnumVCOwner.OWNER_VC), "", null, null, false, null, searchArchived);

        assertNotNull(allAffaires);
        assertEquals(1, allAffaires.getTotalPages());
        assertTrue(allAffaires.getContent().get(0).getBalance().equals(BigDecimal.ZERO.setScale(2)));
    }

    @Test
    public void test_D_getAllAffaires_notZero() {

        LawfirmEntity lawfirmEntity = createLawfirm();
        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        createTTimesheet(lawfirmEntity, dossier);

        String vcKey = lawfirmEntity.getVckey();
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();
        boolean searchArchived = false;
        Page<DossierDTO> allAffaires = dossierV2Service.getAllAffaires(10, 0, userId, vcKey, List.of(EnumVCOwner.OWNER_VC), "", null, null, true, null, searchArchived);

        assertNotNull(allAffaires);
        assertEquals(1, allAffaires.getTotalPages());
        assertFalse(allAffaires.getContent().get(0).getBalance().equals(BigDecimal.ZERO));
    }

    @Test
    public void test_E_updateAffaire_client() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        String email = "my@gmail.com";
        String fullname = lawfirmEntity.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirmEntity.getVckey();
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirmEntity.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        TClients newClient = createClient(lawfirmEntity);

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

    @Test(expected = ResponseStatusException.class)
    public void test_F_updateAffaire_party_exception() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        String email = "my@gmail.com";
        String fullname = lawfirmEntity.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirmEntity.getVckey();
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirmEntity.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        dossier.setDoss_type(EnumDossierType.MD.getDossType());
        testEntityManager.persist(dossier);

        TClients newClient = createClient(lawfirmEntity);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);

        dossierDTO.setIdClient(newClient.getId_client());

        DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey);
    }

    @Test
    public void test_G_updateAffaire_party() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        String email = "my@gmail.com";
        String fullname = lawfirmEntity.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirmEntity.getVckey();
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirmEntity.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        dossier.setDoss_type(EnumDossierType.MD.getDossType());
        testEntityManager.persist(dossier);

        TClients newClient = createClient(lawfirmEntity);

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
        LawfirmEntity lawfirmEntity = createLawfirm();
        String email = "my@gmail.com";
        String fullname = lawfirmEntity.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirmEntity.getVckey();
        Long userId = lawfirmEntity.getLawfirmUsers().get(0).getUser().getId();

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirmEntity.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirmEntity, "new@gmail.com");
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
}
