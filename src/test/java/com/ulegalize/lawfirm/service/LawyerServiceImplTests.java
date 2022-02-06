package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.exception.LawfirmBusinessException;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TUsers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class LawyerServiceImplTests extends EntityTest {
    @Autowired
    private LawyerService lawyerService;

    @Test
    void test_A_getFilterLawyer() throws LawfirmBusinessException {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TUsers user = lawfirmEntity.getLawfirmUsers().get(0).getUser();

        List<LawyerDTO> filterLawyer = lawyerService.getFilterLawyer(user.getFullname().substring(0, 3), null, null);

        assertNotNull(filterLawyer);
        assertTrue(filterLawyer.size() > 0);

    }
}