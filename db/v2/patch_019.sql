use
    avogest;

alter table t_calendar_event
    drop column contact_id;


insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_019', 'drop column calendar event', NOW());

commit;
