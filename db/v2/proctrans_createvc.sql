USE
`avogest`;
DROP procedure IF EXISTS `proctrans_createvc`;


DELIMITER
$$
USE `avogest`$$
CREATE PROCEDURE `proctrans_createvc`(IN cabname VARCHAR (50), IN userid BIGINT(20))
BEGIN
DECLARE
exit handler for sqlexception
BEGIN
GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE,
    @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;
SET
@flag = 0;
   SET
@full_error = CONCAT("ERROR ", @errno, " (", @sqlstate, "): ", @text);
SELECT @full_error;
ROLLBACK;
END;

START TRANSACTION;
INSERT INTO `t_virtualcab` (`key`, `email`, `name`, `abbreviation`, `doc_path`, `user_upd`, `date_upd`, `license_id`,
                            `couthoraire`, `objetsocial`, `street`, `city`, `cp`, `telephone`, `fax`, `website`,
                            `numentreprise`, `logo`, `name_admin`, `email_admin`, `pass_admin`, `dropbox_token`,
                            `onedrive_token`, `refresh_token`, `expire_token`)
VALUES (cabname, '', cabname, LEFT(cabname, 3), '..', 'ulegalize', NOW(), '1', '145', '', '', '', '', '', '', '', '',
        NULL, '', '', '', '', '', '', (now() - interval 1 hour));
INSERT INTO `t_virtualcab_users` (`vc_key`, `id_user`, `id_role`, `is_active`, `valid_from`, `valid_to`,
                                  `is_prestataire`, `user_upd`, `date_upd`)
VALUES (cabname, userid, '1', '1', '2016-05-10', '2025-12-02', '1', 'ulegalize', NOW());

INSERT INTO `t_security_groups` (`vc_key`, `description`, `t_sec_app_group_id`, `user_upd`, `date_upd`)
VALUES (cabname, 'ulegalize-administrateurs', '1', 'ulegalize', NOW());
SELECT LAST_INSERT_ID()
INTO @tsgid;
INSERT INTO `t_security_group_rights` (`id_right`, `t_sec_groups_id`)
VALUES ('0', @tsgid);
INSERT INTO `t_security_group_users` (`id_user`, `t_sec_groups_id`)
VALUES (userid, @tsgid);

INSERT INTO `t_timesheet_type` (`vc_key`, `description`, `user_upd`, `date_upd`, `archived`)
VALUES (cabname, 'Courrier', 'ulegalize', NOW(), 0),
       (cabname, 'Réunion', 'ulegalize', NOW(), 0),

       (cabname, 'Audience remise', 'ulegalize', NOW(), 0),
       (cabname, 'Audience défaut', 'ulegalize', NOW(), 0),

       (cabname, 'Audience plaidoirie', 'ulegalize', NOW(), 0),
       (cabname, 'Démarche', 'ulegalize', NOW(), 0),

       (cabname, 'Note', 'ulegalize', NOW(), 0),
       (cabname, 'Recherche', 'ulegalize', NOW(), 0),
       (cabname, 'Étude du dossier', 'ulegalize', NOW(), 0),

       (cabname, 'Requête', 'ulegalize', NOW(), 0),

       (cabname, 'Recours', 'ulegalize', NOW(), 0),
       (cabname, 'Conclusions', 'ulegalize', NOW(), 0),
       (cabname, 'Mail', 'ulegalize', '2015-12-28 12:20:06', 1),
       (cabname, 'Entretien téléphonique', 'ulegalize', NOW(), 0),

       (cabname, 'Déplacement', 'ulegalize', NOW(), 0),

       (cabname, 'Citation', 'ulegalize', NOW(), 0),
       (cabname, 'Expertise', 'ulegalize', NOW(), 0),
       (cabname, 'Photocopie', 'ulegalize', NOW(), 0),
       (cabname, 'Archivage dossier', 'ulegalize', NOW(), 0),
       (cabname, 'Ouverture dossier', 'ulegalize', NOW(), 0),
       (cabname, 'Suivi du dossier', 'ulegalize', NOW(), 0),
       (cabname, 'Mail', 'ulegalize', NOW(), 0);


INSERT INTO `t_debour_type` (`id_mesure_type`, `price_per_unit`, `vc_key`, `description`, `user_upd`, `date_upd`,
                             `archived`)
VALUES (2, 0.7, cabname, 'Frais de déplacement hors Bruxelles', 'ulegalize', NOW(), 0),

       (1, 0.2, cabname, 'Photocopie(s)', 'ulegalize', NOW(), 0),
       (3, 50, cabname, 'Ouverture et archivage dossier', 'ulegalize', NOW(), 0),
       (1, 10, cabname, 'Page dactylographiée', 'ulegalize', NOW(), 0),

       (1, 10, cabname, 'Lettre dactylographiée', 'ulegalize', NOW(), 0),

       (3, 15, cabname, 'déplacement dans Bxl', 'ulegalize', NOW(), 0),

       (1, 8, cabname, 'courriels', 'ulegalize', NOW(), 0),
       (1, 25, cabname, 'courrier recommandé', 'ulegalize', NOW(), 0);



