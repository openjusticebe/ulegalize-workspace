use
avogest;
INSERT INTO avogest.t_dossiers_type (doss_type, type_desc)
VALUES ('DF', 'Droit col');

commit;
INSERT INTO avogest.t_roles (id_role, role_desc)
VALUES (7, 'Avocat conseil client');

INSERT INTO avogest.t_roles (id_role, role_desc)
VALUES (8, 'Avocat partie adverse');

INSERT INTO avogest.t_roles (id_role, role_desc)
VALUES (9, 'Justiciable partie adverse');

commit;

insert into `t_schema_version` (patch_number, description, date_applied)
values ('patch_0059', 'Ulegalize 1.1.4 - droit collaboratif', NOW());

commit;