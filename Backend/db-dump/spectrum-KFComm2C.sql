BEGIN TRANSACTION;
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
	"feed_scale"	REAL,
	"m_permeate"	REAL,
	"total_perm_weight"	REAL,
	"diafiltration_vol_1"	REAL,
	"diafiltration_vol_2"	REAL,
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
	"nwp"	TEXT,
	"konduit_ch_1"	TEXT,
	"konduit_ch_2"	TEXT,
	"konduit_ch_1_type"	INTEGER,
	"konduit_ch_2_type"	INTEGER,
	"is_paused"	INTEGER DEFAULT 0
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
	"is_active"	INTEGER,
	"is_aux_konduit_2"	INTEGER,
	"ph_min_2"	REAL,
	"ph_max_2"	REAL,
	"turbidity_min_2"	REAL,
	"turbidity_max_2"	REAL,
	"flow_rate_min_2"	REAL,
	"flow_rate_max_2"	REAL
);
CREATE TABLE "target_step_change_log" (
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
CREATE TABLE "end_point_change_log" (
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
	"pass_is_active"	TEXT,
	"master_password" TEXT
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
INSERT INTO "db_version" VALUES (1,1);
INSERT INTO "cassette_lookup" VALUES ('PP001AP1H','HP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001WP1H','HP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003AP1H','HP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003WP1H','HP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005AP1H','HP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005WP1H','HP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010AP1H','HP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010WP1H','HP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030AP1H','HP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030WP1H','HP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050AP1H','HP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050WP1H','HP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100AP1H','HP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100WP1H','HP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300AP1H','HP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300WP1H','HP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005AP1H','HP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005WP1H','HP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010AP1H','HP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010WP1H','HP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030AP1H','HP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030WP1H','HP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050AP1H','HP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050WP1H','HP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100AP1H','HP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100WP1H','HP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300AP1H','HP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300WP1H','HP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10AP1H','HP Screen','Pro',100,'HyStream','0.1 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10WP1H','HP Screen','Pro',100,'HyStream','0.1 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20AP1H','HP Screen','Pro',100,'HyStream','0.2 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20WP1H','HP Screen','Pro',100,'HyStream','0.2 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45AP1H','HP Screen','Pro',100,'HyStream','0.45 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45WP1H','HP Screen','Pro',100,'HyStream','0.45 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65AP1H','HP Screen','Pro',100,'HyStream','0.65 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65WP1H','HP Screen','Pro',100,'HyStream','0.65 μm',0.01,1.2,0.018,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001AP1L','LP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001WP1L','LP Screen','Pro',100,'ProStream','1 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003AP1L','LP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003WP1L','LP Screen','Pro',100,'ProStream','3 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005AP1L','LP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005WP1L','LP Screen','Pro',100,'ProStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010AP1L','LP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010WP1L','LP Screen','Pro',100,'ProStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030AP1L','LP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030WP1L','LP Screen','Pro',100,'ProStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050AP1L','LP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050WP1L','LP Screen','Pro',100,'ProStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100AP1L','LP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100WP1L','LP Screen','Pro',100,'ProStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300AP1L','LP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300WP1L','LP Screen','Pro',100,'ProStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005AP1L','LP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005WP1L','LP Screen','Pro',100,'HyStream','5 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010AP1L','LP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010WP1L','LP Screen','Pro',100,'HyStream','10 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030AP1L','LP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030WP1L','LP Screen','Pro',100,'HyStream','30 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050AP1L','LP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050WP1L','LP Screen','Pro',100,'HyStream','50 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100AP1L','LP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100WP1L','LP Screen','Pro',100,'HyStream','100 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300AP1L','LP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300WP1L','LP Screen','Pro',100,'HyStream','300 kD',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10AP1L','LP Screen','Pro',100,'HyStream','0.1 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10WP1L','LP Screen','Pro',100,'HyStream','0.1 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20AP1L','LP Screen','Pro',100,'HyStream','0.2 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20WP1L','LP Screen','Pro',100,'HyStream','0.2 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45AP1L','LP Screen','Pro',100,'HyStream','0.45 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45WP1L','LP Screen','Pro',100,'HyStream','0.45 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65AP1L','LP Screen','Pro',100,'HyStream','0.65 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65WP1L','LP Screen','Pro',100,'HyStream','0.65 μm',0.01,1.2,0.021,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001AP1S','S Channel','Pro',100,'ProStream','1 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001WP1S','S Channel','Pro',100,'ProStream','1 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003AP1S','S Channel','Pro',100,'ProStream','3 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003WP1S','S Channel','Pro',100,'ProStream','3 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005AP1S','S Channel','Pro',100,'ProStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005WP1S','S Channel','Pro',100,'ProStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010AP1S','S Channel','Pro',100,'ProStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010WP1S','S Channel','Pro',100,'ProStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030AP1S','S Channel','Pro',100,'ProStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030WP1S','S Channel','Pro',100,'ProStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050AP1S','S Channel','Pro',100,'ProStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050WP1S','S Channel','Pro',100,'ProStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100AP1S','S Channel','Pro',100,'ProStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100WP1S','S Channel','Pro',100,'ProStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300AP1S','S Channel','Pro',100,'ProStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300WP1S','S Channel','Pro',100,'ProStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005AP1S','S Channel','Pro',100,'HyStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005WP1S','S Channel','Pro',100,'HyStream','5 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010AP1S','S Channel','Pro',100,'HyStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010WP1S','S Channel','Pro',100,'HyStream','10 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030AP1S','S Channel','Pro',100,'HyStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030WP1S','S Channel','Pro',100,'HyStream','30 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050AP1S','S Channel','Pro',100,'HyStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050WP1S','S Channel','Pro',100,'HyStream','50 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100AP1S','S Channel','Pro',100,'HyStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100WP1S','S Channel','Pro',100,'HyStream','100 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300AP1S','S Channel','Pro',100,'HyStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300WP1S','S Channel','Pro',100,'HyStream','300 kD',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10AP1S','S Channel','Pro',100,'HyStream','0.1 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10WP1S','S Channel','Pro',100,'HyStream','0.1 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20AP1S','S Channel','Pro',100,'HyStream','0.2 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20WP1S','S Channel','Pro',100,'HyStream','0.2 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45AP1S','S Channel','Pro',100,'HyStream','0.45 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45WP1S','S Channel','Pro',100,'HyStream','0.45 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65AP1S','S Channel','Pro',100,'HyStream','0.65 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65WP1S','S Channel','Pro',100,'HyStream','0.65 μm',0.01,1.2,0.017,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001AP2H','HP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001WP2H','HP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003AP2H','HP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003WP2H','HP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005AP2H','HP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005WP2H','HP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010AP2H','HP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010WP2H','HP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030AP2H','HP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030WP2H','HP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050AP2H','HP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050WP2H','HP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100AP2H','HP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100WP2H','HP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300AP2H','HP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300WP2H','HP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005AP2H','HP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005WP2H','HP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010AP2H','HP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010WP2H','HP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030AP2H','HP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030WP2H','HP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050AP2H','HP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050WP2H','HP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100AP2H','HP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100WP2H','HP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300AP2H','HP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300WP2H','HP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10AP2H','HP Screen','Pro',200,'HyStream','0.1 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10WP2H','HP Screen','Pro',200,'HyStream','0.1 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20AP2H','HP Screen','Pro',200,'HyStream','0.2 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20WP2H','HP Screen','Pro',200,'HyStream','0.2 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45AP2H','HP Screen','Pro',200,'HyStream','0.45 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45WP2H','HP Screen','Pro',200,'HyStream','0.45 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65AP2H','HP Screen','Pro',200,'HyStream','0.65 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65WP2H','HP Screen','Pro',200,'HyStream','0.65 μm',0.02,2.1,0.018,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001AP2L','LP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001WP2L','LP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003AP2L','LP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003WP2L','LP Screen','Pro',200,'ProStream','3 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005AP2L','LP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005WP2L','LP Screen','Pro',200,'ProStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010AP2L','LP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010WP2L','LP Screen','Pro',200,'ProStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP020AP2L','LP Screen','Pro',200,'ProStream','20 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030AP2L','LP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030WP2L','LP Screen','Pro',200,'ProStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050AP2L','LP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050WP2L','LP Screen','Pro',200,'ProStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100AP2L','LP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100WP2L','LP Screen','Pro',200,'ProStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300AP2L','LP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300WP2L','LP Screen','Pro',200,'ProStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PPH01AP2L','LP Screen','Pro',200,'ProStream','1 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PPN50AP2L','LP Screen','Pro',200,'ProStream','0.5 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PPN65WP2L','LP Screen','Pro',200,'ProStream','0.65 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005AP2L','LP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005WP2L','LP Screen','Pro',200,'HyStream','5 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010AP2L','LP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010WP2L','LP Screen','Pro',200,'HyStream','10 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030AP2L','LP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030WP2L','LP Screen','Pro',200,'HyStream','30 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050AP2L','LP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050WP2L','LP Screen','Pro',200,'HyStream','50 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100AP2L','LP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100WP2L','LP Screen','Pro',200,'HyStream','100 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300AP2L','LP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300WP2L','LP Screen','Pro',200,'HyStream','300 kD',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10AP2L','LP Screen','Pro',200,'HyStream','0.1 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10WP2L','LP Screen','Pro',200,'HyStream','0.1 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20AP2L','LP Screen','Pro',200,'HyStream','0.2 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20WP2L','LP Screen','Pro',200,'HyStream','0.2 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45AP2L','LP Screen','Pro',200,'HyStream','0.45 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45WP2L','LP Screen','Pro',200,'HyStream','0.45 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65AP2L','LP Screen','Pro',200,'HyStream','0.65 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65WP2L','LP Screen','Pro',200,'HyStream','0.65 μm',0.02,2.1,0.021,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001AP2S','S Channel','Pro',200,'ProStream','1 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001WP2S','S Channel','Pro',200,'ProStream','1 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003AP2S','S Channel','Pro',200,'ProStream','3 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003WP2S','S Channel','Pro',200,'ProStream','3 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005AP2S','S Channel','Pro',200,'ProStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005WP2S','S Channel','Pro',200,'ProStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010AP2S','S Channel','Pro',200,'ProStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010WP2S','S Channel','Pro',200,'ProStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030AP2S','S Channel','Pro',200,'ProStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030WP2S','S Channel','Pro',200,'ProStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050AP2S','S Channel','Pro',200,'ProStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050WP2S','S Channel','Pro',200,'ProStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100AP2S','S Channel','Pro',200,'ProStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100WP2S','S Channel','Pro',200,'ProStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300AP2S','S Channel','Pro',200,'ProStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300WP2S','S Channel','Pro',200,'ProStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PPN50AP2S','S Channel','Pro',200,'ProStream','0.5 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005AP2S','S Channel','Pro',200,'HyStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005WP2S','S Channel','Pro',200,'HyStream','5 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010AP2S','S Channel','Pro',200,'HyStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010WP2S','S Channel','Pro',200,'HyStream','10 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030AP2S','S Channel','Pro',200,'HyStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030WP2S','S Channel','Pro',200,'HyStream','30 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050AP2S','S Channel','Pro',200,'HyStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050WP2S','S Channel','Pro',200,'HyStream','50 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100AP2S','S Channel','Pro',200,'HyStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100WP2S','S Channel','Pro',200,'HyStream','100 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300AP2S','S Channel','Pro',200,'HyStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300WP2S','S Channel','Pro',200,'HyStream','300 kD',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10AP2S','S Channel','Pro',200,'HyStream','0.1 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10WP2S','S Channel','Pro',200,'HyStream','0.1 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20AP2S','S Channel','Pro',200,'HyStream','0.2 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20WP2S','S Channel','Pro',200,'HyStream','0.2 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45AP2S','S Channel','Pro',200,'HyStream','0.45 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45WP2S','S Channel','Pro',200,'HyStream','0.45 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65AP2S','S Channel','Pro',200,'HyStream','0.65 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65WP2S','S Channel','Pro',200,'HyStream','0.65 μm',0.02,2.1,0.017,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001W01H','HP Screen','Pro',1000,'ProStream','1 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003A01H','HP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003W01H','HP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005A01H','HP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005W01H','HP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010A01H','HP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010W01H','HP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030A01H','HP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030W01H','HP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050A01H','HP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050W01H','HP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100A01H','HP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100W01H','HP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300A01H','HP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300W01H','HP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005A01H','HP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005W01H','HP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010A01H','HP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010W01H','HP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030A01H','HP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030W01H','HP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050A01H','HP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050W01H','HP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100A01H','HP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100W01H','HP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300A01H','HP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300W01H','HP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10A01H','HP Screen','Pro',1000,'HyStream','0.1 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10W01H','HP Screen','Pro',1000,'HyStream','0.1 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20A01H','HP Screen','Pro',1000,'HyStream','0.2 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20W01H','HP Screen','Pro',1000,'HyStream','0.2 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45A01H','HP Screen','Pro',1000,'HyStream','0.45 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45W01H','HP Screen','Pro',1000,'HyStream','0.45 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65A01H','HP Screen','Pro',1000,'HyStream','0.65 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65W01H','HP Screen','Pro',1000,'HyStream','0.65 μm',0.1,8.7,0.018,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001A01L','LP Screen','Pro',1000,'ProStream','1 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001W01L','LP Screen','Pro',1000,'ProStream','1 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003A01L','LP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003W01L','LP Screen','Pro',1000,'ProStream','3 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005A01L','LP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005W01L','LP Screen','Pro',1000,'ProStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010A01L','LP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010W01L','LP Screen','Pro',1000,'ProStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030A01L','LP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030W01L','LP Screen','Pro',1000,'ProStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050A01L','LP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050W01L','LP Screen','Pro',1000,'ProStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100A01L','LP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100W01L','LP Screen','Pro',1000,'ProStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300A01L','LP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300W01L','LP Screen','Pro',1000,'ProStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PPN50A01L','LP Screen','Pro',1000,'ProStream','0.5 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PPN65W01L','LP Screen','Pro',1000,'ProStream','0.65 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005A01L','LP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005W01L','LP Screen','Pro',1000,'HyStream','5 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010A01L','LP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010W01L','LP Screen','Pro',1000,'HyStream','10 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030A01L','LP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030W01L','LP Screen','Pro',1000,'HyStream','30 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050A01L','LP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050W01L','LP Screen','Pro',1000,'HyStream','50 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100A01L','LP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100W01L','LP Screen','Pro',1000,'HyStream','100 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300A01L','LP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300W01L','LP Screen','Pro',1000,'HyStream','300 kD',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10A01L','LP Screen','Pro',1000,'HyStream','0.1 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10W01L','LP Screen','Pro',1000,'HyStream','0.1 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20A01L','LP Screen','Pro',1000,'HyStream','0.2 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20W01L','LP Screen','Pro',1000,'HyStream','0.2 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45A01L','LP Screen','Pro',1000,'HyStream','0.45 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45W01L','LP Screen','Pro',1000,'HyStream','0.45 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65A01L','LP Screen','Pro',1000,'HyStream','0.65 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65W01L','LP Screen','Pro',1000,'HyStream','0.65 μm',0.1,8.7,0.021,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001W01S','S Channel','Pro',1000,'ProStream','1 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003A01S','S Channel','Pro',1000,'ProStream','3 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003W01S','S Channel','Pro',1000,'ProStream','3 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005A01S','S Channel','Pro',1000,'ProStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005W01S','S Channel','Pro',1000,'ProStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010A01S','S Channel','Pro',1000,'ProStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010W01S','S Channel','Pro',1000,'ProStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030A01S','S Channel','Pro',1000,'ProStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030W01S','S Channel','Pro',1000,'ProStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050A01S','S Channel','Pro',1000,'ProStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050W01S','S Channel','Pro',1000,'ProStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100A01S','S Channel','Pro',1000,'ProStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100W01S','S Channel','Pro',1000,'ProStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300A01S','S Channel','Pro',1000,'ProStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300W01S','S Channel','Pro',1000,'ProStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005A01S','S Channel','Pro',1000,'HyStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005W01S','S Channel','Pro',1000,'HyStream','5 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010A01S','S Channel','Pro',1000,'HyStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010W01S','S Channel','Pro',1000,'HyStream','10 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030A01S','S Channel','Pro',1000,'HyStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030W01S','S Channel','Pro',1000,'HyStream','30 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050A01S','S Channel','Pro',1000,'HyStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050W01S','S Channel','Pro',1000,'HyStream','50 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100A01S','S Channel','Pro',1000,'HyStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100W01S','S Channel','Pro',1000,'HyStream','100 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300A01S','S Channel','Pro',1000,'HyStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300W01S','S Channel','Pro',1000,'HyStream','300 kD',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10A01S','S Channel','Pro',1000,'HyStream','0.1 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10W01S','S Channel','Pro',1000,'HyStream','0.1 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20A01S','S Channel','Pro',1000,'HyStream','0.2 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20W01S','S Channel','Pro',1000,'HyStream','0.2 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45A01S','S Channel','Pro',1000,'HyStream','0.45 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45W01S','S Channel','Pro',1000,'HyStream','0.45 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65A01S','S Channel','Pro',1000,'HyStream','0.65 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65W01S','S Channel','Pro',1000,'HyStream','0.65 μm',0.1,8.7,0.017,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001B05H','HP Screen','Pro',5000,'ProStream','1 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B05H','HP Screen','Pro',5000,'ProStream','3 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B05H','HP Screen','Pro',5000,'ProStream','5 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B05H','HP Screen','Pro',5000,'ProStream','10 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B05H','HP Screen','Pro',5000,'ProStream','30 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B05H','HP Screen','Pro',5000,'ProStream','50 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B05H','HP Screen','Pro',5000,'ProStream','100 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B05H','HP Screen','Pro',5000,'ProStream','300 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B05H','HP Screen','Pro',5000,'HyStream','5 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B05H','HP Screen','Pro',5000,'HyStream','10 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B05H','HP Screen','Pro',5000,'HyStream','30 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B05H','HP Screen','Pro',5000,'HyStream','50 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B05H','HP Screen','Pro',5000,'HyStream','100 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B05H','HP Screen','Pro',5000,'HyStream','300 kD',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B05H','HP Screen','Pro',5000,'HyStream','0.1 μm',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B05H','HP Screen','Pro',5000,'HyStream','0.2 μm',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B05H','HP Screen','Pro',5000,'HyStream','0.45 μm',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B05H','HP Screen','Pro',5000,'HyStream','0.65 μm',0.5,38.0,0.018,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B05L','LP Screen','Pro',5000,'ProStream','1 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B05L','LP Screen','Pro',5000,'ProStream','3 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B05L','LP Screen','Pro',5000,'ProStream','5 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B05L','LP Screen','Pro',5000,'ProStream','10 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B05L','LP Screen','Pro',5000,'ProStream','30 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B05L','LP Screen','Pro',5000,'ProStream','50 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B05L','LP Screen','Pro',5000,'ProStream','100 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B05L','LP Screen','Pro',5000,'ProStream','300 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PPN65B05L','LP Screen','Pro',5000,'ProStream','0.65 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B05L','LP Screen','Pro',5000,'HyStream','5 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B05L','LP Screen','Pro',5000,'HyStream','10 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B05L','LP Screen','Pro',5000,'HyStream','30 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B05L','LP Screen','Pro',5000,'HyStream','50 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B05L','LP Screen','Pro',5000,'HyStream','100 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B05L','LP Screen','Pro',5000,'HyStream','300 kD',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B05L','LP Screen','Pro',5000,'HyStream','0.1 μm',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B05L','LP Screen','Pro',5000,'HyStream','0.2 μm',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B05L','LP Screen','Pro',5000,'HyStream','0.45 μm',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B05L','LP Screen','Pro',5000,'HyStream','0.65 μm',0.5,38.0,0.021,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B05S','S Channel','Pro',5000,'ProStream','1 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B05S','S Channel','Pro',5000,'ProStream','3 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B05S','S Channel','Pro',5000,'ProStream','5 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B05S','S Channel','Pro',5000,'ProStream','10 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B05S','S Channel','Pro',5000,'ProStream','30 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B05S','S Channel','Pro',5000,'ProStream','50 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B05S','S Channel','Pro',5000,'ProStream','100 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B05S','S Channel','Pro',5000,'ProStream','300 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B05S','S Channel','Pro',5000,'HyStream','5 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B05S','S Channel','Pro',5000,'HyStream','10 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B05S','S Channel','Pro',5000,'HyStream','30 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B05S','S Channel','Pro',5000,'HyStream','50 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B05S','S Channel','Pro',5000,'HyStream','100 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B05S','S Channel','Pro',5000,'HyStream','300 kD',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B05S','S Channel','Pro',5000,'HyStream','0.1 μm',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B05S','S Channel','Pro',5000,'HyStream','0.2 μm',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B05S','S Channel','Pro',5000,'HyStream','0.45 μm',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B05S','S Channel','Pro',5000,'HyStream','0.65 μm',0.5,38.0,0.017,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B15H','HP Screen','Pro',15000,'ProStream','1 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B15H','HP Screen','Pro',15000,'ProStream','3 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B15H','HP Screen','Pro',15000,'ProStream','5 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B15H','HP Screen','Pro',15000,'ProStream','10 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B15H','HP Screen','Pro',15000,'ProStream','30 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B15H','HP Screen','Pro',15000,'ProStream','50 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B15H','HP Screen','Pro',15000,'ProStream','100 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B15H','HP Screen','Pro',15000,'ProStream','300 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B15H','HP Screen','Pro',15000,'HyStream','5 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B15H','HP Screen','Pro',15000,'HyStream','10 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B15H','HP Screen','Pro',15000,'HyStream','30 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B15H','HP Screen','Pro',15000,'HyStream','50 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B15H','HP Screen','Pro',15000,'HyStream','100 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B15H','HP Screen','Pro',15000,'HyStream','300 kD',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B15H','HP Screen','Pro',15000,'HyStream','0.1 μm',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B15H','HP Screen','Pro',15000,'HyStream','0.2 μm',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B15H','HP Screen','Pro',15000,'HyStream','0.45 μm',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B15H','HP Screen','Pro',15000,'HyStream','0.65 μm',1.5,114.0,0.018,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B15L','LP Screen','Pro',15000,'ProStream','1 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B15L','LP Screen','Pro',15000,'ProStream','3 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B15L','LP Screen','Pro',15000,'ProStream','5 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B15L','LP Screen','Pro',15000,'ProStream','10 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B15L','LP Screen','Pro',15000,'ProStream','30 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B15L','LP Screen','Pro',15000,'ProStream','50 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B15L','LP Screen','Pro',15000,'ProStream','100 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B15L','LP Screen','Pro',15000,'ProStream','300 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PPN65B15L','LP Screen','Pro',15000,'ProStream','0.65 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B15L','LP Screen','Pro',15000,'HyStream','5 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B15L','LP Screen','Pro',15000,'HyStream','10 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B15L','LP Screen','Pro',15000,'HyStream','30 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B15L','LP Screen','Pro',15000,'HyStream','50 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B15L','LP Screen','Pro',15000,'HyStream','100 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B15L','LP Screen','Pro',15000,'HyStream','300 kD',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B15L','LP Screen','Pro',15000,'HyStream','0.1 μm',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B15L','LP Screen','Pro',15000,'HyStream','0.2 μm',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B15L','LP Screen','Pro',15000,'HyStream','0.45 μm',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B15L','LP Screen','Pro',15000,'HyStream','0.65 μm',1.5,114.0,0.021,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B15S','S Channel','Pro',15000,'ProStream','1 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B15S','S Channel','Pro',15000,'ProStream','3 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B15S','S Channel','Pro',15000,'ProStream','5 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B15S','S Channel','Pro',15000,'ProStream','10 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B15S','S Channel','Pro',15000,'ProStream','30 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B15S','S Channel','Pro',15000,'ProStream','50 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B15S','S Channel','Pro',15000,'ProStream','100 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B15S','S Channel','Pro',15000,'ProStream','300 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B15S','S Channel','Pro',15000,'HyStream','5 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B15S','S Channel','Pro',15000,'HyStream','10 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B15S','S Channel','Pro',15000,'HyStream','30 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B15S','S Channel','Pro',15000,'HyStream','50 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B15S','S Channel','Pro',15000,'HyStream','100 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B15S','S Channel','Pro',15000,'HyStream','300 kD',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B15S','S Channel','Pro',15000,'HyStream','0.1 μm',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B15S','S Channel','Pro',15000,'HyStream','0.2 μm',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B15S','S Channel','Pro',15000,'HyStream','0.45 μm',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B15S','S Channel','Pro',15000,'HyStream','0.65 μm',1.5,114.0,0.017,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B25H','HP Screen','Pro',25000,'ProStream','1 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B25H','HP Screen','Pro',25000,'ProStream','3 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B25H','HP Screen','Pro',25000,'ProStream','5 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B25H','HP Screen','Pro',25000,'ProStream','10 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B25H','HP Screen','Pro',25000,'ProStream','30 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B25H','HP Screen','Pro',25000,'ProStream','50 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B25H','HP Screen','Pro',25000,'ProStream','100 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B25H','HP Screen','Pro',25000,'ProStream','300 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B25H','HP Screen','Pro',25000,'HyStream','5 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B25H','HP Screen','Pro',25000,'HyStream','10 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B25H','HP Screen','Pro',25000,'HyStream','30 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B25H','HP Screen','Pro',25000,'HyStream','50 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B25H','HP Screen','Pro',25000,'HyStream','100 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B25H','HP Screen','Pro',25000,'HyStream','300 kD',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B25H','HP Screen','Pro',25000,'HyStream','0.1 μm',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20B25H','HP Screen','Pro',25000,'HyStream','0.2 μm',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B25H','HP Screen','Pro',25000,'HyStream','0.45 μm',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B25H','HP Screen','Pro',25000,'HyStream','0.65 μm',2.5,190.0,0.018,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP001B25L','LP Screen','Pro',25000,'ProStream','1 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP003B25L','LP Screen','Pro',25000,'ProStream','3 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP005B25L','LP Screen','Pro',25000,'ProStream','5 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010B25L','LP Screen','Pro',25000,'ProStream','10 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030B25L','LP Screen','Pro',25000,'ProStream','30 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050B25L','LP Screen','Pro',25000,'ProStream','50 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100B25L','LP Screen','Pro',25000,'ProStream','100 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP300B25L','LP Screen','Pro',25000,'ProStream','300 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PPN65B25L','LP Screen','Pro',25000,'ProStream','0.65 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP005B25L','LP Screen','Pro',25000,'HyStream','5 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010B25L','LP Screen','Pro',25000,'HyStream','10 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030B25L','LP Screen','Pro',25000,'HyStream','30 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050B25L','LP Screen','Pro',25000,'HyStream','50 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100B25L','LP Screen','Pro',25000,'HyStream','100 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP300B25L','LP Screen','Pro',25000,'HyStream','300 kD',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10B25L','LP Screen','Pro',25000,'HyStream','0.1 μm',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45B25L','LP Screen','Pro',25000,'HyStream','0.45 μm',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65B25L','LP Screen','Pro',25000,'HyStream','0.65 μm',2.5,190.0,0.021,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP001LP1J','0.5mm Open','SIUS',100,'ProStream','1 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001MP1J','0.5mm Open','SIUS',100,'ProStream','1 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003LP1J','0.5mm Open','SIUS',100,'ProStream','3 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003MP1J','0.5mm Open','SIUS',100,'ProStream','3 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005LP1J','0.5mm Open','SIUS',100,'ProStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005MP1J','0.5mm Open','SIUS',100,'ProStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010LP1J','0.5mm Open','SIUS',100,'ProStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010MP1J','0.5mm Open','SIUS',100,'ProStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030LP1J','0.5mm Open','SIUS',100,'ProStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030MP1J','0.5mm Open','SIUS',100,'ProStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050LP1J','0.5mm Open','SIUS',100,'ProStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050MP1J','0.5mm Open','SIUS',100,'ProStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100LP1J','0.5mm Open','SIUS',100,'ProStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100MP1J','0.5mm Open','SIUS',100,'ProStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300LP1J','0.5mm Open','SIUS',100,'ProStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300MP1J','0.5mm Open','SIUS',100,'ProStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PPN50LP1J','0.5mm Open','SIUS',100,'ProStream','0.5 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005LP1J','0.5mm Open','SIUS',100,'HyStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005MP1J','0.5mm Open','SIUS',100,'HyStream','5 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010LP1J','0.5mm Open','SIUS',100,'HyStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010MP1J','0.5mm Open','SIUS',100,'HyStream','10 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030LP1J','0.5mm Open','SIUS',100,'HyStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030MP1J','0.5mm Open','SIUS',100,'HyStream','30 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050LP1J','0.5mm Open','SIUS',100,'HyStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050MP1J','0.5mm Open','SIUS',100,'HyStream','50 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100LP1J','0.5mm Open','SIUS',100,'HyStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100MP1J','0.5mm Open','SIUS',100,'HyStream','100 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300LP1J','0.5mm Open','SIUS',100,'HyStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300MP1J','0.5mm Open','SIUS',100,'HyStream','300 kD',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10LP1J','0.5mm Open','SIUS',100,'HyStream','0.1 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10MP1J','0.5mm Open','SIUS',100,'HyStream','0.1 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20LP1J','0.5mm Open','SIUS',100,'HyStream','0.2 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20MP1J','0.5mm Open','SIUS',100,'HyStream','0.2 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45LP1J','0.5mm Open','SIUS',100,'HyStream','0.45 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45MP1J','0.5mm Open','SIUS',100,'HyStream','0.45 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65LP1J','0.5mm Open','SIUS',100,'HyStream','0.65 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65MP1J','0.5mm Open','SIUS',100,'HyStream','0.65 μm',0.01,1.2,0.05,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001LP1L','LP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001MP1L','LP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003LP1L','LP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003MP1L','LP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005LP1L','LP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005MP1L','LP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010LP1L','LP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010MP1L','LP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030LP1L','LP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030MP1L','LP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050LP1L','LP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050MP1L','LP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100LP1L','LP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100MP1L','LP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300LP1L','LP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300MP1L','LP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PPN50LP1L','LP Screen','SIUS',100,'ProStream','0.5 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PPN65LP1L','LP Screen','SIUS',100,'ProStream','0.65 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005LP1L','LP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005MP1L','LP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010LP1L','LP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010MP1L','LP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030LP1L','LP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030MP1L','LP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050LP1L','LP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050MP1L','LP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100LP1L','LP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100MP1L','LP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300LP1L','LP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300MP1L','LP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10LP1L','LP Screen','SIUS',100,'HyStream','0.1 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10MP1L','LP Screen','SIUS',100,'HyStream','0.1 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20LP1L','LP Screen','SIUS',100,'HyStream','0.2 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20MP1L','LP Screen','SIUS',100,'HyStream','0.2 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45LP1L','LP Screen','SIUS',100,'HyStream','0.45 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45MP1L','LP Screen','SIUS',100,'HyStream','0.45 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65LP1L','LP Screen','SIUS',100,'HyStream','0.65 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65MP1L','LP Screen','SIUS',100,'HyStream','0.65 μm',0.01,1.2,0.014,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001LP2J','0.5mm Open','SIUS',200,'ProStream','1 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001MP2J','0.5mm Open','SIUS',200,'ProStream','1 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003LP2J','0.5mm Open','SIUS',200,'ProStream','3 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003MP2J','0.5mm Open','SIUS',200,'ProStream','3 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005LP2J','0.5mm Open','SIUS',200,'ProStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005MP2J','0.5mm Open','SIUS',200,'ProStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010LP2J','0.5mm Open','SIUS',200,'ProStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010MP2J','0.5mm Open','SIUS',200,'ProStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030LP2J','0.5mm Open','SIUS',200,'ProStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030MP2J','0.5mm Open','SIUS',200,'ProStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050LP2J','0.5mm Open','SIUS',200,'ProStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050MP2J','0.5mm Open','SIUS',200,'ProStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100LP2J','0.5mm Open','SIUS',200,'ProStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100MP2J','0.5mm Open','SIUS',200,'ProStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300LP2J','0.5mm Open','SIUS',200,'ProStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300MP2J','0.5mm Open','SIUS',200,'ProStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005LP2J','0.5mm Open','SIUS',200,'HyStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005MP2J','0.5mm Open','SIUS',200,'HyStream','5 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010LP2J','0.5mm Open','SIUS',200,'HyStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010MP2J','0.5mm Open','SIUS',200,'HyStream','10 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030LP2J','0.5mm Open','SIUS',200,'HyStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030MP2J','0.5mm Open','SIUS',200,'HyStream','30 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050LP2J','0.5mm Open','SIUS',200,'HyStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050MP2J','0.5mm Open','SIUS',200,'HyStream','50 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100LP2J','0.5mm Open','SIUS',200,'HyStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100MP2J','0.5mm Open','SIUS',200,'HyStream','100 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300LP2J','0.5mm Open','SIUS',200,'HyStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300MP2J','0.5mm Open','SIUS',200,'HyStream','300 kD',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10LP2J','0.5mm Open','SIUS',200,'HyStream','0.1 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10MP2J','0.5mm Open','SIUS',200,'HyStream','0.1 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20LP2J','0.5mm Open','SIUS',200,'HyStream','0.2 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20MP2J','0.5mm Open','SIUS',200,'HyStream','0.2 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45LP2J','0.5mm Open','SIUS',200,'HyStream','0.45 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45MP2J','0.5mm Open','SIUS',200,'HyStream','0.45 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65LP2J','0.5mm Open','SIUS',200,'HyStream','0.65 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65MP2J','0.5mm Open','SIUS',200,'HyStream','0.65 μm',0.02,2.1,0.05,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001LP2L','LP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001MP2L','LP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003LP2L','LP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003MP2L','LP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005LP2L','LP Screen','SIUS',200,'ProStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005MP2L','LP Screen','SIUS',200,'ProStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010LP2L','LP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010MP2L','LP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030LP2L','LP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030MP2L','LP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050LP2L','LP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050MP2L','LP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100LP2L','LP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100MP2L','LP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300LP2L','LP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300MP2L','LP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PPH01LP2L','LP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005LP2L','LP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005MP2L','LP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010LP2L','LP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010MP2L','LP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030LP2L','LP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030MP2L','LP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050LP2L','LP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050MP2L','LP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100LP2L','LP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100MP2L','LP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300LP2L','LP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300MP2L','LP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10LP2L','LP Screen','SIUS',200,'HyStream','0.1 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10MP2L','LP Screen','SIUS',200,'HyStream','0.1 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20LP2L','LP Screen','SIUS',200,'HyStream','0.2 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20MP2L','LP Screen','SIUS',200,'HyStream','0.2 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45LP2L','LP Screen','SIUS',200,'HyStream','0.45 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45MP2L','LP Screen','SIUS',200,'HyStream','0.45 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65LP2L','LP Screen','SIUS',200,'HyStream','0.65 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65MP2L','LP Screen','SIUS',200,'HyStream','0.65 μm',0.02,2.1,0.014,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001L01J','0.5mm Open','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001M01J','0.5mm Open','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003L01J','0.5mm Open','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003M01J','0.5mm Open','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005L01J','0.5mm Open','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005M01J','0.5mm Open','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010L01J','0.5mm Open','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010M01J','0.5mm Open','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030L01J','0.5mm Open','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030M01J','0.5mm Open','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050L01J','0.5mm Open','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050M01J','0.5mm Open','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100L01J','0.5mm Open','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100M01J','0.5mm Open','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300L01J','0.5mm Open','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300M01J','0.5mm Open','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005L01J','0.5mm Open','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005M01J','0.5mm Open','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010L01J','0.5mm Open','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010M01J','0.5mm Open','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030L01J','0.5mm Open','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030M01J','0.5mm Open','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050L01J','0.5mm Open','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050M01J','0.5mm Open','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100L01J','0.5mm Open','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100M01J','0.5mm Open','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300L01J','0.5mm Open','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300M01J','0.5mm Open','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10L01J','0.5mm Open','SIUS',1000,'HyStream','0.1 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10M01J','0.5mm Open','SIUS',1000,'HyStream','0.1 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20L01J','0.5mm Open','SIUS',1000,'HyStream','0.2 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20M01J','0.5mm Open','SIUS',1000,'HyStream','0.2 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45L01J','0.5mm Open','SIUS',1000,'HyStream','0.45 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45M01J','0.5mm Open','SIUS',1000,'HyStream','0.45 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65L01J','0.5mm Open','SIUS',1000,'HyStream','0.65 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65M01J','0.5mm Open','SIUS',1000,'HyStream','0.65 μm',0.1,8.7,0.05,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001L01L','LP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001M01L','LP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003L01L','LP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003M01L','LP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005L01L','LP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005M01L','LP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010L01L','LP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010M01L','LP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030L01L','LP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030M01L','LP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050L01L','LP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050M01L','LP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100L01L','LP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100M01L','LP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300L01L','LP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300M01L','LP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PPH01M01L','LP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PPN50M01L','LP Screen','SIUS',1000,'ProStream','0.5 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005L01L','LP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005M01L','LP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010L01L','LP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010M01L','LP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030L01L','LP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030M01L','LP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050L01L','LP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050M01L','LP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100L01L','LP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100M01L','LP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300L01L','LP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300M01L','LP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10L01L','LP Screen','SIUS',1000,'HyStream','0.1 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10M01L','LP Screen','SIUS',1000,'HyStream','0.1 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20L01L','LP Screen','SIUS',1000,'HyStream','0.2 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20M01L','LP Screen','SIUS',1000,'HyStream','0.2 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45L01L','LP Screen','SIUS',1000,'HyStream','0.45 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45M01L','LP Screen','SIUS',1000,'HyStream','0.45 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65L01L','LP Screen','SIUS',1000,'HyStream','0.65 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65M01L','LP Screen','SIUS',1000,'HyStream','0.65 μm',0.1,8.7,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001G05J','0.5mm Open','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G05J','0.5mm Open','SIUS',5000,'ProStream','3 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G05J','0.5mm Open','SIUS',5000,'ProStream','5 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G05J','0.5mm Open','SIUS',5000,'ProStream','10 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G05J','0.5mm Open','SIUS',5000,'ProStream','30 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G05J','0.5mm Open','SIUS',5000,'ProStream','50 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G05J','0.5mm Open','SIUS',5000,'ProStream','100 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G05J','0.5mm Open','SIUS',5000,'ProStream','300 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G05J','0.5mm Open','SIUS',5000,'HyStream','5 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G05J','0.5mm Open','SIUS',5000,'HyStream','10 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G05J','0.5mm Open','SIUS',5000,'HyStream','30 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G05J','0.5mm Open','SIUS',5000,'HyStream','50 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G05J','0.5mm Open','SIUS',5000,'HyStream','100 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G05J','0.5mm Open','SIUS',5000,'HyStream','300 kD',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G05J','0.5mm Open','SIUS',5000,'HyStream','0.1 μm',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G05J','0.5mm Open','SIUS',5000,'HyStream','0.2 μm',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G05J','0.5mm Open','SIUS',5000,'HyStream','0.45 μm',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G05J','0.5mm Open','SIUS',5000,'HyStream','0.65 μm',0.5,38.0,0.05,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G05L','LP Screen','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G05L','LP Screen','SIUS',5000,'ProStream','3 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G05L','LP Screen','SIUS',5000,'ProStream','5 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G05L','LP Screen','SIUS',5000,'ProStream','10 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G05L','LP Screen','SIUS',5000,'ProStream','30 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G05L','LP Screen','SIUS',5000,'ProStream','50 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G05L','LP Screen','SIUS',5000,'ProStream','100 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G05L','LP Screen','SIUS',5000,'ProStream','300 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PPH01G05L','LP Screen','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G05L','LP Screen','SIUS',5000,'HyStream','5 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G05L','LP Screen','SIUS',5000,'HyStream','10 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G05L','LP Screen','SIUS',5000,'HyStream','30 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G05L','LP Screen','SIUS',5000,'HyStream','50 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G05L','LP Screen','SIUS',5000,'HyStream','100 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G05L','LP Screen','SIUS',5000,'HyStream','300 kD',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G05L','LP Screen','SIUS',5000,'HyStream','0.1 μm',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G05L','LP Screen','SIUS',5000,'HyStream','0.2 μm',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G05L','LP Screen','SIUS',5000,'HyStream','0.45 μm',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G05L','LP Screen','SIUS',5000,'HyStream','0.65 μm',0.5,38.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G15J','0.5mm Open','SIUS',15000,'ProStream','1 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G15J','0.5mm Open','SIUS',15000,'ProStream','3 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G15J','0.5mm Open','SIUS',15000,'ProStream','5 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G15J','0.5mm Open','SIUS',15000,'ProStream','10 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G15J','0.5mm Open','SIUS',15000,'ProStream','30 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G15J','0.5mm Open','SIUS',15000,'ProStream','50 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G15J','0.5mm Open','SIUS',15000,'ProStream','100 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G15J','0.5mm Open','SIUS',15000,'ProStream','300 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G15J','0.5mm Open','SIUS',15000,'HyStream','5 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G15J','0.5mm Open','SIUS',15000,'HyStream','10 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G15J','0.5mm Open','SIUS',15000,'HyStream','30 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G15J','0.5mm Open','SIUS',15000,'HyStream','50 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G15J','0.5mm Open','SIUS',15000,'HyStream','100 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G15J','0.5mm Open','SIUS',15000,'HyStream','300 kD',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G15J','0.5mm Open','SIUS',15000,'HyStream','0.1 μm',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G15J','0.5mm Open','SIUS',15000,'HyStream','0.2 μm',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G15J','0.5mm Open','SIUS',15000,'HyStream','0.45 μm',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G15J','0.5mm Open','SIUS',15000,'HyStream','0.65 μm',1.5,114.0,0.05,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G15L','LP Screen','SIUS',15000,'ProStream','1 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G15L','LP Screen','SIUS',15000,'ProStream','3 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G15L','LP Screen','SIUS',15000,'ProStream','5 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G15L','LP Screen','SIUS',15000,'ProStream','10 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G15L','LP Screen','SIUS',15000,'ProStream','30 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G15L','LP Screen','SIUS',15000,'ProStream','50 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G15L','LP Screen','SIUS',15000,'ProStream','100 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G15L','LP Screen','SIUS',15000,'ProStream','300 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G15L','LP Screen','SIUS',15000,'HyStream','5 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G15L','LP Screen','SIUS',15000,'HyStream','10 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G15L','LP Screen','SIUS',15000,'HyStream','30 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G15L','LP Screen','SIUS',15000,'HyStream','50 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G15L','LP Screen','SIUS',15000,'HyStream','100 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G15L','LP Screen','SIUS',15000,'HyStream','300 kD',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G15L','LP Screen','SIUS',15000,'HyStream','0.1 μm',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G15L','LP Screen','SIUS',15000,'HyStream','0.2 μm',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G15L','LP Screen','SIUS',15000,'HyStream','0.45 μm',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G15L','LP Screen','SIUS',15000,'HyStream','0.65 μm',1.5,114.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G25J','0.5mm Open','SIUS',25000,'ProStream','1 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G25J','0.5mm Open','SIUS',25000,'ProStream','3 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G25J','0.5mm Open','SIUS',25000,'ProStream','5 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G25J','0.5mm Open','SIUS',25000,'ProStream','10 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G25J','0.5mm Open','SIUS',25000,'ProStream','30 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G25J','0.5mm Open','SIUS',25000,'ProStream','50 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G25J','0.5mm Open','SIUS',25000,'ProStream','100 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G25J','0.5mm Open','SIUS',25000,'ProStream','300 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G25J','0.5mm Open','SIUS',25000,'HyStream','5 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G25J','0.5mm Open','SIUS',25000,'HyStream','10 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G25J','0.5mm Open','SIUS',25000,'HyStream','30 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G25J','0.5mm Open','SIUS',25000,'HyStream','50 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G25J','0.5mm Open','SIUS',25000,'HyStream','100 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G25J','0.5mm Open','SIUS',25000,'HyStream','300 kD',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G25J','0.5mm Open','SIUS',25000,'HyStream','0.1 μm',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G25J','0.5mm Open','SIUS',25000,'HyStream','0.2 μm',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G25J','0.5mm Open','SIUS',25000,'HyStream','0.45 μm',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G25J','0.5mm Open','SIUS',25000,'HyStream','0.65 μm',2.5,190.0,0.05,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G25L','LP Screen','SIUS',25000,'ProStream','1 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G25L','LP Screen','SIUS',25000,'ProStream','3 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G25L','LP Screen','SIUS',25000,'ProStream','5 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G25L','LP Screen','SIUS',25000,'ProStream','10 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G25L','LP Screen','SIUS',25000,'ProStream','30 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G25L','LP Screen','SIUS',25000,'ProStream','50 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G25L','LP Screen','SIUS',25000,'ProStream','100 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G25L','LP Screen','SIUS',25000,'ProStream','300 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G25L','LP Screen','SIUS',25000,'HyStream','5 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G25L','LP Screen','SIUS',25000,'HyStream','10 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G25L','LP Screen','SIUS',25000,'HyStream','30 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G25L','LP Screen','SIUS',25000,'HyStream','50 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G25L','LP Screen','SIUS',25000,'HyStream','100 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G25L','LP Screen','SIUS',25000,'HyStream','300 kD',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G25L','LP Screen','SIUS',25000,'HyStream','0.1 μm',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G25L','LP Screen','SIUS',25000,'HyStream','0.2 μm',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G25L','LP Screen','SIUS',25000,'HyStream','0.45 μm',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G25L','LP Screen','SIUS',25000,'HyStream','0.65 μm',2.5,190.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP001LP1E','EP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001MP1E','EP Screen','SIUS',100,'ProStream','1 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003LP1E','EP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP003MP1E','EP Screen','SIUS',100,'ProStream','3 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005LP1E','EP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP005MP1E','EP Screen','SIUS',100,'ProStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010LP1E','EP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP010MP1E','EP Screen','SIUS',100,'ProStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030LP1E','EP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP030MP1E','EP Screen','SIUS',100,'ProStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050LP1E','EP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP050MP1E','EP Screen','SIUS',100,'ProStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100LP1E','EP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP100MP1E','EP Screen','SIUS',100,'ProStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300LP1E','EP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP300MP1E','EP Screen','SIUS',100,'ProStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005LP1E','EP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP005MP1E','EP Screen','SIUS',100,'HyStream','5 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010LP1E','EP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP010MP1E','EP Screen','SIUS',100,'HyStream','10 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030LP1E','EP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP030MP1E','EP Screen','SIUS',100,'HyStream','30 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050LP1E','EP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP050MP1E','EP Screen','SIUS',100,'HyStream','50 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100LP1E','EP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP100MP1E','EP Screen','SIUS',100,'HyStream','100 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300LP1E','EP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XP300MP1E','EP Screen','SIUS',100,'HyStream','300 kD',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10LP1E','EP Screen','SIUS',100,'HyStream','0.1 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM10MP1E','EP Screen','SIUS',100,'HyStream','0.1 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20LP1E','EP Screen','SIUS',100,'HyStream','0.2 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM20MP1E','EP Screen','SIUS',100,'HyStream','0.2 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45LP1E','EP Screen','SIUS',100,'HyStream','0.45 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM45MP1E','EP Screen','SIUS',100,'HyStream','0.45 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65LP1E','EP Screen','SIUS',100,'HyStream','0.65 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('XPM65MP1E','EP Screen','SIUS',100,'HyStream','0.65 µm',0.01,1.2,0.025,3.6,1,0.01);
INSERT INTO "cassette_lookup" VALUES ('PP001LP2E','EP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001MP2E','EP Screen','SIUS',200,'ProStream','1 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003LP2E','EP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP003MP2E','EP Screen','SIUS',200,'ProStream','3 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005LP2E','EP Screen','SIUS',200,'ProStream','5 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP005MP2E','EP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010LP2E','EP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP010MP2E','EP Screen','SIUS',200,'ProStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030LP2E','EP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP030MP2E','EP Screen','SIUS',200,'ProStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050LP2E','EP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP050MP2E','EP Screen','SIUS',200,'ProStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100LP2E','EP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP100MP2E','EP Screen','SIUS',200,'ProStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300LP2E','EP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP300MP2E','EP Screen','SIUS',200,'ProStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005LP2E','EP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP005MP2E','EP Screen','SIUS',200,'HyStream','5 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010LP2E','EP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP010MP2E','EP Screen','SIUS',200,'HyStream','10 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030LP2E','EP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP030MP2E','EP Screen','SIUS',200,'HyStream','30 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050LP2E','EP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP050MP2E','EP Screen','SIUS',200,'HyStream','50 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100LP2E','EP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP100MP2E','EP Screen','SIUS',200,'HyStream','100 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300LP2E','EP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XP300MP2E','EP Screen','SIUS',200,'HyStream','300 kD',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10LP2E','EP Screen','SIUS',200,'HyStream','0.1 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM10MP2E','EP Screen','SIUS',200,'HyStream','0.1 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20LP2E','EP Screen','SIUS',200,'HyStream','0.2 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM20MP2E','EP Screen','SIUS',200,'HyStream','0.2 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45LP2E','EP Screen','SIUS',200,'HyStream','0.45 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM45MP2E','EP Screen','SIUS',200,'HyStream','0.45 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65LP2E','EP Screen','SIUS',200,'HyStream','0.65 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('XPM65MP2E','EP Screen','SIUS',200,'HyStream','0.65 µm',0.02,2.1,0.025,3.6,2,0.02);
INSERT INTO "cassette_lookup" VALUES ('PP001L01E','EP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001M01E','EP Screen','SIUS',1000,'ProStream','1 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003L01E','EP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP003M01E','EP Screen','SIUS',1000,'ProStream','3 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005L01E','EP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP005M01E','EP Screen','SIUS',1000,'ProStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010L01E','EP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010M01E','EP Screen','SIUS',1000,'ProStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030L01E','EP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030M01E','EP Screen','SIUS',1000,'ProStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050L01E','EP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050M01E','EP Screen','SIUS',1000,'ProStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100L01E','EP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100M01E','EP Screen','SIUS',1000,'ProStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300L01E','EP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP300M01E','EP Screen','SIUS',1000,'ProStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005L01E','EP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP005M01E','EP Screen','SIUS',1000,'HyStream','5 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010L01E','EP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010M01E','EP Screen','SIUS',1000,'HyStream','10 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030L01E','EP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030M01E','EP Screen','SIUS',1000,'HyStream','30 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050L01E','EP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050M01E','EP Screen','SIUS',1000,'HyStream','50 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100L01E','EP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100M01E','EP Screen','SIUS',1000,'HyStream','100 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300L01E','EP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP300M01E','EP Screen','SIUS',1000,'HyStream','300 kD',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10L01E','EP Screen','SIUS',1000,'HyStream','0.1 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM10M01E','EP Screen','SIUS',1000,'HyStream','0.1 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20L01E','EP Screen','SIUS',1000,'HyStream','0.2 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM20M01E','EP Screen','SIUS',1000,'HyStream','0.2 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45L01E','EP Screen','SIUS',1000,'HyStream','0.45 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM45M01E','EP Screen','SIUS',1000,'HyStream','0.45 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65L01E','EP Screen','SIUS',1000,'HyStream','0.65 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XPM65M01E','EP Screen','SIUS',1000,'HyStream','0.65 µm',0.1,8.7,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP001G05E','EP Screen','SIUS',5000,'ProStream','1 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G05E','EP Screen','SIUS',5000,'ProStream','3 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G05E','EP Screen','SIUS',5000,'ProStream','5 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G05E','EP Screen','SIUS',5000,'ProStream','10 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G05E','EP Screen','SIUS',5000,'ProStream','30 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G05E','EP Screen','SIUS',5000,'ProStream','50 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G05E','EP Screen','SIUS',5000,'ProStream','100 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G05E','EP Screen','SIUS',5000,'ProStream','300 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G05E','EP Screen','SIUS',5000,'HyStream','5 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G05E','EP Screen','SIUS',5000,'HyStream','10 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G05E','EP Screen','SIUS',5000,'HyStream','30 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G05E','EP Screen','SIUS',5000,'HyStream','50 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G05E','EP Screen','SIUS',5000,'HyStream','100 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G05E','EP Screen','SIUS',5000,'HyStream','300 kD',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G05E','EP Screen','SIUS',5000,'HyStream','0.1 µm',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G05E','EP Screen','SIUS',5000,'HyStream','0.2 µm',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G05E','EP Screen','SIUS',5000,'HyStream','0.45 µm',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G05E','EP Screen','SIUS',5000,'HyStream','0.65 µm',0.5,38.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G15E','EP Screen','SIUS',15000,'ProStream','1 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G15E','EP Screen','SIUS',15000,'ProStream','3 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G15E','EP Screen','SIUS',15000,'ProStream','5 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G15E','EP Screen','SIUS',15000,'ProStream','10 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G15E','EP Screen','SIUS',15000,'ProStream','30 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G15E','EP Screen','SIUS',15000,'ProStream','50 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G15E','EP Screen','SIUS',15000,'ProStream','100 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G15E','EP Screen','SIUS',15000,'ProStream','300 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G15E','EP Screen','SIUS',15000,'HyStream','5 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G15E','EP Screen','SIUS',15000,'HyStream','10 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G15E','EP Screen','SIUS',15000,'HyStream','30 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G15E','EP Screen','SIUS',15000,'HyStream','50 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G15E','EP Screen','SIUS',15000,'HyStream','100 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G15E','EP Screen','SIUS',15000,'HyStream','300 kD',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G15E','EP Screen','SIUS',15000,'HyStream','0.1 µm',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G15E','EP Screen','SIUS',15000,'HyStream','0.2 µm',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G15E','EP Screen','SIUS',15000,'HyStream','0.45 µm',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G15E','EP Screen','SIUS',15000,'HyStream','0.65 µm',1.5,114.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP001G25E','EP Screen','SIUS',25000,'ProStream','1 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP003G25E','EP Screen','SIUS',25000,'ProStream','3 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP005G25E','EP Screen','SIUS',25000,'ProStream','5 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010G25E','EP Screen','SIUS',25000,'ProStream','10 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030G25E','EP Screen','SIUS',25000,'ProStream','30 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050G25E','EP Screen','SIUS',25000,'ProStream','50 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100G25E','EP Screen','SIUS',25000,'ProStream','100 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP300G25E','EP Screen','SIUS',25000,'ProStream','300 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP005G25E','EP Screen','SIUS',25000,'HyStream','5 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010G25E','EP Screen','SIUS',25000,'HyStream','10 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030G25E','EP Screen','SIUS',25000,'HyStream','30 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050G25E','EP Screen','SIUS',25000,'HyStream','50 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100G25E','EP Screen','SIUS',25000,'HyStream','100 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP300G25E','EP Screen','SIUS',25000,'HyStream','300 kD',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM10G25E','EP Screen','SIUS',25000,'HyStream','0.1 µm',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM20G25E','EP Screen','SIUS',25000,'HyStream','0.2 µm',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM45G25E','EP Screen','SIUS',25000,'HyStream','0.45 µm',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XPM65G25E','EP Screen','SIUS',25000,'HyStream','0.65 µm',2.5,190.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010C01L','LP Screen','SIUS Gamma',1000,'ProStream','10 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030C01L','LP Screen','SIUS Gamma',1000,'ProStream','30 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050C01L','LP Screen','SIUS Gamma',1000,'ProStream','50 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100C01L','LP Screen','SIUS Gamma',1000,'ProStream','100 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010F05L','LP Screen','SIUS Gamma',5000,'ProStream','10 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030F05L','LP Screen','SIUS Gamma',5000,'ProStream','30 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050F05L','LP Screen','SIUS Gamma',5000,'ProStream','50 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100F05L','LP Screen','SIUS Gamma',5000,'ProStream','100 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010F15L','LP Screen','SIUS Gamma',15000,'ProStream','10 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030F15L','LP Screen','SIUS Gamma',15000,'ProStream','30 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050F15L','LP Screen','SIUS Gamma',15000,'ProStream','50 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100F15L','LP Screen','SIUS Gamma',15000,'ProStream','100 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010F25L','LP Screen','SIUS Gamma',25000,'ProStream','10 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030F25L','LP Screen','SIUS Gamma',25000,'ProStream','30 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050F25L','LP Screen','SIUS Gamma',25000,'ProStream','50 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100F25L','LP Screen','SIUS Gamma',25000,'ProStream','100 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP010C01E','EP Screen','SIUS Gamma',1000,'ProStream','10 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP030C01E','EP Screen','SIUS Gamma',1000,'ProStream','30 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP050C01E','EP Screen','SIUS Gamma',1000,'ProStream','50 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP100C01E','EP Screen','SIUS Gamma',1000,'ProStream','100 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('PP010F05E','EP Screen','SIUS Gamma',5000,'ProStream','10 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP030F05E','EP Screen','SIUS Gamma',5000,'ProStream','30 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP050F05E','EP Screen','SIUS Gamma',5000,'ProStream','50 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP100F05E','EP Screen','SIUS Gamma',5000,'ProStream','100 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('PP010F15E','EP Screen','SIUS Gamma',15000,'ProStream','10 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP030F15E','EP Screen','SIUS Gamma',15000,'ProStream','30 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP050F15E','EP Screen','SIUS Gamma',15000,'ProStream','50 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP100F15E','EP Screen','SIUS Gamma',15000,'ProStream','100 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('PP010F25E','EP Screen','SIUS Gamma',25000,'ProStream','10 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP030F25E','EP Screen','SIUS Gamma',25000,'ProStream','30 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP050F25E','EP Screen','SIUS Gamma',25000,'ProStream','50 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('PP100F25E','EP Screen','SIUS Gamma',25000,'ProStream','100 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010C01L','LP Screen','SIUS Gamma',1000,'HyStream','10 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030C01L','LP Screen','SIUS Gamma',1000,'HyStream','30 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050C01L','LP Screen','SIUS Gamma',1000,'HyStream','50 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100C01L','LP Screen','SIUS Gamma',1000,'HyStream','100 kD',0.1,61.0,0.014,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010F05L','LP Screen','SIUS Gamma',5000,'HyStream','10 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030F05L','LP Screen','SIUS Gamma',5000,'HyStream','30 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050F05L','LP Screen','SIUS Gamma',5000,'HyStream','50 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100F05L','LP Screen','SIUS Gamma',5000,'HyStream','100 kD',0.5,509.0,0.014,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010F15L','LP Screen','SIUS Gamma',15000,'HyStream','10 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030F15L','LP Screen','SIUS Gamma',15000,'HyStream','30 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050F15L','LP Screen','SIUS Gamma',15000,'HyStream','50 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100F15L','LP Screen','SIUS Gamma',15000,'HyStream','100 kD',1.5,874.0,0.014,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010F25L','LP Screen','SIUS Gamma',25000,'HyStream','10 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030F25L','LP Screen','SIUS Gamma',25000,'HyStream','30 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050F25L','LP Screen','SIUS Gamma',25000,'HyStream','50 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100F25L','LP Screen','SIUS Gamma',25000,'HyStream','100 kD',2.5,1026.0,0.014,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP010C01E','EP Screen','SIUS Gamma',1000,'HyStream','10 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP030C01E','EP Screen','SIUS Gamma',1000,'HyStream','30 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP050C01E','EP Screen','SIUS Gamma',1000,'HyStream','50 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP100C01E','EP Screen','SIUS Gamma',1000,'HyStream','100 kD',0.1,61.0,0.025,3.6,10,0.1);
INSERT INTO "cassette_lookup" VALUES ('XP010F05E','EP Screen','SIUS Gamma',5000,'HyStream','10 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP030F05E','EP Screen','SIUS Gamma',5000,'HyStream','30 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP050F05E','EP Screen','SIUS Gamma',5000,'HyStream','50 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP100F05E','EP Screen','SIUS Gamma',5000,'HyStream','100 kD',0.5,509.0,0.025,16.7,10,0.5);
INSERT INTO "cassette_lookup" VALUES ('XP010F15E','EP Screen','SIUS Gamma',15000,'HyStream','10 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP030F15E','EP Screen','SIUS Gamma',15000,'HyStream','30 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP050F15E','EP Screen','SIUS Gamma',15000,'HyStream','50 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP100F15E','EP Screen','SIUS Gamma',15000,'HyStream','100 kD',1.5,874.0,0.025,16.7,30,1.5);
INSERT INTO "cassette_lookup" VALUES ('XP010F25E','EP Screen','SIUS Gamma',25000,'HyStream','10 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP030F25E','EP Screen','SIUS Gamma',25000,'HyStream','30 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP050F25E','EP Screen','SIUS Gamma',25000,'HyStream','50 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "cassette_lookup" VALUES ('XP100F25E','EP Screen','SIUS Gamma',25000,'HyStream','100 kD',2.5,1026.0,0.025,16.7,50,2.5);
INSERT INTO "firmware_version" VALUES (1,18,'0.6.3');
INSERT INTO "license_activation_status" VALUES (1,'7Dk/i0yto3TAQd47Whyygw==');
INSERT INTO "cfr_status" VALUES (1,'s302TJ7wA5cHQ8+90EMHlA==');
INSERT INTO "tubing_lookup" VALUES (1,'#13',600,60,6,4.0,2,4,4,3,2,600,6000,2,'13','1038','1178','1','3.96875','43481','43473',0.031,0.08,0.8,1.0,6,600,0.36,36,'mL/min');
INSERT INTO "tubing_lookup" VALUES (2,'#14',409,150,22,14.0,1,4,4,2,2,409,15000,1,'14','973','1170','2','4.7625','43481','43473',0.063,0.16,1.6,1.0,6,600,1.3,130,'mL/min');
INSERT INTO "tubing_lookup" VALUES (3,'#15',600,1700,170,NULL,0,4,4,1,1,600,170000,0,'15','668','1043','8','9.525','11749','43540',0.188,0.48,4.8,1.0,6,600,10,1000,'mL/min');
INSERT INTO "tubing_lookup" VALUES (4,'HP15',600,1800,180,NULL,0,4,4,1,1,600,180000,0,'115','668','1043','8','9.525','11749','43540',0.188,0.48,4.8,1.0,6,600,11,1100,'mL/min');
INSERT INTO "tubing_lookup" VALUES (5,'#16',450,600,80,54.0,1,4,4,2,1,450,60000,1,'16','918','1170','4','6.35','43481','43473',0.125,0.32,3.1,1.0,6,600,4.8,480,'mL/min');
INSERT INTO "tubing_lookup" VALUES (6,'#17',439,2000,280,NULL,0,4,4,1,1,429,200000,0,'17','668','1135','8','9.525','43481','43473',0.25,0.64,6.4,1.0,6,600,17,1700,'mL/min');
INSERT INTO "tubing_lookup" VALUES (7,'#18',395,2500,380,NULL,0,4,4,1,1,395,250000,0,'18','543','1168','8','11.1125','43481','43473',0.313,0.8,7.9,1.0,6,600,23,2300,'mL/min');
INSERT INTO "tubing_lookup" VALUES (8,'#24',429,2000,280,NULL,0,4,4,1,1,429,200000,0,'24','543','1025','8','11.1125','11749','43540',0.25,0.64,6.4,1.0,6,600,17,1700,'mL/min');
INSERT INTO "tubing_lookup" VALUES (9,'HP24',400,2000,300,NULL,0,4,4,1,1,400,200000,0,'124','543','1043','8','11.1125','11749','43540',0.25,0.64,6.4,1.0,6,600,18,1800,'mL/min');
INSERT INTO "tubing_lookup" VALUES (10,'#25',600,1700,170,105.0,0,4,4,1,1,600,170000,0,'25','793','1150','4','7.9375','43471','43473',0.188,0.48,4.8,1.0,6,600,10,1000,'mL/min');
INSERT INTO "tubing_lookup" VALUES (11,'#35',395,2500,380,NULL,0,4,4,1,1,395,250000,0,'35','418','1043','8','12.7','11749','43540',0.313,0.8,7.9,1.0,6,600,23,2300,'mL/min');
INSERT INTO "tubing_lookup" VALUES (12,'HP35',419,3000,430,NULL,0,4,4,1,1,419,300000,0,'135','418','1043','8','12.7','11749','43540',0.313,0.8,7.9,1.0,6,600,26,2600,'mL/min');
INSERT INTO "tubing_lookup" VALUES (13,'#36',400,3200,480,NULL,0,4,4,1,1,400,320000,0,'36','293','1043','16','14.2875','11749','43540',0.375,0.95,9.7,1.0,6,600,29,2900,'mL/min');
INSERT INTO "tubing_lookup" VALUES (14,'HP36',400,3800,570,NULL,0,4,4,1,0,400,380000,0,'136','293','1043','16','14.2875','11749','43540',0.375,0.95,9.7,1.0,6,600,34,3400,'mL/min');
INSERT INTO "tubing_lookup" VALUES (15,'#26',468,600,7002,NULL,0,4,4,3,4,468,600,0,'26','378','878','8','12.7','43473','43469',0.25,0.64,6.4,1.0,20,650,120,4000,'l/min');
INSERT INTO "tubing_lookup" VALUES (16,'#70',468,1200,14003,NULL,0,4,5,4,4,468,1200,0,'70','20','628','8','19.05','43469','43532',0.375,0.95,9.5,1.0,20,650,240,8000,'l/min');
INSERT INTO "tubing_lookup" VALUES (17,'#73',468,1200,14003,NULL,0,4,5,4,4,468,1200,0,'73','88','878','16','15.875','43473','43469',0.375,0.95,9.5,1.0,20,650,200,8000,'l/min');
INSERT INTO "tubing_lookup" VALUES (18,'#82',480,2000,22756,NULL,0,5,5,4,4,480,2000,0,'82','20','878','16','19.05','43473','43469',0.5,1.27,12.7,1.0,20,650,400,13000,'l/min');
INSERT INTO "tubing_lookup" VALUES (19,'#88',480,2000,22756,NULL,0,5,5,4,4,480,2000,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.5,1.27,12.7,1.0,20,650,400,13000,'l/min');
INSERT INTO "tubing_lookup" VALUES (20,'#89',493,3000,33258,NULL,0,5,5,4,4,493,3000,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.625,1.59,15.88,1.0,20,650,520,17000,'l/min');
INSERT INTO "tubing_lookup" VALUES (21,'#17',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'17','628','1128','8','9.525','43481','43473',NULL,NULL,NULL,1.0,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "tubing_lookup" VALUES (22,'#18',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'18','503','1128','8','11.1125','43481','43473',NULL,NULL,NULL,1.0,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "tubing_lookup" VALUES (23,'NA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,17,30000,'l/min');
INSERT INTO "tubing_lookup" VALUES (24,'NA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,17,30000,'mL/min');


INSERT INTO "pump_lookup" VALUES (1,'Main pump','KR2i',0.0,600,'rpm','1',1);
INSERT INTO "pump_lookup" VALUES (2,'Main pump','KMPi',0.0,650,'rpm','2',1);
INSERT INTO "pump_lookup" VALUES (3,'Main pump','KrosFlo FS15',30.0,3000,'rpm','1',0);
INSERT INTO "pump_lookup" VALUES (4,'Main pump','KrosFlo FS500',30.0,2400,'rpm','1',0);
INSERT INTO "pump_lookup" VALUES (5,'Aux pump','KRJr',0.0,300,'rpm','0',1);
INSERT INTO "pump_lookup" VALUES (6,'Aux pump','KR1',0.0,600,'rpm','0',1);
INSERT INTO "pump_lookup" VALUES (7,'Aux pump','IP',0.0,650,'rpm','0',1);
INSERT INTO "pump_lookup" VALUES (8,'Valve','KR2i_valve',NULL,NULL,NULL,NULL,1);
INSERT INTO "pump_lookup" VALUES (9,'Valve','KMPi_valve',NULL,NULL,NULL,NULL,1);

INSERT INTO "main_aux_pump_map" VALUES (1,'KR2i','KR1');
INSERT INTO "main_aux_pump_map" VALUES (2,'KR2i','KRJr');
INSERT INTO "main_aux_pump_map" VALUES (3,'KMPi','KR1');
INSERT INTO "main_aux_pump_map" VALUES (4,'KMPi','KRJr');
INSERT INTO "main_aux_pump_map" VALUES (5,'KMPi','IP');
INSERT INTO "main_aux_pump_map" VALUES (6,'KrosFlo FS15','KR1');
INSERT INTO "main_aux_pump_map" VALUES (7,'KrosFlo FS15','KRJr');
INSERT INTO "main_aux_pump_map" VALUES (8,'KrosFlo FS500','KR1');
INSERT INTO "main_aux_pump_map" VALUES (9,'KrosFlo FS500','KRJr');
INSERT INTO "main_aux_pump_map" VALUES (10,'KrosFlo FS500','IP');

INSERT INTO "role_master" VALUES (1,'Admin','Y');
INSERT INTO "role_master" VALUES (2,'Auditor','Y');
INSERT INTO "role_master" VALUES (3,'Manager','Y');
INSERT INTO "role_master" VALUES (4,'Technician','Y');
INSERT INTO "role_master" VALUES (5,'SuperAdmin','Y');


INSERT INTO "ramp_up_time" VALUES (1,1);
INSERT INTO "ramp_up_time" VALUES (2,2);
INSERT INTO "ramp_up_time" VALUES (3,3);
INSERT INTO "ramp_up_time" VALUES (4,4);
INSERT INTO "ramp_up_time" VALUES (5,5);
INSERT INTO "ramp_up_time" VALUES (6,6);
INSERT INTO "ramp_up_time" VALUES (7,7);
INSERT INTO "ramp_up_time" VALUES (8,8);
INSERT INTO "ramp_up_time" VALUES (9,9);
INSERT INTO "ramp_up_time" VALUES (10,10);
INSERT INTO "ramp_up_time" VALUES (11,11);
INSERT INTO "ramp_up_time" VALUES (12,12);
INSERT INTO "ramp_up_time" VALUES (13,13);
INSERT INTO "ramp_up_time" VALUES (14,14);
INSERT INTO "ramp_up_time" VALUES (15,15);

INSERT INTO "end_point_lookup" VALUES (1,'Concentration Factor','C','Y');
INSERT INTO "end_point_lookup" VALUES (2,'Permeate Weight','Both','Y');
INSERT INTO "end_point_lookup" VALUES (3,'UV','Both','Y');
INSERT INTO "end_point_lookup" VALUES (4,'Diafiltration Volume','D','Y');
INSERT INTO "end_point_lookup" VALUES (5,'Conductivity','D','Y');
INSERT INTO "end_point_lookup" VALUES (6,'pH','Both','Y');
INSERT INTO "end_point_lookup" VALUES (7,'Turbidity','Both','Y');
INSERT INTO "recirc_flow_unit" VALUES (1,'per minute');
INSERT INTO "recirc_flow_unit" VALUES (2,'per second');

INSERT INTO "mode_lookup" VALUES (1,'C','Concentration Factor',0,1,'Permeate',NULL,'Y');
INSERT INTO "mode_lookup" VALUES (2,'D','Diafiltration Volume',1,2,'Diafiltration 1','Permeate','Y');
INSERT INTO "mode_lookup" VALUES (3,'C/D','Concentration Factor/Diafiltration Volume',1,2,'Diafiltration 1','Permeate','Y');
INSERT INTO "mode_lookup" VALUES (4,'C/D/C','Concentration Factor/Diafiltration Volume/Concentration Factor',1,2,'Diafiltration 1','Permeate','Y');
INSERT INTO "mode_lookup" VALUES (5,'C/D/D/C','Concentration Factor/Diafiltration Volume/Diafiltration Volume/Concentration Factor',2,2,'Diafiltration 1','Diafiltration 2','Y');
INSERT INTO "mode_lookup" VALUES (6,'C/D/C/D','Concentration Factor/Diafiltration Volume/Concentration Factor/Diafiltration Volume',2,2,'Diafiltration 1','Diafiltration 2','Y');
INSERT INTO "mode_lookup" VALUES (7,'CFC','Constant Feed Concentration',1,2,'Constant Feed','Permeate','Y');
INSERT INTO "mode_lookup" VALUES (8,'CFC/D/C','Constant Feed Concentration/Diafiltration Volume/Concentration Factor',2,2,'Constant Feed','Diafiltration 1','Y');
INSERT INTO "mode_lookup" VALUES (9,'TFDF Wizard','Concentration Factor/Diafiltration Volume/Concentration Factor',NULL,NULL,NULL,NULL,'N');
INSERT INTO "mode_lookup" VALUES (10,'Vacuum','Vacuum',0,0,NULL,NULL,'Y');
INSERT INTO "mode_lookup" VALUES (11,'NWP','Normalized Water Permeability',0,0,NULL,NULL,'Y');
INSERT INTO "mode_lookup" VALUES (12,'Flux C','Flux C',0,0,NULL,NULL,'Y');
INSERT INTO "mode_lookup" VALUES (13,'Flux CV','Flux CV',1,1,'Constant Feed',NULL,'Y');
INSERT INTO "mode_lookup" VALUES (14,'Cleaning','Cleaning',0,1,'Permeate',NULL,'Y');
INSERT INTO "mode_lookup" VALUES (15,'Flushing','Flushing',0,1,'Permeate',NULL,'Y');
INSERT INTO "filter_plate_insert_lookup" VALUES ('TFP99-SP20',121,312);
INSERT INTO "filter_plate_insert_lookup" VALUES ('TFP75-SE16',118,252);
INSERT INTO "filter_plate_insert_lookup" VALUES ('TFPLS-SA08',8,10);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E001-05-N','MicroKros','MICROKROS 20CM 1K MPES .5MM MLLXFLL 1/PK','1 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E001-05-S','MicroKros','MICROKROS 20CM 1K MPES .5MM MLLXFLL 1/PK STERILE','1 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E003-05-N','MicroKros','MICROKROS 20CM 3K MPES 0.5MM MLL X FLL 1/PK','3 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E003-05-S','MicroKros','MICROKROS 20CM 3K MPES 0.5MM MLL X FLL 1/PK STERILE','3 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E003-10-N','MicroKros','MICROKROS 20CM 3K MPES 1MM MLL X FLL 1/PK','3 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E003-10-S','MicroKros','MICROKROS 20CM 3K MPES 1MM MLL X FLL 1/PK STERILE','3 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E005-05-N','MicroKros','MICROKROS 20CM 5K MPES 0.5MM MLL X FLL 1/PK','5 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E005-05-S','MicroKros','MICROKROS 20CM 5K MPES 0.5MM MLL X FLL 1/PK STERILE','5 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E005-10-N','MicroKros','MICROKROS 20CM 5K MPES 1MM MLL X FLL 1/PK','5 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E005-10-S','MicroKros','MICROKROS 20CM 5K MPES 1MM MLL X FLL 1/PK STERILE','5 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E010-05-N','MicroKros','MICROKROS 20CM 10K MPES 0.5MM MLL X FLL 1/PK','10 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,26.0,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E010-05-S','MicroKros','MICROKROS 20CM 10K MPES 0.5MM MLLXFLL 1/PK STERILE','10 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,26.0,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E010-10-N','MicroKros','MICROKROS 20CM 10K MPES 1MM MLL X FLL 1/PK','10 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E010-10-S','MicroKros','MICROKROS 20CM 10K MPES 1MM MLL X FLL 1/PK STERILE','10 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E030-05-N','MicroKros','MICROKROS 20CM 30K MPES 0.5MM MLL X FLL 1/PK','30 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E030-05-S','MicroKros','MICROKROS 20CM 30K MPES 0.5MM MLL X FLL 1/PK STERILE','30 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E030-10-N','MicroKros','MICROKROS 20CM 30K MPES 1MM MLL X FLL 1/PK','30 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E030-10-S','MicroKros','MICROKROS 20CM 30K MPES 1MM MLL X FLL 1/PK STERILE','30 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E050-05-N','MicroKros','MICROKROS 20CM 50K MPES 0.5MM MLL X FLL 1/PK','50 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E050-05-S','MicroKros','MICROKROS 20CM 50K MPES 0.5MM MLL X FLL 1/PK STERILE','50 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E050-10-N','MicroKros','MICROKROS 20CM 50K MPES 1MM MLL X FLL 1/PK','50 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E050-10-S','MicroKros','MICROKROS 20CM 50K MPES 1MM MLL X FLL 1/PK STERILE','50 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E070-05-N','MicroKros','MICROKROS 20CM 70kD MPES 0.5MM MLL X FLL 1/PK','70 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E070-05-S','MicroKros','MICROKROS 20CM 70kD MPES 0.5MM MLL X FLL1/PK STERILE','70 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E070-10-N','MicroKros','MICROKROS 20CM 70K MPES 1MM MLL X FLL 1/PK','70 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E070-10-S','MicroKros','MICROKROS 20CM 70K MPES 1MM MLL X FLL 1/PK STERILE','70 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E100-05-N','MicroKros','MICROKROS 20CM 100K MPES 0.5MM MLL X FLL 1/PK','100 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E100-05-S','MicroKros','MICROKROS 20CM 100K MPES 0.5MM ML X FLL 1/PK STERILE','100 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E100-10-N','MicroKros','','100 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E100-10-S','MicroKros','MICROKROS 20CM100K MPES1MM MLL X FLL 1/PK STERILE','100 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E20U-05-N','MicroKros','MICROKROS 20CM 0.2UM MPES 0.5MM MLL X FLL 1/PK','0.2 μm','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E20U-05-S','MicroKros','MICROKROS 20CM 0.2UM MPES 0.5MM MLL X FLL 1/PK STERILE','0.2 μm','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E20U-10-N','MicroKros','MICROKROS 20CM .2UM MPES .5MM MLLXFLL 1/PK','0.2 μm','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E20U-10-S','MicroKros','MICROKROS 20CM .2UM MPES .5MM MLLXFLL 1/PK STERILE','0.2 μm','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E300-05-N','MicroKros','MICROKROS 20CM 300K MPES 0.5MM MLL X FLL 1/PK','300 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E300-05-S','MicroKros','MICROKROS 20CM 300K MPES 0.5MM MLL X FLL 1/PK STER','300 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E300-10-N','MicroKros','MICROKROS 20CM 300K MPES 1MM MLL X FLL 1/PK','300 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E300-10-S','MicroKros','MICROKROS 20CM 300K MPES 1MM MLL X FLL 1/PK STERILE','300 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E500-05-N','MicroKros','MICROKROS 20CM 500K MPES 0.5MM MLL X FLL 1/PK','500 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E500-05-S','MicroKros','MICROKROS 20CM 500K MPES 0.5MM MLLXFLL 1/PK STERILE','500 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E500-10-N','MicroKros','MICROKROS 20CM 500K MPES 1MM MLL X FLL 1/PK','500 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E500-10-S','MicroKros','MICROKROS 20CM 500K MPES 1MM MLL X FLL 1/PK STERILE','500 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E65U-07-N','MicroKros','MICROKROS 20CM .65UM MPES .75MM MLLXMLL 1/PK','0.65 μm','mPES',0.75,23,'20',3,0.108,46.2,43.0,38.8,15,0.26,0.64,15,45,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E65U-07-S','MicroKros','MICROKROS 20CM .65UM MPES .75MM MLLXMLL STERILE 1/PK','0.65 μm','mPES',0.75,23,'20',3,0.108,46.2,43.0,38.8,15,0.26,0.64,15,45,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E750-05-N','MicroKros','MICROKROS 20CM 750K MPES 0.5MM MLL X FLL 1/PK','750 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E750-05-S','MicroKros','MICROKROS 20CM 750K MPES 0.5MM MLL X FLL 1/PK STERILE','750 kD','mPES',0.5,23,'20',6,0.108,31.5,29.5,27.5,20,0.24,0.67,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E750-10-N','MicroKros','MICROKROS 20CM 750KD MPES 1MM MLLXFLL 1/PK','750 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-E750-10-S','MicroKros','MICROKROS 20CM 750KD MPES 1MM MLLXFLL STERILE 1/PK','750 kD','mPES',1.0,23,'20',2,0.108,57.0,52.5,48.0,13,0.31,0.64,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-M10U-06-N','MicroKros','MICROKROS 20CM 0.1 UM ME 0.63MM MLL X FLL 1/PK','0.1 μm','ME',0.63,23,'20',5,0.108,37.0,34.0,31.0,20,0.31,0.61,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-M10U-06-S','MicroKros','MICROKROS 20CM 0.1 UM ME 0.63MM MLLX FLL1/PK STERILE','0.1 μm','ME',0.63,23,'20',5,0.108,37.0,34.0,31.0,20,0.31,0.61,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-M20U-06-N','MicroKros','MICROKROS 20CM 0.2 UM ME 0.63MM MLL X FLL 1/PK','0.2 μm','ME',0.63,23,'20',5,0.108,37.0,34.0,31.0,20,0.31,0.61,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-M20U-06-S','MicroKros','MICROKROS 20CM0.2 UM ME0.63MM MLL X FLL 1/PK STERILE','0.2 μm','ME',0.63,23,'20',5,0.108,37.0,34.0,31.0,20,0.31,0.61,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-M20U-10-N','MicroKros','MICROKROS 20CM 0.2 UM ME 1MM MLL X FLL 1/PK','0.2 μm','ME',1.0,23,'20',2,0.108,54.0,50.0,46.0,13,0.31,0.69,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-M20U-10-S','MicroKros','MICROKROS 20CM 0.2 UM ME 1MM MLL X FLL 1/PK STERILE','0.2 μm','ME',1.0,23,'20',2,0.108,54.0,50.0,46.0,13,0.31,0.69,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-P20U-05-N','MicroKros','MICROKROS 20CM 0.2 UM PES 0.5MM MLL X FLL 1/PK','0.2 μm','PES',0.5,23,'20',9,0.108,26.6,25.6,24.6,28,0.35,0.6,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-P20U-05-S','MicroKros','MICROKROS 20CM 0.2 UMPES 0.5MM MLL XFLL 1/PK STERILE','0.2 μm','PES',0.5,23,'20',9,0.108,26.6,25.6,24.6,28,0.35,0.6,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-P20U-10-N','MicroKros','MICROKROS 20CM 0.2 UM PES 1MM MLL X FLL 1/PK','0.2 μm','PES',1.0,23,'20',2,0.108,53.1,51.2,49.2,13,0.31,0.67,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-P20U-10-S','MicroKros','MICROKROS 20CM 0.2 UM PES 1MM MLLXFLL 1/PK STERILE','0.2 μm','PES',1.0,23,'20',2,0.108,53.1,51.2,49.2,13,0.31,0.67,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S010-05-N','MicroKros','MICROKROS 20CM 10KD PS 0.5MM MLL X FLL 1/PK','10 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S010-05-P','MicroKros','MICROKROS 20CM 10KD PS 0.5MM MLL X FLL 1/PK WET','10 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S010-05-S','MicroKros','MICROKROS 20CM 10KD PS 0.5MM MLL X FLL 1/PK STERILE','10 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S050-05-N','MicroKros','MICROKROS 20CM 50KD PS 0.5MM MLL X FLL 1/PK','50 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S050-05-P','MicroKros','MICROKROS 20CM 50KD PS 0.5MM MLL X FLL 1/PK WET','50 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S050-05-S','MicroKros','MICROKROS 20CM 50KD PS 0.5MM MLL X FLL 1/PK STERILE','50 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S05U-05-N','MicroKros','MICROKROS 20CM 0.05 UM PS 0.5MM MLL X FLL 1/PK','0.05 μm','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S05U-05-P','MicroKros','MICROKROS 20CM 0.05 UM PS 0.5MM MLL X FLL 1/PK WET','0.05 μm','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S05U-05-S','MicroKros','MICROKROS 20CM 0.05UM PS 0.5MM MLL X FLL1/PK STERILE','0.05 μm','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S500-05-N','MicroKros','MICROKROS 20CM 500KD PS 0.5MM MLL X FLL 1/PK','500 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S500-05-P','MicroKros','MICROKROS 20CM 500KD PS 0.5MM MLL X FLL 1/PK WET','500 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C02-S500-05-S','MicroKros','MICROKROS 20CM 500KD PS 0.5MM MLL X FLL 1/PK STERILE','500 kD','PS',0.5,23,'20',9,0.108,26.8,23.6,20.5,28,0.35,0.69,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E001-05-N','MicroKros','MICROKROS 41.5CM 1K MPES .5MM MLLXFLL 1/PK','1 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E001-05-S','MicroKros','MICROKROS 41.5CM 1K MPES .5MM MLLXFLL 1/PK STERILE','1 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E003-05-N','MicroKros','MICROKROS 41.5CM 3K MPES 0.5MM MLL X FLL 1/PK','3 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E003-05-S','MicroKros','MICROKROS 41.5CM 3K MPES 0.5MM MLL X FLL 1/PK STERILE','3 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E003-10-N','MicroKros','MICROKROS 41.5CM 3K MPES 1MM MLL X FLL 1/PK','3 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E003-10-S','MicroKros','MICROKROS 41.5CM 3K MPES 1MM MLL X FLL 1/PK STERILE','3 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E005-05-N','MicroKros','MICROKROS 41.5CM 5K MPES 0.5MM MLL X FLL 1/PK','5 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E005-05-S','MicroKros','MICROKROS 41.5CM 5K MPES 0.5MM MLL X FLL 1/PK STERILE','5 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E005-10-N','MicroKros','MICROKROS 41.5CM 5K MPES 1MM MLL X FLL 1/PK','5 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E005-10-S','MicroKros','MICROKROS 41.5CM 5K MPES 1MM MLL X FLL 1/PK STERILE','5 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E010-05-N','MicroKros','MICROKROS 41.5CM 10K MPES 0.5MM MLL X FLL 1/PK','10 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,26.0,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E010-05-S','MicroKros','MICROKROS 41.5CM 10K MPES 0.5MM MLL X FLL 1/PK STERILE','10 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,26.0,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E010-10-N','MicroKros','MICROKROS 41.5CM 10K MPES 1MM MLL X FLL 1/PK','10 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E010-10-S','MicroKros','MICROKROS 41.5CM 10K MPES 1MM MLL X FLL 1/PK STERILE','10 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E030-05-N','MicroKros','MICROKROS 41.5CM 30K MPES 0.5MM MLL X FLL 1/PK','30 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E030-05-S','MicroKros','MICROKROS 41.5CM 30K MPES 0.5MM MLL X FLL 1/PK STERILE','30 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E030-10-N','MicroKros','MICROKROS 41.5CM 30K MPES 1MM MLL X FLL 1/PK','30 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E030-10-S','MicroKros','MICROKROS 41.5CM 30K MPES 1MM MLL X FLL 1/PK STERILE','30 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E050-05-N','MicroKros','MICROKROS 41.5CM 50K MPES 0.5MM MLL X FLL 1/PK','50 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E050-05-S','MicroKros','MICROKROS 41.5CM 50K MPES 0.5MM MLL X FLL 1/PK STERILE','50 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E050-10-N','MicroKros','MICROKROS 41.5CM 50K MPES 1MM MLL X FLL 1/PK','50 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E050-10-S','MicroKros','MICROKROS 41.5CM 50K MPES 1MM MLL X FLL 1/PK STERILE','50 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E070-05-N','MicroKros','MICROKROS 41.5CM 70kD MPES 0.5MM MLL X FLL 1/PK','70 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E070-05-S','MicroKros','MICROKROS 41.5CM 70kD MPES 0.5MM MLL X FLL 1/PK STERILE','70 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E070-10-N','MicroKros','MICROKROS 41.5CM 70K MPES 1MM MLL X FLL 1/PK','70 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E070-10-S','MicroKros','MICROKROS 41.5CM 70K MPES 1MM MLL X FLL 1/PK IRR','70 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E100-05-N','MicroKros','MICROKROS 41.5CM 100K MPES 0.5MM MLL X FLL 1/PK','100 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E100-05-S','MicroKros','MICROKROS 41.5CM 100K MPES 0.5MM MLL X FLL 1/PK STERILE','100 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E100-10-N','MicroKros','MICROKROS 41.5CM 100K MPES 1MM MLL X FLL 1/PK','100 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E100-10-S','MicroKros','MICROKROS 41.5CM 100K MPES 1MM MLL X FLL 1/PK STERILE','100 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E20U-05-N','MicroKros','MICROKROS 41.5CM 0.2UM MPES 0.5MM MLL X FLL 1/PK','0.2 μm','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E20U-05-S','MicroKros','MICROKROS 41.5CM 0.2UM MPES 0.5MM MLL X FLL 1/PK STERILE','0.2 μm','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E20U-10-N','MicroKros','MICROKROS 41.5CM .2UM MPES 1MM MLLXFLL 1/PK','0.2 μm','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E20U-10-S','MicroKros','MICROKROS 41.5CM .2UM MPES 1MM MLLXFLL 1/PK STERILE','0.2 μm','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E300-05-N','MicroKros','MICROKROS 41.5CM 300K MPES 0.5MM MLL X FLL 1/PK','300 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E300-05-S','MicroKros','MICROKROS 41.5CM 300K MPES 0.5MM MLL X FLL 1/PK STERILE','300 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E300-10-N','MicroKros','MICROKROS 41.5CM 300K MPES 1MM MLL X FLL 1/PK','300 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E300-10-S','MicroKros','MICROKROS 41.5CM 300K MPES 1MM MLL X FLL 1/PK STERILE','300 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E500-05-N','MicroKros','MICROKROS 41.5CM 500K MPES 0.5MM MLL X FLL 1/PK','500 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E500-05-S','MicroKros','MICROKROS 41.5CM 500K MPES 0.5MM MLL X FLL 1/PK STERILE','500 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E500-10-N','MicroKros','MICROKROS 41.5CM 500K MPES 1MM MLL X FLL 1/PK','500 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E500-10-S','MicroKros','MICROKROS 41.5CM 500K MPES 1MM MLL X FLL 1/PK STERILE','500 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E65U-07-N','MicroKros','MICROKROS 41.5CM .65UM MPES .75MM MLLXMLL 1/PK','0.65 μm','mPES',0.75,45,'41.5',3,0.108,46.2,43.0,38.8,30,0.55,1.32,15,45,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E65U-07-S','MicroKros','MICROKROS 41.5CM .65UM MPES .75MM MLLXMLL STERILE 1/PK','0.65 μm','mPES',0.75,45,'41.5',3,0.108,46.2,43.0,38.8,30,0.55,1.32,15,45,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E750-05-N','MicroKros','MICROKROS 41.5CM 750K MPES 0.5MM MLL X FLL 1/PK','750 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E750-05-S','MicroKros','MICROKROS 41.5CM 750K MPES 0.5MM MLL X FLL 1/PK STERILE','750 kD','mPES',0.5,45,'41.5',6,0.108,31.5,29.5,27.5,40,0.49,1.39,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E750-10-N','MicroKros','MICROKROS 41.5CM 750KD MPES 1MM MLLXFLL 1/PK','750 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-E750-10-S','MicroKros','MICROKROS 41.5CM 750KD MPES 1MM MLLXFLL STERILE 1/PK','750 kD','mPES',1.0,45,'41.5',2,0.108,57.0,52.5,48.0,26,0.65,1.33,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-M10U-06-N','MicroKros','MICROKROS 41.5CM 0.1 UM ME 0.63MM MLL X FLL 1/PK','0.1 μm','ME',0.63,45,'41.5',5,0.108,37.0,34.0,31.0,41,0.65,1.27,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-M10U-06-S','MicroKros','MICROKROS 41.5CM 0.1 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.1 μm','ME',0.63,45,'41.5',5,0.108,37.0,34.0,31.0,41,0.65,1.27,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-M20U-06-N','MicroKros','MICROKROS 41.5CM 0.2 UM ME 0.63MM MLL X FLL 1/PK','0.2 μm','ME',0.63,45,'41.5',5,0.108,37.0,34.0,31.0,41,0.65,1.27,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-M20U-06-S','MicroKros','MICROKROS 41.5CM 0.2 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.2 μm','ME',0.63,45,'41.5',5,0.108,37.0,34.0,31.0,41,0.65,1.27,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-M20U-10-N','MicroKros','MICROKROS 41.5CM 0.2 UM ME 1MM MLL X FLL 1/PK','0.2 μm','ME',1.0,45,'41.5',2,0.108,54.0,50.0,46.0,26,0.65,1.43,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-M20U-10-S','MicroKros','MICROKROS 41.5CM 0.2 UM ME 1MM MLL X FLL 1/PK STERILE','0.2 μm','ME',1.0,45,'41.5',2,0.108,54.0,50.0,46.0,26,0.65,1.43,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-P20U-05-N','MicroKros','MICROKROS 41.5CM 0.2 UM PES 0.5MM MLL X FLL 1/PK','0.2 μm','PES',0.5,45,'41.5',9,0.108,26.6,25.6,24.6,59,0.73,1.25,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-P20U-05-S','MicroKros','MICROKROS 41.5CM 0.2 UM PES 0.5MM MLL X FLL 1/PK STERILE','0.2 μm','PES',0.5,45,'41.5',9,0.108,26.6,25.6,24.6,59,0.73,1.25,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-P20U-10-N','MicroKros','MICROKROS 41.5CM 0.2 UM PES 1MM MLL X FLL 1/PK','0.2 μm','PES',1.0,45,'41.5',2,0.108,53.1,51.2,49.2,26,0.65,1.38,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-P20U-10-S','MicroKros','MICROKROS 41.5CM 0.2 UM PES 1MM MLL X FLL 1/PK STERILE','0.2 μm','PES',1.0,45,'41.5',2,0.108,53.1,51.2,49.2,26,0.65,1.38,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S010-05-N','MicroKros','MICROKROS 41.5CM 10KD PS 0.5MM MLL X FLL 1/PK','10 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S010-05-P','MicroKros','MICROKROS 41.5CM 10KD PS 0.5MM MLL X FLL 1/PK WET','10 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S010-05-S','MicroKros','MICROKROS 41.5CM 10KD PS 0.5MM MLL X FLL 1/PK STERILE','10 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S050-05-N','MicroKros','MICROKROS 41.5CM 50KD PS 0.5MM MLL X FLL 1/PK','50 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S050-05-P','MicroKros','MICROKROS 41.5CM 50KD PS 0.5MM MLL X FLL 1/PK WET','50 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S050-05-S','MicroKros','MICROKROS 41.5CM 50KD PS 0.5MM MLL X FLL 1/PK STERILE','50 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S05U-05-N','MicroKros','MICROKROS 41.5CM 0.05 UM PS 0.5MM MLL X FLL 1/PK','0.05 μm','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S05U-05-P','MicroKros','MICROKROS 41.5CM 0.05 UM PS 0.5MM MLL X FLL 1/PK WET','0.05 μm','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S05U-05-S','MicroKros','MICROKROS 41.5CM 0.05 UM PS 0.5MM MLL X FLL 1/PK STERILE','0.05 μm','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S500-05-N','MicroKros','MICROKROS 41.5CM 500KD PS 0.5MM MLL X FLL 1/PK','500 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S500-05-P','MicroKros','MICROKROS 41.5CM 500KD PS 0.5MM MLL X FLL 1/PK WET','500 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C04-S500-05-S','MicroKros','MICROKROS 41.5CM 500KD PS 0.5MM MLL X FLL 1/PK STERILE','500 kD','PS',0.5,45,'41.5',9,0.108,26.8,23.6,20.5,59,0.73,1.43,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E001-05-N','MicroKros','MICROKROS 65CM 1K MPES .5MM MLLXFLL 1/PK','1 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E001-05-S','MicroKros','MICROKROS 65CM 1K MPES .5MM MLLXFLL 1/PK STERILE','1 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E003-05-N','MicroKros','MICROKROS 65CM 3K MPES 0.5MM MLL X FLL 1/PK','3 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E003-05-S','MicroKros','MICROKROS 65CM 3K MPES 0.5MM MLL X FLL 1/PK STERILE','3 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E003-10-N','MicroKros','MICROKROS 65CM 3K MPES 1MM MLL X FLL 1/PK','3 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E003-10-S','MicroKros','MICROKROS 65CM 3K MPES 1MM MLL X FLL 1/PK STERILE','3 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E005-05-N','MicroKros','MICROKROS 65CM 5K MPES 0.5MM MLL X FLL 1/PK','5 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E005-05-S','MicroKros','MICROKROS 65CM 5K MPES 0.5MM MLL X FLL 1/PK STERILE','5 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E005-10-N','MicroKros','MICROKROS 65CM 5K MPES 1MM MLL X FLL 1/PK','5 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E005-10-S','MicroKros','MICROKROS 65CM 5K MPES 1MM MLL X FLL 1/PK STERILE','5 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E010-05-N','MicroKros','MICROKROS 65CM 10K MPES 0.5MM MLL X FLL 1/PK','10 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,26.0,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E010-05-S','MicroKros','MICROKROS 65CM 10K MPES 0.5MM MLL X FLL 1/PK STERILEIRR','10 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,26.0,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E010-10-N','MicroKros','MICROKROS 65CM 10K MPES 1MM MLL X FLL 1/PK','10 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E010-10-S','MicroKros','MICROKROS 65CM 10K MPES 1MM MLL X FLL 1/PK STERILE','10 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E030-05-N','MicroKros','MICROKROS 65CM 30K MPES 0.5MM MLL X FLL 1/PK','30 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E030-05-S','MicroKros','MICROKROS 65CM 30K MPES 0.5MM MLL X FLL 1/PK STERILE','30 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E030-10-N','MicroKros','MICROKROS 65CM 30K MPES 1MM MLL X FLL 1/PK','30 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E030-10-S','MicroKros','MICROKROS 65CM 30K MPES 1MM MLL X FLL 1/PK STERILE','30 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E050-05-N','MicroKros','MICROKROS 65CM 50K MPES 0.5MM MLL X FLL 1/PK','50 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E050-05-S','MicroKros','MICROKROS 65CM 50K MPES 0.5MM MLL X FLL 1/PK STERILE','50 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E050-10-N','MicroKros','MICROKROS 65CM 50K MPES 1MM MLL X FLL 1/PK','50 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E050-10-S','MicroKros','MICROKROS 65CM 50K MPES 1MM MLL X FLL 1/PK STERILE','50 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E070-05-N','MicroKros','MICROKROS 65CM 70kD MPES 0.5MM MLL X FLL 1/PK','70 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E070-05-S','MicroKros','MICROKROS 65CM 70kD MPES 0.5MM MLL X FLL 1/PK STERILE','70 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E070-10-N','MicroKros','MICROKROS 65CM 70K MPES 1MM MLL X FLL 1/PK','70 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E070-10-S','MicroKros','MICROKROS 65CM 70K MPES 1MM MLL X FLL 1/PK STERILE','70 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E100-05-N','MicroKros','MICROKROS 65CM 100K MPES 0.5MM MLL X FLL 1/PK','100 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E100-05-S','MicroKros','MICROKROS 65CM 100K MPES 0.5MM MLL X FLL 1/PK STERILE','100 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E100-10-N','MicroKros','MICROKROS 65CM 100K MPES 1MM MLL X FLL 1/PK','100 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E100-10-S','MicroKros','MICROKROS 65CM 100K MPES 1MM MLL X FLL 1/PK STERILE','100 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E20U-05-N','MicroKros','MICROKROS 65CM 0.2UM MPES 0.5MM MLL X FLL 1/PK','0.2 μm','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E20U-05-S','MicroKros','MICROKROS 65CM 0.2UM MPES 0.5MM MLL X FLL 1/PK STERILE','0.2 μm','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E20U-10-N','MicroKros','MICROKROS 65CM .2UM MPES 1MM MLLXFLL 1/PK','0.2 μm','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E20U-10-S','MicroKros','MICROKROS 65CM .2UM MPES 1MM MLLXFLL 1/PK STERILE','0.2 μm','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E300-05-N','MicroKros','MICROKROS 65CM 300K MPES 0.5MM MLL X FLL 1/PK','300 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E300-05-S','MicroKros','MICROKROS 65CM 300K MPES 0.5MM MLL X FLL 1/PK STERILE','300 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E300-10-N','MicroKros','MICROKROS 65CM 300K MPES 1MM MLL X FLL 1/PK','300 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E300-10-S','MicroKros','MICROKROS 65CM 300K MPES 1MM MLL X FLL 1/PK STERILE','300 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E500-05-N','MicroKros','MICROKROS 65CM 500K MPES 0.5MM MLL X FLL 1/PK','500 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E500-05-S','MicroKros','MICROKROS 65CM 500K MPES 0.5MM MLL X FLL 1/PK STERILE','500 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E500-10-N','MicroKros','MICROKROS 65CM 500K MPES 1MM MLL X FLL 1/PK','500 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E500-10-S','MicroKros','MICROKROS 65CM 500K MPES 1MM MLL X FLL 1/PK IRR','500 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E65U-07-N','MicroKros','MICROKROS 65CM .65UM MPES .75MM MLLXMLL 1/PK','0.65 μm','mPES',0.75,68,'65',3,0.108,46.2,43.0,38.8,45,0.86,2.07,15,45,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E65U-07-S','MicroKros','MICROKROS 65CM .65UM MPES .75MM MLLXMLL STERILE 1/PK','0.65 μm','mPES',0.75,68,'65',3,0.108,46.2,43.0,38.8,45,0.86,2.07,15,45,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E750-05-N','MicroKros','MICROKROS 65CM 750K MPES 0.5MM MLL X FLL 1/PK','750 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E750-05-S','MicroKros','MICROKROS 65CM 750K MPES 0.5MM MLL X FLL 1/PK STERILE','750 kD','mPES',0.5,68,'65',6,0.108,31.5,29.5,27.5,60,0.77,2.17,9,27,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E750-10-N','MicroKros','MICROKROS 65CM 750KD MPES 1MM MLLXFLL 1/PK','750 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-E750-10-S','MicroKros','MICROKROS 65CM 750KD MPES 1MM MLLXFLL STERILE 1/PK','750 kD','mPES',1.0,68,'65',2,0.108,57.0,52.5,48.0,41,1.02,2.08,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-M10U-06-N','MicroKros','MICROKROS 65CM 0.1 UM ME 0.63MM MLL X FLL 1/PK','0.1 μm','ME',0.63,68,'65',5,0.108,37.0,34.0,31.0,65,1.01,2.0,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-M10U-06-S','MicroKros','MICROKROS 65CM 0.1 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.1 μm','ME',0.63,68,'65',5,0.108,37.0,34.0,31.0,65,1.01,2.0,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-M20U-06-N','MicroKros','MICROKROS 65CM 0.2 UM ME 0.63MM MLL X FLL 1/PK','0.2 μm','ME',0.63,68,'65',5,0.108,37.0,34.0,31.0,65,1.01,2.0,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-M20U-06-S','MicroKros','MICROKROS 65CM 0.2 UM ME 0.63MM MLL X FLL 1/PK STERILE','0.2 μm','ME',0.63,68,'65',5,0.108,37.0,34.0,31.0,65,1.01,2.0,15,44,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-M20U-10-N','MicroKros','MICROKROS 65CM 0.2 UM ME 1MM MLL X FLL 1/PK','0.2 μm','ME',1.0,68,'65',2,0.108,54.0,50.0,46.0,41,1.02,2.25,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-M20U-10-S','MicroKros','MICROKROS 65CM 0.2 UM ME 1MM MLL X FLL 1/PK STERILE','0.2 μm','ME',1.0,68,'65',2,0.108,54.0,50.0,46.0,41,1.02,2.25,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-P20U-05-N','MicroKros','MICROKROS 65CM 0.2 UM PES 0.5MM MLL X FLL 1/PK','0.2 μm','PES',0.5,68,'65',9,0.108,26.6,25.6,24.6,92,1.15,1.96,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-P20U-05-S','MicroKros','MICROKROS 65CM 0.2 UM PES 0.5MM MLL X FLL 1/PK STERILE','0.2 μm','PES',0.5,68,'65',9,0.108,26.6,25.6,24.6,92,1.15,1.96,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-P20U-10-N','MicroKros','MICROKROS 65CM 0.2 UM PES 1MM MLL X FLL 1/PK','0.2 μm','PES',1.0,68,'65',2,0.108,53.1,51.2,49.2,41,1.02,2.17,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-P20U-10-S','MicroKros','MICROKROS 65CM 0.2 UM PES 1MM MLL X FLL 1/PK STERILE','0.2 μm','PES',1.0,68,'65',2,0.108,53.1,51.2,49.2,41,1.02,2.17,24,71,14,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S010-05-N','MicroKros','MICROKROS 65CM 10KD PS 0.5MM MLL X FLL 1/PK','10 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S010-05-P','MicroKros','MICROKROS 65CM 10KD PS 0.5MM MLL X FLL 1/PK WET','10 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S010-05-S','MicroKros','MICROKROS 65CM 10KD PS 0.5MM MLL X FLL 1/PK STERILE','10 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S050-05-N','MicroKros','MICROKROS 65CM 50KD PS 0.5MM MLL X FLL 1/PK','50 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S050-05-P','MicroKros','MICROKROS 65CM 50KD PS 0.5MM MLL X FLL 1/PK WET','50 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S050-05-S','MicroKros','MICROKROS 65CM 50KD PS 0.5MM MLL X FLL 1/PK STERILE','50 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S05U-05-N','MicroKros','MICROKROS 65CM 0.05 UM PS 0.5MM MLL X FLL 1/PK','0.05 μm','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S05U-05-P','MicroKros','MICROKROS 65CM 0.05 UM PS 0.5MM MLL X FLL 1/PK WET','0.05 μm','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S05U-05-S','MicroKros','MICROKROS 65CM 0.05 UM PS 0.5MM MLL X FLL 1/PK STERILE','0.05 μm','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S500-05-N','MicroKros','MICROKROS 65CM 500KD PS 0.5MM MLL X FLL 1/PK','500 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S500-05-P','MicroKros','MICROKROS 65CM 500KD PS 0.5MM MLL X FLL 1/PK WET','500 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('C06-S500-05-S','MicroKros','MICROKROS 65CM 500KD PS 0.5MM MLL X FLL 1/PK STERILE','500 kD','PS',0.5,68,'65',9,0.108,26.8,23.6,20.5,92,1.15,2.24,13,40,13,14,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E001-05-N','MidiKros','MIDIKROS 20CM 1K MPES .5MM FLLXFLL 1/PK','1 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E001-05-S','MidiKros','MIDIKROS 20CM 1K MPES .5MM FLLXFLL 1/PK STERILE','1 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E003-05-N','MidiKros','MIDIKROS 20CM 3K MPES 0.5MM FLL X FLL 1/PK','3 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E003-05-S','MidiKros','MIDIKROS 20CM 3K MPES 0.5MM FLL X FLL 1/PK STERILE','3 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E003-10-N','MidiKros','MIDIKROS 20CM 3K MPES 1MM FLL X FLL 1/PK','3 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E003-10-S','MidiKros','MIDIKROS 20CM 3K MPES 1MM FLL X FLL 1/PK STERILE','3 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E005-05-N','MidiKros','MIDIKROS 20CM 5K MPES 0.5MM FLL X FLL 1/PK','5 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E005-05-S','MidiKros','MIDIKROS 20CM 5K MPES 0.5MM FLL X FLL 1/PK STERILE','5 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E005-10-N','MidiKros','MIDIKROS 20CM 5K MPES 1MM FLL X FLL 1/PK','5 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E005-10-S','MidiKros','MIDIKROS 20CM 5K MPES 1MM FLL X FLL 1/PK STERILE','5 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E010-05-N','MidiKros','MIDIKROS 20CM 10K MPES 0.5MM FLL X FLL 1/PK','10 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,26.0,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E010-05-S','MidiKros','MIDIKROS 20CM 10K MPES 0.5MM FLL X FLL 1/PK STERILE','10 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,26.0,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E010-10-N','MidiKros','MIDIKROS 20CM 10K MPES 1MM FLL X FLL 1/PK','10 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E010-10-S','MidiKros','MIDIKROS 20CM 10K MPES 1MM FLL X FLL 1/PK STERILE','10 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E030-05-N','MidiKros','MIDIKROS 20CM 30K MPES 0.5MM FLL X FLL 1/PK','30 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E030-05-S','MidiKros','MIDIKROS 20CM 30K MPES 0.5MM FLL X FLL 1/PK STERILE','30 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E030-10-N','MidiKros','MIDIKROS 20CM 30K MPES 1MM FLL X FLL 1/PK','30 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E030-10-S','MidiKros','MIDIKROS 20CM 30K MPES 1MM FLL X FLL 1/PK STERILE','30 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E050-05-N','MidiKros','MIDIKROS 20CM 50K MPES 0.5MM FLL X FLL 1/PK','50 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E050-05-S','MidiKros','MIDIKROS 20CM 50K MPES 0.5MM FLL X FLL 1/PK STERILE','50 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E050-10-N','MidiKros','MIDIKROS 20CM 50K MPES 1MM FLL X FLL 1/PK','50 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E050-10-S','MidiKros','MIDIKROS 20CM 50K MPES 1MM FLL X FLL 1/PK STERILE','50 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E070-05-N','MidiKros','MIDIKROS 20CM 70K MPES 0.5MM FLL X FLL 1/PK','70 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E070-05-S','MidiKros','MIDIKROS 20CM 70K MPES 0.5MM FLL X FLL 1/PK STERILE','70 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E070-10-N','MidiKros','MIDIKROS 20CM 70K MPES 1MM FLL X FLL 1/PK','70 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E070-10-S','MidiKros','MIDIKROS 20CM 70K MPES 1MM FLL X FLL 1/PK STERILE','70 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E100-05-N','MidiKros','MIDIKROS 20CM 100K MPES 0.5MM FLL X FLL 1/PK','100 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E100-05-S','MidiKros','MIDIKROS 20CM 100K MPES 0.5MM FLL X FLL 1/PK STERILE','100 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E100-10-N','MidiKros','MIDIKROS 20CM 100K MPES 1MM FLL X FLL 1/PK','100 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E100-10-S','MidiKros','MIDIKROS 20CM 100K MPES 1MM FLL X FLL 1/PK STERILE','100 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E20U-05-N','MidiKros','MIDIKROS 20CM 0.2UM MPES 0.5MM FLL X FLL 1/PK','0.2 μm','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E20U-05-S','MidiKros','MIDIKROS 20CM 0.2UM MPES 0.5MM FLL X FLL 1/PK STERILE','0.2 μm','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E20U-10-N','MidiKros','MIDIKROS 20CM .2UM MPES 1MM FLLXFLL 1/PK','0.2 μm','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E20U-10-S','MidiKros','MIDIKROS 20CM .2UM MPES 1MM FLLXFLL 1/PK STERILE','0.2 μm','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E300-05-N','MidiKros','MIDIKROS 20CM 300K MPES 0.5MM FLL X FLL 1/PK','300 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E300-05-S','MidiKros','MIDIKROS 20CM 300K MPES 0.5MM FLLXFLL 1/PK STERILE','300 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E300-10-N','MidiKros','MIDIKROS 20CM 300K MPES 1MM FLL X FLL 1/PK','300 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E300-10-S','MidiKros','MIDIKROS 20CM 300K MPES 1MM FLL X FLL 1/PK STERILE','300 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E500-05-N','MidiKros','MIDIKROS 20CM 500K MPES 0.5MM FLL X FLL 1/PK','500 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E500-05-S','MidiKros','MIDIKROS 20CM 500K MPES 0.5MM FLL X FLL 1/PK STERILE','500 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E500-10-N','MidiKros','MIDIKROS 20CM 500K MPES 1MM FLL X FLL 1/PK','500 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E500-10-S','MidiKros','MIDIKROS 20CM 500K MPES 1MM FLL X FLL 1/PK STERILE','500 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E65U-07-N','MidiKros','MIDIKROS 20CM .65UM MPES .75MM FLLXFLL 1/PK','0.65 μm','mPES',0.75,25,'20',18,0.275,46.2,43.0,38.8,85,1.59,4.39,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E65U-07-S','MidiKros','MIDIKROS 20CM .65UM MPES .75MM FLLXFLL STERILE 1/PK','0.65 μm','mPES',0.75,25,'20',18,0.275,46.2,43.0,38.8,85,1.59,4.39,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E750-05-N','MidiKros','MIDIKROS 20CM 750K MPES 0.5MM FLL X FLL 1/PK','750 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E750-05-S','MidiKros','MIDIKROS 20CM 750K MPES 0.5MM FLL X FLL 1/PK STERILE','750 kD','mPES',0.5,25,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E750-10-N','MidiKros','MIDIKROS 20CM 750KD MPES 1MM FLLXFLL 1/PK','750 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-E750-10-S','MidiKros','MIDIKROS 20CM 750KD MPES 1MM FLLXFLL STERILE 1/PK','750 kD','mPES',1.0,25,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-M10U-06-N','MidiKros','MIDIKROS 20CM 0.1UM ME 0.6MM FLL X FLL 1/PK','0.1 μm','ME',0.63,25,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-M10U-06-S','MidiKros','MIDIKROS 20CM 0.1UM ME0.6MM FLLXFLL 1/ PK STERILE','0.1 μm','ME',0.63,25,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-M20U-06-N','MidiKros','MIDIKROS 20CM 0.2UM ME 0.6MM FLL X FLL 1/PK','0.2 μm','ME',0.63,25,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-M20U-06-S','MidiKros','MIDIKROS 20CM 0.2UM ME 0.6MM FLL X FLL1/PK STERILE','0.2 μm','ME',0.63,25,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-M20U-10-N','MidiKros','MIDIKROS 20CM 0.2UM ME 1MM FLL X FLL 1/PK','0.2 μm','ME',1.0,25,'20',15,0.275,54.0,50.0,46.0,94,2.36,3.98,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-M20U-10-S','MidiKros','MIDIKROS 20CM 0.2UM ME 1MM FLL X FLL 1/ PK STERILE','0.2 μm','ME',1.0,25,'20',15,0.275,54.0,50.0,46.0,94,2.36,3.98,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-P20U-05-N','MidiKros','MIDIKROS 20CM 0.2UM PES 0.5MM FLL X FLL 1/PK','0.2 μm','PES',0.5,25,'20',45,0.275,26.6,25.6,24.6,140,1.77,4.77,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-P20U-05-S','MidiKros','MIDIKROS 20CM 0.2UM PES 0.5MM FLL X FLL 1/PK STERILE','0.2 μm','PES',0.5,25,'20',45,0.275,26.6,25.6,24.6,140,1.77,4.77,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-P20U-10-N','MidiKros','MIDIKROS 20CM 0.2UM PES 1MM FLL X FLL 1/PK','0.2 μm','PES',1.0,25,'20',14,0.275,53.1,51.2,49.2,88,2.2,4.06,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-P20U-10-S','MidiKros','MIDIKROS 20CM 0.2UM PES 1MM FLL X FLL 1/PK STERILE','0.2 μm','PES',1.0,25,'20',14,0.275,53.1,51.2,49.2,88,2.2,4.06,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S010-05-N','MidiKros','MIDIKROS 20CM 10KD PS 0.5MM FLL X FLL 1/PK','10 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S010-05-P','MidiKros','MIDIKROS 20CM 10KD PS 0.5MM FLL X FLL 1/PK WET','10 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S010-05-S','MidiKros','MIDIKROS 20CM 10KD PS 0.5MM FLL X FLL 1/PK STERILE','10 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S050-05-N','MidiKros','MIDIKROS 20CM 50KD PS 0.5MM FLL X FLL 1/PK','50 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S050-05-P','MidiKros','MIDIKROS 20CM 50KD PS 0.5MM FLL X FLL 1/PK WET','50 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S050-05-S','MidiKros','MIDIKROS 20CM 50KD PS 0.5MM FLL X FLL 1/PK STERILE','50 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S05U-05-N','MidiKros','MIDIKROS 20CM 0.05UM PS 0.5MM FLL X FLL 1/PK','0.05 μm','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S05U-05-P','MidiKros','MIDIKROS 20CM 0.05UM PS 0.5MM FLL X FLL 1/PK WET','0.05 μm','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S05U-05-S','MidiKros','MIDIKROS 20CM 0.05 UM PS 0.5MM FLL X FLL 1/PK STERILE','0.05 μm','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S500-05-N','MidiKros','MIDIKROS 20CM 500KD PS 0.5MM FLL X FLL 1/PK','500 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S500-05-P','MidiKros','MIDIKROS 20CM 500KD PS 0.5MM FLL X FLL 1/PK WET','500 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D02-S500-05-S','MidiKros','MIDIKROS 20CM 500KD PS 0.5MM FLL X FLL 1/PK STERILE','500 kD','PS',0.5,25,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E001-05-N','MidiKros','MIDIKROS 41.5CM 1K MPES .5MM FLLXFLL 1/PK','1 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E001-05-S','MidiKros','MIDIKROS 41.5CM 1K MPES .5MM FLLXFLL 1/PK STERILE','1 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E003-05-N','MidiKros','MIDIKROS 41.5CM 3K MPES 0.5MM FLL X FLL 1/PK','3 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E003-05-S','MidiKros','MIDIKROS 41.5CM 3K MPES 0.5MM FLL X FLL 1/PK STERILE','3 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E003-10-N','MidiKros','MIDIKROS 41.5CM 3K MPES 1MM FLL X FLL 1/PK','3 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E003-10-S','MidiKros','MIDIKROS 41.5CM 3K MPES 1MM FLL X FLL 1/PK STERILE','3 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E005-05-N','MidiKros','MIDIKROS 41.5CM 5K MPES 0.5MM FLL X FLL 1/PK','5 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E005-05-S','MidiKros','MIDIKROS 41.5CM 5K MPES 0.5MM FLL X FLL 1/PK STERILE','5 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E005-10-N','MidiKros','MIDIKROS 41.5CM 5K MPES 1MM FLL X FLL 1/PK','5 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E005-10-S','MidiKros','MIDIKROS 41.5CM 5K MPES 1MM FLL X FLL 1/PK STERILE','5 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E010-05-N','MidiKros','MIDIKROS 41.5CM 10K MPES 0.5MM FLL X FLL 1/PK','10 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,26.0,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E010-05-S','MidiKros','MIDIKROS 41.5CM 10K MPES 0.5MM FLL X FLL 1/PK STERILE','10 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,26.0,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E010-10-N','MidiKros','MIDIKROS 41.5CM 10K MPES 1MM FLL X FLL 1/PK','10 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E010-10-S','MidiKros','MIDIKROS 41.5CM 10K MPES 1MM FLL X FLL 1/PK STERILE','10 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E030-05-N','MidiKros','MIDIKROS 41.5CM 30K MPES 0.5MM FLL X FLL 1/PK','30 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E030-05-S','MidiKros','MIDIKROS 41.5CM 30K MPES 0.5MM FLL X FLL 1/PK STERILE','30 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E030-10-N','MidiKros','MIDIKROS 41.5CM 30K MPES 1MM FLL X FLL 1/PK','30 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E030-10-S','MidiKros','MIDIKROS 41.5CM 30K MPES 1MM FLL X FLL 1/PK STERILE','30 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E050-05-N','MidiKros','MIDIKROS 41.5CM 50K MPES 0.5MM FLL X FLL 1/PK','50 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E050-05-S','MidiKros','MIDIKROS 41.5CM 50K MPES 0.5MM FLL X FLL 1/PK STERILE','50 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E050-10-N','MidiKros','MIDIKROS 41.5CM 50K MPES 1MM FLL X FLL 1/PK','50 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E050-10-S','MidiKros','MIDIKROS 41.5CM 50K MPES 1MM FLL X FLL 1/PK STERILE','50 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E070-05-N','MidiKros','MIDIKROS 41.5CM 70K MPES 0.5MM FLL X FLL 1/PK','70 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E070-05-S','MidiKros','MIDIKROS 41.5CM 70K MPES 0.5MM FLL X FLL 1/PK STERILE','70 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E070-10-N','MidiKros','MIDIKROS 41.5CM 70K MPES 1MM FLL X FLL 1/PK','70 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E070-10-S','MidiKros','MIDIKROS 41.5CM 70K MPES 1MM FLL X FLL 1/PK STERILE','70 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E100-05-N','MidiKros','MIDIKROS 41.5CM 100K MPES 0.5MM FLL X FLL 1/PK','100 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E100-05-S','MidiKros','MIDIKROS 41.5CM 100K MPES 0.5MM FLL X FLL 1/PK STERILE','100 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E100-10-N','MidiKros','MIDIKROS 41.5CM 100K MPES 1MM FLL X FLL 1/PK','100 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E100-10-S','MidiKros','MIDIKROS 41.5CM 100K MPES 1MM FLL X FLL 1/PK STERILE','100 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E20U-05-N','MidiKros','MIDIKROS 41.5CM 0.2UM MPES 0.5MM FLL X FLL 1/PK','0.2 μm','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E20U-05-S','MidiKros','MIDIKROS 41.5CM 0.2UM MPES 0.5MM FLL X FLL 1/PK STERILE','0.2 μm','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E20U-10-N','MidiKros','MIDIKROS 41.5CM .2UM MPES 1MM FLLXFLL 1/PK','0.2 μm','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E20U-10-S','MidiKros','MIDIKROS 41.5CM .2UM MPES 1MM FLLXFLL 1/PK STERILE','0.2 μm','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E300-05-N','MidiKros','MIDIKROS 41.5CM 300K MPES 0.5MM FLL X FLL 1/PK','300 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E300-05-S','MidiKros','MIDIKROS 41.5CM 300K MPES 0.5MM FLL X FLL 1/PK STERILE','300 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E300-10-N','MidiKros','MIDIKROS 41.5CM 300K MPES 1MM FLL X FLL 1/PK','300 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E300-10-S','MidiKros','MIDIKROS 41.5CM 300K MPES 1MM FLL X FLL 1/PK STERILE','300 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E500-05-N','MidiKros','MIDIKROS 41.5CM 500K MPES 0.5MM FLL X FLL 1/PK','500 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E500-05-S','MidiKros','MIDIKROS 41.5CM 500K MPES 0.5MM FLL X FLL 1/PK STERILE','500 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E500-10-N','MidiKros','MIDIKROS 41.5CM 500K MPES 1MM FLL X FLL 1/PK','500 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E500-10-S','MidiKros','MIDIKROS 41.5CM 500K MPES 1MM FLL X FLL 1/PK STERILE','500 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E65U-07-N','MidiKros','MIDIKROS 41.5CM .65UM MPES .75MM FLLXFLL 1/PK','0.65 μm','mPES',0.75,45,'41.5',18,0.275,46.2,43.0,38.8,175,3.3,9.12,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E65U-07-S','MidiKros','MIDIKROS 41.5CM .65UM MPES .75MM FLLXFLL STERILE 1/PK','0.65 μm','mPES',0.75,45,'41.5',18,0.275,46.2,43.0,38.8,175,3.3,9.12,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E750-05-N','MidiKros','MIDIKROS 41.5CM 750K MPES 0.5MM FLL X FLL 1/PK','750 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E750-05-S','MidiKros','MIDIKROS 41.5CM 750K MPES 0.5MM FLL X FLL 1/PK STERILE','750 kD','mPES',0.5,45,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E750-10-N','MidiKros','MIDIKROS 41.5CM 750KD MPES 1MM FLLXFLL 1/PK','750 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-E750-10-S','MidiKros','MIDIKROS 41.5CM 750KD MPES 1MM FLLXFLL STERILE 1/PK','750 kD','mPES',1.0,45,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-M10U-06-N','MidiKros','MIDIKROS 41.5CM 0.1UM ME 0.6MM FLL X FLL 1/PK','0.1 μm','ME',0.63,45,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-M10U-06-S','MidiKros','MIDIKROS 41.5CM 0.1UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.1 μm','ME',0.63,45,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-M20U-06-N','MidiKros','MIDIKROS 41.5CM 0.2UM ME 0.6MM FLL X FLL 1/PK','0.2 μm','ME',0.63,45,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-M20U-06-S','MidiKros','MIDIKROS 41.5CM 0.2UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.2 μm','ME',0.63,45,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-M20U-10-N','MidiKros','MIDIKROS 41.5CM 0.2UM ME 1MM FLL X FLL 1/PK','0.2 μm','ME',1.0,45,'41.5',15,0.275,54.0,50.0,46.0,195,4.89,8.26,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-M20U-10-S','MidiKros','MIDIKROS 41.5CM 0.2UM ME 1MM FLL X FLL 1/ PK STERILE','0.2 μm','ME',1.0,45,'41.5',15,0.275,54.0,50.0,46.0,195,4.89,8.26,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-P20U-05-N','MidiKros','MIDIKROS 41.5CM 0.2UM PES 0.5MM FLL X FLL 1/PK','0.2 μm','PES',0.5,45,'41.5',45,0.275,26.6,25.6,24.6,290,3.66,9.89,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-P20U-05-S','MidiKros','MIDIKROS 41.5CM 0.2UM PES 0.5MM FLL X FLL 1/PK STERILE','0.2 μm','PES',0.5,45,'41.5',45,0.275,26.6,25.6,24.6,290,3.66,9.89,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-P20U-10-N','MidiKros','MIDIKROS 41.5CM 0.2UM PES 1MM FLL X FLL 1/PK','0.2 μm','PES',1.0,45,'41.5',14,0.275,53.1,51.2,49.2,180,4.56,8.42,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-P20U-10-S','MidiKros','MIDIKROS 41.5CM 0.2UM PES 1MM FLL X FLL 1/PK STERILE','0.2 μm','PES',1.0,45,'41.5',14,0.275,53.1,51.2,49.2,180,4.56,8.42,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S010-05-N','MidiKros','MIDIKROS 41.5CM 10KD PS 0.5MM FLL X FLL 1/PK','10 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S010-05-P','MidiKros','MIDIKROS 41.5CM 10KD PS 0.5MM FLL X FLL 1/PK WET','10 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S010-05-S','MidiKros','MIDIKROS 41.5CM 10KD PS 0.5MM FLL X FLL 1/PK STERILE','10 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S050-05-N','MidiKros','MIDIKROS 41.5CM 50KD PS 0.5MM FLL X FLL 1/PK','50 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S050-05-P','MidiKros','MIDIKROS 41.5CM 50KD PS 0.5MM FLL X FLL 1/PK WET','50 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S050-05-S','MidiKros','MIDIKROS 41.5CM 50KD PS 0.5MM FLL X FLL 1/PK STERILE','50 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S05U-05-N','MidiKros','MIDIKROS 41.5CM 0.05UM PS 0.5MM FLL X FLL 1/PK','0.05 μm','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S05U-05-P','MidiKros','MIDIKROS 41.5CM 0.05UM PS 0.5MM FLL X FLL 1/PK WET','0.05 μm','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S05U-05-S','MidiKros','MIDIKROS 41.5CM 0.05UM PS 0.5MM FLL X FLL 1/PK STERILE','0.05 μm','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S500-05-N','MidiKros','MIDIKROS 41.5CM 500KD PS 0.5MM FLL X FLL 1/PK','500 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S500-05-P','MidiKros','MIDIKROS 41.5CM 500KD PS 0.5MM FLL X FLL 1/PK WET','500 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D04-S500-05-S','MidiKros','MIDIKROS 41.5CM 500KD PS 0.5MM FLL X FLL 1/PK STERILE','500 kD','PS',0.5,45,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E001-05-N','MidiKros','MIDIKROS 65CM 1K MPES .5MM FLLXFLL 1/PK','1 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E001-05-S','MidiKros','MIDIKROS 65CM 1K MPES .5MM FLLXFLL 1/PK STERILE','1 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E003-05-N','MidiKros','MIDIKROS 65CM 3K MPES 0.5MM FLL X FLL 1/PK','3 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E003-05-S','MidiKros','MIDIKROS 65CM 3K MPES 0.5MM FLL X FLL 1/PK STERILE','3 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E003-10-N','MidiKros','MIDIKROS 65CM 3K MPES 1MM FLL X FLL 1/PK','3 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E003-10-S','MidiKros','MIDIKROS 65CM 3K MPES 1MM FLL X FLL 1/PK STERILE','3 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E005-05-N','MidiKros','MIDIKROS 65CM 5K MPES 0.5MM FLL X FLL 1/PK','5 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E005-05-S','MidiKros','MIDIKROS 65CM 5K MPES 0.5MM FLL X FLL 1/PK STERILE','5 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E005-10-N','MidiKros','MIDIKROS 65CM 5K MPES 1MM FLL X FLL 1/PK','5 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E005-10-S','MidiKros','MIDIKROS 65CM 5K MPES 1MM FLL X FLL 1/PK STERILE','5 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E010-05-N','MidiKros','MIDIKROS 65CM 10K MPES 0.5MM FLL X FLL 1/PK','10 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,26.0,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E010-05-S','MidiKros','MIDIKROS 65CM 10K MPES 0.5MM FLL X FLL 1/PK STERILE','10 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,26.0,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E010-10-N','MidiKros','MIDIKROS 65CM 10K MPES 1MM FLL X FLL 1/PK','10 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E010-10-S','MidiKros','MIDIKROS 65CM 10K MPES 1MM FLL X FLL 1/PK STERILE','10 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E030-05-N','MidiKros','MIDIKROS 65CM 30K MPES 0.5MM FLL X FLL 1/PK','30 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E030-05-S','MidiKros','MIDIKROS 65CM 30K MPES 0.5MM FLL X FLL 1/PK STERILE','30 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E030-10-N','MidiKros','MIDIKROS 65CM 30K MPES 1MM FLL X FLL 1/PK','30 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E030-10-S','MidiKros','MIDIKROS 65CM 30K MPES 1MM FLL X FLL 1/PK STERILE','30 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E050-05-N','MidiKros','MIDIKROS 65CM 50K MPES 0.5MM FLL X FLL 1/PK','50 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E050-05-S','MidiKros','MIDIKROS 65CM 50K MPES 0.5MM FLL X FLL 1/PK STERILE','50 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E050-10-N','MidiKros','MIDIKROS 65CM 50K MPES 1MM FLL X FLL 1/PK','50 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E050-10-S','MidiKros','MIDIKROS 65CM 50K MPES 1MM FLL X FLL 1/PK STERILE','50 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E070-05-N','MidiKros','MIDIKROS 65CM 70K MPES 0.5MM FLL X FLL 1/PK','70 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E070-05-S','MidiKros','MIDIKROS 65CM 70K MPES 0.5MM FLL X FLL 1/PK STERILE','70 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E070-10-N','MidiKros','MIDIKROS 65CM 70K MPES 1MM FLL X FLL 1/PK','70 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E070-10-S','MidiKros','MIDIKROS 65CM 70K MPES 1MM FLL X FLL 1/PK STERILE','70 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E100-05-N','MidiKros','MIDIKROS 65CM 100K MPES 0.5MM FLL X FLL 1/PK','100 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E100-05-S','MidiKros','MIDIKROS 65CM 100K MPES 0.5MM FLL X FLL 1/PK STERILE','100 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E100-10-N','MidiKros','MIDIKROS 65CM 100K MPES 1MM FLL X FLL 1/PK','100 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E100-10-S','MidiKros','MIDIKROS 65CM 100K MPES 1MM FLL X FLL 1/PK STERILE','100 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E20U-05-N','MidiKros','MIDIKROS 65CM 0.2UM MPES 0.5MM FLL X FLL 1/PK','0.2 μm','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E20U-05-S','MidiKros','MIDIKROS 65CM 0.2UM MPES 0.5MM FLL X FLL 1/PK STERILE','0.2 μm','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E20U-10-N','MidiKros','MIDIKROS 65CM .2UM MPES 1MM FLLXFLL 1/PK','0.2 μm','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E20U-10-S','MidiKros','MIDIKROS 65CM .2UM MPES 1MM FLLXFLL 1/PK STERILE','0.2 μm','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E300-05-N','MidiKros','MIDIKROS 65CM 300K MPES 0.5MM FLL X FLL 1/PK','300 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E300-05-S','MidiKros','MIDIKROS 65CM 300K MPES 0.5MM FLL X FLL 1/PK STERILE','300 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E300-10-N','MidiKros','MIDIKROS 65CM 300K MPES 1MM FLL X FLL 1/PK','300 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E300-10-S','MidiKros','MIDIKROS 65CM 300K MPES 1MM FLL X FLL 1/PK STERILE','300 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E500-05-N','MidiKros','MIDIKROS 65CM 500K MPES 0.5MM FLL X FLL 1/PK','500 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E500-05-S','MidiKros','MIDIKROS 65CM 500K MPES 0.5MM FLL X FLL 1/PK STERILE','500 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E500-10-N','MidiKros','MIDIKROS 65CM 500K MPES 1MM FLL X FLL 1/PK','500 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E500-10-S','MidiKros','MIDIKROS 65CM 500K MPES 1MM FLL X FLL 1/PK STERILE','500 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E65U-07-N','MidiKros','MIDIKROS 65CM .65UM MPES .75MM FLLXFLL 1/PK','0.65 μm','mPES',0.75,69,'65',18,0.275,46.2,43.0,38.8,275,5.17,14.28,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E65U-07-S','MidiKros','MIDIKROS 65CM .65UM MPES .75MM FLLXFLL STERILE 1/PK','0.65 μm','mPES',0.75,69,'65',18,0.275,46.2,43.0,38.8,275,5.17,14.28,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E750-05-N','MidiKros','MIDIKROS 65CM 750K MPES 0.5MM FLL X FLL 1/PK','750 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E750-05-S','MidiKros','MIDIKROS 65CM 750K MPES 0.5MM FLL X FLL 1/PK STERILE','750 kD','mPES',0.5,69,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E750-10-N','MidiKros','MIDIKROS 65CM 750KD MPES 1MM FLLXFLL 1/PK','750 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-E750-10-S','MidiKros','MIDIKROS 65CM 750KD MPES 1MM FLLXFLL STERILE 1/PK','750 kD','mPES',1.0,69,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-M10U-06-N','MidiKros','MIDIKROS 65CM 0.1UM ME 0.6MM FLL X FLL 1/PK','0.1 μm','ME',0.63,69,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-M10U-06-S','MidiKros','MIDIKROS 65CM 0.1UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.1 μm','ME',0.63,69,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-M20U-06-N','MidiKros','MIDIKROS 65CM 0.2um ME 0.6MM FLL X FLL 1/PK','0.2 μm','ME',0.63,69,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-M20U-06-S','MidiKros','MIDIKROS 65CM 0.2UM ME 0.6MM FLL X FLL 1/ PK STERILE','0.2 μm','ME',0.63,69,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-M20U-10-N','MidiKros','MIDIKROS 65CM 0.2UM ME 1MM FLL X FLL 1/PK','0.2 μm','ME',1.0,69,'65',15,0.275,54.0,50.0,46.0,305,7.65,12.94,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-M20U-10-S','MidiKros','MIDIKROS 65CM 0.2UM ME 1MM FLL X FLL 1/ PK STERILE','0.2 μm','ME',1.0,69,'65',15,0.275,54.0,50.0,46.0,305,7.65,12.94,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-P20U-05-N','MidiKros','MIDIKROS 65CM 0.2UM PES 0.5MM FLL X FLL 1/PK','0.2 μm','PES',0.5,69,'65',45,0.275,26.6,25.6,24.6,460,5.74,15.49,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-P20U-05-S','MidiKros','MIDIKROS 65CM 0.2UM PES 0.5MM FLL X FLL 1/PK STERILE','0.2 μm','PES',0.5,69,'65',45,0.275,26.6,25.6,24.6,460,5.74,15.49,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-P20U-10-N','MidiKros','MIDIKROS 65CM 0.2UM PES 1MM FLL X FLL 1/PK','0.2 μm','PES',1.0,69,'65',14,0.275,53.1,51.2,49.2,290,7.14,13.19,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-P20U-10-S','MidiKros','MIDIKROS 65CM 0.2 UM PES 1MM FLL X FLL 1/PK STERILE','0.2 μm','PES',1.0,69,'65',14,0.275,53.1,51.2,49.2,290,7.14,13.19,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S010-05-N','MidiKros','MIDIKROS 65CM 10KD PS 0.5MM FLL X FLL 1/PK','10 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S010-05-P','MidiKros','MIDIKROS 65CM 10KD PS 0.5MM FLL X FLL 1/PK WET','10 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S010-05-S','MidiKros','MIDIKROS 65CM 10KD PS 0.5MM FLL X FLL 1/PK STERILE','10 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S050-05-N','MidiKros','MIDIKROS 65CM 50KD PS 0.5MM FLL X FLL 1/PK','50 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S050-05-P','MidiKros','MIDIKROS 65CM 50KD PS 0.5MM FLL X FLL 1/PK WET','50 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S050-05-S','MidiKros','MIDIKROS 65CM 50KD PS 0.5MM FLL X FLL 1/PK STERILE','50 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S05U-05-N','MidiKros','MIDIKROS 65CM 0.05UM PS 0.5MM FLL X FLL 1/PK','0.05 μm','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S05U-05-P','MidiKros','MIDIKROS 65CM 0.05UM PS 0.5MM FLL X FLL 1/PK WET','0.05 μm','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S05U-05-S','MidiKros','MIDIKROS 65CM 0.05UM PS 0.5MM FLL X FLL 1/PK STERILE','0.05 μm','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S500-05-N','MidiKros','MIDIKROS 65CM 500KD PS 0.5MM FLL X FLL 1/PK','500 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S500-05-P','MidiKros','MIDIKROS 65CM 500KD PS 0.5MM FLL X FLL 1/PK WET','500 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('D06-S500-05-S','MidiKros','MIDIKROS 65CM 500KD PS 0.5MM FLL X FLL 1/PK STERILE','500 kD','PS',0.5,69,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E001-05-N','KrosFlo','KROSFLO 20CM 1K MPES .5MM 3TCX1.5TC 1/PK','1 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E001-05-S','KrosFlo','KROSFLO 20CM 1K MPES .5MM 3TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E003-05-N','KrosFlo','KROSFLO 20CM 3K MPES 0.5MM 3TC X 1.5TC','3 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E003-05-S','KrosFlo','KROSFLO 20CM 3K MPES 0.5MM 3TC X 1.5TC STERILE','3 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E003-10-N','KrosFlo','KROSFLO 20CM 3K MPES 1MM 3TC X 1.5TC','3 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E003-10-S','KrosFlo','KROSFLO 20CM 3K MPES 1MM 3TC X 1.5TC STERILE','3 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E005-05-N','KrosFlo','KROSFLO 20CM 5K MPES 0.5MM 3TC X 1.5TC','5 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E005-05-S','KrosFlo','KROSFLO 20CM 5K MPES 0.5MM 3TC X 1.5TC STERILE','5 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E005-10-N','KrosFlo','KROSFLO 20CM 5K MPES 1MM 3TC X 1.5TC','5 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E005-10-S','KrosFlo','KROSFLO 20CM 5K MPES 1MM 3TC X 1.5TC STERILE','5 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E010-05-N','KrosFlo','KROSFLO 20CM 10K MPES 0.5MM 3TC X 1.5TC','10 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,26.0,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E010-05-S','KrosFlo','KROSFLO 20CM 10K MPES 0.5MM 3TC X 1.5TC STERILE','10 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,26.0,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E010-10-N','KrosFlo','KROSFLO 20CM 10K MPES 1MM 3TC X 1.5TC','10 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E010-10-S','KrosFlo','KROSFLO 20CM 10K MPES 1MM 3TC X 1.5TC STERILE','10 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E030-05-N','KrosFlo','KROSFLO 20CM 30K MPES 0.5MM 3TC X 1.5TC','30 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E030-05-S','KrosFlo','KROSFLO 20CM 30K MPES 0.5MM 3TC X 1.5TC STERILE','30 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E030-10-N','KrosFlo','KROSFLO 20CM 30K MPES 1MM 3TC X 1.5TC','30 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E030-10-S','KrosFlo','KROSFLO 20CM 30K MPES 1MM 3TC X 1.5TC STERILE','30 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E050-05-N','KrosFlo','KROSFLO 20CM 50K MPES 0.5MM 3TC X 1.5TC','50 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E050-05-S','KrosFlo','KROSFLO 20CM 50K MPES 0.5MM 3TC X 1.5TC IRR','50 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E050-10-N','KrosFlo','KROSFLO 20CM 50K MPES 1MM 3TC X 1.5TC','50 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E050-10-S','KrosFlo','KROSFLO 20CM 50K MPES 1MM 3TC X 1.5TC STERILE','50 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E070-05-N','KrosFlo','KROSFLO 20CM 70K MPES 0.5MM 3TC X 1.5TC','70 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E070-05-S','KrosFlo','KROSFLO 20CM 70K MPES 0.5MM 3TC X 1.5TC STERILE','70 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E070-10-N','KrosFlo','KROSFLO 20CM 70K MPES 1MM 3TC X 1.5TC','70 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E070-10-S','KrosFlo','KROSFLO 20CM 70K MPES 1MM 3TC X 1.5TC IRR','70 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E100-05-N','KrosFlo','KROSFLO 20CM 100K MPES 0.5MM 3TC X 1.5TC','100 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E100-05-S','KrosFlo','KROSFLO 20CM 100K MPES 0.5MM 3TC X 1.5TC STERILE','100 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E100-10-N','KrosFlo','KROSFLO 20CM 100K MPES 1MM 3TC X 1.5TC','100 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E100-10-S','KrosFlo','KROSFLO 20CM 100K MPES 1MM 3TC X 1.5TC STERILE','100 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E20U-05-N','KrosFlo','KROSFLO 20CM 0.2UM MPES 0.5MM 3TC X 1.5TC','0.2 μm','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E20U-05-S','KrosFlo','KROSFLO 20CM 0.2UM MPES 0.5MM 3TC X 1.5TC STERILE','0.2 μm','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E20U-10-N','KrosFlo','KROSFLO 20CM .2UM MPES 1MM 3TCX1.5TC 1/PK','0.2 μm','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E20U-10-S','KrosFlo','KROSFLO 20CM .2UM MPES 1MM 3TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E300-05-N','KrosFlo','KROSFLO 20CM 300K MPES 0.5MM 3TC X 1.5TC','300 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E300-05-S','KrosFlo','KROSFLO 20CM 300K MPES 0.5MM 3TC X 1.5TC IRR','300 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E300-10-N','KrosFlo','KROSFLO 20CM 300K MPES 1MM 3TC X 1.5TC','300 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E300-10-S','KrosFlo','KROSFLO 20CM 300K MPES 1MM 3TC X 1.5TC STERILE','300 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E500-05-N','KrosFlo','KROSFLO 20CM 500K MPES 0.5MM 3TC X 1.5TC','500 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E500-05-S','KrosFlo','KROSFLO 20CM 500K MPES 0.5MM 3TC X 1.5TC STERILE','500 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E500-10-N','KrosFlo','KROSFLO 20CM 500K MPES 1MM 3TC X 1.5TC','500 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E500-10-S','KrosFlo','KROSFLO 20CM 500K MPES 1MM 3TC X 1.5TC STERILE','500 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E65U-07-N','Krosflo','KROSFLO 20CM .65UM MPES .75MM 3TCX1.5TC 1/PK','0.65 μm','mPES',0.75,23,'20',1900,2.5,46.2,43.0,38.8,9000,167.79,288.34,9443,28330,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E65U-07-S','Krosflo','KROSFLO 20CM .65UM MPES .75MM 3TCX1.5TC STERILE 1/PK','0.65 μm','mPES',0.75,23,'20',1900,2.5,46.2,43.0,38.8,9000,167.79,288.34,9443,28330,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E750-05-N','KrosFlo','KROSFLO 20CM 750K MPES 0.5MM 3TC X 1.5TC','750 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E750-05-S','KrosFlo','KROSFLO 20CM 750K MPES 0.5MM 3TC X 1.5TC STERILE','750 kD','mPES',0.5,23,'20',4000,2.5,31.5,29.5,27.5,12500,157.0,291.49,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E750-10-N','KrosFlo','KROSFLO 20CM 750KD MPES 1MM 3TCX1.5TC 1/PK','750 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-E750-10-S','KrosFlo','KROSFLO 20CM 750KD MPES 1MM 3TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,23,'20',1250,2.5,57.0,52.5,48.0,7850,196.25,294.99,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-M10U-06-N','KrosFlo','KROSFLO 20CM 0.1 UM ME 0.63MM 3TC X 1.5TC','0.1 μm','ME',0.63,23,'20',3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-M10U-06-S','KrosFlo','KROSFLO 20CM 0.1 UM ME 0.63MM 3TC X 1.5TC STERILE','0.1 μm','ME',0.63,23,'20',3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-M20U-06-N','KrosFlo','KROSFLO 20CM 0.2 UM ME 0.63MM 3TC X 1.5TC','0.2 μm','ME',0.63,23,'20',3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-M20U-06-S','KrosFlo','KROSFLO 20CM 0.2 UM ME 0.63MM 3TC X 1.5TC STERILE','0.2 μm','ME',0.63,23,'20',3000,2.5,37.0,34.0,31.0,12000,186.94,292.77,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-M20U-10-N','KrosFlo','KROSFLO 20CM 0.2 UM ME 1MM 3TC X 1.5TC','0.2 μm','ME',1.0,23,'20',1300,2.5,54.0,50.0,46.0,8200,204.1,314.16,15315,45946,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-M20U-10-S','KrosFlo','KROSFLO 20CM 0.2 UM ME 1MM 3TC X 1.5TC STERILE','0.2 μm','ME',1.0,23,'20',1300,2.5,54.0,50.0,46.0,8200,204.1,314.16,15315,45946,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-P20U-05-N','KrosFlo','KROSFLO 20CM 0.2 UM PES 0.5MM 3TC X 1.5TC','0.2 μm','PES',0.5,23,'20',5000,2.5,26.6,25.6,24.6,16000,196.25,311.53,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-P20U-05-S','KrosFlo','KROSFLO 20CM 0.2 UM PES 0.5MM 3TC X 1.5TC STERILE','0.2 μm','PES',0.5,23,'20',5000,2.5,26.6,25.6,24.6,16000,196.25,311.53,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-P20U-10-N','KrosFlo','KROSFLO 20CM 0.2 UM PES 1MM 3TC X 1.5TC','0.2 μm','PES',1.0,23,'20',1250,2.5,53.1,51.2,49.2,7850,196.25,311.53,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-P20U-10-S','KrosFlo','KROSFLO 20CM 0.2 UM PES 1MM 3TC X 1.5TC STERILE','0.2 μm','PES',1.0,23,'20',1250,2.5,53.1,51.2,49.2,7850,196.25,311.53,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S010-05-N','KrosFlo','KROSFLO 20CM 10KD PS 0.5MM 3TC X 1.5TC','10 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S010-05-P','KrosFlo','KROSFLO 20CM 10KD PS 0.5MM 3TC X 1.5TC WET','10 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S010-05-S','KrosFlo','KROSFLO 20CM 10KD PS 0.5MM 3TC X 1.5TC STERILE','10 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S050-05-N','KrosFlo','KROSFLO 20CM 50KD PS 0.5MM 3TC X 1.5TC','50 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S050-05-P','KrosFlo','KROSFLO 20CM 50KD PS 0.5MM 3TC X 1.5TC WET','50 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S050-05-S','KrosFlo','KROSFLO 20CM 50KD PS 0.5MM 3TC X 1.5TC STERILE','50 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S05U-05-N','KrosFlo','KROSFLO 20CM 0.05 UM PS 0.5MM 3TC X 1.5TC','0.05 μm','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S05U-05-P','KrosFlo','KROSFLO 20CM 0.05 UM PS 0.5MM 3TC X 1.5TC WET','0.05 μm','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S05U-05-S','KrosFlo','KROSFLO 20CM 0.05 UM PS 0.5MM 3TC X 1.5TC STERILE','0.05 μm','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S500-05-N','KrosFlo','KROSFLO 20CM 500KD PS 0.5MM 3TC X 1.5TC','500 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S500-05-P','KrosFlo','KROSFLO 20CM 500KD PS 0.5MM 3TC X 1.5TC WET','500 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K02-S500-05-S','KrosFlo','KROSFLO 20CM 500KD PS 0.5MM 3TC X 1.5TC STERILE','500 kD','PS',0.5,23,'20',5000,2.5,26.8,23.6,20.5,16000,196.25,359.8,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E001-05-N','KrosFlo','KROSFLO 41.5CM 1K MPES .5MM 3TCX1.5TC 1/PK','1 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E001-05-S','KrosFlo','KROSFLO 41.5CM 1K MPES .5MM 3TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E003-05-N','KrosFlo','KROSFLO 41.5CM 3K MPES 0.5MM 3TC X 1.5TC','3 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E003-05-S','KrosFlo','KROSFLO 41.5CM 3K MPES 0.5MM 3TC X 1.5TC STERILE','3 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E003-10-N','KrosFlo','KROSFLO 41.5CM 3K MPES 1MM 3TC X 1.5TC','3 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E003-10-S','KrosFlo','KROSFLO 41.5CM 3K MPES 1MM 3TC X 1.5TC STERILE','3 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E005-05-N','KrosFlo','KROSFLO 41.5CM 5K MPES 0.5MM 3TC X 1.5TC','5 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E005-05-S','KrosFlo','KROSFLO 41.5CM 5K MPES 0.5MM 3TC X 1.5TC STERILE','5 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E005-10-N','KrosFlo','KROSFLO 41.5CM 5K MPES 1MM 3TC X 1.5TC','5 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E005-10-S','KrosFlo','KROSFLO 41.5CM 5K MPES 1MM 3TC X 1.5TC STERILE','5 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E010-05-N','KrosFlo','KROSFLO 41.5CM 10K MPES 0.5MM 3TC X 1.5TC','10 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,26.0,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E010-05-S','KrosFlo','KROSFLO 41.5CM 10K MPES 0.5MM 3TC X 1.5TC IRR','10 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,26.0,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E010-10-N','KrosFlo','KROSFLO 41.5CM 10K MPES 1MM 3TC X 1.5TC','10 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E010-10-S','KrosFlo','KROSFLO 41.5CM 10K MPES 1MM 3TC X 1.5TC STERILE','10 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E030-05-N','KrosFlo','KROSFLO 41.5CM 30K MPES 0.5MM 3TC X 1.5TC','30 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E030-05-S','KrosFlo','KROSFLO 41.5CM 30K MPES 0.5MM 3TC X 1.5TC STERILE','30 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E030-10-N','KrosFlo','KROSFLO 41.5CM 30K MPES 1MM 3TC X 1.5TC','30 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E030-10-S','KrosFlo','KROSFLO 41.5CM 30K MPES 1MM 3TC X 1.5TC STERILE','30 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E050-05-N','KrosFlo','KROSFLO 41.5CM 50K MPES 0.5MM 3TC X 1.5TC','50 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E050-05-S','KrosFlo','KROSFLO 41.5CM 50K MPES 0.5MM 3TC X 1.5TC STERILE','50 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E050-10-N','KrosFlo','KROSFLO 41.5CM 50K MPES 1MM 3TC X 1.5TC','50 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E050-10-S','KrosFlo','KROSFLO 41.5CM 50K MPES 1MM 3TC X 1.5TC STERILE','50 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E070-05-N','KrosFlo','KROSFLO 41.5CM 70K MPES 0.5MM 3TC X 1.5TC','70 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E070-05-S','KrosFlo','KROSFLO 41.5CM 70K MPES 0.5MM 3TC X 1.5TC STERILE','70 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E070-10-N','KrosFlo','KROSFLO 41.5CM 70K MPES 1MM 3TC X 1.5TC','70 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E070-10-S','KrosFlo','KROSFLO 41.5CM 70K MPES 1MM 3TC X 1.5TC STERILE','70 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E100-05-N','KrosFlo','KROSFLO 41.5CM 100K MPES 0.5MM 3TC X 1.5TC','100 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E100-05-S','KrosFlo','KROSFLO 41.5CM 100K MPES 0.5MM 3TC X 1.5TC STERILE','100 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E100-10-N','KrosFlo','KROSFLO 41.5CM 100K MPES 1MM 3TC X 1.5TC','100 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E100-10-S','KrosFlo','KROSFLO 41.5CM 100K MPES 1MM 3TC X 1.5TC STERILE','100 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E20U-05-N','KrosFlo','KROSFLO 41.5CM 0.2UM MPES 0.5MM 3TC X 1.5TC','0.2 μm','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E20U-05-S','KrosFlo','KROSFLO 41.5CM 0.2UM MPES 0.5MM 3TC X 1.5TC STERILE','0.2 μm','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E20U-10-N','KrosFlo','KROSFLO 41.5CM .2UM MPES 1MM 3TCX1.5TC 1/PK','0.2 μm','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E20U-10-S','KrosFlo','KROSFLO 41.5CM .2UM MPES 1MM 3TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E300-05-N','KrosFlo','KROSFLO 41.5CM 300K MPES 0.5MM 3TC X 1.5TC','300 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E300-05-S','KrosFlo','KROSFLO 41.5CM 300K MPES 0.5MM 3TC X 1.5TC STERILE','300 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E300-10-N','KrosFlo','KROSFLO 41.5CM 300K MPES 1MM 3TC X 1.5TC','300 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E300-10-S','KrosFlo','KROSFLO 41.5CM 300K MPES 1MM 3TC X 1.5TC STERILE','300 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E500-05-N','KrosFlo','KROSFLO 41.5CM 500K MPES 0.5MM 3TC X 1.5TC','500 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E500-05-S','KrosFlo','KROSFLO 41.5CM 500K MPES 0.5MM 3TC X 1.5TC STERILE','500 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E500-10-N','KrosFlo','KROSFLO 41.5CM 500K MPES 1MM 3TC X 1.5TC','500 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E500-10-S','KrosFlo','KROSFLO 41.5CM 500K MPES 1MM 3TC X 1.5TC STERILE','500 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E65U-07-N','Krosflo','KROSFLO 41.5CM .65UM MPES .75MM 3TCX1.5TC 1/PK','0.65 μm','mPES',0.75,46,'41.5',1900,2.5,46.2,43.0,38.8,18500,348.17,598.31,9443,28330,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E65U-07-S','Krosflo','KROSFLO 41.5CM .65UM MPES .75MM 3TCX1.5TC STERILE 1/PK','0.65 μm','mPES',0.75,46,'41.5',1900,2.5,46.2,43.0,38.8,18500,348.17,598.31,9443,28330,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E750-05-N','KrosFlo','KROSFLO 41.5CM 750K MPES 0.5MM 3TC X 1.5TC','750 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E750-05-S','KrosFlo','KROSFLO 41.5CM 750K MPES 0.5MM 3TC X 1.5TC STERILE','750 kD','mPES',0.5,46,'41.5',4000,2.5,31.5,29.5,27.5,26000,325.78,604.84,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E750-10-N','KrosFlo','KROSFLO 41.5CM 750KD MPES 1MM 3TCX1.5TC 1/PK','750 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-E750-10-S','KrosFlo','KROSFLO 41.5CM 750KD MPES 1MM 3TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,46,'41.5',1250,2.5,57.0,52.5,48.0,16500,407.22,612.11,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-M10U-06-N','KrosFlo','KROSFLO 41.5CM 0.1 UM ME 0.63MM 3TC X 1.5TC','0.1 μm','ME',0.63,46,'41.5',3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-M10U-06-S','KrosFlo','KROSFLO 41.5CM 0.1 UM ME 0.63MM 3TC X 1.5TC STERILE','0.1 μm','ME',0.63,46,'41.5',3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-M20U-06-N','KrosFlo','KROSFLO 41.5CM 0.2 uM ME 0.63MM 3TC X 1.5TC','0.2 μm','ME',0.63,46,'41.5',3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-M20U-06-S','KrosFlo','KROSFLO 41.5CM 0.2 UM ME 0.63MM 3TC X 1.5TC STERILE','0.2 μm','ME',0.63,46,'41.5',3000,2.5,37.0,34.0,31.0,25000,387.9,607.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-M20U-10-N','KrosFlo','KROSFLO 41.5CM 0.2 UM ME 1MM 3TC X 1.5TC','0.2 μm','ME',1.0,46,'41.5',1300,2.5,54.0,50.0,46.0,17000,423.51,651.88,15315,45946,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-M20U-10-S','KrosFlo','KROSFLO 41.5CM 0.2 UM ME 1MM 3TC X 1.5TC STERILE','0.2 μm','ME',1.0,46,'41.5',1300,2.5,54.0,50.0,46.0,17000,423.51,651.88,15315,45946,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-P20U-05-N','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 0.5MM 3TC X 1.5TC','0.2 μm','PES',0.5,46,'41.5',5000,2.5,26.6,25.6,24.6,32500,407.22,646.42,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-P20U-05-S','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 0.5MM 3TC X 1.5TC STERILE','0.2 μm','PES',0.5,46,'41.5',5000,2.5,26.6,25.6,24.6,32500,407.22,646.42,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-P20U-10-N','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 1MM 3TC X 1.5TC','0.2 μm','PES',1.0,46,'41.5',1250,2.5,53.1,51.2,49.2,16500,407.22,646.42,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-P20U-10-S','KrosFlo','KROSFLO 41.5CM 0.2 UM PES 1MM 3TC X 1.5TC STERILE','0.2 μm','PES',1.0,46,'41.5',1250,2.5,53.1,51.2,49.2,16500,407.22,646.42,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S010-05-N','KrosFlo','KROSFLO 41.5CM 10KD PS 0.5MM 3TC X 1.5TC','10 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S010-05-P','KrosFlo','KROSFLO 41.5CM 10KD PS 0.5MM 3TC X 1.5TC WET','10 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S010-05-S','KrosFlo','KROSFLO 41.5CM 10KD PS 0.5MM 3TC X 1.5TC STERILE','10 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S050-05-N','KrosFlo','KROSFLO 41.5CM 50KD PS 0.5MM 3TC X 1.5TC','50 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S050-05-P','KrosFlo','KROSFLO 41.5CM 50KD PS 0.5MM 3TC X 1.5TC WET','50 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S050-05-S','KrosFlo','KROSFLO 41.5CM 50KD PS 0.5MM 3TC X 1.5TC STERILE','50 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S05U-05-N','KrosFlo','KROSFLO 41.5CM 0.05 UM PS 0.5MM 3TC X 1.5TC','0.05 μm','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S05U-05-P','KrosFlo','KROSFLO 41.5CM 0.05 UM PS 0.5MM 3TC X 1.5TC WET','0.05 μm','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S05U-05-S','KrosFlo','KROSFLO 41.5CM 0.05 UM PS 0.5MM 3TC X 1.5TC STERILE','0.05 μm','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S500-05-N','KrosFlo','KROSFLO 41.5CM 500KD PS 0.5MM 3TC X 1.5TC','500 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S500-05-P','KrosFlo','KROSFLO 41.5CM 500KD PS 0.5MM 3TC X 1.5TC WET','500 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K04-S500-05-S','KrosFlo','KROSFLO 41.5CM 500KD PS 0.5MM 3TC X 1.5TC STERILE','500 kD','PS',0.5,46,'41.5',5000,2.5,26.8,23.6,20.5,32500,407.22,746.59,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E001-05-N','KrosFlo','KROSFLO 65CM 1K MPES .5MM 3TCX1.5TC 1/PK','1 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E001-05-S','KrosFlo','KROSFLO 65CM 1K MPES .5MM 3TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E003-05-N','KrosFlo','KROSFLO 65CM 3K MPES 0.5MM 3TC X 1.5TC','3 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E003-05-S','KrosFlo','KROSFLO 65CM 3K MPES 0.5MM 3TC X 1.5TC STERILE','3 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E003-10-N','KrosFlo','KROSFLO 65CM 3K MPES 1MM 3TC X 1.5TC','3 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E003-10-S','KrosFlo','KROSFLO 65CM 3K MPES 1MM 3TC X 1.5TC STERILE','3 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E005-05-N','KrosFlo','KROSFLO 65CM 5K MPES 0.5MM 3TC X 1.5TC','5 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E005-05-S','KrosFlo','KROSFLO 65CM 5K MPES 0.5MM 3TC X 1.5TC STERILE','5 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E005-10-N','KrosFlo','KROSFLO 65CM 5K MPES 1MM 3TC X 1.5TC','5 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E005-10-S','KrosFlo','KROSFLO 65CM 5K MPES 1MM 3TC X 1.5TC STERILE','5 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E010-05-N','KrosFlo','KROSFLO 65CM 10K MPES 0.5MM 3TC X 1.5TC','10 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,26.0,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E010-05-S','KrosFlo','KROSFLO 65CM 10K MPES 0.5MM 3TC X 1.5TC IRR','10 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,26.0,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E010-10-N','KrosFlo','KROSFLO 65CM 10K MPES 1MM 3TC X 1.5TC','10 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E010-10-S','KrosFlo','KROSFLO 65CM 10K MPES 1MM 3TC X 1.5TC STERILE','10 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E030-05-N','KrosFlo','KROSFLO 65CM 30K MPES 0.5MM 3TC X 1.5TC','30 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E030-05-S','KrosFlo','KROSFLO 65CM 30K MPES 0.5MM 3TC X 1.5TC IRR','30 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E030-10-N','KrosFlo','KROSFLO 65CM 30K MPES 1MM 3TC X 1.5TC','30 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E030-10-S','KrosFlo','KROSFLO 65CM 30K MPES 1MM 3TC X 1.5TC STERILE','30 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E050-05-N','KrosFlo','KROSFLO 65CM 50K MPES 0.5MM 3TC X 1.5TC','50 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E050-05-S','KrosFlo','KROSFLO 65CM 50K MPES 0.5MM 3TC X 1.5TC IRR','50 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E050-10-N','KrosFlo','KROSFLO 65CM 50K MPES 1MM 3TC X 1.5TC','50 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E050-10-S','KrosFlo','KROSFLO 65CM 50K MPES 1MM 3TC X 1.5TC STERILE','50 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E070-05-N','KrosFlo','KROSFLO 65CM 70K MPES 0.5MM 3TC X 1.5TC','70 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E070-05-S','KrosFlo','KROSFLO 65CM 70K MPES 0.5MM 3TC X 1.5TC STERILE','70 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E070-10-N','KrosFlo','KROSFLO 65CM 70K MPES 1MM 3TC X 1.5TC','70 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E070-10-S','KrosFlo','KROSFLO 65CM 70K MPES 1MM 3TC X 1.5TC STERILE','70 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E100-05-N','KrosFlo','KROSFLO 65CM 100K MPES 0.5MM 3TC X 1.5TC','100 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E100-05-S','KrosFlo','KROSFLO 65CM 100K MPES 0.5MM 3TC X 1.5TC STERILE','100 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E100-10-N','KrosFlo','KROSFLO 65CM 100K MPES 1MM 3TC X 1.5TC','100 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E100-10-S','KrosFlo','KROSFLO 65CM 100K MPES 1MM 3TC X 1.5TC STERILE','100 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E20U-05-N','KrosFlo','KROSFLO 65CM 0.2UM MPES 0.5MM 3TC X 1.5TC','0.2 μm','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E20U-05-S','KrosFlo','KROSFLO 65CM 0.2UM MPES 0.5MM 3TC X 1.5TC STERILE','0.2 μm','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E20U-10-N','KrosFlo','KROSFLO 65CM .2UM MPES 1MM 3TCX1.5TC 1/PK','0.2 μm','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E20U-10-S','KrosFlo','KROSFLO 65CM .2UM MPES 1MM 3TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E300-05-N','KrosFlo','KROSFLO 65CM 300K MPES 0.5MM 3TC X 1.5TC','300 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E300-05-S','KrosFlo','KROSFLO 65CM 300K MPES 0.5MM 3TC X 1.5TC STERILE','300 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E300-10-N','KrosFlo','KROSFLO 65CM 300K MPES 1MM 3TC X 1.5TC','300 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E300-10-S','KrosFlo','KROSFLO 65CM 300K MPES 1MM 3TC X 1.5TC STERILE','300 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E500-05-N','KrosFlo','KROSFLO 65CM 500K MPES 0.5MM 3TC X 1.5TC','500 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E500-05-S','KrosFlo','KROSFLO 65CM 500K MPES 0.5MM 3TC X 1.5TC STERILE','500 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E500-10-N','KrosFlo','KROSFLO 65CM 500K MPES 1MM 3TC X 1.5TC','500 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E500-10-S','KrosFlo','+KROSFLO 65CM 500K MPES 1MM 3TC X 1.5TC STERILE','500 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E65U-07-N','Krosflo','KROSFLO 65CM .65UM MPES .75MM 3TCX1.5TC 1/PK','0.65 μm','mPES',0.75,69,'65',1900,2.5,46.2,43.0,38.8,29000,545.33,937.11,9443,28330,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E65U-07-S','Krosflo','KROSFLO 65CM .65UM MPES .75MM 3TCX1.5TC STERILE 1/PK','0.65 μm','mPES',0.75,69,'65',1900,2.5,46.2,43.0,38.8,29000,545.33,937.11,9443,28330,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E750-05-N','KrosFlo','KROSFLO 65CM 750K MPES 0.5MM 3TC X 1.5TC','750 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E750-05-S','KrosFlo','KROSFLO 65CM 750K MPES 0.5MM 3TC X 1.5TC STERILE','750 kD','mPES',0.5,69,'65',4000,2.5,31.5,29.5,27.5,41000,510.25,947.34,5890,17671,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E750-10-N','KrosFlo','KROSFLO 65CM 750KD MPES 1MM 3TCX1.5TC 1/PK','750 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-E750-10-S','KrosFlo','KROSFLO 65CM 750KD MPES 1MM 3TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,69,'65',1250,2.5,57.0,52.5,48.0,25500,637.81,958.72,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-M10U-06-N','KrosFlo','KROSFLO 65CM 0.1 UM ME 0.63MM 3TC X 1.5TC','0.1 μm','ME',0.63,69,'65',3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-M10U-06-S','KrosFlo','KROSFLO 65CM 0.1 UM ME 0.63MM 3TC X 1.5TC STERILE','0.1 μm','ME',0.63,69,'65',3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-M20U-06-N','KrosFlo','KROSFLO 65CM 0.2 UM ME 0.6MM 3TC X 1.5TC','0.2 μm','ME',0.63,69,'65',3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-M20U-06-S','KrosFlo','KROSFLO 65CM 0.2 UM ME 0.6MM 3TC X 1.5TC STERILE','0.2 μm','ME',0.63,69,'65',3000,2.5,37.0,34.0,31.0,39000,607.55,951.49,8837,26512,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-M20U-10-N','KrosFlo','KROSFLO 65CM 0.2 UM ME 1MM 3TC X 1.5TC','0.2 μm','ME',1.0,69,'65',1300,2.5,54.0,50.0,46.0,26500,663.33,1021.01,15315,45946,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-M20U-10-S','KrosFlo','KROSFLO 65CM 0.2 UM ME 1MM 3TC X 1.5TC STERILE','0.2 μm','ME',1.0,69,'65',1300,2.5,54.0,50.0,46.0,26500,663.33,1021.01,15315,45946,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-P20U-05-N','KrosFlo','KROSFLO 65CM 0.2 UM PES 0.5MM 3TC X 1.5TC','0.2 μm','PES',0.5,69,'65',5000,2.5,26.6,25.6,24.6,51000,637.81,1012.46,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-P20U-05-S','KrosFlo','KROSFLO 65CM 0.2 UM PES 0.5MM 3TC X 1.5TC STERILE','0.2 μm','PES',0.5,69,'65',5000,2.5,26.6,25.6,24.6,51000,637.81,1012.46,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-P20U-10-N','KrosFlo','KROSFLO 65CM 0.2 UM PES 1MM 3TC X 1.5TC','0.2 μm','PES',1.0,69,'65',1250,2.5,53.1,51.2,49.2,25500,637.81,1012.46,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-P20U-10-S','KrosFlo','KROSFLO 65CM 0.2 UM PES 1MM 3TC X 1.5TC STERILE','0.2 μm','PES',1.0,69,'65',1250,2.5,53.1,51.2,49.2,25500,637.81,1012.46,14726,44179,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S010-05-N','KrosFlo','KROSFLO 65CM 10KD PS 0.5MM 3TC X 1.5TC','10 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S010-05-P','KrosFlo','KROSFLO 65CM 10KD PS 0.5MM 3TC X 1.5TC WET','10 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S010-05-S','KrosFlo','KROSFLO 65CM 10KD PS 0.5MM 3TC X 1.5TC STERILE','10 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S050-05-N','KrosFlo','KROSFLO 65CM 50KD PS 0.5MM 3TC X 1.5TC','50 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S050-05-P','KrosFlo','KROSFLO 65CM 50KD PS 0.5MM 3TC X 1.5TC WET','50 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S050-05-S','KrosFlo','KROSFLO 65CM 50KD PS 0.5MM 3TC X 1.5TC STERILE','50 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S05U-05-N','KrosFlo','KROSFLO 65CM 0.05 UM PS 0.5MM 3TC X 1.5TC','0.05 μm','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S05U-05-P','KrosFlo','KROSFLO 65CM 0.05 UM PS 0.5MM 3TC X 1.5TC WET','0.05 μm','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S05U-05-S','KrosFlo','KROSFLO 65CM 0.05 UM PS 0.5MM 3TC X 1.5TC STERILE','0.05 μm','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S500-05-N','KrosFlo','KROSFLO 65CM 500KD PS 0.5MM 3TC X 1.5TC','500 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S500-05-P','KrosFlo','KROSFLO 65CM 500KD PS 0.5MM 3TC X 1.5TC WET','500 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('K06-S500-05-S','KrosFlo','KROSFLO 65CM 500KD PS 0.5MM 3TC X 1.5TC STERILE','500 kD','PS',0.5,69,'65',5000,2.5,26.8,23.6,20.5,51000,637.81,1169.37,7363,22089,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E001-05-N','MiniKros','MINIKROS 20CM 1K MPES .5MM 1.5TCX3/4TC 1/PK','1 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E001-05-S','MiniKros','MINIKROS 20CM 1K MPES .5MM 1.5TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E003-05-N','MiniKros','MINIKROS 20CM 3K MPES 0.5MM 1.5TC X 3/4TC','3 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E003-05-S','MiniKros','MINIKROS 20CM 3K MPES 0.5MM 1.5TC X 3/4TC STERILE','3 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E003-10-N','MiniKros','MINIKROS 20CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK','3 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E003-10-S','MiniKros','MINIKROS 20CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','3 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E005-05-N','MiniKros','MINIKROS 20CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E005-05-S','MiniKros','MINIKROS 20CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E005-10-N','MiniKros','MINIKROS 20CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E005-10-S','MiniKros','MINIKROS 20CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E010-05-N','MiniKros','MINIKROS 20CM 10K MPES 0.5MM 1.5TC X 3/4TC','10 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,26.0,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E010-05-S','MiniKros','MINIKROS 20CM 10K MPES 0.5MM 1.5TC X 3/4TC STERILE','10 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,26.0,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E010-10-N','MiniKros','MINIKROS 20CM 10K MPES 1MM 1.5TC X 3/4TC','10 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E010-10-S','MiniKros','MINIKROS 20CM 10K MPES 1MM 1.5TC X 3/4TC STERILE','10 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E030-05-N','MiniKros','MINIKROS 20CM 30K MPES 0.5MM 1.5TC X 3/4TC','30 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E030-05-S','MiniKros','MINIKROS 20CM 30K MPES 0.5MM 1.5TC X 3/4TC STERILE','30 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E030-10-N','MiniKros','MINIKROS 20CM 30K MPES 1MM 1.5TC X 3/4TC','30 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E030-10-S','MiniKros','MINIKROS 20CM 30K MPES 1MM 1.5TC X 3/4TC STERILE','30 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E050-05-N','MiniKros','MINIKROS 20CM 50K MPES 0.5MM 1.5TC X 3/4TC','50 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E050-05-S','MiniKros','MINIKROS 20CM 50K MPES 0.5MM 1.5TC X 3/4TC STERILE','50 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E050-10-N','MiniKros','MINIKROS 20CM 50K MPES 1MM 1.5TC X 3/4TC','50 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E050-10-S','MiniKros','MINIKROS 20CM 50K MPES 1MM 1.5TC X 3/4TC STERILE','50 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E070-05-N','MiniKros','MINIKROS 20CM 70K MPES 0.5MM 1.5TC X 3/4TC','70 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E070-05-S','MiniKros','MINIKROS 20CM 70K MPES 0.5MM 1.5TC X 3/4TCSTERILE','70 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E070-10-N','MiniKros','MINIKROS 20CM 70K MPES 1MM 1.5TC X 3/4TC','70 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E070-10-S','MiniKros','MINIKROS 20CM 70K MPES 1MM 1.5TC X 3/4TC STERILE','70 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E100-05-N','MiniKros','MINIKROS 20CM 100K MPES 0.5MM 1.5TC X 3/4TC','100 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E100-05-S','MiniKros','MINIKROS 20CM 100K MPES 0.5MM 1.5TC X 3/4TC STERILE','100 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E100-10-N','MiniKros','MINIKROS 20CM 100K MPES 1MM 1.5TC X 3/4TC','100 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E100-10-S','MiniKros','MINIKROS 20CM 100K MPES 1MM 1.5TC X 3/4TC STERILE','100 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E20U-05-N','MiniKros','MINIKROS 20CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK','0.2 μm','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E20U-05-S','MiniKros','MINIKROS 20CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','0.2 μm','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E20U-10-N','MiniKros','MINIKROS 20CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK','0.2 μm','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E20U-10-S','MiniKros','MINIKROS 20CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK STERILE','0.2 μm','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E300-05-N','MiniKros','MINIKROS 20CM 300K MPES 0.5MM 1.5TC X 3/4TC','300 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E300-05-S','MiniKros','MINIKROS 20CM 300K MPES 0.5MM 1.5TC X 3/4TC STERILE','300 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E300-10-N','MiniKros','MINIKROS 20CM 300K MPES 1MM 1.5TC X 3/4TC','300 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E300-10-S','MiniKros','MINIKROS 20CM 300K MPES 1MM 1.5TC X 3/4TC STERILE','300 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E500-05-N','MiniKros','MINIKROS 20CM 500K MPES 0.5MM 1.5TC X 3/4TC','500 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E500-05-S','MiniKros','MINIKROS 20CM 500K MPES 0.5MM 1.5TC X 3/4TC STERILE','500 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E500-10-N','MiniKros','MINIKROS 20CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK','500 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E500-10-S','MiniKros','MINIKROS 20CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','500 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E65U-07-N','MiniKros','MINIKROS 20CM .65UM MPES .75MM 1.5TCX3/4TC 1/PK','0.65 μm','mPES',0.75,25,'20',400,1.125,46.2,43.0,38.8,1800,35.33,55.62,1988,5964,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E65U-07-S','MiniKros','MINIKROS 20CM .65UM MPES .75MM 1.5TCX3/4TC STERILE 1/PK','0.65 μm','mPES',0.75,25,'20',400,1.125,46.2,43.0,38.8,1800,35.33,55.62,1988,5964,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E750-05-N','MiniKros','MINIKROS 20CM 750K MPES 0.5MM 1.5TC X 3/4TC','750 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E750-05-S','MiniKros','MINIKROS 20CM 750K MPES 0.5MM 1.5TC X 3/4TC STERILE','750 kD','mPES',0.5,25,'20',830,1.125,31.5,29.5,27.5,2600,32.58,57.32,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E750-10-N','MiniKros','MINIKROS 20CM 750KD MPES 1MM 1.5TCX1.5TC 1/PK','750 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-E750-10-S','MiniKros','MINIKROS 20CM 750KD MPES 1MM 1.5TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,25,'20',250,1.125,57.0,52.5,48.0,1550,39.25,60.58,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-M10U-06-N','MiniKros','MINIKROS 20CM 0.1UM ME 0.63MM 1.5TC X 3/4TC','0.1 μm','ME',0.63,25,'20',590,1.125,37.0,34.0,31.0,2350,36.76,61.27,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-M10U-06-S','MiniKros','MINIKROS 20CM 0.1UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.1 μm','ME',0.63,25,'20',590,1.125,37.0,34.0,31.0,2350,36.76,61.27,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-M20U-06-N','MiniKros','MINIKROS 20CM 0.2um ME 0.63MM 1.5TC X 3/4TC','0.2 μm','ME',0.63,25,'20',590,1.125,37.0,34.0,31.0,2350,36.76,61.27,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-M20U-06-S','MiniKros','MINIKROS 20CM 0.2UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.2 μm','ME',0.63,25,'20',590,1.125,37.0,34.0,31.0,2350,36.76,61.27,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-M20U-10-N','MiniKros','MINIKROS 20CM 0.2UM ME 1MM 1.5TC X 3/4TC','0.2 μm','ME',1.0,25,'20',260,1.125,54.0,50.0,46.0,1650,40.82,64.41,3063,9189,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-M20U-10-S','MiniKros','MINIKROS 20CM 0.2UM ME 1MM 1.5TC X 3/4TC STERILE','0.2 μm','ME',1.0,25,'20',260,1.125,54.0,50.0,46.0,1650,40.82,64.41,3063,9189,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-P20U-05-N','MiniKros','MINIKROS 20CM 0.2UM PES 0.5MM 1.5TC X 3/4TC','0.2 μm','PES',0.5,25,'20',1000,1.125,26.6,25.6,24.6,3100,39.25,63.89,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-P20U-05-S','MiniKros','MINIKROS 20CM 0.2UM PES 0.5MM 1.5TC X 3/4TC STERILE','0.2 μm','PES',0.5,25,'20',1000,1.125,26.6,25.6,24.6,3100,39.25,63.89,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-P20U-10-N','MiniKros','MINIKROS 20CM 0.2UM PES 1MM 1.5TC X 3/4TC','0.2 μm','PES',1.0,25,'20',250,1.125,53.1,51.2,49.2,1570,39.25,63.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-P20U-10-S','MiniKros','MINIKROS 20CM 0.2UM PES 1MM 1.5TC X 3/4TC STERILE','0.2 μm','PES',1.0,25,'20',250,1.125,53.1,51.2,49.2,1570,39.25,63.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S010-05-N','MiniKros','MINIKROS 20CM 10KD PS 0.5MM 1.5TC X 3/4TC','10 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S010-05-P','MiniKros','MINIKROS 20CM 10KD PS 0.5MM 1.5TC X 3/4TC WET','10 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S010-05-S','MiniKros','MINIKROS 20CM 10KD PS 0.5MM 1.5TC X 3/4TC STERILE','10 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S050-05-N','MiniKros','MINIKROS 20CM 50KD PS 0.5MM 1.5TC X 3/4TC','50 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S050-05-P','MiniKros','MINIKROS 20CM 50KD PS 0.5MM 1.5TC X 3/4TC WET','50 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S050-05-S','MiniKros','MINIKROS 20CM 50KD PS 0.5MM 1.5TC X 3/4TC STERILE','50 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S05U-05-N','MiniKros','MINIKROS 20CM 0.05UM PS 0.5MM 1.5TC X 3/4TC','0.05 μm','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S05U-05-P','MiniKros','MINIKROS 20CM 0.05UM PS 0.5MM 1.5TC X 3/4TC WET','0.05 μm','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S05U-05-S','MiniKros','MINIKROS 20CM 0.05UM PS 0.5MM 1.5TC X 3/4TC STERILE','0.05 μm','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S500-05-N','MiniKros','MINIKROS 20CM 500KD PS 0.5MM 1.5TC X 3/4TC','500 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S500-05-P','MiniKros','MINIKROS 20CM 500KD PS 0.5MM 1.5TC X 3/4TC WET','500 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N02-S500-05-S','MiniKros','MINIKROS 20CM 500KD PS 0.5MM 1.5TC X 3/4TC STERILE','500 kD','PS',0.5,25,'20',1000,1.125,26.8,23.6,20.5,3100,39.25,73.54,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E001-05-N','MiniKros','MINIKROS 41.5CM 1K MPES .5MM 1.5TCX3/4TC 1/PK','1 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E001-05-S','MiniKros','MINIKROS 41.5CM 1K MPES .5MM 1.5TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E003-05-N','MiniKros','MINIKROS 41.5CM 3K MPES 0.5MM 1.5TC X 3/4TC','3 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E003-05-S','MiniKros','MINIKROS 41.5CM 3K MPES 0.5MM 1.5TC X 3/4TC STERILE','3 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E003-10-N','MiniKros','MINIKROS 41.5CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK','3 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E003-10-S','MiniKros','MINIKROS 41.5CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','3 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E005-05-N','MiniKros','MINIKROS 41.5CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E005-05-S','MiniKros','MINIKROS 41.5CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E005-10-N','MiniKros','MINIKROS 41.5CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E005-10-S','MiniKros','MINIKROS 41.5CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E010-05-N','MiniKros','MINIKROS 41.5CM 10K MPES 0.5MM 1.5TC X 3/4TC','10 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,26.0,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E010-05-S','MiniKros','MINIKROS 41.5CM 10K MPES 0.5MM 1.5TC X 3/4TC STERILE','10 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,26.0,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E010-10-N','MiniKros','MINIKROS 41.5CM 10K MPES 1MM 1.5TC X 3/4TC','10 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E010-10-S','MiniKros','MINIKROS 41.5CM 10K MPES 1MM 1.5TC X 3/4TC STERILE','10 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E030-05-N','MiniKros','MINIKROS 41.5CM 30K MPES 0.5MM 1.5TC X 3/4TC','30 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E030-05-S','MiniKros','MINIKROS 41.5CM 30K MPES 0.5MM 1.5TC X 3/4TC STERILE','30 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E030-10-N','MiniKros','MINIKROS 41.5CM 30K MPES 1MM 1.5TC X 3/4TC','30 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E030-10-S','MiniKros','MINIKROS 41.5CM 30K MPES 1MM 1.5TC X 3/4TC STERILE','30 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E050-05-N','MiniKros','MINIKROS 41.5CM 50K MPES 0.5MM 1.5TC X 3/4TC','50 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E050-05-S','MiniKros','MINIKROS 41.5CM 50K MPES 0.5MM 1.5TC X 3/4TC STERILE','50 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E050-10-N','MiniKros','MINIKROS 41.5CM 50K MPES 1MM 1.5TC X 3/4TC','50 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E050-10-S','MiniKros','MINIKROS 41.5CM 50K MPES 1MM 1.5TC X 3/4TC STERILE','50 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E070-05-N','MiniKros','MINIKROS 41.5CM 70K MPES 0.5MM 1.5TC X 3/4TC','70 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E070-05-S','MiniKros','MINIKROS 41.5CM 70K MPES 0.5MM 1.5TC X 3/4TC STERILE','70 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E070-10-N','MiniKros','MINIKROS 41.5CM 70K MPES 1MM 1.5TC X 3/4TC','70 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E070-10-S','MiniKros','MINIKROS 41.5CM 70K MPES 1MM 1.5TC X 3/4TC STERILE','70 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E100-05-N','MiniKros','MINIKROS 41.5CM 100K MPES 0.5MM 1.5TC X 3/4TC','100 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E100-05-S','MiniKros','MINIKROS 41.5CM 100K MPES 0.5MM 1.5TC X 3/4TC STERILE','100 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E100-10-N','MiniKros','MINIKROS 41.5CM 100K MPES 1MM 1.5TC X 3/4TC','100 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E100-10-S','MiniKros','MINIKROS 41.5CM 100K MPES 1MM 1.5TC X 3/4TC STERILE','100 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E20U-05-N','MiniKros','MINIKROS 41.5CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK','0.2 μm','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E20U-05-S','MiniKros','MINIKROS 41.5CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','0.2 μm','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E20U-10-N','MiniKros','MINIKROS 41.5CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK','0.2 μm','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E20U-10-S','MiniKros','MINIKROS 41.5CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK STERILE','0.2 μm','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E300-05-N','MiniKros','MINIKROS 41.5CM 300K MPES 0.5MM 1.5TC X 3/4TC','300 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E300-05-S','MiniKros','MINIKROS 41.5CM 300K MPES 0.5MM 1.5TC X 3/4TC STERILE','300 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E300-10-N','MiniKros','MINIKROS 41.5CM 300K MPES 1MM 1.5TC X 3/4TC','300 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E300-10-S','MiniKros','MINIKROS 41.5CM 300K MPES 1MM 1.5TC X 3/4TC STERILE','300 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E500-05-N','MiniKros','MINIKROS 41.5CM 500K MPES 0.5MM 1.5TC X 3/4TC','500 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E500-05-S','MiniKros','MINIKROS 41.5CM 500K MPES 0.5MM 1.5TC X 3/4TC STERILE','500 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E500-10-N','MiniKros','MINIKROS 41.5CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK','500 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E500-10-S','MiniKros','MINIKROS 41.5CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','500 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E65U-07-N','MiniKros','MINIKROS 41.5CM .65UM MPES .75MM 1.5TCX3/4TC 1/PK','0.65 μm','mPES',0.75,46,'41.5',400,1.125,46.2,43.0,38.8,3900,73.3,115.42,1988,5964,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E65U-07-S','MiniKros','MINIKROS 41.5CM .65UM MPES .75MM 1.5TCX3/4TC STERILE 1/PK','0.65 μm','mPES',0.75,46,'41.5',400,1.125,46.2,43.0,38.8,3900,73.3,115.42,1988,5964,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E750-05-N','MiniKros','MINIKROS 41.5CM 750K MPES 0.5MM 1.5TC X 3/4TC','750 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E750-05-S','MiniKros','MINIKROS 41.5CM 750K MPES 0.5MM 1.5TC X 3/4TC STERILE','750 kD','mPES',0.5,46,'41.5',830,1.125,31.5,29.5,27.5,5400,67.6,118.94,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E750-10-N','MiniKros','MINIKROS 41.5CM 750KD MPES 1MM 1.5TCX1.5TC 1/PK','750 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-E750-10-S','MiniKros','MINIKROS 41.5CM 750KD MPES 1MM 1.5TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,46,'41.5',250,1.125,57.0,52.5,48.0,3250,81.44,125.71,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-M10U-06-N','MiniKros','MINIKROS 41.5CM 0.1UM ME 0.63MM 1.5TC X 3/4TC','0.1 μm','ME',0.63,46,'41.5',590,1.125,37.0,34.0,31.0,4900,76.29,127.14,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-M10U-06-S','MiniKros','MINIKROS 41.5CM 0.1UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.1 μm','ME',0.63,46,'41.5',590,1.125,37.0,34.0,31.0,4900,76.29,127.14,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-M20U-06-N','MiniKros','MINIKROS 41.5CM 0.2 UM ME 0.63MM 1.5TC X 3/4TC','0.2 μm','ME',0.63,46,'41.5',590,1.125,37.0,34.0,31.0,4900,76.29,127.14,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-M20U-06-S','MiniKros','MINIKROS 41.5CM 0.2UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.2 μm','ME',0.63,46,'41.5',590,1.125,37.0,34.0,31.0,4900,76.29,127.14,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-M20U-10-N','MiniKros','MINIKROS 41.5CM 0.2UM ME 1MM 1.5TC X 3/4TC','0.2 μm','ME',1.0,46,'41.5',260,1.125,54.0,50.0,46.0,3400,84.7,133.66,3063,9189,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-M20U-10-S','MiniKros','MINIKROS 41.5CM 0.2UM ME 1MM 1.5TC X 3/4TC STERILE','0.2 μm','ME',1.0,46,'41.5',260,1.125,54.0,50.0,46.0,3400,84.7,133.66,3063,9189,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-P20U-05-N','MiniKros','MINIKROS 41.5CM 0.2UM PES 0.5MM 1.5TC X 3/4TC','0.2 μm','PES',0.5,46,'41.5',1000,1.125,26.6,25.6,24.6,6500,81.44,132.57,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-P20U-05-S','MiniKros','MINIKROS 41.5CM 0.2UM PES 0.5MM 1.5TC X 3/4TC STERILE','0.2 μm','PES',0.5,46,'41.5',1000,1.125,26.6,25.6,24.6,6500,81.44,132.57,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-P20U-10-N','MiniKros','MINIKROS 41.5CM 0.2UM PES 1MM 1.5TC X 3/4TC','0.2 μm','PES',1.0,46,'41.5',250,1.125,53.1,51.2,49.2,3260,81.44,132.57,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-P20U-10-S','MiniKros','MINIKROS 41.5CM 0.2UM PES 1MM 1.5TC X 3/4TC STERILE','0.2 μm','PES',1.0,46,'41.5',250,1.125,53.1,51.2,49.2,3260,81.44,132.57,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S010-05-N','MiniKros','MINIKROS 41.5CM 10KD PS 0.5MM 1.5TC X 3/4TC','10 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S010-05-P','MiniKros','MINIKROS 41.5CM 10KD PS 0.5MM 1.5TC X 3/4TC WET','10 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S010-05-S','MiniKros','MINIKROS 41.5CM 10KD PS 0.5MM 1.5TC X 3/4TC STERILE','10 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S050-05-N','MiniKros','MINIKROS 41.5CM 50KD PS 0.5MM 1.5TC X 3/4TC','50 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S050-05-P','MiniKros','MINIKROS 41.5CM 50KD PS 0.5MM 1.5TC X 3/4TC WET','50 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S050-05-S','MiniKros','MINIKROS 41.5CM 50KD PS 0.5MM 1.5TC X 3/4TC STERILE','50 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S05U-05-N','MiniKros','MINIKROS 41.5CM 0.05UM PS 0.5MM 1.5TC X 3/4TC','0.05 μm','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S05U-05-P','MiniKros','MINIKROS 41.5CM 0.05UM PS 0.5MM 1.5TC X 3/4TC WET','0.05 μm','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S05U-05-S','MiniKros','MINIKROS 41.5CM 0.05UM PS 0.5MM 1.5TC X 3/4TC STERILE','0.05 μm','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S500-05-N','MiniKros','MINIKROS 41.5CM 500KD PS 0.5MM 1.5TC X 3/4TC','500 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S500-05-P','MiniKros','MINIKROS 41.5CM 500KD PS 0.5MM 1.5TC X 3/4TC WET','500 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N04-S500-05-S','MiniKros','MINIKROS 41.5CM 500KD PS 0.5MM 1.5TC X 3/4TC STERILE','500 kD','PS',0.5,46,'41.5',1000,1.125,26.8,23.6,20.5,6500,81.44,152.6,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E001-05-N','MiniKros','MINIKROS 65CM 1K MPES .5MM 1.5TCX3/4TC 1/PK','1 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E001-05-S','MiniKros','MINIKROS 65CM 1K MPES .5MM 1.5TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E003-05-N','MiniKros','MINIKROS 65CM 3K MPES 0.5MM 1.5TC X 3/4TC','3 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E003-05-S','MiniKros','MINIKROS 65CM 3K MPES 0.5MM 1.5TC X 3/4TC STERILE','3 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E003-10-N','MiniKros','MINIKROS 65CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK','3 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E003-10-S','MiniKros','MINIKROS 65CM 3K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','3 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E005-05-N','MiniKros','MINIKROS 65CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E005-05-S','MiniKros','MINIKROS 65CM 5K MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E005-10-N','MiniKros','MINIKROS 65CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK','5 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E005-10-S','MiniKros','MINIKROS 65CM 5K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','5 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E010-05-N','MiniKros','MINIKROS 65CM 10K MPES 0.5MM 1.5TC X 3/4TC','10 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,26.0,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E010-05-S','MiniKros','MINIKROS 65CM 10K MPES 0.5MM 1.5TC X 3/4TC STERILE','10 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,26.0,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E010-10-N','MiniKros','MINIKROS 65CM 10K MPES 1MM 1.5TC X 3/4TC','10 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E010-10-S','MiniKros','MINIKROS 65CM 10K MPES 1MM 1.5TC X 3/4TC STERILE','10 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E030-05-N','MiniKros','MINIKROS 65CM 30K MPES 0.5MM 1.5TC X 3/4TC','30 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E030-05-S','MiniKros','MINIKROS 65CM 30K MPES 0.5MM 1.5TC X 3/4TC STERILE','30 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E030-10-N','MiniKros','MINIKROS 65CM 30K MPES 1MM 1.5TC X 3/4TC','30 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E030-10-S','MiniKros','MINIKROS 65CM 30K MPES 1MM 1.5TC X 3/4TC STERILE','30 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E050-05-N','MiniKros','MINIKROS 65CM 50K MPES 0.5MM 1.5TC X 3/4TC','50 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E050-05-S','MiniKros','MINIKROS 65CM 50K MPES 0.5MM 1.5TC X 3/4TC STERILE','50 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E050-10-N','MiniKros','MINIKROS 65CM 50K MPES 1MM 1.5TC X 3/4TC','50 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E050-10-S','MiniKros','MINIKROS 65CM 50K MPES 1MM 1.5TC X 3/4TC STERILE','50 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E070-05-N','MiniKros','MINIKROS 65CM 70K MPES 0.5MM 1.5TC X 3/4TC','70 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E070-05-S','MiniKros','MINIKROS 65CM 70K MPES 0.5MM 1.5TC X 3/4TC STERILE','70 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E070-10-N','MiniKros','MINIKROS 65CM 70K MPES 1MM 1.5TC X 3/4TC','70 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E070-10-S','MiniKros','MINIKROS 65CM 70K MPES 1MM 1.5TC X 3/4TC STERILE','70 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E100-05-N','MiniKros','MINIKROS 65CM 100K MPES 0.5MM 1.5TC X 3/4TC','100 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E100-05-S','MiniKros','MINIKROS 65CM 100K MPES 0.5MM 1.5TC X 3/4TC STERILE','100 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E100-10-N','MiniKros','MINIKROS 65CM 100K MPES 1MM 1.5TC X 3/4TC','100 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E100-10-S','MiniKros','MINIKROS 65CM 100K MPES 1MM 1.5TC X 3/4TC STERILE','100 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E20U-05-N','MiniKros','MINIKROS 65CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK','0.2 μm','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E20U-05-S','MiniKros','MINIKROS 65CM 0.2UM MPES 0.5MM 1.5TC X 3/4TC 1/PK STERILE','0.2 μm','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E20U-10-N','MiniKros','MINIKROS 65CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK','0.2 μm','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E20U-10-S','MiniKros','MINIKROS 65CM .2UM MPES 1MM 1.5TCX3/4TC 1/PK STERILE','0.2 μm','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E300-05-N','MiniKros','MINIKROS 65CM 300K MPES 0.5MM 1.5TC X 3/4TC','300 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E300-05-S','MiniKros','MINIKROS 65CM 300K MPES 0.5MM 1.5TC X 3/4TC STERILE','300 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E300-10-N','MiniKros','MINIKROS 65CM 300K MPES 1MM 1.5TC X 3/4TC','300 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E300-10-S','MiniKros','MINIKROS 65CM 300K MPES 1MM 1.5TC X 3/4TC STERILE','300 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E500-05-N','MiniKros','MINIKROS 65CM 500K MPES 0.5MM 1.5TC X 3/4TC','500 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E500-05-S','MiniKros','MINIKROS 65CM 500K MPES 0.5MM 1.5TC X 3/4TC STERILE','500 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E500-10-N','MiniKros','MINIKROS 65CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK','500 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E500-10-S','MiniKros','MINIKROS 65CM 500K MPES 1MM 1.5TC X 3/4TC 1/PK STERILE','500 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E65U-07-N','MiniKros','MINIKROS 65CM .65UM MPES .75MM 1.5TCX3/4TC 1/PK','0.65 μm','mPES',0.75,70,'65',400,1.125,46.2,43.0,38.8,6100,114.81,180.77,1988,5964,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E65U-07-S','MiniKros','MINIKROS 65CM .65UM MPES .75MM 1.5TCX3/4TC STERILE 1/PK','0.65 μm','mPES',0.75,70,'65',400,1.125,46.2,43.0,38.8,6100,114.81,180.77,1988,5964,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E750-05-N','MiniKros','MINIKROS 65CM 750K MPES 0.5MM 1.5TC X 3/4TC','750 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E750-05-S','MiniKros','MINIKROS 65CM 750K MPES 0.5MM 1.5TC X 3/4TC STERILE','750 kD','mPES',0.5,70,'65',830,1.125,31.5,29.5,27.5,8500,105.88,186.29,1222,3667,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E750-10-N','MiniKros','MINIKROS 65CM 750KD MPES 1MM 1.5TCX1.5TC 1/PK','750 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-E750-10-S','MiniKros','MINIKROS 65CM 750KD MPES 1MM 1.5TCX1.5TC STERILE 1/PK','750 kD','mPES',1.0,70,'65',250,1.125,57.0,52.5,48.0,5100,127.56,196.89,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-M10U-06-N','MiniKros','MINIKROS 65CM 0.1UM ME 0.63MM 1.5TC X 3/4TC','0.1 μm','ME',0.63,70,'65',590,1.125,37.0,34.0,31.0,7650,119.49,199.13,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-M10U-06-S','MiniKros','MINIKROS 65CM 0.1UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.1 μm','ME',0.63,70,'65',590,1.125,37.0,34.0,31.0,7650,119.49,199.13,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-M20U-06-N','MiniKros','MINIKROS 65CM 0.2UM ME 0.63MM 1.5TC X 3/4TC','0.2 μm','ME',0.63,70,'65',590,1.125,37.0,34.0,31.0,7650,119.49,199.13,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-M20U-06-S','MiniKros','MINIKROS 65CM 0.2UM ME 0.63MM 1.5TC X 3/4TC STERILE','0.2 μm','ME',0.63,70,'65',590,1.125,37.0,34.0,31.0,7650,119.49,199.13,1738,5214,18,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-M20U-10-N','MiniKros','MINIKROS 65CM 0.2UM ME 1MM 1.5TC X 3/4TC','0.2 μm','ME',1.0,70,'65',260,1.125,54.0,50.0,46.0,5300,132.67,209.35,3063,9189,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-M20U-10-S','MiniKros','MINIKROS 65CM 0.2UM ME 1MM 1.5TC X 3/4TC STERILE','0.2 μm','ME',1.0,70,'65',260,1.125,54.0,50.0,46.0,5300,132.67,209.35,3063,9189,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-P20U-05-N','MiniKros','MINIKROS 65CM 0.2UM PES 0.5MM 1.5TC X 3/4TC','0.2 μm','PES',0.5,70,'65',1000,1.125,26.6,25.6,24.6,10000,127.56,207.64,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-P20U-05-S','MiniKros','MINIKROS 65CM 0.2UM PES 0.5MM 1.5TC X 3/4TC STERILE','0.2 μm','PES',0.5,70,'65',1000,1.125,26.6,25.6,24.6,10000,127.56,207.64,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-P20U-10-N','MiniKros','MINIKROS 65CM 0.2UM PES 1MM 1.5TC X 3/4TC','0.2 μm','PES',1.0,70,'65',250,1.125,53.1,51.2,49.2,5100,127.56,207.64,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-P20U-10-S','MiniKros','MINIKROS 65CM 0.2UM PES 1MM 1.5TC X 3/4TC STERILE','0.2 μm','PES',1.0,70,'65',250,1.125,53.1,51.2,49.2,5100,127.56,207.64,2945,8836,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S010-05-N','MiniKros','MINIKROS 65CM 10KD PS 0.5MM 1.5TC X 3/4TC','10 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S010-05-P','MiniKros','MINIKROS 65CM 10KD PS 0.5MM 1.5TC X 3/4TC WET','10 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S010-05-S','MiniKros','MINIKROS 65CM 10KD PS 0.5MM 1.5TC X 3/4TC STERILE','10 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S050-05-N','MiniKros','MINIKROS 65CM 50KD PS 0.5MM 1.5TC X 3/4TC','50 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S050-05-P','MiniKros','MINIKROS 65CM 50KD PS 0.5MM 1.5TC X 3/4TC WET','50 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S050-05-S','MiniKros','MINIKROS 65CM 50KD PS 0.5MM 1.5TC X 3/4TC STERILE','50 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S05U-05-N','MiniKros','MINIKROS 65CM 0.05UM PS 0.5MM 1.5TC X 3/4TC','0.05 μm','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S05U-05-P','MiniKros','MINIKROS 65CM 0.05UM PS 0.5MM 1.5TC X 3/4TC WET','0.05 μm','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S05U-05-S','MiniKros','MINIKROS 65CM 0.05UM PS 0.5MM 1.5TC X 3/4TC STERILE','0.05 μm','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S500-05-N','MiniKros','MINIKROS 65CM 500KD PS 0.5MM 1.5TC X 3/4TC','500 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S500-05-P','MiniKros','MINIKROS 65CM 500KD PS 0.5MM 1.5TC X 3/4TC WET','500 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('N06-S500-05-S','MiniKros','MINIKROS 65CM 500KD PS 0.5MM 1.5TC X 3/4TC STERILE','500 kD','PS',0.5,70,'65',1000,1.125,26.8,23.6,20.5,10000,127.56,239.02,1473,4418,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E001-05-N','MiniKros Sampler','SAMPLER 20CM 1K MPES .5MM 3/4TCX3/4TC 1/PK','1 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E001-05-S','MiniKros Sampler','SAMPLER 20CM 1K MPES .5MM 3/4TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E003-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 0.5MM 3/4TC X 3/4TC','3 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E003-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 0.5MM 3/4TC X 3/4TC STERILE','3 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E003-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 1MM 3/4TC X 3/4TC','3 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E003-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 3K MPES 1MM 3/4TC X 3/4TC STERILE','3 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E005-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 0.5MM 3/4TC X 3/4TC','5 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E005-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 0.5MM 3/4TC X 3/4TC STERILE','5 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E005-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 1MM 3/4TC X 3/4TC','5 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E005-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 5K MPES 1MM 3/4TC X 3/4TC STERILE','5 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E010-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 0.5MM 3/4TC X 3/4TC','10 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,26.0,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E010-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 0.5MM 3/4TC X 3/4TC IRR','10 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,26.0,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E010-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 1MM 3/4TC X 3/4TC','10 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E010-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 10K MPES 1MM 3/4TC X 3/4TC STERILE','10 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E030-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 0.5MM 3/4TC X 3/4TC','30 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E030-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 0.5MM 3/4TC X 3/4TC STERILE','30 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E030-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 1MM 3/4TC X 3/4TC','30 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E030-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 30K MPES 1MM 3/4TC X 3/4TC STERILE','30 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E050-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 0.5MM 3/4TC X 3/4TC','50 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E050-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 0.5MM 3/4TC X 3/4TC STERILE','50 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E050-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 1MM 3/4TC X 3/4TC','50 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E050-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 50K MPES 1MM 3/4TC X 3/4TC STERILE','50 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E070-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 0.5MM 3/4TC X 3/4TC','70 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E070-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 0.5MM 3/4TC X 3/4TC STERILE','70 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E070-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 1MM 3/4TC X 3/4TC','70 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E070-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 70K MPES 1MM 3/4TC X 3/4TC STERILE','70 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E100-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 0.5MM 3/4TC X 3/4TC','100 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E100-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 0.5MM 3/4TC X 3/4TC STERILE','100 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E100-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 1MM 3/4TC X 3/4TC','100 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E100-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 100K MPES 1MM 3/4TC X 3/4TC STERILE','100 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC','0.2 μm','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC STERILE','0.2 μm','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E20U-10-N','MiniKros Sampler','SAMPLER 20CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK','0.2 μm','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E20U-10-S','MiniKros Sampler','SAMPLER 20CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK STERILE','0.2 μm','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E300-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 0.5MM 3/4TC X 3/4TC','300 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E300-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 0.5MM 3/4TC X 3/4TC STERILE','300 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E300-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 1MM 3/4TC X 3/4TC','300 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E300-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 300K MPES 1MM 3/4TC X 3/4TC STERILE','300 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E45U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM .45UM MPES 0.5MM 3/4TC X 3/4TC','0.45 μm','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,'Special Part for Mike B not released as standard');
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E500-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 0.5MM 3/4TC X 3/4TC','500 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E500-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 0.5MM 3/4TC X 3/4TC STERILE','500 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E500-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 1MM 3/4TC X 3/4TC','500 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E500-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 500K MPES 1MM 3/4TC X 3/4TC STERILE','500 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E65U-07-N','MiniKros Sampler','SAMPLER 20CM .65UM MPES .75MM 3/4TCX3/4TC 1/PK','0.65 μm','mPES',0.75,25,'20',110,0.625,46.2,43.0,38.8,520,9.71,19.61,547,1640,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E65U-07-S','MiniKros Sampler','SAMPLER 20CM .65UM MPES .75MM 3/4TCX3/4TC STERILE 1/PK','0.65 μm','mPES',0.75,25,'20',110,0.625,46.2,43.0,38.8,520,9.71,19.61,547,1640,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E750-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 750K MPES 0.5MM 3/4TC X 3/4TC','750 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E750-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 750K MPES 0.5MM 3/4TC X 3/4TC STERILE','750 kD','mPES',0.5,25,'20',250,0.625,31.5,29.5,27.5,790,9.81,18.22,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E750-10-N','MiniKros Sampler','SAMPLER 20CM 750KD MPES 1MM 3/4TCX3/4TC 1/PK','750 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-E750-10-S','MiniKros Sampler','SAMPLER 20CM 750KD MPES 1MM 3/4TCX3/4TC STERILE 1/PK','750 kD','mPES',1.0,25,'20',78,0.625,57.0,52.5,48.0,490,12.25,18.47,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-M10U-06-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.1UM ME 0.6MM 3/4TC X 3/4TC','0.1 μm','ME',0.63,25,'20',180,0.625,37.0,34.0,31.0,720,11.22,19.15,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-M10U-06-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.1UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.1 μm','ME',0.63,25,'20',180,0.625,37.0,34.0,31.0,720,11.22,19.15,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-M20U-06-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 0.6MM 3/4TC X 3/4TC','0.2 μm','ME',0.63,25,'20',180,0.625,37.0,34.0,31.0,720,11.22,19.15,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-M20U-06-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.2 μm','ME',0.63,25,'20',180,0.625,37.0,34.0,31.0,720,11.22,19.15,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-M20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 1MM 3/4TC X 3/4TC','0.2 μm','ME',1.0,25,'20',80,0.625,54.0,50.0,46.0,500,12.56,19.94,942,2827,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-M20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM ME 1MM 3/4TC X 3/4TC STERILE','0.2 μm','ME',1.0,25,'20',80,0.625,54.0,50.0,46.0,500,12.56,19.94,942,2827,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-P20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 0.5MM 3/4TC X 3/4TC','0.2 μm','PES',0.5,25,'20',330,0.625,26.6,25.6,24.6,1000,12.95,18.35,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-P20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 0.5MM 3/4TC X 3/4TC STERILE','0.2 μm','PES',0.5,25,'20',330,0.625,26.6,25.6,24.6,1000,12.95,18.35,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-P20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 1MM 3/4TC X 3/4TC','0.2 μm','PES',1.0,25,'20',75,0.625,53.1,51.2,49.2,470,11.78,20.27,884,2651,25,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-P20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM PES 1MM 3/4TC X 3/4TC STERILE','0.2 μm','PES',1.0,25,'20',75,0.625,53.1,51.2,49.2,470,11.78,20.27,884,2651,25,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S010-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 10KD PS 0.5MM 3/4TC X 3/4TC','10 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S010-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 10KD PS 0.5MM 3/4TC X 3/4TC WET','10 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S010-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 10KD PS 0.5MM 3/4TC X 3/4TC STERILE','10 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S050-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 50KD PS 0.5MM 3/4TC X 3/4TC','50 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S050-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 50KD PS 0.5MM 3/4TC X 3/4TC WET','50 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S050-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 50KD PS 0.5MM 3/4TC X 3/4TC STERILE','50 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S05U-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC','0.05 μm','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S05U-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC WET','0.05 μm','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S05U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.05 PS 0.5MM 3/4TC X 3/4TC STERILE','0.05 μm','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S500-05-N','MiniKros Sampler','MINIKROS SAMPLER 20CM 500KD PS 0.5MM 3/4TC X 3/4TC','500 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S500-05-P','MiniKros Sampler','MINIKROS SAMPLER 20CM 500KD PS 0.5MM 3/4TC X 3/4TC WET','500 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S02-S500-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 500KD PS 0.5MM 3/4TC X 3/4TC STERILE','500 kD','PS',0.5,25,'20',330,0.625,26.8,23.6,20.5,1000,12.95,21.53,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E001-05-N','MiniKros Sampler','SAMPLER 41.5CM 1K MPES .5MM 3/4TCX3/4TC 1/PK','1 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E001-05-S','MiniKros Sampler','SAMPLER 41.5CM 1K MPES .5MM 3/4TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E003-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 0.5MM 3/4TC X 3/4TC','3 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E003-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 0.5MM 3/4TC X 3/4TC STERILE','3 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E003-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 1MM 3/4TC X 3/4TC','3 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E003-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 3K MPES 1MM 3/4TC X 3/4TC STERILE','3 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E005-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 0.5MM 3/4TC X 3/4TC','5 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E005-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 0.5MM 3/4TC X 3/4TC STERILE','5 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E005-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 1MM 3/4TC X 3/4TC','5 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E005-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 5K MPES 1MM 3/4TC X 3/4TC STERILE','5 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E010-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 0.5MM 3/4TC X 3/4TC','10 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,26.0,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E010-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 0.5MM 3/4TC X 3/4TC STERILE','10 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,26.0,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E010-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 1MM 3/4TC X 3/4TC','10 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E010-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10K MPES 1MM 3/4TC X 3/4TC STERILE','10 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E030-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 0.5MM 3/4TC X 3/4TC','30 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E030-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 0.5MM 3/4TC X 3/4TC STERILE','30 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E030-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 1MM 3/4TC X 3/4TC','30 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E030-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 30K MPES 1MM 3/4TC X 3/4TC STERILE','30 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E050-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 0.5MM 3/4TC X 3/4TC','50 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E050-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 0.5MM 3/4TC X 3/4TC STERILE','50 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E050-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 1MM 3/4TC X 3/4TC','50 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E050-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50K MPES 1MM 3/4TC X 3/4TC STERILE','50 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E070-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 0.5MM 3/4TC X 3/4TC','70 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E070-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 0.5MM 3/4TC X 3/4TC STERILE','70 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E070-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 1MM 3/4TC X 3/4TC','70 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E070-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 70K MPES 1MM 3/4TC X 3/4TC STERILE','70 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E100-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 0.5MM 3/4TC X 3/4TC','100 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E100-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 0.5MM 3/4TC X 3/4TC STERIL','100 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E100-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 1MM 3/4TC X 3/4TC','100 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E100-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 100K MPES 1MM 3/4TC X 3/4TC STERILE','100 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC','0.2 μm','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 20CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC STERL','0.2 μm','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E20U-10-N','MiniKros Sampler','SAMPLER 41.5CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK','0.2 μm','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E20U-10-S','MiniKros Sampler','SAMPLER 41.5CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK STERILE','0.2 μm','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E300-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 0.5MM 3/4TC X 3/4TC','300 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E300-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 0.5MM 3/4TC X 3/4TC STERIL','300 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E300-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 1MM 3/4TC X 3/4TC','300 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E300-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 300K MPES 1MM 3/4TC X 3/4TC STERILE','300 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E500-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 0.5MM 3/4TC X 3/4TC','500 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E500-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 0.5MM 3/4TC X 3/4TCSTERILE','500 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E500-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 1MM 3/4TC X 3/4TC','500 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E500-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500K MPES 1MM 3/4TC X 3/4TC STERILE','500 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E65U-07-N','MiniKros Sampler','SAMPLER 41.5CM .65UM MPES .75MM 3/4TCX3/4TC 1/PK','0.65 μm','mPES',0.75,47,'41.5',110,0.625,46.2,43.0,38.8,1075,20.16,40.69,547,1640,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E65U-07-S','MiniKros Sampler','SAMPLER 41.5CM .65UM MPES .75MM 3/4TCX3/4TC STERILE 1/PK','0.65 μm','mPES',0.75,47,'41.5',110,0.625,46.2,43.0,38.8,1075,20.16,40.69,547,1640,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E750-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 750K MPES 0.5MM 3/4TC X 3/4TC','750 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E750-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 750K MPES 0.5MM 3/4TC X 3/4TC STERI','750 kD','mPES',0.5,47,'41.5',250,0.625,31.5,29.5,27.5,1600,20.36,37.8,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E750-10-N','MiniKros Sampler','SAMPLER 41.5CM 750KD MPES 1MM 3/4TCX3/4TC 1/PK','750 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-E750-10-S','MiniKros Sampler','SAMPLER 41.5CM 750KD MPES 1MM 3/4TCX3/4TC STERILE 1/PK','750 kD','mPES',1.0,47,'41.5',78,0.625,57.0,52.5,48.0,1000,25.41,38.33,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-M10U-06-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.1UM ME 0.6MM 3/4TC X 3/4TC','0.1 μm','ME',0.63,47,'41.5',180,0.625,37.0,34.0,31.0,1500,23.27,39.73,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-M10U-06-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.1UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.1 μm','ME',0.63,47,'41.5',180,0.625,37.0,34.0,31.0,1500,23.27,39.73,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-M20U-06-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 0.6MM 3/4TC X 3/4TC','0.2 μm','ME',0.63,47,'41.5',180,0.625,37.0,34.0,31.0,1500,23.27,39.73,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-M20U-06-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.2 μm','ME',0.63,47,'41.5',180,0.625,37.0,34.0,31.0,1500,23.27,39.73,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-M20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 1MM 3/4TC X 3/4TC','0.2 μm','ME',1.0,47,'41.5',80,0.625,54.0,50.0,46.0,1050,26.06,41.38,942,2827,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-M20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM ME 1MM 3/4TC X 3/4TC STERILE','0.2 μm','ME',1.0,47,'41.5',80,0.625,54.0,50.0,46.0,1050,26.06,41.38,942,2827,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-P20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 0.5MM 3/4TC X 3/4TC','0.2 μm','PES',0.5,47,'41.5',330,0.625,26.6,25.6,24.6,2200,26.88,38.07,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-P20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 0.5MM 3/4TCX3/4TC STERILE','0.2 μm','PES',0.5,47,'41.5',330,0.625,26.6,25.6,24.6,2200,26.88,38.07,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-P20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 1MM 3/4TC X 3/4TC','0.2 μm','PES',1.0,47,'41.5',75,0.625,53.1,51.2,49.2,980,24.43,42.07,884,2651,25,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-P20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.2UM PES 1MM 3/4TC X 3/4TC STERILE','0.2 μm','PES',1.0,47,'41.5',75,0.625,53.1,51.2,49.2,980,24.43,42.07,884,2651,25,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S010-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10KD PS 0.5MM 3/4TC X 3/4TC','10 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S010-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10KD PS 0.5MM 3/4TC X 3/4TC WET','10 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S010-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 10KD PS 0.5MM 3/4TC X 3/4TC STERILE','10 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S050-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50KD PS 0.5MM 3/4TC X 3/4TC','50 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S050-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50KD PS 0.5MM 3/4TC X 3/4TC WET','50 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S050-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 50KD PS 0.5MM 3/4TC X 3/4TC STERILE','50 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S05U-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC','0.05 μm','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S05U-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC WET','0.05 μm','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S05U-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 0.05UM PS0.5MM 3/4TC X 3/4TC STERILE','0.05 μm','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S500-05-N','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500KD PS 0.5MM 3/4TC X 3/4TC','500 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S500-05-P','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500KD PS 0.5MM 3/4TC X 3/4TC WET','500 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S04-S500-05-S','MiniKros Sampler','MINIKROS SAMPLER 41.5CM 500KD PS 0.5MM 3/4TC X 3/4TC STERILE','500 kD','PS',0.5,47,'41.5',330,0.625,26.8,23.6,20.5,2200,26.88,44.68,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E001-05-N','MiniKros Sampler','SAMPLER 65CM 1K MPES .5MM 3/4TCX3/4TC 1/PK','1 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E001-05-S','MiniKros Sampler','SAMPLER 65CM 1K MPES .5MM 3/4TCX3/4TC 1/PK STERILE','1 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E003-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 0.5MM 3/4TC X 3/4TC','3 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E003-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 0.5MM 3/4TC X 3/4TC STERILE','3 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E003-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 1MM 3/4TC X 3/4TC','3 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E003-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 3K MPES 1MM 3/4TC X 3/4TC STERILE','3 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E005-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 0.5MM 3/4TC X 3/4TC','5 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E005-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 0.5MM 3/4TC X 3/4TC STERILE','5 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E005-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 1MM 3/4TC X 3/4TC','5 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E005-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 5K MPES 1MM 3/4TC X 3/4TC STERILE','5 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E010-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 0.5MM 3/4TC X 3/4TC','10 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,26.0,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E010-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 0.5MM 3/4TC X 3/4TC STERILE','10 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,26.0,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E010-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 1MM 3/4TC X 3/4TC','10 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E010-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 10K MPES 1MM 3/4TC X 3/4TC STERILE','10 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E030-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 0.5MM 3/4TC X 3/4TC','30 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E030-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 0.5MM 3/4TC X 3/4TC STERILE','30 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E030-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 1MM 3/4TC X 3/4TC','30 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E030-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 30K MPES 1MM 3/4TC X 3/4TC STERILE','30 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E050-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 0.5MM 3/4TC X 3/4TC','50 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E050-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 0.5MM 3/4TC X 3/4TC STERILE','50 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E050-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 1MM 3/4TC X 3/4TC','50 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E050-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 50K MPES 1MM 3/4TC X 3/4TC STERILE','50 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E070-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 0.5MM 3/4TC X 3/4TC','70 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E070-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 0.5MM 3/4TC X 3/4TC STERILE','70 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E070-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 1MM 3/4TC X 3/4TC','70 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E070-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 70K MPES 1MM 3/4TC X 3/4TC STERILE','70 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E100-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 0.5MM 3/4TC X 3/4TC','100 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E100-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 0.5MM 3/4TC X 3/4TC STERILE','100 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E100-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 1MM 3/4TC X 3/4TC','100 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E100-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 100K MPES 1MM 3/4TC X 3/4TC STERILE','100 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC','0.2 μm','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM MPES 0.5MM 3/4TC X 3/4TC STERILE','0.2 μm','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E20U-10-N','MiniKros Sampler','SAMPLER 65CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK','0.2 μm','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E20U-10-S','MiniKros Sampler','SAMPLER 65CM .2UM MPES 1MM 3/4TCX3/4TC 1/PK STERILE','0.2 μm','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E300-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 0.5MM 3/4TC X 3/4TC','300 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E300-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 0.5MM 3/4TC X 3/4TC STERILE','300 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E300-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 1MM 3/4TC X 3/4TC','300 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E300-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 300K MPES 1MM 3/4TC X 3/4TC STERILE','300 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E500-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 0.5MM 3/4TC X 3/4TC','500 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E500-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 0.5MM 3/4TC X 3/4TC STERILE','500 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E500-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 1MM 3/4TC X 3/4TC','500 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E500-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 500K MPES 1MM 3/4TC X 3/4TC STERILE','500 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E65U-07-N','MiniKros Sampler','SAMPLER 65CM .65UM MPES .75MM 3/4TCX3/4TC 1/PK','0.65 μm','mPES',0.75,70,'65',110,0.625,46.2,43.0,38.8,1700,31.57,63.73,547,1640,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E65U-07-S','MiniKros Sampler','SAMPLER 65CM .65UM MPES .75MM 3/4TCX3/4TC STERILE 1/PK','0.65 μm','mPES',0.75,70,'65',110,0.625,46.2,43.0,38.8,1700,31.57,63.73,547,1640,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E750-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 750K MPES 0.5MM 3/4TC X 3/4 TC','750 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E750-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 750K MPES 0.5MM 3/4TC X 3/4TC STERILE','750 kD','mPES',0.5,70,'65',250,0.625,31.5,29.5,27.5,2600,31.89,59.21,368,1104,16,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E750-10-N','MiniKros Sampler','SAMPLER 65CM 750KD MPES 1MM 3/4TCX3/4TC 1/PK','750 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-E750-10-S','MiniKros Sampler','SAMPLER 65CM 750KD MPES 1MM 3/4TCX3/4TC STERILE 1/PK','750 kD','mPES',1.0,70,'65',78,0.625,57.0,52.5,48.0,1600,39.8,60.03,919,2757,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-M10U-06-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.1 UM ME 0.6MM 3/4TC X 3/4TC','0.1 μm','ME',0.63,70,'65',180,0.625,37.0,34.0,31.0,2350,36.45,62.23,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-M10U-06-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.1UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.1 μm','ME',0.63,70,'65',180,0.625,37.0,34.0,31.0,2350,36.45,62.23,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-M20U-06-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 0.6MM 3/4TC X 3/4TC','0.2 μm','ME',0.63,70,'65',180,0.625,37.0,34.0,31.0,2350,36.45,62.23,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-M20U-06-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 0.6MM 3/4TC X 3/4TC STERILE','0.2 μm','ME',0.63,70,'65',180,0.625,37.0,34.0,31.0,2350,36.45,62.23,530,1591,25,18,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-M20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 1MM 3/4TC X 3/4TC','0.2 μm','ME',1.0,70,'65',80,0.625,54.0,50.0,46.0,1650,40.82,64.81,942,2827,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-M20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM ME 1MM 3/4TC X 3/4TC STERILE','0.2 μm','ME',1.0,70,'65',80,0.625,54.0,50.0,46.0,1650,40.82,64.81,942,2827,17,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-P20U-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 0.5MM 3/4TC X 3/4TC','0.2 μm','PES',0.5,70,'65',330,0.625,26.6,25.6,24.6,3400,42.1,59.62,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-P20U-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 0.5MM 3/4TC X 3/4TC STERILE','0.2 μm','PES',0.5,70,'65',330,0.625,26.6,25.6,24.6,3400,42.1,59.62,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-P20U-10-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 1MM 3/4TC X 3/4TC','0.2 μm','PES',1.0,70,'65',75,0.625,53.1,51.2,49.2,1500,38.27,65.89,884,2651,25,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-P20U-10-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.2UM PES 1MM 3/4TC X 3/4TC STERILE','0.2 μm','PES',1.0,70,'65',75,0.625,53.1,51.2,49.2,1500,38.27,65.89,884,2651,25,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S010-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 10KD PS 0.5MM 3/4TC X 3/4TC','10 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S010-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 10KD PS 0.5MM 3/4TC X 3/4TC WET','10 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S010-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 10KD PS 0.5MM 3/4TC X 3/4TC STERILE','10 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S050-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 50KD PS 0.5MM 3/4TC X 3/4TC','50 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S050-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 50KD PS 0.5MM 3/4TC X 3/4TC WET','50 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S050-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 50KD PS 0.5MM 3/4TC X 3/4TC STERILE','50 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S05U-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC','0.05 μm','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S05U-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.05 UM PS 0.5MM 3/4TC X 3/4TC WET','0.05 μm','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S05U-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 0.05 PS 0.5MM 3/4TC X 3/4TC STERILE','0.05 μm','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S500-05-N','MiniKros Sampler','MINIKROS SAMPLER 65CM 500KD PS 0.5MM 3/4TC X 3/4TC','500 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S500-05-P','MiniKros Sampler','MINIKROS SAMPLER 65CM 500KD PS 0.5MM 3/4TC X 3/4TC WET','500 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('S06-S500-05-S','MiniKros Sampler','MINIKROS SAMPLER 65CM 500KD PS 0.5MM 3/4TC X 3/4TC STERILE','500 kD','PS',0.5,70,'65',330,0.625,26.8,23.6,20.5,3400,42.1,69.98,486,1458,25,17,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E001-05-N','MidiKros TC','MIDIKROS TC 20CM 1K MPES .5MM 1/2TCXFLL 1/PK','1 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E001-05-S','MidiKros TC','MIDIKROS TC 20CM 1K MPES .5MM 1/2TCXFLL 1/PK STERILE','1 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E003-05-N','MidiKros TC','MIDIKROS 20CM 3K MPES 0.5MM TC X FLL 1/PK','3 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E003-05-S','MidiKros TC','MIDIKROS 20CM 3K MPES 0.5MM TC X FLL 1/PK STERILE','3 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E003-10-N','MidiKros TC','MIDIKROS 20CM 3K MPES 1MM TC X FLL 1/PK','3 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E003-10-S','MidiKros TC','MIDIKROS 20CM 3K MPES 1MM TC X FLL 1/PK STERILE','3 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E005-05-N','MidiKros TC','MIDIKROS 20CM 5K MPES 0.5MM TC X FLL 1/PK','5 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E005-05-S','MidiKros TC','MIDIKROS 20CM 5K MPES 0.5MM TC X FLL 1/PK STERILE','5 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E005-10-N','MidiKros TC','MIDIKROS 20CM 5K MPES 1MM TC X FLL 1/PK','5 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E005-10-S','MidiKros TC','MIDIKROS 20CM 5K MPES 1MM TC X FLL 1/PK STERILE','5 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E010-05-N','MidiKros TC','MIDIKROS 20CM 10K MPES 0.5MM TC X FLL 1/PK','10 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,26.0,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E010-05-S','MidiKros TC','MIDIKROS 20CM 10K MPES 0.5MM TC X FLL 1/PK STERILE','10 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,26.0,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E010-10-N','MidiKros TC','MIDIKROS 20CM 10K MPES 1MM TC X FLL 1/PK','10 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E010-10-S','MidiKros TC','MIDIKROS 20CM 10K MPES 1MM TC X FLL 1/PK STERILE','10 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E030-05-N','MidiKros TC','MIDIKROS 20CM 30K MPES 0.5MM TC X FLL 1/PK','30 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E030-05-S','MidiKros TC','MIDIKROS 20CM 30K MPES 0.5MM TC X FLL 1/PK STERILE','30 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E030-10-N','MidiKros TC','MIDIKROS 20CM 30K MPES 1MM TC X FLL 1/PK','30 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E030-10-S','MidiKros TC','MIDIKROS 20CM 30K MPES 1MM TC X FLL 1/PK STERILE','30 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E050-05-N','MidiKros TC','MIDIKROS 20CM 50K MPES 0.5MM TC X FLL 1/PK','50 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E050-05-S','MidiKros TC','MIDIKROS 20CM 50K MPES 0.5MM TC X FLL 1/PK STTERILE','50 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E050-10-N','MidiKros TC','MIDIKROS 20CM 50K MPES 1MM TC X FLL 1/PK','50 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E050-10-S','MidiKros TC','MIDIKROS 20CM 50K MPES 1MM TC X FLL 1/PK STERILE','50 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E070-05-N','MidiKros TC','MIDIKROS 20CM 70K MPES 0.5MM TC X FLL 1/PK','70 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E070-05-S','MidiKros TC','MIDIKROS 20CM 70K MPES 0.5MM TC X FLL 1/PK STERILE','70 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E070-10-N','MidiKros TC','MIDIKROS 20CM 70K MPES 1MM TC X FLL 1/PK','70 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E070-10-S','MidiKros TC','MIDIKROS 20CM 70K MPES 1MM TC X FLL 1/PK IRR','70 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E100-05-N','MidiKros TC','MIDIKROS 20CM 100K MPES 0.5MM TC X FLL 1/PK','100 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E100-05-S','MidiKros TC','MIDIKROS 20CM 100K MPES 0.5MM TC X FLL 1/PK STERILE','100 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E100-10-N','MidiKros TC','MIDIKROS 20CM 100K MPES 1MM TC X FLL 1/PK','100 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E100-10-S','MidiKros TC','MIDIKROS 20CM 100K MPES 1MM TC X FLL 1/PK STERILE','100 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E20U-05-N','MidiKros TC','MIDIKROS 20CM 0.2UM MPES 0.5MM TC X FLL 1/PK','0.2 μm','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E20U-05-S','MidiKros TC','MIDIKROS 20CM 0.2UM MPES 0.5MM TC X FLL 1/PK STERILE','0.2 μm','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E20U-10-N','MidiKros TC','MIDIKROS TC 20CM .2UM MPES 1MM 1/2TCXFLL 1/PK','0.2 μm','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E20U-10-S','MidiKros TC','MIDIKROS TC 20CM .2UM MPES 1MM 1/2TCXFLL 1/PK STERILE','0.2 μm','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E300-05-N','MidiKros TC','MIDIKROS 20CM 300K MPES 0.5MM TC X FLL 1/PK','300 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E300-05-S','MidiKros TC','MIDIKROS 20CM 300K MPES 0.5MM TC X FLL 1/PK STERILE','300 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E300-10-N','MidiKros TC','MIDIKROS 20CM 300K MPES 1MM TC X FLL 1/PK','300 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E300-10-S','MidiKros TC','MIDIKROS 20CM 300K MPES 1MM TC X FLL 1/PK STERILE','300 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E500-05-N','MidiKros TC','MIDIKROS 20CM 500K MPES 0.5MM TC X FLL 1/PK','500 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E500-05-S','MidiKros TC','MIDIKROS 20CM 500K MPES 0.5MM TC X FLL 1/PK STERILE','500 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E500-10-N','MidiKros TC','MIDIKROS 20CM 500K MPES 1MM TC X FLL 1/PK','500 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E500-10-S','MidiKros TC','MIDIKROS 20CM 500K MPES 1MM TC X FLL 1/PK STERILE','500 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E65U-07-N','MidiKros TC','MIDIKROS TC 20CM .65UM MPES .75MM 1/2TCXFLL 1/PK','0.65 μm','mPES',0.75,23,'20',18,0.275,46.2,43.0,38.8,85,1.59,4.39,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E65U-07-S','MidiKros TC','MIDIKROS TC 20CM .65UM MPES .75MM 1/2TCXFLL STERILE 1/PK','0.65 μm','mPES',0.75,23,'20',18,0.275,46.2,43.0,38.8,85,1.59,4.39,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E750-05-N','MidiKros TC','MIDIKROS 20CM 750K MPES 0.5MM TC X FLL 1/PK','750 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E750-05-S','MidiKros TC','MIDIKROS 20CM 750K MPES 0.5MM TC X FLL 1/PK STERILE','750 kD','mPES',0.5,23,'20',36,0.275,31.5,29.5,27.5,115,1.41,4.59,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E750-10-N','MidiKros TC','MIDIKROS TC 20CM 750KD MPES 1MM 1/2TCXFLL 1/PK','750 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-E750-10-S','MidiKros TC','MIDIKROS TC 20CM 750KD MPES 1MM 1/2TCXFLL STERILE 1/PK','750 kD','mPES',1.0,23,'20',12,0.275,57.0,52.5,48.0,75,1.88,4.41,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-M10U-06-N','MidiKros TC','MIDIKROS 20CM 0.1UM ME 0.6MM TC X FLL 1/PK','0.1 μm','ME',0.63,23,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-M10U-06-S','MidiKros TC','MIDIKROS 20CM 0.1UM ME 0.6MM TC X FLL 1/PK STERILE','0.1 μm','ME',0.63,23,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-M20U-06-N','MidiKros TC','MIDIKROS 20CM 0.2UM ME 0.6MM TC X FLL 1/PK','0.2 μm','ME',0.63,23,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-M20U-06-S','MidiKros TC','MIDIKROS 20CM 0.2UM ME 0.6MM TC X FLL 1/PK STERILE','0.2 μm','ME',0.63,23,'20',26,0.275,37.0,34.0,31.0,105,1.62,4.71,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-M20U-10-N','MidiKros TC','MIDIKROS 20CM 0.2UM ME 1MM TC X FLL 1/PK','0.2 μm','ME',1.0,23,'20',15,0.275,54.0,50.0,46.0,94,2.36,3.98,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-M20U-10-S','MidiKros TC','MIDIKROS 20CM 0.2UM ME 1MM TC X FLL 1/PK STERILE','0.2 μm','ME',1.0,23,'20',15,0.275,54.0,50.0,46.0,94,2.36,3.98,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-P20U-05-N','MidiKros TC','MIDIKROS 20CM 0.2UM PES 0.5MM TC X FLL 1/PK','0.2 μm','PES',0.5,23,'20',45,0.275,26.6,25.6,24.6,140,1.77,4.77,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-P20U-05-S','MidiKros TC','MIDIKROS 20CM 0.2UM PES 0.5MM TC X FLL 1/PK STERILE','0.2 μm','PES',0.5,23,'20',45,0.275,26.6,25.6,24.6,140,1.77,4.77,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-P20U-10-N','MidiKros TC','MIDIKROS 20CM 0.2UM PES 1MM TC X FLL 1/PK','0.2 μm','PES',1.0,23,'20',14,0.275,53.1,51.2,49.2,88,2.2,4.06,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-P20U-10-S','MidiKros TC','MIDIKROS 20CM 0.2UM PES 1MM TC X FLL 1/PK STERILE','0.2 μm','PES',1.0,23,'20',14,0.275,53.1,51.2,49.2,88,2.2,4.06,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S010-05-N','MidiKros TC','MIDIKROS 20CM 10KD PS 0.5MM TC X FLL 1/PK','10 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S010-05-P','MidiKros TC','MIDIKROS 20CM 10KD PS 0.5MM TC X FLL 1/PK WET','10 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S010-05-S','MidiKros TC','MIDIKROS 20CM 10KD PS 0.5MM TC X FLL 1/PK STERILE','10 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S050-05-N','MidiKros TC','MIDIKROS 20CM 50KD PS 0.5MM TC X FLL 1/PK','50 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S050-05-P','MidiKros TC','MIDIKROS 20CM 50KD PS 0.5MM TC X FLL 1/PK WET','50 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S050-05-S','MidiKros TC','MIDIKROS 20CM 50KD PS 0.5MM TC X FLL 1/PK STERILE','50 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S05U-05-N','MidiKros TC','MIDIKROS 20CM 0.05UM PS 0.5MM TC X FLL 1/PK','0.05 μm','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S05U-05-P','MidiKros TC','MIDIKROS 20CM 0.05UM PS 0.5MM TC X FLL 1/PK WET','0.05 μm','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S05U-05-S','MidiKros TC','MIDIKROS 20CM 0.05UM PS 0.5MM TC X FLL 1/PK STERILE','0.05 μm','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S500-05-N','MidiKros TC','MIDIKROS 20CM 500KD PS 0.5MM TC X FLL 1/PK','500 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S500-05-P','MidiKros TC','MIDIKROS 20CM 500KD PS 0.5MM TC X FLL 1/PK WET','500 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T02-S500-05-S','MidiKros TC','MIDIKROS 20CM 500KD PS 0.5MM TC X FLL 1/PK STERILE','500 kD','PS',0.5,23,'20',60,0.275,26.8,23.6,20.5,190,2.36,4.38,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E001-05-N','MidiKros TC','MIDIKROS TC 41.5CM 1K MPES .5MM 1/2TCXFLL 1/PK','1 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E001-05-S','MidiKros TC','MIDIKROS TC 41.5CM 1K MPES .5MM 1/2TCXFLL 1/PK STERILE','1 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E003-05-N','MidiKros TC','MIDIKROS 41.5CM 3K MPES 0.5MM TC X FLL 1/PK','3 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E003-05-S','MidiKros TC','MIDIKROS 41.5CM 3K MPES 0.5MM TC X FLL 1/PK STERILE','3 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E003-10-N','MidiKros TC','MIDIKROS 41.5CM 3K MPES 1MM TC X FLL 1/PK','3 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E003-10-S','MidiKros TC','MIDIKROS 41.5CM 3K MPES 1MM TC X FLL 1/PK STERILE','3 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E005-05-N','MidiKros TC','MIDIKROS 41.5CM 5K MPES 0.5MM TC X FLL 1/PK','5 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E005-05-S','MidiKros TC','MIDIKROS 41.5CM 5K MPES 0.5MM TC X FLL 1/PK STERILE','5 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E005-10-N','MidiKros TC','MIDIKROS 41.5CM 5K MPES 1MM TC X FLL 1/PK','5 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E005-10-S','MidiKros TC','MIDIKROS 41.5CM 5K MPES 1MM TC X FLL 1/PK STERILE','5 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E010-05-N','MidiKros TC','MIDIKROS 41.5CM 10K MPES 0.5MM TC X FLL 1/PK','10 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,26.0,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E010-05-S','MidiKros TC','MIDIKROS 41.5CM 10K MPES 0.5MM TC X FLL 1/PK STERIL','10 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,26.0,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E010-10-N','MidiKros TC','MIDIKROS 41.5CM 10K MPES 1MM TC X FLL 1/PK','10 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E010-10-S','MidiKros TC','MIDIKROS 41.5CM 10K MPES 1MM TC X FLL 1/PK STERILE','10 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E030-05-N','MidiKros TC','MIDIKROS 41.5CM 30K MPES 0.5MM TC X FLL 1/PK','30 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E030-05-S','MidiKros TC','MIDIKROS 41.5CM 30K MPES 0.5MM TC X FLL 1/PK STERILE','30 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E030-10-N','MidiKros TC','MIDIKROS 41.5CM 30K MPES 1MM TC X FLL 1/PK','30 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E030-10-S','MidiKros TC','MIDIKROS 41.5CM 30K MPES 1MM TC X FLL 1/PK STERILE','30 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E050-05-N','MidiKros TC','MIDIKROS 41.5CM 50K MPES 0.5MM TC X FLL 1/PK','50 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E050-05-S','MidiKros TC','MIDIKROS 41.5CM 50K MPES 0.5MM TC X FLL 1/PK STERILE','50 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E050-10-N','MidiKros TC','MIDIKROS 41.5CM 50K MPES 1MM TC X FLL 1/PK','50 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E050-10-S','MidiKros TC','MIDIKROS 41.5CM 50K MPES 1MM TC X FLL 1/PK STERILE','50 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E070-05-N','MidiKros TC','MIDIKROS 41.5CM 70K MPES 0.5MM TC X FLL 1/PK','70 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E070-05-S','MidiKros TC','MIDIKROS 41.5CM 70K MPES 0.5MM TC X FLL 1/PK STERILE','70 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E070-10-N','MidiKros TC','MIDIKROS 41.5CM 70K MPES 1MM TC X FLL 1/PK','70 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E070-10-S','MidiKros TC','MIDIKROS 41.5CM 70K MPES 1MM TC X FLL 1/PK IRR','70 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E100-05-N','MidiKros TC','MIDIKROS 41.5CM 100K MPES 0.5MM TC X FLL 1/PK','100 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E100-05-S','MidiKros TC','MIDIKROS 41.5CM 100K MPES 0.5MM TC X FLL 1/PK IRR','100 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E100-10-N','MidiKros TC','MIDIKROS 41.5CM 100K MPES 1MM TC X FLL 1/PK','100 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E100-10-S','MidiKros TC','MIDIKROS 41.5CM 100K MPES 1MM TC X FLL 1/PK STERILE','100 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E20U-05-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM MPES 0.5MM TC X FLL 1/PK','0.2 μm','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E20U-05-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM MPES 0.5MM TC X FLL 1/PK STERILE','0.2 μm','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E20U-10-N','MidiKros TC','MIDIKROS TC 41.5CM .2UM MPES 1MM 1/2TCXFLL 1/PK','0.2 μm','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E20U-10-S','MidiKros TC','MIDIKROS TC 41.5CM .2UM MPES 1MM 1/2TCXFLL 1/PK STERILE','0.2 μm','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E300-05-N','MidiKros TC','MIDIKROS 41.5CM 300K MPES 0.5MM TC X FLL 1/PK','300 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E300-05-S','MidiKros TC','MIDIKROS 41.5CM 300K MPES 0.5MM TC X FLL 1/PK IRR','300 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E300-10-N','MidiKros TC','MIDIKROS 41.5CM 300K MPES 1MM TC X FLL 1/PK','300 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E300-10-S','MidiKros TC','MIDIKROS 41.5CM 300K MPES 1MM TC X FLL 1/PK STERILE','300 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E500-05-N','MidiKros TC','MIDIKROS 41.5CM 500K MPES 0.5MM TC X FLL 1/PK','500 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E500-05-S','MidiKros TC','MIDIKROS 41.5CM 500K MPES 0.5MM TC X FLL 1/PK STERILE','500 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E500-10-N','MidiKros TC','MIDIKROS 41.5CM 500K MPES 1MM TC X FLL 1/PK','500 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E500-10-S','MidiKros TC','MIDIKROS 41.5CM 500K MPES 1MM TC X FLL 1/PK STERILE','500 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E65U-07-N','MidiKros TC','MIDIKROS TC 41.5CM .65UM MPES .75MM 1/2TCXFLL 1/PK','0.65 μm','mPES',0.75,44,'41.5',18,0.275,46.2,43.0,38.8,175,3.3,9.12,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E65U-07-S','MidiKros TC','MIDIKROS TC 41.5CM .65UM MPES .75MM 1/2TCXFLL STERILE 1/PK','0.65 μm','mPES',0.75,44,'41.5',18,0.275,46.2,43.0,38.8,175,3.3,9.12,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E750-05-N','MidiKros TC','MIDIKROS 41.5CM 750K MPES 0.5MM TC X FLL 1/PK','750 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E750-05-S','MidiKros TC','MIDIKROS 20CM 750K MPES 0.5MM TC X FLL 1/PK STERILE','750 kD','mPES',0.5,44,'41.5',36,0.275,31.5,29.5,27.5,235,2.93,9.52,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E750-10-N','MidiKros TC','MIDIKROS TC 41.5CM 750KD MPES 1MM 1/2TCXFLL 1/PK','750 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-E750-10-S','MidiKros TC','MIDIKROS TC 41.5CM 750KD MPES 1MM 1/2TCXFLL STERILE 1/PK','750 kD','mPES',1.0,44,'41.5',12,0.275,57.0,52.5,48.0,155,3.91,9.16,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-M10U-06-N','MidiKros TC','MIDIKROS 41.5CM 0.1UM ME 0.6MM TC X FLL 1/PK','0.1 μm','ME',0.63,44,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-M10U-06-S','MidiKros TC','MIDIKROS 41.5CM 0.1UM ME 0.6MM TC X FLL 1/PK STERILE','0.1 μm','ME',0.63,44,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-M20U-06-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM ME 0.6MM TC X FLL 1/PK','0.2 μm','ME',0.63,44,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-M20U-06-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM ME 0.6MM TC X FLL 1/PK STERILE','0.2 μm','ME',0.63,44,'41.5',26,0.275,37.0,34.0,31.0,215,3.36,9.77,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-M20U-10-N','MidiKros TC','MIDIKROS 41.5CM 0.2 UM ME 1MM TC X FLL 1/PK','0.2 μm','ME',1.0,44,'41.5',15,0.275,54.0,50.0,46.0,195,4.89,8.26,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-M20U-10-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM ME 1MM TC X FLL 1/PK STERILE','0.2 μm','ME',1.0,44,'41.5',15,0.275,54.0,50.0,46.0,195,4.89,8.26,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-P20U-05-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 0.5MM TC X FLL 1/PK','0.2 μm','PES',0.5,44,'41.5',45,0.275,26.6,25.6,24.6,290,3.66,9.89,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-P20U-05-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 0.5MM TC X FLL 1/PK STERILE','0.2 μm','PES',0.5,44,'41.5',45,0.275,26.6,25.6,24.6,290,3.66,9.89,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-P20U-10-N','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 1MM TC X FLL 1/PK','0.2 μm','PES',1.0,44,'41.5',14,0.275,53.1,51.2,49.2,180,4.56,8.42,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-P20U-10-S','MidiKros TC','MIDIKROS 41.5CM 0.2UM PES 1MM TC X FLL 1/PK STERILE','0.2 μm','PES',1.0,44,'41.5',14,0.275,53.1,51.2,49.2,180,4.56,8.42,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S010-05-N','MidiKros TC','MIDIKROS 41.5CM 10KD PS 0.5MM TC X FLL 1/PK','10 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S010-05-P','MidiKros TC','MIDIKROS 41.5CM 10KD PS 0.5MM TC X FLL 1/PK WET','10 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S010-05-S','MidiKros TC','MIDIKROS 41.5CM 10KD PS 0.5MM TC X FLL 1/PK STERILE','10 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S050-05-N','MidiKros TC','MIDIKROS 41.5CM 50KD PS 0.5MM TC X FLL 1/PK','50 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S050-05-P','MidiKros TC','MIDIKROS 41.5CM 50KD PS 0.5MM TC X FLL 1/PK WET','50 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S050-05-S','MidiKros TC','MIDIKROS 41.5CM 50KD PS 0.5MM TC X FLL 1/PK STERILE','50 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S05U-05-N','MidiKros TC','MIDIKROS 41.5CM 0.05UM PS 0.5MM TC X FLL 1/PK','0.05 μm','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S05U-05-P','MidiKros TC','MIDIKROS 41.5CM 0.05UM PS 0.5MM TC X FLL 1/PK WET','0.05 μm','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S05U-05-S','MidiKros TC','MIDIKROS 41.5CM 0.05UM PS 0.5MM TC X FLL 1/PK STERILE','0.05 μm','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S500-05-N','MidiKros TC','MIDIKROS 41.5CM 500KD PS 0.5MM TC X FLL 1/PK','500 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S500-05-P','MidiKros TC','MIDIKROS 41.5CM 500KD PS 0.5MM TC X FLL 1/PK WET','500 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T04-S500-05-S','MidiKros TC','MIDIKROS 41.5CM 500KD PS 0.5MM TC X FLL 1/PK STERILE','500 kD','PS',0.5,44,'41.5',60,0.275,26.8,23.6,20.5,390,4.89,9.09,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E001-05-N','MidiKros TC','MIDIKROS TC 65CM 1K MPES .5MM 1/2TCXFLL 1/PK','1 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E001-05-S','MidiKros TC','MIDIKROS TC 65CM 1K MPES .5MM 1/2TCXFLL 1/PK STERILE','1 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E003-05-N','MidiKros TC','MIDIKROS 65CM 3K MPES 0.5MM TC X FLL 1/PK','3 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E003-05-S','MidiKros TC','MIDIKROS 65CM 3K MPES 0.5MM TC X FLL 1/PK STERILE','3 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E003-10-N','MidiKros TC','MIDIKROS 65CM 3K MPES 1MM TC X FLL 1/PK','3 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E003-10-S','MidiKros TC','MIDIKROS 65CM 3K MPES 1MM TC X FLL 1/PK STERILE','3 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E005-05-N','MidiKros TC','MIDIKROS 65CM 5K MPES 0.5MM TC X FLL 1/PK','5 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E005-05-S','MidiKros TC','MIDIKROS 65CM 5K MPES 0.5MM TC X FLL 1/PK STERILE','5 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E005-10-N','MidiKros TC','MIDIKROS 65CM 5K MPES 1MM TC X FLL 1/PK','5 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E005-10-S','MidiKros TC','MIDIKROS 65CM 5K MPES 1MM TC X FLL 1/PK STERILE','5 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E010-05-N','MidiKros TC','MIDIKROS 65CM 10K MPES 0.5MM TC X FLL 1/PK','10 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,26.0,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E010-05-S','MidiKros TC','MIDIKROS 65CM 10K MPES 0.5MM TC X FLL 1/PK STERILE','10 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,26.0,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E010-10-N','MidiKros TC','MIDIKROS 65CM 10K MPES 1MM TC X FLL 1/PK','10 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E010-10-S','MidiKros TC','MIDIKROS 65CM 10K MPES 1MM TC X FLL 1/PK STERILE','10 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E030-05-N','MidiKros TC','MIDIKROS 65CM 30K MPES 0.5MM TC X FLL 1/PK','30 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E030-05-S','MidiKros TC','MIDIKROS 65CM 30K MPES 0.5MM TC X FLL 1/PK STERILE','30 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E030-10-N','MidiKros TC','MIDIKROS 65CM 30K MPES 1MM TC X FLL 1/PK','30 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E030-10-S','MidiKros TC','MIDIKROS 65CM 30K MPES 1MM TC X FLL 1/PK STERILE','30 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E050-05-N','MidiKros TC','MIDIKROS 65CM 50K MPES 0.5MM TC X FLL 1/PK','50 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E050-05-S','MidiKros TC','MIDIKROS 65CM 50K MPES 0.5MM TC X FLL 1/PK STERILE','50 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E050-10-N','MidiKros TC','MIDIKROS 65CM 50K MPES 1MM TC X FLL 1/PK','50 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E050-10-S','MidiKros TC','MIDIKROS 65CM 50K MPES 1MM TC X FLL 1/PK STERILE','50 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E070-05-N','MidiKros TC','MIDIKROS 65CM 70K MPES 0.5MM TC X FLL 1/PK','70 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E070-05-S','MidiKros TC','MIDIKROS 65CM 70K MPES 0.5MM TC X FLL 1/PK STERILE','70 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E070-10-N','MidiKros TC','MIDIKROS 65CM 70K MPES 1MM TC X FLL 1/PK','70 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E070-10-S','MidiKros TC','MIDIKROS 65CM 70K MPES 1MM TC X FLL 1/PK IRR','70 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E100-05-N','MidiKros TC','MIDIKROS 65CM 100K MPES 0.5MM TC X FLL 1/PK','100 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E100-05-S','MidiKros TC','MIDIKROS 65CM 100K MPES 0.5MM TC X FLL 1/PK IRR','100 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E100-10-N','MidiKros TC','MIDIKROS 65CM 100K MPES 1MM TC X FLL 1/PK','100 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E100-10-S','MidiKros TC','MIDIKROS 65CM 100K MPES 1MM TC X FLL 1/PK STERILE','100 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E20U-05-N','MidiKros TC','MIDIKROS 65CM 0.2UM MPES 0.5MM TC X FLL 1/PK','0.2 μm','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E20U-05-S','MidiKros TC','MIDIKROS 65CM 0.2UM MPES 0.5MM TC X FLL 1/PK STERILE','0.2 μm','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E20U-10-N','MidiKros TC','MIDIKROS TC 65CM .2UM MPES 1MM 1/2TCXFLL 1/PK','0.2 μm','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E20U-10-S','MidiKros TC','MIDIKROS TC 65CM .2UM MPES 1MM 1/2TCXFLL 1/PK STERILE','0.2 μm','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E300-05-N','MidiKros TC','MIDIKROS 65CM 300K MPES 0.5MM TC X FLL 1/PK','300 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E300-05-S','MidiKros TC','MIDIKROS 65CM 300K MPES 0.5MM TC X FLL 1/PK STERILE','300 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E300-10-N','MidiKros TC','MIDIKROS 65CM 300K MPES 1MM TC X FLL 1/PK','300 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E300-10-S','MidiKros TC','MIDIKROS 65CM 300K MPES 1MM TC X FLL 1/PK STERILE','300 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E500-05-N','MidiKros TC','MIDIKROS 65CM 500K MPES 0.5MM TC X FLL 1/PK','500 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E500-05-S','MidiKros TC','MIDIKROS 65CM 500K MPES 0.5MM TC X FLL 1/PK STERILE','500 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E500-10-N','MidiKros TC','MIDIKROS 65CM 500K MPES 1MM TC X FLL 1/PK','500 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E500-10-S','MidiKros TC','MIDIKROS 65CM 500K MPES 1MM TC X FLL 1/PK STERILE','500 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E65U-07-N','MidiKros TC','MIDIKROS TC 65CM .65UM MPES .75MM 1/2TCXFLL 1/PK','0.65 μm','mPES',0.75,68,'65',18,0.275,46.2,43.0,38.8,275,5.17,14.28,89,268,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E65U-07-S','MidiKros TC','MIDIKROS TC 65CM .65UM MPES .75MM 1/2TCXFLL STERILE 1/PK','0.65 μm','mPES',0.75,68,'65',18,0.275,46.2,43.0,38.8,275,5.17,14.28,89,268,14,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E750-05-N','MidiKros TC','MIDIKROS 65CM 750K MPES 0.5MM TC X FLL 1/PK','750 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E750-05-S','MidiKros TC','MIDIKROS 65CM 750K MPES 0.5MM TC X FLL 1/PK STERILE','750 kD','mPES',0.5,68,'65',36,0.275,31.5,29.5,27.5,370,4.59,14.9,53,159,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E750-10-N','MidiKros TC','MIDIKROS TC 65CM 750KD MPES 1MM 1/2TCXFLL 1/PK','750 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-E750-10-S','MidiKros TC','MIDIKROS TC 65CM 750KD MPES 1MM 1/2TCXFLL STERILE 1/PK','750 kD','mPES',1.0,68,'65',12,0.275,57.0,52.5,48.0,245,6.12,14.35,141,424,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-M10U-06-N','MidiKros TC','MIDIKROS 65CM 0.1UM ME 0.6MM TC X FLL 1/PK','0.1 μm','ME',0.63,68,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-M10U-06-S','MidiKros TC','MIDIKROS 65CM 0.1UM ME 0.6MM TC X FLL 1/PK STERILE','0.1 μm','ME',0.63,68,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-M20U-06-N','MidiKros TC','MIDIKROS 65CM 0.2UM ME 0.6MM TC X FLL 1/PK','0.2 μm','ME',0.63,68,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-M20U-06-S','MidiKros TC','MIDIKROS 65CM 0.2UM ME 0.6MM TC X FLL 1/PK STERILE','0.2 μm','ME',0.63,68,'65',26,0.275,37.0,34.0,31.0,335,5.27,15.31,77,230,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-M20U-10-N','MidiKros TC','MIDIKROS 65CM 0.2UM ME 1MM TC X FLL 1/PK','0.2 μm','ME',1.0,68,'65',15,0.275,54.0,50.0,46.0,305,7.65,12.94,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-M20U-10-S','MidiKros TC','MIDIKROS 65CM 0.2UM ME 1MM TC X FLL 1/PK STERILE','0.2 μm','ME',1.0,68,'65',15,0.275,54.0,50.0,46.0,305,7.65,12.94,177,530,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-P20U-05-N','MidiKros TC','MIDIKROS 65CM 0.2UM PES 0.5MM TC X FLL 1/PK','0.2 μm','PES',0.5,68,'65',45,0.275,26.6,25.6,24.6,460,5.74,15.49,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-P20U-05-S','MidiKros TC','MIDIKROS 65CM 0.2UM PES 0.5MM TC X FLL 1/PK STERILE','0.2 μm','PES',0.5,68,'65',45,0.275,26.6,25.6,24.6,460,5.74,15.49,66,199,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-P20U-10-N','MidiKros TC','MIDIKROS 65CM 0.2UM PES 1MM TC X FLL 1/PK','0.2 μm','PES',1.0,68,'65',14,0.275,53.1,51.2,49.2,290,7.14,13.19,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-P20U-10-S','MidiKros TC','MIDIKROS 65CM 0.2UM PES 1MM TC X FLL 1/PK STERILE','0.2 μm','PES',1.0,68,'65',14,0.275,53.1,51.2,49.2,290,7.14,13.19,165,495,16,25,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S010-05-N','MidiKros TC','MIDIKROS 65CM 10KD PS 0.5MM TC X FLL 1/PK','10 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S010-05-P','MidiKros TC','MIDIKROS 65CM 10KD PS 0.5MM TC X FLL 1/PK WET','10 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S010-05-S','MidiKros TC','MIDIKROS 65CM 10KD PS 0.5MM TC X FLL 1/PK STERILE','10 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S050-05-N','MidiKros TC','MIDIKROS 65CM 50KD PS 0.5MM TC X FLL 1/PK','50 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S050-05-P','MidiKros TC','MIDIKROS 65CM 50KD PS 0.5MM TC X FLL 1/PK WET','50 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S050-05-S','MidiKros TC','MIDIKROS 65CM 50KD PS 0.5MM TC X FLL 1/PK STERILE','50 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S05U-05-N','MidiKros TC','MIDIKROS 65CM 0.05UM PS 0.5MM TC X FLL 1/PK','0.05 μm','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S05U-05-P','MidiKros TC','MIDIKROS 65CM 0.05UM PS 0.5MM TC X FLL 1/PK WET','0.05 μm','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S05U-05-S','MidiKros TC','MIDIKROS 65CM 0.05UM PS 0.5MM TC X FLL 1/PK STERILE','0.05 μm','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S500-05-N','MidiKros TC','MIDIKROS 65CM 500KD PS 0.5MM TC X FLL 1/PK','500 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S500-05-P','MidiKros TC','MIDIKROS 65CM 500KD PS 0.5MM TC X FLL 1/PK WET','500 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('T06-S500-05-S','MidiKros TC','MIDIKROS 65CM 500KD PS 0.5MM TC X FLL 1/PK STERILE','500 kD','PS',0.5,68,'65',60,0.275,26.8,23.6,20.5,610,7.65,14.24,88,265,14,16,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E001-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 1K MPES .5MM 6TCX1.5TC 1/PK','1 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E001-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 1K MPES .5MM 6TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E003-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 0.5MM 6TC x 1.5TC','3 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E003-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 0.5MM 6TC x 1.5TC STERILE','3 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E003-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 1MM 6TC x 1.5TC','3 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E003-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 3K MPES 1MM 6TC x 1.5TC STERILE','3 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E005-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 0.5MM 6TC x 1.5TC','5 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E005-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 0.5MM 6TC x 1.5TC STERILE','5 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E005-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 1MM 6TC x 1.5TC','5 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E005-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 5K MPES 1MM 6TC x 1.5TC STERILE','5 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E010-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 0.5MM 6TC x 1.5TC','10 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,26.0,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E010-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 0.5MM 6TC x 1.5TC STERILE','10 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,26.0,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E010-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 1MM 6TC x 1.5TC','10 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E010-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 10K MPES 1MM 6TC x 1.5TC STERILE','10 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E030-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 0.5MM 6TC x 1.5TC','30 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E030-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 0.5MM 6TC x 1.5TC STERILE','30 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E030-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 1MM 6TC x 1.5TC','30 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E030-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 30K MPES 1MM 6TC x 1.5TC STERILE','30 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E050-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 0.5MM 6TC x 1.5TC','50 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E050-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 0.5MM 6TC x 1.5TC STERILE','50 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E050-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 1MM 6TC x 1.5TC','50 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E050-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 50K MPES 1MM 6TC x 1.5TC STERILE','50 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E070-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 0.5MM 6TC x 1.5TC','70 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E070-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 0.5MM 6TC x 1.5TC STERILE','70 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E070-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 1MM 6TC x 1.5TC','70 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E070-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 70K MPES 1MM 6TC x 1.5TC STERILE','70 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E100-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 0.5MM 6TC x 1.5TC','100 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E100-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 0.5MM 6TC x 1.5TC STERILE','100 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E100-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 1MM 6TC x 1.5TC','100 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E100-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 100K MPES 1MM 6TC x 1.5TC STERILE','100 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E20U-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM MPES 0.5MM 6TC x 1.5TC','0.2 μm','mPES',0.5,51.5,'41.5',11000,2.047,31.5,29.5,27.5,71600,895.88,-1068.42,16199,48597,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E20U-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM MPES 0.5MM 6TC x 1.5TC STERILE','0.2 μm','mPES',0.5,51.5,'41.5',11000,2.047,31.5,29.5,27.5,71600,895.88,-1068.42,16199,48597,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E20U-10-N','KrosFlo Max','KROSFLO MAX 41.5CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 μm','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E20U-10-S','KrosFlo Max','KROSFLO MAX 41.5CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E300-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 0.5MM 6TC x 1.5TC','300 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E300-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 0.5MM 6TC x 1.5TC STERILE','300 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E300-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 1MM 6TC x 1.5TC','300 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E300-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 300K MPES 1MM 6TC x 1.5TC STERILE','300 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E500-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 0.5MM 6TC x 1.5TC','500 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E500-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 0.5MM 6TC x 1.5TC STERILE','500 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E500-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 1MM 6TC x 1.5TC','500 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E500-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 500K MPES 1MM 6TC x 1.5TC STERILE','500 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E65U-07-N','Krosflo Max','KROSFLO MAX 41.5CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 μm','mPES',0.75,51.5,'41.5',5000,2.047,46.2,43.0,38.8,48900,916.24,-1001.68,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E65U-07-S','Krosflo Max','KROSFLO MAX 41.5CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 μm','mPES',0.75,51.5,'41.5',5000,2.047,46.2,43.0,38.8,48900,916.24,-1001.68,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E750-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E750-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 750KD MPES 0.5MM 6TC X 1.5TC STERILE','750 kD','mPES',0.5,51.5,'41.5',12000,2.047,31.5,29.5,27.5,78000,977.33,-1245.61,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E750-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 1K MPES 1MM 6TCX1.5TC 1/PK','750 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-E750-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,51.5,'41.5',3330,2.047,57.0,52.5,48.0,43000,1084.83,-988.1,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-M10U-06-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.1UM ME 0.6MM 6TC x 1.5TC','0.1 μm','ME',0.63,51.5,'41.5',9000,2.047,37.0,34.0,31.0,74000,1163.7,-1237.67,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-M10U-06-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.1UM ME 0.6MM 6TC x 1.5TC STERILE','0.1 μm','ME',0.63,51.5,'41.5',9000,2.047,37.0,34.0,31.0,74000,1163.7,-1237.67,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-M20U-06-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 0.6MM 6TC x 1.5TC','0.2 μm','ME',0.63,51.5,'41.5',9000,2.047,37.0,34.0,31.0,74000,1163.7,-1237.67,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-M20U-06-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 0.6MM 6TC x 1.5TC STERILE','0.2 μm','ME',0.63,51.5,'41.5',9000,2.047,37.0,34.0,31.0,74000,1163.7,-1237.67,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-M20U-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 1MM 6TC x 1.5TC','0.2 μm','ME',1.0,51.5,'41.5',3500,2.047,54.0,50.0,46.0,45500,1140.21,-900.9,41233,123700,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-M20U-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.2UM ME 1MM 6TC x 1.5TC STERILE','0.2 μm','ME',1.0,51.5,'41.5',3500,2.047,54.0,50.0,46.0,45500,1140.21,-900.9,41233,123700,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-P20U-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 0.5MM 6TC x 1.5TC','0.2 μm','PES',0.5,51.5,'41.5',14000,2.047,26.6,25.6,24.6,91200,1140.21,-987.44,20617,61850,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-P20U-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 0.5MM 6TC x 1.5TC STERILE','0.2 μm','PES',0.5,51.5,'41.5',14000,2.047,26.6,25.6,24.6,91200,1140.21,-987.44,20617,61850,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-P20U-10-N','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 1MM 6TC x 1.5TC','0.2 μm','PES',1.0,51.5,'41.5',3350,2.047,53.1,51.2,49.2,43500,1091.35,-907.38,39466,118399,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-P20U-10-S','KrosFlo Max','KROSFLO MAX 41.5CM 20UM PES 1MM 6TC x 1.5TC STERILE','0.2 μm','PES',1.0,51.5,'41.5',3350,2.047,53.1,51.2,49.2,43500,1091.35,-907.38,39466,118399,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S010-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 10K PS 0.5MM 6TC x 1.5TC','10 kD','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S010-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 10K PS 0.5MM 6TC x 1.5TC STERILE','10 kD','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S050-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 50K PS 0.5MM 6TC x 1.5TC','50 kD','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S050-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 50K PS 0.5MM 6TC x 1.5TC STERILE','50 kD','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S05U-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 0.05UM PS 0.5MM 6TC x 1.5TC','0.05 μm','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S05U-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 0.05UM PS 0.5MM 6TC x 1.5TC STERILE','0.05 μm','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S500-05-N','KrosFlo Max','KROSFLO MAX 41.5CM 500K PS 0.5MM 6TC x 1.5TC','500 kD','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X04-S500-05-S','KrosFlo Max','KROSFLO MAX 41.5CM 500K PS 0.5MM 6TC x 1.5TC STERILE','500 kD','PS',0.5,51.5,'41.5',16000,2.047,26.8,23.6,20.5,104000,1303.1,-933.75,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E001-05-N','KrosFlo Max','KROSFLO MAX 50CM 1K MPES .5MM 6TCX1.5TC 1/PK','1 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E001-05-S','KrosFlo Max','KROSFLO MAX 50CM 1K MPES .5MM 6TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E003-05-N','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 0.5MM 6TC X 1.5TC','3 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E003-05-S','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 0.5MM 6TC X 1.5TC STERILE','3 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E003-10-N','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 1MM 6TC X 1.5TC','3 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E003-10-S','KrosFlo Max','KROSFLO MAX 50CM 3K MPES 1MM 6TC X 1.5TC STERILE','3 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E005-05-N','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 0.5MM 6TC X 1.5TC','5 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E005-05-S','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 0.5MM 6TC X 1.5TC STERILE','5 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E005-10-N','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 1MM 6TC X 1.5TC','5 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E005-10-S','KrosFlo Max','KROSFLO MAX 50CM 5K MPES 1MM 6TC X 1.5TC STERILE','5 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E010-05-N','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 0.5MM 6TC x 1.5TC','10 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,26.0,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E010-05-S','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 0.5MM 6TC x 1.5TC STERILE','10 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,26.0,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E010-10-N','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 1MM 6TC X 1.5TC','10 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E010-10-S','KrosFlo Max','KROSFLO MAX 50CM 10K MPES 1MM 6TC X 1.5TC STERILE','10 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E030-05-N','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 0.5MM 6TC X 1.5TC','30 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E030-05-S','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 0.5MM 6TC X 1.5TC STERILE','30 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E030-10-N','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 1MM 6TC X 1.5TC','30 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E030-10-S','KrosFlo Max','KROSFLO MAX 50CM 30K MPES 1MM 6TC X 1.5TC STERILE','30 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E050-05-N','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 0.5MM 6TC X 1.5TC','50 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E050-05-S','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 0.5MM 6TC X 1.5TC STERILE','50 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E050-10-N','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 1MM 6TC X 1.5TC','50 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E050-10-S','KrosFlo Max','KROSFLO MAX 50CM 50K MPES 1MM 6TC X 1.5TC STERILE','50 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E070-05-N','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 0.5MM 6TC X 1.5TC','70 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E070-05-S','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 0.5MM 6TC X 1.5TC STERILE','70 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E070-10-N','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 1MM 6TC X 1.5TC','70 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E070-10-S','KrosFlo Max','KROSFLO MAX 50CM 70K MPES 1MM 6TC X 1.5TC STERILE','70 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E100-05-N','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 0.5MM 6TC X 1.5TC','100 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E100-05-S','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 0.5MM 6TC X 1.5TC STERILE','100 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E100-10-N','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 1MM 6TC X 1.5TC','100 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E100-10-S','KrosFlo Max','KROSFLO MAX 50CM 100K MPES 1MM 6TC X 1.5TC STERILE','100 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E20U-05-N','KrosFlo Max','KROSFLO MAX 50CM 0.2UM MPES 0.5MM 6TC x 1.5TC','0.2 μm','mPES',0.5,60,'50',11000,2.047,31.5,29.5,27.5,86300,1079.38,-1287.25,16199,48597,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E20U-05-S','KrosFlo Max','KROSFLO MAX 50CM 0.2UM MPES 0.5MM 6TC x 1.5TC STERILE','0.2 μm','mPES',0.5,60,'50',11000,2.047,31.5,29.5,27.5,86300,1079.38,-1287.25,16199,48597,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E20U-10-N','KrosFlo Max','KROSFLO MAX 50CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 μm','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E20U-10-S','KrosFlo Max','KROSFLO MAX 50CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E300-05-N','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 0.5MM 6TC X 1.5TC','300 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E300-05-S','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 0.5MM 6TC X 1.5TC STERILE','300 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E300-10-N','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 1MM 6TC X 1.5TC','300 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E300-10-S','KrosFlo Max','KROSFLO MAX 50CM 300K MPES 1MM 6TC X 1.5TC STERILE','300 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E500-05-N','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 0.5MM 6TC X 1.5TC','500 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E500-05-S','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 0.5MM 6TC X 1.5TC STERILE','500 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E500-10-N','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 1MM 6TC X 1.5TC','500 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E500-10-S','KrosFlo Max','KROSFLO MAX 50CM 500K MPES 1MM 6TC X 1.5TC STERILE','500 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E65U-07-N','Krosflo Max','KROSFLO MAX 50CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 μm','mPES',0.75,60,'50',5000,2.047,46.2,43.0,38.8,58000,1103.91,-1206.85,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E65U-07-S','Krosflo Max','KROSFLO MAX 50CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 μm','mPES',0.75,60,'50',5000,2.047,46.2,43.0,38.8,58000,1103.91,-1206.85,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E750-05-N','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E750-05-S','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,60,'50',12000,2.047,31.5,29.5,27.5,94000,1177.5,-1500.73,17671,53014,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E750-10-N','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 1MM 6TC X 1.5TC','750 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-E750-10-S','KrosFlo Max','KROSFLO MAX 50CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,60,'50',3330,2.047,57.0,52.5,48.0,52000,1307.03,-1190.49,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-M10U-06-N','KrosFlo Max','KROSFLO MAX 50CM 0.1UM ME 0.6MM 6TC x 1.5TC','0.1 μm','ME',0.63,60,'50',9000,2.047,37.0,34.0,31.0,89000,1402.05,-1491.16,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-M10U-06-S','KrosFlo Max','KROSFLO MAX 50CM 0.1UM ME 0.6MM 6TC x 1.5TC STERILE','0.1 μm','ME',0.63,60,'50',9000,2.047,37.0,34.0,31.0,89000,1402.05,-1491.16,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-M20U-06-N','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 0.6MM 6TC X 1.5TC','0.2 μm','ME',0.63,60,'50',9000,2.047,37.0,34.0,31.0,89000,1402.05,-1491.16,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-M20U-06-S','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 0.6MM 6TC X 1.5TC STERILE','0.2 μm','ME',0.63,60,'50',9000,2.047,37.0,34.0,31.0,89000,1402.05,-1491.16,26512,79537,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-M20U-10-N','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 1MM 6TC X 1.5TC','0.2 μm','ME',1.0,60,'50',3500,2.047,54.0,50.0,46.0,55000,1373.75,-1085.42,41233,123700,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-M20U-10-S','KrosFlo Max','KROSFLO MAX 50CM 0.2UM ME 1MM 6TC X 1.5TC STERILE','0.2 μm','ME',1.0,60,'50',3500,2.047,54.0,50.0,46.0,55000,1373.75,-1085.42,41233,123700,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-P20U-05-N','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 0.5MM 6TC x 1.5TC','0.2 μm','PES',0.5,60,'50',14000,2.047,26.6,25.6,24.6,110000,1373.75,-1189.69,20617,61850,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-P20U-05-S','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 0.5MM 6TC x 1.5TC STERILE','0.2 μm','PES',0.5,60,'50',14000,2.047,26.6,25.6,24.6,110000,1373.75,-1189.69,20617,61850,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-P20U-10-N','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 1MM 6TC X 1.5TC','0.2 μm','PES',1.0,60,'50',3350,2.047,53.1,51.2,49.2,52500,1314.88,-1093.22,39466,118399,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-P20U-10-S','KrosFlo Max','KROSFLO MAX 50CM 20UM PES 1MM 6TC X 1.5TC STERILE','0.2 μm','PES',1.0,60,'50',3350,2.047,53.1,51.2,49.2,52500,1314.88,-1093.22,39466,118399,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S010-05-N','KrosFlo Max','KROSFLO MAX 50CM 10K PS 0.5MM 6TC x 1.5TC','10 kD','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S010-05-S','KrosFlo Max','KROSFLO MAX 50CM 10K PS 0.5MM 6TC x 1.5TC STERILE','10 kD','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S050-05-N','KrosFlo Max','KROSFLO MAX 50CM 50K PS 0.5MM 6TC X 1.5TC','50 kD','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S050-05-S','KrosFlo Max','KROSFLO MAX 50CM 50K PS 0.5MM 6TC X 1.5TC STERILE','50 kD','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S05U-05-N','KrosFlo Max','KROSFLO MAX 50CM 0.05UM PS 0.5MM 6TC X 1.5TC','0.05 μm','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S05U-05-S','KrosFlo Max','KROSFLO MAX 50CM 0.05UM PS 0.5MM 6TC X 1.5TC STERILE','0.05 μm','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S500-05-N','KrosFlo Max','KROSFLO MAX 50CM 500K PS 0.5MM 6TC X 1.5TC','500 kD','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X05-S500-05-S','KrosFlo Max','KROSFLO MAX 50CM 500K PS 0.5MM 6TC X 1.5TC STERILE','500 kD','PS',0.5,60,'50',16000,2.047,26.8,23.6,20.5,125000,1570.0,-1125.0,23562,70686,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E001-05-N','KrosFlo Max','KROSFLO MAX 68CM 1K MPES .5MM 6TCX1.5TC 1/PK','1 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E001-05-S','KrosFlo Max','KROSFLO MAX 68CM 1K MPES .5MM 6TCX1.5TC 1/PK STERILE','1 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E003-05-N','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 0.5MM 6TC x 1.5TC','3 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E003-05-S','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 0.5MM 6TC x 1.5TC STERILE','3 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E003-10-N','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 1MM 6TC x 1.5TC','3 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E003-10-S','KrosFlo Max','KROSFLO MAX 68CM 3K MPES 1MM 6TC x 1.5TC STERILE','3 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E005-05-N','KrosFlo Max','KROSFLO MAX 68CM 5KD MPES 0.5MM 6TC x 1.5TC','5 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E005-05-S','KrosFlo Max','KROSFLO MAX 68CM 5K MPES 0.5MM 6TC x 1.5TC STERILE','5 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E005-10-N','KrosFlo Max','KROSFLO MAX 68CM 5K MPES 1MM 6TC x 1.5TC','5 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E005-10-S','KrosFlo Max','KROSFLO MAX 68CM 5K MPES 1MM 6TC x 1.5TC STERILE','5 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E010-05-N','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 0.5MM 6TC x 1.5TC','10 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,26.0,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E010-05-S','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 0.5MM 6TC x 1.5TC STERILE','10 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,26.0,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E010-10-N','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 1MM 6TC x 1.5TC','10 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E010-10-S','KrosFlo Max','KROSFLO MAX 68CM 10K MPES 1MM 6TC x 1.5TC STERILE','10 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E030-05-N','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 0.5MM 6TC x 1.5TC','30 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E030-05-S','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 0.5MM 6TC x 1.5TC STERILE','30 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E030-10-N','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 1MM 6TC x 1.5TC','30 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E030-10-S','KrosFlo Max','KROSFLO MAX 68CM 30K MPES 1MM 6TC x 1.5TC STERILE','30 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E050-05-N','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 0.5MM 6TC x 1.5TC','50 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E050-05-S','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 0.5MM 6TC x 1.5TC STERILE','50 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E050-10-N','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 1MM 6TC x 1.5TC','50 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E050-10-S','KrosFlo Max','KROSFLO MAX 68CM 50K MPES 1MM 6TC x 1.5TC STERILE','50 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E070-05-N','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 0.5MM 6TC x 1.5TC','70 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E070-05-S','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 0.5MM 6TC x 1.5TC STERILE','70 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E070-10-N','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 1MM 6TC x 1.5TC','70 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E070-10-S','KrosFlo Max','KROSFLO MAX 68CM 70K MPES 1MM 6TC x 1.5TC STERILE','70 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E100-05-N','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 0.5MM 6TC X 1.5TC','100 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E100-05-S','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 0.5MM 6TC X 1.5TC STERLE','100 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E100-10-N','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 1MM 6TC x 1.5TC','100 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E100-10-S','KrosFlo Max','KROSFLO MAX 68CM 100K MPES 1MM 6TC x 1.5TC STERILE','100 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E20U-05-N','KrosFlo Max','KROSFLO MAX 68CM 0.2UM MPES 0.5MM 6TC x 1.5TC','0.2 μm','mPES',0.5,78,'68',11000,2.047,31.5,29.5,27.5,117400,1467.95,-1750.66,16199,48597,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E20U-05-S','KrosFlo Max','KROSFLO MAX 68CM 0.2UM MPES 0.5MM 6TC x 1.5TC STERILE','0.2 μm','mPES',0.5,78,'68',11000,2.047,31.5,29.5,27.5,117400,1467.95,-1750.66,16199,48597,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E20U-10-N','KrosFlo Max','KROSFLO MAX 68CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 μm','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E20U-10-S','KrosFlo Max','KROSFLO MAX 68CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E300-05-N','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 0.5MM 6TC x 1.5TC','300 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E300-05-S','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 0.5MM 6TC x 1.5TC STERILE','300 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E300-10-N','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 1MM 6TC x 1.5TC','300 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E300-10-S','KrosFlo Max','KROSFLO MAX 68CM 300K MPES 1MM 6TC x 1.5TC STERILE','300 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E500-05-N','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 0.5MM 6TC x 1.5TC','500 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E500-05-S','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 0.5MM 6TC x 1.5TC STERILE','500 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E500-10-N','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 1MM 6TC X 1.5TC','500 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E500-10-S','KrosFlo Max','KROSFLO MAX 68CM 500K MPES 1MM 6TC X 1.5TC','500 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E65U-07-N','Krosflo Max','KROSFLO MAX 65CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 μm','mPES',0.75,78,'68',5000,2.047,46.2,43.0,38.8,80000,1501.31,-1641.31,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E65U-07-S','Krosflo Max','KROSFLO MAX 65CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 μm','mPES',0.75,78,'68',5000,2.047,46.2,43.0,38.8,80000,1501.31,-1641.31,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E750-05-N','KrosFlo Max','KROSFLO MAX 68CM 750KD MPES 0.5MM 6TC X 1.5TC','750 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E750-05-S','KrosFlo Max','KROSFLO MAX 68CM 750KD MPES 0.5MM 6TC X 1.5TC STERILE','750 kD','mPES',0.5,78,'68',12000,2.047,31.5,29.5,27.5,128000,1601.4,-2040.99,17671,53014,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E750-10-N','KrosFlo Max','KROSFLO MAX 65CM 750KD MPES 1MM 6TC X 1.5TC','750 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-E750-10-S','KrosFlo Max','KROSFLO MAX 65CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,78,'68',3330,2.047,57.0,52.5,48.0,71000,1777.55,-1619.06,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-M10U-06-N','KrosFlo Max','KROSFLO MAX 68CM 0.1UM ME 0.6MM 6TC x 1.5TC','0.1 μm','ME',0.63,78,'68',9000,2.047,37.0,34.0,31.0,121000,1906.79,-2027.98,26512,79537,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-M10U-06-S','KrosFlo Max','KROSFLO MAX 68CM 0.1UM ME 0.6MM 6TC x 1.5TC STERILE','0.1 μm','ME',0.63,78,'68',9000,2.047,37.0,34.0,31.0,121000,1906.79,-2027.98,26512,79537,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-M20U-06-N','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 0.6MM 6TC x 1.5TC','0.2 μm','ME',0.63,78,'68',9000,2.047,37.0,34.0,31.0,121000,1906.79,-2027.98,26512,79537,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-M20U-06-S','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 0.6MM 6TC x 1.5TC STERILE','0.2 μm','ME',0.63,78,'68',9000,2.047,37.0,34.0,31.0,121000,1906.79,-2027.98,26512,79537,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-M20U-10-N','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 1MM 6TC x 1.5TC','0.2 μm','ME',1.0,78,'68',3500,2.047,54.0,50.0,46.0,75000,1868.3,-1476.17,41233,123700,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-M20U-10-S','KrosFlo Max','KROSFLO MAX 68CM 0.2UM ME 1MM 6TC x 1.5TC STERILE','0.2 μm','ME',1.0,78,'68',3500,2.047,54.0,50.0,46.0,75000,1868.3,-1476.17,41233,123700,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-P20U-05-N','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 0.5MM 6TC x 1.5TC','0.2 μm','PES',0.5,78,'68',14000,2.047,26.6,25.6,24.6,150000,1868.3,-1617.97,20617,61850,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-P20U-05-S','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 0.5MM 6TC x 1.5TC STERILE','0.2 μm','PES',0.5,78,'68',14000,2.047,26.6,25.6,24.6,150000,1868.3,-1617.97,20617,61850,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-P20U-10-N','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 1MM 6TC x 1.5TC','0.2 μm','PES',1.0,78,'68',3350,2.047,53.1,51.2,49.2,71500,1788.23,-1486.79,39466,118399,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-P20U-10-S','KrosFlo Max','KROSFLO MAX 68CM 20UM PES 1MM 6TC x 1.5TC STERILE','0.2 μm','PES',1.0,78,'68',3350,2.047,53.1,51.2,49.2,71500,1788.23,-1486.79,39466,118399,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S010-05-N','KrosFlo Max','KROSFLO MAX 68CM 10K PS 0.5MM 6TC x 1.5TC','10 kD','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S010-05-S','KrosFlo Max','KROSFLO MAX 68CM 10K PS 0.5MM 6TC x 1.5TC STERILE','10 kD','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S050-05-N','KrosFlo Max','KROSFLO MAX 68CM 50K PS 0.5MM 6TC x 1.5TC','50 kD','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S050-05-S','KrosFlo Max','KROSFLO MAX 68CM 50K PS 0.5MM 6TC x 1.5TC STERILE','50 kD','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S05U-05-N','KrosFlo Max','KROSFLO MAX 68CM 0.05UM PS 0.5MM 6TC x 1.5TC','0.05 μm','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S05U-05-S','KrosFlo Max','KROSFLO MAX 68CM 0.05UM PS 0.5MM 6TC x 1.5TC STERILE','0.05 μm','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'edit description error 04/02/14 - sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S500-05-N','KrosFlo Max','KROSFLO MAX 68CM 500K PS 0.5MM 6TC x 1.5TC','500 kD','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X06-S500-05-S','KrosFlo Max','KROSFLO MAX 68CM 500K PS 0.5MM 6TC x 1.5TC STERILE','500 kD','PS',0.5,78,'68',16000,2.047,26.8,23.6,20.5,171000,2135.2,-1530.0,23562,70686,NULL,NULL,'Update X06 description for correct EFL of 68cm 04/03/14 sl');
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E003-10-N','KrosFlo Max','KROSFLO MAX 108CM 3K MPES 1MM 6TC x 1.5TC','3 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E003-10-S','KrosFlo Max','KROSFLO MAX 108CM 3K MPES 1MM 6TC x 1.5TC STERILE','3 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E005-10-N','KrosFlo Max','KROSFLO MAX 108CM 5K MPES 1MM 6TC x 1.5TC','5 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E005-10-S','KrosFlo Max','KROSFLO MAX 108CM 5K MPES 1MM 6TC x 1.5TC STERILE','5 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E010-10-N','KrosFlo Max','KROSFLO MAX 108CM 10K MPES 1MM 6TC x 1.5TC','10 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E010-10-S','KrosFlo Max','KROSFLO MAX 108CM 10K MPES 1MM 6TC x 1.5TC STERILE','10 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E030-10-N','KrosFlo Max','KROSFLO MAX 108CM 30K MPES 1MM 6TC x 1.5TC','30 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E030-10-S','KrosFlo Max','KROSFLO MAX 108CM 30K MPES 1MM 6TC x 1.5TC STERILE','30 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E050-10-N','KrosFlo Max','KROSFLO MAX 108CM 50K MPES 1MM 6TC x 1.5TC','50 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E050-10-S','KrosFlo Max','KROSFLO MAX 108CM 50K MPES 1MM 6TC x 1.5TC STERILE','50 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E070-10-N','KrosFlo Max','KROSFLO MAX 108CM 70K MPES 1MM 6TC x 1.5TC','70 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E070-10-S','KrosFlo Max','KROSFLO MAX 108CM 70K MPES 1MM 6TC x 1.5TC STERILE','70 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E100-10-N','KrosFlo Max','KROSFLO MAX 108CM 100K MPES 1MM 6TC x 1.5TC','100 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E100-10-S','KrosFlo Max','KROSFLO MAX 108CM 100K MPES 1MM 6TC x 1.5TC STERILE','100 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E20U-10-N','KrosFlo Max','KROSFLO MAX 108CM .2UM MPES 1MM 6TCX1.5TC 1/PK','0.2 μm','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E20U-10-S','KrosFlo Max','KROSFLO MAX 108CM .2UM MPES 1MM 6TCX1.5TC 1/PK STERILE','0.2 μm','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E300-10-N','KrosFlo Max','KROSFLO MAX 108CM 300K MPES 1MM 6TC x 1.5TC','300 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E300-10-S','KrosFlo Max','KROSFLO MAX 108CM 300K MPES 1MM 6TC x 1.5TC STERILE','300 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E500-10-N','KrosFlo Max','KROSFLO MAX 108CM 500K MPES 1MM 6TC x 1.5TC','500 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E500-10-S','KrosFlo Max','KROSFLO MAX 108CM 500K MPES 1MM 6TC x 1.5TC STERILE','500 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E65U-07-N','Krosflo Max','KROSFLO MAX 108CM 0.65U MPES 0.75MM 6TC X 1.5TC','0.65 μm','mPES',0.75,118,'108',5000,4.08,46.2,43.0,38.8,127100,2384.44,4206.33,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E65U-07-S','Krosflo Max','KROSFLO MAX 108CM 0.65U MPES 0.75MM 6TC X 1.5TC STERILE','0.65 μm','mPES',0.75,118,'108',5000,4.08,46.2,43.0,38.8,127100,2384.44,4206.33,24850,74551,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E750-10-N','KrosFlo Max','KROSFLO MAX 108CM 750KD MPES 1MM 6TC X 1.5TC','750 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-E750-10-S','KrosFlo Max','KROSFLO MAX 108CM 750KD MPES 1MM 6TC X 1.5TC STERILE','750 kD','mPES',1.0,118,'108',3330,4.08,57.0,52.5,48.0,113000,2823.17,4241.67,39231,117692,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-M20U-10-N','KrosFlo Max','KROSFLO MAX 108CM 0.2UM ME 1MM 6TC x 1.5TC','0.2 μm','ME',1.0,118,'108',3500,4.08,54.0,50.0,46.0,118500,2967.3,4468.62,41233,123700,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-M20U-10-S','KrosFlo Max','KROSFLO MAX 108CM 0.2UM ME 1MM 6TC x 1.5TC STERILE','0.2 μm','ME',1.0,118,'108',3500,4.08,54.0,50.0,46.0,118500,2967.3,4468.62,41233,123700,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-P20U-10-N','KrosFlo Max','KROSFLO MAX 108CM 20UM PES 1MM 6TC x 1.5TC','0.2 μm','PES',1.0,118,'108',3350,4.08,53.1,51.2,49.2,113500,2840.13,4451.76,39466,118399,NULL,NULL,NULL);
INSERT INTO "hollow_fiber_lookup" VALUES ('X10-P20U-10-S','KrosFlo Max','KROSFLO MAX 108CM 20UM PES 1MM 6TC x 1.5TC STERILE','0.2 μm','PES',1.0,118,'108',3350,4.08,53.1,51.2,49.2,113500,2840.13,4451.76,39466,118399,NULL,NULL,NULL);

INSERT INTO "system_setting_details" VALUES (1,1,1,1,2,2,2,2,2,2,1,0,0.0,2.0,NULL,NULL,NULL,NULL,NULL,NULL,1575532566993,1575532566993,1,0,NULL,NULL,2.0,3.0,NULL,NULL);

INSERT INTO "pump_tube_map" VALUES (1,1);
INSERT INTO "pump_tube_map" VALUES (1,6);
INSERT INTO "pump_tube_map" VALUES (1,5);
INSERT INTO "pump_tube_map" VALUES (1,8);
INSERT INTO "pump_tube_map" VALUES (2,1);
INSERT INTO "pump_tube_map" VALUES (2,6);
INSERT INTO "pump_tube_map" VALUES (2,5);
INSERT INTO "pump_tube_map" VALUES (2,8);
INSERT INTO "pump_tube_map" VALUES (3,1);
INSERT INTO "pump_tube_map" VALUES (3,6);
INSERT INTO "pump_tube_map" VALUES (3,8);
INSERT INTO "pump_tube_map" VALUES (4,1);
INSERT INTO "pump_tube_map" VALUES (4,6);
INSERT INTO "pump_tube_map" VALUES (4,8);
INSERT INTO "pump_tube_map" VALUES (5,1);
INSERT INTO "pump_tube_map" VALUES (5,6);
INSERT INTO "pump_tube_map" VALUES (5,5);
INSERT INTO "pump_tube_map" VALUES (5,8);
INSERT INTO "pump_tube_map" VALUES (6,1);
INSERT INTO "pump_tube_map" VALUES (6,6);
INSERT INTO "pump_tube_map" VALUES (6,8);
INSERT INTO "pump_tube_map" VALUES (7,1);
INSERT INTO "pump_tube_map" VALUES (7,6);
INSERT INTO "pump_tube_map" VALUES (7,8);
INSERT INTO "pump_tube_map" VALUES (8,1);
INSERT INTO "pump_tube_map" VALUES (8,6);
INSERT INTO "pump_tube_map" VALUES (8,8);
INSERT INTO "pump_tube_map" VALUES (9,1);
INSERT INTO "pump_tube_map" VALUES (9,6);
INSERT INTO "pump_tube_map" VALUES (9,8);
INSERT INTO "pump_tube_map" VALUES (10,1);
INSERT INTO "pump_tube_map" VALUES (10,6);
INSERT INTO "pump_tube_map" VALUES (10,5);
INSERT INTO "pump_tube_map" VALUES (10,8);
INSERT INTO "pump_tube_map" VALUES (11,1);
INSERT INTO "pump_tube_map" VALUES (11,6);
INSERT INTO "pump_tube_map" VALUES (11,8);
INSERT INTO "pump_tube_map" VALUES (12,1);
INSERT INTO "pump_tube_map" VALUES (12,6);
INSERT INTO "pump_tube_map" VALUES (12,8);
INSERT INTO "pump_tube_map" VALUES (13,1);
INSERT INTO "pump_tube_map" VALUES (13,6);
INSERT INTO "pump_tube_map" VALUES (13,8);
INSERT INTO "pump_tube_map" VALUES (14,1);
INSERT INTO "pump_tube_map" VALUES (14,6);
INSERT INTO "pump_tube_map" VALUES (15,2);
INSERT INTO "pump_tube_map" VALUES (15,7);
INSERT INTO "pump_tube_map" VALUES (15,9);
INSERT INTO "pump_tube_map" VALUES (16,2);
INSERT INTO "pump_tube_map" VALUES (16,7);
INSERT INTO "pump_tube_map" VALUES (16,9);
INSERT INTO "pump_tube_map" VALUES (17,2);
INSERT INTO "pump_tube_map" VALUES (17,7);
INSERT INTO "pump_tube_map" VALUES (17,9);
INSERT INTO "pump_tube_map" VALUES (18,2);
INSERT INTO "pump_tube_map" VALUES (18,7);
INSERT INTO "pump_tube_map" VALUES (18,9);
INSERT INTO "pump_tube_map" VALUES (19,2);
INSERT INTO "pump_tube_map" VALUES (19,7);
INSERT INTO "pump_tube_map" VALUES (20,2);
INSERT INTO "pump_tube_map" VALUES (20,7);
INSERT INTO "pump_tube_map" VALUES (21,9);
INSERT INTO "pump_tube_map" VALUES (22,9);
INSERT INTO "pump_tube_map" VALUES (23,4);
INSERT INTO "pump_tube_map" VALUES (24,3);
INSERT INTO "pump_mode_map" VALUES (1,1);
INSERT INTO "pump_mode_map" VALUES (1,2);
INSERT INTO "pump_mode_map" VALUES (1,3);
INSERT INTO "pump_mode_map" VALUES (1,4);
INSERT INTO "pump_mode_map" VALUES (1,5);
INSERT INTO "pump_mode_map" VALUES (1,6);
INSERT INTO "pump_mode_map" VALUES (1,7);
INSERT INTO "pump_mode_map" VALUES (1,8);
INSERT INTO "pump_mode_map" VALUES (1,9);
INSERT INTO "pump_mode_map" VALUES (2,1);
INSERT INTO "pump_mode_map" VALUES (2,2);
INSERT INTO "pump_mode_map" VALUES (2,3);
INSERT INTO "pump_mode_map" VALUES (2,4);
INSERT INTO "pump_mode_map" VALUES (2,5);
INSERT INTO "pump_mode_map" VALUES (2,6);
INSERT INTO "pump_mode_map" VALUES (2,7);
INSERT INTO "pump_mode_map" VALUES (2,8);
INSERT INTO "pump_mode_map" VALUES (2,9);
INSERT INTO "pump_mode_map" VALUES (3,1);
INSERT INTO "pump_mode_map" VALUES (3,2);
INSERT INTO "pump_mode_map" VALUES (3,3);
INSERT INTO "pump_mode_map" VALUES (3,4);
INSERT INTO "pump_mode_map" VALUES (3,5);
INSERT INTO "pump_mode_map" VALUES (3,6);
INSERT INTO "pump_mode_map" VALUES (3,7);
INSERT INTO "pump_mode_map" VALUES (3,8);
INSERT INTO "pump_mode_map" VALUES (4,1);
INSERT INTO "pump_mode_map" VALUES (4,2);
INSERT INTO "pump_mode_map" VALUES (4,3);
INSERT INTO "pump_mode_map" VALUES (4,4);
INSERT INTO "pump_mode_map" VALUES (4,5);
INSERT INTO "pump_mode_map" VALUES (4,6);
INSERT INTO "pump_mode_map" VALUES (4,7);
INSERT INTO "pump_mode_map" VALUES (4,8);
INSERT INTO "pump_mode_map" VALUES (1,10);
INSERT INTO "pump_mode_map" VALUES (1,11);
INSERT INTO "pump_mode_map" VALUES (1,12);
INSERT INTO "pump_mode_map" VALUES (1,13);
INSERT INTO "pump_mode_map" VALUES (1,14);
INSERT INTO "pump_mode_map" VALUES (1,15);
INSERT INTO "pump_mode_map" VALUES (2,10);
INSERT INTO "pump_mode_map" VALUES (2,11);
INSERT INTO "pump_mode_map" VALUES (2,12);
INSERT INTO "pump_mode_map" VALUES (2,13);
INSERT INTO "pump_mode_map" VALUES (2,14);
INSERT INTO "pump_mode_map" VALUES (2,15);
INSERT INTO "pump_mode_map" VALUES (3,10);
INSERT INTO "pump_mode_map" VALUES (3,11);
INSERT INTO "pump_mode_map" VALUES (3,12);
INSERT INTO "pump_mode_map" VALUES (3,13);
INSERT INTO "pump_mode_map" VALUES (3,14);
INSERT INTO "pump_mode_map" VALUES (3,15);
INSERT INTO "pump_mode_map" VALUES (4,10);
INSERT INTO "pump_mode_map" VALUES (4,11);
INSERT INTO "pump_mode_map" VALUES (4,12);
INSERT INTO "pump_mode_map" VALUES (4,13);
INSERT INTO "pump_mode_map" VALUES (4,14);
INSERT INTO "pump_mode_map" VALUES (4,15);
COMMIT;
