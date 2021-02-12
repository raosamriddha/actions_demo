ALTER TABLE "firmware_version" ADD COLUMN "kfcomm_version" TEXT;
Insert or Replace INTO "firmware_version" VALUES (1,26,'0.6.5',"2.1.0");

ALTER TABLE "system_setting_details" ADD COLUMN "protein_conc_min" REAL;
ALTER TABLE "system_setting_details" ADD COLUMN "protein_conc_min_2" REAL;
ALTER TABLE "system_setting_details" ADD COLUMN "protein_conc_max" REAL;
ALTER TABLE "system_setting_details" ADD COLUMN "protein_conc_max_2" REAL;
ALTER TABLE "system_setting_details" ADD COLUMN "totalizer_unit" INTEGER;
ALTER TABLE "system_setting_details" ADD COLUMN "totalizer_unit_2" INTEGER;

ALTER TABLE "system_setting_details" RENAME COLUMN "flow_rate_min" TO "totalizer_min";
ALTER TABLE "system_setting_details" RENAME COLUMN "flow_rate_max" TO "totalizer_max";
ALTER TABLE "system_setting_details" RENAME COLUMN "flow_rate_min_2" TO "totalizer_min_2";
ALTER TABLE "system_setting_details" RENAME COLUMN "flow_rate_max_2" TO "totalizer_max_2";

ALTER TABLE "trial_detail" ADD COLUMN "endpoint_cal" INTEGER DEFAULT 0;

ALTER TABLE "target_step_setting" ADD COLUMN "permeate_total" TEXT DEFAULT '';
ALTER TABLE "target_step" ADD COLUMN "permeate_total" TEXT DEFAULT '';

ALTER TABLE "target_step_change_log" ADD COLUMN "old_permeate_total" TEXT DEFAULT '';
ALTER TABLE "target_step_change_log" ADD COLUMN "new_permeate_total" TEXT DEFAULT '';

ALTER TABLE "trial_hist_data" ADD COLUMN "permeate_total" REAL;
ALTER TABLE "trial_hist_data" ADD COLUMN "permeate_total_with_holdup" REAL;

INSERT into "end_point_lookup" VALUES (8, 'Permeate Total', 'Both', 'Y');
INSERT into "end_point_lookup" VALUES (9, 'Protein Concentration', 'Both', 'Y');

ALTER TABLE "alarm_setting" RENAME COLUMN "uv_ch_1_high_alarm" TO "konduit_ch_1_high_alarm";
ALTER TABLE "alarm_setting" RENAME COLUMN "uv_ch_2_high_alarm" TO "konduit_ch_2_high_alarm";
ALTER TABLE "alarm_setting" RENAME COLUMN "uv_ch_1_high_stop" TO "konduit_ch_1_high_stop";
ALTER TABLE "alarm_setting" RENAME COLUMN "uv_ch_2_high_stop" TO "konduit_ch_2_high_stop";

ALTER TABLE "alarm_setting" ADD COLUMN "konduit_ch_1_type" INTEGER DEFAULT 1;
ALTER TABLE "alarm_setting" ADD COLUMN "konduit_ch_2_type" INTEGER DEFAULT 2;
ALTER TABLE "alarm_setting" ADD COLUMN "permeate_total_high_alarm" TEXT DEFAULT '';
ALTER TABLE "alarm_setting" ADD COLUMN "permeate_total_high_stop" TEXT DEFAULT '';

CREATE TABLE "filter_plate_insert_lookup_temp" (
	"fpi_id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"fpi_part_no"	TEXT,
	"fpi_hold_up"	REAL,
	"combinded_feed_and_retenate"	REAL
);

INSERT INTO filter_plate_insert_lookup_temp(fpi_id,fpi_part_no,fpi_hold_up,combinded_feed_and_retenate) SELECT * FROM filter_plate_insert_lookup;

DROP table filter_plate_insert_lookup;

ALTER TABLE filter_plate_insert_lookup_temp RENAME TO filter_plate_insert_lookup;

Insert or Replace INTO "filter_plate_insert_lookup" VALUES (4,'TFPLS-LVFL',1.3,1.26);

ALTER TABLE "trial_run_setting" ADD COLUMN "totalizer_unit" INTEGER DEFAULT 0;
ALTER TABLE "trial_run_setting" ADD COLUMN "totalizer_unit_2" INTEGER DEFAULT 0;