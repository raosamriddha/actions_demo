CREATE TABLE IF NOT EXISTS "notes_desc" (
	"notes_desc_id"	INTEGER,
	"trial_log_id"	INTEGER NOT NULL UNIQUE,
	"sub_mode_name"	TEXT,
	"step"	INTEGER,
	"end_point_name"	TEXT,
	"end_point_value"	REAL,
	"status"	INTEGER,
	"is_active"	INTEGER DEFAULT 1,
	FOREIGN KEY("trial_log_id") REFERENCES "trial_log"("trial_log_id"),
	PRIMARY KEY("notes_desc_id" AUTOINCREMENT)
);

-- create table for setup breakdown system auto log notes
CREATE TABLE "notes_desc_setup" (
	"notes_desc_setup_id"	INTEGER,
	"trial_log_id"	INTEGER NOT NULL UNIQUE,
	"mode_name"	TEXT,
	"tmp"	REAL,
	"duration"	TEXT,
	"permeate_weight"	REAL,
	"permeate_total"	REAL,
	"flow_rate"	REAL,
	"step"	INTEGER,
	"status"	INTEGER,
	PRIMARY KEY("notes_desc_setup_id" AUTOINCREMENT),
	FOREIGN KEY("trial_log_id") REFERENCES "trial_log"("trial_log_id")
);

-- taking backup of previous notes
insert into trial_log(user_id, trial_run_setting_id, action, log_type, created_on, updated_on , is_active)
select um.user_id,n.trial_run_setting_id, n.notes, 0, n.created_on, n.modified_on, n.is_active from notes as n, user_master as um
where um.username = n.posted_by;

-- adding duration to live data
ALTER TABLE "trial_hist_data" ADD COLUMN "duration" TEXT default 'N/A';

ALTER TABLE "trial_run_setting" ADD COLUMN "q_perm_freq" INTEGER default 1;
ALTER TABLE "trial_run_setting" ADD COLUMN "feed_scale_override" TEXT default 'false';

Insert or Replace INTO "firmware_version" VALUES (1,26,'0.6.5',"2.1.1");