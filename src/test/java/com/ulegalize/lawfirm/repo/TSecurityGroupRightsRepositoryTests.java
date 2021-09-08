package com.ulegalize.lawfirm.repo;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TSecurityGroupRights;
import com.ulegalize.lawfirm.model.entity.TSecurityGroups;
import com.ulegalize.lawfirm.repository.TSecurityGroupRightsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class TSecurityGroupRightsRepositoryTests extends EntityTest {
    @Autowired
    private TSecurityGroupRightsRepository tSecurityGroupRightsRepository;


    @Test
    public void test_A_findBySecurityGroup() {
        LawfirmEntity lawfirm = createLawfirm();
        TSecurityGroups tSecurityGroups = createTSecurityGroups(lawfirm, true);

        List<TSecurityGroupRights> securityGroupsId = tSecurityGroupRightsRepository.findByTSecurityGroups_Id(tSecurityGroups.getId());

        assertNotNull(securityGroupsId);
        assertFalse(securityGroupsId.isEmpty());
        assertEquals(securityGroupsId.get(0).getTSecurityGroups().getId(), tSecurityGroups.getId());
    }
}
