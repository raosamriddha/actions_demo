package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.nonrun.NonRunTrialManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.dao.util.ManualNotesUtil;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.services.handler.ManualModeHandler;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.nonrun.NonRunModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.RunModeManager;

public class RunManager {

	private static RunManager runManager;

	public static RunManager getInstance() {

		synchronized (RunManager.class) {
			if (runManager == null) {
				runManager = new RunManager();
			}
		}

		return runManager;
	}

	/**
	 * @param trialDetailJson
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws TrialRunException
	 */
	public int saveTrialRunSetting(final JSONObject trialDetailJson, final Connection conn) throws SQLException, TrialRunException {

		sLog.d(this, trialDetailJson);
		final SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();

		PreparedStatement saveTrialRunSettingPS = null;
		int trialRunSettingId = 0;
		String isFeedEmpty = null;
		int qPermeateFrequency;
		boolean feedScaleOverride;
		String deltaP = ApiConstant.BLANK_QUOTE;
		String deltaPRate = ApiConstant.BLANK_QUOTE;
		String deltaPDuration = ApiConstant.BLANK_QUOTE;
		boolean enableDeltaPLog = false;
		String valve_2_connector = ApiConstant.BLANK_QUOTE;

		String temperature = null;

		try {

			if (ValidationManager.isTrialRunExist(trialDetailJson.getString(ApiConstant.TRIAL_ID))) {
				throw new TrialRunException(ApiConstant.TRIAL_RUN_EXIST_ERROR_MESSAGE);
			}

			qPermeateFrequency = trialDetailJson.getInt(ApiConstant.Q_PERMEATE_FREQUENCY);
			feedScaleOverride = trialDetailJson.getBoolean(ApiConstant.IS_FEED_SCALE_OVERRIDE);
			// setting Q permeate frequency
			DeviceManager.getInstance().setqPermFrequency(qPermeateFrequency);
			DeviceManager.getInstance().setFeedScaleOverride(feedScaleOverride);


			if (trialDetailJson.has(ApiConstant.TEMPERATURE)) {
				try {
					temperature = trialDetailJson.getString(ApiConstant.TEMPERATURE);
					BasicUtility.systemPrint("temperature", temperature);

				} catch (final Exception ex) {
					ex.printStackTrace();
					BasicUtility.systemPrint("Handled", "");
				}
			}

			if (trialDetailJson.getString(ApiConstant.IS_FEED_EMPTY) == null) {
				isFeedEmpty = ApiConstant.BLANK_QUOTE;
			} else {
				isFeedEmpty = trialDetailJson.getString(ApiConstant.IS_FEED_EMPTY);
			}

			if(
					BasicUtility.getInstance().isRunMode(trialDetailJson.getString(ApiConstant.MODE_NAME)) &&
					!ApiConstant.VACUUM_MODE.equalsIgnoreCase(trialDetailJson.getString(ApiConstant.MODE_NAME))
					){
				deltaP = trialDetailJson.getString(ApiConstant.NEW_DELTA_P);
				deltaPRate = trialDetailJson.getString(ApiConstant.NEW_DELTA_P_RATE);
				deltaPDuration = trialDetailJson.getString(ApiConstant.NEW_DELTA_P_DURATION);
				enableDeltaPLog = true;
			}

			//            conn.setAutoCommit(false);


			// setting valve_2 value if exists
			if (trialDetailJson.has(ApiConstant.VALVE_2_CONNECTOR_COLUMN)
					&& trialDetailJson.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN) != null
					&& !trialDetailJson.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN).isEmpty()) {
				valve_2_connector = trialDetailJson.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN);
			}

			saveTrialRunSettingPS = conn.prepareStatement(DbConstant.SAVE_TRIAL_RUN_SETTING, Statement.RETURN_GENERATED_KEYS);

			saveTrialRunSettingPS.setString(1, trialDetailJson.getString(ApiConstant.TRIAL_ID));
			saveTrialRunSettingPS.setString(2, isFeedEmpty);
			saveTrialRunSettingPS.setString(3, trialDetailJson.getString(ApiConstant.FEED_HOLD_UP));
			saveTrialRunSettingPS.setString(4, trialDetailJson.getString(ApiConstant.FEED_START_WEIGHT));
			saveTrialRunSettingPS.setInt(5, trialDetailJson.getInt(ApiConstant.TRIAL_MASTER_ID));
			saveTrialRunSettingPS.setString(6, trialDetailJson.getString(ApiConstant.TRIAL_TYPE));
			saveTrialRunSettingPS.setString(7, temperature);
			saveTrialRunSettingPS.setString(8, BasicUtility.getInstance().getCurrentTimestamp());
			saveTrialRunSettingPS.setString(9, BasicUtility.getInstance().getCurrentTimestamp());
			saveTrialRunSettingPS.setString(10, "");
			saveTrialRunSettingPS.setInt(11,
					BasicUtility.getInstance()
					.getUserId(trialDetailJson)
					);
			saveTrialRunSettingPS.setInt(12, systemSetting.getTotalizerUnit());
			saveTrialRunSettingPS.setInt(13, systemSetting.getTotalizerUnit_2());
			saveTrialRunSettingPS.setInt(14, DeviceManager.getInstance().getqPermFrequency());
			saveTrialRunSettingPS.setString(15, ""+feedScaleOverride);
			saveTrialRunSettingPS.setString(16, trialDetailJson.getString(ApiConstant.DEVICE_ID));
			saveTrialRunSettingPS.setString(17, deltaP);
			saveTrialRunSettingPS.setString(18, deltaPRate);
			saveTrialRunSettingPS.setString(19, deltaPDuration);
			saveTrialRunSettingPS.setString(20, valve_2_connector);

			saveTrialRunSettingPS.executeUpdate();


			try (ResultSet generatedKeys = saveTrialRunSettingPS.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					trialRunSettingId = generatedKeys.getInt(1);
					//                    conn.commit();
				} else {
					throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				}
			}


			if (!ApiConstant.BLANK_QUOTE.equalsIgnoreCase(trialDetailJson.getString(ApiConstant.FEED_START_WEIGHT))) {

				DeviceManager.getInstance().setFeedStartWt(trialDetailJson.getDouble(ApiConstant.FEED_START_WEIGHT));
			} else {

				DeviceManager.getInstance().setFeedStartWt(0);
			}


			// saving trial log
			if (trialRunSettingId != 0) {

				LogManager.getInstance().insertTrialLogGetLogId(
						BasicUtility.getInstance().getUserId(trialDetailJson, conn),
						trialRunSettingId,
						ApiConstant.SAVE_TRIAL_DETAIL_ACTION,
						ApiConstant.LOG_TYPE_TRIAL,
						conn
						);

				if(trialDetailJson.has(ApiConstant.DIGITAL_NOTES)){
					new NotesManager().saveNotesDS(new Notes(trialDetailJson.getString(ApiConstant.DIGITAL_NOTES)), conn);
				}

				final String qPermFreqLog = (qPermeateFrequency>=60)?(qPermeateFrequency/60+ApiConstant.BLANK_SPACE+ApiConstant.MINUTE):(qPermeateFrequency+ApiConstant.BLANK_SPACE+ApiConstant.SECOND);
				LogManager.getInstance().insertTrialLogGetLogId(
						BasicUtility.getInstance().getUserId(trialDetailJson, conn),
						trialRunSettingId,
						ApiConstant.Q_PERMEATE_FREQUENCY_ACTION +qPermFreqLog,
						ApiConstant.LOG_TYPE_TRIAL,
						conn
						);
				if(enableDeltaPLog){
					checkAndAddDeltaPNote(trialDetailJson, trialRunSettingId, conn, false);
				}

				// set valve_2 to comLib and save log
				if (!ApiConstant.BLANK_QUOTE.equals(valve_2_connector)) {
					ManualModeManager.getInstance().setValveToComLib(valve_2_connector, trialRunSettingId);
					ManualNotesUtil.saveManualNoteOnRun(ApiConstant.VALVE_CONNECTOR_KEY, "2", ApiConstant.BLANK_QUOTE,
							valve_2_connector, trialRunSettingId, conn);
				}
				
				if (trialDetailJson.has(ApiConstant.FILTER) 
						&& !trialDetailJson.getJSONObject(ApiConstant.FILTER).isEmpty()) {
					ManualModeManager.getInstance().
						saveManualFilterAtRun(trialDetailJson.getJSONObject(ApiConstant.FILTER), conn, trialRunSettingId);
					ManualNotesUtil.saveManualFilterNote(trialDetailJson, trialRunSettingId, conn);
				}
			} else {
				throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}


		} catch (final SQLException ex) {
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		} /*finally {
            DbConnectionManager.closeDBConnection(null, saveTrialRunSettingPS, conn);
        }*/

		return trialRunSettingId;
	}

	public void editTrialRunSetting(final JSONObject trialDetailJson, final Connection conn) throws SQLException, TrialRunException {

		sLog.d(this, trialDetailJson);

		//        Connection conn = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateTrialRunSettingPS = null;

		try {

			conn.setAutoCommit(false);


			updateTrialRunSettingPS = conn.prepareStatement(DbConstant.UPDATE_TRIAL_RUN_SETTING);

			updateTrialRunSettingPS.setString(1, trialDetailJson.getString(ApiConstant.IS_FEED_EMPTY));
			updateTrialRunSettingPS.setString(2, trialDetailJson.getString(ApiConstant.FEED_HOLD_UP));
			updateTrialRunSettingPS.setString(3, trialDetailJson.getString(ApiConstant.FEED_START_WEIGHT));
			updateTrialRunSettingPS.setInt(4, trialDetailJson.getInt(ApiConstant.TRIAL_MASTER_ID));
			updateTrialRunSettingPS.setString(5, trialDetailJson.getString(ApiConstant.TRIAL_TYPE));
			updateTrialRunSettingPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());
			updateTrialRunSettingPS.setString(7, trialDetailJson.getString(ApiConstant.TRIAL_ID));


			updateTrialRunSettingPS.executeUpdate();


			if (!ApiConstant.BLANK_QUOTE.equalsIgnoreCase(trialDetailJson.getString(ApiConstant.FEED_START_WEIGHT))) {

				DeviceManager.getInstance().setFeedStartWt(trialDetailJson.getDouble(ApiConstant.FEED_START_WEIGHT));

			} else {
				DeviceManager.getInstance().setFeedStartWt(0);
			}

			if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

				LogManager.getInstance().insertTrialLogGetLogId(
						BasicUtility.getInstance().getUserId(trialDetailJson, conn),
						TrialManager.getInstance().getTrialRunSettingId(),
						ApiConstant.EDIT_TRIAL_DETAIL_ACTION,
						ApiConstant.LOG_TYPE_TRIAL,
						conn
						);

				if(trialDetailJson.has(ApiConstant.DIGITAL_NOTES)){
					new NotesManager().saveNotesDS(new Notes(trialDetailJson.getString(ApiConstant.DIGITAL_NOTES)), conn);
				}
			} else {
				throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}

			//            conn.commit();

		} catch (final SQLException ex) {
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		}

	}

	// status -- 0-run , 1-completed, 2-Terminated
	public void updateTrialRunStatus(final int trialRunSettingId, final String endTime, final int status) throws Exception {

		Connection conn = null;
		PreparedStatement updateTrialRunStatusPS = null;

		try {
			conn = DbConnectionManager.getInstance().getConnection();

			updateTrialRunStatusPS = conn.prepareStatement(DbConstant.UPDATE_TRIAL_RUN_STATUS_QUERY);
			updateTrialRunStatusPS.setInt(1, status);
			updateTrialRunStatusPS.setString(2, endTime);
			updateTrialRunStatusPS.setInt(3, trialRunSettingId);
			updateTrialRunStatusPS.executeUpdate();

		} catch (final SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateTrialRunStatusPS, conn);
		}

	}

	public void updateTrialStartTime(final int trialRunSettingId, final String startTime) throws Exception {

		Connection conn = null;
		PreparedStatement updateTrialStartTimePS = null;

		try {
			conn = DbConnectionManager.getInstance().getConnection();

			updateTrialStartTimePS = conn.prepareStatement(DbConstant.UPDATE_TRIAL_START_TIME_QUERY);
			updateTrialStartTimePS.setString(1, startTime);
			updateTrialStartTimePS.setInt(2, trialRunSettingId);
			updateTrialStartTimePS.executeUpdate();

		} catch (final SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateTrialStartTimePS, conn);
		}

	}

	public void resumeTrialOnRestart(final JSONObject runJson, final JSONObject recipeJson) throws Exception {

		final String modeName = recipeJson.getString(ApiConstant.MODE_NAME);

		BasicUtility.systemPrint("modename", modeName);

		if (BasicUtility.getInstance().isRunMode(modeName)) {

			TrialManager.getInstance().startAutoModeTrial(runJson, recipeJson);
		} else {
			NonRunTrialManager.getInstance().startNonRunMode(runJson, recipeJson);
		}
	}

	public boolean isTrialPaused(final int trialRunSettingId) throws Exception {
		Connection conn = null;
		PreparedStatement isTrailPausedPS = null;
		ResultSet isTrailPausedRS = null;

		try {
			conn = DbConnectionManager.getInstance().getConnection();

			isTrailPausedPS = conn.prepareStatement(DbConstant.CHECK_IS_TRIAL_PAUSED_QUERY);
			isTrailPausedPS.setInt(1, trialRunSettingId);
			isTrailPausedRS = isTrailPausedPS.executeQuery();

			if (isTrailPausedRS.next()) {

				BasicUtility.systemPrint("action", isTrailPausedRS.getString(ApiConstant.ACTION));
				if (
						ApiConstant.TRIAL_PAUSE_ACTION.equalsIgnoreCase(isTrailPausedRS.getString(ApiConstant.ACTION)) ||
						ApiConstant.TRIAL_ABRUPT_PAUSE_ACTION.equalsIgnoreCase(isTrailPausedRS.getString(ApiConstant.ACTION))
						) {
					return true;
				}

			}

			return false;


		} catch (final SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, isTrailPausedPS, conn);
		}
	}

	public boolean isEmergencyValveOpen(final int trialRunSettingId) throws Exception {
		Connection conn = null;
		PreparedStatement isEmergencyValveOpenPS = null;
		ResultSet isEmergencyValveOpenRS = null;

		try {
			conn = DbConnectionManager.getInstance().getConnection();

			isEmergencyValveOpenPS = conn.prepareStatement(DbConstant.CHECK_IS_EMERGENCY_VALVE_OPEN);
			isEmergencyValveOpenPS.setInt(1, trialRunSettingId);
			isEmergencyValveOpenRS = isEmergencyValveOpenPS.executeQuery();

			if (isEmergencyValveOpenRS.next()) {

				if (ApiConstant.EMERGENCY_VALVE_OPEN_ACTION.equalsIgnoreCase(isEmergencyValveOpenRS.getString(ApiConstant.ACTION))) {

					return true;
				}

			}

			return false;


		} catch (final SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, isEmergencyValveOpenPS, conn);
		}
	}

	public JSONObject isTrialRunning() throws Exception {

		Connection conn = null;
		PreparedStatement isTrailRunningPS = null;
		ResultSet isTrailRunningRS = null;
		JSONArray runJsonArray = null, recipeJsonArray = null;

		JSONObject runJson = null;
		final JSONObject responseJson = new JSONObject();
		boolean isRunning = false;

		try {
			conn = DbConnectionManager.getInstance().getConnection();

			isTrailRunningPS = conn.prepareStatement(DbConstant.CHECK_IS_TRIAL_RUNNING_QUERY);
			isTrailRunningRS = isTrailRunningPS.executeQuery();


			runJsonArray = JsonMapper.getJSONFromResultSet(isTrailRunningRS);


			if (runJsonArray.length() > 0) {

				runJson = runJsonArray.getJSONObject(0);

				recipeJsonArray = new RequestHandler().getAllTrialDetails(runJson.getInt(ApiConstant.TRIAL_MASTER_ID), runJson.getInt(ApiConstant.TRIAL_RUN_SETTING_ID));

				recipeJsonArray.getJSONObject(0).put(ApiConstant.IS_PAUSED, TrialManager.getInstance().isPaused());
				recipeJsonArray.getJSONObject(0).put(ApiConstant.IS_CONNECTED, ComLib.get().isConnected());
				recipeJsonArray.getJSONObject(0).put(ApiConstant.IS_CONNECTING, DeviceManager.getInstance().isConnecting());
				recipeJsonArray.getJSONObject(0).put(ApiConstant.IS_EMERGENCY_VALVE_OPEN, isEmergencyValveOpen(runJson.getInt(ApiConstant.TRIAL_RUN_SETTING_ID)));
				recipeJsonArray.getJSONObject(0).put(ApiConstant.IS_CLEANING_PAUSE, DeviceManager.getInstance().isCleaningPause());
				recipeJsonArray.getJSONObject(0).put(ApiConstant.CLEANING_MESSAGE, DeviceManager.getInstance().getCleaningMessage());
				recipeJsonArray.getJSONObject(0).put(ApiConstant.END_POINT_SETTING_KEY, EndPointSettingManager.getInstance().getEndPointSettingFromDb());
				recipeJsonArray.getJSONObject(0).put(ApiConstant.TARGET_STEP_SETTING_KEY, TargetStepSettingManager.getInstance().getTargetStepSettingFromDb());


				isRunning = true;

			}

			responseJson.put(ApiConstant.IS_RUNNING, isRunning);
			responseJson.put(ApiConstant.VACUUME_MODE_STEP, DeviceManager.getInstance().getVacuumModeStep());
			responseJson.put(ApiConstant.STAGE_ID, TrialManager.getInstance().getCurrentStageId());
			responseJson.put(ApiConstant.DATA, recipeJsonArray);
			responseJson.put(ApiConstant.PARAM_IS_OLD_CONFIG, ManualModeHandler.isOldConfig());
			return responseJson;

		} catch (final SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, isTrailRunningPS, conn);
		}

	}

	public void recirculationPause(boolean isReconnect) {
		final String modeName = DeviceManager.getInstance()
				.getRecipeJson()
				.getString(ApiConstant.MODE_NAME);
		if(!isReconnect){
			TrialManager.getInstance().setPaused(true);
		}
		if (BasicUtility.getInstance().isRunMode(modeName)) {
			RunModeManager.get().onDisconnect();
		} else {
			NonRunModeManager.get().onDisconnect();
		}
		try {
			TrialManager.getInstance().onPause();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public void updateTrialRunStatusOnUnexpectedClose() throws Exception {

		Connection conn = null;
		PreparedStatement updateTrialStatusPS = null;

		try {
			conn = DbConnectionManager.getInstance().getConnection();

			updateTrialStatusPS = conn.prepareStatement(DbConstant.UPDATE_TRIAL_STATUS_ON_UNEXPECTED_CLOSE_QUERY);
			updateTrialStatusPS.executeUpdate();

		} catch (final SQLException e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateTrialStatusPS, conn);
		}
	}

	public void deleteFailedAutoTrialDetails(final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement deleteFailedTrialStmt = null;
		try {
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_TRIAL_RUN_SETTINGS);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			final int affectedRows = deleteFailedTrialStmt.executeUpdate();
			if (affectedRows == 0) {
				return;
			}
			deleteFailedTrialStmt.close();
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_ALARMS);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			deleteFailedTrialStmt.executeUpdate();


		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			DbConnectionManager.closeDBConnection(null, deleteFailedTrialStmt, con);
		}
	}

	private void AddDeltaPChangeNote(final JSONObject trialDetailJson, final Connection conn) throws SQLException {
		long trialLogId;
		PreparedStatement saveDeltaPChangeLogPS = null;
		trialLogId = LogManager.getInstance().insertTrialLogGetLogId(
				BasicUtility.getInstance().getUserId(trialDetailJson, conn),
				TrialManager.getInstance().getTrialRunSettingId(),
				ApiConstant.BLANK_QUOTE,
				ApiConstant.LOG_TYPE_TRIAL,
				conn
				);

		try {
			saveDeltaPChangeLogPS = conn.prepareStatement(DbConstant.SAVE_END_POINT_CHANGE_LOG_QUERY);
			saveDeltaPChangeLogPS.setLong(1, trialLogId);
			saveDeltaPChangeLogPS.setString(2, ApiConstant.DELTA_P_ABSOLUTE_NAME);
			saveDeltaPChangeLogPS.setDouble(3, trialDetailJson.getDouble(ApiConstant.OLD_DELTA_P));
			saveDeltaPChangeLogPS.setDouble(4, trialDetailJson.getDouble(ApiConstant.NEW_DELTA_P));
			saveDeltaPChangeLogPS.setInt(5, 0);
			saveDeltaPChangeLogPS.setInt(6, TrialManager.getInstance().getTrialRunSettingId());
			saveDeltaPChangeLogPS.executeUpdate();
		} catch (final SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	public void AddDeltaPRateChangeNote(final JSONObject trialDetailJson, final int trialRunSettingId, final Connection conn) throws SQLException {
		final String deltaPRateNote = "Pump rate changed (%) from "
				+trialDetailJson.getString(ApiConstant.OLD_DELTA_P_RATE)
				+ApiConstant.BLANK_SPACE
				+trialDetailJson.getString(ApiConstant.OLD_DELTA_P_DURATION)
				+" to "
				+trialDetailJson.getString(ApiConstant.NEW_DELTA_P_RATE)
				+ApiConstant.BLANK_SPACE
				+trialDetailJson.getString(ApiConstant.NEW_DELTA_P_DURATION);

		if(trialRunSettingId != 0){

			LogManager.getInstance().insertTrialLogGetLogId(
					BasicUtility.getInstance().getUserId(trialDetailJson, conn),
					trialRunSettingId,
					deltaPRateNote,
					ApiConstant.LOG_TYPE_TRIAL,
					conn
					);
		}else{
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	public void checkAndAddDeltaPNote(final JSONObject trialDetailJson, final int trialRunSettingId, final Connection conn, final boolean saveAtRun) throws SQLException {
		if(!trialDetailJson.getString(ApiConstant.NEW_DELTA_P).equals(ApiConstant.BLANK_QUOTE)){
			if(trialDetailJson.getDouble(ApiConstant.OLD_DELTA_P) != trialDetailJson.getDouble(ApiConstant.NEW_DELTA_P)){
				AddDeltaPChangeNote(trialDetailJson, conn);
			}
			if(
					(trialDetailJson.getDouble(ApiConstant.OLD_DELTA_P_RATE) != trialDetailJson.getDouble(ApiConstant.NEW_DELTA_P_RATE)) ||
					!(trialDetailJson.getString(ApiConstant.OLD_DELTA_P_DURATION).equals(trialDetailJson.getString(ApiConstant.NEW_DELTA_P_DURATION)))
					){
				AddDeltaPRateChangeNote(trialDetailJson, trialRunSettingId, conn);
			}
			if(saveAtRun){
				updateDeltaPSettingToDB(trialDetailJson, conn);
			}
		}
	}

	public void updateDeltaPSettingToDB(final JSONObject trialDetailJson, final Connection conn) throws SQLException {
		PreparedStatement saveDeltaPChangeLogPS ;
		try {
			saveDeltaPChangeLogPS = conn.prepareStatement(DbConstant.UPDATE_DELTA_P_SETTING_AT_RUN_QUERY);
			saveDeltaPChangeLogPS.setString(1, trialDetailJson.getString(ApiConstant.NEW_DELTA_P));
			saveDeltaPChangeLogPS.setString(2, trialDetailJson.getString(ApiConstant.NEW_DELTA_P_RATE));
			saveDeltaPChangeLogPS.setString(3, trialDetailJson.getString(ApiConstant.NEW_DELTA_P_DURATION));
			saveDeltaPChangeLogPS.setInt(4, TrialManager.getInstance().getTrialRunSettingId());
			saveDeltaPChangeLogPS.executeUpdate();
		} catch (final SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}
	public void updateDeltaPSettingInModeProp(final JSONObject trialDetailJson){
		if(!trialDetailJson.getString(ApiConstant.NEW_DELTA_P).equals(ApiConstant.BLANK_QUOTE)){
			final double operatingPressure = trialDetailJson.getDouble(ApiConstant.NEW_DELTA_P);
			final double deltaPRate = trialDetailJson.getDouble(ApiConstant.NEW_DELTA_P_RATE);
			final double deltaPInterval = BasicUtility.getInterval(trialDetailJson.getString(ApiConstant.NEW_DELTA_P_DURATION));
			DeviceManager.getInstance().getModeProp().getValveProp(0).setOperatingPressure(operatingPressure);
			DeviceManager.getInstance().getModeProp().getRecirculationProp().setRate(deltaPRate);
			DeviceManager.getInstance().getModeProp().getRecirculationProp().setInterval(deltaPInterval);
		}

	}
}