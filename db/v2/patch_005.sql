use avogest;
drop table if exists t_calendar_participants;

CREATE TABLE t_calendar_participants
(
    `ID`          INT         NOT NULL AUTO_INCREMENT,
    `CALENDAR_ID` BIGINT(20)  NOT NULL,
    `USER_EMAIL`  VARCHAR(50) NOT NULL,
    `CRE_DATE`    DATETIME    NULL DEFAULT NOW(),
    `CRE_USER`    VARCHAR(45) NOT NULL,
    PRIMARY KEY (`ID`),
    UNIQUE INDEX `ID_CAL_US_UNIQUE` (`CALENDAR_ID`, USER_EMAIL)
);

commit;

ALTER TABLE t_calendar_participants
    ADD FOREIGN KEY (CALENDAR_ID) REFERENCES t_calendar_event (ID);
commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_005', 'Ulegalize 1.1.12 - participants for events', NOW());

commit;