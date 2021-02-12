package org.lattice.spectrum_backend_final.dao.constants;

/**
 * This class holds all the constants related to API , Messages , String
 * literals and JSON keys.
 */

public class ApiConstant {

	/*************************************
	 * REST END POINTS
	 *********************************************************/

	public static final String REST_ROOT_ENTRY_POINT = "/spectrum";
	public static final String REST_USER_PATH = "/users";
	public static final String REST_USER_ID_PARAM = "/{userId}";
	public static final String REST_USER_LOGIN = "/login";
	public static final String REST_FORGOT_PASSWORD = "/forgot_password";
	public static final String REST_VALIDATE_PASSWORD = "/validate/password";
	public static final String REST_CHANGE_PASSWORD = "/change/password";
	public static final String REST_CHANGE_EMAIL = "/change/email";
	public static final String REST_SET_PASSWORD = "/set_password";
	public static final String REST_USER_PROFILE = "/profile";
	public static final String REST_ACCESS_LOG = "/access_log";
	public static final String REST_USER_LOGOUT = "/logout/{token}";
	public static final String TIME_OUT = "/timeout";
	public static final String CHANGE_PASSWORD = "/change_password";
	public static final String REST_GET_MAIN_PUMP_COMPATIBLE_TUBE_PATH = "/pump/tube/{pumpId}";
	public static final String REST_RESUME_PRESSURE_PATH = "/run/pressure/resume";
	public static final String REST_VALVE_PERCENT_CLOSED_PATH = "/run/percent_closed";
	public static final String REST_SYSTEM_SETTINGS_ENTRY_POINT = "/spectrum/settings";
	public static final String REST_MANUAL_MODE_ENTRY_POINT = "/spectrum/trial/manual";
	public static final String REST_MANUAL_KF_CONDUIT_PATH = "/kfconduit/{isOldConfig}";
	public static final String REST_MANUAL_KF_KONDUIT_EDIT_PATH = "/kfconduit/{trialRunSettingId}/{isOldConfig}";
	public static final String REST_MANUAL_AUX_PUMP_PATH = "/aux/{isOldConfig}";
	public static final String REST_MANUAL_AUX_EDIT_PATH = "/aux/{trialRunSettingId}/{isOldConfig}";
	public static final String REST_MANUAL_ABV_PATH = "/abv/{isOldConfig}";
	public static final String REST_MANUAL_ABV_EDIT_PATH = "/abv/{trialRunSettingId}/{isOldConfig}";
	public static final String REST_MANUAL_MAIN_PUMP_PATH = "/main/{isOldConfig}";
	public static final String REST_MANUAL_MAIN_PUMP_EDIT_PATH = "/main/{trialRunSettingId}/{isOldConfig}";
	public static final String REST_MANUAL_TRIAL_RUN_PATH = "/start/{trialRunSettingId}/{isOldConfig}";
	public static final String REST_LATEST_MANUAL_DATA_PATH = "/latest/{trialRunSettingId}";
	public static final String REST_SAVE_MANUAL_DATA_PATH = "/save";
	public static final String REST_EDIT_MANUAL_DATA_PATH = "/edit/{trialRunSettingId}";
	public static final String REST_MANUAL_TRIAL_STOP_PATH = "/stop/{pause}";
	public static final String REST_MANUAL_SCALE_TARE_PATH = "/tare/{scale}";
	public static final String REST_LICENSE_STATUS_END_POINT = "/license";
	public static final String SYSTEM_ID_PARAM = "/{systemId}";
	public static final String REST_PUMP_LOOK_UP_PATH = "/trial/pumps/lookup";
	public static final String REST_HISTORICAL_PATH = "/spectrum/hist";
	public static final String REST_CALIBRATE_PATH = "/spectrum/calibrate";
	public static final String PRESSURE_TARE_ENDPOINT = "/pressure/tare";
	public static final String PRESSURE_CALIBRATE_ENDPOINT = "/pressure";
	public static final String PUMP_START_END_POINT = "/pump/start";
	public static final String PUMP_STOP_END_POINT = "/pump/stop/{permScale}";
	public static final String TUBE_CALIBRATE_END_POINT = "/tube";
	public static final String REST_DOWNLOAD_USER_MANUAL_END_POINT = "/help/file";
	public static final String REST_GET_FILE_NAME_END_POINT = "/help/file/name";
	public static final String REST_GRAPH_ENDPOINT = "/{trialRunSettingId}/{type}/{isLive}";
	public static final String REST_HISTORICAL_DATA_ENDPOINT = "/hist/{trialRunSettingId}";
	public static final String REST_GRAPH_PATH = "/spectrum/graph";
	public static final String REST_DUMMY_DATA_PATH = "/spectrum/dummy";
	public static final String REST_REFERENCE_CALCULATOR_ENTRY_POINT = "/spectrum/calculator";
	public static final String REST_TUBING_LIST = "/tubing_list";
	public static final String REST_TRIAL_PATH = "/trial";
	public static final String REST_MODES = "/modes";
	public static final String REST_END_POINT = "/end_point/{step_name}";
	public static final String REST_GET_PUMP = "/pump/{mode_id}";
	public static final String REST_GET_TUBING_SPEC = "/tube/{pump_id}";
	public static final String REST_DROPDOWN_KEY = "/{dropdownKey}";
	public static final String REST_CHECK_TRIAL_ID_PATH = "/run/{trialId}";
	public static final String REST_OPEN_DIR_PATH = "/open/dir";

	/*******************************************
	 * Server Constants
	 ***************************************************/

	public static final String LOCALHOST = "192.168.2.188";
	public static final int WEB_SOCKET_PORT = 8080;
	public static final int JETTY_PORT = 9999;
	public static final String ROOT_PATH = "/";
	public static final String REST_PATH = "/rest/*";
	public static final String JERSEY_CONFIG_SERVER_PROVIDER_PACKAGES = "jersey.config.server.provider.packages";
	public static final String ORG_LATTICE_SPECTRUM_BACKEND_FINAL = "org.lattice.spectrum_backend_final";

	/******************************************
	 * MESSAGES
	 **************************************************************/

	public static final String UNAUTHORIZED_ACCESS_MESSAGE = "Session timed out.";
	public static final String USER_DETAILS_UPDATE_MESSAGE = "User details updated.";
	public static final String USER_NOT_FOUND_MESSAGE = "User not found.";
	public static final String INVALID_LOGIN_MESSAGE = "Invalid Credentials. Please try again.";
	public static final String SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION = "Something went wrong. Please restart application.";
	public static final String USER_CREATED_MESSAGE = "User created.";
	public static final String A_USER_ALREADY_LOGGED_INTO_THE_SYSTEM = "An active session is running, please wait till it ends.";
	public static final String LOGIN_SUCCESSFUL_MESSAGE = "Login successful.";
	public static final String LOGOUT_SUCCESSFUL_MESSAGE = "Logout successful.";
	public static final String PASSWORD_SUCCESSFULLY_RESET_MESSAGE = "Password successfully reset.";
	public static final String PHONE_NUMBER_ALREADY_EXIST = "Phone number already exists.";
	public static final String INVALID_OLD_PASSWORD_MESSAGE = "Invalid password. Cannot use any of last 3 passwords set. Please enter new password.";
	public static final String EMAIL_ALREADY_EXIST_MESSAGE = "Somebody is already registered with this Email ID. Please enter alternate Email ID.";
	public static final String USERNAME_ALREADY_EXIST_MESSAGE = "Username has already been taken. Please enter alternate Username.";
	public static final String CREATING_USER_FAILED_NO_ID_OBTAINED = "Creating user failed, no ID obtained.";
	public static final String INVALID_ROLE_SELECTION_MESSAGE = "Please select a valid role.";

	/********************************************
	 * JSON Constants
	 **********************************************************************/

	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String JSON_USER_ID = "user_id";
	public static final String USERNAME = "username";
	public static final String IS_ACTIVE = "is_active";
	public static final String FULL_NAME = "full_name";
	public static final String EMAIL_ID = "email_id";
	public static final String CONTACT_NUMBER = "contact_number";
	public static final String PASSWORD = "password";
	public static final String MASTER_PASSWORD = "master_password";
	public static final String PASS_STATUS = "pass_status";
	public static final String MIDDLE_NAME = "middle_name";
	public static final String LAST_NAME = "last_name";
	public static final String DIGITAL_USERNAME = "digital_username";
	public static final String DIGITAL_PASSWORD = "digital_password";
	public static final String PREFIX = "prefix";
	public static final String DEFAULT_PASSWORD_STATUS = "D";
	public static final String PHONE_ERROR = "phone_error";
	public static final String DEFAULT_PASSWORD = "default_password";
	public static final String NEW_ADMIN_ROLE = "new_admin_role";
	public static final String NEITHER = "Neither";
	public static final String OLD_ADMIN_ROLE = "old_admin_role";
	public static final String NEW_TECH_ROLE = "new_tech_role";
	public static final String OLD_TECH_ROLE = "old_tech_role";
	public static final String EDIT_USER = "Edit user";
	public static final String EDIT_PROFILE = "Edit profile";
	public static final String CREATE_USER = "Create user";
	public static final String ROLE_DESCRIPTION = "role_description";
	public static final String NEW_PASSWORD = "new_password";
	public static final String OLD_PASSWORD = "old_password";
	public static final String EMAIL_ERROR = "email_error";
	public static final String USERNAME_ERROR = "username_error";
	public static final String STATUS_FOR_USER_CURRENT_PASSWORD = "U";
	public static final String ADMIN_ROLE = "admin_role";
	public static final String TECH_ROLE = "tech_role";
	public static final String LAST_LOGIN_DATE = "last_login_date";
	public static final String PASS_IS_ACTIVE = "pass_is_active";
	public static final String MMM_D_YYYY_HH_MM_SS = "MMM d, yyyy, HH:mm:ss";
	public static final String IS_EDITED = "is_edited";
	public static final String OLD_IS_ACTIVE = "old_is_active";

	/*****************************************
	 * STRINGS LITERALS
	 ***************************************************************/

	public static final String AUTHORIZATION = "Authorization";
	public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
	public static final String USER_TYPE = "userType";
	public static final String USER_ID = "userId";
	public static final String BLANK_SPACE = " ";
	public static final String ADMIN = "Admin";
	public static final String AUDITOR = "Auditor";
	public static final String SUPER_ADMIN = "SuperAdmin";
	public static final String IS_EDITABLE = "is_editable";
	public static final String FALSE = "false";
	public static final String PARTIAL = "partial";
	public static final String TRUE = "true";
	public static final String IS_NEW_USER = "isNewUser";
	public static final String YES = "Yes";
	public static final String INVALID = "Invalid";
	public static final String NO = "No";
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	public static final String GET_POST_PUT_DELETE_HEAD = "GET, POST, PUT, DELETE, HEAD";
	public static final String ORIGIN_CONTENT_TYPE_ACCEPT_AUTHORIZATION = "origin, content-type, accept, authorization";
	public static final String ASTERISK = "*";
	public static final String LOGIN = "Login";
	public static final String ROLES = "roles";
	public static final String FIRST_NAME = "first_name";
	public static final String TECHNICIAN = "Technician";
	public static final String MANAGER = "Manager";
	public static final String ROLE_CHANGED = "Role changed";
	public static final String RESET_PASSWORD = "Reset password";
	public static final String STATUS_CHANGED = "Status Changed";
	public static final String PASSWORD_CHANGED = "Password Changed";
	public static final String EMAIL_CHANGED = "Email Changed";

