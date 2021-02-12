package org.lattice.spectrum_backend_final.dao.constants;

/**
 * @author RAHUL KUMAR MAURYA
 */

import org.lattice.spectrum_backend_final.dao.util.BasicUtility;

/**
 * This class holds all the sql queries.
 */
public class DbConstant {

	/************************************************************** Queries ***************************************************************/

	public static final String CREATE_NEW_USER_DETAILS = "insert into user_details (user_id,prefix,first_name,middle_name,last_name,email_id,contact_number,full_name)" +
			" values(?,?,?,?,?,?,?,?);";

	public static final String CREATE_NEW_USER_MASTER = "insert into user_master (username,is_active,created_by,created_on,org_id) " +
			"values(?,?,?,?,?);";

	public static final String ADD_USER_ROLE_MAP = "insert into user_role_map (user_id , role_id , is_active)" +
			" values(?,?,'Y');";

	public static final String ADD_USER_CREDENTIALS = "insert into user_credentials (user_id  , password , pass_status , pass_is_active) " +
			"values(?,?,'D','Y');";

	public static final String ADD_NEW_USER_CREDENTIALS = "insert into user_credentials (user_id  , password , master_password, pass_status , pass_is_active) " +
			"values(?,?,?,'U','Y');";

	public static final String UPDATE_USER_MASTER = "update user_master set modified_on = ? , is_active = ? , modified_by = ? where user_id = ? ";

	public static final String UPDATE_USER_DETAILS = "update user_details set prefix = ? , first_name = ?, middle_name = ?, last_name = ? ," +
			" contact_number = ? , email_id = ? " +
			" where user_id = ?";

	public static final String UPDATE_USER_ROLE_MAP = "update user_role_map set role_id = ? where user_id = ? and role_id = ?";

	public static final String DISABLE_USER_ROLE_MAP = "update user_role_map set is_active = 'N' " +
			"where user_id = ? and role_id = ? ";


	//	public static final String GET_SINGLE_USER_DETAIL = "select um.user_id , um.username  , um.is_active , ud.prefix , ud.first_name , ud.middle_name , ud.last_name , ud.contact_number ,uc.pass_status , uc.pass_is_active ,ud.email_id ,rm.role_description from user_master as um join user_details as ud ON um.user_id = ud.user_id join user_credentials as uc on uc.user_id = um.user_id join user_role_map as ur ON um.user_id = ur.user_id join role_master as rm ON rm.role_id = ur.role_id where um.user_id = ? and ur.is_active = 'Y'";


	public static final String GET_SINGLE_USER_DETAIL = "select um.user_id , um.username  , um.is_active , ud.prefix ," +
			"ud.first_name , ud.middle_name , ud.last_name , ud.contact_number ," +
			"uc.pass_status , uc.pass_is_active ,ud.email_id ,rm.role_description," +
			"CASE " +
			"WHEN uc.pass_status = 'D' THEN uc.password " +
			"END AS default_password " +
			"from user_master as um " +
			"join user_details as ud " +
			"ON um.user_id = ud.user_id " +
			"join user_credentials as uc " +
			"ON uc.user_id = um.user_id " +
			"join user_role_map as ur " +
			"ON um.user_id = ur.user_id " +
			"join role_master as rm " +
			"ON rm.role_id = ur.role_id " +
			"where um.user_id = ? and ur.is_active = 'Y' " +
			"and uc.pass_is_active = 'Y' ";

	public static final String GET_ALL_USERS_DETAIL = "select um.user_id,um.username , um.is_active , ud.prefix , ud.first_name , ud.middle_name , ud.last_name ," +
			"ud.email_id , ud.contact_number , ud.last_login_date , rm.role_description , " +
			"uc.pass_status , uc.pass_is_active " +
			"from user_master as um " +
			"join user_details as ud " +
			"ON um.user_id = ud.user_id " +
			"join user_credentials as uc " +
			"on um.user_id = uc.user_id " +
			"join user_role_map as ur " +
			"ON um.user_id = ur.user_id " +
			"join role_master as rm " +
			"ON rm.role_id = ur.role_id " +
			"where ur.is_active = 'Y' and uc.pass_is_active = 'Y' " +
			"and rm.role_description <> 'SuperAdmin' " +
			"ORDER BY " +
			"case " +
			"when uc.pass_status = 'R' and um.is_active = 'Active' then 0 " +
			"when uc.pass_status = 'D' and um.is_active = 'Active' then 1 " +
			"when uc.pass_status = 'U' and um.is_active = 'Active' then 2 " +
			"else 3 " +
			"end";


