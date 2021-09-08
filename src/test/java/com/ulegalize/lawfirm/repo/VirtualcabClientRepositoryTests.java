package com.ulegalize.lawfirm.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.repository.VirtualcabClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class VirtualcabClientRepositoryTests extends EntityTest {
    @Autowired
    private VirtualcabClientRepository virtualcabClientRepository;


    private LawfirmEntity lawfirm;

    @Autowired
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setupAuthenticate() {
        lawfirm = createLawfirm();
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

    }


    @Test
    public void test_A_findByLawfirm() {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();

        TClients clients = dossierContactClient.get().getClients();

        clients.setF_company(lawfirm.getVckey());
        testEntityManager.persist(clients);

        List<VirtualcabClient> virtualcabClientList = virtualcabClientRepository.findByLawfirm(lawfirm.getVckey());

        assertNotNull(virtualcabClientList);
        assertTrue(virtualcabClientList.size() > 0);
    }

    @Test
    public void test_B_findByLawfirmAAndTClients() {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        Optional<DossierContact> dossierContactClient = dossier.getDossierContactList().stream().filter(dossierContact -> dossierContact.getContactTypeId().equals(EnumDossierContactType.CLIENT)).findAny();
        TClients clients = dossierContactClient.get().getClients();

        Optional<VirtualcabClient> optionalVirtualcabClient = virtualcabClientRepository.findByLawfirmAAndTClients(lawfirm.getVckey(), clients.getId_client());

        assertNotNull(optionalVirtualcabClient);
        assertTrue(optionalVirtualcabClient.isPresent());


        assertEquals(optionalVirtualcabClient.get().getTClients().getId_client(), clients.getId_client());
    }
}