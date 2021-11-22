package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.PrestationSummary;
import com.ulegalize.lawfirm.model.entity.TTimesheet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TTimesheetRepository extends JpaRepository<TTimesheet, Long>, JpaSpecificationExecutor<TTimesheet> {

    @Query(value = "SELECT d from TTimesheet d " +
            " join d.tDossiers.dossierRightsList dr " +
            " where dr.vcUserId = ?1 " +
            " and dr.vcOwner in (1, 2)")
    List<TTimesheet> findAllWithPagination(Long vcUserIds, Pageable pageable);

    @Query(value = "SELECT d from TTimesheet d " +
            " join d.tDossiers.dossierRightsList dr " +
            " where dr.dossierId = ?1" +
            " and dr.vcUserId = ?2")
    List<TTimesheet> findAllByDossierIdWithPagination(Long dossierId, Long vcUserIds, Pageable pageable);

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

        @Query(value = "SELECT new com.ulegalize.dto.PrestationSummary(" +
                "d.idTs, dr.dossierId, dossier.year_doss, dossier.num_doss, d.idGest," +
                " user.email, d.tsType, d.tTimesheetType.description, d.couthoraire, d.dateAction, d.dh,d.dm, d.comment, " +
                " d.vat, d.forfait, d.forfaitHt, ft.ID) " +
                " from TTimesheet d" +
                " join d.tDossiers dossier " +
                " join dossier.dossierRightsList dr " +
                " join d.tUsers user " +
                " left join d.tFactureTimesheetList ft on ft.tFactures.idFacture = ?1" +
                " where dr.dossierId = ?2 " +
                " and dr.vcUserId = ?3")
    List<PrestationSummary> findAllByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long vcUserIds);

    @Query(value = "SELECT count(d) from TTimesheet d join d.tDossiers.dossierRightsList dr where dr.dossierId = ?1 and dr.vcUserId = ?2")
    Long countAllByIdDossAndVcKey(Long dossierId, Long vcUserId);

}