	public static final String DELETE_ALL_USER_ROLE_MAP = " update user_role_map set is_active = 'N' where user_id = ? ";

	public static final String USER_LOGIN = "select * from user_details as ud join user_master as um on um.user_id = ud.user_id join user_credentials as uc on um.user_id = uc.user_id join user_role_map as ur on ud.user_id = ur.user_id join role_master as rm on ur.role_id = rm.role_id where um.username = ? and uc.password = ? and uc.pass_is_active = 'Y' and rm.is_active = 'Y' and ur.is_active = 'Y' and um.is_active = 'Active' ORDER BY rm.role_description";
	public static final String GET_PASSWORD_FROM_USERNAME = "select * from user_details as ud " +
			"join user_master as um " +
			"on um.user_id = ud.user_id " +
			"join user_credentials as uc " +
			"on um.user_id = uc.user_id " +
			"join user_role_map as ur " +
			"on ud.user_id = ur.user_id " +
			"join role_master as rm " +
			"on ur.role_id = rm.role_id " +
			"where um.username = ? and uc.pass_is_active = 'Y' " +
			"and rm.is_active = 'Y' and ur.is_active = 'Y' " +
			"and um.is_active = 'Active' " +
			"ORDER BY rm.role_description";
	//	public static final String GET_PASSWORD_FROM_USERNAME = "select password from user_credential where user_id = (select user_id from user_master where username = ? and is_active = 'Y') and pass_is_active = 'Y'";

	public static final String FORGOT_PASSWORD_QUERY = "update user_credentials set pass_status = 'R' " +
			"where pass_is_active = 'Y' " +
			"and user_id = ( " +
			"select ud.user_id from user_details as ud " +
			"join user_master as um " +
			"on ud.user_id = um.user_id " +
			"join user_role_map as ur " +
			"on um.user_id = ur.user_id " +
			"join role_master as rm " +
			"on ur.role_id = rm.role_id " +
			"where um.username = ? and um.is_active = 'Active'" +
			" and rm.role_description <> 'SuperAdmin') ";

	//    public static final String RESET_PASSWORD_VERIFICATION_FROM_LAST_THREE_PASSWORD_QUERY = "select * from (select password from user_credentials where pass_is_active = 'N' and user_id = (select user_id from user_master where username = ? ) order by credential_id desc limit 3) where password = ? ";

	public static final String CHANGE_PASSWORD_VERIFICATION_FROM_LAST_THREE_PASSWORD_QUERY = "select * from (select password from user_credentials where user_id = (select user_id from user_master where username = ? ) order by credential_id desc limit 3) where password = ? ";

	public static final String ADD_NEW_PASSWORD_QUERY = "update user_credentials set password = ? , pass_status = 'U' where pass_is_active = 'Y' and password = ? and user_id = (select user_id from user_master where username = ? )";

	public static final String UPDATE_LAST_LOGIN = "update user_details set last_login_date = ? where email_id = ? ";

	//	public static final String SET_DEFAULT_PASSWORD_QUERY = "update user_credentials set password = ? , ";

	public static final String DISABLE_USER_CREDENTIALS = "update user_credentials set pass_is_active = 'N' where user_id = ? ";

	public static final String CHECK_USER_CREDENTIALS_EXISTS = "select * from user_master as um join user_credentials as uc on um.user_id = uc.user_id where um.username = ? and uc.password = ? and uc.pass_is_active = 'Y'";

	public static final String GET_SINGLE_USER_PROFILE = "select um.user_id , um.username  , um.is_active , ud.prefix , ud.first_name , " +
			"ud.middle_name , ud.last_name , ud.contact_number ,uc.pass_status , " +
			"uc.pass_is_active ,ud.email_id ,rm.role_description from user_master as um " +
			"join user_details as ud " +
			"ON um.user_id = ud.user_id " +
			"join user_credentials as uc " +
			"on uc.user_id = um.user_id " +
			"join user_role_map as ur " +
			"ON um.user_id = ur.user_id " +
			"join role_master as rm " +
			"ON rm.role_id = ur.role_id " +
			"where um.username = ? and ur.is_active = 'Y' and uc.pass_is_active = 'Y'";

	public static final String CHECK_PHONE_EXIST = "select * from user_details where contact_number = ?";

	public static final String CHECK_PHONE_EXIST_FROM_USER_ID = "select * from user_details where contact_number = ? and user_id <> ?";

	public static final String CHECK_EMAIL_EXIST = "select * from user_details where email_id = ?";

