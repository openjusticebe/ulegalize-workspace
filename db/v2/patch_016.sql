use avogest;

ALTER TABLE `t_dossier_rights`
    ADD `LAST_ACCESS_DATE` DATETIME NOT NULL;

update t_dossier_rights
set LAST_ACCESS_DATE = now();

create index idx_dossierri_last_access_date on t_dossier_rights (LAST_ACCESS_DATE);

commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_016', 'last dossier access', NOW());

commit;
