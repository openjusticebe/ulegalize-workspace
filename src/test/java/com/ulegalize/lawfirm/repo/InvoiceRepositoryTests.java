package com.ulegalize.lawfirm.repo;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.repository.TFacturesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class InvoiceRepositoryTests extends EntityTest {
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
    public void test_A_getInvoiceById() {
        LawfirmEntity lawfirmEntity = createLawfirm();
        TFactures tFactures = createFacture(lawfirmEntity);

        Optional<TFactures> factureFounded = tFacturesRepository.findById(tFactures.getIdFacture());

        assertNotNull(factureFounded);
        assertTrue(factureFounded.isPresent());
        assertEquals(tFactures.getIdEcheance(), factureFounded.get().getIdEcheance());
    }

}
