package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFrais;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface TFraisRepository extends JpaRepository<TFrais, Long>, JpaSpecificationExecutor<TFrais> {
    public Optional<TFrais> findByIdFraisAndVcKey(Long fraisId, String vcKey);

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

    @Query(nativeQuery = true, value = "SELECT * from t_frais d" +
            " left join t_clients client on client.id_client = d.id_client" +
            " left join t_dossiers dossier on dossier.id_doss = d.id_doss" +
            " where d.vc_key = ?1" +
            " and (client.f_nom like concat('%', ?2, '%') " +
            " or client.f_prenom like concat('%', ?2, '%') )" +
            " and (dossier.year_doss like COALESCE(CONCAT('%', ?3, '%'), '%') and dossier.num_doss like COALESCE(CONCAT(?4, '%'), '%')) " +
            " and d.id_poste like coalesce(?5, '%')",
            countQuery = "SELECT count(*) from t_frais d" +
                    " left join t_clients client on client.id_client = d.id_client" +
                    " left join t_dossiers dossier on dossier.id_doss = d.id_doss" +
                    " where d.vc_key = ?1" +
                    " and (client.f_nom like concat('%', ?2, '%') " +
                    " or client.f_prenom like concat('%', ?2, '%') )" +
                    " and (dossier.year_doss like COALESCE(CONCAT('%', ?3, '%'), '%') and dossier.num_doss like COALESCE(CONCAT(?4, '%'), '%')) " +
                    " and d.id_poste like coalesce(?5, '%')")
    Page<TFrais> findAllWithPagination(String vcKey, String searchCriteriaClient, String searchCriteriaYear, Long searchCriteriaNumber, String searchCriteriaPoste, Pageable pageable);

    @Query(value = "SELECT d from TFrais d where d.idDoss = ?1 and d.vcKey = ?2",
            countQuery = "SELECT count(d) from TFrais d where d.idDoss = ?1 and d.vcKey = ?2")
    Page<TFrais> findByDossierIdWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.fraisProcedure = true " +
            "and compt.accountTypeId = 1", countQuery = "SELECT count(d) " +
            "from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.fraisProcedure = true " +
            "and compt.accountTypeId = 1")
    Page<TFrais> findByDossierIAndDeboursdWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.fraisCollaboration = true " +
            "and compt.accountTypeId = 1",
            countQuery = "SELECT count(d) " +
                    "from TFrais d " +
                    "join d.refPoste poste " +
                    "left join d.refCompte compt " +
                    "where d.idDoss = ?1 and d.vcKey = ?2 " +
                    "and poste.fraisCollaboration = true " +
                    "and compt.accountTypeId = 1")
    Page<TFrais> findByDossierIdAndFraisCollaborationWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.honoraires = true " +
            "and compt.accountTypeId = 1",
            countQuery = "SELECT count(d) " +
                    "from TFrais d " +
                    "join d.refPoste poste " +
                    "left join d.refCompte compt " +
                    "where d.idDoss = ?1 and d.vcKey = ?2 " +
                    "and poste.honoraires = true " +
                    "and compt.accountTypeId = 1")
    Page<TFrais> findByDossierIdAndHonoraireWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and compt.accountTypeId = 2",
            countQuery = "SELECT count(d) " +
                    "from TFrais d " +
                    "left join d.refCompte compt " +
                    "where d.idDoss = ?1 and d.vcKey = ?2 " +
                    "and compt.accountTypeId = 2")
    Page<TFrais> findByDossierIdAndTiersWithPagination(Long dossierId, String vcKey, Pageable pageable);

}