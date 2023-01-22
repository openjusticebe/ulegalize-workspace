package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.EnumFactureType;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TFactureDetails;
import com.ulegalize.lawfirm.model.entity.TFactures;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
//@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class TFacturesRepositoryTests extends EntityTest {
    @Autowired
    TFacturesRepository tFacturesRepository;

    @Test
    public void test_A_getInvoicesByVcKey() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        List<TFactures> tFacturesList = tFacturesRepository.findAll(lawfirmEntity.getVckey());

        assertNotNull(tFacturesList);
        assertEquals(1, tFacturesList.size());
        assertEquals(lawfirmEntity.getVckey(), tFacturesList.get(0).getVcKey());


    }

    @Test
    public void test_B_getInvoiceById() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        Optional<TFactures> factureFounded = tFacturesRepository.findById(tFactures.getIdFacture());

        assertNotNull(factureFounded);
        assertTrue(factureFounded.isPresent());
        assertEquals(tFactures.getIdEcheance(), factureFounded.get().getIdEcheance());
    }

    @Test
    public void test_C_findAllWithPagination() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(order, order2));
        Page<TFactures> allInvoices = tFacturesRepository.findAllWithPagination(lawfirmEntity.getVckey(), null, null, null, pageable);
        assertNotNull(allInvoices);

        boolean anyMatch = allInvoices.stream().anyMatch(factures -> factures.getNumFacture().equals(tFactures.getNumFacture()));
        assertTrue(anyMatch);
    }

    @Test
    public void test_D_findAllWithPagination_byClient() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(order, order2));
        Page<TFactures> allInvoices = tFacturesRepository.findAllWithPagination(lawfirmEntity.getVckey(), null, null, tFactures.getTClients().getF_nom(), pageable);
        assertNotNull(allInvoices);

        boolean anyMatch = allInvoices.stream().anyMatch(factures -> factures.getNumFacture().equals(tFactures.getNumFacture()));
        assertTrue(anyMatch);
    }

    @Test
    public void test_E_findAllWithPagination_noResult() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(order, order2));
        Page<TFactures> allInvoices = tFacturesRepository.findAllWithPagination(lawfirmEntity.getVckey(), null, null, tFactures.getTClients().getF_nom() + "Result", pageable);
        assertNotNull(allInvoices);
        assertEquals(0, allInvoices.getTotalElements());

    }

    @Test
    public void test_F_sumHtvaInvoiceByVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        BigDecimal sum = tFacturesRepository.sumHtvaInvoiceByVcKey(lawfirmEntity.getVckey(), tFactures.getIdDoss());
        assertNotNull(sum);
        BigDecimal sumExpected = tFactures.getTFactureDetailsList().stream().map(TFactureDetails::getHtva)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(sumExpected.toBigInteger(), sum.toBigInteger());

    }

    @Test
    public void test_H_sumTvacInvoiceByVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        BigDecimal sum = tFacturesRepository.sumTvacInvoiceByVcKey(lawfirmEntity.getVckey(), tFactures.getIdDoss());
        assertNotNull(sum);
        assertEquals(tFactures.getMontant().toBigInteger(), sum.toBigInteger());
        BigDecimal sumExpected = tFactures.getTFactureDetailsList().stream().map(TFactureDetails::getTtc)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(sumExpected.toBigInteger(), sum.toBigInteger());

    }

    @Test
    void test_I_countAllActiveByVcKey_not_validated_invoice() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TFactures tFactures = createFacture(lawfirmEntity, 1);

        Long result = tFacturesRepository.countAllActiveByVcKey(lawfirmEntity.getVckey());

        assertEquals(0, result);
    }

    @Test
    void test_J_countAllActiveByVcKey_validated_invoice() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TFactures tFactures = createFacture(lawfirmEntity, 1);
        tFactures.setValid(true);

        Long result = tFacturesRepository.countAllActiveByVcKey(lawfirmEntity.getVckey());

        assertEquals(1, result);
    }

    @Test
    void test_H_countInvoiceByVcAndYear() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TFactures tFactures = createOnlyFacture(lawfirmEntity, 1, 1, EnumFactureType.TEMP, ZonedDateTime.now(), false);
        TFactures tFactures2 = createOnlyFacture(lawfirmEntity, 2, 1, EnumFactureType.SELL, ZonedDateTime.now(), true);
        TFactures tFactures3 = createOnlyFacture(lawfirmEntity, 3, 2, EnumFactureType.SELL, ZonedDateTime.now(), true);

        Long result = tFacturesRepository.countInvoiceByVcAndYear(lawfirmEntity.getVckey(), EnumFactureType.SELL, tFactures.getYearFacture());

        assertEquals(2, result);
    }

    @Test
    void test_I_countInvoiceByVcAndYear_RETURN_ZERO() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TFactures tFactures = createOnlyFacture(lawfirmEntity, 1, 1, EnumFactureType.TEMP, ZonedDateTime.now(), false);
        TFactures tFactures2 = createOnlyFacture(lawfirmEntity, 2, 1, EnumFactureType.SELL, ZonedDateTime.now(), true);
        TFactures tFactures3 = createOnlyFacture(lawfirmEntity, 3, 2, EnumFactureType.SELL, ZonedDateTime.now(), true);

        Long result = tFacturesRepository.countInvoiceByVcAndYear(lawfirmEntity.getVckey(), EnumFactureType.SELL, ZonedDateTime.now().plusYears(1).getYear());

        assertEquals(0, result);
    }

    @Test
    void test_J_findInvoicesByDossierId_With_DossierID() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TDossiers tDossiers = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        TFactures tFactures = createOnlyFacture(lawfirmEntity, 1, 1, EnumFactureType.SELL, ZonedDateTime.now(), false);
        tFactures.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures2 = createOnlyFacture(lawfirmEntity, 2, 2, EnumFactureType.SELL, ZonedDateTime.now(), false);
        tFactures2.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures3 = createOnlyFacture(lawfirmEntity, 3, 3, EnumFactureType.SELL, ZonedDateTime.now(), false);

        List<ItemLongDto> itemLongDtoList = tFacturesRepository.findInvoicesBySearchCriteria(lawfirmEntity.getVckey(), "%", tDossiers.getIdDoss());

        assertEquals(2, itemLongDtoList.size());
    }

    @Test
    void test_K_findInvoicesByDossierId_Without_DossierID() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TDossiers tDossiers = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);

        TFactures tFactures = createOnlyFacture(lawfirmEntity, 1, 1, EnumFactureType.SELL, ZonedDateTime.now(), false);
        tFactures.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures2 = createOnlyFacture(lawfirmEntity, 2, 2, EnumFactureType.SELL, ZonedDateTime.now(), false);
        tFactures2.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures3 = createOnlyFacture(lawfirmEntity, 3, 3, EnumFactureType.SELL, ZonedDateTime.now(), false);

        List<ItemLongDto> itemLongDtoList = tFacturesRepository.findInvoicesBySearchCriteria(lawfirmEntity.getVckey(), "%", null);

        assertEquals(3, itemLongDtoList.size());
    }

}
