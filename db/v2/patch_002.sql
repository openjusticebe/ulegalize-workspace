use
avogest;
-- selected the min virtualcab
CREATE
TEMPORARY TABLE `someTemp`
(
    someVal bigint(20)
);
INSERT INTO `someTemp` (someVal)
select min(id)
from t_virtualcab_users
group by id_user;

update t_virtualcab_users
set is_selected = 1
where id in (
    select someVal
    from someTemp
);
commit;
drop
TEMPORARY TABLE `someTemp`;

alter table t_frais modify id_client bigint null;
alter table t_frais modify id_doss bigint null;
alter table t_frais modify id_facture bigint null;

update t_frais
set id_client = null
where id_client = 0;
update t_frais
set id_doss = null
where id_doss = 0;
update t_frais
set id_facture = null
where id_facture = 0;

commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_0058', 'Ulegalize 11.0.0 - add selected by default', NOW());

commit;