package com.ulegalize.lawfirm.repository;

import com.ulegalize.enumeration.EnumSecurityAppGroups;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TSecurityGroups;
import com.ulegalize.security.EnumRights;
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
public class TSecurityGroupUsersRepositoryTests extends EntityTest {
    @Autowired
    private TSecurityGroupUsersRepository tSecurityGroupUsersRepository;


    @Test
    public void test_A_findByIdUserAndVckey() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        List<EnumRights> securityGroupsId = tSecurityGroupUsersRepository.findByIdUserAndVckey(lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey(), List.of(EnumSecurityAppGroups.ADMIN));

        assertNotNull(securityGroupsId);
        assertFalse(securityGroupsId.isEmpty());
        boolean result = securityGroupsId.stream()
                .allMatch(tSecurityGroupsRight -> tSecurityGroupsRight.equals(EnumRights.ADMINISTRATEUR));
        assertTrue(result);

    }

    @Test
    public void test_B_findByIdUserAndVckey_false() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        List<EnumRights> securityGroupsId = tSecurityGroupUsersRepository.findByIdUserAndVckey(lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey(), List.of(EnumSecurityAppGroups.USER));

        assertNotNull(securityGroupsId);
        assertTrue(securityGroupsId.isEmpty());

    }

    @Test
    public void test_C_findByIdUserAndVckey_notadmin() {
        LawfirmEntity lawfirm = createLawfirm("MYLAW");
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        List<EnumRights> securityGroupsId = tSecurityGroupUsersRepository.findByIdUserAndVckey(lawfirm.getLawfirmUsers().get(0).getUser().getId(), lawfirm.getVckey(), List.of(EnumSecurityAppGroups.USER));

        assertNotNull(securityGroupsId);
        boolean result = securityGroupsId.stream()
                .anyMatch(tSecurityGroupsRight -> tSecurityGroupsRight.equals(EnumRights.ADMINISTRATEUR));
        assertFalse(result);

    }
}
