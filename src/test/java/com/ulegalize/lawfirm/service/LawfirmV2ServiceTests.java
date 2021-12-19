package com.ulegalize.lawfirm.service;

import com.auth0.json.mgmt.users.User;
import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.service.v2.LawfirmV2Service;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class LawfirmV2ServiceTests extends EntityTest {

    @Autowired
    private LawfirmV2Service lawfirmV2Service;

    @Test
    public void test_A_deleteTempVcKey() throws Exception {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, "", null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User();
        user.setEmail(email);
        user.setFamilyName("family");
        user.setGivenName("first");
        Boolean tempVc = lawfirmV2Service.deleteTempVcKey(lawfirm.getVckey());

        assertTrue(tempVc);

    }

    @Test
    public void test_B_getLawfirmInfoByVcKey() throws Exception {
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(),
                lawfirm.getLawfirmUsers().get(0).getUser().getEmail(),
                lawfirm.getLawfirmUsers().get(0).getUser().getEmail(),
                lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LawfirmDTO lawfirmDTO = lawfirmV2Service.getLawfirmInfoByVcKey(lawfirmToken.getVcKey());

        assertNotNull(lawfirmDTO);
        assertNotNull(lawfirmDTO.getVckey());

    }

}
