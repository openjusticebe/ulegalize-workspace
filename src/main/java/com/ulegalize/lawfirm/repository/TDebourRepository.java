package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.FraisAdminDTO;
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

    @Query("select new com.ulegalize.dto.FraisAdminDTO(d.idDebour , " +
            " d.idDebourType, type.description, " +
            " d.pricePerUnit, d.unit, " +
            " d.idMesureType, mesure.description, " +
            " dossier.idDoss, d.dateAction, " +
            "  d.comment, ft.ID) " +
            " from TDebour d" +
            " join d.tDossiers dossier " +
            " join d.tDebourType type " +
            " join d.tMesureType mesure " +
            " join dossier.dossierRightsList dr " +
            " left join d.factureFraisAdmins ft on ft.tFactures.idFacture = ?1" +
            " where dr.dossierId = ?2 " +
            " and dr.vcUserId = ?3")
    List<FraisAdminDTO> findAllByInvoiceIdDossierId(Long invoiceId, Long dossierId, Long id);
}