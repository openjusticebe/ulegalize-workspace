use avogest;

create table `t_sequences`
(
    `id`              bigint      not null auto_increment,
    `sequence_number` bigint      null,
    `sequence_type`   varchar(12) null,
    `upd_user`        VARCHAR(45) NULL,
    `upd_date`        DATETIME    NULL,
    PRIMARY KEY (id)
);

insert into t_sequences (sequence_number, sequence_type, upd_user, upd_date)
values (45, 'TEMP_VC', 'ulegal', current_date);

commit;

ALTER TABLE `t_virtualcab_users`
    ADD COLUMN `is_selected` INT(1) NULL DEFAULT 0 AFTER `id_user`;

commit;


ALTER TABLE `t_virtualcab`
    ADD COLUMN `temporary_vc` INT(1) NULL DEFAULT 1 AFTER `pass_admin`;

update `t_virtualcab`
set temporary_vc = 0;

commit;

ALTER TABLE `avogest`.`t_frais`
    CHANGE COLUMN `date_value` `date_value` DATE     NULL,
    CHANGE COLUMN `date_upd` `date_upd`     DATETIME NULL;


update t_frais
set date_value = null
where CAST(date_value AS CHAR(10)) = '0000-00-00';

update t_frais
set date_upd = null
where CAST(date_upd AS CHAR(20)) = '0000-00-00 00:00:00';

commit;


-- already done
-- selected the min virtualcab
CREATE TEMPORARY TABLE `someTemp`
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
drop TEMPORARY TABLE `someTemp`;



insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_0058', 'Ulegalize 11.0.0 - add selected by default', NOW());

commit;