package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import com.ulegalize.lawfirm.service.PrestationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class PrestationServiceImplTest extends EntityTest {

    @Autowired
    private PrestationService prestationService;

    @Test
    public void test_A_getAllPrestationsByDossierId() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        Page<PrestationSummary> prestations = prestationService.getAllPrestationsByDossierId(20, 0,
                tTimesheet.getIdDoss(),
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(),
                lawfirmEntity.getVckey());

        assertNotNull(prestations);
        assertEquals(1, prestations.getContent().size());

    }

    @Test
    public void test_B_getAllPrestations() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        Page<PrestationSummary> prestations = prestationService.getAllPrestations(20, 0,
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(), lawfirmEntity.getVckey(), null, tTimesheet.getTsType());

        assertNotNull(prestations);
        assertEquals(1, prestations.getContent().size());

    }

    @Test
    public void test_B_getAllPrestations_zero() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.NOT_SAME_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        Page<PrestationSummary> prestations = prestationService.getAllPrestations(20, 0,
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(), lawfirmEntity.getVckey(), null, tTimesheet.getTsType());

        assertNotNull(prestations);
        assertEquals(0, prestations.getContent().size());

    }

    @Test
    public void test_C_getPrestationsByDossierId() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");

        TDossiers dossier = createDossier(lawfirmEntity, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirmEntity, dossier);

        List<PrestationSummary> dossierDTOList = prestationService.getPrestationsByDossierId(tTimesheet.getIdDoss(),
                lawfirmEntity.getLawfirmUsers().get(0).getUser().getId(), lawfirmEntity.getVckey());

        assertNotNull(dossierDTOList);
        assertEquals(1, dossierDTOList.size());

    }
}
