package org.lattice.spectrum_backend_final.services.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.AuxPumpSetting;
import org.lattice.spectrum_backend_final.beans.KFKonduitSetting;
import org.lattice.spectrum_backend_final.beans.MainPumpSetting;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.ManualModeConfig;
import org.lattice.spectrum_backend_final.dao.manager.AlarmManager;
import org.lattice.spectrum_backend_final.dao.manager.BufferManager;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.LogManager;
import org.lattice.spectrum_backend_final.dao.manager.ManualModeManager;
import org.lattice.spectrum_backend_final.dao.manager.ManualTrialManager;
import org.lattice.spectrum_backend_final.dao.manager.NotesManager;
import org.lattice.spectrum_backend_final.dao.manager.RunManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;
import org.lattice.spectrum_backend_final.dao.manager.device.AbvManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.KonduitUtil;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.ManualNotesUtil;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ValveConnectorConfiguration;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.PressureManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.AlarmProp;

/**
 * The Class contains APIs for manual mode operations.
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_MANUAL_MODE_ENTRY_POINT)
public class ManualModeHandler {

	private static ManualModeHandler manualModeHandler = null;
	private final UserAuthorization authorization = new UserAuthorization();
	private static boolean isOldConfig;
	private static Set<Byte> updatedOldConfig = new HashSet<Byte>();
	private static HttpHeaders httpHeadersGlobal = null;

	/**
	 * Gets the single instance of ManualModeHandler.
	 *
	 * @return single instance of ManualModeHandler
	 */
	public static ManualModeHandler getInstance() {

		synchronized (ManualModeHandler.class) {
			if (manualModeHandler == null) {
				manualModeHandler = new ManualModeHandler();
			}
		}
		return manualModeHandler;
	}

	/**
	 * Checks if is old config.
	 *
	 * @return true, if is old config
	 */
	public static boolean isOldConfig() {
		return isOldConfig;
	}

	/**
	 * Gets the http headers.
	 *
	 * @return the http headers
	 */
	public static HttpHeaders getHttpHeaders() {
		return httpHeadersGlobal;
	}

	/** 0 = stop, 1 = running 2, = pause. */
	public static int isTrialStarted = 0;

	private static int[] auxTypeIds = new int[2];
	public static List<AuxPumpSetting> auxPumpSettingsGlobal = new ArrayList<AuxPumpSetting>();
	public static ABVSetting[] abvSettingsGlobal = new ABVSetting[2];
	public static MainPumpSetting mainPumpGlobal = new MainPumpSetting();
	public static KFKonduitSetting kfKonduitGlobal = new KFKonduitSetting();
	public static JSONObject manualFilterDetails;

	/**
	 * ********** KF Conduit APIs *************.
	 *
	 * API_DOC_1: Service to save kf konduit setting
	 * 
	 * @param kfKonduitSetting the request kf konduit setting
	 * @param isOldConfig
	 * @param headers
	 * @return the response
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_KF_CONDUIT_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveKFKonduitDetails(final KFKonduitSetting kfKonduitSetting,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				if (!ManualModeManager.getInstance().isTrialRunSettingExists(kfKonduitSetting.getTrialRunSettingId())) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_TRIAL_RUN_SETTING_NOT_FOUND);
					Logger.error(this, "saveKFKonduitDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				final int kfConduitId = ManualModeManager.getInstance().saveKFConduitSetting(kfKonduitSetting);
				if (kfConduitId == 0) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_KF_CONDUIT_SETTING_NOT_SAVED);
					Logger.error(this, "saveKFKonduitDetails", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.KFCONDUIT_SETTINGS_SAVED_MESSAGE);
				kfKonduitGlobal = kfKonduitSetting;
				if (isOldConfig) {
					updatedOldConfig.add(ManualModeConfig.KONDUIT);
				}
				Logger.info(this, "saveKFKonduitDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();

			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "saveKFKonduitDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "saveKFKonduitDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_2: Service to update kf konduit setting.
	 *
	 * @param trialRunSettingId
	 * @param kfKonduitSetting
	 * @param isOldConfig
	 * @param headers
	 * @return the response
	 */
	@PUT
	@Path(value = ApiConstant.REST_MANUAL_KF_KONDUIT_EDIT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKFConduitDetails(@PathParam(value = "trialRunSettingId") final int trialRunSettingId,
			final KFKonduitSetting kfKonduitSetting,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {
			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				if (!ManualModeManager.getInstance().isKFConduitDetailsExists(trialRunSettingId)) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_KFCONDUIT_NOT_FOUND);
					Logger.error(this, "updateKFConduitDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				kfKonduitSetting.setTrialRunSettingId(trialRunSettingId);
				final int result = ManualModeManager.getInstance().updateKFConduitDetails(kfKonduitSetting);
				if (result < 1) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_KF_CONDUIT_DETAILS_NOT_UPDATED);
					Logger.error(this, "updateKFConduitDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.KFCONDUIT_SETTINGS_UPDATE_MESSAGE);
				sLog.d(ApiConstant.KFCONDUIT_SETTINGS_UPDATE_MESSAGE);
				changeLogKonduit(kfKonduitSetting);
				kfKonduitGlobal = kfKonduitSetting;
				if (isOldConfig) {
					updatedOldConfig.add(ManualModeConfig.KONDUIT);
				}
				Logger.info(this, "updateKFConduitDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "updateKFConduitDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "updateKFConduitDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Save change log of konduit.
	 *
	 * @param kfKonduitSetting
	 */
	private void changeLogKonduit(final KFKonduitSetting kfKonduitSetting) {
		if (kfKonduitSetting.getCh1KFactor() != null && kfKonduitGlobal.getCh1KFactor() != null
				&& !kfKonduitSetting.getCh1KFactor().equals(kfKonduitGlobal.getCh1KFactor())) {
			ManualNotesUtil.saveManualNote("Konduit", "Ch 1", kfKonduitGlobal.getCh1KFactor(),
					kfKonduitSetting.getCh1KFactor(), kfKonduitSetting.getTrialRunSettingId());
		} else if (kfKonduitSetting.getCh2KFactor() != null && kfKonduitGlobal.getCh2KFactor() != null
				&& !kfKonduitGlobal.getCh2KFactor().equals(kfKonduitSetting.getCh2KFactor())) {
			ManualNotesUtil.saveManualNote("Konduit", "Ch 2", kfKonduitGlobal.getCh2KFactor(),
					kfKonduitSetting.getCh2KFactor(), kfKonduitSetting.getTrialRunSettingId());
		}
	}

	/**
	 * ********** AUX pump APIs *************.
	 *
	 * API_DOC_3: Service to save aux pump setting
	 * 
	 * @param auxPumpSetting the request aux pump setting
	 * @param isOldConfig
	 * @param headers
	 * @return the response
	 *
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_AUX_PUMP_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAuxPumpDetails(final AuxPumpSetting auxPumpSetting,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				if (!ManualModeManager.getInstance().isTrialRunSettingExists(auxPumpSetting.getTrialRunSettingId())) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_TRIAL_RUN_SETTING_NOT_FOUND);
					Logger.error(this, "saveAuxPumpDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				final int auxPumpId = ManualModeManager.getInstance().saveAuxPumpSetting(auxPumpSetting);
				if (auxPumpId == 0) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_AUX_PUMP_SETTING_NOT_SAVED);
					Logger.error(this, "saveAuxPumpDetails", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.AUX_PUMP_SETTING_SAVED_MESSAGE);
				if (isTrialStarted == 1) {
					changeLogAux(auxPumpSetting);
					setAUXPumpSettingToComLib(auxPumpSetting);
					if (auxPumpSetting.getType() == 0) {
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.AUX_PUMP_1);
						}
					} else {
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.AUX_PUMP_2);
						}
					}
					updateAuxInTableHeader(auxPumpSetting);
				}
				Logger.info(this, "saveAuxPumpDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "saveAuxPumpDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_4: Service to update aux pump setting.
	 *
	 * @param trialRunSettingId
	 * @param auxPumpSetting
	 * @param isOldConfig
	 * @param headers
	 * @return the response
	 */
	@PUT
	@Path(value = ApiConstant.REST_MANUAL_AUX_EDIT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAuxPumpDetails(@PathParam(value = "trialRunSettingId") final int trialRunSettingId,
			final AuxPumpSetting auxPumpSetting,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
				if (!ManualModeManager.getInstance().isAuxPumpDetailsExists(trialRunSettingId,
						auxPumpSetting.getType())) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_AUX_PUMP_NOT_FOUND);
					Logger.error(this, "updateAuxPumpDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				auxPumpSetting.setTrialRunSettingId(trialRunSettingId);
				final int result = ManualModeManager.getInstance().updateAuxPumpDetails(auxPumpSetting);
				if (result < 1) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_AUX_PUMP_DETAILS_NOT_UPDATED);
					Logger.error(this, "updateAuxPumpDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.AUX_PUMP_SETTINGS_UPDATE_MESSAGE);
				if (isTrialStarted == 1) {
					changeLogAux(auxPumpSetting);
					setAUXPumpSettingToComLib(auxPumpSetting);
					if (auxPumpSetting.getType() == 0) {
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.AUX_PUMP_1);
						}
					} else {
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.AUX_PUMP_2);
						}
					}
					updateAuxInTableHeader(auxPumpSetting);
				}
				Logger.info(this, "updateAuxPumpDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "updateAuxPumpDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "updateAuxPumpDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Update aux in table header.
	 *
	 * @param auxPumpSetting
	 */
	public void updateAuxInTableHeader(final AuxPumpSetting auxPumpSetting) {
		JSONObject tableJsonObject = null;
		sLog.d(this, "updateAuxInTableHeader");
		sLog.d(this, auxPumpSetting);
		if (TrialManager.getInstance().getTableHeader() != null
				&& !TrialManager.getInstance().getTableHeader().isEmpty()) {
			tableJsonObject = TrialManager.getInstance().getTableHeader();
			if (!tableJsonObject.getJSONArray("columns").isEmpty()) {
				if (auxPumpSetting.getType() == 0) {
					for (int i = 0; i < tableJsonObject.getJSONArray("columns").length(); i++) {
						if (tableJsonObject.getJSONArray("columns").get(i).equals("Aux pump 1")) {
							sLog.d(this, "Aux Pump 1");
							tableJsonObject.getJSONArray("column_units").getJSONObject(i).put("propertyName",
									getAuxUnit(auxPumpSetting.getAuxPumpType()));
						}
					}
				} else {
					for (int i = 0; i < tableJsonObject.getJSONArray("columns").length(); i++) {
						if (tableJsonObject.getJSONArray("columns").get(i).equals("Aux pump 2")) {
							sLog.d(this, "Aux Pump 2");
							tableJsonObject.getJSONArray("column_units").getJSONObject(i).put("propertyName",
									getAuxUnit(auxPumpSetting.getAuxPumpType()));
						}
					}
				}
			}
			TrialManager.getInstance().setTableHeader(tableJsonObject);
		}
	}

	/**
	 * Gets the aux unit.
	 *
	 * @param auxPumpType
	 * @return the aux unit
	 */
	private String getAuxUnit(final String auxPumpType) {
		sLog.d(this, auxPumpType);
		return ApiConstant.IP.equalsIgnoreCase(auxPumpType) ? "(L/min)" : "(mL/min)";
	}

	/**
	 * Stop aux pump api.
	 *
	 * @param auxId
	 * @param isOldConfig
	 * @param auxName
	 * @param headers
	 * @return the response
	 */
	@GET
	@Path(ApiConstant.STOP_AUX_PUMP_PATH)
	public Response stopAuxPumpApi(@PathParam(ApiConstant.PARAM_AUX_ID) final int auxId,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@PathParam("auxName") final String auxName, @Context final HttpHeaders headers) {
		final JSONObject responseJson = new JSONObject();
		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				final int auxTypeId = BasicUtility.getAuxPumpTypeIdByAuxName(auxName);
				ComLib.get().getAuxPump().setAuxPumpRPM(auxId, auxTypeId, 0);
				if (isOldConfig) {
					if (auxId == 0) {
						updatedOldConfig.remove(ManualModeConfig.AUX_PUMP_1);
					} else {
						updatedOldConfig.remove(ManualModeConfig.AUX_PUMP_2);
					}
				}
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "stopAuxPumpApi", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}

		} catch (final Exception ex) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			ex.printStackTrace();
			Logger.error(this, "stopAuxPumpApi", HttpStatus.INTERNAL_SERVER_ERROR_500, ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseJson.toString()).build();
		}

		responseJson.put(ApiConstant.SUCCESS, ApiConstant.AUX_PUMP_STOPPED_SUCCESS_MESSAGE);
		Logger.info(this, "stopAuxPumpApi", HttpStatus.OK_200, responseJson);
		return Response.status(Response.Status.OK).entity(responseJson.toString()).build();

	}

	/**
	 * Sets the AUX pump setting to com lib.
	 *
	 * @param auxPumpSetting
	 */
	private void setAUXPumpSettingToComLib(final AuxPumpSetting auxPumpSetting) {

		sLog.d(this, auxPumpSetting);

		if (!auxPumpSettingsGlobal.isEmpty()) {
			auxPumpSettingsGlobal.removeIf(auxPump -> auxPump.getType() == auxPumpSetting.getType());
		}
		auxPumpSettingsGlobal.add(auxPumpSetting);
		final int auxTypeId = BasicUtility.getAuxPumpTypeIdByAuxName(auxPumpSetting.getAuxPumpType());
		BasicUtility.systemPrint("auxTypeId", auxTypeId);
		auxTypeIds[auxPumpSetting.getType()] = auxTypeId;
		if (auxPumpSetting.getSpeed() != null && !auxPumpSetting.getSpeed().isEmpty()) {
			ComLib.get().getAuxPump().setAuxPumpRPM(auxPumpSetting.getType(), auxTypeId,
					Integer.valueOf(auxPumpSetting.getSpeed()).intValue());
		} else {
			final double flowConversion = BasicUtility.getTubeFlowConversion(auxPumpSetting.getAuxPumpType(),
					auxPumpSetting.getTubingSize());
			final double flowRate = Double.valueOf(auxPumpSetting.getFlowRate());
			ComLib.get().getAuxPump().setAuxPump_mlpm(auxPumpSetting.getType(), auxTypeId,
					BasicUtility.getInstance().getConvertedFlowRate(flowRate, auxPumpSetting.getAuxPumpType(), true),
					flowConversion,
					BasicUtility.getPumpMinMaxRpm(auxPumpSetting.getAuxPumpType()).getInt(ApiConstant.MAX_SPEED));
		}
	}

	/**
	 * Save change log of aux.
	 *
	 * @param auxPumpSetting
	 */
	private void changeLogAux(final AuxPumpSetting auxPumpSetting) {
		final AuxPumpSetting oldAuxSetting = auxPumpSettingsGlobal.stream()
				.filter(aux -> aux.getType() == auxPumpSetting.getType()).findFirst().orElse(null);
		final String auxPump = auxPumpSetting.getType() == 0 ? "Aux Pump 1" : "Aux Pump 2";
		if (oldAuxSetting != null) {
			if (oldAuxSetting.getSpeed() != null && auxPumpSetting.getSpeed() != null
					&& !oldAuxSetting.getSpeed().equals(auxPumpSetting.getSpeed())) {
				ManualNotesUtil.saveManualNote(auxPump, ApiConstant.SPEED, oldAuxSetting.getSpeed(),
						auxPumpSetting.getSpeed(), auxPumpSetting.getTrialRunSettingId());
			}
			if (oldAuxSetting.getFlowRate() != null && auxPumpSetting.getFlowRate() != null
					&& !oldAuxSetting.getFlowRate().equals(auxPumpSetting.getFlowRate())) {
				ManualNotesUtil.saveManualNote(auxPump, "flow rate", oldAuxSetting.getFlowRate(),
						auxPumpSetting.getFlowRate(), auxPumpSetting.getTrialRunSettingId());
			}
			if (oldAuxSetting.getFlowRate() == null && auxPumpSetting.getFlowRate() != null) {
				ManualNotesUtil.saveManualNote(auxPump, "flow rate", "0", auxPumpSetting.getFlowRate(),
						auxPumpSetting.getTrialRunSettingId());
			}
			if (oldAuxSetting.getTubingSize() != null && auxPumpSetting.getTubingSize() != null
					&& !oldAuxSetting.getTubingSize().equals(auxPumpSetting.getTubingSize())) {
				ManualNotesUtil.saveManualNote(auxPump, "tubing size", oldAuxSetting.getTubingSize(),
						auxPumpSetting.getTubingSize(), auxPumpSetting.getTrialRunSettingId());
			}
		} else {
			if (auxPumpSetting.getFlowRate() != null) {
				ManualNotesUtil.saveManualNote(auxPump, "flow rate", "0", auxPumpSetting.getFlowRate(),
						auxPumpSetting.getTrialRunSettingId());
			}
		}
	}

	/**
	 * ********** ABV APIs *************.
	 *
	 * API_DOC_5: Service to save abv setting
	 *
	 * @param abvSetting the request abv setting
	 * @param isOldConfig
	 * @param headers
	 * @return the response
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_ABV_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAbvDetails(final ABVSetting abvSetting,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {
			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
				if (!ManualModeManager.getInstance().isTrialRunSettingExists(abvSetting.getTrialRunSettingId())) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_TRIAL_RUN_SETTING_NOT_FOUND);
					Logger.error(this, "saveAbvDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				final int abvId = ManualModeManager.getInstance().saveABVSetting(abvSetting);
				sLog.d("abvId: ", abvId);
				if (abvId == 0) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_ABV_SETTING_NOT_SAVED);
					Logger.error(this, "saveAbvDetails", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.ABV_SETTING_SAVED_MESSAGE);
				sLog.d(ApiConstant.ABV_SETTING_SAVED_MESSAGE);
				sLog.d("isTrialStarted: ", isTrialStarted);
				if (isTrialStarted == 1) {
					final ABVSetting[] abvSettings = new ABVSetting[2];
					abvSettings[0] = new ABVSetting();
					abvSettings[1] = new ABVSetting();

					sLog.d("abvSetting.getType(): ", abvSetting.getType());
					if (abvSetting.getType() == 0) {
						abvSettings[0] = abvSetting;
						abvSettingsGlobal[0] = abvSetting;
						sLog.d("abvSettingsGlobal[0]: ", abvSettingsGlobal[0]);
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.ABV_1);
						}
					}
					if (abvSetting.getType() == 1) {
						abvSettings[1] = abvSetting;
						abvSettingsGlobal[1] = abvSetting;
						sLog.d("abvSettingsGlobal[1]: ", abvSettingsGlobal[1]);
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.ABV_2);
						}
					}
					sLog.d("abvSettings: ", abvSettings);
					new AbvManager().abvPressureControlAtRun(abvSettings);
				}
				Logger.info(this, "saveAbvDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "saveAbvDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			Logger.error(this, "saveAbvDetails", HttpStatus.UNAUTHORIZED_401, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_6: Service to update abv setting.
	 *
	 * @param trialRunSettingId
	 * @param abvSetting
	 * @param isOldConfig
	 * @param headers
	 * @return the response
	 */
	@PUT
	@Path(value = ApiConstant.REST_MANUAL_ABV_EDIT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAbvDetails(@PathParam(value = "trialRunSettingId") final int trialRunSettingId,
			final ABVSetting abvSetting, @PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {
			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
				if (!ManualModeManager.getInstance().isABVDetailsExists(trialRunSettingId, abvSetting.getType())) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_ABV_NOT_FOUND);
					Logger.error(this, "updateAbvDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				abvSetting.setTrialRunSettingId(trialRunSettingId);
				final int result = ManualModeManager.getInstance().updateABVDetails(abvSetting);
				sLog.d("result: ", result);
				if (result < 1) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_ABV_DETAILS_NOT_UPDATED);
					Logger.error(this, "updateAbvDetails", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.ABV_SETTINGS_UPDATE_MESSAGE);
				sLog.d("isTrialStarted: ", isTrialStarted);
				if (isTrialStarted == 1) {
					final ABVSetting[] abvSettings = new ABVSetting[2];
					abvSettings[0] = new ABVSetting();
					abvSettings[1] = new ABVSetting();

					if (abvSetting.getType() == 0) {
						abvSettings[0] = abvSetting;
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.ABV_1);
						}
						changeLogABV(abvSetting);
					}

					if (abvSetting.getType() == 1) {
						abvSettings[1] = abvSetting;
						if (isOldConfig) {
							updatedOldConfig.add(ManualModeConfig.ABV_2);
						}
						changeLogABV(abvSetting);
					}
					sLog.d("abvSettings: ", abvSettings);
					new AbvManager().abvPressureControlAtRun(abvSettings);

				}
				Logger.info(this, "updateAbvDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "updateAbvDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "updateAbvDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Save change log ABV.
	 *
	 * @param abvSetting
	 */
	private void changeLogABV(final ABVSetting abvSetting) {
		if (abvSettingsGlobal != null && abvSettingsGlobal.length > 0) {
			if (abvSetting != null) {
				final String abvName = abvSetting.getType() == 0 ? "ABV 1" : "ABV 2";
				if (abvSettingsGlobal[abvSetting.getType()] != null) {
					if (abvSetting.getOperatingPressure() != null
							&& abvSettingsGlobal[abvSetting.getType()].getOperatingPressure() != null
							&& !abvSetting.getOperatingPressure()
									.equals(abvSettingsGlobal[abvSetting.getType()].getOperatingPressure())) {
						ManualNotesUtil.saveManualNote(abvName, ApiConstant.OPERATING_PRESSURE,
								abvSettingsGlobal[abvSetting.getType()].getOperatingPressure(),
								abvSetting.getOperatingPressure(), abvSetting.getTrialRunSettingId());
					}
					if (abvSetting.getTubingSize() != null
							&& abvSettingsGlobal[abvSetting.getType()].getTubingSize() != null && !abvSetting
									.getTubingSize().equals(abvSettingsGlobal[abvSetting.getType()].getTubingSize())) {
						ManualNotesUtil.saveManualNote(abvName, ApiConstant.NOTES_TYPE_TUBING_SIZE,
								abvSettingsGlobal[abvSetting.getType()].getTubingSize(), abvSetting.getTubingSize(),
								abvSetting.getTrialRunSettingId());
					}
					if (abvSetting.getPosition() != null
							&& abvSettingsGlobal[abvSetting.getType()].getPosition() != null && !abvSetting
									.getPosition().equals(abvSettingsGlobal[abvSetting.getType()].getPosition())) {
						ManualNotesUtil.saveManualNote(abvName, ApiConstant.POSITION,
								abvSettingsGlobal[abvSetting.getType()].getPosition(), abvSetting.getPosition(),
								abvSetting.getTrialRunSettingId());
					}
				}
				abvSettingsGlobal[abvSetting.getType()] = abvSetting;
			}
		}
	}

	/**
	 * ********** Main Pump APIs *************.
	 *
	 * API_DOC_7: Service to save main pump setting
	 * 
	 * @param mainPumpSetting the request main pump setting
	 * @param headers
	 * @return the response
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_MAIN_PUMP_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveMainPumpDetails(final MainPumpSetting mainPumpSetting, @Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {
			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
				if (!ManualModeManager.getInstance().isTrialRunSettingExists(mainPumpSetting.getTrialRunSettingId())) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_TRIAL_RUN_SETTING_NOT_FOUND);
					Logger.error(this, "saveMainPumpDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				final int mainPumpId = ManualModeManager.getInstance().saveMainPumpSetting(mainPumpSetting);
				if (mainPumpId == 0) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_MAIN_PUMP_SETTING_NOT_SAVED);
					Logger.error(this, "saveMainPumpDetails", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.MAIN_PUMP_SETTING_SAVED_MESSAGE);
				sLog.d(ApiConstant.MAIN_PUMP_SETTING_SAVED_MESSAGE);
				if (isTrialStarted == 1) {
					setMainPumpSettingToComLib(mainPumpSetting);
					if (isOldConfig) {
						updatedOldConfig.add(ManualModeConfig.MAIN_PUMP);
					}
				}
				Logger.info(this, "saveMainPumpDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "saveMainPumpDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "saveMainPumpDetails", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_8: Service to update main pump setting.
	 *
	 * @param trialRunSettingId
	 * @param mainPumpSetting
	 * @param headers
	 * @return the response
	 */
	@PUT
	@Path(value = ApiConstant.REST_MANUAL_MAIN_PUMP_EDIT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMainPumpDetails(@PathParam(value = "trialRunSettingId") final int trialRunSettingId,
			final MainPumpSetting mainPumpSetting, @Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {
			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
				if (!ManualModeManager.getInstance().isMainPumpDetailsExists(trialRunSettingId)) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_MAIN_PUMP_NOT_FOUND);
					Logger.error(this, "updateMainPumpDetails", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				mainPumpSetting.setTrialRunSettingId(trialRunSettingId);
				final int result = ManualModeManager.getInstance().updateMainPumpDetails(mainPumpSetting);
				if (result < 1) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_MAIN_PUMP_DETAILS_NOT_UPDATED);
					Logger.error(this, "updateMainPumpDetails", HttpStatus.NOT_IMPLEMENTED_501, responseJson);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.SUCCESS, ApiConstant.MAIN_PUMP_SETTINGS_UPDATE_MESSAGE);

				if (isTrialStarted == 1) {
					changeLogMainPump(mainPumpSetting);
					setMainPumpSettingToComLib(mainPumpSetting);
					if (isOldConfig) {
						updatedOldConfig.add(ManualModeConfig.MAIN_PUMP);
					}
				}
				Logger.info(this, "updateMainPumpDetails", HttpStatus.OK_200);
				return Response.ok(responseJson.toString()).build();
			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "updateMainPumpDetails", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "updateMainPumpDetails", HttpStatus.NOT_IMPLEMENTED_501, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Sets the main pump setting to com lib.
	 *
	 * @param mainPumpSetting
	 * @throws Exception the exception
	 */
	private void setMainPumpSettingToComLib(final MainPumpSetting mainPumpSetting) throws Exception {
		if (mainPumpSetting.getSpeed() != null && !mainPumpSetting.getSpeed().isEmpty()) {
			if (mainPumpSetting.getPumpName().equalsIgnoreCase(ApiConstant.KR2I)) {
				if (mainPumpSetting.getDirection().equalsIgnoreCase(ApiConstant.CLOCKWISE)) {
					ComLib.get().getMainPump().setSpeed(Integer.valueOf(mainPumpSetting.getSpeed()).intValue() * 36);
				} else {
					ComLib.get().getMainPump().setSpeed((-Integer.valueOf(mainPumpSetting.getSpeed()).intValue()) * 36);
				}
			} else if (mainPumpSetting.getPumpName().equalsIgnoreCase(ApiConstant.KMPI)) {
				if (mainPumpSetting.getDirection().equalsIgnoreCase(ApiConstant.CLOCKWISE)) {
					ComLib.get().getMainPump().setSpeed((-Integer.valueOf(mainPumpSetting.getSpeed()).intValue()) * 29);
				} else {
					ComLib.get().getMainPump().setSpeed(Integer.valueOf(mainPumpSetting.getSpeed()).intValue() * 29);
				}
			} else if (ApiConstant.KROSFLOFS15.equalsIgnoreCase(mainPumpSetting.getPumpName())) {
				ComLib.get().getMainPump().setSpeed(Integer.valueOf(mainPumpSetting.getSpeed()).intValue() * 6);
			} else if (ApiConstant.KROSFLOFS500.equalsIgnoreCase(mainPumpSetting.getPumpName())) {
				ComLib.get().getMainPump().setSpeed(Integer.valueOf(mainPumpSetting.getSpeed()).intValue() * 6);
			}
		} else {
			final double speedFactor = BasicUtility.speedFactor(mainPumpSetting.getPumpName(),
					mainPumpSetting.getDirection(), 1);
			final double tubeFactor = BasicUtility.getTubeFlowConversion(mainPumpSetting.getPumpName(),
					mainPumpSetting.getTubingSize());
			final double mainPumpFlowRate = Double.valueOf(mainPumpSetting.getFlowRate());
			final String pumpName = mainPumpSetting.getPumpName();

			BasicUtility.systemPrint("speedFactor1---" + speedFactor + "-----tubeFactor1---" + tubeFactor,
					"-----flowrate1----" + mainPumpFlowRate);
			ComLib.get().getMainPump().setFlow_mlpm(
					BasicUtility.getInstance().getConvertedFlowRate(mainPumpFlowRate, pumpName, true), tubeFactor,
					speedFactor / BasicUtility.getInstance().getCalibrationFactor(mainPumpSetting.getTubingSize(),
							pumpName));

		}
		mainPumpGlobal = mainPumpSetting;
	}

	/**
	 * Save change log main pump.
	 *
	 * @param mainPumpSetting
	 */
	private void changeLogMainPump(final MainPumpSetting mainPumpSetting) {
		if (mainPumpGlobal != null) {
			if (mainPumpSetting.getDirection() != null && mainPumpGlobal.getDirection() != null
					&& !mainPumpSetting.getDirection().equals(mainPumpGlobal.getDirection())
					&& !ApiConstant.KROSFLOFS15.equals(mainPumpSetting.getPumpName())
					&& !ApiConstant.KROSFLOFS500.equals(mainPumpSetting.getPumpName())) {
				ManualNotesUtil.saveManualNote(ApiConstant.MAIN_PUMP, ApiConstant.DIRECTION,
						mainPumpGlobal.getDirection(), mainPumpSetting.getDirection(),
						mainPumpSetting.getTrialRunSettingId());
			}
			if (mainPumpSetting.getFlowRate() != null && mainPumpGlobal.getFlowRate() != null
					&& !mainPumpSetting.getFlowRate().equals(mainPumpGlobal.getFlowRate())) {
				ManualNotesUtil.saveManualNote(ApiConstant.MAIN_PUMP, "flow rate", mainPumpGlobal.getFlowRate(),
						mainPumpSetting.getFlowRate(), mainPumpSetting.getTrialRunSettingId());
			}
			if (mainPumpSetting.getFlowRate() != null && mainPumpGlobal.getFlowRate() == null) {
				ManualNotesUtil.saveManualNote(ApiConstant.MAIN_PUMP, "flow rate", "0", mainPumpSetting.getFlowRate(),
						mainPumpSetting.getTrialRunSettingId());
			}
			if (mainPumpSetting.getSpeed() != null && mainPumpGlobal.getSpeed() != null
					&& !mainPumpSetting.getSpeed().equals(mainPumpGlobal.getSpeed())) {
				ManualNotesUtil.saveManualNote(ApiConstant.MAIN_PUMP, ApiConstant.SPEED, mainPumpGlobal.getSpeed(),
						mainPumpSetting.getSpeed(), mainPumpSetting.getTrialRunSettingId());
			}
			if (mainPumpSetting.getTubingSize() != null && mainPumpGlobal.getTubingSize() != null
					&& !mainPumpSetting.getTubingSize().equals(mainPumpGlobal.getTubingSize())) {
				ManualNotesUtil.saveManualNote(ApiConstant.MAIN_PUMP, "tubing size", mainPumpGlobal.getTubingSize(),
						mainPumpSetting.getTubingSize(), mainPumpSetting.getTrialRunSettingId());
			}
		} else {
			if (mainPumpSetting.getFlowRate() != null) {
				ManualNotesUtil.saveManualNote(ApiConstant.MAIN_PUMP, "flow rate", "0", mainPumpSetting.getFlowRate(),
						mainPumpSetting.getTrialRunSettingId());
			}
		}
	}

	/**
	 * ********** Manual trial run *************.
	 *
	 * API_DOC_9: Service to run manual trial
	 * 
	 * @param trialRunSettingId
	 * @param isOldConfig
	 * @param digitalSignatureValues
	 * @param headers
	 * @return the response
	 * @throws Exception the exception
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_TRIAL_RUN_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response runManualMode(
			@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) final int trialRunSettingId,
			@PathParam(value = ApiConstant.PARAM_IS_OLD_CONFIG) final boolean isOldConfig,
			final String digitalSignatureValues, @Context final HttpHeaders headers) throws Exception {

		sLog.d(this, "runManualMode");
		if (isTrialStarted == 0) {
			org.lattice.spectrum_backend_final.dao.util.Logger.setManualLogToFile(true);
			// DeviceManager.getInstance().reset();
			DeviceManager.getInstance().resetFlowRateIndicator();
			// reset alarm indicator
			AlarmManager.getInstance().stopDeviceAlarmIndicator();
		}
		abvSettingsGlobal = new ABVSetting[2];
		auxPumpSettingsGlobal = new ArrayList<>();
		kfKonduitGlobal = new KFKonduitSetting();
		mainPumpGlobal = new MainPumpSetting();
		ManualModeHandler.isOldConfig = isOldConfig;
		httpHeadersGlobal = headers;
		
		final JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);

		sLog.d(this, digitalSignatureValues + "------values");
		sLog.d(this, digitalSignatureJson);

		final JSONObject responseJson = new JSONObject();
		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {

					DeviceManager.getInstance().setModeType(1);

					// set system setting once for trial.
					Converter.setSystemSetting();
					// DeviceManager.getInstance().autoConnect();

					manualFilterDetails = ManualTrialManager.getInstance().getManualFilterDetails(trialRunSettingId);
					sLog.d(this, ManualModeHandler.manualFilterDetails);
					
					if (!isOldConfig) {
						startManualModeForNewConfig(trialRunSettingId, headers);
					} else {
						startManualModeForOldConfig(trialRunSettingId, headers);
						if (isTrialStarted == 0) {
							setMainPumpToDefault(trialRunSettingId, headers);
						}
					}

					setAlarmListener(trialRunSettingId);

					// BasicUtility.systemPrint("1isRunning",
					// TrialManager.getInstance().isRunning());

					// starting listeners
					if (!TrialManager.getInstance().isRunning()) {
						TrialManager.getInstance().setRunning(true);
						DeviceManager.getInstance().startRxListener();
					}
					DeviceManager.pumpKeyListener.startRxListening();
					DeviceManager.abvErrorTextListener.startRxListening();
					if (!DeviceManager.getInstance().isConnectionListRunning()) {
						DeviceManager.connectionListener.startRxListening();
						DeviceManager.getInstance().setConnectionListRunning(true);

					}
					TrialManager.getInstance().setPaused(false);
					// BasicUtility.systemPrint("11isRunning",
					// TrialManager.getInstance().isRunning());
					if (isTrialStarted == 0) {
						isTrialStarted = 1;

						// saving trial log
						if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

							RunManager.getInstance().updateTrialStartTime(
									TrialManager.getInstance().getTrialRunSettingId(),
									BasicUtility.getInstance().getCurrentTimestamp());

							LogManager.getInstance().insertTrialLog(
									BasicUtility.getInstance().getUserId(digitalSignatureJson),
									TrialManager.getInstance().getTrialRunSettingId(), ApiConstant.TRIAL_START_ACTION,
									ApiConstant.LOG_TYPE_TRIAL);
							responseJson.put(ApiConstant.SUCCESS, ApiConstant.MANUAL_RUN_STARTED);

							// Adding digital signature notes
							if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
								new NotesManager().saveNotes(
										new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
							}

							RunManager.getInstance().updateTrialRunStatus(
									TrialManager.getInstance().getTrialRunSettingId(), null,
									ApiConstant.TRIAL_RUNNING_STATUS);

						} else {
							throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
						}
					} else if (isTrialStarted == 2) {
						isTrialStarted = 1;
						// resume trial log
						if (TrialManager.getInstance().getTrialRunSettingId() != 0) {
							LogManager.getInstance().insertTrialLog(
									BasicUtility.getInstance().getUserId(digitalSignatureJson),
									TrialManager.getInstance().getTrialRunSettingId(), ApiConstant.TRIAL_RESUME_ACTION,
									ApiConstant.LOG_TYPE_TRIAL);
							responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_RESUME_MESSAGE);

							// Adding digital signature notes
							if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
								new NotesManager().saveNotes(
										new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
							}

						} else {
							throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
						}
					}

					Logger.info(this, "runManualMode", HttpStatus.OK_200);
					return Response.status(HttpStatus.OK_200).entity(responseJson.toString()).build();

				} else {
					throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
				}

			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "runManualMode", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			Logger.error(this, "runManualMode", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Start manual mode for new config.
	 *
	 * @param trialRunSettingId
	 * @param headers
	 * @throws Exception the exception
	 */
	private void startManualModeForNewConfig(final int trialRunSettingId, final HttpHeaders headers) throws Exception {
		updatedOldConfig.clear();

		// set Kf Konduit
		kfKonduitGlobal = ManualTrialManager.getInstance().getKFKonduitSetting(trialRunSettingId);

		// aux pump run
		fetchAndSetAuxPumpSettings(trialRunSettingId);

		// abv run
		fetchAndSetABVSettings(trialRunSettingId);

		// main pump run
		fetchAndSetMainPumpSettings(trialRunSettingId, headers);
	}

	/**
	 * Start manual mode for old config.
	 *
	 * @param trialRunSettingId
	 * @param headers
	 */
	private void startManualModeForOldConfig(final int trialRunSettingId, final HttpHeaders headers) {
		if (!updatedOldConfig.isEmpty()) {
			updatedOldConfig.forEach(config -> {
				switch (config) {

				case ManualModeConfig.KONDUIT:
					kfKonduitGlobal = ManualTrialManager.getInstance().getKFKonduitSetting(trialRunSettingId);
					break;

				case ManualModeConfig.AUX_PUMP_1:
					fetchAndSetAuxPumpSettings(trialRunSettingId, ManualModeConfig.AUX_PUMP_1);
					break;

				case ManualModeConfig.AUX_PUMP_2:
					fetchAndSetAuxPumpSettings(trialRunSettingId, ManualModeConfig.AUX_PUMP_2);
					break;

				case ManualModeConfig.ABV_1:
					try {
						fetchAndSetABVSettings(trialRunSettingId, ManualModeConfig.ABV_1);
					} catch (final Exception e1) {
						e1.printStackTrace();
					}
					break;

				case ManualModeConfig.ABV_2:
					try {
						fetchAndSetABVSettings(trialRunSettingId, ManualModeConfig.ABV_2);
					} catch (final Exception e1) {
						e1.printStackTrace();
					}
					break;

				case ManualModeConfig.MAIN_PUMP:
					try {
						fetchAndSetMainPumpSettings(trialRunSettingId, headers);
					} catch (final Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			});
		}
	}

	/**
	 * Fetch and set main pump settings.
	 *
	 * @param trialRunSettingId
	 * @param headers
	 * @throws Exception the exception
	 */
	private void fetchAndSetMainPumpSettings(final int trialRunSettingId, final HttpHeaders headers) throws Exception {
		MainPumpSetting mainPumpSetting = ManualTrialManager.getInstance().getMainPumpSetting(trialRunSettingId);

		sLog.d(this, mainPumpSetting);
		if (mainPumpSetting != null) {
			BasicUtility.systemPrint("inside", "main if");
			setMainPumpSettingToComLib(mainPumpSetting);
		} else {
			setMainPumpToDefault(trialRunSettingId, headers);
		}
	}

	private void setMainPumpToDefault(int trialRunSettingId, HttpHeaders headers) throws Exception {
		BasicUtility.systemPrint("inside", "main else");
		MainPumpSetting mainPumpSetting = new MainPumpSetting();
		mainPumpSetting.setSpeed("0");
		mainPumpSetting.setTrialRunSettingId(trialRunSettingId);
		mainPumpSetting.setPumpName(BasicUtility.getInstance().getConnectedPumpName());
		mainPumpSetting.setDirection(ApiConstant.CLOCKWISE);
		saveMainPumpDetails(mainPumpSetting, headers);
		setMainPumpSettingToComLib(mainPumpSetting);
	}

	/**
	 * Fetch and set ABV settings.
	 *
	 * @param trialRunSettingId
	 * @throws Exception the exception
	 */
	private void fetchAndSetABVSettings(final int trialRunSettingId) throws Exception {
		final List<ABVSetting> abvSettingList = ManualTrialManager.getInstance().getABVSettings(trialRunSettingId);
		final ABVSetting[] abvSettings = new ABVSetting[2];
		abvSettings[0] = new ABVSetting();
		abvSettings[1] = new ABVSetting();
		if (!abvSettingList.isEmpty()) {
			for (final ABVSetting abvSetting : abvSettingList) {
				if (abvSetting.getType() == 0) {
					abvSettings[0] = abvSetting;
				} else {
					abvSettings[1] = abvSetting;
				}
			}
			new AbvManager().abvPressureControlAtRun(abvSettings);
			abvSettingsGlobal = abvSettings;
		}
	}

	/**
	 * Fetch and set ABV settings.
	 *
	 * @param trialRunSettingId
	 * @param type the abv type
	 * @throws Exception the exception
	 */
	// for old config update
	private void fetchAndSetABVSettings(final int trialRunSettingId, final byte type) throws Exception {
		ABVSetting abvSetting = null;
		if (ManualModeConfig.ABV_1 == type) {
			abvSetting = ManualTrialManager.getInstance().getABVSettingByType(trialRunSettingId, 0);
		} else {
			abvSetting = ManualTrialManager.getInstance().getABVSettingByType(trialRunSettingId, 1);
		}

		if (abvSetting != null) {
			final ABVSetting[] abvSettings = new ABVSetting[2];
			abvSettings[0] = new ABVSetting();
			abvSettings[1] = new ABVSetting();
			if (abvSetting.getType() == 0) {
				abvSettings[0] = abvSetting;
			} else {
				abvSettings[1] = abvSetting;
			}
			new AbvManager().abvPressureControlAtRun(abvSettings);
			abvSettingsGlobal = abvSettings;
		}

	}

	/**
	 * Fetch and set aux pump settings.
	 *
	 * @param trialRunSettingId
	 */
	private void fetchAndSetAuxPumpSettings(final int trialRunSettingId) {
		final List<AuxPumpSetting> auxPumpSettingList = ManualTrialManager.getInstance()
				.getAuxPumpSetting(trialRunSettingId);
		if (!auxPumpSettingList.isEmpty()) {
			for (final AuxPumpSetting auxPumpSetting : auxPumpSettingList) {
				setAUXPumpSettingToComLib(auxPumpSetting);
			}
		}
	}

	/**
	 * Fetch and set aux pump settings.
	 *
	 * @param trialRunSettingId
	 * @param type the abv type
	 */
	// for old config update
	private void fetchAndSetAuxPumpSettings(final int trialRunSettingId, final byte type) {
		AuxPumpSetting auxPumpSetting = null;
		if (ManualModeConfig.AUX_PUMP_1 == type) {
			auxPumpSetting = ManualTrialManager.getInstance().getAUXSettingByType(trialRunSettingId, 0);
		} else {
			auxPumpSetting = ManualTrialManager.getInstance().getAUXSettingByType(trialRunSettingId, 1);
		}

		if (auxPumpSetting != null) {
			setAUXPumpSettingToComLib(auxPumpSetting);
		}
	}

	/**
	 * Sets the alarm listener.
	 *
	 * @param trialRunSettingId
	 * @throws Exception the exception
	 */
	public void setAlarmListener(final int trialRunSettingId) throws Exception {

		AlarmProp alarmProp = null;
		final KonduitUtil konduitUtil = new KonduitUtil();

		// fetching alarms if exists
		final JSONObject alarmJson = AlarmManager.getInstance().fetchAlarmSettingForManual(trialRunSettingId);

		final ModeListener dummyModeListener = new ModeListener() {
			@Override
			public void callback(final ModeEvent id, final Object... obj) {
				sLog.d("DummyCallback, Event Reported [ID:" + id + ", Data:" + Arrays.toString(obj));
				try {
					TrialManager.getInstance().setDummyListenerLiveData(id, alarmJson, obj);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		};
		// set alarm property
		if (alarmJson != null) {
			alarmProp = AlarmManager.getInstance().setAlarm(alarmJson);
			com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.AlarmManager.get().start(alarmProp,
					konduitUtil.getKonduitChannelProp(1), konduitUtil.getKonduitChannelProp(1), dummyModeListener);
		}
	}

	/**
	 * Stop manual mode.
	 *
	 * @throws Exception the exception
	 */
	public void stopManualMode() throws Exception {

		try {

			// stop main pump
			ComLib.get().getMainPump().setSpeed(0);

			// stop aux pumps
			if (auxTypeIds.length > 0) {
				for (int index = 0; index < auxTypeIds.length; index++) {
					ComLib.get().getAuxPump().setAuxPumpRPM(index, auxTypeIds[index], 0);
				}
			}

			// stop valves
			ComLib.get().getValveInfo().setValvePosition(0, 0, 0);
			if (ComLib.get().getConfigureAuxiliary() != null
					&& ComLib.get().getConfigureAuxiliary().getConnectorConfig() != null
					&& ComLib.get().getConfigureAuxiliary()
							.getConnectorConfig() == ValveConnectorConfiguration.USE_FOR_VALVE) {
				PressureManager.get().stop(true);
			}

			// stop listeners
			if (TrialManager.getInstance().isRunning()) {
				TrialManager.getInstance().setRunning(false);
				DeviceManager.getInstance().stopRxListener();
			}
			isOldConfig = false;
			TrialManager.getInstance().setTableHeader(new JSONObject());
			// moved to stop only
			// isTrialStarted = false;

		} catch (final Exception ex) {
			Logger.error(this, "stopManualMode", HttpStatus.INTERNAL_SERVER_ERROR_500, ex);
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	/**
	 * API_DOC_10: Service to stop manual trial.
	 *
	 * @param pause (trial pause if 1)
	 * @param headers
	 * @return the response
	 * @throws Exception the exception
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_TRIAL_STOP_PATH)
	public Response stopManualModeApi(@PathParam(value = "pause") final int pause, @Context final HttpHeaders headers)
			throws Exception {

		final JSONObject responseJson = new JSONObject();
		sLog.d("stopManualMode called, is_pause: " + pause);

		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				stopManualMode();
				DeviceManager.getInstance().resetFlowRateIndicator();

				if (pause != 1) {

					// set alarm indicator
					DeviceManager.pumpKeyListener.stopRxListening();
					DeviceManager.abvErrorTextListener.stopRxListening();
					AlarmManager.getInstance().stopDeviceAlarmIndicator();
					// stop alarm manager.
					com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.AlarmManager.get().stop();

					abvSettingsGlobal = new ABVSetting[2];
					auxPumpSettingsGlobal = new ArrayList<>();
					kfKonduitGlobal = new KFKonduitSetting();
					mainPumpGlobal = new MainPumpSetting();
					updatedOldConfig.clear();
					httpHeadersGlobal = null;
					manualFilterDetails = null;
					
					DeviceManager.connectionListener.stopRxListening();
					DeviceManager.getInstance().setConnectionListRunning(false);

					TrialManager.getInstance().setAlarmActive(false);

					// saving trial log
					if (TrialManager.getInstance().getTrialRunSettingId() != 0) {
						DeviceManager.getInstance().setModeType(2);
						isTrialStarted = 0;
						BufferManager.getInstance().setPausedToBuffer(0);
						TrialManager.getInstance().setPaused(false);
						LogManager.getInstance().insertTrialLog(
								DbConnectionManager.getInstance().getTokenManager().getUserId(),
								TrialManager.getInstance().getTrialRunSettingId(), ApiConstant.TRIAL_STOP_ACTION,
								ApiConstant.LOG_TYPE_TRIAL);
						responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_STOPPED_MESSAGE);

						RunManager.getInstance().updateTrialRunStatus(TrialManager.getInstance().getTrialRunSettingId(),
								BasicUtility.getInstance().getCurrentTimestamp(), ApiConstant.TRIAL_TERMINATE_STATUS);

						// TrialManager.getInstance().setTrialRunSettingId(0);

					} else {
						throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
					}

				} else if (pause == 1) {
					// saving trial log
					if (TrialManager.getInstance().getTrialRunSettingId() != 0) {
						TrialManager.getInstance().setPaused(true);
						isTrialStarted = 2;
						BufferManager.getInstance().setPausedToBuffer(1);
						sLog.d(this, BufferManager.getInstance().getTrialBuffer());
						LogManager.getInstance().insertTrialLog(
								DbConnectionManager.getInstance().getTokenManager().getUserId(),
								TrialManager.getInstance().getTrialRunSettingId(), ApiConstant.TRIAL_PAUSE_ACTION,
								ApiConstant.LOG_TYPE_TRIAL);
						responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_PAUSED_MESSAGE);

					} else {
						throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
					}

				}
				Logger.info(this, "stopManualModeApi", HttpStatus.OK_200);
				return Response.status(HttpStatus.OK_200).entity(responseJson.toString()).build();

			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "stopManualModeApi", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
		} catch (final InvalidCredentialException e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "stopManualModeApi", HttpStatus.UNAUTHORIZED_401, e);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		} catch (final Exception e) {
			Logger.error(this, "stopManualModeApi", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject().put(ApiConstant.ERROR,
					ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION)).build();
		}

	}

	/**
	 * API_DOC_11: Service to tare scales.
	 *
	 * @param scale the type of scale
	 * @param headers
	 * @return the response
	 */
	@POST
	@Path(value = ApiConstant.REST_MANUAL_SCALE_TARE_PATH)
	public Response tareScale(@PathParam(value = "scale") final int scale, @Context final HttpHeaders headers) {

		// scale: 0 = permeate, 1 = feed

		final JSONObject responseJson = new JSONObject();
		sLog.d("scale taring started for scale type: " + scale);
		try {

			if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

				final JSONObject jsonObject = ManualModeManager.getInstance().tareScaleManager(scale);

				if (jsonObject.has(ApiConstant.SUCCESS)) {
					Logger.info(this, "tareScale", HttpStatus.OK_200, jsonObject);
					return Response.ok(jsonObject.toString()).build();
				} else {
					Logger.error(this, "tareScale", HttpStatus.NOT_IMPLEMENTED_501, jsonObject);
					return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(jsonObject.toString()).build();
				}

			} else {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "tareScale", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}

		} catch (final Exception e) {
			Logger.error(this, "tareScale", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject().put(ApiConstant.ERROR,
					ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION)).build();
		}
	}

	/**
	 * Old configuration related APIs.
	 *
	 * API_DOC_12: Get latest/last manual data
	 * 
	 * @param trialRunSettingId
	 * @param headers
	 * @return the latest or last manual data
	 */
	@GET
	@Path(value = ApiConstant.REST_LATEST_MANUAL_DATA_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestOrLastManualData(
			@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
			@Context final HttpHeaders headers) {
		final JSONObject responseJson = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "getLatestOrLastManualData", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			if (trialRunSettingId == -1) {
				// fetch trial details
				final JSONArray trialJsonArray = DatabaseManager.getInstance().fetchTrialDetails(trialRunSettingId);
				if (trialJsonArray.isEmpty()) {
					responseJson.put(ApiConstant.ERROR, ApiConstant.ERROR_LAST_TRIAL_RUN_SETTING_NOT_FOUND);
					Logger.error(this, "getLatestOrLastManualData", HttpStatus.NOT_FOUND_404, responseJson);
					return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
				}
				responseJson.put(ApiConstant.DETAILS, trialJsonArray.getJSONObject(0));
				trialRunSettingId = Integer
						.valueOf(trialJsonArray.getJSONObject(0).getString(ApiConstant.TRIAL_RUN_SETTING_ID));
				// fetch alarms
				final JSONArray alarmsJsonArray = DatabaseManager.getInstance().fetchAlarmsDetails(trialRunSettingId);
				responseJson.put(ApiConstant.ALARMS_KEY, alarmsJsonArray);
			} else {
				responseJson.put(ApiConstant.DETAILS, new JSONObject());
				responseJson.put(ApiConstant.ALARMS_KEY, new JSONArray());
			}
			final JSONObject kfKonduitSetting = ManualTrialManager.getInstance().getKFKonduitJSON(trialRunSettingId);
			responseJson.put(ApiConstant.KF_KONDUIT_KEY, kfKonduitSetting);
			final List<ABVSetting> abvSettings = ManualTrialManager.getInstance().getABVSettings(trialRunSettingId);
			responseJson.put(ApiConstant.ABV, abvSettings);
			final List<AuxPumpSetting> auxPumpSettings = ManualTrialManager.getInstance()
					.getAuxPumpSetting(trialRunSettingId);
			responseJson.put(ApiConstant.AUX_PUMP, auxPumpSettings);
			final JSONObject mainPumpSetting = ManualTrialManager.getInstance()
					.getMainPumpSettingJSON(trialRunSettingId);
			responseJson.put(ApiConstant.MAIN_PUMP_KEY, mainPumpSetting);
			final JSONObject filterDetails = ManualTrialManager.getInstance().getManualFilterDetails(trialRunSettingId);
			responseJson.put(ApiConstant.FILTER, filterDetails);
			Logger.info(this, "getLatestOrLastManualData", HttpStatus.OK_200);
			return Response.ok(new JSONObject().put(ApiConstant.DATA, new JSONArray().put(responseJson)).toString())
					.build();
		} catch (final Exception e) {
			if (e.getMessage() == null || e.getMessage().isEmpty()) {
				Logger.error(this, "getLatestOrLastManualData", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject()
						.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION).toString())
						.build();
			} else {
				Logger.error(this, "getLatestOrLastManualData", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
						.entity(new JSONObject().put(ApiConstant.ERROR, e.getMessage()).toString()).build();
			}
		}
	}

	/**
	 * Save old configuration.
	 *
	 * @param headers
	 * @param manualJSON the request JSON
	 * @return the response
	 * @throws JSONException the JSON exception
	 * @throws SQLException the SQL exception
	 * @throws TrialRunException the trial run exception
	 */
	@POST
	@Path(value = ApiConstant.REST_SAVE_MANUAL_DATA_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveOldConfiguration(@Context final HttpHeaders headers, final String manualJSON)
			throws JSONException, SQLException, TrialRunException {
		int trialRunSettingId = 0;
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				Logger.error(this, "saveOldConfiguration", HttpStatus.UNAUTHORIZED_401);
				return Response.status(Response.Status.UNAUTHORIZED).entity(
						new JSONObject().put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE).toString())
						.build();
			}
			DeviceManager.getInstance().reset();

			final JSONObject manualJsonObject = new JSONObject(manualJSON).getJSONArray(ApiConstant.DATA)
					.getJSONObject(0);
			trialRunSettingId = ManualModeManager.getInstance().saveNewTrialConfig(manualJsonObject);
			if (trialRunSettingId == 0) {
				Logger.error(this, "saveOldConfiguration", HttpStatus.INTERNAL_SERVER_ERROR_500);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject()
						.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION)).build();
			}
			ManualModeManager.getInstance().saveNewAlarmConfig(manualJsonObject, trialRunSettingId);
			ManualModeManager.getInstance().saveNewAuxPumpConfig(manualJsonObject, trialRunSettingId);
			ManualModeManager.getInstance().saveNewABVConfig(manualJsonObject, trialRunSettingId);
			ManualModeManager.getInstance().saveNewKonduitConfig(manualJsonObject, trialRunSettingId);
			ManualModeManager.getInstance().saveNewMainPumpConfig(manualJsonObject, trialRunSettingId);
			saveOrUpdateFilterDetails(manualJsonObject, trialRunSettingId);
			TrialManager.getInstance().setTrialRunSettingId(trialRunSettingId);

			addValve2Note(manualJsonObject.getJSONObject(ApiConstant.DETAILS), trialRunSettingId);

		} catch (final Exception e) {
			if (trialRunSettingId > 0) {
				// delete failed trial details
				ManualModeManager.getInstance().deleteFailedTrialDetails(trialRunSettingId);
				Logger.debug(this, ApiConstant.FAILED_TRIAL_DETAILS_DELETED_MESSAGE);
			}
			if (e.getMessage() == null || e.getMessage().isEmpty()) {
				Logger.error(this, "saveOldConfiguration", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject()
						.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION).toString())
						.build();
			} else {
				Logger.error(this, "saveOldConfiguration", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
				return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
						.entity(new JSONObject().put(ApiConstant.ERROR, e.getMessage()).toString()).build();
			}
		}
		Logger.info(this, "saveOldConfiguration", HttpStatus.OK_200);
		return Response
				.ok(new JSONObject().put(ApiConstant.TRIAL_RUN_SETTING_ID, trialRunSettingId)
						.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_DETAIL_SAVED_SUCCESSFULLY_MESSAGE).toString())
				.build();
	}

	/**
	 * Adds the valve 2 note.
	 *
	 * @param manualJsonObject the request manual json object
	 * @param trialRunSettingId
	 */
	private void addValve2Note(JSONObject manualJsonObject, int trialRunSettingId) {
		if (manualJsonObject.has(ApiConstant.VALVE_2_CONNECTOR_COLUMN)
				&& manualJsonObject.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN) != null
				&& !manualJsonObject.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN).isEmpty()) {
			ManualNotesUtil.saveManualNote(ApiConstant.VALVE_CONNECTOR_KEY, "2", ApiConstant.BLANK_QUOTE,
					manualJsonObject.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN), trialRunSettingId);
		}
	}

	/**
	 * On trial reconnect, start manual mode.
	 *
	 * @throws Exception the exception
	 */
	public void onReconnect() throws Exception {
		if (isOldConfig) {
			startManualModeForOldConfig(TrialManager.getInstance().getTrialRunSettingId(), httpHeadersGlobal);
		} else {
			startManualModeForNewConfig(TrialManager.getInstance().getTrialRunSettingId(), httpHeadersGlobal);
		}
	}

	/**
	 * Edits the manual trial detail.
	 *
	 * @param requestDataJSON the request data JSON
	 * @param trialRunSettingId
	 * @param headers
	 * @return the response
	 */
	@PUT
	@Path(value = ApiConstant.REST_EDIT_MANUAL_DATA_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editManualTrialDetail(final String requestDataJSON,
			@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) final int trialRunSettingId,
			@Context final HttpHeaders headers) {

		final JSONObject responseJson = new JSONObject();
		try {
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				Logger.error(this, "editManualTrialDetail", HttpStatus.UNAUTHORIZED_401);
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity(responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE).toString())
						.build();
			}

			final JSONObject manualJSON = new JSONObject(requestDataJSON);

			// update filter details
			saveOrUpdateFilterDetails(manualJSON, trialRunSettingId);

			// update valve 2 connector
			if (!manualJSON.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN)
					.equals(manualJSON.getString(ApiConstant.OLD_VALVE_2_CONNECTOR))) {
				final boolean result = ManualModeManager.getInstance().updateManualDetails(manualJSON,
						trialRunSettingId);
				if (!result) {
					Logger.error(this, "editManualTrialDetail", HttpStatus.INTERNAL_SERVER_ERROR_500);
					return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson
							.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION))
							.build();
				}

				ManualModeManager.getInstance().setValveToComLib(
						manualJSON.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN), trialRunSettingId);
				ManualNotesUtil.saveManualNote(ApiConstant.VALVE_CONNECTOR_KEY, "2", ApiConstant.BLANK_QUOTE,
						manualJSON.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN), trialRunSettingId);
			}
		} catch (final Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "editManualTrialDetail", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseJson.toString()).build();
		}

		responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_DETAIL_UPDATED_SUCCESSFULLY_MESSAGE);
		Logger.info(this, "downloadPDF", HttpStatus.OK_200, responseJson);
		return Response.status(Response.Status.OK).entity(responseJson.toString()).build();
	}

	/**
	 * Save or update filter details.
	 *
	 * @param manualJSON the request manual JSON
	 * @param trialRunSettingId
	 */
	private void saveOrUpdateFilterDetails(JSONObject manualJSON, int trialRunSettingId) {
		sLog.d(this, "saveOrUpdateFilterDetails");
		if (manualJSON.has(ApiConstant.FILTER) && !manualJSON.getJSONObject(ApiConstant.FILTER).isEmpty()) {
			final boolean result = ManualModeManager.getInstance()
					.saveOrUpdateManualFilter(manualJSON.getJSONObject(ApiConstant.FILTER), trialRunSettingId);
			if (!result) {
				throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}
			ManualNotesUtil.saveManualFilterNote(manualJSON, trialRunSettingId, null);
		}
	}

}