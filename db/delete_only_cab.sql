drop procedure delete_only_cab;
create procedure delete_only_cab(IN cabname varchar(50))
begin
    DELETE
    FROM avogest.t_facture_frais_collaboration
    WHERE FACTURE_ID in (
        select id_facture
        FROM avogest.t_factures
        WHERE vc_key LIKE cabname
    );
    DELETE FROM avogest.t_factures WHERE vc_key LIKE cabname;
    delete from t_obj_shared_with where obj_id IN (SELECT ID from t_obj_shared where vc_key = cabname);
    delete from t_obj_shared where vc_key = cabname;
    delete
    from t_security_group_users
    where t_sec_groups_id IN (SELECT id from t_security_groups where vc_key like cabname);
    delete
    from t_security_group_rights
    where t_sec_groups_id in (select id from t_security_groups where vc_key like cabname);
    delete from t_security_groups where vc_key like cabname;
    delete from avogest.t_templates where vc_key like cabname;
delete
from avogest.t_timesheet_type
where vc_key = cabname;
delete
from avogest.t_debour_type
where vc_key = cabname;
delete
from avogest.ref_poste
where vc_key = cabname;


DELETE
FROM avogest.ref_compte
WHERE vc_key = cabname;

DELETE
FROM `avogest`.`t_dossier_contact`
WHERE dossier_id in (
    select DOSSIER_ID
    from `t_dossier_rights`
    WHERE VC_USER_ID in (
        select id
        FROM `avogest`.`t_virtualcab_users`
        WHERE vc_key like cabname
        )
    );
    DELETE
    FROM `avogest`.`t_dossier_contact`
    WHERE client_id in (
        select id_client
        from `t_clients`
        WHERE vc_key in (
            cabname
            )
    );
    DELETE
    FROM `avogest`.`t_dossiers`
    WHERE id_doss in (
        select DOSSIER_ID
        from `t_dossier_rights`
        WHERE VC_USER_ID in (
            select id
            FROM `avogest`.`t_virtualcab_users`
            WHERE vc_key like cabname
        )
    );
    DELETE
    FROM `avogest`.`t_dossier_rights`
    WHERE `VC_USER_ID` in (
        select id
        from t_virtualcab_users
        WHERE `vc_key` like cabname);
    DELETE FROM `avogest`.`t_frais` WHERE `vc_key` like cabname;
    DELETE FROM `avogest`.`t_virtualcab_client` WHERE `vc_key` like cabname;
    DELETE FROM `avogest`.`t_clients` WHERE `vc_key` like cabname;
    DELETE FROM `avogest`.`t_vc_groupment` WHERE `vc_key` like cabname;
    DELETE FROM `avogest`.`t_virtualcab_vat` WHERE `vc_key` like cabname;
    DELETE FROM `avogest`.`t_virtualcab_users` WHERE `vc_key` like cabname;
    DELETE FROM `avogest`.`t_virtualcab` WHERE `key` like cabname;
END;

