package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TTimesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TTimesheetRepository extends JpaRepository<TTimesheet, Long>, JpaSpecificationExecutor<TTimesheet> {

    @Query(
            value = "SELECT t from TTimesheet t " +
                    " join t.tDossiers.dossierRightsList dr " +
                    " join t.tDossiers d" +
                    " join t.tTimesheetType tsT" +
                    " where dr.vcUserId = ?1 " +
                    " and dr.vcOwner in (1, 2)" +
                    " and d.year_doss like COALESCE(concat('%', ?2, '%'), '%')" +
                    " and d.num_doss like COALESCE(CONCAT(?3, '%'), '%')" +
                    " and tsT.idTs like COALESCE(?4, '%')",
            countQuery = "SELECT count(t) from TTimesheet t" +
                    " join t.tDossiers.dossierRightsList dr " +
                    " join t.tDossiers d" +
                    " join t.tTimesheetType tsT" +
                    " where dr.vcUserId = ?1 " +
                    " and dr.vcOwner in (1, 2)" +
                    " and d.year_doss like COALESCE(concat('%', ?2, '%'), '%')" +
                    " and d.num_doss like COALESCE(CONCAT(?3, '%'), '%')" +
                    " and tsT.idTs like COALESCE(?4, '%')")
    Page<TTimesheet> findAllWithPagination(Long vcUserIds, String searchCriteriaYear, Long numberDossier, Integer idTsType, Pageable pageable);

    @Query(value = "SELECT d from TTimesheet d " +
            " join d.tDossiers.dossierRightsList dr " +
            " where dr.dossierId = ?1" +
            " and dr.vcUserId = ?2")
    Page<TTimesheet> findAllByDossierIdWithPagination(Long dossierId, Long vcUserIds, Pageable pageable);

    @Query(value = "SELECT d from TTimesheet d" +
            " join d.tDossiers.dossierRightsList dr " +
            " where dr.dossierId = ?1 and dr.vcUserId = ?2")
    List<TTimesheet> findAllByDossierId(Long dossierId, Long vcUserIds);

    @Query(value = "SELECT count(ts) from TTimesheet ts" +
            " join ts.tDossiers d " +
            " join d.dossierRightsList dr " +
            " where ts.idTs in ?1 " +
            " and ts.idDoss = ?2" +
            " and dr.vcUserId = ?3")
    Long countAllByIdAndDossierId(List<Long> tsId, Long dossierId, Long vcUserid);

    @Query(nativeQuery = true, value = "select" +
            " timesheet.id_ts as id, timesheet.id_doss as dossierId, dossier.year_doss as yearDossier, dossier.num_doss as numDossier, " +
            " timesheet.id_gest as idGest, user.email as email, timesheet.ts_type as tsType, timesheetType.description as tsTypeDescription, " +
            " timesheet.couthoraire as couthoraire, timesheet.date_action as dateAction, timesheet.dh as dh, timesheet.dm as dm, timesheet.comment as comment, timesheet.vat as vat, " +
            " timesheet.is_forfait as forfait, timesheet.forfait_ht as forfaitHt, facture_timesheet.ID as factureTimesheetExtId, " +
            " coalesce (facture_timesheet.ID, 0) as invoiceChecked, coalesce (facture.id_facture, 0) as alreadyInvoiced, facture.facture_ref as factExtRef, facture.id_facture as factExtId" +
            " from t_timesheet timesheet" +
            "         inner join t_dossiers dossier on timesheet.id_doss = dossier.id_doss" +
            "         inner join t_dossier_rights dr on dr.DOSSIER_ID = dossier.id_doss" +
            "         inner join t_users user on user.id = timesheet.id_gest" +
            "         inner join t_timesheet_type timesheetType on timesheetType.id_ts = timesheet.ts_type" +
            "         left outer join t_facture_timesheet facture_timesheet" +
            "                         on timesheet.id_ts = facture_timesheet.ts_id" +
            "                                and (facture_timesheet.facture_id = :invoiceId)" +
            "         left outer join t_facture_timesheet facture_timesheet2" +
            "                         on timesheet.id_ts = facture_timesheet2.ts_id and facture_timesheet2.ID <> coalesce(facture_timesheet.ID, 0)" +
            "         left outer join t_factures facture" +
            "                         on facture.id_facture = facture_timesheet2.FACTURE_ID" +
            " where dr.DOSSIER_ID = :dossierId" +
            "  and dr.VC_USER_ID = :vcUserIds" +
            " and case when :filterAlreadyInvoiced = 0 then facture.id_facture is null" +
            "    when :filterAlreadyInvoiced = 1 then facture.id_facture is not null else 1=1 end")
    List<Object[]> findAllByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long vcUserIds, Boolean filterAlreadyInvoiced);

    @Query(value = "SELECT count(d) from TTimesheet d join d.tDossiers.dossierRightsList dr where dr.dossierId = ?1 and dr.vcUserId = ?2")
    Long countAllByIdDossAndVcKey(Long dossierId, Long vcUserId);

}