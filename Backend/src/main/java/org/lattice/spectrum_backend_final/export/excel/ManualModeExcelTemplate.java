package org.lattice.spectrum_backend_final.export.excel;

import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertPressure;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertScale;
import static org.lattice.spectrum_backend_final.export.excel.ExcelExportMain.decimalFormat;
import static org.lattice.spectrum_backend_final.export.excel.ExcelExportMain.threeDecimalFormat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.ManualTrialManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.GraphUtil;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * The Class used to set values in the manual mode excel template.
 * 
 * @author Pinak
 */
public final class ManualModeExcelTemplate implements IExcelTemplate {

	private JSONObject manualFilterJSON = null;

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
			JSONArray recipeJSON) throws Exception {
		System.gc();
		SXSSFSheet sheet = null;

		try {
			sheet = sxssfWorkbook.getSheet("Trial details");
			if (sheet == null) {
				throw new RuntimeException("Excel sheet 'Trial details' not found in excel template, please check.");
			}
			sheet.setDefaultColumnWidth(20);
			sheet.setDefaultRowHeightInPoints(14);
			sheet.setRandomAccessWindowSize(15);

			final JSONObject trialRS = ExportUtil.getInstance().fetchTrialDetails(trialRunSettingId);
			final boolean isKMPiorFS500 = ApiConstant.KMPI.equalsIgnoreCase(HistoricalConverter.getMainPumpName())
					|| ApiConstant.KROSFLOFS500.equalsIgnoreCase(HistoricalConverter.getMainPumpName());

			manualFilterJSON = ManualTrialManager.getInstance().getManualFilterDetails(trialRunSettingId);

			Font font = sxssfWorkbook.createFont();
			font.setFontName(ApiConstant.FONT_ARIAL);
			font.setFontHeightInPoints(ApiConstant.FONT_SIZE);
			font.setBold(true);

			CellStyle boldStyle = sxssfWorkbook.createCellStyle();
			boldStyle.setFont(font);

			CellStyle backgroundColorStyle = sxssfWorkbook.createCellStyle();
			backgroundColorStyle.cloneStyleFrom(boldStyle);
			backgroundColorStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			backgroundColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			sheet.createRow(0).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(0).setCellValue("Trial ID");
			sheet.getRow(0).createCell(1).setCellValue(trialRS.getString(ApiConstant.TRIAL_ID));
			sheet.getRow(0).createCell(2).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(2).setCellValue("Recipe name");
			sheet.getRow(0).createCell(3).setCellValue(ApiConstant.MANUAL_MODE);
			sheet.getRow(0).createCell(4).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(4).setCellValue("Type");
			sheet.getRow(0).createCell(5).setCellValue(ApiConstant.MANUAL);
			sheet.getRow(0).createCell(6).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(6).setCellValue("Downloaded By");
			sheet.getRow(0).createCell(7)
					.setCellValue(DbConnectionManager.getInstance().getTokenManager().getUsername());
			sheet.getRow(0).createCell(8).setCellStyle(backgroundColorStyle);
			sheet.getRow(0).getCell(8).setCellValue("Downloaded On");
			sheet.getRow(0).createCell(9)
					.setCellValue(BasicUtility.getInstance().convertUnixToDate(System.currentTimeMillis() / 1000));

			sheet.createRow(2).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(2).getCell(0).setCellValue("Trial duration");

			sheet.createRow(3).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(1).setCellValue("Trial start time");
			sheet.getRow(3).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(2).setCellValue("Trial end time");
			sheet.getRow(3).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(3).getCell(3).setCellValue("Total Trial Duration");

			sheet.createRow(4).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(4).getCell(0).setCellValue(ApiConstant.UNITS_DESCRIPTION);
			sheet.getRow(4).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(4).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);

			sheet.createRow(5).createCell(1).setCellValue(trialRS.getString(ApiConstant.TRIAL_START_TIME));
			sheet.getRow(5).createCell(2).setCellValue(trialRS.getString(ApiConstant.TRIAL_END_TIME));
			sheet.getRow(5).createCell(3).setCellValue(DatabaseManager.getInstance().getTrialDuration(
					trialRS.getString(ApiConstant.TRIAL_START_TIME), trialRS.getString(ApiConstant.TRIAL_END_TIME)));

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
			sheet.getRow(8).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(8).getCell(6).setCellValue("Valve 2 connector");

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
			sheet.getRow(10).createCell(4).setCellValue(qPermFreq);
			sheet.getRow(10).createCell(5).setCellValue(trialRS.getString(ApiConstant.DEVICE_ID));
			if (isKMPiorFS500) {
				sheet.getRow(10).createCell(6).setCellValue(trialRS.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN));
			} else {
				sheet.getRow(10).createCell(6).setCellValue(ApiConstant.NOT_APPLICABLE);
			}

			if (isKMPiorFS500) {
				if (!trialRS.getString(ApiConstant.FEED_HOLD_UP).isEmpty()) {
					sheet.getRow(9).createCell(2).setCellValue(ApiConstant.KILOGRAM);
					sheet.getRow(10).createCell(2).setCellValue(decimalFormat
							.format(Double.parseDouble(trialRS.getString(ApiConstant.FEED_HOLD_UP)) / 1000));
				}
			} else {
				sheet.getRow(9).createCell(2).setCellValue(ApiConstant.GRAM);
				sheet.getRow(10).createCell(2).setCellValue(trialRS.getString(ApiConstant.FEED_HOLD_UP));
			}
			sheet.getRow(10).createCell(3).setCellValue(ApiConstant.NOT_APPLICABLE);
			sheet.getRow(10).createCell(7).setCellValue("NA = Feed start weight not entered by user");

			// filter details
			// hollow fiber
			sheet.createRow(12).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(12).getCell(0).setCellValue("Hollow fiber filter");
			sheet.createRow(13).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(13).getCell(1).setCellValue("Non-Repligen Filter");
			sheet.getRow(13).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(13).getCell(2).setCellValue("Part #");
			sheet.getRow(13).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(13).getCell(3).setCellValue("Fiber count");
			sheet.getRow(13).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(13).getCell(4).setCellValue("Fiber ID");
			sheet.getRow(13).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(13).getCell(5).setCellValue("Surface area");
			sheet.getRow(13).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(13).getCell(6).setCellValue("Extra Capillary Space (ECS)");

			sheet.createRow(14).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(14).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(14).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(14).createCell(3).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(14).createCell(4).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(14).createCell(5).setCellValue("sq cm");
			sheet.getRow(14).createCell(6).setCellValue(ApiConstant.MILLILITER);

			// cassette
			sheet.createRow(17).createCell(0).setCellStyle(backgroundColorStyle);
			sheet.getRow(17).getCell(0).setCellValue("Cassette fiber");
			sheet.createRow(18).createCell(1).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(1).setCellValue("Non-Repligen Filter");
			sheet.getRow(18).createCell(2).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(2).setCellValue("Part #");
			sheet.getRow(18).createCell(3).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(3).setCellValue("Width");
			sheet.getRow(18).createCell(4).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(4).setCellValue("Height");
			sheet.getRow(18).createCell(5).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(5).setCellValue("Number of cassettes");
			sheet.getRow(18).createCell(6).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(6).setCellValue("Number of channels");
			sheet.getRow(18).createCell(7).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(7).setCellValue("Surface area");
			sheet.getRow(18).createCell(8).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(8).setCellValue("Total surface area");
			sheet.getRow(18).createCell(9).setCellStyle(boldStyle);
			sheet.getRow(18).getCell(9).setCellValue("Extra Capillary Space (ECS)");

			sheet.createRow(19).createCell(0).setCellStyle(boldStyle);
			sheet.getRow(19).createCell(1).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(19).createCell(2).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(19).createCell(3).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(19).createCell(4).setCellValue(ApiConstant.CENTIMETER);
			sheet.getRow(19).createCell(5).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(19).createCell(6).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			sheet.getRow(19).createCell(7).setCellValue("sq m");
			sheet.getRow(19).createCell(8).setCellValue("sq m");
			sheet.getRow(19).createCell(9).setCellValue(ApiConstant.MILLILITER);

			if (manualFilterJSON != null && !manualFilterJSON.isEmpty()) {
				if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER
						.equals(manualFilterJSON.getString(ApiConstant.FILTER_TYPE))) {

					if (manualFilterJSON.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
						sheet.createRow(15).createCell(1).setCellValue(ApiConstant.NO);
						sheet.getRow(15).createCell(2).setCellValue(manualFilterJSON.getString(ApiConstant.PART_NO));
						sheet.getRow(15).createCell(3).setCellValue(manualFilterJSON.getInt(ApiConstant.FIBER_COUNT));
						sheet.getRow(15).createCell(4).setCellValue(manualFilterJSON.getDouble(ApiConstant.FIBER_ID));
						sheet.getRow(15).createCell(5).setCellValue(manualFilterJSON.getDouble(ApiConstant.SURF_AREA));
						sheet.getRow(15).createCell(6).setCellValue(manualFilterJSON.getDouble(ApiConstant.ECS));
					} else {
						sheet.createRow(15).createCell(1).setCellValue(ApiConstant.YES);
						sheet.getRow(15).createCell(5).setCellValue(manualFilterJSON.getString(ApiConstant.SURF_AREA));
					}

				} else if (ApiConstant.CASSETTE.equals(manualFilterJSON.getString(ApiConstant.FILTER_TYPE))) {

					if (manualFilterJSON.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
						sheet.createRow(20).createCell(1).setCellValue(ApiConstant.NO);
						sheet.getRow(20).createCell(3).setCellValue(manualFilterJSON.getDouble(ApiConstant.WIDTH));
						sheet.getRow(20).createCell(4).setCellValue(manualFilterJSON.getDouble(ApiConstant.HEIGHT));
						sheet.getRow(20).createCell(6).setCellValue(manualFilterJSON.getInt(ApiConstant.NO_OF_CHANNEL));
						sheet.getRow(20).createCell(9).setCellValue(manualFilterJSON.getDouble(ApiConstant.ECS));
						sheet.getRow(20).createCell(7).setCellValue(manualFilterJSON.getDouble(ApiConstant.SURF_AREA));
						sheet.getRow(20).createCell(2).setCellValue(manualFilterJSON.getString(ApiConstant.PART_NO));
					} else {
						sheet.createRow(20).createCell(1).setCellValue(ApiConstant.YES);
						sheet.getRow(20).createCell(7).setCellValue(manualFilterJSON.getString(ApiConstant.SURF_AREA));
					}

					if (manualFilterJSON.has(ApiConstant.NO_OF_CASSETTE)
							&& !manualFilterJSON.isNull(ApiConstant.NO_OF_CASSETTE)) {
						sheet.getRow(20).createCell(5)
								.setCellValue(manualFilterJSON.getInt(ApiConstant.NO_OF_CASSETTE));
					}

					if (manualFilterJSON.has(ApiConstant.TOTAL_SURF_AREA)
							&& !manualFilterJSON.isNull(ApiConstant.TOTAL_SURF_AREA)) {
						sheet.getRow(20).createCell(8)
								.setCellValue(manualFilterJSON.getDouble(ApiConstant.TOTAL_SURF_AREA));
					}
				}
			}
		} catch (

		Exception e) {
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
	 * Populates Trial Table Sheet
	 * 
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @param receipeJson
	 * @param modeName
	 * @param interval
	 */
	@Override
	public void populateTrialTableSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId, JSONArray receipeJson,
			String modeName, int interval) throws IOException {
		Connection con = null;
		PreparedStatement fetchTableLogStmt = null;
		ResultSet resultSet = null;
		String query = null;
		SXSSFSheet sheet = null;
		int index = 1;

		try {
			sheet = sxssfWorkbook.getSheet("Trial tables");
			if (sheet == null) {
				throw new RuntimeException("Excel sheet 'Trial tables' not found in file, please check.");
			}
			sheet.setRandomAccessWindowSize(20);

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

			for (int i = 3; i <= 7; i++) {
				row.getCell(i).setCellValue(
						Converter.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()));
			}

			boolean isKMPiorFS500 = false;
			String mainPump = BasicUtility.getInstance().getMainPumpName(trialRunSettingId);
			if (mainPump != null && !mainPump.isEmpty()) {
				if (ApiConstant.KMPI.equalsIgnoreCase(mainPump)
						|| ApiConstant.KROSFLOFS500.equalsIgnoreCase(mainPump)) {
					isKMPiorFS500 = true;
					for (int i = 8; i <= 10; i++) {
						row.getCell(i).setCellValue(ApiConstant.LITER_PER_MIN);
					}
				} else {
					for (int i = 8; i <= 10; i++) {
						row.getCell(i).setCellValue(ApiConstant.ML_MIN);
					}
				}
			} else {
				for (int i = 8; i <= 10; i++) {
					row.getCell(i).setCellValue(ApiConstant.ML_MIN);
				}
			}

			row.getCell(11).setCellValue(ApiConstant.RPM);

			row.getCell(12).setCellValue(
					Converter.systemSettingWeightMapper(RequestHandler.getStSystemSetting().getWeightUnit()));
			row.getCell(13).setCellValue(
					Converter.systemSettingWeightMapper(RequestHandler.getStSystemSetting().getWeightUnit()));

			row.getCell(14).setCellValue(ApiConstant.GRAM);
			row.getCell(15).setCellValue(ApiConstant.GRAM);
			row.getCell(16).setCellValue(ApiConstant.GRAM);
			row.getCell(17).setCellValue(ApiConstant.X);
			row.getCell(18).setCellValue(ApiConstant.X);
			row.getCell(19).setCellValue(ApiConstant.X);
			row.getCell(20).setCellValue(ApiConstant.PERCENT);
			row.getCell(21).setCellValue(ApiConstant.PERCENT);
			row.getCell(22).setCellValue(ApiConstant.ML_MIN);
			row.getCell(23).setCellValue(ApiConstant.ML_MIN);
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
			row.getCell(35).setCellValue(ApiConstant.ML_MIN);
			row.getCell(36).setCellValue(ApiConstant.ML_MIN);
			row.getCell(37).setCellValue(ApiConstant.UNIT_FOR_PROTEIN_CONCENTRATION);
			row.getCell(38).setCellValue(ApiConstant.UNIT_FOR_PROTEIN_CONCENTRATION);
			row.getCell(39).setCellValue(ApiConstant.UNIT_FOR_FLUX);
			row.getCell(40).setCellValue(ApiConstant.UNIT_FOR_SHEAR);
			row.getCell(41).setCellValue(ApiConstant.UNIT_FOR_CROSS_FLOW_FLUX);
			row.getCell(42).setCellValue(ApiConstant.DOUBLE_HYPHEN);
			row.getCell(43).setCellValue(ApiConstant.UNIT_FOR_FLUX);
			row.getCell(44).setCellValue(ApiConstant.UNIT_FOR_WATER_PERMEABILITY);
			row.getCell(45).setCellValue(ApiConstant.UNIT_FOR_NWP);

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

					// pressure
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
					row.createCell(12).setCellValue(
							Double.parseDouble(convertScale(resultSet.getDouble(ApiConstant.FEED_SCALE))));
					row.createCell(13).setCellValue(
							Double.parseDouble(convertScale(resultSet.getDouble(ApiConstant.M_PERMEATE))));

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

					if (resultSet.getObject(ApiConstant.VT) != null) {
						row.createCell(24).setCellValue(resultSet.getDouble(ApiConstant.VT));
					}

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
					}

					if (resultSet.getObject(ApiConstant.FLUX) != null) {
						row.createCell(39).setCellValue(resultSet.getDouble(ApiConstant.FLUX));
					}

					if (resultSet.getObject(ApiConstant.SHEAR) != null && manualFilterJSON != null
							&& !manualFilterJSON.isEmpty()) {

						if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER
								.equals(manualFilterJSON.getString(ApiConstant.FILTER_TYPE))) {

							row.createCell(40).setCellValue(resultSet.getString(ApiConstant.SHEAR));

						} else if (ApiConstant.CASSETTE.equals(manualFilterJSON.getString(ApiConstant.FILTER_TYPE))) {

							row.createCell(41).setCellValue(resultSet.getString(ApiConstant.SHEAR));

						}
					}

					if (resultSet.getObject(ApiConstant.TEMPERATURE_CORRECTION_FACTOR) != null) {
						if (ApiConstant.NOT_APPLICABLE
								.equalsIgnoreCase(resultSet.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR))) {
							row.createCell(42).setCellValue(ApiConstant.BLANK_QUOTE);
						} else {
							try {
								row.createCell(42).setCellValue(Double
										.parseDouble(resultSet.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR)));
							} catch (NumberFormatException e) {
								row.createCell(42).setCellValue(ApiConstant.BLANK_QUOTE);
							}
						}
					}

					if (resultSet.getObject(ApiConstant.WATER_FLUX_20_DEGREE) != null) {
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
					}

					if (resultSet.getObject(ApiConstant.WATER_PERMEABILITY) != null) {
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

					if (resultSet.getObject(ApiConstant.NWP) != null) {
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
			manualFilterJSON = null;
			DbConnectionManager.closeDBConnection(resultSet, fetchTableLogStmt, con);
			if (sheet != null) {
				sheet.flushRows();
			}
			System.gc();
		}
	}
}
