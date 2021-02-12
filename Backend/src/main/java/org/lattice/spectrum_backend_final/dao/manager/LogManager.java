package org.lattice.spectrum_backend_final.dao.manager;


import org.json.JSONArray;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class LogManager {

    private static LogManager logManager;

    public LogManager() {
    }

    public static LogManager getInstance() {

        synchronized (LogManager.class) {
            if (logManager == null) {
                logManager = new LogManager();
            }
        }

        return logManager;
    }


    /**
     * @param userId
     * @param trialRunSettingId
     * @param action
     * @param logType
     * @return
     * @throws Exception
     */
    public String insertTrialLog(int userId, int trialRunSettingId, String action, int logType) throws Exception {

        Connection conn = null;
        PreparedStatement insertTrialLogPS = null;
        String createdOn = BasicUtility.getInstance().getCurrentTimestamp();

        try {
            conn = DbConnectionManager.getInstance().getConnection();

            insertTrialLogPS = conn.prepareStatement(DbConstant.ADD_TRIAL_LOG_QUERY);
            insertTrialLogPS.setInt(1, userId);
            insertTrialLogPS.setInt(2, trialRunSettingId);
            insertTrialLogPS.setString(3, action);
            insertTrialLogPS.setInt(4, logType);
            insertTrialLogPS.setString(5, createdOn);
            insertTrialLogPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());

            insertTrialLogPS.executeUpdate();


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, insertTrialLogPS, conn);
        }


        return createdOn;
    }

    public String insertSystemLog(int userId, int trialRunSettingId, String action, String notes) throws Exception {

        Connection conn = null;
        PreparedStatement insertTrialLogPS = null;
        String createdOn = BasicUtility.getInstance().getCurrentTimestamp();

        try {
            conn = DbConnectionManager.getInstance().getConnection();

            insertTrialLogPS = conn.prepareStatement(DbConstant.ADD_SYSTEM_LOG_QUERY);
            insertTrialLogPS.setInt(1, userId);
            insertTrialLogPS.setInt(2, trialRunSettingId);
            insertTrialLogPS.setString(3, action);
            insertTrialLogPS.setInt(4, ApiConstant.LOG_TYPE_SYSTEM);
            insertTrialLogPS.setString(5, createdOn);
            insertTrialLogPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());
            insertTrialLogPS.setString(7, notes);

            insertTrialLogPS.executeUpdate();


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, insertTrialLogPS, conn);
        }


        return createdOn;
    }


    public long insertTrialLogGetLogId(int userId, int trialRunSettingId, String action, int logType, Connection conn) throws SQLException {

//        Connection conn = null;
        PreparedStatement insertTrialLogPS = null;
        ResultSet insertTrialLogRS = null;
        String createdOn = BasicUtility.getInstance().getCurrentTimestamp();
        long trialLogId = 0;

        try {
//            conn = DbConnectionManager.getInstance().getConnection();

            insertTrialLogPS = conn.prepareStatement(DbConstant.ADD_TRIAL_LOG_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertTrialLogPS.setInt(1, userId);
            insertTrialLogPS.setInt(2, trialRunSettingId);
            insertTrialLogPS.setString(3, action);
            insertTrialLogPS.setInt(4, logType);
            insertTrialLogPS.setString(5, createdOn);
            insertTrialLogPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());

            insertTrialLogPS.executeUpdate();

            insertTrialLogRS = insertTrialLogPS.getGeneratedKeys();

            if (insertTrialLogRS.next()) {
                trialLogId = insertTrialLogRS.getInt(1);
            } else {
                throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }


        return trialLogId;
    }

    public JSONArray getSystemLog(int logType) {

        JSONArray systemLogJsonArray = null;
        PreparedStatement getSystemLogPS;
        ResultSet getSystemLogRS;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            getSystemLogPS = conn.prepareStatement(DbConstant.GET_ALL_SYSTEM_LOG);
            getSystemLogPS.setInt(1, logType);

            getSystemLogRS = getSystemLogPS.executeQuery();

            systemLogJsonArray = JsonMapper.getJSONFromResultSet(getSystemLogRS);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemLogJsonArray;
    }

    /**
     * Delete auto generated log file older than 28 days.
     * @throws IOException
     * @throws ParseException
     */
    public void deleteAutoGeneratedLogFiles() throws IOException, ParseException {
        File files = new File(ApiConstant.AUTO_GENERATED_LOG_PATH);
        System.out.println("Now will search folders and delete files,");
        for (File file : files.listFiles()) {
            String name = file.getName();
            DateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");
            long eligibleForDeletion = System.currentTimeMillis() -
                    (ApiConstant.AUTO_GENERATED_LOG_EXPIRY_DAY * 24 * 60 * 60 * 1000);
            Date expiryDate = new Date(eligibleForDeletion);
            Date modifiedDate = new Date(file.lastModified());
            String modifiedDateString = dtf.format(modifiedDate);
            boolean isExpired = expiryDate.getTime() > modifiedDate.getTime();
            if (isExpired) {
                BasicUtility.systemPrint("", "file name: " + name + " : modify date : " + modifiedDateString);
                file.delete();
            }
        }
    }


}