	public static final String UTF_8 = "UTF-8";
	public static final String SHA_1 = "SHA-1";
	public static final String AES = "AES";
	public static final String AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
	public static final String SHA_256 = "SHA-256";
	public static final String SECRET = "spectrum and lattice";
	public static final String LOGOUT = "Logout";
	public static final String EXIT_BROWSER = "Exit Browser";
	public static final String PERCENT_CLOSED_ = "percent_closed_";

	public static final String CELL_CULTURE_VOL_CF = "vol_cf";
	public static final String FILTER_RETENTION = "filter_retention";
	public static final String PERMEATE_STOP_FIRST = "perm_stop_first";
	public static final String RECIRCULATION_PRESSURE_CONTROL = "recirc_pressure_cont";
	public static final String RETENTATE_TUBE_SIZE = "retentate_tube_size";
	public static final String FILTER_TYPE_IS_HOLLOW_FIBER = "Hollow fiber";
	public static final String FILTER_TYPE_IS_CASSETTE = "Cassette";
	public static final String RECIPE_CREATED_SUCCESS_MESSAGE = "Recipe successfully created.";
	public static final String RECIPE_ERROR = "recipe_error";
	public static final String RECIPE_ERROR_MESSSAGE = "Recipe name already exist.";
	public static final String STEP = "step";
	public static final String TUBING_SIZE = "tubing_size";
	public static final String KMPI = "KMPi";
	public static final String FS500 = "FS500";
	public static final String MIN_FLOW_RATE = "min_flow_rate";
	public static final String MAX_FLOW_RATE = "max_flow_rate";
	public static final String MAIN_PUMP = "Main Pump";
	public static final String KROSFLOFS15 = "KrosFlo FS15";
	public static final String KROSFLOFS500 = "KrosFlo FS500";
	public static final String INVALID_FLOW_RATE_ERROR_MESSAGE = "Invalid flow rate.";
	public static final String PERMEATE = "Permeate";
	public static final String PERMEATE_WEIGHT = "Permeate Weight";
	public static final String INVALID_ABV_COUNT_ERROR_MESSAGE = "Invalid ABV count.";
	public static final String KR2I = "KR2i";
	public static final String INVALID_ABV_TYPE_ERROR_MESSAGE = "Invalid ABV type.";
	public static final String INVALID_END_POINT_VALUE_ERROR_MESSAGE = "Invalid end point value at step ";

	public static final String REST_GET_AUX_TUBING_SPEC = "/tube/aux/{auxPumpName}";
	public static final String REST_GET_RECIPE = "/recipe/{trial_master_id}";
	public static final String REST_SET_RECIPE_STATUS = "/recipe/{trial_master_id}/{status}";
	public static final String REST_RECIPE_PATH = "/recipe";

	public static final String REST_TRIAL_START_PATH = "/start";
	public static final String REST_TRIAL_STOP_PATH = "/stop";
	public static final String REST_TRIAL_PAUSE_PATH = "/pause";
	public static final String REST_TRIAL_RESUME_PATH = "/resume";

	public static final String REST_GET_ALL_PORT_PATH = "/ports";
	public static final String REST_CONNECT_PATH = "/connect";

	public static final String STATE = "state";

	public static final String REST_CHANGE_DIRECTION_ON_PAUSE_PATH = "/pause/direction";
	public static final String REST_CHANGE_MAIN_PUMP_FLOW_RATE_ON_RUN_PATH = "/run/main_pump_flow_rate";
	public static final String REST_CHANGE_AUX_PUMP_FLOW_RATE_ON_RUN_PATH = "/run/permeate_flow_rate";
	public static final String REST_OPEN_VALVE_PATH = "/run/open_valve";

	// System Settings responses
	public static final String SYSTEM_SETTINGS_CREATED_MESSAGE = "System settings saved successfully.";
	public static final String EXCEPTION = "Exception At: ";
	public static final String SYSTEM_SETTING_NOT_SAVED_ERROR = "Error in saving system setting details. Please try again.";
	public static final String TFDF_PASSWORD_INCORRECT_ERROR = "TFDF password is incorrect.";
	public static final String SYSTEM_SETTING_NOT_FOUND_ERROR = "System settings not found. Please try again.";
	public static final String SYSTEM_ID = "systemId";
	public static final String SYSTEM_SETTING_NOT_UPDATED_ERROR = "Error in updating system setting details. Please try again.";
	public static final String SYSTEM_SETTINGS_UPDATED_MESSAGE = "System settings updated successfully.";

	public static final String TOKEN = "token";
	public static final int TOKEN_LENGTH = 50;
	public static final String COUNT = "count";
	public static final String ATTEMPT_LEFT = "attempt left.";
	public static final String YOUR_ACCOUNT_IS_BLOCKED_DUE_TO_THREE_INVALID_LOGIN_ATTEMPT = "Your account is blocked due to three invalid login attempt. Please contact Super Admin.";
	public static final String INACTIVE = "inactive";
	public static final String USER_IS_INACTIVE_PLEASE_CONTACT_SUPERADMIN = "User is inactive, Please contact Super Admin.";

	public static final String STEP_NAME = "step_name";
	public static final int TIMER_DELAY = 40000;
	public static final String INVALID_MODE_PLEASE_SELECT_VALID_MODE = "Invalid mode. Please select a valid mode.";
	public static final String CONCENTRATION = "Concentration";
	public static final String CONSTANT_FEED_CONCENTRATION = "Constant Feed Concentration";

	public static final String MODE_ID = "mode_id";
	public static final String MODE_NAME = "mode_name";
	public static final String PUMP_ID = "pump_id";
	public static final String PUMP_NAME = "pump_name";
	public static final String DROPDOWN_KEY = "dropdownKey";
	public static final String RAMP_UP_TIME = "ramp_up_time";
	public static final String HOLLOW_FIBER = "hollow_fiber";
	public static final String AUX_PUMP_FUN = "aux_pump_fun";
	public static final String NOT_FOUND = "404 Not Found";
	public static final String RECIRC_PRESSURE_UNIT = "recirc_pressure_unit";
	public static final String RECIPE_NAME = "recipe_name";
	public static final String TRIAL_TYPE = "trial_type";
	public static final String TRIAL_RUN_STATUS = "trial_run_status";
	public static final String TRIAL_ID = "trial_id";
	public static final String FILTER_TYPE = "filter_type";
	public static final String PART_NO = "part_no";
	public static final String FIBER_COUNT = "fiber_count";
	public static final String FIBER_ID = "fiber_id";
	public static final String SURF_AREA = "surf_area";
	public static final String ECS = "ecs";
	public static final String PERMEATE_TUBE_SIZE = "permeate_tube_size";
	public static final String TUBE_LENGTH = "tube_length";
	public static final String TOTAL_PERM_TUBE_HOLDUP = "total_perm_tube_holdup";

	public static final String NO_OF_CASSETTE = "no_of_cassette";
	public static final String HEAD_COUNT = "head_count";
	public static final String PUMP_TYPE = "pump_type";
	public static final String DIRECTION = "direction";
	public static final String FLOW_CONTROL = "flow_control";
	public static final String PUMP_TUBING_SIZE = "pump_tubing_size";
	public static final String FLOW_RATE = "flow_rate";
	public static final String _END_POINT_TYPE = "_end_point_type";
	public static final String _END_POINT_VALUE = "_end_point_value";
	public static final String TOTAL_STEPS = "total_steps";
	public static final String NO_OF_AUX_PUMP = "no_of_aux_pump";
	public static final String PUMP_FUNCTION_ = "pump_function_";
	public static final String AUX_TUBING_SIZE_ = "aux_tubing_size_";
	public static final String TOTAL_ABV = "total_abv";
	public static final String ABV_TYPE_ = "abv_type_";
	public static final String CONT_BASED_ON_ = "cont_based_on_";
	public static final String ABV_MODE_ = "abv_mode_";
	public static final String START_POS_ = "start_pos_";
	public static final String OP_PRESSURE_ = "op_pressure_";
	public static final String FLOW_RATE_ = "flow_rate_";
	public static final String UV_CH_1 = "uv_ch_1";
	public static final String UV_CH_2 = "uv_ch_2";

	public static final String K_FACTOR_CH_1 = "k_factor_ch_1";
	public static final String K_FACTOR_CH_2 = "k_factor_ch_2";
	public static final String NOTES = "notes";
	public static final String RUN_AS_SAFE = "run_as_safe";
	public static final String FEED_TO_EMPTY = "feed_to_empty";
	public static final String PCV = "pcv";
	public static final String EXCPECTED_YIELD_FC = "exp_yield_fc";

	// system_setting_details columns
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_PRESSURE_UNIT = "pressure_unit";
	public static final String COLUMN_WEIGHT_UNIT = "weight_unit";
	public static final String COLUMN_VOLUME_UNIT = "volume_unit";
	public static final String COLUMN_LENGTH_UNIT = "length_unit";
	public static final String COLUMN_DECIMAL_PRESSURE = "decimal_pressure";
	public static final String COLUMN_DECIMAL_WEIGHT = "decimal_weight";
	public static final String COLUMN_DECIMAL_FLOW_RATE = "decimal_flow_rate";
	public static final String COLUMN_DECIMAL_VOLUME = "decimal_volume";
	public static final String COLUMN_DECIMAL_LENGTH = "decimal_length";
	public static final String COLUMN_DATE_FORMAT = "date_format";
	public static final String COLUMN_IS_TFDF_ENABLED = "is_tfdf_enabled";
	public static final String COLUMN_PASSWORD = "password";
	public static final String IS_AUX_KONDUIT = "is_aux_konduit";
	public static final String IS_AUX_KONDUIT_2 = "is_aux_konduit_2";
	public static final String COLUMN_UV_MIN = "uv_min";
	public static final String COLUMN_UV_MAX = "uv_max";
	public static final String COLUMN_PH_MIN = "ph_min";
	public static final String COLUMN_PH_MAX = "ph_max";
	public static final String COLUMN_PH_MIN_2 = "ph_min_2";
	public static final String COLUMN_PH_MAX_2 = "ph_max_2";
	public static final String COLUMN_TURBIDITY_MIN = "turbidity_min";
	public static final String COLUMN_TURBIDITY_MAX = "turbidity_max";
	public static final String COLUMN_TURBIDITY_MIN_2 = "turbidity_min_2";
	public static final String COLUMN_TURBIDITY_MAX_2 = "turbidity_max_2";
	public static final String COLUMN_TOTALIZER_MIN = "totalizer_min";
	public static final String COLUMN_TOTALIZER_MAX = "totalizer_max";
	public static final String COLUMN_TOTALIZER_MIN_2 = "totalizer_min_2";
	public static final String COLUMN_TOTALIZER_MAX_2 = "totalizer_max_2";
	public static final String COLUMN_PROTEIN_CONC_MIN = "protein_conc_min";
	public static final String COLUMN_PROTEIN_CONC_MAX = "protein_conc_max";
	public static final String COLUMN_PROTEIN_CONC_MIN_2 = "protein_conc_min_2";
	public static final String COLUMN_PROTEIN_CONC_MAX_2 = "protein_conc_max_2";
	public static final String COLUMN_TOTALIZER_UNIT = "totalizer_unit";
	public static final String COLUMN_TOTALIZER_UNIT_2 = "totalizer_unit_2";

