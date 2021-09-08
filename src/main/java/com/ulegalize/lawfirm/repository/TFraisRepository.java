package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFrais;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TFraisRepository extends JpaRepository<TFrais, Long>, JpaSpecificationExecutor<TFrais> {
    public Optional<TFrais> findByIdFraisAndVcKey(Long fraisId, String vcKey);

    public List<TFrais> findByVcKey(String vcKey);

    @Query(value = "SELECT count(d) " +
            "from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.fraisProcedure = true " +
            "and compt.accountTypeId = 1")
    Long countAllByIdDossAndVcKeyAndDebours(Long dossierId, String vcKey);

    @Query(value = "SELECT count(d) " +
            "from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.fraisCollaboration = true " +
            "and compt.accountTypeId = 1")
    Long countAllByIdDossAndVcKeyAndFraisCollaboration(Long dossierId, String vcKey);

    @Query(value = "SELECT count(d) " +
            "from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.honoraires = true " +
            "and compt.accountTypeId = 1")
    Long countAllByIdDossAndVcKeyAndHonoraire(Long dossierId, String vcKey);

    @Query(value = "SELECT count(d) " +
            "from TFrais d " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and compt.accountTypeId = 2")
    Long countAllByIdDossAndVcKeyAndTiers(Long dossierId, String vcKey);

    Long countAllByIdDossAndVcKey(Long dossierId, String vcKey);

    Long countAllByVcKey(String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType =2) THEN -d.montantht ELSE (d.montantht) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.fraisProcedure = true " +
            "and compte.accountTypeId = 1")
    BigDecimal sumAllDeboursByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType =2) THEN -d.montantht ELSE (d.montantht) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.fraisCollaboration = true and compt.accountTypeId = 1")
    BigDecimal sumAllCollabByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montantht ELSE (-d.montantht) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.honoraires = true " +
            "and compte.accountTypeId = 1")
    BigDecimal sumAllHonoByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and compte.accountTypeId = 2")
    BigDecimal sumAllTiersByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT d from TFrais d where d.vcKey = ?1")
    List<TFrais> findAllWithPagination(String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d where d.idDoss = ?1 and d.vcKey = ?2")
    List<TFrais> findByDossierIdWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.fraisProcedure = true " +
            "and compt.accountTypeId = 1")
    List<TFrais> findByDossierIAndDeboursdWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.fraisCollaboration = true " +
            "and compt.accountTypeId = 1")
    List<TFrais> findByDossierIdAndFraisCollaborationWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.honoraires = true " +
            "and compt.accountTypeId = 1")
    List<TFrais> findByDossierIdAndHonoraireWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and compt.accountTypeId = 2")
    List<TFrais> findByDossierIdAndTiersWithPagination(Long dossierId, String vcKey, Pageable pageable);

}