use avogest;

ALTER TABLE `t_users`
    ADD COLUMN `is_notification` TINYINT(1) NULL DEFAULT '1';

commit;

ALTER TABLE `t_virtualcab`
    ADD COLUMN `is_notification` TINYINT(1) NULL DEFAULT '1';

commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_006', 'Ulegalize 2.5.0 - add is_notification', NOW());

commit;