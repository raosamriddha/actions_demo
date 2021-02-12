package org.lattice.spectrum_backend_final.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.LogManager;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public class ManualNotesUtil {

	private ManualNotesUtil() {
	}

	public static void saveManualNote(final String device, final String type, final String oldValue,
			final String newValue, final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement saveManualNotePS = null;
		try {
			con.setAutoCommit(false);
			final int userId = DbConnectionManager.getInstance().getTokenManager().getUserId();
			final long trialLogId = LogManager.getInstance().insertTrialLogGetLogId(userId, trialRunSettingId,
					ApiConstant.BLANK_QUOTE, 0, con);
			saveManualNotePS = con.prepareStatement(QueryConstant.INSERT_MANUAL_NOTE);
			saveManualNotePS.setString(1, device);
			saveManualNotePS.setString(2, type);
			saveManualNotePS.setString(3, oldValue);
			saveManualNotePS.setString(4, newValue);
			saveManualNotePS.setLong(5, trialLogId);
			sLog.d(QueryConstant.INSERT_MANUAL_NOTE);
			saveManualNotePS.executeUpdate();
			con.commit();
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				sLog.d(ApiConstant.EXCEPTION, sqlEx);
			}
			sLog.d(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, saveManualNotePS, con);
		}
	}

	public static void saveManualNoteOnRun(final String device, final String type, final String oldValue,
			final String newValue, final int trialRunSettingId, Connection con) {
		PreparedStatement saveManualNotePS = null;
		try {
			final int userId = DbConnectionManager.getInstance().getTokenManager().getUserId();
			final long trialLogId = LogManager.getInstance().insertTrialLogGetLogId(userId, trialRunSettingId,
					ApiConstant.BLANK_QUOTE, 0, con);
			saveManualNotePS = con.prepareStatement(QueryConstant.INSERT_MANUAL_NOTE);
			saveManualNotePS.setString(1, device);
			saveManualNotePS.setString(2, type);
			saveManualNotePS.setString(3, oldValue);
			saveManualNotePS.setString(4, newValue);
			saveManualNotePS.setLong(5, trialLogId);
			sLog.d(QueryConstant.INSERT_MANUAL_NOTE);
			saveManualNotePS.executeUpdate();
		} catch (final Exception ex) {
			sLog.d(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	public static final String buildManualNotesDescAction(final JSONObject jsonObject)
			throws JSONException, SQLException {
		String action = null;

		if (jsonObject.getString(ApiConstant.DEVICE).equals(ApiConstant.MAIN_PUMP)) {
			action = MessageFormat.format(ApiConstant.NOTES_MANUAL_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.DEVICE), jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE),
					convertMainPumpValue(jsonObject.getString(ApiConstant.OLD_VALUE),
							jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)),
					convertMainPumpValue(jsonObject.getString(ApiConstant.NEW_VALUE),
							jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)),
					getUnit(jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)));
		}

		else if (jsonObject.getString(ApiConstant.DEVICE).contains("Aux")) {
			convertAuxValue(jsonObject);
			final String auxUnit = getAuxUnit(jsonObject);
			action = MessageFormat.format(ApiConstant.NOTES_MANUAL_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.DEVICE), jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE),
					jsonObject.getString(ApiConstant.OLD_VALUE), jsonObject.getString(ApiConstant.NEW_VALUE), auxUnit);
		}

		else if (jsonObject.getString(ApiConstant.DEVICE).contains("ABV")) {
			action = MessageFormat.format(ApiConstant.NOTES_MANUAL_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.DEVICE), jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE),
					convertABVValue(jsonObject.getString(ApiConstant.OLD_VALUE),
							jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)),
					convertABVValue(jsonObject.getString(ApiConstant.NEW_VALUE),
							jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)),
					getUnit(jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)));
		}

		else if (jsonObject.getString(ApiConstant.DEVICE).contains("Konduit")) {
			action = MessageFormat.format(ApiConstant.NOTES_MANUAL_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.DEVICE), jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE),
					jsonObject.getString(ApiConstant.OLD_VALUE), jsonObject.getString(ApiConstant.NEW_VALUE),
					getUnit(jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE)));
		}

		else if (jsonObject.getString(ApiConstant.DEVICE).contains(ApiConstant.VALVE_CONNECTOR_KEY)) {
			action = MessageFormat.format(ApiConstant.NOTES_MANUAL_VALVE_2_ACTION_FORMAT,
					jsonObject.getString(ApiConstant.DEVICE), jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE),
					jsonObject.getString(ApiConstant.NEW_VALUE));
		}

		else if (jsonObject.getString(ApiConstant.DEVICE).contains(ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER)
				|| jsonObject.getString(ApiConstant.DEVICE).contains(ApiConstant.CASSETTE)) {

			StringBuilder filterBuilder = new StringBuilder();
			filterBuilder.append("Filter type set to ").append(jsonObject.getString(ApiConstant.DEVICE))
					.append(", part # ");

			if (!jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE).isEmpty()) {
				filterBuilder.append(jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE));
			} else {
				filterBuilder.append(ApiConstant.NOT_APPLICABLE);
			}

			if (jsonObject.getString(ApiConstant.DEVICE).contains(ApiConstant.CASSETTE)
					&& jsonObject.getString(ApiConstant.OLD_VALUE) != null
					&& !jsonObject.getString(ApiConstant.OLD_VALUE).isEmpty()) {
				filterBuilder.append(", no. of cassettes ").append(jsonObject.getString(ApiConstant.OLD_VALUE));
			}

			filterBuilder.append(", surface area ");

			if (jsonObject.getString(ApiConstant.NEW_VALUE) != null
					&& !jsonObject.getString(ApiConstant.NEW_VALUE).isEmpty()) {
				filterBuilder.append(jsonObject.getString(ApiConstant.NEW_VALUE));
			} else {
				filterBuilder.append(ApiConstant.NOT_APPLICABLE);
			}

			if (jsonObject.getString(ApiConstant.DEVICE).contains(ApiConstant.CASSETTE)) {
				filterBuilder.append(" m2");
			} else {
				filterBuilder.append(" cm2");
			}

			action = filterBuilder.toString();
		}

		return action;
	}

	private static String getAuxUnit(final JSONObject jsonObject) {
		if (ApiConstant.SPEED.equals(jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE))) {
			return ApiConstant.RPM;
		} else if (ApiConstant.NOTES_TYPE_FLOW_RATE.equals(jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE))) {
			if ("Aux Pump 1".equals(jsonObject.getString(ApiConstant.DEVICE))) {
				return HistoricalConverter.getAUX1FlowRateUnit();
			} else if ("Aux Pump 2".equals(jsonObject.getString(ApiConstant.DEVICE))) {
				return HistoricalConverter.getAUX2FlowRateUnit();
			}
		}
		return ApiConstant.BLANK_QUOTE;
	}

	private static String getUnit(final String type) throws SQLException {
		String unit = null;
		switch (type) {

		case ApiConstant.NOTES_TYPE_FLOW_RATE:
			unit = HistoricalConverter.getMainPumpFlowRateUnit();
			break;

		case ApiConstant.SPEED:
			unit = ApiConstant.RPM;
			break;

		case ApiConstant.OPERATING_PRESSURE:
			unit = Converter.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit());
			break;

		default:
			unit = ApiConstant.BLANK_QUOTE;
			break;
		}
		return unit;
	}

	private static void convertAuxValue(final JSONObject jsonObject) throws NumberFormatException, SQLException {
		if (jsonObject.getString(ApiConstant.MANUAL_NOTES_TYPE).contains(ApiConstant.NOTES_TYPE_FLOW_RATE)) {
			if ("Aux Pump 1".equals(jsonObject.getString(ApiConstant.DEVICE))) {
				jsonObject.put(ApiConstant.OLD_VALUE,
						HistoricalConverter.convertAUX1FlowRate(jsonObject.getString(ApiConstant.OLD_VALUE)));
				jsonObject.put(ApiConstant.NEW_VALUE,
						HistoricalConverter.convertAUX1FlowRate(jsonObject.getString(ApiConstant.NEW_VALUE)));
			} else if ("Aux Pump 2".equals(jsonObject.getString(ApiConstant.DEVICE))) {
				jsonObject.put(ApiConstant.OLD_VALUE,
						HistoricalConverter.convertAUX2FlowRate(jsonObject.getString(ApiConstant.OLD_VALUE)));
				jsonObject.put(ApiConstant.NEW_VALUE,
						HistoricalConverter.convertAUX2FlowRate(jsonObject.getString(ApiConstant.NEW_VALUE)));
			}
		}
	}

	private static String convertMainPumpValue(final String value, final String type)
			throws NumberFormatException, SQLException {
		if (type.contains(ApiConstant.NOTES_TYPE_FLOW_RATE)) {
			return HistoricalConverter.convertFlowRate(Double.parseDouble(value));
		} else {
			return value;
		}
	}

	private static String convertABVValue(final String value, final String type)
			throws NumberFormatException, SQLException {
		if (type.contains(ApiConstant.OPERATING_PRESSURE)) {
			return HistoricalConverter.convertPressure(Double.parseDouble(value));
		} else {
			return value;
		}
	}

	public static void saveManualFilterNote(JSONObject manualJSON, int trialRunSettingId, Connection conn) {

		String noOfCassettes = null;
		if (!manualJSON.getJSONObject(ApiConstant.FILTER).isNull(ApiConstant.NO_OF_CASSETTE)) {
			noOfCassettes = Integer
					.toString(manualJSON.getJSONObject(ApiConstant.FILTER).getInt(ApiConstant.NO_OF_CASSETTE));
		} else {
			noOfCassettes = ApiConstant.BLANK_QUOTE;
		}
		if (conn != null) {
			saveManualNoteOnRun(manualJSON.getJSONObject(ApiConstant.FILTER).getString(ApiConstant.FILTER_TYPE),
					manualJSON.getJSONObject(ApiConstant.FILTER).getString(ApiConstant.PART_NO), noOfCassettes,
					manualJSON.getJSONObject(ApiConstant.FILTER).getString(ApiConstant.SURF_AREA), trialRunSettingId,
					conn);
		} else {
			ManualNotesUtil.saveManualNote(
					manualJSON.getJSONObject(ApiConstant.FILTER).getString(ApiConstant.FILTER_TYPE),
					manualJSON.getJSONObject(ApiConstant.FILTER).getString(ApiConstant.PART_NO), noOfCassettes,
					manualJSON.getJSONObject(ApiConstant.FILTER).getString(ApiConstant.SURF_AREA), trialRunSettingId);
		}
	}
}
