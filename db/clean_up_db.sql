drop procedure clean_up_db;
create procedure clean_up_db()
begin
    DECLARE finished INTEGER DEFAULT 0;

    DECLARE vcKey varchar(50);
    DECLARE fd_id int(11);
    DECLARE cursor_name CURSOR FOR select `key`
                                   from avogest.t_virtualcab
                                   where `key` not in ('AVOTEST',
                                                       'FINAUXA',
                                                       'ODEKERKEN',
                                                       'MOBILITY',
                                                       'SEDAEMS',
                                                       'SEVERINE'
                                       );

    DECLARE cursor_name_1 CURSOR FOR
select fd.id as fd_id
from avogest.t_facture_details fd
         left join avogest.t_factures tf on fd.id_facture = tf.id_facture
where tf.id_facture is null;


-- declare NOT FOUND handler
DECLARE
CONTINUE HANDLER
        FOR NOT FOUND SET finished = 1;

truncate table back_ref_poste;

truncate table hist_ref_compte;

truncate table hist_ref_poste;

truncate table hist_t_clients;

truncate table hist_t_debour;

truncate table hist_t_debour_type;

truncate table hist_t_dossiers;

truncate table hist_t_echeancier;

truncate table hist_t_frais;

truncate table hist_t_timesheet;

truncate table hist_t_timesheet_type;
truncate table ref_compte_delegate;
truncate table t_calendar;
truncate table t_calendar_doss_subscribers;
truncate table t_calendar_virtuel_cab;
truncate table t_calendar_vc_subscribers;
#
truncate table t_calendar_participants;
#
delete
from t_calendar_event;
truncate table t_cloud_files;
truncate table t_delegate;
#
truncate table t_dossier_contact;
truncate table t_dossier_gestionnaires;

#
truncate table t_dossier_rights;
#
truncate table t_dossier_contact;
commit;
#
delete
from t_dossiers;
truncate table t_echeancier;
#
truncate table t_facture_details;
#
truncate table t_facture_echeance;
#
truncate table t_facture_frais_admin;
#
truncate table t_facture_frais_collaboration;
#
truncate table t_facture_frais_debours;
#
truncate table t_facture_timesheet;
#
delete
from t_debour;
#
delete
from t_factures;
truncate table t_first_time;
#
delete
from t_frais;
truncate table t_jobs;
truncate table t_message_user;
delete
from t_obj_shared;
truncate table t_obj_shared_with;
#
delete
from t_clients;
truncate table t_procedures;
truncate table t_procedures_type;
truncate table t_schema_version;
#
truncate table t_security_group_rights;
#
truncate table t_security_group_users;
#
delete
from t_security_groups;
truncate table t_stripe_subscribers;
truncate table t_templates;
#
truncate table t_timesheet;
#
truncate table t_timesheet_type;
truncate table t_user_preference;
#
truncate table t_vc_groupment;
#
truncate table t_virtualcab_client;
#
truncate table t_virtualcab_config;
#
truncate table t_virtualcab_vat;
#
truncate table t_virtualcab_website;
#
delete
from t_virtualcab_users;
#
commit ;
#
delete
from t_virtualcab;
#
truncate table t_users_roles;
#
delete
from t_users;



OPEN cursor_name;
getEmail
:
    LOOP

        FETCH cursor_name INTO vcKey;
        IF
finished = 1 THEN
            LEAVE getEmail;
END IF;

        DELETE
        FROM avogest.t_facture_details
        WHERE id_facture IN (SELECT id_facture FROM avogest.t_factures WHERE vc_key like vcKey);
        DELETE
        FROM avogest.t_facture_frais_collaboration
        WHERE FACTURE_ID in (
            select id_facture
            FROM avogest.t_factures
            WHERE vc_key LIKE vcKey
        );
        DELETE
        FROM avogest.t_facture_frais_debours
        WHERE FACTURE_ID in (
            select id_facture
            FROM avogest.t_factures
            WHERE vc_key LIKE vcKey
        );
        DELETE
        FROM avogest.t_facture_frais_admin
        WHERE FACTURE_ID in (
            select id_facture
            FROM avogest.t_factures
            WHERE vc_key LIKE vcKey
        );
        DELETE FROM avogest.t_factures WHERE vc_key like vcKey;

        delete
        from avogest.t_timesheet
        where id_doss IN (
            select id_doss
            from `avogest`.`t_dossiers`
            WHERE id_doss in (
                select DOSSIER_ID
                from `t_dossier_rights`
                WHERE VC_USER_ID in (
                    select id
                    FROM `avogest`.`t_virtualcab_users`
                    WHERE vc_key like vcKey
                )
            )
        );
        DELETE FROM avogest.t_frais WHERE vc_key like vcKey;

        delete
        from t_calendar_participants
        where CALENDAR_ID in (
            select id
            from `t_calendar_event`
            WHERE dossier_id in (
                select DOSSIER_ID
                from `t_dossier_rights`
                WHERE VC_USER_ID in (
                    select id
                    FROM `avogest`.`t_virtualcab_users`
                    WHERE vc_key like vcKey
                )
            )
        );
        delete
        from t_calendar_event
        where dossier_id in (
            select DOSSIER_ID
            from `t_dossier_rights`
            WHERE VC_USER_ID in (
                select id
                FROM `avogest`.`t_virtualcab_users`
                WHERE vc_key like vcKey
            )
        );

        COMMIT;

        call delete_only_cab(vcKey);


    END LOOP getEmail;
    CLOSE cursor_name;

    OPEN cursor_name_1;
    cursor_name_fact:
    LOOP

        FETCH cursor_name_1 INTO fd_id;
        IF finished = 1 THEN
            LEAVE cursor_name_fact;
        END IF;

        DELETE
        FROM avogest.t_facture_details
        WHERE id = fd_id;


        COMMIT;

    END LOOP cursor_name_fact;
    CLOSE cursor_name_1;


END;

