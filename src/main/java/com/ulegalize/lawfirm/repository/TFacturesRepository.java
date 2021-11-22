package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactures;
import com.ulegalize.lawfirm.model.enumeration.EnumFactureType;
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
            " left join d.tDossiers doss " +
            " where d.vcKey = :vcKey " +
            " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
            " and doss.year_doss like COALESCE(CONCAT(:searchYearDossier, '%'), '%') " +
            " and doss.num_doss like COALESCE(CONCAT(:searchNumberDossier, '%'), '%') " +
            " and (client.f_nom like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) " +
            " or client.f_prenom like COALESCE(CONCAT('%', :searchClient, '%'), '%' )" +
            " or client.f_company like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) )" +
            " order by d.valid asc, d.idFacture desc",
            countQuery = "SELECT count(d) from TFactures d" +
                    " join d.tClients client " +
                    " left join d.tDossiers doss " +
                    " where d.vcKey = :vcKey " +
                    " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
                    " and doss.year_doss like COALESCE(CONCAT(:searchYearDossier, '%'), '%') " +
                    " and doss.num_doss like COALESCE(CONCAT(:searchNumberDossier, '%'), '%') " +
                    " and (client.f_nom like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) " +
                    " or client.f_prenom like COALESCE(CONCAT('%', :searchClient, '%'), '%' )" +
                    " or client.f_company like COALESCE(CONCAT('%', :searchClient, '%'), '%' ) )")
    Page<TFactures> findAllWithPagination(String vcKey, Integer searchEcheance, String searchYearDossier, Long searchNumberDossier, String searchClient, Pageable pageable);

    @Query(value = "SELECT d from TFactures d " +
            " left join d.tDossiers doss " +
            " where d.vcKey = :vcKey " +
            " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
            " and d.dateValue = :searchDate " +
            " and doss.year_doss like COALESCE(CONCAT(:searchYearDossier, '%'), '%') " +
            " and doss.num_doss like COALESCE(CONCAT(:searchNumberDossier, '%'), '%') " +
            " order by d.valid asc, d.idFacture desc",
            countQuery = "SELECT count(d) from TFactures d" +
                    " left join d.tDossiers doss " +
                    " where d.vcKey = :vcKey " +
                    " and d.idEcheance like COALESCE(:searchEcheance, '%') " +
                    " and d.dateValue = :searchDate " +
                    " and doss.year_doss like COALESCE(CONCAT(:searchYearDossier, '%'), '%') " +
                    " and doss.num_doss like COALESCE(CONCAT(:searchNumberDossier, '%'), '%') ")
    Page<TFactures> findAllByDateWithPagination(String vcKey, Integer searchEcheance, ZonedDateTime searchDate, String searchYearDossier, Long searchNumberDossier, Pageable pageable);

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

    @Query(value = "SELECT max(t.numFactTemp) from TFactures t where t.vcKey = ?1")
    Integer getMaxNumFactTempByVcKey(String vcKey);

    @Query(value = "SELECT max(t.numFacture) from TFactures t where t.vcKey = ?1 and t.idFactureType = ?2 and t.yearFacture = ?3 and t.valid = true")
    Integer getMaxNumFacture(String vcKey, EnumFactureType factureType, Integer yearFacture);


    @Query(value = "SELECT COALESCE(sum( fd.htva ), 0) " +
            " from TFactures t " +
            " join t.tFactureDetailsList fd " +
            " where t.vcKey = ?1 " +
            " and t.idDoss = ?2 ")
    BigDecimal sumHtvaInvoiceByVcKey(String vcKey, Long dossierId);

    @Query(value = "SELECT COALESCE(sum(t.montant), 0) " +
            " from TFactures t " +
            " where t.vcKey = ?1 " +
            " and t.idDoss = ?2 ")
    BigDecimal sumTvacInvoiceByVcKey(String vcKey, Long dossierId);
}