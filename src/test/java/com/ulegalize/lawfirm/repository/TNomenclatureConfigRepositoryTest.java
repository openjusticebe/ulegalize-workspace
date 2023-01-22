package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TNomenclatureConfig;
import com.ulegalize.lawfirm.model.entity.TVirtualcabNomenclature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TNomenclatureConfigRepositoryTest extends EntityTest {

    @Autowired
    private TNomenclatureConfigRepository tNomenclatureConfigRepository;

    @Test
    void test_A_findAllByVcNomenclature() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        createNomenclatureConfig(nomenclature1, "Config1");
        createNomenclatureConfig(nomenclature1, "Config2");
        createNomenclatureConfig(nomenclature1, "Config3");
        List<TNomenclatureConfig> nomenclatureConfigs = tNomenclatureConfigRepository.findAllByVcNomenclature(nomenclature1);

        assertNotNull(nomenclatureConfigs);
        assertEquals(3, nomenclatureConfigs.size());
    }

    @Test
    void test_B_findByVcNomenclatureAndLabel() {
        LawfirmEntity lawfirmEntity = createLawfirm("TEST");
        TVirtualcabNomenclature nomenclature1 = createVirtualcabNomenclature(lawfirmEntity, "Nomenclature1");

        TNomenclatureConfig nomenclatureConfig1 = createNomenclatureConfig(nomenclature1, "Config1");

        Optional<TNomenclatureConfig> nomenclatureConfig = tNomenclatureConfigRepository.findByVcNomenclatureAndLabel(nomenclature1, "Config1");

        assertNotNull(nomenclatureConfig);
        assertFalse(nomenclatureConfig.isEmpty());
        assertEquals(nomenclatureConfig.get().getLabel(), nomenclatureConfig1.getLabel());
    }
}