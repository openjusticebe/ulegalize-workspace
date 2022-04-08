package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TDebour;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TDebourRepository extends JpaRepository<TDebour, Long>, JpaSpecificationExecutor<TDebour> {

    @Query(value = "SELECT d from TDebour d join d.tDossiers.dossierRightsList dr where d.idDoss = ?1 and dr.vcUserId = ?2")
    List<TDebour> findByDossierIdWithPagination(Long dossierId, Long vcUserId, Pageable pageable);

    @Query(value = "SELECT count(d) from TDebour d join d.tDossiers.dossierRightsList dr where d.idDoss = ?1 and dr.vcUserId = ?2")
    Long countByDossierIdWithPagination(Long dossierId, Long vcUserId);

    @Query(value = "SELECT d from TDebour d join d.tDossiers.dossierRightsList dr where dr.vcUserId = ?1")
    List<TDebour> findAllWithPagination(Long vcUserId, Pageable pageable);

    @Query(value = "SELECT COALESCE(sum(d.unit*d.pricePerUnit), 0) from TDebour d join d.tDossiers.dossierRightsList dr where d.idDoss = ?1 and dr.vcUserId = ?2")
    BigDecimal sumByDossierId(Long dossierId, Long vcUserId);

    @Query(value = "SELECT count(ts) from TDebour ts" +
            " join ts.tDossiers d " +
            " join d.dossierRightsList dr " +
            " where ts.idDebour in ?1 " +
            " and ts.idDoss = ?2" +
            " and dr.vcUserId = ?3")
    Long countAllByIdAndDossierId(List<Long> deboursId, Long dossierId, Long id);

    @Query(nativeQuery = true, value = "select" +
            " debour.id_debour as id, debour.id_debour_type as idDebourType, debourType.description as debourTypeDescription, debour.price_per_unit as pricePerUnit," +
            " debour.unit as unit, debour.id_mesure_type as idMesureType, mesureType.description as mesureDescription, debour.id_doss as idDoss," +
            " dossier.year_doss as yearDossier, dossier.num_doss as numDossier, debour.date_action as dateAction, debour.comment as comment, facture_frais_admin.ID as factureLinkedFraisId," +
            " ifNull(facture_frais_admin.ID, 0) as invoiceChecked, ifNull(facture.id_facture, 0) as alreadyInvoiced," +
            " facture.facture_ref as factExtRef, facture.id_facture as factExtId" +
            " from t_debour debour" +
            "         inner join t_dossiers dossier on debour.id_doss = dossier.id_doss" +
            "         inner join t_dossier_rights dr on dr.DOSSIER_ID = dossier.id_doss" +
            "         inner join t_debour_type debourType on debourType.id_debour_type = debour.id_debour_type" +
            "         inner join t_mesure_type mesureType on mesureType.id_mesure_type = debour.id_mesure_type" +
            "         left outer join t_facture_frais_admin facture_frais_admin" +
            "                         on debour.id_debour = facture_frais_admin.DEBOURS_ID" +
            "                                and (facture_frais_admin.facture_id = ?1)" +
            "         left outer join t_facture_frais_admin facture_frais_admin2" +
            "                         on debour.id_debour = facture_frais_admin2.DEBOURS_ID and facture_frais_admin2.ID <> coalesce(facture_frais_admin.ID, 0)" +
            "         left outer join t_factures facture" +
            "                         on facture.id_facture = facture_frais_admin2.FACTURE_ID" +
            " where dr.DOSSIER_ID = ?2" +
            "  and dr.VC_USER_ID = ?3" +
            " and case when ?4 = 0 then facture.id_facture is null" +
            "    when ?4 = 1 then facture.id_facture is not null else 1=1 end")
    List<Object[]> findAllByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long vcUserId, Boolean filterAlreadyInvoiced);
}