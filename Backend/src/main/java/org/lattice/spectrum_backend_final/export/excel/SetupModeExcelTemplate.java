package org.lattice.spectrum_backend_final.export.excel;

import static org.lattice.spectrum_backend_final.export.excel.ExcelExportMain.decimalFormat;

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
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.TargetStepSettingManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

/**
 * The Class used to set values in the setup mode excel template.
 */
public final class SetupModeExcelTemplate implements IExcelTemplate {

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

			sheet.createRow(0).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(0).setCellValue("Trial ID");
			sheet.getRow(0).createCell(1).setCellValue(trialRS.getString(ApiConstant.TRIAL_ID));
			sheet.getRow(0).createCell(2).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(2).setCellValue("Recipe name");
			sheet.getRow(0).createCell(3).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RECIPE_NAME));
			sheet.getRow(0).createCell(4).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(4).setCellValue("Type");
			sheet.getRow(0).createCell(5).setCellValue("Setup break down");
			sheet.getRow(0).createCell(6).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(6).setCellValue("Downloaded By");
			sheet.getRow(0).createCell(7)
					.setCellValue(DbConnectionManager.getInstance().getTokenManager().getUsername());
			sheet.getRow(0).createCell(8).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(8).setCellValue("Downloaded On");
			sheet.getRow(0).createCell(9)
					.setCellValue(BasicUtility.getInstance().convertUnixToDate(System.currentTimeMillis() / 1000));

			sheet.createRow(1).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(1).setCellValue("Trial start time");
			sheet.getRow(1).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(2).setCellValue("Trial end time");
			sheet.getRow(1).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(3).setCellValue("Duration");
			sheet.getRow(1).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(4).setCellValue("Feed to empty");
			sheet.getRow(1).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(5).setCellValue("Run as safe");
			sheet.getRow(1).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(6).setCellValue("Temperature ( Flux C, Flux CV, NWP )");
			sheet.getRow(1).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(1).getCell(8).setCellValue("Setpoint calculation (Permeate scale/ totalizer)");

			sheet.createRow(2).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(2).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(2).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(2).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(2).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(2).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(2).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(2).createCell(6).setCellValue(ApiConstant.CELSIUS);

			sheet.createRow(3).createCell(1).setCellValue(trialRS.getString(ApiConstant.TRIAL_START_TIME));
			sheet.getRow(3).createCell(2).setCellValue(trialRS.getString(ApiConstant.TRIAL_END_TIME));
			sheet.getRow(3).createCell(3).setCellValue(DatabaseManager.getInstance().getTrialDuration(
					trialRS.getString(ApiConstant.TRIAL_START_TIME), trialRS.getString(ApiConstant.TRIAL_END_TIME)));

			if (ApiConstant.BLANK_QUOTE
					.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.FEED_TO_EMPTY))) {
				sheet.getRow(3).createCell(4).setCellValue(ApiConstant.NOT_APPLICABLE);
			} else {
				sheet.getRow(3).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FEED_TO_EMPTY));
			}

			sheet.getRow(3).createCell(5).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RUN_AS_SAFE));

			if (receipeJson.getJSONObject(0).getInt(ApiConstant.END_POINT_CAL) == 0) {
				sheet.getRow(3).createCell(8).setCellValue(ApiConstant.PERMEATE_WEIGHT);
			} else {
				sheet.getRow(3).createCell(8).setCellValue(ApiConstant.ALARM_DESCRIPTION_PERMEATE_TOTAL);
			}

			if (trialRS.getString(ApiConstant.TEMPERATURE) != null
					&& !trialRS.getString(ApiConstant.TEMPERATURE).isEmpty()) {
				sheet.getRow(3).createCell(6).setCellValue(trialRS.getString(ApiConstant.TEMPERATURE));
			} else {
				sheet.getRow(3).createCell(6).setCellValue(ApiConstant.NOT_APPLICABLE);
			}

			sheet.createRow(4).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(4).getCell(0).setCellValue("Trial configuration");
			sheet.getRow(4).createCell(6)
					.setCellValue("NA* means Temperature not entered by user and directly observed from KONDUiT");

			sheet.createRow(5).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(5).getCell(1).setCellValue("Feed hold up");
			sheet.getRow(5).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(5).getCell(2).setCellValue("Feed hold up weight");
			sheet.getRow(5).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(5).getCell(3).setCellValue("Feed start weight");
			sheet.getRow(5).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(5).getCell(4).setCellValue("Q permeate frequency");
			sheet.getRow(5).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(5).getCell(5).setCellValue("Device ID");

			sheet.createRow(6).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(6).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(6).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			Integer qPermFreq = null;
			if (trialRS.getString(ApiConstant.Q_PERMEATE_FREQUENCY) != null
					&& !trialRS.getString(ApiConstant.Q_PERMEATE_FREQUENCY).isEmpty()) {
				qPermFreq = Integer.parseInt(trialRS.getString(ApiConstant.Q_PERMEATE_FREQUENCY));
				if (qPermFreq >= 60) {
					qPermFreq /= 60;
					sheet.getRow(6).createCell(4).setCellValue("min");
				} else {
					sheet.getRow(6).createCell(4).setCellValue("sec");
				}
			}

			String isFeedEmpty = trialRS.getString(ApiConstant.IS_FEED_EMPTY);
			if (isFeedEmpty.equalsIgnoreCase(ApiConstant.YES)) {
				isFeedEmpty = ApiConstant.EMPTY;
			} else if (isFeedEmpty.equalsIgnoreCase(ApiConstant.NO)) {
				isFeedEmpty = ApiConstant.FULL;
			}

			sheet.createRow(7).createCell(1).setCellValue(isFeedEmpty);
			sheet.getRow(7).createCell(4).setCellValue(qPermFreq);
			sheet.getRow(7).createCell(5).setCellValue(trialRS.getString(ApiConstant.DEVICE_ID));

			if (isKMPiorFS500) {
				sheet.getRow(6).createCell(2).setCellValue(ApiConstant.KILOGRAM);
				if (!trialRS.getString(ApiConstant.FEED_HOLD_UP).isEmpty()) {
					sheet.getRow(7).createCell(2).setCellValue(decimalFormat
							.format(Double.parseDouble(trialRS.getString(ApiConstant.FEED_HOLD_UP)) / 1000));
				}
				sheet.getRow(6).createCell(3).setCellValue(ApiConstant.KILOGRAM);
				if (trialRS.getString(ApiConstant.FEED_START_WEIGHT) != null
						&& !trialRS.getString(ApiConstant.FEED_START_WEIGHT).isEmpty()) {
					sheet.getRow(7).createCell(3).setCellValue(decimalFormat
							.format(Double.parseDouble(trialRS.getString(ApiConstant.FEED_START_WEIGHT)) / 1000));
				} else {
					sheet.getRow(7).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
				}
			} else {
				sheet.getRow(6).createCell(2).setCellValue(ApiConstant.GRAM);
				sheet.getRow(7).createCell(2).setCellValue(trialRS.getString(ApiConstant.FEED_HOLD_UP));
				sheet.getRow(6).createCell(3).setCellValue(ApiConstant.GRAM);
				if (trialRS.getString(ApiConstant.FEED_START_WEIGHT) != null
						&& !trialRS.getString(ApiConstant.FEED_START_WEIGHT).isEmpty()) {
					sheet.getRow(7).createCell(3).setCellValue(trialRS.getString(ApiConstant.FEED_START_WEIGHT));
				} else {
					sheet.getRow(7).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
				}
			}

			sheet.getRow(7).createCell(7).setCellValue("NA = Feed start weight not entered by user");

			sheet.createRow(9).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(10).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(10).setHeightInPoints(26);
			sheet.getRow(10).getCell(0).setCellValue("Setpoint(s), target(s)");

			sheet.createRow(11).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(11).getCell(0).setCellValue("Mode Name");

			sheet.getRow(11).createCell(1).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME));

			sheet.createRow(13).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(13).getCell(0).setCellValue("Setpoints (mode specific)");

			sheet.createRow(14).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(14).getCell(0).setCellValue("Flux C/CV");
			sheet.getRow(14).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(1).setCellValue("Setpoint type/ target");
			sheet.getRow(14).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(2).setCellValue("Flowrate 1");
			sheet.getRow(14).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(3).setCellValue("Flowrate 2");
			sheet.getRow(14).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(14).getCell(4).setCellValue("Flowrate 3");

			sheet.createRow(15).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(15).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(15).createCell(2).setCellValue(ApiConstant.ML_MIN);
			sheet.getRow(15).createCell(3).setCellValue(ApiConstant.ML_MIN);
			sheet.getRow(15).createCell(4).setCellValue(ApiConstant.ML_MIN);

			sheet.createRow(17).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(1).setCellValue("Setpoint type/ target");
			sheet.getRow(17).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(2).setCellValue("TMP 1");
			sheet.getRow(17).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(3).setCellValue("Duration 1");
			sheet.getRow(17).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(4).setCellValue("TMP 2");
			sheet.getRow(17).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(5).setCellValue("Duration 2");
			sheet.getRow(17).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(6).setCellValue("TMP 3");
			sheet.getRow(17).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(17).getCell(7).setCellValue("Duration 3");

			sheet.createRow(18).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(18).createCell(2).setCellValue(ApiConstant.PSI);
			sheet.getRow(18).createCell(3).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(18).createCell(4).setCellValue(ApiConstant.PSI);
			sheet.getRow(18).createCell(5).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(18).createCell(6).setCellValue(ApiConstant.PSI);
			sheet.getRow(18).createCell(7).setCellValue(ApiConstant.MINUTE_UNIT);

			sheet.createRow(21).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(21).getCell(0).setCellValue("NWP");
			sheet.getRow(21).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(1).setCellValue("Setpoint type/ target");
			sheet.getRow(21).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(2).setCellValue("TMP 1");
			sheet.getRow(21).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(3).setCellValue("TMP 2");
			sheet.getRow(21).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(4).setCellValue("TMP 3");
			sheet.getRow(21).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(5).setCellValue("TMP 4");
			sheet.getRow(21).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(21).getCell(6).setCellValue("TMP 5");

			sheet.createRow(22).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(22).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(22).createCell(2).setCellValue(ApiConstant.PSI);
			sheet.getRow(22).createCell(3).setCellValue(ApiConstant.PSI);
			sheet.getRow(22).createCell(4).setCellValue(ApiConstant.PSI);
			sheet.getRow(22).createCell(5).setCellValue(ApiConstant.PSI);
			sheet.getRow(22).createCell(6).setCellValue(ApiConstant.PSI);

			sheet.createRow(24).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(24).getCell(1).setCellValue("Setpoint type/ target");
			sheet.getRow(24).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(24).getCell(2).setCellValue("Duration 1");
			sheet.getRow(24).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(24).getCell(3).setCellValue("Duration 2");
			sheet.getRow(24).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(24).getCell(4).setCellValue("Duration 3");
			sheet.getRow(24).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(24).getCell(5).setCellValue("Duration 4");
			sheet.getRow(24).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(24).getCell(6).setCellValue("Duration 5");

			sheet.createRow(25).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(25).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(25).createCell(2).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(25).createCell(3).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(25).createCell(4).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(25).createCell(5).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(25).createCell(6).setCellValue(ApiConstant.MINUTE_UNIT);

			sheet.createRow(28).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(28).getCell(0).setCellValue("Flushing");
			sheet.getRow(28).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(28).getCell(1).setCellValue("Setpoint type/ target");
			sheet.getRow(28).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(28).getCell(2).setCellValue("Duration");
			sheet.getRow(28).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(28).getCell(3).setCellValue("Permeate Weight");
			sheet.getRow(28).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(28).getCell(4).setCellValue("Permeate Total");

			sheet.getRow(28).createCell(6)
					.setCellValue("NA - User selected alternate set point (duration or permeate weight)");

			sheet.createRow(29).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(29).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(29).createCell(2).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(29).createCell(3).setCellValue(ApiConstant.GRAM);
			sheet.getRow(29).createCell(4).setCellValue(ApiConstant.GRAM);

			sheet.createRow(32).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(32).getCell(0).setCellValue("Cleaning");
			sheet.getRow(32).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(3).setCellValue("Setpoint 1");
			sheet.getRow(32).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(32).getCell(6).setCellValue("Setpoint 2");

			sheet.createRow(33).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(1).setCellValue("Setpoint type/ target");
			sheet.getRow(33).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(2).setCellValue("Duration");
			sheet.getRow(33).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(3).setCellValue("Permeate Weight");
			sheet.getRow(33).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(4).setCellValue("Permeate Total");
			sheet.getRow(33).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(5).setCellValue("Duration");
			sheet.getRow(33).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(6).setCellValue("Permeate Weight");
			sheet.getRow(33).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(33).getCell(7).setCellValue("Permeate Total");

			sheet.createRow(34).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(34).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(34).createCell(2).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(34).createCell(3).setCellValue(ApiConstant.GRAM);
			sheet.getRow(34).createCell(4).setCellValue(ApiConstant.GRAM);
			sheet.getRow(34).createCell(5).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(34).createCell(6).setCellValue(ApiConstant.GRAM);
			sheet.getRow(34).createCell(7).setCellValue(ApiConstant.GRAM);

			sheet.createRow(37).createCell(0).setCellValue("-- = Units are not applicable.");
			sheet.createRow(36).createCell(6)
					.setCellValue("NA - User selected alternate set point (duration or permeate weight)");

			if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isEmpty()) {
				if (ApiConstant.FLUX_C.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME))
						|| ApiConstant.FLUX_CV
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME))) {

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(0)) {
						if (isKMPiorFS500) {
							sheet.getRow(15).createCell(2).setCellValue(ApiConstant.LITER_PER_MIN);
							sheet.createRow(16).createCell(2)
									.setCellValue(decimalFormat.format(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.FLOW_RATE))
											/ 1000));
						} else {
							sheet.getRow(15).createCell(2).setCellValue(ApiConstant.ML_MIN);
							sheet.createRow(16).createCell(2)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
											.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
						}

						sheet.createRow(19).createCell(2).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0).getString(ApiConstant.TMP));

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(19).createCell(3)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.DURATION))
											/ 60);
						}
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(1)) {
						if (isKMPiorFS500) {
							sheet.getRow(15).createCell(3).setCellValue(ApiConstant.LITER_PER_MIN);
							sheet.getRow(16).createCell(3)
									.setCellValue(decimalFormat.format(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(1).getString(ApiConstant.FLOW_RATE))
											/ 1000));
						} else {
							sheet.getRow(15).createCell(3).setCellValue(ApiConstant.ML_MIN);
							sheet.getRow(16).createCell(3)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
											.getJSONObject(1).getString(ApiConstant.FLOW_RATE));
						}

						sheet.getRow(19).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1).getString(ApiConstant.TMP));
						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(19).createCell(5)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(1).getString(ApiConstant.DURATION))
											/ 60);
						}
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(2)) {
						if (isKMPiorFS500) {
							sheet.getRow(15).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
							sheet.getRow(16).createCell(4)
									.setCellValue(decimalFormat.format(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(2).getString(ApiConstant.FLOW_RATE))
											/ 1000));
						} else {
							sheet.getRow(15).createCell(4).setCellValue(ApiConstant.ML_MIN);
							sheet.getRow(16).createCell(4)
									.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
											.getJSONObject(2).getString(ApiConstant.FLOW_RATE));
						}

						sheet.getRow(19).createCell(6).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(2).getString(ApiConstant.TMP));
						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(2)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(19).createCell(7)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(2).getString(ApiConstant.DURATION))
											/ 60);
						}
					}

				} else if (ApiConstant.NWP
						.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME))) {

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(0)) {

						sheet.createRow(23).createCell(2).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0).getString(ApiConstant.TMP));

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.createRow(26).createCell(2)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.DURATION))
											/ 60);
						}
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(1)) {

						sheet.getRow(23).createCell(3).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1).getString(ApiConstant.TMP));
						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(26).createCell(3)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(1).getString(ApiConstant.DURATION))
											/ 60);
						}
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(2)) {

						sheet.getRow(23).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(2).getString(ApiConstant.TMP));
						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(2)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(26).createCell(4)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(2).getString(ApiConstant.DURATION))
											/ 60);
						}
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(3)) {

						sheet.getRow(23).createCell(5).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(3).getString(ApiConstant.TMP));
						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(3)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(26).createCell(5)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(3).getString(ApiConstant.DURATION))
											/ 60);
						}
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(4)) {

						sheet.getRow(23).createCell(6).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(4).getString(ApiConstant.TMP));
						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(26).createCell(6)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(4).getString(ApiConstant.DURATION))
											/ 60);
						}
					}

				} else if (ApiConstant.FLUSHING
						.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME))) {

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(0)) {

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.createRow(30).createCell(2)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.DURATION))
											/ 60);
						}

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT).isEmpty()) {
							if (isKMPiorFS500) {
								sheet.getRow(29).createCell(3).setCellValue(ApiConstant.KILOGRAM);
								sheet.getRow(30).createCell(3).setCellValue(Double
										.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
												.getJSONObject(0).getString(ApiConstant.COLUMN_PERMEATE_WEIGHT))
										/ 1000);
							} else {
								sheet.getRow(29).createCell(3).setCellValue(ApiConstant.GRAM);
								sheet.getRow(30).createCell(3)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
												.getJSONObject(0).getString(ApiConstant.COLUMN_PERMEATE_WEIGHT));
							}
						}

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.COLUMN_PERMEATE_TOTAL).isEmpty()) {
							sheet.getRow(29).createCell(4).setCellValue(Converter
									.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
							sheet.getRow(30).createCell(4)
									.setCellValue(HistoricalConverter.convertVolume(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.COLUMN_PERMEATE_TOTAL))));
						}

					}

				} else if (ApiConstant.CLEANING
						.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME))) {

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(0)) {

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.createRow(35).createCell(2)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.DURATION))
											/ 60);
						}

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT).isEmpty()) {
							if (isKMPiorFS500) {
								sheet.getRow(34).createCell(3).setCellValue(ApiConstant.KILOGRAM);
								sheet.getRow(35).createCell(3).setCellValue(Double
										.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
												.getJSONObject(0).getString(ApiConstant.COLUMN_PERMEATE_WEIGHT))
										/ 1000);
							} else {
								sheet.getRow(34).createCell(3).setCellValue(ApiConstant.GRAM);
								sheet.getRow(35).createCell(3)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
												.getJSONObject(0).getString(ApiConstant.COLUMN_PERMEATE_WEIGHT));
							}
						}

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(0)
								.getString(ApiConstant.COLUMN_PERMEATE_TOTAL).isEmpty()) {
							sheet.getRow(34).createCell(4).setCellValue(Converter
									.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
							sheet.getRow(35).createCell(4)
									.setCellValue(HistoricalConverter.convertVolume(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(0).getString(ApiConstant.COLUMN_PERMEATE_TOTAL))));
						}

					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).isNull(1)) {

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1)
								.getString(ApiConstant.DURATION).isEmpty()) {
							sheet.getRow(35).createCell(5)
									.setCellValue(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(1).getString(ApiConstant.DURATION))
											/ 60);
						}

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1)
								.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT).isEmpty()) {
							if (isKMPiorFS500) {
								sheet.getRow(34).createCell(6).setCellValue(ApiConstant.KILOGRAM);
								sheet.getRow(35).createCell(6)
										.setCellValue(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
														.getJSONObject(1).getString(ApiConstant.COLUMN_PERMEATE_WEIGHT))
												/ 100);
							} else {
								sheet.getRow(34).createCell(6).setCellValue(ApiConstant.GRAM);
								sheet.getRow(35).createCell(6)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
												.getJSONObject(1).getString(ApiConstant.COLUMN_PERMEATE_WEIGHT));
							}
						}

						if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(1)
								.getString(ApiConstant.COLUMN_PERMEATE_TOTAL).isEmpty()) {
							sheet.getRow(34).createCell(7).setCellValue(Converter
									.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
							sheet.getRow(35).createCell(7)
									.setCellValue(HistoricalConverter.convertVolume(Double.parseDouble(
											receipeJson.getJSONObject(0).getJSONArray(ApiConstant.TARGET_STEP)
													.getJSONObject(1).getString(ApiConstant.COLUMN_PERMEATE_TOTAL))));
						}

					}
				}
			}

			// main pump
			sheet.createRow(38).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(38).getCell(0).setCellValue("Main pump");

			sheet.createRow(39).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(39).getCell(0).setCellValue("Main pump model");
			sheet.getRow(39).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(2).setCellValue("No of Head(s)");
			sheet.getRow(39).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(3).setCellValue("Direction");
			sheet.getRow(39).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(4).setCellValue("Flow control");
			sheet.getRow(39).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(5).setCellValue("Tubing");
			sheet.getRow(39).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(6).setCellValue("Flow rate");
			sheet.getRow(39).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(7).setCellValue("Pump speed");
			sheet.getRow(39).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(39).getCell(8).setCellValue("Ramp up time (1-15 s)");

			sheet.createRow(40).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(40).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(40).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(40).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(40).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(40).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(40).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(41).createCell(0)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.MAIN_PUMP_NAME));
			sheet.getRow(41).createCell(2).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.HEAD_COUNT));
			sheet.getRow(41).createCell(3).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.DIRECTION));

			if (receipeJson.getJSONObject(0).has(ApiConstant.FLOW_RATE)
					&& receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE) != null
					&& !receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE).isEmpty()) {
				if (isKMPiorFS500) {
					if (!receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE).isEmpty()) {
						sheet.getRow(40).createCell(6).setCellValue(ApiConstant.LITER_PER_MIN);
						sheet.getRow(41).createCell(6).setCellValue(
								Double.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE))
										/ 1000);
					}
				} else {
					sheet.getRow(40).createCell(6).setCellValue(ApiConstant.ML_MIN);
					sheet.getRow(41).createCell(6)
							.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
				}
			}

			sheet.getRow(41).createCell(4)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FLOW_CONTROL));
			sheet.getRow(41).createCell(5)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PUMP_TUBING_SIZE));

			sheet.getRow(40).createCell(7).setCellValue(ApiConstant.RPM);
			sheet.getRow(41).createCell(7).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PUMP_RPM));

			sheet.getRow(40).createCell(8).setCellValue("s");
			sheet.getRow(41).createCell(8)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RAMP_UP_TIME));

			// Hollow fiber
			sheet.createRow(43).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(44).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(44).setHeightInPoints(26);
			sheet.getRow(44).getCell(0).setCellValue("Hollow fiber filter and permeate tubing");

			sheet.createRow(45).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(45).getCell(0).setCellValue("Hollow fiber filter");
			sheet.getRow(45).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(1).setCellValue("Non-Repligen Filter");
			sheet.getRow(45).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(2).setCellValue("Part #");
			sheet.getRow(45).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(3).setCellValue("Fiber count");
			sheet.getRow(45).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(4).setCellValue("Fiber ID");
			sheet.getRow(45).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(5).setCellValue("Surface area");
			sheet.getRow(45).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(6).setCellValue("Extra Capillary Space (ECS)");
			sheet.getRow(45).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(7).setCellValue("Model number (Non-Repligen Filter)");
			sheet.getRow(45).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(45).getCell(8).setCellValue("Manufacturer name (Non-Repligen Filter)");

			sheet.createRow(46).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(46).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(46).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(46).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(46).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(46).createCell(4).setCellValue(ApiConstant.MILLIMETER);
			sheet.getRow(46).createCell(5).setCellValue("sq cm");
			sheet.getRow(46).createCell(6).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(46).createCell(7).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(46).createCell(8).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(48).createCell(4).setCellValue("NA - User selected Non-Repligen filter");

			// Permeate tubing
			sheet.createRow(49).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(50).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(50).getCell(0).setCellValue("Permeate tubing");
			sheet.getRow(50).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(1).setCellValue("Permeate Holdup Volume used? (Y/N)");
			sheet.getRow(50).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(2).setCellValue("Permeate tube part #");
			sheet.getRow(50).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(3).setCellValue("Auto calculate permeate tubing holdup? (Y/N)");
			sheet.getRow(50).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(4).setCellValue("Permeate tube length");
			sheet.getRow(50).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(5).setCellValue("Permeate hold up (tubing)");
			sheet.getRow(50).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(50).getCell(6).setCellValue("Total permeate hold up (ECS + Perm hold up)");

			sheet.createRow(51).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(51).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(51).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(51).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(51).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(51).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(51).createCell(5).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(51).createCell(6).setCellValue(ApiConstant.MILLILITER);

			sheet.createRow(53).createCell(4)
					.setCellValue("NA = User selected to manually enter permeate tubing holdup");
			sheet.getRow(53).createCell(5).setCellValue("NA = User selected 'No permeate tubing holdup in system'");
			sheet.getRow(53).createCell(6).setCellValue(
					"* If value is zero, this means user has selected not to use permeate holdup in calculations");

			// Cassette
			sheet.createRow(54).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(55).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(55).getCell(0).setCellValue("Cassette filter and permeate tubing");

			sheet.createRow(56).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(56).getCell(0).setCellValue("Cassette");
			sheet.getRow(56).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(1).setCellValue("Non-Repligen Filter");
			sheet.getRow(56).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(2).setCellValue("Part #");
			sheet.getRow(56).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(3).setCellValue("Width");
			sheet.getRow(56).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(4).setCellValue("Height");
			sheet.getRow(56).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(5).setCellValue("Number of cassettes");
			sheet.getRow(56).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(6).setCellValue("Number of channels");
			sheet.getRow(56).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(7).setCellValue("Model number (Non-Repligen Filter)");
			sheet.getRow(56).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(56).getCell(8).setCellValue("Manufacturer name (Non-Repligen Filter)");

			sheet.createRow(57).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(57).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(57).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(57).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(57).createCell(3).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(57).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(57).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(57).createCell(6).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(57).createCell(7).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(57).createCell(8).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(59).createCell(4).setCellValue("NA - User selected Non-Repligen cassette");

			sheet.createRow(60).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(60).getCell(1).setCellValue("Surface area");
			sheet.getRow(60).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(60).getCell(2).setCellValue("Total surface area");
			sheet.getRow(60).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(60).getCell(3).setCellValue("Extra Capillary Space  (ECS, per cassette)");
			sheet.getRow(60).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(60).getCell(4).setCellValue("Total ECS (ECS * no of cassettes)");

			sheet.createRow(61).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(61).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(61).createCell(1).setCellValue("sq m");
			sheet.getRow(61).createCell(2).setCellValue("sq m");
			sheet.getRow(61).createCell(3).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(61).createCell(4).setCellValue(ApiConstant.MILLILITER);

			// filter plate
			sheet.createRow(64).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(65).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(65).getCell(0).setCellValue("Filter plate insert");
			sheet.getRow(65).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(65).getCell(1).setCellValue("Filter plate insert model number");
			sheet.getRow(65).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(65).getCell(2).setCellValue("Filter plate insert hold up");

			sheet.createRow(66).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(66).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(66).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(66).createCell(2).setCellValue(ApiConstant.MILLILITER);

			sheet.createRow(69).createCell(0).setCellValue("-- = Units are not applicable.");

			// Permeate tubing
			sheet.createRow(70).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(70).getCell(0).setCellValue("Permeate tubing");

			sheet.getRow(70).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(70).getCell(1).setCellValue("Permeate Holdup Volume used? (Y/N)");
			sheet.getRow(70).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(70).getCell(2).setCellValue("Permeate tube part #");
			sheet.getRow(70).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(70).getCell(3).setCellValue("Auto calculate permeate tubing holdup? (Y/N)");
			sheet.getRow(70).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(70).getCell(4).setCellValue("Permeate tube length");
			sheet.getRow(70).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(70).getCell(5).setCellValue("Permeate hold up (tubing)");
			sheet.getRow(70).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(70).getCell(6).setCellValue("Total permeate hold up (ECS + Perm hold up)");

			sheet.createRow(71).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(71).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(71).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(71).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(71).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(71).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(71).createCell(5).setCellValue(ApiConstant.MILLILITER);
			sheet.getRow(71).createCell(6).setCellValue(ApiConstant.MILLILITER);

			sheet.createRow(73).createCell(4)
					.setCellValue("NA = User selected to manually enter permeate tubing holdup");
			sheet.getRow(73).createCell(5).setCellValue("NA = User selected 'No permeate tubing holdup in system'");
			sheet.getRow(73).createCell(6).setCellValue(
					"* If value is zero, this means user has selected not to use permeate holdup in calculations");

			if (receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE)
					.equalsIgnoreCase(ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER)) {
				if (receipeJson.getJSONObject(0).getBoolean(ApiConstant.IS_GENERIC)) {
					sheet.createRow(47).createCell(1).setCellValue(ApiConstant.YES);
				} else {
					sheet.createRow(47).createCell(1).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(47).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PART_NO));
				sheet.getRow(47).createCell(3)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FIBER_COUNT));
				sheet.getRow(47).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FIBER_ID));
				sheet.getRow(47).createCell(5)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.SURF_AREA));
				sheet.getRow(47).createCell(6).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.ECS));
				sheet.getRow(47).createCell(7)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MODEL_NAME));
				sheet.getRow(47).createCell(8)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MANUFACTURER_NAME));

				sheet.createRow(52).createCell(1).setCellValue(
						receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
				sheet.getRow(52).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_TUBE_SIZE));
				if (receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH).isEmpty()) {
					sheet.getRow(52).createCell(3).setCellValue(ApiConstant.YES);
				} else {
					sheet.getRow(52).createCell(3).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(52).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH));
				sheet.getRow(52).createCell(5)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERM_TUBE_HOLDUP));
				sheet.getRow(52).createCell(6)
						.setCellValue(receipeJson.getJSONObject(0).getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));

			} else if (ApiConstant.CASSETTE
					.equalsIgnoreCase(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_TYPE))) {
				if (receipeJson.getJSONObject(0).getBoolean(ApiConstant.IS_GENERIC)) {
					sheet.createRow(58).createCell(1).setCellValue(ApiConstant.YES);
				} else {
					sheet.createRow(58).createCell(1).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(58).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PART_NO));
				sheet.getRow(58).createCell(3).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.WIDTH));
				sheet.getRow(58).createCell(4).setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.HEIGHT));
				final double noOfCassette = Double
						.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.NO_OF_CASSETTE));
				sheet.getRow(58).createCell(5).setCellValue(noOfCassette);
				sheet.getRow(58).createCell(6)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.NO_OF_CHANNEL));
				sheet.getRow(58).createCell(7)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MODEL_NAME));
				sheet.getRow(58).createCell(8)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_MANUFACTURER_NAME));

				final double surfaceArea = Double
						.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.SURF_AREA));
				sheet.createRow(62).createCell(1).setCellValue(surfaceArea);
				sheet.getRow(62).createCell(2).setCellValue(surfaceArea * noOfCassette);
				final double ecs = Double.parseDouble(receipeJson.getJSONObject(0).getString(ApiConstant.ECS));
				sheet.getRow(62).createCell(3).setCellValue(ecs);
				sheet.getRow(62).createCell(4).setCellValue(ecs * noOfCassette);
				if (!receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO).isEmpty()) {
					// get filter plate lookup
					sheet.createRow(67).createCell(1).setCellValue(
							receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO));
					sheet.getRow(67).createCell(2).setCellValue(ExportUtil.getInstance().getFilterHoldUpUp(
							receipeJson.getJSONObject(0).getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO)));
				}

				sheet.createRow(72).createCell(1).setCellValue(
						receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
				sheet.getRow(72).createCell(2)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_TUBE_SIZE));
				if (receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH).isEmpty()) {
					sheet.getRow(72).createCell(3).setCellValue(ApiConstant.YES);
				} else {
					sheet.getRow(72).createCell(3).setCellValue(ApiConstant.NO);
				}
				sheet.getRow(72).createCell(4)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.TUBE_LENGTH));
				sheet.getRow(72).createCell(5)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERM_TUBE_HOLDUP));
				sheet.getRow(72).createCell(6)
						.setCellValue(receipeJson.getJSONObject(0).getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));
			}

			sheet.createRow(75).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(76).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(76).getCell(0).setCellValue("Aux pump 1");

			sheet.createRow(77).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(77).getCell(0).setCellValue("Aux pump 1 model");
			sheet.getRow(77).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(77).getCell(2).setCellValue("Tubing");
			sheet.getRow(77).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(77).getCell(3).setCellValue("Pump function");
			sheet.getRow(77).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(77).getCell(4).setCellValue("Flow rate");
			sheet.getRow(77).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(77).getCell(5).setCellValue("Permeate stop first");
			sheet.getRow(77).createCell(6).setCellValue("Info : Permeate stop first is only for permeate pump");

			sheet.createRow(78).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(78).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(78).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(78).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(78).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(78).createCell(4).setCellValue(ApiConstant.ML_MIN);
			sheet.getRow(78).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(81).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(82).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(82).getCell(0).setCellValue("Aux pump 2");

			sheet.createRow(83).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(83).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(83).getCell(0).setCellValue("Aux pump 2 model");
			sheet.getRow(83).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(83).getCell(2).setCellValue("Tubing");
			sheet.getRow(83).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(83).getCell(3).setCellValue("Pump function");
			sheet.getRow(83).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(83).getCell(4).setCellValue("Flow rate");
			sheet.getRow(83).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(83).getCell(5).setCellValue("Permeate stop first");

			sheet.createRow(84).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(84).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(84).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(84).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(84).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(84).createCell(4).setCellValue(ApiConstant.ML_MIN);
			sheet.getRow(84).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.getRow(84).createCell(6).setCellValue("Info : Permeate stop first is only for permeate pump");

			// aux pumps array
			if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isEmpty()) {
				// aux pump 1
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isNull(0)) {

					if ("0".equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
							.getJSONObject(0).getString(ApiConstant.TYPE))) {
						sheet.createRow(79).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(79).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(79).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.PUMP_FUNCTION));

						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(78).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(79).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(0).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(78).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(79).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
							}
						}

						sheet.getRow(79).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					} else {
						sheet.createRow(85).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(85).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(85).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.PUMP_FUNCTION));

						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(0).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(84).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(85).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(0).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {
							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(0)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(84).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(85).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(0).getString(ApiConstant.FLOW_RATE));
							}
						}

						sheet.getRow(85).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					}

				}

				// aux pump 2
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).isNull(1)) {

					if ("0".equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
							.getJSONObject(1).getString(ApiConstant.TYPE))) {
						sheet.createRow(79).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(79).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(79).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.PUMP_FUNCTION));

						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(78).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(79).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(1).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(78).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(79).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(1).getString(ApiConstant.FLOW_RATE));
							}
						}

						sheet.getRow(79).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					} else {
						sheet.createRow(85).createCell(0)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE));
						sheet.getRow(85).createCell(2)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_TUBING_SIZE));
						sheet.getRow(85).createCell(3)
								.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.PUMP_FUNCTION));

						if (ApiConstant.IP
								.equalsIgnoreCase(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
										.getJSONObject(1).getString(ApiConstant.AUX_PUMP_TYPE))) {

							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(84).createCell(4).setCellValue(ApiConstant.LITER_PER_MIN);
								sheet.getRow(85).createCell(4)
										.setCellValue(decimalFormat.format(Double.parseDouble(
												receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
														.getJSONObject(1).getString(ApiConstant.FLOW_RATE))
												/ 1000));
							}

						} else {
							if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(1)
									.getString(ApiConstant.FLOW_RATE).isEmpty()) {
								sheet.getRow(84).createCell(4).setCellValue(ApiConstant.ML_MIN);
								sheet.getRow(85).createCell(4)
										.setCellValue(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.AUX_PUMP)
												.getJSONObject(1).getString(ApiConstant.FLOW_RATE));
							}
						}

						sheet.getRow(85).createCell(5)
								.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.PERMEATE_STOP_FIRST));
					}
				}
			}

			// abv 1
			sheet.createRow(87).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(88).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(88).getCell(0).setCellValue("ABV 1");

			sheet.createRow(89).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(89).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(89).getCell(0).setCellValue("Operating pressure control mode");
			sheet.getRow(89).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(89).getCell(2).setCellValue("ABV Type");
			sheet.getRow(89).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(89).getCell(3).setCellValue("Mode");
			sheet.getRow(89).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(89).getCell(4).setCellValue("% closed (at start)");
			sheet.getRow(89).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(89).getCell(5).setCellValue("Operating pressure");

			sheet.createRow(90).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(90).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(90).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(90).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(90).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(90).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(90).createCell(5).setCellValue(ApiConstant.PSI);

			sheet.getRow(90).createCell(6).setCellValue("NA - No operating pressure required for manual ABV control");

			// abv 2
			sheet.createRow(93).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(94).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(94).getCell(0).setCellValue("ABV 2");

			sheet.createRow(95).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(95).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(95).getCell(0).setCellValue("Operating pressure control mode");
			sheet.getRow(95).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(95).getCell(2).setCellValue("ABV Type");
			sheet.getRow(95).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(95).getCell(3).setCellValue("Mode");
			sheet.getRow(95).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(95).getCell(4).setCellValue("% closed (at start)");
			sheet.getRow(95).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(95).getCell(5).setCellValue("Operating pressure");

			sheet.createRow(96).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(96).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(96).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(96).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(96).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(96).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(96).createCell(5).setCellValue(ApiConstant.PSI);

			sheet.getRow(96).createCell(6).setCellValue("NA - No operating pressure required for manual ABV control");

			// abv array
			if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).isEmpty()) {
				// abv 1
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).isNull(0)) {

					sheet.createRow(91).createCell(0).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.CONT_BASED_ON));

					sheet.getRow(91).createCell(2).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.ABV_TYPE));
					sheet.getRow(91).createCell(3).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.ABV_MODE));

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(0)
							.getString(ApiConstant.PERCENT_CLOSED).isEmpty()) {
						sheet.getRow(91).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.PERCENT_CLOSED));
					} else {
						sheet.getRow(91).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(0).getString(ApiConstant.START_POS));
					}
					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(0)
							.getString(ApiConstant.OP_PRESSURE).isEmpty()) {

						sheet.getRow(90).createCell(5).setCellValue(Converter
								.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
						sheet.getRow(91).createCell(5)
								.setCellValue(HistoricalConverter.convertPressure(
										Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV)
												.getJSONObject(0).getString(ApiConstant.OP_PRESSURE))));
					}
				}

				// abv 2
				if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).isNull(1)) {
					sheet.createRow(97).createCell(0).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.CONT_BASED_ON));

					sheet.getRow(97).createCell(2).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.ABV_TYPE));

					sheet.getRow(97).createCell(3).setCellValue(receipeJson.getJSONObject(0)
							.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.ABV_MODE));

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(1)
							.getString(ApiConstant.PERCENT_CLOSED).isEmpty()) {
						sheet.getRow(97).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.PERCENT_CLOSED));
					} else {
						sheet.getRow(97).createCell(4).setCellValue(receipeJson.getJSONObject(0)
								.getJSONArray(ApiConstant.ABV).getJSONObject(1).getString(ApiConstant.START_POS));
					}

					if (!receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV).getJSONObject(1)
							.getString(ApiConstant.OP_PRESSURE).isEmpty()) {
						sheet.getRow(96).createCell(5).setCellValue(Converter
								.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
						sheet.getRow(97).createCell(5)
								.setCellValue(HistoricalConverter.convertPressure(
										Double.parseDouble(receipeJson.getJSONObject(0).getJSONArray(ApiConstant.ABV)
												.getJSONObject(1).getString(ApiConstant.OP_PRESSURE))));
					}
				}
			}

			sheet.createRow(99).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(100).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(100).getCell(0).setCellValue("Retentate tubing");
			sheet.getRow(100).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(100).getCell(2).setCellValue("Retentate tubing #");

			sheet.createRow(101).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(101).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(101).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(103).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(104).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(104).getCell(0).setCellValue("Recirculation pressure control");
			sheet.getRow(104).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(104).getCell(2).setCellValue("Recirculation pressure control (Yes/ No)");
			sheet.getRow(104).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(104).getCell(3).setCellValue("% value");

			sheet.createRow(105).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(105).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(105).getCell(1).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(105).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.getRow(105).createCell(6)
					.setCellValue("NA - If recirc control is selected as no, % value will be NA");

			sheet.createRow(102).createCell(2)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RETENTATE_TUBE_SIZE));
			sheet.createRow(106).createCell(2)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL));
			if (receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL)
					.equalsIgnoreCase(ApiConstant.NO)) {
				sheet.getRow(106).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
			} else {
				sheet.getRow(105).createCell(3).setCellValue(
						receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_PRESSURE_UNIT_VALUE));
				sheet.getRow(106).createCell(3)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.RECIRCULATION_RATE_PER_UNIT));
			}

			// konduit
			sheet.createRow(108).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(109).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(109).getCell(0).setCellValue("KF KONDUiT");

			sheet.createRow(110).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(110).getCell(0).setCellValue("Channel Details");
			sheet.getRow(110).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(110).getCell(1).setCellValue("value");

			sheet.createRow(111).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(111).getCell(0).setCellValue("UV/ AUX Ch 1");
			sheet.createRow(112).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(112).getCell(0).setCellValue("UV/ AUX Ch 2");
			sheet.createRow(113).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(113).getCell(0).setCellValue("K Factor Channel 1");
			sheet.getRow(113).createCell(6).setCellValue("NA - Set valued not defined");
			sheet.createRow(114).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(114).getCell(0).setCellValue("K Factor Channel 2");

			sheet.createRow(116).createCell(0).setCellValue("Aux = pH/ Turbidity/ Flowmeter/ Protein concentration");

			if (receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_1).isEmpty()) {
				sheet.getRow(111).createCell(1).setCellValue("no");
			} else {
				sheet.getRow(111).createCell(1)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_1));
			}
			if (receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_2).isEmpty()) {
				sheet.getRow(112).createCell(1).setCellValue("no");
			} else {
				sheet.getRow(112).createCell(1)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.UV_CH_2));
			}

			sheet.getRow(113).createCell(1)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.K_FACTOR_CH_1));
			sheet.getRow(114).createCell(1)
					.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.K_FACTOR_CH_2));

			// setpoint
			sheet.createRow(117).createCell(0).setCellValue("-- = Units are not applicable.");

			sheet.createRow(118).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(118).getCell(0).setCellValue("Modified trial endpoint(s)/setpoint(s)");
			sheet.getRow(118).createCell(1).setCellValue("-- = Units are not applicable.");

			sheet.createRow(119).createCell(0).setCellStyle(aquaColorStyle);
			sheet.createRow(120).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(120).getCell(0).setCellValue("Mode");
			sheet.getRow(120).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(120).getCell(2).setCellValue("TMP");
			sheet.getRow(120).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(120).getCell(3).setCellValue("Duration");
			sheet.getRow(120).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(120).getCell(4).setCellValue("Permeate Weight");
			sheet.getRow(120).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(120).getCell(5).setCellValue("Permeate Total");
			sheet.getRow(120).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(120).getCell(6).setCellValue("Username");
			sheet.getRow(120).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(120).getCell(7).setCellValue("Timestamp");

			sheet.createRow(121).createCell(0).setCellStyle(aquaColorStyle);
			sheet.getRow(121).createCell(2).setCellValue(ApiConstant.PSI);
			sheet.getRow(121).createCell(3).setCellValue(ApiConstant.MINUTE_UNIT);
			sheet.getRow(121).createCell(4).setCellValue(ApiConstant.GRAM);
			sheet.getRow(121).createCell(5).setCellValue(ApiConstant.MILLILITER);

			final JSONArray targetStepsJsonArray = TargetStepSettingManager.getInstance()
					.getTargetStepChangeLog(trialRunSettingId);

			if (!targetStepsJsonArray.isEmpty()) {
				int setupRow = 122;
				sheet.createRow(122).createCell(0)
						.setCellValue(receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME));
				SXSSFRow sxssfRow = sheet.getRow(setupRow);
				if (sxssfRow == null) {
					sxssfRow = sheet.createRow(setupRow);
				}
				if (isKMPiorFS500) {
					sheet.getRow(setupRow - 1).createCell(4).setCellValue(ApiConstant.KILOGRAM);
				} else {
					sheet.getRow(setupRow - 1).createCell(4).setCellValue(ApiConstant.GRAM);
				}
				sheet.getRow(setupRow - 1).createCell(5).setCellValue(
						Converter.systemSettingVolumeMapper(RequestHandler.getStSystemSetting().getVolumeUnit()));
				addSetPointNumbers(sheet, setupRow);
				for (int i = 0; i < targetStepsJsonArray.length(); i++) {
					JSONObject jsonObject = targetStepsJsonArray.getJSONObject(i);
					if (jsonObject != null) {
						sxssfRow = sheet.getRow(setupRow);
						if (sxssfRow == null) {
							sxssfRow = sheet.createRow(setupRow);
						}

						final int stepNo = jsonObject.getInt(ApiConstant.STEP_NUMBER);

						if (stepNo == 1) {
							if (!jsonObject.getString(ApiConstant.OLD_TMP).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_TMP)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_TMP))) {
									sheet.getRow(setupRow).createCell(2)
											.setCellValue(jsonObject.getString(ApiConstant.NEW_TMP));
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_DURATION).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_DURATION)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_DURATION))) {
									sheet.getRow(setupRow).createCell(3).setCellValue(
											Double.parseDouble(jsonObject.getString(ApiConstant.NEW_DURATION)) / 60);
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT))) {
									if (isKMPiorFS500) {
										sheet.getRow(setupRow).createCell(4).setCellValue(Double.parseDouble(
												jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000);
									} else {
										sheet.getRow(setupRow).createCell(4)
												.setCellValue(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT));
									}
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))) {

									sheet.getRow(setupRow).createCell(5).setCellValue(HistoricalConverter.convertVolume(
											Double.valueOf(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))));

								}
							}
							sheet.getRow(setupRow).createCell(6)
									.setCellValue(jsonObject.getString(ApiConstant.USERNAME));
							sheet.getRow(setupRow).createCell(7)
									.setCellValue(jsonObject.getString(ApiConstant.CREATED_ON));

						} else if (stepNo == 2) {
							if (!jsonObject.getString(ApiConstant.OLD_TMP).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_TMP)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_TMP))) {
									sheet.getRow(setupRow + 1).createCell(2)
											.setCellValue(jsonObject.getString(ApiConstant.NEW_TMP));
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_DURATION).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_DURATION)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_DURATION))) {
									sheet.getRow(setupRow + 1).createCell(3).setCellValue(
											Double.parseDouble(jsonObject.getString(ApiConstant.NEW_DURATION)) / 60);
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT))) {
									if (isKMPiorFS500) {
										sheet.getRow(setupRow + 1).createCell(4).setCellValue(Double.parseDouble(
												jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000);
									} else {
										sheet.getRow(setupRow + 1).createCell(4)
												.setCellValue(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT));
									}
								}
							}

							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))) {

									sheet.getRow(setupRow + 1).createCell(5)
											.setCellValue(HistoricalConverter.convertVolume(Double
													.valueOf(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))));

								}
							}

							sheet.getRow(setupRow + 1).createCell(6)
									.setCellValue(jsonObject.getString(ApiConstant.USERNAME));
							sheet.getRow(setupRow + 1).createCell(7)
									.setCellValue(jsonObject.getString(ApiConstant.CREATED_ON));
						} else if (stepNo == 3) {
							if (!jsonObject.getString(ApiConstant.OLD_TMP).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_TMP)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_TMP))) {
									sheet.getRow(setupRow + 2).createCell(2)
											.setCellValue(jsonObject.getString(ApiConstant.NEW_TMP));
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_DURATION).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_DURATION)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_DURATION))) {
									sheet.getRow(setupRow + 2).createCell(3).setCellValue(
											Double.parseDouble(jsonObject.getString(ApiConstant.NEW_DURATION)) / 60);
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT))) {
									if (isKMPiorFS500) {
										sheet.getRow(setupRow + 2).createCell(4).setCellValue(Double.parseDouble(
												jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000);
									} else {
										sheet.getRow(setupRow + 2).createCell(4)
												.setCellValue(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT));
									}
								}
							}

							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))) {

									sheet.getRow(setupRow + 2).createCell(5)
											.setCellValue(HistoricalConverter.convertVolume(Double
													.valueOf(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))));

								}
							}
							sheet.getRow(setupRow + 2).createCell(6)
									.setCellValue(jsonObject.getString(ApiConstant.USERNAME));
							sheet.getRow(setupRow + 2).createCell(7)
									.setCellValue(jsonObject.getString(ApiConstant.CREATED_ON));
						} else if (stepNo == 4) {
							if (!jsonObject.getString(ApiConstant.OLD_TMP).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_TMP)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_TMP))) {
									sheet.getRow(setupRow + 3).createCell(2)
											.setCellValue(jsonObject.getString(ApiConstant.NEW_TMP));
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_DURATION).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_DURATION)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_DURATION))) {
									sheet.getRow(setupRow + 3).createCell(3).setCellValue(
											Double.parseDouble(jsonObject.getString(ApiConstant.NEW_DURATION)) / 60);
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT))) {
									if (isKMPiorFS500) {
										sheet.getRow(setupRow + 3).createCell(4).setCellValue(Double.parseDouble(
												jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000);
									} else {
										sheet.getRow(setupRow + 3).createCell(4)
												.setCellValue(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT));
									}
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))) {

									sheet.getRow(setupRow + 3).createCell(5)
											.setCellValue(HistoricalConverter.convertVolume(Double
													.valueOf(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))));
								}
							}
							sheet.getRow(setupRow + 3).createCell(6)
									.setCellValue(jsonObject.getString(ApiConstant.USERNAME));
							sheet.getRow(setupRow + 3).createCell(7)
									.setCellValue(jsonObject.getString(ApiConstant.CREATED_ON));
						} else if (stepNo == 5) {
							if (!jsonObject.getString(ApiConstant.OLD_TMP).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_TMP)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_TMP))) {
									sheet.getRow(setupRow + 4).createCell(2)
											.setCellValue(jsonObject.getString(ApiConstant.NEW_TMP));
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_DURATION).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_DURATION)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_DURATION))) {
									sheet.getRow(setupRow + 4).createCell(3).setCellValue(
											Double.parseDouble(jsonObject.getString(ApiConstant.NEW_DURATION)) / 60);
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_WEIGHT)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT))) {
									if (isKMPiorFS500) {
										sheet.getRow(setupRow + 4).createCell(4).setCellValue(Double.parseDouble(
												jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT)) / 1000);
									} else {
										sheet.getRow(setupRow + 4).createCell(4)
												.setCellValue(jsonObject.getString(ApiConstant.NEW_PERMEATE_WEIGHT));
									}
								}
							}
							if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL).isEmpty()) {
								if (!jsonObject.getString(ApiConstant.OLD_PERMEATE_TOTAL)
										.equalsIgnoreCase(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))) {

									sheet.getRow(setupRow + 4).createCell(5)
											.setCellValue(HistoricalConverter.convertVolume(Double
													.valueOf(jsonObject.getString(ApiConstant.NEW_PERMEATE_TOTAL))));

								}
							}
							sheet.getRow(setupRow + 4).createCell(6)
									.setCellValue(jsonObject.getString(ApiConstant.USERNAME));
							sheet.getRow(setupRow + 4).createCell(7)
									.setCellValue(jsonObject.getString(ApiConstant.CREATED_ON));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	/**
	 * Adds the set point numbers.
	 *
	 * @param sheet SXSSFSheet
	 * @param setupRow the setup row
	 */
	private void addSetPointNumbers(SXSSFSheet sheet, final int setupRow) {
		int number = 0;
		for (int index = setupRow; index < setupRow + 5; index++) {
			SXSSFRow row = sheet.getRow(setupRow + number);
			if (row == null) {
				row = sheet.createRow(setupRow + number);
			}
			sheet.getRow(setupRow + number).createCell(1).setCellValue("Setpoint " + (number + 1));
			number++;
		}
	}

	@Override
	public void populateTrialTableSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId, JSONArray receipeJson,
			String modeName, int interval) throws Exception {
		new AutoModeExcelTemplate().populateTrialTableSheet(sxssfWorkbook, trialRunSettingId, receipeJson, modeName,
				interval);
	}

}
