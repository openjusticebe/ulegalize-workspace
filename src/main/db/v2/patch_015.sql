use avogest;

ALTER TABLE `t_calendar_event`
    ADD `room_name` VARCHAR(20) NULL AFTER end;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_015', 'add room name to calendar', NOW());

commit;