	public static final String CHECK_EMAIL_EXIST_FROM_USER_ID = "select * from user_details where email_id = ? and user_id <> ? ";

	public static final String CHECK_USERNAME_EXIST = "select * from user_master where username = ?";

	public static final String ADD_USER_ACCESS_LOG_QUERY = "insert into user_access_log (performed_by , full_name , role , created_on , action , performed_on, digital_notes) " +
			"values (?,?,?,?,?,?,?)";


	public static final String GET_ALL_USER_ACCESS_LOG = "select * from user_access_log";

	public static final String UPDATE_USER_MASTER_PROFILE = "update user_master set modified_on = ? , modified_by = ? where user_id = ? ";

	public static final String UPDATE_USER_DETAILS_PROFILE = "update user_details set prefix = ? , first_name = ?, middle_name = ?, last_name = ? ," +
			" contact_number = ? " +
			" where user_id = ?";

	public static final String GET_USER_ROLE_FROM_ID = "select rm.role_description from user_master as um " +
			"join user_role_map as ur " +
			"on um.user_id = ur.user_id " +
			"join role_master as rm " +
			"on ur.role_id = rm.role_id " +
			"where um.user_id = ? and ur.is_active = 'Y'" +
			"order by rm.role_description";


	public static final String VALIDATE_A_USER = "select * from user_credentials where user_id = ? and password = ?";


	/*********************************************  LOGIN MANAGEMENT *************************************************************/

	public static final String IS_USER_EXIST = "select * from user_master where username = ? ";
	public static final String IS_FIRST_LOGIN_ATTEMPT = "select * from user_login_limit where username = ? and is_active = 'Y'";

	public static final String SET_USER_INACTIVE = "update user_master set is_active = 'Inactive' " +
			"where username = ?";

	public static final String DISABLE_USER_LOGIN_LIMIT = "update user_login_limit set is_active = 'N' " +
			"where username = ?";

	public static final String UPDATE_USER_LOGIN_LIMIT_COUNT = "update user_login_limit set count = ? where username = ?";


	public static final String ADD_USER_LOGIN_LIMIT = "insert into user_login_limit (username, count, timestamp, is_active) values (?,1,?,'Y') ";

	public static final String IS_USER_ACTIVE = "select * from user_master as um " +
			"join user_role_map as ur " +
			"on um.user_id = ur.user_id " +
			"join role_master as rm " +
			"on ur.role_id = rm.role_id " +
			"where um.username = ? and um.is_active = 'Active' and rm.role_description <> 'SuperAdmin' ";

	public static final String RESET_USER_LOGIN_LIMIT = "update user_login_limit set is_active = 'N' where username = ?";
	public static final String IS_USER_INACTIVE = "select * from user_master where username = ? and is_active = 'Inactive'";
	public static final String IS_SUPER_USER_ACTIVE = "select um.is_active, um.user_id, rm.role_id from user_master um join user_role_map as ur on um.user_id = ur.user_id join role_master as rm on ur.role_id = rm.role_id where um.username = ? and um.is_active = 'Active'";
	public static final String DISABLE_USER_CREDENTIAL = "update user_credentials set pass_is_active = 'N' where user_id = ( select user_id from user_master where username = ? ) ";

	public static final String ADD_USER_CREDENTIAL_WITH_DEFAULT_PASSWORD = "insert into user_credentials (user_id  , password , pass_status , pass_is_active) " +
			"values(( select user_id from user_master where username = ? ),?,'D','Y');";


	/********************************************************** Manager/Technician ********************************************************/

	public static final String GET_ALL_TRIAL_MODES = "select * from mode_lookup where is_active = 'Y'";
	public static final String GET_ALL_HOLLOW_FIBER = "select part_no, fiber_count, fiber_id, surface_area, ECS  from hollow_fiber_lookup";
	public static final String GET_C_END_POINT = "select * from end_point_lookup where end_point_group = 'C' or end_point_group = 'Both' and is_active = 'Y'";
	public static final String GET_D_END_POINT = "select * from end_point_lookup where end_point_group = 'D' or end_point_group = 'Both' and is_active = 'Y'";


	// todo 1--pump_lookup - implemented - tested
	public static final String GET_ALL_PUMP_COMPATIBLE_WITH_MODE_QUERY ="select pl.pump_name ,pl.pump_lookup_id, pl.head_count , " +
			"pl.min_speed ,pl.max_speed from pump_lookup as pl " +
			"JOIN pump_mode_map as pmm " +
			"on pl.pump_lookup_id = pmm.pump_lookup_id " +
			"join mode_lookup as ml " +
			"on ml.mode_lookup_id = pmm.mode_lookup_id " +
			"where pl.pump_type = 'Main pump' and ml.mode_lookup_id = ? and pl.is_active = 1";

