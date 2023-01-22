package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TVirtualcabNomenclatureRepositoryTest extends EntityTest {
    @Autowired
    private TVirtualcabNomenclatureRepository tVirtualcabNomenclatureRepository;

    @Test
    public void test_A_findAllByVckey_should_return_true() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");

        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");
        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature2");

        Pageable pageable = new OffsetBasedPageRequest(5, 0);

        Page<TVirtualcabNomenclature> virtualcabNomenclaturePage = tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntity, pageable);
        assertNotNull(virtualcabNomenclaturePage);

        assertEquals(2, virtualcabNomenclaturePage.getTotalElements());
    }

    @Test
    public void test_B_findAllByVckey_should_return_false() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");

        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");
        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature2");
        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature3");

        Pageable pageable = new OffsetBasedPageRequest(5, 0);

        Page<TVirtualcabNomenclature> virtualcabNomenclaturePage = tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntity, pageable);
        assertNotNull(virtualcabNomenclaturePage);

        assertNotEquals(2, virtualcabNomenclaturePage.getTotalElements());
    }

    @Test
    public void test_C_findByIdAndVckey_should_return_true() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");

        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByIdAndLawfirmEntity(virtualcabNomenclature.getId(), lawfirmEntity);

        assertFalse(virtualcabNomenclatureOptional.isEmpty());

        assertEquals(virtualcabNomenclatureOptional.get().getId(), virtualcabNomenclature.getId());
    }

    @Test
    public void test_D_findByIdAndVckey_should_return_false() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        LawfirmEntity lawfirmEntity2 = createLawfirm("TEST2");

        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByIdAndLawfirmEntity(virtualcabNomenclature.getId(), lawfirmEntity2);

        assertTrue(virtualcabNomenclatureOptional.isEmpty());
    }

    @Test
    void test_E_findAllByVckey() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");
        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature2");
        createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature3");

        List<ItemLongDto> virtualcabNomenclatureDTOS = tVirtualcabNomenclatureRepository.findAllByLawfirmEntityOrderByName(lawfirmEntity);

        assertEquals(3, virtualcabNomenclatureDTOS.size());
        assertEquals(virtualcabNomenclatureDTOS.get(0).getLabel(), virtualcabNomenclature.getName());
    }

    @Test
    void test_F_findByVckeyAndAndName() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByLawfirmEntityAndAndName(lawfirmEntity, virtualcabNomenclature.getName());

        assertTrue(virtualcabNomenclatureOptional.isPresent());
        assertEquals(virtualcabNomenclatureOptional.get().getId(), virtualcabNomenclature.getId());
    }

    @Test
    void test_G_findByVckeyAndAndName() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature virtualcabNomenclature = createVirtualcabNomenclature(lawfirmEntity, "TestNomenclature");
        virtualcabNomenclature.setName(virtualcabNomenclature.getName().toLowerCase());
        testEntityManager.merge(virtualcabNomenclature);

        Optional<TVirtualcabNomenclature> virtualcabNomenclatureOptional = tVirtualcabNomenclatureRepository.findByLawfirmEntityAndAndName(lawfirmEntity, "TestNomenclature");

        assertTrue(virtualcabNomenclatureOptional.isEmpty());
    }
}