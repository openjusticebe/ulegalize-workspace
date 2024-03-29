package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.FraisAdminDTO;
import com.ulegalize.dto.PrestationTypeDTO;
import com.ulegalize.dto.SecurityGroupUserDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.entity.*;
import com.ulegalize.security.EnumRights;
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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AdminV2ControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

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
        // "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true,
                new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(),
                EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED));

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null,
                lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A_getAllPrestationType() throws Exception {
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        mvc.perform(get("/v2/admin/prestation/type").with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].description", is(tTimesheetType.getDescription())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_updatePrestationType() throws Exception {
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        PrestationTypeDTO prestationTypeDTO = new PrestationTypeDTO(tTimesheetType.getIdTs(),
                tTimesheetType.getVcKey(), tTimesheetType.getDescription(), tTimesheetType.getUserUpd(),
                tTimesheetType.getDateUpd(), tTimesheetType.getArchived());

        prestationTypeDTO.setDescription(tTimesheetType.getDescription() + "ok");
        mvc.perform(put("/v2/admin/prestation/type/" + tTimesheetType.getIdTs())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(prestationTypeDTO)))
                .andExpect(jsonPath("$.description", is(prestationTypeDTO.getDescription())))
                .andExpect(status().isOk());

        TTimesheetType tTimesheetType1 = testEntityManager.find(TTimesheetType.class, tTimesheetType.getIdTs());

        assertEquals(tTimesheetType1.getDescription(), prestationTypeDTO.getDescription());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_deletePrestationType() throws Exception {
        TTimesheetType tTimesheetType = createTTimesheetType(lawfirm);

        mvc.perform(delete("/v2/admin/prestation/type/" + tTimesheetType.getIdTs())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(tTimesheetType.getIdTs())))
                .andExpect(status().isOk());

        TTimesheetType tTimesheetType1 = testEntityManager.find(TTimesheetType.class, tTimesheetType.getIdTs());

        assertNull(tTimesheetType1);

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_createPrestationType() throws Exception {
        PrestationTypeDTO prestationTypeDTO = new PrestationTypeDTO(null, lawfirm.getVckey(), "NEWONE", null,
                new Date(), false);

        mvc.perform(post("/v2/admin/prestation/type")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(prestationTypeDTO)))
                .andExpect(jsonPath("$.idTs").exists())
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_D_getAllFrais() throws Exception {
        TDebourType tDebourType = createTDebourType(lawfirm);

        mvc.perform(get("/v2/admin/frais/type")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].debourDescription", is(tDebourType.getDescription())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_E_updateFraisType() throws Exception {
        TDebourType tDebourType = createTDebourType(lawfirm);

        FraisAdminDTO fraisAdminDTO = new FraisAdminDTO(tDebourType.getIdDebourType(), tDebourType.getVcKey(),
                tDebourType.getDescription(), tDebourType.getPricePerUnit(),
                tDebourType.getIdMesureType(), "", tDebourType.getArchived());

        fraisAdminDTO.setDebourDescription(tDebourType.getDescription() + "ok");
        mvc.perform(put("/v2/admin/frais/type/" + tDebourType.getIdDebourType())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(fraisAdminDTO)))
                .andExpect(jsonPath("$.debourDescription", is(fraisAdminDTO.getDebourDescription())))
                .andExpect(status().isOk());

        TDebourType tDebourType1 = testEntityManager.find(TDebourType.class, tDebourType.getIdDebourType());

        assertEquals(tDebourType1.getDescription(), fraisAdminDTO.getDebourDescription());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_F_deleteFraisType() throws Exception {
        TDebourType tDebourType = createTDebourType(lawfirm);

        mvc.perform(delete("/v2/admin/frais/type/" + tDebourType.getIdDebourType())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(tDebourType.getIdDebourType().intValue())))
                .andExpect(status().isOk());

        TDebourType tDebourType1 = testEntityManager.find(TDebourType.class, tDebourType.getIdDebourType());

        assertNull(tDebourType1);

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_H_createFraisType() throws Exception {
        TMesureType tMesureType = createTMesureType();
        FraisAdminDTO fraisAdminDTO = new FraisAdminDTO(null, lawfirm.getVckey(), "newone", BigDecimal.ONE,
                tMesureType.getIdMesureType(), "", false);


        mvc.perform(post("/v2/admin/frais/type")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(fraisAdminDTO)))
                .andExpect(jsonPath("$.idDebourType").exists())
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_I_getLawfirm() throws Exception {
        TDebourType tDebourType = createTDebourType(lawfirm);

        mvc.perform(get("/v2/admin/lawfirm")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vckey", is(lawfirm.getVckey())))
                .andExpect(jsonPath("$.abbreviation", is(lawfirm.getAbbreviation())))
                .andExpect(jsonPath("$.numentreprise", is(lawfirm.getCompanyNumber())))
                //add ohter andExpect(other than vckeyà
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_J_getSecurityGroupByName_exist() throws Exception {
        TSecurityGroups securityGroups = createTSecurityGroups(lawfirm, true);

        mvc.perform(get("/v2/admin/security/securityGroup/exists")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("securityGroupName", securityGroups.getDescription()))
                .andExpect(jsonPath("$", is(true)))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_J_getSecurityGroupByName_notexist() throws Exception {
        TSecurityGroups securityGroups = createTSecurityGroups(lawfirm, true);

        mvc.perform(get("/v2/admin/security/securityGroup/exists")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("securityGroupName", securityGroups.getDescription() + "NOT"))
                .andExpect(jsonPath("$", is(false)))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_K_createUserSecurity() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newUserForsecurity@gmail.com");
        SecurityGroupUserDTO securityGroupUserDTO = new SecurityGroupUserDTO();
        securityGroupUserDTO.setFunctionId(EnumRole.AVOCAT.getIdRole());
        securityGroupUserDTO.setEmail(lawfirmUsers.getUser().getEmail());
        securityGroupUserDTO.setSecurityGroupId(tSecurityGroups.getId());

        mvc.perform(post("/v2/admin/security/securityUserGroup")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(securityGroupUserDTO)))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_K_createUserSecurity_forbidden() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        SecurityGroupUserDTO securityGroupUserDTO = new SecurityGroupUserDTO();
        securityGroupUserDTO.setFunctionId(EnumRole.AVOCAT.getIdRole());
        securityGroupUserDTO.setEmail(lawfirm.getLawfirmUsers().get(0).getUser().getEmail());
        securityGroupUserDTO.setSecurityGroupId(tSecurityGroups.getId());

        mvc.perform(post("/v2/admin/security/securityUserGroup")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(securityGroupUserDTO)))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_L_createUserSecurity_error404() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        SecurityGroupUserDTO securityGroupUserDTO = new SecurityGroupUserDTO();
        securityGroupUserDTO.setFunctionId(EnumRole.AVOCAT.getIdRole());
        securityGroupUserDTO.setEmail(null);
        securityGroupUserDTO.setSecurityGroupId(tSecurityGroups.getId());

        mvc.perform(post("/v2/admin/security/securityUserGroup")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(securityGroupUserDTO)))
                .andExpect(status().is4xxClientError());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_M_createSecurityGroup() throws Exception {
        mvc.perform(post("/v2/admin/security/securityGroup")
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(asJsonString("newGroup")))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_N_deleteSecurityGroup() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, false);
        mvc.perform(delete("/v2/admin//security/securityGroup/" + tSecurityGroups.getId())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_O_deleteSecurityGroup_withuser_forbidden() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        mvc.perform(delete("/v2/admin/security/securityGroup/" + tSecurityGroups.getId())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_P_deleteSecurityUserGroup() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        tSecurityGroups.setTSecAppGroupId(EnumSecurityAppGroups.USER);
        testEntityManager.persist(tSecurityGroups);

        mvc.perform(delete("/v2/admin/security/" + tSecurityGroups.getTSecurityGroupUsersList().get(0).getId() + "/user")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_P_deleteSecurityUserGroup_forbidden() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        mvc.perform(delete("/v2/admin/security/" + tSecurityGroups.getTSecurityGroupUsersList().get(0).getId() + "/user")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_Q_addUserSecurity() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        mvc.perform(post("/v2/admin/security/" + tSecurityGroups.getId() + "/user")
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(asJsonString(lawfirmUsers.getUser().getId())))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_R_addUserSecurity_forbidden() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        mvc.perform(post("/v2/admin/security/" + tSecurityGroups.getId() + "/user")
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(asJsonString(lawfirm.getLawfirmUsers().get(0).getUser().getId())))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_S_deleteSecurityRightGroup() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        mvc.perform(delete("/v2/admin/security/" + tSecurityGroups.getTSecurityGroupRightsList().get(0).getId() + "/right")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_T_addRightSecurity() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsers = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        mvc.perform(post("/v2/admin/security/" + tSecurityGroups.getId() + "/right")
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(asJsonString(EnumRights.AVODRIVE_LECTURE.getId())))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_U_deleteSecurityRightGroup() throws Exception {
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        mvc.perform(delete("/v2/admin/security/" + tSecurityGroups.getTSecurityGroupRightsList().get(0).getId() + "/right")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_V_createVat() throws Exception {
        mvc.perform(post("/v2/admin/vat")
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .content(asJsonString(999)))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_W_deleteVat() throws Exception {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));
        virtualcabVat.setIsDefault(false);

        createVirtualcabVat(lawfirm, BigDecimal.valueOf(6));

        mvc.perform(delete("/v2/admin/vat/" + virtualcabVat.getVAT())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_W1_deleteVat_forbiden() throws Exception {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));

        mvc.perform(delete("/v2/admin/vat/" + virtualcabVat.getVAT())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_X_deleteVat_alreadyused_forbidden() throws Exception {
        TFactures facture = createFacture(lawfirm, 1);

        mvc.perform(delete("/v2/admin/vat/" + lawfirm.getTVirtualcabVatList().get(0).getVAT())
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_Y_changeDefaultVat() throws Exception {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));

        mvc.perform(put("/v2/admin/vat/" + virtualcabVat.getVAT())
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_Z_changeDefaultVat_notfound() throws Exception {
        TVirtualcabVat virtualcabVat = createVirtualcabVat(lawfirm, BigDecimal.valueOf(21));
        virtualcabVat.setIsDefault(false);
        testEntityManager.persist(virtualcabVat);

        mvc.perform(put("/v2/admin/vat/" + virtualcabVat.getVAT())
                        .with(authentication(authentication))
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_BB_approveWorkspace_accepted() throws Exception {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/approveWorkspace?id=" + workspaceAssociation.getId() + "&vckey=" + workspaceAssociation.getLawfirmRecipient().getVckey() + "&hashkey=" + workspaceAssociation.getHashkey() + "&status=" + true)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_CC_approveWorkspace_refused() throws Exception {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/approveWorkspace?id=" + workspaceAssociation.getId() + "&vckey=" + workspaceAssociation.getLawfirmRecipient().getVckey() + "&hashkey=" + workspaceAssociation.getHashkey() + "&status=" + false)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_DD_approveWorkspace_retrunException() throws Exception {
        mvc.perform(get("/v2/admin/security/approveWorkspace?id=" + 1L + "&vckey=" + "test" + "&hashkey=" + "test" + "&status=" + false)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test_EE_approveWorkspace_retrunException() throws Exception {
        mvc.perform(get("/v2/admin/security/approveWorkspace?id=" + 1L + "&vckey=" + "test" + "&hashkey=" + "test" + "&status=")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test_FF_getAssociatedWorkspace() throws Exception {
        LawfirmEntity lawfirmSender = lawfirm;
        LawfirmEntity lawfirmSender2 = createLawfirm("TEST1");

        LawfirmEntity lawfirmRecipient = createLawfirm("SEVERINE");
        LawfirmEntity lawfirmRecipient2 = createLawfirm("ULMI");
        LawfirmEntity lawfirmRecipient3 = createLawfirm("FINAUXA");

        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient2);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient3);
        createWorkspaceAssociation(lawfirmSender2, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/associatedWorkspace")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", String.valueOf(0))
                        .param("limit", String.valueOf(10))
                        .param("vcKey", "")
                        .param("searchCriteriaType", "")
                )
                .andExpect(status().isOk());
    }

    @Test
    void test_GG_approveWorkspace_WithErroHashkey() throws Exception {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/approveWorkspace?id=" + workspaceAssociation.getId() + "&vckey=" + workspaceAssociation.getLawfirmRecipient().getVckey() + "&hashkey=" + "test" + "&status=" + false)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void test_HH_updateAssociation_withStatusAccepted() throws Exception {
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirm, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/updateAssociation?vckeyRecipient=" + workspaceAssociation.getLawfirmRecipient().getVckey() + "&typeAssociation=" + false + "&status=" + true)
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_II_updateAssociation_withStatusRefused() throws Exception {
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirm, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/updateAssociation?vckeyRecipient=" + workspaceAssociation.getLawfirmRecipient().getVckey() + "&typeAssociation=" + false + "&status=" + false)
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_II_updateAssociation_withStatusNull() throws Exception {
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirm, lawfirmRecipient);

        mvc.perform(get("/v2/admin/security/updateAssociation?vckeyRecipient=" + workspaceAssociation.getLawfirmRecipient().getVckey() + "&typeAssociation=" + false + "&status=")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
