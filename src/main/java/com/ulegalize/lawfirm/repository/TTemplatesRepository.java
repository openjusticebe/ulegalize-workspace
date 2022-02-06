package com.ulegalize.lawfirm.repository;

import com.ulegalize.lawfirm.model.entity.TTemplates;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TTemplatesRepository extends JpaRepository<TTemplates, Long>, JpaSpecificationExecutor<TTemplates> {
    void deleteByVcKey(String vcKey);

    List<TTemplates> findAllByVcKey(String vcKey);

    List<TTemplates> findAllByVcKeyOrderByTypeDescNameAsc(String vcKey);

    Optional<TTemplates> findByIdAndVcKey(Long templateId, String vcKey);

    @Query(nativeQuery = true, value = "select tu.fullname as _Avocat_Nom, tv.numentreprise as _Cabinet_Tva, " +
            "ta.title as _Client_Titre, a.f_nom as _Client_Nom, a.f_prenom as _Client_Prenom," +
            " a.f_company as _Client_NomEntreprise, a.f_tva as _Client_NumTva, a.f_email as _Client_Email, " +
            "    CONCAT_WS(' ', a.f_num, a.f_rue, a.f_cp, a.f_ville) as _Client_Adresse," +
            "    CONCAT_WS(' ', tv.street, tv.cp, tv.city) as _Cabinet_Adresse," +
            "    DATE_FORMAT(CURDATE(),'%d/%m/%Y') AS _Date, " +
//            "    if(a.id_title='M','Cher,','Chère') as _Client_FormuleAppel, tc.title as _PartieAdv_Titre, " +
            " c.f_nom as _PartieAdv_Nom, c.f_prenom as _PartieAdv_Prenom," +
            " c.f_company as _PartieAdv_NomEntreprise, a.f_tva as _PartieAdv_NumTva, a.f_email _PartieAdv_Email " +
//            "    if(c.id_title='M','Cher,','Chère') as _PartieAdv_FormuleAppel, lpad(b.num_doss,4,'0') as _Dossier_Numero, b.year_doss as _Dossier_Annee " +
            "    from t_dossiers b " +
            " inner join t_dossier_rights dr on b.id_doss = dr.dossier_id " +
            " inner join t_virtualcab_users vcu on vcu.id = dr.VC_USER_ID " +
            " inner join t_dossier_contact as t_dossier_contact on t_dossier_contact.dossier_id = b.id_doss and t_dossier_contact.contact_type_id = 1 " +
            " inner join t_clients as a on t_dossier_contact.client_id = a.id_client " +
            " inner join t_title as ta on ta.id_title=a.id_title " +
            " left join t_dossier_contact as t_dossier_contact2 on t_dossier_contact2.dossier_id = b.id_doss and t_dossier_contact2.contact_type_id = 2" +
            " left join t_clients as c on t_dossier_contact2.client_id = c.id_client" +
            " inner join t_title as tc on tc.id_title=c.id_title " +
            " inner join t_users tu on tu.id = b.id_user_resp " +
            " inner join t_virtualcab tv on tv.key=vcu.vc_key " +
            " WHERE b.id_doss = ?1 " +
            " AND vcu.vc_key= ?2  " +
            " AND vcu.id_user= ?3 " +
            " LIMIT 1 ")
    JSONObject getTemplatDataByDossier(Long dossierId, String vcKey, Long userId);

    @Query(nativeQuery = true, value = "select tu.fullname as _Avocat_Nom, tv.numentreprise as _Cabinet_Tva, " +
            "ta.title as _Client_Titre, a.f_nom as _Client_Nom, a.f_prenom as _Client_Prenom," +
            " a.f_company as _Client_NomEntreprise, a.f_tva as _Client_NumTva, a.f_email as _Client_Email, " +
            "    CONCAT_WS(' ', a.f_num, a.f_rue, a.f_cp, a.f_ville) as _Client_Adresse," +
            "    CONCAT_WS(' ', tv.street, tv.cp, tv.city) as _Cabinet_Adresse," +
            "    DATE_FORMAT(CURDATE(),'%d/%m/%Y') AS _Date, " +
            "    if(a.id_title='M','Cher,','Chère') as _Client_FormuleAppel, tc.title as _PartieAdv_Titre, " +
            " c.f_nom as _PartieAdv_Nom, c.f_prenom as _PartieAdv_Prenom," +
            " c.f_company as _PartieAdv_NomEntreprise, a.f_tva as _PartieAdv_NumTva, a.f_email _PartieAdv_Email, " +
            "    if(c.id_title='M','Cher,','Chère') as _PartieAdv_FormuleAppel, lpad(b.num_doss,4,'0') as _Dossier_Numero, b.year_doss as _Dossier_Annee " +
            "    from t_dossiers b" +
            " inner join t_dossier_rights dr on b.id_doss = dr.dossier_id " +
            " inner join t_virtualcab_users vcu on vcu.id = dr.VC_USER_ID " +
            " inner join t_dossier_contact as t_dossier_contact on t_dossier_contact.dossier_id = b.id_doss and t_dossier_contact.contact_type_id = 1 " +
            " inner join t_clients as a on t_dossier_contact.client_id = a.id_client " +
            " inner join t_title as ta on ta.id_title=a.id_title " +
            " left join t_dossier_contact as t_dossier_contact2 on t_dossier_contact2.dossier_id = b.id_doss and t_dossier_contact2.contact_type_id = 2" +
            " left join t_clients as c on t_dossier_contact2.client_id = c.id_client" +
            " inner join t_title as tc on tc.id_title=c.id_title " +
            " inner join t_users tu on tu.id = b.id_user_resp " +
            " inner join t_virtualcab tv on tv.key=vcu.vc_key " +
            " WHERE vcu.vc_key= ?1  " +
            " AND vcu.id_user= ?2 " +
            " LIMIT 1 ")
    JSONObject getTemplatData(String vcKey, Long userId);
}