package org.lattice.spectrum_backend_final.services.handler;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.SystemSettingsManager;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class used to fetch and save system setting details.
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_SYSTEM_SETTINGS_ENTRY_POINT)
public class SystemSettingsHandler {

	private SystemSettingsManager settingsManager = new SystemSettingsManager();
	private UserAuthorization authorization = new UserAuthorization();

	/**
	 *  API_DOC_1: Service to save spectrum system setting details.
	 *
	 * @param systemSetting the request system setting
	 * @param headers
	 * @return the response
	 */
	@POST
	@Path(value = "/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveSystemSettings(SystemSetting systemSetting, @Context HttpHeaders headers) {

		JSONObject responseJson = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "saveSystemSettings", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			int systemId = settingsManager.insertSystemSettings(systemSetting);
			if (systemId == 0) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SYSTEM_SETTING_NOT_SAVED_ERROR);
				Logger.error(this, "saveSystemSettings", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}
			// set other settings active status to 0
			settingsManager.inactivePreviousSettings(systemId);
			responseJson.put(ApiConstant.SUCCESS, ApiConstant.SYSTEM_SETTINGS_CREATED_MESSAGE);
			Logger.info(this, "saveSystemSettings", HttpStatus.OK_200, responseJson);
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "saveSystemSettings", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 *  API_DOC_2: Service to fetch spectrum system setting details.
	 *
	 * @param headers
	 * @return the system settings
	 */
	@GET
	@Path(value = "/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSystemSettings(@Context HttpHeaders headers) {

		JSONObject responseJson = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)
					&& !authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "getSystemSettings", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			SystemSetting fetchedSetting = settingsManager.fetchSystemSettings();
			if (fetchedSetting == null) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SYSTEM_SETTING_NOT_FOUND_ERROR);
				Logger.error(this, "getSystemSettingDetails", HttpStatus.NOT_FOUND_404, responseJson);
				return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
			}
			Logger.info(this, "getSystemSettingDetails", HttpStatus.OK_200);
			return Response.ok(new ObjectMapper().writeValueAsString(fetchedSetting)).build();

		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getSystemSettingDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

}
