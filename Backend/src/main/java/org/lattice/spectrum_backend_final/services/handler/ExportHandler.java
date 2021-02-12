package org.lattice.spectrum_backend_final.services.handler;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.export.excel.ExcelExportMain;
import org.lattice.spectrum_backend_final.export.pdf.PDFExport;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.sun.jersey.multipart.FormDataParam;

/**
 * The Class contains APIs for excel/pdf export.
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_HISTORICAL_PATH)
public class ExportHandler {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT;	
	private UserAuthorization authorization = new UserAuthorization();

	static {
		SIMPLE_DATE_FORMAT = new SimpleDateFormat(ApiConstant.EXCEL_DATE_FORMAT);
	}

	/**
	 * Service to download excel.
	 *
	 * @param trialRunSettingId the trial run setting id
	 * @param trialMasterId the trial master id
	 * @param trialId the trial id
	 * @param interval the interval
	 * @param headers the headers
	 * @return the response downloaded file name and location
	 * @throws JSONException the JSON exception
	 * @throws Exception     the exception
	 */
	@GET
	@Path(value = ApiConstant.REST_DOWNLOAD_HIST_TRIAL_LOGS_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadTrialLogs(
			@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) final int trialRunSettingId,
			@PathParam(ApiConstant.PARAM_TRIAL_MASTER_ID) final int trialMasterId,
			@QueryParam(value = ApiConstant.TRIAL_ID_PATH_PARAM) String trialId,
			@QueryParam(value = ApiConstant.INTERVAL) final int interval, @Context HttpHeaders headers)
			throws JSONException, Exception {

		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)
				&& !authorization.isUserRoleEqualsSuperAdmin(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "downloadTrialLogs", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}

		final String fileLocation = getExcelFileDownloadPath(trialId);
		try {
			if (!new ExcelExportMain().createExcel(trialRunSettingId, trialMasterId, trialId, fileLocation, interval)) {
				deleteFailedFile(fileLocation);
				sLog.d(this, "downloadTrialLogs, STATUS: " + HttpStatus.INTERNAL_SERVER_ERROR_500);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson
						.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION).toString())
						.build();
			}
			responseJson.put(ApiConstant.FILE_NAME, trialId + ApiConstant.EXCEL_FILE_EXTENTION);
			responseJson.put(ApiConstant.FILE_LOCATION, new File(fileLocation).getCanonicalPath());
			Logger.info(this, "downloadTrialLogs", HttpStatus.OK_200);
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			deleteFailedFile(fileLocation);
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "downloadTrialLogs", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	
	/**
	 * Gets the historical PDF.
	 *
	 * @param trialRunSettingId the trial run setting id
	 * @param trialMasterId the trial master id
	 * @param interval the interval
	 * @param headers the headers
	 * @return the historical PDF data
	 * @throws JSONException the JSON exception
	 * @throws Exception     the exception
	 */
	@GET
	@Path(value = ApiConstant.REST_PDF_HIST_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHistoricalPDF(@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
			@PathParam(ApiConstant.PARAM_TRIAL_MASTER_ID) int trialMasterId,
			@QueryParam(value = ApiConstant.INTERVAL) final int interval, @Context HttpHeaders headers)
			throws JSONException, Exception {

		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)
				&& !authorization.isUserRoleEqualsSuperAdmin(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getHistoricalPDF", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}
		try {
			JSONObject trialData = PDFExport.getInstance().fetchDataForPDF(trialRunSettingId, trialMasterId, interval);
			Logger.info(this, "getHistoricalPDF", HttpStatus.OK_200);
			return Response.ok(trialData.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getHistoricalPDF", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Download PDF.
	 *
	 * @param fileInputStream the file input stream
	 * @param trialId the trial id
	 * @param headers the headers
	 * @return the response
	 * @throws JSONException the JSON exception
	 * @throws Exception     the exception
	 */
	@POST
	@Path(value = ApiConstant.REST_PDF_DOWNLOAD_PATH)
	@Consumes(value = MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadPDF(@FormDataParam(ApiConstant.FILE) final InputStream fileInputStream,
			@PathParam(ApiConstant.TRIAL_ID_PATH_PARAM) final String trialId, @Context HttpHeaders headers)
			throws JSONException, Exception {
		final JSONObject responseJSON = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)
					&& !authorization.isUserRoleEqualsSuperAdmin(headers)) {
				responseJSON.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "downloadPDF", HttpStatus.UNAUTHORIZED_401, responseJSON);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJSON.toString()).build();
			}
			final String fileLocation = MessageFormat.format(
					getFileDownloadDirectory().getAbsolutePath() + ApiConstant.PDF_FILE_NAME_FORMAT,
					SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis())), trialId);

			final File exportedPDF = PDFExport.exportPDF(fileInputStream, fileLocation);
			responseJSON.put(ApiConstant.FILE_NAME, exportedPDF.getName());
			responseJSON.put(ApiConstant.FILE_LOCATION, exportedPDF.getCanonicalPath());
			Logger.info(this, "downloadPDF", HttpStatus.OK_200);
			return Response.ok(responseJSON.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(this, "downloadPDF", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
					.entity(responseJSON.put(ApiConstant.ERROR, ApiConstant.ERROR_DOWNLOAD_FILE)).build();
		}
	}

	/**
	 * Open download directory.
	 *
	 * @param headers
	 * @return the response
	 */
	@GET
	@Path(ApiConstant.REST_OPEN_DIR_PATH)
	public Response openDownloadDirectory(@Context HttpHeaders headers) {

		JSONObject responseJson = null;
		try {
			if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
				responseJson = new JSONObject();
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "openDownloadDirectory", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}

			Desktop.getDesktop().open(new File(ApiConstant.EXCEL_DOWNLOAD_DIR));
			return Response.ok().build();
		} catch (IllegalArgumentException | IOException e) {
			responseJson = new JSONObject();
			responseJson.put(ApiConstant.ERROR, ApiConstant.DIRECTORY_NOT_FOUND);
			Logger.error(this, "openDownloadDirectory", HttpStatus.NOT_FOUND_404, responseJson);
			return Response.status(Response.Status.NOT_FOUND).entity(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson = new JSONObject();
			responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			Logger.error(this, "openDownloadDirectory", HttpStatus.INTERNAL_SERVER_ERROR_500, responseJson);
			return Response.serverError().entity(responseJson.toString()).build();
		}
	}
	
	/**
	 * Gets the excel file download path.
	 *
	 * @param trialId the trial id
	 * @return the excel file download path
	 */
	public final String getExcelFileDownloadPath(String trialId) {
		return MessageFormat.format(getFileDownloadDirectory().getAbsolutePath() + ApiConstant.EXCEL_FILE_NAME_FORMAT,
				SIMPLE_DATE_FORMAT.format(new Date(System.currentTimeMillis())), trialId);
	}

	/**
	 * Gets the file download directory.
	 *
	 * @return the file download directory
	 */
	private File getFileDownloadDirectory() {
		final File dirLocation = new File(ApiConstant.EXCEL_DOWNLOAD_DIR);
		if (!dirLocation.isDirectory()) {
			dirLocation.mkdir();
		}
		return dirLocation;
	}

	/**
	 * Delete failed file.
	 *
	 * @param fileName the file name
	 */
	private void deleteFailedFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			sLog.d(this, "Exception, deleting file...");
			if (!file.delete()) {
				sLog.d(this, "Error, file not deleted");
			}
			sLog.d(this, "File deleted!!");
		}
	}

}
