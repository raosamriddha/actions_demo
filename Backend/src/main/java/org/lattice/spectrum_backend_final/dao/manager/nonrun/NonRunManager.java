package org.lattice.spectrum_backend_final.dao.manager.nonrun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.dao.util.RecipeMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NonRunManager {

    public void addTargetStep(JSONObject recipeJson, int trialMasterId, Connection conn) throws SQLException {

        PreparedStatement addTargetStepPS = null;
        int totalTargetStep = recipeJson.getInt(ApiConstant.TOTAL_TARGET_STEP);
        int loopCount = 1;

        try {

            while (loopCount <= totalTargetStep) {

                addTargetStepPS = conn.prepareStatement(DbConstant.ADD_TARGET_STEP_QUERY);
                addTargetStepPS = new RecipeMapper().insertTargetStepRecipeSetting(addTargetStepPS, recipeJson, trialMasterId, loopCount);
                addTargetStepPS.executeUpdate();
                loopCount++;
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

    }


    public JSONArray getTargetStep(int trialMasterId){

        final Connection conn = DbConnectionManager.getInstance().getConnection();
        PreparedStatement getTargetPS = null;
        ResultSet getTargetRS = null;
        JSONArray targetStepJsonArray = null;


        try {
            getTargetPS = conn.prepareStatement(DbConstant.GET_TARGET_STEP);
            getTargetPS.setInt(1, trialMasterId);

            getTargetRS = getTargetPS.executeQuery();

            targetStepJsonArray = JsonMapper.getJSONFromResultSet(getTargetRS);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }finally {
            DbConnectionManager.closeDBConnection(getTargetRS, getTargetPS, conn);
        }

        return targetStepJsonArray;

    }

}
