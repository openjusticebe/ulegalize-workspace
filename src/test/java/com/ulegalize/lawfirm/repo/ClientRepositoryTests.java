package com.ulegalize.lawfirm.repo;

import com.ulegalize.enumeration.EnumClientType;
import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TClients;
import com.ulegalize.lawfirm.model.entity.TUsers;
import com.ulegalize.lawfirm.repository.ClientRepository;
import com.ulegalize.lawfirm.repository.OffsetBasedPageRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
public class ClientRepositoryTests extends EntityTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void test_A_findByAliasPublic() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        List<TClients> itemStringDtos = clientRepository.findByAliasPublic(user.getAliasPublic(), client.getF_email());
        assertNotNull(itemStringDtos);

        assertEquals(1, itemStringDtos.size());
    }

    @Test
    public void test_B_findByUserIdOrVcKey() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        List<TClients> itemStringDtos = clientRepository.findByUserIdOrVcKey(List.of(lawfirm.getVckey()), client.getUser_id());
        assertNotNull(itemStringDtos);

        assertEquals(1, itemStringDtos.size());
    }

    @Test
    public void test_C_findByUserIdOrVcKeyAAndF_email() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        List<TClients> itemStringDtos = clientRepository.findByUserIdOrVcKeyAAndF_email(List.of(lawfirm.getVckey()), client.getUser_id(), client.getF_email());
        assertNotNull(itemStringDtos);

        assertEquals(1, itemStringDtos.size());
    }

    @Test
    public void test_D_findByUserIdOrVcKeyWithPagination() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        Pageable pageable = new OffsetBasedPageRequest(5, 0, Sort.by(Sort.Direction.ASC, "f_nom"));
        List<TClients> itemStringDtos = clientRepository.findByUserIdOrVcKeyWithPagination(lawfirm.getVckey(), user.getId(), "", pageable);
        assertNotNull(itemStringDtos);

        assertEquals(1, itemStringDtos.size());
    }

    @Test
    public void test_E_countByUserIdOrVcKeyWithPagination() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        Long itemStringDtos = clientRepository.countByUserIdOrVcKeyWithPagination(lawfirm.getVckey(), user.getId(), null);
        assertNotNull(itemStringDtos);

        assertEquals(Long.valueOf(1), itemStringDtos);
    }

    @Test
    public void test_F_findById_clientAndUserIdOrVcKey() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        Optional<TClients> result = clientRepository.findById_clientAndUserIdOrVcKey(client.getId_client(), lawfirm.getVckey(), user.getId());

        assertTrue(result.isPresent());
    }

    @Test
    public void test_G_countByUserIdOrVcKey() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        TUsers user = lawfirm.getLawfirmUsers().get(0).getUser();
        Long result = clientRepository.countByUserIdOrVcKey(lawfirm.getVckey(), user.getId());

        assertNotNull(result);

        assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void test_H_countByVcKeyAndClientTypeAndFCompany() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);

        Long result = clientRepository.countByVcKeyAndClientTypeAndFCompany(lawfirm.getVckey(), EnumClientType.NATURAL_PERSON, client.getF_company());

        assertNotNull(result);

        assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void test_I_findByIds_oneResult() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);

        List<TClients> result = clientRepository.findByIds(List.of(client.getId_client()), lawfirm.getVckey(), lawfirm.getLawfirmUsers().get(0).getUser().getId());

        assertNotNull(result);

        assertEquals(1, result.size());
    }

    @Test
    public void test_J_findByIds_twoResult() {
        LawfirmEntity lawfirm = createLawfirm();
        TClients client = createClient(lawfirm);
        TClients client2 = createClient(lawfirm);

        List<TClients> result = clientRepository.findByIds(List.of(client.getId_client(), client2.getId_client()), lawfirm.getVckey(), lawfirm.getLawfirmUsers().get(0).getUser().getId());

        assertNotNull(result);

        assertEquals(2, result.size());
    }
}
