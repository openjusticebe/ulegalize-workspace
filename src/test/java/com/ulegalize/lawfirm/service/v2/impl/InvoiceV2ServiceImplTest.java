package com.ulegalize.lawfirm.service.v2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.InvoiceDTO;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.repository.TFacturesRepository;
import com.ulegalize.lawfirm.service.impl.InvoiceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class InvoiceV2ServiceImplTest extends EntityTest {

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @Autowired
    private TFacturesRepository facturesRepository;

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
    void test_A_countAllActiveByVcKey_validated_invoice() {
        TFactures tFactures = createFacture(lawfirm, 1);
        tFactures.setValid(true);

        Long result = invoiceService.countAllActiveByVcKey(lawfirm.getVckey());

        assertEquals(1, result);
    }

    @Test
    void test_B_countAllActiveByVcKey_not_validated_invoice() {
        TFactures tFactures = createFacture(lawfirm, 1);
        tFactures.setValid(false);

        Long result = invoiceService.countAllActiveByVcKey(lawfirm.getVckey());

        assertEquals(0, result);
    }

    @Test
    void test_C_validateInvoice_Invoice_Already_Validate_For_Same_Year() {

        TFactures tFactures = createOnlyFacture(lawfirm, 1, 1, EnumFactureType.TEMP, ZonedDateTime.now(), false);

        TFactures tFactures2 = createOnlyFacture(lawfirm, 2, 1, EnumFactureType.SELL, ZonedDateTime.now(), true);

        TFactures tFactures3 = createOnlyFacture(lawfirm, 3, 2, EnumFactureType.SELL, ZonedDateTime.now(), true);

        InvoiceDTO invoiceDTO = invoiceService.validateInvoice(tFactures.getIdFacture(), lawfirm.getVckey());

        assertEquals(3, facturesRepository.countInvoiceByVcAndYear(lawfirm.getVckey(), EnumFactureType.SELL, ZonedDateTime.now().getYear()));
    }

    @Test
    void test_D_validateInvoice_Invoice_Not_Validate_For_Year() {
        ZonedDateTime yearPlusOne = ZonedDateTime.now().plusYears(1);

        TFactures tFactures = createOnlyFacture(lawfirm, 1, 1, EnumFactureType.TEMP, yearPlusOne, false);

        TFactures tFactures2 = createOnlyFacture(lawfirm, 2, 2, EnumFactureType.SELL, ZonedDateTime.now(), true);

        InvoiceDTO invoiceDTO = invoiceService.validateInvoice(tFactures.getIdFacture(), lawfirm.getVckey());

        assertEquals(1, facturesRepository.countInvoiceByVcAndYear(lawfirm.getVckey(), EnumFactureType.SELL, yearPlusOne.getYear()));
    }

    @Test
    void test_E_getInvoicesBySearchCriteria_With_DossierID() {
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFactures tFactures = createOnlyFacture(lawfirm, 1, 1, EnumFactureType.SELL, ZonedDateTime.now(), true);
        tFactures.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures2 = createOnlyFacture(lawfirm, 2, 2, EnumFactureType.SELL, ZonedDateTime.now(), true);
        tFactures2.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures3 = createOnlyFacture(lawfirm, 3, 3, EnumFactureType.SELL, ZonedDateTime.now(), true);

        List<ItemLongDto> itemLongDtoList = invoiceService.getInvoicesBySearchCriteria(lawfirm.getVckey(), "%", tDossiers.getIdDoss());

        assertEquals(2, itemLongDtoList.size());
    }

    @Test
    void test_F_getInvoicesBySearchCriteria_Without_DossierID() {
        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        TFactures tFactures = createOnlyFacture(lawfirm, 1, 1, EnumFactureType.SELL, ZonedDateTime.now(), true);
        tFactures.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures2 = createOnlyFacture(lawfirm, 2, 2, EnumFactureType.SELL, ZonedDateTime.now(), true);
        tFactures2.setIdDoss(tDossiers.getIdDoss());

        TFactures tFactures3 = createOnlyFacture(lawfirm, 3, 3, EnumFactureType.SELL, ZonedDateTime.now(), true);

        List<ItemLongDto> itemLongDtoList = invoiceService.getInvoicesBySearchCriteria(lawfirm.getVckey(), "%", null);

        assertEquals(3, itemLongDtoList.size());
    }
}
