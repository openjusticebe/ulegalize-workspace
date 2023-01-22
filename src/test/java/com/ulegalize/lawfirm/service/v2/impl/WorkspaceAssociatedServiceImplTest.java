package com.ulegalize.lawfirm.service.v2.impl;

import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.AssociatedWorkspaceDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import com.ulegalize.lawfirm.repository.TWorkspaceAssociatedRepository;
import com.ulegalize.lawfirm.service.v2.WorkspaceAssociatedService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class WorkspaceAssociatedServiceImplTest extends EntityTest {

    @Autowired
    private WorkspaceAssociatedService workspaceAssociatedService;
    @Autowired
    private TWorkspaceAssociatedRepository workspaceAssociatedRepository;

    @Test
    void test_A_validateAssociation_accepted() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        workspaceAssociatedService.validateAssociation(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), workspaceAssociation.getHashkey(), true);
        Optional<TWorkspaceAssociated> workspaceAssociated = workspaceAssociatedRepository.findByIdAndLawfirmRecipientAndStatus(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));
        TWorkspaceAssociated tWorkspaceAssociated = null;
        if (workspaceAssociated.isPresent()) {
            tWorkspaceAssociated = workspaceAssociated.get();
        }
        assertNotNull(tWorkspaceAssociated);
        assertEquals(tWorkspaceAssociated.getStatus(), EnumStatusAssociation.ACCEPTED);
    }

    @Test
    void test_B_validateAssociation_refused() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        workspaceAssociatedService.validateAssociation(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), workspaceAssociation.getHashkey(), false);

        Optional<TWorkspaceAssociated> workspaceAssociated = workspaceAssociatedRepository.findByIdAndLawfirmRecipientAndStatus(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

        assertEquals(workspaceAssociated, Optional.empty());
    }

    @Test
    void test_C_validateAssociation_ReturnException() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        assertThrows(ResponseStatusException.class, () -> {
            workspaceAssociatedService.validateAssociation(null, workspaceAssociation.getLawfirmRecipient().getVckey(), workspaceAssociation.getHashkey(), true);
        });
    }

    @Test
    void test_D_validateAssociation_ReturnException() {
        assertThrows(ResponseStatusException.class, () -> {
            workspaceAssociatedService.validateAssociation(1L, "test", "test", true);
        });
    }

    @Test
    void test_E_createAssociation() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AssociatedWorkspaceDTO associatedWorkspaceDTO = new AssociatedWorkspaceDTO();
        associatedWorkspaceDTO.setVcKeyRecipient(lawfirmRecipient.getVckey());
        associatedWorkspaceDTO.setMessage("Test Message workspace Association");

        assertTrue(workspaceAssociatedService.createAssociation(associatedWorkspaceDTO));

    }

    @Test
    void test_F_createAssociation_Recipient_VcKey_Error() throws ResponseStatusException {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("");

        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AssociatedWorkspaceDTO associatedWorkspaceDTO = new AssociatedWorkspaceDTO();
        associatedWorkspaceDTO.setVcKeyRecipient(lawfirmRecipient.getVckey());
        associatedWorkspaceDTO.setMessage("Test Message workspace Association");

        assertThrows(ResponseStatusException.class, () -> {
            workspaceAssociatedService.createAssociation(associatedWorkspaceDTO);
        });

    }

    @Test
    void test_G_createAssociation_Recipient_VcKey_Not_Exist() throws ResponseStatusException {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AssociatedWorkspaceDTO associatedWorkspaceDTO = new AssociatedWorkspaceDTO();
        associatedWorkspaceDTO.setVcKeyRecipient("CAB3");
        associatedWorkspaceDTO.setMessage("Test Message workspace Association");

        assertThrows(ResponseStatusException.class, () -> {
            workspaceAssociatedService.createAssociation(associatedWorkspaceDTO);
        });

    }

    @Test
    void test_H_createAssociation_Association_Already_Exist() throws ResponseStatusException {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        AssociatedWorkspaceDTO associatedWorkspaceDTO = new AssociatedWorkspaceDTO();
        associatedWorkspaceDTO.setVcKeyRecipient(lawfirmRecipient.getVckey());
        associatedWorkspaceDTO.setMessage("Test Message workspace Association");

        assertThrows(ResponseStatusException.class, () -> {
            workspaceAssociatedService.createAssociation(associatedWorkspaceDTO);
        });

    }

    @Test
    void test_I_getAllAssociatedWorkspace_searchByRecipient() {
        LawfirmEntity lawfirmSender = createLawfirm("AVOTEST");
        LawfirmEntity lawfirmSender2 = createLawfirm("TEST1");

        LawfirmEntity lawfirmRecipient = createLawfirm("SEVERINE");
        LawfirmEntity lawfirmRecipient2 = createLawfirm("ULMI");
        LawfirmEntity lawfirmRecipient3 = createLawfirm("FINAUXA");

        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient2);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient3);
        createWorkspaceAssociation(lawfirmSender2, lawfirmRecipient);

        Long userId = lawfirmRecipient.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmRecipient.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmRecipient.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmRecipient.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmRecipient.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Page<AssociatedWorkspaceDTO> workspaceAssociatedPage = workspaceAssociatedService.getAllAssociatedWorkspace(5, 0, lawfirmRecipient.getVckey(), lawfirmToken.getUserId(), null);

        assertNotNull(workspaceAssociatedPage);
        assertEquals(2, workspaceAssociatedPage.getContent().size());
    }

    @Test
    void test_J_getAllAssociatedWorkspace_serachBySender() {

        LawfirmEntity lawfirmSender = createLawfirm("AVOTEST");
        LawfirmEntity lawfirmSender2 = createLawfirm("TEST1");

        LawfirmEntity lawfirmRecipient = createLawfirm("SEVERINE");
        LawfirmEntity lawfirmRecipient2 = createLawfirm("ULMI");
        LawfirmEntity lawfirmRecipient3 = createLawfirm("FINAUXA");

        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient2);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient3);
        createWorkspaceAssociation(lawfirmSender2, lawfirmRecipient);

        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        Page<AssociatedWorkspaceDTO> workspaceAssociatedPage = workspaceAssociatedService.getAllAssociatedWorkspace(5, 0, lawfirmSender.getVckey(), lawfirmToken.getUserId(), null);

        assertNotNull(workspaceAssociatedPage);
        assertEquals(3, workspaceAssociatedPage.getContent().size());
    }

    @Test
    void test_K_updateAssociation_statusAccepted() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        workspaceAssociatedService.validateAssociation(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), workspaceAssociation.getHashkey(), true);

        Boolean status = true;
        workspaceAssociatedService.updateAssociation(lawfirmSender.getVckey(), lawfirmToken.getUserId(), lawfirmRecipient.getVckey(), false, status);

        Optional<TWorkspaceAssociated> workspaceAssociated = workspaceAssociatedRepository.findByLawfirmSenderAndLawfirmRecipientAndStatus(lawfirmSender.getVckey(), lawfirmRecipient.getVckey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));
        TWorkspaceAssociated tWorkspaceAssociated = null;
        if (workspaceAssociated.isPresent()) {
            tWorkspaceAssociated = workspaceAssociated.get();
        }
        assertNotNull(tWorkspaceAssociated);
        assertEquals(tWorkspaceAssociated.getStatus(), EnumStatusAssociation.ACCEPTED);
    }

    @Test
    void test_K_updateAssociation_statusRefused() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");
        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        workspaceAssociatedService.validateAssociation(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), workspaceAssociation.getHashkey(), true);

        Boolean status = false;
        workspaceAssociatedService.updateAssociation(lawfirmSender.getVckey(), lawfirmToken.getUserId(), lawfirmRecipient.getVckey(), false, status);

        Optional<TWorkspaceAssociated> workspaceAssociated = workspaceAssociatedRepository.findByLawfirmSenderAndLawfirmRecipientAndStatus(lawfirmSender.getVckey(), lawfirmRecipient.getVckey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));
        TWorkspaceAssociated tWorkspaceAssociated = null;
        if (workspaceAssociated.isPresent()) {
            tWorkspaceAssociated = workspaceAssociated.get();
        }
        assertNull(tWorkspaceAssociated);
    }

    @Test
    void test_L_updateAssociation_statusIsNull_shouldReturnException() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");
        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        workspaceAssociatedService.validateAssociation(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), workspaceAssociation.getHashkey(), true);

        Boolean status = null;

        assertThrows(ResponseStatusException.class, () -> {
            workspaceAssociatedService.updateAssociation(lawfirmSender.getVckey(), lawfirmToken.getUserId(), lawfirmRecipient.getVckey(), false, status);
        });
    }
}