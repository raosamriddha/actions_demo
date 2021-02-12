ALTER TABLE "pump_master" ADD COLUMN "delta_p" TEXT default '';
ALTER TABLE "pump_master" ADD COLUMN "delta_p_rate" TEXT default '';
ALTER TABLE "pump_master" ADD COLUMN "delta_p_duration" TEXT default '';

UPDATE pump_lookup SET is_active = 1 WHERE pump_lookup_id = 3 OR pump_lookup_id = 4;

ALTER TABLE "trial_run_setting" ADD COLUMN "device_id" TEXT default '';

ALTER TABLE "filter_perm_master" ADD COLUMN "model" TEXT default '';
ALTER TABLE "filter_perm_master" ADD COLUMN "manufacturer" TEXT default '';

-- DS notes add on
ALTER TABLE "trial_detail" ADD COLUMN "digital_notes" TEXT default '';
ALTER TABLE "user_access_log" ADD COLUMN "digital_notes" TEXT default '';
ALTER TABLE "trial_log" ADD COLUMN "digital_notes" TEXT default '';

-- Remove prefix from full name
UPDATE    user_details
SET       full_name = REPLACE(REPLACE(REPLACE(REPLACE(full_name, 'Dr. ', ''), 'Mr. ', ''), 'Ms. ', ''), 'Mrs. ', '')
WHERE     full_name like 'Dr.%'
OR      full_name like 'Mr.%'
OR      full_name like 'Ms.%'
OR      full_name like 'Mrs.%';

-- Add on for issue #1677
ALTER TABLE "pump_master" ADD COLUMN "pump_rpm" TEXT default '';
ALTER TABLE "pump_master" ADD COLUMN "is_speed" INTEGER default 0;

-- Delta_P trial run changes #1540
ALTER TABLE "trial_run_setting" ADD COLUMN "delta_p" TEXT default '';
ALTER TABLE "trial_run_setting" ADD COLUMN "delta_p_rate" TEXT default '';
ALTER TABLE "trial_run_setting" ADD COLUMN "delta_p_duration" TEXT default '';

CREATE TABLE IF NOT EXISTS "notes_desc_manual" (
	"mn_id"	INTEGER,
	"trial_log_id"	INTEGER NOT NULL UNIQUE,
	"mn_device"	TEXT,
	"mn_type"	TEXT,
	"mn_old_value"	TEXT,
	"mn_new_value"	TEXT,
	PRIMARY KEY("mn_id" AUTOINCREMENT),
	FOREIGN KEY("trial_log_id") REFERENCES "trial_log"("trial_log_id")
);

Update tubing_lookup set calibration_factor = 1 , min_flow_rate = 202 , max_flow_rate = 19363.2 where tubing_lookup_id = 23;
Update tubing_lookup set calibration_factor = 1 , min_flow_rate = 33.59 , max_flow_rate = 3063 where tubing_lookup_id = 24;

Update pump_lookup set max_speed = 2200 where pump_lookup_id = 4;

Insert or Replace INTO "firmware_version" VALUES (1,28,'0.6.5',"2.1.3");