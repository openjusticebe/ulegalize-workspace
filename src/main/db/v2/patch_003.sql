use
avogest;
-- selected the min virtualcab
CREATE
TEMPORARY TABLE `someTemp`
(
    someVal bigint(20),
    someDate datetime
);
INSERT INTO `someTemp` (someVal, someDate)
SELECT vu.id, max(u.login_date)
FROM avogest.t_virtualcab_users vu
         inner join t_users u on u.id = vu.id_user
group by vu.id_user;

update t_virtualcab_users
set is_selected = 1
where id in (
    select someVal
    from someTemp
);
commit;
drop
TEMPORARY TABLE `someTemp`;

insert into avogest.t_templates (vc_key, type, format, context, subcontext, name, template, title, description,
                                 is_archived, user_upd, date_upd)
SELECT vc_key,
       type,
       format,
       context,
       concat(subcontext, '-N'),
       name,
       template,
       title,
       concat(description, ' NV'),
       is_archived,
       user_upd,
       date_upd
FROM avogest.t_templates
where type = 'S'
  and context = 'FACTURE';

commit;

update avogest.t_templates
set template = replace(template, 'é', '&eacute;');

commit;
update avogest.t_templates
set template = replace(template, 'ê', '&ecirc;');
commit;
update avogest.t_templates
set template = replace(template, 'à', '&agrave;');
commit;
update avogest.t_templates
set template = replace(template, 'è', '&egrave;');
commit;


update avogest.t_templates
set template = replace(template, '«', '&laquo;');
commit;
update avogest.t_templates
set template = replace(template, '»', '&raquo;');
commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_0058', 'Ulegalize 11.0.0 - add selected by default', NOW());

commit;