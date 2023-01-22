package com.ulegalize.lawfirm.service.v2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import com.ulegalize.lawfirm.service.v2.UserV2Service;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserV2ServiceImplTest extends EntityTest {
    @Autowired
    UserV2Service userV2Service;
    @Autowired
    TUsersRepository tUsersRepository;
    @Autowired
    private MockMvc mvc;

    @Autowired
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser(value = "spring")
    @Test
    void test_A_verifyUser_true() {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        assertTrue(userV2Service.verifyUser(user.getEmail(), user.getHashkey()));

        Optional<TUsers> usersOptional = tUsersRepository.findById(user.getId());
        usersOptional.ifPresent(tUsers -> {
            assertEquals(usersOptional.get().getIdValid(), EnumValid.VERIFIED);
        });

    }

    @WithMockUser(value = "spring")
    @Test
    void test_A_verifyUser_false() {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        assertFalse(userV2Service.verifyUser(user.getEmail(), "test"));
        Optional<TUsers> usersOptional = tUsersRepository.findById(user.getId());
        usersOptional.ifPresent(tUsers -> {
            assertNotEquals(usersOptional.get().getIdValid(), EnumValid.VERIFIED);
        });
    }

    @WithMockUser(value = "spring")
    @Test
    void test_A_verifyUser_exception() {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.UNVERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        assertThrows(ResponseStatusException.class, () -> {
            userV2Service.verifyUser("", "test");

        });
    }

    @Test
    void getLawfirmUserById() {
        TUsers user = createUser("e@cgn.com");
        user.setIdValid(EnumValid.VERIFIED);
        user.setHashkey("fhdgh");
        testEntityManager.persist(user);

        LawyerDTO lawyerDTO = userV2Service.getLawfirmUserById(user.getId());

        assertNotNull(lawyerDTO);
        assertEquals(lawyerDTO.getId(), user.getId());
    }
}