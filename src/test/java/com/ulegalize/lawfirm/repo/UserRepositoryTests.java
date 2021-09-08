package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.TUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
}
