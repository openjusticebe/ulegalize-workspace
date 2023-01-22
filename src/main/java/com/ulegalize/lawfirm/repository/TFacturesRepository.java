package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.ItemLongDto;
import com.ulegalize.enumeration.EnumFactureType;
import com.ulegalize.lawfirm.model.entity.TFactures;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TFacturesRepository extends JpaRepository<TFactures, Long>, JpaSpecificationExecutor<TFactures> {
    Optional<TFactures> findByIdFactureAndVcKey(Long factureId, String vcKey);

    @Query(value = "SELECT d from TFactures d " +
            " join d.tClients client " +
            " left join d.tDossiers doss on doss.nomenclature like COALESCE(CONCAT(:searchNomenclature, '%'), '%') " +
            " where d.vcKey = :vcKey " +
            " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
            " and (client.f_nom like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) " +
            " or client.f_prenom like COALESCE(CONCAT('%', :searchClient, '%'), '%' )" +
            " or client.f_company like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) )",
            countQuery = "SELECT count(d) from TFactures d" +
                    " join d.tClients client " +
                    " left join d.tDossiers doss on doss.nomenclature like COALESCE(CONCAT(:searchNomenclature, '%'), '%') " +
                    " where d.vcKey = :vcKey " +
                    " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
                    " and (client.f_nom like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) " +
                    " or client.f_prenom like COALESCE(CONCAT('%', :searchClient, '%'), '%' )" +
                    " or client.f_company like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) )")
    Page<TFactures> findAllWithPagination(String vcKey, Integer searchEcheance, String searchNomenclature, String searchClient, Pageable pageable);

    @Query(value = "SELECT d from TFactures d " +
            " left join d.tDossiers doss on doss.nomenclature like COALESCE(CONCAT(:searchNomenclature, '%'), '%')" +
            " where d.vcKey = :vcKey " +
            " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
            " and d.dateValue = :searchDate ",
            countQuery = "SELECT count(d) from TFactures d" +
                    " left join d.tDossiers doss on doss.nomenclature like COALESCE(CONCAT(:searchNomenclature, '%'), '%')" +
                    " where d.vcKey = :vcKey " +
                    " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
                    " and d.dateValue = :searchDate ")
    Page<TFactures> findAllByDateWithPagination(String vcKey, Integer searchEcheance, ZonedDateTime searchDate, String searchNomenclature, Pageable pageable);

    @Query(value = "SELECT d from TFactures d where d.vcKey = ?1")
    List<TFactures> findAll(String vcKey);

    @Query(value = "SELECT d from TFactures d " +
            " where d.idDoss = ?1 and d.vcKey = ?2 order by d.valid asc, d.idFacture desc",
            countQuery = "SELECT count(d) from TFactures d where d.idDoss = ?1 and d.vcKey = ?2")
    Page<TFactures> findByDossierIdWithPagination(Long dossierId, String vcKey, Pageable pageable);

    @Query(value = "SELECT count(d) " +
            " from TFactures d " +
            " join d.tFactureDetailsList fd " +
            " where d.vcKey = ?1" +
            " and fd.tva = ?2 ")
    Long countAllByVcKeyAndVat(String vcKey, BigDecimal vat);

    @Query(value = "SELECT count(d) from TFactures d " +
            " join d.tClients client " +
            "where d.vcKey = ?1 and client.f_company = ?1")
    Long countAllByVcKeyAndClient(String vcKey);

    @Query(value = "SELECT max(t.numFactTemp) from TFactures t where t.vcKey = ?1 and t.idFactureType = ?2")
    Integer getMaxNumFactTempByVcKey(String vcKey, EnumFactureType enumFactureType);

    @Query(value = "SELECT max(t.numFacture) from TFactures t where t.vcKey = ?1 and t.idFactureType = ?2 and t.yearFacture = ?3 and t.valid = true")
    Integer getMaxNumFactureAndValid(String vcKey, EnumFactureType factureType, Integer yearFacture);

    @Query(value = "SELECT count(d)" +
            " from TFactures d " +
            " where d.vcKey = ?1 and d.valid = true"
    )
    Long countAllActiveByVcKey(String vcKey);

    @Query(nativeQuery = true, value = "SELECT COALESCE(sum( case when t.id_facture_type <> 2 and t.id_facture_type <> 4 then sub.montant else -sub.montant end ), 0) " +
            " from t_factures t " +
            " join (" +
            " select round(coalesce(sum(tfd.htva), 0) , 2) montant, t.id_facture " +
            " from t_factures t " +
            " join t_facture_details tfd on t.id_facture = tfd.id_facture " +
            " where t.vc_key = ?1" +
            " and t.id_doss = ?2 " +
            " group by t.id_facture " +
            ") sub on t.id_facture = sub.id_facture")
    BigDecimal sumHtvaInvoiceByVcKey(String vcKey, Long dossierId);

    @Query(value = "SELECT COALESCE(sum(case when t.idFactureType <> com.ulegalize.enumeration.EnumFactureType.CREDIT then t.montant else -t.montant end), 0) " +
            " from TFactures t " +
            " where t.vcKey = ?1 " +
            " and t.idDoss = ?2 ")
    BigDecimal sumTvacInvoiceByVcKey(String vcKey, Long dossierId);


    @Query(value = "SELECT new com.ulegalize.dto.ItemLongDto( t.idFacture, CONCAT(t.factureRef, ' - ', COALESCE(tc.f_nom, ''), COALESCE(tc.f_prenom, ''), ' - ',  COALESCE(t.montant, ''), ' €', ' - ', COALESCE(t.dateValue, ''))) " +
            " from TFactures t " +
            " left join TClients tc on tc.id_client = t.tClients.id_client " +
            " where (t.vcKey like UPPER(:vckey) and (t.factureRef like UPPER(CONCAT('%',:searchCriteria,'%')) or tc.f_nom like (CONCAT('%',:searchCriteria,'%')) or tc.f_prenom like (CONCAT('%',:searchCriteria,'%'))))" +
            " and (:dossierId is null or t.idDoss = :dossierId)")
    List<ItemLongDto> findInvoicesBySearchCriteria(String vckey, String searchCriteria, Long dossierId);


    @Query(value = "select count(t.numFacture) " +
            "from TFactures t " +
            "where t.vcKey = :vcKey and t.idFactureType = :enumFactureType and t.yearFacture = :year ")
    Long countInvoiceByVcAndYear(String vcKey, EnumFactureType enumFactureType, Integer year);

}
