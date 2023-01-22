package com.ulegalize.lawfirm.service.v2.impl;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class VirtualCabTagsServiceImplTest extends EntityTest {

    @Autowired
    private VirtualCabTagsServiceImpl virtualCabTagsService;

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

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void test_A_getTagsByVckey() {

        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TVirtualCabTags tVirtualCabTags2 = createTags(lawfirm);
        tVirtualCabTags2.setLabel("Tag2");
        TVirtualCabTags tVirtualCabTags3 = createTags(lawfirm);
        tVirtualCabTags3.setLabel("Tag3");

        String searchCriteria = "tag";

        List<ItemLongDto> itemLongDtoList = virtualCabTagsService.getTagsByVckey(searchCriteria);

        assertNotNull(itemLongDtoList);

        assertEquals(3, itemLongDtoList.size());
    }

    @Test
    void test_B_getTagsByVckey_criteria_null() {

        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);
        TVirtualCabTags tVirtualCabTags2 = createTags(lawfirm);
        tVirtualCabTags2.setLabel("Tag2");
        TVirtualCabTags tVirtualCabTags3 = createTags(lawfirm);
        tVirtualCabTags3.setLabel("Tag3");

        List<ItemLongDto> itemLongDtoList = virtualCabTagsService.getTagsByVckey(null);

        assertNotNull(itemLongDtoList);

        assertEquals(3, itemLongDtoList.size());
    }

    @Test
    void test_C_findTVirtualCabTagsByLabel_Should_Throw_Error() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);

        assertThrows(ResponseStatusException.class, () -> {
            virtualCabTagsService.findTVirtualCabTagsByLabel("test");
        });
    }

    @Test
    void test_D_findTVirtualCabTagsByLabel_Should_Return_Result() {
        TVirtualCabTags tVirtualCabTags = createTags(lawfirm);

        Optional<TVirtualCabTags> tVirtualCabTagsOptional = virtualCabTagsService.findTVirtualCabTagsByLabel(tVirtualCabTags.getLabel());

        assertNotNull(tVirtualCabTagsOptional);
        assertEquals(tVirtualCabTags.getLabel(), tVirtualCabTagsOptional.get().getLabel());
    }
}
