package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ComptaDTO;
import com.ulegalize.lawfirm.model.entity.TFrais;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
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
    BigDecimal sumAllHonoHtvaByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) " +
            " from TFrais d " +
            " join d.refPoste poste " +
            " join d.refCompte compte " +
            " where d.idDoss = ?1 and d.vcKey = ?2 " +
            " and poste.honoraires = true " +
            " and compte.accountTypeId = 1 ")
    BigDecimal sumAllHonoTtcByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) " +
            " from TFrais d " +
            " join d.refPoste poste " +
            " join d.refCompte compte " +
            " where d.idDoss = ?1 and d.vcKey = ?2 " +
            " and poste.honoraires = true " +
            " and compte.accountTypeId = 1 " +
            " and d.idFacture is not null")
    BigDecimal sumAllHonoTtcOnlyInvoiceByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idFacture = ?1 and d.vcKey = ?2 " +
            "and poste.honoraires = true " +
            "and compte.accountTypeId = 1")
    BigDecimal sumAllHonoTtcByInvoiceId(Long invoiceId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and compte.accountTypeId = 2")
    BigDecimal sumAllTiersByVcKey(Long dossierId, String vcKey);

    @Query(nativeQuery = true, value = "SELECT * from t_frais d" +
            " left join t_clients client on client.id_client = d.id_client " +
            "     and (client.f_nom like concat('%', ?2, '%')" +
            "        or client.f_prenom like concat('%', ?2, '%') )" +
            " left join t_dossiers dossier on dossier.id_doss = d.id_doss " +
            " and (dossier.year_doss like COALESCE(CONCAT('%', ?3, '%'), '%') and dossier.num_doss like COALESCE(CONCAT(?4, '%'), '%'))" +
            " where d.vc_key = ?1" +
            " and d.id_poste like coalesce(?5, '%')",
            countQuery = "SELECT count(*) from t_frais d" +
                    " left join t_clients client on client.id_client = d.id_client" +
                    "     and (client.f_nom like concat('%', ?2, '%')" +
                    "        or client.f_prenom like concat('%', ?2, '%') )" +
                    " left join t_dossiers dossier on dossier.id_doss = d.id_doss " +
                    " and (dossier.year_doss like COALESCE(CONCAT('%', ?3, '%'), '%') and dossier.num_doss like COALESCE(CONCAT(?4, '%'), '%'))" +
                    " where d.vc_key = ?1" +
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

    @Query(value = "SELECT count(ts) from TFrais ts" +
            " join ts.refPoste poste " +
            " join ts.tDossiers d " +
            " join d.dossierRightsList dr " +
            " where ts.idFrais in ?1 " +
            " and ts.idDoss = ?2" +
            " and poste.fraisProcedure = true" +
            " and dr.vcUserId = ?3")
    Long countAllFraisDeboursByIdAndDossierId(List<Long> fraisId, Long dossierId, Long id);

    @Query(value = "SELECT count(ts) from TFrais ts" +
            " join ts.refPoste poste " +
            " join ts.tDossiers d " +
            " join d.dossierRightsList dr " +
            " where ts.idFrais in ?1 " +
            " and ts.idDoss = ?2" +
            " and poste.fraisCollaboration = true" +
            " and dr.vcUserId = ?3")
    Long countAllFraisCollaByIdAndDossierId(List<Long> fraisId, Long dossierId, Long id);

    @Query("select new com.ulegalize.dto.ComptaDTO(d.idFrais , " +
            " d.vcKey, " +
            " d.idPoste, poste.refPoste, " +
            " d.montant, d.montantht, " +
            " concat(client.f_nom, ' ', client.f_prenom), " +
            "  ft.ID) " +
            " from TFrais d" +
            " join d.refPoste poste " +
            " left join d.tDossiers dossier " +
            " left join dossier.dossierRightsList dr " +
            " left join d.tClients client " +
            " left join d.factureFraisDeboursList ft on ft.tFactures.idFacture = ?1" +
            " where dr.dossierId = ?2 " +
            " and dr.vcUserId = ?3" +
            " and poste.fraisProcedure = true")
    List<ComptaDTO> findAllDeboursByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long id);

    @Query("select new com.ulegalize.dto.ComptaDTO(d.idFrais , " +
            " d.vcKey, " +
            " d.idPoste, poste.refPoste, " +
            " d.montant, d.montantht, " +
            " concat(client.f_nom, ' ', client.f_prenom), " +
            "  ft.ID)" +
            " from TFrais d" +
            " join d.refPoste poste " +
            " left join d.tDossiers dossier " +
            " left join dossier.dossierRightsList dr " +
            " left join d.tClients client " +
            " left join d.factureFraisCollaborationList ft on ft.tFactures.idFacture = ?1" +
            " where dr.dossierId = ?2 " +
            " and dr.vcUserId = ?3" +
            " and poste.fraisCollaboration = true")
    List<ComptaDTO> findAllCollabByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long id);
}