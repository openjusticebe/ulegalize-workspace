use
avogest;

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
  and context = 'FACTURE'
  and subcontext not like '%-N';

commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_008', 'Add new template', NOW());

commit;