package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TFactureTimesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TFactureTimesheetRepository extends JpaRepository<TFactureTimesheet, Integer>, JpaSpecificationExecutor<TFactureTimesheet> {
    @Query("select t from TFactureTimesheet t" +
            " where t.tsId = :tsId" +
            " and t.ID <> ifnull(:id, 0)")
    List<TFactureTimesheet> findByTsIdAndNotId(Integer id, Long tsId);

    @Query("select t from TFactureTimesheet t " +
            " where t.tFactures.idFacture = :idFacture")
    List<TFactureTimesheet> findAllByTFactures(Long idFacture);

    @Query(nativeQuery = true, value = "select " +
            " round( if( sheet.is_forfait = 0, ((sheet.dh * 60+sheet.dm) / 60) * sheet.couthoraire , sheet.forfait_ht),2) cout_htva ," +
            " round( if( sheet.is_forfait = 0, (((sheet.dh * 60+sheet.dm) / 60) * sheet.couthoraire ) * (1+sheet.vat/100) , sheet.forfait_ht * (1+sheet.vat/100)) ,2 ) cout," +
            " sheet.date_action," +
            " tt.description," +
            " sheet.vat tva" +
            " from t_timesheet sheet" +
            " INNER JOIN t_facture_timesheet ft ON ft.TS_ID = sheet.id_ts" +
            "    inner join t_timesheet_type tt on tt.id_ts = sheet.ts_type" +
            "    where  ft.FACTURE_ID = :idFacture")
    List<Object[]> findAllByIdFactures(Long idFacture);
}