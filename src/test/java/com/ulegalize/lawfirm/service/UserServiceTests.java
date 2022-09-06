package com.ulegalize.lawfirm.service;

import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class UserServiceTests extends EntityTest {

    @Autowired
    private UserV2Service userV2Service;

    @Test
    public void test_A_changeLanguage() {

        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        assertNotEquals(user.getLanguage(), EnumLanguage.EN.getShortCode());
        userV2Service.changeLanguage(user.getId(), EnumLanguage.EN.getShortCode());

        TUsers tUsers = testEntityManager.find(TUsers.class, user.getId());
        assertEquals(tUsers.getLanguage(), EnumLanguage.EN.getShortCode());

    }

    @Test
    public void test_B_createUser() {

        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        assertNotEquals(user.getLanguage(), EnumLanguage.EN.getShortCode());
        EnumLanguage language = EnumLanguage.fromshortCode(user.getLanguage());

        TUsers users = userV2Service.createUsers("j2@j.com", "workspace", language, true);

        TUsers tUsers = testEntityManager.find(TUsers.class, users.getId());
        assertEquals(tUsers.getEmail(), "j2@j.com");
        assertNotEquals(tUsers.getIdUser(), "ulegal");

    }

    @Test
    public void test_C_createUser_fullname() {

        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        assertNotEquals(user.getLanguage(), EnumLanguage.EN.getShortCode());
        EnumLanguage language = EnumLanguage.fromshortCode(user.getLanguage());

        TUsers users = userV2Service.createUsers("j2@j.com", "workspace", language, true);
        assertEquals(users.getFullname(), "j2");

    }
}
