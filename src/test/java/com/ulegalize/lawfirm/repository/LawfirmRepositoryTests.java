package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class LawfirmRepositoryTests extends EntityTest {
    @Autowired
    private LawfirmRepository lawfirmRepository;

    @Test
    public void test_A_searchLawfirmDTOByVckey() {
        LawfirmEntity lawfirmSender = createLawfirm("AVOTEST");
        LawfirmEntity lawfirmSender2 = createLawfirm("TEST1");

        LawfirmEntity lawfirmRecipient = createLawfirm("SEVERINE");
        LawfirmEntity lawfirmRecipient2 = createLawfirm("ULMI");
        LawfirmEntity lawfirmRecipient3 = createLawfirm("FINAUXA");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        workspaceAssociation.setStatus(EnumStatusAssociation.ACCEPTED);
        testEntityManager.persist(workspaceAssociation);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient2);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient3);
        createWorkspaceAssociation(lawfirmSender2, lawfirmRecipient);

        List<LawfirmEntity> lawfirmEntities = lawfirmRepository.searchLawfirmDTOByVckey(lawfirmRecipient.getVckey().substring(0, 3), lawfirmSender.getVckey(), EnumStatusAssociation.ACCEPTED);
        assertNotNull(lawfirmEntities);
        assertEquals(1, lawfirmEntities.size());
        assertEquals(lawfirmRecipient.getVckey(), lawfirmEntities.get(0).getVckey());
    }

    @Test
    public void test_B_searchLawfirmDTOByVckey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(lawfirm.getVckey());
        assertNotNull(lawfirmDTOOptional);

        assertTrue(lawfirmDTOOptional.isPresent());

    }

    @Test
    public void test_C_findAllByLicenseIdAndTemporaryVc() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        List<LawfirmEntity> lawfirmEntities = lawfirmRepository.findAllByLicenseIdAndTemporaryVc(lawfirm.getVckey());

        assertNotNull(lawfirmEntities);
        assertTrue(lawfirmEntities.size() > 0);
        assertEquals(lawfirm.getVckey(), lawfirmEntities.get(0).getVckey());

    }

    @Test
    void test_D_searchLawfirmDTOByVckeyAndStatusAssociation_Status_Accepted() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        workspaceAssociation.setStatus(EnumStatusAssociation.ACCEPTED);

        List<LawfirmEntity> lawfirmEntities = lawfirmRepository.searchLawfirmDTOByVckeyAndStatusAssociation(lawfirmRecipient.getVckey(), lawfirmToken.getVcKey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

        assertEquals(0, lawfirmEntities.size());
    }

    @Test
    void test_E_searchLawfirmDTOByVckeyAndStatusAssociation_Status_Pending() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        Long userId = lawfirmSender.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirmSender.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirmSender.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirmSender.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirmSender.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        workspaceAssociation.setStatus(EnumStatusAssociation.REFUSED);

        List<LawfirmEntity> lawfirmDTOS = lawfirmRepository.searchLawfirmDTOByVckeyAndStatusAssociation(lawfirmRecipient.getVckey(), lawfirmToken.getVcKey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));

        assertEquals(1, lawfirmDTOS.size());
    }


    @Test
    public void test_F_findBySelected() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findBySelected(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        assertNotNull(lawfirmDTOOptional);

        assertTrue(lawfirmDTOOptional.isPresent());

    }

    @Test
    public void test_G_findBySelected() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        LawfirmEntity lawfirm2 = createLawfirm("MYLAW2");

        Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findBySelected(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        assertNotNull(lawfirmDTOOptional);

        assertTrue(lawfirmDTOOptional.isPresent());

    }

}
