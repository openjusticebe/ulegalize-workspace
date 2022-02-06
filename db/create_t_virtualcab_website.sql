create table `t_virtualcab_website` (
	`vc_key` varchar(50) not null,
	`title` varchar(200) null,
	`intro` varchar(200) null,
	`philosophy` LONGTEXT null,
	`about` LONGTEXT null,
	`active` int not null default 0,
	`accept_appointment` int not null default 0,
	`upd_user` VARCHAR(45) NULL,
  	`upd_date` DATETIME    NULL,
	PRIMARY KEY (vc_key)
);