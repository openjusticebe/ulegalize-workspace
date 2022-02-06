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
    DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET finished = 1;

    OPEN cursor_name;
    getEmail:
    LOOP

        FETCH cursor_name INTO vcKey;
        IF finished = 1 THEN
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

