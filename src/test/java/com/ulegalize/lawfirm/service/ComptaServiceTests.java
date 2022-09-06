package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.enumeration.EnumRefTransaction;
import com.ulegalize.enumeration.EnumTType;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class ComptaServiceTests extends EntityTest {

    @Autowired
    private ComptaService comptaService;
    private TDossiers dossier;
    private LawfirmEntity lawfirm;

    @BeforeEach
    public void init() {
        lawfirm = createLawfirm("MYLAW");
        dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

    }

    @Test
    public void test_A_createTempVc() {

        TFrais tFrais = createTFrais(lawfirm, dossier);

        ComptaDTO compta = comptaService.getComptaById(tFrais.getIdFrais(), lawfirm.getVckey());

        assertNotNull(compta);
        assertEquals(compta.getId(), tFrais.getIdFrais());
    }

    @Test
    public void test_B_updateCompta() {

        TFrais tFrais = createTFrais(lawfirm, dossier);

        ComptaDTO compta = comptaService.getComptaById(tFrais.getIdFrais(), lawfirm.getVckey());

        compta.setDateValue(LocalDate.now().minusDays(1));

        comptaService.updateCompta(compta, lawfirm.getVckey());
        TFrais tFrais1 = testEntityManager.find(TFrais.class, tFrais.getIdFrais());

        assertEquals(tFrais1.getDateValue(), compta.getDateValue());
    }

    @Test
    public void test_C_createCompta() {


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


        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisProcedure(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), true, null, null, null);

        assertNotNull(compta);
        assertTrue(compta.getTotalElements() > 0);
    }

    @Test
    public void test_E_getAllComptaByDossierId_debours_notFound() {


        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisCollaboration(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), true, null, null, null);

        assertNotNull(compta);
        assertEquals(0, compta.getTotalElements());
    }

    @Test
    public void test_F_getAllComptaByDossierId_FraisCollaboration() {


        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisCollaboration(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), null, true, null, null);

        assertNotNull(compta);
        assertTrue(compta.getTotalElements() > 0);
    }

    @Test
    public void test_G_getAllComptaByDossierId_FraisCollaboration_notFound() {


        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);

        testEntityManager.persist(tFrais.getRefPoste());

        Page<ComptaDTO> compta = comptaService.getAllComptaByDossierId(5, 0, tFrais.getIdDoss(), lawfirm.getVckey(), null, true, null, null);

        assertNotNull(compta);
        assertEquals(0, compta.getTotalElements());
    }
}
