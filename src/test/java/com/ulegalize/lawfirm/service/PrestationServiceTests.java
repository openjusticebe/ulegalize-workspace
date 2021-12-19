package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class PrestationServiceTests extends EntityTest {

    @Autowired
    private PrestationService prestationService;

    @Test
    public void test_A_getAllPrestationsByDossierId() {

        LawfirmEntity lawfirmEntity = createLawfirm();
        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        List<PrestationSummary> dossierDTOList = prestationService.getAllPrestationsByDossierId(20, 0,
                tTimesheet.getIdDoss(),
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey());

        assertNotNull(dossierDTOList);
        assertEquals(1, dossierDTOList.size());

    }

    @Test
    public void test_B_getAllPrestations() {

        LawfirmEntity lawfirmEntity = createLawfirm();

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        List<PrestationSummary> dossierDTOList = prestationService.getAllPrestations(20, 0,
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(), lawfirmEntity.getVckey());

        assertNotNull(dossierDTOList);
        assertEquals(1, dossierDTOList.size());

    }

    @Test
    public void test_B_getAllPrestations_zero() {

        LawfirmEntity lawfirmEntity = createLawfirm();

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.NOT_SAME_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        List<PrestationSummary> dossierDTOList = prestationService.getAllPrestations(20, 0,
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(), lawfirmEntity.getVckey());

        assertNotNull(dossierDTOList);
        assertEquals(0, dossierDTOList.size());

    }

    @Test
    public void test_C_getPrestationsByDossierId() {

        LawfirmEntity lawfirmEntity = createLawfirm();

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        List<PrestationSummary> dossierDTOList = prestationService.getPrestationsByDossierId(tTimesheet.getIdDoss(),
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(), lawfirmEntity.getVckey());

        assertNotNull(dossierDTOList);
        assertEquals(1, dossierDTOList.size());

    }
}
