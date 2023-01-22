package com.ulegalize.lawfirm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
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

    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

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