	public static final String KF_CONDUIT_SETTING_ID = "kf_conduit_setting_id";
	public static final String KF_CONDUIT = "kf_conduit";
	public static final String KF_CONDUIT_ERROR_MESSAGE = "KF Conduit is not connected.";

	// license activation status columns

	public static final String REST_ALARM_SETTING_ENTRY_POINT = "/spectrum/alarm";

	// manual mode response messages
	public static final String ERROR_TRIAL_DETAILS_NOT_FOUND = "Trial details not found.";
	public static final String KFCONDUIT_SETTINGS_SAVED_MESSAGE = "KF konduit setting saved successfully.";
	public static final String ERROR_KF_CONDUIT_SETTING_NOT_SAVED = "Error in saving KF konduit setting. Please try again.";
	public static final String ERROR_KFCONDUIT_NOT_FOUND = "KF konduit setting not found.";
	public static final String KFCONDUIT_SETTINGS_UPDATE_MESSAGE = "KF konduit setting updated successfully.";
	public static final String ERROR_KF_CONDUIT_DETAILS_NOT_UPDATED = "Error in updating KF konduit setting. Please try again.";
	public static final String ERROR_TRIAL_RUN_SETTING_NOT_FOUND = "Trial details not found.";
	public static final String ERROR_AUX_PUMP_SETTING_NOT_SAVED = "Error in saving AUX pump setting. Please try again.";
	public static final String AUX_PUMP_SETTING_SAVED_MESSAGE = "AUX pump setting saved successfully.";
	public static final String ERROR_AUX_PUMP_NOT_FOUND = "AUX pump setting not found.";
	public static final String ERROR_AUX_PUMP_DETAILS_NOT_UPDATED = "Error in updating AUX pump setting. Please try again.";
	public static final String AUX_PUMP_SETTINGS_UPDATE_MESSAGE = "AUX pump setting updated successfully.";
	public static final String ERROR_ABV_SETTING_NOT_SAVED = "Error in saving ABV setting. Please try again.";
	public static final String ABV_SETTING_SAVED_MESSAGE = "ABV setting saved successfully.";
	public static final String ERROR_ABV_NOT_FOUND = "ABV setting not found.";
	public static final String ERROR_ABV_DETAILS_NOT_UPDATED = "Error in updating ABV setting. Please try again.";
	public static final String ABV_SETTINGS_UPDATE_MESSAGE = "ABV setting updated successfully.";
	public static final String ERROR_MAIN_PUMP_SETTING_NOT_SAVED = "Error in saving main pump setting. Please try again.";
	public static final String MAIN_PUMP_SETTING_SAVED_MESSAGE = "Main pump setting saved successfully.";
	public static final String ERROR_MAIN_PUMP_NOT_FOUND = "Main pump setting not found.";
	public static final String ERROR_MAIN_PUMP_DETAILS_NOT_UPDATED = "Error in updating main pump setting. Please try again.";
	public static final String MAIN_PUMP_SETTINGS_UPDATE_MESSAGE = "Main pump setting updated successfully.";

	public static final String AUX_PUMP_ = "aux_pump_";
	public static final String IP = "IP";
	public static final String CONCENTRATION_FACTOR = "Concentration Factor";
	public static final String TRIAL_ERROR = "trial_error";
	public static final String TRIAL_ERROR_MESSSAGE = "Trial id already exist.";
	public static final String AUX_PUMP = "aux_pump";
	public static final String AUX_PUMP_NAME = "auxPumpName";
	public static final String TRIAL_MASTER_ID = "trial_master_id";
	public static final String MODE_FULL_NAME = "mode_full_name";
	public static final String FLOW_RATE_UNIT = "flow_unit";
	public static final String LITER_PER_MIN = "L/min";
	public static final int RESULT_SET_SIZE = 999;
	public static final String END_POINTS = "end_points";
	public static final String ABV = "abv";
	public static final String KF_KONDUIT_SETTING_ID = "kf_konduit_setting_id";
	public static final String END_POINT_VALUE = "end_point_value";
	public static final String OP_PRESSURE = "op_pressure";
	public static final String C_MODE = "C";
	public static final String D_MODE = "D";
	public static final String CD_MODE = "C/D";
	public static final String CFC_MODE = "CFC";
	public static final String CDDC_MODE = "C/D/D/C";
	public static final String CFDC_MODE = "CFC/D/C";
	public static final String CDC_MODE = "C/D/C";
	public static final String HARVEST_MODE = "TFDF";
	public static final String VACUUM_MODE = "Vacuum";
	public static final String FEED = "feed";
	public static final String RETENTATE = "retentate";
	public static final String TMP_ABV_CONTROL_BASED = "TMP";
	public static final String PERMEATE_ABV_CONTROL_BASED = "Permeate";
	public static final String RETENTATE_ABV_CONTROL_BASED = "Retentate";
	public static final String KR1 = "KR1";
	public static final String KRJR = "KRJr";
	public static final String DEVICE_ID = "device_id";
	public static final String DEVICE_NAME = "device_name";
	public static final String IS_CONNECTED = "is_connected";
	public static final String SCALE = "scale";
	public static final String KF_KONDUIT = "kf_conduit";
	public static final String PLACED_ON = "placed_on";
	public static final String IS_MAIN_PUMP_CONNECTED = "is_main_pump_connected";
	public static final String FLOW_CONVERSION = "flow_conversion";
	public static final String FLOW_CONVERSION_KRJR = "flow_conversion_KRJr";
	public static final String PERMEATE_PRESSURE = "permeate_pressure";
	public static final String FEED_PRESSURE = "feed_pressure";
	public static final String RETENTATE_PRESSURE = "retentate_pressure";
	public static final String TMP_PRESSURE = "tmp_pressure";
	public static final String MAIN_PUMP_FLOW_RATE = "main_pump_flow_rate";
	public static final String AUX_PUMP_1_FLOW_RATE = "aux_pump_1_flow_rate";
	public static final String AUX_PUMP_2_FLOW_RATE = "aux_pump_2_flow_rate";
	public static final String CONDUCTIVITY_1 = "conductivity_1";
	public static final String TEMPERATURE_1 = "temperature_1";
	public static final String FEED_SCALE = "feed_scale";
	public static final String PERMEATE_SCALE = "permeate_scale";
	public static final String DELTA_P = "delta_p";
	public static final String UPTO_TWO_DECIMAL_PLACES = "###############0.00";
	public static final String TIMESTAMP = "timestamp";
	public static final String MAIN_PUMP_SPEED = "main_pump_speed";
	public static final String AUX_PUMP_1_SPEED = "aux_pump_1_speed";
	public static final String AUX_PUMP_2_SPEED = "aux_pump_2_speed";
	public static final String SURFACE_AREA = "surface_area";
	public static final String FLUX = "flux";
	public static final String MAIN_PUMP_VALIDATION_ERROR_MESSAGE = "Main pump type mismatch.";
	public static final String KF_KONDUIT_ERROR_MESSAGE = "Konduit is not connected.";
	public static final String AUX_PUMP_1_CONNECTED = "aux_pump_1_connected";
	public static final String AUX_PUMP_2_CONNECTED = "aux_pump_2_connected";
	public static final String AUX_PUMP_TYPE = "aux_pump_type";
	public static final String AUX_TUBING_SIZE = "aux_tubing_size";
	public static final String AUX_PUMP_CONNECTION_ERROR_MESSAGE = " aux pump not connected.";
	public static final String SCALE_CONNECTION_ERROR_MESSAGE = " scale not connected.";
	public static final String PUMP_FUNCTION = "pump_function";
	public static final String CONCENTRATION_FACT = "concentration_fact";
	public static final String DIAFILTRATION_VOL_1 = "diafiltration_vol_1";
	public static final String CLOCKWISE = "clockwise";
	public static final String END_POINT_TYPE = "end_point_type";
	public static final String COM_PORT = "COM";
	public static final String PORT_NAME = "port_name";
	public static final String PORT_SUCCESSFULLY_CONNECTED_MESSAGE = "Successfully connected to ";
	public static final String DIAFILTRATION_VOLUME = "Diafiltration Volume";
	public static final String CONDUCTIVITY = "Conductivity";
	public static final String UV = "UV";
	public static final String DIAFILTRATION = "Diafiltration";
	public static final String MAX_SPEED = "max_speed";
	public static final String CONTROL_BASED_ON = "control_based_on";
	public static final String START_POS = "start_pos";
	public static final String PERCENT_CLOSED = "percent_closed";
	public static final String ABV_MODE = "abv_mode";
	public static final String AUTO = "auto";
	public static final String RECIRCULATION_RATE_PER_UNIT = "recirc_rate_per_unit";
	public static final String RECIRCULATION_PRESSURE_UNIT_VALUE = "recirc_flow_unit_value";
	public static final String PER_MINUTE = "per minute";
	public static final String PER_SECOND = "per second";
	public static final String CONT_BASED_ON = "cont_based_on";
	public static final String INVALID_PORT = "Invalid port. Please connect to a valid port.";
	public static final String TYPE = "type";
	public static final String LIVE = "live";
	public static final String EVENT = "event";
	public static final String SUB_MODE_FINISHED = " sub mode finished.";
	public static final String SUPER_MODE_FINISHED = " mode finished.";
	public static final String ENDPOINT_REACHED = " end point reached.";
	public static final String MESSAGE = "message";
	public static final String RAMP_UP_DONE = "Ramp up done.";
	public static final String IS_FINISHED = "is_finished";
	public static final String INVALID_ENDPOINT = "Invalid endpoint. Please connect to a valid endpoint.";
	public static final String PERMEATE_SCALE_MISSING = "Please connect a permeate scale";
	public static final String SUB_MODE_STARTED = " sub mode started";
	public static final String FEED_SCALE_VALUE_IS_NOT_VALID = "Feed weight is not valid. Feed weight must be greater than hold up.";
	public static final String PERMEATE_SCALE_TARE_STARTED = "Permeate scale taring started.";
	public static final String PERMEATE_SCALE_TARE_COMPLETED = "Permeate scale taring complete.";
	public static final String PERMEATE_SCALE_TARE_TIMEOUT = "Permeate scale tare timeout.";
	public static final String FORWARD_SLASH = "/";
	public static final String CONSTANT_FEED = "Constant Feed";
	public static final String COM_PORT_CONNECTION_ERROR_MESSAGE = "COM port not connected. Please connect first.";
	public static final String RECIPE_DOES_NOT_EXIST_ERROR_MESSAGE = "Recipe doesn't exist.";
	public static final String PERM_TUBE_HOLDUP = "perm_tube_holdup";
	public static final String FEED_FLOW_RATE = "feed_flow_rate";
	public static final String PERMEATE_FLOW_RATE = "permeate_flow_rate";
	public static final String RETENTATE_FLOW_RATE = "retentate_flow_rate";
	public static final String SHEAR = "shear";
	public static final String ABV_1 = "abv_1";
	public static final String ABV_2 = "abv_2";
	public static final int BUFFER_SIZE = 8;
	public static final String ENDPOINT_IS_NOT_FEASIBLE = "End point is not feasible.";
	public static final String INVALID_PARAMETER = "Invalid parameter.";
	public static final String UNABLE_TO_MAINTAIN_PRESSURE = "Unable to maintain pressure.";
	public static final String VALVE_FULL_OPEN = " Valve fully open.";
	public static final String VALVE_FULL_CLOSE = " Valve fully closed.";
	public static final String RUN_ERROR = "";
	public static final String CONDUCTIVITY_2 = "conductivity_2";
	public static final String TEMPERATURE_2 = "temperature_2";
	public static final String TOTAL_PERM_WEIGHT = "total_perm_weight";
	public static final String M_PERMEATE = "m_permeate";
	public static final String DIRECTION_CHANGE_SUCCESS_MESSAGE = "Main pump direction successfully changed.";
	public static final String MAIN_PUMP_FLOW_RATE_CHANGE_SUCCESS_MESSAGE = "Main pump flow rate changed to ";
	public static final String ML_MIN = " mL/min";
	public static final String AUX_PUMP_FLOW_RATE_CHANGE_SUCCESS_MESSAGE = "Aux pump flow rate changed to ";
	public static final String VALVE_FULLY_OPENED_MESSAGE = "Valve fully opened.";
	public static final String VT = "vt";
	public static final String CDCD_MODE = "C/D/C/D";
	public static final String OPEN_POSITION = "valve_pos_full_open";
	public static final String CLOSED_POSITION = "valve_pos_full_pinched";
	public static final String VALVE_POSITION_NOT_SET_ERROR_MESSAGE = "Valve position not set.";
	public static final String LIVE_DATA_ADD_EVENT = "add";
	public static final String TRIAL_STOPPED_MESSAGE = "Trial stopped.";
	public static final String TRIAL_PAUSED_MESSAGE = "Trial paused.";
	public static final String RESUME_TRIAL_MESSAGE = "Trial resumed.";
	public static final String IS_FEED_EMPTY = "is_feed_empty";
	public static final String FEED_HOLD_UP = "feed_hold_up";

