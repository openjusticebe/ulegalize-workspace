use avogest;
drop table if exists t_virtualcab_client;

create table `t_virtualcab_client`
(
    `id`        bigint(20)  not null auto_increment,
    `vc_key`    varchar(50) not null,
    `client_id` bigint(20)  not null,
    `cre_user`  VARCHAR(45) not NULL,
    `cre_date`  DATETIME    not NULL default CURRENT_TIMESTAMP,
    `upd_user`  VARCHAR(45) NULL,
    `upd_date`  DATETIME    NULL,
    PRIMARY KEY (id)
);

update t_clients
set vc_key = 'AVOTEST'
WHERE user_id IN (96, 203);
update t_clients
set vc_key = 'FINAUXA'
WHERE user_id IN (48);
update t_clients
set vc_key = 'ULEGAL53'
WHERE user_id IN (232);

commit;

insert into t_virtualcab_client (vc_key, client_id, cre_user, cre_date)
SELECT vc_key, id_client, user_upd, date_upd
FROM t_clients
where vc_key is not null;

create index t_virtualcab_client_vc_key_client_id_index
    on t_virtualcab_client (vc_key, client_id);


insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_011', 'create client per cab ', NOW());

commit;
