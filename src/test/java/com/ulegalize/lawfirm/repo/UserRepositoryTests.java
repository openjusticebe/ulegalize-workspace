package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class UserRepositoryTests extends EntityTest {
    @Autowired
    private TUsersRepository usersRepository;


    @Test
    public void test_A_findByEmail() {
        TUsers user = createUser(EMAIL);

        Optional<LawyerDTO> usersOptional = usersRepository.findDTOByEmail(user.getEmail());

        assertNotNull(usersOptional);
        assertTrue(usersOptional.isPresent());
        assertEquals(usersOptional.get().getEmail(), EMAIL);
    }

    @Test
    public void test_B_findDTOByEmailAndValid() {
        TUsers user = createUser(EMAIL);

        Optional<LawyerDTO> usersOptional = usersRepository.findDTOByEmailAndValid(user.getEmail(), EnumValid.VERIFIED);

        assertNotNull(usersOptional);
        assertTrue(usersOptional.isPresent());
        assertEquals(usersOptional.get().getEmail(), EMAIL);
    }
}
