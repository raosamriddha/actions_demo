CREATE TABLE IF NOT EXISTS "trial_hist_data" (
"trial_live_data_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_run_setting_id"	INTEGER,
	"feed_pressure"	REAL,
	"permeate_pressure"	REAL,
	"retentate_pressure"	REAL,
	"tmp"	REAL,
	"main_pump_speed"	REAL,
	"main_pump_flow_rate"	REAL,
	"aux_pump_1_flow_rate"	REAL,
	"aux_pump_2_flow_rate"	REAL,
	"conductivity_1"	REAL,
	"conductivity_2"	REAL,
	"temperature_1"	REAL,
	"temperature_2"	REAL,
	"uv_ch_1"	REAL,
	"uv_ch_2"	REAL,
	"feed_scale"	REAL,
	"permeate_scale"	REAL,
	"diafiltration_vol"	REAL,
	"concentration_factor"	REAL,
	"timestamp"	INTEGER,
	"feed_flow_rate"	REAL,
	"permeate_flow_rate"	REAL,
	"retentate_flow_rate"	REAL,
	"vt"	REAL,
	"shear"	REAL,
	"abv_1"	TEXT,
	"abv_2"	TEXT,
	"flux"	REAL,
	"notes"	TEXT,
	"trial_master_id"	INTEGER,
	"delta_p"	REAL,
	"tcf"	TEXT,
	"water_flux_20_degree"	TEXT,
	"water_perm"	TEXT,
	"nwp"	TEXT
);
CREATE TABLE IF NOT EXISTS "system_setting_details" (
	"system_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"pressure_unit"	INTEGER,
	"weight_unit"	INTEGER,
	"volume_unit"	INTEGER,
	"length_unit"	INTEGER,
	"decimal_pressure"	INTEGER,
	"decimal_weight"	INTEGER,
	"decimal_flow_rate"	INTEGER,
	"decimal_volume"	INTEGER,
	"decimal_length"	INTEGER,
	"date_format"	INTEGER,
	"is_aux_konduit"	INTEGER,
	"uv_min"	REAL,
	"uv_max"	REAL,
	"ph_min"	REAL,
	"ph_max"	REAL,
	"turbidity_min"	REAL,
	"turbidity_max"	REAL,
	"flow_rate_min"	REAL,
	"flow_rate_max"	REAL,
	"created_on"	INTEGER,
	"updated_on"	INTEGER,
	"is_active"	INTEGER
);
CREATE TABLE IF NOT EXISTS"target_step_change_log" (
	"target_step_change_log_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_log_id"	INTEGER,
	"old_tmp"	TEXT,
	"new_tmp"	TEXT,
	"old_duration"	TEXT,
	"new_duration"	TEXT,
	"old_permeate_weight"	TEXT,
	"new_permeate_weight"	TEXT,
	"step_no"	INTEGER,
	"is_active"	TEXT DEFAULT 'Y',
	"trial_run_setting_id"	INTEGER
);
CREATE TABLE IF NOT EXISTS "target_step_setting" (
	"target_step_setting_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_run_setting_id"	INTEGER,
	"tmp"	TEXT,
	"duration"	TEXT,
	"permeate_weight"	TEXT,
	"step_no"	INTEGER,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "end_point_setting" (
	"end_point_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_run_setting_id"	INTEGER,
	"end_point_type"	TEXT,
	"end_point_value"	TEXT,
	"step_no"	INTEGER,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y',
	FOREIGN KEY("trial_run_setting_id") REFERENCES "trial_run_setting"("trial_run_setting_id")
);
CREATE TABLE IF NOT EXISTS "end_point_change_log" (
	"end_point_change_log_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"trial_log_id"	INTEGER NOT NULL,
	"end_point_type"	TEXT NOT NULL,
	"old_end_point_value"	REAL NOT NULL,
	"new_end_point_value"	REAL NOT NULL,
	"step_no"	INTEGER NOT NULL,
	"is_active"	INTEGER NOT NULL DEFAULT 1,
	"trial_run_setting_id"	INTEGER
);
CREATE TABLE IF NOT EXISTS "db_version" (
	"db_version_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"version"	INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS "cassette_lookup" (
	"part_no"	TEXT,
	"channel_type"	TEXT,
	"cassette_family"	TEXT,
	"catalog"	INTEGER,
	"material"	TEXT,
	"rating"	TEXT,
	"surface_area"	REAL,
	"ECS"	REAL,
	"height"	REAL,
	"width"	REAL,
	"no_of_channel"	INTEGER,
	"membrane_area"	REAL
);
CREATE TABLE IF NOT EXISTS "firmware_version" (
	"firmware_version_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"software_version"	INTEGER,
	"interface_version"	TEXT
);
CREATE TABLE IF NOT EXISTS "trial_run_setting" (
	"trial_run_setting_id"	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	"trial_id"	TEXT UNIQUE,
	"trial_master_id"	INTEGER,
	"is_feed_empty"	TEXT,
	"feed_hold_up"	TEXT,
	"feed_start_wt"	TEXT,
	"trial_type"	TEXT,
	"trial_status"	INTEGER DEFAULT -1,
	"temperature"	TEXT,
	"user_id"	INTEGER,
	"trial_start_time"	TEXT,
	"trial_end_time"	TEXT,
	"updated_on"	TEXT,
	"created_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "license_activation_status" (
	"status_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"status"	TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "cfr_status" (
	"status_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"status"	TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS "tubing_lookup" (
	"tubing_lookup_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"tubing_size"	TEXT,
	"revolutions"	INTEGER,
	"expected_volume"	INTEGER,
	"flow_conversion"	INTEGER,
	"flow_conversion_KRJr"	REAL,
	"1h_decimals"	INTEGER,
	"1h_rate_digits"	INTEGER,
	"2h_rate_digits"	INTEGER,
	"1h_cal_digits"	INTEGER,
	"2h_cal_digits"	INTEGER,
	"cal_motor_revs"	INTEGER,
	"cal_volume"	INTEGER,
	"2h_decimals"	INTEGER,
	"tubing_integer"	TEXT,
	"valve_pos_full_open"	TEXT,
	"valve_pos_full_pinched"	TEXT,
	"start_step_size"	TEXT,
	"OD_mm"	TEXT,
	"wall"	TEXT,
	"gap"	TEXT,
	"internal_diameter_in"	REAL,
	"internal_diameter_cm"	REAL,
	"internal_diameter_mm"	REAL,
	"calibration_factor"	REAL DEFAULT 1,
	"min_rpm"	INTEGER,
	"max_rpm"	INTEGER,
	"min_flow_rate"	INTEGER,
	"max_flow_rate"	INTEGER,
	"flow_unit"	TEXT
);
CREATE TABLE IF NOT EXISTS "manual_abv_master" (
	"abv_setting_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	"type"	INTEGER,
	"abv_type"	TEXT,
	"position"	TEXT,
	"tubing_size"	TEXT,
	"abv_mode"	TEXT,
	"op_pressure"	TEXT,
	"abv_target"	TEXT,
	"trial_run_setting_id"	INTEGER
);
CREATE TABLE IF NOT EXISTS "user_master" (
	"user_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"username"	TEXT NOT NULL UNIQUE,
	"is_active"	NUMERIC DEFAULT 'Y',
	"created_by"	TEXT,
	"created_on"	TEXT NOT NULL,
	"modified_by"	TEXT,
	"modified_on"	TEXT,
	"org_id"	TEXT
);
CREATE TABLE IF NOT EXISTS "pump_lookup" (
	"pump_lookup_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"pump_type"	TEXT,
	"pump_name"	TEXT,
	"min_speed"	REAL,
	"max_speed"	INTEGER,
	"speed_unit"	TEXT,
	"head_count"	TEXT,
	"is_active"	INTEGER DEFAULT 1
);
CREATE TABLE IF NOT EXISTS "user_details" (
	"user_details_id"	INTEGER,
	"user_id"	INTEGER UNIQUE,
	"prefix"	TEXT NOT NULL,
	"first_name"	TEXT NOT NULL,
	"middle_name"	TEXT,
	"last_name"	TEXT NOT NULL,
	"full_name"	TEXT,
	"email_id"	TEXT NOT NULL UNIQUE,
	"contact_number"	TEXT,
	"last_login_date"	TEXT,
	PRIMARY KEY("user_details_id"),
	FOREIGN KEY("user_id") REFERENCES "user_master"("user_id")
);
CREATE TABLE IF NOT EXISTS "trial_master" (
	"trial_master_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"recipe_name"	TEXT,
	"trial_type"	TEXT,
	"trial_status"	TEXT,
	"created_by"	TEXT,
	"created_on"	TEXT,
	"modified_by"	TEXT,
	"modified_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "target_step" (
	"target_step_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_master_id"	INTEGER,
	"tmp"	TEXT,
	"duration"	TEXT,
	"flow_rate"	TEXT,
	"permeate_weight"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "trial_detail" (
	"trial_detail_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_master_id"	INTEGER,
	"notes"	TEXT,
	"run_as_safe"	TEXT,
	"feed_to_empty"	TEXT,
	"no_of_aux_pump"	INTEGER,
	"perm_stop_first"	TEXT,
	"recirc_pressure_cont"	TEXT,
	"recirc_flow_unit_value"	INTEGER,
	"recirc_rate_per_unit"	INTEGER,
	"retentate_tube_size"	TEXT,
	"is_active"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "pump_master" (
	"pump_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"head_count"	INTEGER,
	"pump_name"	TEXT,
	"pump_type"	TEXT,
	"direction"	TEXT,
	"flow_control"	TEXT,
	"pump_tubing_size"	TEXT,
	"pump_tubing_lookup_id"	INTEGER,
	"flow_rate"	TEXT,
	"ramp_up_time"	INTEGER,
	"trial_master_id"	INTEGER,
	"is_active"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "mode_master" (
	"mode_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"mode_name"	TEXT,
	"mode_full_name"	TEXT,
	"trial_master_id"	INTEGER,
	"pcv"	TEXT,
	"exp_yield_fc"	TEXT,
	"vol_cf"	TEXT,
	"filter_retention"	TEXT,
	"is_active"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "main_aux_pump_map" (
	"main_aux_pump_map_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"main_pump"	TEXT,
	"aux_pump"	INTEGER
);
CREATE TABLE IF NOT EXISTS "user_role_map" (
	"user_id"	INTEGER NOT NULL,
	"role_id"	INTEGER NOT NULL,
	"is_active"	TEXT
);
CREATE TABLE IF NOT EXISTS "role_master" (
	"role_id"	INTEGER,
	"role_description"	TEXT NOT NULL UNIQUE,
	"is_active"	TEXT NOT NULL,
	PRIMARY KEY("role_id")
);
CREATE TABLE IF NOT EXISTS "user_credentials" (
	"credential_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"user_id"	INTEGER NOT NULL,
	"password"	TEXT,
	"pass_status"	TEXT,
	"pass_is_active"	TEXT
);
CREATE TABLE IF NOT EXISTS "user_login_limit" (
	"login_limit_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"username"	TEXT NOT NULL,
	"count"	INTEGER NOT NULL,
	"timestamp"	TEXT NOT NULL,
	"is_active"	TEXT,
	FOREIGN KEY("username") REFERENCES "user_master"("username")
);
CREATE TABLE IF NOT EXISTS "ramp_up_time" (
	"ramp_up_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"ramp_up_time_value"	INTEGER
);
CREATE TABLE IF NOT EXISTS "abv_master" (
	"abv_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"abv_type"	TEXT,
	"cont_based_on"	TEXT,
	"abv_mode"	TEXT,
	"start_pos"	TEXT,
	"percent_closed"	TEXT,
	"op_pressure"	TEXT,
	"trial_master_id"	INTEGER,
	"is_active"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "end_point_lookup" (
	"end_point_lookup_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"end_point_name"	TEXT,
	"end_point_group"	TEXT,
	"is_active"	TEXT
);
CREATE TABLE IF NOT EXISTS "recirc_flow_unit" (
	"recirc_flow_unit_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"recirc_flow_unit_value"	INTEGER UNIQUE
);
CREATE TABLE IF NOT EXISTS "end_point" (
	"end_point_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"end_point_type"	TEXT,
	"end_point_value"	TEXT,
	"trial_master_id"	INTEGER,
	"step_no"	INTEGER,
	"is_active"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "mode_lookup" (
	"mode_lookup_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"mode_name"	TEXT UNIQUE,
	"mode_full_name"	TEXT,
	"min_aux_pump"	INTEGER,
	"max_aux_pump"	INTEGER,
	"aux_pump_1"	TEXT,
	"aux_pump_2"	TEXT,
	"is_active"	TEXT
);
CREATE TABLE IF NOT EXISTS "filter_plate_insert_lookup" (
	"fpi_part_no"	TEXT,
	"fpi_hold_up"	INTEGER,
	"combinded_feed_and_retenate"	INTEGER
);
CREATE TABLE IF NOT EXISTS "hollow_fiber_lookup" (
	"part_no"	TEXT,
	"module_family"	TEXT,
	"MAPICSPartDescription"	TEXT,
	"MWCO"	TEXT,
	"MembraneChemistry"	TEXT,
	"fiber_id"	REAL,
	"total_length"	INTEGER,
	"effective_length"	TEXT,
	"fiber_count"	INTEGER,
	"HousingID"	REAL,
	"FiberODMax"	REAL,
	"FiberODNom"	REAL,
	"FiberODMin"	REAL,
	"surface_area"	INTEGER,
	"IDHoldupVolume"	REAL,
	"ECS"	REAL,
	"FlowRate"	INTEGER,
	"FlowRate6000sec-1"	INTEGER,
	"TubingRecommendation2000sec-1"	INTEGER,
	"TubingRecommendation6000sec-1"	INTEGER,
	"Notes"	TEXT
);
CREATE TABLE IF NOT EXISTS "alarm_setting" (
	"alarm_setting_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_run_setting_id"	INTEGER UNIQUE,
	"feed_pressure_low_alarm"	TEXT,
	"feed_pressure_low_stop"	TEXT,
	"feed_pressure_high_alarm"	TEXT,
	"feed_pressure_high_stop"	TEXT,
	"feed_wt_low_alarm"	TEXT,
	"feed_wt_low_stop"	TEXT,
	"feed_wt_high_alarm"	TEXT,
	"feed_wt_high_stop"	TEXT,
	"perm_wt_low_alarm"	TEXT,
	"perm_wt_low_stop"	TEXT,
	"perm_wt_high_alarm"	TEXT,
	"perm_wt_high_stop"	TEXT,
	"perm_pressure_low_alarm"	TEXT,
	"perm_pressure_low_stop"	TEXT,
	"perm_pressure_high_alarm"	TEXT,
	"perm_pressure_high_stop"	TEXT,
	"uv_ch_1_high_alarm"	TEXT,
	"uv_ch_1_high_stop"	TEXT,
	"uv_ch_2_high_alarm"	TEXT,
	"uv_ch_2_high_stop"	TEXT,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "notes" (
	"notes_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_run_setting_id"	INTEGER,
	"notes"	TEXT,
	"posted_by"	TEXT,
	"created_on"	TEXT,
	"modified_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "alarm_history" (
	"alarm_history_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_run_setting_id"	INTEGER,
	"mode_name"	TEXT,
	"alarm_stop_desc"	TEXT,
	"alarm_stop_value"	TEXT,
	"nature"	TEXT,
	"created_on"	TEXT,
	"updated_on"	TEXT,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "user_access_log" (
	"access_log_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"performed_by"	TEXT NOT NULL,
	"full_name"	TEXT NOT NULL,
	"role"	TEXT NOT NULL,
	"created_on"	TEXT NOT NULL,
	"action"	TEXT NOT NULL,
	"performed_on"	TEXT NOT NULL,
	"is_active"	TEXT DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "filter_perm_master" (
	"fiter_perm_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"trial_master_id"	INTEGER,
	"filter_type"	TEXT,
	"part_no"	REAL,
	"fiber_count"	INTEGER DEFAULT 0,
	"fiber_id"	TEXT DEFAULT '',
	"surf_area"	REAL,
	"ecs"	TEXT,
	"permeate_tube_size"	TEXT,
	"tube_length"	TEXT,
	"perm_tube_holdup"	TEXT,
	"total_perm_tube_holdup"	TEXT,
	"no_of_cassette"	INTEGER DEFAULT 0,
	"height"	REAL DEFAULT '',
	"width"	REAL DEFAULT '',
	"perm_hold_up_cal"	TEXT,
	"no_of_channel"	INTEGER DEFAULT 0,
	"fpi_part_no"	TEXT DEFAULT '',
	"is_generic"	TEXT DEFAULT '',
	"is_active"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "trial_log" (
	"trial_log_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"user_id"	INTEGER,
	"trial_run_setting_id"	INTEGER NOT NULL,
	"action"	TEXT NOT NULL,
	"log_type"	INTEGER NOT NULL,
	"created_on"	TEXT NOT NULL,
	"updated_on"	INTEGER,
	"is_active"	TEXT NOT NULL DEFAULT 'Y'
);
CREATE TABLE IF NOT EXISTS "aux_pump_master" (
	"aux_pump_master_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"aux_pump_type"	TEXT,
	"pump_function"	TEXT,
	"aux_tubing_size"	TEXT,
	"flow_rate"	TEXT,
	"trial_master_id"	INTEGER,
	"is_active"	TEXT,
	"type"	INTEGER,
	"trial_run_setting_id"	INTEGER,
	"pump_speed"	TEXT,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "kf_konduit_setting" (
	"kf_konduit_setting_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"uv_ch_1"	TEXT,
	"uv_ch_2"	TEXT,
	"k_factor_ch_1"	TEXT,
	"k_factor_ch_2"	TEXT,
	"trial_master_id"	INTEGER,
	"is_active"	TEXT,
	"trial_run_setting_id"	INTEGER,
	FOREIGN KEY("trial_master_id") REFERENCES "trial_master"("trial_master_id")
);
CREATE TABLE IF NOT EXISTS "manual_pump_master" (
	"pump_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	"pump_name"	TEXT,
	"speed"	TEXT,
	"flow_rate"	TEXT,
	"direction"	TEXT,
	"trial_run_setting_id"	INTEGER,
	"tubing_size"	TEXT
);
CREATE TABLE IF NOT EXISTS "pump_tube_map" (
	"tubing_lookup_id"	INTEGER,
	"pump_lookup_id"	INTEGER
);
CREATE TABLE IF NOT EXISTS "pump_mode_map" (
	"pump_lookup_id"	INTEGER,
	"mode_lookup_id"	INTEGER
);

Alter table "user_credentials" add "master_password" TEXT;

CREATE TABLE "system_setting_details_temp" (
	"system_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"pressure_unit"	INTEGER,
	"weight_unit"	INTEGER,
	"volume_unit"	INTEGER,
	"length_unit"	INTEGER,
	"decimal_pressure"	INTEGER,
	"decimal_weight"	INTEGER,
	"decimal_flow_rate"	INTEGER,
	"decimal_volume"	INTEGER,
	"decimal_length"	INTEGER,
	"date_format"	INTEGER,
	"is_aux_konduit"	INTEGER,
	"uv_min"	REAL,
	"uv_max"	REAL,
	"ph_min"	REAL,
	"ph_max"	REAL,
	"turbidity_min"	REAL,
	"turbidity_max"	REAL,
	"flow_rate_min"	REAL,
	"flow_rate_max"	REAL,
	"created_on"	INTEGER,
	"updated_on"	INTEGER,
	"is_active"	INTEGER
);

INSERT INTO system_setting_details_temp SELECT * FROM system_setting_details where is_active = 1;

DROP table system_setting_details;

ALTER TABLE system_setting_details_temp RENAME TO system_setting_details;


Alter table "system_setting_details" add "is_aux_konduit_2" INTEGER;
Alter table "system_setting_details" add "ph_min_2"	REAL;
Alter table "system_setting_details" add "ph_max_2"	REAL;
Alter table "system_setting_details" add "turbidity_min_2"	REAL;
Alter table "system_setting_details" add "turbidity_max_2"	REAL;
Alter table "system_setting_details" add "flow_rate_min_2"	REAL;
Alter table "system_setting_details" add "flow_rate_max_2"	REAL; 
 
 ALTER TABLE "trial_hist_data" rename COLUMN "permeate_scale" to "m_permeate";
 ALTER TABLE "trial_hist_data" rename COLUMN "uv_ch_1" to "konduit_ch_1";
 ALTER TABLE "trial_hist_data" rename COLUMN "uv_ch_2" to "konduit_ch_2";
 ALTER TABLE "trial_hist_data" rename COLUMN "diafiltration_vol" to "diafiltration_vol_1";

Alter table "trial_hist_data" add "total_perm_weight"	REAL;
Alter table "trial_hist_data" add "diafiltration_vol_2"	REAL;
Alter table "trial_hist_data" add "konduit_ch_1_type"	INTEGER;
Alter table "trial_hist_data" add "konduit_ch_2_type"	INTEGER;
Alter table "trial_hist_data" add "is_paused"	INTEGER DEFAULT 0;

CREATE TABLE "user_role_map_temp" (
	"user_role_map_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"user_id"	INTEGER NOT NULL,
	"role_id"	INTEGER NOT NULL,
	"is_active"	TEXT
);

INSERT INTO user_role_map_temp(user_id,role_id,is_active) SELECT * FROM user_role_map;

DROP table user_role_map;

ALTER TABLE user_role_map_temp RENAME TO user_role_map;


CREATE TABLE "pump_mode_map_temp" (
	"pump_mode_map_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"pump_lookup_id"	INTEGER NOT NULL,
	"mode_lookup_id"	INTEGER NOT NULL
);

INSERT INTO pump_mode_map_temp(pump_lookup_id,mode_lookup_id) SELECT * FROM pump_mode_map;

DROP table pump_mode_map;

ALTER TABLE pump_mode_map_temp RENAME TO pump_mode_map;


CREATE TABLE "pump_tube_map_temp" (
	"pump_tube_map_id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"tubing_lookup_id"	INTEGER NOT NULL,
	"pump_lookup_id"	INTEGER NOT NULL
);

INSERT INTO pump_tube_map_temp(tubing_lookup_id,pump_lookup_id) SELECT * FROM pump_tube_map;

DROP table pump_tube_map;

ALTER TABLE pump_tube_map_temp RENAME TO pump_tube_map;

CREATE TABLE "cassette_lookup_temp" (
	"cst_lookup_id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"part_no"	TEXT,
	"channel_type"	TEXT,
	"cassette_family"	TEXT,
	"catalog"	INTEGER,
	"material"	TEXT,
	"rating"	TEXT,
	"surface_area"	REAL,
	"ECS"	REAL,
	"height"	REAL,
	"width"	REAL,
	"no_of_channel"	INTEGER,
	"membrane_area"	REAL
);

INSERT INTO cassette_lookup_temp(part_no,channel_type,cassette_family,catalog,material,rating,surface_area,ECS,height,width,no_of_channel,membrane_area) SELECT * FROM cassette_lookup;

DROP table cassette_lookup;

ALTER TABLE cassette_lookup_temp RENAME TO cassette_lookup;

CREATE TABLE "filter_plate_insert_lookup_temp" (
	"fpi_id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"fpi_part_no"	TEXT,
	"fpi_hold_up"	INTEGER,
	"combinded_feed_and_retenate"	INTEGER
);

INSERT INTO filter_plate_insert_lookup_temp(fpi_part_no,fpi_hold_up,combinded_feed_and_retenate) SELECT * FROM filter_plate_insert_lookup;

DROP table filter_plate_insert_lookup;

ALTER TABLE filter_plate_insert_lookup_temp RENAME TO filter_plate_insert_lookup;

CREATE TABLE "hollow_fiber_lookup_temp" (
	"hf_lookup_id" INTEGER PRIMARY KEY AUTOINCREMENT,
	"part_no"	TEXT,
	"module_family"	TEXT,
	"MAPICSPartDescription"	TEXT,
	"MWCO"	TEXT,
	"MembraneChemistry"	TEXT,
	"fiber_id"	REAL,
	"total_length"	INTEGER,
	"effective_length"	TEXT,
	"fiber_count"	INTEGER,
	"HousingID"	REAL,
	"FiberODMax"	REAL,
	"FiberODNom"	REAL,
	"FiberODMin"	REAL,
	"surface_area"	INTEGER,
	"IDHoldupVolume"	REAL,
	"ECS"	REAL,
	"FlowRate"	INTEGER,
	"FlowRate6000sec-1"	INTEGER,
	"TubingRecommendation2000sec-1"	INTEGER,
	"TubingRecommendation6000sec-1"	INTEGER,
	"Notes"	TEXT
);

INSERT INTO hollow_fiber_lookup_temp(part_no,module_family,MAPICSPartDescription,MWCO,MembraneChemistry,fiber_id,total_length,effective_length,fiber_count,HousingID,FiberODMax,FiberODNom,FiberODMin,surface_area,IDHoldupVolume,ECS,FlowRate,"FlowRate6000sec-1","TubingRecommendation2000sec-1","TubingRecommendation6000sec-1",Notes) SELECT * FROM hollow_fiber_lookup;

DROP table hollow_fiber_lookup;

ALTER TABLE hollow_fiber_lookup_temp RENAME TO hollow_fiber_lookup;

Insert or Ignore INTO "db_version" VALUES (1,1);

Insert or Replace INTO "firmware_version" VALUES (1,20,'0.6.3');
Insert or ignore INTO "license_activation_status" VALUES (1,'7Dk/i0yto3TAQd47Whyygw==');
Insert or Ignore INTO "cfr_status" VALUES (1,'s302TJ7wA5cHQ8+90EMHlA==');
Insert or Replace INTO "tubing_lookup" VALUES (1,'#13',600,60,6,4.0,2,4,4,3,2,600,6000,2,'13','1038','1178','1','3.96875','43481','43473',0.031,0.08,0.8,1.0,6,600,0.36,36,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (2,'#14',409,150,22,14.0,1,4,4,2,2,409,15000,1,'14','973','1170','2','4.7625','43481','43473',0.063,0.16,1.6,1.0,6,600,1.3,130,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (3,'#15',600,1700,170,NULL,0,4,4,1,1,600,170000,0,'15','668','1043','8','9.525','11749','43540',0.188,0.48,4.8,1.0,6,600,10,1000,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (4,'HP15',600,1800,180,NULL,0,4,4,1,1,600,180000,0,'115','668','1043','8','9.525','11749','43540',0.188,0.48,4.8,1.0,6,600,11,1100,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (5,'#16',450,600,80,54.0,1,4,4,2,1,450,60000,1,'16','918','1170','4','6.35','43481','43473',0.125,0.32,3.1,1.0,6,600,4.8,480,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (6,'#17',439,2000,280,NULL,0,4,4,1,1,429,200000,0,'17','668','1135','8','9.525','43481','43473',0.25,0.64,6.4,1.0,6,600,17,1700,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (7,'#18',395,2500,380,NULL,0,4,4,1,1,395,250000,0,'18','543','1168','8','11.1125','43481','43473',0.313,0.8,7.9,1.0,6,600,23,2300,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (8,'#24',429,2000,280,NULL,0,4,4,1,1,429,200000,0,'24','543','1025','8','11.1125','11749','43540',0.25,0.64,6.4,1.0,6,600,17,1700,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (9,'HP24',400,2000,300,NULL,0,4,4,1,1,400,200000,0,'124','543','1043','8','11.1125','11749','43540',0.25,0.64,6.4,1.0,6,600,18,1800,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (10,'#25',600,1700,170,105.0,0,4,4,1,1,600,170000,0,'25','793','1150','4','7.9375','43471','43473',0.188,0.48,4.8,1.0,6,600,10,1000,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (11,'#35',395,2500,380,NULL,0,4,4,1,1,395,250000,0,'35','418','1043','8','12.7','11749','43540',0.313,0.8,7.9,1.0,6,600,23,2300,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (12,'HP35',419,3000,430,NULL,0,4,4,1,1,419,300000,0,'135','418','1043','8','12.7','11749','43540',0.313,0.8,7.9,1.0,6,600,26,2600,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (13,'#36',400,3200,480,NULL,0,4,4,1,1,400,320000,0,'36','293','1043','16','14.2875','11749','43540',0.375,0.95,9.7,1.0,6,600,29,2900,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (14,'HP36',400,3800,570,NULL,0,4,4,1,0,400,380000,0,'136','293','1043','16','14.2875','11749','43540',0.375,0.95,9.7,1.0,6,600,34,3400,'mL/min');
Insert or Replace INTO "tubing_lookup" VALUES (15,'#26',468,600,7002,NULL,0,4,4,3,4,468,600,0,'26','378','878','8','12.7','43473','43469',0.25,0.64,6.4,1.0,20,650,120,4000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (16,'#70',468,1200,14003,NULL,0,4,5,4,4,468,1200,0,'70','20','628','8','19.05','43469','43532',0.375,0.95,9.5,1.0,20,650,240,8000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (17,'#73',468,1200,14003,NULL,0,4,5,4,4,468,1200,0,'73','88','878','16','15.875','43473','43469',0.375,0.95,9.5,1.0,20,650,200,8000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (18,'#82',480,2000,22756,NULL,0,5,5,4,4,480,2000,0,'82','20','878','16','19.05','43473','43469',0.5,1.27,12.7,1.0,20,650,400,13000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (19,'#88',480,2000,22756,NULL,0,5,5,4,4,480,2000,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.5,1.27,12.7,1.0,20,650,400,13000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (20,'#89',493,3000,33258,NULL,0,5,5,4,4,493,3000,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.625,1.59,15.88,1.0,20,650,520,17000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (21,'#17',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'17','628','1128','8','9.525','43481','43473',NULL,NULL,NULL,1.0,NULL,NULL,NULL,NULL,NULL);
Insert or Replace INTO "tubing_lookup" VALUES (22,'#18',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'18','503','1128','8','11.1125','43481','43473',NULL,NULL,NULL,1.0,NULL,NULL,NULL,NULL,NULL);
Insert or Replace INTO "tubing_lookup" VALUES (23,'NA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,17,30000,'l/min');
Insert or Replace INTO "tubing_lookup" VALUES (24,'NA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,17,30000,'mL/min');


Insert or Replace INTO "pump_lookup" VALUES (1,'Main pump','KR2i',0.0,600,'rpm','1',1);
Insert or Replace INTO "pump_lookup" VALUES (2,'Main pump','KMPi',0.0,650,'rpm','2',1);
Insert or Replace INTO "pump_lookup" VALUES (3,'Main pump','KrosFlo FS15',30.0,3000,'rpm','1',0);
Insert or Replace INTO "pump_lookup" VALUES (4,'Main pump','KrosFlo FS500',30.0,2400,'rpm','1',0);
Insert or Replace INTO "pump_lookup" VALUES (5,'Aux pump','KRJr',0.0,300,'rpm','0',1);
Insert or Replace INTO "pump_lookup" VALUES (6,'Aux pump','KR1',0.0,600,'rpm','0',1);
Insert or Replace INTO "pump_lookup" VALUES (7,'Aux pump','IP',0.0,650,'rpm','0',1);
Insert or Replace INTO "pump_lookup" VALUES (8,'Valve','KR2i_valve',NULL,NULL,NULL,NULL,1);
Insert or Replace INTO "pump_lookup" VALUES (9,'Valve','KMPi_valve',NULL,NULL,NULL,NULL,1);

Insert or Replace INTO "main_aux_pump_map" VALUES (1,'KR2i','KR1');
Insert or Replace INTO "main_aux_pump_map" VALUES (2,'KR2i','KRJr');
Insert or Replace INTO "main_aux_pump_map" VALUES (3,'KMPi','KR1');
Insert or Replace INTO "main_aux_pump_map" VALUES (4,'KMPi','KRJr');
Insert or Replace INTO "main_aux_pump_map" VALUES (5,'KMPi','IP');
Insert or Replace INTO "main_aux_pump_map" VALUES (6,'KrosFlo FS15','KR1');
Insert or Replace INTO "main_aux_pump_map" VALUES (7,'KrosFlo FS15','KRJr');
Insert or Replace INTO "main_aux_pump_map" VALUES (8,'KrosFlo FS500','KR1');
Insert or Replace INTO "main_aux_pump_map" VALUES (9,'KrosFlo FS500','KRJr');
Insert or Replace INTO "main_aux_pump_map" VALUES (10,'KrosFlo FS500','IP');

Insert or Replace INTO "role_master" VALUES (1,'Admin','Y');
Insert or Replace INTO "role_master" VALUES (2,'Auditor','Y');
Insert or Replace INTO "role_master" VALUES (3,'Manager','Y');
Insert or Replace INTO "role_master" VALUES (4,'Technician','Y');
Insert or Replace INTO "role_master" VALUES (5,'SuperAdmin','Y');


Insert or Replace INTO "ramp_up_time" VALUES (1,1);
Insert or Replace INTO "ramp_up_time" VALUES (2,2);
Insert or Replace INTO "ramp_up_time" VALUES (3,3);
Insert or Replace INTO "ramp_up_time" VALUES (4,4);
Insert or Replace INTO "ramp_up_time" VALUES (5,5);
Insert or Replace INTO "ramp_up_time" VALUES (6,6);
Insert or Replace INTO "ramp_up_time" VALUES (7,7);
Insert or Replace INTO "ramp_up_time" VALUES (8,8);
Insert or Replace INTO "ramp_up_time" VALUES (9,9);
Insert or Replace INTO "ramp_up_time" VALUES (10,10);
Insert or Replace INTO "ramp_up_time" VALUES (11,11);
Insert or Replace INTO "ramp_up_time" VALUES (12,12);
Insert or Replace INTO "ramp_up_time" VALUES (13,13);
Insert or Replace INTO "ramp_up_time" VALUES (14,14);
Insert or Replace INTO "ramp_up_time" VALUES (15,15);

Insert or Replace INTO "end_point_lookup" VALUES (1,'Concentration Factor','C','Y');
Insert or Replace INTO "end_point_lookup" VALUES (2,'Permeate Weight','Both','Y');
Insert or Replace INTO "end_point_lookup" VALUES (3,'UV','Both','Y');
Insert or Replace INTO "end_point_lookup" VALUES (4,'Diafiltration Volume','D','Y');
Insert or Replace INTO "end_point_lookup" VALUES (5,'Conductivity','D','Y');
Insert or Replace INTO "end_point_lookup" VALUES (6,'pH','Both','Y');
Insert or Replace INTO "end_point_lookup" VALUES (7,'Turbidity','Both','Y');
Insert or Replace INTO "recirc_flow_unit" VALUES (1,'per minute');
Insert or Replace INTO "recirc_flow_unit" VALUES (2,'per second');

Insert or Replace INTO "mode_lookup" VALUES (1,'C','Concentration Factor',0,1,'Permeate',NULL,'Y');
Insert or Replace INTO "mode_lookup" VALUES (2,'D','Diafiltration Volume',1,2,'Diafiltration 1','Permeate','Y');
Insert or Replace INTO "mode_lookup" VALUES (3,'C/D','Concentration Factor/Diafiltration Volume',1,2,'Diafiltration 1','Permeate','Y');
Insert or Replace INTO "mode_lookup" VALUES (4,'C/D/C','Concentration Factor/Diafiltration Volume/Concentration Factor',1,2,'Diafiltration 1','Permeate','Y');
Insert or Replace INTO "mode_lookup" VALUES (5,'C/D/D/C','Concentration Factor/Diafiltration Volume/Diafiltration Volume/Concentration Factor',2,2,'Diafiltration 1','Diafiltration 2','Y');
Insert or Replace INTO "mode_lookup" VALUES (6,'C/D/C/D','Concentration Factor/Diafiltration Volume/Concentration Factor/Diafiltration Volume',2,2,'Diafiltration 1','Diafiltration 2','Y');
Insert or Replace INTO "mode_lookup" VALUES (7,'CFC','Constant Feed Concentration',1,2,'Constant Feed','Permeate','Y');
Insert or Replace INTO "mode_lookup" VALUES (8,'CFC/D/C','Constant Feed Concentration/Diafiltration Volume/Concentration Factor',2,2,'Constant Feed','Diafiltration 1','Y');
Insert or Replace INTO "mode_lookup" VALUES (9,'TFDF Wizard','Concentration Factor/Diafiltration Volume/Concentration Factor',NULL,NULL,NULL,NULL,'N');
Insert or Replace INTO "mode_lookup" VALUES (10,'Vacuum','Vacuum',0,0,NULL,NULL,'Y');
Insert or Replace INTO "mode_lookup" VALUES (11,'NWP','Normalized Water Permeability',0,0,NULL,NULL,'Y');
Insert or Replace INTO "mode_lookup" VALUES (12,'Flux C','Flux C',0,0,NULL,NULL,'Y');
Insert or Replace INTO "mode_lookup" VALUES (13,'Flux CV','Flux CV',1,1,'Constant Feed',NULL,'Y');
Insert or Replace INTO "mode_lookup" VALUES (14,'Cleaning','Cleaning',0,1,'Permeate',NULL,'Y');
Insert or Replace INTO "mode_lookup" VALUES (15,'Flushing','Flushing',0,1,'Permeate',NULL,'Y');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1,'C02-E001-05-N','MicroKros','MICROKROS 20CM 1K MPES .5MM MLLXFLL 1/PK','1 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'9','27',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (2,'C02-E001-05-S','MicroKros','MICROKROS 20CM 1K MPES .5MM MLLXFLL 1/PK STERILE','1 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'9','27',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (3,'C02-E003-05-N','MicroKros','MICROKROS 20CM 3K MPES 0.5MM MLL X FLL 1/PK','3 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (4,'C02-E003-05-S','MicroKros','MICROKROS 20CM 3K MPES 0.5MM MLL X FLL 1/PK STERILE','3 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (5,'C02-E003-10-N','MicroKros','MICROKROS 20CM 3K MPES 1MM MLL X FLL 1/PK','3 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (6,'C02-E003-10-S','MicroKros','MICROKROS 20CM 3K MPES 1MM MLL X FLL 1/PK STERILE','3 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (7,'C02-E005-05-N','MicroKros','MICROKROS 20CM 5K MPES 0.5MM MLL X FLL 1/PK','5 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (8,'C02-E005-05-S','MicroKros','MICROKROS 20CM 5K MPES 0.5MM MLL X FLL 1/PK STERILE','5 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (9,'C02-E005-10-N','MicroKros','MICROKROS 20CM 5K MPES 1MM MLL X FLL 1/PK','5 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (10,'C02-E005-10-S','MicroKros','MICROKROS 20CM 5K MPES 1MM MLL X FLL 1/PK STERILE','5 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (11,'C02-E010-05-N','MicroKros','MICROKROS 20CM 10K MPES 0.5MM MLL X FLL 1/PK','10 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,26.0,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (12,'C02-E010-05-S','MicroKros','MICROKROS 20CM 10K MPES 0.5MM MLLXFLL 1/PK  STERILE','10 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,26.0,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (13,'C02-E010-10-N','MicroKros','MICROKROS 20CM 10K MPES 1MM MLL X FLL 1/PK','10 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (14,'C02-E010-10-S','MicroKros','MICROKROS 20CM 10K MPES 1MM MLL X FLL 1/PK STERILE','10 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (15,'C02-E030-05-N','MicroKros','MICROKROS 20CM 30K MPES 0.5MM MLL X FLL 1/PK','30 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (16,'C02-E030-05-S','MicroKros','MICROKROS 20CM 30K MPES 0.5MM MLL X FLL 1/PK STERILE','30 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (17,'C02-E030-10-N','MicroKros','MICROKROS 20CM 30K MPES 1MM MLL X FLL 1/PK','30 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (18,'C02-E030-10-S','MicroKros','MICROKROS 20CM 30K MPES 1MM MLL X FLL 1/PK STERILE','30 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (19,'C02-E050-05-N','MicroKros','MICROKROS 20CM 50K MPES 0.5MM MLL X FLL 1/PK','50 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (20,'C02-E050-05-S','MicroKros','MICROKROS 20CM 50K MPES 0.5MM MLL X FLL 1/PK STERILE','50 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (21,'C02-E050-10-N','MicroKros','MICROKROS 20CM 50K MPES 1MM MLL X FLL 1/PK','50 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (22,'C02-E050-10-S','MicroKros','MICROKROS 20CM 50K MPES 1MM MLL X FLL 1/PK STERILE','50 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (23,'C02-E070-05-N','MicroKros','MICROKROS 20CM 70kD MPES 0.5MM MLL X FLL 1/PK','70 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (24,'C02-E070-05-S','MicroKros','MICROKROS 20CM 70kD MPES 0.5MM MLL X FLL1/PK STERILE','70 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (25,'C02-E070-10-N','MicroKros','MICROKROS 20CM 70K MPES 1MM MLL X FLL 1/PK','70 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (26,'C02-E070-10-S','MicroKros','MICROKROS 20CM 70K MPES 1MM MLL X FLL 1/PK STERILE','70 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (27,'C02-E100-05-N','MicroKros','MICROKROS 20CM 100K MPES 0.5MM MLL X FLL 1/PK','100 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (28,'C02-E100-05-S','MicroKros','MICROKROS 20CM 100K MPES 0.5MM ML X FLL 1/PK STERILE','100 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (29,'C02-E100-10-N','MicroKros','MICROKROS 20CM 100K MPES 1MM MLL X FLL 1/PK','100 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (30,'C02-E100-10-S','MicroKros','MICROKROS 20CM100K MPES1MM MLL X FLL 1/PK STERILE','100 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (31,'C02-E20U-05-N','MicroKros','MICROKROS 20CM 0.2UM MPES 0.5MM MLL X FLL 1/PK','0.2 ?m','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (32,'C02-E20U-05-S','MicroKros','MICROKROS 20CM 0.2UM MPES 0.5MM MLL X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (33,'C02-E20U-10-N','MicroKros','MICROKROS 20CM .2UM MPES .5MM MLLXFLL 1/PK','0.2 ?m','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (34,'C02-E20U-10-S','MicroKros','MICROKROS 20CM .2UM MPES .5MM MLLXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (35,'C02-E300-05-N','MicroKros','MICROKROS 20CM 300K MPES 0.5MM MLL X FLL 1/PK','300 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (36,'C02-E300-05-S','MicroKros','MICROKROS 20CM 300K MPES 0.5MM MLL X FLL 1/PK STER','300 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (37,'C02-E300-10-N','MicroKros','MICROKROS 20CM 300K MPES 1MM MLL X FLL 1/PK','300 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (38,'C02-E300-10-S','MicroKros','MICROKROS 20CM 300K MPES 1MM MLL X FLL 1/PK STERILE','300 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (39,'C02-E500-05-N','MicroKros','MICROKROS 20CM 500K MPES 0.5MM MLL X FLL 1/PK','500 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (40,'C02-E500-05-S','MicroKros','MICROKROS 20CM 500K MPES 0.5MM MLLXFLL 1/PK STERILE','500 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (41,'C02-E500-10-N','MicroKros','MICROKROS 20CM 500K MPES 1MM MLL X FLL 1/PK','500 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (42,'C02-E500-10-S','MicroKros','MICROKROS 20CM 500K MPES 1MM MLL X FLL 1/PK STERILE','500 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (43,'C02-E65U-07-N','MicroKros','MICROKROS 20CM .65UM MPES .75MM MLLXMLL 1/PK','0.65 ?m','mPES',0.75,23,20.0,3,0.108,46.2,43.0,38.8,15,0.26,0.64,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (44,'C02-E65U-07-S','MicroKros','MICROKROS 20CM .65UM MPES .75MM MLLXMLL STERILE 1/PK','0.65 ?m','mPES',0.75,23,20.0,3,0.108,46.2,43.0,38.8,15,0.26,0.64,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (45,'C02-E750-05-N','MicroKros','MICROKROS 20CM 750K MPES 0.5MM MLL X FLL 1/PK','750 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (46,'C02-E750-05-S','MicroKros','MICROKROS 20CM 750K MPES 0.5MM MLL X FLL 1/PK STERILE','750 kD','mPES',0.5,23,20.0,6,0.108,31.5,29.5,27.5,20,0.24,0.67,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (47,'C02-E750-10-N','MicroKros','MICROKROS 20CM 750KD MPES 1MM MLLXFLL 1/PK','750 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (48,'C02-E750-10-S','MicroKros','MICROKROS 20CM 750KD MPES 1MM MLLXFLL STERILE 1/PK','750 kD','mPES',1.0,23,20.0,2,0.108,57.0,52.5,48.0,13,0.31,0.64,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (49,'C02-M10U-06-N','MicroKros','MICROKROS 20CM 0.1 UM ME 0.63MM MLL X FLL 1/PK','0.1 ?m','ME',0.63,23,20.0,5,0.108,37.0,34.0,31.0,20,0.31,0.61,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (50,'C02-M10U-06-S','MicroKros','MICROKROS 20CM 0.1 UM ME 0.63MM MLLX FLL1/PK STERILE','0.1 ?m','ME',0.63,23,20.0,5,0.108,37.0,34.0,31.0,20,0.31,0.61,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (51,'C02-M20U-06-N','MicroKros','MICROKROS 20CM 0.2 UM ME 0.63MM MLL X FLL 1/PK','0.2 ?m','ME',0.63,23,20.0,5,0.108,37.0,34.0,31.0,20,0.31,0.61,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (52,'C02-M20U-06-S','MicroKros','MICROKROS 20CM0.2 UM ME0.63MM MLL X FLL 1/PK STERILE','0.2 ?m','ME',0.63,23,20.0,5,0.108,37.0,34.0,31.0,20,0.31,0.61,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (53,'C02-M20U-10-N','MicroKros','MICROKROS 20CM 0.2 UM ME 1MM MLL X FLL 1/PK','0.2 ?m','ME',1.0,23,20.0,2,0.108,54.0,50.0,46.0,13,0.31,0.69,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (54,'C02-M20U-10-S','MicroKros','MICROKROS 20CM 0.2  UM ME 1MM MLL X FLL 1/PK STERILE','0.2 ?m','ME',1.0,23,20.0,2,0.108,54.0,50.0,46.0,13,0.31,0.69,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (55,'C02-P20U-05-N','MicroKros','MICROKROS 20CM 0.2 UM PES 0.5MM MLL X FLL 1/PK','0.2 ?m','PES',0.5,23,20.0,9,0.108,26.6,25.6,24.6,28,0.35,0.6,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (56,'C02-P20U-05-S','MicroKros','MICROKROS 20CM 0.2 UMPES 0.5MM MLL XFLL 1/PK STERILE','0.2 ?m','PES',0.5,23,20.0,9,0.108,26.6,25.6,24.6,28,0.35,0.6,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (57,'C02-P20U-10-N','MicroKros','MICROKROS 20CM 0.2 UM PES 1MM MLL X FLL 1/PK','0.2 ?m','PES',1.0,23,20.0,2,0.108,53.1,51.2,49.2,13,0.31,0.67,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (58,'C02-P20U-10-S','MicroKros','MICROKROS 20CM 0.2 UM PES 1MM MLLXFLL 1/PK STERILE','0.2 ?m','PES',1.0,23,20.0,2,0.108,53.1,51.2,49.2,13,0.31,0.67,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (59,'C02-S010-05-N','MicroKros','MICROKROS 20CM 10KD PS 0.5MM MLL X FLL 1/PK','10 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (60,'C02-S010-05-P','MicroKros','MICROKROS 20CM 10KD PS 0.5MM MLL X FLL 1/PK WET','10 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (61,'C02-S010-05-S','MicroKros','MICROKROS 20CM 10KD PS 0.5MM MLL X FLL 1/PK STERILE','10 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (62,'C02-S050-05-N','MicroKros','MICROKROS 20CM 50KD PS 0.5MM MLL X FLL 1/PK','50 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (63,'C02-S050-05-P','MicroKros','MICROKROS 20CM 50KD PS 0.5MM MLL X FLL 1/PK WET','50 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (64,'C02-S050-05-S','MicroKros','MICROKROS 20CM 50KD PS 0.5MM MLL X FLL 1/PK STERILE','50 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (65,'C02-S05U-05-N','MicroKros','MICROKROS 20CM 0.05 UM PS 0.5MM MLL X FLL 1/PK','0.05 ?m','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (66,'C02-S05U-05-P','MicroKros','MICROKROS 20CM 0.05 UM PS 0.5MM MLL X FLL 1/PK WET','0.05 ?m','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (67,'C02-S05U-05-S','MicroKros','MICROKROS 20CM 0.05UM PS 0.5MM MLL X FLL1/PK STERILE','0.05 ?m','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (68,'C02-S500-05-N','MicroKros','MICROKROS 20CM 500KD PS 0.5MM MLL X FLL 1/PK','500 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (69,'C02-S500-05-P','MicroKros','MICROKROS 20CM 500KD PS 0.5MM MLL X FLL 1/PK WET','500 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (70,'C02-S500-05-S','MicroKros','MICROKROS 20CM 500KD PS 0.5MM MLL X FLL 1/PK STERILE','500 kD','PS',0.5,23,20.0,9,0.108,26.8,23.6,20.5,28,0.35,0.69,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (71,'C04-E001-05-N','MicroKros','MICROKROS 41.5CM 1K MPES .5MM MLLXFLL 1/PK','1 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'9','27',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (72,'C04-E001-05-S','MicroKros','MICROKROS 41.5CM 1K MPES .5MM MLLXFLL 1/PK STERILE','1 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'9','27',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (73,'C04-E003-05-N','MicroKros','MICROKROS 41.5CM 3K MPES 0.5MM MLL X FLL 1/PK','3 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (74,'C04-E003-05-S','MicroKros','MICROKROS 41.5CM 3K MPES 0.5MM MLL X FLL 1/PK STERILE','3 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (75,'C04-E003-10-N','MicroKros','MICROKROS 41.5CM 3K MPES 1MM MLL X FLL 1/PK','3 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (76,'C04-E003-10-S','MicroKros','MICROKROS 41.5CM 3K MPES 1MM MLL X FLL 1/PK STERILE','3 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (77,'C04-E005-05-N','MicroKros','MICROKROS 41.5CM 5K MPES 0.5MM MLL X FLL 1/PK','5 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (78,'C04-E005-05-S','MicroKros','MICROKROS 41.5CM 5K MPES 0.5MM MLL X FLL 1/PK STERILE','5 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (79,'C04-E005-10-N','MicroKros','MICROKROS 41.5CM 5K MPES 1MM MLL X FLL 1/PK','5 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (80,'C04-E005-10-S','MicroKros','MICROKROS 41.5CM 5K MPES 1MM MLL X FLL 1/PK STERILE','5 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (81,'C04-E010-05-N','MicroKros','MICROKROS 41.5CM 10K MPES 0.5MM MLL X FLL 1/PK','10 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,26.0,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (82,'C04-E010-05-S','MicroKros','MICROKROS 41.5CM 10K MPES 0.5MM MLL X FLL 1/PK STERILE','10 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,26.0,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (83,'C04-E010-10-N','MicroKros','MICROKROS 41.5CM 10K MPES 1MM MLL X FLL 1/PK','10 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (84,'C04-E010-10-S','MicroKros','MICROKROS 41.5CM 10K MPES 1MM MLL X FLL 1/PK STERILE','10 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (85,'C04-E030-05-N','MicroKros','MICROKROS 41.5CM 30K MPES 0.5MM MLL X FLL 1/PK','30 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (86,'C04-E030-05-S','MicroKros','MICROKROS 41.5CM 30K MPES 0.5MM MLL X FLL 1/PK STERILE','30 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (87,'C04-E030-10-N','MicroKros','MICROKROS 41.5CM 30K MPES 1MM MLL X FLL 1/PK','30 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (88,'C04-E030-10-S','MicroKros','MICROKROS 41.5CM 30K MPES 1MM MLL X FLL 1/PK STERILE','30 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (89,'C04-E050-05-N','MicroKros','MICROKROS 41.5CM 50K MPES 0.5MM MLL X FLL 1/PK','50 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (90,'C04-E050-05-S','MicroKros','MICROKROS 41.5CM 50K MPES 0.5MM MLL X FLL 1/PK STERILE','50 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (91,'C04-E050-10-N','MicroKros','MICROKROS 41.5CM 50K MPES 1MM MLL X FLL 1/PK','50 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (92,'C04-E050-10-S','MicroKros','MICROKROS 41.5CM 50K MPES 1MM MLL X FLL 1/PK STERILE','50 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (93,'C04-E070-05-N','MicroKros','MICROKROS 41.5CM 70kD MPES 0.5MM MLL X FLL 1/PK','70 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (94,'C04-E070-05-S','MicroKros','MICROKROS 41.5CM 70kD MPES 0.5MM MLL X FLL 1/PK STERILE','70 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (95,'C04-E070-10-N','MicroKros','MICROKROS 41.5CM 70K MPES 1MM MLL X FLL 1/PK','70 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (96,'C04-E070-10-S','MicroKros','MICROKROS 41.5CM 70K MPES 1MM MLL X FLL 1/PK IRR','70 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (97,'C04-E100-05-N','MicroKros','MICROKROS 41.5CM 100K MPES 0.5MM MLL X FLL 1/PK','100 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (98,'C04-E100-05-S','MicroKros','MICROKROS 41.5CM 100K MPES 0.5MM MLL X FLL 1/PK STERILE','100 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (99,'C04-E100-10-N','MicroKros','MICROKROS 41.5CM 100K MPES 1MM MLL X FLL 1/PK','100 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (100,'C04-E100-10-S','MicroKros','MICROKROS 41.5CM 100K MPES 1MM MLL X FLL 1/PK STERILE','100 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (101,'C04-E20U-05-N','MicroKros','MICROKROS 41.5CM 0.2UM MPES 0.5MM MLL X FLL 1/PK','0.2 ?m','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (102,'C04-E20U-05-S','MicroKros','MICROKROS 41.5CM 0.2UM MPES 0.5MM MLL X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (103,'C04-E20U-10-N','MicroKros','MICROKROS 41.5CM .2UM MPES 1MM MLLXFLL 1/PK','0.2 ?m','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (104,'C04-E20U-10-S','MicroKros','MICROKROS 41.5CM .2UM MPES 1MM MLLXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (105,'C04-E300-05-N','MicroKros','MICROKROS 41.5CM 300K MPES 0.5MM MLL X FLL 1/PK','300 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (106,'C04-E300-05-S','MicroKros','MICROKROS 41.5CM 300K MPES 0.5MM MLL X FLL 1/PK STERILE','300 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (107,'C04-E300-10-N','MicroKros','MICROKROS 41.5CM 300K MPES 1MM MLL X FLL 1/PK','300 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (108,'C04-E300-10-S','MicroKros','MICROKROS 41.5CM 300K MPES 1MM MLL X FLL 1/PK STERILE','300 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (109,'C04-E500-05-N','MicroKros','MICROKROS 41.5CM 500K MPES 0.5MM MLL X FLL 1/PK','500 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (110,'C04-E500-05-S','MicroKros','MICROKROS 41.5CM 500K MPES 0.5MM MLL X FLL 1/PK STERILE','500 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (111,'C04-E500-10-N','MicroKros','MICROKROS 41.5CM 500K MPES 1MM MLL X FLL 1/PK','500 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (112,'C04-E500-10-S','MicroKros','MICROKROS 41.5CM 500K MPES 1MM MLL X FLL 1/PK STERILE','500 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (113,'C04-E65U-07-N','MicroKros','MICROKROS 41.5CM .65UM MPES .75MM MLLXMLL 1/PK','0.65 ?m','mPES',0.75,45,41.5,3,0.108,46.2,43.0,38.8,30,0.55,1.32,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (114,'C04-E65U-07-S','MicroKros','MICROKROS 41.5CM .65UM MPES .75MM MLLXMLL STERILE 1/PK','0.65 ?m','mPES',0.75,45,41.5,3,0.108,46.2,43.0,38.8,30,0.55,1.32,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (115,'C04-E750-05-N','MicroKros','MICROKROS 41.5CM 750K MPES 0.5MM MLL X FLL 1/PK','750 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (116,'C04-E750-05-S','MicroKros','MICROKROS 41.5CM 750K MPES 0.5MM MLL X FLL 1/PK STERILE','750 kD','mPES',0.5,45,41.5,6,0.108,31.5,29.5,27.5,40,0.49,1.39,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (117,'C04-E750-10-N','MicroKros','MICROKROS 41.5CM 750KD MPES 1MM MLLXFLL 1/PK','750 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (118,'C04-E750-10-S','MicroKros','MICROKROS 41.5CM 750KD MPES 1MM MLLXFLL STERILE 1/PK','750 kD','mPES',1.0,45,41.5,2,0.108,57.0,52.5,48.0,26,0.65,1.33,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (119,'C04-M10U-06-N','MicroKros','MICROKROS 41.5CM 0.1 UM ME 0.63MM MLL X FLL 1/PK','0.1 ?m','ME',0.63,45,41.5,5,0.108,37.0,34.0,31.0,41,0.65,1.27,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (120,'C04-M10U-06-S','MicroKros','MICROKROS 41.5CM 0.1 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.1 ?m','ME',0.63,45,41.5,5,0.108,37.0,34.0,31.0,41,0.65,1.27,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (121,'C04-M20U-06-N','MicroKros','MICROKROS 41.5CM 0.2 UM ME 0.63MM MLL X FLL 1/PK','0.2 ?m','ME',0.63,45,41.5,5,0.108,37.0,34.0,31.0,41,0.65,1.27,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (122,'C04-M20U-06-S','MicroKros','MICROKROS 41.5CM 0.2 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.2 ?m','ME',0.63,45,41.5,5,0.108,37.0,34.0,31.0,41,0.65,1.27,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (123,'C04-M20U-10-N','MicroKros','MICROKROS 41.5CM 0.2 UM ME 1MM MLL X FLL 1/PK','0.2 ?m','ME',1.0,45,41.5,2,0.108,54.0,50.0,46.0,26,0.65,1.43,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (124,'C04-M20U-10-S','MicroKros','MICROKROS 41.5CM 0.2 UM ME 1MM MLL X FLL 1/PK STERILE','0.2 ?m','ME',1.0,45,41.5,2,0.108,54.0,50.0,46.0,26,0.65,1.43,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (125,'C04-P20U-05-N','MicroKros','MICROKROS 41.5CM 0.2 UM PES 0.5MM MLL X FLL 1/PK','0.2 ?m','PES',0.5,45,41.5,9,0.108,26.6,25.6,24.6,59,0.73,1.25,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (126,'C04-P20U-05-S','MicroKros','MICROKROS 41.5CM 0.2 UM PES 0.5MM MLL X FLL 1/PK STERILE','0.2 ?m','PES',0.5,45,41.5,9,0.108,26.6,25.6,24.6,59,0.73,1.25,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (127,'C04-P20U-10-N','MicroKros','MICROKROS 41.5CM 0.2 UM PES 1MM MLL X FLL 1/PK','0.2 ?m','PES',1.0,45,41.5,2,0.108,53.1,51.2,49.2,26,0.65,1.38,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (128,'C04-P20U-10-S','MicroKros','MICROKROS 41.5CM 0.2 UM PES 1MM MLL X FLL 1/PK STERILE','0.2 ?m','PES',1.0,45,41.5,2,0.108,53.1,51.2,49.2,26,0.65,1.38,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (129,'C04-S010-05-N','MicroKros','MICROKROS 41.5CM 10KD PS 0.5MM MLL X FLL 1/PK','10 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (130,'C04-S010-05-P','MicroKros','MICROKROS 41.5CM 10KD PS 0.5MM MLL X FLL 1/PK WET','10 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (131,'C04-S010-05-S','MicroKros','MICROKROS 41.5CM 10KD PS 0.5MM MLL X FLL 1/PK STERILE','10 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (132,'C04-S050-05-N','MicroKros','MICROKROS 41.5CM 50KD PS 0.5MM MLL X FLL 1/PK','50 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (133,'C04-S050-05-P','MicroKros','MICROKROS 41.5CM 50KD PS 0.5MM MLL X FLL 1/PK WET','50 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (134,'C04-S050-05-S','MicroKros','MICROKROS 41.5CM 50KD PS 0.5MM MLL X FLL 1/PK STERILE','50 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (135,'C04-S05U-05-N','MicroKros','MICROKROS 41.5CM 0.05 UM PS 0.5MM MLL X FLL 1/PK','0.05 ?m','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (136,'C04-S05U-05-P','MicroKros','MICROKROS 41.5CM 0.05 UM PS 0.5MM MLL X FLL 1/PK WET','0.05 ?m','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (137,'C04-S05U-05-S','MicroKros','MICROKROS 41.5CM 0.05 UM PS 0.5MM MLL X FLL 1/PK STERILE','0.05 ?m','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (138,'C04-S500-05-N','MicroKros','MICROKROS 41.5CM 500KD PS 0.5MM MLL X FLL 1/PK','500 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (139,'C04-S500-05-P','MicroKros','MICROKROS 41.5CM 500KD PS 0.5MM MLL X FLL 1/PK WET','500 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (140,'C04-S500-05-S','MicroKros','MICROKROS 41.5CM 500KD PS 0.5MM MLL X FLL 1/PK STERILE','500 kD','PS',0.5,45,41.5,9,0.108,26.8,23.6,20.5,59,0.73,1.43,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (141,'C06-E001-05-N','MicroKros','MICROKROS 65CM 1K MPES .5MM MLLXFLL 1/PK','1 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'9','27',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (142,'C06-E001-05-S','MicroKros','MICROKROS 65CM 1K MPES .5MM MLLXFLL 1/PK STERILE','1 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'9','27',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (143,'C06-E003-05-N','MicroKros','MICROKROS 65CM 3K MPES 0.5MM MLL X FLL 1/PK','3 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (144,'C06-E003-05-S','MicroKros','MICROKROS 65CM 3K MPES 0.5MM MLL X FLL 1/PK STERILE','3 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (145,'C06-E003-10-N','MicroKros','MICROKROS 65CM 3K MPES 1MM MLL X FLL 1/PK','3 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (146,'C06-E003-10-S','MicroKros','MICROKROS 65CM 3K MPES 1MM MLL X FLL 1/PK STERILE','3 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (147,'C06-E005-05-N','MicroKros','MICROKROS 65CM 5K MPES 0.5MM MLL X FLL 1/PK','5 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (148,'C06-E005-05-S','MicroKros','MICROKROS 65CM 5K MPES 0.5MM MLL X FLL 1/PK STERILE','5 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (149,'C06-E005-10-N','MicroKros','MICROKROS 65CM 5K MPES 1MM MLL X FLL 1/PK','5 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (150,'C06-E005-10-S','MicroKros','MICROKROS 65CM 5K MPES 1MM MLL X FLL 1/PK STERILE','5 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (151,'C06-E010-05-N','MicroKros','MICROKROS 65CM 10K MPES 0.5MM MLL X FLL 1/PK','10 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,26.0,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (152,'C06-E010-05-S','MicroKros','MICROKROS 65CM 10K MPES 0.5MM MLL X FLL 1/PK STERILEIRR','10 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,26.0,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (153,'C06-E010-10-N','MicroKros','MICROKROS 65CM 10K MPES 1MM MLL X FLL 1/PK','10 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (154,'C06-E010-10-S','MicroKros','MICROKROS 65CM 10K MPES 1MM MLL X FLL 1/PK STERILE','10 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (155,'C06-E030-05-N','MicroKros','MICROKROS 65CM 30K MPES 0.5MM MLL X FLL 1/PK','30 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (156,'C06-E030-05-S','MicroKros','MICROKROS 65CM 30K MPES 0.5MM MLL X FLL 1/PK STERILE','30 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (157,'C06-E030-10-N','MicroKros','MICROKROS 65CM 30K MPES 1MM MLL X FLL 1/PK','30 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (158,'C06-E030-10-S','MicroKros','MICROKROS 65CM 30K MPES 1MM MLL X FLL 1/PK STERILE','30 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (159,'C06-E050-05-N','MicroKros','MICROKROS 65CM 50K MPES 0.5MM MLL X FLL 1/PK','50 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (160,'C06-E050-05-S','MicroKros','MICROKROS 65CM 50K MPES 0.5MM MLL X FLL 1/PK STERILE','50 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (161,'C06-E050-10-N','MicroKros','MICROKROS 65CM 50K MPES 1MM MLL X FLL 1/PK','50 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (162,'C06-E050-10-S','MicroKros','MICROKROS 65CM 50K MPES 1MM MLL X FLL 1/PK STERILE','50 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (163,'C06-E070-05-N','MicroKros','MICROKROS 65CM 70kD MPES 0.5MM MLL X FLL 1/PK','70 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (164,'C06-E070-05-S','MicroKros','MICROKROS 65CM 70kD MPES 0.5MM MLL X FLL 1/PK STERILE','70 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (165,'C06-E070-10-N','MicroKros','MICROKROS 65CM 70K MPES 1MM MLL X FLL 1/PK','70 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (166,'C06-E070-10-S','MicroKros','MICROKROS 65CM 70K MPES 1MM MLL X FLL 1/PK STERILE','70 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (167,'C06-E100-05-N','MicroKros','MICROKROS 65CM 100K MPES 0.5MM MLL X FLL 1/PK','100 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (168,'C06-E100-05-S','MicroKros','MICROKROS 65CM 100K MPES 0.5MM MLL X FLL 1/PK STERILE','100 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (169,'C06-E100-10-N','MicroKros','MICROKROS 65CM 100K MPES 1MM MLL X FLL 1/PK','100 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (170,'C06-E100-10-S','MicroKros','MICROKROS 65CM 100K MPES 1MM MLL X FLL 1/PK STERILE','100 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (171,'C06-E20U-05-N','MicroKros','MICROKROS 65CM 0.2UM MPES 0.5MM MLL X FLL 1/PK','0.2 ?m','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (172,'C06-E20U-05-S','MicroKros','MICROKROS 65CM 0.2UM MPES 0.5MM MLL X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (173,'C06-E20U-10-N','MicroKros','MICROKROS 65CM .2UM MPES 1MM MLLXFLL 1/PK','0.2 ?m','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (174,'C06-E20U-10-S','MicroKros','MICROKROS 65CM .2UM MPES 1MM MLLXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (175,'C06-E300-05-N','MicroKros','MICROKROS 65CM 300K MPES 0.5MM MLL X FLL 1/PK','300 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (176,'C06-E300-05-S','MicroKros','MICROKROS 65CM 300K MPES 0.5MM MLL X FLL 1/PK STERILE','300 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (177,'C06-E300-10-N','MicroKros','MICROKROS 65CM 300K MPES 1MM MLL X FLL 1/PK','300 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (178,'C06-E300-10-S','MicroKros','MICROKROS 65CM 300K MPES 1MM MLL X FLL 1/PK STERILE','300 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (179,'C06-E500-05-N','MicroKros','MICROKROS 65CM 500K MPES 0.5MM MLL X FLL 1/PK','500 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (180,'C06-E500-05-S','MicroKros','MICROKROS 65CM 500K MPES 0.5MM MLL X FLL 1/PK STERILE','500 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (181,'C06-E500-10-N','MicroKros','MICROKROS 65CM 500K MPES 1MM MLL X FLL 1/PK','500 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (182,'C06-E500-10-S','MicroKros','MICROKROS 65CM 500K MPES 1MM MLL X FLL 1/PK IRR','500 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (183,'C06-E65U-07-N','MicroKros','MICROKROS 65CM .65UM MPES .75MM MLLXMLL 1/PK','0.65 ?m','mPES',0.75,68,65.0,3,0.108,46.2,43.0,38.8,45,0.86,2.07,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (184,'C06-E65U-07-S','MicroKros','MICROKROS 65CM .65UM MPES .75MM MLLXMLL STERILE 1/PK','0.65 ?m','mPES',0.75,68,65.0,3,0.108,46.2,43.0,38.8,45,0.86,2.07,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (185,'C06-E750-05-N','MicroKros','MICROKROS 65CM 750K MPES 0.5MM MLL X FLL 1/PK','750 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (186,'C06-E750-05-S','MicroKros','MICROKROS 65CM 750K MPES 0.5MM MLL X FLL 1/PK STERILE','750 kD','mPES',0.5,68,65.0,6,0.108,31.5,29.5,27.5,60,0.77,2.17,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (187,'C06-E750-10-N','MicroKros','MICROKROS 65CM 750KD MPES 1MM MLLXFLL 1/PK','750 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (188,'C06-E750-10-S','MicroKros','MICROKROS 65CM 750KD MPES 1MM MLLXFLL STERILE 1/PK','750 kD','mPES',1.0,68,65.0,2,0.108,57.0,52.5,48.0,41,1.02,2.08,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (189,'C06-M10U-06-N','MicroKros','MICROKROS 65CM 0.1 UM ME 0.63MM MLL X FLL 1/PK','0.1 ?m','ME',0.63,68,65.0,5,0.108,37.0,34.0,31.0,65,1.01,2.0,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (190,'C06-M10U-06-S','MicroKros','MICROKROS 65CM 0.1 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.1 ?m','ME',0.63,68,65.0,5,0.108,37.0,34.0,31.0,65,1.01,2.0,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (191,'C06-M20U-06-N','MicroKros','MICROKROS 65CM 0.2 UM ME 0.63MM MLL X FLL 1/PK','0.2 ?m','ME',0.63,68,65.0,5,0.108,37.0,34.0,31.0,65,1.01,2.0,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (192,'C06-M20U-06-S','MicroKros','MICROKROS 65CM 0.2 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.2 ?m','ME',0.63,68,65.0,5,0.108,37.0,34.0,31.0,65,1.01,2.0,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (193,'C06-M20U-10-N','MicroKros','MICROKROS 65CM 0.2 UM ME 1MM MLL X FLL 1/PK','0.2 ?m','ME',1.0,68,65.0,2,0.108,54.0,50.0,46.0,41,1.02,2.25,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (194,'C06-M20U-10-S','MicroKros','MICROKROS 65CM 0.2 UM ME 1MM MLL X FLL 1/PK STERILE','0.2 ?m','ME',1.0,68,65.0,2,0.108,54.0,50.0,46.0,41,1.02,2.25,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (195,'C06-P20U-05-N','MicroKros','MICROKROS 65CM 0.2 UM PES 0.5MM MLL X FLL 1/PK','0.2 ?m','PES',0.5,68,65.0,9,0.108,26.6,25.6,24.6,92,1.15,1.96,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (196,'C06-P20U-05-S','MicroKros','MICROKROS 65CM 0.2 UM PES 0.5MM MLL X FLL 1/PK STERILE','0.2 ?m','PES',0.5,68,65.0,9,0.108,26.6,25.6,24.6,92,1.15,1.96,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (197,'C06-P20U-10-N','MicroKros','MICROKROS 65CM 0.2 UM PES 1MM MLL X FLL 1/PK','0.2 ?m','PES',1.0,68,65.0,2,0.108,53.1,51.2,49.2,41,1.02,2.17,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (198,'C06-P20U-10-S','MicroKros','MICROKROS 65CM 0.2 UM PES 1MM MLL X FLL 1/PK STERILE','0.2 ?m','PES',1.0,68,65.0,2,0.108,53.1,51.2,49.2,41,1.02,2.17,'#VALUE!','#VALUE!',14,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (199,'C06-S010-05-N','MicroKros','MICROKROS 65CM 10KD PS 0.5MM MLL X FLL 1/PK','10 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (200,'C06-S010-05-P','MicroKros','MICROKROS 65CM 10KD PS 0.5MM MLL X FLL 1/PK WET','10 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (201,'C06-S010-05-S','MicroKros','MICROKROS 65CM 10KD PS 0.5MM MLL X FLL 1/PK STERILE','10 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (202,'C06-S050-05-N','MicroKros','MICROKROS 65CM 50KD PS 0.5MM MLL X FLL 1/PK','50 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (203,'C06-S050-05-P','MicroKros','MICROKROS 65CM 50KD PS 0.5MM MLL X FLL 1/PK WET','50 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (204,'C06-S050-05-S','MicroKros','MICROKROS 65CM 50KD PS 0.5MM MLL X FLL 1/PK STERILE','50 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (205,'C06-S05U-05-N','MicroKros','MICROKROS 65CM 0.05 UM PS 0.5MM MLL X FLL 1/PK','0.05 ?m','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (206,'C06-S05U-05-P','MicroKros','MICROKROS 65CM 0.05 UM PS 0.5MM MLL X FLL 1/PK WET','0.05 ?m','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (207,'C06-S05U-05-S','MicroKros','MICROKROS 65CM 0.05 UM PS 0.5MM MLL X FLL 1/PK STERILE','0.05 ?m','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (208,'C06-S500-05-N','MicroKros','MICROKROS 65CM 500KD PS 0.5MM MLL X FLL 1/PK','500 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (209,'C06-S500-05-P','MicroKros','MICROKROS 65CM 500KD PS 0.5MM MLL X FLL 1/PK WET','500 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (210,'C06-S500-05-S','MicroKros','MICROKROS 65CM 500KD PS 0.5MM MLL X FLL 1/PK STERILE','500 kD','PS',0.5,68,65.0,9,0.108,26.8,23.6,20.5,92,1.15,2.24,'#VALUE!','#VALUE!',13,14,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (211,'D02-E001-05-N','MidiKros','MIDIKROS 20CM 1K MPES .5MM FLLXFLL 1/PK','1 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (212,'D02-E001-05-S','MidiKros','MIDIKROS 20CM 1K MPES .5MM FLLXFLL 1/PK STERILE','1 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (213,'D02-E003-05-N','MidiKros','MIDIKROS  20CM 3K MPES 0.5MM FLL X FLL 1/PK','3 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (214,'D02-E003-05-S','MidiKros','MIDIKROS  20CM 3K MPES 0.5MM FLL X FLL 1/PK STERILE','3 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (215,'D02-E003-10-N','MidiKros','MIDIKROS 20CM 3K MPES 1MM FLL X FLL 1/PK','3 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (216,'D02-E003-10-S','MidiKros','MIDIKROS 20CM 3K MPES 1MM FLL X FLL 1/PK STERILE','3 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (217,'D02-E005-05-N','MidiKros','MIDIKROS 20CM 5K MPES 0.5MM FLL X FLL 1/PK','5 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (218,'D02-E005-05-S','MidiKros','MIDIKROS 20CM 5K MPES 0.5MM FLL X FLL 1/PK STERILE','5 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (219,'D02-E005-10-N','MidiKros','MIDIKROS 20CM 5K MPES 1MM FLL X FLL 1/PK','5 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (220,'D02-E005-10-S','MidiKros','MIDIKROS 20CM 5K MPES 1MM FLL X FLL 1/PK STERILE','5 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (221,'D02-E010-05-N','MidiKros','MIDIKROS 20CM 10K MPES 0.5MM FLL X FLL 1/PK','10 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,26.0,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (222,'D02-E010-05-S','MidiKros','MIDIKROS 20CM 10K MPES 0.5MM FLL X FLL 1/PK STERILE','10 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,26.0,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (223,'D02-E010-10-N','MidiKros','MIDIKROS  20CM 10K MPES 1MM FLL X FLL 1/PK','10 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (224,'D02-E010-10-S','MidiKros','MIDIKROS  20CM 10K MPES 1MM FLL X FLL 1/PK STERILE','10 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (225,'D02-E030-05-N','MidiKros','MIDIKROS 20CM 30K MPES 0.5MM FLL X FLL 1/PK','30 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (226,'D02-E030-05-S','MidiKros','MIDIKROS 20CM 30K MPES 0.5MM FLL X FLL 1/PK STERILE','30 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (227,'D02-E030-10-N','MidiKros','MIDIKROS  20CM 30K MPES 1MM FLL X FLL 1/PK','30 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (228,'D02-E030-10-S','MidiKros','MIDIKROS  20CM 30K MPES 1MM FLL X FLL 1/PK STERILE','30 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (229,'D02-E050-05-N','MidiKros','MIDIKROS 20CM 50K MPES 0.5MM FLL X FLL 1/PK','50 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (230,'D02-E050-05-S','MidiKros','MIDIKROS 20CM 50K MPES 0.5MM FLL X FLL 1/PK STERILE','50 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (231,'D02-E050-10-N','MidiKros','MIDIKROS  20CM 50K MPES 1MM FLL X FLL 1/PK','50 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (232,'D02-E050-10-S','MidiKros','MIDIKROS  20CM 50K MPES 1MM FLL X FLL 1/PK STERILE','50 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (233,'D02-E070-05-N','MidiKros','MIDIKROS 20CM 70K MPES 0.5MM FLL X FLL 1/PK','70 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (234,'D02-E070-05-S','MidiKros','MIDIKROS 20CM 70K MPES 0.5MM FLL X FLL 1/PK STERILE','70 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (235,'D02-E070-10-N','MidiKros','MIDIKROS  20CM 70K MPES 1MM FLL X FLL 1/PK','70 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (236,'D02-E070-10-S','MidiKros','MIDIKROS  20CM 70K MPES 1MM FLL X FLL 1/PK STERILE','70 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (237,'D02-E100-05-N','MidiKros','MIDIKROS 20CM 100K MPES 0.5MM FLL X FLL 1/PK','100 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (238,'D02-E100-05-S','MidiKros','MIDIKROS 20CM 100K MPES 0.5MM FLL X FLL 1/PK STERILE','100 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (239,'D02-E100-10-N','MidiKros','MIDIKROS  20CM 100K MPES 1MM FLL X FLL 1/PK','100 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (240,'D02-E100-10-S','MidiKros','MIDIKROS  20CM 100K MPES 1MM FLL X FLL 1/PK STERILE','100 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (241,'D02-E20U-05-N','MidiKros','MIDIKROS 20CM 0.2UM MPES 0.5MM FLL X FLL 1/PK','0.2 ?m','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (242,'D02-E20U-05-S','MidiKros','MIDIKROS 20CM 0.2UM MPES 0.5MM FLL X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (243,'D02-E20U-10-N','MidiKros','MIDIKROS 20CM .2UM MPES 1MM FLLXFLL 1/PK','0.2 ?m','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (244,'D02-E20U-10-S','MidiKros','MIDIKROS 20CM .2UM MPES 1MM FLLXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (245,'D02-E300-05-N','MidiKros','MIDIKROS  20CM 300K MPES 0.5MM FLL X FLL 1/PK','300 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (246,'D02-E300-05-S','MidiKros','MIDIKROS 20CM 300K MPES 0.5MM FLLXFLL 1/PK STERILE','300 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (247,'D02-E300-10-N','MidiKros','MIDIKROS  20CM 300K MPES 1MM FLL X FLL 1/PK','300 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (248,'D02-E300-10-S','MidiKros','MIDIKROS  20CM 300K MPES 1MM FLL X FLL 1/PK STERILE','300 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (249,'D02-E500-05-N','MidiKros','MIDIKROS 20CM 500K MPES 0.5MM FLL X FLL 1/PK','500 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (250,'D02-E500-05-S','MidiKros','MIDIKROS 20CM 500K MPES 0.5MM FLL X FLL 1/PK STERILE','500 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (251,'D02-E500-10-N','MidiKros','MIDIKROS 20CM 500K MPES 1MM FLL X FLL 1/PK','500 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (252,'D02-E500-10-S','MidiKros','MIDIKROS 20CM 500K MPES 1MM FLL X FLL 1/PK STERILE','500 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (253,'D02-E65U-07-N','MidiKros','MIDIKROS 20CM .65UM MPES .75MM FLLXFLL 1/PK','0.65 ?m','mPES',0.75,25,20.0,18,0.275,46.2,43.0,38.8,85,1.59,4.39,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (254,'D02-E65U-07-S','MidiKros','MIDIKROS 20CM .65UM MPES .75MM FLLXFLL STERILE 1/PK','0.65 ?m','mPES',0.75,25,20.0,18,0.275,46.2,43.0,38.8,85,1.59,4.39,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (255,'D02-E750-05-N','MidiKros','MIDIKROS 20CM 750K MPES 0.5MM FLL X FLL 1/PK','750 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (256,'D02-E750-05-S','MidiKros','MIDIKROS 20CM 750K MPES 0.5MM FLL X FLL 1/PK STERILE','750 kD','mPES',0.5,25,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (257,'D02-E750-10-N','MidiKros','MIDIKROS 20CM 750KD MPES 1MM FLLXFLL 1/PK','750 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (258,'D02-E750-10-S','MidiKros','MIDIKROS 20CM 750KD MPES 1MM FLLXFLL STERILE 1/PK','750 kD','mPES',1.0,25,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (259,'D02-M10U-06-N','MidiKros','MIDIKROS 20CM 0.1UM ME 0.6MM FLL X FLL 1/PK','0.1 ?m','ME',0.63,25,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (260,'D02-M10U-06-S','MidiKros','MIDIKROS 20CM 0.1UM ME0.6MM FLLXFLL 1/ PK STERILE','0.1 ?m','ME',0.63,25,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (261,'D02-M20U-06-N','MidiKros','MIDIKROS 20CM 0.2UM ME 0.6MM FLL X FLL 1/PK','0.2 ?m','ME',0.63,25,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (262,'D02-M20U-06-S','MidiKros','MIDIKROS 20CM 0.2UM ME 0.6MM FLL X FLL1/PK STERILE','0.2 ?m','ME',0.63,25,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (263,'D02-M20U-10-N','MidiKros','MIDIKROS 20CM 0.2UM ME 1MM FLL X FLL 1/PK','0.2 ?m','ME',1.0,25,20.0,15,0.275,54.0,50.0,46.0,94,2.36,3.98,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (264,'D02-M20U-10-S','MidiKros','MIDIKROS 20CM 0.2UM ME 1MM FLL X FLL 1/ PK STERILE','0.2 ?m','ME',1.0,25,20.0,15,0.275,54.0,50.0,46.0,94,2.36,3.98,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (265,'D02-P20U-05-N','MidiKros','MIDIKROS 20CM 0.2UM PES 0.5MM FLL X FLL 1/PK','0.2 ?m','PES',0.5,25,20.0,45,0.275,26.6,25.6,24.6,140,1.77,4.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (266,'D02-P20U-05-S','MidiKros','MIDIKROS 20CM 0.2UM PES 0.5MM FLL X FLL 1/PK STERILE','0.2 ?m','PES',0.5,25,20.0,45,0.275,26.6,25.6,24.6,140,1.77,4.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (267,'D02-P20U-10-N','MidiKros','MIDIKROS 20CM 0.2UM PES 1MM FLL X FLL 1/PK','0.2 ?m','PES',1.0,25,20.0,14,0.275,53.1,51.2,49.2,88,2.2,4.06,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (268,'D02-P20U-10-S','MidiKros','MIDIKROS 20CM 0.2UM PES 1MM FLL X FLL 1/PK STERILE','0.2 ?m','PES',1.0,25,20.0,14,0.275,53.1,51.2,49.2,88,2.2,4.06,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (269,'D02-S010-05-N','MidiKros','MIDIKROS 20CM 10KD PS 0.5MM FLL X FLL 1/PK','10 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (270,'D02-S010-05-P','MidiKros','MIDIKROS 20CM 10KD PS 0.5MM FLL X FLL 1/PK WET','10 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (271,'D02-S010-05-S','MidiKros','MIDIKROS 20CM 10KD PS 0.5MM FLL X FLL 1/PK STERILE','10 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (272,'D02-S050-05-N','MidiKros','MIDIKROS 20CM 50KD PS 0.5MM FLL X FLL 1/PK','50 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (273,'D02-S050-05-P','MidiKros','MIDIKROS 20CM 50KD PS 0.5MM FLL X FLL 1/PK WET','50 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (274,'D02-S050-05-S','MidiKros','MIDIKROS 20CM 50KD PS 0.5MM FLL X FLL 1/PK STERILE','50 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (275,'D02-S05U-05-N','MidiKros','MIDIKROS 20CM 0.05UM PS 0.5MM FLL X FLL 1/PK','0.05 ?m','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (276,'D02-S05U-05-P','MidiKros','MIDIKROS 20CM 0.05UM PS 0.5MM FLL X FLL 1/PK WET','0.05 ?m','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (277,'D02-S05U-05-S','MidiKros','MIDIKROS 20CM 0.05 UM PS 0.5MM FLL X FLL 1/PK STERILE','0.05 ?m','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (278,'D02-S500-05-N','MidiKros','MIDIKROS 20CM 500KD PS 0.5MM FLL X FLL 1/PK','500 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (279,'D02-S500-05-P','MidiKros','MIDIKROS 20CM 500KD PS 0.5MM FLL X FLL 1/PK WET','500 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (280,'D02-S500-05-S','MidiKros','MIDIKROS 20CM 500KD PS 0.5MM FLL X FLL 1/PK STERILE','500 kD','PS',0.5,25,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (281,'D04-E001-05-N','MidiKros','MIDIKROS 41.5CM 1K MPES .5MM FLLXFLL 1/PK','1 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (282,'D04-E001-05-S','MidiKros','MIDIKROS 41.5CM 1K MPES .5MM FLLXFLL 1/PK STERILE','1 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (283,'D04-E003-05-N','MidiKros','MIDIKROS  41.5CM 3K MPES 0.5MM FLL X FLL 1/PK','3 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (284,'D04-E003-05-S','MidiKros','MIDIKROS  41.5CM 3K MPES 0.5MM FLL X FLL 1/PK STERILE','3 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (285,'D04-E003-10-N','MidiKros','MIDIKROS 41.5CM 3K MPES 1MM FLL X FLL 1/PK','3 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (286,'D04-E003-10-S','MidiKros','MIDIKROS 41.5CM 3K MPES 1MM FLL X FLL 1/PK STERILE','3 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (287,'D04-E005-05-N','MidiKros','MIDIKROS 41.5CM 5K MPES 0.5MM FLL X FLL 1/PK','5 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (288,'D04-E005-05-S','MidiKros','MIDIKROS 41.5CM 5K MPES 0.5MM FLL X FLL 1/PK STERILE','5 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (289,'D04-E005-10-N','MidiKros','MIDIKROS 41.5CM 5K MPES 1MM FLL X FLL 1/PK','5 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (290,'D04-E005-10-S','MidiKros','MIDIKROS 41.5CM 5K MPES 1MM FLL X FLL 1/PK STERILE','5 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (291,'D04-E010-05-N','MidiKros','MIDIKROS 41.5CM 10K MPES 0.5MM FLL X FLL 1/PK','10 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,26.0,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (292,'D04-E010-05-S','MidiKros','MIDIKROS 41.5CM 10K MPES 0.5MM FLL X FLL 1/PK STERILE','10 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,26.0,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (293,'D04-E010-10-N','MidiKros','MIDIKROS  41.5CM 10K MPES 1MM FLL X FLL 1/PK','10 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (294,'D04-E010-10-S','MidiKros','MIDIKROS  41.5CM 10K MPES 1MM FLL X FLL 1/PK STERILE','10 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (295,'D04-E030-05-N','MidiKros','MIDIKROS 41.5CM 30K MPES 0.5MM FLL X FLL 1/PK','30 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (296,'D04-E030-05-S','MidiKros','MIDIKROS 41.5CM 30K MPES 0.5MM FLL X FLL 1/PK STERILE','30 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (297,'D04-E030-10-N','MidiKros','MIDIKROS  41.5CM 30K MPES 1MM FLL X FLL 1/PK','30 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (298,'D04-E030-10-S','MidiKros','MIDIKROS  41.5CM 30K MPES 1MM FLL X FLL 1/PK STERILE','30 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (299,'D04-E050-05-N','MidiKros','MIDIKROS 41.5CM 50K MPES 0.5MM FLL X FLL 1/PK','50 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (300,'D04-E050-05-S','MidiKros','MIDIKROS 41.5CM 50K MPES 0.5MM FLL X FLL 1/PK STERILE','50 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (301,'D04-E050-10-N','MidiKros','MIDIKROS  41.5CM 50K MPES 1MM FLL X FLL 1/PK','50 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (302,'D04-E050-10-S','MidiKros','MIDIKROS  41.5CM 50K MPES 1MM FLL X FLL 1/PK STERILE','50 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (303,'D04-E070-05-N','MidiKros','MIDIKROS 41.5CM 70K MPES 0.5MM FLL X FLL 1/PK','70 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (304,'D04-E070-05-S','MidiKros','MIDIKROS 41.5CM 70K MPES 0.5MM FLL X FLL 1/PK STERILE','70 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (305,'D04-E070-10-N','MidiKros','MIDIKROS  41.5CM 70K MPES 1MM FLL X FLL 1/PK','70 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (306,'D04-E070-10-S','MidiKros','MIDIKROS  41.5CM 70K MPES 1MM FLL X FLL 1/PK STERILE','70 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (307,'D04-E100-05-N','MidiKros','MIDIKROS 41.5CM 100K MPES 0.5MM FLL X FLL 1/PK','100 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (308,'D04-E100-05-S','MidiKros','MIDIKROS 41.5CM 100K MPES 0.5MM FLL X FLL 1/PK STERILE','100 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (309,'D04-E100-10-N','MidiKros','MIDIKROS  41.5CM 100K MPES 1MM FLL X FLL 1/PK','100 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (310,'D04-E100-10-S','MidiKros','MIDIKROS  41.5CM 100K MPES 1MM FLL X FLL 1/PK STERILE','100 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (311,'D04-E20U-05-N','MidiKros','MIDIKROS 41.5CM 0.2UM MPES 0.5MM FLL X FLL 1/PK','0.2 ?m','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (312,'D04-E20U-05-S','MidiKros','MIDIKROS 41.5CM 0.2UM MPES 0.5MM FLL X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (313,'D04-E20U-10-N','MidiKros','MIDIKROS 41.5CM .2UM MPES 1MM FLLXFLL 1/PK','0.2 ?m','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (314,'D04-E20U-10-S','MidiKros','MIDIKROS 41.5CM .2UM MPES 1MM FLLXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (315,'D04-E300-05-N','MidiKros','MIDIKROS  41.5CM 300K MPES 0.5MM FLL X FLL 1/PK','300 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (316,'D04-E300-05-S','MidiKros','MIDIKROS  41.5CM 300K MPES 0.5MM FLL X FLL 1/PK STERILE','300 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (317,'D04-E300-10-N','MidiKros','MIDIKROS  41.5CM 300K MPES 1MM FLL X FLL 1/PK','300 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (318,'D04-E300-10-S','MidiKros','MIDIKROS  41.5CM 300K MPES 1MM FLL X FLL 1/PK STERILE','300 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (319,'D04-E500-05-N','MidiKros','MIDIKROS 41.5CM 500K MPES 0.5MM FLL X FLL 1/PK','500 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (320,'D04-E500-05-S','MidiKros','MIDIKROS 41.5CM 500K MPES 0.5MM FLL X FLL 1/PK STERILE','500 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (321,'D04-E500-10-N','MidiKros','MIDIKROS 41.5CM 500K MPES 1MM FLL X FLL 1/PK','500 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (322,'D04-E500-10-S','MidiKros','MIDIKROS 41.5CM 500K MPES 1MM FLL X FLL 1/PK STERILE','500 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (323,'D04-E65U-07-N','MidiKros','MIDIKROS 41.5CM .65UM MPES .75MM FLLXFLL 1/PK','0.65 ?m','mPES',0.75,45,41.5,18,0.275,46.2,43.0,38.8,175,3.3,9.12,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (324,'D04-E65U-07-S','MidiKros','MIDIKROS 41.5CM .65UM MPES .75MM FLLXFLL STERILE 1/PK','0.65 ?m','mPES',0.75,45,41.5,18,0.275,46.2,43.0,38.8,175,3.3,9.12,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (325,'D04-E750-05-N','MidiKros','MIDIKROS 41.5CM 750K MPES 0.5MM FLL X FLL 1/PK','750 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (326,'D04-E750-05-S','MidiKros','MIDIKROS 41.5CM 750K MPES 0.5MM FLL X FLL 1/PK STERILE','750 kD','mPES',0.5,45,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (327,'D04-E750-10-N','MidiKros','MIDIKROS 41.5CM 750KD MPES 1MM FLLXFLL 1/PK','750 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (328,'D04-E750-10-S','MidiKros','MIDIKROS 41.5CM 750KD MPES 1MM FLLXFLL STERILE 1/PK','750 kD','mPES',1.0,45,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (329,'D04-M10U-06-N','MidiKros','MIDIKROS 41.5CM 0.1UM ME 0.6MM FLL X FLL 1/PK','0.1 ?m','ME',0.63,45,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (330,'D04-M10U-06-S','MidiKros','MIDIKROS 41.5CM 0.1UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.1 ?m','ME',0.63,45,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (331,'D04-M20U-06-N','MidiKros','MIDIKROS 41.5CM 0.2UM ME 0.6MM FLL X FLL 1/PK','0.2 ?m','ME',0.63,45,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (332,'D04-M20U-06-S','MidiKros','MIDIKROS 41.5CM 0.2UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.2 ?m','ME',0.63,45,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (333,'D04-M20U-10-N','MidiKros','MIDIKROS 41.5CM 0.2UM ME 1MM FLL X FLL 1/PK','0.2 ?m','ME',1.0,45,41.5,15,0.275,54.0,50.0,46.0,195,4.89,8.26,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (334,'D04-M20U-10-S','MidiKros','MIDIKROS 41.5CM 0.2UM ME 1MM FLL X FLL 1/ PK STERILE','0.2 ?m','ME',1.0,45,41.5,15,0.275,54.0,50.0,46.0,195,4.89,8.26,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (335,'D04-P20U-05-N','MidiKros','MIDIKROS 41.5CM 0.2UM PES 0.5MM FLL X FLL 1/PK','0.2 ?m','PES',0.5,45,41.5,45,0.275,26.6,25.6,24.6,290,3.66,9.89,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (336,'D04-P20U-05-S','MidiKros','MIDIKROS 41.5CM 0.2UM PES 0.5MM FLL X FLL 1/PK STERILE','0.2 ?m','PES',0.5,45,41.5,45,0.275,26.6,25.6,24.6,290,3.66,9.89,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (337,'D04-P20U-10-N','MidiKros','MIDIKROS 41.5CM 0.2UM PES 1MM FLL X FLL 1/PK','0.2 ?m','PES',1.0,45,41.5,14,0.275,53.1,51.2,49.2,180,4.56,8.42,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (338,'D04-P20U-10-S','MidiKros','MIDIKROS 41.5CM 0.2UM PES 1MM FLL X FLL 1/PK STERILE','0.2 ?m','PES',1.0,45,41.5,14,0.275,53.1,51.2,49.2,180,4.56,8.42,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (339,'D04-S010-05-N','MidiKros','MIDIKROS 41.5CM 10KD PS 0.5MM FLL X FLL 1/PK','10 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (340,'D04-S010-05-P','MidiKros','MIDIKROS 41.5CM 10KD PS 0.5MM FLL X FLL 1/PK WET','10 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (341,'D04-S010-05-S','MidiKros','MIDIKROS 41.5CM 10KD PS 0.5MM FLL X FLL 1/PK STERILE','10 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (342,'D04-S050-05-N','MidiKros','MIDIKROS 41.5CM 50KD PS 0.5MM FLL X FLL 1/PK','50 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (343,'D04-S050-05-P','MidiKros','MIDIKROS 41.5CM 50KD PS 0.5MM FLL X FLL 1/PK WET','50 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (344,'D04-S050-05-S','MidiKros','MIDIKROS 41.5CM 50KD PS 0.5MM FLL X FLL 1/PK STERILE','50 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (345,'D04-S05U-05-N','MidiKros','MIDIKROS 41.5CM 0.05UM PS 0.5MM FLL X FLL 1/PK','0.05 ?m','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (346,'D04-S05U-05-P','MidiKros','MIDIKROS 41.5CM 0.05UM  PS 0.5MM FLL X FLL 1/PK WET','0.05 ?m','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (347,'D04-S05U-05-S','MidiKros','MIDIKROS 41.5CM 0.05UM PS 0.5MM FLL X FLL 1/PK STERILE','0.05 ?m','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (348,'D04-S500-05-N','MidiKros','MIDIKROS 41.5CM 500KD PS 0.5MM FLL X FLL 1/PK','500 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (349,'D04-S500-05-P','MidiKros','MIDIKROS 41.5CM 500KD PS 0.5MM FLL X FLL 1/PK WET','500 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (350,'D04-S500-05-S','MidiKros','MIDIKROS 41.5CM 500KD PS 0.5MM FLL X FLL 1/PK STERILE','500 kD','PS',0.5,45,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (351,'D06-E001-05-N','MidiKros','MIDIKROS 65CM 1K MPES .5MM FLLXFLL 1/PK','1 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (352,'D06-E001-05-S','MidiKros','MIDIKROS 65CM 1K MPES .5MM FLLXFLL 1/PK STERILE','1 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (353,'D06-E003-05-N','MidiKros','MIDIKROS  65CM 3K MPES 0.5MM FLL X FLL 1/PK','3 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (354,'D06-E003-05-S','MidiKros','MIDIKROS  65CM 3K MPES 0.5MM FLL X FLL 1/PK STERILE','3 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (355,'D06-E003-10-N','MidiKros','MIDIKROS 65CM 3K MPES 1MM FLL X FLL 1/PK','3 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (356,'D06-E003-10-S','MidiKros','MIDIKROS 65CM 3K MPES 1MM FLL X FLL 1/PK STERILE','3 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (357,'D06-E005-05-N','MidiKros','MIDIKROS 65CM 5K MPES 0.5MM FLL X FLL 1/PK','5 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (358,'D06-E005-05-S','MidiKros','MIDIKROS 65CM 5K MPES 0.5MM FLL X FLL 1/PK STERILE','5 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (359,'D06-E005-10-N','MidiKros','MIDIKROS 65CM 5K MPES 1MM FLL X FLL 1/PK','5 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (360,'D06-E005-10-S','MidiKros','MIDIKROS 65CM 5K MPES 1MM FLL X FLL 1/PK STERILE','5 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (361,'D06-E010-05-N','MidiKros','MIDIKROS 65CM 10K MPES 0.5MM FLL X FLL 1/PK','10 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,26.0,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (362,'D06-E010-05-S','MidiKros','MIDIKROS 65CM 10K MPES 0.5MM FLL X FLL 1/PK STERILE','10 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,26.0,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (363,'D06-E010-10-N','MidiKros','MIDIKROS  65CM 10K MPES 1MM FLL X FLL 1/PK','10 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (364,'D06-E010-10-S','MidiKros','MIDIKROS  65CM 10K MPES 1MM FLL X FLL 1/PK STERILE','10 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (365,'D06-E030-05-N','MidiKros','MIDIKROS 65CM 30K MPES 0.5MM FLL X FLL 1/PK','30 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (366,'D06-E030-05-S','MidiKros','MIDIKROS 65CM 30K MPES 0.5MM FLL X FLL 1/PK STERILE','30 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (367,'D06-E030-10-N','MidiKros','MIDIKROS  65CM 30K MPES 1MM FLL X FLL 1/PK','30 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (368,'D06-E030-10-S','MidiKros','MIDIKROS  65CM 30K MPES 1MM FLL X FLL 1/PK STERILE','30 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (369,'D06-E050-05-N','MidiKros','MIDIKROS 65CM 50K MPES 0.5MM FLL X FLL 1/PK','50 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (370,'D06-E050-05-S','MidiKros','MIDIKROS 65CM 50K MPES 0.5MM FLL X FLL 1/PK STERILE','50 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (371,'D06-E050-10-N','MidiKros','MIDIKROS  65CM 50K MPES 1MM FLL X FLL 1/PK','50 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (372,'D06-E050-10-S','MidiKros','MIDIKROS  65CM 50K MPES 1MM FLL X FLL 1/PK STERILE','50 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (373,'D06-E070-05-N','MidiKros','MIDIKROS 65CM 70K MPES 0.5MM FLL X FLL 1/PK','70 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (374,'D06-E070-05-S','MidiKros','MIDIKROS 65CM 70K MPES 0.5MM FLL X FLL 1/PK STERILE','70 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (375,'D06-E070-10-N','MidiKros','MIDIKROS  65CM 70K MPES 1MM FLL X FLL 1/PK','70 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (376,'D06-E070-10-S','MidiKros','MIDIKROS  65CM 70K MPES 1MM FLL X FLL 1/PK STERILE','70 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (377,'D06-E100-05-N','MidiKros','MIDIKROS 65CM 100K MPES 0.5MM FLL X FLL 1/PK','100 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (378,'D06-E100-05-S','MidiKros','MIDIKROS 65CM 100K MPES 0.5MM FLL X FLL 1/PK STERILE','100 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (379,'D06-E100-10-N','MidiKros','MIDIKROS  65CM 100K MPES 1MM FLL X FLL 1/PK','100 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (380,'D06-E100-10-S','MidiKros','MIDIKROS  65CM 100K MPES 1MM FLL X FLL 1/PK STERILE','100 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (381,'D06-E20U-05-N','MidiKros','MIDIKROS 65CM 0.2UM MPES 0.5MM FLL X FLL 1/PK','0.2 ?m','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (382,'D06-E20U-05-S','MidiKros','MIDIKROS 65CM 0.2UM MPES 0.5MM FLL X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (383,'D06-E20U-10-N','MidiKros','MIDIKROS 65CM .2UM MPES 1MM FLLXFLL 1/PK','0.2 ?m','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (384,'D06-E20U-10-S','MidiKros','MIDIKROS 65CM .2UM MPES 1MM FLLXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (385,'D06-E300-05-N','MidiKros','MIDIKROS  65CM 300K MPES 0.5MM FLL X FLL 1/PK','300 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (386,'D06-E300-05-S','MidiKros','MIDIKROS  65CM 300K MPES 0.5MM FLL X FLL 1/PK STERILE','300 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (387,'D06-E300-10-N','MidiKros','MIDIKROS  65CM 300K MPES 1MM FLL X FLL 1/PK','300 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (388,'D06-E300-10-S','MidiKros','MIDIKROS  65CM 300K MPES 1MM FLL X FLL 1/PK STERILE','300 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (389,'D06-E500-05-N','MidiKros','MIDIKROS 65CM 500K MPES 0.5MM FLL X FLL 1/PK','500 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (390,'D06-E500-05-S','MidiKros','MIDIKROS 65CM 500K MPES 0.5MM FLL X FLL 1/PK STERILE','500 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (391,'D06-E500-10-N','MidiKros','MIDIKROS 65CM 500K MPES 1MM FLL X FLL 1/PK','500 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (392,'D06-E500-10-S','MidiKros','MIDIKROS 65CM 500K MPES 1MM FLL X FLL 1/PK STERILE','500 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (393,'D06-E65U-07-N','MidiKros','MIDIKROS 65CM .65UM MPES .75MM FLLXFLL 1/PK','0.65 ?m','mPES',0.75,69,65.0,18,0.275,46.2,43.0,38.8,275,5.17,14.28,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (394,'D06-E65U-07-S','MidiKros','MIDIKROS 65CM .65UM MPES .75MM FLLXFLL STERILE 1/PK','0.65 ?m','mPES',0.75,69,65.0,18,0.275,46.2,43.0,38.8,275,5.17,14.28,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (395,'D06-E750-05-N','MidiKros','MIDIKROS 65CM 750K MPES 0.5MM FLL X FLL 1/PK','750 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (396,'D06-E750-05-S','MidiKros','MIDIKROS  65CM 750K MPES 0.5MM FLL X FLL 1/PK STERILE','750 kD','mPES',0.5,69,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (397,'D06-E750-10-N','MidiKros','MIDIKROS 65CM 750KD MPES 1MM FLLXFLL 1/PK','750 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (398,'D06-E750-10-S','MidiKros','MIDIKROS 65CM 750KD MPES 1MM FLLXFLL STERILE 1/PK','750 kD','mPES',1.0,69,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (399,'D06-M10U-06-N','MidiKros','MIDIKROS 65CM 0.1UM ME 0.6MM FLL X FLL 1/PK','0.1 ?m','ME',0.63,69,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (400,'D06-M10U-06-S','MidiKros','MIDIKROS 65CM 0.1UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.1 ?m','ME',0.63,69,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (401,'D06-M20U-06-N','MidiKros','MIDIKROS 65CM 0.2um ME 0.6MM FLL X FLL 1/PK','0.2 ?m','ME',0.63,69,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (402,'D06-M20U-06-S','MidiKros','MIDIKROS 65CM 0.2UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.2 ?m','ME',0.63,69,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (403,'D06-M20U-10-N','MidiKros','MIDIKROS 65CM 0.2UM ME 1MM FLL X FLL 1/PK','0.2 ?m','ME',1.0,69,65.0,15,0.275,54.0,50.0,46.0,305,7.65,12.94,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (404,'D06-M20U-10-S','MidiKros','MIDIKROS 65CM 0.2UM ME 1MM FLL X FLL 1/ PK STERILE','0.2 ?m','ME',1.0,69,65.0,15,0.275,54.0,50.0,46.0,305,7.65,12.94,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (405,'D06-P20U-05-N','MidiKros','MIDIKROS 65CM 0.2UM PES 0.5MM FLL X FLL 1/PK','0.2 ?m','PES',0.5,69,65.0,45,0.275,26.6,25.6,24.6,460,5.74,15.49,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (406,'D06-P20U-05-S','MidiKros','MIDIKROS 65CM 0.2UM PES 0.5MM FLL X FLL 1/PK STERILE','0.2 ?m','PES',0.5,69,65.0,45,0.275,26.6,25.6,24.6,460,5.74,15.49,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (407,'D06-P20U-10-N','MidiKros','MIDIKROS 65CM 0.2UM PES 1MM FLL X FLL 1/PK','0.2 ?m','PES',1.0,69,65.0,14,0.275,53.1,51.2,49.2,290,7.14,13.19,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (408,'D06-P20U-10-S','MidiKros','MIDIKROS 65CM 0.2 UM PES 1MM FLL X FLL 1/PK STERILE','0.2 ?m','PES',1.0,69,65.0,14,0.275,53.1,51.2,49.2,290,7.14,13.19,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (409,'D06-S010-05-N','MidiKros','MIDIKROS 65CM 10KD PS 0.5MM FLL X FLL 1/PK','10 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (410,'D06-S010-05-P','MidiKros','MIDIKROS 65CM 10KD PS 0.5MM FLL X FLL 1/PK WET','10 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (411,'D06-S010-05-S','MidiKros','MIDIKROS 65CM 10KD PS 0.5MM FLL X FLL 1/PK STERILE','10 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (412,'D06-S050-05-N','MidiKros','MIDIKROS 65CM 50KD PS 0.5MM FLL X FLL 1/PK','50 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (413,'D06-S050-05-P','MidiKros','MIDIKROS 65CM 50KD PS 0.5MM FLL X FLL 1/PK WET','50 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (414,'D06-S050-05-S','MidiKros','MIDIKROS 65CM 50KD PS 0.5MM FLL X FLL 1/PK STERILE','50 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (415,'D06-S05U-05-N','MidiKros','MIDIKROS 65CM 0.05UM PS 0.5MM FLL X FLL 1/PK','0.05 ?m','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (416,'D06-S05U-05-P','MidiKros','MIDIKROS 65CM 0.05UM PS 0.5MM FLL X FLL 1/PK WET','0.05 ?m','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (417,'D06-S05U-05-S','MidiKros','MIDIKROS 65CM 0.05UM PS 0.5MM FLL X FLL 1/PK STERILE','0.05 ?m','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (418,'D06-S500-05-N','MidiKros','MIDIKROS 65CM 500KD PS 0.5MM FLL X FLL 1/PK','500 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (419,'D06-S500-05-P','MidiKros','MIDIKROS 65CM 500KD PS 0.5MM FLL X FLL 1/PK WET','500 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (420,'D06-S500-05-S','MidiKros','MIDIKROS 65CM 500KD PS 0.5MM FLL X FLL 1/PK STERILE','500 kD','PS',0.5,69,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (421,'K02-E001-05-N','KrosFlo','KROSFLO 20CM 1K MPES .5MM 3TCX1.5TC 1/PK','1 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'5890','17671',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (422,'K02-E001-05-S','KrosFlo','KROSFLO 20CM 1K MPES .5MM 3TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'5890','17671',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (423,'K02-E003-05-N','KrosFlo','KROSFLO 20CM 3K MPES 0.5MM 3TC X 1.5TC','3 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (424,'K02-E003-05-S','KrosFlo','KROSFLO 20CM 3K MPES 0.5MM 3TC X 1.5TC STERILE','3 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (425,'K02-E003-10-N','KrosFlo','KROSFLO 20CM 3K MPES 1MM 3TC X 1.5TC','3 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (426,'K02-E003-10-S','KrosFlo','KROSFLO 20CM 3K MPES 1MM 3TC X 1.5TC STERILE','3 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (427,'K02-E005-05-N','KrosFlo','KROSFLO 20CM 5K MPES 0.5MM 3TC X 1.5TC','5 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (428,'K02-E005-05-S','KrosFlo','KROSFLO 20CM 5K MPES 0.5MM 3TC X 1.5TC STERILE','5 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (429,'K02-E005-10-N','KrosFlo','KROSFLO 20CM 5K MPES 1MM 3TC X 1.5TC','5 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (430,'K02-E005-10-S','KrosFlo','KROSFLO 20CM 5K MPES 1MM 3TC X 1.5TC STERILE','5 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (431,'K02-E010-05-N','KrosFlo','KROSFLO 20CM 10K MPES 0.5MM 3TC X 1.5TC','10 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,26.0,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (432,'K02-E010-05-S','KrosFlo','KROSFLO 20CM 10K MPES 0.5MM 3TC X 1.5TC STERILE','10 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,26.0,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (433,'K02-E010-10-N','KrosFlo','KROSFLO  20CM 10K MPES 1MM 3TC X 1.5TC','10 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (434,'K02-E010-10-S','KrosFlo','KROSFLO  20CM 10K MPES 1MM 3TC X 1.5TC STERILE','10 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (435,'K02-E030-05-N','KrosFlo','KROSFLO 20CM 30K MPES 0.5MM 3TC X 1.5TC','30 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (436,'K02-E030-05-S','KrosFlo','KROSFLO 20CM 30K MPES 0.5MM 3TC X 1.5TC STERILE','30 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (437,'K02-E030-10-N','KrosFlo','KROSFLO  20CM 30K MPES 1MM 3TC X 1.5TC','30 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (438,'K02-E030-10-S','KrosFlo','KROSFLO  20CM 30K MPES 1MM 3TC X 1.5TC STERILE','30 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (439,'K02-E050-05-N','KrosFlo','KROSFLO 20CM 50K MPES 0.5MM 3TC X 1.5TC','50 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (440,'K02-E050-05-S','KrosFlo','KROSFLO 20CM 50K MPES 0.5MM 3TC X 1.5TC IRR','50 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (441,'K02-E050-10-N','KrosFlo','KROSFLO  20CM 50K MPES 1MM 3TC X 1.5TC','50 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (442,'K02-E050-10-S','KrosFlo','KROSFLO  20CM 50K MPES 1MM 3TC X 1.5TC STERILE','50 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (443,'K02-E070-05-N','KrosFlo','KROSFLO 20CM 70K MPES 0.5MM 3TC X 1.5TC','70 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (444,'K02-E070-05-S','KrosFlo','KROSFLO 20CM 70K MPES 0.5MM 3TC X 1.5TC STERILE','70 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (445,'K02-E070-10-N','KrosFlo','KROSFLO 20CM 70K MPES 1MM 3TC X 1.5TC','70 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (446,'K02-E070-10-S','KrosFlo','KROSFLO 20CM 70K MPES 1MM 3TC X 1.5TC IRR','70 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (447,'K02-E100-05-N','KrosFlo','KROSFLO 20CM 100K MPES 0.5MM 3TC X 1.5TC','100 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (448,'K02-E100-05-S','KrosFlo','KROSFLO 20CM 100K MPES 0.5MM 3TC X 1.5TC STERILE','100 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (449,'K02-E100-10-N','KrosFlo','KROSFLO 20CM 100K MPES 1MM 3TC X 1.5TC','100 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (450,'K02-E100-10-S','KrosFlo','KROSFLO 20CM 100K MPES 1MM 3TC X 1.5TC STERILE','100 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (451,'K02-E20U-05-N','KrosFlo','KROSFLO 20CM 0.2UM MPES 0.5MM 3TC X 1.5TC','0.2 ?m','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (452,'K02-E20U-05-S','KrosFlo','KROSFLO 20CM 0.2UM MPES 0.5MM 3TC X 1.5TC STERILE','0.2 ?m','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (453,'K02-E20U-10-N','KrosFlo','KROSFLO 20CM .2UM MPES 1MM 3TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (454,'K02-E20U-10-S','KrosFlo','KROSFLO 20CM .2UM MPES 1MM 3TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (455,'K02-E300-05-N','KrosFlo','KROSFLO 20CM 300K MPES 0.5MM 3TC X 1.5TC','300 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (456,'K02-E300-05-S','KrosFlo','KROSFLO 20CM 300K MPES 0.5MM 3TC X 1.5TC IRR','300 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (457,'K02-E300-10-N','KrosFlo','KROSFLO 20CM 300K MPES 1MM 3TC X 1.5TC','300 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (458,'K02-E300-10-S','KrosFlo','KROSFLO 20CM 300K MPES 1MM 3TC X 1.5TC STERILE','300 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (459,'K02-E500-05-N','KrosFlo','KROSFLO 20CM 500K MPES 0.5MM 3TC X 1.5TC','500 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (460,'K02-E500-05-S','KrosFlo','KROSFLO 20CM 500K MPES 0.5MM 3TC X 1.5TC STERILE','500 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (461,'K02-E500-10-N','KrosFlo','KROSFLO 20CM 500K MPES 1MM 3TC X 1.5TC','500 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (462,'K02-E500-10-S','KrosFlo','KROSFLO 20CM 500K MPES 1MM 3TC X 1.5TC STERILE','500 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (463,'K02-E65U-07-N','Krosflo','KROSFLO 20CM .65UM MPES .75MM 3TCX1.5TC 1/PK','0.65 ?m','mPES',0.75,23,20.0,1900,2.5,46.2,43.0,38.8,9000,167.79,288.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (464,'K02-E65U-07-S','Krosflo','KROSFLO 20CM .65UM MPES .75MM 3TCX1.5TC STERILE 1/PK','0.65 ?m','mPES',0.75,23,20.0,1900,2.5,46.2,43.0,38.8,9000,167.79,288.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (465,'K02-E750-05-N','KrosFlo','KROSFLO  20CM 750K MPES 0.5MM 3TC X 1.5TC','750 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (466,'K02-E750-05-S','KrosFlo','KROSFLO  20CM 750K MPES 0.5MM 3TC X 1.5TC STERILE','750 kD','mPES',0.5,23,20.0,4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (467,'K02-E750-10-N','KrosFlo','KROSFLO 20CM 750KD MPES 1MM 3TCX1.5TC 1/PK','750 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (468,'K02-E750-10-S','KrosFlo','KROSFLO 20CM 750KD MPES 1MM 3TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,23,20.0,1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (469,'K02-M10U-06-N','KrosFlo','KROSFLO 20CM 0.1 UM ME 0.63MM 3TC X 1.5TC','0.1 ?m','ME',0.63,23,20.0,3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (470,'K02-M10U-06-S','KrosFlo','KROSFLO 20CM 0.1 UM ME 0.63MM 3TC X 1.5TC STERILE','0.1 ?m','ME',0.63,23,20.0,3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (471,'K02-M20U-06-N','KrosFlo','KROSFLO 20CM 0.2 UM ME 0.63MM 3TC X 1.5TC','0.2 ?m','ME',0.63,23,20.0,3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (472,'K02-M20U-06-S','KrosFlo','KROSFLO 20CM 0.2 UM ME 0.63MM 3TC X 1.5TC STERILE','0.2 ?m','ME',0.63,23,20.0,3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (473,'K02-M20U-10-N','KrosFlo','KROSFLO 20CM 0.2 UM ME 1MM 3TC X 1.5TC','0.2 ?m','ME',1.0,23,20.0,1300,2.5,54.0,50.0,46.0,8200,204.1,314.16,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (474,'K02-M20U-10-S','KrosFlo','KROSFLO 20CM 0.2 UM ME 1MM 3TC X 1.5TC STERILE','0.2 ?m','ME',1.0,23,20.0,1300,2.5,54.0,50.0,46.0,8200,204.1,314.16,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (475,'K02-P20U-05-N','KrosFlo','KROSFLO 20CM 0.2 UM PES 0.5MM 3TC X 1.5TC','0.2 ?m','PES',0.5,23,20.0,5000,2.5,26.6,25.6,24.6,16000,196.25,311.53,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (476,'K02-P20U-05-S','KrosFlo','KROSFLO 20CM 0.2 UM PES 0.5MM 3TC X 1.5TC STERILE','0.2 ?m','PES',0.5,23,20.0,5000,2.5,26.6,25.6,24.6,16000,196.25,311.53,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (477,'K02-P20U-10-N','KrosFlo','KROSFLO 20CM 0.2 UM PES 1MM 3TC X 1.5TC','0.2 ?m','PES',1.0,23,20.0,1250,2.5,53.1,51.2,49.2,7850,196.25,311.53,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (478,'K02-P20U-10-S','KrosFlo','KROSFLO 20CM 0.2 UM PES 1MM 3TC X 1.5TC STERILE','0.2 ?m','PES',1.0,23,20.0,1250,2.5,53.1,51.2,49.2,7850,196.25,311.53,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (479,'K02-S010-05-N','KrosFlo','KROSFLO 20CM 10KD PS 0.5MM 3TC X 1.5TC','10 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (480,'K02-S010-05-P','KrosFlo','KROSFLO 20CM 10KD PS 0.5MM 3TC X 1.5TC WET','10 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (481,'K02-S010-05-S','KrosFlo','KROSFLO 20CM 10KD PS 0.5MM 3TC X 1.5TC STERILE','10 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (482,'K02-S050-05-N','KrosFlo','KROSFLO 20CM 50KD PS 0.5MM 3TC X 1.5TC','50 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (483,'K02-S050-05-P','KrosFlo','KROSFLO 20CM 50KD PS 0.5MM 3TC X 1.5TC WET','50 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (484,'K02-S050-05-S','KrosFlo','KROSFLO 20CM 50KD PS 0.5MM 3TC X 1.5TC STERILE','50 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (485,'K02-S05U-05-N','KrosFlo','KROSFLO 20CM 0.05 UM PS 0.5MM 3TC X 1.5TC','0.05 ?m','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (486,'K02-S05U-05-P','KrosFlo','KROSFLO 20CM 0.05 UM PS 0.5MM 3TC X 1.5TC WET','0.05 ?m','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (487,'K02-S05U-05-S','KrosFlo','KROSFLO 20CM 0.05 UM PS 0.5MM 3TC X 1.5TC STERILE','0.05 ?m','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (488,'K02-S500-05-N','KrosFlo','KROSFLO 20CM 500KD PS 0.5MM 3TC X 1.5TC','500 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (489,'K02-S500-05-P','KrosFlo','KROSFLO 20CM 500KD PS 0.5MM 3TC X 1.5TC WET','500 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (490,'K02-S500-05-S','KrosFlo','KROSFLO 20CM 500KD PS 0.5MM 3TC X 1.5TC STERILE','500 kD','PS',0.5,23,20.0,5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (491,'K04-E001-05-N','KrosFlo','KROSFLO 41.5CM 1K MPES .5MM 3TCX1.5TC 1/PK','1 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'5890','17671',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (492,'K04-E001-05-S','KrosFlo','KROSFLO 41.5CM 1K MPES .5MM 3TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'5890','17671',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (493,'K04-E003-05-N','KrosFlo','KROSFLO 41.5CM 3K MPES 0.5MM 3TC X 1.5TC','3 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (494,'K04-E003-05-S','KrosFlo','KROSFLO 41.5CM 3K MPES 0.5MM 3TC X 1.5TC STERILE','3 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (495,'K04-E003-10-N','KrosFlo','KROSFLO 41.5CM 3K MPES 1MM 3TC X 1.5TC','3 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (496,'K04-E003-10-S','KrosFlo','KROSFLO 41.5CM 3K MPES 1MM 3TC X 1.5TC STERILE','3 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (497,'K04-E005-05-N','KrosFlo','KROSFLO 41.5CM 5K MPES 0.5MM 3TC X 1.5TC','5 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (498,'K04-E005-05-S','KrosFlo','KROSFLO 41.5CM 5K MPES 0.5MM 3TC X 1.5TC STERILE','5 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (499,'K04-E005-10-N','KrosFlo','KROSFLO 41.5CM 5K MPES 1MM 3TC X 1.5TC','5 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (500,'K04-E005-10-S','KrosFlo','KROSFLO 41.5CM 5K MPES 1MM 3TC X 1.5TC STERILE','5 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (501,'K04-E010-05-N','KrosFlo','KROSFLO 41.5CM 10K MPES 0.5MM 3TC X 1.5TC','10 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,26.0,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (502,'K04-E010-05-S','KrosFlo','KROSFLO 41.5CM 10K MPES 0.5MM 3TC X 1.5TC IRR','10 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,26.0,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (503,'K04-E010-10-N','KrosFlo','KROSFLO  41.5CM 10K MPES 1MM 3TC X 1.5TC','10 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (504,'K04-E010-10-S','KrosFlo','KROSFLO  41.5CM 10K MPES 1MM 3TC X 1.5TC STERILE','10 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (505,'K04-E030-05-N','KrosFlo','KROSFLO 41.5CM 30K MPES 0.5MM 3TC X 1.5TC','30 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (506,'K04-E030-05-S','KrosFlo','KROSFLO 41.5CM 30K MPES 0.5MM 3TC X 1.5TC STERILE','30 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (507,'K04-E030-10-N','KrosFlo','KROSFLO  41.5CM 30K MPES 1MM 3TC X 1.5TC','30 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (508,'K04-E030-10-S','KrosFlo','KROSFLO  41.5CM 30K MPES 1MM 3TC X 1.5TC STERILE','30 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (509,'K04-E050-05-N','KrosFlo','KROSFLO 41.5CM 50K MPES 0.5MM 3TC X 1.5TC','50 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (510,'K04-E050-05-S','KrosFlo','KROSFLO 41.5CM 50K MPES 0.5MM 3TC X 1.5TC STERILE','50 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (511,'K04-E050-10-N','KrosFlo','KROSFLO  41.5CM 50K MPES 1MM 3TC X 1.5TC','50 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (512,'K04-E050-10-S','KrosFlo','KROSFLO  41.5CM 50K MPES 1MM 3TC X 1.5TC STERILE','50 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (513,'K04-E070-05-N','KrosFlo','KROSFLO 41.5CM 70K MPES 0.5MM 3TC X 1.5TC','70 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (514,'K04-E070-05-S','KrosFlo','KROSFLO 41.5CM 70K MPES 0.5MM 3TC X 1.5TC STERILE','70 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (515,'K04-E070-10-N','KrosFlo','KROSFLO 41.5CM 70K MPES 1MM 3TC X 1.5TC','70 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (516,'K04-E070-10-S','KrosFlo','KROSFLO 41.5CM 70K MPES 1MM 3TC X 1.5TC STERILE','70 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (517,'K04-E100-05-N','KrosFlo','KROSFLO 41.5CM 100K MPES 0.5MM 3TC X 1.5TC','100 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (518,'K04-E100-05-S','KrosFlo','KROSFLO 41.5CM 100K MPES 0.5MM 3TC X 1.5TC STERILE','100 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (519,'K04-E100-10-N','KrosFlo','KROSFLO 41.5CM 100K MPES 1MM 3TC X 1.5TC','100 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (520,'K04-E100-10-S','KrosFlo','KROSFLO 41.5CM 100K MPES 1MM 3TC X 1.5TC STERILE','100 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (521,'K04-E20U-05-N','KrosFlo','KROSFLO 41.5CM 0.2UM MPES 0.5MM 3TC X 1.5TC','0.2 ?m','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (522,'K04-E20U-05-S','KrosFlo','KROSFLO 41.5CM 0.2UM MPES 0.5MM 3TC X 1.5TC STERILE','0.2 ?m','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (523,'K04-E20U-10-N','KrosFlo','KROSFLO 41.5CM .2UM MPES 1MM 3TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (524,'K04-E20U-10-S','KrosFlo','KROSFLO 41.5CM .2UM MPES 1MM 3TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (525,'K04-E300-05-N','KrosFlo','KROSFLO 41.5CM 300K MPES 0.5MM 3TC X 1.5TC','300 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (526,'K04-E300-05-S','KrosFlo','KROSFLO 41.5CM 300K MPES 0.5MM 3TC X 1.5TC STERILE','300 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (527,'K04-E300-10-N','KrosFlo','KROSFLO 41.5CM 300K MPES 1MM 3TC X 1.5TC','300 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (528,'K04-E300-10-S','KrosFlo','KROSFLO 41.5CM 300K MPES 1MM 3TC X 1.5TC STERILE','300 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (529,'K04-E500-05-N','KrosFlo','KROSFLO 41.5CM 500K MPES 0.5MM 3TC X 1.5TC','500 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (530,'K04-E500-05-S','KrosFlo','KROSFLO 41.5CM 500K MPES 0.5MM 3TC X 1.5TC  STERILE','500 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (531,'K04-E500-10-N','KrosFlo','KROSFLO 41.5CM 500K MPES 1MM 3TC X 1.5TC','500 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (532,'K04-E500-10-S','KrosFlo','KROSFLO 41.5CM 500K MPES 1MM 3TC X 1.5TC STERILE','500 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (533,'K04-E65U-07-N','Krosflo','KROSFLO 41.5CM .65UM MPES .75MM 3TCX1.5TC 1/PK','0.65 ?m','mPES',0.75,46,41.5,1900,2.5,46.2,43.0,38.8,18500,348.17,598.31,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (534,'K04-E65U-07-S','Krosflo','KROSFLO 41.5CM .65UM MPES .75MM 3TCX1.5TC STERILE 1/PK','0.65 ?m','mPES',0.75,46,41.5,1900,2.5,46.2,43.0,38.8,18500,348.17,598.31,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (535,'K04-E750-05-N','KrosFlo','KROSFLO  41.5CM 750K MPES 0.5MM 3TC X 1.5TC','750 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (536,'K04-E750-05-S','KrosFlo','KROSFLO  41.5CM 750K MPES 0.5MM 3TC X 1.5TC STERILE','750 kD','mPES',0.5,46,41.5,4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (537,'K04-E750-10-N','KrosFlo','KROSFLO 41.5CM 750KD MPES 1MM 3TCX1.5TC 1/PK','750 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (538,'K04-E750-10-S','KrosFlo','KROSFLO 41.5CM 750KD MPES 1MM 3TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,46,41.5,1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (539,'K04-M10U-06-N','KrosFlo','KROSFLO 41.5CM 0.1 UM ME 0.63MM 3TC X 1.5TC','0.1 ?m','ME',0.63,46,41.5,3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (540,'K04-M10U-06-S','KrosFlo','KROSFLO 41.5CM 0.1 UM ME 0.63MM 3TC X 1.5TC STERILE','0.1 ?m','ME',0.63,46,41.5,3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (541,'K04-M20U-06-N','KrosFlo','KROSFLO 41.5CM 0.2 uM ME 0.63MM 3TC X 1.5TC','0.2 ?m','ME',0.63,46,41.5,3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (542,'K04-M20U-06-S','KrosFlo','KROSFLO 41.5CM 0.2 UM ME 0.63MM 3TC X 1.5TC STERILE','0.2 ?m','ME',0.63,46,41.5,3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (543,'K04-M20U-10-N','KrosFlo','KROSFLO 41.5CM 0.2 UM ME 1MM 3TC X 1.5TC','0.2 ?m','ME',1.0,46,41.5,1300,2.5,54.0,50.0,46.0,17000,423.51,651.88,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (544,'K04-M20U-10-S','KrosFlo','KROSFLO 41.5CM 0.2 UM ME 1MM 3TC X 1.5TC STERILE','0.2 ?m','ME',1.0,46,41.5,1300,2.5,54.0,50.0,46.0,17000,423.51,651.88,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (545,'K04-P20U-05-N','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 0.5MM 3TC X 1.5TC','0.2 ?m','PES',0.5,46,41.5,5000,2.5,26.6,25.6,24.6,32500,407.22,646.42,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (546,'K04-P20U-05-S','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 0.5MM 3TC X 1.5TC STERILE','0.2 ?m','PES',0.5,46,41.5,5000,2.5,26.6,25.6,24.6,32500,407.22,646.42,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (547,'K04-P20U-10-N','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 1MM 3TC X 1.5TC','0.2 ?m','PES',1.0,46,41.5,1250,2.5,53.1,51.2,49.2,16500,407.22,646.42,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (548,'K04-P20U-10-S','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 1MM 3TC X 1.5TC STERILE','0.2 ?m','PES',1.0,46,41.5,1250,2.5,53.1,51.2,49.2,16500,407.22,646.42,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (549,'K04-S010-05-N','KrosFlo','KROSFLO 41.5CM 10KD PS 0.5MM 3TC X 1.5TC','10 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (550,'K04-S010-05-P','KrosFlo','KROSFLO 41.5CM 10KD PS 0.5MM 3TC X 1.5TC WET','10 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (551,'K04-S010-05-S','KrosFlo','KROSFLO 41.5CM 10KD PS 0.5MM 3TC X 1.5TC STERILE','10 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (552,'K04-S050-05-N','KrosFlo','KROSFLO 41.5CM 50KD PS 0.5MM 3TC X 1.5TC','50 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (553,'K04-S050-05-P','KrosFlo','KROSFLO 41.5CM 50KD PS 0.5MM 3TC X 1.5TC WET','50 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (554,'K04-S050-05-S','KrosFlo','KROSFLO 41.5CM 50KD PS 0.5MM 3TC X 1.5TC STERILE','50 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (555,'K04-S05U-05-N','KrosFlo','KROSFLO 41.5CM 0.05 UM PS 0.5MM 3TC X 1.5TC','0.05 ?m','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (556,'K04-S05U-05-P','KrosFlo','KROSFLO 41.5CM 0.05 UM PS 0.5MM 3TC X 1.5TC WET','0.05 ?m','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (557,'K04-S05U-05-S','KrosFlo','KROSFLO 41.5CM 0.05 UM PS 0.5MM 3TC X 1.5TC STERILE','0.05 ?m','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (558,'K04-S500-05-N','KrosFlo','KROSFLO 41.5CM 500KD PS 0.5MM 3TC X 1.5TC','500 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (559,'K04-S500-05-P','KrosFlo','KROSFLO 41.5CM 500KD PS 0.5MM 3TC X 1.5TC WET','500 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (560,'K04-S500-05-S','KrosFlo','KROSFLO 41.5CM 500KD PS 0.5MM 3TC X 1.5TC STERILE','500 kD','PS',0.5,46,41.5,5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (561,'K06-E001-05-N','KrosFlo','KROSFLO 65CM 1K MPES .5MM 3TCX1.5TC 1/PK','1 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'5890','17671',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (562,'K06-E001-05-S','KrosFlo','KROSFLO 65CM 1K MPES .5MM 3TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'5890','17671',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (563,'K06-E003-05-N','KrosFlo','KROSFLO 65CM 3K MPES 0.5MM 3TC X 1.5TC','3 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (564,'K06-E003-05-S','KrosFlo','KROSFLO 65CM 3K MPES 0.5MM 3TC X 1.5TC STERILE','3 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (565,'K06-E003-10-N','KrosFlo','KROSFLO 65CM 3K MPES 1MM 3TC X 1.5TC','3 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (566,'K06-E003-10-S','KrosFlo','KROSFLO 65CM 3K MPES 1MM 3TC X 1.5TC STERILE','3 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (567,'K06-E005-05-N','KrosFlo','KROSFLO 65CM 5K MPES 0.5MM 3TC X 1.5TC','5 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (568,'K06-E005-05-S','KrosFlo','KROSFLO 65CM 5K MPES 0.5MM 3TC X 1.5TC STERILE','5 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (569,'K06-E005-10-N','KrosFlo','KROSFLO 65CM 5K MPES 1MM 3TC X 1.5TC','5 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (570,'K06-E005-10-S','KrosFlo','KROSFLO 65CM 5K MPES 1MM 3TC X 1.5TC STERILE','5 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (571,'K06-E010-05-N','KrosFlo','KROSFLO 65CM 10K MPES 0.5MM 3TC X 1.5TC','10 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,26.0,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (572,'K06-E010-05-S','KrosFlo','KROSFLO 65CM 10K MPES 0.5MM 3TC X 1.5TC IRR','10 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,26.0,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (573,'K06-E010-10-N','KrosFlo','KROSFLO  65CM 10K MPES 1MM 3TC X 1.5TC','10 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (574,'K06-E010-10-S','KrosFlo','KROSFLO  65CM 10K MPES 1MM 3TC X 1.5TC STERILE','10 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (575,'K06-E030-05-N','KrosFlo','KROSFLO 65CM 30K MPES 0.5MM 3TC X 1.5TC','30 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (576,'K06-E030-05-S','KrosFlo','KROSFLO 65CM 30K MPES 0.5MM 3TC X 1.5TC IRR','30 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (577,'K06-E030-10-N','KrosFlo','KROSFLO  65CM 30K MPES 1MM 3TC X 1.5TC','30 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (578,'K06-E030-10-S','KrosFlo','KROSFLO  65CM 30K MPES 1MM 3TC X 1.5TC STERILE','30 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (579,'K06-E050-05-N','KrosFlo','KROSFLO 65CM 50K MPES 0.5MM 3TC X 1.5TC','50 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (580,'K06-E050-05-S','KrosFlo','KROSFLO 65CM 50K MPES 0.5MM 3TC X 1.5TC IRR','50 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (581,'K06-E050-10-N','KrosFlo','KROSFLO  65CM 50K MPES 1MM 3TC X 1.5TC','50 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (582,'K06-E050-10-S','KrosFlo','KROSFLO  65CM 50K MPES 1MM 3TC X 1.5TC STERILE','50 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (583,'K06-E070-05-N','KrosFlo','KROSFLO 65CM 70K MPES 0.5MM 3TC X 1.5TC','70 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (584,'K06-E070-05-S','KrosFlo','KROSFLO 65CM 70K MPES 0.5MM 3TC X 1.5TC STERILE','70 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (585,'K06-E070-10-N','KrosFlo','KROSFLO 65CM 70K MPES 1MM 3TC X 1.5TC','70 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (586,'K06-E070-10-S','KrosFlo','KROSFLO 65CM 70K MPES 1MM 3TC X 1.5TC STERILE','70 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (587,'K06-E100-05-N','KrosFlo','KROSFLO 65CM 100K MPES 0.5MM 3TC X 1.5TC','100 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (588,'K06-E100-05-S','KrosFlo','KROSFLO 65CM 100K MPES 0.5MM 3TC X 1.5TC STERILE','100 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (589,'K06-E100-10-N','KrosFlo','KROSFLO 65CM 100K MPES 1MM 3TC X 1.5TC','100 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (590,'K06-E100-10-S','KrosFlo','KROSFLO 65CM 100K MPES 1MM 3TC X 1.5TC STERILE','100 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (591,'K06-E20U-05-N','KrosFlo','KROSFLO 65CM 0.2UM MPES 0.5MM 3TC X 1.5TC','0.2 ?m','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (592,'K06-E20U-05-S','KrosFlo','KROSFLO 65CM 0.2UM MPES 0.5MM 3TC X 1.5TC STERILE','0.2 ?m','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (593,'K06-E20U-10-N','KrosFlo','KROSFLO 65CM .2UM MPES 1MM 3TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (594,'K06-E20U-10-S','KrosFlo','KROSFLO 65CM .2UM MPES 1MM 3TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (595,'K06-E300-05-N','KrosFlo','KROSFLO 65CM 300K MPES 0.5MM 3TC X 1.5TC','300 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (596,'K06-E300-05-S','KrosFlo','KROSFLO 65CM 300K MPES 0.5MM 3TC X 1.5TC STERILE','300 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (597,'K06-E300-10-N','KrosFlo','KROSFLO 65CM 300K MPES 1MM 3TC X 1.5TC','300 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (598,'K06-E300-10-S','KrosFlo','KROSFLO 65CM 300K MPES 1MM 3TC X 1.5TC STERILE','300 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (599,'K06-E500-05-N','KrosFlo','KROSFLO 65CM 500K MPES 0.5MM 3TC X 1.5TC','500 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (600,'K06-E500-05-S','KrosFlo','KROSFLO 65CM 500K MPES 0.5MM 3TC X 1.5TC  STERILE','500 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (601,'K06-E500-10-N','KrosFlo','KROSFLO 65CM 500K MPES 1MM 3TC X 1.5TC','500 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (602,'K06-E500-10-S','KrosFlo','+KROSFLO 65CM 500K MPES 1MM 3TC X 1.5TC STERILE','500 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (603,'K06-E65U-07-N','Krosflo','KROSFLO 65CM .65UM MPES .75MM 3TCX1.5TC 1/PK','0.65 ?m','mPES',0.75,69,65.0,1900,2.5,46.2,43.0,38.8,29000,545.33,937.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (604,'K06-E65U-07-S','Krosflo','KROSFLO 65CM .65UM MPES .75MM 3TCX1.5TC STERILE 1/PK','0.65 ?m','mPES',0.75,69,65.0,1900,2.5,46.2,43.0,38.8,29000,545.33,937.11,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (605,'K06-E750-05-N','KrosFlo','KROSFLO  65CM 750K MPES 0.5MM 3TC X 1.5TC','750 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (606,'K06-E750-05-S','KrosFlo','KROSFLO  65CM 750K MPES 0.5MM 3TC X 1.5TC STERILE','750 kD','mPES',0.5,69,65.0,4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (607,'K06-E750-10-N','KrosFlo','KROSFLO 65CM 750KD MPES 1MM 3TCX1.5TC 1/PK','750 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (608,'K06-E750-10-S','KrosFlo','KROSFLO 65CM 750KD MPES 1MM 3TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,69,65.0,1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (609,'K06-M10U-06-N','KrosFlo','KROSFLO 65CM 0.1 UM ME 0.63MM 3TC X 1.5TC','0.1 ?m','ME',0.63,69,65.0,3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (610,'K06-M10U-06-S','KrosFlo','KROSFLO 65CM 0.1 UM ME 0.63MM 3TC X 1.5TC STERILE','0.1 ?m','ME',0.63,69,65.0,3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (611,'K06-M20U-06-N','KrosFlo','KROSFLO 65CM 0.2 UM ME 0.6MM 3TC X 1.5TC','0.2 ?m','ME',0.63,69,65.0,3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (612,'K06-M20U-06-S','KrosFlo','KROSFLO 65CM 0.2 UM ME 0.6MM 3TC X 1.5TC STERILE','0.2 ?m','ME',0.63,69,65.0,3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (613,'K06-M20U-10-N','KrosFlo','KROSFLO 65CM 0.2 UM ME 1MM 3TC X 1.5TC','0.2 ?m','ME',1.0,69,65.0,1300,2.5,54.0,50.0,46.0,26500,663.33,1021.01,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (614,'K06-M20U-10-S','KrosFlo','KROSFLO 65CM 0.2 UM ME 1MM 3TC X 1.5TC STERILE','0.2 ?m','ME',1.0,69,65.0,1300,2.5,54.0,50.0,46.0,26500,663.33,1021.01,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (615,'K06-P20U-05-N','KrosFlo','KROSFLO 65CM 0.2 UM PES 0.5MM 3TC X 1.5TC','0.2 ?m','PES',0.5,69,65.0,5000,2.5,26.6,25.6,24.6,51000,637.81,1012.46,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (616,'K06-P20U-05-S','KrosFlo','KROSFLO 65CM 0.2 UM PES 0.5MM 3TC X 1.5TC STERILE','0.2 ?m','PES',0.5,69,65.0,5000,2.5,26.6,25.6,24.6,51000,637.81,1012.46,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (617,'K06-P20U-10-N','KrosFlo','KROSFLO 65CM 0.2 UM PES 1MM 3TC X 1.5TC','0.2 ?m','PES',1.0,69,65.0,1250,2.5,53.1,51.2,49.2,25500,637.81,1012.46,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (618,'K06-P20U-10-S','KrosFlo','KROSFLO 65CM 0.2 UM PES 1MM 3TC X 1.5TC STERILE','0.2 ?m','PES',1.0,69,65.0,1250,2.5,53.1,51.2,49.2,25500,637.81,1012.46,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (619,'K06-S010-05-N','KrosFlo','KROSFLO 65CM 10KD PS 0.5MM 3TC X 1.5TC','10 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (620,'K06-S010-05-P','KrosFlo','KROSFLO 65CM 10KD PS 0.5MM 3TC X 1.5TC WET','10 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (621,'K06-S010-05-S','KrosFlo','KROSFLO 65CM 10KD PS 0.5MM 3TC X 1.5TC STERILE','10 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (622,'K06-S050-05-N','KrosFlo','KROSFLO 65CM 50KD PS 0.5MM 3TC X 1.5TC','50 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (623,'K06-S050-05-P','KrosFlo','KROSFLO 65CM 50KD PS 0.5MM 3TC X 1.5TC WET','50 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (624,'K06-S050-05-S','KrosFlo','KROSFLO 65CM 50KD PS 0.5MM 3TC X 1.5TC STERILE','50 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (625,'K06-S05U-05-N','KrosFlo','KROSFLO 65CM 0.05 UM PS 0.5MM 3TC X 1.5TC','0.05 ?m','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (626,'K06-S05U-05-P','KrosFlo','KROSFLO 65CM 0.05 UM PS 0.5MM 3TC X 1.5TC WET','0.05 ?m','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (627,'K06-S05U-05-S','KrosFlo','KROSFLO 65CM 0.05 UM PS 0.5MM 3TC X 1.5TC STERILE','0.05 ?m','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (628,'K06-S500-05-N','KrosFlo','KROSFLO 65CM 500KD PS 0.5MM 3TC X 1.5TC','500 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (629,'K06-S500-05-P','KrosFlo','KROSFLO 65CM 500KD PS 0.5MM 3TC X 1.5TC WET','500 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (630,'K06-S500-05-S','KrosFlo','KROSFLO 65CM 500KD PS 0.5MM 3TC X 1.5TC STERILE','500 kD','PS',0.5,69,65.0,5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (631,'N02-E001-05-N','MiniKros','MINIKROS 20CM 1K MPES .5MM 1.5TCX3/4TC 1/PK','1 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'1222','3667',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (632,'N02-E001-05-S','MiniKros','MINIKROS 20CM 1K MPES .5MM 1.5TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'1222','3667',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (633,'N02-E003-05-N','MiniKros','MINIKROS  20CM 3K MPES 0.5MM 1.5TC X 3/4TC','3 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (634,'N02-E003-05-S','MiniKros','MINIKROS  20CM 3K MPES 0.5MM 1.5TC X 3/4TC STERILE','3 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (635,'N02-E003-10-N','MiniKros','MINIKROS 20CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK','3 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (636,'N02-E003-10-S','MiniKros','MINIKROS 20CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','3 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (637,'N02-E005-05-N','MiniKros','MINIKROS 20CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (638,'N02-E005-05-S','MiniKros','MINIKROS 20CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (639,'N02-E005-10-N','MiniKros','MINIKROS 20CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (640,'N02-E005-10-S','MiniKros','MINIKROS 20CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (641,'N02-E010-05-N','MiniKros','MINIKROS  20CM 10K MPES 0.5MM 1.5TC X 3/4TC','10 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,26.0,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (642,'N02-E010-05-S','MiniKros','MINIKROS  20CM 10K MPES 0.5MM 1.5TC X 3/4TC STERILE','10 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,26.0,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (643,'N02-E010-10-N','MiniKros','MINIKROS  20CM 10K MPES 1MM 1.5TC X 3/4TC','10 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (644,'N02-E010-10-S','MiniKros','MINIKROS  20CM 10K MPES 1MM 1.5TC X 3/4TC STERILE','10 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (645,'N02-E030-05-N','MiniKros','MINIKROS  20CM 30K MPES 0.5MM 1.5TC X 3/4TC','30 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (646,'N02-E030-05-S','MiniKros','MINIKROS  20CM 30K MPES 0.5MM 1.5TC X 3/4TC STERILE','30 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (647,'N02-E030-10-N','MiniKros','MINIKROS  20CM 30K MPES 1MM 1.5TC X 3/4TC','30 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (648,'N02-E030-10-S','MiniKros','MINIKROS  20CM 30K MPES 1MM 1.5TC X 3/4TC STERILE','30 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (649,'N02-E050-05-N','MiniKros','MINIKROS  20CM 50K MPES 0.5MM 1.5TC X 3/4TC','50 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (650,'N02-E050-05-S','MiniKros','MINIKROS  20CM 50K MPES 0.5MM 1.5TC X 3/4TC STERILE','50 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (651,'N02-E050-10-N','MiniKros','MINIKROS  20CM 50K MPES 1MM 1.5TC X 3/4TC','50 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (652,'N02-E050-10-S','MiniKros','MINIKROS  20CM 50K MPES 1MM 1.5TC X 3/4TC STERILE','50 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (653,'N02-E070-05-N','MiniKros','MINIKROS  20CM 70K MPES 0.5MM 1.5TC X 3/4TC','70 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (654,'N02-E070-05-S','MiniKros','MINIKROS  20CM 70K MPES 0.5MM 1.5TC X 3/4TCSTERILE','70 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (655,'N02-E070-10-N','MiniKros','MINIKROS  20CM 70K MPES 1MM 1.5TC X 3/4TC','70 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (656,'N02-E070-10-S','MiniKros','MINIKROS  20CM 70K MPES 1MM 1.5TC X 3/4TC STERILE','70 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (657,'N02-E100-05-N','MiniKros','MINIKROS  20CM 100K MPES 0.5MM 1.5TC X 3/4TC','100 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (658,'N02-E100-05-S','MiniKros','MINIKROS  20CM 100K MPES 0.5MM 1.5TC X 3/4TC STERILE','100 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (659,'N02-E100-10-N','MiniKros','MINIKROS  20CM 100K MPES 1MM 1.5TC X 3/4TC','100 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (660,'N02-E100-10-S','MiniKros','MINIKROS  20CM 100K MPES 1MM 1.5TC X 3/4TC STERILE','100 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (661,'N02-E20U-05-N','MiniKros','MINIKROS 20CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK','0.2 ?m','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (662,'N02-E20U-05-S','MiniKros','MINIKROS 20CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','0.2 ?m','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (663,'N02-E20U-10-N','MiniKros','MINIKROS 20CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK','0.2 ?m','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (664,'N02-E20U-10-S','MiniKros','MINIKROS 20CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK STERILE','0.2 ?m','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (665,'N02-E300-05-N','MiniKros','MINIKROS  20CM 300K MPES 0.5MM 1.5TC X 3/4TC','300 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (666,'N02-E300-05-S','MiniKros','MINIKROS  20CM 300K MPES 0.5MM 1.5TC X 3/4TC STERILE','300 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (667,'N02-E300-10-N','MiniKros','MINIKROS  20CM 300K MPES 1MM 1.5TC X 3/4TC','300 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (668,'N02-E300-10-S','MiniKros','MINIKROS  20CM 300K MPES 1MM 1.5TC X 3/4TC STERILE','300 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (669,'N02-E500-05-N','MiniKros','MINIKROS  20CM 500K MPES 0.5MM 1.5TC X 3/4TC','500 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (670,'N02-E500-05-S','MiniKros','MINIKROS  20CM 500K MPES 0.5MM 1.5TC X 3/4TC STERILE','500 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (671,'N02-E500-10-N','MiniKros','MINIKROS 20CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK','500 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (672,'N02-E500-10-S','MiniKros','MINIKROS 20CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','500 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (673,'N02-E65U-07-N','MiniKros','MINIKROS 20CM .65UM MPES .75MM 1.5TCX3/4TC 1/PK','0.65 ?m','mPES',0.75,25,20.0,400,1.125,46.2,43.0,38.8,1800,35.33,55.62,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (674,'N02-E65U-07-S','MiniKros','MINIKROS 20CM .65UM MPES .75MM 1.5TCX3/4TC STERILE 1/PK','0.65 ?m','mPES',0.75,25,20.0,400,1.125,46.2,43.0,38.8,1800,35.33,55.62,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (675,'N02-E750-05-N','MiniKros','MINIKROS  20CM 750K MPES 0.5MM 1.5TC X 3/4TC','750 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (676,'N02-E750-05-S','MiniKros','MINIKROS  20CM 750K MPES 0.5MM 1.5TC X 3/4TC STERILE','750 kD','mPES',0.5,25,20.0,830,1.125,31.5,29.5,27.5,2600,32.58,57.32,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (677,'N02-E750-10-N','MiniKros','MINIKROS 20CM 750KD MPES 1MM 1.5TCX1.5TC 1/PK','750 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (678,'N02-E750-10-S','MiniKros','MINIKROS 20CM 750KD MPES 1MM 1.5TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,25,20.0,250,1.125,57.0,52.5,48.0,1550,39.25,60.58,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (679,'N02-M10U-06-N','MiniKros','MINIKROS  20CM 0.1UM ME 0.63MM 1.5TC X 3/4TC','0.1 ?m','ME',0.63,25,20.0,590,1.125,37.0,34.0,31.0,2350,36.76,61.27,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (680,'N02-M10U-06-S','MiniKros','MINIKROS  20CM 0.1UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.1 ?m','ME',0.63,25,20.0,590,1.125,37.0,34.0,31.0,2350,36.76,61.27,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (681,'N02-M20U-06-N','MiniKros','MINIKROS  20CM 0.2um ME 0.63MM 1.5TC X 3/4TC','0.2 ?m','ME',0.63,25,20.0,590,1.125,37.0,34.0,31.0,2350,36.76,61.27,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (682,'N02-M20U-06-S','MiniKros','MINIKROS  20CM 0.2UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.2 ?m','ME',0.63,25,20.0,590,1.125,37.0,34.0,31.0,2350,36.76,61.27,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (683,'N02-M20U-10-N','MiniKros','MINIKROS  20CM 0.2UM ME 1MM 1.5TC X 3/4TC','0.2 ?m','ME',1.0,25,20.0,260,1.125,54.0,50.0,46.0,1650,40.82,64.41,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (684,'N02-M20U-10-S','MiniKros','MINIKROS  20CM 0.2UM ME 1MM 1.5TC X 3/4TC STERILE','0.2 ?m','ME',1.0,25,20.0,260,1.125,54.0,50.0,46.0,1650,40.82,64.41,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (685,'N02-P20U-05-N','MiniKros','MINIKROS  20CM 0.2UM PES 0.5MM 1.5TC X 3/4TC','0.2 ?m','PES',0.5,25,20.0,1000,1.125,26.6,25.6,24.6,3100,39.25,63.89,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (686,'N02-P20U-05-S','MiniKros','MINIKROS  20CM 0.2UM PES 0.5MM 1.5TC X 3/4TC STERILE','0.2 ?m','PES',0.5,25,20.0,1000,1.125,26.6,25.6,24.6,3100,39.25,63.89,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (687,'N02-P20U-10-N','MiniKros','MINIKROS  20CM 0.2UM PES 1MM 1.5TC X 3/4TC','0.2 ?m','PES',1.0,25,20.0,250,1.125,53.1,51.2,49.2,1570,39.25,63.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (688,'N02-P20U-10-S','MiniKros','MINIKROS  20CM 0.2UM PES 1MM 1.5TC X 3/4TC STERILE','0.2 ?m','PES',1.0,25,20.0,250,1.125,53.1,51.2,49.2,1570,39.25,63.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (689,'N02-S010-05-N','MiniKros','MINIKROS  20CM 10KD PS 0.5MM 1.5TC X 3/4TC','10 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (690,'N02-S010-05-P','MiniKros','MINIKROS  20CM 10KD PS 0.5MM 1.5TC X 3/4TC WET','10 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (691,'N02-S010-05-S','MiniKros','MINIKROS  20CM 10KD PS 0.5MM 1.5TC X 3/4TC STERILE','10 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (692,'N02-S050-05-N','MiniKros','MINIKROS  20CM 50KD PS 0.5MM 1.5TC X 3/4TC','50 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (693,'N02-S050-05-P','MiniKros','MINIKROS  20CM 50KD PS 0.5MM 1.5TC X 3/4TC WET','50 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (694,'N02-S050-05-S','MiniKros','MINIKROS  20CM 50KD PS 0.5MM 1.5TC X 3/4TC STERILE','50 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (695,'N02-S05U-05-N','MiniKros','MINIKROS  20CM 0.05UM PS 0.5MM 1.5TC X 3/4TC','0.05 ?m','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (696,'N02-S05U-05-P','MiniKros','MINIKROS  20CM 0.05UM PS 0.5MM 1.5TC X 3/4TC WET','0.05 ?m','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (697,'N02-S05U-05-S','MiniKros','MINIKROS  20CM 0.05UM  PS 0.5MM 1.5TC X 3/4TC STERILE','0.05 ?m','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (698,'N02-S500-05-N','MiniKros','MINIKROS  20CM 500KD PS 0.5MM 1.5TC X 3/4TC','500 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (699,'N02-S500-05-P','MiniKros','MINIKROS  20CM 500KD PS 0.5MM 1.5TC X 3/4TC WET','500 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (700,'N02-S500-05-S','MiniKros','MINIKROS  20CM 500KD PS 0.5MM 1.5TC X 3/4TC STERILE','500 kD','PS',0.5,25,20.0,1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (701,'N04-E001-05-N','MiniKros','MINIKROS 41.5CM 1K MPES .5MM 1.5TCX3/4TC 1/PK','1 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'1222','3667',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (702,'N04-E001-05-S','MiniKros','MINIKROS 41.5CM 1K MPES .5MM 1.5TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'1222','3667',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (703,'N04-E003-05-N','MiniKros','MINIKROS  41.5CM 3K MPES 0.5MM 1.5TC X 3/4TC','3 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (704,'N04-E003-05-S','MiniKros','MINIKROS  41.5CM 3K MPES 0.5MM 1.5TC X 3/4TC STERILE','3 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (705,'N04-E003-10-N','MiniKros','MINIKROS 41.5CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK','3 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (706,'N04-E003-10-S','MiniKros','MINIKROS 41.5CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','3 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (707,'N04-E005-05-N','MiniKros','MINIKROS 41.5CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (708,'N04-E005-05-S','MiniKros','MINIKROS 41.5CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (709,'N04-E005-10-N','MiniKros','MINIKROS 41.5CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (710,'N04-E005-10-S','MiniKros','MINIKROS 41.5CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (711,'N04-E010-05-N','MiniKros','MINIKROS  41.5CM 10K MPES 0.5MM 1.5TC X 3/4TC','10 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,26.0,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (712,'N04-E010-05-S','MiniKros','MINIKROS  41.5CM 10K MPES 0.5MM 1.5TC X 3/4TC STERILE','10 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,26.0,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (713,'N04-E010-10-N','MiniKros','MINIKROS  41.5CM 10K MPES 1MM 1.5TC X 3/4TC','10 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (714,'N04-E010-10-S','MiniKros','MINIKROS  41.5CM 10K MPES 1MM 1.5TC X 3/4TC STERILE','10 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (715,'N04-E030-05-N','MiniKros','MINIKROS  41.5CM 30K MPES 0.5MM 1.5TC X 3/4TC','30 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (716,'N04-E030-05-S','MiniKros','MINIKROS  41.5CM 30K MPES 0.5MM 1.5TC X 3/4TC STERILE','30 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (717,'N04-E030-10-N','MiniKros','MINIKROS  41.5CM 30K MPES 1MM 1.5TC X 3/4TC','30 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (718,'N04-E030-10-S','MiniKros','MINIKROS  41.5CM 30K MPES 1MM 1.5TC X 3/4TC STERILE','30 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (719,'N04-E050-05-N','MiniKros','MINIKROS  41.5CM 50K MPES 0.5MM 1.5TC X 3/4TC','50 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (720,'N04-E050-05-S','MiniKros','MINIKROS  41.5CM 50K MPES 0.5MM 1.5TC X 3/4TC STERILE','50 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (721,'N04-E050-10-N','MiniKros','MINIKROS  41.5CM 50K MPES 1MM 1.5TC X 3/4TC','50 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (722,'N04-E050-10-S','MiniKros','MINIKROS  41.5CM 50K MPES 1MM 1.5TC X 3/4TC STERILE','50 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (723,'N04-E070-05-N','MiniKros','MINIKROS  41.5CM 70K MPES 0.5MM 1.5TC X 3/4TC','70 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (724,'N04-E070-05-S','MiniKros','MINIKROS  41.5CM 70K MPES 0.5MM 1.5TC X 3/4TC STERILE','70 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (725,'N04-E070-10-N','MiniKros','MINIKROS  41.5CM 70K MPES 1MM 1.5TC X 3/4TC','70 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (726,'N04-E070-10-S','MiniKros','MINIKROS  41.5CM 70K MPES 1MM 1.5TC X 3/4TC STERILE','70 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (727,'N04-E100-05-N','MiniKros','MINIKROS  41.5CM 100K MPES 0.5MM 1.5TC X 3/4TC','100 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (728,'N04-E100-05-S','MiniKros','MINIKROS  41.5CM 100K MPES 0.5MM 1.5TC X 3/4TC STERILE','100 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (729,'N04-E100-10-N','MiniKros','MINIKROS  41.5CM 100K MPES 1MM 1.5TC X 3/4TC','100 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (730,'N04-E100-10-S','MiniKros','MINIKROS  41.5CM 100K MPES 1MM 1.5TC X 3/4TC STERILE','100 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (731,'N04-E20U-05-N','MiniKros','MINIKROS 41.5CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK','0.2 ?m','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (732,'N04-E20U-05-S','MiniKros','MINIKROS 41.5CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','0.2 ?m','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (733,'N04-E20U-10-N','MiniKros','MINIKROS 41.5CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK','0.2 ?m','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (734,'N04-E20U-10-S','MiniKros','MINIKROS 41.5CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK STERILE','0.2 ?m','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (735,'N04-E300-05-N','MiniKros','MINIKROS  41.5CM 300K MPES 0.5MM 1.5TC X 3/4TC','300 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (736,'N04-E300-05-S','MiniKros','MINIKROS  41.5CM 300K MPES 0.5MM 1.5TC X 3/4TC STERILE','300 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (737,'N04-E300-10-N','MiniKros','MINIKROS  41.5CM 300K MPES 1MM 1.5TC X 3/4TC','300 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (738,'N04-E300-10-S','MiniKros','MINIKROS  41.5CM 300K MPES 1MM 1.5TC X 3/4TC STERILE','300 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (739,'N04-E500-05-N','MiniKros','MINIKROS  41.5CM 500K MPES 0.5MM 1.5TC X 3/4TC','500 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (740,'N04-E500-05-S','MiniKros','MINIKROS  41.5CM 500K MPES 0.5MM 1.5TC X 3/4TC STERILE','500 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (741,'N04-E500-10-N','MiniKros','MINIKROS 41.5CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK','500 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (742,'N04-E500-10-S','MiniKros','MINIKROS 41.5CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','500 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (743,'N04-E65U-07-N','MiniKros','MINIKROS 41.5CM .65UM MPES .75MM 1.5TCX3/4TC 1/PK','0.65 ?m','mPES',0.75,46,41.5,400,1.125,46.2,43.0,38.8,3900,73.3,115.42,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (744,'N04-E65U-07-S','MiniKros','MINIKROS 41.5CM .65UM MPES .75MM 1.5TCX3/4TC STERILE 1/PK','0.65 ?m','mPES',0.75,46,41.5,400,1.125,46.2,43.0,38.8,3900,73.3,115.42,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (745,'N04-E750-05-N','MiniKros','MINIKROS  41.5CM 750K MPES 0.5MM 1.5TC X 3/4TC','750 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (746,'N04-E750-05-S','MiniKros','MINIKROS  41.5CM 750K MPES 0.5MM 1.5TC X 3/4TC STERILE','750 kD','mPES',0.5,46,41.5,830,1.125,31.5,29.5,27.5,5400,67.6,118.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (747,'N04-E750-10-N','MiniKros','MINIKROS 41.5CM 750KD MPES 1MM 1.5TCX1.5TC 1/PK','750 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (748,'N04-E750-10-S','MiniKros','MINIKROS 41.5CM 750KD MPES 1MM 1.5TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,46,41.5,250,1.125,57.0,52.5,48.0,3250,81.44,125.71,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (749,'N04-M10U-06-N','MiniKros','MINIKROS  41.5CM 0.1UM ME 0.63MM 1.5TC X 3/4TC','0.1 ?m','ME',0.63,46,41.5,590,1.125,37.0,34.0,31.0,4900,76.29,127.14,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (750,'N04-M10U-06-S','MiniKros','MINIKROS  41.5CM 0.1UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.1 ?m','ME',0.63,46,41.5,590,1.125,37.0,34.0,31.0,4900,76.29,127.14,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (751,'N04-M20U-06-N','MiniKros','MINIKROS  41.5CM 0.2 UM ME 0.63MM 1.5TC X 3/4TC','0.2 ?m','ME',0.63,46,41.5,590,1.125,37.0,34.0,31.0,4900,76.29,127.14,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (752,'N04-M20U-06-S','MiniKros','MINIKROS  41.5CM 0.2UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.2 ?m','ME',0.63,46,41.5,590,1.125,37.0,34.0,31.0,4900,76.29,127.14,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (753,'N04-M20U-10-N','MiniKros','MINIKROS  41.5CM 0.2UM ME 1MM 1.5TC X 3/4TC','0.2 ?m','ME',1.0,46,41.5,260,1.125,54.0,50.0,46.0,3400,84.7,133.66,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (754,'N04-M20U-10-S','MiniKros','MINIKROS  41.5CM 0.2UM ME 1MM 1.5TC X 3/4TC STERILE','0.2 ?m','ME',1.0,46,41.5,260,1.125,54.0,50.0,46.0,3400,84.7,133.66,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (755,'N04-P20U-05-N','MiniKros','MINIKROS  41.5CM 0.2UM PES 0.5MM 1.5TC X 3/4TC','0.2 ?m','PES',0.5,46,41.5,1000,1.125,26.6,25.6,24.6,6500,81.44,132.57,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (756,'N04-P20U-05-S','MiniKros','MINIKROS  41.5CM 0.2UM PES 0.5MM 1.5TC X 3/4TC STERILE','0.2 ?m','PES',0.5,46,41.5,1000,1.125,26.6,25.6,24.6,6500,81.44,132.57,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (757,'N04-P20U-10-N','MiniKros','MINIKROS  41.5CM 0.2UM PES 1MM 1.5TC X 3/4TC','0.2 ?m','PES',1.0,46,41.5,250,1.125,53.1,51.2,49.2,3260,81.44,132.57,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (758,'N04-P20U-10-S','MiniKros','MINIKROS  41.5CM 0.2UM PES 1MM 1.5TC X 3/4TC STERILE','0.2 ?m','PES',1.0,46,41.5,250,1.125,53.1,51.2,49.2,3260,81.44,132.57,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (759,'N04-S010-05-N','MiniKros','MINIKROS  41.5CM 10KD PS 0.5MM 1.5TC X 3/4TC','10 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (760,'N04-S010-05-P','MiniKros','MINIKROS  41.5CM 10KD PS 0.5MM 1.5TC X 3/4TC WET','10 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (761,'N04-S010-05-S','MiniKros','MINIKROS  41.5CM 10KD PS 0.5MM 1.5TC X 3/4TC STERILE','10 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (762,'N04-S050-05-N','MiniKros','MINIKROS  41.5CM 50KD PS 0.5MM 1.5TC X 3/4TC','50 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (763,'N04-S050-05-P','MiniKros','MINIKROS  41.5CM 50KD PS 0.5MM 1.5TC X 3/4TC WET','50 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (764,'N04-S050-05-S','MiniKros','MINIKROS  41.5CM 50KD PS 0.5MM 1.5TC X 3/4TC STERILE','50 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (765,'N04-S05U-05-N','MiniKros','MINIKROS  41.5CM 0.05UM PS 0.5MM 1.5TC X 3/4TC','0.05 ?m','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (766,'N04-S05U-05-P','MiniKros','MINIKROS  41.5CM 0.05UM PS 0.5MM 1.5TC X 3/4TC WET','0.05 ?m','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (767,'N04-S05U-05-S','MiniKros','MINIKROS  41.5CM 0.05UM PS 0.5MM 1.5TC X 3/4TC STERILE','0.05 ?m','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (768,'N04-S500-05-N','MiniKros','MINIKROS  41.5CM 500KD PS 0.5MM 1.5TC X 3/4TC','500 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (769,'N04-S500-05-P','MiniKros','MINIKROS  41.5CM 500KD PS 0.5MM 1.5TC X 3/4TC WET','500 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (770,'N04-S500-05-S','MiniKros','MINIKROS  41.5CM 500KD PS 0.5MM 1.5TC X 3/4TC STERILE','500 kD','PS',0.5,46,41.5,1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (771,'N06-E001-05-N','MiniKros','MINIKROS 65CM 1K MPES .5MM 1.5TCX3/4TC 1/PK','1 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'1222','3667',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (772,'N06-E001-05-S','MiniKros','MINIKROS 65CM 1K MPES .5MM 1.5TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'1222','3667',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (773,'N06-E003-05-N','MiniKros','MINIKROS  65CM 3K MPES 0.5MM 1.5TC X 3/4TC','3 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (774,'N06-E003-05-S','MiniKros','MINIKROS  65CM 3K MPES 0.5MM 1.5TC X 3/4TC STERILE','3 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (775,'N06-E003-10-N','MiniKros','MINIKROS 65CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK','3 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (776,'N06-E003-10-S','MiniKros','MINIKROS 65CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','3 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (777,'N06-E005-05-N','MiniKros','MINIKROS 65CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (778,'N06-E005-05-S','MiniKros','MINIKROS 65CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (779,'N06-E005-10-N','MiniKros','MINIKROS 65CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (780,'N06-E005-10-S','MiniKros','MINIKROS 65CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (781,'N06-E010-05-N','MiniKros','MINIKROS  65CM 10K MPES 0.5MM 1.5TC X 3/4TC','10 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,26.0,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (782,'N06-E010-05-S','MiniKros','MINIKROS  65CM 10K MPES 0.5MM 1.5TC X 3/4TC STERILE','10 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,26.0,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (783,'N06-E010-10-N','MiniKros','MINIKROS  65CM 10K MPES 1MM 1.5TC X 3/4TC','10 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (784,'N06-E010-10-S','MiniKros','MINIKROS  65CM 10K MPES 1MM 1.5TC X 3/4TC STERILE','10 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (785,'N06-E030-05-N','MiniKros','MINIKROS  65CM 30K MPES 0.5MM 1.5TC X 3/4TC','30 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (786,'N06-E030-05-S','MiniKros','MINIKROS  65CM 30K MPES 0.5MM 1.5TC X 3/4TC STERILE','30 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (787,'N06-E030-10-N','MiniKros','MINIKROS  65CM 30K MPES 1MM 1.5TC X 3/4TC','30 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (788,'N06-E030-10-S','MiniKros','MINIKROS  65CM 30K MPES 1MM 1.5TC X 3/4TC STERILE','30 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (789,'N06-E050-05-N','MiniKros','MINIKROS  65CM 50K MPES 0.5MM 1.5TC X 3/4TC','50 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (790,'N06-E050-05-S','MiniKros','MINIKROS  65CM 50K MPES 0.5MM 1.5TC X 3/4TC STERILE','50 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (791,'N06-E050-10-N','MiniKros','MINIKROS  65CM 50K MPES 1MM 1.5TC X 3/4TC','50 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (792,'N06-E050-10-S','MiniKros','MINIKROS  65CM 50K MPES 1MM 1.5TC X 3/4TC STERILE','50 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (793,'N06-E070-05-N','MiniKros','MINIKROS  65CM 70K MPES 0.5MM 1.5TC X 3/4TC','70 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (794,'N06-E070-05-S','MiniKros','MINIKROS  65CM 70K MPES 0.5MM 1.5TC X 3/4TC STERILE','70 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (795,'N06-E070-10-N','MiniKros','MINIKROS  65CM 70K MPES 1MM 1.5TC X 3/4TC','70 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (796,'N06-E070-10-S','MiniKros','MINIKROS  65CM 70K MPES 1MM 1.5TC X 3/4TC STERILE','70 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (797,'N06-E100-05-N','MiniKros','MINIKROS  65CM 100K MPES 0.5MM 1.5TC X 3/4TC','100 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (798,'N06-E100-05-S','MiniKros','MINIKROS  65CM 100K MPES 0.5MM 1.5TC X 3/4TC STERILE','100 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (799,'N06-E100-10-N','MiniKros','MINIKROS  65CM 100K MPES 1MM 1.5TC X 3/4TC','100 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (800,'N06-E100-10-S','MiniKros','MINIKROS  65CM 100K MPES 1MM 1.5TC X 3/4TC STERILE','100 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (801,'N06-E20U-05-N','MiniKros','MINIKROS 65CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK','0.2 ?m','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (802,'N06-E20U-05-S','MiniKros','MINIKROS 65CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','0.2 ?m','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (803,'N06-E20U-10-N','MiniKros','MINIKROS 65CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK','0.2 ?m','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (804,'N06-E20U-10-S','MiniKros','MINIKROS 65CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK STERILE','0.2 ?m','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (805,'N06-E300-05-N','MiniKros','MINIKROS  65CM 300K MPES 0.5MM 1.5TC X 3/4TC','300 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (806,'N06-E300-05-S','MiniKros','MINIKROS  65CM 300K MPES 0.5MM 1.5TC X 3/4TC STERILE','300 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (807,'N06-E300-10-N','MiniKros','MINIKROS  65CM 300K MPES 1MM 1.5TC X 3/4TC','300 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (808,'N06-E300-10-S','MiniKros','MINIKROS  65CM 300K MPES 1MM 1.5TC X 3/4TC STERILE','300 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (809,'N06-E500-05-N','MiniKros','MINIKROS  65CM 500K MPES 0.5MM 1.5TC X 3/4TC','500 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (810,'N06-E500-05-S','MiniKros','MINIKROS  65CM 500K MPES 0.5MM 1.5TC X 3/4TC STERILE','500 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (811,'N06-E500-10-N','MiniKros','MINIKROS 65CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK','500 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (812,'N06-E500-10-S','MiniKros','MINIKROS 65CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','500 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (813,'N06-E65U-07-N','MiniKros','MINIKROS 65CM .65UM MPES .75MM 1.5TCX3/4TC 1/PK','0.65 ?m','mPES',0.75,70,65.0,400,1.125,46.2,43.0,38.8,6100,114.81,180.77,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (814,'N06-E65U-07-S','MiniKros','MINIKROS 65CM .65UM MPES .75MM 1.5TCX3/4TC STERILE 1/PK','0.65 ?m','mPES',0.75,70,65.0,400,1.125,46.2,43.0,38.8,6100,114.81,180.77,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (815,'N06-E750-05-N','MiniKros','MINIKROS  65CM 750K MPES 0.5MM 1.5TC X 3/4TC','750 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (816,'N06-E750-05-S','MiniKros','MINIKROS  65CM 750K MPES 0.5MM 1.5TC X 3/4TC STERILE','750 kD','mPES',0.5,70,65.0,830,1.125,31.5,29.5,27.5,8500,105.88,186.29,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (817,'N06-E750-10-N','MiniKros','MINIKROS 65CM 750KD MPES 1MM 1.5TCX1.5TC 1/PK','750 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (818,'N06-E750-10-S','MiniKros','MINIKROS 65CM 750KD MPES 1MM 1.5TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,70,65.0,250,1.125,57.0,52.5,48.0,5100,127.56,196.89,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (819,'N06-M10U-06-N','MiniKros','MINIKROS  65CM 0.1UM ME 0.63MM 1.5TC X 3/4TC','0.1 ?m','ME',0.63,70,65.0,590,1.125,37.0,34.0,31.0,7650,119.49,199.13,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (820,'N06-M10U-06-S','MiniKros','MINIKROS  65CM 0.1UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.1 ?m','ME',0.63,70,65.0,590,1.125,37.0,34.0,31.0,7650,119.49,199.13,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (821,'N06-M20U-06-N','MiniKros','MINIKROS  65CM 0.2UM ME 0.63MM 1.5TC X 3/4TC','0.2 ?m','ME',0.63,70,65.0,590,1.125,37.0,34.0,31.0,7650,119.49,199.13,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (822,'N06-M20U-06-S','MiniKros','MINIKROS  65CM 0.2UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.2 ?m','ME',0.63,70,65.0,590,1.125,37.0,34.0,31.0,7650,119.49,199.13,'#VALUE!','#VALUE!',18,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (823,'N06-M20U-10-N','MiniKros','MINIKROS  65CM 0.2UM ME 1MM 1.5TC X 3/4TC','0.2 ?m','ME',1.0,70,65.0,260,1.125,54.0,50.0,46.0,5300,132.67,209.35,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (824,'N06-M20U-10-S','MiniKros','MINIKROS  65CM 0.2UM ME 1MM 1.5TC X 3/4TC STERILE','0.2 ?m','ME',1.0,70,65.0,260,1.125,54.0,50.0,46.0,5300,132.67,209.35,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (825,'N06-P20U-05-N','MiniKros','MINIKROS  65CM 0.2UM PES 0.5MM 1.5TC X 3/4TC','0.2 ?m','PES',0.5,70,65.0,1000,1.125,26.6,25.6,24.6,10000,127.56,207.64,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (826,'N06-P20U-05-S','MiniKros','MINIKROS  65CM 0.2UM PES 0.5MM 1.5TC X 3/4TC STERILE','0.2 ?m','PES',0.5,70,65.0,1000,1.125,26.6,25.6,24.6,10000,127.56,207.64,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (827,'N06-P20U-10-N','MiniKros','MINIKROS  65CM 0.2UM PES 1MM 1.5TC X 3/4TC','0.2 ?m','PES',1.0,70,65.0,250,1.125,53.1,51.2,49.2,5100,127.56,207.64,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (828,'N06-P20U-10-S','MiniKros','MINIKROS  65CM 0.2UM PES 1MM 1.5TC X 3/4TC STERILE','0.2 ?m','PES',1.0,70,65.0,250,1.125,53.1,51.2,49.2,5100,127.56,207.64,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (829,'N06-S010-05-N','MiniKros','MINIKROS  65CM 10KD PS 0.5MM 1.5TC X 3/4TC','10 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (830,'N06-S010-05-P','MiniKros','MINIKROS  65CM 10KD PS 0.5MM 1.5TC X 3/4TC WET','10 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (831,'N06-S010-05-S','MiniKros','MINIKROS  65CM 10KD PS 0.5MM 1.5TC X 3/4TC STERILE','10 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (832,'N06-S050-05-N','MiniKros','MINIKROS  65CM 50KD PS 0.5MM 1.5TC X 3/4TC','50 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (833,'N06-S050-05-P','MiniKros','MINIKROS  65CM 50KD PS 0.5MM 1.5TC X 3/4TC WET','50 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (834,'N06-S050-05-S','MiniKros','MINIKROS  65CM 50KD PS 0.5MM 1.5TC X 3/4TC STERILE','50 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (835,'N06-S05U-05-N','MiniKros','MINIKROS  65CM 0.05UM PS 0.5MM 1.5TC X 3/4TC','0.05 ?m','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (836,'N06-S05U-05-P','MiniKros','MINIKROS  65CM 0.05UM PS 0.5MM 1.5TC X 3/4TC WET','0.05 ?m','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (837,'N06-S05U-05-S','MiniKros','MINIKROS  65CM 0.05UM PS 0.5MM 1.5TC X 3/4TC STERILE','0.05 ?m','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (838,'N06-S500-05-N','MiniKros','MINIKROS  65CM 500KD PS 0.5MM 1.5TC X 3/4TC','500 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (839,'N06-S500-05-P','MiniKros','MINIKROS  65CM 500KD PS 0.5MM 1.5TC X 3/4TC WET','500 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (840,'N06-S500-05-S','MiniKros','MINIKROS  65CM 500KD PS 0.5MM 1.5TC X 3/4TC STERILE','500 kD','PS',0.5,70,65.0,1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (841,'S02-E001-05-N','MiniKros Sampler','SAMPLER 20CM 1K MPES .5MM 3/4TCX3/4TC 1/PK','1 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'368','1104',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (842,'S02-E001-05-S','MiniKros Sampler','SAMPLER 20CM 1K MPES .5MM 3/4TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'368','1104',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (843,'S02-E003-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 0.5MM 3/4TC X 3/4TC','3 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (844,'S02-E003-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 0.5MM 3/4TC X 3/4TC STERILE','3 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (845,'S02-E003-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 1MM 3/4TC X 3/4TC','3 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (846,'S02-E003-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 1MM 3/4TC X 3/4TC STERILE','3 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (847,'S02-E005-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 0.5MM 3/4TC X 3/4TC','5 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (848,'S02-E005-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 0.5MM 3/4TC X 3/4TC STERILE','5 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (849,'S02-E005-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 1MM 3/4TC X 3/4TC','5 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (850,'S02-E005-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 1MM 3/4TC X 3/4TC STERILE','5 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (851,'S02-E010-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 0.5MM 3/4TC X 3/4TC','10 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,26.0,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (852,'S02-E010-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 0.5MM 3/4TC X 3/4TC IRR','10 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,26.0,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (853,'S02-E010-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 1MM 3/4TC X 3/4TC','10 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (854,'S02-E010-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 1MM 3/4TC X 3/4TC STERILE','10 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (855,'S02-E030-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 0.5MM 3/4TC X 3/4TC','30 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (856,'S02-E030-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 0.5MM 3/4TC X 3/4TC STERILE','30 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (857,'S02-E030-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 1MM 3/4TC X 3/4TC','30 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (858,'S02-E030-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 1MM 3/4TC X 3/4TC STERILE','30 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (859,'S02-E050-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 0.5MM 3/4TC X 3/4TC','50 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (860,'S02-E050-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 0.5MM 3/4TC X 3/4TC STERILE','50 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (861,'S02-E050-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 1MM 3/4TC X 3/4TC','50 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (862,'S02-E050-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 1MM 3/4TC X 3/4TC STERILE','50 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (863,'S02-E070-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 0.5MM 3/4TC X 3/4TC','70 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (864,'S02-E070-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 0.5MM 3/4TC X 3/4TC STERILE','70 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (865,'S02-E070-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 1MM 3/4TC X 3/4TC','70 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (866,'S02-E070-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 1MM 3/4TC X 3/4TC STERILE','70 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (867,'S02-E100-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 0.5MM 3/4TC X 3/4TC','100 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (868,'S02-E100-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 0.5MM 3/4TC X 3/4TC STERILE','100 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (869,'S02-E100-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 1MM 3/4TC X 3/4TC','100 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (870,'S02-E100-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 1MM 3/4TC X 3/4TC STERILE','100 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (871,'S02-E20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC','0.2 ?m','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (872,'S02-E20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC STERILE','0.2 ?m','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (873,'S02-E20U-10-N','MiniKros Sampler','SAMPLER 20CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK','0.2 ?m','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (874,'S02-E20U-10-S','MiniKros Sampler','SAMPLER 20CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK STERILE','0.2 ?m','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (875,'S02-E300-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 0.5MM 3/4TC X 3/4TC','300 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (876,'S02-E300-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 0.5MM 3/4TC X 3/4TC STERILE','300 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (877,'S02-E300-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 1MM 3/4TC X 3/4TC','300 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (878,'S02-E300-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 1MM 3/4TC X 3/4TC STERILE','300 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (879,'S02-E45U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM .45UM MPES 0.5MM 3/4TC X 3/4TC','0.45 ?m','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,'Special Part for Mike B, not released as standard');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (880,'S02-E500-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 0.5MM 3/4TC X 3/4TC','500 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (881,'S02-E500-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 0.5MM 3/4TC X 3/4TC STERILE','500 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (882,'S02-E500-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 1MM 3/4TC X 3/4TC','500 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (883,'S02-E500-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 1MM 3/4TC X 3/4TC STERILE','500 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (884,'S02-E65U-07-N','MiniKros Sampler','SAMPLER 20CM .65UM MPES .75MM 3/4TCX3/4TC 1/PK','0.65 ?m','mPES',0.75,25,20.0,110,0.625,46.2,43.0,38.8,520,9.71,19.61,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (885,'S02-E65U-07-S','MiniKros Sampler','SAMPLER 20CM .65UM MPES .75MM 3/4TCX3/4TC STERILE 1/PK','0.65 ?m','mPES',0.75,25,20.0,110,0.625,46.2,43.0,38.8,520,9.71,19.61,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (886,'S02-E750-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 750K MPES 0.5MM 3/4TC X 3/4TC','750 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (887,'S02-E750-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 750K MPES 0.5MM 3/4TC X 3/4TC STERILE','750 kD','mPES',0.5,25,20.0,250,0.625,31.5,29.5,27.5,790,9.81,18.22,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (888,'S02-E750-10-N','MiniKros Sampler','SAMPLER 20CM 750KD MPES 1MM 3/4TCX3/4TC 1/PK','750 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (889,'S02-E750-10-S','MiniKros Sampler','SAMPLER 20CM 750KD MPES 1MM 3/4TCX3/4TC STERILE 1/PK','750 kD','mPES',1.0,25,20.0,78,0.625,57.0,52.5,48.0,490,12.25,18.47,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (890,'S02-M10U-06-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.1UM ME 0.6MM 3/4TC X 3/4TC','0.1 ?m','ME',0.63,25,20.0,180,0.625,37.0,34.0,31.0,720,11.22,19.15,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (891,'S02-M10U-06-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.1UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.1 ?m','ME',0.63,25,20.0,180,0.625,37.0,34.0,31.0,720,11.22,19.15,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (892,'S02-M20U-06-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 0.6MM 3/4TC X 3/4TC','0.2 ?m','ME',0.63,25,20.0,180,0.625,37.0,34.0,31.0,720,11.22,19.15,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (893,'S02-M20U-06-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.2 ?m','ME',0.63,25,20.0,180,0.625,37.0,34.0,31.0,720,11.22,19.15,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (894,'S02-M20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 1MM 3/4TC X 3/4TC','0.2 ?m','ME',1.0,25,20.0,80,0.625,54.0,50.0,46.0,500,12.56,19.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (895,'S02-M20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 1MM 3/4TC X 3/4TC STERILE','0.2 ?m','ME',1.0,25,20.0,80,0.625,54.0,50.0,46.0,500,12.56,19.94,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (896,'S02-P20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 0.5MM 3/4TC X 3/4TC','0.2 ?m','PES',0.5,25,20.0,330,0.625,26.6,25.6,24.6,1000,12.95,18.35,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (897,'S02-P20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 0.5MM 3/4TC X 3/4TC STERILE','0.2 ?m','PES',0.5,25,20.0,330,0.625,26.6,25.6,24.6,1000,12.95,18.35,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (898,'S02-P20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 1MM 3/4TC X 3/4TC','0.2 ?m','PES',1.0,25,20.0,75,0.625,53.1,51.2,49.2,470,11.78,20.27,'#VALUE!','#VALUE!',25,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (899,'S02-P20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 1MM 3/4TC X 3/4TC STERILE','0.2 ?m','PES',1.0,25,20.0,75,0.625,53.1,51.2,49.2,470,11.78,20.27,'#VALUE!','#VALUE!',25,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (900,'S02-S010-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 10KD PS 0.5MM 3/4TC X 3/4TC','10 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (901,'S02-S010-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 10KD PS 0.5MM 3/4TC X 3/4TC WET','10 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (902,'S02-S010-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 10KD PS 0.5MM 3/4TC X 3/4TC STERILE','10 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (903,'S02-S050-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 50KD PS 0.5MM 3/4TC X 3/4TC','50 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (904,'S02-S050-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 50KD PS 0.5MM 3/4TC X 3/4TC WET','50 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (905,'S02-S050-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 50KD PS 0.5MM 3/4TC X 3/4TC STERILE','50 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (906,'S02-S05U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC','0.05 ?m','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (907,'S02-S05U-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC WET','0.05 ?m','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (908,'S02-S05U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.05  PS 0.5MM 3/4TC X 3/4TC STERILE','0.05 ?m','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (909,'S02-S500-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 500KD PS 0.5MM 3/4TC X 3/4TC','500 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (910,'S02-S500-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 500KD PS 0.5MM 3/4TC X 3/4TC WET','500 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (911,'S02-S500-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 500KD PS 0.5MM 3/4TC X 3/4TC STERILE','500 kD','PS',0.5,25,20.0,330,0.625,26.8,23.6,20.5,1000,12.95,21.53,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (912,'S04-E001-05-N','MiniKros Sampler','SAMPLER 41.5CM 1K MPES .5MM 3/4TCX3/4TC 1/PK','1 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'368','1104',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (913,'S04-E001-05-S','MiniKros Sampler','SAMPLER 41.5CM 1K MPES .5MM 3/4TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'368','1104',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (914,'S04-E003-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 0.5MM 3/4TC X 3/4TC','3 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (915,'S04-E003-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 0.5MM 3/4TC X 3/4TC STERILE','3 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (916,'S04-E003-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 1MM 3/4TC X 3/4TC','3 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (917,'S04-E003-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 1MM 3/4TC X 3/4TC STERILE','3 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (918,'S04-E005-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 0.5MM 3/4TC X 3/4TC','5 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (919,'S04-E005-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 0.5MM 3/4TC X 3/4TC STERILE','5 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (920,'S04-E005-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 1MM 3/4TC X 3/4TC','5 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (921,'S04-E005-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 1MM 3/4TC X 3/4TC STERILE','5 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (922,'S04-E010-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 0.5MM 3/4TC X 3/4TC','10 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,26.0,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (923,'S04-E010-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 0.5MM 3/4TC X 3/4TC STERILE','10 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,26.0,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (924,'S04-E010-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 1MM 3/4TC X 3/4TC','10 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (925,'S04-E010-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 1MM 3/4TC X 3/4TC STERILE','10 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (926,'S04-E030-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 0.5MM 3/4TC X 3/4TC','30 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (927,'S04-E030-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 0.5MM 3/4TC X 3/4TC STERILE','30 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (928,'S04-E030-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 1MM 3/4TC X 3/4TC','30 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (929,'S04-E030-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 1MM 3/4TC X 3/4TC STERILE','30 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (930,'S04-E050-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 0.5MM 3/4TC X 3/4TC','50 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (931,'S04-E050-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 0.5MM 3/4TC X 3/4TC STERILE','50 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (932,'S04-E050-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 1MM 3/4TC X 3/4TC','50 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (933,'S04-E050-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 1MM 3/4TC X 3/4TC STERILE','50 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (934,'S04-E070-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 0.5MM 3/4TC X 3/4TC','70 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (935,'S04-E070-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 0.5MM 3/4TC X 3/4TC STERILE','70 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (936,'S04-E070-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 1MM 3/4TC X 3/4TC','70 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (937,'S04-E070-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 1MM 3/4TC X 3/4TC STERILE','70 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (938,'S04-E100-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 0.5MM 3/4TC X 3/4TC','100 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (939,'S04-E100-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 0.5MM 3/4TC X 3/4TC STERIL','100 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (940,'S04-E100-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 1MM 3/4TC X 3/4TC','100 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (941,'S04-E100-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 1MM 3/4TC X 3/4TC STERILE','100 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (942,'S04-E20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC','0.2 ?m','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (943,'S04-E20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC STERL','0.2 ?m','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (944,'S04-E20U-10-N','MiniKros Sampler','SAMPLER 41.5CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK','0.2 ?m','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (945,'S04-E20U-10-S','MiniKros Sampler','SAMPLER 41.5CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK STERILE','0.2 ?m','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (946,'S04-E300-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 0.5MM 3/4TC X 3/4TC','300 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (947,'S04-E300-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 0.5MM 3/4TC X 3/4TC STERIL','300 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (948,'S04-E300-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 1MM 3/4TC X 3/4TC','300 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (949,'S04-E300-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 1MM 3/4TC X 3/4TC STERILE','300 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (950,'S04-E500-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 0.5MM 3/4TC X 3/4TC','500 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (951,'S04-E500-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 0.5MM 3/4TC X 3/4TCSTERILE','500 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (952,'S04-E500-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 1MM 3/4TC X 3/4TC','500 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (953,'S04-E500-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 1MM 3/4TC X 3/4TC STERILE','500 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (954,'S04-E65U-07-N','MiniKros Sampler','SAMPLER 41.5CM .65UM MPES .75MM 3/4TCX3/4TC 1/PK','0.65 ?m','mPES',0.75,47,41.5,110,0.625,46.2,43.0,38.8,1075,20.16,40.69,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (955,'S04-E65U-07-S','MiniKros Sampler','SAMPLER 41.5CM .65UM MPES .75MM 3/4TCX3/4TC STERILE 1/PK','0.65 ?m','mPES',0.75,47,41.5,110,0.625,46.2,43.0,38.8,1075,20.16,40.69,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (956,'S04-E750-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 750K MPES 0.5MM 3/4TC X 3/4TC','750 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (957,'S04-E750-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 750K MPES 0.5MM 3/4TC X 3/4TC STERI','750 kD','mPES',0.5,47,41.5,250,0.625,31.5,29.5,27.5,1600,20.36,37.8,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (958,'S04-E750-10-N','MiniKros Sampler','SAMPLER 41.5CM 750KD MPES 1MM 3/4TCX3/4TC 1/PK','750 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (959,'S04-E750-10-S','MiniKros Sampler','SAMPLER 41.5CM 750KD MPES 1MM 3/4TCX3/4TC STERILE 1/PK','750 kD','mPES',1.0,47,41.5,78,0.625,57.0,52.5,48.0,1000,25.41,38.33,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (960,'S04-M10U-06-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.1UM ME 0.6MM 3/4TC X 3/4TC','0.1 ?m','ME',0.63,47,41.5,180,0.625,37.0,34.0,31.0,1500,23.27,39.73,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (961,'S04-M10U-06-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.1UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.1 ?m','ME',0.63,47,41.5,180,0.625,37.0,34.0,31.0,1500,23.27,39.73,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (962,'S04-M20U-06-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 0.6MM 3/4TC X 3/4TC','0.2 ?m','ME',0.63,47,41.5,180,0.625,37.0,34.0,31.0,1500,23.27,39.73,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (963,'S04-M20U-06-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.2 ?m','ME',0.63,47,41.5,180,0.625,37.0,34.0,31.0,1500,23.27,39.73,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (964,'S04-M20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 1MM 3/4TC X 3/4TC','0.2 ?m','ME',1.0,47,41.5,80,0.625,54.0,50.0,46.0,1050,26.06,41.38,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (965,'S04-M20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 1MM 3/4TC X 3/4TC STERILE','0.2 ?m','ME',1.0,47,41.5,80,0.625,54.0,50.0,46.0,1050,26.06,41.38,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (966,'S04-P20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 0.5MM 3/4TC X 3/4TC','0.2 ?m','PES',0.5,47,41.5,330,0.625,26.6,25.6,24.6,2200,26.88,38.07,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (967,'S04-P20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 0.5MM 3/4TCX3/4TC STERILE','0.2 ?m','PES',0.5,47,41.5,330,0.625,26.6,25.6,24.6,2200,26.88,38.07,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (968,'S04-P20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 1MM 3/4TC X 3/4TC','0.2 ?m','PES',1.0,47,41.5,75,0.625,53.1,51.2,49.2,980,24.43,42.07,'#VALUE!','#VALUE!',25,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (969,'S04-P20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 1MM 3/4TC X 3/4TC STERILE','0.2 ?m','PES',1.0,47,41.5,75,0.625,53.1,51.2,49.2,980,24.43,42.07,'#VALUE!','#VALUE!',25,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (970,'S04-S010-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10KD PS 0.5MM 3/4TC X 3/4TC','10 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (971,'S04-S010-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10KD PS 0.5MM 3/4TC X 3/4TC WET','10 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (972,'S04-S010-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10KD PS 0.5MM 3/4TC X 3/4TC STERILE','10 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (973,'S04-S050-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50KD PS 0.5MM 3/4TC X 3/4TC','50 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (974,'S04-S050-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50KD PS 0.5MM 3/4TC X 3/4TC WET','50 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (975,'S04-S050-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50KD PS 0.5MM 3/4TC X 3/4TC STERILE','50 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (976,'S04-S05U-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC','0.05 ?m','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (977,'S04-S05U-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC WET','0.05 ?m','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (978,'S04-S05U-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.05UM PS0.5MM 3/4TC X 3/4TC STERILE','0.05 ?m','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (979,'S04-S500-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500KD PS 0.5MM 3/4TC X 3/4TC','500 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (980,'S04-S500-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500KD PS 0.5MM 3/4TC X 3/4TC WET','500 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (981,'S04-S500-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500KD PS 0.5MM 3/4TC X 3/4TC STERILE','500 kD','PS',0.5,47,41.5,330,0.625,26.8,23.6,20.5,2200,26.88,44.68,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (982,'S06-E001-05-N','MiniKros Sampler','SAMPLER 65CM 1K MPES .5MM 3/4TCX3/4TC 1/PK','1 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'368','1104',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (983,'S06-E001-05-S','MiniKros Sampler','SAMPLER 65CM 1K MPES .5MM 3/4TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'368','1104',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (984,'S06-E003-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 0.5MM 3/4TC X 3/4TC','3 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (985,'S06-E003-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 0.5MM 3/4TC X 3/4TC STERILE','3 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (986,'S06-E003-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 1MM 3/4TC X 3/4TC','3 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (987,'S06-E003-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 1MM 3/4TC X 3/4TC STERILE','3 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (988,'S06-E005-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 0.5MM 3/4TC X 3/4TC','5 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (989,'S06-E005-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 0.5MM 3/4TC X 3/4TC STERILE','5 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (990,'S06-E005-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 1MM 3/4TC X 3/4TC','5 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (991,'S06-E005-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 1MM 3/4TC X 3/4TC STERILE','5 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (992,'S06-E010-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 0.5MM 3/4TC X 3/4TC','10 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,26.0,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (993,'S06-E010-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 0.5MM 3/4TC X 3/4TC STERILE','10 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,26.0,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (994,'S06-E010-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 1MM 3/4TC X 3/4TC','10 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (995,'S06-E010-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 1MM 3/4TC X 3/4TC STERILE','10 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (996,'S06-E030-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 0.5MM 3/4TC X 3/4TC','30 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (997,'S06-E030-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 0.5MM 3/4TC X 3/4TC STERILE','30 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (998,'S06-E030-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 1MM 3/4TC X 3/4TC','30 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (999,'S06-E030-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 1MM 3/4TC X 3/4TC STERILE','30 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1000,'S06-E050-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 0.5MM 3/4TC X 3/4TC','50 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1001,'S06-E050-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 0.5MM 3/4TC X 3/4TC STERILE','50 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1002,'S06-E050-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 1MM 3/4TC X 3/4TC','50 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1003,'S06-E050-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 1MM 3/4TC X 3/4TC STERILE','50 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1004,'S06-E070-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 0.5MM 3/4TC X 3/4TC','70 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1005,'S06-E070-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 0.5MM 3/4TC X 3/4TC STERILE','70 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1006,'S06-E070-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 1MM 3/4TC X 3/4TC','70 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1007,'S06-E070-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 1MM 3/4TC X 3/4TC STERILE','70 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1008,'S06-E100-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 0.5MM 3/4TC X 3/4TC','100 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1009,'S06-E100-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 0.5MM 3/4TC X 3/4TC STERILE','100 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1010,'S06-E100-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 1MM 3/4TC X 3/4TC','100 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1011,'S06-E100-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 1MM 3/4TC X 3/4TC STERILE','100 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1012,'S06-E20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC','0.2 ?m','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1013,'S06-E20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC STERILE','0.2 ?m','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1014,'S06-E20U-10-N','MiniKros Sampler','SAMPLER 65CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK','0.2 ?m','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1015,'S06-E20U-10-S','MiniKros Sampler','SAMPLER 65CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK STERILE','0.2 ?m','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1016,'S06-E300-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 0.5MM 3/4TC X 3/4TC','300 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1017,'S06-E300-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 0.5MM 3/4TC X 3/4TC STERILE','300 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1018,'S06-E300-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 1MM 3/4TC X 3/4TC','300 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1019,'S06-E300-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 1MM 3/4TC X 3/4TC STERILE','300 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1020,'S06-E500-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 0.5MM 3/4TC X 3/4TC','500 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1021,'S06-E500-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 0.5MM 3/4TC X 3/4TC STERILE','500 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1022,'S06-E500-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 1MM 3/4TC X 3/4TC','500 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1023,'S06-E500-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 1MM 3/4TC X 3/4TC STERILE','500 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1024,'S06-E65U-07-N','MiniKros Sampler','SAMPLER 65CM .65UM MPES .75MM 3/4TCX3/4TC 1/PK','0.65 ?m','mPES',0.75,70,65.0,110,0.625,46.2,43.0,38.8,1700,31.57,63.73,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1025,'S06-E65U-07-S','MiniKros Sampler','SAMPLER 65CM .65UM MPES .75MM 3/4TCX3/4TC STERILE 1/PK','0.65 ?m','mPES',0.75,70,65.0,110,0.625,46.2,43.0,38.8,1700,31.57,63.73,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1026,'S06-E750-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 750K MPES 0.5MM 3/4TC X 3/4 TC','750 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1027,'S06-E750-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 750K MPES 0.5MM 3/4TC X 3/4TC STERILE','750 kD','mPES',0.5,70,65.0,250,0.625,31.5,29.5,27.5,2600,31.89,59.21,'#VALUE!','#VALUE!',16,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1028,'S06-E750-10-N','MiniKros Sampler','SAMPLER 65CM 750KD MPES 1MM 3/4TCX3/4TC 1/PK','750 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1029,'S06-E750-10-S','MiniKros Sampler','SAMPLER 65CM 750KD MPES 1MM 3/4TCX3/4TC STERILE 1/PK','750 kD','mPES',1.0,70,65.0,78,0.625,57.0,52.5,48.0,1600,39.8,60.03,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1030,'S06-M10U-06-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.1 UM ME 0.6MM 3/4TC X 3/4TC','0.1 ?m','ME',0.63,70,65.0,180,0.625,37.0,34.0,31.0,2350,36.45,62.23,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1031,'S06-M10U-06-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.1UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.1 ?m','ME',0.63,70,65.0,180,0.625,37.0,34.0,31.0,2350,36.45,62.23,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1032,'S06-M20U-06-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 0.6MM 3/4TC X 3/4TC','0.2 ?m','ME',0.63,70,65.0,180,0.625,37.0,34.0,31.0,2350,36.45,62.23,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1033,'S06-M20U-06-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.2 ?m','ME',0.63,70,65.0,180,0.625,37.0,34.0,31.0,2350,36.45,62.23,'#VALUE!','#VALUE!',25,18,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1034,'S06-M20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 1MM 3/4TC X 3/4TC','0.2 ?m','ME',1.0,70,65.0,80,0.625,54.0,50.0,46.0,1650,40.82,64.81,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1035,'S06-M20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 1MM 3/4TC X 3/4TC STERILE','0.2 ?m','ME',1.0,70,65.0,80,0.625,54.0,50.0,46.0,1650,40.82,64.81,'#VALUE!','#VALUE!',17,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1036,'S06-P20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 0.5MM 3/4TC X 3/4TC','0.2 ?m','PES',0.5,70,65.0,330,0.625,26.6,25.6,24.6,3400,42.1,59.62,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1037,'S06-P20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 0.5MM 3/4TC X 3/4TC STERILE','0.2 ?m','PES',0.5,70,65.0,330,0.625,26.6,25.6,24.6,3400,42.1,59.62,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1038,'S06-P20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 1MM 3/4TC X 3/4TC','0.2 ?m','PES',1.0,70,65.0,75,0.625,53.1,51.2,49.2,1500,38.27,65.89,'#VALUE!','#VALUE!',25,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1039,'S06-P20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 1MM 3/4TC X 3/4TC STERILE','0.2 ?m','PES',1.0,70,65.0,75,0.625,53.1,51.2,49.2,1500,38.27,65.89,'#VALUE!','#VALUE!',25,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1040,'S06-S010-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 10KD PS 0.5MM 3/4TC X 3/4TC','10 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1041,'S06-S010-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 10KD PS 0.5MM 3/4TC X 3/4TC WET','10 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1042,'S06-S010-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 10KD PS 0.5MM 3/4TC X 3/4TC STERILE','10 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1043,'S06-S050-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 50KD PS 0.5MM 3/4TC X 3/4TC','50 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1044,'S06-S050-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 50KD PS 0.5MM 3/4TC X 3/4TC WET','50 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1045,'S06-S050-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 50KD PS 0.5MM 3/4TC X 3/4TC STERILE','50 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1046,'S06-S05U-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC','0.05 ?m','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1047,'S06-S05U-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC WET','0.05 ?m','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1048,'S06-S05U-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.05  PS 0.5MM 3/4TC X 3/4TC STERILE','0.05 ?m','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1049,'S06-S500-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 500KD PS 0.5MM 3/4TC X 3/4TC','500 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1050,'S06-S500-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 500KD PS 0.5MM 3/4TC X 3/4TC WET','500 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1051,'S06-S500-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 500KD PS 0.5MM 3/4TC X 3/4TC STERILE','500 kD','PS',0.5,70,65.0,330,0.625,26.8,23.6,20.5,3400,42.1,69.98,'#VALUE!','#VALUE!',25,17,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1052,'T02-E001-05-N','MidiKros TC','MIDIKROS TC 20CM 1K MPES .5MM 1/2TCXFLL 1/PK','1 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1053,'T02-E001-05-S','MidiKros TC','MIDIKROS TC 20CM 1K MPES .5MM 1/2TCXFLL 1/PK STERILE','1 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1054,'T02-E003-05-N','MidiKros TC','MIDIKROS 20CM 3K MPES 0.5MM TC X FLL 1/PK','3 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1055,'T02-E003-05-S','MidiKros TC','MIDIKROS 20CM 3K MPES 0.5MM TC X FLL 1/PK STERILE','3 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1056,'T02-E003-10-N','MidiKros TC','MIDIKROS 20CM 3K MPES 1MM TC X FLL 1/PK','3 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1057,'T02-E003-10-S','MidiKros TC','MIDIKROS 20CM 3K MPES 1MM TC X FLL 1/PK STERILE','3 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1058,'T02-E005-05-N','MidiKros TC','MIDIKROS 20CM 5K MPES 0.5MM TC X FLL 1/PK','5 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1059,'T02-E005-05-S','MidiKros TC','MIDIKROS 20CM 5K MPES 0.5MM TC X FLL 1/PK STERILE','5 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1060,'T02-E005-10-N','MidiKros TC','MIDIKROS 20CM 5K MPES 1MM TC X FLL 1/PK','5 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1061,'T02-E005-10-S','MidiKros TC','MIDIKROS 20CM 5K MPES 1MM TC X FLL 1/PK STERILE','5 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1062,'T02-E010-05-N','MidiKros TC','MIDIKROS 20CM 10K MPES 0.5MM TC X FLL 1/PK','10 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,26.0,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1063,'T02-E010-05-S','MidiKros TC','MIDIKROS 20CM 10K MPES 0.5MM TC X FLL 1/PK STERILE','10 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,26.0,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1064,'T02-E010-10-N','MidiKros TC','MIDIKROS 20CM 10K MPES 1MM TC X FLL 1/PK','10 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1065,'T02-E010-10-S','MidiKros TC','MIDIKROS 20CM 10K MPES 1MM TC X FLL 1/PK STERILE','10 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1066,'T02-E030-05-N','MidiKros TC','MIDIKROS 20CM 30K MPES 0.5MM TC X FLL 1/PK','30 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1067,'T02-E030-05-S','MidiKros TC','MIDIKROS 20CM 30K MPES 0.5MM TC X FLL 1/PK STERILE','30 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1068,'T02-E030-10-N','MidiKros TC','MIDIKROS 20CM 30K MPES 1MM TC X FLL 1/PK','30 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1069,'T02-E030-10-S','MidiKros TC','MIDIKROS 20CM 30K MPES 1MM TC X FLL 1/PK STERILE','30 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1070,'T02-E050-05-N','MidiKros TC','MIDIKROS 20CM 50K MPES 0.5MM TC X FLL 1/PK','50 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1071,'T02-E050-05-S','MidiKros TC','MIDIKROS 20CM 50K MPES 0.5MM TC X FLL 1/PK STTERILE','50 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1072,'T02-E050-10-N','MidiKros TC','MIDIKROS 20CM 50K MPES 1MM TC X FLL 1/PK','50 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1073,'T02-E050-10-S','MidiKros TC','MIDIKROS 20CM 50K MPES 1MM TC X FLL 1/PK STERILE','50 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1074,'T02-E070-05-N','MidiKros TC','MIDIKROS 20CM 70K MPES 0.5MM TC X FLL 1/PK','70 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1075,'T02-E070-05-S','MidiKros TC','MIDIKROS 20CM 70K MPES 0.5MM TC X FLL 1/PK STERILE','70 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1076,'T02-E070-10-N','MidiKros TC','MIDIKROS 20CM 70K MPES 1MM TC X FLL 1/PK','70 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1077,'T02-E070-10-S','MidiKros TC','MIDIKROS 20CM 70K MPES 1MM TC X FLL 1/PK IRR','70 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1078,'T02-E100-05-N','MidiKros TC','MIDIKROS 20CM 100K MPES 0.5MM TC X FLL 1/PK','100 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1079,'T02-E100-05-S','MidiKros TC','MIDIKROS 20CM 100K MPES 0.5MM TC X FLL 1/PK STERILE','100 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1080,'T02-E100-10-N','MidiKros TC','MIDIKROS 20CM 100K MPES 1MM TC X FLL 1/PK','100 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1081,'T02-E100-10-S','MidiKros TC','MIDIKROS 20CM 100K MPES 1MM TC X FLL 1/PK STERILE','100 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1082,'T02-E20U-05-N','MidiKros TC','MIDIKROS 20CM 0.2UM MPES 0.5MM TC X FLL 1/PK','0.2 ?m','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1083,'T02-E20U-05-S','MidiKros TC','MIDIKROS 20CM 0.2UM MPES 0.5MM TC X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1084,'T02-E20U-10-N','MidiKros TC','MIDIKROS TC 20CM .2UM MPES 1MM 1/2TCXFLL 1/PK','0.2 ?m','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1085,'T02-E20U-10-S','MidiKros TC','MIDIKROS TC 20CM .2UM MPES 1MM 1/2TCXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1086,'T02-E300-05-N','MidiKros TC','MIDIKROS 20CM 300K MPES 0.5MM TC X FLL 1/PK','300 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1087,'T02-E300-05-S','MidiKros TC','MIDIKROS 20CM 300K MPES 0.5MM TC X FLL 1/PK STERILE','300 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1088,'T02-E300-10-N','MidiKros TC','MIDIKROS 20CM 300K MPES 1MM TC X FLL 1/PK','300 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1089,'T02-E300-10-S','MidiKros TC','MIDIKROS 20CM 300K MPES 1MM TC X FLL 1/PK STERILE','300 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1090,'T02-E500-05-N','MidiKros TC','MIDIKROS 20CM 500K MPES 0.5MM TC X FLL 1/PK','500 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1091,'T02-E500-05-S','MidiKros TC','MIDIKROS 20CM 500K MPES 0.5MM TC X FLL 1/PK STERILE','500 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1092,'T02-E500-10-N','MidiKros TC','MIDIKROS 20CM 500K MPES 1MM TC X FLL 1/PK','500 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1093,'T02-E500-10-S','MidiKros TC','MIDIKROS 20CM 500K MPES 1MM TC X FLL 1/PK STERILE','500 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1094,'T02-E65U-07-N','MidiKros TC','MIDIKROS TC 20CM .65UM MPES .75MM 1/2TCXFLL 1/PK','0.65 ?m','mPES',0.75,23,20.0,18,0.275,46.2,43.0,38.8,85,1.59,4.39,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1095,'T02-E65U-07-S','MidiKros TC','MIDIKROS TC 20CM .65UM MPES .75MM 1/2TCXFLL STERILE 1/PK','0.65 ?m','mPES',0.75,23,20.0,18,0.275,46.2,43.0,38.8,85,1.59,4.39,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1096,'T02-E750-05-N','MidiKros TC','MIDIKROS 20CM 750K MPES 0.5MM TC X FLL 1/PK','750 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1097,'T02-E750-05-S','MidiKros TC','MIDIKROS 20CM 750K MPES 0.5MM TC X FLL 1/PK STERILE','750 kD','mPES',0.5,23,20.0,36,0.275,31.5,29.5,27.5,115,1.41,4.59,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1098,'T02-E750-10-N','MidiKros TC','MIDIKROS TC 20CM 750KD MPES 1MM 1/2TCXFLL 1/PK','750 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1099,'T02-E750-10-S','MidiKros TC','MIDIKROS TC 20CM 750KD MPES 1MM 1/2TCXFLL STERILE 1/PK','750 kD','mPES',1.0,23,20.0,12,0.275,57.0,52.5,48.0,75,1.88,4.41,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1100,'T02-M10U-06-N','MidiKros TC','MIDIKROS 20CM 0.1UM ME 0.6MM TC X FLL 1/PK','0.1 ?m','ME',0.63,23,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1101,'T02-M10U-06-S','MidiKros TC','MIDIKROS 20CM 0.1UM ME 0.6MM TC X FLL 1/PK STERILE','0.1 ?m','ME',0.63,23,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1102,'T02-M20U-06-N','MidiKros TC','MIDIKROS 20CM 0.2UM ME 0.6MM TC X FLL 1/PK','0.2 ?m','ME',0.63,23,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1103,'T02-M20U-06-S','MidiKros TC','MIDIKROS 20CM 0.2UM ME 0.6MM TC X FLL 1/PK STERILE','0.2 ?m','ME',0.63,23,20.0,26,0.275,37.0,34.0,31.0,105,1.62,4.71,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1104,'T02-M20U-10-N','MidiKros TC','MIDIKROS 20CM 0.2UM ME 1MM TC X FLL 1/PK','0.2 ?m','ME',1.0,23,20.0,15,0.275,54.0,50.0,46.0,94,2.36,3.98,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1105,'T02-M20U-10-S','MidiKros TC','MIDIKROS 20CM 0.2UM ME 1MM TC X FLL 1/PK STERILE','0.2 ?m','ME',1.0,23,20.0,15,0.275,54.0,50.0,46.0,94,2.36,3.98,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1106,'T02-P20U-05-N','MidiKros TC','MIDIKROS 20CM 0.2UM PES 0.5MM TC X FLL 1/PK','0.2 ?m','PES',0.5,23,20.0,45,0.275,26.6,25.6,24.6,140,1.77,4.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1107,'T02-P20U-05-S','MidiKros TC','MIDIKROS 20CM 0.2UM PES 0.5MM TC X FLL 1/PK STERILE','0.2 ?m','PES',0.5,23,20.0,45,0.275,26.6,25.6,24.6,140,1.77,4.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1108,'T02-P20U-10-N','MidiKros TC','MIDIKROS 20CM 0.2UM PES 1MM TC X FLL 1/PK','0.2 ?m','PES',1.0,23,20.0,14,0.275,53.1,51.2,49.2,88,2.2,4.06,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1109,'T02-P20U-10-S','MidiKros TC','MIDIKROS 20CM 0.2UM PES 1MM TC X FLL 1/PK STERILE','0.2 ?m','PES',1.0,23,20.0,14,0.275,53.1,51.2,49.2,88,2.2,4.06,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1110,'T02-S010-05-N','MidiKros TC','MIDIKROS 20CM 10KD PS 0.5MM TC X FLL 1/PK','10 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1111,'T02-S010-05-P','MidiKros TC','MIDIKROS 20CM 10KD PS 0.5MM TC X FLL 1/PK  WET','10 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1112,'T02-S010-05-S','MidiKros TC','MIDIKROS 20CM 10KD PS 0.5MM TC X FLL 1/PK  STERILE','10 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1113,'T02-S050-05-N','MidiKros TC','MIDIKROS 20CM 50KD PS 0.5MM TC X FLL 1/PK','50 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1114,'T02-S050-05-P','MidiKros TC','MIDIKROS 20CM 50KD PS 0.5MM TC X FLL 1/PK WET','50 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1115,'T02-S050-05-S','MidiKros TC','MIDIKROS 20CM 50KD PS 0.5MM TC X FLL 1/PK STERILE','50 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1116,'T02-S05U-05-N','MidiKros TC','MIDIKROS 20CM 0.05UM PS 0.5MM TC X FLL 1/PK','0.05 ?m','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1117,'T02-S05U-05-P','MidiKros TC','MIDIKROS 20CM 0.05UM PS 0.5MM TC X FLL 1/PK WET','0.05 ?m','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1118,'T02-S05U-05-S','MidiKros TC','MIDIKROS 20CM 0.05UM PS 0.5MM TC X FLL 1/PK STERILE','0.05 ?m','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1119,'T02-S500-05-N','MidiKros TC','MIDIKROS 20CM 500KD PS 0.5MM TC X FLL 1/PK','500 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1120,'T02-S500-05-P','MidiKros TC','MIDIKROS 20CM 500KD PS 0.5MM TC X FLL 1/PK WET','500 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1121,'T02-S500-05-S','MidiKros TC','MIDIKROS 20CM 500KD PS 0.5MM TC X FLL 1/PK STERILE','500 kD','PS',0.5,23,20.0,60,0.275,26.8,23.6,20.5,190,2.36,4.38,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1122,'T04-E001-05-N','MidiKros TC','MIDIKROS TC 41.5CM 1K MPES .5MM 1/2TCXFLL 1/PK','1 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1123,'T04-E001-05-S','MidiKros TC','MIDIKROS TC 41.5CM 1K MPES .5MM 1/2TCXFLL 1/PK STERILE','1 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1124,'T04-E003-05-N','MidiKros TC','MIDIKROS 41.5CM 3K MPES 0.5MM TC X FLL 1/PK','3 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1125,'T04-E003-05-S','MidiKros TC','MIDIKROS 41.5CM 3K MPES 0.5MM TC X FLL 1/PK STERILE','3 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1126,'T04-E003-10-N','MidiKros TC','MIDIKROS 41.5CM 3K MPES 1MM TC X FLL 1/PK','3 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1127,'T04-E003-10-S','MidiKros TC','MIDIKROS 41.5CM 3K MPES 1MM TC X FLL 1/PK STERILE','3 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1128,'T04-E005-05-N','MidiKros TC','MIDIKROS 41.5CM 5K MPES 0.5MM TC X FLL 1/PK','5 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1129,'T04-E005-05-S','MidiKros TC','MIDIKROS 41.5CM 5K MPES 0.5MM TC X FLL 1/PK STERILE','5 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1130,'T04-E005-10-N','MidiKros TC','MIDIKROS 41.5CM 5K MPES 1MM TC X FLL 1/PK','5 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1131,'T04-E005-10-S','MidiKros TC','MIDIKROS 41.5CM 5K MPES 1MM TC X FLL 1/PK STERILE','5 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1132,'T04-E010-05-N','MidiKros TC','MIDIKROS 41.5CM 10K MPES 0.5MM TC X FLL 1/PK','10 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,26.0,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1133,'T04-E010-05-S','MidiKros TC','MIDIKROS 41.5CM 10K MPES 0.5MM TC X FLL 1/PK STERIL','10 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,26.0,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1134,'T04-E010-10-N','MidiKros TC','MIDIKROS 41.5CM 10K MPES 1MM TC X FLL 1/PK','10 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1135,'T04-E010-10-S','MidiKros TC','MIDIKROS 41.5CM 10K MPES 1MM TC X FLL 1/PK STERILE','10 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1136,'T04-E030-05-N','MidiKros TC','MIDIKROS 41.5CM 30K MPES 0.5MM TC X FLL 1/PK','30 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1137,'T04-E030-05-S','MidiKros TC','MIDIKROS 41.5CM 30K MPES 0.5MM TC X FLL 1/PK STERILE','30 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1138,'T04-E030-10-N','MidiKros TC','MIDIKROS 41.5CM 30K MPES 1MM TC X FLL 1/PK','30 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1139,'T04-E030-10-S','MidiKros TC','MIDIKROS 41.5CM 30K MPES 1MM TC X FLL 1/PK STERILE','30 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1140,'T04-E050-05-N','MidiKros TC','MIDIKROS 41.5CM 50K MPES 0.5MM TC X FLL 1/PK','50 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1141,'T04-E050-05-S','MidiKros TC','MIDIKROS 41.5CM 50K MPES 0.5MM TC X FLL 1/PK STERILE','50 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1142,'T04-E050-10-N','MidiKros TC','MIDIKROS 41.5CM 50K MPES 1MM TC X FLL 1/PK','50 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1143,'T04-E050-10-S','MidiKros TC','MIDIKROS 41.5CM 50K MPES 1MM TC X FLL 1/PK STERILE','50 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1144,'T04-E070-05-N','MidiKros TC','MIDIKROS 41.5CM 70K MPES 0.5MM TC X FLL 1/PK','70 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1145,'T04-E070-05-S','MidiKros TC','MIDIKROS 41.5CM 70K MPES 0.5MM TC X FLL 1/PK STERILE','70 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1146,'T04-E070-10-N','MidiKros TC','MIDIKROS 41.5CM 70K MPES 1MM TC X FLL 1/PK','70 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1147,'T04-E070-10-S','MidiKros TC','MIDIKROS 41.5CM 70K MPES 1MM TC X FLL 1/PK IRR','70 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1148,'T04-E100-05-N','MidiKros TC','MIDIKROS 41.5CM 100K MPES 0.5MM TC X FLL 1/PK','100 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1149,'T04-E100-05-S','MidiKros TC','MIDIKROS 41.5CM 100K MPES 0.5MM TC X FLL 1/PK IRR','100 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1150,'T04-E100-10-N','MidiKros TC','MIDIKROS 41.5CM 100K MPES 1MM TC X FLL 1/PK','100 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1151,'T04-E100-10-S','MidiKros TC','MIDIKROS 41.5CM 100K MPES 1MM TC X FLL 1/PK STERILE','100 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1152,'T04-E20U-05-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM MPES 0.5MM TC X FLL 1/PK','0.2 ?m','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1153,'T04-E20U-05-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM MPES 0.5MM TC X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1154,'T04-E20U-10-N','MidiKros TC','MIDIKROS TC 41.5CM .2UM MPES 1MM 1/2TCXFLL 1/PK','0.2 ?m','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1155,'T04-E20U-10-S','MidiKros TC','MIDIKROS TC 41.5CM .2UM MPES 1MM 1/2TCXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1156,'T04-E300-05-N','MidiKros TC','MIDIKROS 41.5CM 300K MPES 0.5MM TC X FLL 1/PK','300 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1157,'T04-E300-05-S','MidiKros TC','MIDIKROS 41.5CM 300K MPES 0.5MM TC X FLL 1/PK  IRR','300 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1158,'T04-E300-10-N','MidiKros TC','MIDIKROS 41.5CM 300K MPES 1MM TC X FLL 1/PK','300 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1159,'T04-E300-10-S','MidiKros TC','MIDIKROS 41.5CM 300K MPES 1MM TC X FLL 1/PK STERILE','300 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1160,'T04-E500-05-N','MidiKros TC','MIDIKROS 41.5CM 500K MPES 0.5MM TC X FLL 1/PK','500 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1161,'T04-E500-05-S','MidiKros TC','MIDIKROS 41.5CM 500K MPES 0.5MM TC X FLL 1/PK STERILE','500 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1162,'T04-E500-10-N','MidiKros TC','MIDIKROS 41.5CM 500K MPES 1MM TC X FLL 1/PK','500 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1163,'T04-E500-10-S','MidiKros TC','MIDIKROS 41.5CM 500K MPES 1MM TC X FLL 1/PK STERILE','500 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1164,'T04-E65U-07-N','MidiKros TC','MIDIKROS TC 41.5CM .65UM MPES .75MM 1/2TCXFLL 1/PK','0.65 ?m','mPES',0.75,44,41.5,18,0.275,46.2,43.0,38.8,175,3.3,9.12,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1165,'T04-E65U-07-S','MidiKros TC','MIDIKROS TC 41.5CM .65UM MPES .75MM 1/2TCXFLL STERILE 1/PK','0.65 ?m','mPES',0.75,44,41.5,18,0.275,46.2,43.0,38.8,175,3.3,9.12,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1166,'T04-E750-05-N','MidiKros TC','MIDIKROS 41.5CM 750K MPES 0.5MM TC X FLL 1/PK','750 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1167,'T04-E750-05-S','MidiKros TC','MIDIKROS 20CM 750K MPES 0.5MM TC X FLL 1/PK STERILE','750 kD','mPES',0.5,44,41.5,36,0.275,31.5,29.5,27.5,235,2.93,9.52,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1168,'T04-E750-10-N','MidiKros TC','MIDIKROS TC 41.5CM 750KD MPES 1MM 1/2TCXFLL 1/PK','750 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1169,'T04-E750-10-S','MidiKros TC','MIDIKROS TC 41.5CM 750KD MPES 1MM 1/2TCXFLL STERILE 1/PK','750 kD','mPES',1.0,44,41.5,12,0.275,57.0,52.5,48.0,155,3.91,9.16,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1170,'T04-M10U-06-N','MidiKros TC','MIDIKROS 41.5CM 0.1UM ME 0.6MM TC X FLL 1/PK','0.1 ?m','ME',0.63,44,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1171,'T04-M10U-06-S','MidiKros TC','MIDIKROS 41.5CM 0.1UM ME 0.6MM TC X FLL 1/PK STERILE','0.1 ?m','ME',0.63,44,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1172,'T04-M20U-06-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM ME 0.6MM TC X FLL 1/PK','0.2 ?m','ME',0.63,44,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1173,'T04-M20U-06-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM ME 0.6MM TC X FLL 1/PK STERILE','0.2 ?m','ME',0.63,44,41.5,26,0.275,37.0,34.0,31.0,215,3.36,9.77,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1174,'T04-M20U-10-N','MidiKros TC','MIDIKROS 41.5CM 0.2 UM ME 1MM TC X FLL 1/PK','0.2 ?m','ME',1.0,44,41.5,15,0.275,54.0,50.0,46.0,195,4.89,8.26,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1175,'T04-M20U-10-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM ME 1MM TC X FLL 1/PK STERILE','0.2 ?m','ME',1.0,44,41.5,15,0.275,54.0,50.0,46.0,195,4.89,8.26,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1176,'T04-P20U-05-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 0.5MM TC X FLL 1/PK','0.2 ?m','PES',0.5,44,41.5,45,0.275,26.6,25.6,24.6,290,3.66,9.89,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1177,'T04-P20U-05-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 0.5MM TC X FLL 1/PK STERILE','0.2 ?m','PES',0.5,44,41.5,45,0.275,26.6,25.6,24.6,290,3.66,9.89,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1178,'T04-P20U-10-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 1MM TC X FLL 1/PK','0.2 ?m','PES',1.0,44,41.5,14,0.275,53.1,51.2,49.2,180,4.56,8.42,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1179,'T04-P20U-10-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 1MM TC X FLL 1/PK STERILE','0.2 ?m','PES',1.0,44,41.5,14,0.275,53.1,51.2,49.2,180,4.56,8.42,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1180,'T04-S010-05-N','MidiKros TC','MIDIKROS 41.5CM 10KD PS 0.5MM TC X FLL 1/PK','10 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1181,'T04-S010-05-P','MidiKros TC','MIDIKROS 41.5CM 10KD PS 0.5MM TC X FLL 1/PK WET','10 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1182,'T04-S010-05-S','MidiKros TC','MIDIKROS 41.5CM 10KD PS 0.5MM TC X FLL 1/PK STERILE','10 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1183,'T04-S050-05-N','MidiKros TC','MIDIKROS 41.5CM 50KD PS 0.5MM TC X FLL 1/PK','50 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1184,'T04-S050-05-P','MidiKros TC','MIDIKROS 41.5CM 50KD PS 0.5MM TC X FLL 1/PK WET','50 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1185,'T04-S050-05-S','MidiKros TC','MIDIKROS 41.5CM 50KD PS 0.5MM TC X FLL 1/PK STERILE','50 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1186,'T04-S05U-05-N','MidiKros TC','MIDIKROS 41.5CM 0.05UM PS 0.5MM TC X FLL 1/PK','0.05 ?m','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1187,'T04-S05U-05-P','MidiKros TC','MIDIKROS 41.5CM 0.05UM PS 0.5MM TC X FLL 1/PK WET','0.05 ?m','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1188,'T04-S05U-05-S','MidiKros TC','MIDIKROS 41.5CM 0.05UM PS 0.5MM TC X FLL 1/PK STERILE','0.05 ?m','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1189,'T04-S500-05-N','MidiKros TC','MIDIKROS 41.5CM 500KD PS 0.5MM TC X FLL 1/PK','500 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1190,'T04-S500-05-P','MidiKros TC','MIDIKROS 41.5CM 500KD PS 0.5MM TC X FLL 1/PK WET','500 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1191,'T04-S500-05-S','MidiKros TC','MIDIKROS 41.5CM 500KD PS 0.5MM TC X FLL 1/PK STERILE','500 kD','PS',0.5,44,41.5,60,0.275,26.8,23.6,20.5,390,4.89,9.09,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1192,'T06-E001-05-N','MidiKros TC','MIDIKROS TC 65CM 1K MPES .5MM 1/2TCXFLL 1/PK','1 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1193,'T06-E001-05-S','MidiKros TC','MIDIKROS TC 65CM 1K MPES .5MM 1/2TCXFLL 1/PK STERILE','1 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'53','159',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1194,'T06-E003-05-N','MidiKros TC','MIDIKROS 65CM 3K MPES 0.5MM TC X FLL 1/PK','3 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1195,'T06-E003-05-S','MidiKros TC','MIDIKROS 65CM 3K MPES 0.5MM TC X FLL 1/PK STERILE','3 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1196,'T06-E003-10-N','MidiKros TC','MIDIKROS 65CM 3K MPES 1MM TC X FLL 1/PK','3 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1197,'T06-E003-10-S','MidiKros TC','MIDIKROS 65CM 3K MPES 1MM TC X FLL 1/PK STERILE','3 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1198,'T06-E005-05-N','MidiKros TC','MIDIKROS 65CM 5K MPES 0.5MM TC X FLL 1/PK','5 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1199,'T06-E005-05-S','MidiKros TC','MIDIKROS 65CM 5K MPES 0.5MM TC X FLL 1/PK STERILE','5 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1200,'T06-E005-10-N','MidiKros TC','MIDIKROS 65CM 5K MPES 1MM TC X FLL 1/PK','5 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1201,'T06-E005-10-S','MidiKros TC','MIDIKROS 65CM 5K MPES 1MM TC X FLL 1/PK STERILE','5 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1202,'T06-E010-05-N','MidiKros TC','MIDIKROS 65CM 10K MPES 0.5MM TC X FLL 1/PK','10 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,26.0,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1203,'T06-E010-05-S','MidiKros TC','MIDIKROS 65CM 10K MPES 0.5MM TC X FLL 1/PK STERILE','10 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,26.0,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1204,'T06-E010-10-N','MidiKros TC','MIDIKROS 65CM 10K MPES 1MM TC X FLL 1/PK','10 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1205,'T06-E010-10-S','MidiKros TC','MIDIKROS 65CM 10K MPES 1MM TC X FLL 1/PK STERILE','10 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1206,'T06-E030-05-N','MidiKros TC','MIDIKROS 65CM 30K MPES 0.5MM TC X FLL 1/PK','30 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1207,'T06-E030-05-S','MidiKros TC','MIDIKROS 65CM 30K MPES 0.5MM TC X FLL 1/PK STERILE','30 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1208,'T06-E030-10-N','MidiKros TC','MIDIKROS 65CM 30K MPES 1MM TC X FLL 1/PK','30 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1209,'T06-E030-10-S','MidiKros TC','MIDIKROS 65CM 30K MPES 1MM TC X FLL 1/PK STERILE','30 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1210,'T06-E050-05-N','MidiKros TC','MIDIKROS 65CM 50K MPES 0.5MM TC X FLL 1/PK','50 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1211,'T06-E050-05-S','MidiKros TC','MIDIKROS 65CM 50K MPES 0.5MM TC X FLL 1/PK STERILE','50 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1212,'T06-E050-10-N','MidiKros TC','MIDIKROS 65CM 50K MPES 1MM TC X FLL 1/PK','50 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1213,'T06-E050-10-S','MidiKros TC','MIDIKROS 65CM 50K MPES 1MM TC X FLL 1/PK STERILE','50 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1214,'T06-E070-05-N','MidiKros TC','MIDIKROS 65CM 70K MPES 0.5MM TC X FLL 1/PK','70 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1215,'T06-E070-05-S','MidiKros TC','MIDIKROS 65CM 70K MPES 0.5MM TC X FLL 1/PK STERILE','70 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1216,'T06-E070-10-N','MidiKros TC','MIDIKROS 65CM 70K MPES 1MM TC X FLL 1/PK','70 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1217,'T06-E070-10-S','MidiKros TC','MIDIKROS 65CM 70K MPES 1MM TC X FLL 1/PK IRR','70 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1218,'T06-E100-05-N','MidiKros TC','MIDIKROS 65CM 100K MPES 0.5MM TC X FLL 1/PK','100 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1219,'T06-E100-05-S','MidiKros TC','MIDIKROS 65CM 100K MPES 0.5MM TC X FLL 1/PK IRR','100 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1220,'T06-E100-10-N','MidiKros TC','MIDIKROS 65CM 100K MPES 1MM TC X FLL 1/PK','100 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1221,'T06-E100-10-S','MidiKros TC','MIDIKROS 65CM 100K MPES 1MM TC X FLL 1/PK STERILE','100 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1222,'T06-E20U-05-N','MidiKros TC','MIDIKROS 65CM 0.2UM MPES 0.5MM TC X FLL 1/PK','0.2 ?m','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1223,'T06-E20U-05-S','MidiKros TC','MIDIKROS 65CM 0.2UM MPES 0.5MM TC X FLL 1/PK STERILE','0.2 ?m','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1224,'T06-E20U-10-N','MidiKros TC','MIDIKROS TC 65CM .2UM MPES 1MM 1/2TCXFLL 1/PK','0.2 ?m','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1225,'T06-E20U-10-S','MidiKros TC','MIDIKROS TC 65CM .2UM MPES 1MM 1/2TCXFLL 1/PK STERILE','0.2 ?m','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1226,'T06-E300-05-N','MidiKros TC','MIDIKROS 65CM 300K MPES 0.5MM TC X FLL 1/PK','300 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1227,'T06-E300-05-S','MidiKros TC','MIDIKROS 65CM 300K MPES 0.5MM TC X FLL 1/PK STERILE','300 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1228,'T06-E300-10-N','MidiKros TC','MIDIKROS 65CM 300K MPES 1MM TC X FLL 1/PK','300 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1229,'T06-E300-10-S','MidiKros TC','MIDIKROS 65CM 300K MPES 1MM TC X FLL 1/PK STERILE','300 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1230,'T06-E500-05-N','MidiKros TC','MIDIKROS 65CM 500K MPES 0.5MM TC X FLL 1/PK','500 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1231,'T06-E500-05-S','MidiKros TC','MIDIKROS 65CM 500K MPES 0.5MM TC X FLL 1/PK STERILE','500 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1232,'T06-E500-10-N','MidiKros TC','MIDIKROS 65CM 500K MPES 1MM TC X FLL 1/PK','500 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1233,'T06-E500-10-S','MidiKros TC','MIDIKROS 65CM 500K MPES 1MM TC X FLL 1/PK STERILE','500 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1234,'T06-E65U-07-N','MidiKros TC','MIDIKROS TC 65CM .65UM MPES .75MM 1/2TCXFLL 1/PK','0.65 ?m','mPES',0.75,68,65.0,18,0.275,46.2,43.0,38.8,275,5.17,14.28,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1235,'T06-E65U-07-S','MidiKros TC','MIDIKROS TC 65CM .65UM MPES .75MM 1/2TCXFLL STERILE 1/PK','0.65 ?m','mPES',0.75,68,65.0,18,0.275,46.2,43.0,38.8,275,5.17,14.28,'#VALUE!','#VALUE!',14,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1236,'T06-E750-05-N','MidiKros TC','MIDIKROS 65CM 750K MPES 0.5MM TC X FLL 1/PK','750 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1237,'T06-E750-05-S','MidiKros TC','MIDIKROS 65CM 750K MPES 0.5MM TC X FLL 1/PK STERILE','750 kD','mPES',0.5,68,65.0,36,0.275,31.5,29.5,27.5,370,4.59,14.9,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1238,'T06-E750-10-N','MidiKros TC','MIDIKROS TC 65CM 750KD MPES 1MM 1/2TCXFLL 1/PK','750 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1239,'T06-E750-10-S','MidiKros TC','MIDIKROS TC 65CM 750KD MPES 1MM 1/2TCXFLL STERILE 1/PK','750 kD','mPES',1.0,68,65.0,12,0.275,57.0,52.5,48.0,245,6.12,14.35,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1240,'T06-M10U-06-N','MidiKros TC','MIDIKROS 65CM 0.1UM ME 0.6MM TC X FLL 1/PK','0.1 ?m','ME',0.63,68,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1241,'T06-M10U-06-S','MidiKros TC','MIDIKROS 65CM 0.1UM ME 0.6MM TC X FLL 1/PK STERILE','0.1 ?m','ME',0.63,68,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1242,'T06-M20U-06-N','MidiKros TC','MIDIKROS 65CM 0.2UM ME 0.6MM TC X FLL 1/PK','0.2 ?m','ME',0.63,68,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1243,'T06-M20U-06-S','MidiKros TC','MIDIKROS 65CM 0.2UM ME 0.6MM TC X FLL 1/PK STERILE','0.2 ?m','ME',0.63,68,65.0,26,0.275,37.0,34.0,31.0,335,5.27,15.31,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1244,'T06-M20U-10-N','MidiKros TC','MIDIKROS 65CM 0.2UM ME 1MM TC X FLL 1/PK','0.2 ?m','ME',1.0,68,65.0,15,0.275,54.0,50.0,46.0,305,7.65,12.94,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1245,'T06-M20U-10-S','MidiKros TC','MIDIKROS 65CM 0.2UM ME 1MM TC X FLL 1/PK STERILE','0.2 ?m','ME',1.0,68,65.0,15,0.275,54.0,50.0,46.0,305,7.65,12.94,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1246,'T06-P20U-05-N','MidiKros TC','MIDIKROS 65CM 0.2UM PES 0.5MM TC X FLL 1/PK','0.2 ?m','PES',0.5,68,65.0,45,0.275,26.6,25.6,24.6,460,5.74,15.49,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1247,'T06-P20U-05-S','MidiKros TC','MIDIKROS 65CM 0.2UM PES 0.5MM TC X FLL 1/PK STERILE','0.2 ?m','PES',0.5,68,65.0,45,0.275,26.6,25.6,24.6,460,5.74,15.49,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1248,'T06-P20U-10-N','MidiKros TC','MIDIKROS 65CM 0.2UM PES 1MM TC X FLL 1/PK','0.2 ?m','PES',1.0,68,65.0,14,0.275,53.1,51.2,49.2,290,7.14,13.19,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1249,'T06-P20U-10-S','MidiKros TC','MIDIKROS 65CM 0.2UM PES 1MM TC X FLL 1/PK STERILE','0.2 ?m','PES',1.0,68,65.0,14,0.275,53.1,51.2,49.2,290,7.14,13.19,'#VALUE!','#VALUE!',16,25,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1250,'T06-S010-05-N','MidiKros TC','MIDIKROS 65CM 10KD PS 0.5MM TC X FLL 1/PK','10 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1251,'T06-S010-05-P','MidiKros TC','MIDIKROS 65CM 10KD PS 0.5MM TC X FLL 1/PK WET','10 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1252,'T06-S010-05-S','MidiKros TC','MIDIKROS 65CM 10KD PS 0.5MM TC X FLL 1/PK STERILE','10 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1253,'T06-S050-05-N','MidiKros TC','MIDIKROS 65CM 50KD PS 0.5MM TC X FLL 1/PK','50 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1254,'T06-S050-05-P','MidiKros TC','MIDIKROS 65CM 50KD PS 0.5MM TC X FLL 1/PK WET','50 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1255,'T06-S050-05-S','MidiKros TC','MIDIKROS 65CM 50KD PS 0.5MM TC X FLL 1/PK STERILE','50 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1256,'T06-S05U-05-N','MidiKros TC','MIDIKROS 65CM 0.05UM PS 0.5MM TC X FLL 1/PK','0.05 ?m','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1257,'T06-S05U-05-P','MidiKros TC','MIDIKROS 65CM 0.05UM PS 0.5MM TC X FLL 1/PK WET','0.05 ?m','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1258,'T06-S05U-05-S','MidiKros TC','MIDIKROS 65CM 0.05UM PS 0.5MM TC X FLL 1/PK STERILE','0.05 ?m','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1259,'T06-S500-05-N','MidiKros TC','MIDIKROS 65CM 500KD PS 0.5MM TC X FLL 1/PK','500 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1260,'T06-S500-05-P','MidiKros TC','MIDIKROS 65CM 500KD PS 0.5MM TC X FLL 1/PK WET','500 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1261,'T06-S500-05-S','MidiKros TC','MIDIKROS 65CM 500KD PS 0.5MM TC X FLL 1/PK STERILE','500 kD','PS',0.5,68,65.0,60,0.275,26.8,23.6,20.5,610,7.65,14.24,'#VALUE!','#VALUE!',14,16,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1262,'X04-E001-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 1K MPES .5MM 6TCX1.5TC 1/PK','1 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.325,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1263,'X04-E001-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 1K MPES .5MM 6TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1264,'X04-E003-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 0.5MM 6TC x 1.5TC','3 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1265,'X04-E003-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 0.5MM 6TC x 1.5TC STERILE','3 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1266,'X04-E003-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 1MM 6TC x 1.5TC','3 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1267,'X04-E003-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 1MM 6TC x 1.5TC STERILE','3 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1268,'X04-E005-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 0.5MM 6TC x 1.5TC','5 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1269,'X04-E005-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 0.5MM 6TC x 1.5TC STERILE','5 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1270,'X04-E005-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 1MM 6TC x 1.5TC','5 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1271,'X04-E005-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 1MM 6TC x 1.5TC STERILE','5 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1272,'X04-E010-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 0.5MM 6TC x 1.5TC','10 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,26.0,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1273,'X04-E010-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 0.5MM 6TC x 1.5TC STERILE','10 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,26.0,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1274,'X04-E010-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 1MM 6TC x 1.5TC','10 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1275,'X04-E010-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 1MM 6TC x 1.5TC STERILE','10 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1276,'X04-E030-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 0.5MM 6TC x 1.5TC','30 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1277,'X04-E030-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 0.5MM 6TC x 1.5TC STERILE','30 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1278,'X04-E030-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 1MM 6TC x 1.5TC','30 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1279,'X04-E030-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 1MM 6TC x 1.5TC STERILE','30 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1280,'X04-E050-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 0.5MM 6TC x 1.5TC','50 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1281,'X04-E050-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 0.5MM 6TC x 1.5TC STERILE','50 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1282,'X04-E050-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 1MM 6TC x 1.5TC','50 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1283,'X04-E050-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 1MM 6TC x 1.5TC STERILE','50 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1284,'X04-E070-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 0.5MM 6TC x 1.5TC','70 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1285,'X04-E070-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 0.5MM 6TC x 1.5TC STERILE','70 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1286,'X04-E070-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 1MM 6TC x 1.5TC','70 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1287,'X04-E070-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 1MM 6TC x 1.5TC STERILE','70 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1288,'X04-E100-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 0.5MM 6TC x 1.5TC','100 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1289,'X04-E100-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 0.5MM 6TC x 1.5TC STERILE','100 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1290,'X04-E100-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 1MM 6TC x 1.5TC','100 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1291,'X04-E100-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 1MM 6TC x 1.5TC STERILE','100 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1292,'X04-E20U-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM MPES 0.5MM 6TC x 1.5TC','0.2 ?m','mPES',0.5,51.5,41.5,11000,2.047,31.5,29.5,27.5,71600,895.88,1847.22,'16199','48597',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1293,'X04-E20U-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM MPES 0.5MM 6TC x 1.5TC STERILE','0.2 ?m','mPES',0.5,51.5,41.5,11000,2.047,31.5,29.5,27.5,71600,895.88,1847.22,'16199','48597',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1294,'X04-E20U-10-N','KrosFlo Max','KROSFLO MAX 41.5CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1295,'X04-E20U-10-S','KrosFlo Max','KROSFLO MAX 41.5CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1296,'X04-E300-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 0.5MM 6TC x 1.5TC','300 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1297,'X04-E300-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 0.5MM 6TC x 1.5TC STERILE','300 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1298,'X04-E300-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 1MM 6TC x 1.5TC','300 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1299,'X04-E300-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 1MM 6TC x 1.5TC STERILE','300 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1300,'X04-E500-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 0.5MM 6TC x 1.5TC','500 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1301,'X04-E500-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 0.5MM 6TC x 1.5TC STERILE','500 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1302,'X04-E500-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 1MM 6TC x 1.5TC','500 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1303,'X04-E500-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 1MM 6TC x 1.5TC STERILE','500 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1304,'X04-E65U-07-N','Krosflo Max','KROSFLO MAX 41.5CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 ?m','mPES',0.75,51.5,41.5,5000,2.047,46.2,43.0,38.8,48900,916.24,1913.95,'24850','74551',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1305,'X04-E65U-07-S','Krosflo Max','KROSFLO MAX 41.5CM 0.65U MPES 0.75MM 6TC X 1.5TC  STERILE','0.65 ?m','mPES',0.75,51.5,41.5,5000,2.047,46.2,43.0,38.8,48900,916.24,1913.95,'24850','74551',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1306,'X04-E750-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1307,'X04-E750-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 750KD MPES 0.5MM 6TC X 1.5TC STERILE','750 kD','mPES',0.5,51.5,41.5,12000,2.047,31.5,29.5,27.5,78000,977.33,1670.03,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1308,'X04-E750-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 1K MPES 1MM 6TCX1.5TC 1/PK','750 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1309,'X04-E750-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,51.5,41.5,3330,2.047,57.0,52.5,48.0,43000,1084.83,1927.53,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1310,'X04-M10U-06-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.1UM ME 0.6MM 6TC x 1.5TC','0.1 ?m','ME',0.63,51.5,41.5,9000,2.047,37.0,34.0,31.0,74000,1163.7,1677.97,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1311,'X04-M10U-06-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.1UM ME 0.6MM 6TC x 1.5TC STERILE','0.1 ?m','ME',0.63,51.5,41.5,9000,2.047,37.0,34.0,31.0,74000,1163.7,1677.97,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1312,'X04-M20U-06-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 0.6MM 6TC x 1.5TC','0.2 ?m','ME',0.63,51.5,41.5,9000,2.047,37.0,34.0,31.0,74000,1163.7,1677.97,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1313,'X04-M20U-06-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 0.6MM 6TC x 1.5TC STERILE','0.2 ?m','ME',0.63,51.5,41.5,9000,2.047,37.0,34.0,31.0,74000,1163.7,1677.97,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1314,'X04-M20U-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 1MM 6TC x 1.5TC','0.2 ?m','ME',1.0,51.5,41.5,3500,2.047,54.0,50.0,46.0,45500,1140.21,2014.74,'41233','123700',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1315,'X04-M20U-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 1MM 6TC x 1.5TC STERILE','0.2 ?m','ME',1.0,51.5,41.5,3500,2.047,54.0,50.0,46.0,45500,1140.21,2014.74,'41233','123700',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1316,'X04-P20U-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 0.5MM 6TC x 1.5TC','0.2 ?m','PES',0.5,51.5,41.5,14000,2.047,26.6,25.6,24.6,91200,1140.21,1928.2,'20617','61850',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1317,'X04-P20U-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 0.5MM 6TC x 1.5TC STERILE','0.2 ?m','PES',0.5,51.5,41.5,14000,2.047,26.6,25.6,24.6,91200,1140.21,1928.2,'20617','61850',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1318,'X04-P20U-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 1MM 6TC x 1.5TC','0.2 ?m','PES',1.0,51.5,41.5,3350,2.047,53.1,51.2,49.2,43500,1091.35,2008.26,'39466','118399',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1319,'X04-P20U-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 1MM 6TC x 1.5TC STERILE','0.2 ?m','PES',1.0,51.5,41.5,3350,2.047,53.1,51.2,49.2,43500,1091.35,2008.26,'39466','118399',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1320,'X04-S010-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 10K PS 0.5MM 6TC x 1.5TC','10 kD','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1321,'X04-S010-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 10K PS 0.5MM 6TC x 1.5TC STERILE','10 kD','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1322,'X04-S050-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 50K PS 0.5MM 6TC x 1.5TC','50 kD','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1323,'X04-S050-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 50K PS 0.5MM 6TC x 1.5TC STERILE','50 kD','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1324,'X04-S05U-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.05UM PS 0.5MM 6TC x 1.5TC','0.05 ?m','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1325,'X04-S05U-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.05UM PS 0.5MM 6TC x 1.5TC STERILE','0.05 ?m','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1326,'X04-S500-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 500K PS 0.5MM 6TC x 1.5TC','500 kD','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1327,'X04-S500-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 500K PS 0.5MM 6TC x 1.5TC STERILE','500 kD','PS',0.5,51.5,41.5,16000,2.047,26.8,23.6,20.5,104000,1303.1,1981.89,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1328,'X05-E001-05-N','KrosFlo Max','KROSFLO MAX 50CM 1K MPES .5MM 6TCX1.5TC 1/PK','1 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1329,'X05-E001-05-S','KrosFlo Max','KROSFLO MAX 50CM 1K MPES .5MM 6TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1330,'X05-E003-05-N','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 0.5MM 6TC X 1.5TC','3 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1331,'X05-E003-05-S','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 0.5MM 6TC X 1.5TC STERILE','3 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1332,'X05-E003-10-N','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 1MM 6TC X 1.5TC','3 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1333,'X05-E003-10-S','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 1MM 6TC X 1.5TC STERILE','3 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1334,'X05-E005-05-N','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 0.5MM 6TC X 1.5TC','5 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1335,'X05-E005-05-S','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 0.5MM 6TC X 1.5TC STERILE','5 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1336,'X05-E005-10-N','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 1MM 6TC X 1.5TC','5 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1337,'X05-E005-10-S','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 1MM 6TC X 1.5TC STERILE','5 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1338,'X05-E010-05-N','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 0.5MM 6TC x 1.5TC','10 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,26.0,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1339,'X05-E010-05-S','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 0.5MM 6TC x 1.5TC STERILE','10 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,26.0,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1340,'X05-E010-10-N','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 1MM 6TC X 1.5TC','10 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1341,'X05-E010-10-S','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 1MM 6TC X 1.5TC STERILE','10 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1342,'X05-E030-05-N','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 0.5MM 6TC X 1.5TC','30 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1343,'X05-E030-05-S','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 0.5MM 6TC X 1.5TC STERILE','30 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1344,'X05-E030-10-N','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 1MM 6TC X 1.5TC','30 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1345,'X05-E030-10-S','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 1MM 6TC X 1.5TC STERILE','30 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1346,'X05-E050-05-N','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 0.5MM 6TC X 1.5TC','50 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1347,'X05-E050-05-S','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 0.5MM 6TC X 1.5TC STERILE','50 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1348,'X05-E050-10-N','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 1MM 6TC X 1.5TC','50 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1349,'X05-E050-10-S','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 1MM 6TC X 1.5TC STERILE','50 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1350,'X05-E070-05-N','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 0.5MM 6TC X 1.5TC','70 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1351,'X05-E070-05-S','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 0.5MM 6TC X 1.5TC STERILE','70 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1352,'X05-E070-10-N','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 1MM 6TC X 1.5TC','70 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1353,'X05-E070-10-S','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 1MM 6TC X 1.5TC STERILE','70 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1354,'X05-E100-05-N','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 0.5MM 6TC X 1.5TC','100 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1355,'X05-E100-05-S','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 0.5MM 6TC X 1.5TC STERILE','100 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1356,'X05-E100-10-N','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 1MM 6TC X 1.5TC','100 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1357,'X05-E100-10-S','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 1MM 6TC X 1.5TC STERILE','100 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1358,'X05-E20U-05-N','KrosFlo Max','KROSFLO MAX 50CM 0.2UM MPES 0.5MM 6TC x 1.5TC','0.2 ?m','mPES',0.5,60,50.0,11000,2.047,31.5,29.5,27.5,86300,1079.38,2225.57,'16199','48597',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1359,'X05-E20U-05-S','KrosFlo Max','KROSFLO MAX 50CM 0.2UM MPES 0.5MM 6TC x 1.5TC STERILE','0.2 ?m','mPES',0.5,60,50.0,11000,2.047,31.5,29.5,27.5,86300,1079.38,2225.57,'16199','48597',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1360,'X05-E20U-10-N','KrosFlo Max','KROSFLO MAX 50CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1361,'X05-E20U-10-S','KrosFlo Max','KROSFLO MAX 50CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1362,'X05-E300-05-N','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 0.5MM 6TC X 1.5TC','300 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1363,'X05-E300-05-S','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 0.5MM 6TC X 1.5TC STERILE','300 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1364,'X05-E300-10-N','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 1MM 6TC X 1.5TC','300 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1365,'X05-E300-10-S','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 1MM 6TC X 1.5TC STERILE','300 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1366,'X05-E500-05-N','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 0.5MM 6TC X 1.5TC','500 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1367,'X05-E500-05-S','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 0.5MM 6TC X 1.5TC STERILE','500 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1368,'X05-E500-10-N','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 1MM 6TC X 1.5TC','500 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1369,'X05-E500-10-S','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 1MM 6TC X 1.5TC STERILE','500 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1370,'X05-E65U-07-N','Krosflo Max','KROSFLO MAX 50CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 ?m','mPES',0.75,60,50.0,5000,2.047,46.2,43.0,38.8,58000,1103.91,2305.97,'24850','74551',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1371,'X05-E65U-07-S','Krosflo Max','KROSFLO MAX 50CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 ?m','mPES',0.75,60,50.0,5000,2.047,46.2,43.0,38.8,58000,1103.91,2305.97,'24850','74551',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1372,'X05-E750-05-N','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1373,'X05-E750-05-S','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,60,50.0,12000,2.047,31.5,29.5,27.5,94000,1177.5,2012.08,'17671','53014',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1374,'X05-E750-10-N','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 1MM 6TC X 1.5TC','750 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1375,'X05-E750-10-S','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,60,50.0,3330,2.047,57.0,52.5,48.0,52000,1307.03,2322.33,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1376,'X05-M10U-06-N','KrosFlo Max','KROSFLO MAX 50CM 0.1UM ME 0.6MM 6TC x 1.5TC','0.1 ?m','ME',0.63,60,50.0,9000,2.047,37.0,34.0,31.0,89000,1402.05,2021.65,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1377,'X05-M10U-06-S','KrosFlo Max','KROSFLO MAX 50CM 0.1UM ME 0.6MM 6TC x 1.5TC STERILE','0.1 ?m','ME',0.63,60,50.0,9000,2.047,37.0,34.0,31.0,89000,1402.05,2021.65,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1378,'X05-M20U-06-N','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 0.6MM 6TC X 1.5TC','0.2 ?m','ME',0.63,60,50.0,9000,2.047,37.0,34.0,31.0,89000,1402.05,2021.65,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1379,'X05-M20U-06-S','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 0.6MM 6TC X 1.5TC STERILE','0.2 ?m','ME',0.63,60,50.0,9000,2.047,37.0,34.0,31.0,89000,1402.05,2021.65,'26512','79537',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1380,'X05-M20U-10-N','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 1MM 6TC X 1.5TC','0.2 ?m','ME',1.0,60,50.0,3500,2.047,54.0,50.0,46.0,55000,1373.75,2427.4,'41233','123700',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1381,'X05-M20U-10-S','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 1MM 6TC X 1.5TC STERILE','0.2 ?m','ME',1.0,60,50.0,3500,2.047,54.0,50.0,46.0,55000,1373.75,2427.4,'41233','123700',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1382,'X05-P20U-05-N','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 0.5MM 6TC x 1.5TC','0.2 ?m','PES',0.5,60,50.0,14000,2.047,26.6,25.6,24.6,110000,1373.75,2323.13,'20617','61850',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1383,'X05-P20U-05-S','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 0.5MM 6TC x 1.5TC STERILE','0.2 ?m','PES',0.5,60,50.0,14000,2.047,26.6,25.6,24.6,110000,1373.75,2323.13,'20617','61850',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1384,'X05-P20U-10-N','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 1MM 6TC X 1.5TC','0.2 ?m','PES',1.0,60,50.0,3350,2.047,53.1,51.2,49.2,52500,1314.88,2419.59,'39466','118399',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1385,'X05-P20U-10-S','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 1MM 6TC X 1.5TC STERILE','0.2 ?m','PES',1.0,60,50.0,3350,2.047,53.1,51.2,49.2,52500,1314.88,2419.59,'39466','118399',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1386,'X05-S010-05-N','KrosFlo Max','KROSFLO MAX 50CM 10K PS 0.5MM 6TC x 1.5TC','10 kD','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1387,'X05-S010-05-S','KrosFlo Max','KROSFLO MAX 50CM 10K PS 0.5MM 6TC x 1.5TC STERILE','10 kD','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1388,'X05-S050-05-N','KrosFlo Max','KROSFLO MAX 50CM 50K PS 0.5MM 6TC X 1.5TC','50 kD','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1389,'X05-S050-05-S','KrosFlo Max','KROSFLO MAX 50CM 50K PS 0.5MM 6TC X 1.5TC STERILE','50 kD','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1390,'X05-S05U-05-N','KrosFlo Max','KROSFLO MAX 50CM 0.05UM PS 0.5MM 6TC X 1.5TC','0.05 ?m','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1391,'X05-S05U-05-S','KrosFlo Max','KROSFLO MAX 50CM 0.05UM PS 0.5MM 6TC X 1.5TC STERILE','0.05 ?m','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1392,'X05-S500-05-N','KrosFlo Max','KROSFLO MAX 50CM 500K PS 0.5MM 6TC X 1.5TC','500 kD','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1393,'X05-S500-05-S','KrosFlo Max','KROSFLO MAX 50CM 500K PS 0.5MM 6TC X 1.5TC STERILE','500 kD','PS',0.5,60,50.0,16000,2.047,26.8,23.6,20.5,125000,1570.0,2387.81,'23562','70686',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1394,'X06-E001-05-N','KrosFlo Max','KROSFLO MAX 68CM 1K MPES .5MM 6TCX1.5TC 1/PK','1 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1395,'X06-E001-05-S','KrosFlo Max','KROSFLO MAX 68CM 1K MPES .5MM 6TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1396,'X06-E003-05-N','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 0.5MM 6TC x 1.5TC','3 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1397,'X06-E003-05-S','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 0.5MM 6TC x 1.5TC STERILE','3 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1398,'X06-E003-10-N','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 1MM 6TC x 1.5TC','3 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1399,'X06-E003-10-S','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 1MM 6TC x 1.5TC STERILE','3 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1400,'X06-E005-05-N','KrosFlo Max','KROSFLO MAX 68CM 5KD MPES 0.5MM 6TC x 1.5TC','5 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1401,'X06-E005-05-S','KrosFlo Max','KROSFLO MAX 68CM 5K MPES 0.5MM 6TC x 1.5TC STERILE','5 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1402,'X06-E005-10-N','KrosFlo Max','KROSFLO MAX 68CM 5K MPES 1MM 6TC x 1.5TC','5 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1403,'X06-E005-10-S','KrosFlo Max','KROSFLO MAX 68CM 5K MPES 1MM 6TC x 1.5TC STERILE','5 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1404,'X06-E010-05-N','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 0.5MM 6TC x 1.5TC','10 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,26.0,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1405,'X06-E010-05-S','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 0.5MM 6TC x 1.5TC STERILE','10 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,26.0,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1406,'X06-E010-10-N','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 1MM 6TC x 1.5TC','10 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1407,'X06-E010-10-S','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 1MM 6TC x 1.5TC STERILE','10 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1408,'X06-E030-05-N','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 0.5MM 6TC x 1.5TC','30 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1409,'X06-E030-05-S','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 0.5MM 6TC x 1.5TC STERILE','30 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1410,'X06-E030-10-N','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 1MM 6TC x 1.5TC','30 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1411,'X06-E030-10-S','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 1MM 6TC x 1.5TC STERILE','30 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1412,'X06-E050-05-N','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 0.5MM 6TC x 1.5TC','50 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1413,'X06-E050-05-S','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 0.5MM 6TC x 1.5TC STERILE','50 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1414,'X06-E050-10-N','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 1MM 6TC x 1.5TC','50 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1415,'X06-E050-10-S','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 1MM 6TC x 1.5TC STERILE','50 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1416,'X06-E070-05-N','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 0.5MM 6TC x 1.5TC','70 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1417,'X06-E070-05-S','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 0.5MM 6TC x 1.5TC STERILE','70 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1418,'X06-E070-10-N','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 1MM 6TC x 1.5TC','70 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1419,'X06-E070-10-S','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 1MM 6TC x 1.5TC STERILE','70 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1420,'X06-E100-05-N','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 0.5MM 6TC X 1.5TC','100 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1421,'X06-E100-05-S','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 0.5MM 6TC X 1.5TC STERLE','100 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1422,'X06-E100-10-N','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 1MM 6TC x 1.5TC','100 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1423,'X06-E100-10-S','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 1MM 6TC x 1.5TC STERILE','100 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1424,'X06-E20U-05-N','KrosFlo Max','KROSFLO MAX 68CM 0.2UM MPES 0.5MM 6TC x 1.5TC','0.2 ?m','mPES',0.5,78,68.0,11000,2.047,31.5,29.5,27.5,117400,1467.95,3026.77,'16199','48597',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1425,'X06-E20U-05-S','KrosFlo Max','KROSFLO MAX 68CM 0.2UM MPES 0.5MM 6TC x 1.5TC STERILE','0.2 ?m','mPES',0.5,78,68.0,11000,2.047,31.5,29.5,27.5,117400,1467.95,3026.77,'16199','48597',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1426,'X06-E20U-10-N','KrosFlo Max','KROSFLO MAX 68CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1427,'X06-E20U-10-S','KrosFlo Max','KROSFLO MAX 68CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1428,'X06-E300-05-N','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 0.5MM 6TC x 1.5TC','300 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1429,'X06-E300-05-S','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 0.5MM 6TC x 1.5TC STERILE','300 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1430,'X06-E300-10-N','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 1MM 6TC x 1.5TC','300 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1431,'X06-E300-10-S','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 1MM 6TC x 1.5TC STERILE','300 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1432,'X06-E500-05-N','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 0.5MM 6TC x 1.5TC','500 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1433,'X06-E500-05-S','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 0.5MM 6TC x 1.5TC STERILE','500 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1434,'X06-E500-10-N','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 1MM 6TC X 1.5TC','500 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1435,'X06-E500-10-S','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 1MM 6TC X 1.5TC','500 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1436,'X06-E65U-07-N','Krosflo Max','KROSFLO MAX 65CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 ?m','mPES',0.75,78,68.0,5000,2.047,46.2,43.0,38.8,80000,1501.31,3136.12,'24850','74551',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1437,'X06-E65U-07-S','Krosflo Max','KROSFLO MAX 65CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 ?m','mPES',0.75,78,68.0,5000,2.047,46.2,43.0,38.8,80000,1501.31,3136.12,'24850','74551',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1438,'X06-E750-05-N','KrosFlo Max','KROSFLO MAX 68CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1439,'X06-E750-05-S','KrosFlo Max','KROSFLO MAX 68CM 750KD MPES 0.5MM 6TC X 1.5TC STERILE','750 kD','mPES',0.5,78,68.0,12000,2.047,31.5,29.5,27.5,128000,1601.4,2736.43,'17671','53014',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1440,'X06-E750-10-N','KrosFlo Max','KROSFLO MAX 65CM 750KD MPES 1MM 6TC X 1.5TC','750 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1441,'X06-E750-10-S','KrosFlo Max','KROSFLO MAX 65CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,78,68.0,3330,2.047,57.0,52.5,48.0,71000,1777.55,3158.36,'39231','117692',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1442,'X06-M10U-06-N','KrosFlo Max','KROSFLO MAX 68CM 0.1UM ME 0.6MM 6TC x 1.5TC','0.1 ?m','ME',0.63,78,68.0,9000,2.047,37.0,34.0,31.0,121000,1906.79,2749.44,'26512','79537',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1443,'X06-M10U-06-S','KrosFlo Max','KROSFLO MAX 68CM 0.1UM ME 0.6MM 6TC x 1.5TC STERILE','0.1 ?m','ME',0.63,78,68.0,9000,2.047,37.0,34.0,31.0,121000,1906.79,2749.44,'26512','79537',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1444,'X06-M20U-06-N','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 0.6MM 6TC x 1.5TC','0.2 ?m','ME',0.63,78,68.0,9000,2.047,37.0,34.0,31.0,121000,1906.79,2749.44,'26512','79537',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1445,'X06-M20U-06-S','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 0.6MM 6TC x 1.5TC STERILE','0.2 ?m','ME',0.63,78,68.0,9000,2.047,37.0,34.0,31.0,121000,1906.79,2749.44,'26512','79537',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1446,'X06-M20U-10-N','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 1MM 6TC x 1.5TC','0.2 ?m','ME',1.0,78,68.0,3500,2.047,54.0,50.0,46.0,75000,1868.3,3301.26,'41233','123700',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1447,'X06-M20U-10-S','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 1MM 6TC x 1.5TC STERILE','0.2 ?m','ME',1.0,78,68.0,3500,2.047,54.0,50.0,46.0,75000,1868.3,3301.26,'41233','123700',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1448,'X06-P20U-05-N','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 0.5MM 6TC x 1.5TC','0.2 ?m','PES',0.5,78,68.0,14000,2.047,26.6,25.6,24.6,150000,1868.3,3159.46,'20617','61850',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1449,'X06-P20U-05-S','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 0.5MM 6TC x 1.5TC STERILE','0.2 ?m','PES',0.5,78,68.0,14000,2.047,26.6,25.6,24.6,150000,1868.3,3159.46,'20617','61850',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1450,'X06-P20U-10-N','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 1MM 6TC x 1.5TC','0.2 ?m','PES',1.0,78,68.0,3350,2.047,53.1,51.2,49.2,71500,1788.23,3290.64,'39466','118399',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1451,'X06-P20U-10-S','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 1MM 6TC x 1.5TC STERILE','0.2 ?m','PES',1.0,78,68.0,3350,2.047,53.1,51.2,49.2,71500,1788.23,3290.64,'39466','118399',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1452,'X06-S010-05-N','KrosFlo Max','KROSFLO MAX 68CM 10K PS 0.5MM 6TC x 1.5TC','10 kD','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1453,'X06-S010-05-S','KrosFlo Max','KROSFLO MAX 68CM 10K PS 0.5MM 6TC x 1.5TC STERILE','10 kD','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1454,'X06-S050-05-N','KrosFlo Max','KROSFLO MAX 68CM 50K PS 0.5MM 6TC x 1.5TC','50 kD','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1455,'X06-S050-05-S','KrosFlo Max','KROSFLO MAX 68CM 50K PS 0.5MM 6TC x 1.5TC STERILE','50 kD','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1456,'X06-S05U-05-N','KrosFlo Max','KROSFLO MAX 68CM 0.05UM PS 0.5MM 6TC x 1.5TC','0.05 ?m','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1457,'X06-S05U-05-S','KrosFlo Max','KROSFLO MAX 68CM 0.05UM PS 0.5MM 6TC x 1.5TC STERILE','0.05 ?m','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'edit description error 04/02/14 - sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1458,'X06-S500-05-N','KrosFlo Max','KROSFLO MAX 68CM 500K PS 0.5MM 6TC x 1.5TC','500 kD','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1459,'X06-S500-05-S','KrosFlo Max','KROSFLO MAX 68CM 500K PS 0.5MM 6TC x 1.5TC STERILE','500 kD','PS',0.5,78,68.0,16000,2.047,26.8,23.6,20.5,171000,2135.2,3247.43,'23562','70686',NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1460,'X10-E003-10-N','KrosFlo Max','KROSFLO MAX 108CM 3K MPES 1MM 6TC x 1.5TC','3 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1461,'X10-E003-10-S','KrosFlo Max','KROSFLO MAX 108CM 3K MPES 1MM 6TC x 1.5TC STERILE','3 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1462,'X10-E005-10-N','KrosFlo Max','KROSFLO MAX 108CM 5K MPES 1MM 6TC x 1.5TC','5 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1463,'X10-E005-10-S','KrosFlo Max','KROSFLO MAX 108CM 5K MPES 1MM 6TC x 1.5TC STERILE','5 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1464,'X10-E010-10-N','KrosFlo Max','KROSFLO MAX 108CM 10K MPES 1MM 6TC x 1.5TC','10 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1465,'X10-E010-10-S','KrosFlo Max','KROSFLO MAX 108CM 10K MPES 1MM 6TC x 1.5TC STERILE','10 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1466,'X10-E030-10-N','KrosFlo Max','KROSFLO MAX 108CM 30K MPES 1MM 6TC x 1.5TC','30 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1467,'X10-E030-10-S','KrosFlo Max','KROSFLO MAX 108CM 30K MPES 1MM 6TC x 1.5TC STERILE','30 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1468,'X10-E050-10-N','KrosFlo Max','KROSFLO MAX 108CM 50K MPES 1MM 6TC x 1.5TC','50 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1469,'X10-E050-10-S','KrosFlo Max','KROSFLO MAX 108CM 50K MPES 1MM 6TC x 1.5TC STERILE','50 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1470,'X10-E070-10-N','KrosFlo Max','KROSFLO MAX 108CM 70K MPES 1MM 6TC x 1.5TC','70 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1471,'X10-E070-10-S','KrosFlo Max','KROSFLO MAX 108CM 70K MPES 1MM 6TC x 1.5TC STERILE','70 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1472,'X10-E100-10-N','KrosFlo Max','KROSFLO MAX 108CM 100K MPES 1MM 6TC x 1.5TC','100 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1473,'X10-E100-10-S','KrosFlo Max','KROSFLO MAX 108CM 100K MPES 1MM 6TC x 1.5TC STERILE','100 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1474,'X10-E20U-10-N','KrosFlo Max','KROSFLO MAX 108CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 ?m','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1475,'X10-E20U-10-S','KrosFlo Max','KROSFLO MAX 108CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 ?m','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1476,'X10-E300-10-N','KrosFlo Max','KROSFLO MAX 108CM 300K MPES 1MM 6TC x 1.5TC','300 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1477,'X10-E300-10-S','KrosFlo Max','KROSFLO MAX 108CM 300K MPES 1MM 6TC x 1.5TC STERILE','300 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1478,'X10-E500-10-N','KrosFlo Max','KROSFLO MAX 108CM 500K MPES 1MM 6TC x 1.5TC','500 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1479,'X10-E500-10-S','KrosFlo Max','KROSFLO MAX 108CM 500K MPES 1MM 6TC x 1.5TC STERILE','500 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1480,'X10-E65U-07-N','Krosflo Max','KROSFLO MAX 108CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 ?m','mPES',0.75,118,108.0,5000,4.08,46.2,43.0,38.8,127100,2384.44,4206.33,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1481,'X10-E65U-07-S','Krosflo Max','KROSFLO MAX 108CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 ?m','mPES',0.75,118,108.0,5000,4.08,46.2,43.0,38.8,127100,2384.44,4206.33,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1482,'X10-E750-10-N','KrosFlo Max','KROSFLO MAX 108CM 750KD MPES 1MM 6TC X 1.5TC','750 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1483,'X10-E750-10-S','KrosFlo Max','KROSFLO MAX 108CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,118,108.0,3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1484,'X10-M20U-10-N','KrosFlo Max','KROSFLO MAX 108CM 0.2UM ME 1MM 6TC x 1.5TC','0.2 ?m','ME',1.0,118,108.0,3500,4.08,54.0,50.0,46.0,118500,2967.3,4468.62,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1485,'X10-M20U-10-S','KrosFlo Max','KROSFLO MAX 108CM 0.2UM ME 1MM 6TC x 1.5TC STERILE','0.2 ?m','ME',1.0,118,108.0,3500,4.08,54.0,50.0,46.0,118500,2967.3,4468.62,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1486,'X10-P20U-10-N','KrosFlo Max','KROSFLO MAX 108CM 20UM PES 1MM 6TC x 1.5TC','0.2 ?m','PES',1.0,118,108.0,3350,4.08,53.1,51.2,49.2,113500,2840.13,4451.76,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "hollow_fiber_lookup" VALUES (1487,'X10-P20U-10-S','KrosFlo Max','KROSFLO MAX 108CM 20UM PES 1MM 6TC x 1.5TC STERILE','0.2 ?m','PES',1.0,118,108.0,3350,4.08,53.1,51.2,49.2,113500,2840.13,4451.76,'#VALUE!','#VALUE!',NULL,NULL,NULL);
Insert or Replace INTO "filter_plate_insert_lookup" VALUES (1,'TFP99-SP20',121,312);
Insert or Replace INTO "filter_plate_insert_lookup" VALUES (2,'TFP75-SE16',118,252);
Insert or Replace INTO "filter_plate_insert_lookup" VALUES (3,'TFPLS-SA08',8,10);
Insert or Replace INTO "cassette_lookup" VALUES (1,'PP001AP1H','HP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (2,'PP001WP1H','HP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (3,'PP003AP1H','HP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (4,'PP003WP1H','HP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (5,'PP005AP1H','HP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (6,'PP005WP1H','HP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (7,'PP010AP1H','HP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (8,'PP010WP1H','HP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (9,'PP030AP1H','HP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (10,'PP030WP1H','HP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (11,'PP050AP1H','HP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (12,'PP050WP1H','HP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (13,'PP100AP1H','HP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (14,'PP100WP1H','HP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (15,'PP300AP1H','HP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (16,'PP300WP1H','HP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (17,'XP005AP1H','HP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (18,'XP005WP1H','HP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (19,'XP010AP1H','HP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (20,'XP010WP1H','HP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (21,'XP030AP1H','HP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (22,'XP030WP1H','HP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (23,'XP050AP1H','HP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (24,'XP050WP1H','HP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (25,'XP100AP1H','HP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (26,'XP100WP1H','HP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (27,'XP300AP1H','HP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (28,'XP300WP1H','HP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (29,'XPM10AP1H','HP Screen','Pro',100,'HyStream','0.1 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (30,'XPM10WP1H','HP Screen','Pro',100,'HyStream','0.1 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (31,'XPM20AP1H','HP Screen','Pro',100,'HyStream','0.2 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (32,'XPM20WP1H','HP Screen','Pro',100,'HyStream','0.2 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (33,'XPM45AP1H','HP Screen','Pro',100,'HyStream','0.45 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (34,'XPM45WP1H','HP Screen','Pro',100,'HyStream','0.45 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (35,'XPM65AP1H','HP Screen','Pro',100,'HyStream','0.65 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (36,'XPM65WP1H','HP Screen','Pro',100,'HyStream','0.65 �m',0.01,1.2,0.018,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (37,'PP001AP1L','LP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (38,'PP001WP1L','LP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (39,'PP003AP1L','LP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (40,'PP003WP1L','LP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (41,'PP005AP1L','LP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (42,'PP005WP1L','LP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (43,'PP010AP1L','LP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (44,'PP010WP1L','LP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (45,'PP030AP1L','LP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (46,'PP030WP1L','LP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (47,'PP050AP1L','LP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (48,'PP050WP1L','LP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (49,'PP100AP1L','LP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (50,'PP100WP1L','LP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (51,'PP300AP1L','LP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (52,'PP300WP1L','LP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (53,'XP005AP1L','LP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (54,'XP005WP1L','LP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (55,'XP010AP1L','LP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (56,'XP010WP1L','LP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (57,'XP030AP1L','LP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (58,'XP030WP1L','LP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (59,'XP050AP1L','LP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (60,'XP050WP1L','LP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (61,'XP100AP1L','LP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (62,'XP100WP1L','LP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (63,'XP300AP1L','LP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (64,'XP300WP1L','LP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (65,'XPM10AP1L','LP Screen','Pro',100,'HyStream','0.1 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (66,'XPM10WP1L','LP Screen','Pro',100,'HyStream','0.1 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (67,'XPM20AP1L','LP Screen','Pro',100,'HyStream','0.2 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (68,'XPM20WP1L','LP Screen','Pro',100,'HyStream','0.2 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (69,'XPM45AP1L','LP Screen','Pro',100,'HyStream','0.45 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (70,'XPM45WP1L','LP Screen','Pro',100,'HyStream','0.45 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (71,'XPM65AP1L','LP Screen','Pro',100,'HyStream','0.65 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (72,'XPM65WP1L','LP Screen','Pro',100,'HyStream','0.65 �m',0.01,1.2,0.021,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (73,'PP001AP1S','S Channel','Pro',100,'ProStream','1 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (74,'PP001WP1S','S Channel','Pro',100,'ProStream','1 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (75,'PP003AP1S','S Channel','Pro',100,'ProStream','3 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (76,'PP003WP1S','S Channel','Pro',100,'ProStream','3 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (77,'PP005AP1S','S Channel','Pro',100,'ProStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (78,'PP005WP1S','S Channel','Pro',100,'ProStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (79,'PP010AP1S','S Channel','Pro',100,'ProStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (80,'PP010WP1S','S Channel','Pro',100,'ProStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (81,'PP030AP1S','S Channel','Pro',100,'ProStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (82,'PP030WP1S','S Channel','Pro',100,'ProStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (83,'PP050AP1S','S Channel','Pro',100,'ProStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (84,'PP050WP1S','S Channel','Pro',100,'ProStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (85,'PP100AP1S','S Channel','Pro',100,'ProStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (86,'PP100WP1S','S Channel','Pro',100,'ProStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (87,'PP300AP1S','S Channel','Pro',100,'ProStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (88,'PP300WP1S','S Channel','Pro',100,'ProStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (89,'XP005AP1S','S Channel','Pro',100,'HyStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (90,'XP005WP1S','S Channel','Pro',100,'HyStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (91,'XP010AP1S','S Channel','Pro',100,'HyStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (92,'XP010WP1S','S Channel','Pro',100,'HyStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (93,'XP030AP1S','S Channel','Pro',100,'HyStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (94,'XP030WP1S','S Channel','Pro',100,'HyStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (95,'XP050AP1S','S Channel','Pro',100,'HyStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (96,'XP050WP1S','S Channel','Pro',100,'HyStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (97,'XP100AP1S','S Channel','Pro',100,'HyStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (98,'XP100WP1S','S Channel','Pro',100,'HyStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (99,'XP300AP1S','S Channel','Pro',100,'HyStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (100,'XP300WP1S','S Channel','Pro',100,'HyStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (101,'XPM10AP1S','S Channel','Pro',100,'HyStream','0.1 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (102,'XPM10WP1S','S Channel','Pro',100,'HyStream','0.1 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (103,'XPM20AP1S','S Channel','Pro',100,'HyStream','0.2 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (104,'XPM20WP1S','S Channel','Pro',100,'HyStream','0.2 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (105,'XPM45AP1S','S Channel','Pro',100,'HyStream','0.45 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (106,'XPM45WP1S','S Channel','Pro',100,'HyStream','0.45 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (107,'XPM65AP1S','S Channel','Pro',100,'HyStream','0.65 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (108,'XPM65WP1S','S Channel','Pro',100,'HyStream','0.65 �m',0.01,1.2,0.017,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (109,'PP001AP2H','HP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (110,'PP001WP2H','HP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (111,'PP003AP2H','HP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (112,'PP003WP2H','HP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (113,'PP005AP2H','HP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (114,'PP005WP2H','HP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (115,'PP010AP2H','HP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (116,'PP010WP2H','HP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (117,'PP030AP2H','HP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (118,'PP030WP2H','HP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (119,'PP050AP2H','HP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (120,'PP050WP2H','HP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (121,'PP100AP2H','HP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (122,'PP100WP2H','HP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (123,'PP300AP2H','HP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (124,'PP300WP2H','HP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (125,'XP005AP2H','HP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (126,'XP005WP2H','HP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (127,'XP010AP2H','HP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (128,'XP010WP2H','HP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (129,'XP030AP2H','HP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (130,'XP030WP2H','HP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (131,'XP050AP2H','HP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (132,'XP050WP2H','HP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (133,'XP100AP2H','HP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (134,'XP100WP2H','HP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (135,'XP300AP2H','HP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (136,'XP300WP2H','HP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (137,'XPM10AP2H','HP Screen','Pro',200,'HyStream','0.1 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (138,'XPM10WP2H','HP Screen','Pro',200,'HyStream','0.1 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (139,'XPM20AP2H','HP Screen','Pro',200,'HyStream','0.2 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (140,'XPM20WP2H','HP Screen','Pro',200,'HyStream','0.2 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (141,'XPM45AP2H','HP Screen','Pro',200,'HyStream','0.45 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (142,'XPM45WP2H','HP Screen','Pro',200,'HyStream','0.45 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (143,'XPM65AP2H','HP Screen','Pro',200,'HyStream','0.65 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (144,'XPM65WP2H','HP Screen','Pro',200,'HyStream','0.65 �m',0.02,2.1,0.018,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (145,'PP001AP2L','LP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (146,'PP001WP2L','LP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (147,'PP003AP2L','LP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (148,'PP003WP2L','LP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (149,'PP005AP2L','LP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (150,'PP005WP2L','LP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (151,'PP010AP2L','LP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (152,'PP010WP2L','LP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (153,'PP020AP2L','LP Screen','Pro',200,'ProStream','20 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (154,'PP030AP2L','LP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (155,'PP030WP2L','LP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (156,'PP050AP2L','LP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (157,'PP050WP2L','LP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (158,'PP100AP2L','LP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (159,'PP100WP2L','LP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (160,'PP300AP2L','LP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (161,'PP300WP2L','LP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (162,'PPH01AP2L','LP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (163,'PPN50AP2L','LP Screen','Pro',200,'ProStream','0.5 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (164,'PPN65WP2L','LP Screen','Pro',200,'ProStream','0.65 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (165,'XP005AP2L','LP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (166,'XP005WP2L','LP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (167,'XP010AP2L','LP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (168,'XP010WP2L','LP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (169,'XP030AP2L','LP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (170,'XP030WP2L','LP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (171,'XP050AP2L','LP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (172,'XP050WP2L','LP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (173,'XP100AP2L','LP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (174,'XP100WP2L','LP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (175,'XP300AP2L','LP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (176,'XP300WP2L','LP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (177,'XPM10AP2L','LP Screen','Pro',200,'HyStream','0.1 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (178,'XPM10WP2L','LP Screen','Pro',200,'HyStream','0.1 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (179,'XPM20AP2L','LP Screen','Pro',200,'HyStream','0.2 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (180,'XPM20WP2L','LP Screen','Pro',200,'HyStream','0.2 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (181,'XPM45AP2L','LP Screen','Pro',200,'HyStream','0.45 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (182,'XPM45WP2L','LP Screen','Pro',200,'HyStream','0.45 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (183,'XPM65AP2L','LP Screen','Pro',200,'HyStream','0.65 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (184,'XPM65WP2L','LP Screen','Pro',200,'HyStream','0.65 �m',0.02,2.1,0.021,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (185,'PP001AP2S','S Channel','Pro',200,'ProStream','1 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (186,'PP001WP2S','S Channel','Pro',200,'ProStream','1 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (187,'PP003AP2S','S Channel','Pro',200,'ProStream','3 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (188,'PP003WP2S','S Channel','Pro',200,'ProStream','3 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (189,'PP005AP2S','S Channel','Pro',200,'ProStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (190,'PP005WP2S','S Channel','Pro',200,'ProStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (191,'PP010AP2S','S Channel','Pro',200,'ProStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (192,'PP010WP2S','S Channel','Pro',200,'ProStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (193,'PP030AP2S','S Channel','Pro',200,'ProStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (194,'PP030WP2S','S Channel','Pro',200,'ProStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (195,'PP050AP2S','S Channel','Pro',200,'ProStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (196,'PP050WP2S','S Channel','Pro',200,'ProStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (197,'PP100AP2S','S Channel','Pro',200,'ProStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (198,'PP100WP2S','S Channel','Pro',200,'ProStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (199,'PP300AP2S','S Channel','Pro',200,'ProStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (200,'PP300WP2S','S Channel','Pro',200,'ProStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (201,'PPN50AP2S','S Channel','Pro',200,'ProStream','0.5 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (202,'XP005AP2S','S Channel','Pro',200,'HyStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (203,'XP005WP2S','S Channel','Pro',200,'HyStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (204,'XP010AP2S','S Channel','Pro',200,'HyStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (205,'XP010WP2S','S Channel','Pro',200,'HyStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (206,'XP030AP2S','S Channel','Pro',200,'HyStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (207,'XP030WP2S','S Channel','Pro',200,'HyStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (208,'XP050AP2S','S Channel','Pro',200,'HyStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (209,'XP050WP2S','S Channel','Pro',200,'HyStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (210,'XP100AP2S','S Channel','Pro',200,'HyStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (211,'XP100WP2S','S Channel','Pro',200,'HyStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (212,'XP300AP2S','S Channel','Pro',200,'HyStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (213,'XP300WP2S','S Channel','Pro',200,'HyStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (214,'XPM10AP2S','S Channel','Pro',200,'HyStream','0.1 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (215,'XPM10WP2S','S Channel','Pro',200,'HyStream','0.1 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (216,'XPM20AP2S','S Channel','Pro',200,'HyStream','0.2 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (217,'XPM20WP2S','S Channel','Pro',200,'HyStream','0.2 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (218,'XPM45AP2S','S Channel','Pro',200,'HyStream','0.45 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (219,'XPM45WP2S','S Channel','Pro',200,'HyStream','0.45 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (220,'XPM65AP2S','S Channel','Pro',200,'HyStream','0.65 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (221,'XPM65WP2S','S Channel','Pro',200,'HyStream','0.65 �m',0.02,2.1,0.017,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (222,'PP001W01H','HP Screen','Pro',1000,'ProStream','1 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (223,'PP003A01H','HP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (224,'PP003W01H','HP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (225,'PP005A01H','HP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (226,'PP005W01H','HP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (227,'PP010A01H','HP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (228,'PP010W01H','HP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (229,'PP030A01H','HP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (230,'PP030W01H','HP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (231,'PP050A01H','HP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (232,'PP050W01H','HP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (233,'PP100A01H','HP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (234,'PP100W01H','HP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (235,'PP300A01H','HP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (236,'PP300W01H','HP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (237,'XP005A01H','HP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (238,'XP005W01H','HP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (239,'XP010A01H','HP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (240,'XP010W01H','HP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (241,'XP030A01H','HP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (242,'XP030W01H','HP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (243,'XP050A01H','HP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (244,'XP050W01H','HP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (245,'XP100A01H','HP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (246,'XP100W01H','HP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (247,'XP300A01H','HP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (248,'XP300W01H','HP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (249,'XPM10A01H','HP Screen','Pro',1000,'HyStream','0.1 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (250,'XPM10W01H','HP Screen','Pro',1000,'HyStream','0.1 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (251,'XPM20A01H','HP Screen','Pro',1000,'HyStream','0.2 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (252,'XPM20W01H','HP Screen','Pro',1000,'HyStream','0.2 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (253,'XPM45A01H','HP Screen','Pro',1000,'HyStream','0.45 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (254,'XPM45W01H','HP Screen','Pro',1000,'HyStream','0.45 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (255,'XPM65A01H','HP Screen','Pro',1000,'HyStream','0.65 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (256,'XPM65W01H','HP Screen','Pro',1000,'HyStream','0.65 �m',0.1,8.7,0.018,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (257,'PP001A01L','LP Screen','Pro',1000,'ProStream','1 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (258,'PP001W01L','LP Screen','Pro',1000,'ProStream','1 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (259,'PP003A01L','LP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (260,'PP003W01L','LP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (261,'PP005A01L','LP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (262,'PP005W01L','LP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (263,'PP010A01L','LP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (264,'PP010W01L','LP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (265,'PP030A01L','LP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (266,'PP030W01L','LP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (267,'PP050A01L','LP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (268,'PP050W01L','LP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (269,'PP100A01L','LP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (270,'PP100W01L','LP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (271,'PP300A01L','LP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (272,'PP300W01L','LP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (273,'PPN50A01L','LP Screen','Pro',1000,'ProStream','0.5 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (274,'PPN65W01L','LP Screen','Pro',1000,'ProStream','0.65 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (275,'XP005A01L','LP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (276,'XP005W01L','LP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (277,'XP010A01L','LP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (278,'XP010W01L','LP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (279,'XP030A01L','LP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (280,'XP030W01L','LP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (281,'XP050A01L','LP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (282,'XP050W01L','LP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (283,'XP100A01L','LP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (284,'XP100W01L','LP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (285,'XP300A01L','LP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (286,'XP300W01L','LP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (287,'XPM10A01L','LP Screen','Pro',1000,'HyStream','0.1 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (288,'XPM10W01L','LP Screen','Pro',1000,'HyStream','0.1 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (289,'XPM20A01L','LP Screen','Pro',1000,'HyStream','0.2 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (290,'XPM20W01L','LP Screen','Pro',1000,'HyStream','0.2 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (291,'XPM45A01L','LP Screen','Pro',1000,'HyStream','0.45 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (292,'XPM45W01L','LP Screen','Pro',1000,'HyStream','0.45 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (293,'XPM65A01L','LP Screen','Pro',1000,'HyStream','0.65 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (294,'XPM65W01L','LP Screen','Pro',1000,'HyStream','0.65 �m',0.1,8.7,0.021,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (295,'PP001W01S','S Channel','Pro',1000,'ProStream','1 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (296,'PP003A01S','S Channel','Pro',1000,'ProStream','3 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (297,'PP003W01S','S Channel','Pro',1000,'ProStream','3 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (298,'PP005A01S','S Channel','Pro',1000,'ProStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (299,'PP005W01S','S Channel','Pro',1000,'ProStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (300,'PP010A01S','S Channel','Pro',1000,'ProStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (301,'PP010W01S','S Channel','Pro',1000,'ProStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (302,'PP030A01S','S Channel','Pro',1000,'ProStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (303,'PP030W01S','S Channel','Pro',1000,'ProStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (304,'PP050A01S','S Channel','Pro',1000,'ProStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (305,'PP050W01S','S Channel','Pro',1000,'ProStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (306,'PP100A01S','S Channel','Pro',1000,'ProStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (307,'PP100W01S','S Channel','Pro',1000,'ProStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (308,'PP300A01S','S Channel','Pro',1000,'ProStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (309,'PP300W01S','S Channel','Pro',1000,'ProStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (310,'XP005A01S','S Channel','Pro',1000,'HyStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (311,'XP005W01S','S Channel','Pro',1000,'HyStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (312,'XP010A01S','S Channel','Pro',1000,'HyStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (313,'XP010W01S','S Channel','Pro',1000,'HyStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (314,'XP030A01S','S Channel','Pro',1000,'HyStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (315,'XP030W01S','S Channel','Pro',1000,'HyStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (316,'XP050A01S','S Channel','Pro',1000,'HyStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (317,'XP050W01S','S Channel','Pro',1000,'HyStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (318,'XP100A01S','S Channel','Pro',1000,'HyStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (319,'XP100W01S','S Channel','Pro',1000,'HyStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (320,'XP300A01S','S Channel','Pro',1000,'HyStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (321,'XP300W01S','S Channel','Pro',1000,'HyStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (322,'XPM10A01S','S Channel','Pro',1000,'HyStream','0.1 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (323,'XPM10W01S','S Channel','Pro',1000,'HyStream','0.1 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (324,'XPM20A01S','S Channel','Pro',1000,'HyStream','0.2 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (325,'XPM20W01S','S Channel','Pro',1000,'HyStream','0.2 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (326,'XPM45A01S','S Channel','Pro',1000,'HyStream','0.45 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (327,'XPM45W01S','S Channel','Pro',1000,'HyStream','0.45 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (328,'XPM65A01S','S Channel','Pro',1000,'HyStream','0.65 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (329,'XPM65W01S','S Channel','Pro',1000,'HyStream','0.65 �m',0.1,8.7,0.017,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (330,'PP001B05H','HP Screen','Pro',5000,'ProStream','1 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (331,'PP003B05H','HP Screen','Pro',5000,'ProStream','3 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (332,'PP005B05H','HP Screen','Pro',5000,'ProStream','5 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (333,'PP010B05H','HP Screen','Pro',5000,'ProStream','10 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (334,'PP030B05H','HP Screen','Pro',5000,'ProStream','30 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (335,'PP050B05H','HP Screen','Pro',5000,'ProStream','50 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (336,'PP100B05H','HP Screen','Pro',5000,'ProStream','100 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (337,'PP300B05H','HP Screen','Pro',5000,'ProStream','300 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (338,'XP005B05H','HP Screen','Pro',5000,'HyStream','5 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (339,'XP010B05H','HP Screen','Pro',5000,'HyStream','10 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (340,'XP030B05H','HP Screen','Pro',5000,'HyStream','30 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (341,'XP050B05H','HP Screen','Pro',5000,'HyStream','50 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (342,'XP100B05H','HP Screen','Pro',5000,'HyStream','100 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (343,'XP300B05H','HP Screen','Pro',5000,'HyStream','300 kD',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (344,'XPM10B05H','HP Screen','Pro',5000,'HyStream','0.1 �m',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (345,'XPM20B05H','HP Screen','Pro',5000,'HyStream','0.2 �m',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (346,'XPM45B05H','HP Screen','Pro',5000,'HyStream','0.45 �m',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (347,'XPM65B05H','HP Screen','Pro',5000,'HyStream','0.65 �m',0.5,38.0,0.018,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (348,'PP001B05L','LP Screen','Pro',5000,'ProStream','1 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (349,'PP003B05L','LP Screen','Pro',5000,'ProStream','3 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (350,'PP005B05L','LP Screen','Pro',5000,'ProStream','5 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (351,'PP010B05L','LP Screen','Pro',5000,'ProStream','10 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (352,'PP030B05L','LP Screen','Pro',5000,'ProStream','30 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (353,'PP050B05L','LP Screen','Pro',5000,'ProStream','50 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (354,'PP100B05L','LP Screen','Pro',5000,'ProStream','100 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (355,'PP300B05L','LP Screen','Pro',5000,'ProStream','300 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (356,'PPN65B05L','LP Screen','Pro',5000,'ProStream','0.65 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (357,'XP005B05L','LP Screen','Pro',5000,'HyStream','5 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (358,'XP010B05L','LP Screen','Pro',5000,'HyStream','10 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (359,'XP030B05L','LP Screen','Pro',5000,'HyStream','30 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (360,'XP050B05L','LP Screen','Pro',5000,'HyStream','50 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (361,'XP100B05L','LP Screen','Pro',5000,'HyStream','100 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (362,'XP300B05L','LP Screen','Pro',5000,'HyStream','300 kD',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (363,'XPM10B05L','LP Screen','Pro',5000,'HyStream','0.1 �m',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (364,'XPM20B05L','LP Screen','Pro',5000,'HyStream','0.2 �m',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (365,'XPM45B05L','LP Screen','Pro',5000,'HyStream','0.45 �m',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (366,'XPM65B05L','LP Screen','Pro',5000,'HyStream','0.65 �m',0.5,38.0,0.021,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (367,'PP001B05S','S Channel','Pro',5000,'ProStream','1 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (368,'PP003B05S','S Channel','Pro',5000,'ProStream','3 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (369,'PP005B05S','S Channel','Pro',5000,'ProStream','5 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (370,'PP010B05S','S Channel','Pro',5000,'ProStream','10 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (371,'PP030B05S','S Channel','Pro',5000,'ProStream','30 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (372,'PP050B05S','S Channel','Pro',5000,'ProStream','50 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (373,'PP100B05S','S Channel','Pro',5000,'ProStream','100 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (374,'PP300B05S','S Channel','Pro',5000,'ProStream','300 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (375,'XP005B05S','S Channel','Pro',5000,'HyStream','5 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (376,'XP010B05S','S Channel','Pro',5000,'HyStream','10 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (377,'XP030B05S','S Channel','Pro',5000,'HyStream','30 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (378,'XP050B05S','S Channel','Pro',5000,'HyStream','50 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (379,'XP100B05S','S Channel','Pro',5000,'HyStream','100 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (380,'XP300B05S','S Channel','Pro',5000,'HyStream','300 kD',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (381,'XPM10B05S','S Channel','Pro',5000,'HyStream','0.1 �m',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (382,'XPM20B05S','S Channel','Pro',5000,'HyStream','0.2 �m',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (383,'XPM45B05S','S Channel','Pro',5000,'HyStream','0.45 �m',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (384,'XPM65B05S','S Channel','Pro',5000,'HyStream','0.65 �m',0.5,38.0,0.017,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (385,'PP001B15H','HP Screen','Pro',15000,'ProStream','1 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (386,'PP003B15H','HP Screen','Pro',15000,'ProStream','3 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (387,'PP005B15H','HP Screen','Pro',15000,'ProStream','5 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (388,'PP010B15H','HP Screen','Pro',15000,'ProStream','10 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (389,'PP030B15H','HP Screen','Pro',15000,'ProStream','30 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (390,'PP050B15H','HP Screen','Pro',15000,'ProStream','50 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (391,'PP100B15H','HP Screen','Pro',15000,'ProStream','100 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (392,'PP300B15H','HP Screen','Pro',15000,'ProStream','300 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (393,'XP005B15H','HP Screen','Pro',15000,'HyStream','5 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (394,'XP010B15H','HP Screen','Pro',15000,'HyStream','10 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (395,'XP030B15H','HP Screen','Pro',15000,'HyStream','30 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (396,'XP050B15H','HP Screen','Pro',15000,'HyStream','50 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (397,'XP100B15H','HP Screen','Pro',15000,'HyStream','100 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (398,'XP300B15H','HP Screen','Pro',15000,'HyStream','300 kD',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (399,'XPM10B15H','HP Screen','Pro',15000,'HyStream','0.1 �m',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (400,'XPM20B15H','HP Screen','Pro',15000,'HyStream','0.2 �m',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (401,'XPM45B15H','HP Screen','Pro',15000,'HyStream','0.45 �m',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (402,'XPM65B15H','HP Screen','Pro',15000,'HyStream','0.65 �m',1.5,114.0,0.018,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (403,'PP001B15L','LP Screen','Pro',15000,'ProStream','1 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (404,'PP003B15L','LP Screen','Pro',15000,'ProStream','3 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (405,'PP005B15L','LP Screen','Pro',15000,'ProStream','5 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (406,'PP010B15L','LP Screen','Pro',15000,'ProStream','10 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (407,'PP030B15L','LP Screen','Pro',15000,'ProStream','30 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (408,'PP050B15L','LP Screen','Pro',15000,'ProStream','50 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (409,'PP100B15L','LP Screen','Pro',15000,'ProStream','100 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (410,'PP300B15L','LP Screen','Pro',15000,'ProStream','300 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (411,'PPN65B15L','LP Screen','Pro',15000,'ProStream','0.65 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (412,'XP005B15L','LP Screen','Pro',15000,'HyStream','5 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (413,'XP010B15L','LP Screen','Pro',15000,'HyStream','10 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (414,'XP030B15L','LP Screen','Pro',15000,'HyStream','30 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (415,'XP050B15L','LP Screen','Pro',15000,'HyStream','50 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (416,'XP100B15L','LP Screen','Pro',15000,'HyStream','100 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (417,'XP300B15L','LP Screen','Pro',15000,'HyStream','300 kD',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (418,'XPM10B15L','LP Screen','Pro',15000,'HyStream','0.1 �m',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (419,'XPM20B15L','LP Screen','Pro',15000,'HyStream','0.2 �m',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (420,'XPM45B15L','LP Screen','Pro',15000,'HyStream','0.45 �m',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (421,'XPM65B15L','LP Screen','Pro',15000,'HyStream','0.65 �m',1.5,114.0,0.021,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (422,'PP001B15S','S Channel','Pro',15000,'ProStream','1 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (423,'PP003B15S','S Channel','Pro',15000,'ProStream','3 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (424,'PP005B15S','S Channel','Pro',15000,'ProStream','5 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (425,'PP010B15S','S Channel','Pro',15000,'ProStream','10 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (426,'PP030B15S','S Channel','Pro',15000,'ProStream','30 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (427,'PP050B15S','S Channel','Pro',15000,'ProStream','50 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (428,'PP100B15S','S Channel','Pro',15000,'ProStream','100 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (429,'PP300B15S','S Channel','Pro',15000,'ProStream','300 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (430,'XP005B15S','S Channel','Pro',15000,'HyStream','5 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (431,'XP010B15S','S Channel','Pro',15000,'HyStream','10 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (432,'XP030B15S','S Channel','Pro',15000,'HyStream','30 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (433,'XP050B15S','S Channel','Pro',15000,'HyStream','50 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (434,'XP100B15S','S Channel','Pro',15000,'HyStream','100 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (435,'XP300B15S','S Channel','Pro',15000,'HyStream','300 kD',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (436,'XPM10B15S','S Channel','Pro',15000,'HyStream','0.1 �m',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (437,'XPM20B15S','S Channel','Pro',15000,'HyStream','0.2 �m',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (438,'XPM45B15S','S Channel','Pro',15000,'HyStream','0.45 �m',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (439,'XPM65B15S','S Channel','Pro',15000,'HyStream','0.65 �m',1.5,114.0,0.017,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (440,'PP001B25H','HP Screen','Pro',25000,'ProStream','1 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (441,'PP003B25H','HP Screen','Pro',25000,'ProStream','3 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (442,'PP005B25H','HP Screen','Pro',25000,'ProStream','5 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (443,'PP010B25H','HP Screen','Pro',25000,'ProStream','10 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (444,'PP030B25H','HP Screen','Pro',25000,'ProStream','30 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (445,'PP050B25H','HP Screen','Pro',25000,'ProStream','50 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (446,'PP100B25H','HP Screen','Pro',25000,'ProStream','100 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (447,'PP300B25H','HP Screen','Pro',25000,'ProStream','300 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (448,'XP005B25H','HP Screen','Pro',25000,'HyStream','5 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (449,'XP010B25H','HP Screen','Pro',25000,'HyStream','10 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (450,'XP030B25H','HP Screen','Pro',25000,'HyStream','30 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (451,'XP050B25H','HP Screen','Pro',25000,'HyStream','50 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (452,'XP100B25H','HP Screen','Pro',25000,'HyStream','100 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (453,'XP300B25H','HP Screen','Pro',25000,'HyStream','300 kD',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (454,'XPM10B25H','HP Screen','Pro',25000,'HyStream','0.1 �m',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (455,'XPM20B25H','HP Screen','Pro',25000,'HyStream','0.2 �m',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (456,'XPM45B25H','HP Screen','Pro',25000,'HyStream','0.45 �m',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (457,'XPM65B25H','HP Screen','Pro',25000,'HyStream','0.65 �m',2.5,190.0,0.018,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (458,'PP001B25L','LP Screen','Pro',25000,'ProStream','1 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (459,'PP003B25L','LP Screen','Pro',25000,'ProStream','3 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (460,'PP005B25L','LP Screen','Pro',25000,'ProStream','5 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (461,'PP010B25L','LP Screen','Pro',25000,'ProStream','10 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (462,'PP030B25L','LP Screen','Pro',25000,'ProStream','30 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (463,'PP050B25L','LP Screen','Pro',25000,'ProStream','50 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (464,'PP100B25L','LP Screen','Pro',25000,'ProStream','100 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (465,'PP300B25L','LP Screen','Pro',25000,'ProStream','300 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (466,'PPN65B25L','LP Screen','Pro',25000,'ProStream','0.65 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (467,'XP005B25L','LP Screen','Pro',25000,'HyStream','5 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (468,'XP010B25L','LP Screen','Pro',25000,'HyStream','10 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (469,'XP030B25L','LP Screen','Pro',25000,'HyStream','30 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (470,'XP050B25L','LP Screen','Pro',25000,'HyStream','50 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (471,'XP100B25L','LP Screen','Pro',25000,'HyStream','100 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (472,'XP300B25L','LP Screen','Pro',25000,'HyStream','300 kD',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (473,'XPM10B25L','LP Screen','Pro',25000,'HyStream','0.1 �m',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (474,'XPM45B25L','LP Screen','Pro',25000,'HyStream','0.45 �m',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (475,'XPM65B25L','LP Screen','Pro',25000,'HyStream','0.65 �m',2.5,190.0,0.021,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (476,'PP001LP1J','0.5mm Open','SIUS',100,'ProStream','1 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (477,'PP001MP1J','0.5mm Open','SIUS',100,'ProStream','1 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (478,'PP003LP1J','0.5mm Open','SIUS',100,'ProStream','3 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (479,'PP003MP1J','0.5mm Open','SIUS',100,'ProStream','3 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (480,'PP005LP1J','0.5mm Open','SIUS',100,'ProStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (481,'PP005MP1J','0.5mm Open','SIUS',100,'ProStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (482,'PP010LP1J','0.5mm Open','SIUS',100,'ProStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (483,'PP010MP1J','0.5mm Open','SIUS',100,'ProStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (484,'PP030LP1J','0.5mm Open','SIUS',100,'ProStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (485,'PP030MP1J','0.5mm Open','SIUS',100,'ProStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (486,'PP050LP1J','0.5mm Open','SIUS',100,'ProStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (487,'PP050MP1J','0.5mm Open','SIUS',100,'ProStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (488,'PP100LP1J','0.5mm Open','SIUS',100,'ProStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (489,'PP100MP1J','0.5mm Open','SIUS',100,'ProStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (490,'PP300LP1J','0.5mm Open','SIUS',100,'ProStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (491,'PP300MP1J','0.5mm Open','SIUS',100,'ProStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (492,'PPN50LP1J','0.5mm Open','SIUS',100,'ProStream','0.5 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (493,'XP005LP1J','0.5mm Open','SIUS',100,'HyStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (494,'XP005MP1J','0.5mm Open','SIUS',100,'HyStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (495,'XP010LP1J','0.5mm Open','SIUS',100,'HyStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (496,'XP010MP1J','0.5mm Open','SIUS',100,'HyStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (497,'XP030LP1J','0.5mm Open','SIUS',100,'HyStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (498,'XP030MP1J','0.5mm Open','SIUS',100,'HyStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (499,'XP050LP1J','0.5mm Open','SIUS',100,'HyStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (500,'XP050MP1J','0.5mm Open','SIUS',100,'HyStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (501,'XP100LP1J','0.5mm Open','SIUS',100,'HyStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (502,'XP100MP1J','0.5mm Open','SIUS',100,'HyStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (503,'XP300LP1J','0.5mm Open','SIUS',100,'HyStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (504,'XP300MP1J','0.5mm Open','SIUS',100,'HyStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (505,'XPM10LP1J','0.5mm Open','SIUS',100,'HyStream','0.1 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (506,'XPM10MP1J','0.5mm Open','SIUS',100,'HyStream','0.1 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (507,'XPM20LP1J','0.5mm Open','SIUS',100,'HyStream','0.2 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (508,'XPM20MP1J','0.5mm Open','SIUS',100,'HyStream','0.2 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (509,'XPM45LP1J','0.5mm Open','SIUS',100,'HyStream','0.45 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (510,'XPM45MP1J','0.5mm Open','SIUS',100,'HyStream','0.45 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (511,'XPM65LP1J','0.5mm Open','SIUS',100,'HyStream','0.65 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (512,'XPM65MP1J','0.5mm Open','SIUS',100,'HyStream','0.65 �m',0.01,1.2,0.05,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (513,'PP001LP1L','LP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (514,'PP001MP1L','LP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (515,'PP003LP1L','LP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (516,'PP003MP1L','LP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (517,'PP005LP1L','LP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (518,'PP005MP1L','LP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (519,'PP010LP1L','LP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (520,'PP010MP1L','LP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (521,'PP030LP1L','LP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (522,'PP030MP1L','LP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (523,'PP050LP1L','LP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (524,'PP050MP1L','LP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (525,'PP100LP1L','LP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (526,'PP100MP1L','LP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (527,'PP300LP1L','LP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (528,'PP300MP1L','LP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (529,'PPN50LP1L','LP Screen','SIUS',100,'ProStream','0.5 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (530,'PPN65LP1L','LP Screen','SIUS',100,'ProStream','0.65 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (531,'XP005LP1L','LP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (532,'XP005MP1L','LP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (533,'XP010LP1L','LP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (534,'XP010MP1L','LP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (535,'XP030LP1L','LP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (536,'XP030MP1L','LP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (537,'XP050LP1L','LP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (538,'XP050MP1L','LP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (539,'XP100LP1L','LP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (540,'XP100MP1L','LP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (541,'XP300LP1L','LP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (542,'XP300MP1L','LP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (543,'XPM10LP1L','LP Screen','SIUS',100,'HyStream','0.1 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (544,'XPM10MP1L','LP Screen','SIUS',100,'HyStream','0.1 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (545,'XPM20LP1L','LP Screen','SIUS',100,'HyStream','0.2 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (546,'XPM20MP1L','LP Screen','SIUS',100,'HyStream','0.2 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (547,'XPM45LP1L','LP Screen','SIUS',100,'HyStream','0.45 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (548,'XPM45MP1L','LP Screen','SIUS',100,'HyStream','0.45 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (549,'XPM65LP1L','LP Screen','SIUS',100,'HyStream','0.65 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (550,'XPM65MP1L','LP Screen','SIUS',100,'HyStream','0.65 �m',0.01,1.2,0.014,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (551,'PP001LP2J','0.5mm Open','SIUS',200,'ProStream','1 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (552,'PP001MP2J','0.5mm Open','SIUS',200,'ProStream','1 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (553,'PP003LP2J','0.5mm Open','SIUS',200,'ProStream','3 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (554,'PP003MP2J','0.5mm Open','SIUS',200,'ProStream','3 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (555,'PP005LP2J','0.5mm Open','SIUS',200,'ProStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (556,'PP005MP2J','0.5mm Open','SIUS',200,'ProStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (557,'PP010LP2J','0.5mm Open','SIUS',200,'ProStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (558,'PP010MP2J','0.5mm Open','SIUS',200,'ProStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (559,'PP030LP2J','0.5mm Open','SIUS',200,'ProStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (560,'PP030MP2J','0.5mm Open','SIUS',200,'ProStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (561,'PP050LP2J','0.5mm Open','SIUS',200,'ProStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (562,'PP050MP2J','0.5mm Open','SIUS',200,'ProStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (563,'PP100LP2J','0.5mm Open','SIUS',200,'ProStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (564,'PP100MP2J','0.5mm Open','SIUS',200,'ProStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (565,'PP300LP2J','0.5mm Open','SIUS',200,'ProStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (566,'PP300MP2J','0.5mm Open','SIUS',200,'ProStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (567,'XP005LP2J','0.5mm Open','SIUS',200,'HyStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (568,'XP005MP2J','0.5mm Open','SIUS',200,'HyStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (569,'XP010LP2J','0.5mm Open','SIUS',200,'HyStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (570,'XP010MP2J','0.5mm Open','SIUS',200,'HyStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (571,'XP030LP2J','0.5mm Open','SIUS',200,'HyStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (572,'XP030MP2J','0.5mm Open','SIUS',200,'HyStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (573,'XP050LP2J','0.5mm Open','SIUS',200,'HyStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (574,'XP050MP2J','0.5mm Open','SIUS',200,'HyStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (575,'XP100LP2J','0.5mm Open','SIUS',200,'HyStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (576,'XP100MP2J','0.5mm Open','SIUS',200,'HyStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (577,'XP300LP2J','0.5mm Open','SIUS',200,'HyStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (578,'XP300MP2J','0.5mm Open','SIUS',200,'HyStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (579,'XPM10LP2J','0.5mm Open','SIUS',200,'HyStream','0.1 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (580,'XPM10MP2J','0.5mm Open','SIUS',200,'HyStream','0.1 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (581,'XPM20LP2J','0.5mm Open','SIUS',200,'HyStream','0.2 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (582,'XPM20MP2J','0.5mm Open','SIUS',200,'HyStream','0.2 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (583,'XPM45LP2J','0.5mm Open','SIUS',200,'HyStream','0.45 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (584,'XPM45MP2J','0.5mm Open','SIUS',200,'HyStream','0.45 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (585,'XPM65LP2J','0.5mm Open','SIUS',200,'HyStream','0.65 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (586,'XPM65MP2J','0.5mm Open','SIUS',200,'HyStream','0.65 �m',0.02,2.1,0.05,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (587,'PP001LP2L','LP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (588,'PP001MP2L','LP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (589,'PP003LP2L','LP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (590,'PP003MP2L','LP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (591,'PP005LP2L','LP Screen','SIUS',200,'ProStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (592,'PP005MP2L','LP Screen','SIUS',200,'ProStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (593,'PP010LP2L','LP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (594,'PP010MP2L','LP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (595,'PP030LP2L','LP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (596,'PP030MP2L','LP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (597,'PP050LP2L','LP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (598,'PP050MP2L','LP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (599,'PP100LP2L','LP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (600,'PP100MP2L','LP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (601,'PP300LP2L','LP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (602,'PP300MP2L','LP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (603,'PPH01LP2L','LP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (604,'XP005LP2L','LP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (605,'XP005MP2L','LP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (606,'XP010LP2L','LP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (607,'XP010MP2L','LP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (608,'XP030LP2L','LP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (609,'XP030MP2L','LP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (610,'XP050LP2L','LP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (611,'XP050MP2L','LP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (612,'XP100LP2L','LP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (613,'XP100MP2L','LP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (614,'XP300LP2L','LP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (615,'XP300MP2L','LP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (616,'XPM10LP2L','LP Screen','SIUS',200,'HyStream','0.1 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (617,'XPM10MP2L','LP Screen','SIUS',200,'HyStream','0.1 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (618,'XPM20LP2L','LP Screen','SIUS',200,'HyStream','0.2 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (619,'XPM20MP2L','LP Screen','SIUS',200,'HyStream','0.2 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (620,'XPM45LP2L','LP Screen','SIUS',200,'HyStream','0.45 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (621,'XPM45MP2L','LP Screen','SIUS',200,'HyStream','0.45 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (622,'XPM65LP2L','LP Screen','SIUS',200,'HyStream','0.65 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (623,'XPM65MP2L','LP Screen','SIUS',200,'HyStream','0.65 �m',0.02,2.1,0.014,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (624,'PP001L01J','0.5mm Open','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (625,'PP001M01J','0.5mm Open','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (626,'PP003L01J','0.5mm Open','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (627,'PP003M01J','0.5mm Open','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (628,'PP005L01J','0.5mm Open','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (629,'PP005M01J','0.5mm Open','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (630,'PP010L01J','0.5mm Open','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (631,'PP010M01J','0.5mm Open','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (632,'PP030L01J','0.5mm Open','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (633,'PP030M01J','0.5mm Open','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (634,'PP050L01J','0.5mm Open','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (635,'PP050M01J','0.5mm Open','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (636,'PP100L01J','0.5mm Open','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (637,'PP100M01J','0.5mm Open','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (638,'PP300L01J','0.5mm Open','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (639,'PP300M01J','0.5mm Open','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (640,'XP005L01J','0.5mm Open','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (641,'XP005M01J','0.5mm Open','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (642,'XP010L01J','0.5mm Open','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (643,'XP010M01J','0.5mm Open','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (644,'XP030L01J','0.5mm Open','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (645,'XP030M01J','0.5mm Open','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (646,'XP050L01J','0.5mm Open','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (647,'XP050M01J','0.5mm Open','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (648,'XP100L01J','0.5mm Open','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (649,'XP100M01J','0.5mm Open','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (650,'XP300L01J','0.5mm Open','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (651,'XP300M01J','0.5mm Open','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (652,'XPM10L01J','0.5mm Open','SIUS',1000,'HyStream','0.1 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (653,'XPM10M01J','0.5mm Open','SIUS',1000,'HyStream','0.1 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (654,'XPM20L01J','0.5mm Open','SIUS',1000,'HyStream','0.2 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (655,'XPM20M01J','0.5mm Open','SIUS',1000,'HyStream','0.2 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (656,'XPM45L01J','0.5mm Open','SIUS',1000,'HyStream','0.45 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (657,'XPM45M01J','0.5mm Open','SIUS',1000,'HyStream','0.45 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (658,'XPM65L01J','0.5mm Open','SIUS',1000,'HyStream','0.65 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (659,'XPM65M01J','0.5mm Open','SIUS',1000,'HyStream','0.65 �m',0.1,8.7,0.05,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (660,'PP001L01L','LP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (661,'PP001M01L','LP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (662,'PP003L01L','LP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (663,'PP003M01L','LP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (664,'PP005L01L','LP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (665,'PP005M01L','LP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (666,'PP010L01L','LP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (667,'PP010M01L','LP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (668,'PP030L01L','LP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (669,'PP030M01L','LP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (670,'PP050L01L','LP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (671,'PP050M01L','LP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (672,'PP100L01L','LP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (673,'PP100M01L','LP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (674,'PP300L01L','LP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (675,'PP300M01L','LP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (676,'PPH01M01L','LP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (677,'PPN50M01L','LP Screen','SIUS',1000,'ProStream','0.5 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (678,'XP005L01L','LP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (679,'XP005M01L','LP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (680,'XP010L01L','LP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (681,'XP010M01L','LP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (682,'XP030L01L','LP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (683,'XP030M01L','LP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (684,'XP050L01L','LP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (685,'XP050M01L','LP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (686,'XP100L01L','LP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (687,'XP100M01L','LP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (688,'XP300L01L','LP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (689,'XP300M01L','LP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (690,'XPM10L01L','LP Screen','SIUS',1000,'HyStream','0.1 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (691,'XPM10M01L','LP Screen','SIUS',1000,'HyStream','0.1 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (692,'XPM20L01L','LP Screen','SIUS',1000,'HyStream','0.2 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (693,'XPM20M01L','LP Screen','SIUS',1000,'HyStream','0.2 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (694,'XPM45L01L','LP Screen','SIUS',1000,'HyStream','0.45 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (695,'XPM45M01L','LP Screen','SIUS',1000,'HyStream','0.45 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (696,'XPM65L01L','LP Screen','SIUS',1000,'HyStream','0.65 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (697,'XPM65M01L','LP Screen','SIUS',1000,'HyStream','0.65 �m',0.1,8.7,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (698,'PP001G05J','0.5mm Open','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (699,'PP003G05J','0.5mm Open','SIUS',5000,'ProStream','3 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (700,'PP005G05J','0.5mm Open','SIUS',5000,'ProStream','5 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (701,'PP010G05J','0.5mm Open','SIUS',5000,'ProStream','10 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (702,'PP030G05J','0.5mm Open','SIUS',5000,'ProStream','30 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (703,'PP050G05J','0.5mm Open','SIUS',5000,'ProStream','50 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (704,'PP100G05J','0.5mm Open','SIUS',5000,'ProStream','100 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (705,'PP300G05J','0.5mm Open','SIUS',5000,'ProStream','300 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (706,'XP005G05J','0.5mm Open','SIUS',5000,'HyStream','5 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (707,'XP010G05J','0.5mm Open','SIUS',5000,'HyStream','10 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (708,'XP030G05J','0.5mm Open','SIUS',5000,'HyStream','30 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (709,'XP050G05J','0.5mm Open','SIUS',5000,'HyStream','50 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (710,'XP100G05J','0.5mm Open','SIUS',5000,'HyStream','100 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (711,'XP300G05J','0.5mm Open','SIUS',5000,'HyStream','300 kD',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (712,'XPM10G05J','0.5mm Open','SIUS',5000,'HyStream','0.1 �m',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (713,'XPM20G05J','0.5mm Open','SIUS',5000,'HyStream','0.2 �m',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (714,'XPM45G05J','0.5mm Open','SIUS',5000,'HyStream','0.45 �m',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (715,'XPM65G05J','0.5mm Open','SIUS',5000,'HyStream','0.65 �m',0.5,38.0,0.05,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (716,'PP001G05L','LP Screen','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (717,'PP003G05L','LP Screen','SIUS',5000,'ProStream','3 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (718,'PP005G05L','LP Screen','SIUS',5000,'ProStream','5 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (719,'PP010G05L','LP Screen','SIUS',5000,'ProStream','10 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (720,'PP030G05L','LP Screen','SIUS',5000,'ProStream','30 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (721,'PP050G05L','LP Screen','SIUS',5000,'ProStream','50 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (722,'PP100G05L','LP Screen','SIUS',5000,'ProStream','100 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (723,'PP300G05L','LP Screen','SIUS',5000,'ProStream','300 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (724,'PPH01G05L','LP Screen','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (725,'XP005G05L','LP Screen','SIUS',5000,'HyStream','5 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (726,'XP010G05L','LP Screen','SIUS',5000,'HyStream','10 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (727,'XP030G05L','LP Screen','SIUS',5000,'HyStream','30 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (728,'XP050G05L','LP Screen','SIUS',5000,'HyStream','50 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (729,'XP100G05L','LP Screen','SIUS',5000,'HyStream','100 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (730,'XP300G05L','LP Screen','SIUS',5000,'HyStream','300 kD',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (731,'XPM10G05L','LP Screen','SIUS',5000,'HyStream','0.1 �m',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (732,'XPM20G05L','LP Screen','SIUS',5000,'HyStream','0.2 �m',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (733,'XPM45G05L','LP Screen','SIUS',5000,'HyStream','0.45 �m',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (734,'XPM65G05L','LP Screen','SIUS',5000,'HyStream','0.65 �m',0.5,38.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (735,'PP001G15J','0.5mm Open','SIUS',15000,'ProStream','1 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (736,'PP003G15J','0.5mm Open','SIUS',15000,'ProStream','3 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (737,'PP005G15J','0.5mm Open','SIUS',15000,'ProStream','5 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (738,'PP010G15J','0.5mm Open','SIUS',15000,'ProStream','10 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (739,'PP030G15J','0.5mm Open','SIUS',15000,'ProStream','30 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (740,'PP050G15J','0.5mm Open','SIUS',15000,'ProStream','50 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (741,'PP100G15J','0.5mm Open','SIUS',15000,'ProStream','100 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (742,'PP300G15J','0.5mm Open','SIUS',15000,'ProStream','300 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (743,'XP005G15J','0.5mm Open','SIUS',15000,'HyStream','5 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (744,'XP010G15J','0.5mm Open','SIUS',15000,'HyStream','10 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (745,'XP030G15J','0.5mm Open','SIUS',15000,'HyStream','30 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (746,'XP050G15J','0.5mm Open','SIUS',15000,'HyStream','50 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (747,'XP100G15J','0.5mm Open','SIUS',15000,'HyStream','100 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (748,'XP300G15J','0.5mm Open','SIUS',15000,'HyStream','300 kD',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (749,'XPM10G15J','0.5mm Open','SIUS',15000,'HyStream','0.1 �m',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (750,'XPM20G15J','0.5mm Open','SIUS',15000,'HyStream','0.2 �m',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (751,'XPM45G15J','0.5mm Open','SIUS',15000,'HyStream','0.45 �m',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (752,'XPM65G15J','0.5mm Open','SIUS',15000,'HyStream','0.65 �m',1.5,114.0,0.05,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (753,'PP001G15L','LP Screen','SIUS',15000,'ProStream','1 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (754,'PP003G15L','LP Screen','SIUS',15000,'ProStream','3 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (755,'PP005G15L','LP Screen','SIUS',15000,'ProStream','5 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (756,'PP010G15L','LP Screen','SIUS',15000,'ProStream','10 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (757,'PP030G15L','LP Screen','SIUS',15000,'ProStream','30 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (758,'PP050G15L','LP Screen','SIUS',15000,'ProStream','50 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (759,'PP100G15L','LP Screen','SIUS',15000,'ProStream','100 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (760,'PP300G15L','LP Screen','SIUS',15000,'ProStream','300 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (761,'XP005G15L','LP Screen','SIUS',15000,'HyStream','5 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (762,'XP010G15L','LP Screen','SIUS',15000,'HyStream','10 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (763,'XP030G15L','LP Screen','SIUS',15000,'HyStream','30 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (764,'XP050G15L','LP Screen','SIUS',15000,'HyStream','50 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (765,'XP100G15L','LP Screen','SIUS',15000,'HyStream','100 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (766,'XP300G15L','LP Screen','SIUS',15000,'HyStream','300 kD',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (767,'XPM10G15L','LP Screen','SIUS',15000,'HyStream','0.1 �m',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (768,'XPM20G15L','LP Screen','SIUS',15000,'HyStream','0.2 �m',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (769,'XPM45G15L','LP Screen','SIUS',15000,'HyStream','0.45 �m',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (770,'XPM65G15L','LP Screen','SIUS',15000,'HyStream','0.65 �m',1.5,114.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (771,'PP001G25J','0.5mm Open','SIUS',25000,'ProStream','1 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (772,'PP003G25J','0.5mm Open','SIUS',25000,'ProStream','3 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (773,'PP005G25J','0.5mm Open','SIUS',25000,'ProStream','5 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (774,'PP010G25J','0.5mm Open','SIUS',25000,'ProStream','10 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (775,'PP030G25J','0.5mm Open','SIUS',25000,'ProStream','30 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (776,'PP050G25J','0.5mm Open','SIUS',25000,'ProStream','50 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (777,'PP100G25J','0.5mm Open','SIUS',25000,'ProStream','100 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (778,'PP300G25J','0.5mm Open','SIUS',25000,'ProStream','300 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (779,'XP005G25J','0.5mm Open','SIUS',25000,'HyStream','5 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (780,'XP010G25J','0.5mm Open','SIUS',25000,'HyStream','10 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (781,'XP030G25J','0.5mm Open','SIUS',25000,'HyStream','30 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (782,'XP050G25J','0.5mm Open','SIUS',25000,'HyStream','50 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (783,'XP100G25J','0.5mm Open','SIUS',25000,'HyStream','100 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (784,'XP300G25J','0.5mm Open','SIUS',25000,'HyStream','300 kD',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (785,'XPM10G25J','0.5mm Open','SIUS',25000,'HyStream','0.1 �m',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (786,'XPM20G25J','0.5mm Open','SIUS',25000,'HyStream','0.2 �m',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (787,'XPM45G25J','0.5mm Open','SIUS',25000,'HyStream','0.45 �m',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (788,'XPM65G25J','0.5mm Open','SIUS',25000,'HyStream','0.65 �m',2.5,190.0,0.05,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (789,'PP001G25L','LP Screen','SIUS',25000,'ProStream','1 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (790,'PP003G25L','LP Screen','SIUS',25000,'ProStream','3 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (791,'PP005G25L','LP Screen','SIUS',25000,'ProStream','5 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (792,'PP010G25L','LP Screen','SIUS',25000,'ProStream','10 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (793,'PP030G25L','LP Screen','SIUS',25000,'ProStream','30 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (794,'PP050G25L','LP Screen','SIUS',25000,'ProStream','50 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (795,'PP100G25L','LP Screen','SIUS',25000,'ProStream','100 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (796,'PP300G25L','LP Screen','SIUS',25000,'ProStream','300 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (797,'XP005G25L','LP Screen','SIUS',25000,'HyStream','5 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (798,'XP010G25L','LP Screen','SIUS',25000,'HyStream','10 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (799,'XP030G25L','LP Screen','SIUS',25000,'HyStream','30 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (800,'XP050G25L','LP Screen','SIUS',25000,'HyStream','50 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (801,'XP100G25L','LP Screen','SIUS',25000,'HyStream','100 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (802,'XP300G25L','LP Screen','SIUS',25000,'HyStream','300 kD',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (803,'XPM10G25L','LP Screen','SIUS',25000,'HyStream','0.1 �m',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (804,'XPM20G25L','LP Screen','SIUS',25000,'HyStream','0.2 �m',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (805,'XPM45G25L','LP Screen','SIUS',25000,'HyStream','0.45 �m',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (806,'XPM65G25L','LP Screen','SIUS',25000,'HyStream','0.65 �m',2.5,190.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (807,'PP001LP1E','EP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (808,'PP001MP1E','EP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (809,'PP003LP1E','EP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (810,'PP003MP1E','EP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (811,'PP005LP1E','EP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (812,'PP005MP1E','EP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (813,'PP010LP1E','EP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (814,'PP010MP1E','EP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (815,'PP030LP1E','EP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (816,'PP030MP1E','EP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (817,'PP050LP1E','EP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (818,'PP050MP1E','EP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (819,'PP100LP1E','EP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (820,'PP100MP1E','EP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (821,'PP300LP1E','EP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (822,'PP300MP1E','EP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (823,'XP005LP1E','EP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (824,'XP005MP1E','EP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (825,'XP010LP1E','EP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (826,'XP010MP1E','EP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (827,'XP030LP1E','EP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (828,'XP030MP1E','EP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (829,'XP050LP1E','EP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (830,'XP050MP1E','EP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (831,'XP100LP1E','EP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (832,'XP100MP1E','EP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (833,'XP300LP1E','EP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (834,'XP300MP1E','EP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (835,'XPM10LP1E','EP Screen','SIUS',100,'HyStream','0.1 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (836,'XPM10MP1E','EP Screen','SIUS',100,'HyStream','0.1 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (837,'XPM20LP1E','EP Screen','SIUS',100,'HyStream','0.2 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (838,'XPM20MP1E','EP Screen','SIUS',100,'HyStream','0.2 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (839,'XPM45LP1E','EP Screen','SIUS',100,'HyStream','0.45 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (840,'XPM45MP1E','EP Screen','SIUS',100,'HyStream','0.45 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (841,'XPM65LP1E','EP Screen','SIUS',100,'HyStream','0.65 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (842,'XPM65MP1E','EP Screen','SIUS',100,'HyStream','0.65 �m',0.01,1.2,0.025,3.6,1,0.01);
Insert or Replace INTO "cassette_lookup" VALUES (843,'PP001LP2E','EP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (844,'PP001MP2E','EP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (845,'PP003LP2E','EP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (846,'PP003MP2E','EP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (847,'PP005LP2E','EP Screen','SIUS',200,'ProStream','5 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (848,'PP005MP2E','EP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (849,'PP010LP2E','EP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (850,'PP010MP2E','EP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (851,'PP030LP2E','EP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (852,'PP030MP2E','EP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (853,'PP050LP2E','EP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (854,'PP050MP2E','EP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (855,'PP100LP2E','EP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (856,'PP100MP2E','EP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (857,'PP300LP2E','EP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (858,'PP300MP2E','EP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (859,'XP005LP2E','EP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (860,'XP005MP2E','EP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (861,'XP010LP2E','EP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (862,'XP010MP2E','EP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (863,'XP030LP2E','EP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (864,'XP030MP2E','EP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (865,'XP050LP2E','EP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (866,'XP050MP2E','EP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (867,'XP100LP2E','EP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (868,'XP100MP2E','EP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (869,'XP300LP2E','EP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (870,'XP300MP2E','EP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (871,'XPM10LP2E','EP Screen','SIUS',200,'HyStream','0.1 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (872,'XPM10MP2E','EP Screen','SIUS',200,'HyStream','0.1 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (873,'XPM20LP2E','EP Screen','SIUS',200,'HyStream','0.2 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (874,'XPM20MP2E','EP Screen','SIUS',200,'HyStream','0.2 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (875,'XPM45LP2E','EP Screen','SIUS',200,'HyStream','0.45 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (876,'XPM45MP2E','EP Screen','SIUS',200,'HyStream','0.45 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (877,'XPM65LP2E','EP Screen','SIUS',200,'HyStream','0.65 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (878,'XPM65MP2E','EP Screen','SIUS',200,'HyStream','0.65 �m',0.02,2.1,0.025,3.6,2,0.02);
Insert or Replace INTO "cassette_lookup" VALUES (879,'PP001L01E','EP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (880,'PP001M01E','EP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (881,'PP003L01E','EP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (882,'PP003M01E','EP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (883,'PP005L01E','EP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (884,'PP005M01E','EP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (885,'PP010L01E','EP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (886,'PP010M01E','EP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (887,'PP030L01E','EP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (888,'PP030M01E','EP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (889,'PP050L01E','EP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (890,'PP050M01E','EP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (891,'PP100L01E','EP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (892,'PP100M01E','EP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (893,'PP300L01E','EP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (894,'PP300M01E','EP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (895,'XP005L01E','EP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (896,'XP005M01E','EP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (897,'XP010L01E','EP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (898,'XP010M01E','EP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (899,'XP030L01E','EP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (900,'XP030M01E','EP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (901,'XP050L01E','EP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (902,'XP050M01E','EP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (903,'XP100L01E','EP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (904,'XP100M01E','EP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (905,'XP300L01E','EP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (906,'XP300M01E','EP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (907,'XPM10L01E','EP Screen','SIUS',1000,'HyStream','0.1 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (908,'XPM10M01E','EP Screen','SIUS',1000,'HyStream','0.1 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (909,'XPM20L01E','EP Screen','SIUS',1000,'HyStream','0.2 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (910,'XPM20M01E','EP Screen','SIUS',1000,'HyStream','0.2 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (911,'XPM45L01E','EP Screen','SIUS',1000,'HyStream','0.45 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (912,'XPM45M01E','EP Screen','SIUS',1000,'HyStream','0.45 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (913,'XPM65L01E','EP Screen','SIUS',1000,'HyStream','0.65 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (914,'XPM65M01E','EP Screen','SIUS',1000,'HyStream','0.65 �m',0.1,8.7,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (915,'PP001G05E','EP Screen','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (916,'PP003G05E','EP Screen','SIUS',5000,'ProStream','3 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (917,'PP005G05E','EP Screen','SIUS',5000,'ProStream','5 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (918,'PP010G05E','EP Screen','SIUS',5000,'ProStream','10 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (919,'PP030G05E','EP Screen','SIUS',5000,'ProStream','30 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (920,'PP050G05E','EP Screen','SIUS',5000,'ProStream','50 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (921,'PP100G05E','EP Screen','SIUS',5000,'ProStream','100 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (922,'PP300G05E','EP Screen','SIUS',5000,'ProStream','300 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (923,'XP005G05E','EP Screen','SIUS',5000,'HyStream','5 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (924,'XP010G05E','EP Screen','SIUS',5000,'HyStream','10 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (925,'XP030G05E','EP Screen','SIUS',5000,'HyStream','30 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (926,'XP050G05E','EP Screen','SIUS',5000,'HyStream','50 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (927,'XP100G05E','EP Screen','SIUS',5000,'HyStream','100 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (928,'XP300G05E','EP Screen','SIUS',5000,'HyStream','300 kD',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (929,'XPM10G05E','EP Screen','SIUS',5000,'HyStream','0.1 �m',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (930,'XPM20G05E','EP Screen','SIUS',5000,'HyStream','0.2 �m',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (931,'XPM45G05E','EP Screen','SIUS',5000,'HyStream','0.45 �m',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (932,'XPM65G05E','EP Screen','SIUS',5000,'HyStream','0.65 �m',0.5,38.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (933,'PP001G15E','EP Screen','SIUS',15000,'ProStream','1 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (934,'PP003G15E','EP Screen','SIUS',15000,'ProStream','3 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (935,'PP005G15E','EP Screen','SIUS',15000,'ProStream','5 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (936,'PP010G15E','EP Screen','SIUS',15000,'ProStream','10 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (937,'PP030G15E','EP Screen','SIUS',15000,'ProStream','30 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (938,'PP050G15E','EP Screen','SIUS',15000,'ProStream','50 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (939,'PP100G15E','EP Screen','SIUS',15000,'ProStream','100 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (940,'PP300G15E','EP Screen','SIUS',15000,'ProStream','300 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (941,'XP005G15E','EP Screen','SIUS',15000,'HyStream','5 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (942,'XP010G15E','EP Screen','SIUS',15000,'HyStream','10 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (943,'XP030G15E','EP Screen','SIUS',15000,'HyStream','30 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (944,'XP050G15E','EP Screen','SIUS',15000,'HyStream','50 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (945,'XP100G15E','EP Screen','SIUS',15000,'HyStream','100 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (946,'XP300G15E','EP Screen','SIUS',15000,'HyStream','300 kD',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (947,'XPM10G15E','EP Screen','SIUS',15000,'HyStream','0.1 �m',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (948,'XPM20G15E','EP Screen','SIUS',15000,'HyStream','0.2 �m',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (949,'XPM45G15E','EP Screen','SIUS',15000,'HyStream','0.45 �m',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (950,'XPM65G15E','EP Screen','SIUS',15000,'HyStream','0.65 �m',1.5,114.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (951,'PP001G25E','EP Screen','SIUS',25000,'ProStream','1 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (952,'PP003G25E','EP Screen','SIUS',25000,'ProStream','3 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (953,'PP005G25E','EP Screen','SIUS',25000,'ProStream','5 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (954,'PP010G25E','EP Screen','SIUS',25000,'ProStream','10 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (955,'PP030G25E','EP Screen','SIUS',25000,'ProStream','30 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (956,'PP050G25E','EP Screen','SIUS',25000,'ProStream','50 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (957,'PP100G25E','EP Screen','SIUS',25000,'ProStream','100 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (958,'PP300G25E','EP Screen','SIUS',25000,'ProStream','300 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (959,'XP005G25E','EP Screen','SIUS',25000,'HyStream','5 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (960,'XP010G25E','EP Screen','SIUS',25000,'HyStream','10 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (961,'XP030G25E','EP Screen','SIUS',25000,'HyStream','30 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (962,'XP050G25E','EP Screen','SIUS',25000,'HyStream','50 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (963,'XP100G25E','EP Screen','SIUS',25000,'HyStream','100 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (964,'XP300G25E','EP Screen','SIUS',25000,'HyStream','300 kD',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (965,'XPM10G25E','EP Screen','SIUS',25000,'HyStream','0.1 �m',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (966,'XPM20G25E','EP Screen','SIUS',25000,'HyStream','0.2 �m',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (967,'XPM45G25E','EP Screen','SIUS',25000,'HyStream','0.45 �m',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (968,'XPM65G25E','EP Screen','SIUS',25000,'HyStream','0.65 �m',2.5,190.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (969,'PP010C01L','LP Screen','SIUS Gamma',1000,'ProStream','10 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (970,'PP030C01L','LP Screen','SIUS Gamma',1000,'ProStream','30 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (971,'PP050C01L','LP Screen','SIUS Gamma',1000,'ProStream','50 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (972,'PP100C01L','LP Screen','SIUS Gamma',1000,'ProStream','100 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (973,'PP010F05L','LP Screen','SIUS Gamma',5000,'ProStream','10 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (974,'PP030F05L','LP Screen','SIUS Gamma',5000,'ProStream','30 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (975,'PP050F05L','LP Screen','SIUS Gamma',5000,'ProStream','50 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (976,'PP100F05L','LP Screen','SIUS Gamma',5000,'ProStream','100 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (977,'PP010F15L','LP Screen','SIUS Gamma',15000,'ProStream','10 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (978,'PP030F15L','LP Screen','SIUS Gamma',15000,'ProStream','30 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (979,'PP050F15L','LP Screen','SIUS Gamma',15000,'ProStream','50 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (980,'PP100F15L','LP Screen','SIUS Gamma',15000,'ProStream','100 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (981,'PP010F25L','LP Screen','SIUS Gamma',25000,'ProStream','10 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (982,'PP030F25L','LP Screen','SIUS Gamma',25000,'ProStream','30 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (983,'PP050F25L','LP Screen','SIUS Gamma',25000,'ProStream','50 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (984,'PP100F25L','LP Screen','SIUS Gamma',25000,'ProStream','100 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (985,'PP010C01E','EP Screen','SIUS Gamma',1000,'ProStream','10 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (986,'PP030C01E','EP Screen','SIUS Gamma',1000,'ProStream','30 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (987,'PP050C01E','EP Screen','SIUS Gamma',1000,'ProStream','50 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (988,'PP100C01E','EP Screen','SIUS Gamma',1000,'ProStream','100 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (989,'PP010F05E','EP Screen','SIUS Gamma',5000,'ProStream','10 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (990,'PP030F05E','EP Screen','SIUS Gamma',5000,'ProStream','30 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (991,'PP050F05E','EP Screen','SIUS Gamma',5000,'ProStream','50 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (992,'PP100F05E','EP Screen','SIUS Gamma',5000,'ProStream','100 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (993,'PP010F15E','EP Screen','SIUS Gamma',15000,'ProStream','10 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (994,'PP030F15E','EP Screen','SIUS Gamma',15000,'ProStream','30 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (995,'PP050F15E','EP Screen','SIUS Gamma',15000,'ProStream','50 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (996,'PP100F15E','EP Screen','SIUS Gamma',15000,'ProStream','100 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (997,'PP010F25E','EP Screen','SIUS Gamma',25000,'ProStream','10 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (998,'PP030F25E','EP Screen','SIUS Gamma',25000,'ProStream','30 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (999,'PP050F25E','EP Screen','SIUS Gamma',25000,'ProStream','50 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1000,'PP100F25E','EP Screen','SIUS Gamma',25000,'ProStream','100 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1001,'XP010C01L','LP Screen','SIUS Gamma',1000,'HyStream','10 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1002,'XP030C01L','LP Screen','SIUS Gamma',1000,'HyStream','30 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1003,'XP050C01L','LP Screen','SIUS Gamma',1000,'HyStream','50 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1004,'XP100C01L','LP Screen','SIUS Gamma',1000,'HyStream','100 kD',0.1,61.0,0.014,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1005,'XP010F05L','LP Screen','SIUS Gamma',5000,'HyStream','10 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1006,'XP030F05L','LP Screen','SIUS Gamma',5000,'HyStream','30 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1007,'XP050F05L','LP Screen','SIUS Gamma',5000,'HyStream','50 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1008,'XP100F05L','LP Screen','SIUS Gamma',5000,'HyStream','100 kD',0.5,509.0,0.014,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1009,'XP010F15L','LP Screen','SIUS Gamma',15000,'HyStream','10 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1010,'XP030F15L','LP Screen','SIUS Gamma',15000,'HyStream','30 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1011,'XP050F15L','LP Screen','SIUS Gamma',15000,'HyStream','50 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1012,'XP100F15L','LP Screen','SIUS Gamma',15000,'HyStream','100 kD',1.5,874.0,0.014,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1013,'XP010F25L','LP Screen','SIUS Gamma',25000,'HyStream','10 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1014,'XP030F25L','LP Screen','SIUS Gamma',25000,'HyStream','30 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1015,'XP050F25L','LP Screen','SIUS Gamma',25000,'HyStream','50 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1016,'XP100F25L','LP Screen','SIUS Gamma',25000,'HyStream','100 kD',2.5,1026.0,0.014,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1017,'XP010C01E','EP Screen','SIUS Gamma',1000,'HyStream','10 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1018,'XP030C01E','EP Screen','SIUS Gamma',1000,'HyStream','30 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1019,'XP050C01E','EP Screen','SIUS Gamma',1000,'HyStream','50 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1020,'XP100C01E','EP Screen','SIUS Gamma',1000,'HyStream','100 kD',0.1,61.0,0.025,3.6,10,0.1);
Insert or Replace INTO "cassette_lookup" VALUES (1021,'XP010F05E','EP Screen','SIUS Gamma',5000,'HyStream','10 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1022,'XP030F05E','EP Screen','SIUS Gamma',5000,'HyStream','30 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1023,'XP050F05E','EP Screen','SIUS Gamma',5000,'HyStream','50 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1024,'XP100F05E','EP Screen','SIUS Gamma',5000,'HyStream','100 kD',0.5,509.0,0.025,16.7,10,0.5);
Insert or Replace INTO "cassette_lookup" VALUES (1025,'XP010F15E','EP Screen','SIUS Gamma',15000,'HyStream','10 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1026,'XP030F15E','EP Screen','SIUS Gamma',15000,'HyStream','30 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1027,'XP050F15E','EP Screen','SIUS Gamma',15000,'HyStream','50 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1028,'XP100F15E','EP Screen','SIUS Gamma',15000,'HyStream','100 kD',1.5,874.0,0.025,16.7,30,1.5);
Insert or Replace INTO "cassette_lookup" VALUES (1029,'XP010F25E','EP Screen','SIUS Gamma',25000,'HyStream','10 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1030,'XP030F25E','EP Screen','SIUS Gamma',25000,'HyStream','30 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1031,'XP050F25E','EP Screen','SIUS Gamma',25000,'HyStream','50 kD',2.5,1026.0,0.025,16.7,50,2.5);
Insert or Replace INTO "cassette_lookup" VALUES (1032,'XP100F25E','EP Screen','SIUS Gamma',25000,'HyStream','100 kD',2.5,1026.0,0.025,16.7,50,2.5);

Insert or Ignore INTO "system_setting_details" VALUES (1,1,1,1,2,2,2,2,2,2,1,0,0.0,2.0,NULL,NULL,NULL,NULL,NULL,NULL,1575532566993,1575532566993,1,0,NULL,NULL,2.0,3.0,NULL,NULL);

Insert or Replace INTO "pump_tube_map" VALUES (1,1,1);
Insert or Replace INTO "pump_tube_map" VALUES (2,1,6);
Insert or Replace INTO "pump_tube_map" VALUES (3,1,5);
Insert or Replace INTO "pump_tube_map" VALUES (4,1,8);
Insert or Replace INTO "pump_tube_map" VALUES (5,2,1);
Insert or Replace INTO "pump_tube_map" VALUES (6,2,6);
Insert or Replace INTO "pump_tube_map" VALUES (7,2,5);
Insert or Replace INTO "pump_tube_map" VALUES (8,2,8);
Insert or Replace INTO "pump_tube_map" VALUES (9,3,1);
Insert or Replace INTO "pump_tube_map" VALUES (10,3,6);
Insert or Replace INTO "pump_tube_map" VALUES (11,3,8);
Insert or Replace INTO "pump_tube_map" VALUES (12,4,1);
Insert or Replace INTO "pump_tube_map" VALUES (13,4,6);
Insert or Replace INTO "pump_tube_map" VALUES (14,4,8);
Insert or Replace INTO "pump_tube_map" VALUES (15,5,1);
Insert or Replace INTO "pump_tube_map" VALUES (16,5,6);
Insert or Replace INTO "pump_tube_map" VALUES (17,5,5);
Insert or Replace INTO "pump_tube_map" VALUES (18,5,8);
Insert or Replace INTO "pump_tube_map" VALUES (19,6,1);
Insert or Replace INTO "pump_tube_map" VALUES (20,6,6);
Insert or Replace INTO "pump_tube_map" VALUES (21,6,8);
Insert or Replace INTO "pump_tube_map" VALUES (22,7,1);
Insert or Replace INTO "pump_tube_map" VALUES (23,7,6);
Insert or Replace INTO "pump_tube_map" VALUES (24,7,8);
Insert or Replace INTO "pump_tube_map" VALUES (25,8,1);
Insert or Replace INTO "pump_tube_map" VALUES (26,8,6);
Insert or Replace INTO "pump_tube_map" VALUES (27,8,8);
Insert or Replace INTO "pump_tube_map" VALUES (28,9,1);
Insert or Replace INTO "pump_tube_map" VALUES (29,9,6);
Insert or Replace INTO "pump_tube_map" VALUES (30,9,8);
Insert or Replace INTO "pump_tube_map" VALUES (31,10,1);
Insert or Replace INTO "pump_tube_map" VALUES (32,10,6);
Insert or Replace INTO "pump_tube_map" VALUES (33,10,5);
Insert or Replace INTO "pump_tube_map" VALUES (34,10,8);
Insert or Replace INTO "pump_tube_map" VALUES (35,11,1);
Insert or Replace INTO "pump_tube_map" VALUES (36,11,6);
Insert or Replace INTO "pump_tube_map" VALUES (37,11,8);
Insert or Replace INTO "pump_tube_map" VALUES (38,12,1);
Insert or Replace INTO "pump_tube_map" VALUES (39,12,6);
Insert or Replace INTO "pump_tube_map" VALUES (40,12,8);
Insert or Replace INTO "pump_tube_map" VALUES (41,13,1);
Insert or Replace INTO "pump_tube_map" VALUES (42,13,6);
Insert or Replace INTO "pump_tube_map" VALUES (43,13,8);
Insert or Replace INTO "pump_tube_map" VALUES (44,14,1);
Insert or Replace INTO "pump_tube_map" VALUES (45,14,6);
Insert or Replace INTO "pump_tube_map" VALUES (46,15,2);
Insert or Replace INTO "pump_tube_map" VALUES (47,15,7);
Insert or Replace INTO "pump_tube_map" VALUES (48,15,9);
Insert or Replace INTO "pump_tube_map" VALUES (49,16,2);
Insert or Replace INTO "pump_tube_map" VALUES (50,16,7);
Insert or Replace INTO "pump_tube_map" VALUES (51,16,9);
Insert or Replace INTO "pump_tube_map" VALUES (52,17,2);
Insert or Replace INTO "pump_tube_map" VALUES (53,17,7);
Insert or Replace INTO "pump_tube_map" VALUES (54,17,9);
Insert or Replace INTO "pump_tube_map" VALUES (55,18,2);
Insert or Replace INTO "pump_tube_map" VALUES (56,18,7);
Insert or Replace INTO "pump_tube_map" VALUES (57,18,9);
Insert or Replace INTO "pump_tube_map" VALUES (58,19,2);
Insert or Replace INTO "pump_tube_map" VALUES (59,19,7);
Insert or Replace INTO "pump_tube_map" VALUES (60,20,2);
Insert or Replace INTO "pump_tube_map" VALUES (61,20,7);
Insert or Replace INTO "pump_tube_map" VALUES (62,21,9);
Insert or Replace INTO "pump_tube_map" VALUES (63,22,9);
Insert or Replace INTO "pump_tube_map" VALUES (64,23,4);
Insert or Replace INTO "pump_tube_map" VALUES (65,24,3);
Insert or Replace INTO "pump_mode_map" VALUES (1,1,1);
Insert or Replace INTO "pump_mode_map" VALUES (2,1,2);
Insert or Replace INTO "pump_mode_map" VALUES (3,1,3);
Insert or Replace INTO "pump_mode_map" VALUES (4,1,4);
Insert or Replace INTO "pump_mode_map" VALUES (5,1,5);
Insert or Replace INTO "pump_mode_map" VALUES (6,1,6);
Insert or Replace INTO "pump_mode_map" VALUES (7,1,7);
Insert or Replace INTO "pump_mode_map" VALUES (8,1,8);
Insert or Replace INTO "pump_mode_map" VALUES (9,1,9);
Insert or Replace INTO "pump_mode_map" VALUES (10,2,1);
Insert or Replace INTO "pump_mode_map" VALUES (11,2,2);
Insert or Replace INTO "pump_mode_map" VALUES (12,2,3);
Insert or Replace INTO "pump_mode_map" VALUES (13,2,4);
Insert or Replace INTO "pump_mode_map" VALUES (14,2,5);
Insert or Replace INTO "pump_mode_map" VALUES (15,2,6);
Insert or Replace INTO "pump_mode_map" VALUES (16,2,7);
Insert or Replace INTO "pump_mode_map" VALUES (17,2,8);
Insert or Replace INTO "pump_mode_map" VALUES (18,2,9);
Insert or Replace INTO "pump_mode_map" VALUES (19,3,1);
Insert or Replace INTO "pump_mode_map" VALUES (20,3,2);
Insert or Replace INTO "pump_mode_map" VALUES (21,3,3);
Insert or Replace INTO "pump_mode_map" VALUES (22,3,4);
Insert or Replace INTO "pump_mode_map" VALUES (23,3,5);
Insert or Replace INTO "pump_mode_map" VALUES (24,3,6);
Insert or Replace INTO "pump_mode_map" VALUES (25,3,7);
Insert or Replace INTO "pump_mode_map" VALUES (26,3,8);
Insert or Replace INTO "pump_mode_map" VALUES (27,4,1);
Insert or Replace INTO "pump_mode_map" VALUES (28,4,2);
Insert or Replace INTO "pump_mode_map" VALUES (29,4,3);
Insert or Replace INTO "pump_mode_map" VALUES (30,4,4);
Insert or Replace INTO "pump_mode_map" VALUES (31,4,5);
Insert or Replace INTO "pump_mode_map" VALUES (32,4,6);
Insert or Replace INTO "pump_mode_map" VALUES (33,4,7);
Insert or Replace INTO "pump_mode_map" VALUES (34,4,8);
Insert or Replace INTO "pump_mode_map" VALUES (35,1,10);
Insert or Replace INTO "pump_mode_map" VALUES (36,1,11);
Insert or Replace INTO "pump_mode_map" VALUES (37,1,12);
Insert or Replace INTO "pump_mode_map" VALUES (38,1,13);
Insert or Replace INTO "pump_mode_map" VALUES (39,1,14);
Insert or Replace INTO "pump_mode_map" VALUES (40,1,15);
Insert or Replace INTO "pump_mode_map" VALUES (41,2,10);
Insert or Replace INTO "pump_mode_map" VALUES (42,2,11);
Insert or Replace INTO "pump_mode_map" VALUES (43,2,12);
Insert or Replace INTO "pump_mode_map" VALUES (44,2,13);
Insert or Replace INTO "pump_mode_map" VALUES (45,2,14);
Insert or Replace INTO "pump_mode_map" VALUES (46,2,15);
Insert or Replace INTO "pump_mode_map" VALUES (47,3,10);
Insert or Replace INTO "pump_mode_map" VALUES (48,3,11);
Insert or Replace INTO "pump_mode_map" VALUES (49,3,12);
Insert or Replace INTO "pump_mode_map" VALUES (50,3,13);
Insert or Replace INTO "pump_mode_map" VALUES (51,3,14);
Insert or Replace INTO "pump_mode_map" VALUES (52,3,15);
Insert or Replace INTO "pump_mode_map" VALUES (53,4,10);
Insert or Replace INTO "pump_mode_map" VALUES (54,4,11);
Insert or Replace INTO "pump_mode_map" VALUES (55,4,12);
Insert or Replace INTO "pump_mode_map" VALUES (56,4,13);
Insert or Replace INTO "pump_mode_map" VALUES (57,4,14);
Insert or Replace INTO "pump_mode_map" VALUES (58,4,15);