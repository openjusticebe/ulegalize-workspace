package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class DossierRightRepositoryTests extends EntityTest {
    @Autowired
    private TDossierRightsRepository dossierRightsRepository;

    @Test
    public void test_A_getDossierRight() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Long dossiersOptional = dossierRightsRepository.countByDossierIdAndVcOwnerAndVcKey(dossier.getIdDoss(), lawfirm.getVckey());
        assertNotNull(dossiersOptional);

        assertEquals(1L, dossiersOptional);
    }
}
