package com.ulegalize.lawfirm.service;

import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumSecurityAppGroups;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class SecurityGroupServiceTests extends EntityTest {
    @Autowired
    private SecurityGroupService securityGroupService;


    @Test
    public void test_A_getUserProfile() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        String email = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();

        LawfirmToken userProfile = securityGroupService.getUserProfile(null, email, "", true);

        assertNotNull(userProfile);
        assertEquals(lawfirm.getVckey(), userProfile.getVcKey());
        assertEquals(tSecurityGroups.getTSecurityGroupRightsList().get(0).getIdRight(), EnumRights.ADMINISTRATEUR);
        assertNotNull(userProfile.getEnumRights());
    }

    @Test
    public void test_B_addUserSecurity() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        Long aLong = securityGroupService.addUserSecurity(tSecurityGroups.getId(), lawfirmUsers.getUser().getId());

        assertNotNull(aLong);

        TSecurityGroupUsers tSecurityGroupUsers = testEntityManager.find(TSecurityGroupUsers.class, aLong);
        assertNotNull(tSecurityGroupUsers);

    }

    @Test
    public void test_C_deleteSecurityUserGroup() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        // add second user to the group admin
        TSecurityGroupUsers tSecurityGroupUsersRemain = createTSecurityGroupUsers(lawfirmUsers, tSecurityGroups);
        assertEquals(2, tSecurityGroups.getTSecurityGroupUsersList().size());

        Long aLong = securityGroupService.deleteSecurityUsersGroup(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        testEntityManager.clear();
        testEntityManager.flush();

        assertNotNull(aLong);

        TSecurityGroups tSecurityGroups1 = testEntityManager.find(TSecurityGroups.class, tSecurityGroups.getId());
        assertNotNull(tSecurityGroups1);
        assertEquals(1, tSecurityGroups1.getTSecurityGroupUsersList().size());
    }

    @Test
    public void test_C2_deleteSecurityUserGroup_oneRemaining() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        assertThrows(ResponseStatusException.class, () -> {
            Long aLong = securityGroupService.deleteSecurityUsersGroup(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        });
    }

    @Test
    public void test_D_deleteSecurityRightGroup() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        Long aLong = securityGroupService.deleteSecurityGroupRightById(tSecurityGroups.getTSecurityGroupRightsList().get(0).getId());
        testEntityManager.clear();
        testEntityManager.flush();

        assertNotNull(aLong);

        TSecurityGroupRights tSecurityGroupRights = testEntityManager.find(TSecurityGroupRights.class, aLong);
        assertNull(tSecurityGroupRights);
    }

    @Test
    public void test_D_addRightSecurity() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        Long aLong = securityGroupService.addRightSecurity(tSecurityGroups.getId(), EnumRights.CALENDAR_ECRITURE.getId());

        assertNotNull(aLong);

        TSecurityGroupRights securityGroupRights = testEntityManager.find(TSecurityGroupRights.class, aLong);
        assertNotNull(securityGroupRights);

    }

    @Test
    public void test_E_addRightSecurity_forbidden() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        assertThrows(ResponseStatusException.class, () -> {
            Long aLong = securityGroupService.addRightSecurity(tSecurityGroups.getId(), tSecurityGroups.getTSecurityGroupRightsList().get(0).getIdRight().getId());
        });


    }

    @Test
    public void test_F_deleteSecurityGroupById() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        tSecurityGroups.setTSecAppGroupId(EnumSecurityAppGroups.USER);
        testEntityManager.persist(tSecurityGroups);

        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        Long aLong = securityGroupService.deleteSecurityGroupById(tSecurityGroups.getTSecurityGroupUsersList().get(0).getId());

        assertNotNull(aLong);

        TSecurityGroupUsers tSecurityGroupUsers = testEntityManager.find(TSecurityGroupUsers.class, aLong);
        assertNull(tSecurityGroupUsers);

    }

    @Test
    public void test_G_deleteSecurityGroupById_forbidden() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        assertThrows(ResponseStatusException.class, () -> {
            Long aLong = securityGroupService.deleteSecurityGroupById(tSecurityGroups.getTSecurityGroupUsersList().get(0).getId());
        });

    }
}
