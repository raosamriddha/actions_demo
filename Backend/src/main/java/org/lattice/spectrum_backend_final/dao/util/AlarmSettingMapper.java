package org.lattice.spectrum_backend_final.dao.util;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlarmSettingMapper {

    public static PreparedStatement saveAlarmSettingStatementMapper(JSONObject alarmJson, PreparedStatement saveAlarmPS, int trialRunSettingId) throws SQLException {
        saveAlarmPS.setInt(1, trialRunSettingId);
        saveAlarmPS.setString(2, alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_ALARM));
        saveAlarmPS.setString(3, alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_STOP));
        saveAlarmPS.setString(4, alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_ALARM));
        saveAlarmPS.setString(5, alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_STOP));
        saveAlarmPS.setString(6, alarmJson.getString(ApiConstant.FEED_WT_LOW_ALARM));
        saveAlarmPS.setString(7, alarmJson.getString(ApiConstant.FEED_WT_LOW_STOP));
        saveAlarmPS.setString(8, alarmJson.getString(ApiConstant.FEED_WT_HIGH_ALARM));
        saveAlarmPS.setString(9, alarmJson.getString(ApiConstant.FEED_WT_HIGH_STOP));
        saveAlarmPS.setString(10, alarmJson.getString(ApiConstant.PERM_WT_LOW_ALARM));
        saveAlarmPS.setString(11, alarmJson.getString(ApiConstant.PERM_WT_LOW_STOP));
        saveAlarmPS.setString(12, alarmJson.getString(ApiConstant.PERM_WT_HIGH_ALARM));
        saveAlarmPS.setString(13, alarmJson.getString(ApiConstant.PERM_WT_HIGH_STOP));
        saveAlarmPS.setString(14, alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_ALARM));
        saveAlarmPS.setString(15, alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_STOP));
        saveAlarmPS.setString(16, alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_ALARM));
        saveAlarmPS.setString(17, alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_STOP));
        saveAlarmPS.setString(18, alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_ALARM));
        saveAlarmPS.setString(19, alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_STOP));
        saveAlarmPS.setString(20, alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_ALARM));
        saveAlarmPS.setString(21, alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_STOP));
        saveAlarmPS.setString(22, BasicUtility.getInstance().getCurrentTimestamp());
        saveAlarmPS.setString(23, BasicUtility.getInstance().getCurrentTimestamp());
        saveAlarmPS.setInt(24, alarmJson.getInt(ApiConstant.KONDUIT_CH_1_TYPE));
        saveAlarmPS.setInt(25, alarmJson.getInt(ApiConstant.KONDUIT_CH_2_TYPE));
        saveAlarmPS.setString(26, alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_ALARM));
        saveAlarmPS.setString(27, alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_STOP));

        return saveAlarmPS;
    }


    public PreparedStatement updateAlarmSettingStatementMapper(JSONObject alarmJson, PreparedStatement updateAlarmPS) throws SQLException {
        updateAlarmPS.setString(1, alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_ALARM));
        updateAlarmPS.setString(2, alarmJson.getString(ApiConstant.FEED_PRESSURE_LOW_STOP));
        updateAlarmPS.setString(3, alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_ALARM));
        updateAlarmPS.setString(4, alarmJson.getString(ApiConstant.FEED_PRESSURE_HIGH_STOP));
        updateAlarmPS.setString(5, alarmJson.getString(ApiConstant.FEED_WT_LOW_ALARM));
        updateAlarmPS.setString(6, alarmJson.getString(ApiConstant.FEED_WT_LOW_STOP));
        updateAlarmPS.setString(7, alarmJson.getString(ApiConstant.FEED_WT_HIGH_ALARM));
        updateAlarmPS.setString(8, alarmJson.getString(ApiConstant.FEED_WT_HIGH_STOP));
        updateAlarmPS.setString(9, alarmJson.getString(ApiConstant.PERM_WT_LOW_ALARM));
        updateAlarmPS.setString(10, alarmJson.getString(ApiConstant.PERM_WT_LOW_STOP));
        updateAlarmPS.setString(11, alarmJson.getString(ApiConstant.PERM_WT_HIGH_ALARM));
        updateAlarmPS.setString(12, alarmJson.getString(ApiConstant.PERM_WT_HIGH_STOP));
        updateAlarmPS.setString(13, alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_ALARM));
        updateAlarmPS.setString(14, alarmJson.getString(ApiConstant.PERM_PRESSURE_LOW_STOP));
        updateAlarmPS.setString(15, alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_ALARM));
        updateAlarmPS.setString(16, alarmJson.getString(ApiConstant.PERM_PRESSURE_HIGH_STOP));
        updateAlarmPS.setString(17, alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_ALARM));
        updateAlarmPS.setString(18, alarmJson.getString(ApiConstant.KONDUIT_CH_1_HIGH_STOP));
        updateAlarmPS.setString(19, alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_ALARM));
        updateAlarmPS.setString(20, alarmJson.getString(ApiConstant.KONDUIT_CH_2_HIGH_STOP));
        updateAlarmPS.setString(21, BasicUtility.getInstance().getCurrentTimestamp());
        updateAlarmPS.setInt(22, alarmJson.getInt(ApiConstant.KONDUIT_CH_1_TYPE));
        updateAlarmPS.setInt(23, alarmJson.getInt(ApiConstant.KONDUIT_CH_2_TYPE));
        updateAlarmPS.setString(24, alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_ALARM));
        updateAlarmPS.setString(25, alarmJson.getString(ApiConstant.PERMEATE_TOTAL_HIGH_STOP));
        updateAlarmPS.setString(26, alarmJson.getString(ApiConstant.TRIAL_ID));

        return updateAlarmPS;
    }

    public PreparedStatement saveAlarmHistoryStatementMapper(JSONObject alarmLiveJson, PreparedStatement saveAlarmHistoryPS) throws SQLException {

        saveAlarmHistoryPS.setInt(1, alarmLiveJson.getInt(ApiConstant.TRIAL_RUN_SETTING_ID));
        saveAlarmHistoryPS.setString(2, alarmLiveJson.getString(ApiConstant.MODE_NAME));
        saveAlarmHistoryPS.setString(3, alarmLiveJson.getString(ApiConstant.ALARM_STOP_DESCRIPTION));
        saveAlarmHistoryPS.setString(4, alarmLiveJson.getString(ApiConstant.ALARM_STOP_VALUE));
        saveAlarmHistoryPS.setString(5, alarmLiveJson.getString(ApiConstant.NATURE));
        saveAlarmHistoryPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());
        saveAlarmHistoryPS.setString(7, BasicUtility.getInstance().getCurrentTimestamp());

        return saveAlarmHistoryPS;
    }

}
