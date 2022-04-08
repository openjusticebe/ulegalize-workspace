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

    @Query(value = "SELECT d from TDossiers d join d.dossierRightsList dr where dr.lawfirmUsers.lawfirm.vckey = ?1 order by d.year_doss desc, d.num_doss desc")
    List<TDossiers> findAllByVCKey(String vcKey);

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
            " order by d.year_doss desc, d.num_doss desc")
    List<TDossiers> findAllByVCKeyAndUserId(String vcKey, Long userId, List<EnumVCOwner> vcOwner);

    @Query(value = "SELECT d from TDossiers d " +
            " join fetch d.dossierRightsList dr " +
            " join d.dossierContactList dc" +
            " where d.idDoss = ?1 and dr.vcUserId = ?2 and dr.vcOwner in (?3)")
    Optional<TDossiers> findByIdDoss(Long id_doss, Long vcUserId, List<EnumVCOwner> enumVCOwnerList);

    @Query(value = "SELECT d from TDossiers d join d.dossierRightsList dr where d.idDoss = ?1 and dr.lawfirmUsers.lawfirm.vckey = ?2")
    Optional<TDossiers> findByIdDossAndVcKey(Long id_doss, String vcKey);

    @Query(value = "SELECT d from TDossiers d join d.dossierRightsList dr where d.idDoss = ?1 and dr.vcUserId = ?2")
    Optional<TDossiers> findAuthorizedByIdDoss(Long isDossier, Long vcUserId);

    @Query(nativeQuery = true, value = "select tdossiers0_.id_doss as id, tdossiers0_.year_doss as year, tdossiers0_.num_doss as num, users.initiales, " +
            " GROUP_CONCAT( concat(coalesce(tclients4_.f_nom,''), ' ' , coalesce(tclients4_.f_prenom,'')) SEPARATOR ',' ) as partiesName, " +
            " tclients2_.f_prenom as firstnameClient, tclients2_.f_nom as lastnameClient, tclients2_.f_company as companyClient, tclients2_.id_client as idClient, " +
            " tclients3_.f_prenom as adverseFirstnameClient, tclients3_.f_nom as adverseLastnameClient, tclients3_.f_company as adverseCompanyClient, tclients3_.id_client as adverseIdClient, " +
            " round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2) balance," +
            " owner.vc_key as vckeyOwner," +
            " tdossiers0_.date_close as closeDossier," +
            " tdossiers0_.date_open as openDossier," +
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
            " left join t_dossier_contact as t_dossier_contact on t_dossier_contact.dossier_id = tdossiers0_.id_doss and t_dossier_contact.contact_type_id = 1 " +
            " left join t_clients as tclients2_ on t_dossier_contact.client_id = tclients2_.id_client " +
            " left join t_dossier_contact as t_dossier_contact2 on t_dossier_contact2.dossier_id = tdossiers0_.id_doss and t_dossier_contact2.contact_type_id = 2" +
            " left join t_clients as tclients3_ on t_dossier_contact2.client_id = tclients3_.id_client" +
            " left join t_dossier_contact as t_dossier_contact3 on t_dossier_contact3.dossier_id = tdossiers0_.id_doss and t_dossier_contact3.contact_type_id = 3" +
            " left join t_clients as tclients4_ on t_dossier_contact3.client_id = tclients4_.id_client" +
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
            " where dossierrig1_.vc_user_id = ?1 " +
            "   and dossierrig1_.vc_owner in (?2) " +
            "   and (" +
            " tclients2_.f_prenom like CONCAT('%', ?3, '%') or tclients2_.f_nom like CONCAT('%', ?3, '%') " +
            " or tclients3_.f_prenom like CONCAT('%', ?3, '%') or tclients3_.f_nom like CONCAT('%', ?3, '%') " +
            " or tclients4_.f_prenom like CONCAT('%', ?3, '%') or tclients4_.f_nom like CONCAT('%', ?3, '%') " +
            ") " +
            "   and (tdossiers0_.year_doss like COALESCE(CONCAT('%', ?4, '%'), '%') and tdossiers0_.num_doss like COALESCE(CONCAT(?5, '%'), '%')) " +
            " and users.initiales like CONCAT('%', ?6, '%')" +
            "  and (case when ?7 = 1 then round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2) <> 0 " +
            " when ?7 = 0 then round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2) =0 else 1=1 end)" +
            "  and (case when ?8 = 0 then tdossiers0_.date_close is null " +
            " when ?8 = 1 then tdossiers0_.date_close is not null else 1=1 end) " +
            " group by tdossiers0_.id_doss   ," +
            "       tdossiers0_.year_doss  ," +
            "       tdossiers0_.num_doss  " +
            " ",
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
                    " inner join t_dossier_contact as t_dossier_contact on t_dossier_contact.dossier_id = tdossiers0_.id_doss and t_dossier_contact.contact_type_id = 1" +
                    " inner join t_clients as tclients2_ on t_dossier_contact.client_id = tclients2_.id_client" +
                    " left join t_dossier_contact as t_dossier_contact2 on t_dossier_contact2.dossier_id = tdossiers0_.id_doss and t_dossier_contact2.contact_type_id = 2" +
                    " left join t_clients as tclients3_ on t_dossier_contact2.client_id = tclients3_.id_client" +
                    " left join t_dossier_contact as t_dossier_contact3 on t_dossier_contact3.dossier_id = tdossiers0_.id_doss and t_dossier_contact3.contact_type_id = 3" +
                    " left join t_clients as tclients4_ on t_dossier_contact3.client_id = tclients4_.id_client" +
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
                    " where dossierrig1_.vc_user_id = ?1 " +
                    "   and dossierrig1_.vc_owner in (?2) " +
                    "   and (" +
                    " tclients2_.f_prenom like CONCAT('%', ?3, '%') or tclients2_.f_nom like CONCAT('%', ?3, '%') " +
                    " or tclients3_.f_prenom like CONCAT('%', ?3, '%') or tclients3_.f_nom like CONCAT('%', ?3, '%') " +
                    " or tclients4_.f_prenom like CONCAT('%', ?3, '%') or tclients4_.f_nom like CONCAT('%', ?3, '%') " +
                    ") " +
                    "   and (tdossiers0_.year_doss like COALESCE(CONCAT('%', ?4, '%'), '%') and tdossiers0_.num_doss like COALESCE(CONCAT(?5, '%'), '%')) " +
                    " and users.initiales like CONCAT('%', ?6, '%')" +
                    "  and (case when ?7 = 1 then round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2) <> 0 " +
                    " when ?7 = 0 then round(ifnull(presta.cout,0), 2)+ifnull(fraisadmin.cout,0)+ ifnull(fraisProcedure.cout,0)+ ifnull(fraisCollaboration.cout,0) - round(ifnull(t_factures.cout,0), 2) =0 else 1=1 end)" +
                    "  and (case when ?8 = 0 then tdossiers0_.date_close is null " +
                    " when ?8 = 1 then tdossiers0_.date_close is not null else 1=1 end)" +
                    " group by tdossiers0_.id_doss")
    Page<IDossierDTO> findByVcUserIdAllWithPagination(Long vcUserId, List<Integer> vcOwner,
                                                      String searchCriteriaClient, String year, Long number, String initiales,
                                                      Boolean withBalance,
                                                      Boolean searchArchived,
                                                      Pageable pageable);

    @Query(value = "SELECT COALESCE(max(d.num_doss) , 0) +1 " +
            " from TDossiers d " +
            " join d.dossierRightsList dr " +
            " join dr.lawfirmUsers vcu " +
            " where vcu.lawfirm.vckey = ?1" +
            " and d.year_doss = ?2")
    Long getMaxDossierByVckeyAndYear(String vcKey, String year);

    @Query(nativeQuery = true, value = "select tdossiers0_.id_doss as id, tdossiers0_.year_doss as year, tdossiers0_.num_doss as num, users.initiales, " +
            " GROUP_CONCAT( concat(coalesce(tclients4_.f_nom,''), ' ' , coalesce(tclients4_.f_prenom,'')) SEPARATOR ',' ) as partiesName, " +
            " tclients2_.f_prenom as firstnameClient, tclients2_.f_nom as lastnameClient, tclients2_.f_company as companyClient, tclients2_.id_client as idClient, " +
            " tclients3_.f_prenom as adverseFirstnameClient, tclients3_.f_nom as adverseLastnameClient, tclients3_.f_company as adverseCompanyClient, tclients3_.id_client as adverseIdClient, " +
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
            " left join t_dossier_contact as t_dossier_contact on t_dossier_contact.dossier_id = tdossiers0_.id_doss and t_dossier_contact.contact_type_id = 1 " +
            " left join t_clients as tclients2_ on t_dossier_contact.client_id = tclients2_.id_client " +
            " left join t_dossier_contact as t_dossier_contact2 on t_dossier_contact2.dossier_id = tdossiers0_.id_doss and t_dossier_contact2.contact_type_id = 2" +
            " left join t_clients as tclients3_ on t_dossier_contact2.client_id = tclients3_.id_client" +
            " left join t_dossier_contact as t_dossier_contact3 on t_dossier_contact3.dossier_id = tdossiers0_.id_doss and t_dossier_contact3.contact_type_id = 3" +
            " left join t_clients as tclients4_ on t_dossier_contact3.client_id = tclients4_.id_client" +
            " where dossierrig1_.vc_user_id = ?1 " +
            "   and dossierrig1_.vc_owner in (?2) " +
            "   and (" +
            " tclients2_.f_prenom like CONCAT('%', ?3, '%') or tclients2_.f_nom like CONCAT('%', ?3, '%') " +
            " or tclients3_.f_prenom like CONCAT('%', ?3, '%') or tclients3_.f_nom like CONCAT('%', ?3, '%') " +
            " or tclients4_.f_prenom like CONCAT('%', ?3, '%') or tclients4_.f_nom like CONCAT('%', ?3, '%') " +
            ") " +
            "   and (case when ?6 = 0 then tdossiers0_.year_doss like COALESCE(CONCAT('%', ?4, '%'), '%') or tdossiers0_.num_doss like COALESCE(CONCAT(?5, '%'), '%') " +
            "    when ?6 = 1 then tdossiers0_.year_doss like COALESCE(CONCAT('%', ?4, '%'), '%') and tdossiers0_.num_doss like COALESCE(CONCAT(?5, '%'), '%') end)  " +
            "  and tdossiers0_.date_close is null  " +
            " group by tdossiers0_.id_doss   ," +
            "       tdossiers0_.year_doss  ," +
            "       tdossiers0_.num_doss   " +
            " order by dossierrig1_.last_access_date desc, tdossiers0_.year_doss desc, tdossiers0_.num_doss desc")
    List<IDossierDTO> findAffairesByVcUserId(Long vcUserId, List<Integer> vcOwner, String searchCriteriaClient, String year, Long number, boolean bothYearAndNumber);

    @Query(value = "SELECT count(d) from TDossiers d " +
            " join d.dossierContactList dc" +
            " join dc.clients cc" +
            " where cc.id_client = :clientId" +
            " or cc.id_client = :clientAdvId" +
            " or d.opposingCounsel.id_client = :clientOpposing")
    Long countByClient_cabAndOrClient_adv(Long clientId, Long clientAdvId, Long clientOpposing);

    @Query(value = "SELECT d from LawfirmUsers l " +
            " join l.lawfirm lawfirm" +
            " join l.dossierRightsList dr" +
            " join dr.tDossiers d" +
            " where lawfirm.vckey = :vcKey" +
            " and dr.dossierId = :dossierId" +
            " ORDER BY d.year_doss, d.num_doss")
    List<TDossiers> findByVcKeyAndDossier(String vcKey, Long dossierId);
}
