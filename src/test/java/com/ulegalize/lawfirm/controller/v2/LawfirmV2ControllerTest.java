package com.ulegalize.lawfirm.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulegalize.dto.LawyerDTO;
import com.ulegalize.enumeration.*;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.LawfirmToken;
import com.ulegalize.lawfirm.model.converter.EntityToUserConverter;
import com.ulegalize.lawfirm.model.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LawfirmV2ControllerTest extends EntityTest {
    @Autowired
    private MockMvc mvc;

    private UsernamePasswordAuthenticationToken authentication;

    private LawfirmEntity lawfirm;
    @Autowired
    private EntityToUserConverter entityToUserConverter;

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
//        "support@ulegalize.com";
        LawfirmToken lawfirmToken = new LawfirmToken(userId, usermail, usermail, lawfirm.getVckey(), null, true, new ArrayList<>(), "", true, EnumLanguage.FR.getShortCode(), EnumRefCurrency.EUR.getSymbol(), fullname, DriveType.openstack, "", "", lawfirm.getLawfirmUsers().get(0).getUser().getIdValid().equals(EnumValid.VERIFIED));

        authentication = new UsernamePasswordAuthenticationToken(lawfirmToken, null, lawfirmToken.getAuthorities());

    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A1_updateRoleLawfirmUser() throws Exception {
        LawfirmUsers lawfirmUsers = lawfirm.getLawfirmUsers().get(0);
        LawyerDTO lawyerDTO = entityToUserConverter.apply(lawfirmUsers.getUser(), false);
        lawyerDTO.setFunctionId(EnumRole.ASSISTANT.getIdRole());
        // deactivate
        lawyerDTO.setActive(!lawfirmUsers.isActive());
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);
        LawfirmUsers lawfirmUsersNew = createLawfirmUsers(lawfirm, "newSecu@gmail.com");
        // add second user to the group admin
        TSecurityGroupUsers tSecurityGroupUsersRemain = createTSecurityGroupUsers(lawfirmUsersNew, tSecurityGroups);

        mvc.perform(put("/v2/lawfirm/users/role")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(lawyerDTO)))
                .andExpect(jsonPath("$.functionId", is(lawyerDTO.getFunctionId())))
                .andExpect(jsonPath("$.active", is(lawyerDTO.isActive())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_A2_updateRoleLawfirmUser_403() throws Exception {
        LawfirmUsers lawfirmUsers = lawfirm.getLawfirmUsers().get(0);
        LawyerDTO lawyerDTO = entityToUserConverter.apply(lawfirmUsers.getUser(), false);
        lawyerDTO.setFunctionId(EnumRole.ASSISTANT.getIdRole());
        // deactivate
        lawyerDTO.setActive(!lawfirmUsers.isActive());
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        mvc.perform(put("/v2/lawfirm/users/role")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(lawyerDTO)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_B_updateRoleLawfirmUser_not() throws Exception {
        LawfirmUsers lawfirmUsers = lawfirm.getLawfirmUsers().get(0);
        LawyerDTO lawyerDTO = entityToUserConverter.apply(lawfirmUsers.getUser(), false);
        lawyerDTO.setFunctionId(EnumRole.ASSISTANT.getIdRole());
        EnumRole enumBeforeUpdate = lawfirmUsers.getIdRole();
        lawyerDTO.setActive(lawfirmUsers.isActive());

        mvc.perform(put("/v2/lawfirm/users/role")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(lawyerDTO)))
                .andExpect(jsonPath("$.functionId", not(enumBeforeUpdate.getIdRole())))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_C_getLawfirmUsers() throws Exception {
        TCalendarEvent tCalendarEvent = createTCalendarEvent(lawfirm, EnumCalendarEventType.PERM, new Date(), new Date());

        mvc.perform(get("/v2/lawfirm/users")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].public", is(lawfirm.getLawfirmUsers().get(0).isPublic())))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void test_D_searchLawfirmByNameAndStatus() throws Exception {

        LawfirmEntity lawfirm = createLawfirm("CAB1");

        mvc.perform(get("/v2/lawfirm/searchbystatus")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", lawfirm.getVckey()))
                .andExpect(status().isOk());
    }
}
