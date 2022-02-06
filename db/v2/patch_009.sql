use
    avogest;

ALTER TABLE `t_clients`
    ADD COLUMN `iban` varchar(34) NULL;
ALTER TABLE `t_clients`
    ADD COLUMN `bic` varchar(8) NULL;

commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_009', 'Add iban and bic to client', NOW());

commit;