	public static final String IS_SET_TO = " is set to ";
	public static final String ABV_PLACED_ON = "Abv placed on ";
	public static final String PERCENT = "%";
	public static final String PRESSURE_CONTROL_RESUMED = "Pressure control resumed.";
	public static final String NOT_APPLICABLE = "N/A";

	// license activation status columns
	public static final String STATUS_ID = "status_id";
	public static final String STATUS = "status";
	public static final String LICENSE_ACTIVATION_SUCCESS = "License activated successfully";
	public static final String LICENSE_ACTIVATION_ERROR = "Error in activating license. Please try again.";

	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String PERMEATE_HOLD_UP_VOLUME_CALCULATION = "perm_hold_up_cal";
	public static final String FILTER_PLATE_INSERT_PART_NO = "fpi_part_no";
	public static final String IS_GENERIC = "is_generic";
	public static final String CASSETTE = "cassette";
	public static final String FILTER_PLATE_INSERT = "fpi";
	public static final String NO_OF_CHANNEL = "no_of_channel";

	public static final String PSI = "psi";
	public static final String M_BAR = "mbar";
	public static final String BAR = "bar";
	public static final String GRAM = "g";
	public static final String KILOGRAM = "kg";
	public static final String LITER = "L";
	public static final String INCH = "in";
	public static final String MILLIMETER = "mm";
	public static final String CENTIMETER = "cm";
	public static final String MILLILITER = "mL";
	public static final String UNDER_SCORE = "_";
	public static final String TRIAL_RESUME_MESSAGE = "Trial resumed.";
	public static final String CLAMP_PERMEATE_LINE_WHEN_RETENTATE_CLEANING = "Put retentate line into permeate reservoir and clamp off permeate tube. Click ‘Yes’ if completed.";
	public static final String OPEN_RETENTATE_LINE_WHEN_PERMEATE_CLEANING_WITHOUT_PERMEATE_PUMP = "0";

	public static final String FEED_PRESSURE_LOW_ALARM = "feed_pressure_low_alarm";
	public static final String FEED_PRESSURE_LOW_STOP = "feed_pressure_low_stop";
	public static final String FEED_PRESSURE_HIGH_ALARM = "feed_pressure_high_alarm";
	public static final String FEED_PRESSURE_HIGH_STOP = "feed_pressure_high_stop";

	public static final String FEED_WT_LOW_ALARM = "feed_wt_low_alarm";
	public static final String FEED_WT_LOW_STOP = "feed_wt_low_stop";
	public static final String FEED_WT_HIGH_ALARM = "feed_wt_high_alarm";
	public static final String FEED_WT_HIGH_STOP = "feed_wt_high_stop";

	public static final String PERM_WT_LOW_ALARM = "perm_wt_low_alarm";
	public static final String PERM_WT_LOW_STOP = "perm_wt_low_stop";
	public static final String PERM_WT_HIGH_ALARM = "perm_wt_high_alarm";
	public static final String PERM_WT_HIGH_STOP = "perm_wt_high_stop";

	public static final String PERM_PRESSURE_LOW_ALARM = "perm_pressure_low_alarm";
	public static final String PERM_PRESSURE_LOW_STOP = "perm_pressure_low_stop";
	public static final String PERM_PRESSURE_HIGH_ALARM = "perm_pressure_high_alarm";
	public static final String PERM_PRESSURE_HIGH_STOP = "perm_pressure_high_stop";

	public static final String KONDUIT_CH_1_HIGH_ALARM = "konduit_ch_1_high_alarm";
	public static final String KONDUIT_CH_1_HIGH_STOP = "konduit_ch_1_high_stop";

	public static final String KONDUIT_CH_2_HIGH_ALARM = "konduit_ch_2_high_alarm";
	public static final String KONDUIT_CH_2_HIGH_STOP = "konduit_ch_2_high_stop";
	public static final String PERMEATE_TOTAL_HIGH_ALARM = "permeate_total_high_alarm";
	public static final String PERMEATE_TOTAL_HIGH_STOP = "permeate_total_high_stop";

	public static final String REST_SPECTRUM_TRIAL_ENTRY_POINT = "/spectrum/trial";
	public static final String FEED_START_WEIGHT = "feed_start_wt";
	public static final String ALARM_SAVED_SUCCESSFULLY_MESSAGE = "Alarm successfully saved.";
	public static final String REST_ALARM_SETTING_AT_RUN_ENTRY_POINT = "/trial/alarm/run";
	public static final String BLANK_QUOTE = "";
	public static final String TRIAL_RUN_EXIST_ERROR_MESSAGE = "Trial id already exist.";
	/**
	 * Alarm indicator text message
	 */
	public static final String FEED_PRESSURE_ALARM_HIGH_STOP_MESSAGE = "Feed PR High Stp";
	public static final String FEED_PRESSURE_ALARM_HIGH_ALARM_MESSAGE = "Feed PR High";
	public static final String FEED_PRESSURE_ALARM_LOW_ALARM_MESSAGE = "Feed PR Low";
	public static final String FEED_PRESSURE_ALARM_LOW_STOP_MESSAGE = "Feed PR Low Stp";
	public static final String PERMEATE_PRESSURE_ALARM_HIGH_STOP_MESSAGE = "Perm PR High Stp";
	public static final String PERMEATE_PRESSURE_ALARM_HIGH_ALARM_MESSAGE = "Perm PR High";
	public static final String PERMEATE_PRESSURE_ALARM_LOW_ALARM_MESSAGE = "Perm PR Low";
	public static final String PERMEATE_PRESSURE_ALARM_LOW_STOP_MESSAGE = "Perm PR Low Stp";
	public static final String FEED_WEIGHT_ALARM_HIGH_STOP_MESSAGE = "Feed Wt. High Stp";
	public static final String FEED_WEIGHT_ALARM_HIGH_ALARM_MESSAGE = "Feed Wt. High";
	public static final String FEED_WEIGHT_ALARM_LOW_ALARM_MESSAGE = "Feed Wt. Low";
	public static final String FEED_WEIGHT_ALARM_LOW_STOP_MESSAGE = "Feed Wt. Low Stp";
	public static final String PERMEATE_WEIGHT_ALARM_HIGH_STOP_MESSAGE = "Perm Wt. High Stp";
	public static final String PERMEATE_WEIGHT_ALARM_HIGH_ALARM_MESSAGE = "Perm Wt. High";
	public static final String PERMEATE_WEIGHT_ALARM_LOW_ALARM_MESSAGE = "Perm Wt. Low";
	public static final String PERMEATE_WEIGHT_ALARM_LOW_STOP_MESSAGE = "Perm Wt. Low Stp";
	public static final String KONDUIT_CH2_ALARM_HIGH_STOP_MESSAGE = " Ch2 High Stp";
	public static final String KONDUIT_CH2_ALARM_HIGH_ALARM_MESSAGE = " Ch2 High";
	public static final String KONDUIT_CH1_ALARM_HIGH_ALARM_MESSAGE = " Ch1 High";
	public static final String KONDUIT_CH1_ALARM_HIGH_STOP_MESSAGE = " Ch1 High Stp";
	public static final String FLOW_METER_CH1_ALARM_HIGH_ALARM_MESSAGE = "Perm Total High";
	public static final String FLOW_METER_CH1_ALARM_HIGH_STOP_MESSAGE = "Perm Total High Stp";
	public static final String FLOW_METER_CH2_ALARM_HIGH_ALARM_MESSAGE = "Perm Total High";
	public static final String FLOW_METER_CH2_ALARM_HIGH_STOP_MESSAGE = "Perm Total High Stp";
	public static final String FEED_TO_EMPTY_DETECTED = "Extra feed empty detected.";
	public static final String INVALID_AUX_PORT_ID_ERROR_MESSAGE = "Invalid port id for permeate pump.";
	public static final String ALARM_DESCRIPTION_FEED_PRESSURE = "Feed pressure";
	public static final String ALARM_DESCRIPTION_PERMEATE_PRESSURE = "Permeate pressure";
	public static final String ALARM_DESCRIPTION_FEED_WEIGHT = "Feed weight";
	public static final String ALARM_DESCRIPTION_PERMEATE_WEIGHT = "Permeate weight";
	public static final String ALARM_DESCRIPTION_KONDUIT_CH_1 = " Ch 1";
	public static final String ALARM_DESCRIPTION_KONDUIT_CH_2 = " Ch 2";
	public static final String OF = "of";
	public static final String ALARM = "alarm";
	public static final String ALARM_STOP_DESCRIPTION = "alarm_stop_desc";
	public static final String ALARM_STOP_VALUE = "alarm_stop_value";
	public static final String NATURE = "nature";
	public static final String LOW = "Low";
	public static final String HIGH = "High";
	public static final String STOP = "Stop";
	public static final String REST_AUTO_CONNECT_PORT_PATH = "/auto_connect";
	public static final String CONNECTION_FAILED_ERROR_MESSAGE = "Connection failed. Please try again.";
	public static final String PORT_NOT_FOUND = "Port not found.";
	public static final String TURBIDITY = "Turbidity";
	public static final String PH = "pH";
	public static final String REST_NOTES_ENTRY_POINT = "/spectrum/trial/notes";
	public static final String NOTES_ADDED_SUCCESSFULLY_MESSAGE = "Note successfully added.";
	public static final String POSTED_BY = "posted_by";
	public static final String CREATED_ON = "created_on";
	public static final String NOTES_ID = "notes_id";
	public static final int MAX_NOTES_COUNT = 25;
	public static final String REST_TRIAL_DETAIL_PATH = "/trial/detail";
	public static final String TRIAL_RUN_SETTING_ID = "trial_run_setting_id";
	public static final String REST_TRIAL_SAVE_NOTES_ENTRY_POINT = "/trial/notes";
	public static final String REST_TRIAL_GET_NOTES_ENTRY_POINT = "/trial/notes/{trialId}";
	public static final String CONNECTION_LIST_EVENT = "connection";
	public static final String IS_ABV_1_CONNECTED = "is_abv_1_connected";
	public static final String IS_ABV_2_CONNECTED = "is_abv_2_connected";
	public static final String AUX_PUMP_1_TYPE = "aux_pump_1_type";
	public static final String AUX_PUMP_2_TYPE = "aux_pump_2_type";
	public static final String DISCONNECT = "disconnect";
	public static final String AUX_PUMP_TYPE_MISMATCH_ERROR_MESSAGE = "aux pump type mismatch.";
	public static final String KR2I_WITH_QUATRTRO_PUMP_HEAD = "KR2i with Quattro pump head";
	public static final String KMPI_WITH_QUATRTRO_PUMP_HEAD = "KMPi with Quattro pump head";
	public static final String TRIAL_DETAIL_SAVED_SUCCESSFULLY_MESSAGE = "Trial details saved.";
	public static final String PUMP_LOOKUP_ID = "pump_lookup_id";
	public static final String REST_GET_TUBE_COMPATIBLE_AUX_PATH = "/tube/aux_pump/{tubing_lookup_id}";

