package com.ulegalize.lawfirm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TVirtualCabTags;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class VirtualcabTagsRepositoryTest extends EntityTest {

    @Autowired
    private VirtualcabTagsRepository virtualcabTagsRepository;

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

        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);
    }

    @Test
    void test_A_findTVirtualCabTagsBySearchCriteria() {

        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);

        String searchCriteria = tVirtualCabTags.getLabel();

        List<ItemLongDto> tVirtualCabTagsList = virtualcabTagsRepository.findTVirtualCabTagsBySearchCriteria(lawfirm.getVckey(), searchCriteria);

        assertNotNull(tVirtualCabTagsList);

        assertEquals(tVirtualCabTags.getLabel(), tVirtualCabTagsList.get(0).getLabel());
    }

    @Test
    void test_B_findTVirtualCabTagsBySearchCriteria_Multiple_tags() {

        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TVirtualCabTags tVirtualCabTags2 = createTags(lawfirm);
        tVirtualCabTags2.setLabel("Tag2");
        TVirtualCabTags tVirtualCabTags3 = createTags(lawfirm);
        tVirtualCabTags3.setLabel("Tag3");

        String searchCriteria = "tag";

        List<ItemLongDto> tVirtualCabTagsList = virtualcabTagsRepository.findTVirtualCabTagsBySearchCriteria(lawfirm.getVckey(), searchCriteria);

        assertNotNull(tVirtualCabTagsList);
        assertEquals(3, tVirtualCabTagsList.size());
    }

    @Test
    void test_C_findTVirtualCabTagsByLabel_Label_Exist() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);

        Optional<TVirtualCabTags> tVirtualCabTagsOptional = virtualcabTagsRepository.findTVirtualCabTagsByLabel(tVirtualCabTags.getLabel(), lawfirm.getVckey());

        assertNotNull(tVirtualCabTagsOptional);

        assertEquals(tVirtualCabTagsOptional.get().getLabel(), tVirtualCabTags.getLabel());
    }

    @Test
    void test_D_findTVirtualCabTagsByLabel_Label_Does_Not_Exist() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);

        Optional<TVirtualCabTags> tVirtualCabTagsOptional = virtualcabTagsRepository.findTVirtualCabTagsByLabel("test", lawfirm.getVckey());

        assertEquals(tVirtualCabTagsOptional, Optional.empty());
    }
}
