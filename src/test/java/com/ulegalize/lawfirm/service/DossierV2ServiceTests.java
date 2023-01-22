package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.dto.CaseDTO;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumMatiereRubrique;
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
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

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

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, vcKey, null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        String searchCriteria = dossier.getNomenclature();
        Long vcUserId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean digital = false;

        List<ItemLongDto> itemLongDtos = dossierV2Service.getAffairesByVcUserIdAndSearchCriteria(
                vcKey, vcUserId, searchCriteria, digital);

        assertNotNull(itemLongDtos);
        assertEquals(1, itemLongDtos.size());
        assertTrue(itemLongDtos.get(0).getLabel().toLowerCase().contains(searchCriteria.toLowerCase()));

    }

    @Test
    public void test_B_getAllAffaires_zero() {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        String vcKey = lawfirm.getVckey();
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean searchArchived = false;
        Page<DossierDTO> allAffaires = dossierV2Service.getAllAffaires(10, 0, userId, vcKey, EnumLanguage.FR.getShortCode(), List.of(EnumVCOwner.OWNER_VC), "", searchCriteria, null, false, null, searchArchived, null);

        assertNotNull(allAffaires);
        assertEquals(1, allAffaires.getTotalPages());
        assertEquals(allAffaires.getContent().get(0).getBalance().setScale(2), BigDecimal.ZERO.setScale(2));
    }

    @Test
    public void test_C_getAllAffaires_notZero() {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        createTTimesheet(lawfirm, dossier);

        String vcKey = lawfirm.getVckey();
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean searchArchived = false;
        Page<DossierDTO> allAffaires = dossierV2Service.getAllAffaires(10, 0, userId, vcKey, EnumLanguage.FR.getShortCode(), List.of(EnumVCOwner.OWNER_VC), "", null, null, true, null, searchArchived, null);

        assertNotNull(allAffaires);
        assertEquals(1, allAffaires.getTotalPages());
        assertNotEquals(allAffaires.getContent().get(0).getBalance(), BigDecimal.ZERO);
    }

    @Test
    public void test_D_updateAffaire_client() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TClients clientUpdateData = createClient(lawfirm);
        clientUpdateData.setF_nom("Client update");
        clientUpdateData.setF_email("Clientupdate@update.com");

        ItemLongDto itemClientUpdate = new ItemLongDto();
        itemClientUpdate.setLabel(clientUpdateData.getF_nom());
        itemClientUpdate.setValue(clientUpdateData.getId_client());

        dossierDTO.getDossierContactDTO().get(0).setClient(itemClientUpdate);
        dossierDTO.getDossierContactDTO().get(0).setClientType(clientItem);

        DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey, false);

        assertNotNull(updateAffaire);

        // double check
        TDossiers tDossiersnew = testEntityManager.find(TDossiers.class, dossier.getIdDoss());
        Optional<DossierContact> dossierContactClient = tDossiersnew.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().name().equals(EnumDossierContactType.CLIENT.name())).findAny();
        Long resultAfterUpdate = updateAffaire.getDossierContactDTO().get(0).getClient().getValue();
        assertEquals(dossierContactClient.get().getClients().getId_client(), resultAfterUpdate);
    }

    @Test
    public void test_E_updateAffaire_party_exception() {
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
        dossierDTO.setDossierContactDTO(new ArrayList<>());
        assertThrows(ResponseStatusException.class, () -> {
            DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey, false);
        });
    }

    @Test
    public void test_F_updateAffaire_party() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDoss_type(EnumDossierType.MD.name());

        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemDto partyItem = new ItemDto();
        partyItem.setLabel(EnumDossierContactType.PARTY.name());
        partyItem.setValue(EnumDossierContactType.PARTY.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(partyItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(partyItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TClients clientUpdateData = createClient(lawfirm);
        clientUpdateData.setF_nom("Client update");
        clientUpdateData.setF_email("Clientupdate@update.com");

        ItemLongDto itemClientUpdate = new ItemLongDto();
        itemClientUpdate.setLabel(clientUpdateData.getF_nom());
        itemClientUpdate.setValue(clientUpdateData.getId_client());

        dossierDTO.getDossierContactDTO().get(0).setClient(itemClientUpdate);
        dossierDTO.getDossierContactDTO().get(0).setClientType(partyItem);

        DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey, false);

        assertNotNull(updateAffaire);

        // double check
        TDossiers tDossiersnew = testEntityManager.find(TDossiers.class, dossier.getIdDoss());
        Optional<DossierContact> dossierContactClient = tDossiersnew.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().name().equals(EnumDossierContactType.PARTY.name())).findFirst();
        Long resultAfterUpdate = updateAffaire.getDossierContactDTO().get(0).getClient().getValue();
        assertEquals(dossierContactClient.get().getClients().getId_client(), resultAfterUpdate);
    }

    @Test
    public void test_G_addShareFolderUser_NotMd() {
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

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
    public void test_H_inviteConseil_withnewVcKey() {
        createTSequences();
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

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

    @Test
    public void test_I_saveAffaire_Not_Mediation() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");
        TClients client3 = createClient(lawfirm);
        client3.setF_nom("Client Name3");
        client3.setF_email("Client@name3.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemLongDto client3dto = new ItemLongDto();
        client3dto.setLabel(client3.getF_nom());
        client3dto.setValue(client3.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO3 = new DossierContactDTO();
        dossierContactDTO3.setClient(client3dto);
        dossierContactDTO3.setClientType(opposingItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);
        dossierContactDTOS.add(dossierContactDTO3);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        Long dossierCreated = dossierV2Service.saveAffaireAndCreateCase(dossierDTO, vcKey);
        TDossiers tDossiersNew = testEntityManager.find(TDossiers.class, dossierCreated);

        assertNotNull(dossierCreated);
        assertEquals(dossierCreated, tDossiersNew.getIdDoss());
        assertEquals(tDossiersNew.getDossierNumber(), newDossierNumber + 1);
    }

    @Test
    public void test_J_saveAffaire_Mediation() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDossierNumber(newDossierNumber);
        dossier.setDoss_type(EnumDossierType.MD.getDossType());
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");
        TClients client3 = createClient(lawfirm);
        client3.setF_nom("Client Name3");
        client3.setF_email("Client@name3.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemLongDto client3dto = new ItemLongDto();
        client3dto.setLabel(client3.getF_nom());
        client3dto.setValue(client3.getId_client());

        ItemDto partyItem = new ItemDto();
        partyItem.setLabel(EnumDossierContactType.PARTY.name());
        partyItem.setValue(EnumDossierContactType.PARTY.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(partyItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(partyItem);

        DossierContactDTO dossierContactDTO3 = new DossierContactDTO();
        dossierContactDTO3.setClient(client3dto);
        dossierContactDTO3.setClientType(partyItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);
        dossierContactDTOS.add(dossierContactDTO3);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        Long dossierCreated = dossierV2Service.saveAffaireAndCreateCase(dossierDTO, vcKey);
        TDossiers tDossiersNew = testEntityManager.find(TDossiers.class, dossierCreated);

        assertNotNull(dossierCreated);
        assertEquals(dossierCreated, tDossiersNew.getIdDoss());
        assertEquals(tDossiersNew.getDossierNumber(), newDossierNumber + 1);
    }

    @Test
    public void test_K_updateAffaire_OPPOSING() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();


        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TClients clientUpdateData = createClient(lawfirm);
        clientUpdateData.setF_nom("Client update");
        clientUpdateData.setF_email("Clientupdate@update.com");

        ItemLongDto itemClientUpdate = new ItemLongDto();
        itemClientUpdate.setLabel(clientUpdateData.getF_nom());
        itemClientUpdate.setValue(clientUpdateData.getId_client());

        dossierDTO.getDossierContactDTO().get(1).setClient(itemClientUpdate);
        dossierDTO.getDossierContactDTO().get(1).setClientType(opposingItem);

        DossierDTO updateAffaire = dossierV2Service.updateAffaire(dossierDTO, userId, USER, vcKey, false);

        assertNotNull(updateAffaire);

        // double check
        TDossiers tDossiersnew = testEntityManager.find(TDossiers.class, dossier.getIdDoss());
        Optional<DossierContact> dossierContactClient = tDossiersnew.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().name().equals(EnumDossierContactType.OPPOSING.name())).findAny();
        Long resultAfterUpdate = updateAffaire.getDossierContactDTO().get(1).getClient().getValue();
        assertEquals(dossierContactClient.get().getClients().getId_client(), resultAfterUpdate);
    }

    @Test
    public void test_L_saveAffaire_Not_Mediation_Multiple_Client() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");
        TClients client3 = createClient(lawfirm);
        client3.setF_nom("Client Name3");
        client3.setF_email("Client@name3.com");
        TClients client4 = createClient(lawfirm);
        client4.setF_nom("Client Name4");
        client4.setF_email("Client@name4.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemLongDto client3dto = new ItemLongDto();
        client3dto.setLabel(client3.getF_nom());
        client3dto.setValue(client3.getId_client());

        ItemLongDto client4dto = new ItemLongDto();
        client4dto.setLabel(client4.getF_nom());
        client4dto.setValue(client4.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO3 = new DossierContactDTO();
        dossierContactDTO3.setClient(client3dto);
        dossierContactDTO3.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO4 = new DossierContactDTO();
        dossierContactDTO4.setClient(client4dto);
        dossierContactDTO4.setClientType(clientItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);
        dossierContactDTOS.add(dossierContactDTO3);
        dossierContactDTOS.add(dossierContactDTO4);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        Long dossierCreated = dossierV2Service.saveAffaireAndCreateCase(dossierDTO, vcKey);
        TDossiers tDossiersNew = testEntityManager.find(TDossiers.class, dossierCreated);

        assertNotNull(dossierCreated);
        assertEquals(dossierCreated, tDossiersNew.getIdDoss());
        assertEquals(tDossiersNew.getDossierNumber(), newDossierNumber + 1);
    }

    @Test
    void test_M_saveAffaireAndAttachToCase_Client_No_ContactID_Should_Throw_Error() {

        TClients tClients = createClient(lawfirm);

        int responsableId = Math.toIntExact(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        TVirtualcabNomenclature tVirtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "NomenclatureTest");
        CaseDTO caseDTO = createCaseDTO(tClients);

        assertThrows(ResponseStatusException.class, () ->
                dossierV2Service.saveAffaireAndAttachToCase(caseDTO, responsableId, tVirtualcabNomenclature.getName(), Math.toIntExact(tVirtualcabNomenclature.getId()), lawfirm.getVckey())
        );
    }

    @Test
    void test_N_saveAffaireAndAttachToCase_Client_With_ContactID() throws LawfirmBusinessException {
        TClients tClients = createClient(lawfirm);

        int responsableId = Math.toIntExact(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        TVirtualcabNomenclature tVirtualcabNomenclature = createVirtualcabNomenclature(lawfirm, "NomenclatureTest");

        CaseDTO caseDTO = createCaseDTO(tClients);
        caseDTO.getPartieEmail().get(0).setContactId(tClients.getId_client());

        Long affaireSaved = dossierV2Service.saveAffaireAndAttachToCase(caseDTO, responsableId, tVirtualcabNomenclature.getName(), Math.toIntExact(tVirtualcabNomenclature.getId()), lawfirm.getVckey());

        TDossiers dossierCreated = testEntityManager.find(TDossiers.class, affaireSaved);

        assertNotNull(dossierCreated);
        assertEquals(caseDTO.getPartieEmail().get(0).getEmail(), dossierCreated.getDossierContactList().get(0).getClients().getF_email());
    }

    @Test
    public void test_O_saveAffaire_With_tags() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");
        TClients client3 = createClient(lawfirm);
        client3.setF_nom("Client Name3");
        client3.setF_email("Client@name3.com");
        TClients client4 = createClient(lawfirm);
        client4.setF_nom("Client Name4");
        client4.setF_email("Client@name4.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemLongDto client3dto = new ItemLongDto();
        client3dto.setLabel(client3.getF_nom());
        client3dto.setValue(client3.getId_client());

        ItemLongDto client4dto = new ItemLongDto();
        client4dto.setLabel(client4.getF_nom());
        client4dto.setValue(client4.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO3 = new DossierContactDTO();
        dossierContactDTO3.setClient(client3dto);
        dossierContactDTO3.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO4 = new DossierContactDTO();
        dossierContactDTO4.setClient(client4dto);
        dossierContactDTO4.setClientType(clientItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);
        dossierContactDTOS.add(dossierContactDTO3);
        dossierContactDTOS.add(dossierContactDTO4);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TVirtualCabTags tVirtualCabTags2 = createTags(lawfirm);
        tVirtualCabTags2.setLabel("Tag2");

        ItemLongDto virtualcabTagInDto = new ItemLongDto(null, "Tag3", "true");
        ItemLongDto virtualcabTagInDto2 = new ItemLongDto(null, tVirtualCabTags.getLabel(), "false");
        ItemLongDto virtualcabTagInDto3 = new ItemLongDto(null, tVirtualCabTags2.getLabel(), "false");

        List<ItemLongDto> itemLongDtoList = new ArrayList<>();
        itemLongDtoList.add(virtualcabTagInDto);
        itemLongDtoList.add(virtualcabTagInDto2);
        itemLongDtoList.add(virtualcabTagInDto3);

        dossierDTO.setTagsList(itemLongDtoList);

        Long dossierCreated = dossierV2Service.saveAffaireAndCreateCase(dossierDTO, vcKey);
        TDossiers tDossiersNew = testEntityManager.find(TDossiers.class, dossierCreated);

        assertNotNull(dossierCreated);
        assertEquals(dossierCreated, tDossiersNew.getIdDoss());
        assertEquals(tDossiersNew.getTDossiersVcTags().size(), dossierDTO.getTagsList().size());
    }

    @Test
    public void test_P_getDossierById_With_Tags() {
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);

        TDossiersVcTags tDossiersVcTags = createTDossierVcTags(tDossiers, tVirtualCabTags);

        tDossiers.getTDossiersVcTags().add(tDossiersVcTags);

        DossierDTO dossierDTO = dossierV2Service.getDossierById(tDossiers.getIdDoss());

        assertNotNull(dossierDTO);

        assertEquals(dossierDTO.getTagsList().size(), tDossiers.getTDossiersVcTags().size());

    }

    @Test
    public void test_Q_getDossierById_Without_Tags() {
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        DossierDTO dossierDTO = dossierV2Service.getDossierById(tDossiers.getIdDoss());

        assertNotNull(dossierDTO);

        assertEquals(0, tDossiers.getTDossiersVcTags().size());

    }

    @Test
    public void test_R_addNewContactToDossier_DC() {
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        DossierDTO dossierDTO = dossierV2Service.getDossierById(tDossiers.getIdDoss());

        TClients tClients = createClient(lawfirm);

        List<Long> clientIdList = new ArrayList<>();

        clientIdList.add(tClients.getId_client());

        assertTrue(dossierV2Service.addNewContactToDossier(dossierDTO, clientIdList));
    }

    @Test
    public void test_S_addNewContactToDossier_MD() {
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        tDossiers.setDoss_type(EnumDossierType.MD.name());

        DossierDTO dossierDTO = dossierV2Service.getDossierById(tDossiers.getIdDoss());

        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemDto partyItem = new ItemDto();
        partyItem.setLabel(EnumDossierContactType.PARTY.name());
        partyItem.setValue(EnumDossierContactType.PARTY.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(partyItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(partyItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);

        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TClients tClients = createClient(lawfirm);

        List<Long> clientIdList = new ArrayList<>();

        clientIdList.add(tClients.getId_client());

        assertTrue(dossierV2Service.addNewContactToDossier(dossierDTO, clientIdList));
    }

    @Test
    public void test_T_saveAffaire_Not_Mediation() {
        String email = "my@gmail.com";
        Long newDossierNumber = 3L;
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDossierNumber(newDossierNumber);
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");
        TClients client3 = createClient(lawfirm);
        client3.setF_nom("Client Name3");
        client3.setF_email("Client@name3.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemLongDto client3dto = new ItemLongDto();
        client3dto.setLabel(client3.getF_nom());
        client3dto.setValue(client3.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO3 = new DossierContactDTO();
        dossierContactDTO3.setClient(client3dto);
        dossierContactDTO3.setClientType(opposingItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);
        dossierContactDTOS.add(dossierContactDTO3);

        DossierDTO dossierDTO = entityToDossierConverter.apply(dossier, EnumLanguage.FR);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        Long dossierCreated = dossierV2Service.saveAffaire(dossierDTO, vcKey);
        TDossiers tDossiersNew = testEntityManager.find(TDossiers.class, dossierCreated);

        assertNotNull(dossierCreated);
        assertEquals(dossierCreated, tDossiersNew.getIdDoss());
        assertEquals(tDossiersNew.getDossierNumber(), newDossierNumber + 1);
    }

    @Test
    public void test_U_saveAffaire_no_dossier() {
        long startDossierNumber = 2;
        lawfirm.setStartDossierNumber(startDossierNumber);
        testEntityManager.persist(lawfirm);
        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String vcKey = lawfirm.getVckey();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(userId, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TClients client1 = createClient(lawfirm);

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);

        DossierDTO dossierDTO = dossierV2Service.getDefaultDossier(lawfirm.getVckey(), userId);
        dossierDTO.setDossierContactDTO(dossierContactDTOS);
        dossierDTO.setId_matiere_rubrique(EnumMatiereRubrique.MEDIATION_MATIERE_SOCIALE.getMatiereId());

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        Long dossierCreated = dossierV2Service.saveAffaire(dossierDTO, vcKey);
        TDossiers tDossiersNew = testEntityManager.find(TDossiers.class, dossierCreated);

        assertNotNull(dossierCreated);
        assertEquals(dossierCreated, tDossiersNew.getIdDoss());
        assertEquals(tDossiersNew.getDossierNumber(), startDossierNumber);
    }


}