	public static final String START_STEP_SIZE = "start_step_size";

	public static final String RPM = "RPM";
	public static final String MANUAL = "Manual";
	public static final String SAVE_TRIAL_DETAIL_ACTION = "Save Trial";
	public static final String EDIT_TRIAL_DETAIL_ACTION = "Edit Trial";
	public static final String TRIAL_STOP_ACTION = "Trial stop button pressed";
	public static final String TRIAL_START_ACTION = "Trial Start";
	public static final String TRIAL_PAUSE_ACTION = "Trial pause button pressed";
	public static final String TRIAL_RESUME_ACTION = "Trial resume button pressed";
	public static final String PUMP_FLOW_RATE_CHANGE_ACTION = "Main Pump Flow Rate Change";
	public static final String EMERGENCY_VALVE_OPEN_ACTION = "Emergency Valve Open";
	public static final String RESUME_PRESSURE_CONTROL_ACTION = "Pressure Control Resume";
	public static final String TRIAL_ID_PATH_PARAM = "trialId";
	public static final int LOG_TYPE_TRIAL = 0;
	public static final int LOG_TYPE_SYSTEM = 1;
	public static final int LOG_TYPE_TRIAL_AUTO_LOG = 2;
	public static final String SET_SYSTEM_SETTING_ACTION = "Set System Settings";
	public static final String PUMP_SPEED = "pump_speed";
	public static final String POSITION = "position";
	public static final String ABV_TYPE = "abv_type";
	public static final String MAIN_PUMP_NAME = "pump_name";
	public static final String SPEED = "speed";

	// pump look up
	public static final String PUMP_LOOK_UP_LIST_NOT_FOUND_ERROR = "Pump details not found in system.";
	public static final String PUMP_LOOK_UP_ID = "pump_lookup_id";
	public static final String MIN_SPEED = "min_speed";
	public static final String SPEED_UNIT = "speed_unit";

	public static final String PERMEATE_SCALE_TIMEOUT = "Permeate scale tare timeout. Please try again.";
	public static final String PERMEATE_SCALE_TARED_SUCCESS = "Permeate scale tared successfully.";
	public static final String PERMEATE_SCALE_NOT_FOUND = "Permeate scale missing.";
	public static final String FEED_SCALE_TARE_TIMEOUT = "Feed scale tare timeout. Please try again.";
	public static final String FEED_SCALE_TARED_SUCCESS = "Feed scale tared successfully.";
	public static final String FEED_SCALE_NOT_FOUND = "Feed scale missing.";
	public static final String MANUAL_RUN_STARTED = "Manual run started.";

	public static final String TRIAL_LIVE_DATA_ID = "trial_live_data_id";
	public static final String TRIAL_TABLE_LOGS = "table";
	public static final String TOTAL_PAGES = "totalPages";
	public static final String ALARM_HISTORY_ID = "alarm_history_id";
	public static final String ALARM_LOGS = "alarms";
	public static final String COLUMN_CONCENTRATION_FACTOR = "concentration_factor";
	public static final String TMP = "tmp";
	public static final String NOTES_LOGS = "notes";
	public static final String REST_HIST_TABLE_PATH = "/table/{trialRunSettingId}";
	public static final String REST_HIST_ALARMS_PATH = "/alarms/{trialRunSettingId}";
	public static final String PAGE_SIZE = "pageSize";
	public static final String OFFSET = "offset";
	public static final String PAGE_COUNT = "pageCount";
	public static final String INTERVAL = "interval";
	public static final String REST_HIST_NOTES_PATH = "/notes/{trialRunSettingId}";
	public static final String REST_HIST_TRIAL_LOGS_PATH = "/trial/{trialRunSettingId}";
	public static final String REST_HIST_ALL_TRIAL_LOGS_PATH = "/trial/logs/{trialRunSettingId}";
	public static final String REST_DOWNLOAD_HIST_TRIAL_LOGS_PATH = "/download/{trialRunSettingId}/{trialMasterId}";
	public static final String REST_PDF_HIST_PATH = "/pdf/{trialRunSettingId}/{trialMasterId}";
	public static final String REST_PDF_DOWNLOAD_PATH = "/pdf/download/{trialId}";
	public static final String PARAM_TRIAL_RUN_SETTING_ID = "trialRunSettingId";
	public static final String SEARCH = "search";
	public static final String TRIAL_LOG_ID = "trial_log_id";
	public static final String ACTION = "action";
	public static final String ROLE = "role";
	public static final String TRIAL_LOGS = "trial_logs";
	public static final String SEARCH_PARAM = "%{0}%";
	public static final String FILTER = "filter";
	public static final String REST_TRIAL_LOGS_LIST_PATH = "/trial/logs";
	public static final String REST_TRIAL_LOGS_DETAILS_PATH = "/trial/logs/{trialRunSettingId}/{trialMasterId}";
	public static final String TRIAL_START_TIME = "trial_start_time";
	public static final String TRIAL_END_TIME = "trial_end_time";
	public static final String TRIAL_STATUS = "trial_status";
	public static final String TRIAL_LOGS_LIST_NOT_FOUND_ERROR = "Trial logs list not found.";
	public static final String PARAM_TRIAL_MASTER_ID = "trialMasterId";
	public static final String RUN_KEY = "run";
	public static final String ALARMS_KEY = "alarms";
	public static final String TRIAL_DURATION_KEY = "trial_duration";
	public static final String DURATION_FORMAT = "{0} hours, {1} minutes, {2} seconds";
	public static final String TUBING_EVENT = "tubingEvent";
	public static final String PRESSURE_EVENT = "pressureEvent";
	public static final String TUBING_LOOK_UP_ID_PARAM = "tubingLookUpId";
	public static final String CALIBRATION_FACTOR_PARAM = "calFactor";
	public static final String CHANNEL_ID = "channelId";
	public static final String VALUE = "value";

	public static final String EDIT_KONDUIT_ACTION = "Edit Konduit";
	public static final String EDIT_ABV_SETTING_ACTION = "Abv Setting Edit";
	public static final String AUX_PUMP_SETTING_EDIT_ACTION = "Aux Pump Setting Edit";
	public static final String MAIN_PUMP_SETTING_EDIT_ACTION = "Main Pump Setting Edit";
	public static final int TRIAL_RUNNING_STATUS = 0;
	public static final int TRIAL_COMPLETE_STATUS = 1;
	public static final int TRIAL_TERMINATE_STATUS = 2;
	public static final String EDIT_ALARM_ACTION = "Trial configuration updated";

