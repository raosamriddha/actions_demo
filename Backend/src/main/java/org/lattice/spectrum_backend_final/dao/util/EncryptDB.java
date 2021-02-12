package org.lattice.spectrum_backend_final.dao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;

import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.property.ResourceManager;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public final class EncryptDB {

	private EncryptDB() {
	}

	public static final boolean isEncryptPropertiesExists() {
		Logger.setDBEncryptLogToFile(true);
		final File tempFile = new File(ResourceManager.getProperty(ApiConstant.ENCRYPT_PROPERTIES_FILE_LOCATION));
		if (tempFile.exists() && tempFile.isFile()) {
			sLog.d("ENCRYPT_PROCESS: encrypt.properties file exists");
			return true;
		}
		sLog.d("ENCRYPT_PROCESS: encrypt.properties file not found");
		return false;
	}

	public static final boolean isTempFileExists() {
		final File tempFile = new File(ResourceManager.getProperty(ApiConstant.JRE_FOLDER_LOCATION)
				+ ResourceManager.getProperty(ApiConstant.TEMPORARY_DB_NAME));
		if (tempFile.exists() && tempFile.isFile()) {
			sLog.d("ENCRYPT_PROCESS: temp.db file exists");
			return true;
		}
		sLog.d("ENCRYPT_PROCESS: temp.db file not found");
		return false;
	}

	public static void encryptDatabase() throws Exception {
		String encryptStatus = null;
		try {
			encryptStatus = getEncryptionStatus();
			if (ApiConstant.ENCRYPTION_NOT_ACTIVE.equalsIgnoreCase(encryptStatus)) {
				sLog.d("ENCRYPT_PROCESS: Database not encrypted");
				DbConnectionManager.getInstance().setDB_URL(ResourceManager.getProperty(ApiConstant.PLAIN_DB_URL));
				sLog.d("Migrating database...");
				DatabaseMigration.getInstance().migrate();
				DbConnectionManager.getInstance()
						.setDB_URL(ResourceManager.getProperty(ApiConstant.TEMPORARY_DB_URL) + PasswordUtil.decrypt(
								ResourceManager.getProperty(ApiConstant.DB_ENCRYPTION_KEY), ApiConstant.SECRET));
				startDatabaseEncryption();
			} else if (ApiConstant.ENCRYPTION_ACTIVE.equalsIgnoreCase(encryptStatus)) {
				sLog.d("ENCRYPT_PROCESS: Database already encrypted");
				DbConnectionManager.getInstance()
						.setDB_URL(ResourceManager.getProperty(ApiConstant.DEFAULT_DB_URL) + PasswordUtil.decrypt(
								ResourceManager.getProperty(ApiConstant.DB_ENCRYPTION_KEY), ApiConstant.SECRET));
				sLog.d("Migrating database...");
				DatabaseMigration.getInstance().migrate();
			} else {
				sLog.d("ENCRYPT_PROCESS: encrypt.properties file corrupted");
				throw new RuntimeException(ApiConstant.ERROR_ENCRYPT_PROPERTIES_NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			deleteFailedDBFile(ApiConstant.TEMPORARY_DB_NAME);
			sLog.d("ENCRYPT_PROCESS: Database encryption process failed");
		}
	}

	private static final boolean startDatabaseEncryption() throws Exception {
		sLog.d("ENCRYPT_PROCESS: Database encryption process started...");
		if (copyDataToTempDB()) {
			DbConnectionManager.getInstance()
					.setDB_URL(ResourceManager.getProperty(ApiConstant.DEFAULT_DB_URL) + PasswordUtil
							.decrypt(ResourceManager.getProperty(ApiConstant.DB_ENCRYPTION_KEY), ApiConstant.SECRET));
			sLog.d("ENCRYPT_PROCESS: Changed DB_URL to encrypted");
			deleteFailedDBFile(ApiConstant.DEFAULT_DB_NAME);
			renameDBFile(ApiConstant.TEMPORARY_DB_NAME, ApiConstant.DEFAULT_DB_NAME);
			setEncryptionStatus(ApiConstant.ENCRYPTION_ACTIVE);
			sLog.d("ENCRYPT_PROCESS: Database encryption process finished");
			return true;
		} else {
			throw new RuntimeException(ApiConstant.ERROR_ENCRYPTION_PROCESS_FAILED);
		}
	}

	private static final boolean copyDataToTempDB() throws Exception {
		Connection con = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			final File originalFile = new File(ResourceManager.getProperty(ApiConstant.JRE_FOLDER_LOCATION)
					+ ResourceManager.getProperty(ApiConstant.DEFAULT_DB_NAME));
			if (originalFile.exists()) {
				sLog.d("ENCRYPT_PROCESS: Plain DB file found, copying data to temp DB");
				Statement statement = con.createStatement();
				statement.execute("PRAGMA rekey=''");
				statement.execute("ATTACH DATABASE '" + originalFile.getAbsolutePath() + "' AS oldDB");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS trial_hist_data ( trial_live_data_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_run_setting_id INTEGER, feed_pressure REAL, permeate_pressure REAL, retentate_pressure REAL, tmp REAL, main_pump_speed REAL, main_pump_flow_rate REAL, aux_pump_1_flow_rate REAL, aux_pump_2_flow_rate REAL, conductivity_1 REAL, conductivity_2 REAL, temperature_1 REAL, temperature_2 REAL, konduit_ch_1 REAL, konduit_ch_2 REAL, feed_scale REAL, m_permeate REAL, diafiltration_vol_1 REAL, concentration_factor REAL, timestamp INTEGER, feed_flow_rate REAL, permeate_flow_rate REAL, retentate_flow_rate REAL, vt REAL, shear TEXT, abv_1 TEXT, abv_2 TEXT, flux REAL, notes TEXT, trial_master_id INTEGER, delta_p REAL, tcf TEXT, water_flux_20_degree TEXT, water_perm TEXT, nwp TEXT, total_perm_weight REAL, diafiltration_vol_2 REAL, konduit_ch_1_type INTEGER, konduit_ch_2_type INTEGER, is_paused INTEGER DEFAULT 0 , flow_rate_count INTEGER DEFAULT 1, permeate_total REAL, permeate_total_with_holdup REAL, duration TEXT default 'N/A')");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS hollow_fiber_lookup ( hf_lookup_id INTEGER PRIMARY KEY AUTOINCREMENT, part_no TEXT, module_family TEXT, MAPICSPartDescription TEXT, MWCO TEXT, MembraneChemistry TEXT, fiber_id REAL, total_length INTEGER, effective_length TEXT, fiber_count INTEGER, HousingID REAL, FiberODMax REAL, FiberODNom REAL, FiberODMin REAL, surface_area INTEGER, IDHoldupVolume REAL, ECS REAL, FlowRate INTEGER, `FlowRate6000sec-1` INTEGER, `TubingRecommendation2000sec-1` INTEGER, `TubingRecommendation6000sec-1` INTEGER, Notes TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS filter_plate_insert_lookup ( fpi_id INTEGER PRIMARY KEY AUTOINCREMENT, fpi_part_no TEXT, fpi_hold_up REAL, combinded_feed_and_retenate REAL )");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS cassette_lookup ( cst_lookup_id INTEGER PRIMARY KEY AUTOINCREMENT, part_no TEXT, channel_type TEXT, cassette_family TEXT, catalog INTEGER, material TEXT, rating TEXT, surface_area REAL, ECS REAL, height REAL, width REAL, no_of_channel INTEGER, membrane_area REAL );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS pump_tube_map ( pump_tube_map_id INTEGER PRIMARY KEY AUTOINCREMENT, tubing_lookup_id INTEGER NOT NULL, pump_lookup_id INTEGER NOT NULL );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS pump_mode_map ( pump_mode_map_id INTEGER PRIMARY KEY AUTOINCREMENT, pump_lookup_id INTEGER NOT NULL, mode_lookup_id INTEGER NOT NULL );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS user_role_map ( user_role_map_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, role_id INTEGER NOT NULL, is_active TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS system_setting_details ( system_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, pressure_unit INTEGER, weight_unit INTEGER, volume_unit INTEGER, length_unit INTEGER, decimal_pressure INTEGER, decimal_weight INTEGER, decimal_flow_rate INTEGER, decimal_volume INTEGER, decimal_length INTEGER, date_format INTEGER, is_aux_konduit INTEGER, uv_min REAL, uv_max REAL, ph_min REAL, ph_max REAL, turbidity_min REAL, turbidity_max REAL, totalizer_min REAL, totalizer_max REAL, created_on INTEGER, updated_on INTEGER, is_active INTEGER , is_aux_konduit_2 INTEGER, ph_min_2 REAL, ph_max_2 REAL, turbidity_min_2 REAL, turbidity_max_2 REAL, totalizer_min_2 REAL, totalizer_max_2 REAL, protein_conc_min REAL, protein_conc_min_2 REAL, protein_conc_max REAL, protein_conc_max_2 REAL, totalizer_unit INTEGER, totalizer_unit_2 INTEGER)");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS manual_pump_master ( pump_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, pump_name TEXT, speed TEXT, flow_rate TEXT, direction TEXT, trial_run_setting_id INTEGER, tubing_size TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS kf_konduit_setting ( kf_konduit_setting_id INTEGER PRIMARY KEY AUTOINCREMENT, uv_ch_1 TEXT, uv_ch_2 TEXT, k_factor_ch_1 TEXT, k_factor_ch_2 TEXT, trial_master_id INTEGER, is_active TEXT, trial_run_setting_id INTEGER, FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS aux_pump_master ( aux_pump_master_id INTEGER PRIMARY KEY AUTOINCREMENT, aux_pump_type TEXT, pump_function TEXT, aux_tubing_size TEXT, flow_rate TEXT, trial_master_id INTEGER, is_active TEXT, type INTEGER, trial_run_setting_id INTEGER, pump_speed TEXT, FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS trial_log ( trial_log_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, trial_run_setting_id INTEGER NOT NULL, action TEXT NOT NULL, log_type INTEGER NOT NULL, created_on TEXT NOT NULL, updated_on INTEGER, is_active TEXT NOT NULL DEFAULT 'Y', digital_notes TEXT default '' );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS filter_perm_master ( fiter_perm_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_master_id INTEGER, filter_type TEXT, part_no REAL, fiber_count INTEGER DEFAULT 0, fiber_id TEXT DEFAULT '', surf_area REAL, ecs TEXT, permeate_tube_size TEXT, tube_length TEXT, perm_tube_holdup TEXT, total_perm_tube_holdup TEXT, no_of_cassette INTEGER DEFAULT 0, height REAL DEFAULT '', width REAL DEFAULT '', perm_hold_up_cal TEXT, no_of_channel INTEGER DEFAULT 0, fpi_part_no TEXT DEFAULT '', is_generic TEXT DEFAULT '', is_active TEXT, model TEXT default '', manufacturer TEXT default '', FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS user_access_log ( access_log_id INTEGER PRIMARY KEY AUTOINCREMENT, performed_by TEXT NOT NULL, full_name TEXT NOT NULL, role TEXT NOT NULL, created_on TEXT NOT NULL, action TEXT NOT NULL, performed_on TEXT NOT NULL, is_active TEXT DEFAULT 'Y', digital_notes TEXT default '' );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS alarm_history ( alarm_history_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_run_setting_id INTEGER, mode_name TEXT, alarm_stop_desc TEXT, alarm_stop_value TEXT, nature TEXT, created_on TEXT, updated_on TEXT, is_active TEXT DEFAULT 'Y' );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS notes ( notes_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_run_setting_id INTEGER, notes TEXT, posted_by TEXT, created_on TEXT, modified_on TEXT, is_active TEXT DEFAULT 'Y' );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS alarm_setting ( alarm_setting_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_run_setting_id INTEGER UNIQUE, feed_pressure_low_alarm TEXT, feed_pressure_low_stop TEXT, feed_pressure_high_alarm TEXT, feed_pressure_high_stop TEXT, feed_wt_low_alarm TEXT, feed_wt_low_stop TEXT, feed_wt_high_alarm TEXT, feed_wt_high_stop TEXT, perm_wt_low_alarm TEXT, perm_wt_low_stop TEXT, perm_wt_high_alarm TEXT, perm_wt_high_stop TEXT, perm_pressure_low_alarm TEXT, perm_pressure_low_stop TEXT, perm_pressure_high_alarm TEXT, perm_pressure_high_stop TEXT, konduit_ch_1_high_alarm TEXT, konduit_ch_1_high_stop TEXT, konduit_ch_2_high_alarm TEXT, konduit_ch_2_high_stop TEXT, created_on TEXT, updated_on TEXT, is_active TEXT DEFAULT 'Y' , konduit_ch_1_type INTEGER DEFAULT 1, konduit_ch_2_type INTEGER DEFAULT 2, permeate_total_high_alarm TEXT DEFAULT '', permeate_total_high_stop TEXT DEFAULT '')");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS mode_lookup ( mode_lookup_id INTEGER PRIMARY KEY AUTOINCREMENT, mode_name TEXT UNIQUE, mode_full_name TEXT, min_aux_pump INTEGER, max_aux_pump INTEGER, aux_pump_1 TEXT, aux_pump_2 TEXT, is_active TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS end_point ( end_point_id INTEGER PRIMARY KEY AUTOINCREMENT, end_point_type TEXT, end_point_value TEXT, trial_master_id INTEGER, step_no INTEGER, is_active TEXT, FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS recirc_flow_unit ( recirc_flow_unit_id INTEGER PRIMARY KEY AUTOINCREMENT, recirc_flow_unit_value INTEGER UNIQUE );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS end_point_lookup ( end_point_lookup_id INTEGER PRIMARY KEY AUTOINCREMENT, end_point_name TEXT, end_point_group TEXT, is_active TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS abv_master ( abv_id INTEGER PRIMARY KEY AUTOINCREMENT, abv_type TEXT, cont_based_on TEXT, abv_mode TEXT, start_pos TEXT, percent_closed TEXT, op_pressure TEXT, trial_master_id INTEGER, is_active TEXT, FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS ramp_up_time ( ramp_up_id INTEGER PRIMARY KEY AUTOINCREMENT, ramp_up_time_value INTEGER );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS user_login_limit ( login_limit_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, count INTEGER NOT NULL, timestamp TEXT NOT NULL, is_active TEXT, FOREIGN KEY(username) REFERENCES user_master(username) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS user_credentials ( credential_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, password TEXT, pass_status TEXT, pass_is_active TEXT, master_password TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS role_master ( role_id INTEGER, role_description TEXT NOT NULL UNIQUE, is_active TEXT NOT NULL, PRIMARY KEY(role_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS main_aux_pump_map ( main_aux_pump_map_id INTEGER PRIMARY KEY AUTOINCREMENT, main_pump TEXT, aux_pump INTEGER );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS mode_master ( mode_id INTEGER PRIMARY KEY AUTOINCREMENT, mode_name TEXT, mode_full_name TEXT, trial_master_id INTEGER, pcv TEXT, exp_yield_fc TEXT, vol_cf TEXT, filter_retention TEXT, is_active TEXT, FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS pump_master ( pump_id INTEGER PRIMARY KEY AUTOINCREMENT, head_count INTEGER, pump_name TEXT, pump_type TEXT, direction TEXT, flow_control TEXT, pump_tubing_size TEXT, pump_tubing_lookup_id INTEGER, flow_rate TEXT, ramp_up_time INTEGER, trial_master_id INTEGER, is_active TEXT, delta_p TEXT default '', delta_p_rate TEXT default '', delta_p_duration TEXT default '', pump_rpm TEXT default '', is_speed INTEGER default 0, FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS trial_detail ( trial_detail_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_master_id INTEGER, notes TEXT, run_as_safe TEXT, feed_to_empty TEXT, no_of_aux_pump INTEGER, perm_stop_first TEXT, recirc_pressure_cont TEXT, recirc_flow_unit_value INTEGER, recirc_rate_per_unit INTEGER, retentate_tube_size TEXT, is_active TEXT, endpoint_cal INTEGER DEFAULT 0, digital_notes TEXT default '', FOREIGN KEY(trial_master_id) REFERENCES trial_master(trial_master_id) )");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS target_step ( target_step_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_master_id INTEGER, tmp TEXT, duration TEXT, flow_rate TEXT, permeate_weight TEXT, is_active TEXT DEFAULT 'Y' , permeate_total TEXT DEFAULT '')");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS trial_master ( trial_master_id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_name TEXT, trial_type TEXT, trial_status TEXT, created_by TEXT, created_on TEXT, modified_by TEXT, modified_on TEXT, is_active TEXT DEFAULT 'Y' );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS user_details ( user_details_id INTEGER, user_id INTEGER UNIQUE, prefix TEXT NOT NULL, first_name TEXT NOT NULL, middle_name TEXT, last_name TEXT NOT NULL, full_name TEXT, email_id TEXT NOT NULL UNIQUE, contact_number TEXT, last_login_date TEXT, PRIMARY KEY(user_details_id), FOREIGN KEY(user_id) REFERENCES user_master(user_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS pump_lookup ( pump_lookup_id INTEGER PRIMARY KEY AUTOINCREMENT, pump_type TEXT, pump_name TEXT, min_speed REAL, max_speed INTEGER, speed_unit TEXT, head_count TEXT, is_active INTEGER DEFAULT 1 );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS user_master ( user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, is_active NUMERIC DEFAULT 'Y', created_by TEXT, created_on TEXT NOT NULL, modified_by TEXT, modified_on TEXT, org_id TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS manual_abv_master ( abv_setting_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, type INTEGER, abv_type TEXT, position TEXT, tubing_size TEXT, abv_mode TEXT, op_pressure TEXT, abv_target TEXT, trial_run_setting_id INTEGER );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS tubing_lookup ( tubing_lookup_id INTEGER PRIMARY KEY AUTOINCREMENT, tubing_size TEXT, revolutions INTEGER, expected_volume INTEGER, flow_conversion INTEGER, flow_conversion_KRJr REAL, `1h_decimals` INTEGER, `1h_rate_digits` INTEGER, `2h_rate_digits` INTEGER, `1h_cal_digits` INTEGER, `2h_cal_digits` INTEGER, cal_motor_revs INTEGER, cal_volume INTEGER, `2h_decimals` INTEGER, tubing_integer TEXT, valve_pos_full_open TEXT, valve_pos_full_pinched TEXT, start_step_size TEXT, OD_mm TEXT, wall TEXT, gap TEXT, internal_diameter_in REAL, internal_diameter_cm REAL, internal_diameter_mm REAL, calibration_factor REAL DEFAULT 1, min_rpm INTEGER, max_rpm INTEGER, min_flow_rate INTEGER, max_flow_rate INTEGER, flow_unit TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS cfr_status ( status_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, status TEXT NOT NULL );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS license_activation_status ( status_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, status TEXT NOT NULL );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS trial_run_setting ( trial_run_setting_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, trial_id TEXT UNIQUE, trial_master_id INTEGER, is_feed_empty TEXT, feed_hold_up TEXT, feed_start_wt TEXT, trial_type TEXT, trial_status INTEGER DEFAULT -1, temperature TEXT, user_id INTEGER, trial_start_time TEXT, trial_end_time TEXT, updated_on TEXT, created_on TEXT, is_active TEXT DEFAULT 'Y' , totalizer_unit INTEGER DEFAULT 0, totalizer_unit_2 INTEGER DEFAULT 0, q_perm_freq INTEGER default 1, feed_scale_override TEXT default 'false', device_id TEXT default '', delta_p TEXT default '', delta_p_rate TEXT default '', delta_p_duration TEXT default '', valve_2_connector TEXT default '')");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS firmware_version ( firmware_version_id INTEGER PRIMARY KEY AUTOINCREMENT, software_version INTEGER, interface_version TEXT , kfcomm_version TEXT)");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS db_version ( db_version_id INTEGER PRIMARY KEY AUTOINCREMENT, version INTEGER NOT NULL );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS end_point_change_log ( end_point_change_log_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, trial_log_id INTEGER NOT NULL, end_point_type TEXT NOT NULL, old_end_point_value REAL NOT NULL, new_end_point_value REAL NOT NULL, step_no INTEGER NOT NULL, is_active INTEGER NOT NULL DEFAULT 1, trial_run_setting_id INTEGER );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS end_point_setting ( end_point_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_run_setting_id INTEGER, end_point_type TEXT, end_point_value TEXT, step_no INTEGER, created_on TEXT, updated_on TEXT, is_active TEXT DEFAULT 'Y', FOREIGN KEY(trial_run_setting_id) REFERENCES trial_run_setting(trial_run_setting_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS target_step_setting ( target_step_setting_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_run_setting_id INTEGER, tmp TEXT, duration TEXT, permeate_weight TEXT, step_no INTEGER, created_on TEXT, updated_on TEXT, is_active TEXT DEFAULT 'Y' , permeate_total TEXT DEFAULT '')");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS target_step_change_log ( target_step_change_log_id INTEGER PRIMARY KEY AUTOINCREMENT, trial_log_id INTEGER, old_tmp TEXT, new_tmp TEXT, old_duration TEXT, new_duration TEXT, old_permeate_weight TEXT, new_permeate_weight TEXT, step_no INTEGER, is_active TEXT DEFAULT 'Y', trial_run_setting_id INTEGER , old_permeate_total TEXT DEFAULT '', new_permeate_total TEXT DEFAULT '')");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS flyway_schema_history ( installed_rank INT NOT NULL, version VARCHAR(50), description VARCHAR(200) NOT NULL, type VARCHAR(20) NOT NULL, script VARCHAR(1000) NOT NULL, checksum INT, installed_by VARCHAR(100) NOT NULL, installed_on TEXT NOT NULL DEFAULT (strftime('%Y-%m-%d %H:%M:%f','now')), execution_time INT NOT NULL, success BOOLEAN NOT NULL, PRIMARY KEY(installed_rank) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS registry_hash ( registry_hash_id INTEGER PRIMARY KEY AUTOINCREMENT, reg_hash TEXT );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS notes_desc ( notes_desc_id INTEGER, trial_log_id INTEGER NOT NULL UNIQUE, sub_mode_name TEXT, step INTEGER, end_point_name TEXT, end_point_value REAL, status INTEGER, is_active INTEGER DEFAULT 1, FOREIGN KEY(trial_log_id) REFERENCES trial_log(trial_log_id), PRIMARY KEY(notes_desc_id AUTOINCREMENT) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS notes_desc_setup ( notes_desc_setup_id INTEGER, trial_log_id INTEGER NOT NULL UNIQUE, mode_name TEXT, tmp REAL, duration TEXT, permeate_weight REAL, permeate_total REAL, flow_rate REAL, step INTEGER, status INTEGER, PRIMARY KEY(notes_desc_setup_id AUTOINCREMENT), FOREIGN KEY(trial_log_id) REFERENCES trial_log(trial_log_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS notes_desc_manual ( mn_id INTEGER, trial_log_id INTEGER NOT NULL UNIQUE, mn_device TEXT, mn_type TEXT, mn_old_value TEXT, mn_new_value TEXT, PRIMARY KEY(mn_id AUTOINCREMENT), FOREIGN KEY(trial_log_id) REFERENCES trial_log(trial_log_id) );");

				statement.execute(
						"CREATE TABLE IF NOT EXISTS com_port (port_id INTEGER, port_name TEXT, PRIMARY KEY(port_id AUTOINCREMENT));");

				statement.execute("CREATE TABLE IF NOT EXISTS manual_filter_details (mn_filter_id INTEGER, filter_type TEXT, part_no TEXT, no_of_cassette INTEGER, is_non_repligen INTEGER, surf_area TEXT, trial_run_setting_id INTEGER, PRIMARY KEY(mn_filter_id AUTOINCREMENT) );");
				
				statement.execute("INSERT INTO abv_master SELECT * FROM oldDB.abv_master");
				statement.execute("INSERT INTO alarm_history SELECT * FROM oldDB.alarm_history");
				statement.execute("INSERT INTO alarm_setting SELECT * FROM oldDB.alarm_setting;");
				statement.execute("INSERT INTO aux_pump_master SELECT * FROM oldDB.aux_pump_master;");
				statement.execute("INSERT INTO cassette_lookup SELECT * FROM oldDB.cassette_lookup;");
				statement.execute("INSERT INTO cfr_status SELECT * FROM oldDB.cfr_status;");
				statement.execute("INSERT INTO db_version SELECT * FROM oldDB.db_version");
				statement.execute("INSERT INTO end_point SELECT * FROM oldDB.end_point;");
				statement.execute("INSERT INTO end_point_change_log SELECT * FROM oldDB.end_point_change_log;");
				statement.execute("INSERT INTO end_point_lookup SELECT * FROM oldDB.end_point_lookup;");
				statement.execute("INSERT INTO end_point_setting SELECT * FROM oldDB.end_point_setting;");
				statement.execute("INSERT INTO filter_perm_master SELECT * FROM oldDB.filter_perm_master;");
				statement.execute(
						"INSERT INTO filter_plate_insert_lookup SELECT * FROM oldDB.filter_plate_insert_lookup;");
				statement.execute("INSERT INTO firmware_version SELECT * FROM oldDB.firmware_version;");
				statement.execute("INSERT INTO flyway_schema_history SELECT * FROM oldDB.flyway_schema_history;");
				statement.execute("INSERT INTO hollow_fiber_lookup SELECT * FROM oldDB.hollow_fiber_lookup;");
				statement.execute("INSERT INTO kf_konduit_setting SELECT * FROM oldDB.kf_konduit_setting;");
				statement.execute(
						"INSERT INTO license_activation_status SELECT * FROM oldDB.license_activation_status;");
				statement.execute("INSERT INTO main_aux_pump_map SELECT * FROM oldDB.main_aux_pump_map;");
				statement.execute("INSERT INTO manual_abv_master SELECT * FROM oldDB.manual_abv_master;");
				statement.execute("INSERT INTO manual_pump_master SELECT * FROM oldDB.manual_pump_master;");
				statement.execute("INSERT INTO mode_lookup SELECT * FROM oldDB.mode_lookup;");
				statement.execute("INSERT INTO mode_master SELECT * FROM oldDB.mode_master;");
				statement.execute("INSERT INTO notes SELECT * FROM oldDB.notes;");
				statement.execute("INSERT INTO pump_lookup SELECT * FROM oldDB.pump_lookup;");
				statement.execute("INSERT INTO pump_master SELECT * FROM oldDB.pump_master;");
				statement.execute("INSERT INTO pump_mode_map SELECT * FROM oldDB.pump_mode_map;");
				statement.execute("INSERT INTO pump_tube_map SELECT * FROM oldDB.pump_tube_map;");
				statement.execute("INSERT INTO ramp_up_time SELECT * FROM oldDB.ramp_up_time;");
				statement.execute("INSERT INTO recirc_flow_unit SELECT * FROM oldDB.recirc_flow_unit;");
				statement.execute("INSERT INTO role_master SELECT * FROM oldDB.role_master;");
				statement.execute("INSERT INTO system_setting_details SELECT * FROM oldDB.system_setting_details;");
				statement.execute("INSERT INTO target_step SELECT * FROM oldDB.target_step;");
				statement.execute("INSERT INTO target_step_change_log SELECT * FROM oldDB.target_step_change_log;");
				statement.execute("INSERT INTO target_step_setting SELECT * FROM oldDB.target_step_setting;");
				statement.execute("INSERT INTO trial_detail SELECT * FROM oldDB.trial_detail;");
				statement.execute("INSERT INTO trial_log SELECT * FROM oldDB.trial_log;");
				statement.execute("INSERT INTO trial_master SELECT * FROM oldDB.trial_master;");
				statement.execute("INSERT INTO trial_run_setting SELECT * FROM oldDB.trial_run_setting;");
				statement.execute("INSERT INTO tubing_lookup SELECT * FROM oldDB.tubing_lookup;");
				statement.execute("INSERT INTO user_access_log SELECT * FROM oldDB.user_access_log;");
				statement.execute("INSERT INTO user_credentials SELECT * FROM oldDB.user_credentials;");
				statement.execute("INSERT INTO user_details SELECT * FROM oldDB.user_details;");
				statement.execute("INSERT INTO user_login_limit SELECT * FROM oldDB.user_login_limit;");
				statement.execute("INSERT INTO user_master SELECT * FROM oldDB.user_master;");
				statement.execute("INSERT INTO user_role_map SELECT * FROM oldDB.user_role_map;");
				statement.execute("INSERT INTO registry_hash SELECT * FROM oldDB.registry_hash;");
				statement.execute("INSERT INTO trial_hist_data SELECT * FROM oldDB.trial_hist_data;");
				statement.execute("INSERT INTO notes_desc SELECT * FROM oldDB.notes_desc;");
				statement.execute("INSERT INTO notes_desc_setup SELECT * FROM oldDB.notes_desc_setup;");
				statement.execute("INSERT INTO notes_desc_manual SELECT * FROM oldDB.notes_desc_manual;");
				statement.execute("INSERT INTO com_port SELECT * FROM oldDB.com_port;");
				statement.execute("INSERT INTO manual_filter_details SELECT * FROM oldDB.manual_filter_details;");
				statement.execute(MessageFormat.format("PRAGMA rekey=''{0}''", PasswordUtil
						.decrypt(ResourceManager.getProperty(ApiConstant.DB_ENCRYPTION_KEY), ApiConstant.SECRET)));
				sLog.d("ENCRYPT_PROCESS: Copying data to new DB successfully");
			}
			return true;
		} catch (Exception e) {
			sLog.d("ENCRYPT_PROCESS: Copying data to encrypted DB failed");
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(null, null, con);
		}
	}

	public static void deleteFailedDBFile(final String name) {
		sLog.d("ENCRYPT_PROCESS: Deleting DB file");
		final File failedTempFile = new File(
				ResourceManager.getProperty(ApiConstant.JRE_FOLDER_LOCATION) + ResourceManager.getProperty(name));
		if (failedTempFile.exists()) {
			failedTempFile.delete();
		}
		sLog.d("ENCRYPT_PROCESS: DB file deleted");
	}

	public static void renameDBFile(final String fromName, final String toName) {
		sLog.d("ENCRYPT_PROCESS: Renaming DB file...");
		final File sourceFile = new File(
				ResourceManager.getProperty(ApiConstant.JRE_FOLDER_LOCATION) + ResourceManager.getProperty(fromName));
		final File destFile = new File(
				ResourceManager.getProperty(ApiConstant.JRE_FOLDER_LOCATION) + ResourceManager.getProperty(toName));
		if (sourceFile.exists() && sourceFile.isFile()) {
			if (!sourceFile.renameTo(destFile)) {
				sLog.d("ENCRYPT_PROCESS: Renaming DB file failed");
				throw new RuntimeException(ApiConstant.ERROR_RENAME_FILE);
			}
			sLog.d("ENCRYPT_PROCESS: DB file renamed");
		} else {
			sLog.d("ENCRYPT_PROCESS: Renaming DB file failed");
			throw new RuntimeException(ApiConstant.ERROR_RENAME_FILE);
		}
	}

	public static void setEncryptionStatus(final String encryptStatus) throws Exception {
		FileInputStream in = null;
		FileOutputStream out = null;
		Properties props = null;
		try {
			sLog.d("ENCRYPT_PROCESS: Setting encryption status...");
			in = new FileInputStream(ResourceManager.getProperty(ApiConstant.ENCRYPT_PROPERTIES_FILE_LOCATION));
			props = new Properties();
			props.load(in);
			in.close();

			out = new FileOutputStream(ResourceManager.getProperty(ApiConstant.ENCRYPT_PROPERTIES_FILE_LOCATION));
			props.setProperty(ApiConstant.DB_ENCRYPT_STATUS, PasswordUtil.encrypt(encryptStatus, ApiConstant.SECRET));
			props.store(out, null);
			out.close();
			sLog.d("ENCRYPT_PROCESS: Encryption status set successfully");
		} catch (Exception e) {
			e.printStackTrace();
			sLog.d("ENCRYPT_PROCESS: Setting encryption status failed");
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	private static final String getEncryptionStatus() throws Exception {
		FileInputStream in = null;
		Properties props = null;
		try {
			sLog.d("ENCRYPT_PROCESS: Getting encryption status...");
			in = new FileInputStream(ResourceManager.getProperty(ApiConstant.ENCRYPT_PROPERTIES_FILE_LOCATION));
			props = new Properties();
			props.load(in);
			return PasswordUtil.decrypt(props.getProperty(ApiConstant.DB_ENCRYPT_STATUS), ApiConstant.SECRET);
		} catch (Exception e) {
			e.printStackTrace();
			sLog.d("ENCRYPT_PROCESS: Getting encryption status failed");
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static final boolean createEncryptProperties() throws Exception {
		sLog.d("ENCRYPT_PROCESS: Creating encrypt.properties file...");
		BufferedWriter output = null;
		try {
			File file = new File(ResourceManager.getProperty(ApiConstant.ENCRYPT_PROPERTIES_FILE_LOCATION));
			output = new BufferedWriter(new FileWriter(file));
			output.write(ApiConstant.DB_ENCRYPT_STATUS + "="
					+ PasswordUtil.encrypt(ApiConstant.ENCRYPTION_NOT_ACTIVE, ApiConstant.SECRET));
			sLog.d("ENCRYPT_PROCESS: encrypt.properties file created");
			return true;
		} catch (Exception e) {
			sLog.d("ENCRYPT_PROCESS: Failed creating encrypt.properties file");
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
		return false;
	}

}
