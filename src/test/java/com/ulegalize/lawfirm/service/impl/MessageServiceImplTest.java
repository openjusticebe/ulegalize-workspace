package com.ulegalize.lawfirm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.dto.MessageDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TMessageUser;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.TMessageUserRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class MessageServiceImplTest extends EntityTest {

    @Autowired
    private MessageServiceImpl messageService;
    private LawfirmEntity lawfirm;

    @Autowired
    private TMessageUserRepository tMessageUserRepository;
    private UsernamePasswordAuthenticationToken authentication;

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
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void test_A_getMessage() {
        TMessageUser tMessageUser = createTMessageUser(lawfirm.getLawfirmUsers().get(0).getUser(), true);

        MessageDTO messageDTO = messageService.getMessage();

        assertNotNull(messageDTO);
        assertEquals(tMessageUser.getUserId(), messageDTO.getUserId());
    }

    @Test
    void test_B_getMessage_Null() {
        createTMessageUser(lawfirm.getLawfirmUsers().get(0).getUser(), false);

        MessageDTO messageDTO = messageService.getMessage();

        assertNull(messageDTO);
    }

    @Test
    void test_C_deactivateMessage_If_Message_True() {
        TMessageUser tMessageUser = createTMessageUser(lawfirm.getLawfirmUsers().get(0).getUser(), true);

        messageService.deactivateMessage();

        assertEquals(tMessageUser.getValid(), false);
    }

    @Test
    void testCreateMessage() {

        ZonedDateTime now = ZonedDateTime.now();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageFr("Salut");
        messageDTO.setMessageEn("Hello");
        messageDTO.setMessageDe("Hallo");
        messageDTO.setMessageNl("Goeiedag");
        messageDTO.setDateTo(now);

        TUsers tUsers = createUser("test1234@gmail.com");
        TUsers tUsers2 = createUser("tUsers2@gmail.com");

        messageService.createMessage(messageDTO);

        List<TMessageUser> tMessageUserList = tMessageUserRepository.findAll();

        assertEquals(tMessageUserList.get(0).getUserId(), lawfirm.getLawfirmUsers().get(0).getUser().getId());
    }
}
