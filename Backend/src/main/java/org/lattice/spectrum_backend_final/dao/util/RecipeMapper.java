package org.lattice.spectrum_backend_final.dao.util;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import sun.security.x509.AVA;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecipeMapper {

    public PreparedStatement insertTrialMasterSetting(PreparedStatement trialMasterPS, JSONObject recipeJson) throws SQLException {
        trialMasterPS.setString(1, recipeJson.getString(ApiConstant.RECIPE_NAME));
        trialMasterPS.setString(2, recipeJson.getString(ApiConstant.TRIAL_TYPE));
        trialMasterPS.setString(3, BasicUtility.getInstance().getCurrentTimestamp());
        trialMasterPS.setString(4, BasicUtility.getInstance().getCurrentTimestamp());
        trialMasterPS.setString(5, DbConnectionManager.getInstance().getTokenManager().getUsername());
        return trialMasterPS;
    }

    public PreparedStatement insertHollowFiberRecipeSetting(PreparedStatement filterPS, JSONObject recipeJson, int trialMasterId) throws SQLException{
        filterPS.setString(1, recipeJson.getString(ApiConstant.FILTER_TYPE));
        filterPS.setString(2, recipeJson.getString(ApiConstant.PART_NO));
        filterPS.setString(3, recipeJson.getString(ApiConstant.FIBER_COUNT));
        filterPS.setString(4, recipeJson.getString(ApiConstant.FIBER_ID));
        filterPS.setString(5, recipeJson.getString(ApiConstant.SURF_AREA));
        filterPS.setString(6, recipeJson.getString(ApiConstant.ECS));
        filterPS.setString(7, recipeJson.getString(ApiConstant.PERMEATE_TUBE_SIZE));
        filterPS.setString(8, recipeJson.getString(ApiConstant.TUBE_LENGTH));
        filterPS.setString(9, recipeJson.getString(ApiConstant.PERM_TUBE_HOLDUP));
        filterPS.setDouble(10, recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));
        filterPS.setString(11, recipeJson.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
        filterPS.setString(12, recipeJson.getString(ApiConstant.NO_OF_CASSETTE));
        filterPS.setString(13, "" + recipeJson.getBoolean(ApiConstant.IS_GENERIC));
        filterPS.setInt(14, trialMasterId);
        filterPS.setString(15, recipeJson.getString(ApiConstant.FILTER_MODEL_NAME));
        filterPS.setString(16, recipeJson.getString(ApiConstant.FILTER_MANUFACTURER_NAME));
        return filterPS;
    }

    public PreparedStatement insertCassetteRecipeSetting(PreparedStatement filterPS, JSONObject recipeJson, int trialMasterId) throws SQLException{
        filterPS.setString(1, recipeJson.getString(ApiConstant.FILTER_TYPE));
        filterPS.setString(2, recipeJson.getString(ApiConstant.PART_NO));
        filterPS.setString(3, recipeJson.getString(ApiConstant.SURF_AREA));
        filterPS.setString(4, recipeJson.getString(ApiConstant.ECS));
        filterPS.setString(5, recipeJson.getString(ApiConstant.PERMEATE_TUBE_SIZE));
        filterPS.setString(6, recipeJson.getString(ApiConstant.TUBE_LENGTH));
        filterPS.setString(7, recipeJson.getString(ApiConstant.PERM_TUBE_HOLDUP));
        filterPS.setDouble(8, recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));
        filterPS.setString(9, recipeJson.getString(ApiConstant.NO_OF_CASSETTE));
        filterPS.setString(10, recipeJson.getString(ApiConstant.WIDTH));
        filterPS.setString(11, recipeJson.getString(ApiConstant.HEIGHT));
        filterPS.setString(12, recipeJson.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
        filterPS.setString(13, recipeJson.getString(ApiConstant.NO_OF_CHANNEL));
        filterPS.setString(14, recipeJson.getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO));
        filterPS.setString(15, "" + recipeJson.getBoolean(ApiConstant.IS_GENERIC));
        filterPS.setInt(16, trialMasterId);
        filterPS.setString(17, recipeJson.getString(ApiConstant.FILTER_MODEL_NAME));
        filterPS.setString(18, recipeJson.getString(ApiConstant.FILTER_MANUFACTURER_NAME));
        return filterPS;
    }

    public PreparedStatement insertPumpRecipeSetting(PreparedStatement pumpMasterPS, JSONObject recipeJson, int trialMasterId)throws SQLException{
        pumpMasterPS.setInt(1, recipeJson.getInt(ApiConstant.HEAD_COUNT));
        pumpMasterPS.setString(2, recipeJson.getString(ApiConstant.PUMP_NAME));
        pumpMasterPS.setString(3, ApiConstant.MAIN_PUMP);
        pumpMasterPS.setString(4, recipeJson.getString(ApiConstant.DIRECTION));
        pumpMasterPS.setString(5, recipeJson.getString(ApiConstant.FLOW_CONTROL));
        pumpMasterPS.setString(6, recipeJson.getString(ApiConstant.PUMP_TUBING_SIZE));
        pumpMasterPS.setString(7, recipeJson.getString(ApiConstant.PUMP_TUBING_LOOKUP_ID));
        pumpMasterPS.setString(8, recipeJson.getString(ApiConstant.FLOW_RATE));
        pumpMasterPS.setInt(9, recipeJson.getInt(ApiConstant.RAMP_UP_TIME));
        pumpMasterPS.setInt(10, trialMasterId);
        pumpMasterPS.setString(11, recipeJson.getString(ApiConstant.DELTA_P));
        pumpMasterPS.setString(12, recipeJson.getString(ApiConstant.DELTA_P_RATE));
        pumpMasterPS.setString(13, recipeJson.getString(ApiConstant.DELTA_P_DURATION));
        pumpMasterPS.setString(14, recipeJson.getString(ApiConstant.PUMP_RPM));
        pumpMasterPS.setInt(15, recipeJson.getInt(ApiConstant.IS_SPEED));
        return pumpMasterPS;
    }

    public PreparedStatement insertEndPointRecipeSetting(PreparedStatement endPointPS, JSONObject recipeJson, int trialMasterId, int loopCount)throws SQLException{
        endPointPS.setString(1,
                recipeJson.getString(ApiConstant.STEP + loopCount + ApiConstant._END_POINT_TYPE));
        endPointPS.setString(2,
                recipeJson.getString(ApiConstant.STEP + loopCount + ApiConstant._END_POINT_VALUE));
        endPointPS.setInt(3, loopCount);
        endPointPS.setInt(4, trialMasterId);
        return endPointPS;
    }

    public PreparedStatement insertTargetStepRecipeSetting(PreparedStatement addTargetStepPS, JSONObject recipeJson, int trialMasterId, int loopCount)throws SQLException{
        addTargetStepPS.setInt(1, trialMasterId);
        addTargetStepPS.setString(2, recipeJson.getString(
                ApiConstant.TMP +
                        ApiConstant.UNDER_SCORE +
                        loopCount
        ));

        addTargetStepPS.setString(3, recipeJson.getString(
                ApiConstant.DURATION +
                        ApiConstant.UNDER_SCORE +
                        loopCount
        ));
        addTargetStepPS.setString(4, recipeJson.getString(
                ApiConstant.FLUX_FLOW_RATE +
                        ApiConstant.UNDER_SCORE +
                        loopCount
        ));
        addTargetStepPS.setString(5, recipeJson.getString(
                ApiConstant.PERM_WT +
                        ApiConstant.UNDER_SCORE +
                        loopCount
        ));
        addTargetStepPS.setString(6, recipeJson.getString(
                ApiConstant.PERMEATE_TOTAL_KEY +
                        ApiConstant.UNDER_SCORE +
                        loopCount
        ));
        return addTargetStepPS;
    }

    public PreparedStatement insertAuxPumpRecipeSetting(PreparedStatement auxPumpPS, JSONObject recipeJson, int trialMasterId, int loopCount)throws SQLException{
        auxPumpPS.setString(1, recipeJson.getString(ApiConstant.AUX_PUMP_ + loopCount));
        auxPumpPS.setString(2, recipeJson.getString(ApiConstant.PUMP_FUNCTION_ + loopCount));
        auxPumpPS.setString(3, recipeJson.getString(ApiConstant.AUX_TUBING_SIZE_ + loopCount));
        auxPumpPS.setString(4, recipeJson.getString(ApiConstant.FLOW_RATE_ + loopCount));
        auxPumpPS.setString(5, recipeJson.getString(ApiConstant.PORT + ApiConstant.UNDER_SCORE + loopCount));
        auxPumpPS.setInt(6, trialMasterId);
        return auxPumpPS;
    }

    public PreparedStatement insertAbvRecipeSetting(PreparedStatement abvMasterPS, JSONObject recipeJson, int trialMasterId, int loopCount)throws SQLException{
        abvMasterPS.setString(1, recipeJson.getString(ApiConstant.ABV_TYPE_ + loopCount));
        abvMasterPS.setString(2, recipeJson.getString(ApiConstant.CONT_BASED_ON_ + loopCount));
        abvMasterPS.setString(3, recipeJson.getString(ApiConstant.ABV_MODE_ + loopCount));
        abvMasterPS.setString(4, recipeJson.getString(ApiConstant.START_POS_ + loopCount));
        abvMasterPS.setString(5, recipeJson.getString(ApiConstant.PERCENT_CLOSED_ + loopCount));
        abvMasterPS.setString(6, recipeJson.getString(ApiConstant.OP_PRESSURE_ + loopCount));
        abvMasterPS.setInt(7, trialMasterId);
        return abvMasterPS;
    }

    public PreparedStatement insertKfKonduitRecipeSetting(PreparedStatement kfConduitPS, JSONObject recipeJson, int trialMasterId)throws SQLException{
        kfConduitPS.setString(1, recipeJson.getString(ApiConstant.UV_CH_1));
        kfConduitPS.setString(2, recipeJson.getString(ApiConstant.UV_CH_2));
        kfConduitPS.setString(3, recipeJson.getString(ApiConstant.K_FACTOR_CH_1));
        kfConduitPS.setString(4, recipeJson.getString(ApiConstant.K_FACTOR_CH_2));
        kfConduitPS.setInt(5, trialMasterId);
        return kfConduitPS;
    }

    public PreparedStatement insertTrialDetailSetting(PreparedStatement trialDetailPS, JSONObject recipeJson, int trialMasterId)throws SQLException{
        trialDetailPS.setInt(1, trialMasterId);
        trialDetailPS.setString(2, recipeJson.getString(ApiConstant.NOTES));
        trialDetailPS.setString(3, recipeJson.getString(ApiConstant.RUN_AS_SAFE));
        trialDetailPS.setString(4, recipeJson.getString(ApiConstant.FEED_TO_EMPTY));
        trialDetailPS.setString(5, recipeJson.getString(ApiConstant.NO_OF_AUX_PUMP));
        trialDetailPS.setString(6, recipeJson.getString(ApiConstant.PERMEATE_STOP_FIRST));
        trialDetailPS.setString(7, recipeJson.getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL));
        trialDetailPS.setString(8, recipeJson.getString(ApiConstant.RECIRCULATION_RATE_PER_UNIT));
        trialDetailPS.setString(9, recipeJson.getString(ApiConstant.RECIRCULATION_PRESSURE_UNIT_VALUE));
        trialDetailPS.setString(10, recipeJson.getString(ApiConstant.RETENTATE_TUBE_SIZE));
        trialDetailPS.setInt(11, recipeJson.getInt(ApiConstant.END_POINT_CAL));
        trialDetailPS.setString(
                12,
                (recipeJson.has(ApiConstant.DIGITAL_NOTES))?
                        recipeJson.getString(ApiConstant.DIGITAL_NOTES):
                        ApiConstant.BLANK_QUOTE
        );
        return trialDetailPS;
    }

}
