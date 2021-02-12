package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.util.AlarmSettingMapper;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.services.handler.ManualModeHandler;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.USBConfiguration;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ValveConnectorConfiguration;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.ModeProp.AlarmProp;

public class AlarmManager {

	private static AlarmManager alarmManager;

	private AlarmSettingMapper alarmSettingMapper = new AlarmSettingMapper();

	private AlarmManager() {
	}

	public static AlarmManager getInstance() {

		synchronized (AlarmManager.class) {
			if (alarmManager == null) {
				alarmManager = new AlarmManager();
			}
		}

		return alarmManager;
	}

	/**
	 * Saving trial detail before run.
	 * @param trialDetailValues json having trial details to be saved.
	 * @return returns trial run id.
	 * @throws Exception throws exception if any issue occur while performing this action.
	 */
	public int saveTrialDetail(String trialDetailValues) throws Exception {

		JSONObject trialDetailJson = new JSONObject(trialDetailValues);
		PreparedStatement saveAlarmPS = null;
		PreparedStatement fetchAlarmPS = null;
		ResultSet fetchAlarmRS = null;
		Connection conn = null;
		int trialRunSettingId = 0;
		JSONArray endPointJsonArray = null;
		JSONArray targetStepJsonArray = null;

		sLog.d(this, trialDetailJson);


		try {
			Logger.info(this, "saveTrialDetail : Saving trial detail...");

			if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(trialDetailJson, false)) {

				conn = DbConnectionManager.getInstance().getConnection();
				conn.setAutoCommit(false);

				// reset all the variable values.
				DeviceManager.getInstance().reset();

				//saving trial run setting details
				trialRunSettingId = RunManager.getInstance().saveTrialRunSetting(trialDetailJson, conn);


				// setting trialRunSettingId for other purposes
				TrialManager.getInstance().setTrialRunSettingId(trialRunSettingId);
				BasicUtility.systemPrint("saveTrialDetail : trialRunSettingId : "+ TrialManager.getInstance().getTrialRunSettingId()+" : GET_ALARM_SETTING_QUERY");
				fetchAlarmPS = conn.prepareStatement(DbConstant.GET_ALARM_SETTING_QUERY);
				fetchAlarmPS.setInt(1, trialRunSettingId);

				fetchAlarmRS = fetchAlarmPS.executeQuery();

				if (fetchAlarmRS.next()) {
					BasicUtility.systemPrint("saveTrialDetail : "+ ApiConstant.TRIAL_ERROR_MESSSAGE);
					throw new SQLException(ApiConstant.TRIAL_ERROR_MESSSAGE);

				} else {
					saveAlarmPS = conn.prepareStatement(DbConstant.SAVE_ALARM_SETTING_QUERY);
					alarmSettingMapper.saveAlarmSettingStatementMapper(trialDetailJson, saveAlarmPS, trialRunSettingId).executeUpdate();
				}


				if (ApiConstant.AUTO.equals(trialDetailJson.getString(ApiConstant.TRIAL_TYPE))) {

					String modeName = trialDetailJson.getString(ApiConstant.MODE_NAME);
					JSONObject recipeJson = DatabaseManager.getInstance()
							.getRecipe(trialDetailJson.getInt(ApiConstant.TRIAL_MASTER_ID))
							.getJSONObject(0);

					// set Konduit/valve configuration

					if (recipeJson.getJSONArray(ApiConstant.ABV).length() == 2) {

						ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_VALVE, USBConfiguration.DO_RESET);

					} else {

						ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_KFCONDUIT, USBConfiguration.DO_RESET);
					}


					if (!ApiConstant.VACUUM_MODE.equalsIgnoreCase(modeName) && BasicUtility.getInstance().isRunMode(modeName)) {
						// Saving endPointArray
						endPointJsonArray = recipeJson.getJSONArray(ApiConstant.END_POINTS);
						EndPointSettingManager.getInstance().setEndPointSettingsArray(endPointJsonArray);
						EndPointSettingManager.getInstance().saveEndPointSetting(trialDetailJson, conn, false);

					} else if (
							ApiConstant.FLUSHING.equalsIgnoreCase(modeName) ||
							ApiConstant.CLEANING.equalsIgnoreCase(modeName) ||
							ApiConstant.NWP.equalsIgnoreCase(modeName)
							) {
						// Saving targetStepArray
						targetStepJsonArray = recipeJson.getJSONArray(ApiConstant.TARGET_STEP);
						TargetStepSettingManager.getInstance().setTargetStepSettingsArray(targetStepJsonArray);
						TargetStepSettingManager.getInstance().saveTargetStepSetting(trialDetailJson, conn, false);
					}

				}
				conn.commit();
			} else {

				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}


		} catch (InvalidCredentialException ex) {
			throw new InvalidCredentialException(ex.getMessage());
		} catch (SQLException ex) {

			try {

				if (conn != null) {

					conn.rollback();
				}
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

		} catch (TrialRunException trEx) {
			trEx.printStackTrace();
			throw new TrialRunException(ApiConstant.TRIAL_ERROR_MESSSAGE);

		} catch (Exception ex) {

			try {

				if (conn != null) {

					conn.rollback();
				}
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		} finally {
			DbConnectionManager.closeDBConnection(fetchAlarmRS, fetchAlarmPS, conn);
		}

		return trialRunSettingId;
	}


	public void editTrialDetail(String trialDetailValues) throws Exception {

		JSONObject trialDetailJson = new JSONObject(trialDetailValues);
		Connection conn = null;

		sLog.d(this, trialDetailJson);


		try {

			if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(trialDetailJson, false)) {

				conn = DbConnectionManager.getInstance().getConnection();
				conn.setAutoCommit(false);

				//edit trial run setting details
				RunManager.getInstance().editTrialRunSetting(trialDetailJson, conn);

				saveAlarmAtRun(trialDetailValues, conn, false);

				conn.commit();

				//                EndPointSettingManager.getInstance().updateEPSettingAndSaveEPChangeLog(trialDetailJson, true);

				// saving trial lo

			}

		} catch (InvalidCredentialException ex) {
			throw new InvalidCredentialException(ex.getMessage());
		} catch (SQLException ex) {

			try {

				if (conn != null) {

					conn.rollback();
				}
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

		} catch (Exception ex) {

			try {

				if (conn != null) {

					conn.rollback();
				}
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

		} finally {
			DbConnectionManager.closeDBConnection(null, null, conn);
		}

	}


	public void saveAlarmAtRun(String alarmValues, Connection conn, boolean saveAtRun) throws Exception {

		JSONObject alarmJson = new JSONObject(alarmValues);
		PreparedStatement updateAlarmPS = null;
		JSONArray endPointJsonArray = null;
		JSONArray targetStepJsonArray = null;

		sLog.d(this, alarmJson);

		try {

			updateAlarmPS = conn.prepareStatement(DbConstant.UPDATE_ALARM_SETTING_QUERY);
			updateAlarmPS = alarmSettingMapper.updateAlarmSettingStatementMapper(alarmJson, updateAlarmPS);
			int rows = updateAlarmPS.executeUpdate();
			if(rows == 0){
				throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}

			if (ApiConstant.AUTO.equals(alarmJson.getString(ApiConstant.TRIAL_TYPE))) {
				String modeName = alarmJson.getString(ApiConstant.MODE_NAME);
				JSONObject recipeJson = DatabaseManager.getInstance()
						.getRecipe(alarmJson.getInt(ApiConstant.TRIAL_MASTER_ID))
						.getJSONObject(0);
				if (!saveAtRun) {
					if (recipeJson.getJSONArray(ApiConstant.ABV).length() == 2) {
						ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_VALVE, USBConfiguration.DO_RESET);
					} else {
						ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_KFCONDUIT, USBConfiguration.DO_RESET);
					}
				}
				if (!ApiConstant.VACUUM_MODE.equalsIgnoreCase(modeName) && BasicUtility.getInstance().isRunMode(modeName)) {
					// Saving endPointArray
					if (
							(EndPointSettingManager.getInstance().getEndPointSettings() == null) ||
							(EndPointSettingManager.getInstance().getEndPointSettings()[0].getTrialMasterId() != recipeJson.getInt(ApiConstant.TRIAL_MASTER_ID))
							) {
						BasicUtility.systemPrint("inside", "endPointset");
						endPointJsonArray = recipeJson.getJSONArray(ApiConstant.END_POINTS);
						EndPointSettingManager.getInstance().setEndPointSettingsArray(endPointJsonArray);
					}
					RunManager.getInstance().checkAndAddDeltaPNote(
							alarmJson,
							TrialManager.getInstance().getTrialRunSettingId(),
							conn,
							true);
					EndPointSettingManager.getInstance().saveEndPointSetting(alarmJson, conn, true);
				} else if (
						ApiConstant.FLUSHING.equalsIgnoreCase(modeName) ||
						ApiConstant.CLEANING.equalsIgnoreCase(modeName) ||
						ApiConstant.NWP.equalsIgnoreCase(modeName)
						) {
					// Saving TargetStepArray
					if (
							(TargetStepSettingManager.getInstance().getTargetStepSettings()) == null ||
							(TargetStepSettingManager.getInstance().getTargetStepSettings()[0].getTrialMasterId() != recipeJson.getInt(ApiConstant.TRIAL_MASTER_ID))
							) {
						BasicUtility.systemPrint("inside", "targetStepset");
						targetStepJsonArray = recipeJson.getJSONArray(ApiConstant.TARGET_STEP);
						TargetStepSettingManager.getInstance().setTargetStepSettingsArray(targetStepJsonArray);
					}
					TargetStepSettingManager.getInstance().saveTargetStepSetting(alarmJson, conn, true);
				}
			}

			if (saveAtRun) {

				if (DeviceManager.getInstance().getModeType() == 0) {
					AlarmProp alarmProp = setAlarmAtRun(alarmJson);
					DeviceManager.getInstance().getModeProp().setAlarmProp(alarmProp);
					sLog.d(this, DeviceManager.getInstance().getModeProp().getAlarmProp());
					String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);
					if (!ApiConstant.VACUUM_MODE.equalsIgnoreCase(modeName) && BasicUtility.getInstance().isRunMode(modeName)) {
						//update deltaP setting at run
						RunManager.getInstance().updateDeltaPSettingInModeProp(alarmJson);
						//update endpoint at run
						EndPointSettingManager.getInstance().updateModePropEndPoint();
					} else if (
							ApiConstant.FLUSHING.equalsIgnoreCase(modeName) ||
							ApiConstant.CLEANING.equalsIgnoreCase(modeName) ||
							ApiConstant.NWP.equalsIgnoreCase(modeName)
							) {
						TargetStepSettingManager.getInstance().updateModePropTargetStep();
					}
				}
				if (DeviceManager.getInstance().getModeType() == 1) {
					BasicUtility.systemPrint("commit", "done");
					conn.commit();
					ManualModeHandler.getInstance().setAlarmListener(TrialManager.getInstance().getTrialRunSettingId());
				}


				//set alarm indicator
				AlarmManager.getInstance().stopDeviceAlarmIndicator();
				TrialManager.getInstance().setAlarmActive(false);

				// saving trial log
				if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

					LogManager.getInstance().insertTrialLogGetLogId(
							BasicUtility.getInstance().getUserId(alarmJson, conn),
							TrialManager.getInstance().getTrialRunSettingId(),
							ApiConstant.EDIT_ALARM_ACTION,
							ApiConstant.LOG_TYPE_TRIAL,
							conn
							);

					//Adding digital signature notes
					if(alarmJson.has(ApiConstant.DIGITAL_NOTES)){
						new NotesManager().saveNotesDS(new Notes(alarmJson.getString(ApiConstant.DIGITAL_NOTES)), conn);
					}
				} else {
					throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				}


			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}


	}

	private Double getAlarmValue(String alarmValue) {
		if (ApiConstant.BLANK_QUOTE.equals(alarmValue)) {
			return null;
		}

		return Double.parseDouble(alarmValue);
	}


	public AlarmProp setAlarm(JSONObject alarmJson) throws Exception {

		AlarmProp alarmProp = new AlarmProp();

		sLog.d(this, alarmJson);

		Double feedPressureLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_ALARM));
		Double feedPressureLowStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_STOP));
		Double feedPressureHighStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_STOP));
		Double feedPressureHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_ALARM));
		alarmProp.setFeedPressure(new AlarmProp.AlarmRange(feedPressureHighStop, feedPressureHighAlarm, feedPressureLowAlarm, feedPressureLowStop));


		Double feedWtLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_LOW_ALARM));
		Double feedWtLowStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_LOW_STOP));
		Double feedWtHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_HIGH_ALARM));
		Double feedWtHighStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_HIGH_STOP));
		alarmProp.setFeedWt(new AlarmProp.AlarmRange(feedWtHighStop, feedWtHighAlarm, feedWtLowAlarm, feedWtLowStop));


		Double permWtLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_LOW_ALARM));
		Double permWtLowStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_LOW_STOP));
		Double permWtHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_HIGH_ALARM));
		Double permWtHighStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_HIGH_STOP));
		alarmProp.setPermWt(new AlarmProp.AlarmRange(permWtHighStop, permWtHighAlarm, permWtLowAlarm, permWtLowStop));


		Double permPressureLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_ALARM));
		Double permPressureLowStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_STOP));
		Double permPressureHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_ALARM));
		Double permPressureHighStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_STOP));
		alarmProp.setPermPressure(new AlarmProp.AlarmRange(permPressureHighStop, permPressureHighAlarm, permPressureLowAlarm, permPressureLowStop));


		Double konduitCh1HighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_ALARM));
		Double konduitCh1HighStop = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_STOP));
		alarmProp.setKonduit1(new AlarmProp.AlarmRange(konduitCh1HighStop, konduitCh1HighAlarm, null, null));


		Double konduitCh2HighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_ALARM));
		Double konduitCh2HighStop = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_STOP));
		alarmProp.setKonduit2(new AlarmProp.AlarmRange(konduitCh2HighStop, konduitCh2HighAlarm, null, null));

		Double flowMeterCh1HighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_ALARM));
		Double flowMeterCh1HighStop = getAlarmValue(alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_STOP));
		alarmProp.setFlowMeterVolumeCh1(new AlarmProp.AlarmRange(flowMeterCh1HighStop, flowMeterCh1HighAlarm, null, null));

		return alarmProp;

	}

	public AlarmProp setAlarmAtRun(JSONObject alarmJson) throws Exception {
		System.out.println(alarmJson.toString());
		AlarmProp alarmProp = DeviceManager.getInstance().getModeProp().getAlarmProp();

		sLog.d(this, alarmJson);

		Double feedPressureLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_ALARM));
		Double feedPressureLowStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_STOP));
		Double feedPressureHighStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_STOP));
		Double feedPressureHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_ALARM));
		alarmProp.setFeedPressure(new AlarmProp.AlarmRange(feedPressureHighStop, feedPressureHighAlarm, feedPressureLowAlarm, feedPressureLowStop));


		Double feedWtLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_LOW_ALARM));
		Double feedWtLowStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_LOW_STOP));
		Double feedWtHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_HIGH_ALARM));
		Double feedWtHighStop = getAlarmValue(alarmJson.getString(ApiConstant.FEED_WT_HIGH_STOP));
		alarmProp.setFeedWt(new AlarmProp.AlarmRange(feedWtHighStop, feedWtHighAlarm, feedWtLowAlarm, feedWtLowStop));


		Double permWtLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_LOW_ALARM));
		Double permWtLowStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_LOW_STOP));
		Double permWtHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_HIGH_ALARM));
		Double permWtHighStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_WT_HIGH_STOP));
		alarmProp.setPermWt(new AlarmProp.AlarmRange(permWtHighStop, permWtHighAlarm, permWtLowAlarm, permWtLowStop));


		Double permPressureLowAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_ALARM));
		Double permPressureLowStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_STOP));
		Double permPressureHighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_ALARM));
		Double permPressureHighStop = getAlarmValue(alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_STOP));
		alarmProp.setPermPressure(new AlarmProp.AlarmRange(permPressureHighStop, permPressureHighAlarm, permPressureLowAlarm, permPressureLowStop));


		Double konduitCh1HighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_ALARM));
		Double konduitCh1HighStop = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_STOP));
		alarmProp.setKonduit1(new AlarmProp.AlarmRange(konduitCh1HighStop, konduitCh1HighAlarm, null, null));


		Double konduitCh2HighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_ALARM));
		Double konduitCh2HighStop = getAlarmValue(alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_STOP));
		alarmProp.setKonduit2(new AlarmProp.AlarmRange(konduitCh2HighStop, konduitCh2HighAlarm, null, null));

		Double flowMeterCh1HighAlarm = getAlarmValue(alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_ALARM));
		Double flowMeterCh1HighStop = getAlarmValue(alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_STOP));
		alarmProp.setFlowMeterVolumeCh1(new AlarmProp.AlarmRange(flowMeterCh1HighStop, flowMeterCh1HighAlarm, null, null));

		return alarmProp;

	}

	public JSONObject fetchAlarmSetting(String trialId) throws Exception {

		Connection conn = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchAlarmSettingPS = null;
		ResultSet fetchAlarmSettingRS = null;

		try {
			conn.setAutoCommit(true);

			fetchAlarmSettingPS = conn.prepareStatement(DbConstant.GET_ALARM_SETTING_FROM_TRIAL_ID_QUERY);
			fetchAlarmSettingPS.setString(1, trialId);

			fetchAlarmSettingRS = fetchAlarmSettingPS.executeQuery();
			JSONArray alarmSettingJson = JsonMapper.getJSONFromResultSet(fetchAlarmSettingRS);
			sLog.d(this, alarmSettingJson);
			if (alarmSettingJson.length() > 0) {

				return alarmSettingJson.getJSONObject(0);
			}


		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(fetchAlarmSettingRS, fetchAlarmSettingPS, conn);
		}
		return null;
	}

	public JSONObject fetchAlarmSettingForManual(int trialRunSettingId) throws Exception {

		Connection conn = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchAlarmSettingPS = null;
		ResultSet fetchAlarmSettingRS = null;

		try {
			conn.setAutoCommit(true);

			fetchAlarmSettingPS = conn.prepareStatement(QueryConstant.GET_ALARM_SETTING_FOR_MANUAL_QUERY);
			fetchAlarmSettingPS.setInt(1, trialRunSettingId);
			Logger.info(QueryConstant.GET_ALARM_SETTING_FOR_MANUAL_QUERY);
			fetchAlarmSettingRS = fetchAlarmSettingPS.executeQuery();
			JSONArray alarmSettingJson = JsonMapper.getJSONFromResultSet(fetchAlarmSettingRS);
			sLog.d(this, alarmSettingJson);
			if (alarmSettingJson.length() > 0) {

				return alarmSettingJson.getJSONObject(0);
			}


		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(fetchAlarmSettingRS, fetchAlarmSettingPS, conn);
		}
		return null;
	}

	public void saveAlarmHistory(JSONObject alarmLiveJson) throws SQLException {

		PreparedStatement saveAlarmHistoryPS = null;
		Connection conn = null;

		try {

			conn = DbConnectionManager.getInstance().getConnection();
			conn.setAutoCommit(false);

			saveAlarmHistoryPS = conn.prepareStatement(DbConstant.SAVE_ALARM_HISTORY_SETTING_QUERY);
			alarmSettingMapper.saveAlarmHistoryStatementMapper(alarmLiveJson, saveAlarmHistoryPS).executeUpdate();

			conn.commit();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (SQLException sqlEx) {

				Logger.error(ApiConstant.EXCEPTION + sqlEx.getMessage(), sqlEx);
			}
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			DbConnectionManager.closeDBConnection(null, saveAlarmHistoryPS, conn);
		}

	}

	public void setDeviceAlarmIndicator(String message) {
		ComLib.get().getPumpAlarmIndicatorHandler().send(2, message);
	}

	public void stopDeviceAlarmIndicator() {
		ComLib.get().getPumpAlarmIndicatorHandler().send(1, ApiConstant.BLANK_SPACE);
	}

	public void muteAlarmIndicator(String message) {
		ComLib.get().getPumpAlarmIndicatorHandler().send(3, message);
	}


}
