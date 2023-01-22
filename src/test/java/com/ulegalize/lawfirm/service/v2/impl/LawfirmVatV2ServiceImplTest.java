package com.ulegalize.lawfirm.service.v2.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumValid;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.model.entity.TVirtualcabVat;
import com.ulegalize.lawfirm.repository.TVirtualcabVatRepository;
import com.ulegalize.lawfirm.service.v2.LawfirmVatV2Service;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class LawfirmVatV2ServiceImplTest extends EntityTest {
    protected static BigDecimal VAT = BigDecimal.valueOf(21);

    @Autowired
    private LawfirmVatV2Service lawfirmVatV2Service;

    @Autowired
    private TVirtualcabVatRepository tVirtualcabVatRepository;

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
    void test_A_deleteVat() {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, VAT);

        // At least 1 must exist
        createVirtualcabVat(lawfirm, BigDecimal.valueOf(12));

        virtualcabVat.setIsDefault(false);

        lawfirmVatV2Service.deleteVat(virtualcabVat.getVAT());

        Optional<TVirtualcabVat> virtualcabVatOptional = tVirtualcabVatRepository.findAllByVcKeyAndVAT(lawfirm.getVckey(), virtualcabVat.getVAT());

        assertTrue(virtualcabVatOptional.isEmpty());
    }

    @Test
    void test_B_deleteVat_with_only_one_virtualcabVat_must_return_exception() {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, VAT);

        virtualcabVat.setIsDefault(false);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                lawfirmVatV2Service.deleteVat(virtualcabVat.getVAT()));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("virtual vat has only one vat remaining"));
    }

    @Test
    void test_C_deleteVat_without_virtualcabVat_must_return_exception() {
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                lawfirmVatV2Service.deleteVat(BigDecimal.valueOf(21)));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("virtual vat not founded"));
    }

    @Test
    void test_D_deleteVat_with_facture_must_return_exception() {
        TFactures facture = createFacture(lawfirm, 1);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                lawfirmVatV2Service.deleteVat(facture.getTFactureDetailsList().get(0).getTva()));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("Number of invoices greather than 0"));
    }

    @Test
    void test_E_deleteVat_with_virtualcabVat_isDefaultIsTrue_must_return_exception() {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, VAT);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () ->
                lawfirmVatV2Service.deleteVat(virtualcabVat.getVAT()));
        assertTrue(Objects.requireNonNull(responseStatusException.getMessage()).contains("This VAT cannot be removed as it is selected by default."));
    }
}