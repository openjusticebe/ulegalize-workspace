use
avogest;

ALTER TABLE `t_calendar_event`
    ADD `path_file` VARCHAR(50) NULL AFTER end;
ALTER TABLE `t_calendar_event`
    ADD `micro_text` MEDIUMTEXT NULL AFTER path_file;
ALTER TABLE `t_calendar_event`
    ADD `audio_text` MEDIUMTEXT NULL AFTER micro_text;

ALTER TABLE `t_calendar_event`
    ADD `speech_to_text_activated` tinyint(1) not NULL default 0 AFTER audio_text;

alter table t_calendar_event modify type varchar (6) not null;


insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_018', 'add path to event', NOW());

commit;
