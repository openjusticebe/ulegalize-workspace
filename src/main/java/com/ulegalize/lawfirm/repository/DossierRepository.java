package com.ulegalize.lawfirm.repository;

import com.ulegalize.dto.IDossierDTO;
import com.ulegalize.enumeration.EnumVCOwner;
import com.ulegalize.lawfirm.model.entity.TDossiers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DossierRepository extends JpaRepository<TDossiers, Long> {

    @Query(value = "SELECT d from TDossiers d join d.dossierRightsList dr where dr.lawfirmUsers.lawfirm.vckey = ?1 order by d.year_doss desc, d.dossierNumber desc")
    List<TDossiers> findAllByVCKey(String vcKey);

    @Query(value = "SELECT d from TDossiers d " +
            "join d.dossierRightsList dr" +
            " where dr.lawfirmUsers.lawfirm.vckey = ?1" +
            " and dr.vcOwner in (?2) ")
    List<TDossiers> findAllByVCKey(String vcKey, List<EnumVCOwner> vcOwner);

    @Query(value = "SELECT distinct d" +
            " from TDossiers d" +
            " join d.dossierRightsList dr " +
            " join dr.lawfirmUsers lu" +
            " join lu.user user" +
            " where lu.lawfirm.vckey = ?1 " +
            " and d.idDoss not in (" +
            " SELECT distinct d.idDoss" +
            " from TDossiers d" +
            " join d.dossierRightsList dr " +
            " join dr.lawfirmUsers lu" +
            " join lu.user user" +
            " where lu.lawfirm.vckey = ?1 " +
            " and user.id = ?2 " +
            " and dr.vcOwner in (?3) " +
            ")" +
            " order by d.year_doss desc, d.dossierNumber desc")
    List<TDossiers> findAllByVCKeyAndUserId(String vcKey, Long userId, List<EnumVCOwner> vcOwner);

    @Query(value = "SELECT count(d)" +
            " from TDossiers d" +
            " join d.dossierRightsList dr " +
            " join dr.lawfirmUsers lu" +
            " join lu.user user" +
            " where dr.vcUserId = ?1" +
            " and dr.vcOwner in (?2) ")
    Long countSharedAffaires(Long vcUserId, List<EnumVCOwner> enumVcOwners);

    @Query(value = "SELECT d from TDossiers d " +
            " join fetch d.dossierRightsList dr " +
            " join d.dossierContactList dc " +
            " where d.idDoss = ?1 and dr.vcUserId = ?2 and dr.vcOwner in (?3)")
    Optional<TDossiers> findByIdDoss(Long id_doss, Long vcUserId, List<EnumVCOwner> enumVCOwnerList);

    @Query(value = "SELECT d from TDossiers d join d.dossierRightsList dr where d.idDoss = ?1 and dr.lawfirmUsers.lawfirm.vckey = ?2")
    Optional<TDossiers> findByIdDossAndVcKey(Long id_doss, String vcKey);

    @Query(value = "SELECT d from TDossiers d join d.dossierRightsList dr where d.idDoss = ?1 and dr.vcUserId = ?2")
    Optional<TDossiers> findAuthorizedByIdDoss(Long isDossier, Long vcUserId);

    @Query(nativeQuery = true, value = "select tdossiers0_.id_doss as id, tdossiers0_.year_doss as `year`, tdossiers0_.nomenclature as nomenclature, users.initiales, " +
            " dossierClient.partiesName as partiesName," +
            " subtot.balance as balance," +
            " owner.vc_key as vckeyOwner," +
            " tdossiers0_.date_close as closeDossier," +
            " tdossiers0_.date_open as openDossier," +
            " tdossiers0_.doss_type as type," +
            " dossierrig1_.last_access_date as lastAccessDate, " +
            " tagDossier.tags as tagsName " +
            " from t_dossiers tdossiers0_ " +
            " inner join t_dossier_rights dossierrig1_ on tdossiers0_.id_doss = dossierrig1_.dossier_id " +
            " left join (" +
            " select distinct tu2.vc_key, d.id_doss from t_dossiers d" +
            " inner join t_dossier_rights dr on d.id_doss = dr.dossier_id" +
            " inner join t_virtualcab_users tu2 on tu2.id = dr.VC_USER_ID" +
            " where  dr.VC_OWNER = 1" +
            " ) owner on owner.id_doss = tdossiers0_.id_doss " +
            " inner join t_users users on users.id = tdossiers0_.id_user_resp " +
            " inner join t_virtualcab_users tu on tu.id = dossierrig1_.VC_USER_ID " +
            " left join (" +
            "        select doss.DOSSIER_ID,  GROUP_CONCAT(fullname" +
            "                            SEPARATOR" +
            "                            ', ') partiesName" +
            "        from t_dossier_rights doss" +
            "                 left join t_dossier_contact as t_dossier_contact3" +
            "                           on t_dossier_contact3.dossier_id = doss.DOSSIER_ID and t_dossier_contact3.contact_type_id in (1,2,3,4,5)" +
            "           left join (" +
            "        select id_client," +
            "   (case when tclients4_.client_type=1 then concat(coalesce(tclients4_.f_nom, ''), ' ', coalesce(tclients4_.f_prenom, '')) else tclients4_.f_company end) as fullname " +
            "        from t_clients as tclients4_" +
            "        group by id_client" +
            "            ) tclients4_  on t_dossier_contact3.client_id = tclients4_.id_client" +
            "        where doss.VC_USER_ID = ?1" +
            "        group by doss.DOSSIER_ID" +
            ") dossierClient on dossierClient.DOSSIER_ID = dossierrig1_.DOSSIER_ID " +
            "  left join (" +
            "                 select tdvt.id_dossier,  GROUP_CONCAT(tvt.label" +
            "                                                       SEPARATOR" +
            "                                                       ', ') tags" +
            "                 from t_dossiers_vc_tags tdvt" +
            "                          left join t_virtualcab_tags tvt on tdvt.id_vc_tags = tvt.id " +
            "                 group by tdvt.id_dossier" +
            "             ) tagDossier on tagDossier.id_dossier = dossierrig1_.DOSSIER_ID " +
            " left join (" +
            "        select dossierrig1_.DOSSIER_ID," +
            " round(round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2),2) balance" +
            " from t_dossier_rights dossierrig1_" +
            "             inner join t_virtualcab_users tu2 on tu2.id = dossierrig1_.VC_USER_ID" +
            "  left join (" +
            " select id_doss,sum(tfd.htva) cout FROM" +
            " t_factures " +
            " inner join t_facture_details tfd on t_factures.id_facture = tfd.id_facture" +
            " group by id_doss) t_factures on dossierrig1_.DOSSIER_ID= t_factures.id_doss" +
            " left join (select id_doss,sum(cout) cout FROM ( " +
            " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.is_forfait = 1) THEN d.forfait_ht ELSE (( (( (d.dh * 60) + d.dm ) / 60 ) * d.couthoraire ) ) END), 0), 2) cout " +
            " from t_timesheet d) xx group by id_doss" +
            " ) presta on dossierrig1_.DOSSIER_ID=presta.id_doss" +
            " left join (select id_doss,sum(cout) cout FROM ( " +
            " SELECT d.id_doss, round(COALESCE((d.unit*d.price_per_unit), 0), 2) cout " +
            " from t_debour d " +
            " ) xx group by id_doss " +
            " ) fraisadmin on dossierrig1_.DOSSIER_ID=fraisadmin.id_doss " +
            " left join (select id_doss,sum(cout) cout FROM ( " +
            " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.id_type = 2) THEN -d.montantht ELSE (d.montantht) END), 0),2) cout " +
            " from t_frais d " +
            " join ref_poste poste on poste.id_poste = d.id_poste " +
            " join ref_compte compte on compte.id_compte = d.id_compte " +
            " and poste.is_frais_collaboration = 1 " +
            " and compte.account_type_id = 1" +
            "              ) xx group by id_doss" +
            "              ) fraisCollaboration on dossierrig1_.DOSSIER_ID=fraisCollaboration.id_doss " +
            " left join (select id_doss,sum(cout) cout FROM ( " +
            " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.id_type = 2) THEN -d.montantht ELSE (d.montantht) END), 0),2) cout " +
            " from t_frais d " +
            " join ref_poste poste on poste.id_poste = d.id_poste " +
            " join ref_compte compte on compte.id_compte = d.id_compte " +
            " and poste.is_frais_procedure = 1 " +
            " and compte.account_type_id = 1" +
            "              ) xx group by id_doss" +
            "              ) fraisProcedure on dossierrig1_.DOSSIER_ID=fraisProcedure.id_doss " +
            " left join (select id_doss,sum(cout) cout FROM ( " +
            " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.id_type > 1) THEN d.montantht ELSE (-d.montantht) END), 0),2) cout " +
            " from t_frais d " +
            " join ref_poste poste on poste.id_poste = d.id_poste " +
            " join ref_compte compte on compte.id_compte = d.id_compte " +
            " and poste.is_honoraires = 1 " +
            " and compte.account_type_id = 1" +
            "              ) xx group by id_doss" +
            "              ) honoraire on dossierrig1_.DOSSIER_ID=honoraire.id_doss " +
            " where tu2.id = ?1 " +
            ") subtot" +
            " on subtot.DOSSIER_ID = dossierrig1_.DOSSIER_ID" +
            " where dossierrig1_.vc_user_id = ?1 " +
            "   and dossierrig1_.vc_owner in (?2) " +
            "   and " +
            " dossierClient.partiesName like CONCAT('%', ?3, '%') " +
            "   and tdossiers0_.year_doss like COALESCE(CONCAT('%', ?8, '%'), '%') and tdossiers0_.nomenclature like COALESCE(CONCAT('%', ?4, '%'), '%')" +
            " and users.initiales like CONCAT('%', ?5, '%')" +
            "  and (case when ?6 = 1 then subtot.balance <> 0 " +
            " when ?6 = 0 then subtot.balance =0 else 1=1 end) " +
            " and (case when ?7 = 0 then tdossiers0_.date_close is null " +
            " when ?7 = 1 then tdossiers0_.date_close is not null else 1=1 end) ",
            countQuery = "select count(tdossiers0_.id_doss) " +
                    " from t_dossiers tdossiers0_ " +
                    " inner join t_dossier_rights dossierrig1_ on tdossiers0_.id_doss = dossierrig1_.dossier_id " +
                    " left join (" +
                    " select distinct tu2.vc_key, d.id_doss from t_dossiers d" +
                    " inner join t_dossier_rights dr on d.id_doss = dr.dossier_id" +
                    " inner join t_virtualcab_users tu2 on tu2.id = dr.VC_USER_ID" +
                    " where  dr.VC_OWNER = 1" +
                    " ) owner on owner.id_doss = tdossiers0_.id_doss " +
                    " inner join t_users users on users.id = tdossiers0_.id_user_resp " +
                    " inner join t_virtualcab_users tu on tu.id = dossierrig1_.VC_USER_ID " +
                    " left join (" +
                    "        select doss.DOSSIER_ID,  GROUP_CONCAT(fullname" +
                    "                            SEPARATOR" +
                    "                            ', ') partiesName" +
                    "        from t_dossier_rights doss" +
                    "                 left join t_dossier_contact as t_dossier_contact3" +
                    "                           on t_dossier_contact3.dossier_id = doss.DOSSIER_ID and t_dossier_contact3.contact_type_id in (1,2,3,4,5) " +
                    "           left join (" +
                    "        select id_client," +
                    "            concat(coalesce(tclients4_.f_nom, ''), ' ', coalesce(tclients4_.f_prenom, '')) as fullname" +
                    "        from t_clients as tclients4_" +
                    "        group by id_client" +
                    "            ) tclients4_  on t_dossier_contact3.client_id = tclients4_.id_client" +
                    "        where doss.VC_USER_ID = ?1" +
                    "        group by doss.DOSSIER_ID" +
                    ") dossierClient on dossierClient.DOSSIER_ID = dossierrig1_.DOSSIER_ID " +
                    "  left join (" +
                    "                 select tdvt.id_dossier,  GROUP_CONCAT(tvt.label" +
                    "                                                       SEPARATOR" +
                    "                                                       ', ') tags" +
                    "                 from t_dossiers_vc_tags tdvt" +
                    "                          left join t_virtualcab_tags tvt on tdvt.id_vc_tags = tvt.id " +
                    "                 group by tdvt.id_dossier" +
                    "             ) tagDossier on tagDossier.id_dossier = dossierrig1_.DOSSIER_ID " +
                    "left join (" +
                    "        select dossierrig1_.DOSSIER_ID," +
                    " round(round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2),2) balance" +
                    " from t_dossier_rights dossierrig1_" +
                    "             inner join t_virtualcab_users tu2 on tu2.id = dossierrig1_.VC_USER_ID" +
                    "  left join (" +
                    " select id_doss,sum(tfd.htva) cout FROM" +
                    " t_factures " +
                    " inner join t_facture_details tfd on t_factures.id_facture = tfd.id_facture" +
                    " group by id_doss) t_factures on dossierrig1_.DOSSIER_ID= t_factures.id_doss" +
                    " left join (select id_doss,sum(cout) cout FROM ( " +
                    " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.is_forfait = 1) THEN d.forfait_ht ELSE (( (( (d.dh * 60) + d.dm ) / 60 ) * d.couthoraire ) ) END), 0), 2) cout " +
                    " from t_timesheet d) xx group by id_doss" +
                    " ) presta on dossierrig1_.DOSSIER_ID=presta.id_doss" +
                    " left join (select id_doss,sum(cout) cout FROM ( " +
                    " SELECT d.id_doss, round(COALESCE((d.unit*d.price_per_unit), 0), 2) cout " +
                    " from t_debour d " +
                    " ) xx group by id_doss " +
                    " ) fraisadmin on dossierrig1_.DOSSIER_ID=fraisadmin.id_doss " +
                    " left join (select id_doss,sum(cout) cout FROM ( " +
                    " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.id_type = 2) THEN -d.montantht ELSE (d.montantht) END), 0),2) cout " +
                    " from t_frais d " +
                    " join ref_poste poste on poste.id_poste = d.id_poste " +
                    " join ref_compte compte on compte.id_compte = d.id_compte " +
                    " and poste.is_frais_collaboration = 1 " +
                    " and compte.account_type_id = 1" +
                    "              ) xx group by id_doss" +
                    "              ) fraisCollaboration on dossierrig1_.DOSSIER_ID=fraisCollaboration.id_doss " +
                    " left join (select id_doss,sum(cout) cout FROM ( " +
                    " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.id_type = 2) THEN -d.montantht ELSE (d.montantht) END), 0),2) cout " +
                    " from t_frais d " +
                    " join ref_poste poste on poste.id_poste = d.id_poste " +
                    " join ref_compte compte on compte.id_compte = d.id_compte " +
                    " and poste.is_frais_procedure = 1 " +
                    " and compte.account_type_id = 1" +
                    "              ) xx group by id_doss" +
                    "              ) fraisProcedure on dossierrig1_.DOSSIER_ID=fraisProcedure.id_doss " +
                    " left join (select id_doss,sum(cout) cout FROM ( " +
                    " SELECT d.id_doss, round(COALESCE(( CASE WHEN (d.id_type > 1) THEN d.montantht ELSE (-d.montantht) END), 0),2) cout " +
                    " from t_frais d " +
                    " join ref_poste poste on poste.id_poste = d.id_poste " +
                    " join ref_compte compte on compte.id_compte = d.id_compte " +
                    " and poste.is_honoraires = 1 " +
                    " and compte.account_type_id = 1" +
                    "              ) xx group by id_doss" +
                    "              ) honoraire on dossierrig1_.DOSSIER_ID=honoraire.id_doss " +
                    " where tu2.id = ?1 " +
                    ") subtot" +
                    " on subtot.DOSSIER_ID = dossierrig1_.DOSSIER_ID" +
                    " where dossierrig1_.vc_user_id = ?1 " +
                    "   and dossierrig1_.vc_owner in (?2) " +
                    "   and " +
                    " dossierClient.partiesName like CONCAT('%', ?3, '%') " +
                    "   and tdossiers0_.year_doss like COALESCE(CONCAT('%', ?8, '%'), '%') and tdossiers0_.nomenclature like COALESCE(CONCAT('%', ?4, '%'), '%')" +
                    " and users.initiales like CONCAT('%', ?5, '%')" +
                    "  and (case when ?6 = 1 then subtot.balance <> 0 " +
                    " when ?6 = 0 then subtot.balance =0 else 1=1 end)" +
                    "  and (case when ?7 = 0 then tdossiers0_.date_close is null " +
                    " when ?7 = 1 then tdossiers0_.date_close is not null else 1=1 end) "
    )
    Page<IDossierDTO> findByVcUserIdAllWithPagination(Long vcUserId, List<Integer> vcOwner,
                                                      String searchCriteriaClient, String nomenclature, String initiales,
                                                      Boolean withBalance,
                                                      Boolean searchArchived,
                                                      String yearDoss,
                                                      Pageable pageable);

    @Query(value = "SELECT (max(d.dossierNumber) + 1) " +
            " from TDossiers d " +
            " join d.dossierRightsList dr " +
            " join dr.lawfirmUsers vcu " +
            " where vcu.lawfirm.vckey = ?1")
    Long getMaxDossierByVckey(String vcKey);

    @Query(nativeQuery = true, value = "select tdossiers0_.id_doss as id, tdossiers0_.nomenclature as nomenclature, users.initiales, " +
            " dossierClient.partiesName as partiesName, " +
            " owner.vc_key as vckeyOwner," +
            " dossierrig1_.vc_owner as owner," +
            " tdossiers0_.date_close as closeDossier," +
            " tdossiers0_.doss_type as type," +
            " dossierrig1_.last_access_date as lastAccessDate" +
            " from t_dossiers tdossiers0_ " +
            " inner join t_dossier_rights dossierrig1_ on tdossiers0_.id_doss = dossierrig1_.dossier_id " +
            " left join (" +
            " select distinct tu2.vc_key, d.id_doss from t_dossiers d" +
            " inner join t_dossier_rights dr on d.id_doss = dr.dossier_id" +
            " inner join t_virtualcab_users tu2 on tu2.id = dr.VC_USER_ID" +
            " where  dr.VC_OWNER = 1" +
            " ) owner on owner.id_doss = tdossiers0_.id_doss " +
            " inner join t_users users on users.id = tdossiers0_.id_user_resp " +
            " inner join t_virtualcab_users tu on tu.id = dossierrig1_.VC_USER_ID " +
            " left join (" +
            "        select doss.DOSSIER_ID,  GROUP_CONCAT(fullname" +
            "                            SEPARATOR" +
            "                            ', ') partiesName" +
            "        from t_dossier_rights doss" +
            "                 left join t_dossier_contact as t_dossier_contact3" +
            "                           on t_dossier_contact3.dossier_id = doss.DOSSIER_ID and t_dossier_contact3.contact_type_id in (1,2,3,4,5)" +
            "           left join (" +
            "        select id_client," +
            "   (case when tclients4_.client_type=1 then concat(coalesce(tclients4_.f_nom, ''), ' ', coalesce(tclients4_.f_prenom, '')) else tclients4_.f_company end) as fullname " +
            "        from t_clients as tclients4_" +
            "        group by id_client" +
            "            ) tclients4_  on t_dossier_contact3.client_id = tclients4_.id_client" +
            "        where doss.VC_USER_ID = ?1" +
            "        group by doss.DOSSIER_ID" +
            ") dossierClient on dossierClient.DOSSIER_ID = dossierrig1_.DOSSIER_ID " +
            " where dossierrig1_.vc_user_id = ?1 " +
            "   and dossierrig1_.vc_owner in (?2) " +
            "   and (case when ?5 = 1 then tdossiers0_.is_digital = 0 else tdossiers0_.is_digital in (0,1) end) " +
            "   and ((" +
            " dossierClient.partiesName like CONCAT('%', ?3, '%') " +
            ") " +
            "  or tdossiers0_.nomenclature like COALESCE(CONCAT('%', ?4, '%'), '%')" +
            " ) " +
            "  and tdossiers0_.date_close is null  " +
            " order by dossierrig1_.last_access_date desc, tdossiers0_.nomenclature desc")
    List<IDossierDTO> findAffairesByVcUserId(Long vcUserId, List<Integer> vcOwner, String searchCriteriaClient, String nomenclature, Boolean digital);

    @Query(value = "SELECT count(d) from TDossiers d " +
            " join d.dossierContactList dc" +
            " join dc.clients cc" +
            " where dc.clients.id_client in :clientIdList")
    Long countByClient_cabAndOrClient_adv(List<Long> clientIdList);

    @Query(value = "SELECT d from LawfirmUsers l " +
            " join l.lawfirm lawfirm" +
            " join l.dossierRightsList dr" +
            " join dr.tDossiers d" +
            " where lawfirm.vckey = :vcKey" +
            " and dr.dossierId = :dossierId" +
            " ORDER BY d.year_doss, d.dossierNumber")
    List<TDossiers> findByVcKeyAndDossier(String vcKey, Long dossierId);

    @Query(nativeQuery = true, value = "select * " +
            " from t_dossiers" +
            " where year_doss = 2022" +
            " and substr(nomenclature, 1, 4) <> t_dossiers.year_doss" +
            " UNION ALL" +
            " select *" +
            " from t_dossiers" +
            " where year_doss = 2022" +
            " and substr(nomenclature, 1, 4) = t_dossiers.year_doss" +
            " and substr(nomenclature, 5, 1) <> '/'")
    List<TDossiers> findMigrate();

}