	public static final String GET_MODE_FROM_MODE_ID = "select mode_name  from mode_lookup where mode_lookup_id = ?";

	//todo pump_lookup - not needed
	public static final String GET_PUMP_FROM_PUMP_ID = "select pump_name from pump_lookup where pump_lookup_id = ?";


	//todo 2 & 3--tubing-lookup - completed - tested
	public static final String GET_TUBING_SPEC_COMPATIBLE_WITH_PUMP_QUERY = "SELECT * from tubing_lookup as tl " +
			"join pump_tube_map as ptm " +
			"on tl.tubing_lookup_id = ptm.tubing_lookup_id " +
			"join pump_lookup as pl " +
			"on pl.pump_lookup_id = ptm.pump_lookup_id " +
			"where pl.pump_lookup_id = ? " +
			"group by tl.tubing_lookup_id ";

	public static final String GET_ALL_RAMP_UP_TIME = "select ramp_up_time_value from ramp_up_time";
	public static final String GET_RECIRC_FLOW_UNIT = "select * from recirc_flow_unit ";
	//todo shift trial id - done
	public static final String CREATE_NEW_TRIAL_MASTER = "insert into trial_master( recipe_name, trial_type, created_on, modified_on, created_by, is_active) values(?,?,?,?,?,'Y')";


	public static final String CREATE_NEW_MODE_MASTER = "insert into mode_master (mode_name, trial_master_id, is_active) values(?,?,'Y')";
	public static final String ADD_NEW_FILTER_PERM_MASTER_FOR_HOLLOW_FIBER = "insert into filter_perm_master (filter_type, part_no, fiber_count, fiber_id, surf_area, ecs, permeate_tube_size, tube_length,perm_tube_holdup, total_perm_tube_holdup, perm_hold_up_cal, no_of_cassette, is_generic, trial_master_id, is_active, model, manufacturer) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?)";
	public static final String ADD_NEW_PUMP_MASTER = "insert into pump_master (head_count, pump_name, pump_type, direction, flow_control, pump_tubing_size, pump_tubing_lookup_id, flow_rate, ramp_up_time, trial_master_id, delta_p, is_active, delta_p_rate, delta_p_duration, pump_rpm, is_speed) values(?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?,?)";
	public static final String ADD_NEW_END_POINT = "insert into end_point (end_point_type, end_point_value, step_no, trial_master_id, is_active) values(?,?,?,?, 'Y')";
	public static final String ADD_NEW_AUX_PUMP_MASTER = "insert into aux_pump_master ( aux_pump_type, pump_function, aux_tubing_size, flow_rate, type, trial_master_id, is_active) values(?,?,?,?,?,?,'Y')";
	public static final String ADD_NEW_ABV_MASTER = "insert into abv_master (abv_type, cont_based_on, abv_mode, start_pos, percent_closed, op_pressure, trial_master_id, is_active) values(?,?,?,?,?,?,?,'Y')";

	public static final String ADD_NEW_KF_KONDUIT_SETTING = "insert into kf_konduit_setting (uv_ch_1, uv_ch_2, k_factor_ch_1, k_factor_ch_2, trial_master_id, is_active) values (?,?,?,?,?,'Y')";
	public static final String ADD_NEW_TRIAL_DETAIL = "insert into trial_detail (trial_master_id, notes, run_as_safe, feed_to_empty, no_of_aux_pump, perm_stop_first, recirc_pressure_cont,recirc_rate_per_unit, recirc_flow_unit_value, retentate_tube_size, endpoint_cal, is_active, digital_notes) values(?,?,?,?,?,?,?,?,?,?,?,'Y',?)";
	public static final String ADD_NEW_FILTER_PERM_MASTER_FOR_CASSETTE = "insert into filter_perm_master (filter_type, part_no, surf_area, ecs, permeate_tube_size, tube_length, perm_tube_holdup, total_perm_tube_holdup, no_of_cassette, width, height, perm_hold_up_cal, no_of_channel, fpi_part_no, is_generic, trial_master_id, is_active, model, manufacturer) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?)";
	public static final String CHECK_RECIPE_EXIST = "select trial_master_id from trial_master where recipe_name = ?";

