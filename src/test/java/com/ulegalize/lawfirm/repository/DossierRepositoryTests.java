package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.IDossierDTO;
import com.ulegalize.enumeration.EnumDossierContactType;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class DossierRepositoryTests extends EntityTest {
    @Autowired
    private DossierRepository dossierRepository;


    @Test
    public void test_A_getMaxDossierByVckey_where_dossiernumber_not_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        dossier.setDossierNumber(4L);

        Long maxDossier = dossierRepository.getMaxDossierByVckey(lawfirm.getVckey());

        assertNotNull(maxDossier);
        Long startNum = 5L;

        assertEquals(startNum, maxDossier);
    }

    @Test
    public void test_B_getMaxDossierByVckey_where_dossiernumber_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Long maxDossier = dossierRepository.getMaxDossierByVckey(lawfirm.getVckey());

        assertNotNull(maxDossier);

        assertEquals(dossier.getDossierNumber() + 1, maxDossier);
    }

    @Test
    public void test_B1_getMaxDossierByVckey_where_dossiernumber_zero() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        lawfirm.setStartDossierNumber(2L);
        testEntityManager.persist(lawfirm);

        Long maxDossier = dossierRepository.getMaxDossierByVckey(lawfirm.getVckey());

        assertNull(maxDossier);

    }

    @Test
    public void test_C_getDossierRight() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Optional<TDossiers> dossiersOptional = dossierRepository.findById(dossier.getIdDoss());
        assertNotNull(dossiersOptional);
        assertTrue(dossiersOptional.isPresent());

        assertEquals(1, dossiersOptional.get().getDossierRightsList().size());
    }

    @Test
    public void test_D_getAffairesByVcUserIdAndSearchCriteria() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Long vcUserId = dossier.getDossierRightsList().get(0).getVcUserId();
        boolean digital = false;
        List<IDossierDTO> dossiersList = dossierRepository.findAffairesByVcUserId(vcUserId, List.of(EnumVCOwner.OWNER_VC.getId(), EnumVCOwner.NOT_OWNER_VC.getId()), "", "", digital);

        assertNotNull(dossiersList);
        assertEquals(1, dossiersList.size());
    }


    @Test
    public void test_E_countByClient_cabAndOrClient_adv() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);


        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

        TClients clients = dossierContactClient.get().getClients();

        Long nbDossier = dossierRepository.countByClient_cabAndOrClient_adv(List.of(clients.getId_client()));
        assertNotNull(nbDossier);

        assertTrue(nbDossier > 0);
    }

    @Test
    public void test_F_findByVcUserIdWithPagination() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

        TClients clients = dossierContactClient.get().getClients();
        clients.setF_company("company");

        testEntityManager.persist(clients);
        List<Integer> integers = List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC).stream().map(EnumVCOwner::getId).collect(Collectors.toList());
        Pageable pageable = new OffsetBasedPageRequest(10, 0, Sort.by(Sort.Direction.DESC, "id_doss"));
        boolean searchArchived = false;
        Page<IDossierDTO> tDossiersList = dossierRepository.findByVcUserIdAllWithPagination(lawfirm.getLawfirmUsers().get(0).getId(), integers,
                "%", null, null, null, searchArchived, null,
                pageable);
        assertNotNull(tDossiersList);

        assertTrue(tDossiersList.getTotalElements() > 0);
    }

    @Test
    public void test_G_findByVcUserIdWithPagination_withBalance() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);
        List<Integer> integers = List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC).stream().map(EnumVCOwner::getId).collect(Collectors.toList());
        Pageable pageable = new OffsetBasedPageRequest(10, 0, Sort.by(Sort.Direction.DESC, "id_doss"));
        boolean searchArchived = false;
        Page<IDossierDTO> tDossiersList = dossierRepository.findByVcUserIdAllWithPagination(lawfirm.getLawfirmUsers().get(0).getId(), integers,
                "%", null, null, null, searchArchived, null,
                pageable);
        assertNotNull(tDossiersList);

        boolean anyMatch = tDossiersList.stream().anyMatch(tDossiers -> tDossiers.getBalance().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(anyMatch);
    }

    @Test
    public void test_H_findByVcUserIdWithPagination_withoutBalance() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);

        tTimesheet.setForfait(true);
        tTimesheet.setForfaitHt(BigDecimal.ZERO);

        testEntityManager.persist(tTimesheet);

        List<Integer> integers = List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC).stream().map(EnumVCOwner::getId).collect(Collectors.toList());
        Pageable pageable = new OffsetBasedPageRequest(10, 0, Sort.by(Sort.Direction.DESC, "id_doss"));
        boolean searchArchived = false;
        Page<IDossierDTO> tDossiersList = dossierRepository.findByVcUserIdAllWithPagination(lawfirm.getLawfirmUsers().get(0).getId(), integers,
                "%", null, null, false, searchArchived, null,
                pageable);
        assertNotNull(tDossiersList);

        boolean anyMatch = tDossiersList.stream().anyMatch(tDossiers -> tDossiers.getBalance().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(anyMatch);
    }

    @Test
    public void test_I_findByVcUserIdAllWithPagination() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        List<Integer> integers = List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC).stream().map(EnumVCOwner::getId).collect(Collectors.toList());
        Pageable pageable = new OffsetBasedPageRequest(10, 0, Sort.by(Sort.Direction.DESC, "id_doss"));
        boolean searchArchived = false;
        Page<IDossierDTO> tDossiersList = dossierRepository.findByVcUserIdAllWithPagination(lawfirm.getLawfirmUsers().get(0).getId(), integers,
                "%", "%", "", null, searchArchived, "%", pageable);
        assertNotNull(tDossiersList);

        assertTrue(tDossiersList.getSize() > 0);
    }

    @Test
    public void test_J_findByIdDoss() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Optional<TDossiers> tDossiers = dossierRepository.findByIdDoss(dossier.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getId(), List.of(EnumVCOwner.OWNER_VC, EnumVCOwner.NOT_OWNER_VC));

        assertTrue(tDossiers.isPresent());
    }

    @Test
    public void test_K_findByIdDossAndVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        LawfirmUsers newUser = createLawfirmUsers(lawfirm, EMAIL);
        testEntityManager.persist(lawfirm);

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TDossierRights tDossierRights = createTDossierRights(dossier, newUser, EnumVCOwner.NOT_SAME_VC);

        Optional<TDossiers> tDossiers = dossierRepository.findByIdDossAndVcKey(dossier.getIdDoss(), lawfirm.getVckey());

        assertTrue(tDossiers.isPresent());
    }

    @Test
    public void test_L_findByIdDossAndVcKey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        LawfirmUsers newUser = createLawfirmUsers(lawfirm, EMAIL);

        testEntityManager.persist(lawfirm);
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TDossierRights tDossierRights = createTDossierRights(dossier, newUser, EnumVCOwner.NOT_SAME_VC);

        List<TDossiers> tDossiers = dossierRepository.findByVcKeyAndDossier(lawfirm.getVckey(), dossier.getIdDoss());

        assertTrue(tDossiers.size() > 0);
    }

    @Test
    public void test_M_countSharedAffaires() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        LawfirmUsers newUser = createLawfirmUsers(lawfirm, EMAIL);

        testEntityManager.persist(lawfirm);
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        TDossierRights tDossierRights = createTDossierRights(dossier, newUser, EnumVCOwner.NOT_SAME_VC);

        Long countDossier = dossierRepository.countSharedAffaires(newUser.getId(), List.of(EnumVCOwner.NOT_SAME_VC));

        assertEquals(1, countDossier);
    }


}
