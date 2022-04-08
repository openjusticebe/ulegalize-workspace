use
avogest;

alter table t_facture_timesheet
    modify ID BIGINT auto_increment;



insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_020', 'change type column', NOW());

commit;
