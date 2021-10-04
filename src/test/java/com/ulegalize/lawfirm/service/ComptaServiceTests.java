package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumRefTransaction;
import com.ulegalize.lawfirm.model.enumeration.EnumTType;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;


@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ComptaServiceTests extends EntityTest {

    @Autowired
    private ComptaService comptaService;

    @Test
    public void test_A_createTempVc() {
        LawfirmEntity lawfirm = createLawfirm();
        TFrais tFrais = createTFrais(lawfirm);

        ComptaDTO compta = comptaService.getComptaById(tFrais.getIdFrais(), lawfirm.getVckey());

        assertNotNull(compta);
        assertEquals(compta.getId(), tFrais.getIdFrais());
    }

    @Test
    public void test_B_updateCompta() {
        LawfirmEntity lawfirm = createLawfirm();
        TFrais tFrais = createTFrais(lawfirm);

        ComptaDTO compta = comptaService.getComptaById(tFrais.getIdFrais(), lawfirm.getVckey());

        compta.setDateValue(LocalDate.now().minusDays(1));

        comptaService.updateCompta(compta, lawfirm.getVckey());
        TFrais tFrais1 = testEntityManager.find(TFrais.class, tFrais.getIdFrais());

        assertEquals(tFrais1.getDateValue(), compta.getDateValue());
    }

    @Test
    public void test_C_createCompta() {
        LawfirmEntity lawfirm = createLawfirm();

        ComptaDTO compta = new ComptaDTO();
        compta.setVcKey(lawfirm.getVckey());
        // must be an enum
        TGrid grids = createGrids();
        compta.setGridId(grids.getID());
        compta.setMontant(BigDecimal.ZERO);
        RefCompte refCompte = createRefCompte(lawfirm);
        compta.setIdCompte(refCompte.getIdCompte());

        RefPoste refPoste1 = createRefPoste(lawfirm);

        compta.setIdPost(refPoste1.getIdPoste());
        compta.setIdTransaction(EnumRefTransaction.VIREMENT.getId());
        compta.setIdType(EnumTType.ENTREE.getIdType());
        compta.setMontantHt(BigDecimal.ZERO);
        compta.setRatio(BigDecimal.valueOf(100));
        compta.setDateValue(LocalDate.now());
        compta.setDateValue(LocalDate.now());

        compta.setDateValue(LocalDate.now().minusDays(1));

        Long compta1 = comptaService.createCompta(compta, lawfirm.getVckey());
        TFrais tFrais1 = testEntityManager.find(TFrais.class, compta1);

        assertEquals(tFrais1.getDateValue(), compta.getDateValue());
    }

    @Test
    public void test_D_getAllComptaByDossierId_debours() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setFraisProcedure(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), true, null, null, null);

        assertNotNull(compta);
        assertTrue(compta.getTotalElements() > 0);
    }

    @Test
    public void test_E_getAllComptaByDossierId_debours_notFound() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setFraisCollaboration(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), true, null, null, null);

        assertNotNull(compta);
        assertEquals(0, compta.getTotalElements());
    }

    @Test
    public void test_F_getAllComptaByDossierId_FraisCollaboration() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setFraisCollaboration(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), null, true, null, null);

        assertNotNull(compta);
        assertTrue(compta.getTotalElements() > 0);
    }

    @Test
    public void test_G_getAllComptaByDossierId_FraisCollaboration_notFound() {
        LawfirmEntity lawfirm = createLawfirm();

        TFrais tFrais = createTFrais(lawfirm);
        tFrais.getRefPoste().setHonoraires(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), null, true, null, null);

        assertNotNull(compta);
        assertEquals(0, compta.getTotalElements());
    }
}
