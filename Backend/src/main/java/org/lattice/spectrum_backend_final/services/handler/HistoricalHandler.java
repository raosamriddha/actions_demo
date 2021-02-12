package org.lattice.spectrum_backend_final.services.handler;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.NotesHistory;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.HistoricalManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.export.excel.ExportUtil;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * The Class contains APIs for trial logs section.
 * 
 * @author Pinak
 */
@Path(ApiConstant.REST_HISTORICAL_PATH)
public class HistoricalHandler {

	private UserAuthorization authorization = new UserAuthorization();

	/**
	 * API_DOC_1: Service to fetch table historical data.
	 *
	 * @param trialRunSettingId
	 * @param pageSize the page size is the no. of elements required
	 * @param interval the interval of data
	 * @param direction forward or backward page navigation
	 * @param offset the last/first trialLiveDataId
	 * @param pageCount the page count
	 * @param headers
	 * @return the table history logs
	 */
	@GET
	@Path(value = ApiConstant.REST_HIST_TABLE_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTableHistoryLogs(
			@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
			@QueryParam(value = ApiConstant.PAGE_SIZE) int pageSize,
			@QueryParam(value = ApiConstant.INTERVAL) int interval,
			@QueryParam(value = ApiConstant.DIRECTION) int direction,
			@QueryParam(value = ApiConstant.OFFSET) long offset,
			@QueryParam(value = ApiConstant.PAGE_COUNT) int pageCount, @Context HttpHeaders headers) {

		// PAGES: [0 - without total pages, 1 - need total pages]
		// TYPE: [0 - live, 1 - historical]
		// DIRECTION: [0 - backward, 1 - forward]
		// OFFSET is trialLiveDataId
		JSONObject responseJson = null;

		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
			responseJson = new JSONObject();
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getTableHistoryLogs", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}
		long timeStamp = 0;
		try {
			if (offset <= 0) {
				timeStamp = System.currentTimeMillis() / 1000;
			}
			responseJson = new HistoricalManager().fetchTrialTableLogs(trialRunSettingId, direction, pageSize, interval,
					pageCount, timeStamp, offset / 1000);
			Logger.info(this, "getTableHistoryLogs", HttpStatus.OK_200);
			return Response.ok(responseJson.toString()).build();
		} catch (Exception e) {
			responseJson = new JSONObject();
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getTableHistoryLogs", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_2: Service to fetch alarms historical data.
	 *
	 * @param trialRunSettingId
	 * @param pageSize
	 * @param direction
	 * @param offset
	 * @param pageCount
	 * @param type live or historical
	 * @param nature s
	 * @param headers
	 * @return the alarms history
	 * @throws SQLException the SQL exception
	 */
	@GET
	@Path(value = ApiConstant.REST_HIST_ALARMS_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlarmsHistory(@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
			@QueryParam(value = ApiConstant.PAGE_SIZE) int pageSize,
			@QueryParam(value = ApiConstant.DIRECTION) int direction,
			@QueryParam(value = ApiConstant.OFFSET) int offset,
			@QueryParam(value = ApiConstant.PAGE_COUNT) int pageCount,
			@QueryParam(value = ApiConstant.TYPE) String type, @QueryParam(value = ApiConstant.NATURE) String nature,
			@Context HttpHeaders headers) throws SQLException {

		// PAGES: [0 - without total pages, 1 - need total pages]
		// TYPE: [0 - live, 1 - historical]
		// DIRECTION: [0 - backward, 1 - forward]
		// OFFSET is alarm_history_id
		if (TrialManager.getInstance().isRunning() || TrialManager.getInstance().isPaused()) {
			RequestHandler.setSystemSetting();
		}
		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getAlarmsHistory", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}
		try {
			JSONObject alarmsLogsTable = new HistoricalManager().fetchAlarmsLogs(trialRunSettingId, direction, pageSize,
					pageCount, offset, type, nature);
			Logger.info(this, "getAlarmsHistory", HttpStatus.OK_200);
			return Response.ok(alarmsLogsTable.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getAlarmsHistory", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_3: Service to fetch notes historical data.
	 *
	 * @param trialRunSettingId
	 * @param headers
	 * @return the notes history
	 */
	@GET
	@Path(value = ApiConstant.REST_HIST_NOTES_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotesHistory(@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
			@Context HttpHeaders headers) {

		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getNotesHistory", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}
		try {
			List<NotesHistory> notesLogsTable = ExportUtil.getInstance().fetchNotesLogs(trialRunSettingId);
			sLog.d(this, "getNotesHistory, STATUS: " + HttpStatus.OK_200);
			GenericEntity<List<NotesHistory>> notesGenericEntity = new GenericEntity<List<NotesHistory>>(
					notesLogsTable) {
			};
			Logger.info(this, "getNotesHistory", HttpStatus.OK_200);
			return Response.ok(notesGenericEntity).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getNotesHistory", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * API_DOC_4: Service to fetch trial logs.
	 *
	 * @param trialRunSettingId
	 * @param pageSize
	 * @param direction
	 * @param pageCount
	 * @param search the search text
	 * @param offset
	 * @param headers
	 * @return the trial logs
	 * @throws JSONException the JSON exception
	 * @throws Exception the exception
	 */
	@GET
	@Path(value = ApiConstant.REST_HIST_TRIAL_LOGS_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrialLogs(@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
			@QueryParam(value = ApiConstant.PAGE_SIZE) int pageSize,
			@QueryParam(value = ApiConstant.DIRECTION) int direction,
			@QueryParam(value = ApiConstant.PAGE_COUNT) int pageCount,
			@QueryParam(value = ApiConstant.SEARCH) String search, @QueryParam(value = ApiConstant.OFFSET) int offset,
			@Context HttpHeaders headers) throws JSONException, Exception {

		// PAGES: [0 - without total pages, 1 - need total pages]
		// TYPE: [0 - live, 1 - historical]
		// DIRECTION: [0 - backward, 1 - forward]
		// OFFSET is notes_id

		JSONObject responseJson = new JSONObject();
		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)
				&& !authorization.isUserRoleEqualsSuperAdmin(headers)) {
			responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
			Logger.error(this, "getTrialLogs", HttpStatus.UNAUTHORIZED_401, responseJson);
			return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
		}
		try {
			JSONObject trialLogsTable = new HistoricalManager().fetchTrialLogs(trialRunSettingId, direction, pageSize,
					pageCount, search, offset);
			Logger.info(this, "getTrialLogs", HttpStatus.OK_200);
			return Response.ok(trialLogsTable.toString()).build();
		} catch (Exception e) {
			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "getTrialLogs", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
		}
	}

	/**
	 * Gets the all trial logs.
	 *
	 * @param trialRunSettingId
	 * @param headers
	 * @return all trial logs
	 * @throws JSONException the JSON exception
	 * @throws Exception the exception
	 */
	@GET
	@Path(value = ApiConstant.REST_HIST_ALL_TRIAL_LOGS_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTrialLogs(
			@PathParam(value = ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) final int trialRunSettingId,
			@Context HttpHeaders headers) throws JSONException, Exception {

		if (!authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)
				&& !authorization.isUserRoleEqualsSuperAdmin(headers)) {
			Logger.error(this, "getAllTrialLogs", HttpStatus.UNAUTHORIZED_401);
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(new JSONObject().put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE).toString())
					.build();
		}
		try {
			final JSONArray trialLogsTableArray = ExportUtil.getInstance().fetchTrialLogs(trialRunSettingId, true);
			Logger.info(this, "getAllTrialLogs", HttpStatus.OK_200);
			return Response.ok(trialLogsTableArray.toString()).build();
		} catch (Exception e) {
			Logger.error(this, "getAllTrialLogs", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
					.entity(new JSONObject().put(ApiConstant.ERROR, e.getMessage()).toString()).build();
		}
	}

}
