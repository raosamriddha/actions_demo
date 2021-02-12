package org.lattice.spectrum_backend_final.dao.manager;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.apache.commons.math3.analysis.function.StepFunction;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.SystemSettingsMapper;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class SystemSettingsManager {

    private static final Logger log = LoggerFactory.getLogger(SystemSettingsManager.class);

    private SystemSettingsMapper settingsMapper = new SystemSettingsMapper();

    public int insertSystemSettings(SystemSetting systemSetting) throws Exception {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement insertSystemSettingStmt = null;
        JSONObject systemSettingJson = new JSONObject(systemSetting);
        String notes = ApiConstant.BLANK_QUOTE;
        sLog.d(this, systemSettingJson);
        try {

            con.setAutoCommit(false);
            if (DatabaseManager.getInstance().isDigitalSignatureManagerVerified(systemSettingJson)) {

                if(systemSettingJson.has(ApiConstant.DIGITAL_NOTES)){
                    notes = systemSettingJson.getString(ApiConstant.DIGITAL_NOTES);
                }

                insertSystemSettingStmt = con.prepareStatement(QueryConstant.INSERT_SYSTEM_SETTINGS, Statement.RETURN_GENERATED_KEYS);
                log.info(QueryConstant.INSERT_SYSTEM_SETTINGS);
                int affectedRows = settingsMapper.insertSystemSettingsStatementMapper(systemSetting, insertSystemSettingStmt)
                        .executeUpdate();
                if (affectedRows == 0) {
                    return 0;
                }
                ResultSet generatedKeys = insertSystemSettingStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    con.commit();
                    systemSetting.setSystemId(generatedKeys.getInt(1));
                    // saving trial log

                    LogManager.getInstance().insertSystemLog(
                            BasicUtility.getInstance().getUserId(systemSettingJson),
                            0,
                            ApiConstant.SET_SYSTEM_SETTING_ACTION,
                            notes
                    );
                } else {
                    return 0;
                }

            } else {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }
            return systemSetting.getSystemId();

        } catch (InvalidCredentialException ex) {
            throw new InvalidCredentialException(ex.getMessage());
        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (SQLException sqlEx) {
                log.error(ApiConstant.EXCEPTION, sqlEx);
            }
            log.error(ApiConstant.EXCEPTION, ex);
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, insertSystemSettingStmt, con);
        }
    }

    public SystemSetting fetchSystemSettings() throws SQLException {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement fetchSystemSettingStmt = null;
        ResultSet resultSet = null;
        try {

            fetchSystemSettingStmt = con.prepareStatement(QueryConstant.FETCH_SYSTEM_SETTINGS);
            log.info(QueryConstant.FETCH_SYSTEM_SETTINGS);
            resultSet = fetchSystemSettingStmt.executeQuery();
            if (resultSet.next()) {
                return settingsMapper.fetchSystemSettingsResultMapper(resultSet);
            }
            return null;
        } catch (Exception ex) {
            log.error(ApiConstant.EXCEPTION, ex);
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, fetchSystemSettingStmt, con);
        }
    }

    public boolean isSystemSettingExists(int systemId) {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement fetchSystemSettingsStmt = null;
        ResultSet resultSet = null;
        try {
            fetchSystemSettingsStmt = con.prepareStatement(QueryConstant.CHECK_SYSTEM_SETTING_EXISTS);
            fetchSystemSettingsStmt.setInt(1, systemId);
            log.info(QueryConstant.CHECK_SYSTEM_SETTING_EXISTS);
            resultSet = fetchSystemSettingsStmt.executeQuery();
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return (count > 0) ? true : false;
        } catch (Exception ex) {
            log.error(ApiConstant.EXCEPTION, ex);
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, fetchSystemSettingsStmt, con);
        }
    }

//    public int updateSystemSettings(SystemSetting systemSetting) {
//        final Connection con = DbConnectionManager.getInstance().getConnection();
//        PreparedStatement updateSystemSettingStmt = null;
//        int affectedRows = 0;
//        try {
//            con.setAutoCommit(false);
//            updateSystemSettingStmt = con.prepareStatement(QueryConstant.UPDATE_SYSTEM_SETTINGS);
//            log.info(QueryConstant.UPDATE_SYSTEM_SETTINGS);
//            affectedRows = settingsMapper.updateSystemSettingsStatementMapper(systemSetting, updateSystemSettingStmt)
//                    .executeUpdate();
//            if (affectedRows < 1) {
//                return 0;
//            }
//            con.commit();
//        } catch (Exception ex) {
//            try {
//                con.rollback();
//            } catch (SQLException sqlEx) {
//                log.error(ApiConstant.EXCEPTION, sqlEx.getMessage());
//            }
//            log.error(ApiConstant.EXCEPTION, ex);
//            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
//        } finally {
//            DbConnectionManager.closeDBConnection(null, updateSystemSettingStmt, con);
//        }
//        return affectedRows;
//    }

    public void inactivePreviousSettings(int systemId) {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement inactiveSystemSettingStmt = null;
        int affectedRows = 0;
        try {
            con.setAutoCommit(false);
            inactiveSystemSettingStmt = con.prepareStatement(QueryConstant.INACTIVE_PREVIOUS_SYSTEM_SETTINGS);
            inactiveSystemSettingStmt.setInt(1, systemId);
            log.info(QueryConstant.INACTIVE_PREVIOUS_SYSTEM_SETTINGS);
            affectedRows = inactiveSystemSettingStmt.executeUpdate();
            if (affectedRows < 1) {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
            con.commit();
        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (SQLException sqlEx) {
                log.error(ApiConstant.EXCEPTION, sqlEx.getMessage());
            }
            log.error(ApiConstant.EXCEPTION, ex);
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, inactiveSystemSettingStmt, con);
        }
    }

}
