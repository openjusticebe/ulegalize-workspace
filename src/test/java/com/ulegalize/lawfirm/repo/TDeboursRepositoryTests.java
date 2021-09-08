package com.ulegalize.lawfirm.repo;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDebour;
import com.ulegalize.lawfirm.repository.TDebourRepository;
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
public class TDeboursRepositoryTests extends EntityTest {
    @Autowired
    private TDebourRepository tDebourRepository;

    @Test
    public void test_A_sumByDossierId() {
        LawfirmEntity lawfirm = createLawfirm();

        TDebour tDebour = createTDebour(lawfirm);

        BigDecimal countAllJusticeByVcKey = tDebourRepository.sumByDossierId(tDebour.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getId());

        assertNotNull(countAllJusticeByVcKey);
        BigDecimal result = tDebour.getPricePerUnit().multiply(BigDecimal.valueOf(tDebour.getUnit()));
        assertThat(result, Matchers.comparesEqualTo(countAllJusticeByVcKey));
    }
}
