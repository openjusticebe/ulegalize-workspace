use avogest;

ALTER TABLE `t_users`
    ADD COLUMN `client_from` varchar(25) NULL DEFAULT 'Ulegalize Workspace';

commit;

update t_users
set client_from = 'Ulegalize Workspace'
where 1 = 1;


insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_007', 'Add client_from', NOW());

commit;