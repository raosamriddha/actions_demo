package org.lattice.spectrum_backend_final.dao.constants;

public final class QueryConstant {

	private QueryConstant() {
	}

	// system settings
	public static final String INSERT_SYSTEM_SETTINGS = "insert into system_setting_details (pressure_unit,weight_unit,"
			+ "volume_unit,length_unit,decimal_pressure,decimal_weight,decimal_volume,decimal_length,decimal_flow_rate,"
			+ "date_format,is_aux_konduit,uv_min,uv_max,ph_min,ph_max,turbidity_min,"
			+ "turbidity_max,totalizer_min,totalizer_max,created_on,updated_on,"
			+ "is_aux_konduit_2,ph_min_2,ph_max_2,turbidity_min_2,turbidity_max_2,totalizer_min_2,totalizer_max_2,"
			+ "protein_conc_min,protein_conc_max,protein_conc_min_2,protein_conc_max_2,totalizer_unit,totalizer_unit_2,is_active) "
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)";

	public static final String FETCH_SYSTEM_SETTINGS = "select * from system_setting_details where is_active = 1";

	public static final String CHECK_SYSTEM_SETTING_EXISTS = "select count(*) from system_setting_details where system_id = ?";

	//	public static final String UPDATE_SYSTEM_SETTINGS = "update system_setting_details set pressure_unit = ?, weight_unit = ?,"
	//			+ "volume_unit = ?, length_unit = ?, decimal_pressure = ?, decimal_weight = ?, decimal_volume = ?, "
	//			+ "decimal_length = ?, decimal_flow_rate = ?, date_format = ?,"
	//			+ "is_aux_konduit = ?, uv_min = ?, uv_max = ?, ph_min = ?, ph_max = ?, turbidity_min = ?, turbidity_max = ?,"
	//			+ "totalizer_min = ?, totalizer_max = ?, updated_on = ?, is_aux_konduit_2 = ?, ph_min_2 = ?, ph_max_2 = ?, "
	//			+ "turbidity_min_2 = ?, turbidity_max_2 = ?, totalizer_min_2 = ?, totalizer_max_2 = ?, "
	//			+ "protein_conc_min = ?,  protein_conc_max = ?, protein_conc_min_2 = ?, protein_conc_max_2 = ? where system_id = ?";

	public static final String INACTIVE_PREVIOUS_SYSTEM_SETTINGS = "update system_setting_details set is_active = 0 where system_id != ?";

	// license activation status
	public static final String FETCH_LICENSE_STATUS = "select * from license_activation_status limit 1";
	public static final String UPDATE_LICENSE_STATUS = "update license_activation_status set status = ? where status_id = ?";

	// manual mode
	public static final String INSERT_KF_KONDUIT_SETTING = "insert into kf_konduit_setting (k_factor_ch_1,k_factor_ch_2,trial_run_setting_id,is_active)"
			+ " values(?,?,?,'Y')";

	public static final String CHECK_KF_KONDUIT_SETTING_EXISTS = "select count(*) from kf_konduit_setting where trial_run_setting_id = ?";

	public static final String UPDATE_KF_CONDUIT_DETAILS = "update kf_konduit_setting set k_factor_ch_1 = ?, k_factor_ch_2 = ? "
			+ "where trial_run_setting_id = ?";

	public static final String CHECK_TRIAL_RUN_SETTING_EXISTS = "select count(*) from trial_run_setting where trial_run_setting_id = ?";

	public static final String INSERT_AUX_PUMP_SETTING = "insert into aux_pump_master "
			+ "(aux_pump_type,aux_tubing_size,flow_rate,type,pump_speed,trial_run_setting_id,is_active) values(?,?,?,?,?,?,'Y')";

	public static final String CHECK_AUX_PUMP_EXISTS = "select count(*) from aux_pump_master where trial_run_setting_id = ? and type = ?";

	public static final String UPDATE_AUX_PUMP_DETAILS = "update aux_pump_master set aux_pump_type = ?, aux_tubing_size = ?, "
			+ "flow_rate = ?, pump_speed = ? where type = ? and trial_run_setting_id = ?";

	public static final String INSERT_ABV_SETTING = "insert into manual_abv_master (type,abv_type,position,tubing_size,abv_mode, op_pressure, abv_target, trial_run_setting_id) "
			+ "values(?,?,?,?,?,?,?,?)";

	public static final String UPDATE_ABV_DETAILS = "update manual_abv_master set abv_type = ?, position = ?, "
			+ "tubing_size = ?, abv_mode = ?, op_pressure = ?, abv_target = ? where type = ? and trial_run_setting_id = ?";

	public static final String CHECK_ABV_EXISTS = "select count(*) from manual_abv_master where trial_run_setting_id = ? and type = ?";

	public static final String INSERT_MAIN_PUMP_SETTING = "insert into manual_pump_master (pump_name,speed,flow_rate,direction,trial_run_setting_id,tubing_size) "
			+ "values(?,?,?,?,?,?)";

	public static final String CHECK_MAIN_PUMP_EXISTS = "select count(*) from manual_pump_master where trial_run_setting_id = ?";

	public static final String UPDATE_MAIN_PUMP_DETAILS = "update manual_pump_master set pump_name = ?, speed = ?, flow_rate = ?, "
			+ "direction = ?, tubing_size = ? where trial_run_setting_id = ?";

	public static final String FETCH_KF_CONDUIT_SETTING = "select k_factor_ch_1,k_factor_ch_2 from kf_konduit_setting where trial_run_setting_id =?";

	public static final String FETCH_AUX_PUMPS_SETTING = "select * from aux_pump_master where trial_run_setting_id = ?";

	public static final String FETCH_AUX_PUMPS_SETTING_BY_TYPE = "select * from aux_pump_master where trial_run_setting_id = ? and type = ?";

	public static final String FETCH_ABV_SETTING = "select * from manual_abv_master where trial_run_setting_id = ?";

	public static final String FETCH_ABV_SETTING_BY_TYPE = "select * from manual_abv_master where trial_run_setting_id = ? and type = ?";

	public static final String FETCH_MAIN_PUMP_SETTING = "select * from manual_pump_master where trial_run_setting_id = ?";

	public static final String FETCH_PUMP_LOOK_UP_LIST = "select pump_lookup_id,pump_type,pump_name,min_speed,max_speed,speed_unit from pump_lookup";

	public static final String GET_ALARM_SETTING_FOR_MANUAL_QUERY = "select * from alarm_setting where trial_run_setting_id = ?";

	public static final String FETCH_TABLE_LOGS_FORWARD_HISTORICAL = "select * from trial_hist_data where trial_live_data_id > ? and "
			+ "trial_run_setting_id = ? order by trial_live_data_id desc limit ?";

	public static final String FETCH_TABLE_LOGS_BACKWARD_HISTORICAL = "select * from trial_hist_data where trial_live_data_id < ? and "
			+ "trial_run_setting_id = ? order by trial_live_data_id desc limit ?";

	public static final String FETCH_TABLE_LOGS_FIRST_ROW_TIMESTAMP = "select thd.*, kfs.k_factor_ch_1, kfs.k_factor_ch_2,trs.trial_id from trial_hist_data thd LEFT join kf_konduit_setting kfs on kfs.trial_run_setting_id = thd.trial_run_setting_id left join trial_run_setting trs on trs.trial_run_setting_id = thd.trial_run_setting_id where thd.trial_run_setting_id = ? and thd.timestamp <= ? order by thd.trial_live_data_id desc limit 1";

	public static final String FETCH_TABLE_LOGS_FIRST_ROW_OFFSET = "select * from trial_hist_data where trial_run_setting_id = ? and trial_live_data_id = ? order by trial_live_data_id desc limit 1";

	public static final String FETCH_TABLE_LOGS_TIMESTAMP = "select * from trial_hist_data where trial_run_setting_id = ? and timestamp between ? and ? order by trial_live_data_id desc limit ?";

	public static final String FETCH_TRIAL_TOTAL_PAGES = "select count(*) from trial_hist_data where trial_run_setting_id = ?";

	public static final String FETCH_ALARM_LOGS_WITHOUT_OFFSET = "select * from alarm_history where trial_run_setting_id = ? order by alarm_history_id desc limit ?";

	public static final String FETCH_ALARM_LOGS_WITH_OFFSET_BACKWARD = "select * from alarm_history where trial_run_setting_id = ? and alarm_history_id < ?  order by alarm_history_id desc limit ?";

	public static final String FETCH_ALARM_LOGS_WITH_OFFSET_FORWARD = "select * from alarm_history where trial_run_setting_id = ? and alarm_history_id > ? limit ?";

	public static final String FETCH_ALARM_TOTAL_PAGES = "select count(*) from alarm_history where trial_run_setting_id = ?";

	public static final String FETCH_NOTES_LOGS = "select * from notes where trial_run_setting_id = ? order by notes_id desc";

	// todo notes change
	public static final String FETCH_TRIAL_LOGS_WITHOUT_OFFSET = "SELECT tl.trial_log_id, tl.action, tl.created_on, "
			+ "um.username, ud.full_name, " + "rm.role_description, trs.trial_id from trial_log tl \r\n"
			+ "LEFT JOIN user_master um on um.user_id = tl.user_id\r\n"
			+ "LEFT JOIN user_details ud on ud.user_id = tl.user_id\r\n"
			+ "LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id\r\n"
			+ "LEFT JOIN role_master rm on rm.role_id = \n"
			+ "(select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4))\r\n"
			+ "where tl.trial_run_setting_id = ? and tl.log_type = 0 order by tl.trial_log_id desc limit ?";

	// todo notes change
	public static final String FETCH_TRIAL_LOGS_WITH_OFFSET_BACKWARD = "SELECT tl.trial_log_id, tl.action, tl.created_on, "
			+ "um.username, ud.full_name, " + "rm.role_description, trs.trial_id from trial_log tl \r\n"
			+ "LEFT JOIN user_master um on um.user_id = tl.user_id\r\n"
			+ "LEFT JOIN user_details ud on ud.user_id = tl.user_id\r\n"
			+ "LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id\r\n"
			+ "LEFT JOIN role_master rm on rm.role_id = \n"
			+ "(select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4))\r\n"
			+ "where tl.trial_run_setting_id = ? and tl.trial_log_id < ? and tl.log_type = 0 order by tl.trial_log_id desc limit ?";

	// todo notes change
	public static final String FETCH_TRIAL_LOGS_WITH_OFFSET_FORWARD = "SELECT tl.trial_log_id, tl.action, tl.created_on, "
			+ "um.username, ud.full_name, " + "rm.role_description, trs.trial_id from trial_log tl \r\n"
			+ "LEFT JOIN user_master um on um.user_id = tl.user_id\r\n"
			+ "LEFT JOIN user_details ud on ud.user_id = tl.user_id\r\n"
			+ "LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id\r\n"
			+ "LEFT JOIN role_master rm on rm.role_id = \n"
			+ "(select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4))\r\n"
			+ "where tl.trial_run_setting_id = ? and tl.trial_log_id > ? and tl.log_type = 0 limit ?";

	// todo notes change
	public static final String SEARCH_TRIAL_LOGS_WITHOUT_OFFSET = "SELECT tl.trial_log_id, tl.action, tl.created_on, "
			+ "um.username, ud.full_name, " + "rm.role_description, trs.trial_id from trial_log tl \r\n"
			+ "LEFT JOIN user_master um on um.user_id = tl.user_id\r\n"
			+ "LEFT JOIN user_details ud on ud.user_id = tl.user_id\r\n"
			+ "LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id\r\n"
			+ "LEFT JOIN role_master rm on rm.role_id = (select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4))\r\n"
			+ "where tl.trial_run_setting_id = ? and tl.log_type = 0 and (um.username LIKE ? OR ud.full_name LIKE ? OR tl.action LIKE ? OR "
			+ "tl.created_on LIKE ? OR rm.role_description LIKE ? OR trs.trial_id LIKE ?) order by tl.trial_log_id desc limit ?";

	// todo notes change
	public static final String SEARCH_TRIAL_LOGS_WITH_OFFSET_BACKWARD = "SELECT tl.trial_log_id, tl.action, tl.created_on, "
			+ "um.username, ud.full_name, " + "rm.role_description, trs.trial_id from trial_log tl \r\n"
			+ "LEFT JOIN user_master um on um.user_id = tl.user_id\r\n"
			+ "LEFT JOIN user_details ud on ud.user_id = tl.user_id\r\n"
			+ "LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id\r\n"
			+ "LEFT JOIN role_master rm on rm.role_id = (select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4))\r\n"
			+ "where tl.trial_run_setting_id = ? and tl.trial_log_id < ? and tl.log_type = 0 and (um.username LIKE ? OR ud.full_name LIKE ? OR tl.action LIKE ? OR "
			+ "tl.created_on LIKE ? OR rm.role_description LIKE ? OR trs.trial_id LIKE ?) order by tl.trial_log_id desc limit ?";

	// todo notes change - done
	public static final String SEARCH_TRIAL_LOGS_WITH_OFFSET_FORWARD = "SELECT tl.trial_log_id, tl.action, tl.created_on, "
			+ "um.username, ud.full_name, " + "rm.role_description, trs.trial_id from trial_log tl \r\n"
			+ "LEFT JOIN user_master um on um.user_id = tl.user_id\r\n"
			+ "LEFT JOIN user_details ud on ud.user_id = tl.user_id\r\n"
			+ "LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id\r\n"
			+ "LEFT JOIN role_master rm on rm.role_id = (select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4))\r\n"
			+ "where tl.trial_run_setting_id = ? and tl.trial_log_id > ? and (tl.log_type = 0 OR tl.log_type = 2) and (um.username LIKE ? OR ud.full_name LIKE ? OR tl.action LIKE ? OR "
			+ "tl.created_on LIKE ? OR rm.role_description LIKE ? OR trs.trial_id LIKE ?) limit ?";

	// todo notes change - done
	public static final String FETCH_TRIAL_LOGS_TOTAL_PAGES = "select count(*) as count from trial_log where trial_run_setting_id = ? and (log_type = 0 OR log_type = 2) ";
	// todo notes change - done
	public static final String FETCH_TRIAL_LOGS_TOTAL_PAGES_SEARCH = "SELECT count(*) as count , ud.full_name from trial_log tl LEFT JOIN user_master um on um.user_id = tl.user_id LEFT JOIN user_details ud on ud.user_id = tl.user_id LEFT JOIN trial_run_setting trs on trs.trial_run_setting_id = tl.trial_run_setting_id LEFT JOIN role_master rm on rm.role_id = (select role_id from user_role_map where user_id = tl.user_id and (role_id = 3 or role_id = 4)) where tl.trial_run_setting_id = ? and (tl.log_type = 0 OR tl.log_type = 2) and (um.username LIKE ? OR ud.full_name LIKE ? OR tl.action LIKE ? OR tl.created_on LIKE ? OR rm.role_description LIKE ? OR trs.trial_id LIKE ?)";

	public static final String ALARM_LOGS_WITHOUT_OFFSET = "select * from alarm_history where trial_run_setting_id = ? and alarm_stop_desc = ? and nature IN (?) order by alarm_history_id desc limit ?";

	public static final String GET_TRIAL_START_TIME = "select timestamp from trial_hist_data where trial_run_setting_id = ? limit 1";

	public static final String GET_TRIAL_END_TIME = "select timestamp from trial_hist_data where trial_run_setting_id = ? order by trial_live_data_id desc limit 1";

	public static final String FETCH_TRIAL_LOGS_LIST = "SELECT trs.trial_id, trs.trial_master_id, trs.trial_run_setting_id, trs.trial_status, trs.trial_start_time, trs.trial_end_time, tm.recipe_name, um.username from trial_run_setting trs left join trial_master tm on tm.trial_master_id = trs.trial_master_id left join user_master um on um.user_id = trs.user_id where trs.is_active = 'Y'";

	public static final String FETCH_TRIAL_DETAILS = "select trs.*, um.username from trial_run_setting trs left join user_master um on um.user_id = trs.user_id where trial_run_setting_id = ?";

	public static final String FETCH_ALARM_DETAILS = "SELECT * from alarm_setting where trial_run_setting_id = ?";

	public static final String UPDATE_TUBING_CALIBRATION_FACTOR = "update tubing_lookup set calibration_factor = ? where tubing_lookup_id = ?";

	public static final String FETCH_FEED_PRESSURE_CALIBRATE_LOG = "SELECT tl.created_on, um.username from trial_log tl LEFT JOIN user_master um on um.user_id = tl.user_id where log_type = 1 and action = 'Feed Pressure Calibrated' order by trial_log_id desc limit 1";

	public static final String FETCH_RETENTATE_PRESSURE_CALIBRATE_LOG = "SELECT tl.created_on, um.username from trial_log tl LEFT JOIN user_master um on um.user_id = tl.user_id where log_type = 1 and action = 'Retentate Pressure Calibrated' order by trial_log_id desc limit 1";

	public static final String FETCH_PERMEATE_PRESSURE_CALIBRATE_LOG = "SELECT tl.created_on, um.username from trial_log tl LEFT JOIN user_master um on um.user_id = tl.user_id where log_type = 1 and action = 'Permeate Pressure Calibrated' order by trial_log_id desc limit 1";

	public static final String FETCH_TUBING_CALIBRATE_LOG = "SELECT tl.created_on, um.username from trial_log tl LEFT JOIN user_master um on um.user_id = tl.user_id where log_type = 1 and action = 'Pump Tubing Calibrated' order by trial_log_id desc limit 1";

	public static final String FETCH_TRIAL_HISTORY_LOGS = "SELECT tl.log_type, tl.trial_log_id, tl.[action], tl.created_on, um.username, ud.full_name, rm.role_description, trs.trial_id, epcl.end_point_type, epcl.old_end_point_value, epcl.new_end_point_value, epcl.step_no, epcl.end_point_change_log_id, tscl.target_step_change_log_id, tscl.old_tmp, tscl.new_tmp, tscl.old_duration, tscl.new_duration, tscl.old_permeate_weight, tscl.new_permeate_weight, tscl.old_permeate_total, tscl.new_permeate_total, tscl.step_no AS ts_step_no, nd.end_point_name, nd.notes_desc_id, nd.step as nd_step, nd.status, nd.sub_mode_name, nd.end_point_value, nds.step, nds.duration, nds.flow_rate, nds.notes_desc_setup_id, nds.permeate_total, nds.permeate_weight, nds.tmp, nds.mode_name, nds.status as setup_status, ndm.* FROM trial_log tl LEFT JOIN user_master um ON um.user_id = tl.user_id LEFT JOIN user_details ud ON ud.user_id = tl.user_id LEFT JOIN trial_run_setting trs ON trs.trial_run_setting_id = tl.trial_run_setting_id LEFT JOIN role_master rm ON rm.role_id = ( SELECT role_id FROM user_role_map WHERE user_id = tl.user_id AND (role_id = 3 OR role_id = 4) ) LEFT JOIN end_point_change_log epcl ON epcl.trial_log_id = tl.trial_log_id LEFT JOIN target_step_change_log tscl ON tscl.trial_log_id = tl.trial_log_id LEFT JOIN notes_desc nd ON nd.trial_log_id = tl.trial_log_id LEFT JOIN notes_desc_setup nds ON nds.trial_log_id = tl.trial_log_id LEFT JOIN notes_desc_manual ndm ON ndm.trial_log_id = tl.trial_log_id WHERE tl.trial_run_setting_id = ? AND (tl.log_type = 0 OR tl.log_type = 2) ORDER BY tl.trial_log_id DESC LIMIT 1000;";

	public static final String FETCH_TRIAL_ALARMS_LOGS = "select * from alarm_history where trial_run_setting_id = ? order by alarm_history_id asc";

	public static final String FETCH_TRIAL_TABLE_LOGS = "select thd.*, kfs.k_factor_ch_1, kfs.k_factor_ch_2, trs.trial_id from trial_hist_data thd left join kf_konduit_setting kfs on kfs.trial_run_setting_id = thd.trial_run_setting_id left join trial_run_setting trs on trs.trial_run_setting_id = thd.trial_run_setting_id where thd.trial_run_setting_id = ? order by thd.trial_live_data_id asc";

	public static final String FETCH_TRIAL_TABLE_LOGS_INTERVAL = "select thd.*, kfs.k_factor_ch_1, kfs.k_factor_ch_2, trs.trial_id from trial_hist_data thd left join kf_konduit_setting kfs on kfs.trial_run_setting_id = thd.trial_run_setting_id left join trial_run_setting trs on trs.trial_run_setting_id = thd.trial_run_setting_id where thd.trial_run_setting_id = ?";

	public static final String FETCH_NOTES_LOGS_ASC = "select * from notes where trial_run_setting_id = ? order by notes_id asc";

	public static final String FETCH_TRIAL_RUN_DETAILS = "select trial_id,trial_start_time,trial_end_time,is_feed_empty,feed_hold_up,feed_start_wt,temperature,q_perm_freq,device_id,valve_2_connector from trial_run_setting where trial_run_setting_id = ?";

	public static final String DELETE_USER_CREDENTIALS = "delete from user_credentials where user_id = ?";

	public static final String DELETE_USER_ROLE_MAP = "delete from user_role_map where user_id = ?";

	public static final String DELETE_USER_DETAILS = "delete from user_details where user_id = ?";

	public static final String DELETE_USER_MASTER = "delete from user_master where user_id = ?";

	public static final String ACTIVATE_SUPER_ADMIN_ACCOUNT = "update user_master set is_active = 'Active' where user_id=?";

	public static final String CHECK_IS_MASTER_PASSWORD_VALID = "select ud.* from user_master um join user_credentials ud on ud.user_id = um.user_id where um.username = ? and ud.pass_is_active = 'Y'";

	public static final String INACTIVE_SUPER_ADMIN_CREDENTIALS = "update user_credentials set pass_status = 'R' where user_id = ? and pass_is_active = 'Y'";

	public static final String CHECK_IS_SUPER_ADMIN = "select * from user_credentials where user_id = ? and pass_status = 'R' and pass_is_active = 'Y'";

	public static final String GET_TARGET_STEP_CHANGE_LOG = "select * from target_step_change_log where trial_log_id = ? and is_active = 'Y'";

	public static final String GET_END_POINTS_CHANGE_LOG = "select * from end_point_change_log where trial_log_id = ? and is_active = 1";

	// graph
	public static final String GET_FLOW_RATE_TIME_QUERY = "select feed_flow_rate, permeate_flow_rate, retentate_flow_rate, timestamp, duration from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_PRESURE_TIME_QUERY = "select feed_pressure, permeate_pressure, retentate_pressure, tmp, delta_p, timestamp, duration from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_FLUX_TIME_QUERY = "select flux, timestamp, duration from trial_hist_data where trial_run_setting_id = ? order by trial_live_data_id asc";

	public static final String GET_RPM_TIME_QUERY = "select main_pump_speed, timestamp, duration from trial_hist_data where trial_run_setting_id = ? order by trial_live_data_id asc";

	public static final String GET_PRESSURE_CF_TIME_QUERY = "select feed_pressure, permeate_pressure, retentate_pressure, tmp, delta_p, concentration_factor, timestamp, duration from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_FLUX_CF_QUERY = "select flux, concentration_factor from trial_hist_data where trial_run_setting_id = ? order by trial_live_data_id asc";

	public static final String GET_FLUX_TMP_QUERY = "select water_flux_20_degree, tmp, flow_rate_count, flux from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_PRESSURE_CF_VT_QUERY = "select feed_pressure, permeate_pressure, retentate_pressure, tmp, delta_p, concentration_factor, vt from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_DV_CONDT_TIME_QUERY = "select diafiltration_vol_1, diafiltration_vol_2, timestamp, duration, conductivity_1, conductivity_2 from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_KCH1_TEMP_TIME_QUERY = "select konduit_ch_1, konduit_ch_1_type, timestamp, duration from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_KCH2_TEMP_TIME_QUERY = "select konduit_ch_2, konduit_ch_2_type, timestamp, duration from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_FLUX_CF_TIME_QUERY = "select flux, concentration_factor, timestamp, duration from trial_hist_data where trial_run_setting_id = ? order by trial_live_data_id asc";

	public static final String GET_TEMP_CONDT_TIME_QUERY = "select timestamp, duration, conductivity_1, conductivity_2, temperature_1, temperature_2 from trial_hist_data where trial_run_setting_id = ?";

	// last 1000 historical data
	public static final String GET_LAST_1000_HISTORICAL_POINTS = "select * from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_TRIAL_END_TIME_HIST = "select timestamp from trial_hist_data where trial_run_setting_id = ? and timestamp <= ? order by trial_live_data_id desc limit 1";

	public static final String FETCH_TABLE_FOR_PDF = "select trial_live_data_id, timestamp, duration, feed_pressure, permeate_pressure, retentate_pressure, tmp, feed_flow_rate, permeate_flow_rate, feed_scale, total_perm_weight, abv_1, abv_2, aux_pump_1_flow_rate, aux_pump_2_flow_rate, conductivity_1, conductivity_2, temperature_1, temperature_2, konduit_ch_1, konduit_ch_2, konduit_ch_1_type, konduit_ch_2_type, permeate_total, permeate_total_with_holdup from trial_hist_data where trial_run_setting_id = ?";

	public static final String GET_MODE_NAME = "select mode_name from mode_master where trial_master_id = (select trial_master_id from trial_run_setting where trial_run_setting_id = ?)";

	public static final String GET_MAIN_PUMP_NAME = "select pump_name from pump_master where trial_master_id = (select trial_master_id from trial_run_setting where trial_run_setting_id = ?)";
	public static final String GET_MAIN_PUMP_NAME_MANUAL = "select pump_name from manual_pump_master where trial_run_setting_id = ?";
	public static final String GET_AUX_TYPE = "select aux_pump_type, type from aux_pump_master where (trial_run_setting_id = ? OR trial_master_id = (select trial_master_id from trial_run_setting where trial_run_setting_id = ?))";

	public static final String GET_TRIAL_DETAILS = "select thd.*,kfs.k_factor_ch_1,kfs.k_factor_ch_2,trs.trial_id from trial_hist_data thd left join kf_konduit_setting kfs on kfs.trial_run_setting_id = thd.trial_run_setting_id left join trial_run_setting trs on trs.trial_run_setting_id = thd.trial_run_setting_id where thd.trial_run_setting_id = ";

	public static final String AND_TIMESTAMP_IN = " and thd.timestamp IN (";

	public static final String ORDER_BY_TRAIL_LIVE_DATA_ID_DESC = "order by thd.trial_live_data_id desc";

	public static final String FETCH_TIMESTAMP_BEFORE_PAUSE = "select trial_live_data_id, timestamp from trial_hist_data where trial_run_setting_id = ? and is_paused = 1 order by trial_live_data_id asc";

	public static final String CLOSING_BRACKET = ") ";

	public static final String GET_TIMESTAMP_WHERE_PAUSE_EQUALS_0 = "select timestamp from trial_hist_data where trial_run_setting_id = ? and is_paused = 0 and trial_live_data_id > ? order by trial_live_data_id asc limit 1";

	public static final String DUMMY_TABLE_INSERT_QUERY = "insert into trial_hist_data (feed_pressure,permeate_pressure, tmp, delta_p, main_pump_speed,  main_pump_flow_rate, aux_pump_1_flow_rate, aux_pump_2_flow_rate,conductivity_1, conductivity_2, temperature_1, temperature_2, konduit_ch_1, konduit_ch_2, konduit_ch_1_type, konduit_ch_2_type, feed_scale, m_permeate, total_perm_weight, diafiltration_vol_1, diafiltration_vol_2,  concentration_factor, timestamp, feed_flow_rate, permeate_flow_rate, retentate_flow_rate, vt, shear, abv_1, abv_2, flux, main_pump_speed, notes ,trial_master_id, tcf, water_flux_20_degree, water_perm, nwp, trial_run_setting_id, retentate_pressure, is_paused, flow_rate_count, permeate_total, permeate_total_with_holdup) values ";

	public static final String OPENING_BRACKET = "(";

	public static final String CLOSING_BRACKET_WITH_COMMA = "),";

	public static final String FETCH_LAST_TRIAL_DETAILS = "select * from trial_run_setting where trial_master_id = -1 order by trial_run_setting_id desc limit 1";

	public static final String DELETE_FAILED_TRIAL_RUN_SETTINGS = "delete from trial_run_setting where trial_run_setting_id = ?";

	public static final String DELETE_FAILED_ALARMS = "delete from alarm_setting where trial_run_setting_id = ?";

	public static final String DELETE_FAILED_AUX = "delete from aux_pump_master where trial_run_setting_id = ?";

	public static final String DELETE_FAILED_KONDUIT = "delete from kf_konduit_setting where trial_run_setting_id = ?";

	public static final String DELETE_FAILED_ABV = "delete from manual_abv_master where trial_run_setting_id = ?";

	public static final String DELETE_FAILED_MAIN_PUMP = "delete from manual_pump_master where trial_run_setting_id = ?";

	public static final String FETCH_DUPLICATE_EMAIL_USER_DETAILS = "select um.username, um.user_id, ud.full_name from user_master um left join user_details ud on ud.user_id = um.user_id where ud.email_id = ?";

	public static final String CHANGE_USER_EMAIL = "update user_details set email_id = ? where user_id = ?";

	public static final String INACTIVE_PREVIOUS_SUPER_ADMINS = "update user_role_map set is_active = 'N' where role_id = 5 and user_id != ?";

	public static final String INACTIVE_PREVIOUS_SUPER_ADMINS_USER_MASTER = "update user_master set is_active = 'Inactive' where username='SuperAdmin'";

	public static final String INSERT_MANUAL_NOTE = "insert into notes_desc_manual (mn_device, mn_type, mn_old_value, mn_new_value, trial_log_id) values(?,?,?,?,?)";

	public static final String FETCH_TUBING_LOOKUP_ID = "select ptm.tubing_lookup_id from pump_tube_map ptm join pump_lookup pl on ptm.pump_lookup_id = pl.pump_lookup_id where pl.pump_name = ?";

	public static final String UPDATE_MANUAL_TRIAL_DETAILS = "update trial_run_setting set valve_2_connector = ? where trial_run_setting_id = ?";

	public static final String INSERT_MANUAL_FILTER = "insert into manual_filter_details (filter_type,part_no,no_of_cassette,is_non_repligen,surf_area,trial_run_setting_id) values(?,?,?,?,?,?)";

	public static final String FETCH_MANUAL_FILTER = "select * from manual_filter_details where trial_run_setting_id =?";

	public static final String UPDATE_MANUAL_FILTER = "update manual_filter_details set filter_type = ?, part_no = ?, no_of_cassette = ?, is_non_repligen = ?, surf_area = ? where trial_run_setting_id = ?";

	public static final String FETCH_MANUAL_HF_DETAILS = "select fiber_count, fiber_id, surface_area, ecs from hollow_fiber_lookup where part_no = ?";
	
	public static final String FETCH_MANUAL_CASSETTE_DETAILS = "select width, height, no_of_channel, surface_area, ecs from cassette_lookup where part_no = ?";

	public static final String CHECK_MANAUL_FILTER_EXISTS = "select count(*) from manual_filter_details where trial_run_setting_id = ?";

	public static final String FETCH_TUBING_DETAILS_LIST = "select GROUP_CONCAT(list.main_pump) as main_pump,GROUP_CONCAT(list.aux_pump) as aux_pump, GROUP_CONCAT(list.abv) as abv, "
			+ "list.internal_diameter_in,list.internal_diameter_mm, list.tubing_size,list.min_max_rpm,list.flow_rate from (SELECT CASE WHEN pump_type = 'Main pump' THEN pl.pump_name END AS "
			+ "main_pump,CASE WHEN pump_type = 'Aux pump' THEN pl.pump_name END AS aux_pump,CASE WHEN pump_type = 'Valve' THEN pl.pump_name END AS abv,tl.tubing_lookup_id,tubing_size, internal_diameter_in, "
			+ "internal_diameter_mm, min_rpm || '-' || max_rpm as min_max_rpm, min_flow_rate || '-' || max_flow_rate as flow_rate from tubing_lookup tl left join pump_tube_map ptm on tl.tubing_lookup_id = ptm.tubing_lookup_id "
			+ "left join pump_lookup pl on ptm.pump_lookup_id = pl.pump_lookup_id) as list GROUP BY list.tubing_lookup_id";

}