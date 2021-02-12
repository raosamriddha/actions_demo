package org.lattice.spectrum_backend_final.dao.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.TargetStepChangeLog;
import org.lattice.spectrum_backend_final.beans.TargetStepSetting;
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

public class TargetStepSettingManager {

    private static TargetStepSettingManager targetStepSettingManager = null;

    private TargetStepSetting[] targetStepSettings = null;

    private TargetStepChangeLog targetStepChangeLog = null;


    public static TargetStepSettingManager getInstance() {

        synchronized (TargetStepSettingManager.class) {
            if (targetStepSettingManager == null) {
                targetStepSettingManager = new TargetStepSettingManager();
            }
        }

        return targetStepSettingManager;
    }

    public TargetStepSetting[] getTargetStepSettings() {
        return targetStepSettings;
    }

    public void setTargetStepSettings(TargetStepSetting[] targetStepSettings) {
        this.targetStepSettings = targetStepSettings;
    }


    public void setTargetStepSettingsArray(JSONArray targetStepJsonArray) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        try {

            // Convert JSON string to Object
            targetStepSettings = mapper.readValue(targetStepJsonArray.toString(), TargetStepSetting[].class);
            sLog.d(this, targetStepSettings);


        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }


    }

    public JSONArray getTargetStepSettingsJsonArray() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JSONArray targetStepJsonArray = null;
        try {

            // Convert POJO array to JSONArray
            String arrayToJson = mapper.writeValueAsString(targetStepSettings);

            targetStepJsonArray = new JSONArray(arrayToJson);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        return targetStepJsonArray;


    }


    public void saveTargetStepSetting(JSONObject runJson, Connection conn, boolean isEditMode) throws SQLException, TrialRunException {

        PreparedStatement saveTargetStepSettingPS = null;
        int loopCount = 0;

        try {


            updateTSSettingAndSaveTSChangeLog(runJson, conn, isEditMode);

            if (targetStepSettings == null) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }


            // only executes when save trial call occurs [ not in edit mode ].
            if (!isEditMode) {

                saveTargetStepSettingPS = conn.prepareStatement(DbConstant.SAVE_TARGET_STEP_SETTING_DETAILS);

                while (loopCount < targetStepSettings.length) {
                    saveTargetStepSettingPS.setInt(1, TrialManager.getInstance().getTrialRunSettingId());
                    saveTargetStepSettingPS.setString(2, targetStepSettings[loopCount].getTmp());
                    saveTargetStepSettingPS.setString(3, targetStepSettings[loopCount].getDuration());
                    saveTargetStepSettingPS.setString(4, targetStepSettings[loopCount].getPermeateWeight());
                    saveTargetStepSettingPS.setString(5, targetStepSettings[loopCount].getPermeateTotal());
                    saveTargetStepSettingPS.setInt(6, (loopCount +1));
                    saveTargetStepSettingPS.setString(7, BasicUtility.getInstance().getCurrentTimestamp());
                    saveTargetStepSettingPS.setString(8, BasicUtility.getInstance().getCurrentTimestamp());

                    saveTargetStepSettingPS.addBatch();

                    loopCount++;
                }

                saveTargetStepSettingPS.executeBatch();
            }

//            conn.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }


    }


    public void updateTSSettingAndSaveTSChangeLog(JSONObject runTargetStepJson, Connection conn, boolean isEditMode) throws SQLException, TrialRunException {

        int loopCount = 0;
        long trialLogId;

        String modeName = runTargetStepJson.getString(ApiConstant.MODE_NAME);

        while (loopCount < runTargetStepJson.getInt(ApiConstant.TOTAL_END_POINT)) {

            String tmp = runTargetStepJson.getString(
                    ApiConstant.TMP
                    + ApiConstant.UNDER_SCORE
                    +(loopCount + 1)
            );
            String duration = runTargetStepJson.getString(
                    ApiConstant.DURATION
                            + ApiConstant.UNDER_SCORE
                            +(loopCount + 1)
            );
            String permWt = runTargetStepJson.getString(
                    ApiConstant.PERM_WT
                            + ApiConstant.UNDER_SCORE
                            +(loopCount + 1)
            );
            String permTotal = runTargetStepJson.getString(
                    ApiConstant.PERMEATE_TOTAL_KEY
                            + ApiConstant.UNDER_SCORE
                            +(loopCount + 1)
            );

            sLog.d("", targetStepSettings);

            // check weather targetStep is changed or not
            if (isTargetStepChanged(modeName, tmp, duration, permWt, permTotal, loopCount)) {

                if (TrialManager.getInstance().getTrialRunSettingId() == 0) {
                    throw new TrialRunException(ApiConstant.TRIAL_RUN_SETTING_ID_EXCEPTION);
                }

                trialLogId = LogManager.getInstance().insertTrialLogGetLogId(
                        BasicUtility.getInstance().getUserId(runTargetStepJson, conn),
                        TrialManager.getInstance().getTrialRunSettingId(),
                        ApiConstant.BLANK_QUOTE,
                        ApiConstant.LOG_TYPE_TRIAL,
                        conn
                );

                if (trialLogId == 0) {
                    throw new RuntimeException(ApiConstant.TRIAL_LOG_ID_EXCEPTION);
                }


                // if changed then update targetStepChangeLog and make a log

                targetStepChangeLog = new TargetStepChangeLog();
                targetStepChangeLog.setOldTmp(targetStepSettings[loopCount].getTmp());
                targetStepChangeLog.setNewTmp(tmp);
                targetStepChangeLog.setOldDuration(targetStepSettings[loopCount].getDuration());
                targetStepChangeLog.setNewDuration(duration);
                targetStepChangeLog.setOldPermeateWeight(targetStepSettings[loopCount].getPermeateWeight());
                targetStepChangeLog.setNewPermeateWeight(permWt);
                targetStepChangeLog.setOldPermeateTotal(targetStepSettings[loopCount].getPermeateTotal());
                targetStepChangeLog.setNewPermeateTotal(permTotal);
                targetStepChangeLog.setStepNo(loopCount + 1);
                targetStepChangeLog.setTrialLogId(trialLogId);

                // saving targetStepChangeLog to db
                saveTargetStepChangeLogToDb(conn);

                if (isEditMode) {

                    //update targetStepSetting to db
                    updateTargetStepSetting(conn);
                }

                // update targetStepSettings with changed values
                targetStepSettings[loopCount].setTmp(tmp);
                targetStepSettings[loopCount].setDuration(duration);
                targetStepSettings[loopCount].setPermeateWeight(permWt);
                targetStepSettings[loopCount].setPermeateTotal(permTotal);

            }


            loopCount++;
            //

        }

    }


    private boolean isTargetStepChanged(String modeName, String tmp, String duration, String permWt, String permTotal, int loopCount){
        if(ApiConstant.NWP.equalsIgnoreCase(modeName)){
            if (
                    Double.parseDouble(tmp) != Double.parseDouble(targetStepSettings[loopCount].getTmp()) ||
                    Double.parseDouble(duration) != Double.parseDouble(targetStepSettings[loopCount].getDuration())
            ) {
                return true;
            }

        }else if( ApiConstant.FLUSHING.equalsIgnoreCase(modeName) || ApiConstant.CLEANING.equalsIgnoreCase(modeName) ){

            if(
                    !duration.isEmpty() &&
                    Double.parseDouble(duration) != Double.parseDouble(targetStepSettings[loopCount].getDuration())
            ){
                return true;
            }else if(
                    !permWt.isEmpty() &&
                     Double.parseDouble(permWt) != Double.parseDouble(targetStepSettings[loopCount].getPermeateWeight())
            ){
                return true;
            }else if(
                    !permTotal.isEmpty() &&
                     Double.parseDouble(permTotal) != Double.parseDouble(targetStepSettings[loopCount].getPermeateTotal())
            ){
                return true;
            }
        }
        return false;
    }


    private void saveTargetStepChangeLogToDb(Connection conn) throws SQLException {
        PreparedStatement saveTargetStepChangeLogPS = null;
        try {
            if (targetStepSettings == null) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
            saveTargetStepChangeLogPS = conn.prepareStatement(DbConstant.SAVE_TARGET_STEP_CHANGE_LOG_QUERY);
            saveTargetStepChangeLogPS.setLong(1, targetStepChangeLog.getTrialLogId());
            saveTargetStepChangeLogPS.setString(2, targetStepChangeLog.getOldTmp());
            saveTargetStepChangeLogPS.setString(3, targetStepChangeLog.getNewTmp());
            saveTargetStepChangeLogPS.setString(4, targetStepChangeLog.getOldDuration());
            saveTargetStepChangeLogPS.setString(5, targetStepChangeLog.getNewDuration());
            saveTargetStepChangeLogPS.setString(6, targetStepChangeLog.getOldPermeateWeight());
            saveTargetStepChangeLogPS.setString(7, targetStepChangeLog.getNewPermeateWeight());
            saveTargetStepChangeLogPS.setString(8, targetStepChangeLog.getOldPermeateTotal());
            saveTargetStepChangeLogPS.setString(9, targetStepChangeLog.getNewPermeateTotal());
            saveTargetStepChangeLogPS.setInt(10, targetStepChangeLog.getStepNo());
            saveTargetStepChangeLogPS.setInt(11, TrialManager.getInstance().getTrialRunSettingId());
            saveTargetStepChangeLogPS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }


    private void updateTargetStepSetting(Connection conn) throws SQLException {
        PreparedStatement updateTargetStepSettingPS = null;
        try {
            if (targetStepSettings == null) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
            updateTargetStepSettingPS = conn.prepareStatement(DbConstant.UPDATE_TARGET_STEP_SETTING_DETAILS);
            updateTargetStepSettingPS.setString(1, targetStepChangeLog.getNewTmp());
            updateTargetStepSettingPS.setString(2, targetStepChangeLog.getNewDuration());
            updateTargetStepSettingPS.setString(3, targetStepChangeLog.getNewPermeateWeight());
            updateTargetStepSettingPS.setString(4, targetStepChangeLog.getNewPermeateTotal());
            updateTargetStepSettingPS.setString(5, BasicUtility.getInstance().getCurrentTimestamp());
            updateTargetStepSettingPS.setInt(6, TrialManager.getInstance().getTrialRunSettingId());
            updateTargetStepSettingPS.setInt(7, targetStepChangeLog.getStepNo());
            updateTargetStepSettingPS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }


    }

    public void updateModePropTargetStep() {

        int totalTargetStep = targetStepSettings.length;
        int loopCount = 0;
        String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);


        while (loopCount < totalTargetStep) {

            if(ApiConstant.NWP.equalsIgnoreCase(modeName)){

                DeviceManager.getInstance()
                        .getModeProp()
                        .getNonRunModes(loopCount)
                        .setOperatingPressure(Double.parseDouble(targetStepSettings[loopCount].getTmp()));

                DeviceManager.getInstance()
                        .getModeProp()
                        .getNonRunModes(loopCount)
                        .setTargetDuration(Integer.parseInt(targetStepSettings[loopCount].getDuration()));


            }else if(ApiConstant.CLEANING.equalsIgnoreCase(modeName) || ApiConstant.FLUSHING.equalsIgnoreCase(modeName)){


                if(EndPointTarget.Duration == DeviceManager.getInstance().getModeProp().getNonRunModes(loopCount).getEndPointTarget()){

                    DeviceManager.getInstance()
                            .getModeProp()
                            .getNonRunModes(loopCount)
                            .setEndPointValue(Double.parseDouble(targetStepSettings[loopCount].getDuration()));

                }else if(EndPointTarget.PermeateWeight == DeviceManager.getInstance().getModeProp().getNonRunModes(loopCount).getEndPointTarget()){
                    DeviceManager.getInstance()
                            .getModeProp()
                            .getNonRunModes(loopCount)
                            .setEndPointValue(Double.parseDouble(targetStepSettings[loopCount].getPermeateWeight()));
                }else {
                    DeviceManager.getInstance()
                            .getModeProp()
                            .getNonRunModes(loopCount)
                            .setEndPointValue(Double.parseDouble(targetStepSettings[loopCount].getPermeateTotal()));
                }
            }

            loopCount++;

        }

        sLog.d(this, DeviceManager.getInstance().getModeProp());
    }

    public JSONArray getTargetStepSettingFromDb() throws SQLException {

        Connection conn = null;
        PreparedStatement getTargetStepSettingPS = null;
        ResultSet getTargetStepSettingRS = null;
        JSONArray targetStepJsonArray = null;
        try {
            conn = DbConnectionManager.getInstance().getConnection();
            getTargetStepSettingPS = conn.prepareStatement(DbConstant.GET_TARGET_STEP_SETTING_QUERY);
            getTargetStepSettingPS.setInt(1, TrialManager.getInstance().getTrialRunSettingId());
            getTargetStepSettingRS = getTargetStepSettingPS.executeQuery();
            targetStepJsonArray = JsonMapper.getJSONFromResultSet(getTargetStepSettingRS);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(getTargetStepSettingRS, getTargetStepSettingPS, conn);
        }

        return targetStepJsonArray;

    }
    
    public JSONArray getTargetStepSettingHistorical(int trialRunSettingId) throws SQLException {

        Connection conn = null;
        PreparedStatement getTargetStepSettingPS = null;
        ResultSet getTargetStepSettingRS = null;
        JSONArray targetStepJsonArray = null;
        try {
            conn = DbConnectionManager.getInstance().getConnection();
            getTargetStepSettingPS = conn.prepareStatement(DbConstant.GET_TARGET_STEP_SETTING_QUERY);
            getTargetStepSettingPS.setInt(1, trialRunSettingId);
            getTargetStepSettingRS = getTargetStepSettingPS.executeQuery();
            targetStepJsonArray = JsonMapper.getJSONFromResultSet(getTargetStepSettingRS);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(getTargetStepSettingRS, getTargetStepSettingPS, conn);
        }

        return targetStepJsonArray;

    }
    
    public JSONArray getTargetStepChangeLog(int trialRunSettingId) throws SQLException {
		Connection conn = null;
		PreparedStatement getEndPointSettingPS = null;
		ResultSet getEndPointSettingRS = null;
		JSONArray endPointJsonArray = null;
		try {
			conn = DbConnectionManager.getInstance().getConnection();
			getEndPointSettingPS = conn.prepareStatement(
					"select tl.created_on, tscl.*, um.username from trial_log tl join target_step_change_log tscl on tscl.trial_log_id = tl.trial_log_id join user_master um on um.user_id = tl.user_id WHERE tl.action = \"\" and tl.trial_run_setting_id = ?");
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
