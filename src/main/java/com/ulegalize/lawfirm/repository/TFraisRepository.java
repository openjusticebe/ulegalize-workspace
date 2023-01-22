package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.template.ItemDebours;
import com.ulegalize.dto.template.ItemFraisColla;
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
    Optional<TFrais> findByIdFraisAndVcKey(Long fraisId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType =2) THEN -d.montantht ELSE (d.montantht) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 and d.isDeleted = false " +
            "and poste.fraisProcedure = true " +
            "and compte.accountTypeId = 1")
    BigDecimal sumAllDeboursByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType =2) THEN -d.montantht ELSE (d.montantht) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            "where d.idDoss = ?1 and d.vcKey = ?2 and d.isDeleted = false " +
            "and poste.fraisCollaboration = true and compt.accountTypeId = 1")
    BigDecimal sumAllCollabByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montantht ELSE (-d.montantht) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 " +
            "and poste.honoraires = true and d.isDeleted = false " +
            "and compte.accountTypeId = 1")
    BigDecimal sumAllHonoHtvaByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) " +
            " from TFrais d " +
            " join d.refPoste poste " +
            " join d.refCompte compte " +
            " where d.idDoss = ?1 and d.vcKey = ?2 " +
            " and poste.honoraires = true and d.isDeleted = false " +
            " and compte.accountTypeId = 1 ")
    BigDecimal sumAllHonoTtcByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) " +
            " from TFrais d " +
            " join d.refPoste poste " +
            " join d.refCompte compte " +
            " where d.idDoss = ?1 and d.vcKey = ?2 " +
            " and poste.honoraires = true " +
            " and compte.accountTypeId = 1 and d.isDeleted = false " +
            " and d.idFacture is not null")
    BigDecimal sumAllHonoTtcOnlyInvoiceByVcKey(Long dossierId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idFacture = ?1 and d.vcKey = ?2 " +
            "and poste.honoraires = true and d.isDeleted = false " +
            "and compte.accountTypeId = 1")
    BigDecimal sumAllHonoTtcByInvoiceId(Long invoiceId, String vcKey);

    @Query(value = "SELECT COALESCE(sum( CASE WHEN (d.idType > 1) THEN d.montant ELSE (-d.montant) END), 0) from TFrais d " +
            "join d.refPoste poste " +
            "join d.refCompte compte " +
            "where d.idDoss = ?1 and d.vcKey = ?2 and d.isDeleted = false " +
            "and compte.accountTypeId = 2")
    BigDecimal sumAllTiersByVcKey(Long dossierId, String vcKey);

    @Query(nativeQuery = true, value = "SELECT * from t_frais d" +
            " left join t_clients client on client.id_client = d.id_client " +
            "     and (client.f_nom like concat('%', ?2, '%')" +
            "        or client.f_prenom like concat('%', ?2, '%') )" +
            " left join t_dossiers dossier on dossier.id_doss = d.id_doss and dossier.nomenclature like COALESCE(concat('%', ?3, '%'), '%')" +
            " where d.vc_key = ?1 and d.is_deleted = false " +
            " and d.id_poste like coalesce(?4, '%')" +
            " and d.id_type like concat('%', ?5, '%')" +
            " and d.id_compte like concat(?6, '%')",
            countQuery = "SELECT count(d.id_frais) from t_frais d" +
                    " left join t_clients client on client.id_client = d.id_client" +
                    "     and (client.f_nom like concat('%', ?2, '%')" +
                    "        or client.f_prenom like concat('%', ?2, '%') )" +
                    " left join t_dossiers dossier on dossier.id_doss = d.id_doss  and dossier.nomenclature like COALESCE(concat('%', ?3, '%'), '%')" +
                    " where d.vc_key = ?1 and d.is_deleted = false " +
                    " and d.id_poste like coalesce(?4, '%')" +
                    " and d.id_type like concat('%', ?5, '%')" +
                    " and d.id_compte like concat(?6, '%')")
    Page<TFrais> findAllWithPagination(String vcKey, String searchCriteriaClient, String searchCriteriaNomenclature, String searchCriteriaPoste, String typeId, String searchCriteriaCompte, Pageable pageable);

    @Query(value = "SELECT d from TFrais d where d.idDoss = ?1 and d.vcKey = ?2 and d.isDeleted = false ",
            countQuery = "SELECT count(d) from TFrais d where d.idDoss = ?1 and d.vcKey = ?2 and d.isDeleted = false ")
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
            "and compt.accountTypeId = 1 and d.isDeleted = false ")
    Page<TFrais> findByDossierIAndDeboursdWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.fraisCollaboration = true " +
            "and compt.accountTypeId = 1 and d.isDeleted = false",
            countQuery = "SELECT count(d) " +
                    "from TFrais d " +
                    "join d.refPoste poste " +
                    "left join d.refCompte compt " +
                    "where d.idDoss = ?1 and d.vcKey = ?2 " +
                    "and poste.fraisCollaboration = true " +
                    "and compt.accountTypeId = 1 and d.isDeleted = false ")
    Page<TFrais> findByDossierIdAndFraisCollaborationWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and poste.honoraires = true " +
            "and compt.accountTypeId = 1 and d.isDeleted = false",
            countQuery = "SELECT count(d) " +
                    "from TFrais d " +
                    "join d.refPoste poste " +
                    "left join d.refCompte compt " +
                    "where d.idDoss = ?1 and d.vcKey = ?2 " +
                    "and poste.honoraires = true " +
                    "and compt.accountTypeId = 1 and d.isDeleted = false")
    Page<TFrais> findByDossierIdAndHonoraireWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT d from TFrais d " +
            "join d.refPoste poste " +
            "left join d.refCompte compt " +
            " where d.idDoss = ?1 " +
            "and d.vcKey = ?2 " +
            "and compt.accountTypeId = 2 and d.isDeleted = false ",
            countQuery = "SELECT count(d) " +
                    "from TFrais d " +
                    "left join d.refCompte compt " +
                    "where d.idDoss = ?1 and d.vcKey = ?2 " +
                    "and compt.accountTypeId = 2 and d.isDeleted = false ")
    Page<TFrais> findByDossierIdAndTiersWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT count(ts) from TFrais ts" +
            " join ts.refPoste poste " +
            " join ts.tDossiers d " +
            " join d.dossierRightsList dr " +
            " where ts.idFrais in ?1 " +
            " and ts.idDoss = ?2" +
            " and poste.fraisProcedure = true" +
            " and dr.vcUserId = ?3 and ts.isDeleted = false")
    Long countAllFraisDeboursByIdAndDossierId(List<Long> fraisId, Long dossierId, Long id);

    @Query(value = "SELECT count(ts) from TFrais ts" +
            " join ts.refPoste poste " +
            " join ts.tDossiers d " +
            " join d.dossierRightsList dr " +
            " where ts.idFrais in ?1 " +
            " and ts.idDoss = ?2" +
            " and poste.fraisCollaboration = true" +
            " and dr.vcUserId = ?3 and ts.isDeleted = false")
    Long countAllFraisCollaByIdAndDossierId(List<Long> fraisId, Long dossierId, Long id);

    @Query(nativeQuery = true, value = "select" +
            " frais.id_frais as id, frais.vc_key as vcKey, frais.id_poste as idPost, poste.ref_poste as posteDescription, " +
            " frais.montant as montant, frais.montantht as montantHt,  concat(client.f_nom, ' ', client.f_prenom) as tiersFullname, " +
            " factureFraisDebour.ID as factureExtFraisId, factureFraisDebour.ID as factureLinkedFraisId," +
            " ifNull(factureFraisDebour.ID, 0) as invoiceChecked, ifNull(facture.id_facture, 0) as alreadyInvoiced," +
            " facture.facture_ref as factExtRef, facture.id_facture as factExtId, frais.id_transaction as id_transaction" +
            " from t_frais frais" +
            "         left join t_dossiers dossier on frais.id_doss = dossier.id_doss" +
            "         left join t_dossier_rights dr on dr.DOSSIER_ID = dossier.id_doss" +
            "         inner join ref_poste poste on poste.id_poste = frais.id_poste" +
            "         left join t_clients client on client.id_client = frais.id_client" +
            "         left outer join t_facture_frais_debours factureFraisDebour" +
            "                         on frais.id_frais = factureFraisDebour.FRAIS_ID" +
            "                                and (factureFraisDebour.FACTURE_ID = :invoiceId)" +
            "         left outer join t_facture_frais_debours factureFraisDebour2" +
            "                         on frais.id_frais = factureFraisDebour2.FRAIS_ID and factureFraisDebour2.ID <> coalesce(factureFraisDebour.ID, 0)" +
            "         left outer join t_factures facture" +
            "                         on facture.id_facture = factureFraisDebour2.FACTURE_ID" +
            " where dr.DOSSIER_ID = :dossierId" +
            "  and dr.VC_USER_ID = :vcUserId" +
            "  and poste.is_frais_procedure = true and frais.is_deleted = false " +
            " and case when :filterAlreadyInvoiced = 0 then facture.id_facture is null" +
            "    when :filterAlreadyInvoiced = 1 then facture.id_facture is not null else 1=1 end")
    List<Object[]> findAllDeboursByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long vcUserId, Boolean filterAlreadyInvoiced);

    @Query(nativeQuery = true, value = "select" +
            " frais.id_frais as id, frais.vc_key as vcKey, frais.id_poste as idPost, poste.ref_poste as posteDescription, " +
            " frais.montant as montant, frais.montantht as montantHt,  concat(client.f_nom, ' ', client.f_prenom) as tiersFullname, " +
            " factureFraisCollaboration.ID as factureExtFraisId, factureFraisCollaboration.ID as factureLinkedFraisId," +
            " ifNull(factureFraisCollaboration.ID, 0) as invoiceChecked, ifNull(facture.id_facture, 0) as alreadyInvoiced," +
            " facture.facture_ref as factExtRef, facture.id_facture as factExtId, frais.id_transaction as id_transaction" +
            " from t_frais frais" +
            "         left join t_dossiers dossier on frais.id_doss = dossier.id_doss" +
            "         left join t_dossier_rights dr on dr.DOSSIER_ID = dossier.id_doss" +
            "         inner join ref_poste poste on poste.id_poste = frais.id_poste" +
            "         left join t_clients client on client.id_client = frais.id_client" +
            "         left outer join t_facture_frais_collaboration factureFraisCollaboration" +
            "                         on frais.id_frais = factureFraisCollaboration.FRAIS_ID" +
            "                                and (factureFraisCollaboration.FACTURE_ID = :invoiceId)" +
            "         left outer join t_facture_frais_collaboration factureFraisCollaboration2" +
            "                         on frais.id_frais = factureFraisCollaboration2.FRAIS_ID and factureFraisCollaboration2.ID <> coalesce(factureFraisCollaboration.ID, 0)" +
            "         left outer join t_factures facture" +
            "                         on facture.id_facture = factureFraisCollaboration2.FACTURE_ID" +
            " where dr.DOSSIER_ID = :dossierId" +
            "  and dr.VC_USER_ID = :vcUserId" +
            "  and poste.is_frais_collaboration = true and frais.is_deleted = false " +
            " and case when :filterAlreadyInvoiced = 0 then facture.id_facture is null" +
            "    when :filterAlreadyInvoiced = 1 then facture.id_facture is not null else 1=1 end")
    List<Object[]> findAllCollabByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long vcUserId, Boolean filterAlreadyInvoiced);

    @Query(value = "select new com.ulegalize.dto.template.ItemDebours(poste.refPoste," +
            "                   concat(client.f_nom, ' ', client.f_prenom)," +
            "                   sheet.montantht," +
            "                   sheet.tva," +
            "                   sheet.montant)" +
            "             from TFrais sheet" +
            "                     inner join RefPoste poste on poste.idPoste = sheet.idPoste" +
            "                     left join TClients client on client.id_client = sheet.idClient" +
            "                     INNER JOIN FactureFraisDebours ft ON ft.fraisId = sheet.idFrais" +
            "             where  ft.tFactures.idFacture = ?1" +
            "             and poste.fraisProcedure = true")
    List<ItemDebours> findAllDeboursByIdPosteAndFactureId(Long idFacture);

    @Query(value = "select new com.ulegalize.dto.template.ItemFraisColla(poste.refPoste," +
            "                   concat(client.f_nom, ' ', client.f_prenom)," +
            "                   sheet.montantht," +
            "                   sheet.tva," +
            "                   sheet.montant)" +
            "             from TFrais sheet" +
            "                     inner join RefPoste poste on poste.idPoste = sheet.idPoste" +
            "                     left join TClients client on client.id_client = sheet.idClient" +
            "                     INNER JOIN FactureFraisCollaboration ft ON ft.fraisId = sheet.idFrais" +
            "             where  ft.tFactures.idFacture = ?1" +
            "             and poste.fraisCollaboration = true")
    List<ItemFraisColla> findAllCollaByIdPosteAndFactureId(Long idFacture);
}