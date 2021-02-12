package org.lattice.spectrum_backend_final.dao.util;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.TrialBuffer;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BufferMapper {

    public PreparedStatement bufferToStatementMapper(PreparedStatement liveDataPS, TrialBuffer[] trialBuffer, int localIndex) throws SQLException {

        liveDataPS.setDouble(1, trialBuffer[localIndex].getFeedPressure());
        liveDataPS.setDouble(2, trialBuffer[localIndex].getPermeatePressure());
        liveDataPS.setDouble(3, trialBuffer[localIndex].getTmp());
        liveDataPS.setDouble(4, trialBuffer[localIndex].getDeltaP());
        liveDataPS.setInt(5, trialBuffer[localIndex].getMainPumpSpeed());
        liveDataPS.setDouble(6, trialBuffer[localIndex].getMainPumpFlowRate());
        liveDataPS.setDouble(7, trialBuffer[localIndex].getAuxPump1FlowRate());
        liveDataPS.setDouble(8, trialBuffer[localIndex].getAuxPump2FlowRate());
        liveDataPS.setObject(9, trialBuffer[localIndex].getConductivity1());
        liveDataPS.setObject(10, trialBuffer[localIndex].getConductivity2());
        liveDataPS.setObject(11, trialBuffer[localIndex].getTemperature1());
        liveDataPS.setObject(12, trialBuffer[localIndex].getTemperature2());
        liveDataPS.setObject(13, trialBuffer[localIndex].getKonduitCh1());
        liveDataPS.setObject(14, trialBuffer[localIndex].getKonduitCh2());
        liveDataPS.setInt(15, trialBuffer[localIndex].getKonduitCh1Type());
        liveDataPS.setInt(16, trialBuffer[localIndex].getKonduitCh2Type());
        liveDataPS.setDouble(17, trialBuffer[localIndex].getFeedScale());
        liveDataPS.setObject(18, trialBuffer[localIndex].getmPermeate());
        liveDataPS.setObject(19, trialBuffer[localIndex].getTotalPermWt());
        liveDataPS.setDouble(20, trialBuffer[localIndex].getDiaFiltrationVol1());
        liveDataPS.setDouble(21, trialBuffer[localIndex].getDiaFiltrationVol2());
        liveDataPS.setDouble(22, trialBuffer[localIndex].getConcentrationFactor());
        liveDataPS.setLong(23, trialBuffer[localIndex].getTimestamp());
        liveDataPS.setDouble(24, trialBuffer[localIndex].getFeedFlowRate());
        liveDataPS.setDouble(25, trialBuffer[localIndex].getPermeateFlowRate());
        liveDataPS.setDouble(26, trialBuffer[localIndex].getRetentateFlowRate());
        liveDataPS.setDouble(27, trialBuffer[localIndex].getVt());
        liveDataPS.setString(28, trialBuffer[localIndex].getShear());
        liveDataPS.setString(29, trialBuffer[localIndex].getAbv1());
        liveDataPS.setString(30, trialBuffer[localIndex].getAbv2());
        liveDataPS.setDouble(31, trialBuffer[localIndex].getFlux());
        liveDataPS.setInt(32, trialBuffer[localIndex].getMainPumpSpeed());
        liveDataPS.setString(33, "");

        if (DeviceManager.getInstance().getModeType() == 0) {
            liveDataPS.setInt(34, DeviceManager.getInstance().getRecipeJson().getInt(ApiConstant.TRIAL_MASTER_ID));
        } else if (DeviceManager.getInstance().getModeType() == 1) {
            liveDataPS.setInt(34, -1);
        }

        liveDataPS.setString(35, trialBuffer[localIndex].getTcf());
        liveDataPS.setString(36, trialBuffer[localIndex].getWaterFlux20Degree());
        liveDataPS.setString(37, trialBuffer[localIndex].getWaterPerm());
        liveDataPS.setString(38, trialBuffer[localIndex].getNwp());
        liveDataPS.setInt(39, TrialManager.getInstance().getTrialRunSettingId());
        liveDataPS.setDouble(40, trialBuffer[localIndex].getRetentatePressure());
        liveDataPS.setInt(41, trialBuffer[localIndex].getIsPaused());
        liveDataPS.setInt(42, trialBuffer[localIndex].getFlowRateCount());
        liveDataPS.setObject(43, trialBuffer[localIndex].getPermeateTotal());
        liveDataPS.setObject(44, trialBuffer[localIndex].getPermeateTotalWithHoldup());
        liveDataPS.setObject(45, trialBuffer[localIndex].getDuration());

        return liveDataPS;

    }

    public TrialBuffer jsonToBufferMapper(JSONObject liveDataJson, TrialBuffer[] trialBuffer, int index) throws Exception {

        trialBuffer[index].setFeedScale(liveDataJson.getDouble(ApiConstant.FEED_SCALE));
        trialBuffer[index].setAuxPump1FlowRate(liveDataJson.getDouble(ApiConstant.AUX_PUMP_1_FLOW_RATE));
        trialBuffer[index].setAuxPump2FlowRate(liveDataJson.getDouble(ApiConstant.AUX_PUMP_2_FLOW_RATE));
        trialBuffer[index].setRetentateFlowRate(liveDataJson.getDouble(ApiConstant.RETENTATE_FLOW_RATE));
        trialBuffer[index].setMainPumpSpeed(liveDataJson.getInt(ApiConstant.MAIN_PUMP_SPEED));
        trialBuffer[index].setMainPumpFlowRate(liveDataJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE));
        trialBuffer[index].setTimestamp(
                BasicUtility.getInstance()
                        .convertDateToUnixGraph(liveDataJson.getString(ApiConstant.TIMESTAMP)) / 1000
        );
        trialBuffer[index].setFeedPressure(liveDataJson.getDouble(ApiConstant.FEED_PRESSURE));

        trialBuffer[index].setmPermeate(liveDataJson.get(
                ApiConstant.M_PERMEATE).toString().equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.M_PERMEATE));

        trialBuffer[index].setTotalPermWt(liveDataJson.get(
                ApiConstant.TOTAL_PERM_WEIGHT).toString().equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.TOTAL_PERM_WEIGHT));

        trialBuffer[index].setConductivity1(
                liveDataJson.getString(ApiConstant.CONDUCTIVITY_1).equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.CONDUCTIVITY_1)
        );

        trialBuffer[index].setConductivity2(liveDataJson.getString(
                ApiConstant.CONDUCTIVITY_2).equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.CONDUCTIVITY_2));

        trialBuffer[index].setTemperature1(liveDataJson.getString(
                ApiConstant.TEMPERATURE_1).equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.TEMPERATURE_1));

        trialBuffer[index].setTemperature2(liveDataJson.getString(
                ApiConstant.TEMPERATURE_2).equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.TEMPERATURE_2));

        trialBuffer[index].setKonduitCh1(liveDataJson.getString(
                ApiConstant.KONDUIT_CH_1).equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.KONDUIT_CH_1));

        trialBuffer[index].setKonduitCh2(liveDataJson.getString(
                ApiConstant.KONDUIT_CH_2).equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.KONDUIT_CH_2));

        trialBuffer[index].setKonduitCh1Type(liveDataJson.getInt(ApiConstant.KONDUIT_CH_1_TYPE));
        trialBuffer[index].setKonduitCh2Type(liveDataJson.getInt(ApiConstant.KONDUIT_CH_2_TYPE));
        trialBuffer[index].setDeltaP(liveDataJson.getDouble(ApiConstant.DELTA_P));
        trialBuffer[index].setConcentrationFactor(liveDataJson.getDouble(ApiConstant.CONCENTRATION_FACT));
        trialBuffer[index].setDiaFiltrationVol1(liveDataJson.getDouble(ApiConstant.DIAFILTRATION_VOL_1));
        trialBuffer[index].setDiaFiltrationVol2(liveDataJson.getDouble(ApiConstant.DIAFILTRATION_VOL_2));
        trialBuffer[index].setFeedFlowRate(liveDataJson.getDouble(ApiConstant.FEED_FLOW_RATE));
        trialBuffer[index].setRetentatePressure(liveDataJson.getDouble(ApiConstant.RETENTATE_PRESSURE));
        trialBuffer[index].setPermeatePressure(liveDataJson.getDouble(ApiConstant.PERMEATE_PRESSURE));
        trialBuffer[index].setTmp(liveDataJson.getDouble(ApiConstant.TMP_PRESSURE));
        trialBuffer[index].setPermeateFlowRate(liveDataJson.getDouble(ApiConstant.PERMEATE_FLOW_RATE));
        trialBuffer[index].setVt(liveDataJson.getDouble(ApiConstant.VT));
        trialBuffer[index].setShear(liveDataJson.getString(ApiConstant.SHEAR));
        trialBuffer[index].setAbv1(liveDataJson.getString(ApiConstant.ABV_1));
        trialBuffer[index].setAbv2(liveDataJson.getString(ApiConstant.ABV_2));
        trialBuffer[index].setFlux(liveDataJson.getDouble(ApiConstant.FLUX));
        trialBuffer[index].setTcf(liveDataJson.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR));
        trialBuffer[index].setWaterFlux20Degree(liveDataJson.getString(ApiConstant.WATER_FLUX_20_DEGREE));
        trialBuffer[index].setWaterPerm(liveDataJson.getString(ApiConstant.WATER_PERMEABILITY));
        trialBuffer[index].setNwp(liveDataJson.getString(ApiConstant.NWP));
        trialBuffer[index].setIsPaused(0);
        trialBuffer[index].setFlowRateCount(DeviceManager.getInstance().getFlowRateCount());
        trialBuffer[index].setPermeateTotal(liveDataJson.get(
                ApiConstant.PERMEATE_TOTAL_KEY).toString().equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.PERMEATE_TOTAL_KEY));

        trialBuffer[index].setPermeateTotalWithHoldup(liveDataJson.get(
                ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP).toString().equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)
                ? null
                : liveDataJson.getDouble(ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP));
        trialBuffer[index].setDuration(liveDataJson.getString(ApiConstant.DURATION));

        return trialBuffer[index];
    }
}
