package org.lattice.spectrum_backend_final.export.pdf;

import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertPressure;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertScale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.NotesHistory;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.EndPointSettingManager;
import org.lattice.spectrum_backend_final.dao.manager.ManualTrialManager;
import org.lattice.spectrum_backend_final.dao.manager.TargetStepSettingManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.GraphUtil;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.export.excel.ExportUtil;

/**
 * The Class to fetch data for PDF API or to download PDF.
 */
public final class PDFExport {

	private PDFExport() {
	}

	private static PDFExport pdfManager;

	/**
	 * Gets the single instance of PDFExport.
	 *
	 * @return single instance of PDFExport
	 */
	public static PDFExport getInstance() {

		synchronized (PDFExport.class) {
			if (pdfManager == null) {
				pdfManager = new PDFExport();
			}
		}
		return pdfManager;
	}

	/**
	 * Fetch data for PDF.
	 *
	 * @param trialRunSettingId
	 * @param trialMasterId
	 * @param interval
	 * @return the JSON object
	 */
	public final JSONObject fetchDataForPDF(final int trialRunSettingId, final int trialMasterId, final int interval) {

		final JSONObject resultJSON = new JSONObject();

		try {
			JSONArray recipeJson = null;
			if (trialMasterId > 0) {
				recipeJson = DatabaseManager.getInstance().getRecipe(trialMasterId);
				if (recipeJson != null) {
					if (ApiConstant.BLANK_QUOTE
							.equalsIgnoreCase(recipeJson.getJSONObject(0).getString(ApiConstant.FEED_TO_EMPTY))) {
						recipeJson.getJSONObject(0).put(ApiConstant.FEED_TO_EMPTY, ApiConstant.NOT_APPLICABLE);
					}
					if (ApiConstant.CASSETTE
							.equalsIgnoreCase(recipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE))) {
						recipeJson.getJSONObject(0).put(ApiConstant.FPI_HOLD_UP,
								ExportUtil.getInstance().getFilterHoldUpUp(recipeJson.getJSONObject(0)
										.getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO)));
					}
				}

			} else {
				recipeJson = new JSONArray();
			}

			final JSONObject trialJson = ExportUtil.getInstance().fetchTrialDetails(trialRunSettingId);
			trialJson.put(ApiConstant.DOWNLOADED_ON,
					BasicUtility.getInstance().convertUnixToDate(System.currentTimeMillis() / 1000));
			trialJson.put(ApiConstant.DOWNLOADED_BY, DbConnectionManager.getInstance().getTokenManager().getUsername());
			if (trialMasterId <= 0) {
				String mainPump = BasicUtility.getInstance().getMainPumpName(trialRunSettingId);
				if (mainPump != null && !mainPump.isEmpty()) {
					trialJson.put(ApiConstant.MAIN_PUMP_KEY, mainPump);
				} else {
					trialJson.put(ApiConstant.MAIN_PUMP_KEY, ApiConstant.BLANK_QUOTE);
				}
				final JSONObject auxPumpType = BasicUtility.getInstance().getAuxPumpType(trialRunSettingId);
				if (auxPumpType != null) {
					trialJson.put(ApiConstant.AUX_PUMP, auxPumpType);
				} else {
					trialJson.put(ApiConstant.AUX_PUMP, new JSONObject());
				}

				final JSONObject filterDetails = ManualTrialManager.getInstance()
						.getManualFilterDetails(trialRunSettingId);
				resultJSON.put(ApiConstant.FILTER, filterDetails);
			}
			recipeJson.put(trialJson);
			resultJSON.put(ApiConstant.DETAILS, recipeJson);
			final List<NotesHistory> notesList = ExportUtil.getInstance().fetchNotesLogs(trialRunSettingId);
			resultJSON.put(ApiConstant.NOTES, notesList);
			final JSONArray alarmsArray = ExportUtil.getInstance().fetchAlarmLogs(trialRunSettingId);
			resultJSON.put(ApiConstant.ALARMS_KEY, alarmsArray);
			final JSONArray logsArray = ExportUtil.getInstance().fetchTrialLogs(trialRunSettingId, false);
			resultJSON.put(ApiConstant.TRIAL_LOGS, logsArray);
			final JSONArray tableArray = fetchTableForPDF(trialRunSettingId, interval);
			resultJSON.put(ApiConstant.TRIAL_TABLE_LOGS, tableArray);
			final JSONArray targetStepsJsonArray = TargetStepSettingManager.getInstance()
					.getTargetStepChangeLog(trialRunSettingId);
			resultJSON.put(ApiConstant.TARGET_STEP_CHANGE_LOG, targetStepsJsonArray);
			final JSONArray endPointsJsonArray = EndPointSettingManager.getInstance()
					.getEndPointChangeLog(trialRunSettingId);
			resultJSON.put(ApiConstant.END_POINT_CHANGE_LOG, endPointsJsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
		return resultJSON;
	}

