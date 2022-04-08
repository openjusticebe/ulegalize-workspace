use
avogest;

ALTER TABLE `t_calendar_event`
    ADD `url_room` VARCHAR(1200) NULL AFTER room_name;

alter table t_calendar_event
drop
column room_name;

ALTER TABLE t_dossier_contact DROP FOREIGN KEY t_dossier_contact_ibfk_1;
#
ALTER TABLE t_dossier_contact DROP FOREIGN KEY t_dossier_contact_ibfk_2;
#
# ALTER TABLE t_dossier_contact DROP INDEX t_dossier_contact_ibfk_2;
ALTER TABLE t_dossier_contact DROP FOREIGN KEY t_dossier_contact_ibfk_3;
#
# ALTER TABLE t_dossier_contact DROP INDEX t_dossier_contact_ibfk_3;
DROP INDEX dossier_id ON t_dossier_contact;
ALTER TABLE t_dossier_contact modify column dossier_id bigint(20) NOT NULL;
DROP INDEX ID_FAC_TS_UNIQUE ON t_facture_timesheet;
ALTER TABLE t_facture_timesheet modify column FACTURE_ID bigint(20) NOT NULL;
ALTER TABLE t_facture_timesheet modify column TS_ID bigint(20) NOT NULL;
CREATE INDEX dossier_id ON t_dossier_contact (dossier_id);
CREATE UNIQUE INDEX ID_FAC_TS_UNIQUE ON t_facture_timesheet (FACTURE_ID, TS_ID);
ALTER TABLE t_dossier_contact
    ADD CONSTRAINT t_dossier_contact_ibfk_1 FOREIGN KEY (dossier_id) REFERENCES t_dossiers (id_doss) ON UPDATE No action ON DELETE No action;
ALTER TABLE t_dossier_contact
    ADD CONSTRAINT t_dossier_contact_ibfk_2 FOREIGN KEY (client_id) REFERENCES t_clients (id_client) ON UPDATE No action ON DELETE No action;
ALTER TABLE t_dossier_contact
    ADD CONSTRAINT t_dossier_contact_ibfk_3 FOREIGN KEY (contact_type_id) REFERENCES t_dossier_contact_type (id) ON UPDATE No action ON DELETE No action;
ALTER TABLE t_facture_timesheet
    ADD CONSTRAINT FKt_facture_510030 FOREIGN KEY (TS_ID) REFERENCES t_timesheet (id_ts);
ALTER TABLE t_facture_timesheet
    ADD CONSTRAINT FKt_facture_123735 FOREIGN KEY (FACTURE_ID) REFERENCES t_factures (id_facture);

ALTER TABLE t_timesheet
    ADD CONSTRAINT FKt_timeshee927767 FOREIGN KEY (ts_type) REFERENCES t_timesheet_type (id_ts);

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_021', 'url room event', NOW());

commit;

# for test env
# delete from t_timesheet where id_ts in (select col1 from temp);
#
# insert into temp(col1)
#     select t_timesheet.id_ts
#     from t_timesheet
#              left join t_timesheet_type tft on t_timesheet.ts_type = tft.id_ts
#     where tft.id_ts is null

select *
from t_facture_timesheet
         left join t_timesheet tt on tt.id_ts = t_facture_timesheet.TS_ID
where tt.id_ts is null
;