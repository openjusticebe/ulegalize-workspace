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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
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
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

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
    public void test_B2_createInvoice() {

        LawfirmEntity lawfirm = createLawfirm();

        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

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

        // prestation
        invoice.setPrestationIdList(new ArrayList<>());

        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);
        invoice.getPrestationIdList().add(tTimesheet.getIdTs());

        // frais admin
        invoice.setFraisAdminIdList(new ArrayList<>());

        TDebour tDebour = createTDebour(lawfirm, dossier);
        invoice.getFraisAdminIdList().add(tDebour.getIdDebour());

        // debours
        invoice.setDeboursIdList(new ArrayList<>());

        // to link debours
        TFrais frais = createTFrais(lawfirm, dossier);
        frais.getRefPoste().setFraisProcedure(true);
        testEntityManager.persist(frais.getRefPoste());
        invoice.getDeboursIdList().add(frais.getIdFrais());

        // frais collabo
        invoice.setFraisCollaborationIdList(new ArrayList<>());

        // to link colla
        TFrais fraisCol = createTFrais(lawfirm, dossier);
        fraisCol.getRefPoste().setFraisCollaboration(true);
        testEntityManager.persist(frais.getRefPoste());
        invoice.getFraisCollaborationIdList().add(fraisCol.getIdFrais());

        invoice.setMontant(BigDecimal.TEN);

        Long invoiceId = invoiceService.createInvoice(invoice, lawfirm.getVckey());
        TFactures tFactures = testEntityManager.find(TFactures.class, invoiceId);

        assertEquals(tFactures.getMontant(), invoice.getMontant());

    }

    @Test
    public void test_B3_createInvoice_forbidden() {

        LawfirmEntity lawfirm = createLawfirm();

        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, "", null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setVcKey(lawfirm.getVckey());

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);
        invoiceDTO.setDossierId(dossier.getIdDoss());

        TClients client = createClient(lawfirm);
        invoiceDTO.setClientId(client.getId_client());

        // facture type must be an enum
        invoiceDTO.setTypeId(EnumFactureType.TEMP.getId());
        invoiceDTO.setTypeItem(new ItemLongDto(
                EnumFactureType.TEMP.getId(),
                EnumFactureType.TEMP.getDescription()
        ));

        RefPoste refPoste1 = createRefPoste(lawfirm);
        invoiceDTO.setPosteId(refPoste1.getIdPoste());
        invoiceDTO.setPosteItem(new ItemDto(refPoste1.getIdPoste(),
                refPoste1.getRefPoste()));

        TFactureEcheance factureEcheance = createFactureEcheance();
        invoiceDTO.setEcheanceId(factureEcheance.getID());
        invoiceDTO.setDateValue(ZonedDateTime.now());

        invoiceDTO.setDateEcheance(invoiceDTO.getDateValue().plusDays(7));

        invoiceDTO.setYearFacture(2020);
        invoiceDTO.setNumFacture(0);
        invoiceDTO.setReference("FT-2020-001");
        invoiceDTO.setValid(false);

        TVirtualcabVat virtual = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));
        ItemBigDecimalDto itemBigDecimalDto = new ItemBigDecimalDto(virtual.getVAT(), virtual.getVAT().setScale(2, RoundingMode.HALF_UP) + " %");

        invoiceDTO.setInvoiceDetailsDTOList(new ArrayList<>());

        invoiceDTO.getInvoiceDetailsDTOList().add(new InvoiceDetailsDTO(
                null, null, "test",
                itemBigDecimalDto.getValue(),
                itemBigDecimalDto, BigDecimal.ONE,
                BigDecimal.ONE
        ));

        // prestation
        invoiceDTO.setPrestationIdList(new ArrayList<>());

        TTimesheet tTimesheet = createTTimesheet(lawfirm, dossier);
        invoiceDTO.getPrestationIdList().add(tTimesheet.getIdTs());

        // frais admin
        invoiceDTO.setFraisAdminIdList(new ArrayList<>());

        TDebour tDebour = createTDebour(lawfirm, dossier);
        invoiceDTO.getFraisAdminIdList().add(tDebour.getIdDebour());

        // debours
        invoiceDTO.setDeboursIdList(new ArrayList<>());

        TFrais frais = createTFrais(lawfirm, dossier);
        invoiceDTO.getDeboursIdList().add(frais.getIdFrais());

        // frais collabo
        invoiceDTO.setFraisCollaborationIdList(new ArrayList<>());

        TFrais fraisCol = createTFrais(lawfirm, dossier);
        invoiceDTO.getFraisCollaborationIdList().add(fraisCol.getIdFrais());

        invoiceDTO.setMontant(BigDecimal.TEN);
        assertThrows(ResponseStatusException.class, () -> {
            Long invoiceId = invoiceService.createInvoice(invoiceDTO, lawfirm.getVckey());

        });
    }

    @Test
    public void test_C_updateInvoice_frais_notlink_exception() {
        LawfirmEntity lawfirm = createLawfirm();

        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(0L, email, email, "", null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);
        // to link debours
        tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste().setFraisProcedure(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(tFactures.getIdFacture(), lawfirm.getVckey());

        invoiceDTO.setDateValue(ZonedDateTime.now().minusDays(1));
        assertThrows(ResponseStatusException.class, () -> {
            invoiceService.updateInvoice(invoiceDTO, lawfirm.getVckey());

        });

    }

    @Test
    public void test_D_updateInvoice() {
        LawfirmEntity lawfirm = createLawfirm();

        String email = "my@gmail.com";
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);
        // to link debours
        tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste().setFraisProcedure(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());

        // to link colla
        tFactures.getFraisCollaborationArrayList().get(0).getTFrais().getRefPoste().setFraisCollaboration(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceById(tFactures.getIdFacture(), lawfirm.getVckey());

        invoiceDTO.setDateValue(ZonedDateTime.now().minusDays(1));

        invoiceService.updateInvoice(invoiceDTO, lawfirm.getVckey());

    }

    @Test
    public void test_E_deleteInvoiceById() {

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

    @Test
    public void test_F_getPrestationByDossierId() {

        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);

        List<PrestationSummary> entityList = invoiceService.getPrestationByDossierId(tFactures.getIdFacture(), tFactures.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey());


        assertEquals(1, entityList.size());
    }

    @Test
    public void test_G_getFraisAdminByDossierId() {

        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);

        List<FraisAdminDTO> entityList = invoiceService.getFraisAdminByDossierId(tFactures.getIdFacture(), tFactures.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey());


        assertEquals(1, entityList.size());
    }

    @Test
    public void test_H_getDeboursByDossierId() {

        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);
        // to link debours
        tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste().setFraisProcedure(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());

        List<ComptaDTO> entityList = invoiceService.getDeboursByDossierId(tFactures.getIdFacture(), tFactures.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey());


        assertEquals(1, entityList.size());
    }

    @Test
    public void test_I_getFraisCollabByDossierId() {

        String email = "my@gmail.com";
        LawfirmEntity lawfirm = createLawfirm();
        String fullname = lawfirm.getLawfirmUsers().get(0).getUser().getFullname();
        LawfirmToken lawfirmToken = new LawfirmToken(lawfirm.getLawfirmUsers().get(0).getUser().getId(), email, email, lawfirm.getVckey(), null, true, new ArrayList<EnumRights>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TFactures tFactures = createFacture(lawfirm);
        // to link debours
        tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste().setFraisCollaboration(true);
        testEntityManager.persist(tFactures.getFraisDeboursList().get(0).getTFrais().getRefPoste());

        List<ComptaDTO> entityList = invoiceService.getFraisCollabByDossierId(tFactures.getIdFacture(), tFactures.getIdDoss(), lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey());


        assertEquals(1, entityList.size());
    }
}
