package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.TMatieres;
import com.ulegalize.lawfirm.repository.TMatieresRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class MatieresRepositoryTests extends EntityTest {
    @Autowired
    private TMatieresRepository matieresRepository;

    @Test
    public void test_A_getMatieres() {
        TMatieres matieres = createMatieres();

        List<ItemDto> matieresList = matieresRepository.findAllOrderByMatiereDesc();
        assertNotNull(matieresList);

        assertEquals(2, matieresList.size());
        assertEquals(matieres.getIdMatiere(), matieresList.get(0).getValue());

    }
}
