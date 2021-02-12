package org.lattice.spectrum_backend_final.services.handler;

import java.net.ConnectException;
import java.sql.Connection;
import java.text.MessageFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.LicenseStatus;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.CfrManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.LicenseActivationManager;
import org.lattice.spectrum_backend_final.dao.manager.RegistryManager;
import org.lattice.spectrum_backend_final.dao.manager.ValidationManager;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.PasswordUtil;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.sun.jersey.api.container.filter.LoggingFilter;

/**
 * <p>A class containing APIs for handling software activation.</p>
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_ROOT_ENTRY_POINT)
public class LicenseActivationHandler {

	private LicenseActivationManager licenseActivationManager = new LicenseActivationManager();

	/**
	 * <b>API service to update license activation status.</b>
	 * 
	 * <p>Adds a default super admin account in the system after successful activation.</p>
	 * 
	 * @param licenseDetails 
	 * 		 <p>Request data that contains details like email, password and
	 *        license/activation key</p>
	 *        
	 * @throws Exception - For any uncaught exception
	 * @throws JSONException - If any exception in request JSON
	 * 
	 * @return Response status
	 */
	@PUT
	@Path(value = ApiConstant.REST_LICENSE_STATUS_END_POINT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveLicenseActivationStatus(final String licenseDetails) throws JSONException, Exception {
		JSONObject responseJson = new JSONObject();
		int userId = 0;
		Connection connection = null;
		try {
			Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
			WebTarget webTarget = client.target(ApiConstant.ACTIVATE_LICENSE_REQUEST_PATH);
			JSONObject jsonObject = new JSONObject(licenseDetails);
			jsonObject.put(ApiConstant.LIC_TYPE, new CfrManager().get21CfrStatus());
			Response response = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(jsonObject.toString()));
			JSONObject activateLicResJSON = new JSONObject(response.readEntity(String.class));

			if (response.getStatus() != 200) {
				responseJson.put(ApiConstant.ERROR, activateLicResJSON.getString(ApiConstant.ERROR_KEY));
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_ACCEPTABLE_406, responseJson);
				return Response.status(HttpStatus.NOT_ACCEPTABLE_406).entity(responseJson.toString()).build();
			}

			connection = DbConnectionManager.getInstance().getConnection();
			if (ValidationManager.isUsernameExist(activateLicResJSON.getString(ApiConstant.USERNAME), connection)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_DUPLICATE_USER_NAME);
				responseJson.put(ApiConstant.TYPE, 0);
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_ACCEPTABLE_406, responseJson);
				return Response.status(HttpStatus.NOT_ACCEPTABLE_406).entity(responseJson.toString()).build();
			}

			if (ValidationManager.isEmailExist(activateLicResJSON.getString(ApiConstant.EMAIL), connection)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_DUPLICATE_EMAIL);
				responseJson.put(ApiConstant.TYPE, 1);
				responseJson.put(ApiConstant.DETAILS, LicenseActivationManager
						.getUserDetailsByEmail(activateLicResJSON.getString(ApiConstant.EMAIL)));
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}

			userId = licenseActivationManager.createSuperAdmin(activateLicResJSON);
			if (userId <= 0) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.LICENSE_ACTIVATION_ERROR);
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}
			client.close();

			Client client2 = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
			WebTarget ackWebTarget = client2.target(
					MessageFormat.format(ApiConstant.LICENSE_ACK_PATH, activateLicResJSON.getInt(ApiConstant.LIC_ID)));
			Response ackResponse = ackWebTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(new JSONObject()));
			JSONObject messageJSON = new JSONObject(ackResponse.readEntity(String.class));

			if (ackResponse.getStatus() != 200) {
				deleteSuperAdminOnFailedRequest(userId);
				responseJson.put(ApiConstant.ERROR, messageJSON.getString(ApiConstant.ERROR_KEY));
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}
			client2.close();

			if (!licenseActivationManager.activateSuperAdminAccount(userId)) {
				deleteSuperAdminOnFailedRequest(userId);
				responseJson.put(ApiConstant.ERROR, ApiConstant.LICENSE_ACTIVATION_ERROR);
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}

			if (licenseActivationManager
					.updateLicenseActivation(PasswordUtil.encrypt(ApiConstant.ACTIVE, ApiConstant.SECRET)) <= 0) {
				deleteSuperAdminOnFailedRequest(userId);
				responseJson.put(ApiConstant.ERROR, ApiConstant.LICENSE_ACTIVATION_ERROR);
				Logger.error(this, "saveLicenseActivationStatus", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
				licenseActivationManager
						.updateLicenseActivation(PasswordUtil.encrypt(ApiConstant.INACTIVE, ApiConstant.SECRET));
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}
			// create registry entry set
			RegistryManager.createRegistryHash();
			responseJson.put(ApiConstant.SUCCESS, messageJSON.getString(ApiConstant.SUCCESS_KEY));
			// inactive default super admin account on success
			licenseActivationManager.inactiveOldSuperAdmin(userId);
			Logger.info(this, "saveLicenseActivationStatus", HttpStatus.OK_200, responseJson);
			return Response.ok(responseJson.toString()).build();

		} catch (ConnectException | ProcessingException e) {
			licenseActivationManager
					.updateLicenseActivation(PasswordUtil.encrypt(ApiConstant.INACTIVE, ApiConstant.SECRET));
			deleteSuperAdminOnFailedRequest(userId);
			e.printStackTrace();
			responseJson.put(ApiConstant.ERROR, ApiConstant.CONNECT_EXCEPTION_MEASSGE);
			Logger.error(this, "saveLicenseActivationStatus", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.serverError().entity(responseJson.toString()).build();
		} catch (Exception e) {
			licenseActivationManager
					.updateLicenseActivation(PasswordUtil.encrypt(ApiConstant.INACTIVE, ApiConstant.SECRET));
			deleteSuperAdminOnFailedRequest(userId);
			e.printStackTrace();
			if (e.getMessage() != null && !e.getMessage().isEmpty()) {
				responseJson.put(ApiConstant.ERROR, e.getMessage());
				sLog.d("saveLicenseActivationStatus, " + responseJson.toString());
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d("saveLicenseActivationStatus, " + ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}
			Logger.error(this, "saveLicenseActivationStatus", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.serverError().entity(responseJson.toString()).build();
		} finally {
			DbConnectionManager.closeDBConnection(null, null, connection);
		}
	}

	private void deleteSuperAdminOnFailedRequest(final int userId) throws Exception {
		if (userId > 0) {
			licenseActivationManager.deleteSuperAdmin(userId);
		}
	}

	/**
	 * <b>API service to fetch status of license activation.</b>
	 * 
	 * <p>Also checks for software piracy.</p>
	 * 
	 * @return licenseStatus
	 */
	@GET
	@Path(value = ApiConstant.REST_LICENSE_STATUS_END_POINT)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLicenseActivationStatus() {

		final JSONObject responseJson = new JSONObject();
		try {
			org.lattice.spectrum_backend_final.dao.util.Logger.setLicenceLogToFile(true);
			final LicenseStatus licenseStatus = licenseActivationManager.fetchLicenseStatus();
			if (licenseStatus == null) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				Logger.error(this, "getLicenseActivationStatus", HttpStatus.NOT_FOUND_404, responseJson);
				return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
			}
			// check for software piracy
			if (DbConnectionManager.getInstance().isPiracyControlMode) {
				checkLicensePiracy(licenseStatus);
			}
			Logger.info(this, "getLicenseActivationStatus", HttpStatus.OK_200, licenseStatus);
			return Response.ok(licenseStatus).build();
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getLicenseActivationStatus", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.serverError().entity(responseJson.toString()).build();
		}
	}

	private void checkLicensePiracy(LicenseStatus licenseStatus) throws Exception {
		if (licenseStatus.getStatus().equals(ApiConstant.ACTIVE)) {
			if (RegistryManager.isSoftwarePirated()) {
				licenseStatus.setStatus(ApiConstant.INACTIVE);
			}
		}
	}

}