	//    public static final String GET_ALL_TUBING_DETAILS = "select tubing_size , revolutions , expected_volume , flow_conversion ,  min_flow_rate, max_flow_rate from tubing_lookup ";
	//todo tubing-lookup - not needed
	public static final String GET_ALL_TUBING_DETAILS = "select * from (select * from tubing_lookup where tubing_size <> 'NA' order by tubing_lookup_id) where tubing_size <> 'NA' group by tubing_size";

	//todo tubing-lookup - not needed
	public static final String GET_AUX_AND_MAIN_PUMP_COMPATIBLE_TUBING_SIZE_FIRST_HALF = "select tubing_size from tubing_lookup where \"";

	public static final String GET_AUX_AND_MAIN_PUMP_COMPATIBLE_TUBING_SIZE_SECOND_HALF = "\" = 'TRUE'";

	//todo shift trial id - done
	public static final String DISABLE_TRIAL_MASTER = "update trial_master set is_active = 'N' , recipe_name = '" + BasicUtility.generateRandomName(20) + "' where recipe_name = ? ";
	public static final String GET_SINGLE_TUBING_SPEC = "select min_flow_rate , max_flow_rate , valve_pos_full_open, valve_pos_full_pinched, start_step_size from tubing_lookup where tubing_size = ? limit 1";

	//todo shift trial id - done
	public static final String CHECK_TRIAL_EXIST = "select trial_master_id from trial_master where recipe_name = ? ";

	public static final String GET_AUX_PUMP_COMPATIBLE_WITH_MAIN_PUMP = "select aux_pump from main_aux_pump_map where main_pump = ?";


	//-----------------get recipe
	public static final String GET_TRIAL_MASTER_FROM_RECIPE = "select * from trial_master where trial_master_id = ?";
	public static final String GET_TRIAL_DETAIL = "select * from trial_detail where trial_master_id = ?";
	public static final String GET_MODE_MASTER = "select * from mode_master where trial_master_id = ?";
	public static final String GET_PUMP_MASTER_DETAILS = "select * from pump_master where trial_master_id = ?";
	public static final String GET_FILTER_PERM_MASTER_DETAILS = "select * from filter_perm_master where trial_master_id = ?";
	public static final String GET_ALL_ENDPOINTS_RELATED_TO_TRIAL = "select * from end_point where trial_master_id = ?";
	public static final String GET_AUX_PUMP_DETAILS = "select * from aux_pump_master where trial_master_id = ?";
	public static final String GET_ABV_MASTER_DETAILS = "select * from abv_master where trial_master_id = ?";
	//todo konduit
	public static final String GET_KF_KONDUIT_SETTING_DETAIL = "select * from kf_konduit_setting where trial_master_id = ?";
	//todo shift trial id - done
	//    public static final String GET_ALL_RECIPE = "select tm.recipe_name, tm.trial_id, tm.trial_master_id, tm.trial_type, tm.created_on, " +
	//            "tm.created_by, td.notes from trial_master as tm " +
	//            "join trial_detail as td " +
	//            "where tm.trial_master_id = td.trial_master_id and tm.is_active = 'Y'";
	public static final String GET_ALL_RECIPE = "select tm.recipe_name, tm.trial_master_id, tm.trial_type, " +
			"tm.created_on, tm.created_by, td.notes, mm.mode_name, c.no_of_trial, tm.is_active " +
			"from trial_master as tm " +
			"join trial_detail as td " +
			"on tm.trial_master_id = td.trial_master_id " +
			"join mode_master as mm " +
			"on tm.trial_master_id = mm.trial_master_id " +
			"left join (select trial_master_id, count(trial_master_id) as no_of_trial " +
			"from trial_run_setting group by trial_master_id) as c " +
			"on c.trial_master_id = tm.trial_master_id ";

	//todo tubing-lookup - not needed
	public static final String GET_FLOW_CONVERSION_NUMBER_FROM_TUBE_SIZE = "select flow_conversion from tubing_lookup where tubing_size = ? limit 1";
	//todo tubing-lookup - not needed
	public static final String GET_KRJR_FLOW_CONVERSION_NUMBER_FROM_TUBE_SIZE = "select flow_conversion_KRJr from tubing_lookup where tubing_size = ? limit 1";
	public static final String GET_CASSETTE_DETAILS = "select * from cassette_lookup where part_no = ?";
	public static final String GET_HOLLOW_FIBER_DETAILS = "select * from hollow_fiber_lookup where part_no = ?";


