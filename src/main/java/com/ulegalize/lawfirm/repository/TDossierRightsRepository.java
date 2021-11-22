package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ShareAffaireDTO;
import com.ulegalize.lawfirm.model.entity.TDossierRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TDossierRightsRepository extends JpaRepository<TDossierRights, Long>, JpaSpecificationExecutor<TDossierRights> {

    @Query(value = "SELECT count(dr) from TDossierRights dr where dr.dossierId = ?1 and dr.vcOwner = 1 and dr.lawfirmUsers.lawfirm.vckey = ?2 ")
    Long countByDossierIdAndVcOwnerAndVcKey(Long id_doss, String vcKey);

    @Query(value = "SELECT count(dr) from TDossierRights dr where dr.dossierId = ?1 and dr.vcOwner = 1 and dr.vcUserId = ?2 ")
    Long countByDossierIdAndVcOwnerAndvcUserId(Long id_doss, Long vcUserId);

    @Query(value = "SELECT new com.ulegalize.dto.ShareAffaireDTO(" +
            "vcu.id, dr.dossierId, u.id, vcu.lawfirm.vckey, dr.vcOwner, u.email, u.fullname, dr.creDate" +
            ") " +
            " from TDossierRights dr" +
            " join dr.lawfirmUsers vcu " +
            " join vcu.user u " +
            " where dr.dossierId = ?1 ")
    List<ShareAffaireDTO> findShareUserByAffaireId(Long affaireid);

    List<TDossierRights> findAllByVcUserId(Long vcUserId);

    Optional<TDossierRights> findAllByVcUserIdAndDossierId(Long vcUserId, Long dossierId);

    @Query(value = "delete from TDossierRights l where l.vcUserId = ?1")
    @Modifying
    void deleteAllByVcUserId(Long vcUserId);

    @Query(value = "delete from TDossierRights l " +
            " where l.dossierId = :dossierId and l.vcUserId = :vcUserId")
    @Modifying
    void deleteAllByVcUserIdAndDossierId(Long dossierId, Long vcUserId);
}