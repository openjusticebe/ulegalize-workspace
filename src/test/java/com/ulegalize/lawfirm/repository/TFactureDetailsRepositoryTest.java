package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.dto.GroupVatDTO;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.model.entity.TVirtualcabVat;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TFactureDetailsRepositoryTest extends EntityTest {
    @Autowired
    TFactureDetailsRepository tFactureDetailsRepository;

    @Test
    void findItemVat() {
        LawfirmEntity lawfirmEntity = createLawfirm("NEWCAB");
        TFactures tFactures = createFacture(lawfirmEntity, 1);
        TVirtualcabVat tVirtualcabVat = createVirtualcabVat(lawfirmEntity, BigDecimal.valueOf(21));
        createTFactureDetails(tFactures, tVirtualcabVat);

        List<GroupVatDTO> itemVat = tFactureDetailsRepository.findItemVat(tFactures.getIdFacture());
        assertNotNull(itemVat);

    }
}
