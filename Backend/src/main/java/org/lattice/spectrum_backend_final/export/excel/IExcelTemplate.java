package org.lattice.spectrum_backend_final.export.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * The Interface contains abstract and default methods for common excel operations.
 */
public interface IExcelTemplate {

	/**
	 * Populate trial details sheet.
	 *
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @param trialId
	 * @param receipeJson
	 * @throws Exception the exception
	 */
	void populateTrialDetailsSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId, String trialId,
			JSONArray receipeJson) throws Exception;

	/**
	 * Populate trial table sheet.
	 *
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @param receipeJson
	 * @param modeName
	 * @param interval
	 * @throws Exception the exception
	 */
	void populateTrialTableSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId, JSONArray receipeJson,
			String modeName, int interval) throws Exception;

	/**
	 * Populate alarms sheet.
	 *
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	default void populateAlarmsSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId) throws IOException {
		SXSSFSheet sheet = null;
		try {
			final JSONArray alarmsArray = ExportUtil.getInstance().fetchAlarmLogs(trialRunSettingId);
			if (!alarmsArray.isEmpty()) {
				sheet = sxssfWorkbook.getSheet("Alarms");
				if (sheet == null) {
					throw new RuntimeException("Excel sheet 'Alarms' not found in excel template, please check.");
				}
				sheet.setRandomAccessWindowSize(10);
				SXSSFRow row = null;
				int index = 1;
				for (int i = 0; i < alarmsArray.length(); i++) {
					row = sheet.getRow(index);
					if (row == null) {
						row = sheet.createRow(index);
					}
					row.createCell(0)
							.setCellValue(alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_DESCRIPTION));
					row.createCell(1)
							.setCellValue(alarmsArray.getJSONObject(i).getString(ApiConstant.ALARM_STOP_VALUE));
					row.createCell(2).setCellValue(alarmsArray.getJSONObject(i).getString(ApiConstant.NATURE));
					row.createCell(3).setCellValue(alarmsArray.getJSONObject(i).getString(ApiConstant.CREATED_ON));
					index++;
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
	 * Populate notes sheet.
	 *
	 * @param sxssfWorkbook
	 * @param trialRunSettingId
	 * @throws Exception the exception
	 */
	default void populateNotesSheet(SXSSFWorkbook sxssfWorkbook, int trialRunSettingId) throws Exception {
		SXSSFSheet sheet = null;
		try {
			final JSONArray logsArray = ExportUtil.getInstance().fetchTrialLogs(trialRunSettingId, false);
			if (!logsArray.isEmpty()) {
				sheet = sxssfWorkbook.getSheet("Notes");
				if (sheet == null) {
					throw new RuntimeException("Excel sheet 'Notes' not found in excel template, please check.");
				}
				sheet.setRandomAccessWindowSize(10);
				int index = 1;
				SXSSFRow row = null;
				for (int i = 0; i < logsArray.length(); i++) {
					row = sheet.getRow(index);
					if (row == null) {
						row = sheet.createRow(index);
					}
					row.createCell(0).setCellValue(logsArray.getJSONObject(i).getString(ApiConstant.USERNAME));
					row.createCell(1).setCellValue(logsArray.getJSONObject(i).getString(ApiConstant.FULL_NAME));
					row.createCell(2).setCellValue(logsArray.getJSONObject(i).getString(ApiConstant.ROLE_DESCRIPTION));
					row.createCell(3).setCellValue(logsArray.getJSONObject(i).getString(ApiConstant.ACTION));
					row.createCell(4).setCellValue(logsArray.getJSONObject(i).getString(ApiConstant.CREATED_ON));
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (sheet != null) {
				sheet.flushRows();
			}
			System.gc();
		}
	}

	/**
	 * Encrypt excel file.
	 *
	 * @param trialId
	 * @param workbook
	 * @param fileName
	 * @throws InvalidFormatException the invalid format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws GeneralSecurityException the general security exception
	 * @throws InterruptedException the interrupted exception
	 */
	default void encryptExcelFile(String trialId, SXSSFWorkbook workbook, String fileName)
			throws InvalidFormatException, IOException, GeneralSecurityException, InterruptedException {
		try (POIFSFileSystem poifsFileSystem = new POIFSFileSystem()) {
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile, CipherAlgorithm.aes192, HashAlgorithm.sha384,
					-1, -1, null);
			final Encryptor encryptor = info.getEncryptor();
			encryptor.confirmPassword(trialId);
			File file = new File(fileName);
			final FileOutputStream fileOutputStream = new FileOutputStream(file);
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			workbook.close();
			workbook.dispose();

			try (OPCPackage opcPackage = OPCPackage.open(file, PackageAccess.READ_WRITE);
					OutputStream outputStream = encryptor.getDataStream(poifsFileSystem)) {
				opcPackage.save(outputStream);
			}
			// Write out the encrypted version
			try (FileOutputStream fos = new FileOutputStream(file)) {
				poifsFileSystem.writeFilesystem(fos);
			}
			sLog.d(this, "Excel file encrypted!!");
		} finally {
			System.gc();
		}
	}

	/**
	 * Protect sheets and write file.
	 *
	 * @param fileName
	 * @param workbook
	 * @param trialId
	 * @throws Exception the exception
	 */
	default void protectSheetsAndWriteFile(String fileName, SXSSFWorkbook workbook, String trialId) throws Exception {
		try {
			workbook.setActiveSheet(workbook.getFirstVisibleTab());
			workbook.forEach(sheet -> {
				sheet.protectSheet(trialId);
			});

			File file = new File(fileName);
			final FileOutputStream fileOutputStream = new FileOutputStream(file);
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			workbook.close();
			workbook.dispose();
			sLog.d(this, "Excel file generated!!");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			System.gc();
		}
	}

}
