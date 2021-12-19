package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.LawfirmDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.repository.LawfirmRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class LawfirmRepositoryTests extends EntityTest {
    @Autowired
    private LawfirmRepository lawfirmRepository;

    @Test
    public void test_A_searchLawfirmDTOByVckey() {
        LawfirmEntity lawfirm = createLawfirm();

        List<LawfirmDTO> lawfirmDTOS = lawfirmRepository.searchLawfirmDTOByVckey(lawfirm.getVckey().substring(0, 3));
        assertNotNull(lawfirmDTOS);

        assertEquals(1, lawfirmDTOS.size());
        assertEquals(lawfirm.getVckey(), lawfirmDTOS.get(0).getVckey());

    }

    @Test
    public void test_B_searchLawfirmDTOByVckey() {
        LawfirmEntity lawfirm = createLawfirm();

        Optional<LawfirmDTO> lawfirmDTOOptional = lawfirmRepository.findLawfirmDTOByVckey(lawfirm.getVckey());
        assertNotNull(lawfirmDTOOptional);

        assertTrue(lawfirmDTOOptional.isPresent());

    }
}