INSERT INTO `ref_poste` (`vc_key`, `ref_poste`, `user_upd`, `date_upd`, `is_frais_procedure`, `is_honoraires`,
                         `is_frais_collaboration`, `is_facturable`, `is_archived`)
VALUES (cabname, 'Honoraires', 'ulegalize', NOW(), 0, 1, 0, 1, 0),
       (cabname, 'Cotisations sociales', 'ulegalize', NOW(), 0, 0, 0, 0, 1),
       (cabname, 'Livres, revues', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Conférences, colloques', 'ulegalize', NOW(), 0, 0, 0, 0, 1),

       (cabname, 'Frais d''huissier', 'ulegalize', NOW(), 1, 0, 0, 0, 0),
       (cabname, 'Bon de Greffe', 'ulegalize', NOW(), 1, 0, 0, 0, 0),
       (cabname, 'Frais d''administration', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Timbres', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Recommandés', 'ulegalize', NOW(), 0, 0, 0, 0, 0),

       (cabname, 'Frais de collaboration', 'ulegalize', NOW(), 0, 0, 1, 0, 0),
       (cabname, 'Cotisations à l''Ordre', 'ulegalize', NOW(), 0, 0, 0, 0, 1),

       (cabname, 'Secrétaire', 'ulegalize', NOW(), 0, 0, 0, 0, 0),

       (cabname, 'Secretariat social', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Parking', 'ulegalize', NOW(), 0, 0, 0, 0, 1),
       (cabname, 'Telephone', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Restaurant', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Essence', 'ulegalize', NOW(), 0, 0, 0, 0, 1),
       (cabname, 'Transports en commun', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Taxi', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Bureau - loyer', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Bureau - charges', 'ulegalize', NOW(), 0, 0, 0, 0, 1),
       (cabname, 'Frais bancaire', 'ulegalize', NOW(), 0, 0, 0, 0, 1),
       (cabname, 'Versements anticipés', 'ulegalize', NOW(), 0, 0, 0, 0, 0),

       (cabname, 'Bureau - fournitures', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Bureau - matériel à amortir', 'ulegalize', NOW(), 0, 0, 0, 0, 0),

       (cabname, 'Mastercard', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'loyers', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'remboursement partie civile', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'Salaire stagiaire', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'frais traduction', 'ulegalize', NOW(), 1, 0, 0, 0, 0),
       (cabname, 'remboursement client', 'ulegalize', NOW(), 0, 0, 0, 0, 0),
       (cabname, 'paiement adversaire', 'ulegalize', NOW(), 0, 0, 0, 0, 0);



INSERT INTO `t_templates` (vc_key, type, context, subcontext, name, template, user_upd, date_upd)
VALUES (cabname, 'S', 'FACTURE', 'FT-1', 'Facture temporaire comptant',
        '<p>&nbsp;</p>\n<p>En cas de non-paiement d\'une facture &agrave;
l
\'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l\'&eacute;ch
&eacute;ance
portent de plein droit un int&eacute;r
&ecirc;t
de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FV-1', 'Facture comptant ',
 '<p>&nbsp;
</p>\n<p>En cas de non-paiement d\'une facture &agrave; l\'&eacute;ch
&eacute;ance
, le montant sera major&eacute; de
15% avec un minimum de 50,00 &euro; de
plein droit et sans mise en demeure. Les paiements post&eacute;rieurs
&agrave; l
\'&eacute;ch&eacute;ance portent de plein droit un int&eacute;r&ecirc;t de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname,'S','CABINET','HEADER',' ',
'<table>\n<tbody>\n<tr>\n<td rowspan=\"2\" width=\"12%\">{_Cabinet_Logo}</td>\n<td style=\"font-weight: bold;\" align=\"left\">{_Cabinet_NomComplet}</td>\n</tr>\n<tr>\n<td style=\"color: #ccc; font-weight: bold;\" align=\"left\">{_Cabinet_ObjetSocial}</td>\n</tr>\n</tbody>\n</table>','ulegalize',NOW()),
(cabname, 'S', 'FACTURE', 'FT-3', 'Facture temporaire échéance ',
 '<p>&nbsp;</p>\n<p>En cas de non-paiement d\'une facture &agrave; l\'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l\'&eacute;ch&eacute;ance portent de plein droit un int&eacute;r&ecirc;t de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FT-2', 'Facture temporaire payée',
 '<p>&nbsp;</p>\n<p>Facture acquitt&eacute;e le {_Facture_DateEcheance}</p>', 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FV-3', 'Facture échéance ',
 '<p>&nbsp;</p>\n<p>En cas de non-paiement d\'une facture &agrave; l\'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l\'&eacute;ch&eacute;ance portent de plein droit un int&eacute;r&ecirc;t de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FV-2', 'Facture payée ',
 '<p>&nbsp;</p>\n<p>Facture acquitt&eacute;e le {_Facture_DateEcheance}</p>', 'ulegalize', NOW());


INSERT INTO `t_templates` (vc_key, type, context, subcontext, name, template, user_upd, date_upd)
VALUES (cabname, 'S', 'FACTURE', 'FT-1-N', 'Facture temporaire comptant',
        '<p>&nbsp;</p>\n<p>En cas de non-paiement d\'une facture &agrave;
l
\'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l\'&eacute;ch
&eacute;ance
portent de plein droit un int&eacute;r
&ecirc;t
de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FV-1-N', 'Facture comptant ',
 '<p>&nbsp;
</p>\n<p>En cas de non-paiement d\'une facture &agrave; l\'&eacute;ch
&eacute;ance
, le montant sera major&eacute; de
15% avec un minimum de 50,00 &euro; de
plein droit et sans mise en demeure. Les paiements post&eacute;rieurs
&agrave; l
\'&eacute;ch&eacute;ance portent de plein droit un int&eacute;r&ecirc;t de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname,'S','CABINET','HEADER',' ',
'<table>\n<tbody>\n<tr>\n<td rowspan=\"2\" width=\"12%\">{_Cabinet_Logo}</td>\n<td style=\"font-weight: bold;\" align=\"left\">{_Cabinet_NomComplet}</td>\n</tr>\n<tr>\n<td style=\"color: #ccc; font-weight: bold;\" align=\"left\">{_Cabinet_ObjetSocial}</td>\n</tr>\n</tbody>\n</table>','ulegalize',NOW()),
(cabname, 'S', 'FACTURE', 'FT-3-N', 'Facture temporaire échéance ',
 '<p>&nbsp;</p>\n<p>En cas de non-paiement d\'une facture &agrave; l\'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l\'&eacute;ch&eacute;ance portent de plein droit un int&eacute;r&ecirc;t de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FT-2-N', 'Facture temporaire payée',
 '<p>&nbsp;</p>\n<p>Facture acquitt&eacute;e le {_Facture_DateEcheance}</p>', 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FV-3-N', 'Facture échéance ',
 '<p>&nbsp;</p>\n<p>En cas de non-paiement d\'une facture &agrave; l\'&eacute;ch&eacute;ance, le montant sera major&eacute; de 15% avec un minimum de 50,00 &euro; de plein droit et sans mise en demeure. Les paiements post&eacute;rieurs &agrave; l\'&eacute;ch&eacute;ance portent de plein droit un int&eacute;r&ecirc;t de 1% par mois.</p>',
 'ulegalize', NOW()),
(cabname, 'S', 'FACTURE', 'FV-2-N', 'Facture payée ',
 '<p>&nbsp;</p>\n<p>Facture acquitt&eacute;e le {_Facture_DateEcheance}</p>', 'ulegalize', NOW());


INSERT INTO t_first_time (USER_ID, ACTIVATED, CRE_USER)
VALUES (userid, 1, userid);


insert into t_virtualcab_vat(VC_KEY, VAT, CRE_USER, CRE_DATE)
VALUES (cabname, 0, 'legalize', now());

COMMIT;
insert into t_virtualcab_vat(VC_KEY, VAT, CRE_USER, CRE_DATE)
VALUES (cabname, 6, 'legalize', now());

COMMIT;
insert into t_virtualcab_vat(VC_KEY, VAT, CRE_USER, CRE_DATE)
VALUES (cabname, 12, 'legalize', now());

COMMIT;
insert into t_virtualcab_vat(VC_KEY, VAT, CRE_USER, CRE_DATE)
VALUES (cabname, 21, 'legalize', now());

COMMIT;

INSERT INTO `t_vc_groupment` (`VC_KEY`, `GROUPMENT_ID`, CRE_USER)
VALUES (cabname, '1', 'legalize');
INSERT INTO `t_vc_groupment` (`VC_KEY`, `GROUPMENT_ID`, CRE_USER)
VALUES (cabname, '2', 'legalize');
INSERT INTO `t_vc_groupment` (`VC_KEY`, `GROUPMENT_ID`, CRE_USER)
VALUES (cabname, '3', 'legalize');
COMMIT;

INSERT INTO `t_virtualcab_website` (`vc_key`, `title`, `intro`, `philosophy`, `about`, `active`, `accept_appointment`,
                                    `upd_user`, `upd_date`)
VALUES (cabname, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL);

COMMIT;
SET
@flag = 1;
END