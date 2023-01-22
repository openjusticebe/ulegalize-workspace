package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.EntityTest;
import com.ulegalize.lawfirm.model.entity.LawfirmEntity;
import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class TWorkspaceAssociatedRepositoryTest extends EntityTest {

    @Autowired
    private TWorkspaceAssociatedRepository tWorkspaceAssociatedRepository;

    @Test
    void test_A_findByIdAndLawfirmRecipientAndStatus() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        Optional<TWorkspaceAssociated> workspaceAssociatedOptional = tWorkspaceAssociatedRepository.findByIdAndLawfirmRecipientAndStatus(workspaceAssociation.getId(), workspaceAssociation.getLawfirmRecipient().getVckey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING));
        assertNotNull(workspaceAssociatedOptional);
        assertEquals(workspaceAssociation.getId(), workspaceAssociatedOptional.get().getId());

    }

    @Test
    void test_B_findByLawfirmSenderAndLawfirmRecipientAndStatus_Should_Return_Not_Null() {
        LawfirmEntity lawfirmSender = createLawfirm("CAB1");
        LawfirmEntity lawfirmRecipient = createLawfirm("CAB2");

        TWorkspaceAssociated workspaceAssociation = createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        assertNotNull(tWorkspaceAssociatedRepository.findByLawfirmSenderAndLawfirmRecipientAndStatus(workspaceAssociation.getLawfirmSender().getVckey(), workspaceAssociation.getLawfirmRecipient().getVckey(), List.of(EnumStatusAssociation.ACCEPTED, EnumStatusAssociation.PENDING)));
    }

    @Test
    void test_C_findAllByLawfirmSenderOrLawfirmRecipientAndStatus_searchBySender() {

        LawfirmEntity lawfirmSender = createLawfirm("AVOTEST");
        LawfirmEntity lawfirmSender2 = createLawfirm("TEST1");

        LawfirmEntity lawfirmRecipient = createLawfirm("SEVERINE");
        LawfirmEntity lawfirmRecipient2 = createLawfirm("ULMI");
        LawfirmEntity lawfirmRecipient3 = createLawfirm("FINAUXA");

        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient2);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient3);
        createWorkspaceAssociation(lawfirmSender2, lawfirmRecipient);

        Pageable pageable = new OffsetBasedPageRequest(5, 0);

        Page<TWorkspaceAssociated> workspaceAssociatedPage = tWorkspaceAssociatedRepository.findAllByLawfirmSenderOrLawfirmRecipientAndStatus(lawfirmSender.getVckey(), null, EnumStatusAssociation.REFUSED.name(), pageable);

        assertNotNull(workspaceAssociatedPage);
        assertEquals(3, workspaceAssociatedPage.getContent().size());
    }

    @Test
    void test_D_findAllByLawfirmSenderOrLawfirmRecipientAndStatus_searchByRecipient() {

        LawfirmEntity lawfirmSender = createLawfirm("AVOTEST");
        LawfirmEntity lawfirmSender2 = createLawfirm("TEST1");

        LawfirmEntity lawfirmRecipient = createLawfirm("SEVERINE");
        LawfirmEntity lawfirmRecipient2 = createLawfirm("ULMI");
        LawfirmEntity lawfirmRecipient3 = createLawfirm("FINAUXA");

        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient2);
        createWorkspaceAssociation(lawfirmSender, lawfirmRecipient3);
        createWorkspaceAssociation(lawfirmSender2, lawfirmRecipient);

        Pageable pageable = new OffsetBasedPageRequest(5, 0);

        Page<TWorkspaceAssociated> workspaceAssociatedPage = tWorkspaceAssociatedRepository.findAllByLawfirmSenderOrLawfirmRecipientAndStatus(lawfirmRecipient.getVckey(), null, EnumStatusAssociation.REFUSED.name(), pageable);

        assertNotNull(workspaceAssociatedPage);
        assertEquals(2, workspaceAssociatedPage.getContent().size());
    }
}