package org.lattice.spectrum_backend_final.dao.util;

import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertFlowRate;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertPressure;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertScale;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertTotalizer_1;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertTotalizer_2;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertVolume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;

public final class GraphUtil {

	private static GraphUtil graphUtil;

	public static GraphUtil getInstance() {

		synchronized (GraphUtil.class) {
			if (graphUtil == null) {
				graphUtil = new GraphUtil();
			}
		}
		return graphUtil;
	}

	public String getGraphQueryByType(final int type, final int trialRunSettingId) throws Exception {
		switch (type) {
		case GraphType.FLOW_RATE_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_FLOW_RATE_TIME_QUERY, 10);

		case GraphType.PRESSURE_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_PRESURE_TIME_QUERY, 10);

		case GraphType.FLUX_VS_TIME:
			return QueryConstant.GET_FLUX_TIME_QUERY;

		case GraphType.PRESSURE_VS_CF_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_PRESSURE_CF_TIME_QUERY, 10);

		case GraphType.FLUX_VS_CF:
			return QueryConstant.GET_FLUX_CF_QUERY;

		case GraphType.FLUX_VS_TMP:
			return getQuery(trialRunSettingId, QueryConstant.GET_FLUX_TMP_QUERY, 10);

		case GraphType.PRESSURE_VS_CF_VS_VT:
			return getQuery(trialRunSettingId, QueryConstant.GET_PRESSURE_CF_VT_QUERY, 10);

		case GraphType.DV_VS_CONDUCTIVTY_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_DV_CONDT_TIME_QUERY, 5);

		case GraphType.KCH_1_VS_TEMP_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_KCH1_TEMP_TIME_QUERY, 5);

		case GraphType.KCH_2_VS_TEMP_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_KCH2_TEMP_TIME_QUERY, 5);

		case GraphType.FLUX_VS_CF_TIME:
			return QueryConstant.GET_FLUX_CF_TIME_QUERY;

		case GraphType.TEMP_VS_CONDUCTIVTY_VS_TIME:
			return getQuery(trialRunSettingId, QueryConstant.GET_TEMP_CONDT_TIME_QUERY, 5);

		case GraphType.RPM_VS_TIME:
			return QueryConstant.GET_RPM_TIME_QUERY;

		default:
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	public final String getQuery(final int trialRunSettingId, String initialQuery, final int delay) throws Exception {
		StringBuilder queryBuilder = null;
		final long endTime = getLastTimeStamp(trialRunSettingId);
		long startTime = 0;
		if (endTime > 0) {
			startTime = getFirstTimeStamp(trialRunSettingId);
			if (startTime > 0) {
				queryBuilder = new StringBuilder(initialQuery).append(" and timestamp IN (");
				while (startTime <= endTime) {
					queryBuilder.append(startTime).append(",");
					startTime = BasicUtility.getInstance().manipulateTimeStamp(startTime, delay, true);
				}
				queryBuilder.deleteCharAt(queryBuilder.length() - 1);
				queryBuilder.append(") ").append("order by trial_live_data_id asc");
			}
		}
		if (queryBuilder != null) {
			return queryBuilder.toString();
		} else {
			return null;
		}
	}

	public final String getQueryForTrialTablePDF(final int trialRunSettingId, String initialQuery, final int delay)
			throws Exception {
		StringBuilder queryBuilder = null;
		final long endTime = getLastTimeStamp(trialRunSettingId);
		long startTime = 0;
		if (endTime > 0) {
			startTime = getFirstTimeStamp(trialRunSettingId);
			if (startTime > 0) {
				queryBuilder = new StringBuilder();
				queryBuilder.append(initialQuery).append(" and timestamp IN (");
				while (startTime <= endTime) {
					queryBuilder.append(startTime).append(",");
					startTime = BasicUtility.getInstance().manipulateTimeStamp(startTime, delay, true);
				}
				queryBuilder.deleteCharAt(queryBuilder.length() - 1);
				queryBuilder.append(") ").append("order by trial_live_data_id asc");
			}
		}
		if (queryBuilder != null) {
			return queryBuilder.toString();
		} else {
			return null;
		}
	}

	private final long getLastTimeStamp(final int trialRunSettingId) throws Exception {
		Connection conn = null;
		PreparedStatement lastTSPreparedStatement = null;
		ResultSet resultSet = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			lastTSPreparedStatement = conn.prepareStatement(QueryConstant.GET_TRIAL_END_TIME);
			lastTSPreparedStatement.setInt(1, trialRunSettingId);
			resultSet = lastTSPreparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong(ApiConstant.TIMESTAMP);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, lastTSPreparedStatement, conn);
		}
		return 0;
	}

	private final long getFirstTimeStamp(final int trialRunSettingId) throws Exception {

		Connection conn = null;
		PreparedStatement firstTSPreparedStatement = null;
		ResultSet resultSet = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			firstTSPreparedStatement = conn.prepareStatement(QueryConstant.GET_TRIAL_START_TIME);
			firstTSPreparedStatement.setInt(1, trialRunSettingId);
			resultSet = firstTSPreparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong(ApiConstant.TIMESTAMP);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, firstTSPreparedStatement, conn);
		}
		return 0;
	}

	public final String getHistoricalDataQuery(final int trialRunSettingId, final long currentTimestamp)
			throws Exception {
		StringBuilder queryBuilder = null;
		final long startTime = getFirstTimeStamp(trialRunSettingId);
		long endTime = 0;
		if (startTime > 0) {
			endTime = getLastTimeStampHist(trialRunSettingId, currentTimestamp);
			if (endTime > 0) {
				queryBuilder = new StringBuilder(QueryConstant.GET_LAST_1000_HISTORICAL_POINTS)
						.append(" and timestamp IN (");
				for (int i = 0; i < 1000; i++) {
					if (endTime >= startTime) {
						queryBuilder.append(endTime).append(",");
						endTime = BasicUtility.getInstance().manipulateTimeStamp(endTime, 10, false);
					} else {
						break;
					}
				}
				queryBuilder.deleteCharAt(queryBuilder.length() - 1);
				queryBuilder.append(") ").append("order by trial_live_data_id desc limit 1000");
			}
		}
		if (queryBuilder != null) {
			return queryBuilder.toString();
		} else {
			return null;
		}
	}

	private final long getLastTimeStampHist(final int trialRunSettingId, final long currentTimestamp) throws Exception {
		Connection conn = null;
		PreparedStatement lastTSPreparedStatement = null;
		ResultSet resultSet = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			lastTSPreparedStatement = conn.prepareStatement(QueryConstant.GET_TRIAL_END_TIME_HIST);
			lastTSPreparedStatement.setInt(1, trialRunSettingId);
			lastTSPreparedStatement.setLong(2, currentTimestamp);
			resultSet = lastTSPreparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong(ApiConstant.TIMESTAMP);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, lastTSPreparedStatement, conn);
		}
		return 0;
	}

	public JSONObject resultSetToJSON(ResultSet resultSet) throws JSONException, SQLException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ApiConstant.FEED_SCALE,
				convertScale(Double.parseDouble(resultSet.getString(ApiConstant.FEED_SCALE))));
		jsonObject.put(ApiConstant.WATER_FLUX_20_DEGREE, resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE));
		jsonObject.put(ApiConstant.AUX_PUMP_1_FLOW_RATE,
				HistoricalConverter.convertAUX1FlowRate(resultSet.getString(ApiConstant.AUX_PUMP_1_FLOW_RATE)));

		if (resultSet.getObject(ApiConstant.CONDUCTIVITY_2) != null) {
			jsonObject.put(ApiConstant.CONDUCTIVITY_2,
					DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.CONDUCTIVITY_2)));
		} else {
			jsonObject.put(ApiConstant.CONDUCTIVITY_2, ApiConstant.NOT_APPLICABLE);
		}

		jsonObject.put(ApiConstant.RETENTATE_FLOW_RATE,
				convertFlowRate(Double.parseDouble(resultSet.getString(ApiConstant.RETENTATE_FLOW_RATE))));
		jsonObject.put(ApiConstant.TYPE, ApiConstant.BLANK_QUOTE);
		jsonObject.put(ApiConstant.MAIN_PUMP_FLOW_RATE,
				convertFlowRate(Double.parseDouble(resultSet.getString(ApiConstant.MAIN_PUMP_FLOW_RATE))));
		jsonObject.put(ApiConstant.FEED_PRESSURE,
				convertPressure(Double.parseDouble(resultSet.getString(ApiConstant.FEED_PRESSURE))));
		jsonObject.put(ApiConstant.KONDUIT_CH_1_TYPE, resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE));

		if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
			if (jsonObject.getInt(ApiConstant.KONDUIT_CH_1_TYPE) == ApiConstant.CHANNEL_1_TOTALIZER_TYPE) {
				jsonObject.put(ApiConstant.KONDUIT_CH_1,
						convertTotalizer_1(Double.valueOf(resultSet.getString(ApiConstant.KONDUIT_CH_1))));
			} else {
				jsonObject.put(ApiConstant.KONDUIT_CH_1,
						DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.KONDUIT_CH_1)));
			}
		} else {
			jsonObject.put(ApiConstant.KONDUIT_CH_1, ApiConstant.NOT_APPLICABLE);
		}

		jsonObject.put(ApiConstant.KONDUIT_CH_2_TYPE, resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE));

		if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
			if (jsonObject.getInt(ApiConstant.KONDUIT_CH_2_TYPE) == ApiConstant.CHANNEL_2_TOTALIZER_TYPE) {
				jsonObject.put(ApiConstant.KONDUIT_CH_2,
						convertTotalizer_2(Double.valueOf(resultSet.getString(ApiConstant.KONDUIT_CH_2))));
			} else {
				jsonObject.put(ApiConstant.KONDUIT_CH_2,
						DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.KONDUIT_CH_2)));
			}
		} else {
			jsonObject.put(ApiConstant.KONDUIT_CH_2, ApiConstant.NOT_APPLICABLE);
		}

		jsonObject.put(ApiConstant.TEMPERATURE_CORRECTION_FACTOR,
				resultSet.getObject(ApiConstant.TEMPERATURE_CORRECTION_FACTOR));

		jsonObject.put(ApiConstant.PERCENTAGE, 0.0);

		if (resultSet.getObject(ApiConstant.CONDUCTIVITY_1) != null) {
			jsonObject.put(ApiConstant.CONDUCTIVITY_1,
					DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.CONDUCTIVITY_1)));
		} else {
			jsonObject.put(ApiConstant.CONDUCTIVITY_1, ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.DIAFILTRATION_VOL_2) != null) {
			jsonObject.put(ApiConstant.DIAFILTRATION_VOL_2,
					DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.DIAFILTRATION_VOL_2)));
		} else {
			jsonObject.put(ApiConstant.DIAFILTRATION_VOL_2, ApiConstant.NOT_APPLICABLE);
		}
		
		jsonObject.put(ApiConstant.DELTA_P,
				convertPressure(Double.parseDouble(resultSet.getString(ApiConstant.DELTA_P))));
		jsonObject.put(ApiConstant.DIAFILTRATION_VOL_1,
				DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.DIAFILTRATION_VOL_1)));
		jsonObject.put(ApiConstant.FLUX, DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.FLUX)));
		jsonObject.put(ApiConstant.TIMESTAMP,
				BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getLong(ApiConstant.TIMESTAMP)).toString());
		jsonObject.put(ApiConstant.CONCENTRATION_FACT,
				DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.COLUMN_CONCENTRATION_FACTOR)));
		jsonObject.put(ApiConstant.AUX_PUMP_1_SPEED, "0");
		jsonObject.put(ApiConstant.ABV_1, resultSet.getString(ApiConstant.ABV_1));
		jsonObject.put(ApiConstant.FEED_FLOW_RATE,
				convertFlowRate(Double.parseDouble(resultSet.getString(ApiConstant.FEED_FLOW_RATE))));
		jsonObject.put(ApiConstant.SHEAR, resultSet.getString(ApiConstant.SHEAR));
		jsonObject.put(ApiConstant.NWP, resultSet.getString(ApiConstant.NWP));
		jsonObject.put(ApiConstant.ABV_2, resultSet.getString(ApiConstant.ABV_2));
		jsonObject.put(ApiConstant.AUX_PUMP_2_FLOW_RATE,
				HistoricalConverter.convertAUX2FlowRate(resultSet.getString(ApiConstant.AUX_PUMP_2_FLOW_RATE)));

		if (resultSet.getObject(ApiConstant.TOTAL_PERM_WEIGHT) != null) {
			jsonObject.put(ApiConstant.TOTAL_PERM_WEIGHT,
					convertScale(Double.parseDouble(resultSet.getString(ApiConstant.TOTAL_PERM_WEIGHT))));
		} else {
			jsonObject.put(ApiConstant.TOTAL_PERM_WEIGHT, ApiConstant.NOT_APPLICABLE);
		}

		jsonObject.put(ApiConstant.RETENTATE_PRESSURE,
				convertPressure(Double.parseDouble(resultSet.getString(ApiConstant.RETENTATE_PRESSURE))));
		jsonObject.put(ApiConstant.WATER_FLUX, ApiConstant.NOT_APPLICABLE);

		if (resultSet.getObject(ApiConstant.M_PERMEATE) != null) {
			jsonObject.put(ApiConstant.M_PERMEATE,
					convertScale(Double.parseDouble(resultSet.getString(ApiConstant.M_PERMEATE))));
		} else {
			jsonObject.put(ApiConstant.M_PERMEATE, ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.TEMPERATURE_2) != null) {
			jsonObject.put(ApiConstant.TEMPERATURE_2,
					DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.TEMPERATURE_2)));
		} else {
			jsonObject.put(ApiConstant.TEMPERATURE_2, ApiConstant.NOT_APPLICABLE);
		}

		jsonObject.put(ApiConstant.PERMEATE_PRESSURE,
				convertPressure(Double.parseDouble(resultSet.getString(ApiConstant.PERMEATE_PRESSURE))));

		if (resultSet.getObject(ApiConstant.TEMPERATURE_1) != null) {
			jsonObject.put(ApiConstant.TEMPERATURE_1,
					DeviceManager.decimalFormat.format(resultSet.getObject(ApiConstant.TEMPERATURE_1)));
		} else {
			jsonObject.put(ApiConstant.TEMPERATURE_1, ApiConstant.NOT_APPLICABLE);
		}

		jsonObject.put(ApiConstant.MAIN_PUMP_SPEED, resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED));
		jsonObject.put(ApiConstant.AUX_PUMP_2_SPEED, "0");
		if (ApiConstant.TMP_PRESSURE.equalsIgnoreCase(resultSet.getMetaData().getColumnName(37))) {
			jsonObject.put(ApiConstant.TMP_PRESSURE,
					convertPressure(Double.parseDouble(resultSet.getString(ApiConstant.TMP_PRESSURE))));
		} else {
			jsonObject.put(ApiConstant.TMP_PRESSURE,
					convertPressure(Double.parseDouble(resultSet.getString(ApiConstant.TMP))));
		}
		jsonObject.put(ApiConstant.VT, resultSet.getString(ApiConstant.VT));
		jsonObject.put(ApiConstant.WATER_PERMEABILITY, resultSet.getString(ApiConstant.WATER_PERMEABILITY));
		jsonObject.put(ApiConstant.PERMEATE_FLOW_RATE,
				convertFlowRate(Double.parseDouble(resultSet.getString(ApiConstant.PERMEATE_FLOW_RATE))));
		jsonObject.put(ApiConstant.TIMESTAMP_2,
				BasicUtility.getInstance().convertUnixToDate(resultSet.getLong(ApiConstant.TIMESTAMP)).toString());

		if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL) != null) {
			jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL,
					convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL)));
		} else {
			jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL, ApiConstant.NOT_APPLICABLE);
		}

		if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP) != null) {
			jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP,
					convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP)));
		} else {
			jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP, ApiConstant.NOT_APPLICABLE);
		}
		jsonObject.put(ApiConstant.DURATION, resultSet.getString(ApiConstant.DURATION));
		return jsonObject;
	}

}