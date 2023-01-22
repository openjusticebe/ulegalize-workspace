package com.ulegalize.lawfirm.service.impl;

import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemEventDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.RefCompte;
import com.ulegalize.lawfirm.model.entity.RefPoste;
import com.ulegalize.lawfirm.model.entity.TDossiersType;
import com.ulegalize.lawfirm.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class SearchServiceImplTest extends EntityTest {

    @Autowired
    private SearchService searchService;

    private UsernamePasswordAuthenticationToken authentication;
    private LawfirmEntity lawfirm;


    @BeforeEach
    public void setupAuthenticate() {
        lawfirm = createLawfirm("MYLAW");
        Long userId = lawfirm.getLawfirmUsers().get(0).getUser().getId();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        String usermail = lawfirm.getLawfirmUsers().get(0).getUser().getEmail();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void test_A_getRefCompte() {

        RefCompte refCompte = createRefCompte(lawfirm);

        List<ItemDto> refCompteList = searchService.getRefCompte(lawfirm.getVckey());

        assertNotNull(refCompteList);
        assertEquals(refCompte.getIdCompte(), refCompteList.get(0).getValue());
        assertTrue(refCompteList.get(0).getLabel().contains(refCompte.getCompteNum()));
    }

    @Test
    public void test_B_getRefPoste() {


        RefPoste refPoste = createRefPoste(lawfirm);

        List<ItemDto> refPosteList = searchService.getPostes(lawfirm.getVckey());

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
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.MD.getDossType(), EnumDossierType.MD.name());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.FR.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(50, itemDtos.get(0).getValue());
    }

    @Test
    void test_E_getMatieres_Mediation_EN() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.MD.getDossType(), EnumDossierType.MD.name());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.EN.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(50, itemDtos.get(0).getValue());
    }

    @Test
    void test_F_getMatieres_Not_Mediation_FR() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.DF.getDossType(), EnumDossierType.DF.name());
        List<ItemLongDto> itemDtos = searchService.getMatieres(tDossiersType.getDossType(), EnumLanguage.FR.getShortCode());
        log.info("Result : {}", itemDtos);

        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.get(0).getValue());
    }

    @Test
    void test_G_getMatieres_Not_Mediation_EN() {
        TDossiersType tDossiersType = createTDossierType(EnumDossierType.DF.getDossType(), EnumDossierType.DF.name());
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


    @Test
    void test_I_getRefTransaction_In_French() {
        String lang = "fr";

        List<ItemDto> itemDtoList = searchService.getRefTransaction(lang);

        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.get(0).getValue());
    }

    @Test
    void test_J_getRefTransaction_In_En() {
        String lang = "en";

        List<ItemDto> itemDtoList = searchService.getRefTransaction(lang);

        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.get(0).getValue());
    }

    @Test
    void test_K_getDossierContactType_DossierType_DC() {

        String dossierType = "DC";

        List<ItemDto> itemDtoList = searchService.getEnumDossierContactType(dossierType);

        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.get(0).getValue());
        assertEquals(4, itemDtoList.toArray().length);
    }

    @Test
    void test_L_getDossierContactType_DossierType_MD() {

        String dossierType = "MD";

        List<ItemDto> itemDtoList = searchService.getEnumDossierContactType(dossierType);

        assertNotNull(itemDtoList);
        assertEquals(3, itemDtoList.get(0).getValue());
        assertEquals(1, itemDtoList.toArray().length);
    }


}
