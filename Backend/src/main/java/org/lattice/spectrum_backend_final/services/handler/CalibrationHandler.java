package org.lattice.spectrum_backend_final.services.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.TubingPumpCal;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.LogManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.Managers.run.RunModeManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.FlowProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.RunSubModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.FlowControlMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

/**
 * The Class used to handle pressure and tubing calibration.
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_CALIBRATE_PATH)
public class CalibrationHandler {

	private static CalibrationHandler calibrationHandler = null;
	private UserAuthorization authorization = new UserAuthorization();

	/**
	 * Gets the single instance of CalibrationHandler.
	 *
	 * @return single instance of CalibrationHandler
	 */
	public static CalibrationHandler getInstance() {

		synchronized (CalibrationHandler.class) {
			if (calibrationHandler == null) {
				calibrationHandler = new CalibrationHandler();
			}
		}
		return calibrationHandler;
	}


	/**
	 * API_DOC_1: Service to tare pressures.
	 *
	 * @param headers
	 * @param digitalSignatureValues the digital signature values
	 * @return the response
	 * @throws Exception the exception
	 */
	@POST
	@Path(value = ApiConstant.PRESSURE_TARE_ENDPOINT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response tarePressure(@Context HttpHeaders headers, String digitalSignatureValues) throws Exception {

		Logger.setPressureCalibrationLogToFile(true);

		JSONObject responseJson = new JSONObject();
		String notes = ApiConstant.BLANK_QUOTE;
		try {

			final JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);

			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "tarePressure", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			if (!digitalSignatureJson.isEmpty()) {
				if (!DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {
					throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
				}
			}
			if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
				notes = digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES);
			}
			ComLib.get().getPressureTare().start();
			responseJson.put(ApiConstant.SUCCESS, ApiConstant.PRESSURE_TARED);
			Logger.info(this, "tarePressure", HttpStatus.OK_200, responseJson);
			if (!digitalSignatureJson.isEmpty()) {
				LogManager.getInstance().insertSystemLog(BasicUtility.getInstance().getUserId(digitalSignatureJson), 0,
						"Pressure Tared", notes);
			}
			// todo system log
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			e.printStackTrace();
			Logger.error(this, "tarePressure", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 *  API_DOC_2: Service to calibrate pressures.
	 *
	 * @param channelId
	 * @param value
	 * @param digitalSignatureValues
	 * @param headers
	 * @return the response
	 */
	@POST
	@Path(value = ApiConstant.PRESSURE_CALIBRATE_ENDPOINT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calibratePressure(@QueryParam(value = ApiConstant.CHANNEL_ID) int channelId,
			@QueryParam(value = ApiConstant.VALUE) Double value, String digitalSignatureValues,
			@Context HttpHeaders headers) {

		JSONObject responseJson = new JSONObject();

		try {
			JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "calibratePressure", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			if (!DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}

			ComLib.get().getPressureCalibrate().calibrate(channelId,
					(int) (value / ComLib.get().getPressureInfo().getScaleFactor()));

			RxListener valveListener = new RxListener(ComLib.get().getPressureCalibrate()) {
				@Override
				public void OnReceive() {
					JSONObject resultJson = new JSONObject();
					String notes = ApiConstant.BLANK_QUOTE;
					if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
						notes = digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES);
					}
					byte result = ComLib.get().getPressureCalibrate().getResult();
					switch (result) {
					case 0:
						resultJson.put(ApiConstant.RESULT,
								getAction(channelId) + ApiConstant.PRESSURE_CALIBRATED_SUCCESS);

						try {
							LogManager.getInstance().insertSystemLog(
									BasicUtility.getInstance().getUserId(digitalSignatureJson), 0,
									getAction(channelId) + ApiConstant.PRESSURE_CALIBRATED_SUCCESS, notes);
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					case 1:
						resultJson.put(ApiConstant.RESULT, getAction(channelId) + ApiConstant.SENSOR_NOT_CONNECTED);
						break;
					case 2:
						resultJson.put(ApiConstant.RESULT, ApiConstant.PRESSURE_CALIBRATION_OUT_OF_RANGE);
						break;
					case 3:
						resultJson.put(ApiConstant.RESULT, ApiConstant.INVALID_CHANNEL_SELECTED);
						break;
					default:
						resultJson.put(ApiConstant.RESULT, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
						break;
					}
					Logger.debug(this, "calibratePressure", responseJson);
					DeviceManager.getInstance().broadcastPressureEvent(resultJson);
					this.stopRxListening();
				}
			};
			valveListener.startRxListening();
			Logger.info(this, "calibratePressure", HttpStatus.OK_200);
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			e.printStackTrace();
			Logger.error(this, "calibratePressure", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Gets the action.
	 *
	 * @param channelId
	 * @return the action
	 */
	private String getAction(int channelId) {
		String action = null;
		switch (channelId) {
		case 0:
			action = ApiConstant.FEED_CALIBRATE;
			break;
		case 1:
			action = ApiConstant.RETENTATE_CALIBRATE;
			break;
		case 2:
			action = ApiConstant.PERMEATE_CALIBRATE;
			break;

		default:
			break;
		}
		return action;
	}

	/**
	 * API_DOC_3: Service to start main pump.
	 *
	 * @param tubingPumpCal
	 * @param headers
	 * @return the response
	 * @throws Exception the exception
	 */
	@POST
	@Path(value = ApiConstant.PUMP_START_END_POINT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response startMainPump(TubingPumpCal tubingPumpCal, @Context HttpHeaders headers) throws Exception {

		Logger.setTubingCalibrationLogToFile(true);
		JSONObject responseJson = new JSONObject();
		String notes = ApiConstant.BLANK_QUOTE;
		try {
			DeviceManager.getInstance().resetFlowRateIndicator();
			DeviceManager.getInstance().reset();

			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "startMainPump", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}

			if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(new JSONObject(tubingPumpCal), false)) {
				if (tubingPumpCal.getIsPermConnected() == 1) {
					double flowRate = BasicUtility.getInstance().getConvertedFlowRate(tubingPumpCal.getFlowRate(),
							tubingPumpCal.getPumpName(), true);
					FlowProp flowProp = new FlowProp(FlowControlMode.FEED, flowRate,
							BasicUtility.getTubeFlowConversion(tubingPumpCal.getPumpName(),
									tubingPumpCal.getTubingSize()),
							BasicUtility.speedFactor(tubingPumpCal.getPumpName(), tubingPumpCal.getDirection(), 1), 3,
							ApiConstant.FLOW_FACTOR);

					ModeProp tubingCalModeProp = new ModeProp();
					tubingCalModeProp.setSuperID(OperationMode.TubeCalib_MODE);
					tubingCalModeProp.setFlowProp(flowProp);
					tubingCalModeProp.setTotalizerMode(TotalizerMode.NONE);
					tubingCalModeProp.setRunModes(new RunSubModeProp[] { new RunSubModeProp()
							.setMode(OperationMode.TubeCalib_MODE).setTubeCalTimeout(tubingPumpCal.getDuration())
							.setEndPointTarget(EndPointTarget.ConcentrationFactor).setEndPointValue(10) });

					sLog.d(this, tubingCalModeProp);

					ModeListener dummyModeListener = new ModeListener() {
						@Override
						public void callback(ModeEvent id, Object... obj) {
							sLog.d("DummyCallback", "Event Reported [ID:" + id + ", Data:" + Arrays.toString(obj));

							if (id == ModeEvent.TIMER_START) {
								JSONObject sendToLive = new JSONObject();
								sendToLive.put(ApiConstant.START_KEY, true);
								sendToLive.put(ApiConstant.MESSAGE, BasicUtility.enumToStringConversion(id));
								DeviceManager.getInstance().broadcastExpectedValue(sendToLive);
							} else if (id == ModeEvent.TC_ENDPOINT_REACHED) {
								// expected value send to event
								if (obj != null) {
									double expectedFlowRate = BasicUtility.getInstance().getConvertedFlowRate(
											Double.parseDouble(obj[1].toString()), tubingPumpCal.getPumpName(), false);
									JSONObject sendToLive = new JSONObject();
									sendToLive.put(ApiConstant.EXPECTED_VALUE_KEY, expectedFlowRate);
									sendToLive.put(ApiConstant.ACTUAL_VALUE_KEY, obj[2].toString());
									sendToLive.put(ApiConstant.STOP_KEY, true);
									sendToLive.put(ApiConstant.MESSAGE, BasicUtility.enumToStringConversion(id));
									DeviceManager.getInstance().broadcastExpectedValue(sendToLive);
								}
							} else if (id == ModeEvent.SUB_MODE_STARTED || id == ModeEvent.SUB_MODE_FINISHED) {
								// No need to send.
							} else {
								JSONObject messageJson = new JSONObject().put(ApiConstant.MESSAGE,
										BasicUtility.enumToStringConversion(id));
								sLog.d(this, messageJson);
								DeviceManager.getInstance().broadcastExpectedValue(messageJson);
//								if (id == ModeEvent.PERMEATE_SCALE_TARE_TIMEOUT) {
//									RunModeManager.get().stop();
//								}
							}
						}
					};
					RunModeManager.get().setProp(tubingCalModeProp).setSuperCallback(dummyModeListener).setup();
				} else {
					double speedFactor = BasicUtility.speedFactor(tubingPumpCal.getPumpName(),
							tubingPumpCal.getDirection(), 1);
					double tubeFactor = BasicUtility.getTubeFlowConversion(tubingPumpCal.getPumpName(),
							tubingPumpCal.getTubingSize());
					double mainPumpFlowRate = tubingPumpCal.getFlowRate();
					String pumpName = tubingPumpCal.getPumpName();

					BasicUtility.systemPrint("speedFactor1---" + speedFactor + "-----tubeFactor1---" + tubeFactor,
							"-----flowrate1----" + mainPumpFlowRate);
					ComLib.get().getMainPump().setFlow_mlpm(
							BasicUtility.getInstance().getConvertedFlowRate(mainPumpFlowRate, pumpName, true),
							tubeFactor, speedFactor);
				}
				LogManager.getInstance().insertSystemLog(
						BasicUtility.getInstance().getUserId(new JSONObject(tubingPumpCal)), 0, "Main pump started",
						notes);

			} else {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}

			Logger.info(this, "startMainPump", HttpStatus.OK_200);
			return Response.ok().build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			e.printStackTrace();
			Logger.error(this, "startMainPump", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 *  API_DOC_4: Service to stop main pump.
	 *
	 * @param headers
	 * @param digitalSignatureValues
	 * @param permScale
	 * @return the response
	 */
	@POST
	@Path(value = ApiConstant.PUMP_STOP_END_POINT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response stopMainPump(@Context HttpHeaders headers, String digitalSignatureValues,
			@PathParam(value = "permScale") int permScale) {

		JSONObject responseJson = new JSONObject();
		String notes = ApiConstant.BLANK_QUOTE;
		try {
			JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "stopMainPump", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}

			if (!digitalSignatureJson.getBoolean("required")
					|| DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {

				if (permScale == 1) {
					RunModeManager.get().stop();
				} else {
					ComLib.get().getMainPump().setSpeed(0);
				}

				if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
					notes = digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES);
					LogManager.getInstance().insertSystemLog(BasicUtility.getInstance().getUserId(digitalSignatureJson),
							0, "Main pump stopped", notes);
				}

			} else {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}

			Logger.info(this, "stopMainPump", HttpStatus.OK_200);
			return Response.ok().build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			e.printStackTrace();
			Logger.error(this, "stopMainPump", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 *  API_DOC_5: Service to calibrate tubing.
	 *
	 * @param tubingLookUpId
	 * @param calFactor
	 * @param digitalSignatureValues
	 * @param headers
	 * @return the response
	 */
	@PUT
	@Path(value = ApiConstant.TUBE_CALIBRATE_END_POINT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response calibrateTubing(@QueryParam(value = ApiConstant.TUBING_LOOK_UP_ID_PARAM) Integer tubingLookUpId,
			@QueryParam(value = ApiConstant.CALIBRATION_FACTOR_PARAM) String calFactor, String digitalSignatureValues,
			@Context HttpHeaders headers) {
		JSONObject responseJson = new JSONObject();
		String notes = ApiConstant.BLANK_QUOTE;
		try {
			JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);
			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "calibrateTubing", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}
			if (!DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}
			if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
				notes = digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES);
			}

			if (tubingLookUpId == null || tubingLookUpId == 0) {
				// if pump is fs15 or fs500
				tubingLookUpId = getTubingLookUpIdByPumpName();
			}
			if (tubingLookUpId == 0) {
				throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}
			final int result = updateCalibrationFactor(tubingLookUpId, calFactor);
			if (result <= 0) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				sLog.d("calibrateTubing, STATUS: " + HttpStatus.NOT_IMPLEMENTED_501);
				return Response.status(HttpStatus.NOT_IMPLEMENTED_501).entity(responseJson.toString()).build();
			}
			responseJson.put(ApiConstant.SUCCESS, ApiConstant.TUBING_CALIBRATED_MESSAGE);
			DeviceManager.connectionListener.stopRxListening();
			DeviceManager.getInstance().setConnectionListRunning(false);
			LogManager.getInstance().insertSystemLog(BasicUtility.getInstance().getUserId(digitalSignatureJson), 0,
					ApiConstant.TUBE_CALIBRATE, notes);
			Logger.info(this, "calibrateTubing", HttpStatus.OK_200);
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			e.printStackTrace();
			Logger.error(this, "calibrateTubing", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Gets the tubing look up id by pump name.
	 *
	 * @return the tubing look up id by pump name
	 */
	private final int getTubingLookUpIdByPumpName() {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchTubingLookUpIDStmt = null;
		ResultSet resultSet = null;
		int tubingLookUpId = 0;
		try {
			final String pumpName = BasicUtility.getInstance().getConnectedPumpName();
			if (pumpName == null) {
				return 0;
			}
			fetchTubingLookUpIDStmt = con.prepareStatement(QueryConstant.FETCH_TUBING_LOOKUP_ID);
			fetchTubingLookUpIDStmt.setString(1, pumpName);
			sLog.d(this, QueryConstant.FETCH_TUBING_LOOKUP_ID);
			resultSet = fetchTubingLookUpIDStmt.executeQuery();
			if (resultSet.next()) {
				tubingLookUpId = resultSet.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchTubingLookUpIDStmt, con);
		}
		return tubingLookUpId;
	}

	/**
	 * Update calibration factor.
	 *
	 * @param tubingLookUpId
	 * @param calFactor
	 * @return the affectedRows
	 */
	private int updateCalibrationFactor(int tubingLookUpId, String calFactor) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateCalFactorStmt = null;
		int affectedRows = 0;
		try {
			con.setAutoCommit(false);
			updateCalFactorStmt = con.prepareStatement(QueryConstant.UPDATE_TUBING_CALIBRATION_FACTOR);
			updateCalFactorStmt.setString(1, calFactor);
			updateCalFactorStmt.setInt(2, tubingLookUpId);
			sLog.d(QueryConstant.UPDATE_TUBING_CALIBRATION_FACTOR);
			affectedRows = updateCalFactorStmt.executeUpdate();
			if (affectedRows < 1) {
				return 0;
			}
			con.commit();
		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateCalFactorStmt, con);
		}
		return affectedRows;
	}

	/**
	 *  API_DOC: Service to fetch pressure calibrate logs.
	 *
	 * @return the pressure calibrate logs
	 */
	@GET
	@Path(value = ApiConstant.REST_PRESSURE_CALIBRATE_LOG_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPressureCalibrateLogs() {

		JSONObject responseJson = new JSONObject();
		try {
			JSONObject resJsonObject = fetchPressureLogs();
			Logger.info(this, "getPressureCalibrateLogs", HttpStatus.OK_200);
			return Response.ok(resJsonObject.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getPressureCalibrateLogs", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Fetch pressure logs.
	 *
	 * @return the JSON object containing pressure logs.
	 */
	private JSONObject fetchPressureLogs() {
		Connection con = null;
		Connection con2 = null;
		Connection con3 = null;
		PreparedStatement fetchFeedLogStmt = null;
		ResultSet feedResult = null;
		PreparedStatement fetchRetLogStmt = null;
		ResultSet fetchRetResult = null;
		PreparedStatement fetchPermLogStmt = null;
		ResultSet fetchPermResult = null;
		JSONObject jsonObject = new JSONObject();
		try {
			con = DbConnectionManager.getInstance().getConnection();
			fetchFeedLogStmt = con.prepareStatement(QueryConstant.FETCH_FEED_PRESSURE_CALIBRATE_LOG);
			sLog.d(this, QueryConstant.FETCH_FEED_PRESSURE_CALIBRATE_LOG);
			feedResult = fetchFeedLogStmt.executeQuery();
			if (feedResult.next()) {
				jsonObject.put(ApiConstant.FEED_CREATED_ON, feedResult.getString(ApiConstant.CREATED_ON));
				jsonObject.put(ApiConstant.FEED_USERNAME, feedResult.getString(ApiConstant.USERNAME));
			}

			con2 = DbConnectionManager.getInstance().getConnection();
			fetchRetLogStmt = con2.prepareStatement(QueryConstant.FETCH_RETENTATE_PRESSURE_CALIBRATE_LOG);
			sLog.d(this, QueryConstant.FETCH_RETENTATE_PRESSURE_CALIBRATE_LOG);
			fetchRetResult = fetchRetLogStmt.executeQuery();
			if (fetchRetResult.next()) {
				jsonObject.put(ApiConstant.RETENTATE_CREATED_ON, fetchRetResult.getString(ApiConstant.CREATED_ON));
				jsonObject.put(ApiConstant.RETENTATE_USERNAME, fetchRetResult.getString(ApiConstant.USERNAME));
			}

			con3 = DbConnectionManager.getInstance().getConnection();
			fetchPermLogStmt = con3.prepareStatement(QueryConstant.FETCH_PERMEATE_PRESSURE_CALIBRATE_LOG);
			sLog.d(this, QueryConstant.FETCH_PERMEATE_PRESSURE_CALIBRATE_LOG);
			fetchPermResult = fetchPermLogStmt.executeQuery();
			if (fetchPermResult.next()) {
				jsonObject.put(ApiConstant.PERMEATE_CREATED_ON, fetchPermResult.getString(ApiConstant.CREATED_ON));
				jsonObject.put(ApiConstant.PERMEATE_USERNAME, fetchPermResult.getString(ApiConstant.USERNAME));
			}
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(feedResult, fetchFeedLogStmt, con);
			DbConnectionManager.closeDBConnection(fetchRetResult, fetchRetLogStmt, con2);
			DbConnectionManager.closeDBConnection(fetchPermResult, fetchPermLogStmt, con3);
		}
	}

	/**
	 *  API_DOC: Service to fetch tubing calibrate logs.
	 *
	 * @return the tubing calibrate logs
	 */
	@GET
	@Path(value = ApiConstant.REST_TUBING_CALIBRATE_LOG_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTubingCalibrateLogs() {

		JSONObject responseJson = new JSONObject();
		try {
			JSONObject resJsonObject = fetchTubingCalibrateLogs();
			Logger.info(this, "getTubingCalibrateLogs", HttpStatus.OK_200);
			return Response.ok(resJsonObject.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			sLog.d(this, "getTubingCalibrateLogs, EXCEPTION: " + e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Fetch tubing calibrate logs.
	 *
	 * @return the JSON object of tubing calibrate logs
	 */
	private JSONObject fetchTubingCalibrateLogs() {
		Connection con = null;
		PreparedStatement fetchTubeLogStmt = null;
		ResultSet tubeResult = null;
		JSONObject jsonObject = new JSONObject();
		try {
			con = DbConnectionManager.getInstance().getConnection();
			fetchTubeLogStmt = con.prepareStatement(QueryConstant.FETCH_TUBING_CALIBRATE_LOG);
			sLog.d(this, QueryConstant.FETCH_TUBING_CALIBRATE_LOG);
			tubeResult = fetchTubeLogStmt.executeQuery();
			if (tubeResult.next()) {
				jsonObject.put(ApiConstant.CREATED_ON, tubeResult.getString(ApiConstant.CREATED_ON));
				jsonObject.put(ApiConstant.USERNAME, tubeResult.getString(ApiConstant.USERNAME));
			}
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(tubeResult, fetchTubeLogStmt, con);
		}
	}
}