	public static final String TUBING_LOOKUP_ID = "tubing_lookup_id";
	public static final String CREATED_BY = "created_by";
	public static final String REST_GET_SYSTEM_LOG_PATH = "/trial/log/{logType}";
	public static final String PORT = "port";
	public static final String CALIBRATION_FACTOR = "calibration_factor";
	public static final String PUMP_TUBING_LOOKUP_ID = "pump_tubing_lookup_id";
	public static final String NWP = "NWP";
	public static final String FLUX_C = "Flux C";
	public static final String FLUX_CV = "Flux CV";
	public static final String CLEANING = "Cleaning";
	public static final String FLUSHING = "Flushing";
	public static final String TOTAL_TARGET_STEP = "total_target_step";
	public static final String DURATION = "duration";
	public static final String FLUX_FLOW_RATE = "flux_flow_rate";
	public static final String PERM_WT = "permeate_weight";
	public static final String TARGET_STEP = "target_step";
	public static final String INVALID_MODE_ERROR_MESSAGE = "Invalid mode. Please select a valid mode";
	public static final String TEMPERATURE_CORRECTION_FACTOR = "tcf";
	public static final String WATER_FLUX = "water_flux";
	public static final String WATER_FLUX_20_DEGREE = "water_flux_20_degree";
	public static final String WATER_PERMEABILITY = "water_perm";
	public static final String TEMPERATURE = "temperature";
	public static final String VACCUM_SUB_MODE_SWITCH_MESSAGE = "Sub Mode Changed.";
	public static final String REST_SWITCH_VACUME_SUB_MODE_API = "/trial/vacuum/switch";
	public static final String EXPECTED_VALUE_KEY = "expectedValue";
	public static final String ACTUAL_VALUE_KEY = "actualValue";
	public static final String START_KEY = "start";
	public static final String STOP_KEY = "stop";
	public static final String IS_FEED_ACTIVE = "feed_active";
	public static final String IS_PERMEATE_ACTIVE = "perm_active";
	public static final String IS_RETENTATE_ACTIVE = "ret_active";
	public static final String FEED_VALUE = "feed_value";
	public static final String PERMEATE_VALUE = "perm_value";
	public static final String RETENTATE_VALUE = "ret_value";
	public static final String PRESSURE_KEY = "pressure";
	public static final String FEED_CALIBRATE = "Feed Pressure";
	public static final String PERMEATE_CALIBRATE = "Permeate Pressure";
	public static final String RETENTATE_CALIBRATE = "Retentate Pressure";
	public static final String RESULT = "result";
	public static final String PRESSURE_CALIBRATION_OUT_OF_RANGE = "Calibration pressure value out of range.";
	public static final String INVALID_CHANNEL_SELECTED = "Invalid pressure channel selected.";
	public static final String PRESSURE_CALIBRATED_SUCCESS = " Calibrated";
	public static final String SENSOR_NOT_CONNECTED = " sensor not connected";
	public static final String TUBING_CALIBRATED_MESSAGE = "Tubing Calibrated.";
	public static final String UPTO_THREE_DECIMAL_PLACES = "0.000";
	public static final String PRESSURE_TARED = "Pressures tared.";
	public static final String REST_PRESSURE_CALIBRATE_LOG_PATH = "/pressure/logs";
	public static final String REST_TUBING_CALIBRATE_LOG_PATH = "/tubing/logs";
	public static final String FEED_CREATED_ON = "feed_created_on";
	public static final String FEED_USERNAME = "feed_username";
	public static final String RETENTATE_CREATED_ON = "ret_created_on";
	public static final String RETENTATE_USERNAME = "ret_username";
	public static final String PERMEATE_CREATED_ON = "perm_created_on";
	public static final String PERMEATE_USERNAME = "perm_username";
	public static final String LIVE_DATA_DECIMAL_FORMAT_PATTERN = "##############.#########";
	public static final String PUMP_MIN_FLOW_RATE = "pump_min_flow_rate";
	public static final String PUMP_MAX_FLOW_RATE = "pump_max_flow_rate";
	public static final String PERM_MIN_FLOW_RATE = "perm_min_flow_rate";
	public static final String PERM_MAX_FLOW_RATE = "perm_max_flow_rate";
	public static final int KROSFLOFS15_PUMP_ID = 3;
	public static final int KROSFLOFS500_PUMP_ID = 4;

	public static final String REST_SWITCH_ROLE_API = "/users/switch/role/{role}";
	public static final String STAGE_ID = "stage_id";
	public static final String SUB_STAGE_ID = "sub_stage_id";
	public static final String NORMALIZED_WATER_PERMEABILITY = "nwp";
	public static final String REST_CHECK_15_MIN_LOCKOUT_PATH = "/lockout/{isSessionTimeout}";
	public static final String FILE_NAME = "name";
	public static final String FILE_LOCATION = "location";
	public static final String EMPTY = "Empty";
	public static final String FULL = "Full";

	public static final String PERCENTAGE = "percentage";

	// excel
	// for deployment
	//	public static final String INPUT_EXCEL_FILE_PATH = "./jre/TrialLogsFormat.xlsx";

	// for testing
	public static final String INPUT_EXCEL_FILE_PATH_AUTO = "./jre/TrialLogsFormatAutomode.xlsx";
	public static final String INPUT_EXCEL_FILE_PATH_MANUAL = "./jre/TrialLogsFormatManual.xlsx";
	public static final String INPUT_EXCEL_FILE_PATH_SETUP = "./jre/TrialLogsFormatSetupBreakdown.xlsx";
	public static final String EXCEL_DATE_FORMAT = "MMMddyyyyHHmmss";
	public static final String EXCEL_FILE_NAME_FORMAT = "\\{0}-{1}.xlsx";
	public static final String PDF_FILE_NAME_FORMAT = "\\{0}-{1}.pdf";
	//	public static final String EXCEL_DOWNLOAD_DIR_KFCOMM2C = "C:\\Repligen\\KF Comm 2C\\Downloads";
	//	public static final String EXCEL_DOWNLOAD_DIR_KFCOMM2 = "C:\\Repligen\\KF Comm 2\\Downloads";
	public static final String EXCEL_DOWNLOAD_DIR = "..\\Downloads";
	public static final String EXCEL_FILE_EXTENTION = ".xls";
	public static final String REST_ABV_PRESSURE_CONTROL_PATH = "/trial/run/abv";

	public static final String TUBE_CALIBRATE = "Pump Tubing Calibrated";
	public static final String END_POINT_NAME = "end_point_name";
	public static final String GET_CFR_STATUS_API = "/cfr";
	public static final String CFR_STATUS = "cfr_status";
	public static final String CHANGE_VACUUM_SUB_MODE = "Switch to ";
	public static final int ZERO = 0;

	// license
	public static final String EMAIL = "email";
	public static final String CORRUPT_BUILD_ERROR = "Build is corrupt, please reinstall the software.";
	public static final String ERROR_KEY = "error";
	public static final String SUCCESS_KEY = "success";
	//	public static final String ACTIVATE_LICENSE_REQUEST_PATH = "https://spectrum-api.hdmz.com/spectrum/customer/activateLicense";
	//	public static final String LICENSE_ACK_PATH = "https://spectrum-api.hdmz.com/spectrum/customer/ackLicense/{0}";
	public static final String ACTIVATE_LICENSE_REQUEST_PATH = "http://142.93.218.164:8092/spectrum/customer/activateLicense";
	public static final String LICENSE_ACK_PATH = "http://142.93.218.164:8092/spectrum/customer/ackLicense/{0}";
	public static final String LIC_ID = "licId";
	public static final String ACTIVE = "active";

	public static final int AVERAGE_PERMEATE_COUNT = 5;

	// we are using 100 as flow factor for the KR2i and KMPi pumps. It can varry for
	// different pumps
	public static final int FLOW_FACTOR = 100;
	public static final String GET_RUNNING_STATUS = "/isRun";
	public static final String IS_RUNNING = "isRunning";
	public static final String DATA = "data";
	public static final String IS_PAUSED = "isPaused";
	public static final String IS_EMERGENCY_VALVE_OPEN = "isEmergencyValveOpen";
	public static final String TRIAL_DISCONNECT_PAUSE = "Device disconnected.";
	public static final String FIRMWARE_UPDATE_PATH = "/spectrum/firmware";
	public static final String UPDATING_FIRMWARE = "Updating Firmware.";
	public static final String FIRMWARE_UP_TO_DATE = "Firmware up to date.";
	public static final String SOFTWARE_VERSION = "software_version";
	public static final String INTERFACE_VERSION = "interface_version";
	public static final String CHECK_FIRMWARE_VERSION_PATH = "/check";
	public static final String SPECTRUM_SOFTWARE_PATH = "/spectrum/software";
	public static final String SPECTRUM_QUIT_PATH = "/quit";
	public static final String SPECTRUM_SOFTWARE_SHUTDOWN_PATH = "/shutdown";
	public static final String USER_DIRECTORY = "user.dir";
	public static final String CLOSE_FRONT_END_FILE_PATH = "\\jre\\quit.bat";
	public static final String UPDATE_FIRMWARE_FILE_PATH = "\\jre\\updateFirmware.bat";
	public static final String CLOSE_BACKEND_FILE_PATH = "\\jre\\HardStop.bat";
	public static final String OPERATION_FAILED_MESSAGE = "A trial is running. Please wait until it stops.";
	public static final String SHUTDOWN_SOFTWARE_ACTION = "Shutdown Software";
	public static final String UPDATE_FIRMWARE_PATH = "/update";
	public static final String UPDATE_FIRMWARE_ACTION = "Firmware Update";
	public static final String STOP_AUX_PUMP_PATH = "/aux/{auxId}/{auxName}/{isOldConfig}";
	public static final String PARAM_AUX_ID = "auxId";
	public static final String AUX_PUMP_STOPPED_SUCCESS_MESSAGE = "Aux pump stopped successfully.";
	public static final String IS_UPDATED = "isUpdated";
	public static final String FIRMWARE_MISMATCH_ERROR_MESSAGE = "You are using an incompatible firmware version. Please update.";
	public static final String TRIAL_ABRUPT_PAUSE_ACTION = "Abrupt Pause";
	public static final String LIVE_DATA_DISCONNECT_EVENT = "disconnect";
	public static final String DISCONNECT_PAUSE = "disconnect_pause";
	public static final String IS_VALIDATION_FAILED = "isValidationFailed";

	public static final String USER_MANUAL_KFCOMM2_FOLDER_PATH = "./jre/UserManual/KFComm2/";
	public static final String USER_MANUAL_KFCOMM2C_FOLDER_PATH = "./jre/UserManual/KFComm2C/";
	public static final String ERROR_USER_MANUAL_NOT_FOUND = "User Manual not found, please contact admin.";
	public static final String PH_CHANNEL_1 = "ph_ch_1";
	public static final String PH_CHANNEL_2 = "ph_ch_2";
	public static final String IS_SUPER_ADMIN = "isSuperAdmin";
	public static final String PASSWORD_RESET_MESSAGE = "Please reset your password.";
	public static final String IS_CLEANING_PAUSE = "isCleaningPause";
	public static final String CLEANING_MESSAGE = "cleaningMessage";
	public static final String OPEN_RETENTATE_LINE_WHEN_PERMEATE_CLEANING_WITH_PERMEATE_PUMP = "1";
	public static final String PH_CH_1 = "ph_ch_1";
	public static final String PH_CH_2 = "ph_ch_2";
	public static final String TURBIDITY_CH_1 = "turbidity_ch_1";
	public static final String TURBIDITY_CH_2 = "turbidity_ch_2";
	public static final String DIAFILTRATION_VOL_2 = "diafiltration_vol_2";
	public static final String RUN = "Run";

	public static final String END_POINT_ID = "end_point_id";
	public static final String STEP_NUMBER = "step_no";
	public static final String TOTAL_END_POINT = "total_end_point";
	public static final String TRIAL_LOG_ID_EXCEPTION = "TrialLogId can't be 0.";
	public static final String TRIAL_RUN_SETTING_ID_EXCEPTION = "TrialRunSettingId can't be 0.";

	public static final String FLOW_RATE_CH_1 = "flow_rate_ch_1";
	public static final String FLOW_RATE_CH_2 = "flow_rate_ch_2";
	public static final String PC_CH_1 = "pc_ch_1";
	public static final String PC_CH_2 = "pc_ch_2";

	public static final String UPTO_ONE_DECIMAL_PLACES = "0.0";
	public static final String END_POINT_SETTING_KEY = "end_point_setting";
	public static final String TARGET_STEP_ID = "target_step_id";

