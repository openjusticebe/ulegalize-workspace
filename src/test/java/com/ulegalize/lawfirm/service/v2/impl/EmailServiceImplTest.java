package com.ulegalize.lawfirm.service.v2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.ProfileDTO;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.EmailsEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.service.EmailService;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class EmailServiceImplTest extends EntityTest {

    private LawfirmEntity lawfirm;
    @Autowired
    private EmailService emailService;

    @Autowired
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm("MYLAW");
    }

    @Test
    void test_A_registeredUser() {
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ProfileDTO profileDTO = new ProfileDTO(lawfirmToken.getUserId(), lawfirmToken.getUserEmail(), lawfirmToken.getUserEmail(), null, lawfirmToken.getVcKey(),
                lawfirmToken.getTemporary(),
                lawfirmToken.getLanguage(),
                lawfirmToken.getSymbolCurrency(),
                lawfirmToken.getUserId(),
                lawfirmToken.getUsername(),
                lawfirmToken.getEnumRights().stream().map(EnumRights::getId).collect(Collectors.toList()),
                lawfirmToken.getDriveType(),
                "",
                "",
                "",
                ZonedDateTime.now(),
                lawfirmToken.isVerified(),
                lawfirmToken.getClientFrom());
        EmailsEntity emailsEntityRegistered = emailService.registeredUser(lawfirmToken.getVcKey(), profileDTO);


        assertTrue(emailsEntityRegistered.isWelcomeSent());
        assertTrue(emailsEntityRegistered.isSupportSent());
        assertFalse(emailsEntityRegistered.isReminderSent());
    }

    @Test
    void test_B_reminderSignup_sent() {
        EmailsEntity emailsEntity1 = createEmailsEntity(lawfirm);
        emailsEntity1.setWelcomeSent(true);
        emailsEntity1.setSupportSent(true);
        emailsEntity1.setReminderDate(ZonedDateTime.now().minusDays(1));

        assertFalse(emailsEntity1.isReminderSent());

        emailService.reminderSignup();

        EmailsEntity emailsEntity = testEntityManager.find(EmailsEntity.class, emailsEntity1.getId());

        assertTrue(emailsEntity.isWelcomeSent());
        assertTrue(emailsEntity.isSupportSent());
        assertTrue(emailsEntity.isReminderSent());
    }
}
