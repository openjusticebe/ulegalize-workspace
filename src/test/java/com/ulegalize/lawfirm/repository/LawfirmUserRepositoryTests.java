package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.LawfirmUsers;
import com.ulegalize.lawfirm.model.entity.TDossiers;
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
public class LawfirmUserRepositoryTests extends EntityTest {
    @Autowired
    private LawfirmUserRepository lawfirmUserRepository;

    @Test
    public void test_A_find() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        createLawfirmWebsiteEntity(lawfirm);

        List<LawfirmUsers> lawfirmDTOS = lawfirmUserRepository.findLawfirmUsersByUserId(lawfirm.getLawfirmUsers().get(0).getUser().getId());
        assertNotNull(lawfirmDTOS);

        assertEquals(1, lawfirmDTOS.size());
        assertEquals(lawfirm.getVckey(), lawfirmDTOS.get(0).getLawfirm().getVckey());

    }

    @Test
    public void test_B_findByVcKeyAndDossier_NotExist_alreadyShare() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        // get whole list of user not in the dossier
        List<LawfirmUsers> lawfirmDTOS = lawfirmUserRepository.findByVcKeyAndDossier_NotExist(lawfirm.getVckey(), dossier.getIdDoss());
        assertNotNull(lawfirmDTOS);

        assertEquals(0, lawfirmDTOS.size());

    }

    @Test
    public void test_C_findByVcKeyAndDossier_NotExist_notShared() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        LawfirmEntity lawfirmOther = createLawfirm("MYLAW2");
        TDossiers dossier = createDossier(lawfirmOther, EnumVCOwner.OWNER_VC);

        // get whole list of user not in the dossier
        List<LawfirmUsers> lawfirmDTOS = lawfirmUserRepository.findByVcKeyAndDossier_NotExist(lawfirm.getVckey(), dossier.getIdDoss());
        assertNotNull(lawfirmDTOS);

        assertEquals(1, lawfirmDTOS.size());
        assertEquals(lawfirm.getVckey(), lawfirmDTOS.get(0).getLawfirm().getVckey());

    }

}
