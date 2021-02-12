package org.lattice.spectrum_backend_final.export.excel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.NotesHistory;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.dao.util.HistoricalMapper;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.dao.util.ManualNotesUtil;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * The Class contains utility methods for excel and pdf.
 */
public final class ExportUtil {

	private ExportUtil() {
	}

	private static ExportUtil exportUtil;

	/**
	 * Gets the single instance of ExportUtil.
	 *
	 * @return single instance of ExportUtil
	 */
	public static ExportUtil getInstance() {

		synchronized (ExportUtil.class) {
			if (exportUtil == null) {
				exportUtil = new ExportUtil();
			}
		}
		return exportUtil;
	}

	/**
	 * Gets the filter hold up up.
	 *
	 * @param partNo the part no
	 * @return the filter hold up up
	 */
	public final String getFilterHoldUpUp(String partNo) {
		Connection con = null;
		PreparedStatement getFilterHoldUpUpStmt = null;
		ResultSet resultSet = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			getFilterHoldUpUpStmt = con
					.prepareStatement("select fpi_hold_up from filter_plate_insert_lookup where fpi_part_no = ?");
			getFilterHoldUpUpStmt.setString(1, partNo);
			sLog.d(this, "select fpi_hold_up from filter_plate_insert_lookup where fpi_part_no = ?");
			resultSet = getFilterHoldUpUpStmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, getFilterHoldUpUpStmt, con);
		}
	}

	/**
	 * Fetch alarm logs.
	 *
	 * @param trialRunSettingId
	 * @return the JSON array
	 */
	public final JSONArray fetchAlarmLogs(final int trialRunSettingId) {
		JSONArray alarmsArray = new JSONArray();
		try (final Connection con = DbConnectionManager.getInstance().getConnection();
				final PreparedStatement fetchAlarmsLogStmt = con
						.prepareStatement(QueryConstant.FETCH_TRIAL_ALARMS_LOGS);) {

			fetchAlarmsLogStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.FETCH_TRIAL_ALARMS_LOGS);

			try (final ResultSet resultSet = fetchAlarmsLogStmt.executeQuery()) {
				alarmsArray = JsonMapper.getJSONFromResultSet(resultSet);
				if (!alarmsArray.isEmpty()) {
					addDefaultUnitForAlarms(alarmsArray);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
		return alarmsArray;
	}

	/**
	 * Adds the default unit for alarms.
	 *
	 * @param alarmsArray
	 */
	private void addDefaultUnitForAlarms(JSONArray alarmsArray) {
		for (int i = 0; i < alarmsArray.length(); i++) {
			if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE) != null && !alarmsArray
					.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE).contains(ApiConstant.BLANK_SPACE)) {
				if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.WEIGHT)) {
					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.GRAM);
				} else if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.PRESSURE)) {
					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.PSI);
				} else if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.PERMEATE_TOTAL_ENDPOINT)) {

					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.MILLILITER);

				} else if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.TOTALIZER)) {

					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.ML_MIN);

				} else if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.UV)) {

					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.AU);

				} else if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.TURBIDITY)) {

					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.NTU);

				} else if (alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION)
						.contains(ApiConstant.PROTEIN_CONCENTRATION)) {

					alarmsArray.getJSONObject(i).put(ApiConstant.ALARM_STOP_VALUE,
							alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE)
									+ ApiConstant.BLANK_SPACE + ApiConstant.MG_ML);

				}
			}
		}
	}

	/**
	 * Fetch notes logs.
	 *
	 * @param trialRunSettingId
	 * @return the notes list
	 */
	public final List<NotesHistory> fetchNotesLogs(int trialRunSettingId) {
		Connection con = null;
		PreparedStatement fetchNotesLogStmt = null;
		ResultSet resultSet = null;
		final List<NotesHistory> notesHistoryList = new ArrayList<>();
		try {
			con = DbConnectionManager.getInstance().getConnection();
			fetchNotesLogStmt = con.prepareStatement(QueryConstant.FETCH_NOTES_LOGS);
			fetchNotesLogStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.FETCH_NOTES_LOGS);
			resultSet = fetchNotesLogStmt.executeQuery();
			while (resultSet.next()) {
				notesHistoryList.add(HistoricalMapper.notesHistoryMapper(resultSet));
			}
			return notesHistoryList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchNotesLogStmt, con);
		}
	}

	/**
	 * Fetch trial logs.
	 *
	 * @param trialRunSettingId
	 * @param isHistorical
	 * @return the JSON array
	 * @throws SQLException the SQL exception
	 */
	public final JSONArray fetchTrialLogs(final int trialRunSettingId, boolean isHistorical) throws SQLException {

		try (final Connection con = DbConnectionManager.getInstance().getConnection();
				final PreparedStatement fetchTrialLogStmt = con
						.prepareStatement(QueryConstant.FETCH_TRIAL_HISTORY_LOGS);) {

			RequestHandler.setSystemSetting();
			RequestHandler.setTrialRunSettingId(0);
			HistoricalConverter.resetPumpNameValues();
			HistoricalConverter.resetTotalizerUnits();
			HistoricalConverter.setTotalizerUnits(RequestHandler.getStSystemSetting().getTotalizerUnit(),
					RequestHandler.getStSystemSetting().getTotalizerUnit_2());
			HistoricalConverter.assignPumpNameAndAuxType(trialRunSettingId);

			String mainPump = BasicUtility.getInstance().getMainPumpName(trialRunSettingId);
			boolean isKMPi = false;
			if (mainPump != null && !mainPump.isEmpty()) {
				if (ApiConstant.KMPI.equalsIgnoreCase(mainPump)
						|| ApiConstant.KROSFLOFS500.equalsIgnoreCase(mainPump)) {
					isKMPi = true;
				}
			}
			fetchTrialLogStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.FETCH_TRIAL_HISTORY_LOGS);

			try (final ResultSet resultSet = fetchTrialLogStmt.executeQuery()) {
				JSONArray jsonArray = JsonMapper.getJSONFromResultSet(resultSet);
				if (!jsonArray.isEmpty()) {
					formatChangeLogAction(jsonArray, isHistorical, isKMPi);
				}
				return jsonArray;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	/**
	 * Format change log action.
	 *
	 * @param jsonArray
	 * @param isHistorical
	 * @param isKMPi the is pump KMPi
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private void formatChangeLogAction(JSONArray jsonArray, final boolean isHistorical, final boolean isKMPi)
			throws JSONException, SQLException {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (jsonObject.getInt(ApiConstant.LOG_TYPE) == 2) {
				if (jsonObject.getString(ApiConstant.ACTION) == null
						|| jsonObject.getString(ApiConstant.ACTION).isEmpty()) {
					if (!jsonObject.getString(ApiConstant.NOTES_DESC_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, buildNotesDescAction(jsonObject));
						addDefaultSystemAutoLog(jsonObject);
					} else if (!jsonObject.getString(ApiConstant.NOTES_DESC_SETUP_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, buildSetupNotesDescAction(jsonObject));
						addDefaultSystemAutoLog(jsonObject);
					} else if (!jsonObject.getString(ApiConstant.END_POINT_CHANGE_LOG_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, buildEndPointAction(jsonObject, isKMPi, isHistorical));
						addDefaultSystemAutoLog(jsonObject);
					} else if (!jsonObject.getString(ApiConstant.TARGET_STEP_CHANGE_LOG_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, buildTargetStepAction(jsonObject, isHistorical, isKMPi));
						addDefaultSystemAutoLog(jsonObject);
					}
				} else {
					addDefaultSystemAutoLog(jsonObject);
				}
			} else {
				if (jsonObject.getString(ApiConstant.ACTION) == null
						|| jsonObject.getString(ApiConstant.ACTION).isEmpty()) {
					if (!jsonObject.getString(ApiConstant.END_POINT_CHANGE_LOG_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, buildEndPointAction(jsonObject, isKMPi, isHistorical));
					} else if (!jsonObject.getString(ApiConstant.TARGET_STEP_CHANGE_LOG_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, buildTargetStepAction(jsonObject, isHistorical, isKMPi));
					} else if (!jsonObject.getString(ApiConstant.NOTES_DESC_MANUAL_ID).isEmpty()) {
						jsonObject.put(ApiConstant.ACTION, ManualNotesUtil.buildManualNotesDescAction(jsonObject));
					}
				}
			}
		}
	}

	/**
	 * Adds the default system auto log.
	 *
	 * @param jsonObject the json object
	 */
	private void addDefaultSystemAutoLog(JSONObject jsonObject) {
		jsonObject.put(ApiConstant.USERNAME, ApiConstant.NOT_APPLICABLE);
		jsonObject.put(ApiConstant.ROLE_DESCRIPTION, ApiConstant.NOT_APPLICABLE);
		jsonObject.put(ApiConstant.FULL_NAME, ApiConstant.SYSTEM_AUTO_LOG);
	}

	/**
	 * Builds the notes desc action.
	 *
	 * @param jsonObject
	 * @return the action string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String buildNotesDescAction(JSONObject jsonObject) throws JSONException, SQLException {
		String unit = getUnit(jsonObject.getString(ApiConstant.END_POINT_NAME));

		if (ApiConstant.PERMEATE_WEIGHT.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_NAME))
				|| ApiConstant.PERMEATE_TOTAL_ENDPOINT
						.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_NAME))) {
			return MessageFormat.format(ApiConstant.NOTES_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.SUB_MODE_NAME), jsonObject.getString(ApiConstant.NOTES_DESC_STEP),
					getAutoModeStatus(jsonObject), jsonObject.getString(ApiConstant.END_POINT_NAME),
					HistoricalConverter.convertScale(jsonObject.getDouble(ApiConstant.END_POINT_VALUE)), unit);
		} else if (ApiConstant.DURATION.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_NAME))) {
			return MessageFormat.format(ApiConstant.NOTES_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.SUB_MODE_NAME), jsonObject.getString(ApiConstant.NOTES_DESC_STEP),
					getAutoModeStatus(jsonObject), jsonObject.getString(ApiConstant.END_POINT_NAME),
					DeviceManager.decimalFormat.format(jsonObject.getDouble(ApiConstant.END_POINT_VALUE) / 60), unit);
		} else if (ApiConstant.TMP.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_NAME))) {
			return MessageFormat.format(ApiConstant.NOTES_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.SUB_MODE_NAME), jsonObject.getString(ApiConstant.NOTES_DESC_STEP),
					getAutoModeStatus(jsonObject), jsonObject.getString(ApiConstant.END_POINT_NAME),
					HistoricalConverter.convertPressure(jsonObject.getDouble(ApiConstant.END_POINT_VALUE)), unit);
		} else {
			return MessageFormat.format(ApiConstant.NOTES_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.SUB_MODE_NAME), jsonObject.getString(ApiConstant.NOTES_DESC_STEP),
					getAutoModeStatus(jsonObject), jsonObject.getString(ApiConstant.END_POINT_NAME),
					DeviceManager.decimalFormat.format(jsonObject.getDouble(ApiConstant.END_POINT_VALUE)), unit);
		}
	}

	/**
	 * Builds the setup notes desc action.
	 *
	 * @param jsonObject the json object
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String buildSetupNotesDescAction(JSONObject jsonObject) throws JSONException, SQLException {

		if (ApiConstant.CLEANING.equalsIgnoreCase(jsonObject.getString(ApiConstant.MODE_NAME))) {
			return cleaningModeActionBuilder(jsonObject);
		} else if (ApiConstant.FLUSHING.equalsIgnoreCase(jsonObject.getString(ApiConstant.MODE_NAME))) {
			return flushingModeActionBuilder(jsonObject);
		} else if (ApiConstant.NWP.equalsIgnoreCase(jsonObject.getString(ApiConstant.MODE_NAME))) {
			return nwpModeActionBuilder(jsonObject);
		} else if (ApiConstant.FLUX_C.equalsIgnoreCase(jsonObject.getString(ApiConstant.MODE_NAME))
				|| ApiConstant.FLUX_CV.equalsIgnoreCase(jsonObject.getString(ApiConstant.MODE_NAME))) {
			return fluxModeActionBuilder(jsonObject);
		}
		return null;
	}

	/**
	 * Flux mode action builder.
	 *
	 * @param jsonObject the json object
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String fluxModeActionBuilder(JSONObject jsonObject) throws JSONException, SQLException {
		String unit = getUnit(ApiConstant.TMP);
		return MessageFormat.format(ApiConstant.NOTES_SETUP_FLUX_MODE_ACTION_FORMAT,
				jsonObject.getString(ApiConstant.MODE_NAME), jsonObject.getString(ApiConstant.STEP),
				getModeStatus(jsonObject), ApiConstant.FLOW_RATE_DISPLAY_NAME,
				HistoricalConverter.convertFlowRate(jsonObject.getDouble(ApiConstant.FLOW_RATE)),
				HistoricalConverter.getMainPumpFlowRateUnit(), ApiConstant.TMP_DISPLAY_NAME,
				HistoricalConverter.convertPressure(jsonObject.getDouble(ApiConstant.TMP)), unit,
				jsonObject.getString(ApiConstant.DURATION));
	}

	/**
	 * Nwp mode action builder.
	 *
	 * @param jsonObject the json object
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String nwpModeActionBuilder(JSONObject jsonObject) throws JSONException, SQLException {
		String unit = getUnit(ApiConstant.TMP);
		return MessageFormat.format(ApiConstant.NOTES_SETUP_NWP_MODE_ACTION_FORMAT,
				jsonObject.getString(ApiConstant.MODE_NAME), jsonObject.getString(ApiConstant.STEP),
				getModeStatus(jsonObject), ApiConstant.TMP_DISPLAY_NAME,
				HistoricalConverter.convertPressure(jsonObject.getDouble(ApiConstant.TMP)), unit,
				jsonObject.getString(ApiConstant.DURATION));
	}

	/**
	 * Flushing mode action builder.
	 *
	 * @param jsonObject the json object
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String flushingModeActionBuilder(JSONObject jsonObject) throws JSONException, SQLException {
		if (jsonObject.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT) != null
				&& !jsonObject.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT).isEmpty()) {

			String unit = getUnit(ApiConstant.PERMEATE_WEIGHT);
			return MessageFormat.format(ApiConstant.NOTES_SETUP_FLUSHING_MODE_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.MODE_NAME), getModeStatus(jsonObject), ApiConstant.PERMEATE_WEIGHT,
					HistoricalConverter.convertScale(jsonObject.getDouble(ApiConstant.COLUMN_PERMEATE_WEIGHT)), unit);
		}

		else if (jsonObject.getString(ApiConstant.COLUMN_PERMEATE_TOTAL) != null
				&& !jsonObject.getString(ApiConstant.COLUMN_PERMEATE_TOTAL).isEmpty()) {

			String unit = getUnit(ApiConstant.PERMEATE_TOTAL_ENDPOINT);
			return MessageFormat.format(ApiConstant.NOTES_SETUP_FLUSHING_MODE_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.MODE_NAME), getModeStatus(jsonObject),
					ApiConstant.PERMEATE_TOTAL_ENDPOINT,
					HistoricalConverter.convertScale(jsonObject.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL)), unit);
		}

		else if (jsonObject.getString(ApiConstant.DURATION) != null
				&& !jsonObject.getString(ApiConstant.DURATION).isEmpty()) {

			return MessageFormat.format(ApiConstant.NOTES_SETUP_DURATION_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.MODE_NAME), getModeStatus(jsonObject),
					jsonObject.getString(ApiConstant.DURATION));
		}
		return null;
	}

	/**
	 * Cleaning mode action builder.
	 *
	 * @param jsonObject the json object
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String cleaningModeActionBuilder(JSONObject jsonObject) throws JSONException, SQLException {

		if (jsonObject.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT) != null
				&& !jsonObject.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT).isEmpty()) {

			String unit = getUnit(ApiConstant.PERMEATE_WEIGHT);
			return MessageFormat.format(ApiConstant.NOTES_SETUP_CLEANING_MODE_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.MODE_NAME), jsonObject.getString(ApiConstant.STEP),
					getModeStatus(jsonObject), ApiConstant.PERMEATE_WEIGHT,
					HistoricalConverter.convertScale(jsonObject.getDouble(ApiConstant.COLUMN_PERMEATE_WEIGHT)), unit);
		}

		else if (jsonObject.getString(ApiConstant.COLUMN_PERMEATE_TOTAL) != null
				&& !jsonObject.getString(ApiConstant.COLUMN_PERMEATE_TOTAL).isEmpty()) {

			String unit = getUnit(ApiConstant.PERMEATE_TOTAL_ENDPOINT);
			return MessageFormat.format(ApiConstant.NOTES_SETUP_CLEANING_MODE_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.MODE_NAME), jsonObject.getString(ApiConstant.STEP),
					getModeStatus(jsonObject), ApiConstant.PERMEATE_TOTAL_ENDPOINT,
					HistoricalConverter.convertScale(jsonObject.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL)), unit);
		}

		else if (jsonObject.getString(ApiConstant.DURATION) != null
				&& !jsonObject.getString(ApiConstant.DURATION).isEmpty()) {

			return MessageFormat.format(ApiConstant.NOTES_SETUP_CLEANING_DURATION_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.MODE_NAME), jsonObject.getString(ApiConstant.STEP),
					getModeStatus(jsonObject), jsonObject.getString(ApiConstant.DURATION));
		}
		return null;
	}

	/**
	 * Gets the mode status.
	 *
	 * @param jsonObject the json object
	 * @return the mode status
	 */
	private final String getModeStatus(JSONObject jsonObject) {
		if (jsonObject.getInt(ApiConstant.SETUP_STATUS) == 1) {
			return ApiConstant.STARTED;
		} else {
			return ApiConstant.ENDED;
		}
	}

	/**
	 * Gets the auto mode status.
	 *
	 * @param jsonObject the json object
	 * @return the auto mode status
	 */
	private final String getAutoModeStatus(JSONObject jsonObject) {
		if (jsonObject.getInt(ApiConstant.STATUS) == 1) {
			return ApiConstant.STARTED;
		} else {
			return ApiConstant.ENDED;
		}
	}

	/**
	 * Builds the target step action.
	 *
	 * @param jsonObject the json object
	 * @param isHistorical the is historical
	 * @param isKMPi the is KM pi
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String buildTargetStepAction(final JSONObject jsonObject, boolean isHistorical, final boolean isKMPi)
			throws JSONException, SQLException {
		final StringBuilder stringBuilder = new StringBuilder().append(ApiConstant.SET_POINT)
				.append(jsonObject.getString(ApiConstant.TS_STEP_NO));
		targetStepActionAppend(jsonObject, stringBuilder, isHistorical, isKMPi);
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}

	/**
	 * Target step action append.
	 *
	 * @param jsonObject the json object
	 * @param stringBuilder the string builder
	 * @param isHistorical the is historical
	 * @param isKMPi the is KM pi
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private void targetStepActionAppend(final JSONObject jsonObject, final StringBuilder stringBuilder,
			boolean isHistorical, final boolean isKMPi) throws JSONException, SQLException {
		if (!jsonObject.getString(ApiConstant.OLD_TMP).isEmpty()) {
			if (!jsonObject.getString(ApiConstant.OLD_TMP)
					.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_TMP))) {
				if (isHistorical) {
					stringBuilder.append(MessageFormat.format(ApiConstant.TMP_CHANGE_LOG_FORMAT,
							HistoricalConverter
									.convertPressure(Double.parseDouble(jsonObject.getString(ApiConstant.OLD_TMP))),
							HistoricalConverter
									.convertPressure(Double.parseDouble(jsonObject.getString(ApiConstant.NEW_TMP))),
							Converter.systemSettingPressureMapper(
									RequestHandler.getStSystemSetting().getPressureUnit())));
				} else {
					stringBuilder.append(MessageFormat.format(ApiConstant.TMP_CHANGE_LOG_FORMAT,
							jsonObject.getString(ApiConstant.OLD_TMP), jsonObject.getString(ApiConstant.NEW_TMP),
							ApiConstant.PSI));
				}
			}
		}
		if (!jsonObject.getString(ApiConstant.OLD_DURATION).isEmpty()) {
			if (!jsonObject.getString(ApiConstant.OLD_DURATION)
					.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_DURATION))) {
				stringBuilder.append(MessageFormat.format(ApiConstant.DURATION_CHANGE_LOG_FORMAT,
						Double.parseDouble(jsonObject.getString(ApiConstant.OLD_DURATION)) / 60,
						Double.parseDouble(jsonObject.getString(ApiConstant.NEW_DURATION)) / 60));
			}
		}
		if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT).isEmpty()) {
			if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)
					.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT))) {
				if (isHistorical) {
					String unit = getUnit(ApiConstant.PERMEATE_WEIGHT);
					if (ApiConstant.KILOGRAM.equalsIgnoreCase(unit)) {
						stringBuilder.append(MessageFormat.format(ApiConstant.PERMEATE_WEIGHT_CHANGE_LOG_FORMAT,
								Double.parseDouble(jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)) / 1000,
								Double.parseDouble(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000,
								unit));
					} else {
						stringBuilder.append(MessageFormat.format(ApiConstant.PERMEATE_WEIGHT_CHANGE_LOG_FORMAT,
								jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT),
								jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT), unit));
					}
				} else {
					if (isKMPi) {
						stringBuilder.append(MessageFormat.format(ApiConstant.PERMEATE_WEIGHT_CHANGE_LOG_FORMAT,
								Double.parseDouble(jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)) / 1000,
								Double.parseDouble(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000,
								ApiConstant.KILOGRAM));
					} else {
						stringBuilder.append(MessageFormat.format(ApiConstant.PERMEATE_WEIGHT_CHANGE_LOG_FORMAT,
								jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT),
								jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT), ApiConstant.GRAM));
					}
				}

			}
		}

		if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL).isEmpty()) {
			if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL)
					.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))) {

				stringBuilder.append(MessageFormat.format(ApiConstant.PERMEATE_TOTAL_CHANGE_LOG_FORMAT,
						HistoricalConverter.convertVolume(
								Double.parseDouble(jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL))),
						HistoricalConverter.convertVolume(
								Double.parseDouble(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))),
						Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit())));
			}
		}
	}

	/**
	 * Builds the end point action.
	 *
	 * @param jsonObject the json object
	 * @param isKMPi the is KM pi
	 * @param isHistorical the is historical
	 * @return the string
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 */
	private String buildEndPointAction(final JSONObject jsonObject, final boolean isKMPi, final boolean isHistorical)
			throws JSONException, SQLException {
		String unit = getUnit(jsonObject.getString(ApiConstant.END_POINT_TYPE));
		if (jsonObject.getString(ApiConstant.END_POINT_TYPE).contains(ApiConstant.DELTA_P_ABSOLUTE_NAME)) {
			return MessageFormat.format(ApiConstant.DELTA_P_ACTION_FORMAT,
					HistoricalConverter.convertPressure(jsonObject.getDouble(ApiConstant.OLD_END_POINT_CHANGE_VALUE)),
					unit,
					HistoricalConverter.convertPressure(jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE)),
					unit);
		} else {
			if (unit.isEmpty()) {
				return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT_EMPTY,
						jsonObject.getString(ApiConstant.STEP_NUMBER), jsonObject.getString(ApiConstant.END_POINT_TYPE),
						unit, jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE),
						jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
			} else {
				if (ApiConstant.PERMEATE_WEIGHT.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
					if (isHistorical) {
						if (ApiConstant.KILOGRAM.equalsIgnoreCase(unit)) {
							return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT,
									jsonObject.getString(ApiConstant.STEP_NUMBER),
									jsonObject.getString(ApiConstant.END_POINT_TYPE), unit,
									Double.parseDouble(jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE))
											/ 1000,
									Double.parseDouble(jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE))
											/ 1000);
						} else {
							return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT,
									jsonObject.getString(ApiConstant.STEP_NUMBER),
									jsonObject.getString(ApiConstant.END_POINT_TYPE), unit,
									jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE),
									jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
						}
					} else {
						if (isKMPi) {
							return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT,
									jsonObject.getString(ApiConstant.STEP_NUMBER),
									jsonObject.getString(ApiConstant.END_POINT_TYPE), ApiConstant.KILOGRAM,
									Double.parseDouble(jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE))
											/ 1000,
									Double.parseDouble(jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE))
											/ 1000);
						} else {
							return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT,
									jsonObject.getString(ApiConstant.STEP_NUMBER),
									jsonObject.getString(ApiConstant.END_POINT_TYPE), ApiConstant.GRAM,
									jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE),
									jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
						}
					}
				} else if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
						.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {

					return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT,
							jsonObject.getString(ApiConstant.STEP_NUMBER),
							jsonObject.getString(ApiConstant.END_POINT_TYPE), unit,
							HistoricalConverter.convertVolume(
									Double.valueOf(jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE))),
							HistoricalConverter.convertVolume(
									Double.valueOf(jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE))));

				} else {
					return MessageFormat.format(ApiConstant.END_POINT_ACTION_FORMAT,
							jsonObject.getString(ApiConstant.STEP_NUMBER),
							jsonObject.getString(ApiConstant.END_POINT_TYPE), unit,
							jsonObject.getString(ApiConstant.OLD_END_POINT_CHANGE_VALUE),
							jsonObject.getString(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
				}
			}
		}
	}

	/**
	 * Gets the unit.
	 *
	 * @param type the type
	 * @return the unit
	 * @throws SQLException the SQL exception
	 */
	private String getUnit(final String type) throws SQLException {
		if (ApiConstant.PERMEATE_WEIGHT.equalsIgnoreCase(type)) {
			return Converter.systemSettingWeightMapper(RequestHandler.getStSystemSetting().getWeightUnit());
		} else if (ApiConstant.DIAFILTRATION_VOLUME.equalsIgnoreCase(type)
				|| ApiConstant.CONCENTRATION_FACTOR.equalsIgnoreCase(type)) {
			return ApiConstant.X;
		} else if (ApiConstant.UV.equalsIgnoreCase(type)) {
			return ApiConstant.AU;
		} else if (ApiConstant.TURBIDITY.equalsIgnoreCase(type)) {
			return ApiConstant.NTU;
		} else if (ApiConstant.PERMEATE_TOTAL_ENDPOINT.equalsIgnoreCase(type)) {
			return Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit());
		} else if (ApiConstant.PROTEIN_CONCENTRATION.equalsIgnoreCase(type)) {
			return ApiConstant.MG_ML;
		} else if (ApiConstant.CONDUCTIVITY.equalsIgnoreCase(type)) {
			return ApiConstant.UNIT_FOR_CONDUCTIVITY;
		} else if (ApiConstant.TMP.equalsIgnoreCase(type) || ApiConstant.DELTA_P_ABSOLUTE_NAME.equalsIgnoreCase(type)) {
			return Converter.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit());
		} else if (ApiConstant.DURATION.equalsIgnoreCase(type)) {
			return ApiConstant.MINUTE_UNIT;
		}
		return "";
	}

	/**
	 * Fetch trial details.
	 *
	 * @param trialRunSettingId the trial run setting id
	 * @return the JSON object
	 */
	public JSONObject fetchTrialDetails(final int trialRunSettingId) {
		Connection con = null;
		PreparedStatement fetchNotesLogStmt = null;
		ResultSet resultSet = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();

			fetchNotesLogStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_RUN_DETAILS);
			fetchNotesLogStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.FETCH_TRIAL_RUN_DETAILS);
			resultSet = fetchNotesLogStmt.executeQuery();
			JSONObject trialDetailsJsonObject = JsonMapper.getJSONFromResultSet(resultSet).getJSONObject(0);
			if (trialDetailsJsonObject.length() > 0) {
				return trialDetailsJsonObject;
			} else {
				throw new RuntimeException(ApiConstant.ERROR_TRIAL_DETAILS_NOT_FOUND);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchNotesLogStmt, con);
		}
	}

	/**
	 * Gets the bold italic font.
	 *
	 * @param sxssfWorkbook the sxssf workbook
	 * @return the bold italic font
	 */
	public final Font getBoldItalicFont(SXSSFWorkbook sxssfWorkbook) {
		Font font = sxssfWorkbook.createFont();
		setFontNameAndSize(font);
		font.setBold(true);
		font.setItalic(true);
		return font;
	}

	/**
	 * Gets the italic font.
	 *
	 * @param sxssfWorkbook the sxssf workbook
	 * @return the italic font
	 */
	public Font getItalicFont(SXSSFWorkbook sxssfWorkbook) {
		Font font = sxssfWorkbook.createFont();
		setFontNameAndSize(font);
		font.setItalic(true);
		return font;
	}

	/**
	 * Gets the bond font.
	 *
	 * @param sxssfWorkbook the sxssf workbook
	 * @return the bond font
	 */
	public Font getBondFont(SXSSFWorkbook sxssfWorkbook) {
		Font font = sxssfWorkbook.createFont();
		setFontNameAndSize(font);
		font.setItalic(true);
		return font;
	}

	/**
	 * Sets the font name and size.
	 *
	 * @param font the new font name and size
	 */
	private void setFontNameAndSize(Font font) {
		font.setFontName(ApiConstant.FONT_ARIAL);
		font.setFontHeightInPoints(ApiConstant.FONT_SIZE);
	}

}
