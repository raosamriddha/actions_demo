package org.lattice.spectrum_backend_final.export.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;

/**
 * This Class is the entry point of excel download.
 * 
 * @author Pinak
 */
public final class ExcelExportMain {

	/** The Constant decimalFormat. */
	public static final DecimalFormat decimalFormat;
	
	/** The Constant threeDecimalFormat. */
	public static final DecimalFormat threeDecimalFormat;

	static {
		decimalFormat = new DecimalFormat(ApiConstant.UPTO_TWO_DECIMAL_PLACES, Converter.symbols);
		threeDecimalFormat = new DecimalFormat(ApiConstant.UPTO_THREE_DECIMAL_PLACES, Converter.symbols);
	}

	/**
	 * Creates the excel.
	 * Follow factory pattern for excel templates.
	 *
	 * @param trialRunSettingId
	 * @param trialMasterId
	 * @param trialId
	 * @param fileName
	 * @param interval
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public final boolean createExcel(int trialRunSettingId, int trialMasterId, String trialId, String fileName,
			final int interval) throws Exception {
		System.gc();
		JSONArray receipeJson = null;
		int mode = -1;
		String modeName = null;
		SXSSFWorkbook sxssfWorkbook = null;
		OPCPackage opcPackage = null;
		XSSFWorkbook workbook = null;

		try {
			if (trialMasterId > 0) {
				// auto or setup mode
				receipeJson = DatabaseManager.getInstance().getRecipe(trialMasterId);
				modeName = receipeJson.getJSONObject(0).getString(ApiConstant.MODE_NAME);
				mode = getModeByModeName(modeName);
			}

			opcPackage = OPCPackage.open(getExcelTemplateFile(mode), PackageAccess.READ_WRITE);
			workbook = new XSSFWorkbook(opcPackage);
			sxssfWorkbook = new SXSSFWorkbook(workbook, 30, true, true);
			sxssfWorkbook.setCompressTempFiles(true);

			IExcelTemplate excelTemplate = getTemplateInstance(mode);
			excelTemplate.populateTrialDetailsSheet(sxssfWorkbook, trialRunSettingId, trialId, receipeJson);
			excelTemplate.populateTrialTableSheet(sxssfWorkbook, trialRunSettingId, receipeJson, modeName, interval);
			excelTemplate.populateAlarmsSheet(sxssfWorkbook, trialRunSettingId);
			excelTemplate.populateNotesSheet(sxssfWorkbook, trialRunSettingId);
//			excelTemplate.encryptExcelFile(trialId, sxssfWorkbook, fileName);
			excelTemplate.protectSheetsAndWriteFile(fileName, sxssfWorkbook, trialId);

			sxssfWorkbook = null;
			workbook = null;
			opcPackage = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			closeWorkbookInstance(sxssfWorkbook, workbook);
			System.gc();

		}
	}

	/**
	 * Close workbook instance.
	 *
	 * @param sxssfWorkbook the sxssf workbook
	 * @param workbook
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// utility methods
	private void closeWorkbookInstance(SXSSFWorkbook sxssfWorkbook, XSSFWorkbook workbook) throws IOException {
		if (sxssfWorkbook != null) {
			sxssfWorkbook.close();
			sxssfWorkbook.dispose();
		}
		if (workbook != null) {
			workbook = null;
		}
	}

	/**
	 * Gets the mode integer value by mode name.
	 *
	 * @param modeName
	 * @return the mode int value
	 */
	private final int getModeByModeName(String modeName) {
		if (!ApiConstant.VACUUM_MODE.equalsIgnoreCase(modeName) && BasicUtility.getInstance().isRunMode(modeName)) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Gets the excel template file.
	 *
	 * @param mode the mode integer
	 * @return the excel template file
	 * @throws FileNotFoundException the file not found exception
	 */
	private final File getExcelTemplateFile(int mode) throws FileNotFoundException {
		File file = null;
		switch (mode) {
		case -1:
			// Load manual excel template file
			file = new File(ApiConstant.INPUT_EXCEL_FILE_PATH_MANUAL);
			break;
		case 0:
			// Load auto mode excel template file
			file = new File(ApiConstant.INPUT_EXCEL_FILE_PATH_AUTO);
			break;
		case 1:
			// Load setup breakdown excel template file
			file = new File(ApiConstant.INPUT_EXCEL_FILE_PATH_SETUP);
			break;
		default:
			throw new FileNotFoundException(ApiConstant.ERROR_EXCEL_TEMPLATE_NOT_FOUND);
		}
		return file;
	}

	/**
	 * Gets the template instance.
	 *
	 * @param mode the mode integer
	 * @return the template instance
	 */
	private final IExcelTemplate getTemplateInstance(int mode) {
		IExcelTemplate excelTemplate = null;
		switch (mode) {
		case -1:
			excelTemplate = new ManualModeExcelTemplate();
			break;
		case 0:
			excelTemplate = new AutoModeExcelTemplate();
			break;
		case 1:
			excelTemplate = new SetupModeExcelTemplate();
			break;
		default:
			break;
		}
		return excelTemplate;
	}

}
