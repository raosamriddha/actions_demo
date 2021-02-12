package org.lattice.spectrum_backend_final.services.handler;

import static org.lattice.spectrum_backend_final.dao.constants.ApiConstant.PARAM_TRIAL_RUN_SETTING_ID;
import static org.lattice.spectrum_backend_final.dao.constants.ApiConstant.REST_GRAPH_ENDPOINT;
import static org.lattice.spectrum_backend_final.dao.constants.ApiConstant.REST_GRAPH_PATH;
import static org.lattice.spectrum_backend_final.dao.constants.ApiConstant.REST_HISTORICAL_DATA_ENDPOINT;
import static org.lattice.spectrum_backend_final.dao.constants.ApiConstant.TYPE;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.GraphManager;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;

/**
 * This class contains APIs used to fetch graph data
 * 
 * @author Pinak
 */
@Path(REST_GRAPH_PATH)
public class GraphHandler {

	private static GraphHandler graphHandler = null;
	private final UserAuthorization authorization = new UserAuthorization();
	
	/**
	 * Gets the single instance of GraphHandler.
	 *
	 * @return single instance of GraphHandler
	 */
	public static GraphHandler getInstance() {

		synchronized (GraphHandler.class) {
			if (graphHandler == null) {
				graphHandler = new GraphHandler();
			}
		}
		return graphHandler;
	}

	/**
	 * Gets the graph data.
	 *
	 * @param trialRunSettingId the trial run setting id
	 * @param type the type of the graph
	 * @param isLive is the trial running or historical data
	 * @param headers
	 * @return the graph data
	 */
	@GET
	@Path(value = REST_GRAPH_ENDPOINT)
	@Produces(MediaType.APPLICATION_JSON)
	public final Response getGraphData(@PathParam(value = PARAM_TRIAL_RUN_SETTING_ID) final int trialRunSettingId,
			@PathParam(value = TYPE) final int type, @PathParam(value = "isLive") final boolean isLive,
			@Context HttpHeaders headers) {
		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getGraphData", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}

		try {
			final JSONArray graphArray = GraphManager.getInstance().fetchGraphData(trialRunSettingId, type, isLive);
			Logger.info(this, "getGraphData", HttpStatus.OK_200);
			return Response.ok(graphArray.toString()).build();
		} catch (Exception e) {
			Logger.error(this, "getGraphData", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject()
					.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION).toString())
					.build();
		}
	}

	/**
	 * Gets the last 1000 historical data.
	 *
	 * @param trialRunSettingId
	 * @param headers
	 * @return the historical data
	 * @throws SQLException the SQL exception
	 */
	@GET
	@Path(value = REST_HISTORICAL_DATA_ENDPOINT)
	@Produces(MediaType.APPLICATION_JSON)
	public final Response getHistoricalData(@PathParam(value = PARAM_TRIAL_RUN_SETTING_ID) final int trialRunSettingId,
			@Context HttpHeaders headers) throws SQLException {
		final long currentTimestamp = System.currentTimeMillis() / 1000;

		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getHistoricalData", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}

		try {
			RequestHandler.setSystemSetting();
			RequestHandler.setTrialRunSettingId(0);
			HistoricalConverter.resetPumpNameValues();
			HistoricalConverter.resetTotalizerUnits();
			HistoricalConverter.setTotalizerUnits(RequestHandler.getStSystemSetting().getTotalizerUnit(),
					RequestHandler.getStSystemSetting().getTotalizerUnit_2());
			HistoricalConverter.assignPumpNameAndAuxType(trialRunSettingId);

			final JSONArray graphArray = GraphManager.getInstance().fetchHistoricalData(trialRunSettingId,
					currentTimestamp);
			Logger.info(this, "getHistoricalData", HttpStatus.OK_200);
			return Response.ok(graphArray.toString()).build();
		} catch (Exception e) {
			Logger.error(this, "getHistoricalData", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(new JSONObject()
					.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION).toString())
					.build();
		}
	}

}
