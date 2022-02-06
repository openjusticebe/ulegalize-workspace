use
avogest;
drop table if exists t_virtualcab_client;

create table `t_virtualcab_client`
(
    `id`        bigint(20) not null auto_increment,
    `vc_key`    varchar(50) not null,
    `client_id` bigint(20) not null,
    `cre_user`  VARCHAR(45) not NULL,
    `cre_date`  DATETIME    not NULL default CURRENT_TIMESTAMP,
    `upd_user`  VARCHAR(45) NULL,
    `upd_date`  DATETIME NULL,
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

insert into t_virtualcab_client (vc_key, client_id, cre_user, cre_date)
select cl.*
from (
         SELECT distinct dossier.vckey, t_clients.id_client, t_clients.user_upd, t_clients.date_upd
         from (
                  select dr.DOSSIER_ID, tv.`key` vckey
                  from t_dossier_rights dr
                           join t_virtualcab_users tvu on tvu.ID = dr.VC_USER_ID
                           join t_virtualcab tv on tvu.vc_key = tv.`key`
                  where dr.VC_OWNER in (0, 2)
                  group by dr.DOSSIER_ID, tv.`key`
              ) dossier
                  join t_dossiers d on d.id_doss = dossier.DOSSIER_ID
                  join t_clients on t_clients.id_client = d.client_cab
         where dossier.vckey <> t_clients.vc_key
     ) cl;
create unique index t_virtualcab_client_vc_key_client_id_uindex
    on t_virtualcab_client (vc_key, client_id);

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_012', 'create client per cab bis ', NOW());

commit;
