package com.ulegalize.lawfirm.service.v2.impl;

import com.auth0.json.mgmt.users.User;
import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class LawfirmV2ServiceImplTest extends EntityTest {

    @Autowired
    private LawfirmV2Service lawfirmV2Service;
    private LawfirmEntity lawfirm;


    @Test
    public void test_A_deleteTempVcKey() throws Exception {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User();
        user.setEmail(usermail);
        user.setFamilyName("family");
        user.setGivenName("first");
        Boolean tempVc = lawfirmV2Service.deleteTempVcKey(lawfirm.getVckey());

        assertTrue(tempVc);

    }

    @Test
    public void test_B_getLawfirmInfoByVcKey() throws Exception {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LawfirmDTO lawfirmDTO = lawfirmV2Service.getLawfirmInfoByVcKey(lawfirm.getVckey());

        assertNotNull(lawfirmDTO);
        assertNotNull(lawfirmDTO.getVckey());

    }

    @Test
    @Disabled
    void test_C_searchLawfirmInfoByVcKeyAndStatusAssociation_Status_Accepted() {
        lawfirm = createLawfirm("CAB1");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LawfirmEntity lawfirm2 = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirm, lawfirm2);
        workspaceAssociation.setStatus(EnumStatusAssociation.ACCEPTED);

        List<LawfirmDTO> lawfirmDTOList = lawfirmV2Service.searchLawfirmInfoByVcKeyAndStatusAssociation(lawfirm2.getVckey());

        assertEquals(0, lawfirmDTOList.size());
    }

    @Test
    @Disabled
    void test_D_searchLawfirmInfoByVcKeyAndStatusAssociation_Status_Refused() {
        lawfirm = createLawfirm("CAB1");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LawfirmEntity lawfirm2 = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirm, lawfirm2);
        workspaceAssociation.setStatus(EnumStatusAssociation.REFUSED);

        List<LawfirmDTO> lawfirmDTOList = lawfirmV2Service.searchLawfirmInfoByVcKeyAndStatusAssociation(lawfirm2.getVckey());

        assertEquals(1, lawfirmDTOList.size());
    }

    @Test
    void registerUser() {

        createTSequences();
        String newEmail = "test@test.com";

        LawfirmToken lawfirmToken = new LawfirmToken(0L, newEmail, newEmail, "NO", "", true, Collections.singletonList(EnumRights.ADMINISTRATEUR), "", true,
                EnumLanguage.FR.getShortCode(),
                EnumRefCurrency.EUR.getSymbol(), newEmail, DriveType.openstack, "", false);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TUsers user = createUser("test@test.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);
        String vckeyTemp = lawfirmV2Service.registerUser(user.getEmail(), "workspace");

        assertEquals(vckeyTemp, EnumValid.UNVERIFIED.name());

    }


}
