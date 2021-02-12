package org.lattice.spectrum_backend_final.dao.util;

import static org.lattice.spectrum_backend_final.dao.manager.DeviceManager.decimalFormat;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertPressure;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertScale;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertVolume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.lattice.spectrum_backend_final.beans.AlarmHistory;
import org.lattice.spectrum_backend_final.beans.NotesHistory;
import org.lattice.spectrum_backend_final.beans.TrialHistory;
import org.lattice.spectrum_backend_final.beans.TrialTableLogs;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

public final class HistoricalMapper {

	private HistoricalMapper() {
	}

	public static final TrialTableLogs trialTableLogsMapper(final ResultSet resultSet) throws SQLException {
		final TrialTableLogs trialTableLogs = new TrialTableLogs();
		trialTableLogs.setTrialId(resultSet.getString(ApiConstant.TRIAL_ID));
		trialTableLogs.setTrialLiveDataId(resultSet.getInt(ApiConstant.TRIAL_LIVE_DATA_ID));
		trialTableLogs
				.setTimestamp(BasicUtility.getInstance().convertUnixToDate(resultSet.getInt(ApiConstant.TIMESTAMP)));
		trialTableLogs.setFeedPressure(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)));
		trialTableLogs.setPermeatePressure(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)));
		trialTableLogs.setRetentatePressure(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)));
		trialTableLogs.setTmp(convertPressure(resultSet.getDouble(ApiConstant.TMP)));
		trialTableLogs.setDeltaP(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)));
		trialTableLogs.setMainPumpSpeed((int) resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED));
		trialTableLogs.setMainPumpFlowRate(decimalFormat.format(resultSet.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE)));
		trialTableLogs.setAuxPump1FlowRate(decimalFormat.format(resultSet.getDouble(ApiConstant.AUX_PUMP_1_FLOW_RATE)));
		trialTableLogs.setAuxPump2FlowRate(decimalFormat.format(resultSet.getDouble(ApiConstant.AUX_PUMP_2_FLOW_RATE)));

		if (resultSet.getObject(ApiConstant.CONDUCTIVITY_1) != null) {
			trialTableLogs.setConductivity1(decimalFormat.format(resultSet.getObject(ApiConstant.CONDUCTIVITY_1)));
		} else {
			trialTableLogs.setConductivity1(ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.CONDUCTIVITY_2) != null) {
			trialTableLogs.setConductivity2(decimalFormat.format(resultSet.getObject(ApiConstant.CONDUCTIVITY_2)));
		} else {
			trialTableLogs.setConductivity2(ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.TEMPERATURE_1) != null) {
			trialTableLogs.setTemperature1(decimalFormat.format(resultSet.getObject(ApiConstant.TEMPERATURE_1)));
		} else {
			trialTableLogs.setTemperature1(ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.TEMPERATURE_2) != null) {
			trialTableLogs.setTemperature2(decimalFormat.format(resultSet.getObject(ApiConstant.TEMPERATURE_2)));
		} else {
			trialTableLogs.setTemperature2(ApiConstant.NOT_APPLICABLE);
		}

		trialTableLogs.setFeedScale(convertScale(resultSet.getDouble(ApiConstant.FEED_SCALE)));

		if (resultSet.getObject(ApiConstant.M_PERMEATE) != null) {
			trialTableLogs.setMPermeate(convertScale(resultSet.getDouble(ApiConstant.M_PERMEATE)));
		} else {
			trialTableLogs.setMPermeate(ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.TOTAL_PERM_WEIGHT) != null) {
			trialTableLogs.setTotalPermWeight(convertScale(resultSet.getDouble(ApiConstant.TOTAL_PERM_WEIGHT)));
		} else {
			trialTableLogs.setTotalPermWeight(ApiConstant.NOT_APPLICABLE);
		}

		trialTableLogs.setDiaFiltrationVol1(decimalFormat.format(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_1)));
		trialTableLogs.setDiaFiltrationVol2(decimalFormat.format(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_2)));
		trialTableLogs.setConcentrationFactor(
				decimalFormat.format(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR)));
		trialTableLogs.setFeedFlowRate(decimalFormat.format(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE)));
		trialTableLogs.setPermeateFlowRate(decimalFormat.format(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE)));
		trialTableLogs.setRetentateFlowRate(decimalFormat.format(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE)));
		trialTableLogs.setVt(decimalFormat.format(resultSet.getDouble(ApiConstant.VT)));
		trialTableLogs.setShear(resultSet.getString(ApiConstant.SHEAR));
		trialTableLogs.setFlux(decimalFormat.format(resultSet.getDouble(ApiConstant.FLUX)));
		trialTableLogs.setkFactorCh1(resultSet.getString(ApiConstant.K_FACTOR_CH_1));
		trialTableLogs.setkFactorCh2(resultSet.getString(ApiConstant.K_FACTOR_CH_2));
		trialTableLogs.setAbv1(resultSet.getString(ApiConstant.ABV_1));
		trialTableLogs.setAbv2(resultSet.getString(ApiConstant.ABV_2));
		trialTableLogs.setTcf(resultSet.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR));
		trialTableLogs.setWaterFlux20Degree(resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE));
		trialTableLogs.setWaterPerm(resultSet.getString(ApiConstant.WATER_PERMEABILITY));
		trialTableLogs.setNwp(resultSet.getString(ApiConstant.NORMALIZED_WATER_PERMEABILITY));
		trialTableLogs.setKonduitCh1Type(resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE));
		trialTableLogs.setKonduitCh2Type(resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE));

		if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
			if (trialTableLogs.getKonduitCh1Type() == ApiConstant.CHANNEL_1_TOTALIZER_TYPE) {
				trialTableLogs.setKonduitCh1(HistoricalConverter
						.convertTotalizer_1(Double.valueOf(resultSet.getDouble(ApiConstant.KONDUIT_CH_1))));
			} else {
				trialTableLogs.setKonduitCh1(
						String.valueOf(decimalFormat.format(resultSet.getDouble(ApiConstant.KONDUIT_CH_1))));
			}
		} else {
			trialTableLogs.setKonduitCh1(ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
			if (trialTableLogs.getKonduitCh2Type() == ApiConstant.CHANNEL_2_TOTALIZER_TYPE) {
				trialTableLogs.setKonduitCh2(HistoricalConverter
						.convertTotalizer_2(Double.valueOf(resultSet.getDouble(ApiConstant.KONDUIT_CH_2))));
			} else {
				trialTableLogs.setKonduitCh2(
						String.valueOf(decimalFormat.format(resultSet.getDouble(ApiConstant.KONDUIT_CH_2))));
			}
		} else {
			trialTableLogs.setKonduitCh2(ApiConstant.NOT_APPLICABLE);
		}

		trialTableLogs.setIsPaused(resultSet.getInt(ApiConstant.COLUMN_IS_PAUSED));

		if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL) != null) {
			trialTableLogs.setPermeateTotal(convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL)));
		} else {
			trialTableLogs.setPermeateTotal(ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP) != null) {
			trialTableLogs.setPermTotalWithHoldUp(
					convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP)));
		} else {
			trialTableLogs.setPermTotalWithHoldUp(ApiConstant.NOT_APPLICABLE);
		}
		trialTableLogs.setDuration(resultSet.getString(ApiConstant.DURATION));
		HistoricalConverter.convertTrialLogPumpFlowRate(trialTableLogs);
		return trialTableLogs;
	}

	public static final AlarmHistory alarmsHistoryMapper(ResultSet resultSet) throws SQLException {
		AlarmHistory alarmHistory = new AlarmHistory();
		alarmHistory.setAlarmHistoryId(resultSet.getInt(ApiConstant.ALARM_HISTORY_ID));
		alarmHistory.setModeName(resultSet.getString(ApiConstant.MODE_NAME));
		alarmHistory.setAlarmStopDesc(resultSet.getString(ApiConstant.ALARM_STOP_DESCRIPTION));
		alarmHistory.setAlarmStopValue(resultSet.getString(ApiConstant.ALARM_STOP_VALUE));
		alarmHistory.setNature(resultSet.getString(ApiConstant.NATURE));
		alarmHistory.setCreatedOn(resultSet.getString(ApiConstant.CREATED_ON));
		if (alarmHistory.getAlarmStopValue() != null) {
			setConvertedAlarmValue(alarmHistory);
		}
		return alarmHistory;
	}

	private static void setConvertedAlarmValue(AlarmHistory alarmHistory) throws SQLException {
		if (alarmHistory.getAlarmStopValue().contains(ApiConstant.BLANK_SPACE)) {
			convertOldLogicAlarmValue(alarmHistory);
		} else {
			convertNewLogicAlarmValue(alarmHistory);
		}
	}

	private static void convertNewLogicAlarmValue(AlarmHistory alarmHistory)
			throws NumberFormatException, SQLException {
		String convertedValue = null;
		if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.WEIGHT)) {
			convertedValue = HistoricalConverter.convertScale(Double.parseDouble(alarmHistory.getAlarmStopValue()));
			setUnit(convertedValue, alarmHistory, true);
		} else if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.PRESSURE)) {
			convertedValue = HistoricalConverter.convertPressure(Double.parseDouble(alarmHistory.getAlarmStopValue()));
			setUnit(convertedValue, alarmHistory, false);
		} else if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.PERMEATE_TOTAL_ENDPOINT)) {

			if (alarmHistory.getAlarmStopValue() != null) {
				convertedValue = String
						.valueOf(HistoricalConverter.convertVolume(Double.valueOf(alarmHistory.getAlarmStopValue())));
				alarmHistory.setAlarmStopValue(convertedValue + ApiConstant.BLANK_SPACE
						+ Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
			} else {
				alarmHistory.setAlarmStopValue(ApiConstant.NOT_APPLICABLE);
			}

		} else if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.TOTALIZER_CH_1)) {
			alarmHistory.setAlarmStopValue(
					HistoricalConverter.convertTotalizer_1(Double.valueOf(alarmHistory.getAlarmStopValue()))
							+ HistoricalConverter.getTotalizerUnit(1));
		} else if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.TOTALIZER_CH_2)) {
			alarmHistory.setAlarmStopValue(
					HistoricalConverter.convertTotalizer_2(Double.valueOf(alarmHistory.getAlarmStopValue()))
							+ HistoricalConverter.getTotalizerUnit(2));
		} else {
			alarmHistory.setAlarmStopValue(Converter.getAuxConverter(Double.valueOf(alarmHistory.getAlarmStopValue())));
		}
	}

	private static void convertOldLogicAlarmValue(AlarmHistory alarmHistory) throws SQLException {
		String convertedValue = null;
		double value = Double.parseDouble(alarmHistory.getAlarmStopValue().substring(0,
				alarmHistory.getAlarmStopValue().indexOf(ApiConstant.BLANK_SPACE)));
		String unit = alarmHistory.getAlarmStopValue().substring(
				alarmHistory.getAlarmStopValue().indexOf(ApiConstant.BLANK_SPACE) + 1,
				alarmHistory.getAlarmStopValue().length());

		if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.WEIGHT)) {

			if (ApiConstant.KILOGRAM.equalsIgnoreCase(unit)) {
				value *= 1000;
			}
			convertedValue = HistoricalConverter.convertScale(value);
			setUnit(convertedValue, alarmHistory, true);

		} else if (alarmHistory.getAlarmStopDesc().contains(ApiConstant.PRESSURE)) {

			if (!ApiConstant.PSI.equalsIgnoreCase(unit)) {
				value = convertPressureToPSI(value, unit);
			}
			convertedValue = HistoricalConverter.convertPressure(value);
			setUnit(convertedValue, alarmHistory, false);
		}
	}

	private static void setUnit(String convertedValue, AlarmHistory alarmHistory, boolean isWeight)
			throws SQLException {
		if (convertedValue != null) {
			if (isWeight) {
				alarmHistory.setAlarmStopValue(convertedValue + ApiConstant.BLANK_SPACE
						+ Converter.systemSettingWeightMapper(RequestHandler.getStSystemSetting().getWeightUnit()));
			} else {
				alarmHistory.setAlarmStopValue(convertedValue + ApiConstant.BLANK_SPACE
						+ Converter.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
			}
		}
	}

	private static double convertPressureToPSI(double value, String unit) {
		switch (unit) {
		case ApiConstant.M_BAR:
			value *= 0.0145038;
			break;
		case ApiConstant.BAR:
			value *= 14.5038;
			break;
		}
		return value;
	}

	public static final NotesHistory notesHistoryMapper(ResultSet resultSet) throws SQLException {
		NotesHistory notesHistory = new NotesHistory();
		notesHistory.setNotesId(resultSet.getInt(ApiConstant.NOTES_ID));
		notesHistory.setNotes(resultSet.getString(ApiConstant.NOTES));
		notesHistory.setPostedBy(resultSet.getString(ApiConstant.POSTED_BY));
		notesHistory.setCreatedOn(resultSet.getString(ApiConstant.CREATED_ON));
		return notesHistory;
	}

	public static final TrialHistory trialHistoryMapper(ResultSet resultSet) throws SQLException {
		TrialHistory trialHistory = new TrialHistory();
		trialHistory.setTrialLogId(resultSet.getInt(ApiConstant.TRIAL_LOG_ID));
		trialHistory.setAction(resultSet.getString(ApiConstant.ACTION));
		trialHistory.setCreatedOn(resultSet.getString(ApiConstant.CREATED_ON));
		trialHistory.setFullName(resultSet.getString(ApiConstant.FULL_NAME));
		trialHistory.setUserName(resultSet.getString(ApiConstant.USERNAME));
		trialHistory.setRole(resultSet.getString(ApiConstant.ROLE_DESCRIPTION));
		trialHistory.setTrialId(resultSet.getString(ApiConstant.TRIAL_ID));
		if (trialHistory.getAction() == null || trialHistory.getAction().isEmpty()) {
			trialHistory.setEndPointsChangeLog(getEndPointChangeLog(trialHistory.getTrialLogId()));
			if (trialHistory.getEndPointsChangeLog().isEmpty()) {
				trialHistory.setTargetStepChangeLog(getTargetStepChangeLog(trialHistory.getTrialLogId()));
			}
		}
		return trialHistory;
	}

	public static final JSONArray getTargetStepChangeLog(int trialLogId) throws SQLException {
		Connection conn = null;
		PreparedStatement getTargetStepChangeLogPS = null;
		ResultSet getTargetStepChangeLogRS = null;
		JSONArray targetStepJsonArray = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getTargetStepChangeLogPS = conn.prepareStatement(QueryConstant.GET_TARGET_STEP_CHANGE_LOG);
			getTargetStepChangeLogPS.setInt(1, trialLogId);
			getTargetStepChangeLogRS = getTargetStepChangeLogPS.executeQuery();
			targetStepJsonArray = JsonMapper.getJSONFromResultSet(getTargetStepChangeLogRS);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getTargetStepChangeLogRS, getTargetStepChangeLogPS, conn);
		}
		return targetStepJsonArray;
	}

	public static final JSONArray getEndPointChangeLog(int trialLogId) throws SQLException {
		Connection conn = null;
		PreparedStatement getEndPointChangeLogPS = null;
		ResultSet getEndPointChangeLogRS = null;
		JSONArray endPointJsonArray = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getEndPointChangeLogPS = conn.prepareStatement(QueryConstant.GET_END_POINTS_CHANGE_LOG);
			getEndPointChangeLogPS.setInt(1, trialLogId);
			getEndPointChangeLogRS = getEndPointChangeLogPS.executeQuery();
			endPointJsonArray = JsonMapper.getJSONFromResultSet(getEndPointChangeLogRS);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getEndPointChangeLogRS, getEndPointChangeLogPS, conn);
		}
		return endPointJsonArray;
	}
}