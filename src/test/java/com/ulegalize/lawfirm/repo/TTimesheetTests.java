package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.repository.TTimesheetRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class TTimesheetTests extends EntityTest {
    @Autowired
    private TTimesheetRepository timesheetRepository;

    @Test
    public void test_A_findAllByInvoiceIdDossierId() {
        LawfirmEntity lawfirm = createLawfirm();
        TFactures facture = createFacture(lawfirm);

        List<PrestationSummary> tTimesheetTypes = timesheetRepository.findAllByInvoiceIdDossierId(facture.getIdFacture(), facture.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getId());

        assertNotNull(tTimesheetTypes);
        assertTrue(tTimesheetTypes.size() > 0);
    }

}
