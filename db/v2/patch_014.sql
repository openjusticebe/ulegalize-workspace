use
avogest;

alter table t_dossiers_type
    modify type_desc varchar (20) default '' not null;


UPDATE t_dossiers_type t
SET t.type_desc = 'Droit collaboratif'
WHERE t.doss_type LIKE 'DF' ESCAPE '#';

INSERT INTO t_dossiers_type (doss_type, type_desc)
VALUES ('MD', 'Mediation');

drop table if exists t_dossier_contact;
drop table if exists t_dossier_contact_type;

create table `t_dossier_contact`
(
    `id`              bigint(20) not null auto_increment,
    `dossier_id`      bigint(2) not null,
    `client_id`       bigint(20) not null,
    `contact_type_id` int(2) not null,
    `cre_user`        VARCHAR(45) not NULL,
    `cre_date`        DATETIME    not NULL default CURRENT_TIMESTAMP,
    `upd_user`        VARCHAR(45) NULL,
    `upd_date`        DATETIME NULL,
    PRIMARY KEY (id)
);
create table `t_dossier_contact_type`
(
    `id`    INT(2) not null,
    `label` varchar(20) not null,
    PRIMARY KEY (id),
    UNIQUE INDEX `ID_DOSS_CON_UNIQUE` (`label`)

);

insert into t_dossier_contact_type(id, label) value (1, 'CLIENT');
insert into t_dossier_contact_type(id, label) value (2, 'OPPOSING');
insert into t_dossier_contact_type(id, label) value (3, 'PARTY');

ALTER TABLE t_dossier_contact
    ADD FOREIGN KEY (dossier_id) REFERENCES t_dossiers (id_doss);
commit;

ALTER TABLE t_dossier_contact
    ADD FOREIGN KEY (client_id) REFERENCES t_clients (id_client);
commit;

insert into t_dossier_contact (dossier_id, client_id, contact_type_id, cre_user, cre_date)
SELECT d.id_doss, client_cab, 1, user_upd, date_upd
FROM t_dossiers d;

insert into t_dossier_contact (dossier_id, client_id, contact_type_id, cre_user, cre_date)
SELECT d.id_doss, client_adv, 2, user_upd, date_upd
FROM t_dossiers d;

ALTER TABLE t_dossier_contact
    ADD FOREIGN KEY (contact_type_id) REFERENCES t_dossier_contact_type (id);
commit;

alter table t_dossiers modify client_cab bigint null;

alter table t_dossiers modify client_adv bigint null;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_014', 'dossier type ', NOW());

commit;