	//todo pump_lookup - not needed
	public static final String GET_PUMP_MIN_MAX_RPM = "select min_speed, max_speed from pump_lookup where pump_name = ?";
	public static final String SAVE_LIVE_DATA_TO_DB = "insert into trial_hist_data (feed_pressure,permeate_pressure," +
			" tmp, delta_p, main_pump_speed,  main_pump_flow_rate," +
			" aux_pump_1_flow_rate, aux_pump_2_flow_rate,conductivity_1," +
			" conductivity_2, temperature_1, temperature_2, konduit_ch_1, konduit_ch_2, konduit_ch_1_type, konduit_ch_2_type," +
			" feed_scale, m_permeate, total_perm_weight, diafiltration_vol_1, diafiltration_vol_2," +
			"  concentration_factor," +
			" timestamp, feed_flow_rate, permeate_flow_rate, retentate_flow_rate," +
			" vt, shear, abv_1, abv_2, flux, main_pump_speed, notes ,trial_master_id, tcf, water_flux_20_degree, water_perm, nwp, trial_run_setting_id, retentate_pressure, is_paused, flow_rate_count, permeate_total, permeate_total_with_holdup, duration) " +
			"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;

	public static final String GET_ALL_CASSETTE_DETAILS = "select part_no, width, height, no_of_channel, surface_area, ecs from cassette_lookup";
	public static final String GET_ALL_FILTER_PLATE_INSERT = "select fpi_part_no, fpi_hold_up from filter_plate_insert_lookup";
	public static final String SAVE_ALARM_SETTING_QUERY = "insert into alarm_setting (trial_run_setting_id, feed_pressure_low_alarm," +
			" feed_pressure_low_stop, feed_pressure_high_alarm, feed_pressure_high_stop, " +
			"feed_wt_low_alarm, feed_wt_low_stop, feed_wt_high_alarm, feed_wt_high_stop, " +
			"perm_wt_low_alarm, perm_wt_low_stop, perm_wt_high_alarm, perm_wt_high_stop, " +
			"perm_pressure_low_alarm, perm_pressure_low_stop, perm_pressure_high_alarm, " +
			"perm_pressure_high_stop, konduit_ch_1_high_alarm, " +
			"konduit_ch_1_high_stop,  konduit_ch_2_high_alarm, konduit_ch_2_high_stop, " +
			"created_on, updated_on, konduit_ch_1_type, konduit_ch_2_type, permeate_total_high_alarm, permeate_total_high_stop) " +
			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String GET_ALARM_SETTING_QUERY = "select * from alarm_setting where trial_run_setting_id = ?";

	public static final String GET_ALARM_SETTING_FROM_TRIAL_ID_QUERY = "select * from alarm_setting " +
			"where trial_run_setting_id = (select trial_run_setting_id from trial_run_setting where trial_id = ?)";

	public static final String UPDATE_ALARM_SETTING_QUERY = "update alarm_setting set feed_pressure_low_alarm = ?," +
			"feed_pressure_low_stop = ?, feed_pressure_high_alarm = ?, feed_pressure_high_stop = ?, " +
			"feed_wt_low_alarm = ?, feed_wt_low_stop = ?, feed_wt_high_alarm = ?, feed_wt_high_stop = ?, " +
			"perm_wt_low_alarm = ?, perm_wt_low_stop = ?, perm_wt_high_alarm = ?, perm_wt_high_stop = ?, " +
			"perm_pressure_low_alarm = ?, perm_pressure_low_stop = ?, perm_pressure_high_alarm = ?, " +
			"perm_pressure_high_stop = ?,  konduit_ch_1_high_alarm = ?, " +
			"konduit_ch_1_high_stop = ?, konduit_ch_2_high_alarm = ?, konduit_ch_2_high_stop = ?, " +
			"updated_on = ?, konduit_ch_1_type = ?, konduit_ch_2_type = ?, permeate_total_high_alarm = ?, permeate_total_high_stop = ? " +
			"where trial_run_setting_id = (select trial_run_setting_id from trial_run_setting where trial_id = ?)";

	public static final String SAVE_TRIAL_RUN_SETTING = "insert into trial_run_setting (trial_id, is_feed_empty, feed_hold_up, feed_start_wt, trial_master_id, trial_type, temperature, created_on, updated_on, trial_start_time, user_id, totalizer_unit, totalizer_unit_2, q_perm_freq, feed_scale_override, device_id, delta_p, delta_p_rate, delta_p_duration, valve_2_connector) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String GET_TRIAL_RUN_SETTING_QUERY = "select * from trial_run_setting where trial_id = ?";
	public static final String SAVE_ALARM_HISTORY_SETTING_QUERY = "insert into alarm_history (trial_run_setting_id, mode_name, alarm_stop_desc, alarm_stop_value, nature, created_on, updated_on) values(?,?,?,?,?,?,?)";

