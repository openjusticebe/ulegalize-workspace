package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactureTimesheet;
import com.ulegalize.lawfirm.model.entity.TFactures;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TFactureTimesheetRepositoryTest extends EntityTest {
    @Autowired
    TFactureTimesheetRepository tFactureTimesheetRepository;

    @Test
    void findAllByTFactures() {
        LawfirmEntity lawfirmEntity = createLawfirm("NEWCAB");
        TFactures tFactures = createFacture(lawfirmEntity, 1);

        List<TFactureTimesheet> factureTimesheet = tFactureTimesheetRepository.findAllByTFactures(tFactures.getIdFacture());
        assertNotNull(factureTimesheet);

    }
}
