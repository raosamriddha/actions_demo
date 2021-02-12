package org.lattice.spectrum_backend_final.dao.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeError;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeMsg;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.FlowControlMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;

/**
 * @author RAHUL KUMAR MAURYA
 */

/**
 * This class provides basic utilities for different purposes.
 */
public class BasicUtility {

	/**
	 * gets the current timestamp.
	 *
	 * @return Current date and time.
	 */

	public static BasicUtility basicUtility;

	private static int auxPumpTypeId1;

	private static int[] auxPumpTypeId = new int[2];
	// private static String auxPumpName = "";
	private static boolean isAuxPumpIdSet = false;
	private static boolean isAuxPumpNameSet = false;

	public final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(ApiConstant.MMM_D_YYYY_HH_MM_SS);
	public final static SimpleDateFormat GRAPH_DATE_FORMAT = new SimpleDateFormat(ApiConstant.DD_MM_YY_HH_MM_SS);

	private static String[] auxPumpName = new String[2];


	public static BasicUtility getInstance() {

		synchronized (BasicUtility.class) {
			if (basicUtility == null) {
				basicUtility = new BasicUtility();
			}
		}

		return basicUtility;
	}



	public String getCurrentTimestamp() {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ApiConstant.MMM_D_YYYY_HH_MM_SS, Locale.ENGLISH);
		LocalDateTime now = LocalDateTime.now();
		return dateTimeFormatter.format(now);

	}

	public static int getMinutesFromDate(String date) {

		String[] dateTime = date.split(" ");

		String[] time = dateTime[3].split(":");

		float minute = (Integer.parseInt(time[0]) * 60) + (Integer.parseInt(time[1]))
				+ (Integer.parseInt(time[2]) / 60);

		return Integer.parseInt(dateTime[1]);
	}

	/**
	 * Get id related to particular role.
	 *
	 * @param techRole Takes role as input.
	 * @return Id related to particular role.
	 */
	public static int getTechRoleId(int techRole) {
		int techRoleId;
		switch (techRole) {
		case 4:
			techRoleId = 3;
			break;
		case 5:
			techRoleId = 4;
			break;
		default:
			techRoleId = 0;

		}
		return techRoleId;
	}

	/**
	 * Get role id for a particular user type.
	 *
	 * @param
	 * @return
	 */
	public static int getRoleId(String userType) {
		if (userType.equals(ApiConstant.ADMIN)) {
			return 1;
		} else if (userType.equals(ApiConstant.AUDITOR)) {
			return 2;
		} else if (userType.equals(ApiConstant.MANAGER)) {
			return 3;
		} else if (userType.equals(ApiConstant.TECHNICIAN)) {
			return 4;
		}
		return 0;

	}

	/**
	 * Get the max priority role from roles ResultSet.
	 *
	 * @param username Username of user.
	 * @return Max priority role else null if no roles are present.
	 * @throws SQLException When there is any database error.
	 */
	public static ArrayList<String> getUserRole(String username) throws SQLException {
		String userRole = null;
		PreparedStatement getUserRolePS = null;
		ResultSet getUserRoleRS = null;
		ArrayList<String> roles = new ArrayList<>();

		Connection conn = DbConnectionManager.getInstance().getConnection();

		try {

			getUserRolePS = conn.prepareStatement(DbConstant.GET_USER_ROLE_FROM_USERNAME_QUERY);
			getUserRolePS.setString(1, username);

			getUserRoleRS = getUserRolePS.executeQuery();

			while (getUserRoleRS.next()) {
				roles.add(getUserRoleRS.getString(ApiConstant.ROLE_DESCRIPTION));
			}

		} catch (SQLException e) {
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getUserRoleRS, getUserRolePS, conn);
		}

		return roles;

	}

	/**
	 * Get the max priority role from roles json array.
	 *
	 * @param roleArray JSON array of roles.
	 * @return Max priority role else null if no roles are present.
	 * @throws JSONException When there is any JSON Exception.
	 */
	public static String getUserRole(JSONArray roleArray) throws JSONException {
		String userRole = null;

		if (roleArray.length() != 0) {
			userRole = roleArray.getJSONObject(0).getString(ApiConstant.ROLE_DESCRIPTION);

			if ((roleArray.length() == 2) && (getUserPriority(
					roleArray.getJSONObject(1).getString(ApiConstant.ROLE_DESCRIPTION)) < getUserPriority(userRole))) {
				userRole = roleArray.getJSONObject(1).getString(ApiConstant.ROLE_DESCRIPTION);
			}
		}

		return userRole;

	}

	/**
	 * Gets priority related to a particular user type.
	 *
	 * @param userType Takes user type.
	 * @return A numeric value as priority. Lesser is the value greater is the
	 * priority.
	 */
	public static int getUserPriority(String userType) {
		BasicUtility.systemPrint("usertype", userType);
		if (userType.equals(ApiConstant.SUPER_ADMIN)) {
			return 0;
		} else if (userType.equals(ApiConstant.ADMIN)) {
			return 1;
		} else if (userType.equals(ApiConstant.AUDITOR)) {
			return 2;
		} else if (userType.equals(ApiConstant.MANAGER)) {
			return 3;
		} else if (userType.equals(ApiConstant.TECHNICIAN)) {
			return 4;
		}

		return -1;
	}

	public static String getMacAddress() {
		InetAddress ip;
		StringBuilder stringBuilder = new StringBuilder();
		try {

			ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			for (int i = 0; i < mac.length; i++) {
				stringBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (SocketException e) {

			e.printStackTrace();

		}

		return stringBuilder.toString();
	}


	/**
	 * Generate random name of given size.
	 *
	 * @param count
	 * @return
	 */
	public static String generateRandomName(int count) {
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	public static Object stringToEnumConversion(String input) {

		switch (input) {

		// Mode Conversion

		case ApiConstant.C_MODE:
			return OperationMode.C_MODE;
		case ApiConstant.D_MODE:
			return OperationMode.D_MODE;
		case ApiConstant.CD_MODE:
			return OperationMode.CD_MODE;
		case ApiConstant.CFC_MODE:
			return OperationMode.CF_MODE;
		case ApiConstant.CDDC_MODE:
			return OperationMode.CDDC_MODE;
		case ApiConstant.CDCD_MODE:
			return OperationMode.CDCD_MODE;
		case ApiConstant.CFDC_MODE:
			return OperationMode.CFDC_MODE;
		case ApiConstant.CDC_MODE:
			return OperationMode.CDC_MODE;
		case ApiConstant.HARVEST_MODE:
			return OperationMode.HARVEST_MODE;
		case ApiConstant.VACUUM_MODE:
			return OperationMode.VACUUM_MODE;
		case ApiConstant.FLUX_CV:
			return OperationMode.FluxCV;
		case ApiConstant.FLUX_C:
			return OperationMode.FluxCF;
		case ApiConstant.NWP:
			return OperationMode.NWP;
		case ApiConstant.FLUSHING:
			return OperationMode.Flushing;
		case ApiConstant.CLEANING:
			return OperationMode.Cleaning;

			// Flow Conversion

		case ApiConstant.FEED:
			return FlowControlMode.FEED;
		case ApiConstant.RETENTATE:
			return FlowControlMode.RETENTATE;

			// Valve Control conversion
		case ApiConstant.TMP:
		case ApiConstant.TMP_ABV_CONTROL_BASED:
			return PressureTarget.TMP;
		case ApiConstant.PERMEATE_ABV_CONTROL_BASED:
			return PressureTarget.PERMEATE;
		case ApiConstant.RETENTATE_ABV_CONTROL_BASED:
			return PressureTarget.RETENTATE;
		case ApiConstant.FEED_ABV_CONTROL_BASED:
			return PressureTarget.FEED;

			// End points
		case ApiConstant.CONCENTRATION_FACTOR:
			return EndPointTarget.ConcentrationFactor;
		case ApiConstant.DIAFILTRATION_VOLUME:
			return EndPointTarget.DiafiltrationVolume;
		case ApiConstant.PERMEATE_WEIGHT:
		case ApiConstant.PERM_WT:
			return EndPointTarget.PermeateWeight;
		case ApiConstant.CONDUCTIVITY:
			return EndPointTarget.Conductivity;
		case ApiConstant.UV:
			return EndPointTarget.UV;
		case ApiConstant.DURATION:
			return EndPointTarget.Duration;
		case ApiConstant.TURBIDITY:
			return EndPointTarget.Turbidity;
		case ApiConstant.PH:
			return EndPointTarget.pH;
		case ApiConstant.PROTEIN_CONCENTRATION:
			return EndPointTarget.Protein;
		case ApiConstant.PERMEATE_TOTAL_ENDPOINT:
		case ApiConstant.PERMEATE_TOTAL_KEY:
			return EndPointTarget.PermeateVolume;
		default:
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

		}

	}

	public static void systemPrint(Object num, Object value) {
		Logger.info(num + " : " + value);
	}

	public static void systemPrint(String value) {
		Logger.info(value);
	}

	public static double speedToFlowRate(String pumpName, double speed, String tubeSize, double speedFactor) {

		double flowConversionNumber;
		double flowRate = 0;

		try {

			flowConversionNumber = getTubeFlowConversion(pumpName, tubeSize);


			flowRate = ((flowConversionNumber * speed) / speedFactor);

			flowRate = BasicUtility.getInstance()
					.getConvertedFlowRate(flowRate, pumpName, false);

			if(BasicUtility.getInstance().isMainPump(pumpName)){
				flowRate *= BasicUtility.getInstance().getCalibrationFactor(tubeSize, pumpName);
			}

			BasicUtility.systemPrint(
					"speedToFlowRate : flowConversionNumber : " + flowConversionNumber + " : speed : " + speed,
					" : flowrate : " + flowRate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		BasicUtility.systemPrint("speedToFlowRate : flowrate : ", flowRate);
		return flowRate;

	}

	public static double getTubeFlowConversion(String pumpName, String tubingSize) {

		PreparedStatement getFlowConversionPS;
		ResultSet getFlowConversionRS;
		double flowConversion = 0;

		try (Connection conn = DbConnectionManager.getInstance().getConnection()

				) {
			if (pumpName.equalsIgnoreCase(ApiConstant.KRJR)) {
				getFlowConversionPS = conn.prepareStatement(DbConstant.GET_KRJR_FLOW_CONVERSION_NUMBER_FROM_TUBE_SIZE);
				getFlowConversionPS.setString(1, tubingSize);

				getFlowConversionRS = getFlowConversionPS.executeQuery();

				if (getFlowConversionRS.next()) {
					flowConversion = getFlowConversionRS.getDouble(ApiConstant.FLOW_CONVERSION_KRJR);
				}
			} else if (ApiConstant.KROSFLOFS15.equalsIgnoreCase(pumpName)) {

				flowConversion = ApiConstant.KROSFLO_FS15_SLOPE;

			} else if (ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)) {

				flowConversion = ApiConstant.KROSFLO_FS500_SLOPE;
			} else {
				getFlowConversionPS = conn.prepareStatement(DbConstant.GET_FLOW_CONVERSION_NUMBER_FROM_TUBE_SIZE);
				getFlowConversionPS.setString(1, tubingSize);

				getFlowConversionRS = getFlowConversionPS.executeQuery();

				if (getFlowConversionRS.next()) {
					flowConversion = getFlowConversionRS.getDouble(ApiConstant.FLOW_CONVERSION);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flowConversion;
	}

	public static double speedFactor(String pumpName, String direction,
			int pumpHeadCount/* , boolean isSetToComLib */) {

		double speedFactor;

		if (ApiConstant.KR2I.equalsIgnoreCase(pumpName)) {

			if (ApiConstant.CLOCKWISE.equalsIgnoreCase(direction)) {
				speedFactor = (36d / pumpHeadCount);
			} else {
				speedFactor = (-36d / pumpHeadCount);
			}

		} else if (ApiConstant.KMPI.equalsIgnoreCase(pumpName)) {

			if (ApiConstant.CLOCKWISE.equalsIgnoreCase(direction)) {
				speedFactor = (-32 * 1024d / pumpHeadCount) /* / 2.88 */;
			} else {
				speedFactor = (32 * 1024d / pumpHeadCount) /* / 2.88 */;
			}

		} else if (ApiConstant.KROSFLOFS15.equalsIgnoreCase(pumpName)) {

			speedFactor = 6;

		} else if (ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)) {

			speedFactor = 6;

		} else {

			if (ApiConstant.CLOCKWISE.equalsIgnoreCase(direction)) {

				speedFactor = 1;

			} else {

				speedFactor = -1;

			}

		}

		//        if (
		//                ApiConstant.KR2I.equalsIgnoreCase(pumpName) ||
		//                        ApiConstant.KRJR.equalsIgnoreCase(pumpName) ||
		//                        ApiConstant.KR1.equalsIgnoreCase(pumpName)
		//        ) {
		//
		//            if (isSetToComLib) {
		//                speedFactor /= 100;
		//            } else {
		//                speedFactor *= 100;
		//            }
		//
		//        } else if (ApiConstant.IP.equalsIgnoreCase(pumpName)) {
		//            if (isSetToComLib) {
		//                speedFactor = speedFactor /32678*28.8;
		//            } else {
		//                speedFactor = speedFactor *32678/28.8;
		//            }
		//        }

		return speedFactor;

	}

	public static JSONObject getPumpMinMaxRpm(String pumpName) {

		PreparedStatement getPumpMaxRpmPS;
		ResultSet getPumpMaxRpmRS;
		JSONObject pumpMinMaxRpm = new JSONObject();

		try (Connection conn = DbConnectionManager.getInstance().getConnection()

				) {
			getPumpMaxRpmPS = conn.prepareStatement(DbConstant.GET_PUMP_MIN_MAX_RPM);
			getPumpMaxRpmPS.setString(1, pumpName);

			getPumpMaxRpmRS = getPumpMaxRpmPS.executeQuery();

			if (getPumpMaxRpmRS.next()) {
				pumpMinMaxRpm.put(ApiConstant.MIN_SPEED,
						ApiConstant.BLANK_QUOTE + getPumpMaxRpmRS.getInt(ApiConstant.MIN_SPEED));
				pumpMinMaxRpm.put(ApiConstant.MAX_SPEED,
						ApiConstant.BLANK_QUOTE + getPumpMaxRpmRS.getInt(ApiConstant.MAX_SPEED));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pumpMinMaxRpm;

	}

	public static int getAuxTypeId(String auxPumpName) {

		//        ComLib.get().getAuxPumpList().requestList(0);

		switch (auxPumpName) {

		case ApiConstant.KRJR:
			return 1;

		case ApiConstant.KR1:
			return 2;

		case ApiConstant.IP:
			return 3;

		default:
			return 0;
		}

	}

	public static int getInterval(String timeUnit) {

		switch (timeUnit) {

		case ApiConstant.PER_MINUTE:
			return 60;

		case ApiConstant.PER_SECOND:
			return 1;
		default:
			return 0;

		}

	}

	public static int getRpmFromDegreePerSecond(String pumpName, double speed) {

		double speedInRpm;

		if (pumpName.equals(ApiConstant.KR2I)) {

			speedInRpm = speed / 36;

		} else if (pumpName.equals(ApiConstant.KMPI)) {
			speedInRpm = speed / 28.8;
		} else {
			speedInRpm = speed / 6;
		}

		return (int) Math.round(speedInRpm);

	}

	public static int getDegreePerSecondFromRpm(String pumpName, double speed) {

		double speedInDegree;

		if (pumpName.equals(ApiConstant.KR2I)) {

			speedInDegree = speed * 36;

		} else if (pumpName.equals(ApiConstant.KMPI)) {
			speedInDegree = speed * 28.8;
		} else {
			speedInDegree = speed * 6;
		}

		return (int) speedInDegree;

	}

	public static String enumToStringConversion(Object enumString) {

		if (enumString instanceof ModeEvent) {

			switch ((ModeEvent) enumString) {

			case PERMEATE_SCALE_TARE_STARTED:
				return ApiConstant.PERMEATE_SCALE_TARE_STARTED;

			case PERMEATE_SCALE_TARE_COMPLETED:
				return ApiConstant.PERMEATE_SCALE_TARE_COMPLETED;

			case PERMEATE_SCALE_TARE_TIMEOUT:
				return ApiConstant.PERMEATE_SCALE_TARE_TIMEOUT;

			case SUB_MODE_STARTED:
				return ApiConstant.SUB_MODE_STARTED;

			case SUB_MODE_FINISHED:
				return ApiConstant.SUB_MODE_FINISHED;

			case SUPER_MODE_FINISHED:
				return ApiConstant.SUPER_MODE_FINISHED;

			case ENDPOINT_REACHED:
				return ApiConstant.ENDPOINT_REACHED;

			case RAMP_UP_DONE:
				return ApiConstant.RAMP_UP_DONE;

			case ERROR:
				return ApiConstant.RUN_ERROR;

			case RESUME:
				return ApiConstant.TRIAL_RESUME_MESSAGE;

			case PAUSE:
				return ApiConstant.TRIAL_PAUSED_MESSAGE;

			case DISCONNECT_PAUSE:
				return ApiConstant.TRIAL_DISCONNECT_PAUSE;

			case STOP:
				return ApiConstant.TRIAL_STOPPED_MESSAGE;

			case FEED_TO_EMPTY_DETECTED:
				return ApiConstant.FEED_TO_EMPTY_DETECTED;

			case NWP_VALVE_DONE:
				return ApiConstant.NWP_VALVE_DONE_MESSAGE;


				/**
				 * Alarm Frontend toast
				 */
			case FEED_PRESSURE_ALARM_HIGH_STOP:
				return ApiConstant.FEED_PRESSURE_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case FEED_PRESSURE_ALARM_HIGH_ALARM:
				return ApiConstant.FEED_PRESSURE_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case FEED_PRESSURE_ALARM_LOW_ALARM:
				return ApiConstant.FEED_PRESSURE_ALARM_LOW_ALARM_TOAST_MESSAGE;

			case FEED_PRESSURE_ALARM_LOW_STOP:
				return ApiConstant.FEED_PRESSURE_ALARM_LOW_STOP_TOAST_MESSAGE;

			case PERMEATE_PRESSURE_ALARM_HIGH_STOP:
				return ApiConstant.PERMEATE_PRESSURE_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case PERMEATE_PRESSURE_ALARM_HIGH_ALARM:
				return ApiConstant.PERMEATE_PRESSURE_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case PERMEATE_PRESSURE_ALARM_LOW_ALARM:
				return ApiConstant.PERMEATE_PRESSURE_ALARM_LOW_ALARM_TOAST_MESSAGE;

			case PERMEATE_PRESSURE_ALARM_LOW_STOP:
				return ApiConstant.PERMEATE_PRESSURE_ALARM_LOW_STOP_TOAST_MESSAGE;

			case FEED_WEIGHT_ALARM_HIGH_STOP:
				return ApiConstant.FEED_WEIGHT_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case FEED_WEIGHT_ALARM_HIGH_ALARM:
				return ApiConstant.FEED_WEIGHT_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case FEED_WEIGHT_ALARM_LOW_ALARM:
				return ApiConstant.FEED_WEIGHT_ALARM_LOW_ALARM_TOAST_MESSAGE;

			case FEED_WEIGHT_ALARM_LOW_STOP:
				return ApiConstant.FEED_WEIGHT_ALARM_LOW_STOP_TOAST_MESSAGE;

			case PERMEATE_WEIGHT_ALARM_HIGH_STOP:
				return ApiConstant.PERMEATE_WEIGHT_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case PERMEATE_WEIGHT_ALARM_HIGH_ALARM:
				return ApiConstant.PERMEATE_WEIGHT_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case PERMEATE_WEIGHT_ALARM_LOW_ALARM:
				return ApiConstant.PERMEATE_WEIGHT_ALARM_LOW_ALARM_TOAST_MESSAGE;

			case PERMEATE_WEIGHT_ALARM_LOW_STOP:
				return ApiConstant.PERMEATE_WEIGHT_ALARM_LOW_STOP_TOAST_MESSAGE;

			case KONDUIT2_ALARM_HIGH_STOP:
				return ApiConstant.KONDUIT_CH2_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case KONDUIT2_ALARM_HIGH_ALARM:
				return ApiConstant.KONDUIT_CH2_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case KONDUIT1_ALARM_HIGH_ALARM:
				return ApiConstant.KONDUIT_CH1_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case KONDUIT1_ALARM_HIGH_STOP:
				return ApiConstant.KONDUIT_CH1_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case FLOW_METER_CH1_ALARM_HIGH_ALARM:
				return ApiConstant.FLOW_METER_CH1_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case FLOW_METER_CH1_ALARM_HIGH_STOP:
				return ApiConstant.FLOW_METER_CH1_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case FLOW_METER_CH2_ALARM_HIGH_ALARM:
				return ApiConstant.FLOW_METER_CH2_ALARM_HIGH_ALARM_TOAST_MESSAGE;

			case FLOW_METER_CH2_ALARM_HIGH_STOP:
				return ApiConstant.FLOW_METER_CH2_ALARM_HIGH_STOP_TOAST_MESSAGE;

			case START_FINAL_COLLECTION:
				return ApiConstant.START_FINAL_COLLECTION_MESSAGE;

			case DONE_FINAL_COLLECTION:
				return ApiConstant.DONE_FINAL_COLLECTION_MESSAGE;

			case FINAL_COLLECTION_TIME:
				return ApiConstant.FINAL_COLLECTION_TIME_MESSAGE;
			}
		} else if (enumString instanceof OperationMode) {

			switch ((OperationMode) enumString) {
			case C_MODE:
				return ApiConstant.C_MODE;
			case D_MODE:
				return ApiConstant.D_MODE;
			case CD_MODE:
				return ApiConstant.CD_MODE;
			case CF_MODE:
				return ApiConstant.CFC_MODE;
			case CDC_MODE:
				return ApiConstant.CDC_MODE;
			case CDDC_MODE:
				return ApiConstant.CDDC_MODE;
			case CDCD_MODE:
				return ApiConstant.CDCD_MODE;
			case CFDC_MODE:
				return ApiConstant.CFDC_MODE;
			case FluxCF:
				return ApiConstant.FLUX_C;
			case FluxCV:
				return ApiConstant.FLUX_CV;
			case Cleaning:
				return ApiConstant.CLEANING;
			case Flushing:
				return ApiConstant.FLUSHING;
			case NWP:
				return ApiConstant.NWP;
			case VACUUM_MODE:
				return ApiConstant.VACUUM_MODE;
			case HARVEST_MODE:
				return ApiConstant.HARVEST_MODE;

			}
		}else if (enumString instanceof EndPointTarget) {

			switch ((EndPointTarget) enumString) {
			case DiafiltrationVolume:
				return ApiConstant.DIAFILTRATION_VOLUME;
			case ConcentrationFactor:
				return ApiConstant.CONCENTRATION_FACTOR;

			}
		} else if (enumString instanceof ModeException) {

			// PERMEATE_SCALE_MISSING, INVALID_ENDPOINT, ENDPOINT_IS_NOT_FEASIBLE,
			// INVALID_PARAMETER

			switch ((ModeException) enumString) {
			case INVALID_ENDPOINT:
				return ApiConstant.INVALID_ENDPOINT;
			case PERMEATE_SCALE_MISSING:
				return ApiConstant.PERMEATE_SCALE_MISSING;
			case ENDPOINT_IS_NOT_FEASIBLE:
				return ApiConstant.ENDPOINT_IS_NOT_FEASIBLE;
			case INVALID_PARAMETER:
				return ApiConstant.INVALID_PARAMETER;
			case KF_KONDUIT_MISSING:
				return ApiConstant.KF_KONDUIT_MISSING_ERROR_MESSAGE;
			case KONDUIT_NOT_DEFINE:
				return ApiConstant.KONDUIT_NOT_DEFINE_ERROR_MESSAGE;
			}
		} else if (enumString instanceof ModeError) {

			// PERMEATE_SCALE_MISSING, INVALID_ENDPOINT, ENDPOINT_IS_NOT_FEASIBLE,
			// INVALID_PARAMETER

			switch ((ModeError) enumString) {
			case UNABLE_TO_MAINTAIN_PRESSURE:
				return ApiConstant.UNABLE_TO_MAINTAIN_PRESSURE;
			case UNABLE_TO_MAINTAIN_DELTA_PRESSURE:
				return ApiConstant.UNABLE_TO_MAINTAIN_DELTA_PRESSURE;

			}
		}

		// VALVE_FULL_OPEN, VALVE_FULL_CLOSE, CLAMP_PERMEATE_LINE, OPEN_RETENTATE_LINE
		else if (enumString instanceof ModeMsg) {

			switch ((ModeMsg) enumString) {
			case VALVE_FULL_OPEN:
				return ApiConstant.VALVE_FULL_OPEN;
			case VALVE_FULL_CLOSE:
				return ApiConstant.VALVE_FULL_CLOSE;
			case CLAMP_PERMEATE_LINE:
				return ApiConstant.CLAMP_PERMEATE_LINE_WHEN_RETENTATE_CLEANING;
			case OPEN_RETENTATE_LINE:
				if (DeviceManager.getInstance().getRecipeJson().getJSONArray(ApiConstant.AUX_PUMP).length() == 1) {

					DeviceManager.getInstance().setCleaningMessage(
							ApiConstant.OPEN_RETENTATE_LINE_WHEN_PERMEATE_CLEANING_WITH_PERMEATE_PUMP);
					return ApiConstant.OPEN_RETENTATE_LINE_WHEN_PERMEATE_CLEANING_WITH_PERMEATE_PUMP;
				} else {
					DeviceManager.getInstance().setCleaningMessage(
							ApiConstant.OPEN_RETENTATE_LINE_WHEN_PERMEATE_CLEANING_WITHOUT_PERMEATE_PUMP);
					return ApiConstant.OPEN_RETENTATE_LINE_WHEN_PERMEATE_CLEANING_WITHOUT_PERMEATE_PUMP;

				}

			}
		}else if (enumString instanceof KonduitType) {

			switch ((KonduitType) enumString) {
			case UV:
				return ApiConstant.UV;
			case pH:
				return ApiConstant.PH;
			case PROTEIN:
				return ApiConstant.PROTEIN_CONCENTRATION;
			case TOTALIZER:
				return ApiConstant.FLOW_METER;
			case TURBIDITY:
				return ApiConstant.TURBIDITY;

			}
		}

		return ApiConstant.BLANK_QUOTE;

	}


	public String getDeviceAlarmIndicator(ModeEvent event) {

		switch (event){
		case FEED_PRESSURE_ALARM_HIGH_STOP:
			return ApiConstant.FEED_PRESSURE_ALARM_HIGH_STOP_MESSAGE;

		case FEED_PRESSURE_ALARM_HIGH_ALARM:
			return ApiConstant.FEED_PRESSURE_ALARM_HIGH_ALARM_MESSAGE;

		case FEED_PRESSURE_ALARM_LOW_ALARM:
			return ApiConstant.FEED_PRESSURE_ALARM_LOW_ALARM_MESSAGE;

		case FEED_PRESSURE_ALARM_LOW_STOP:
			return ApiConstant.FEED_PRESSURE_ALARM_LOW_STOP_MESSAGE;

		case PERMEATE_PRESSURE_ALARM_HIGH_STOP:
			return ApiConstant.PERMEATE_PRESSURE_ALARM_HIGH_STOP_MESSAGE;

		case PERMEATE_PRESSURE_ALARM_HIGH_ALARM:
			return ApiConstant.PERMEATE_PRESSURE_ALARM_HIGH_ALARM_MESSAGE;

		case PERMEATE_PRESSURE_ALARM_LOW_ALARM:
			return ApiConstant.PERMEATE_PRESSURE_ALARM_LOW_ALARM_MESSAGE;

		case PERMEATE_PRESSURE_ALARM_LOW_STOP:
			return ApiConstant.PERMEATE_PRESSURE_ALARM_LOW_STOP_MESSAGE;

		case FEED_WEIGHT_ALARM_HIGH_STOP:
			return ApiConstant.FEED_WEIGHT_ALARM_HIGH_STOP_MESSAGE;

		case FEED_WEIGHT_ALARM_HIGH_ALARM:
			return ApiConstant.FEED_WEIGHT_ALARM_HIGH_ALARM_MESSAGE;

		case FEED_WEIGHT_ALARM_LOW_ALARM:
			return ApiConstant.FEED_WEIGHT_ALARM_LOW_ALARM_MESSAGE;

		case FEED_WEIGHT_ALARM_LOW_STOP:
			return ApiConstant.FEED_WEIGHT_ALARM_LOW_STOP_MESSAGE;

		case PERMEATE_WEIGHT_ALARM_HIGH_STOP:
			return ApiConstant.PERMEATE_WEIGHT_ALARM_HIGH_STOP_MESSAGE;

		case PERMEATE_WEIGHT_ALARM_HIGH_ALARM:
			return ApiConstant.PERMEATE_WEIGHT_ALARM_HIGH_ALARM_MESSAGE;

		case PERMEATE_WEIGHT_ALARM_LOW_ALARM:
			return ApiConstant.PERMEATE_WEIGHT_ALARM_LOW_ALARM_MESSAGE;

		case PERMEATE_WEIGHT_ALARM_LOW_STOP:
			return ApiConstant.PERMEATE_WEIGHT_ALARM_LOW_STOP_MESSAGE;

		case KONDUIT2_ALARM_HIGH_STOP:
			return ApiConstant.KONDUIT_CH2_ALARM_HIGH_STOP_MESSAGE;

		case KONDUIT2_ALARM_HIGH_ALARM:
			return ApiConstant.KONDUIT_CH2_ALARM_HIGH_ALARM_MESSAGE;

		case KONDUIT1_ALARM_HIGH_ALARM:
			return ApiConstant.KONDUIT_CH1_ALARM_HIGH_ALARM_MESSAGE;

		case KONDUIT1_ALARM_HIGH_STOP:
			return ApiConstant.KONDUIT_CH1_ALARM_HIGH_STOP_MESSAGE;

		case FLOW_METER_CH1_ALARM_HIGH_ALARM:
			return ApiConstant.FLOW_METER_CH1_ALARM_HIGH_ALARM_MESSAGE;

		case FLOW_METER_CH1_ALARM_HIGH_STOP:
			return ApiConstant.FLOW_METER_CH1_ALARM_HIGH_STOP_MESSAGE;

		case FLOW_METER_CH2_ALARM_HIGH_ALARM:
			return ApiConstant.FLOW_METER_CH2_ALARM_HIGH_ALARM_MESSAGE;

		case FLOW_METER_CH2_ALARM_HIGH_STOP:
			return ApiConstant.FLOW_METER_CH2_ALARM_HIGH_STOP_MESSAGE;

		default:
			return ApiConstant.BLANK_QUOTE;
		}

	}

	public static String getAuxPumpName(String maxRPM) {

		int rpm;

		rpm = Integer.parseInt(maxRPM.replaceAll("[^0-9]", ""));

		switch (rpm) {
		case 650:
			return ApiConstant.IP;
		case 600:
			return ApiConstant.KR1;
		case 300:
			return ApiConstant.KRJR;
		default:
			return ApiConstant.INVALID;
		}

	}

	public static JSONObject getDeviceJsonObject(int deviceId, boolean isConnected) {

		JSONObject deviceJson = new JSONObject();
		DeviceManager.getInstance().setIsMainPumpConnected(true);
		deviceJson.put(ApiConstant.DEVICE_ID, deviceId);
		deviceJson.put(ApiConstant.IS_CONNECTED, isConnected);

		return deviceJson;
	}

	public static JSONObject getDeviceJsonObject(String placedOn, boolean isConnected) {

		JSONObject deviceJson = new JSONObject();

		deviceJson.put(ApiConstant.PLACED_ON, placedOn);
		deviceJson.put(ApiConstant.IS_CONNECTED, isConnected);

		return deviceJson;
	}

	public HashMap<String, Double> getTubePosition(String tubingSize, String abvType) {

		sLog.d(this, tubingSize);
		sLog.d(this, abvType);
		HashMap<String, Double> position = new HashMap<>();

		ResultSet getTubeSpecRS = null;
		PreparedStatement getTubeSpecPS = null;

		try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
			if (ApiConstant.KR2I.equalsIgnoreCase(abvType)) {

				BasicUtility.systemPrint("inside", "KR2i");

				getTubeSpecPS = conn.prepareStatement(DbConstant.GET_SINGLE_TUBING_SPEC);

			} else if (ApiConstant.KMPI.equalsIgnoreCase(abvType)) {

				BasicUtility.systemPrint("inside", "KMPi");
				getTubeSpecPS = conn.prepareStatement(DbConstant.GET_SINGLE_TUBING_SPEC_FOR_KMPI_VALVE);
			}

			getTubeSpecPS.setString(1, tubingSize);
			getTubeSpecRS = getTubeSpecPS.executeQuery();

			position.put(ApiConstant.OPEN_POSITION, getTubeSpecRS.getDouble(ApiConstant.OPEN_POSITION));
			position.put(ApiConstant.CLOSED_POSITION, getTubeSpecRS.getDouble(ApiConstant.CLOSED_POSITION));
			position.put(ApiConstant.START_STEP_SIZE, getTubeSpecRS.getDouble(ApiConstant.START_STEP_SIZE));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return position;

	}

	public HashMap<String, Double> getTubePosition(JSONObject recipeJsonObject, JSONObject abvJson) {

		HashMap<String, Double> position = new HashMap<>();

		ResultSet getTubeSpecRS = null;
		PreparedStatement getTubeSpecPS = null;
		String tubingSize = null;
		String abvType;
		String controlBasedOn;

		try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
			abvType = abvJson.getString(ApiConstant.ABV_TYPE);
			controlBasedOn = abvJson.getString(ApiConstant.CONT_BASED_ON);

			// set open and close position if abv on tmp or retentate.
			if (
					controlBasedOn.equalsIgnoreCase(ApiConstant.RETENTATE_ABV_CONTROL_BASED) ||
					controlBasedOn.equalsIgnoreCase(ApiConstant.TMP_ABV_CONTROL_BASED) ||
					controlBasedOn.equalsIgnoreCase(ApiConstant.FEED_ABV_CONTROL_BASED)
					) {

				tubingSize = recipeJsonObject.getString(ApiConstant.RETENTATE_TUBE_SIZE);

			}
			// set open and close position if abv on permeate
			else if (controlBasedOn.equalsIgnoreCase(ApiConstant.PERMEATE_ABV_CONTROL_BASED)) {

				tubingSize = recipeJsonObject.getString(ApiConstant.PERMEATE_TUBE_SIZE);
			}else{
				throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}


			if (ApiConstant.KR2I.equalsIgnoreCase(abvType)) {

				BasicUtility.systemPrint("inside", "KR2i");

				getTubeSpecPS = conn.prepareStatement(DbConstant.GET_SINGLE_TUBING_SPEC);

			} else if (ApiConstant.KMPI.equalsIgnoreCase(abvType)) {

				BasicUtility.systemPrint("inside", "KMPi");
				getTubeSpecPS = conn.prepareStatement(DbConstant.GET_SINGLE_TUBING_SPEC_FOR_KMPI_VALVE);
			}

			getTubeSpecPS.setString(1, tubingSize);
			getTubeSpecRS = getTubeSpecPS.executeQuery();

			position.put(ApiConstant.OPEN_POSITION, getTubeSpecRS.getDouble(ApiConstant.OPEN_POSITION));
			position.put(ApiConstant.CLOSED_POSITION, getTubeSpecRS.getDouble(ApiConstant.CLOSED_POSITION));
			position.put(ApiConstant.START_STEP_SIZE, getTubeSpecRS.getDouble(ApiConstant.START_STEP_SIZE));

			BasicUtility.systemPrint("openPoss-----" + position.get(ApiConstant.OPEN_POSITION) + "-----closePos", position.get(ApiConstant.CLOSED_POSITION));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return position;

	}

	public static int getAuxPumpTypeIdByAuxName(String auxPump) {
		ComLib.get().getAuxPumpList().requestList(0);

		new RxListener(ComLib.get().getAuxPumpList()) {
			@Override
			public void OnReceive() {

				// waits until the aux pump list is completed.
				if (ComLib.get().getAuxPumpList().getAuxPumpTypeList() != null) {
					String[] auxPumpList = ComLib.get().getAuxPumpList().getAuxPumpTypeList();
					for (int index = 0; index <= (auxPumpList.length - 1); index++) {
						String auxName = getAuxPumpName(auxPumpList[index]);

						if (auxName.equalsIgnoreCase(auxPump)) {
							auxPumpTypeId1 = index;
							isAuxPumpIdSet = true;

						}
					}
					stopRxListening();
				}

			}

		}.startRxListening();

		while (!isAuxPumpIdSet) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		isAuxPumpIdSet = false;
		return auxPumpTypeId1;

	}

	public static int getPermAuxPumpPortId(JSONObject recipeJson) throws Exception {

		int loopCount = 0;

		int totalAuxPump = 0;

		if (recipeJson == null) {
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}

		totalAuxPump = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length();

		while (loopCount < totalAuxPump) {

			JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount);

			if (ApiConstant.PERMEATE.equalsIgnoreCase(auxJson.getString(ApiConstant.PUMP_FUNCTION))) {
				return auxJson.getInt(ApiConstant.TYPE);
			}

			loopCount++;

		}

		return -1;
	}

	public static int getPermAuxPumpIndex(JSONObject recipeJson) throws Exception {

		int loopCount = 0;

		int totalAuxPump = 0;

		if (recipeJson == null) {
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}

		BasicUtility.systemPrint("inside", "getPermAuxPumpPortIndex");

		totalAuxPump = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length();

		while (loopCount < totalAuxPump) {

			BasicUtility.systemPrint("inside while", "getPermAuxPumpPortIndex");

			JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount);

			if (ApiConstant.PERMEATE.equalsIgnoreCase(auxJson.getString(ApiConstant.PUMP_FUNCTION))) {
				BasicUtility.systemPrint("inside while if", "getPermAuxPumpPortIndex");
				return loopCount;
			}

			loopCount++;

		}

		return -1;
	}

	public String getFullName(JSONObject userJson) {
		String middleName = "";
		if (userJson.has(ApiConstant.MIDDLE_NAME)) {
			middleName = userJson.getString(ApiConstant.MIDDLE_NAME);
		}
		return Stream
				.of(userJson.getString(ApiConstant.FIRST_NAME), middleName,
						userJson.getString(ApiConstant.LAST_NAME))
				.filter(text -> text != null && !text.isEmpty()).collect(Collectors.joining(" "));
	}

	public int getUserId(JSONObject digitalSignatureJson) throws SQLException {

		String username;
		Connection conn = DbConnectionManager.getInstance().getConnection();

		PreparedStatement getUserIdPS = null;
		ResultSet getUserIdRS = null;
		int userId = 0;

		try {

			BasicUtility.systemPrint("username", DbConnectionManager.getInstance().getTokenManager().getUsername());

			if (DbConnectionManager.getInstance().getCfrStatus().getStatus() == 0) {
				username = DbConnectionManager.getInstance().getTokenManager().getUsername();
			} else {
				username = digitalSignatureJson.getString(ApiConstant.DIGITAL_USERNAME);
			}

			conn.setAutoCommit(false);

			if (username.equals(DbConnectionManager.getInstance().getTokenManager().getUsername())) {
				return DbConnectionManager.getInstance().getTokenManager().getUserId();
			}

			getUserIdPS = conn.prepareStatement(DbConstant.GET_USER_ID_QUERY);
			getUserIdPS.setString(1, username);

			BasicUtility.systemPrint("query----" + DbConstant.GET_USER_ID_QUERY, "value---" + userId);

			getUserIdRS = getUserIdPS.executeQuery();

			if (getUserIdRS.next()) {
				userId = getUserIdRS.getInt(ApiConstant.JSON_USER_ID);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getUserIdRS, getUserIdPS, conn);
		}

		return userId;
	}

	public int getUserId(JSONObject digitalSignatureJson, Connection conn) throws SQLException {

		String username;
		//        Connection conn = DbConnectionManager.getInstance().getConnection();

		PreparedStatement getUserIdPS = null;
		ResultSet getUserIdRS = null;
		int userId = 0;

		try {

			if (DbConnectionManager.getInstance().getCfrStatus().getStatus() == 0) {
				username = DbConnectionManager.getInstance().getTokenManager().getUsername();
			} else {
				username = digitalSignatureJson.getString(ApiConstant.DIGITAL_USERNAME);
			}

			conn.setAutoCommit(false);

			if (username.equals(DbConnectionManager.getInstance().getTokenManager().getUsername())) {
				return DbConnectionManager.getInstance().getTokenManager().getUserId();
			}

			getUserIdPS = conn.prepareStatement(DbConstant.GET_USER_ID_QUERY);
			getUserIdPS.setString(1, username);

			BasicUtility.systemPrint("query----" + DbConstant.GET_USER_ID_QUERY, "value---" + userId);

			getUserIdRS = getUserIdPS.executeQuery();

			if (getUserIdRS.next()) {
				userId = getUserIdRS.getInt(ApiConstant.JSON_USER_ID);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} /*
		 * finally { DbConnectionManager.closeDBConnection(getUserIdRS, getUserIdPS,
		 * conn); }
		 */

		return userId;
	}

	public double getCalibrationFactor(String tubingSize, String pumpName) throws Exception {

		Connection conn = DbConnectionManager.getInstance().getConnection();

		PreparedStatement getCalibrationFactorPS = null;
		ResultSet getCalibrationFactorRS = null;
		double calibrationFactor = 1;

		try {

			conn.setAutoCommit(false);

			if (tubingSize == null || tubingSize.isEmpty()) {
				if(ApiConstant.KROSFLOFS15.equals(pumpName)){
					getCalibrationFactorPS = conn.prepareStatement(DbConstant.GET_TUBING_CALIBRATION_FACTOR_FOR_KROSFLO);
					getCalibrationFactorPS.setInt(1, 24);
				}else if(ApiConstant.KROSFLOFS500.equals(pumpName)){
					getCalibrationFactorPS = conn.prepareStatement(DbConstant.GET_TUBING_CALIBRATION_FACTOR_FOR_KROSFLO);
					getCalibrationFactorPS.setInt(1, 23);
				}else{
					return 1;
				}
			}else{
				getCalibrationFactorPS = conn.prepareStatement(DbConstant.GET_TUBING_CALIBRATION_FACTOR);
				getCalibrationFactorPS.setString(1, tubingSize);
			}

			getCalibrationFactorRS = getCalibrationFactorPS.executeQuery();

			if (getCalibrationFactorRS.next()) {
				calibrationFactor = getCalibrationFactorRS.getDouble(ApiConstant.CALIBRATION_FACTOR);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getCalibrationFactorRS, getCalibrationFactorPS, conn);
		}

		return calibrationFactor;

	}

	public boolean isRunMode(String modeName) {

		if (ApiConstant.C_MODE.equalsIgnoreCase(modeName) || ApiConstant.D_MODE.equalsIgnoreCase(modeName)
				|| ApiConstant.CD_MODE.equalsIgnoreCase(modeName) || ApiConstant.CDC_MODE.equalsIgnoreCase(modeName)
				|| ApiConstant.CDDC_MODE.equalsIgnoreCase(modeName) || ApiConstant.CDCD_MODE.equalsIgnoreCase(modeName)
				|| ApiConstant.CFC_MODE.equalsIgnoreCase(modeName) || ApiConstant.CFDC_MODE.equalsIgnoreCase(modeName)
				|| ApiConstant.VACUUM_MODE.equalsIgnoreCase(modeName)

				) {
			return true;

		} else {
			return false;
		}

	}


	public boolean isMainPump(String pumpName) {
		if (ApiConstant.KR2I.equalsIgnoreCase(pumpName) || ApiConstant.KMPI.equalsIgnoreCase(pumpName)
				|| ApiConstant.KROSFLOFS15.equalsIgnoreCase(pumpName)
				|| ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)) {
			return true;
		}

		return false;
	}

	public double getConvertedFlowRate(double flowRate, String pumpName, boolean isSetToComLib) {

		// Slope conversion for Krosflo pumps
		if(flowRate > 0){
			if(ApiConstant.KROSFLOFS15.equals(pumpName)){
				if(isSetToComLib){
					flowRate -= ApiConstant.KROSFLO_FS15_CONSTANT;
				}else{
					flowRate += ApiConstant.KROSFLO_FS15_CONSTANT;
				}
			}else if(ApiConstant.KROSFLOFS500.equals(pumpName)){
				if(isSetToComLib){
					BasicUtility.systemPrint("setting pump "+pumpName,"  flowrate "+flowRate);
					flowRate += ApiConstant.KROSFLO_FS500_CONSTANT;
				}else{
					flowRate -= ApiConstant.KROSFLO_FS500_CONSTANT;
					BasicUtility.systemPrint("getting pump "+pumpName,"  flowrate "+flowRate);
				}
			}
		}

		if (ApiConstant.KR2I.equalsIgnoreCase(pumpName) || ApiConstant.KRJR.equalsIgnoreCase(pumpName)
				|| ApiConstant.KR1.equalsIgnoreCase(pumpName)) {

			if (isSetToComLib) {
				flowRate *= 100;
			} else {
				flowRate /= 100;
			}

		} else if (ApiConstant.IP.equalsIgnoreCase(pumpName)) {
			if (isSetToComLib) {
				flowRate = flowRate * 32678 / 28.8;
			} else {
				flowRate = flowRate / 32678 * 28.8;
			}
		}

		return flowRate;
	}

	public String getTemperature() throws Exception {

		Connection conn = DbConnectionManager.getInstance().getConnection();

		PreparedStatement getTemperaturePS = null;
		ResultSet getTemperatureRS = null;
		String temperature = null;
		int trialRunSettingId = TrialManager.getInstance().getTrialRunSettingId();

		try {

			conn.setAutoCommit(false);

			if (trialRunSettingId == 0) {
				throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}

			getTemperaturePS = conn.prepareStatement(DbConstant.GET_TEMPERATURE_FOR_FLUX_TRIAL);
			getTemperaturePS.setInt(1, trialRunSettingId);
			getTemperatureRS = getTemperaturePS.executeQuery();

			if (getTemperatureRS.next()) {
				temperature = getTemperatureRS.getString(ApiConstant.TEMPERATURE);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getTemperatureRS, getTemperaturePS, conn);
		}

		return temperature;

	}

	public JSONObject getMinMaxFlowRate(String tubingSize) throws Exception {

		Connection conn = DbConnectionManager.getInstance().getConnection();

		PreparedStatement getMinMaxFlowRatePS = null;
		ResultSet getMinMaxFlowRateRS = null;

		JSONObject minMaxFlowRateJson = new JSONObject();

		try {

			conn.setAutoCommit(false);
			getMinMaxFlowRatePS = conn.prepareStatement(DbConstant.GET_MIN_MAX_FLOW_RATE_QUERY);
			getMinMaxFlowRatePS.setString(1, tubingSize);

			getMinMaxFlowRateRS = getMinMaxFlowRatePS.executeQuery();

			if (getMinMaxFlowRateRS.next()) {
				minMaxFlowRateJson.put(ApiConstant.MIN_FLOW_RATE,
						ApiConstant.BLANK_QUOTE + getMinMaxFlowRateRS.getDouble(ApiConstant.MIN_FLOW_RATE));
				minMaxFlowRateJson.put(ApiConstant.MAX_FLOW_RATE,
						ApiConstant.BLANK_QUOTE + getMinMaxFlowRateRS.getDouble(ApiConstant.MAX_FLOW_RATE));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getMinMaxFlowRateRS, getMinMaxFlowRatePS, conn);
		}

		return minMaxFlowRateJson;
	}

	public String getConnectedPumpName() {
		String pumpName = null;
		String connectedPumpString = ComLib.get().getMainPump().getMotorModel();
		if(connectedPumpString == null){
			return null;
		}
		if (ApiConstant.KMPI_WITH_QUATRTRO_PUMP_HEAD.equalsIgnoreCase(connectedPumpString)) {
			pumpName = ApiConstant.KROSFLOFS500;
		} else if (ApiConstant.KR2I_WITH_QUATRTRO_PUMP_HEAD.equalsIgnoreCase(connectedPumpString)) {
			pumpName = ApiConstant.KROSFLOFS15;
		} else {
			pumpName = connectedPumpString;
		}

		return pumpName;
	}

	public void updateUserRoleInAccessLog(String role) throws Exception {

		Connection conn = DbConnectionManager.getInstance().getConnection();

		PreparedStatement updateUserRolePS = null;

		try {

			updateUserRolePS = conn.prepareStatement(DbConstant.UPDATE_USER_ROLE_IN_ACCESS_LOG_QUERY);
			updateUserRolePS.setString(1, role);
			updateUserRolePS.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateUserRolePS, conn);
		}

	}

	public JSONObject getProgressBarData(Object[] obj, JSONObject liveDataJson) {

		String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);
		double percentage;

		if (isRunMode(modeName)) {

			liveDataJson.put(ApiConstant.STAGE_ID, obj[1]);
			liveDataJson.put(ApiConstant.PERCENTAGE, Double
					.parseDouble(DeviceManager.decimalFormat.format(DeviceManager.getInstance().getPercentage())));

		} else if (ApiConstant.FLUX_CV.equalsIgnoreCase(modeName) || ApiConstant.FLUX_C.equalsIgnoreCase(modeName)) {

			liveDataJson.put(ApiConstant.STAGE_ID, obj[0]);
			liveDataJson.put(ApiConstant.SUB_STAGE_ID, obj[1]);

			percentage = (double) (((((int) obj[0]) * 3) + ((int) obj[1]) + 1) * 100) / 9;
			BasicUtility.systemPrint("percentage1", percentage);
			DeviceManager.getInstance().setPercentage(percentage);
			liveDataJson.put(ApiConstant.PERCENTAGE,
					Double.parseDouble(DeviceManager.decimalFormat.format(percentage)));

		} else {

			liveDataJson.put(ApiConstant.STAGE_ID, obj[0]);
			percentage = (double) ((((int) obj[0]) + 1) * 100)
					/ DeviceManager.getInstance().getRecipeJson().getJSONArray(ApiConstant.TARGET_STEP).length();
			BasicUtility.systemPrint("percentage2", percentage);
			DeviceManager.getInstance().setPercentage(percentage);
			liveDataJson.put(ApiConstant.PERCENTAGE,
					Double.parseDouble((DeviceManager.decimalFormat.format(percentage))));
		}
		return liveDataJson;

	}

	public String convertUnixToDate(long timestamp) {
		return SIMPLE_DATE_FORMAT.format(new Date(timestamp * 1000));
	}

	public long convertDateToUnix(String date) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(SIMPLE_DATE_FORMAT.parse(date));
		return calendar.getTime().getTime();
	}

	public long convertDateToUnixGraph(String date) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(GRAPH_DATE_FORMAT.parse(date));
		return calendar.getTime().getTime();
	}

	public String convertUnixToDateGraph(long timestamp) {
		return GRAPH_DATE_FORMAT.format(new Date(timestamp * 1000));
	}

	public long manipulateTimeStamp(long timeStamp, int seconds, boolean isAdd) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(timeStamp * 1000));
		if (isAdd) {
			calendar.add(Calendar.SECOND, seconds);
		} else {
			calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) - seconds));
		}
		return calendar.getTime().getTime() / 1000;
	}

	public final String getMainPumpName(final int trialRunSettingId) throws SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			preparedStatement = conn.prepareStatement(QueryConstant.GET_MAIN_PUMP_NAME);
			preparedStatement.setInt(1, trialRunSettingId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(ApiConstant.PUMP_NAME);
			} else {
				// manual mode
				preparedStatement.close();
				resultSet.close();

				preparedStatement = conn.prepareStatement(QueryConstant.GET_MAIN_PUMP_NAME_MANUAL);
				preparedStatement.setInt(1, trialRunSettingId);
				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					return resultSet.getString(ApiConstant.PUMP_NAME);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, preparedStatement, conn);
		}
		return null;
	}

	public final String getModeName(final int trialRunSettingId) throws SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			preparedStatement = conn.prepareStatement(QueryConstant.GET_MODE_NAME);
			preparedStatement.setInt(1, trialRunSettingId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(ApiConstant.MODE_NAME);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, preparedStatement, conn);
		}
		return null;
	}

	public final JSONObject getAuxPumpType(final int trialRunSettingId) throws SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		JSONObject jsonObject = new JSONObject();
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			preparedStatement = conn.prepareStatement(QueryConstant.GET_AUX_TYPE);
			preparedStatement.setInt(1, trialRunSettingId);
			preparedStatement.setInt(2, trialRunSettingId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet.getObject(ApiConstant.TYPE) != null) {
					if (resultSet.getInt(ApiConstant.TYPE) == 0) {
						jsonObject.put(ApiConstant.AUX_PUMP_1_TYPE, resultSet.getString(ApiConstant.AUX_PUMP_TYPE));
					} else if (resultSet.getInt(ApiConstant.TYPE) == 1) {
						jsonObject.put(ApiConstant.AUX_PUMP_2_TYPE, resultSet.getString(ApiConstant.AUX_PUMP_TYPE));
					}
				}
			}
			return jsonObject;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, preparedStatement, conn);
		}
	}


	public boolean isNwpOrFluxMode() {
		String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);

		if (
				ApiConstant.FLUX_C.equalsIgnoreCase(modeName) ||
				ApiConstant.FLUX_CV.equalsIgnoreCase(modeName) ||
				ApiConstant.NWP.equalsIgnoreCase(modeName)
				) {
			return true;
		}
		return false;
	}

	public int getFlowFactor(String pumpName) {
		if (ApiConstant.KMPI.equalsIgnoreCase(pumpName)) {
			return 1;
		} else {
			return 100;
		}
	}

	/**
	 *
	 * @return kfcomm software, interface and firmware version.
	 */
	public JSONArray getKfcommInfo(){
		Connection conn = null;
		PreparedStatement getKfcommInfoPS = null;
		ResultSet getKfcommInfoRS = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getKfcommInfoPS = conn.prepareStatement(DbConstant.GET_KFCOMM_INFO_QUERY);
			getKfcommInfoRS = getKfcommInfoPS.executeQuery();
			return JsonMapper.getJSONFromResultSet(getKfcommInfoRS);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.closeDBConnection(getKfcommInfoRS, getKfcommInfoPS, conn);
		}
		return new JSONArray();
	}

    /**
     *
     * @return Last connected COM port.
     */
    public String getLastConnectedComPort(){
        Connection conn = null;
        PreparedStatement getComPortPS = null;
        ResultSet getComPortRS = null;
        try {
            conn = DbConnectionManager.getInstance().getConnection();
            getComPortPS = conn.prepareStatement(DbConstant.GET_LAST_CONNECTED_COM_PORT_QUERY);
            getComPortRS = getComPortPS.executeQuery();
            return getComPortRS.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnectionManager.closeDBConnection(getComPortRS, getComPortPS, conn);
        }
        return null;
    }

    /**
     *
     * @return Update last connected COM port.
     */
    public void updateLastConnectedComPort(String portName){
        Connection conn = null;
        PreparedStatement updateComPortPS = null;
        try {
            conn = DbConnectionManager.getInstance().getConnection();
            updateComPortPS = conn.prepareStatement(DbConstant.UPDATE_LAST_CONNECTED_COM_PORT_QUERY);
            updateComPortPS.setString(1, portName);
            updateComPortPS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnectionManager.closeDBConnection(null, updateComPortPS, conn);
        }
    }



}
