CREATE TABLE IF NOT EXISTS "com_port" (
	"port_id"	INTEGER,
	"port_name"	TEXT,
	PRIMARY KEY("port_id" AUTOINCREMENT)
);

insert into com_port values(1, null);

CREATE TABLE IF NOT EXISTS "manual_filter_details" (
	"mn_filter_id" INTEGER,
	"filter_type" TEXT,
	"part_no" TEXT,
	"no_of_cassette" INTEGER,
	"is_non_repligen" INTEGER,
	"surf_area" TEXT,
	"trial_run_setting_id" INTEGER,
	PRIMARY KEY("mn_filter_id" AUTOINCREMENT)
);

Insert or Replace INTO "firmware_version" VALUES (1,29,'0.6.5',"2.1.6");