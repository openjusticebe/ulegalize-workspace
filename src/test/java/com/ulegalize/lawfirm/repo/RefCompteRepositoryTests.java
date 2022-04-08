package com.ulegalize.lawfirm.repo;

import com.ulegalize.dto.BankAccountDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.enumeration.EnumAccountType;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.RefCompte;
import com.ulegalize.lawfirm.repository.RefCompteRepository;
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
public class RefCompteRepositoryTests extends EntityTest {

    @Autowired
    private RefCompteRepository refCompteRepository;

    @Test
    public void test_A_getRefCompte() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefCompte refCompte = createRefCompte(lawfirmEntity);

        List<ItemDto> refCompteList = refCompteRepository.findAllOrderBy(lawfirmEntity.getVckey());

        assertNotNull(refCompteList);

        assertEquals(1, refCompteList.size());
        assertEquals(refCompte.getIdCompte(), refCompteList.get(0).getValue());
        assertTrue(refCompteList.get(0).getLabel().contains(refCompte.getCompteNum()));
    }

    @Test
    public void test_B_findAllItemByVcKey() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefCompte refCompte = createRefCompte(lawfirmEntity);

        List<BankAccountDTO> refCompteList = refCompteRepository.findAllItemByVcKey(lawfirmEntity.getVckey());

        assertNotNull(refCompteList);

        assertEquals(1, refCompteList.size());
        assertEquals(refCompte.getIdCompte(), refCompteList.get(0).getCompteId());
        assertEquals(refCompte.getCompteNum(), refCompteList.get(0).getAccountNumber());
    }

    @Test
    public void test_C_findDTOById() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefCompte refCompte = createRefCompte(lawfirmEntity);

        BankAccountDTO bankAccountDTO = refCompteRepository.findDTOById(lawfirmEntity.getVckey(), refCompte.getIdCompte());

        assertNotNull(bankAccountDTO);

        assertEquals(refCompte.getIdCompte(), bankAccountDTO.getCompteId());
        assertEquals(refCompte.getCompteNum(), bankAccountDTO.getAccountNumber());
    }

    @Test
    public void test_D_findDTOByIdAndAccountTypeId() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefCompte refCompte = createRefCompte(lawfirmEntity);

        refCompte.setAccountTypeId(EnumAccountType.PRO_ACCOUNT);
        testEntityManager.persist(refCompte);

        List<ItemDto> refCompteList = refCompteRepository.findDTOByIdAndAccountTypeId(lawfirmEntity.getVckey(), EnumAccountType.PRO_ACCOUNT);

        assertNotNull(refCompteList);

        assertEquals(1, refCompteList.size());
        assertEquals(refCompte.getAccountTypeId(), EnumAccountType.PRO_ACCOUNT);
        assertTrue(refCompteList.get(0).getLabel().contains(refCompte.getCompteNum()));
    }

    @Test
    public void test_E_findDTOByIdAndAccountTypeId_noresult() {
        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefCompte refCompte = createRefCompte(lawfirmEntity);

        refCompte.setAccountTypeId(EnumAccountType.PRO_ACCOUNT);
        testEntityManager.persist(refCompte);

        List<ItemDto> refCompteList = refCompteRepository.findDTOByIdAndAccountTypeId(lawfirmEntity.getVckey(), EnumAccountType.ACCOUNT_TIERS);

        assertNotNull(refCompteList);

        assertEquals(0, refCompteList.size());
    }
}

