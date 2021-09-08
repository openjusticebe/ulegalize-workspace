create table `t_calendar_event` (
	`id` bigint not null auto_increment,
	`dossier_id` bigint null,
	`user_id` bigint null,
	`title` varchar(100) null,
	`note` varchar(500) null,
	`type` varchar(4) not null,
	`approved` int null default 0,
	`start` DATETIME not null,
	`end` DATETIME NOT NULL,
	`cre_user` VARCHAR(45) NOT NULL,
  	`cre_date` DATETIME    NULL DEFAULT NOW(),
  	`upd_user` VARCHAR(45) NULL,
  	`upd_date` DATETIME    NULL,
	PRIMARY KEY (id)
);