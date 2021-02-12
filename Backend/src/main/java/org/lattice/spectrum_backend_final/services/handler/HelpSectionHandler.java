package org.lattice.spectrum_backend_final.services.handler;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.CfrManager;
import org.lattice.spectrum_backend_final.dao.manager.FirmwareManager;
import org.lattice.spectrum_backend_final.dao.util.GitCommitUtil;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * The Class HelpSectionHandler used to fetch and download user manual files.
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_ROOT_ENTRY_POINT)
public class HelpSectionHandler {

	private static HelpSectionHandler helpSectionHandler = null;

	/**
	 * Gets the single instance of HelpSectionHandler.
	 *
	 * @return single instance of HelpSectionHandler
	 */
	public static HelpSectionHandler getInstance() {

		synchronized (HelpSectionHandler.class) {
			if (helpSectionHandler == null) {
				helpSectionHandler = new HelpSectionHandler();
			}
		}
		return helpSectionHandler;
	}

	private UserAuthorization authorization = new UserAuthorization();
	private CfrManager cfrManager = new CfrManager();

	/**
	 * API_DOC_1: Service to fetch user manual file names.
	 *
	 * @param headers
	 * @return the file names list
	 */
	@GET
	@Path(value = ApiConstant.REST_GET_FILE_NAME_END_POINT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFileName(@Context HttpHeaders headers) {

		JSONObject responseJson = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)
					&& !authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "getFileName", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			File folder = null;
			if (cfrManager.get21CfrStatus() == 0) {
				folder = new File(ApiConstant.USER_MANUAL_KFCOMM2_FOLDER_PATH);
			} else {
				folder = new File(ApiConstant.USER_MANUAL_KFCOMM2C_FOLDER_PATH);
			}
			if (!folder.exists()) {
				// Error: UserManual folder not found
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d(this, "getFileName, Directory does not exists.");
				Logger.error(this, "getFileName", HttpStatus.INTERNAL_SERVER_ERROR_500, responseJson);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
			}
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length == 0) {
				// Error: no files present in user manual folder
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d(this, "getFileName, No files present in user manual folder");
				Logger.error(this, "getFileName", HttpStatus.INTERNAL_SERVER_ERROR_500, responseJson);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
			}

			final JSONArray fileNamesArray = new JSONArray();
			for (int i = 0; i < listOfFiles.length; i++) {
				String fileName = listOfFiles[i].getName();
				if (fileName.contains(ApiConstant.PERCENT)) {
					final JSONObject jsonObject = new JSONObject();
					jsonObject.put(ApiConstant.POSITION, fileName.substring(0, 1));
					jsonObject.put(ApiConstant.HEADING, fileName.substring(fileName.indexOf(ApiConstant.PERCENT) + 1,
							fileName.lastIndexOf(ApiConstant.PERCENT)));
					jsonObject.put(ApiConstant.FILE_NAME,
							fileName.substring(fileName.lastIndexOf(ApiConstant.PERCENT) + 1));
					fileNamesArray.put(jsonObject);
				}
			}
			responseJson.put(ApiConstant.FILE_NAMES, fileNamesArray);
			responseJson.put(ApiConstant.VERSION_DETAILS, FirmwareManager.getFirmwareVersionFromDb());
			responseJson.getJSONObject(ApiConstant.VERSION_DETAILS).put(ApiConstant.COMMIT,
					new GitCommitUtil().fetchLatestCommit());
			Logger.info(this, "getFileName", HttpStatus.OK_200);
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			e.printStackTrace();
			Logger.error(this, "getFileName", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.serverError().entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_2: Service to download file.
	 *
	 * @param headers
	 * @param position the position of the file in the list
	 * @return the response
	 */
	@GET
	@Path(value = ApiConstant.REST_DOWNLOAD_USER_MANUAL_END_POINT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response downloadUserManual(@Context HttpHeaders headers,
			@QueryParam(value = ApiConstant.PARAM_POSITION) String position) {
		JSONObject responseJson = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)
					&& !authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "downloadUserManual", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			File folder = null;
			if (cfrManager.get21CfrStatus() == 0) {
				folder = new File(ApiConstant.USER_MANUAL_KFCOMM2_FOLDER_PATH);
			} else {
				folder = new File(ApiConstant.USER_MANUAL_KFCOMM2C_FOLDER_PATH);
			}
			if (!folder.exists()) {
				// Error: UserManual folder not found
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d(this, "getFileName, Directory does not exists.");
				Logger.error(this, "downloadUserManual", HttpStatus.INTERNAL_SERVER_ERROR_500, responseJson);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
			}
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length == 0) {
				// Error: no files present in user manual folder
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d(this, "downloadUserManual, No files present in user manual folder");
				Logger.error(this, "downloadUserManual", HttpStatus.INTERNAL_SERVER_ERROR_500, responseJson);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
			}

			java.nio.file.Path filePath = null;
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].getName().contains(ApiConstant.PERCENT)) {
					if (position.equalsIgnoreCase(listOfFiles[i].getName().substring(0,
							listOfFiles[i].getName().indexOf(ApiConstant.PERCENT)))) {
						if (!listOfFiles[i].exists() || !listOfFiles[i].isFile()) {
							responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_USER_MANUAL_NOT_FOUND);
							sLog.d(this, ApiConstant.ERROR_USER_MANUAL_NOT_FOUND);
							return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
						}
						filePath = listOfFiles[i].toPath();
					}
				}
			}
			if (filePath == null) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d(this, "downloadUserManual, File path not found in user manual folder");
				Logger.error(this, "downloadUserManual", HttpStatus.INTERNAL_SERVER_ERROR_500, responseJson);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
			}
			byte[] fileContent = Files.readAllBytes(filePath);
			String value = Base64.getEncoder().encodeToString(fileContent);
			responseJson.put(ApiConstant.FILE, value);
			Logger.info(this, "downloadUserManual", HttpStatus.OK_200);
			return Response.ok().entity(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "downloadUserManual", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.serverError().entity(responseJson.toString()).build();
		}
	}

}
