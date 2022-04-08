package com.ulegalize.lawfirm.repo;

import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import com.ulegalize.lawfirm.repository.TTimesheetRepository;
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
public class TTimesheetTests extends EntityTest {
    @Autowired
    private TTimesheetRepository timesheetRepository;

    @Test
    public void test_A_findAllByInvoiceIdDossierId() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TFactures facture = createFacture(lawfirm);

        List<Object[]> tTimesheetTypes = timesheetRepository.findAllByInvoiceIdDossierId(facture.getIdFacture(), facture.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getId(), null);

        assertNotNull(tTimesheetTypes);
        assertTrue(tTimesheetTypes.size() > 0);
    }

    @Test
    public void test_B_countAllByIdAndDossierId() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);

        Long tTimesheetTypes = timesheetRepository.countAllByIdAndDossierId(List.of(tTimesheet.getIdTs()), dossier.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(tTimesheetTypes);
        assertEquals(1, tTimesheetTypes.intValue());
    }

    @Test
    public void test_B1_countAllByIdAndDossierId_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);
        TDossiers dossier2 = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Long tTimesheetTypes = timesheetRepository.countAllByIdAndDossierId(List.of(tTimesheet.getIdTs()), dossier2.getIdDoss(), dossier.getDossierRightsList().get(0).getVcUserId());

        assertNotNull(tTimesheetTypes);
        assertEquals(0, tTimesheetTypes.intValue());
    }

}
