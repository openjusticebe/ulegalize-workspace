package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.ItemStringDto;
import com.ulegalize.dto.ModelDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToTemplateConverter;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TTemplates;
import com.ulegalize.lawfirm.model.enumeration.EnumValid;
import com.ulegalize.lawfirm.service.v2.TemplateV2Service;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
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
public class TemplateV2ServiceTests extends EntityTest {
    @Autowired
    TemplateV2Service templateV2Service;
    @Autowired
    EntityToTemplateConverter entityToTemplateConverter;


    @Test
    public void test_A_getModelsList() {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        creatTTemplates(lawfirm);

        List<ModelDTO> tTemplatesList = templateV2Service.getModelsList();
        assertNotNull(tTemplatesList);
        assertEquals(1, tTemplatesList.size());
    }

    @Test
    public void test_B_updateModels() {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TTemplates tTemplates = creatTTemplates(lawfirm);

        ModelDTO modelDTO = entityToTemplateConverter.apply(tTemplates);

        modelDTO.setDescription("new");

        Long templateId = templateV2Service.updateModels(modelDTO);
        assertNotNull(templateId);

        TTemplates tTemplatesUpdated = testEntityManager.find(TTemplates.class, templateId);
        assertEquals(modelDTO.getDescription(), tTemplatesUpdated.getDescription());
    }

    @Test
    public void test_C_createModels() {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setName("name");
        modelDTO.setContext(EnumContextTemplate.DOSSIER);
        modelDTO.setFormat("D");
        modelDTO.setDescription("desc");
        modelDTO.setType(EnumTypeTemplate.U);
        String template = StringEscapeUtils.unescapeHtml4("template");
        modelDTO.setTemplate(template);
        modelDTO.setTemplateItem(new ItemStringDto(template, template));

        Long templateId = templateV2Service.createModels(modelDTO);
        assertNotNull(templateId);

        TTemplates tTemplatesUpdated = testEntityManager.find(TTemplates.class, templateId);
        assertEquals(modelDTO.getDescription(), tTemplatesUpdated.getDescription());

    }

    @Test
    public void test_D_deleteModels() {
        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        boolean verifyUser = lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED);

        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", verifyUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TTemplates tTemplates = creatTTemplates(lawfirm);

        Long templateId = templateV2Service.deleteModels(tTemplates.getId());
        TTemplates tTemplatesUpdated = testEntityManager.find(TTemplates.class, templateId);
        assertNull(tTemplatesUpdated);
    }
}