	public static final String TARGET_STEP_SETTING_KEY = "target_step_setting";
	public static final String KONDUIT_CH_1 = "konduit_ch_1";
	public static final String KONDUIT_CH_2 = "konduit_ch_2";
	public static final int CHANNEL_1_UV_TYPE = 1;
	public static final int CHANNEL_2_UV_TYPE = 2;
	public static final int CHANNEL_1_PH_TYPE = 3;
	public static final int CHANNEL_2_PH_TYPE = 4;
	public static final int CHANNEL_1_TURBIDITY_TYPE = 5;
	public static final int CHANNEL_2_TURBIDITY_TYPE = 6;
	public static final int CHANNEL_1_PROTEIN_CONC_TYPE = 7;
	public static final int CHANNEL_2_PROTEIN_CONC_TYPE = 8;
	public static final int CHANNEL_1_TOTALIZER_TYPE = 9;
	public static final int CHANNEL_2_TOTALIZER_TYPE = 10;
	public static final String KONDUIT_CH_1_TYPE = "konduit_ch_1_type";
	public static final String KONDUIT_CH_2_TYPE = "konduit_ch_2_type";
	public static final String COLUMN_IS_PAUSED = "is_paused";
	public static final String NEW_END_POINT_CHANGE_VALUE = "new_end_point_value";
	public static final String TRIAL_DETAIL_UPDATED_SUCCESSFULLY_MESSAGE = "Trial details updated.";
	public static final String DD_MM_YY_HH_MM_SS = "dd/MM/yy HH:mm:ss";
	public static final String CATEGORIES = "categories";
	public static final String CATEGORY = "category";
	public static final String DATASET = "dataset";
	public static final String SERIES_NAME = "seriesname";
	public static final String GRAPH_SEPARATER = "|";
	public static final String TIMESTAMP_2 = "timestamp_2";
	public static final String PARAM_POSITION = "position";
	public static final String DETAILS = "details";
	public static final String OLD_END_POINT_CHANGE_VALUE = "old_end_point_value";
	public static final String END_POINT_ACTION_FORMAT = "Endpoint {0} {1}({2}) changed from {3} to {4}";
	public static final String END_POINT_ACTION_FORMAT_EMPTY = "Endpoint {0} {1}{2} changed from {3} to {4}";
	public static final String DELTA_P_ACTION_FORMAT = "Delta P changed from {0} {1} to {2} {3}";
	public static final String END_POINT_CHANGE_LOG_ID = "end_point_change_log_id";
	public static final String TARGET_STEP_CHANGE_LOG_ID = "target_step_change_log_id";
	public static final String OLD_TMP = "old_tmp";
	public static final String NEW_TMP = "new_tmp";
	public static final String TMP_CHANGE_LOG_FORMAT = " TMP({2}) changed from {0} to {1},";
	public static final String SET_POINT = "Setpoint ";
	public static final String TS_STEP_NO = "ts_step_no";
	public static final String OLD_DURATION = "old_duration";
	public static final String NEW_DURATION = "new_duration";
	public static final String DURATION_CHANGE_LOG_FORMAT = " Duration(min) changed from {0} to {1},";
	public static final String OLD_PERMEATE_WEIGHT = "old_permeate_weight";
	public static final String NEW_PERMEATE_WEIGHT = "new_permeate_weight";
	public static final String PERMEATE_WEIGHT_CHANGE_LOG_FORMAT = " Permeate Weight({2}) changed from {0} to {1},";
	public static final String PERMEATE_TOTAL_CHANGE_LOG_FORMAT = " Permeate Total({2}) changed from {0} to {1},";
	public static final String COLUMN_PERMEATE_WEIGHT = "permeate_weight";
	public static final String TARGET_STEP_CHANGE_LOG = "target_step_change_log";
	public static final String END_POINT_CHANGE_LOG = "end_point_change_log";
	public static final String FPI_HOLD_UP = "fpi_hold_up";
	public static final String FLOW_RATE_COUNT = "flow_rate_count";
	public static final String X_AXIS = "x";
	public static final String Y_AXIS = "y";
	public static final String DOWNLOADED_ON = "downloaded_on";
	public static final String DOWNLOADED_BY = "downloaded_by";
	public static final String VACUUME_MODE_STEP = "vacuum_mode_step";
	public static final String MAIN_PUMP_KEY = "main_pump";
	public static final String KF_KONDUIT_KEY = "kf_konduit";
	public static final String ABV_TARGET = "abv_target";
	public static final String TRIAL_START_MESSAGE = "Trial started.";
	public static final String KFCOMM2C_DB = "kfcomm2c.db";
	public static final String KFCOMM2_DB = "kfcomm2.db";
	public static final String REGEX_TO_REMOVE_NEGATIVE_ZERO = "^-(?=0(\\.0*)?$)";
	public static final String WEIGHT = "weight";
	public static final String PRESSURE = "pressure";
	public static final String WRONG_OLD_PASSWORD_MESSAGE = "Please input correct old password.";
	public static final String MAX_WORKBOOK_LIMIT_EXCEPTION = ". Please download with higher resolution.";
	public static final String NWP_VALVE_DONE_MESSAGE = "ABV pinching started, Ramp up will start after 5 seconds.";

	public static final String FILE_NAMES = "fileNames";
	public static final String HEADING = "heading";
	public static final String FILE = "file";
	public static final String VERSION_DETAILS = "versionDetails";

	public static final String IS_CONNECTING = "is_connecting";
	public static final String RECONNECTED = "reconnected";
	public static final String RECONNECTION_FAILED = "reconnection failed";
	public static final String TRIAL_AUTO_RESUME_ACTION = "Auto Resume";
	public static final int RECONNECT_ATTEMPT_COUNTER = 10;
	public static final String COLUMN_PERMEATE_TOTAL = "permeate_total";
	public static final String COLUMN_PERMEATE_TOTAL_WITH_HOLDUP = "permeate_total_with_holdup";
	public static final char COMMA = ',';
	public static final String OLD_PERMEATE_TOTAL = "old_permeate_total";
	public static final String NEW_PERMEATE_TOTAL = "new_permeate_total";
	public static final String UNIT_ML = "mL";
	public static final String UNIT_LITRE = "L";
	public static final String DUMMY_DATA_PATH = "/{trialMasterId}/{trialRunSettingId}/{records}";
	public static final String PARAM_RECORDS = "records";

	public static final String PERMEATE_TOTAL_ENDPOINT = "Permeate Total";
	public static final String PROTEIN_CONCENTRATION = "Protein Concentration";
	public static final String END_POINT_CAL = "endpoint_cal";
	public static final String PERMEATE_TOTAL_KEY = "permeate_total";
	public static final String KF_KONDUIT_MISSING_ERROR_MESSAGE = "Konduit missing";
	public static final String KONDUIT_NOT_DEFINE_ERROR_MESSAGE = "Konduit not define";

	public static final String TOTALIZER_CH_1 = "Totalizer Ch 1";
	public static final String TOTALIZER_CH_2 = "Totalizer Ch 2";
	public static final String AU = "AU";
	public static final String NTU = "NTU";
	public static final String X = "X";
	public static final String MG_ML = "mg/mL";
	public static final String ALARM_DESCRIPTION_PERMEATE_TOTAL = "Permeate Total";
	public static final String TOTALIZER = "Totalizer";
	public static final String PROTEIN_CONCENTRATION_DEVICE_TEXT = "PC";
	public static final String PERMEATE_TOTAL_DEVICE_TEXT = "Perm Total";
	public static final String TOTALIZER_DEVICE_TEXT = "Flow M";
	public static final String TURBIDITY_DEVICE_TEXT = "Turb";
	public static final String UNIT_FOR_PROTEIN_CONCENTRATION= "mg/mL";
	public static final String UNIT_FOR_TURBIDITY= "NTU";
	public static final String UNIT_FOR_UV= "AU";
	public static final String PERMEATE_TOTAL_WITH_HOLDUP = "permeate_total_with_holdup";

	/**
	 * Alarm frontend message
	 */
	public static final String FEED_PRESSURE_ALARM_HIGH_STOP_TOAST_MESSAGE = "Feed Pressure High Stop";
	public static final String FEED_PRESSURE_ALARM_HIGH_ALARM_TOAST_MESSAGE = "Feed Pressure High";
	public static final String FEED_PRESSURE_ALARM_LOW_ALARM_TOAST_MESSAGE = "Feed Pressure Low";
	public static final String FEED_PRESSURE_ALARM_LOW_STOP_TOAST_MESSAGE = "Feed Pressure Low Stop";
	public static final String PERMEATE_PRESSURE_ALARM_HIGH_STOP_TOAST_MESSAGE = "Permeate Pressure High Stop";
	public static final String PERMEATE_PRESSURE_ALARM_HIGH_ALARM_TOAST_MESSAGE = "Permeate Pressure High";
	public static final String PERMEATE_PRESSURE_ALARM_LOW_ALARM_TOAST_MESSAGE = "Permeate Pressure Low";
	public static final String PERMEATE_PRESSURE_ALARM_LOW_STOP_TOAST_MESSAGE = "Permeate Pressure Low Stop";
	public static final String FEED_WEIGHT_ALARM_HIGH_STOP_TOAST_MESSAGE = "Feed Weight High Stop";
	public static final String FEED_WEIGHT_ALARM_HIGH_ALARM_TOAST_MESSAGE = "Feed Weight High";
	public static final String FEED_WEIGHT_ALARM_LOW_ALARM_TOAST_MESSAGE = "Feed Weight Low";
	public static final String FEED_WEIGHT_ALARM_LOW_STOP_TOAST_MESSAGE = "Feed Weight Low Stop";
	public static final String PERMEATE_WEIGHT_ALARM_HIGH_STOP_TOAST_MESSAGE = "Permeate Weight High Stop";
	public static final String PERMEATE_WEIGHT_ALARM_HIGH_ALARM_TOAST_MESSAGE = "Permeate Weight High";
	public static final String PERMEATE_WEIGHT_ALARM_LOW_ALARM_TOAST_MESSAGE = "Permeate Weight Low";
	public static final String PERMEATE_WEIGHT_ALARM_LOW_STOP_TOAST_MESSAGE = "Permeate Weight Low Stop";
	public static final String KONDUIT_CH2_ALARM_HIGH_STOP_TOAST_MESSAGE = " Ch 2 High Stop";
	public static final String KONDUIT_CH2_ALARM_HIGH_ALARM_TOAST_MESSAGE = " Ch 2 High";
	public static final String KONDUIT_CH1_ALARM_HIGH_ALARM_TOAST_MESSAGE = " Ch 1 High";
	public static final String KONDUIT_CH1_ALARM_HIGH_STOP_TOAST_MESSAGE = " Ch 1 High Stop";
	public static final String FLOW_METER_CH1_ALARM_HIGH_ALARM_TOAST_MESSAGE = "Permeate Total High";
	public static final String FLOW_METER_CH1_ALARM_HIGH_STOP_TOAST_MESSAGE = "Permeate Total High Stop";
	public static final String FLOW_METER_CH2_ALARM_HIGH_ALARM_TOAST_MESSAGE = "Permeate Total High";
	public static final String FLOW_METER_CH2_ALARM_HIGH_STOP_TOAST_MESSAGE = "Permeate Total High Stop";
	public static final String FLOW_METER = "Flow Meter";
	public static final String RECONNECT = "reconnect";

