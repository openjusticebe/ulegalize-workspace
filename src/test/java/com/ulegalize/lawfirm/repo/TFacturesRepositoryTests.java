package com.ulegalize.lawfirm.repo;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactureDetails;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import com.ulegalize.lawfirm.repository.TFacturesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class TFacturesRepositoryTests extends EntityTest {
    @Autowired
    TFacturesRepository tFacturesRepository;

    @Test
    public void test_A_getInvoicesByVcKey() {

        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        List<TFactures> tFacturesList = tFacturesRepository.findAll(lawfirmEntity.getVckey());

        assertNotNull(tFacturesList);
        assertEquals(1, tFacturesList.size());
        assertEquals(lawfirmEntity.getVckey(), tFacturesList.get(0).getVcKey());


    }

    @Test
    public void test_B_getInvoiceById() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        Optional<TFactures> factureFounded = tFacturesRepository.findById(tFactures.getIdFacture());

        assertNotNull(factureFounded);
        assertTrue(factureFounded.isPresent());
        assertEquals(tFactures.getIdEcheance(), factureFounded.get().getIdEcheance());
    }

    @Test
    public void test_C_findAllWithPagination() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(order, order2));
        Page<TFactures> allInvoices = tFacturesRepository.findAllWithPagination(lawfirmEntity.getVckey(), null, null, null, null, pageable);
        assertNotNull(allInvoices);

        boolean anyMatch = allInvoices.stream().anyMatch(factures -> factures.getNumFacture().equals(tFactures.getNumFacture()));
        assertTrue(anyMatch);
    }

    @Test
    public void test_D_findAllWithPagination_byClient() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(order, order2));
        Page<TFactures> allInvoices = tFacturesRepository.findAllWithPagination(lawfirmEntity.getVckey(), null, null, null, tFactures.getTClients().getF_nom(), pageable);
        assertNotNull(allInvoices);

        boolean anyMatch = allInvoices.stream().anyMatch(factures -> factures.getNumFacture().equals(tFactures.getNumFacture()));
        assertTrue(anyMatch);
    }

    @Test
    public void test_E_findAllWithPagination_noResult() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "idFactureType");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "factureRef");
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(order, order2));
        Page<TFactures> allInvoices = tFacturesRepository.findAllWithPagination(lawfirmEntity.getVckey(), null, null, null, tFactures.getTClients().getF_nom() + "Result", pageable);
        assertNotNull(allInvoices);
        assertEquals(0, allInvoices.getTotalElements());

    }

    @Test
    public void test_F_sumHtvaInvoiceByVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        BigDecimal sum = tFacturesRepository.sumHtvaInvoiceByVcKey(lawfirmEntity.getVckey(), tFactures.getIdDoss());
        assertNotNull(sum);
        BigDecimal sumExpected = tFactures.getTFactureDetailsList().stream().map(TFactureDetails::getHtva)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(sumExpected.toBigInteger(), sum.toBigInteger());

    }

    @Test
    public void test_H_sumTvacInvoiceByVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        BigDecimal sum = tFacturesRepository.sumTvacInvoiceByVcKey(lawfirmEntity.getVckey(), tFactures.getIdDoss());
        assertNotNull(sum);
        assertEquals(tFactures.getMontant().toBigInteger(), sum.toBigInteger());
        BigDecimal sumExpected = tFactures.getTFactureDetailsList().stream().map(TFactureDetails::getTtc)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(sumExpected.toBigInteger(), sum.toBigInteger());

    }

}