	public static final String SAVE_NOTES_QUERY = "insert into trial_log (notes, trial_run_setting_id, user_id, created_on, updated_on, log_type) values(?,?,?,?,?,0)";
	public static final String GET_ALL_NOTES_QUERY = "select n.notes_id, n.trial_run_setting_id, n.notes, n.posted_by, n.created_on from notes as n join trial_run_setting as trs on n.trial_run_setting_id = trs.trial_run_setting_id where trs.trial_id = ?";

	public static final String UPDATE_TRIAL_RUN_SETTING = "update trial_run_setting set is_feed_empty = ?, feed_hold_up = ?, " +
			"feed_start_wt = ?, trial_master_id = ?, trial_type = ?, updated_on = ? where trial_id = ?";

	public static final String ADD_TRIAL_LOG_QUERY = "insert into trial_log (user_id, trial_run_setting_id, action, log_type, created_on, updated_on) values(?,?,?,?,?,?)";
	public static final String ADD_SYSTEM_LOG_QUERY = "insert into trial_log (user_id, trial_run_setting_id, action, log_type, created_on, updated_on, digital_notes) values(?,?,?,?,?,?,?)";

	public static final String UPDATE_TRIAL_RUN_STATUS_QUERY = "update trial_run_setting set trial_status = ? , trial_end_time = ? where trial_run_setting_id = ?";

	public static final String GET_USER_ID_QUERY = "select user_id from user_master where username = ? ";

	public static final String GET_USER_ROLE_FROM_USERNAME_QUERY = "select rm.role_description from user_master as um " +
			"JOIN user_role_map as urm " +
			"on um.user_id = urm.user_id " +
			"join role_master as rm " +
			"on urm.role_id = rm.role_id " +
			"where um.username = ? and urm.is_active = 'Y'";

	public static final String GET_PUMP_ID_QUERY = "select pump_lookup_id from pump_lookup where pump_name = ?";

	public static final String GET_TUBE_COMPATIBLE_AUX_PUMP_QUERY = "select pl.pump_name from tubing_lookup as tl join pump_tube_map as ptm on tl.tubing_lookup_id = ptm.tubing_lookup_id join pump_lookup as pl on pl.pump_lookup_id = ptm.pump_lookup_id where tl.tubing_lookup_id = ? and pl.pump_type = 'Aux pump'";
	public static final String GET_ALL_SYSTEM_LOG = "select um.username, ud.full_name, rm.role_description, tl.created_on, tl.action, tl.digital_notes from trial_log as tl " +
			"left join user_master as um " +
			"on tl.user_id = um.user_id " +
			"left join user_details as ud " +
			"on tl.user_id = ud.user_id " +
			"left join user_role_map as urm " +
			"on tl.user_id = urm.user_id " +
			"left join role_master as rm " +
			"on rm.role_id = urm.role_id " +
			"where tl.is_active = 'Y' " +
			"and (rm.role_id = 3 or rm.role_id = 4 or urm.role_id IS NULL) " +
			"and tl.log_type = ? ";

	public static final String GET_TUBING_CALIBRATION_FACTOR = "select calibration_factor from tubing_lookup where tubing_size = ? limit 1";

	public static final String ADD_TARGET_STEP_QUERY = "insert into target_step (trial_master_id, tmp, duration, flow_rate, permeate_weight, permeate_total) values(?,?,?,?,?,?)";
	public static final String GET_TARGET_STEP = "select * from target_step where trial_master_id = ?";
	public static final String GET_SINGLE_TUBING_SPEC_FOR_KMPI_VALVE = "select min_flow_rate , max_flow_rate , valve_pos_full_open, valve_pos_full_pinched, start_step_size from (select * from tubing_lookup where tubing_size <> 'NA' order by tubing_lookup_id DESC) where tubing_size = ? limit 1 ";
	public static final String GET_TEMPERATURE_FOR_FLUX_TRIAL = "select temperature from trial_run_setting where trial_run_setting_id = ?";
	public static final String GET_MIN_MAX_FLOW_RATE_QUERY = "select min_flow_rate, max_flow_rate from tubing_lookup where tubing_size = ? limit 1";
	public static final String UPDATE_USER_ROLE_IN_ACCESS_LOG_QUERY = "update user_access_log set role = ? WHERE access_log_id = (SELECT access_log_id FROM user_access_log ORDER by access_log_id Desc limit 1)";

