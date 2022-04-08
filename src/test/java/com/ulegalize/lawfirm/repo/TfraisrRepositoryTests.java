package com.ulegalize.lawfirm.repo;

import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.model.entity.TFrais;
import com.ulegalize.lawfirm.model.enumeration.EnumTType;
import com.ulegalize.lawfirm.repository.TFraisRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class TfraisrRepositoryTests extends EntityTest {
    @Autowired
    private TFraisRepository tFraisRepository;

    @Test
    public void test_A_sumAllJusticeByVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisProcedure(true);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllDeboursByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht().negate(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_B_sumAllHtvaCollabByVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisCollaboration(true);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllCollabByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht().negate(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_C_sumAllHtvaHonoByVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.ENTREE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoHtvaByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_D_sumAllHonoTtcByVcKey_negative() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.SORTIE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoTtcByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontant().negate(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_E_sumAllHonoTtcByVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.ENTREE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoTtcByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontant(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_F_sumAllHonoTtcOnlyInvoiceByVcKey_withoutInvoice() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.SORTIE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoTtcOnlyInvoiceByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertEquals(0, countAllJusticeByVcKey.intValue());
    }

    @Test
    public void test_G_sumAllHonoTtcOnlyInvoiceByVcKey_withInvoice() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.ENTREE);

        testEntityManager.persist(tFrais.getRefPoste());

        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);


        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoTtcOnlyInvoiceByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontant(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_H_sumAllHonoByVcKey_negative() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.SORTIE);

        testEntityManager.persist(tFrais.getRefPoste());

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoHtvaByVcKey(tFrais.getIdDoss(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        assertThat(tFrais.getMontantht().negate(), Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }

    @Test
    public void test_I_sumAllHonoTtcByInvoiceId() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());

        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());

        testEntityManager.persist(tFrais);

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoTtcByInvoiceId(tFrais.getIdFacture(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertNotEquals(facture.getMontant().toBigInteger(), countAllJusticeByVcKey.toBigInteger());
    }

    @Test
    public void test_J_sumAllHonoTtcByInvoiceId_equals() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setHonoraires(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());

        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);

        BigDecimal countAllJusticeByVcKey = tFraisRepository.sumAllHonoTtcByInvoiceId(tFrais.getIdFacture(), lawfirm.getVckey());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(facture.getMontant().toBigInteger(), countAllJusticeByVcKey.toBigInteger());
    }

    @Test
    public void test_K_countAllFraisDeboursByIdAndDossierId() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisProcedure(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());
        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setTFactures(facture);
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);
        Long countAllJusticeByVcKey = tFraisRepository.countAllFraisDeboursByIdAndDossierId(List.of(tFrais.getIdFrais()), tFrais.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(1, countAllJusticeByVcKey.intValue());
    }

    @Test
    public void test_K1_fraisDeboursByIdAndDossierId_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);
        // frais collaboration error
        tFrais.getRefPoste().setFraisCollaboration(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());
        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setTFactures(facture);
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);
        Long countAllJusticeByVcKey = tFraisRepository.countAllFraisDeboursByIdAndDossierId(List.of(tFrais.getIdFrais()), tFrais.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(0, countAllJusticeByVcKey.intValue());
    }

    @Test
    public void test_K2_fraisDeboursByIdAndDossierId_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisProcedure(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());
        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setTFactures(facture);
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);
        TDossiers dossier2 = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        // other dossier
        Long countAllJusticeByVcKey = tFraisRepository.countAllFraisDeboursByIdAndDossierId(List.of(tFrais.getIdFrais()), dossier2.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(0, countAllJusticeByVcKey.intValue());
    }

    @Test
    public void test_L_countAllFraisCollaByIdAndDossierId() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisCollaboration(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());
        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setTFactures(facture);
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);
        Long countAllJusticeByVcKey = tFraisRepository.countAllFraisCollaByIdAndDossierId(List.of(tFrais.getIdFrais()), tFrais.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(1, countAllJusticeByVcKey.intValue());
    }

    @Test
    public void test_L1_countAllFraisCollaByIdAndDossierId_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);
        // frais collaboration error
        tFrais.getRefPoste().setFraisProcedure(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());
        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setTFactures(facture);
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);
        Long countAllJusticeByVcKey = tFraisRepository.countAllFraisCollaByIdAndDossierId(List.of(tFrais.getIdFrais()), tFrais.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(0, countAllJusticeByVcKey.intValue());
    }

    @Test
    public void test_L2_countAllFraisCollaByIdAndDossierId_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TFrais tFrais = createTFrais(lawfirm, dossier);
        tFrais.getRefPoste().setFraisCollaboration(true);
        tFrais.setIdType(EnumTType.ENTREE);
        testEntityManager.persist(tFrais.getRefPoste());
        TFactures facture = createFacture(lawfirm);
        tFrais.setIdFacture(facture.getIdFacture());
        tFrais.setTFactures(facture);
        tFrais.setMontant(facture.getMontant());
        tFrais.setMontantht(facture.getMontant().divide(new BigDecimal("1" + tFrais.getTva().toString()), 2, RoundingMode.HALF_UP));

        testEntityManager.persist(tFrais);
        TDossiers dossier2 = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        // other dossier
        Long countAllJusticeByVcKey = tFraisRepository.countAllFraisCollaByIdAndDossierId(List.of(tFrais.getIdFacture()), dossier2.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(countAllJusticeByVcKey);
        // could be different
        assertEquals(0, countAllJusticeByVcKey.intValue());
    }

}
