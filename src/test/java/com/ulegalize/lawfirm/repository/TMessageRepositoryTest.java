package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TMessageUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TMessageRepositoryTest extends EntityTest {

    @Autowired
    private TMessageUserRepository tMessageUserRepository;

    @Test
    void test_A_findByUserId_Should_Return_NotNull() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TMessageUser tMessageUser = createTMessageUser(lawfirm.getLawfirmUsers().get(0).getUser(), true);
        ZonedDateTime now = ZonedDateTime.now();
        log.info("now moins 2j : " + now.minusDays(2));

        Optional<TMessageUser> response = tMessageUserRepository.findByUserId(tMessageUser.getUserId(), now.minusDays(2));

        assertTrue(response.isPresent());
    }

    @Test
    void test_B_findByUserId_Should_Return_False() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TMessageUser tMessageUser = createTMessageUser(lawfirm.getLawfirmUsers().get(0).getUser(), true);
        ZonedDateTime now = ZonedDateTime.now();
        log.info("now plus une heure : " + now.plusDays(3));

        Optional<TMessageUser> response = tMessageUserRepository.findByUserId(tMessageUser.getUserId(), now.plusDays(3));

        assertFalse(response.isPresent());
    }

    @Test
    void test_C_findByUserId_Should_Return_False_If_Validity_False() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TMessageUser tMessageUser = createTMessageUser(lawfirm.getLawfirmUsers().get(0).getUser(), false);
        ZonedDateTime now = ZonedDateTime.now();

        Optional<TMessageUser> response = tMessageUserRepository.findByUserId(tMessageUser.getUserId(), now);

        assertFalse(response.isPresent());
    }
}