	public static final String GET_21_CFR_STATUS = "select * from cfr_status";
	public static final String UPDATE_TRIAL_START_TIME_QUERY = "update trial_run_setting set trial_start_time = ? where trial_run_setting_id = ?";

	public static final String CHECK_IS_TRIAL_RUNNING_QUERY = "select * from trial_run_setting where trial_status = 0";

	// todo notes change
	public static final String CHECK_IS_TRIAL_PAUSED_QUERY = "select action from trial_log where trial_run_setting_id = ? and log_type = 0 and (action like '%Trial%' or action like '%Abrupt%' or action like '%Auto%') order by trial_log_id desc limit 1";
	// todo notes change
	public static final String CHECK_IS_EMERGENCY_VALVE_OPEN = "select action from trial_log where trial_run_setting_id = ? and log_type = 0 and (action like '%Control%' or action like 'Emergency%') order by trial_log_id desc limit 1";
	public static final String GET_FIRMWARE_VERSION_QUERY = "select * from firmware_version";
	public static final String UPDATE_FIRMWARE_VERSION_QUERY = "update firmware_version set software_version = ?, interface_version = ?";
	public static final String UPDATE_TRIAL_STATUS_ON_UNEXPECTED_CLOSE_QUERY = "update trial_run_setting set trial_status = 2 where trial_status = 0";
	public static final String SAVE_END_POINT_SETTING_DETAILS = "insert into end_point_setting (trial_run_setting_id, end_point_type, end_point_value, step_no, created_on, updated_on) values(?,?,?,?,?,?)";
	public static final String SAVE_END_POINT_CHANGE_LOG_QUERY = "insert into end_point_change_log (trial_log_id, end_point_type, old_end_point_value, new_end_point_value, step_no, trial_run_setting_id) values(?,?,?,?,?,?)";
	public static final String UPDATE_END_POINT_SETTING_DETAILS = "update end_point_setting set end_point_value = ? , updated_on = ? where trial_run_setting_id = ? and step_no = ?" ;
	public static final String GET_END_POINT_SETTING_QUERY = "select * from end_point_setting where trial_run_setting_id = ?";
	public static final String SAVE_TARGET_STEP_SETTING_DETAILS = "insert into target_step_setting (trial_run_setting_id, tmp, duration, permeate_weight, permeate_total, step_no, created_on, updated_on) values(?,?,?,?,?,?,?,?)";
	public static final String SAVE_TARGET_STEP_CHANGE_LOG_QUERY = "insert into target_step_change_log (trial_log_id, old_tmp, new_tmp, old_duration, new_duration, old_permeate_weight, new_permeate_weight, old_permeate_total, new_permeate_total, step_no, trial_run_setting_id) values(?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_TARGET_STEP_SETTING_DETAILS = "update target_step_setting set tmp = ?, duration = ?, permeate_weight = ?, permeate_total = ?, updated_on = ? where trial_run_setting_id = ? and step_no = ?";
	public static final String GET_TARGET_STEP_SETTING_QUERY = "select * from target_step_setting where trial_run_setting_id = ?";
	public static final String UPDATE_RECIPE_IS_ACTIVE_STATUS = "update trial_master set is_active = ? where trial_master_id = ?";

	/**
	 * Registry manager
	 */
	public static final String GET_REGISTRY_HASH_QUERY = "select reg_hash from registry_hash where registry_hash_id = 1";
	public static final String UPDATE_REGISTRY_HASH_QUERY = "update registry_hash set reg_hash = ? where registry_hash_id = 1";
	public static final String GET_KFCOMM_INFO_QUERY = "select * from firmware_version";
	public static final String SAVE_NOTES_DESCRIPTION_QUERY = "insert into notes_desc (trial_log_id, sub_mode_name, step, end_point_name, end_point_value, status) values (?,?,?,?,?,?)";
	public static final String SAVE_NOTES_DESCRIPTION_SETUP_QUERY = "insert into notes_desc_setup (trial_log_id, tmp, duration, permeate_weight, permeate_total, flow_rate, step, status, mode_name) values(?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_DELTA_P_SETTING_AT_RUN_QUERY = "update trial_run_setting set delta_p = ?, delta_p_rate = ?, delta_p_duration = ? where trial_run_setting_id = ?";
	public static final String GET_TUBING_CALIBRATION_FACTOR_FOR_KROSFLO = "select calibration_factor from tubing_lookup where tubing_lookup_id = ?";
    public static final String GET_LAST_CONNECTED_COM_PORT_QUERY = "select port_name from com_port";
    public static final String UPDATE_LAST_CONNECTED_COM_PORT_QUERY = "update com_port set port_name = ?";
}