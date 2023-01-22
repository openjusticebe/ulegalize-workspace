package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TWorkspaceAssociated;
import com.ulegalize.lawfirm.model.enumeration.EnumStatusAssociation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TWorkspaceAssociatedRepository extends JpaRepository<TWorkspaceAssociated, Long>, JpaSpecificationExecutor<TWorkspaceAssociated> {

    @Query(value = "select w" +
            " from TWorkspaceAssociated w" +
            " where w.id = ?1" +
            " and w.lawfirmRecipient.vckey = ?2" +
            " and w.status in ?3")
    Optional<TWorkspaceAssociated> findByIdAndLawfirmRecipientAndStatus(Long id, String vcKeyRecipient, List<EnumStatusAssociation> enumStatusAssociationList);

    @Query(value = "select w" +
            " from TWorkspaceAssociated w" +
            " where w.lawfirmSender.vckey = ?1" +
            " and w.lawfirmRecipient.vckey = ?2" +
            " and w.status in ?3")
    Optional<TWorkspaceAssociated> findByLawfirmSenderAndLawfirmRecipientAndStatus(String vcKeySender, String vcKeyRecipient, List<EnumStatusAssociation> enumStatusAssociationList);

    @Query(nativeQuery = true, value = "select *" +
            " from t_workspace_associated as w" +
            " where (" +
            "          case" +
            "              when ?2 is null" +
            "                  then w.id_sender like ?1" +
            "                  or w.id_recipient like ?1" +
            "              when ?2 = 0 then w.id_sender LIKE ?1" +
            "              when ?2 = 1 then w.id_recipient like ?1" +
            "              end" +
            " and w.status != ?3" +
            ")",
            countQuery = "select count(w.id_sender)" +
                    " from t_workspace_associated as w" +
                    " where (" +
                    "          case" +
                    "              when ?2 is null" +
                    "                  then w.id_sender like ?1" +
                    "                  or w.id_recipient like ?1" +
                    "              when ?2 = 0 then w.id_sender LIKE ?1" +
                    "              when ?2 = 1 then w.id_recipient like ?1" +
                    "              end" +
                    " and w.status != ?3" +
                    ")"
    )
    Page<TWorkspaceAssociated> findAllByLawfirmSenderOrLawfirmRecipientAndStatus(String vcKey, Boolean searchCriteriaType, String status, Pageable pageable);
}
