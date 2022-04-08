package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.PrestationTypeDTO;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TTimesheetType;
import com.ulegalize.lawfirm.repository.TTimesheetTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class TTimesheetTypeTests extends EntityTest {
    @Autowired
    private TTimesheetTypeRepository tTimesheetTypeRepository;

    @Test
    public void test_A_findAllByVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        List<TTimesheetType> tTimesheetTypes = tTimesheetTypeRepository.findAllByVcKey(lawfirm.getVckey());

        assertNotNull(tTimesheetTypes);
        assertTrue(tTimesheetTypes.size() > 0);
    }

    @Test
    public void test_B_findAllByVcKey_notfound() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");

        List<TTimesheetType> tTimesheetTypes = tTimesheetTypeRepository.findAllByVcKey(lawfirm.getVckey());

        assertNotNull(tTimesheetTypes);
        assertTrue(tTimesheetTypes.size() == 0);
    }

    @Test
    public void test_C_findAllItemByVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        List<PrestationTypeDTO> tTimesheetTypes = tTimesheetTypeRepository.findAllItemByVcKey(lawfirm.getVckey());

        assertNotNull(tTimesheetTypes);
        assertTrue(tTimesheetTypes.size() > 0);
    }

    @Test
    public void test_D_findDTOById() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        PrestationTypeDTO tTimesheetTypes = tTimesheetTypeRepository.findDTOById(lawfirm.getVckey(), tTimesheetType.getIdTs());

        assertNotNull(tTimesheetTypes);
        assertEquals(tTimesheetTypes.getIdTs(), tTimesheetType.getIdTs());
    }

    @Test
    public void test_E_findAllByVcKeyAndArchived() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        List<ItemDto> tTimesheetTypes = tTimesheetTypeRepository.findAllByVcKeyAndArchived(lawfirm.getVckey(), tTimesheetType.getArchived());

        assertNotNull(tTimesheetTypes);
        assertTrue(tTimesheetTypes.size() > 0);
    }
}
