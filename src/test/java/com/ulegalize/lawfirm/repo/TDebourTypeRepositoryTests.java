package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDebourType;
import com.ulegalize.lawfirm.repository.TDebourTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class TDebourTypeRepositoryTests extends EntityTest {
    @Autowired
    private TDebourTypeRepository tDebourTypeRepository;

    @Test
    public void test_A_findAllByVcKeyAndArchived() {
        LawfirmEntity lawfirm = createLawfirm();

        TDebourType tDebourType = createTDebourType(lawfirm);

        List<ItemLongDto> itemLongDtoList = tDebourTypeRepository.findAllByVcKeyAndArchived(lawfirm.getVckey(), false);

        assertNotNull(itemLongDtoList);
        boolean result = itemLongDtoList.stream().allMatch(itemLongDto -> itemLongDto.value.equals(tDebourType.getIdDebourType()));
        assertTrue(result);
    }

    @Test
    public void test_B_getFraisMatiere() {
        LawfirmEntity lawfirm = createLawfirm();

        TDebourType tDebourType = createTDebourType(lawfirm);

        FraisAdminDTO fraisMatiere = tDebourTypeRepository.getFraisMatiere(tDebourType.getIdDebourType(), false);

        assertNotNull(fraisMatiere);
        assertEquals(tDebourType.getIdMesureType(), fraisMatiere.getIdMesureType());
    }
}