	// Manual mode new config constants
	public static final String MANUAL_TUBING_SIZE = "tubingSize";
	public static final String MANUAL_ABV_TYPE = "abvType";
	public static final String MANUAL_ABV_TARGET = "abvTarget";
	public static final String MANUAL_ABV_MODE = "abvMode";
	public static final String MANUAL_OP_PRESSURE = "operatingPressure";
	public static final String MANUAL_AUX_PUMP_TYPE = "auxPumpType";
	public static final String MANUAL_FLOW_RATE = "flowRate";
	public static final String PARAM_IS_OLD_CONFIG = "isOldConfig";
	public static final String FAILED_TRIAL_DETAILS_DELETED_MESSAGE = "Failed trial details config deleted";
	public static final String UNIT_FOR_CONDUCTIVITY = "mS/cm";
	public static final String ERROR_LAST_TRIAL_RUN_SETTING_NOT_FOUND = "Old trial configuration does not exists.";
	public static final String RECIPE_ACTIVE_STATUS = "Y";
	public static final String RECIPE_INACTIVE_STATUS = "N";
	public static final String REST_TABLE_PATH = "/spectrum/table";
	public static final String REST_TABLE_HEADER_PATH = "/header";
	public static final long AUTO_GENERATED_LOG_EXPIRY_DAY = 28;
	public static final String AUTO_GENERATED_LOG_PATH = "./log";
	//----------------
	public static final String ERROR_DOWNLOAD_FILE = "Error while downloading file. Please try again!";
	public static final String FLAG_INACTIVE = "Inactive";
	public static final String ERROR_DUPLICATE_USER_NAME = "Duplicate username found.";
	public static final String ERROR_DUPLICATE_EMAIL = "Duplicate email ID found.";
	public static final String LIC_TYPE = "licType";

	// DB constants
	public static final String TEMPORARY_DB_URL = "db.temp.url";
	public static final String DEFAULT_DB_URL = "db.url";
	public static final String DEFAULT_DB_NAME = "db.name";
	public static final String TEMPORARY_DB_NAME = "db.temp.name";
	public static final String ENCRYPTION_ACTIVE = "1";
	public static final String ENCRYPTION_NOT_ACTIVE = "0";
	public static final String DB_ENCRYPT_STATUS = "encrypt.db.status";
	public static final String ENCRYPT_PROPERTIES_FILE_LOCATION = "encrypt.properties.location";
	public static final String JRE_FOLDER_LOCATION = "jre.folder.location";
	public static final String ERROR_RENAME_FILE = "Error in renaming file.";
	public static final String ERROR_ENCRYPTION_PROCESS_FAILED = "Database encryption process failed";
	public static final String DB_ENCRYPTION_KEY = "encrypt.key";
	public static final String ERROR_ENCRYPT_PROPERTIES_NOT_FOUND = "File 'encrypt.properties' not found";
	public static final String PLAIN_DB_URL = "plain.db.url";

	//Registry hash key
	public static final String REGISTRY_HASH_KEY = "registry.hash.key";
	public static final String SESSION_TIMEOUT_KEY = "isSessionTimeout";

	// excel
	public static final String ERROR_EXCEL_TEMPLATE_NOT_FOUND = "Input excel template does not exists, please check.";
	public static final String UNITS = "Units";
	public static final String DOUBLE_HYPHEN = "--";
	public static final String FONT_ARIAL = "Arial";
	public static final short FONT_SIZE = 10;
	public static final String UNIT_FOR_VT = "L/m^2";
	public static final String CELSIUS = "C";
	public static final String UNIT_FOR_FLUX = "LMH";
	public static final String UNIT_FOR_SHEAR = "s^-1";
	public static final String UNIT_FOR_CROSS_FLOW_FLUX = "L/m^2/min";
	public static final String UNIT_FOR_WATER_PERMEABILITY = "LMH/psi";
	public static final String UNIT_FOR_NWP = "LMH/psi@20°C";
	public static final String NOTES_DESC_ID = "notes_desc_id";
	public static final String NOTES_DESC_SETUP_ID = "notes_desc_setup_id";
	public static final String SYSTEM_AUTO_LOG = "System (Auto log)";
	public static final String NOTES_ACTION_FORMAT = "{0} mode {1} {2}, {3} {4} {5}";
	public static final String NOTES_SETUP_CLEANING_MODE_ACTION_FORMAT = "{0} step {1} {2} at {3} {4} {5}";
	public static final String NOTES_SETUP_CLEANING_DURATION_ACTION_FORMAT = "{0} step {1} {2} at {3}";
	public static final String NOTES_SETUP_FLUSHING_MODE_ACTION_FORMAT = "{0} {1} at {2} {3} {4}";
	public static final String NOTES_SETUP_DURATION_ACTION_FORMAT = "{0} {1} at {2}";
	public static final String NOTES_SETUP_FLUSHING_DURATION_ACTION_FORMAT = "{0} {1} at {2}";
	public static final String NOTES_SETUP_NWP_MODE_ACTION_FORMAT = "{0} step {1} {2} at {3} {4} {5} at {6}";
	public static final String NOTES_SETUP_FLUX_MODE_ACTION_FORMAT = "{0} step {1} {2} at {3} {4} {5} at {6} {7} {8} at {9}";
	public static final String SUB_MODE_NAME = "sub_mode_name";
	public static final String MODE = "mode";
	public static final String STARTED = "started";
	public static final String ENDED = "ended";
	public static final String MANUAL_MODE = "Manual Mode";
	public static final String UNITS_DESCRIPTION = "Units / description";
	public static final String MINUTE_UNIT = "min";
	public static final String TMP_DISPLAY_NAME = "TMP";

	// notes
	public static final int NOTES_DESC_START_STATUS = 1 ;
	public static final int NOTES_DESC_END_STATUS = 0 ;
	public static final String DIGITAL_NOTES = "digital_notes";

	public static final String COLON = ":";
	public static final String DURATION_TIME_FORMAT = "00";

	// git commit
	public static final String COMMIT = "commit";
	public static final String Q_PERMEATE_FREQUENCY = "q_perm_freq";
	public static final String IS_FEED_SCALE_OVERRIDE = "feed_scale_override";
	public static final String FEED_ABV_CONTROL_BASED = "Feed";
	public static final String Q_PERMEATE_FREQUENCY_ACTION = "Sets Q permeate frequency ";
	public static final String MINUTE = "minute";
	public static final String SECOND = "second";
	public static final String LOG_TYPE = "log_type";

	public static final String FLOW_RATE_DISPLAY_NAME = "Flowrate";

	// manual notes desc
	public static final String NOTES_DESC_MANUAL_ID = "mn_id";
	public static final String DEVICE = "mn_device";
	public static final String NOTES_MANUAL_ACTION_FORMAT = "{0} {1} changed from {2} to {3} {4}";
	public static final String NOTES_MANUAL_VALVE_2_ACTION_FORMAT = "{0} {1} connector set to {2}";
	public static final String OLD_VALUE = "mn_old_value";
	public static final String NEW_VALUE = "mn_new_value";
	public static final String OPERATING_PRESSURE = "operating pressure";
	public static final String NOTES_TYPE_FLOW_RATE = "flow rate";
	public static final String NOTES_TYPE_TUBING_SIZE = "tubing size";
	public static final String MANUAL_NOTES_TYPE = "mn_type";
	public static final String NOTES_DESC_STEP = "nd_step";

	public static final String FILTER_MODEL_NAME = "model";
	public static final String FILTER_MANUFACTURER_NAME = "manufacturer";
	public static final String DELTA_P_RATE = "delta_p_rate";
	public static final String DELTA_P_DURATION = "delta_p_duration";
	public static final String PUMP_RPM = "pump_rpm";
	public static final String IS_SPEED = "is_speed";
	public static final String NEW_DELTA_P = "new_delta_p";
	public static final String NEW_DELTA_P_RATE = "new_delta_p_rate";
	public static final String NEW_DELTA_P_DURATION = "new_delta_p_duration";
	public static final String OLD_DELTA_P = "old_delta_p";
	public static final String OLD_DELTA_P_RATE = "old_delta_p_rate";
	public static final String OLD_DELTA_P_DURATION = "old_delta_p_duration";
	public static final String DELTA_P_ABSOLUTE_NAME = "Delta P";
	public static final String SETUP_STATUS = "setup_status";
	public static final String UNABLE_TO_MAINTAIN_DELTA_PRESSURE = "Unable to maintain delta pressure.";
	public static final double KROSFLO_FS15_CONSTANT = 2.99;
	public static final double KROSFLO_FS500_CONSTANT = 62.8;
	public static final double KROSFLO_FS15_SLOPE = 1.02;
	public static final double KROSFLO_FS500_SLOPE = 8.83;
	public static final String ABV_ERROR_MESSAGE = "Unable to perform this operation.";
	public static final String TYPE_ABV_ERROR = "abvError";

	public static final String VALVE_2_CONNECTOR_COLUMN = "valve_2_connector";
	public static final String OLD_VALVE_2_CONNECTOR = "old_valve_2_connector";
	public static final String VALVE_2_IS_NOTHING = "Nothing";
	public static final String VALVE_2_IS_KONDUIT = "Konduit";
	public static final String VALVE_2_IS_VALVE = "Valve";
	public static final String VALVE_CONNECTOR_KEY = "Valve";
	public static final String IS_NON_REPLIGEN = "is_non_repligen";
	public static final String COLUMN_ECS = "ECS";
	public static final String TOTAL_SURF_AREA = "total_surface_area";
	public static final String AUX_PUMP_FLOW_RATE_CHANGE_ACTION = "Permeate Pump Flow Rate Changed.";
	public static final String CONNECT_EXCEPTION_MEASSGE = "Connection timed out: unable to connect to server. Please check your connection and try again.";
	public static final String REST_DEVICE_SETTING_PATH = ApiConstant.REST_ROOT_ENTRY_POINT + "/device";
	public static final String REST_VALVE_CONFIGURATION_PATH = "/valve/config/{type}";
	public static final String INVALID_CONFIGURATION_ID_ERROR_MESSAGE = "Invalid Configuration Id.";
	public static final String VALVE_2_CONFIGURATION = "valve_2_config";

	// Issue #1946 30 Sec data collection
	public static final String START_FINAL_COLLECTION_MESSAGE = "Final collection started";
	public static final String DONE_FINAL_COLLECTION_MESSAGE = "Final collection completed";
	public static final String FINAL_COLLECTION_TIME_MESSAGE = "Collecting final data";
	public static final String TIMER_KEY = "timer";
	
	// Tubing details
	public static final String TUBE_NAME = "tubing_size";
	public static final String TUBE_ID_IN = "internal_diameter_in";
	public static final String TUBE_ID_MM = "internal_diameter_mm";
	public static final String TUBE_RPM_MIN_MAX = "min_max_rpm";
	public static final String TUBE_FLOW_RATE = "flow_rate";
	public static final String TUBE_MAIN_PUMP = "main_pump";
	public static final String TUBE_AUX_PUMP = "aux_pump";
	public static final String TUBE_ABV_TYPE = "abv";

	public static final String DIRECTORY_NOT_FOUND = "Download directory does not exists. Please check.";

}