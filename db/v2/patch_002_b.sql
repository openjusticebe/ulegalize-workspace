use
    avogest;

ALTER TABLE `avogest`.`t_security_groups`
    CHANGE COLUMN `date_upd` `date_upd` DATETIME NULL;

update t_security_groups
set date_upd = null
where CAST(date_upd AS CHAR(20)) = '0000-00-00 00:00:00';


ALTER TABLE `avogest`.`t_timesheet_type`
    CHANGE COLUMN `date_upd` `date_upd` DATETIME NULL;


update t_timesheet_type
set date_upd = null
where CAST(date_upd AS CHAR(20)) = '0000-00-00 00:00:00';


insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_0057b', 'before Ulegalize 11.0.0 ', NOW());

commit;