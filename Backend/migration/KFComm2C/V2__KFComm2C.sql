Insert or Replace INTO "firmware_version" VALUES (1,20,'0.6.3');

CREATE TABLE IF NOT EXISTS "trial_hist_data_tmp" (
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
	"konduit_ch_1"	REAL,
	"konduit_ch_2"	REAL,
	"feed_scale"	REAL,
	"m_permeate"	REAL,
	"diafiltration_vol_1"	REAL,
	"concentration_factor"	REAL,
	"timestamp"	INTEGER,
	"feed_flow_rate"	REAL,
	"permeate_flow_rate"	REAL,
	"retentate_flow_rate"	REAL,
	"vt"	REAL,
	"shear"	TEXT,
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
	"total_perm_weight"	REAL,
	"diafiltration_vol_2"	REAL,
	"konduit_ch_1_type"	INTEGER,
	"konduit_ch_2_type"	INTEGER,
	"is_paused"	INTEGER DEFAULT 0
);

INSERT INTO trial_hist_data_tmp SELECT * FROM trial_hist_data;

DROP table trial_hist_data;

ALTER TABLE trial_hist_data_tmp RENAME TO trial_hist_data;

Alter table "trial_hist_data" add "flow_rate_count" INTEGER DEFAULT 1;