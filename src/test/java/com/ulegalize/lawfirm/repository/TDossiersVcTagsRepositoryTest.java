package com.ulegalize.lawfirm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TDossiersVcTags;
import com.ulegalize.lawfirm.model.entity.TVirtualCabTags;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TDossiersVcTagsRepositoryTest extends EntityTest {

    @Autowired
    private TDossiersVcTagsRepository tDossiersVcTagsRepository;

    private LawfirmEntity lawfirm;

    private UsernamePasswordAuthenticationToken authentication;

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

        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
    }
    @Test
    void test_A_findTagsByDossierID_DossierVCTags_Exist() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TVirtualCabTags tVirtualCabTags2 = createTags(lawfirm);
        tVirtualCabTags2.setLabel("Tag2");

        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TDossiersVcTags tDossiersVcTags = createTDossierVcTags(tDossiers, tVirtualCabTags);

        List<Long> tDossiersVcTagsList = tDossiersVcTagsRepository.findTagsByDossierID(tDossiers.getIdDoss());

        assertTrue(tDossiersVcTagsList.size() > 0);
    }

    @Test
    void test_B_findTagsByDossierID_DossierVCTags_Does_Not_Exist() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        List<Long> tDossiersVcTagsList = tDossiersVcTagsRepository.findTagsByDossierID(tDossiers.getIdDoss());

        assertEquals(tDossiersVcTagsList.size(), 0);
    }


    @Test
    void test_C_findDossierTagsByIdDossandTagsId() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TDossiersVcTags tDossiersVcTags = createTDossierVcTags(tDossiers, tVirtualCabTags);

        Optional<TDossiersVcTags> tDossiersVcTagsOptional = tDossiersVcTagsRepository.findDossierTagsByIdDossandTagsId(tDossiers.getIdDoss(), tVirtualCabTags.getId());

        assertNotNull(tDossiersVcTagsOptional);
        assertTrue(tDossiersVcTagsOptional.isPresent());
        assertEquals(tDossiersVcTagsOptional.get().getId(), tDossiersVcTags.getId());
    }
}
