use avogest;
drop table if exists t_facture_frais_admin;

-- frais admin
CREATE TABLE `t_facture_frais_admin`
(
    `ID`         BIGINT(20)  NOT NULL AUTO_INCREMENT,
    `FACTURE_ID` BIGINT(20)  NOT NULL,
    `DEBOURS_ID` INT(11)     NOT NULL,
    `CRE_USER`   VARCHAR(45) NOT NULL,
    `CRE_DATE`   DATETIME    NULL DEFAULT NOW(),
    `UPD_DATE`   DATETIME    NULL,
    `UPD_USER`   VARCHAR(45) NULL,
    PRIMARY KEY (`ID`),
    UNIQUE INDEX `ID_FAC_FRAIS_ADM_UNIQUE` (`FACTURE_ID`, DEBOURS_ID)
);

COMMIT;

ALTER TABLE t_facture_frais_admin
    ADD FOREIGN KEY (FACTURE_ID) REFERENCES t_factures (id_facture);
commit;
ALTER TABLE t_facture_frais_admin
    ADD FOREIGN KEY (DEBOURS_ID) REFERENCES t_debour (id_debour);
commit;
drop table if exists t_facture_frais_debours;

-- debours OR frais collaboration
CREATE TABLE `t_facture_frais_debours`
(
    `ID`         INT         NOT NULL AUTO_INCREMENT,
    `FACTURE_ID` BIGINT(20)  NOT NULL,
    `FRAIS_ID`   BIGINT(20)  NOT NULL,
    `CRE_USER`   VARCHAR(45) NOT NULL,
    `CRE_DATE`   DATETIME    NULL DEFAULT NOW(),
    `UPD_DATE`   DATETIME    NULL,
    `UPD_USER`   VARCHAR(45) NULL,
    PRIMARY KEY (`ID`),
    UNIQUE INDEX `ID_FAC_DEB_FRAIS_UNIQUE` (`FACTURE_ID`, FRAIS_ID)
);

COMMIT;


ALTER TABLE t_facture_frais_debours
    ADD FOREIGN KEY (FACTURE_ID) REFERENCES t_factures (id_facture);
commit;
ALTER TABLE t_facture_frais_debours
    ADD FOREIGN KEY (FRAIS_ID) REFERENCES t_frais (id_frais);
commit;
drop table if exists t_facture_frais_collaboration;

-- debours OR frais collaboration
CREATE TABLE `t_facture_frais_collaboration`
(
    `ID`         INT         NOT NULL AUTO_INCREMENT,
    `FACTURE_ID` BIGINT(20)  NOT NULL,
    `FRAIS_ID`   BIGINT(20)  NOT NULL,
    `CRE_USER`   VARCHAR(45) NOT NULL,
    `CRE_DATE`   DATETIME    NULL DEFAULT NOW(),
    `UPD_DATE`   DATETIME    NULL,
    `UPD_USER`   VARCHAR(45) NULL,
    PRIMARY KEY (`ID`),
    UNIQUE INDEX `ID_FAC_COL_FRAIS_UNIQUE` (`FACTURE_ID`, FRAIS_ID)
);

COMMIT;

ALTER TABLE t_facture_frais_collaboration
    ADD FOREIGN KEY (FACTURE_ID) REFERENCES t_factures (id_facture);
commit;
ALTER TABLE t_facture_frais_collaboration
    ADD FOREIGN KEY (FRAIS_ID) REFERENCES t_frais (id_frais);
commit;

DROP TABLE t_calendar_doss_subscribers;
DROP TABLE t_calendar_dossiers;
DROP TABLE t_calendar_vc_subscribers;
DROP TABLE t_calendar_virtuel_cab;
DROP TABLE t_calendar;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_017', 'invoice change', NOW());

commit;
