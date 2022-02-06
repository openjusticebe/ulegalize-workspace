use avogest;
drop table if exists t_vat_country;

create table `t_vat_country`
(
    `id`                bigint        not null auto_increment,
    `vat`               decimal(4, 2) not null,
    `id_country_alpha2` varchar(4)    not null,
    `is_default`        int(1)        not null,
    `upd_user`          VARCHAR(45)   NULL,
    `upd_date`          DATETIME      NULL,
    PRIMARY KEY (id)
);
INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (0, 'BE', 0, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (6, 'BE', 0, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (12, 'BE', 0, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (21, 'BE', 1, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (0, 'FR', 0, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (5.5, 'FR', 0, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (10, 'FR', 0, 'ulegalize', NOW());

INSERT INTO avogest.t_vat_country (vat, id_country_alpha2, is_default, upd_user, upd_date)
VALUES (20, 'FR', 1, 'ulegalize', NOW());



insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_010', 'vat per country', NOW());

commit;