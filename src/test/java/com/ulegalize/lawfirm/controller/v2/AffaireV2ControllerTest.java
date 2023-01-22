package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.DossierContactDTO;
import com.ulegalize.dto.DossierDTO;
import com.ulegalize.dto.ItemDto;
import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToDossierConverter;
import com.ulegalize.lawfirm.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AffaireV2ControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityToDossierConverter entityToDossierConverter;

    private UsernamePasswordAuthenticationToken authentication;

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
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", verifyUser);

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_saveAffaire() throws Exception {
        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");
        TClients client3 = createClient(lawfirm);
        client3.setF_nom("Client Name3");
        client2.setF_email("Client@name3.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemLongDto client3dto = new ItemLongDto();
        client3dto.setLabel(client3.getF_nom());
        client3dto.setValue(client3.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        DossierContactDTO dossierContactDTO3 = new DossierContactDTO();
        dossierContactDTO3.setClient(client3dto);
        dossierContactDTO3.setClientType(opposingItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);
        dossierContactDTOS.add(dossierContactDTO3);

        DossierDTO dossierDTO = new DossierDTO();
        dossierDTO.setNote("note");
        dossierDTO.setOpenDossier(new Date());
        dossierDTO.setType(EnumDossierType.DC);
        dossierDTO.setMemo("");

        dossierDTO.setSuccess_fee_perc(0);
        dossierDTO.setSuccess_fee_montant(BigDecimal.valueOf(100));
        dossierDTO.setCouthoraire(0);
        dossierDTO.setQuality("");
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "mine@mine.vok");
        dossierDTO.setIdUserResponsible(lawfirmUsers.getUser().getId());
        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TMatiereRubriques tMatiereRubriques = createTMatiereRubriques();
        dossierDTO.setId_matiere_rubrique(tMatiereRubriques.getIdMatiereRubrique());
        TClients clientAdv = createClient(lawfirm);

        TVirtualcabNomenclature vcNomenclature = createVirtualcabNomenclature(lawfirm, "Nomenclature1");
        ItemLongDto itemLongDto = new ItemLongDto();
        itemLongDto.setLabel(vcNomenclature.getName() + dossierDTO.getNum());
        itemLongDto.setValue(vcNomenclature.getId());
        dossierDTO.setVirtualcabNomenclature(itemLongDto);

        mvc.perform(post("/v2/affaires")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dossierDTO)))
                .andExpect(jsonPath("$.note", is(dossierDTO.getNote())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getAffairesByVcUserIdAndSearchCriteria_noparam() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].label", containsStringIgnoringCase(dossier.getNomenclature())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_getAffairesByVcUserIdAndSearchCriteria_withParamFouned() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", String.valueOf(dossier.getDossierNumber())))
                .andExpect(jsonPath("$[0].label", containsStringIgnoringCase(dossier.getNomenclature())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getAffairesByVcUserIdAndSearchCriteria_withParamNotFouned() throws Exception {

        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchCriteria", "any criteria ..."))
                .andExpect(jsonPath("$").exists())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_getAllAffairesPagination() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires/list")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", String.valueOf(0))
                .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$.content[0].id", equalTo(dossier.getIdDoss().intValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_E_getAllSharedAffairesPagination_notexist() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        mvc.perform(get("/v2/affaires/shared/list")
                .with(authentication(authentication))
                .contentType(MediaType.APPLICATION_JSON)
                .param("offset", String.valueOf(0))
                .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_getAllSharedAffairesPagination() throws Exception {
        TDossiers dossier = createDossier(lawfirm, EnumVCOwner.NOT_SAME_VC);

        mvc.perform(get("/v2/affaires/shared/list")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", String.valueOf(0))
                        .param("limit", String.valueOf(10)))
                .andExpect(jsonPath("$.content[0].id", equalTo(dossier.getIdDoss().intValue())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_G_addNewContactToDossier() throws Exception {

        TDossiers tDossiers = createDossier(lawfirm, EnumVCOwner.OWNER_VC);

        DossierDTO dossierDTO = entityToDossierConverter.apply(tDossiers, EnumLanguage.FR);

        TClients client1 = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);
        client2.setF_nom("Client Name2");
        client2.setF_email("Client@name2.com");

        ItemLongDto client1dto = new ItemLongDto();
        client1dto.setLabel(client1.getF_nom());
        client1dto.setValue(client1.getId_client());

        ItemLongDto client2dto = new ItemLongDto();
        client2dto.setLabel(client2.getF_nom());
        client2dto.setValue(client2.getId_client());

        ItemDto clientItem = new ItemDto();
        clientItem.setLabel(EnumDossierContactType.CLIENT.name());
        clientItem.setValue(EnumDossierContactType.CLIENT.getId());

        ItemDto opposingItem = new ItemDto();
        opposingItem.setLabel(EnumDossierContactType.OPPOSING.name());
        opposingItem.setValue(EnumDossierContactType.OPPOSING.getId());

        DossierContactDTO dossierContactDTO1 = new DossierContactDTO();
        dossierContactDTO1.setClient(client1dto);
        dossierContactDTO1.setClientType(clientItem);

        DossierContactDTO dossierContactDTO2 = new DossierContactDTO();
        dossierContactDTO2.setClient(client2dto);
        dossierContactDTO2.setClientType(opposingItem);

        List<DossierContactDTO> dossierContactDTOS = new ArrayList<>();

        dossierContactDTOS.add(dossierContactDTO1);
        dossierContactDTOS.add(dossierContactDTO2);

        dossierDTO.setDossierContactDTO(dossierContactDTOS);

        TClients tClients = createClient(lawfirm);
        tClients.setF_nom("Test1");
        TClients tClients2 = createClient(lawfirm);
        tClients2.setF_nom("Test2");

        List<Long> clientIdList = new ArrayList<>();

        clientIdList.add(tClients.getId_client());
        clientIdList.add(tClients2.getId_client());

        String clientIds = String.valueOf(clientIdList).substring(1, String.valueOf(clientIdList).length() - 1);

        mvc.perform(post("/v2/affaires/contact")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dossierDTO))
                        .param("newContactIdList", clientIds))
                .andExpect(status().isOk());
    }

}
