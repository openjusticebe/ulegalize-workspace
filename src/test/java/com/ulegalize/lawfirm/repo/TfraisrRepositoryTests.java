package com.ulegalize.lawfirm.repo;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFrais;
import com.ulegalize.lawfirm.model.enumeration.EnumTType;
import com.ulegalize.lawfirm.repository.TFraisRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class TfraisrRepositoryTests extends EntityTest {
    @Autowired
    private TFraisRepository tFraisRepository;

    @Test
    public void test_A_sumAllJusticeByVcKey() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setFraisProcedure(true);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllDeboursByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_B_sumAllCollabByVcKey() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setFraisCollaboration(true);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllCollabByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_C_sumAllHonoByVcKey() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.SORTIE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_C_sumAllHonoByVcKey_negative() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.ENTREE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht().negate(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

}
