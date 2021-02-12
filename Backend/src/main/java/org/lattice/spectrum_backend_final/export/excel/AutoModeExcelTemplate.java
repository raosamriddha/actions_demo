package org.lattice.spectrum_backend_final.export.excel;

import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertPressure;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertScale;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertVolume;
import static org.lattice.spectrum_backend_final.export.excel.ExcelExportMain.decimalFormat;
import static org.lattice.spectrum_backend_final.export.excel.ExcelExportMain.threeDecimalFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.EndPointSettingManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.GraphUtil;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * The Class used to set values in the auto mode excel template.
 * 
 * @author Pinak
 */
public final class AutoModeExcelTemplate implements IExcelTemplate {

	/**
	 * Populates Trial Details Sheet
	 * 
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @param trialId
	 * @param receipeJson
	 */
	@Override
	public void populateTrialDetailsSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId, String trialId,
			JSONArray receipeJson) throws Exception {
		SXSSFSheet sheet = null;

		try {
			sheet = sxssfWorkbook.getSheet("Trial details");
			if (sheet == null) {
				throw new RuntimeException("Excel sheet 'Trial details' not found in excel template, please check.");
			}
			sheet.setDefaultColumnWidth(20);
			sheet.setDefaultRowHeightInPoints(18);
			sheet.setRandomAccessWindowSize(25);

			JSONObject trialRS = ExportUtil.getInstance().fetchTrialDetails(trialRunSettingId);
			final boolean isKMPiorFS500 = ApiConstant.KMPI
					.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MAIN_PUMP_NAME))
					|| ApiConstant.KROSFLOFS500
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MAIN_PUMP_NAME));

			Font font = sxssfWorkbook.createFont();
			font.setFontName(ApiConstant.FONT_ARIAL);
			font.setFontHeightInPoints(ApiConstant.FONT_SIZE);
			font.setBold(true);

			CellStyle boldStyle = sxssfWorkbook.createCellStyle();
			boldStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			boldStyle.setWrapText(true);
			boldStyle.setAlignment(HorizontalAlignment.LEFT);
			boldStyle.setFont(font);

			CellStyle backgroundColorStyle = sxssfWorkbook.createCellStyle();
			backgroundColorStyle.cloneStyleFrom(boldStyle);
			backgroundColorStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			backgroundColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			CellStyle aquaColorStyle = sxssfWorkbook.createCellStyle();
			aquaColorStyle.cloneStyleFrom(boldStyle);
			aquaColorStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			aquaColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// auto mode
			sheet.createRow(0).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(0).setCellValue("Trial ID");
			sheet.getRow(0).createCell(1).setCellValue(trialRS.getString(ApiConstant.TRIAL_ID));
			sheet.getRow(0).createCell(2).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(2).setCellValue("Recipe name");
			sheet.getRow(0).createCell(3).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RECIPE_NAME));
			sheet.getRow(0).createCell(4).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(4).setCellValue("Type");
			sheet.getRow(0).createCell(5).setCellValue(ApiConstant.AUTO);
			sheet.getRow(0).createCell(6).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(6).setCellValue("Downloaded By");
			sheet.getRow(0).createCell(7)
					.setCellValue(DbConnectionManager.getInstance().getTokenManager().getUsername());
			sheet.getRow(0).createCell(8).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(8).setCellValue("Downloaded On");
			sheet.getRow(0).createCell(9)
					.setCellValue(BasicUtility.getInstance().convertUnixToDate(System.currentTimeMillis() / 1000));

			sheet.createRow(2).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(2).getCell(0).setCellValue("Recipe configuration");

			sheet.createRow(3).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(1).setCellValue("Trial start time");
			sheet.getRow(3).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(2).setCellValue("Trial end time");
			sheet.getRow(3).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(3).setCellValue("Total Trial Duration");
			sheet.getRow(3).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(4).setCellValue("Feed to empty");
			sheet.getRow(3).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(5).setCellValue("Run as safe");
			sheet.getRow(3).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(6).setCellValue("Endpoint calculation (Permeate scale/ totalizer)");

			sheet.createRow(4).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(4).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(4).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(6).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(5).createCell(1).setCellValue(trialRS.getString(ApiConstant.TRIAL_START_TIME));
			sheet.getRow(5).createCell(2).setCellValue(trialRS.getString(ApiConstant.TRIAL_END_TIME));
			sheet.getRow(5).createCell(3).setCellValue(DatabaseManager.getInstance().getTrialDuration(
					trialRS.getString(ApiConstant.TRIAL_START_TIME), trialRS.getString(ApiConstant.TRIAL_END_TIME)));

			if (ApiConstant.BLANK_QUOTE
					.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.FEED_TO_EMPTY))) {
				sheet.getRow(5).createCell(4).setCellValue(ApiConstant.NOT_APPLICABLE);
			} else {
				sheet.getRow(5).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FEED_TO_EMPTY));
			}

			sheet.getRow(5).createCell(5).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RUN_AS_SAFE));

			if (receipeJson.getJSONObject(0).getInt(ApiConstant.END_POINT_CAL) == 0) {
				sheet.getRow(5).createCell(6).setCellValue(ApiConstant.PERMEATE_WEIGHT);
			} else {
				sheet.getRow(5).createCell(6).setCellValue(ApiConstant.ALARM_DESCRIPTION_PERMEATE_TOTAL);
			}

			sheet.createRow(7).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(7).getCell(0).setCellValue("Trial configuration");

			sheet.createRow(8).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(8).getCell(1).setCellValue("Feed hold up");
			sheet.getRow(8).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(8).getCell(2).setCellValue("Feed hold up weight");
			sheet.getRow(8).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(8).getCell(3).setCellValue("Feed start weight");
			sheet.getRow(8).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(8).getCell(4).setCellValue("Q permeate frequency");
			sheet.getRow(8).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(8).getCell(5).setCellValue("Device ID");

			sheet.createRow(9).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(9).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(9).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			Integer qPermFreq = null;
			if (trialRS.getString(ApiConstant.Q_PERMEATE_FREQUENCY) != null
					&& !trialRS.getString(ApiConstant.Q_PERMEATE_FREQUENCY).isEmpty()) {
				qPermFreq = Integer.parseInt(trialRS.getString(ApiConstant.Q_PERMEATE_FREQUENCY));
				if (qPermFreq >= 60) {
					qPermFreq /= 60;
					sheet.getRow(9).createCell(4).setCellValue("min");
				} else {
					sheet.getRow(9).createCell(4).setCellValue("sec");
				}
			}

			String isFeedEmpty = trialRS.getString(ApiConstant.IS_FEED_EMPTY);
			if (isFeedEmpty.equalsIgnoreCase(ApiConstant.YES)) {
				isFeedEmpty = ApiConstant.EMPTY;
			} else if (isFeedEmpty.equalsIgnoreCase(ApiConstant.NO)) {
				isFeedEmpty = ApiConstant.FULL;
			}
			sheet.createRow(10).createCell(1).setCellValue(isFeedEmpty);
			sheet.getRow(10).createCell(7).setCellValue("NA = Feed start weight not entered by user");
			sheet.getRow(10).createCell(4).setCellValue(qPermFreq);
			sheet.getRow(10).createCell(5).setCellValue(trialRS.getString(ApiConstant.DEVICE_ID));

			if (isKMPiorFS500) {
				sheet.getRow(9).createCell(2).setCellValue(ApiConstant.KILOGRAM);
				if (!trialRS.getString(ApiConstant.FEED_HOLD_UP).isEmpty()) {
					sheet.getRow(10).createCell(2).setCellValue(decimalFormat
							.format(Double.parseDouble(trialRS.getString(ApiConstant.FEED_HOLD_UP)) / 1000));
				}
				sheet.getRow(9).createCell(3).setCellValue(ApiConstant.KILOGRAM);
				if (trialRS.getString(ApiConstant.FEED_START_WEIGHT) != null
						&& !trialRS.getString(ApiConstant.FEED_START_WEIGHT).isEmpty()) {
					sheet.getRow(10).createCell(3)
							.setCellValue(Double.parseDouble(trialRS.getString(ApiConstant.FEED_START_WEIGHT)) / 1000);
				} else {
					sheet.getRow(10).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
				}
			} else {
				sheet.getRow(9).createCell(2).setCellValue(ApiConstant.GRAM);
				sheet.getRow(10).createCell(2).setCellValue(trialRS.getString(ApiConstant.FEED_HOLD_UP));
				sheet.getRow(9).createCell(3).setCellValue(ApiConstant.GRAM);
				if (trialRS.getString(ApiConstant.FEED_START_WEIGHT) != null
						&& !trialRS.getString(ApiConstant.FEED_START_WEIGHT).isEmpty()) {
					sheet.getRow(10).createCell(3).setCellValue(trialRS.getString(ApiConstant.FEED_START_WEIGHT));
				} else {
					sheet.getRow(10).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
				}
			}

			sheet.createRow(12).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(13).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(13).getCell(0).setCellValue("Endpoint(s), target(s)");

			sheet.createRow(14).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(14).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(2).setCellValue("Endpoint 1");
			sheet.getRow(14).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(3).setCellValue("Endpoint 2");
			sheet.getRow(14).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(4).setCellValue("Endpoint 3");
			sheet.getRow(14).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(5).setCellValue("Endpoint 4");

			sheet.createRow(15).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(15).getCell(0).setCellValue("Mode");
			sheet.getRow(15).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(15).getCell(1).setCellValue("Endpoint type");

			sheet.createRow(16).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(16).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(16).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);

			sheet.createRow(17).createCell(0)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME));

			if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).isEmpty()) {

				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).isNull(0)) {
					final boolean isPermWt = placeEndPointTypeAndUnits(sheet,
							receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).getJSONObject(0), 2, 16,
							isKMPiorFS500);

					if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
									.getJSONObject(0).getString(ApiConstant.END_POINT_TYPE))) {

						sheet.getRow(17).createCell(2)
								.setCellValue(HistoricalConverter.convertVolume(
										Double.valueOf(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
												.getJSONObject(0).getString(ApiConstant.END_POINT_VALUE))));
					} else {
						if (isPermWt && isKMPiorFS500) {
							sheet.getRow(17).createCell(2).setCellValue(Double.parseDouble(decimalFormat.format(
									Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(0).getString(ApiConstant.END_POINT_VALUE)) / 1000)));
						} else {
							sheet.getRow(17).createCell(2)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(0).getString(ApiConstant.END_POINT_VALUE));
						}
					}

				}
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).isNull(1)) {
					final boolean isPermWt = placeEndPointTypeAndUnits(sheet,
							receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).getJSONObject(1), 3, 16,
							isKMPiorFS500);
					if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
									.getJSONObject(1).getString(ApiConstant.END_POINT_TYPE))) {

						sheet.getRow(17).createCell(3)
								.setCellValue(HistoricalConverter.convertVolume(
										Double.valueOf(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
												.getJSONObject(1).getString(ApiConstant.END_POINT_VALUE))));
					} else {
						if (isPermWt && isKMPiorFS500) {
							sheet.getRow(17).createCell(3).setCellValue(Double.parseDouble(decimalFormat.format(
									Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(1).getString(ApiConstant.END_POINT_VALUE)) / 1000)));
						} else {
							sheet.getRow(17).createCell(3)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(1).getString(ApiConstant.END_POINT_VALUE));
						}
					}
				}
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).isNull(2)) {
					final boolean isPermWt = placeEndPointTypeAndUnits(sheet,
							receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).getJSONObject(2), 4, 16,
							isKMPiorFS500);
					if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
									.getJSONObject(2).getString(ApiConstant.END_POINT_TYPE))) {

						sheet.getRow(17).createCell(4)
								.setCellValue(HistoricalConverter.convertVolume(
										Double.valueOf(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
												.getJSONObject(2).getString(ApiConstant.END_POINT_VALUE))));

					} else {
						if (isPermWt && isKMPiorFS500) {
							sheet.getRow(17).createCell(4).setCellValue(Double.parseDouble(decimalFormat.format(
									Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(2).getString(ApiConstant.END_POINT_VALUE)) / 1000)));
						} else {
							sheet.getRow(17).createCell(4)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(2).getString(ApiConstant.END_POINT_VALUE));
						}
					}

				}
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).isNull(3)) {
					final boolean isPermWt = placeEndPointTypeAndUnits(sheet,
							receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS).getJSONObject(3), 5, 16,
							isKMPiorFS500);

					if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
									.getJSONObject(3).getString(ApiConstant.END_POINT_TYPE))) {

						sheet.getRow(17).createCell(5)
								.setCellValue(HistoricalConverter.convertVolume(
										Double.valueOf(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
												.getJSONObject(3).getString(ApiConstant.END_POINT_VALUE))));

					} else {
						if (isPermWt && isKMPiorFS500) {
							sheet.getRow(17).createCell(5).setCellValue(Double.parseDouble(decimalFormat.format(
									Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(3).getString(ApiConstant.END_POINT_VALUE)) / 1000)));
						} else {
							sheet.getRow(17).createCell(5)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.END_POINTS)
											.getJSONObject(3).getString(ApiConstant.END_POINT_VALUE));
						}
					}
				}
			}

			// main pump
			sheet.createRow(19).createCell(0).setCellValue("-- = Units are not applicable.");
			sheet.createRow(20).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(20).getCell(0).setCellValue("Main pump");

			sheet.createRow(21).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(21).getCell(0).setCellValue("Main pump model");
			sheet.getRow(21).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(2).setCellValue("No of Head(s)");
			sheet.getRow(21).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(3).setCellValue("Direction");
			sheet.getRow(21).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(4).setCellValue("Flow control");
			sheet.getRow(21).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(5).setCellValue("Tubing");
			sheet.getRow(21).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(6).setCellValue("Delta P");
			sheet.getRow(21).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(7).setCellValue("Pump rate change(%)");
			sheet.getRow(21).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(8).setCellValue("Flow rate");
			sheet.getRow(21).createCell(9).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(9).setCellValue("Pump speed");
			sheet.getRow(21).createCell(10).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(10).setCellValue("Ramp up time (1-15 s)");

			sheet.createRow(22).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(22).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(22).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(22).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(22).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(22).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(22).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(23).createCell(0)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.MAIN_PUMP_NAME));
			sheet.getRow(23).createCell(2).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.HEAD_COUNT));
			sheet.getRow(23).createCell(3).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.DIRECTION));
			sheet.getRow(23).createCell(4)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_CONTROL));
			sheet.getRow(23).createCell(5)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PUMP_TUBING_SIZE));

			String deltaP = null;
			if (receipeJson.getJSONObject(0).has(ApiConstant.DELTA_P)) {
				deltaP = receipeJson.getJSONObject(0).getString(ApiConstant.DELTA_P);
			}
			if (deltaP != null && !deltaP.isEmpty()) {
				sheet.getRow(22).createCell(6).setCellValue(
						Converter.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
				sheet.getRow(23).createCell(6)
						.setCellValue(HistoricalConverter.convertPressure(Double.parseDouble(deltaP)));
				sheet.getRow(22).createCell(7)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.DELTA_P_DURATION));
				sheet.getRow(23).createCell(7)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.DELTA_P_RATE));
			} else {
				sheet.getRow(23).createCell(6).setCellValue(ApiConstant.NOT_APPLICABLE);
			}

			if (receipeJson.getJSONObject(0).has(ApiConstant.FLOW_RATE)
					&& receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE) != null
					&& !receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE).isEmpty()) {
				if (isKMPiorFS500) {
					sheet.getRow(22).createCell(8).setCellValue(ApiConstant.LITER_PER_MIN);
					sheet.getRow(23).createCell(8).setCellValue(Double.parseDouble(decimalFormat.format(
							Double.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE)) / 1000)));
				} else {
					sheet.getRow(22).createCell(8).setCellValue(ApiConstant.ML_MIN);
					sheet.getRow(23).createCell(8)
							.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
				}
			}

			sheet.getRow(22).createCell(9).setCellValue(ApiConstant.RPM);
			sheet.getRow(23).createCell(9).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PUMP_RPM));

			sheet.getRow(22).createCell(10).setCellValue("s");
			sheet.getRow(23).createCell(10)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RAMP_UP_TIME));

			// Hollow fiber
			sheet.createRow(25).createCell(0).setCellValue("-- = Units are not applicable.");
			sheet.getRow(25).createCell(6).setCellValue("NA = User selected 'No'");

			sheet.createRow(26).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(26).setHeightInPoints(26);
			sheet.getRow(26).getCell(0).setCellValue("Hollow fiber filter and permeate tubing");

			sheet.createRow(27).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(27).getCell(0).setCellValue("Hollow fiber filter");
			sheet.getRow(27).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(1).setCellValue("Non-Repligen Filter");
			sheet.getRow(27).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(2).setCellValue("Part #");
			sheet.getRow(27).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(3).setCellValue("Fiber count");
			sheet.getRow(27).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(4).setCellValue("Fiber ID");
			sheet.getRow(27).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(5).setCellValue("Surface area");
			sheet.getRow(27).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(6).setCellValue("Extra Capillary Space (ECS)");
			sheet.getRow(27).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(7).setCellValue("Model number (Non-Repligen Filter)");
			sheet.getRow(27).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(27).getCell(8).setCellValue("Manufacturer name (Non-Repligen Filter)");

			sheet.createRow(28).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(28).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(28).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(28).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(28).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(28).createCell(4).setCellValue(ApiConstant.MILLIMETER);
			sheet.getRow(28).createCell(5).setCellValue("sq cm");
			sheet.getRow(28).createCell(6).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(28).createCell(7).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(28).createCell(8).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(30).createCell(4).setCellValue("NA - User selected Non-Repligen filter");

			// Permeate tubing
			sheet.createRow(31).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(32).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(32).getCell(0).setCellValue("Permeate tubing");
			sheet.getRow(32).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(1).setCellValue("Permeate Holdup Volume used? (Y/N)");
			sheet.getRow(32).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(2).setCellValue("Permeate tube part #");
			sheet.getRow(32).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(3).setCellValue("Auto calculate permeate tubing holdup? (Y/N)");
			sheet.getRow(32).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(4).setCellValue("Permeate tube length");
			sheet.getRow(32).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(5).setCellValue("Permeate hold up (tubing)");
			sheet.getRow(32).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(6).setCellValue("Total permeate hold up (ECS + Perm hold up)");

			sheet.createRow(33).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(33).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(33).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(33).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(33).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(33).createCell(5).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(33).createCell(6).setCellValue(ApiConstant.MILLILITER);

			sheet.createRow(35).createCell(4)
					.setCellValue("NA = User selected to manually enter permeate tubing holdup");
			sheet.getRow(35).createCell(5).setCellValue("NA = User selected 'No permeate tubing holdup in system'");
			sheet.getRow(35).createCell(6).setCellValue(
					"* If value is zero, this means user has selected not to use permeate holdup in calculations");

			// Cassette
			sheet.createRow(36).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(37).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(37).getCell(0).setCellValue("Cassette filter and permeate tubing");

			sheet.createRow(38).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(38).getCell(0).setCellValue("Cassette");
			sheet.getRow(38).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(1).setCellValue("Non-Repligen Filter");
			sheet.getRow(38).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(2).setCellValue("Part #");
			sheet.getRow(38).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(3).setCellValue("Width");
			sheet.getRow(38).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(4).setCellValue("Height");
			sheet.getRow(38).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(5).setCellValue("Number of cassettes");
			sheet.getRow(38).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(6).setCellValue("Number of channels");
			sheet.getRow(38).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(7).setCellValue("Model number (Non-Repligen Filter)");
			sheet.getRow(38).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(38).getCell(8).setCellValue("Manufacturer name (Non-Repligen Filter)");

			sheet.createRow(39).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(39).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(39).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(39).createCell(3).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(39).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(39).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(39).createCell(6).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(39).createCell(7).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(39).createCell(8).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(41).createCell(4).setCellValue("NA - User selected Non-Repligen cassette");

			sheet.createRow(43).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(43).getCell(1).setCellValue("Surface area");
			sheet.getRow(43).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(43).getCell(2).setCellValue("Total surface area");
			sheet.getRow(43).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(43).getCell(3).setCellValue("Extra Capillary Space  (ECS, per cassette)");
			sheet.getRow(43).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(43).getCell(4).setCellValue("Total ECS (ECS * no of cassettes)");

			sheet.createRow(44).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(44).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(44).createCell(1).setCellValue("sq m");
			sheet.getRow(44).createCell(2).setCellValue("sq m");
			sheet.getRow(44).createCell(3).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(44).createCell(4).setCellValue(ApiConstant.MILLILITER);

			// filter plate
			sheet.createRow(47).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(49).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(49).getCell(0).setCellValue("Filter plate insert");
			sheet.getRow(49).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(49).getCell(1).setCellValue("Filter plate insert model number");
			sheet.getRow(49).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(49).getCell(2).setCellValue("Filter plate insert hold up");

			sheet.createRow(50).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(50).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(50).createCell(2).setCellValue(ApiConstant.MILLILITER);

			sheet.createRow(52).createCell(1)
					.setCellValue("NA - User has not selected filter plate insert model number");

			sheet.createRow(53).createCell(0).setCellValue("-- = Units are not applicable.");

			// Permeate tubing
			sheet.createRow(54).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(54).getCell(0).setCellValue("Permeate tubing");
			sheet.getRow(54).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(54).getCell(1).setCellValue("Permeate Holdup Volume used? (Y/N)");
			sheet.getRow(54).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(54).getCell(2).setCellValue("Permeate tube part #");
			sheet.getRow(54).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(54).getCell(3).setCellValue("Auto calculate permeate tubing holdup? (Y/N)");
			sheet.getRow(54).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(54).getCell(4).setCellValue("Permeate tube length");
			sheet.getRow(54).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(54).getCell(5).setCellValue("Permeate hold up (tubing)");
			sheet.getRow(54).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(54).getCell(6).setCellValue("Total permeate hold up (ECS + Perm hold up)");

			sheet.createRow(55).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(55).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(55).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(55).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(55).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(55).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(55).createCell(5).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(55).createCell(6).setCellValue(ApiConstant.MILLILITER);

			sheet.createRow(57).createCell(4)
					.setCellValue("NA = User selected to manually enter permeate tubing holdup");
			sheet.getRow(57).createCell(5).setCellValue("NA = User selected 'No permeate tubing holdup in system'");
			sheet.getRow(57).createCell(6).setCellValue(
					"* If value is zero, this means user has selected not to use permeate holdup in calculations");

			if (receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE)
					.equalsIgnoreCase(ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER)) {
				if (receipeJson.getJSONObject(0).getBoolean(ApiConstant.IS_GENERIC)) {
					sheet.createRow(29).createCell(1).setCellValue(ApiConstant.YES);
				} else {
					sheet.createRow(29).createCell(1).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(29).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PART_NO));
				sheet.getRow(29).createCell(3)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FIBER_COUNT));
				sheet.getRow(29).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FIBER_ID));
				sheet.getRow(29).createCell(5)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.SURF_AREA));
				sheet.getRow(29).createCell(6).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.ECS));
				sheet.getRow(29).createCell(7)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MODEL_NAME));
				sheet.getRow(29).createCell(8)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MANUFACTURER_NAME));

				sheet.createRow(34).createCell(1).setCellValue(
						receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
				sheet.getRow(34).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_TUBE_SIZE));
				if (receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH).isEmpty()) {
					sheet.getRow(34).createCell(3).setCellValue(ApiConstant.YES);
				} else {
					sheet.getRow(34).createCell(3).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(34).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH));
				sheet.getRow(34).createCell(5)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERM_TUBE_HOLDUP));
				sheet.getRow(34).createCell(6)
						.setCellValue(receipeJson.getJSONObject(0).getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));

			} else if (ApiConstant.CASSETTE
					.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE))) {
				if (receipeJson.getJSONObject(0).getBoolean(ApiConstant.IS_GENERIC)) {
					sheet.createRow(40).createCell(1).setCellValue(ApiConstant.YES);
				} else {
					sheet.createRow(40).createCell(1).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(40).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PART_NO));
				sheet.getRow(40).createCell(3).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.WIDTH));
				sheet.getRow(40).createCell(4).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.HEIGHT));
				final double noOfCassette = Double
						.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.NO_OF_CASSETTE));
				sheet.getRow(40).createCell(5).setCellValue(noOfCassette);
				sheet.getRow(40).createCell(6)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.NO_OF_CHANNEL));
				sheet.getRow(40).createCell(7)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MODEL_NAME));
				sheet.getRow(40).createCell(8)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MANUFACTURER_NAME));

				final double surfaceArea = Double
						.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.SURF_AREA));
				sheet.createRow(45).createCell(1).setCellValue(surfaceArea);
				sheet.getRow(45).createCell(2).setCellValue(surfaceArea * noOfCassette);
				final double ecs = Double.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.ECS));
				sheet.getRow(45).createCell(3).setCellValue(ecs);
				sheet.getRow(45).createCell(4).setCellValue(ecs * noOfCassette);

				if (!receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO).isEmpty()) {
					// get filter plate lookup
					sheet.createRow(51).createCell(1).setCellValue(
							receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO));
					sheet.getRow(51).createCell(2).setCellValue(ExportUtil.getInstance().getFilterHoldUpUp(
							receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO)));
				}

				sheet.createRow(56).createCell(1).setCellValue(
						receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
				sheet.getRow(56).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_TUBE_SIZE));
				if (receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH).isEmpty()) {
					sheet.getRow(56).createCell(3).setCellValue(ApiConstant.YES);
				} else {
					sheet.getRow(56).createCell(3).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(56).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH));
				sheet.getRow(56).createCell(5)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERM_TUBE_HOLDUP));
				sheet.getRow(56).createCell(6)
						.setCellValue(receipeJson.getJSONObject(0).getInt(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));
			}

			sheet.createRow(59).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(60).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(60).getCell(0).setCellValue("Aux pump 1");

			sheet.createRow(61).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(61).getCell(0).setCellValue("Aux pump 1 model");
			sheet.getRow(61).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(61).getCell(2).setCellValue("Tubing");
			sheet.getRow(61).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(61).getCell(3).setCellValue("Pump function");
			sheet.getRow(61).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(61).getCell(4).setCellValue("Flow rate");
			sheet.getRow(61).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(61).getCell(5).setCellValue("Permeate stop first");
			sheet.getRow(61).createCell(6).setCellValue("Info : Permeate stop first is only for permeate pump");

			sheet.createRow(62).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(62).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(62).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(62).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(62).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(62).createCell(4).setCellValue(ApiConstant.ML_MIN);
			sheet.getRow(62).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			// aux pump 2
			sheet.createRow(65).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(66).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(66).getCell(0).setCellValue("Aux pump 2");

			sheet.createRow(67).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(67).getCell(0).setCellValue("Aux pump 2 model");
			sheet.getRow(67).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(67).getCell(2).setCellValue("Tubing");
			sheet.getRow(67).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(67).getCell(3).setCellValue("Pump function");
			sheet.getRow(67).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(67).getCell(4).setCellValue("Flow rate");
			sheet.getRow(67).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(67).getCell(5).setCellValue("Permeate stop first");

			sheet.createRow(68).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(68).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(68).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(68).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(68).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(68).createCell(4).setCellValue(ApiConstant.ML_MIN);
			sheet.getRow(68).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.getRow(68).createCell(6).setCellValue("Info : Permeate stop first is only for permeate pump");

			// aux pumps array
			if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isEmpty()) {
				// aux pump 1
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isNull(0)) {

					if ("0".equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
							.getJSONObject(0).getString(ApiConstant.TYPE))) {
						sheet.createRow(63).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(63).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(63).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.PUMP_FUNCTION));
						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(62).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(63).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(0).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(62).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(63).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
							}

						}
						sheet.getRow(63).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					} else {
						sheet.createRow(69).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(69).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(69).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.PUMP_FUNCTION));
						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(68).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(69).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(0).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {
							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(68).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(69).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
							}

						}
						sheet.getRow(69).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					}

				}

				// aux pump 2
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isNull(1)) {

					if ("0".equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
							.getJSONObject(1).getString(ApiConstant.TYPE))) {
						sheet.createRow(63).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(63).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(63).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.PUMP_FUNCTION));
						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(62).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(63).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(1).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(62).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(63).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(1).getString(ApiConstant.FLOW_RATE));
							}

						}
						sheet.getRow(63).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					} else {
						sheet.createRow(69).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(69).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(69).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.PUMP_FUNCTION));
						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(68).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(69).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(1).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {
							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(68).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(69).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(1).getString(ApiConstant.FLOW_RATE));
							}

						}
						sheet.getRow(69).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					}
				}
			}

			// abv 1
			sheet.createRow(71).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(72).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(72).getCell(0).setCellValue("ABV 1");

			sheet.createRow(73).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(73).getCell(0).setCellValue("Operating pressure control mode");
			sheet.getRow(73).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(73).getCell(2).setCellValue("ABV Type");
			sheet.getRow(73).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(73).getCell(3).setCellValue("Mode");
			sheet.getRow(73).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(73).getCell(4).setCellValue("% closed (at start)");
			sheet.getRow(73).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(73).getCell(5).setCellValue("Operating pressure");

			sheet.createRow(74).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(74).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(74).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(74).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(74).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(74).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(74).createCell(5).setCellValue(ApiConstant.PSI);

			sheet.getRow(74).createCell(6).setCellValue("NA - No operating pressure required for manual ABV control");

			// abv 2
			sheet.createRow(77).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(78).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(78).getCell(0).setCellValue("ABV 2");

			sheet.createRow(79).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(79).getCell(0).setCellValue("Operating pressure control mode");
			sheet.getRow(79).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(79).getCell(2).setCellValue("ABV Type");
			sheet.getRow(79).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(79).getCell(3).setCellValue("Mode");
			sheet.getRow(79).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(79).getCell(4).setCellValue("% closed (at start)");
			sheet.getRow(79).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(79).getCell(5).setCellValue("Operating pressure");

			sheet.createRow(80).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(80).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(80).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(80).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(80).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(80).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(80).createCell(5).setCellValue(ApiConstant.PSI);

			sheet.getRow(80).createCell(6).setCellValue("NA - No operating pressure required for manual ABV control");

			// abv array
			if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).isEmpty()) {
				// abv 1
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).isNull(0)) {

					sheet.createRow(75).createCell(0).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.CONT_BASED_ON));

					sheet.getRow(75).createCell(2).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.ABV_TYPE));
					sheet.getRow(75).createCell(3).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.ABV_MODE));

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(0)
							.getString(ApiConstant.PERCENT_CLOSED).isEmpty()) {
						sheet.getRow(75).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.PERCENT_CLOSED));
					} else {
						sheet.getRow(75).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.START_POS));
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(0)
							.getString(ApiConstant.OP_PRESSURE).isEmpty()) {
						sheet.getRow(74).createCell(5).setCellValue(Converter
								.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
						sheet.getRow(75).createCell(5)
								.setCellValue(HistoricalConverter.convertPressure(
										Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV)
												.getJSONObject(0).getString(ApiConstant.OP_PRESSURE))));
					}
				}

				// abv 2
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).isNull(1)) {
					sheet.createRow(81).createCell(0).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.CONT_BASED_ON));

					sheet.getRow(81).createCell(2).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.ABV_TYPE));

					sheet.getRow(81).createCell(3).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.ABV_MODE));

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(1)
							.getString(ApiConstant.PERCENT_CLOSED).isEmpty()) {
						sheet.getRow(81).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.PERCENT_CLOSED));
					} else {
						sheet.getRow(81).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.START_POS));
					}

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(1)
							.getString(ApiConstant.OP_PRESSURE).isEmpty()) {
						sheet.getRow(80).createCell(5).setCellValue(Converter
								.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
						sheet.getRow(81).createCell(5)
								.setCellValue(HistoricalConverter.convertPressure(
										Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV)
												.getJSONObject(1).getString(ApiConstant.OP_PRESSURE))));
					}
				}
			}

			sheet.createRow(83).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(84).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(84).getCell(0).setCellValue("Retentate tubing");
			sheet.getRow(84).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(84).getCell(2).setCellValue("Retentate tubing #");

			sheet.createRow(85).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(85).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(85).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(87).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(88).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(88).getCell(0).setCellValue("Recirculation pressure control");
			sheet.getRow(88).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(88).getCell(2).setCellValue("Recirculation pressure control (Yes/ No)");
			sheet.getRow(88).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(88).getCell(3).setCellValue("% value");

			sheet.createRow(89).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(89).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(89).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.getRow(89).createCell(6).setCellValue("NA - If recirc control is selected as no, % value will be NA");

			sheet.createRow(86).createCell(2)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RETENTATE_TUBE_SIZE));
			sheet.createRow(90).createCell(2)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL));
			if (receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL)
					.equalsIgnoreCase(ApiConstant.NO)) {
				sheet.getRow(90).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
			} else {
				sheet.getRow(89).createCell(3).setCellValue(
						receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_PRESSURE_UNIT_VALUE));
				sheet.getRow(90).createCell(3)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_RATE_PER_UNIT));
			}

			// konduit
			sheet.createRow(92).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(93).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(93).getCell(0).setCellValue("KF KONDUiT");

			sheet.createRow(94).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(94).getCell(0).setCellValue("Channel Details");
			sheet.getRow(94).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(94).getCell(1).setCellValue("value");
			sheet.getRow(94).createCell(6).setCellValue("NA - Set value not defined");

			sheet.createRow(95).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(95).getCell(0).setCellValue("UV/ AUX Ch 1");
			sheet.createRow(96).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(96).getCell(0).setCellValue("UV/ AUX Ch 2");
			sheet.createRow(97).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(97).getCell(0).setCellValue("K Factor Channel 1");
			sheet.createRow(98).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(98).getCell(0).setCellValue("K Factor Channel 2");

			sheet.createRow(100).createCell(0).setCellValue("Aux = pH/ Turbidity/ Flowmeter/ Protein concentration");

			if (receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_1).isEmpty()) {
				sheet.getRow(95).createCell(1).setCellValue("no");
			} else {
				sheet.getRow(95).createCell(1)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_1));
			}

			if (receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_2).isEmpty()) {
				sheet.getRow(96).createCell(1).setCellValue("no");
			} else {
				sheet.getRow(96).createCell(1)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_2));
			}

			sheet.getRow(97).createCell(1)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.K_FACTOR_CH_1));
			sheet.getRow(98).createCell(1)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.K_FACTOR_CH_2));

			// endpoint
			sheet.createRow(101).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(102).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(102).getCell(0).setCellValue("Modified trial endpoint(s)/setpoint(s)");
			sheet.getRow(102).createCell(1).setCellValue("-- = Units are not applicable.");

			sheet.createRow(103).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(103).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(103).getCell(2).setCellValue("Endpoint 1");
			sheet.getRow(103).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(103).getCell(3).setCellValue("Endpoint 2");
			sheet.getRow(103).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(103).getCell(4).setCellValue("Endpoint 3");
			sheet.getRow(103).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(103).getCell(5).setCellValue("Endpoint 4");
			sheet.getRow(103).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(103).getCell(6).setCellValue("Username");
			sheet.getRow(103).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(103).getCell(7).setCellValue("Timestamp");

			sheet.createRow(104).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(104).getCell(0).setCellValue("Mode");
			sheet.getRow(104).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(104).getCell(1).setCellValue("Endpoint type");
			sheet.createRow(105).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(105).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(105).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);

			JSONArray endPointsJsonArray = EndPointSettingManager.getInstance().getEndPointChangeLog(trialRunSettingId);

			if (!endPointsJsonArray.isEmpty()) {
				sheet.createRow(106).createCell(0)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME));
				int endPointRow = 106;
				SXSSFRow sxssfRow = null;
				for (int i = 0; i < endPointsJsonArray.length(); i++) {
					JSONObject jsonObject = endPointsJsonArray.getJSONObject(i);
					if (jsonObject != null) {
						if (!ApiConstant.DELTA_P_ABSOLUTE_NAME
								.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
							sxssfRow = sheet.getRow(endPointRow);
							if (sxssfRow == null) {
								sxssfRow = sheet.createRow(endPointRow);
							}
							final int stepNo = jsonObject.getInt(ApiConstant.STEP_NUMBER);
							if (stepNo == 1) {
								final boolean isPermWt = placeEndPointTypeAndUnits(sheet, jsonObject, 2, 105,
										isKMPiorFS500);
								if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
									sheet.getRow(endPointRow).createCell(2)
											.setCellValue(HistoricalConverter.convertVolume(
													jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE)));
								} else {
									if (isPermWt && isKMPiorFS500) {
										sheet.getRow(endPointRow).createCell(2).setCellValue(decimalFormat.format(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE) / 1000));
									} else {
										sheet.getRow(endPointRow).createCell(2).setCellValue(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
									}
								}

							} else if (stepNo == 2) {
								final boolean isPermWt = placeEndPointTypeAndUnits(sheet, jsonObject, 3, 105,
										isKMPiorFS500);
								if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
									sheet.getRow(endPointRow).createCell(3)
											.setCellValue(HistoricalConverter.convertVolume(
													jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE)));
								} else {
									if (isPermWt && isKMPiorFS500) {
										sheet.getRow(endPointRow).createCell(3).setCellValue(decimalFormat.format(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE) / 1000));
									} else {
										sheet.getRow(endPointRow).createCell(3).setCellValue(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
									}
								}

							} else if (stepNo == 3) {
								final boolean isPermWt = placeEndPointTypeAndUnits(sheet, jsonObject, 4, 105,
										isKMPiorFS500);
								if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
									sheet.getRow(endPointRow).createCell(4)
											.setCellValue(HistoricalConverter.convertVolume(
													jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE)));
								} else {
									if (isPermWt && isKMPiorFS500) {
										sheet.getRow(endPointRow).createCell(4).setCellValue(decimalFormat.format(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE) / 1000));
									} else {
										sheet.getRow(endPointRow).createCell(4).setCellValue(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
									}
								}

							} else if (stepNo == 4) {
								final boolean isPermWt = placeEndPointTypeAndUnits(sheet, jsonObject, 5, 105,
										isKMPiorFS500);
								if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
									sheet.getRow(endPointRow).createCell(5)
											.setCellValue(HistoricalConverter.convertVolume(
													jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE)));
								} else {
									if (isPermWt && isKMPiorFS500) {
										sheet.getRow(endPointRow).createCell(5).setCellValue(decimalFormat.format(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE) / 1000));
									} else {
										sheet.getRow(endPointRow).createCell(5).setCellValue(
												jsonObject.getDouble(ApiConstant.NEW_END_POINT_CHANGE_VALUE));
									}
								}

							}
							sheet.getRow(endPointRow).createCell(6)
									.setCellValue(jsonObject.getString(ApiConstant.USERNAME));
							sheet.getRow(endPointRow).createCell(7)
									.setCellValue(jsonObject.getString(ApiConstant.CREATED_ON));
							endPointRow++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			if (sheet != null) {
				sheet.flushRows();
			}
			System.gc();
		}
	}

	/**
	 * Place end point type and units.
	 *
	 * @param sheet      the SXSSFSheet
	 * @param jsonObject
	 * @param col        the column number
	 * @param row        the row number
	 * @param isKMPi     is the main pump KMPi
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	private boolean placeEndPointTypeAndUnits(SXSSFSheet sheet, JSONObject jsonObject, int col, int row,
			final boolean isKMPi) throws SQLException {
		sheet.getRow(row - 1).createCell(col).setCellValue(jsonObject.getString(ApiConstant.END_POINT_TYPE));
		boolean isPermWt = false;
		if (ApiConstant.PERMEATE_WEIGHT.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			isPermWt = true;
			if (isKMPi) {
				sheet.getRow(row).createCell(col).setCellValue(ApiConstant.KILOGRAM);
			} else {
				sheet.getRow(row).createCell(col).setCellValue(ApiConstant.GRAM);
			}
		} else if (ApiConstant.DIAFILTRATION_VOLUME
				.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(ApiConstant.X);
		} else if (ApiConstant.CONCENTRATION_FACTOR
				.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(ApiConstant.X);
		} else if (ApiConstant.UV.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(ApiConstant.UNIT_FOR_UV);
		} else if (ApiConstant.PH.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue("-");
		} else if (ApiConstant.TURBIDITY.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(ApiConstant.UNIT_FOR_TURBIDITY);
		} else if (ApiConstant.PERMEATE_TOTAL_ENDPOINT
				.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(
					Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
		} else if (ApiConstant.CONDUCTIVITY.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(ApiConstant.UNIT_FOR_CONDUCTIVITY);
		} else if (ApiConstant.PROTEIN_CONCENTRATION
				.equalsIgnoreCase(jsonObject.getString(ApiConstant.END_POINT_TYPE))) {
			sheet.getRow(row).createCell(col).setCellValue(ApiConstant.UNIT_FOR_PROTEIN_CONCENTRATION);
		}
		return isPermWt;
	}

	/**
	 * Populates Trial Table Sheet
	 * 
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @param receipeJson
	 * @param modeName
	 * @param interval
	 */
	@Override
	public void populateTrialTableSheet(SXSSFWorkbook sxssfWorkbook, final int trialRunSettingId, JSONArray receipeJson,
			String modeName, final int interval) throws Exception {
		Connection con = null;
		PreparedStatement fetchTableLogStmt = null;
		ResultSet resultSet = null;
		String query = null;
		SXSSFSheet sheet = null;
		try {
			sheet = sxssfWorkbook.getSheet("Trial tables");
			if (sheet == null) {
				throw new RuntimeException("Excel sheet 'Trial tables' not found in excel template, please check.");
			}
			sheet.setRandomAccessWindowSize(20);
			con = DbConnectionManager.getInstance().getConnection();
			if (interval == 1) {
				query = QueryConstant.FETCH_TRIAL_TABLE_LOGS;
				fetchTableLogStmt = con.prepareStatement(query);
				sLog.d(this, QueryConstant.FETCH_TRIAL_TABLE_LOGS);
			} else {
				query = GraphUtil.getInstance().getQuery(trialRunSettingId,
						QueryConstant.FETCH_TRIAL_TABLE_LOGS_INTERVAL, interval);
				if (query != null) {
					sLog.d(this, QueryConstant.FETCH_TRIAL_TABLE_LOGS_INTERVAL);
					fetchTableLogStmt = con.prepareStatement(query);
				}
			}
			if (query != null) {
				fetchTableLogStmt.setInt(1, trialRunSettingId);
				resultSet = fetchTableLogStmt.executeQuery();

				int index = 1;
				SXSSFRow row = sheet.getRow(index);
				if (row == null) {
					row = sheet.createRow(index);
				}

				CellStyle cellStyle = sxssfWorkbook.createCellStyle();
				cellStyle.setFont(ExportUtil.getInstance().getBoldItalicFont(sxssfWorkbook));

				row.createCell(0).setCellStyle(cellStyle);
				row.getCell(0).setCellValue(ApiConstant.UNITS);

				CellStyle cellStyle2 = sxssfWorkbook.createCellStyle();
				cellStyle2.setFont(ExportUtil.getInstance().getItalicFont(sxssfWorkbook));

				// For each cell in the row, Set the style
				for (int i = 1; i <= 45; i++) {
					row.createCell(i).setCellStyle(cellStyle2);
				}

				row.getCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
				row.getCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);

				boolean isIP1 = false;
				boolean isIP2 = false;
				final boolean isKMPiorFS500 = ApiConstant.KMPI
						.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MAIN_PUMP_NAME))
						|| ApiConstant.KROSFLOFS500
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MAIN_PUMP_NAME));

				for (int i = 3; i <= 7; i++) {
					row.getCell(i).setCellValue(Converter
							.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
				}

				if (isKMPiorFS500) {
					for (int i = 8; i <= 10; i++) {
						row.getCell(i).setCellValue(ApiConstant.LITER_PER_MIN);
					}
				} else {
					for (int i = 8; i <= 10; i++) {
						row.getCell(i).setCellValue(ApiConstant.ML_MIN);
					}
				}

				row.getCell(11).setCellValue(ApiConstant.RPM);

				for (int i = 12; i <= 14; i++) {
					row.getCell(i).setCellValue(
							Converter.systemSettingWeightMapper(RequestHandler.getStSystemSetting().getWeightUnit()));
				}

				row.getCell(15).setCellValue(
						Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
				row.getCell(16).setCellValue(
						Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
				row.getCell(17).setCellValue(ApiConstant.X);
				row.getCell(18).setCellValue(ApiConstant.X);
				row.getCell(19).setCellValue(ApiConstant.X);
				row.getCell(20).setCellValue(ApiConstant.PERCENT);
				row.getCell(21).setCellValue(ApiConstant.PERCENT);
				row.getCell(22).setCellValue(ApiConstant.ML_MIN);
				row.getCell(23).setCellValue(ApiConstant.ML_MIN);

				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isEmpty()) {
					// aux pump 1
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isNull(0)) {

						if ("0".equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
								.getJSONObject(0).getString(ApiConstant.TYPE))) {
							if (ApiConstant.IP
									.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
											.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE))) {
								isIP1 = true;
								row.getCell(22).setCellValue(ApiConstant.LITER_PER_MIN);
							} else {
								row.getCell(22).setCellValue(ApiConstant.ML_MIN);
							}
						} else {
							if (ApiConstant.IP
									.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
											.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE))) {
								isIP1 = true;
								row.getCell(23).setCellValue(ApiConstant.LITER_PER_MIN);
							} else {
								row.getCell(23).setCellValue(ApiConstant.ML_MIN);
							}
						}
					}

					// aux pump 2
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isNull(1)) {

						if ("0".equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
								.getJSONObject(1).getString(ApiConstant.TYPE))) {
							if (ApiConstant.IP
									.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
											.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE))) {
								isIP2 = true;
								row.getCell(22).setCellValue(ApiConstant.LITER_PER_MIN);
							} else {
								row.getCell(22).setCellValue(ApiConstant.ML_MIN);
							}
						} else {
							if (ApiConstant.IP
									.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
											.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE))) {
								isIP2 = true;
								row.getCell(23).setCellValue(ApiConstant.LITER_PER_MIN);
							} else {
								row.getCell(23).setCellValue(ApiConstant.ML_MIN);
							}
						}
					}
				}

				row.getCell(24).setCellValue(ApiConstant.UNIT_FOR_VT);
				row.getCell(25).setCellValue(ApiConstant.UNIT_FOR_CONDUCTIVITY);
				row.getCell(26).setCellValue(ApiConstant.UNIT_FOR_CONDUCTIVITY);
				row.getCell(27).setCellValue(ApiConstant.CELSIUS);
				row.getCell(28).setCellValue(ApiConstant.CELSIUS);
				row.getCell(29).setCellValue(ApiConstant.UNIT_FOR_UV);
				row.getCell(30).setCellValue(ApiConstant.UNIT_FOR_UV);
				row.getCell(31).setCellValue(ApiConstant.DOUBLE_HYPHEN);
				row.getCell(32).setCellValue(ApiConstant.DOUBLE_HYPHEN);
				row.getCell(33).setCellValue(ApiConstant.UNIT_FOR_TURBIDITY);
				row.getCell(34).setCellValue(ApiConstant.UNIT_FOR_TURBIDITY);

				if (HistoricalConverter.getTotalizerUnit() == 1) {
					row.getCell(35).setCellValue(ApiConstant.LITER_PER_MIN);
				} else {
					row.getCell(35).setCellValue(ApiConstant.ML_MIN);
				}

				if (HistoricalConverter.getTotalizerUnit_2() == 1) {
					row.getCell(36).setCellValue(ApiConstant.LITER_PER_MIN);
				} else {
					row.getCell(36).setCellValue(ApiConstant.ML_MIN);
				}

				row.getCell(37).setCellValue(ApiConstant.UNIT_FOR_PROTEIN_CONCENTRATION);
				row.getCell(38).setCellValue(ApiConstant.UNIT_FOR_PROTEIN_CONCENTRATION);
				row.getCell(39).setCellValue(ApiConstant.UNIT_FOR_FLUX);
				row.getCell(40).setCellValue(ApiConstant.UNIT_FOR_SHEAR);
				row.getCell(41).setCellValue(ApiConstant.UNIT_FOR_CROSS_FLOW_FLUX);
				row.getCell(42).setCellValue(ApiConstant.DOUBLE_HYPHEN);
				row.getCell(43).setCellValue(ApiConstant.UNIT_FOR_FLUX);
				row.getCell(44).setCellValue(ApiConstant.UNIT_FOR_WATER_PERMEABILITY);
				row.getCell(45).setCellValue(ApiConstant.UNIT_FOR_NWP);

				index++;

				while (resultSet.next()) {

					row = sheet.getRow(index);
					if (row == null) {
						row = sheet.createRow(index);
					}

					row.createCell(0).setCellValue(resultSet.getString(ApiConstant.TRIAL_ID));
					row.createCell(1).setCellValue(
							BasicUtility.getInstance().convertUnixToDate(resultSet.getInt(ApiConstant.TIMESTAMP)));
					row.createCell(2).setCellValue(resultSet.getString(ApiConstant.DURATION));

					// pressures
					row.createCell(3).setCellValue(
							Double.parseDouble(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE))));
					row.createCell(4).setCellValue(
							Double.parseDouble(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE))));
					row.createCell(5).setCellValue(
							Double.parseDouble(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE))));
					row.createCell(6)
							.setCellValue(Double.parseDouble(convertPressure(resultSet.getDouble(ApiConstant.TMP))));
					row.createCell(7).setCellValue(
							Double.parseDouble(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P))));

					if (isKMPiorFS500) {
						row.createCell(8).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE) / 1000)));
						row.createCell(9).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE) / 1000)));
						row.createCell(10).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE) / 1000)));
					} else {
						row.createCell(8).setCellValue(Double
								.parseDouble(decimalFormat.format(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE))));
						row.createCell(9).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE))));
						row.createCell(10).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE))));
					}

					row.createCell(11).setCellValue((int) resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED));

					// weights
					row.createCell(12).setCellValue(
							Double.parseDouble(convertScale(resultSet.getDouble(ApiConstant.FEED_SCALE))));

					if (resultSet.getObject(ApiConstant.M_PERMEATE) != null) {
						row.createCell(13).setCellValue(
								Double.parseDouble(convertScale(resultSet.getDouble(ApiConstant.M_PERMEATE))));
					} else {
						row.createCell(13).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getObject(ApiConstant.TOTAL_PERM_WEIGHT) != null) {
						row.createCell(14).setCellValue(
								Double.parseDouble(convertScale(resultSet.getDouble(ApiConstant.TOTAL_PERM_WEIGHT))));
					} else {
						row.createCell(14).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					// permeate total
					if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL) != null) {
						row.createCell(15)
								.setCellValue(convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL)));
					} else {
						row.createCell(15).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getObject(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP) != null) {
						row.createCell(16).setCellValue(
								convertVolume(resultSet.getDouble(ApiConstant.COLUMN_PERMEATE_TOTAL_WITH_HOLDUP)));
					} else {
						row.createCell(16).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					row.createCell(17).setCellValue(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR));
					row.createCell(18).setCellValue(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_1));
					row.createCell(19).setCellValue(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_2));
					if (ApiConstant.NOT_APPLICABLE.equalsIgnoreCase(resultSet.getString(ApiConstant.ABV_1))) {
						row.createCell(20).setCellValue(resultSet.getString(ApiConstant.ABV_1));
					} else {
						row.createCell(20).setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.ABV_1)));
					}
					if (ApiConstant.NOT_APPLICABLE.equalsIgnoreCase(resultSet.getString(ApiConstant.ABV_2))) {
						row.createCell(21).setCellValue(resultSet.getString(ApiConstant.ABV_2));
					} else {
						row.createCell(21).setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.ABV_2)));
					}

					if (isIP1) {
						row.createCell(22).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.AUX_PUMP_1_FLOW_RATE) / 1000)));
					} else {
						row.createCell(22).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.AUX_PUMP_1_FLOW_RATE))));
					}

					if (isIP2) {
						row.createCell(23).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.AUX_PUMP_2_FLOW_RATE) / 1000)));
					} else {
						row.createCell(23).setCellValue(Double.parseDouble(
								decimalFormat.format(resultSet.getDouble(ApiConstant.AUX_PUMP_2_FLOW_RATE))));
					}
					row.createCell(24).setCellValue(resultSet.getDouble(ApiConstant.VT));

					if (resultSet.getObject(ApiConstant.CONDUCTIVITY_1) != null) {
						row.createCell(25).setCellValue(Double
								.parseDouble(decimalFormat.format(resultSet.getDouble(ApiConstant.CONDUCTIVITY_1))));

					} else {
						row.createCell(25).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getObject(ApiConstant.CONDUCTIVITY_2) != null) {

						row.createCell(26).setCellValue(Double
								.parseDouble(decimalFormat.format(resultSet.getDouble(ApiConstant.CONDUCTIVITY_2))));

					} else {
						row.createCell(26).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getObject(ApiConstant.TEMPERATURE_1) != null) {
						row.createCell(27).setCellValue(Double
								.parseDouble(decimalFormat.format(resultSet.getDouble(ApiConstant.TEMPERATURE_1))));
					} else {
						row.createCell(27).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getObject(ApiConstant.TEMPERATURE_2) != null) {
						row.createCell(28).setCellValue(Double
								.parseDouble(decimalFormat.format(resultSet.getDouble(ApiConstant.TEMPERATURE_2))));
					} else {
						row.createCell(28).setCellValue(ApiConstant.NOT_APPLICABLE);
					}

					if (resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE) == ApiConstant.CHANNEL_1_UV_TYPE) {
						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							row.createCell(29).setCellValue(Double.parseDouble(threeDecimalFormat
									.format(Double.valueOf(resultSet.getString(ApiConstant.KONDUIT_CH_1)))));
						} else {
							row.createCell(29).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE) == ApiConstant.CHANNEL_1_PH_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							row.createCell(31)
									.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_1)));
						} else {
							row.createCell(31).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet
							.getInt(ApiConstant.KONDUIT_CH_1_TYPE) == ApiConstant.CHANNEL_1_TURBIDITY_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							row.createCell(33)
									.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_1)));
						} else {
							row.createCell(33).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet
							.getInt(ApiConstant.KONDUIT_CH_1_TYPE) == ApiConstant.CHANNEL_1_TOTALIZER_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							row.createCell(35).setCellValue(HistoricalConverter.convertTotalizer_1(
									Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_1))));
						} else {
							row.createCell(35).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet
							.getInt(ApiConstant.KONDUIT_CH_1_TYPE) == ApiConstant.CHANNEL_1_PROTEIN_CONC_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							row.createCell(37)
									.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_1)));
						} else {
							row.createCell(37).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					}

					if (resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE) == ApiConstant.CHANNEL_2_UV_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							row.createCell(30).setCellValue(Double.parseDouble(threeDecimalFormat
									.format(Double.valueOf(resultSet.getString(ApiConstant.KONDUIT_CH_2)))));
						} else {
							row.createCell(30).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE) == ApiConstant.CHANNEL_2_PH_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							row.createCell(32)
									.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_2)));
						} else {
							row.createCell(32).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet
							.getInt(ApiConstant.KONDUIT_CH_2_TYPE) == ApiConstant.CHANNEL_2_TURBIDITY_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							row.createCell(34)
									.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_2)));
						} else {
							row.createCell(34).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet
							.getInt(ApiConstant.KONDUIT_CH_2_TYPE) == ApiConstant.CHANNEL_2_TOTALIZER_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							row.createCell(36).setCellValue(HistoricalConverter.convertTotalizer_2(
									Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_2))));
						} else {
							row.createCell(36).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					} else if (resultSet
							.getInt(ApiConstant.KONDUIT_CH_2_TYPE) == ApiConstant.CHANNEL_2_PROTEIN_CONC_TYPE) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							row.createCell(38)
									.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.KONDUIT_CH_2)));
						} else {
							row.createCell(38).setCellValue(ApiConstant.NOT_APPLICABLE);
						}

					}

					row.createCell(39).setCellValue(resultSet.getDouble(ApiConstant.FLUX));

					if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE))) {
						row.createCell(40).setCellValue(resultSet.getString(ApiConstant.SHEAR));

					} else if (ApiConstant.CASSETTE
							.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE))) {
						row.createCell(41).setCellValue(resultSet.getString(ApiConstant.SHEAR));
					}

					if (ApiConstant.NWP.equalsIgnoreCase(modeName)) {
						if (ApiConstant.NOT_APPLICABLE
								.equalsIgnoreCase(resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE))) {
							row.createCell(43).setCellValue(ApiConstant.BLANK_QUOTE);
						} else {
							try {
								row.createCell(43).setCellValue(
										Double.parseDouble(resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE)));
							} catch (NumberFormatException e) {
								row.createCell(43).setCellValue(ApiConstant.BLANK_QUOTE);
							}
						}
						if (ApiConstant.NOT_APPLICABLE.equalsIgnoreCase(resultSet.getString(ApiConstant.NWP))) {
							row.createCell(45).setCellValue(ApiConstant.BLANK_QUOTE);
						} else {
							try {
								row.createCell(45)
										.setCellValue(Double.parseDouble(resultSet.getString(ApiConstant.NWP)));
							} catch (NumberFormatException e) {
								row.createCell(45).setCellValue(ApiConstant.BLANK_QUOTE);
							}
						}
						if (ApiConstant.NOT_APPLICABLE
								.equalsIgnoreCase(resultSet.getString(ApiConstant.WATER_PERMEABILITY))) {
							row.createCell(44).setCellValue(ApiConstant.BLANK_QUOTE);
						} else {
							try {
								row.createCell(44).setCellValue(
										Double.parseDouble(resultSet.getString(ApiConstant.WATER_PERMEABILITY)));
							} catch (NumberFormatException e) {
								row.createCell(44).setCellValue(ApiConstant.BLANK_QUOTE);
							}
						}
					} else if (ApiConstant.FLUX_C.equalsIgnoreCase(modeName)
							|| ApiConstant.FLUX_CV.equalsIgnoreCase(modeName)) {
						if (ApiConstant.NOT_APPLICABLE
								.equalsIgnoreCase(resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE))) {
							row.createCell(43).setCellValue(ApiConstant.BLANK_QUOTE);
						} else {
							try {
								row.createCell(43).setCellValue(
										Double.parseDouble(resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE)));
							} catch (NumberFormatException e) {
								row.createCell(43).setCellValue(ApiConstant.BLANK_QUOTE);
							}
						}
						if (ApiConstant.NOT_APPLICABLE
								.equalsIgnoreCase(resultSet.getString(ApiConstant.WATER_PERMEABILITY))) {
							row.createCell(44).setCellValue(ApiConstant.BLANK_QUOTE);
						} else {
							try {
								row.createCell(44).setCellValue(
										Double.parseDouble(resultSet.getString(ApiConstant.WATER_PERMEABILITY)));
							} catch (NumberFormatException e) {
								row.createCell(44).setCellValue(ApiConstant.BLANK_QUOTE);
							}
						}
					}
					if (ApiConstant.NOT_APPLICABLE
							.equalsIgnoreCase(resultSet.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR))) {
						row.createCell(42).setCellValue(ApiConstant.BLANK_QUOTE);
					} else {
						try {
							row.createCell(42).setCellValue(
									Double.parseDouble(resultSet.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR)));
						} catch (NumberFormatException e) {
							row.createCell(42).setCellValue(ApiConstant.BLANK_QUOTE);
						}
					}
					index++;
					if (index % 2000 == 0) {
						System.gc();
						sheet.flushRows();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchTableLogStmt, con);
			if (sheet != null) {
				sheet.flushRows();
			}
			System.gc();
		}
	}

}