	/**
	 * Fetch table data for PDF.
	 *
	 * @param trialRunSettingId
	 * @param interval
	 * @return the JSON array
	 * @throws Exception the exception
	 */
	private final JSONArray fetchTableForPDF(final int trialRunSettingId, final int interval) throws Exception {
		String tableQuery = GraphUtil.getInstance().getQueryForTrialTablePDF(trialRunSettingId,
				QueryConstant.FETCH_TABLE_FOR_PDF, interval);
		if (tableQuery == null) {
			return new JSONArray();
		}
		try (final Connection con = DbConnectionManager.getInstance().getConnection();
				final PreparedStatement fetchTableDataStmt = con.prepareStatement(tableQuery);) {

			fetchTableDataStmt.setInt(1, trialRunSettingId);

			try (final ResultSet resultSet = fetchTableDataStmt.executeQuery()) {
				return getTableArrayFromResultSet(resultSet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	/**
	 * Gets the table array from result set.
	 *
	 * @param resultSet
	 * @return the table array from result set
	 * @throws Exception the exception
	 */
	private final JSONArray getTableArrayFromResultSet(final ResultSet resultSet) throws Exception {
		final JSONArray resultJsonArray = new JSONArray();
		try {
			if (resultSet != null) {
				while (resultSet.next()) {
					final JSONObject jsonObject = new JSONObject();
					jsonObject.put(ApiConstant.TRIAL_LIVE_DATA_ID, resultSet.getInt(ApiConstant.TRIAL_LIVE_DATA_ID));
					jsonObject.put(ApiConstant.TIMESTAMP,
							BasicUtility.getInstance().convertUnixToDate(resultSet.getInt(ApiConstant.TIMESTAMP)));
					jsonObject.put(ApiConstant.DURATION, resultSet.getString(ApiConstant.DURATION));
					jsonObject.put(ApiConstant.FEED_PRESSURE,
							Double.valueOf(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE))));
					jsonObject.put(ApiConstant.RETENTATE_PRESSURE,
							Double.valueOf(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE))));
					jsonObject.put(ApiConstant.PERMEATE_PRESSURE,
							Double.valueOf(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE))));
					if (ApiConstant.TMP_PRESSURE.equalsIgnoreCase(resultSet.getMetaData().getColumnName(6))) {
						jsonObject.put(ApiConstant.TMP_PRESSURE,
								convertPressure(Double.valueOf(resultSet.getString(ApiConstant.TMP_PRESSURE))));
					} else {
						jsonObject.put(ApiConstant.TMP_PRESSURE,
								convertPressure(Double.valueOf(resultSet.getString(ApiConstant.TMP))));
					}
					jsonObject.put(ApiConstant.FEED_FLOW_RATE, resultSet.getDouble(ApiConstant.FEED_FLOW_RATE));
					jsonObject.put(ApiConstant.PERMEATE_FLOW_RATE, resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE));
					jsonObject.put(ApiConstant.FEED_SCALE,
							Double.valueOf(convertScale(resultSet.getDouble(ApiConstant.FEED_SCALE))));

					if (resultSet.getObject(ApiConstant.TOTAL_PERM_WEIGHT) != null) {
						jsonObject.put(ApiConstant.TOTAL_PERM_WEIGHT,
								convertScale(resultSet.getDouble(ApiConstant.TOTAL_PERM_WEIGHT)));
					} else {
						jsonObject.put(ApiConstant.TOTAL_PERM_WEIGHT, ApiConstant.NOT_APPLICABLE);
					}

					jsonObject.put(ApiConstant.ABV_1, resultSet.getString(ApiConstant.ABV_1));
					jsonObject.put(ApiConstant.ABV_2, resultSet.getString(ApiConstant.ABV_2));
					jsonObject.put(ApiConstant.AUX_PUMP_1_FLOW_RATE,
							resultSet.getDouble(ApiConstant.AUX_PUMP_1_FLOW_RATE));
					jsonObject.put(ApiConstant.AUX_PUMP_2_FLOW_RATE,
							resultSet.getDouble(ApiConstant.AUX_PUMP_2_FLOW_RATE));
					jsonObject.put(ApiConstant.CONDUCTIVITY_1,
							Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1)).map(Object::toString)
									.orElse(ApiConstant.NOT_APPLICABLE));
					jsonObject.put(ApiConstant.CONDUCTIVITY_2,
							Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2)).map(Object::toString)
									.orElse(ApiConstant.NOT_APPLICABLE));
					jsonObject.put(ApiConstant.TEMPERATURE_1,
							Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_1)).map(Object::toString)
									.orElse(ApiConstant.NOT_APPLICABLE));
					jsonObject.put(ApiConstant.TEMPERATURE_2,
							Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_2)).map(Object::toString)
									.orElse(ApiConstant.NOT_APPLICABLE));

					if (ApiConstant.CHANNEL_1_TOTALIZER_TYPE == resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE)) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							jsonObject.put(ApiConstant.KONDUIT_CH_1, HistoricalConverter
									.convertTotalizer_1(resultSet.getDouble(ApiConstant.KONDUIT_CH_1)));
						} else {
							jsonObject.put(ApiConstant.KONDUIT_CH_1, ApiConstant.NOT_APPLICABLE);
						}

					} else {
						jsonObject.put(ApiConstant.KONDUIT_CH_1,
								Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_1)).map(Object::toString)
										.orElse(ApiConstant.NOT_APPLICABLE));
					}

					if (ApiConstant.CHANNEL_2_TOTALIZER_TYPE == resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE)) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							jsonObject.put(ApiConstant.KONDUIT_CH_2, HistoricalConverter
									.convertTotalizer_2(resultSet.getDouble(ApiConstant.KONDUIT_CH_2)));
						} else {
							jsonObject.put(ApiConstant.KONDUIT_CH_2, ApiConstant.NOT_APPLICABLE);
						}

					} else {
						jsonObject.put(ApiConstant.KONDUIT_CH_2,
								Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_2)).map(Object::toString)
										.orElse(ApiConstant.NOT_APPLICABLE));
					}
					jsonObject.put(ApiConstant.KONDUIT_CH_1_TYPE, resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE));
					jsonObject.put(ApiConstant.KONDUIT_CH_2_TYPE, resultSet.getDouble(ApiConstant.KONDUIT_CH_2_TYPE));

					if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL) != null) {
						jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL, HistoricalConverter
								.convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL)));
					} else {
						jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL, ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP) != null) {
						jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP, HistoricalConverter
								.convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP)));
					} else {
						jsonObject.put(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP, ApiConstant.NOT_APPLICABLE);
					}

					resultJsonArray.put(jsonObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultJsonArray;
	}

	/**
	 * Export PDF.
	 *
	 * @param fileInputStream 
	 * @param fileLocation
	 * @return the file to write
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static final File exportPDF(final InputStream fileInputStream, final String fileLocation)
			throws IOException {
		OutputStream outputStream = null;
		try {
			int read = 0;
			final byte[] bytes = new byte[1024];
			final File fileToWrite = new File(fileLocation);
			outputStream = new FileOutputStream(fileToWrite);
			while ((read = fileInputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			return fileToWrite;
		} catch (IOException e) {
			throw new RuntimeException(ApiConstant.ERROR_DOWNLOAD_FILE);
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}

}
