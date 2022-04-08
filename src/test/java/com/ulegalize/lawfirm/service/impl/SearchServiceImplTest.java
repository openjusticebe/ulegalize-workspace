package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemEventDto;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.RefCompte;
import com.ulegalize.lawfirm.model.entity.RefPoste;
import com.ulegalize.lawfirm.service.SearchService;
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
public class SearchServiceImplTest extends EntityTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void test_A_getRefCompte() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefCompte refCompte = createRefCompte(lawfirmEntity);

        List<ItemDto> refCompteList = searchService.getRefCompte(lawfirmEntity.getVckey());

        assertNotNull(refCompteList);
        assertEquals(refCompte.getIdCompte(), refCompteList.get(0).getValue());
        assertTrue(refCompteList.get(0).getLabel().contains(refCompte.getCompteNum()));
    }

    @Test
    public void test_B_getRefPoste() {

        LawfirmEntity lawfirmEntity = createLawfirm("MYLAW");
        RefPoste refPoste = createRefPoste(lawfirmEntity);

        List<ItemDto> refPosteList = searchService.getPostes(lawfirmEntity.getVckey());

        assertNotNull(refPosteList);
        assertEquals(refPoste.getIdPoste(), refPosteList.get(0).getValue());
        assertEquals(refPoste.getRefPoste(), refPosteList.get(0).getLabel());
//        assertEquals(refPoste.getHonoraires(), refPosteList.get(0).getDefault());
    }

    @Test
    void test_C_getCalendarEventType() {

        EnumLanguage enumLanguage = EnumLanguage.fromshortCode(EnumLanguage.FR.getShortCode());
        List<ItemEventDto> itemEventDtoList = searchService.getCalendarEventType(enumLanguage);

        assertNotNull(itemEventDtoList);
    }
}
