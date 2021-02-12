package org.lattice.spectrum_backend_final.dao.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.EndPointChangeLog;
import org.lattice.spectrum_backend_final.beans.EndPointSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EndPointSettingManager {


	private static EndPointSettingManager endPointSettingManager = null;

    private EndPointSetting[] endPointSettings = null;

    private EndPointChangeLog endPointChangeLog = null;


    public static EndPointSettingManager getInstance() {

        synchronized (EndPointSettingManager.class) {
            if (endPointSettingManager == null) {
                endPointSettingManager = new EndPointSettingManager();
            }
        }

        return endPointSettingManager;
    }


    public EndPointSetting[] getEndPointSettings() {
        return endPointSettings;
    }

    public void setEndPointSettings(EndPointSetting[] endPointSettings) {
        this.endPointSettings = endPointSettings;
    }

    public void setEndPointSettingsArray(JSONArray endPointJsonArray) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        try {

            // Convert JSON string to Object
            endPointSettings = mapper.readValue(endPointJsonArray.toString(), EndPointSetting[].class);
            sLog.d(this, endPointSettings);


        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }


    }

    public JSONArray getEndPointSettingsJsonArray() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JSONArray endPointJsonArray = null;
        try {

            // Convert POJO array to JSONArray
            String arrayToJson = mapper.writeValueAsString(endPointSettings);
            endPointJsonArray = new JSONArray(arrayToJson);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        return endPointJsonArray;


    }


    public void saveEndPointSetting(JSONObject runJson, Connection conn, boolean isEditMode) throws SQLException, TrialRunException {
        PreparedStatement saveEndPointSettingPS = null;
        int loopCount = 0;
        try {
            updateEPSettingAndSaveEPChangeLog(runJson, conn, isEditMode);
            if (endPointSettings == null) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
            // only executes when save trial call occurs [ not in edit mode ].
            if (!isEditMode) {
                saveEndPointSettingPS = conn.prepareStatement(DbConstant.SAVE_END_POINT_SETTING_DETAILS);
                while (loopCount < endPointSettings.length) {
                    saveEndPointSettingPS.setInt(1, TrialManager.getInstance().getTrialRunSettingId());
                    saveEndPointSettingPS.setString(2, endPointSettings[loopCount].getEndPointType());
                    saveEndPointSettingPS.setDouble(3, endPointSettings[loopCount].getEndPointValue());
                    saveEndPointSettingPS.setInt(4, endPointSettings[loopCount].getStepNo());
                    saveEndPointSettingPS.setString(5, BasicUtility.getInstance().getCurrentTimestamp());
                    saveEndPointSettingPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());
                    saveEndPointSettingPS.addBatch();
                    loopCount++;
                }
                saveEndPointSettingPS.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }


    }


    public void updateEPSettingAndSaveEPChangeLog(JSONObject runEndPointsJson, Connection conn, boolean isEditMode) throws SQLException, TrialRunException {
        int loopCount = 0;
        long trialLogId;
        while (loopCount < runEndPointsJson.getInt(ApiConstant.TOTAL_END_POINT)) {
            double endPointValue = runEndPointsJson.getDouble(
                    ApiConstant.END_POINT_VALUE
                            + ApiConstant.UNDER_SCORE
                            + (loopCount + 1)
            );
            sLog.d("", endPointSettings);
            // check weather endPointValue is changed or not
            if (endPointValue != endPointSettings[loopCount].getEndPointValue()) {
                if (TrialManager.getInstance().getTrialRunSettingId() == 0) {
                    throw new TrialRunException(ApiConstant.TRIAL_RUN_SETTING_ID_EXCEPTION);
                }
                trialLogId = LogManager.getInstance().insertTrialLogGetLogId(
                        BasicUtility.getInstance().getUserId(runEndPointsJson, conn),
                        TrialManager.getInstance().getTrialRunSettingId(),
                        ApiConstant.BLANK_QUOTE,
                        ApiConstant.LOG_TYPE_TRIAL,
                        conn
                );
                if (trialLogId == 0) {
                    throw new RuntimeException(ApiConstant.TRIAL_LOG_ID_EXCEPTION);
                }
                // if changed then update endPointChangeLog and make a log
                endPointChangeLog = new EndPointChangeLog();
                endPointChangeLog.setEndPointType(endPointSettings[loopCount].getEndPointType());
                endPointChangeLog.setOldEndPointValue(endPointSettings[loopCount].getEndPointValue());
                endPointChangeLog.setNewEndPointValue(endPointValue);
                endPointChangeLog.setStepNo(loopCount + 1);
                endPointChangeLog.setTrialLogId(trialLogId);
                // saving endPointChangeLog to db
                saveEndPointChangeLogToDb(conn);
                if (isEditMode) {
                    //update endPointSetting to db
                    updateEndPointSetting(conn);
                }
                endPointSettings[loopCount].setEndPointValue(endPointValue);
            }
            loopCount++;
        }
    }

    private void updateEndPointSetting(Connection conn) throws SQLException {
        PreparedStatement updateEndPointSettingPS = null;
        try {
            if (endPointSettings == null) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
            updateEndPointSettingPS = conn.prepareStatement(DbConstant.UPDATE_END_POINT_SETTING_DETAILS);
            updateEndPointSettingPS.setDouble(1, endPointChangeLog.getNewEndPointValue());
            updateEndPointSettingPS.setString(2, BasicUtility.getInstance().getCurrentTimestamp());
            updateEndPointSettingPS.setInt(3, TrialManager.getInstance().getTrialRunSettingId());
            updateEndPointSettingPS.setInt(4, endPointChangeLog.getStepNo());
            updateEndPointSettingPS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }


    private void saveEndPointChangeLogToDb(Connection conn) throws SQLException {
        PreparedStatement saveEndPointChangeLogPS = null;
        try {
            if (endPointSettings == null) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
            saveEndPointChangeLogPS = conn.prepareStatement(DbConstant.SAVE_END_POINT_CHANGE_LOG_QUERY);
            saveEndPointChangeLogPS.setLong(1, endPointChangeLog.getTrialLogId());
            saveEndPointChangeLogPS.setString(2, endPointChangeLog.getEndPointType());
            saveEndPointChangeLogPS.setDouble(3, endPointChangeLog.getOldEndPointValue());
            saveEndPointChangeLogPS.setDouble(4, endPointChangeLog.getNewEndPointValue());
            saveEndPointChangeLogPS.setInt(5, endPointChangeLog.getStepNo());
            saveEndPointChangeLogPS.setInt(6, TrialManager.getInstance().getTrialRunSettingId());
            saveEndPointChangeLogPS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }

    public void updateModePropEndPoint() {
        int totalEndPoints = endPointSettings.length;
        int loopCount = 0;
        while (loopCount < totalEndPoints) {
            DeviceManager.getInstance()
                    .getModeProp()
                    .getRunModes()[loopCount]
                    .setEndPointValue(endPointSettings[loopCount].getEndPointValue());
            loopCount++;
        }
        sLog.d(this, DeviceManager.getInstance().getModeProp());
    }

	public JSONArray getEndPointSettingFromDb() throws SQLException {
		Connection conn = null;
		PreparedStatement getEndPointSettingPS = null;
		ResultSet getEndPointSettingRS = null;
		JSONArray endPointJsonArray = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getEndPointSettingPS = conn.prepareStatement(DbConstant.GET_END_POINT_SETTING_QUERY);
			getEndPointSettingPS.setInt(1, TrialManager.getInstance().getTrialRunSettingId());
			getEndPointSettingRS = getEndPointSettingPS.executeQuery();
			endPointJsonArray = JsonMapper.getJSONFromResultSet(getEndPointSettingRS);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getEndPointSettingRS, getEndPointSettingPS, conn);
		}
		return endPointJsonArray;
	}

	public JSONArray getEndPointSettingHistorical(int trialRunSettingId) throws SQLException {
		Connection conn = null;
		PreparedStatement getEndPointSettingPS = null;
		ResultSet getEndPointSettingRS = null;
		JSONArray endPointJsonArray = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getEndPointSettingPS = conn.prepareStatement(DbConstant.GET_END_POINT_SETTING_QUERY);
			getEndPointSettingPS.setInt(1, trialRunSettingId);
			getEndPointSettingRS = getEndPointSettingPS.executeQuery();
			endPointJsonArray = JsonMapper.getJSONFromResultSet(getEndPointSettingRS);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getEndPointSettingRS, getEndPointSettingPS, conn);
		}
		return endPointJsonArray;
	}

	public JSONArray getEndPointChangeLog(int trialRunSettingId) throws SQLException {
		Connection conn = null;
		PreparedStatement getEndPointSettingPS = null;
		ResultSet getEndPointSettingRS = null;
		JSONArray endPointJsonArray = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getEndPointSettingPS = conn.prepareStatement(
					"select tl.created_on, epcl.new_end_point_value, epcl.step_no, epcl.end_point_type, um.username from trial_log tl join end_point_change_log epcl on epcl.trial_log_id = tl.trial_log_id join user_master um on um.user_id = tl.user_id WHERE tl.action = \"\" and tl.trial_run_setting_id = ?");
			getEndPointSettingPS.setInt(1, trialRunSettingId);
			getEndPointSettingRS = getEndPointSettingPS.executeQuery();
			endPointJsonArray = JsonMapper.getJSONFromResultSet(getEndPointSettingRS);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(getEndPointSettingRS, getEndPointSettingPS, conn);
		}
		return endPointJsonArray;
	}

}
