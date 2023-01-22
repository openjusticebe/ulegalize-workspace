package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.EmailsEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class EmailsEntityRepositoryTest extends EntityTest {
    @Autowired
    private EmailsEntityRepository emailsEntityRepository;

    @Test
    void test_A_findEmailsEntitiesByReminderDate_ReminderNotSent() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        EmailsEntity emailsEntity = createEmailsEntity(lawfirmEntity);

        emailsEntity.setReminderDate(ZonedDateTime.now().minusDays(1));

        // simulate call
        List<EmailsEntity> emailsEntities = emailsEntityRepository.findEmailsEntitiesByReminderDate(ZonedDateTime.now());

        assertTrue(emailsEntities.size() > 0);
    }

    @Test
    void test_A_findEmailsEntitiesByReminderDate_alreadyReminderSent() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        EmailsEntity emailsEntity = createEmailsEntity(lawfirmEntity);

        emailsEntity.setReminderDate(ZonedDateTime.now().minusDays(1));
        emailsEntity.setReminderSent(true);

        // simulate call
        List<EmailsEntity> emailsEntities = emailsEntityRepository.findEmailsEntitiesByReminderDate(ZonedDateTime.now());

        assertEquals(0, emailsEntities.size());
    }
}