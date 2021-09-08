package com.ulegalize.lawfirm.service;

import com.ulegalize.dto.*;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumLanguage;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.lawfirm.model.enumeration.EnumFactureType;
import com.ulegalize.security.EnumRights;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class InvoiceServiceTests extends EntityTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    public void test_A_getInvoicesBySearchCriteria_ByYearfacture_2020_founded() {

        LawfirmEntity lawfirm = createLawfirm();
        TFactures tFactures = createFacture(lawfirm);
        String searchCriteria = String.valueOf(LocalDate.now().getYear());
        List<ItemLongDto> itemLongDtoList = invoiceService
                .getInvoicesBySearchCriteria(lawfirm.getVckey(), searchCriteria);

        assertNotNull(itemLongDtoList);
        assertTrue(itemLongDtoList.get(0).getLabel().toLowerCase()
                .contains(searchCriteria.toLowerCase()));
        assertEquals(itemLongDtoList.get(0).getValue(), tFactures.getIdFacture());

    }

    @Test
    public void test_B_createInvoice() {

        LawfirmEntity lawfirm = createLawfirm();

        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, "", null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setVcKey(lawfirm.getVckey());

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        invoice.setDossierId(dossier.getIdDoss());

        TClients client = createClient(lawfirm);
        invoice.setClientId(client.getId_client());

        // facture type must be an enum
        invoice.setTypeId(EnumFactureType.TEMP.getId());
        invoice.setTypeItem(new ItemLongDto(
                EnumFactureType.TEMP.getId(),
                EnumFactureType.TEMP.getDescription()
        ));

        RefPoste refPoste1 = createRefPoste(lawfirm);
        invoice.setPosteId(refPoste1.getIdPoste());
        invoice.setPosteItem(new ItemDto(refPoste1.getIdPoste(),
                refPoste1.getRefPoste()));

        TFactureEcheance factureEcheance = createFactureEcheance();
        invoice.setEcheanceId(factureEcheance.getID());
        invoice.setDateValue(ZonedDateTime.now());

        invoice.setDateEcheance(invoice.getDateValue().plusDays(7));

        invoice.setYearFacture(2020);
        invoice.setNumFacture(0);
        invoice.setReference("FT-2020-001");
        invoice.setValid(false);

        TVirtualcabVat virtual = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));
        ItemBigDecimalDto itemBigDecimalDto = new ItemBigDecimalDto(virtual.getVAT(), virtual.getVAT().setScale(2, RoundingMode.HALF_UP) + " %");

        invoice.setInvoiceDetailsDTOList(new ArrayList<>());

        invoice.getInvoiceDetailsDTOList().add(new InvoiceDetailsDTO(
                null, null, "test",
                itemBigDecimalDto.getValue(),
                itemBigDecimalDto, BigDecimal.ONE,
                BigDecimal.ONE
        ));

        invoice.setMontant(BigDecimal.TEN);

        Long invoiceId = invoiceService.createInvoice(invoice, lawfirm.getVckey());
        TFactures tFactures = testEntityManager.find(TFactures.class, invoiceId);

        assertEquals(tFactures.getMontant(), invoice.getMontant());

    }

    @Test
    public void test_C_updateInvoice() {
        LawfirmEntity lawfirm = createLawfirm();

        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, "", null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(tFactures.getIdFacture(), lawfirm.getVckey());

        invoiceDTO.setDateValue(ZonedDateTime.now().minusDays(1));

        invoiceService.updateInvoice(invoiceDTO, lawfirm.getVckey());

        TFactures tFacturesUpdated = testEntityManager.find(TFactures.class, tFactures.getIdFacture());

        assertEquals(tFacturesUpdated.getDateValue(), tFactures.getDateValue());
    }

    @Test
    public void test_C_deleteInvoiceById() {

        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);

        Long idInvoiceDeleted = invoiceService.deleteInvoiceById(tFactures.getIdFacture());

        TFactures factureDeleted = testEntityManager.find(TFactures.class, tFactures.getIdFacture());

        assertEquals(idInvoiceDeleted, tFactures.getIdFacture());
        assertNull(factureDeleted);

    }
}
