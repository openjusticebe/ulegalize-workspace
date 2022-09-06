package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemEventDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.EnumDossierType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.RefCompte;
import com.ulegalize.lawfirm.model.entity.RefPoste;
import com.ulegalize.lawfirm.model.entity.TDossiersType;
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

    @Test
    void test_D_getMatieres_Mediation_FR() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.MD.getDossType(), EnumDossierType.MD.getLabelFr());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.FR.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(50, itemDtos.get(0).getValue());
    }

    @Test
    void test_E_getMatieres_Mediation_EN() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.MD.getDossType(), EnumDossierType.MD.getLabelEn());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.EN.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(50, itemDtos.get(0).getValue());
    }

    @Test
    void test_F_getMatieres_Not_Mediation_FR() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.DF.getDossType(), EnumDossierType.DF.getLabelFr());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.FR.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.get(0).getValue());
    }

    @Test
    void test_G_getMatieres_Not_Mediation_EN() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.DF.getDossType(), EnumDossierType.DF.getLabelEn());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.EN.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.get(0).getValue());
    }

    @Test
    void test_H_getMatieres_Null_Parameter() {

        List<ItemLongDto> itemDtos = searchService.getMatieres(null, EnumLanguage.EN.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.get(0).getValue());
    }
}
