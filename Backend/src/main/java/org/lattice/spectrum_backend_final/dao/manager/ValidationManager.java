package org.lattice.spectrum_backend_final.dao.manager;

/**
 * @author RAHUL KUMAR MAURYA
 */


import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.USBConfiguration;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ValveConnectorConfiguration;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.KonduitUtil;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import org.lattice.spectrum_backend_final.exception.InvalidAbvException;
import org.lattice.spectrum_backend_final.exception.InvalidEndPointValueException;
import org.lattice.spectrum_backend_final.exception.InvalidFlowRateException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 */
public class ValidationManager {


    /**
     * @param phone
     * @param conn
     * @return
     */
    public static boolean checkPhone(String phone, Connection conn) {
        try (
                PreparedStatement checkPhoneExistPS = conn.prepareStatement(DbConstant.CHECK_PHONE_EXIST)
        ) {

            checkPhoneExistPS.setString(1, phone);

            ResultSet resultSet = checkPhoneExistPS.executeQuery();


            if (resultSet.next()) {
                return true;
            }


        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }


        return false;
    }


    /**
     * @param phone
     * @param user_id
     * @param conn
     * @return
     */
    public static boolean checkPhone(String phone, int user_id, Connection conn) {
        try (
                PreparedStatement checkPhoneExistPS = conn.prepareStatement(DbConstant.CHECK_PHONE_EXIST_FROM_USER_ID)

        ) {

            checkPhoneExistPS.setString(1, phone);
            checkPhoneExistPS.setInt(2, user_id);

            ResultSet checkPhoneRS = checkPhoneExistPS.executeQuery();


            if (checkPhoneRS.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }


        return false;
    }


    /**
     * @param email
     * @param conn
     * @return
     */
    public static boolean isEmailExist(String email, Connection conn) {
        try (
                PreparedStatement checkEmailExistPS = conn.prepareStatement(DbConstant.CHECK_EMAIL_EXIST)

        ) {

            checkEmailExistPS.setString(1, email);

            ResultSet checkEmailRS = checkEmailExistPS.executeQuery();


            if (checkEmailRS.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }


        return false;
    }


    /**
     * @param email
     * @param userId
     * @param conn
     * @return
     */
    public static boolean isEmailExist(String email, int userId, Connection conn) {
        try (
                PreparedStatement checkEmailExistPS = conn.prepareStatement(DbConstant.CHECK_EMAIL_EXIST_FROM_USER_ID)

        ) {

            checkEmailExistPS.setString(1, email);
            checkEmailExistPS.setInt(2, userId);

            ResultSet checkEmailRS = checkEmailExistPS.executeQuery();

            if (checkEmailRS.next()) {
                return true;
            }


        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }


        return false;
    }


    /**
     * @param username
     * @param conn
     * @return
     */
    public static boolean isUsernameExist(String username, Connection conn) {

        try (
                PreparedStatement checkUserExistPS = conn.prepareStatement(DbConstant.CHECK_USERNAME_EXIST)

        ) {

            checkUserExistPS.setString(1, username);

            ResultSet checkUserRS = checkUserExistPS.executeQuery();

            if (checkUserRS.next()) {
                return true;
            }


        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    /**
     * @param user_id
     * @return
     */
    public static String getUserTypeFromId(int user_id) {

        try (
                Connection conn = DbConnectionManager.getInstance().getConnection();
                PreparedStatement getUserRolePS = conn.prepareStatement(DbConstant.GET_USER_ROLE_FROM_ID)

        ) {

            getUserRolePS.setInt(1, user_id);

            ResultSet userRoleRS = getUserRolePS.executeQuery();
            if (userRoleRS.next()) {
                return userRoleRS.getString(ApiConstant.ROLE_DESCRIPTION);
            }


            userRoleRS.close();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }


    /**
     * @param recipe_name
     * @param conn
     * @return
     */
    public static boolean isRecipeExist(String recipe_name, Connection conn) {

        try (
                PreparedStatement preparedStatement = conn.prepareStatement(DbConstant.CHECK_RECIPE_EXIST)

        ) {

            preparedStatement.setString(1, recipe_name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    /**
     * @param recipeJson
     * @param conn
     * @param tubing_size
     * @param flowRate
     * @return
     * @throws SQLException
     */
    public static boolean isFlowRateValid(
            JSONObject recipeJson, Connection conn, String tubing_size, Float flowRate, String pumpType
    ) throws SQLException {

        PreparedStatement tubingSpecPS= null;
        PreparedStatement getPumpIdPS= null;
        ResultSet getPumpIdRS = null;
        int pumpId = 0;

        try {

            // get min max flow rate in case of krosflo because tubing size is not there
            if ((recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.KROSFLOFS15) ||
                    recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.KROSFLOFS500)) &&
                    ApiConstant.MAIN_PUMP.equalsIgnoreCase(pumpType)
            ) {

                getPumpIdPS = conn.prepareStatement(DbConstant.GET_PUMP_ID_QUERY);
                getPumpIdPS.setString(1, recipeJson.getString(ApiConstant.PUMP_NAME));

                getPumpIdRS = getPumpIdPS.executeQuery();

                if(getPumpIdRS.next()){
                    pumpId = getPumpIdRS.getInt(ApiConstant.PUMP_LOOKUP_ID);
                }

                tubingSpecPS = conn.prepareStatement(DbConstant.GET_TUBING_SPEC_COMPATIBLE_WITH_PUMP_QUERY);
                tubingSpecPS.setInt(1, pumpId);

            } else {
                tubingSpecPS = conn.prepareStatement(DbConstant.GET_SINGLE_TUBING_SPEC);
                tubingSpecPS.setString(1, tubing_size);
            }


            ResultSet tubeSpecRS = tubingSpecPS.executeQuery();


            while (tubeSpecRS.next()) {
                Float minFlowRate = tubeSpecRS.getFloat(ApiConstant.MIN_FLOW_RATE);
                Float maxFlowRate = tubeSpecRS.getFloat(ApiConstant.MAX_FLOW_RATE);

                System.out.println("---------minflow------------------" + minFlowRate + "---------maxFlow--------" + maxFlowRate + "------flow-------" + flowRate + "---tubing_size----" + tubing_size);

                if ((flowRate >= minFlowRate && flowRate < maxFlowRate) || (flowRate > minFlowRate && flowRate <= maxFlowRate)) {
                    System.out.println("-------if-----");
                    return true;
                } else {
                    System.out.println("-------else-----");
                    return false;
                }

            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return false;

    }


    public static boolean isRecipeValidForSave(JSONObject recipeJson, Connection conn) throws SQLException,
            InvalidFlowRateException,
            InvalidAbvException,
            InvalidEndPointValueException {

        int loopCount = 1;
        int totalSteps = 0;
        try {

            if (BasicUtility.getInstance().isRunMode(recipeJson.getString(ApiConstant.MODE_NAME))) {

                // Validate flow rate for Main pump.
                if (isFlowRateValid(recipeJson, conn, recipeJson.getString(ApiConstant.PUMP_TUBING_SIZE), recipeJson.getFloat(ApiConstant.FLOW_RATE), ApiConstant.MAIN_PUMP) == false) {
                    throw new InvalidFlowRateException(ApiConstant.INVALID_FLOW_RATE_ERROR_MESSAGE);
                }

            }


            // Validate flow rate for Aux pump.
            while (loopCount <= recipeJson.getInt(ApiConstant.NO_OF_AUX_PUMP)) {
                System.out.println("--------loopcount-------" + loopCount);
                if (recipeJson.getString(ApiConstant.PUMP_FUNCTION_ + loopCount).equals(ApiConstant.PERMEATE)) {
                    System.out.println("--------insideif1-------" + loopCount);

                    if (!recipeJson.getString(ApiConstant.FLOW_RATE_ + loopCount).equals("")) {

                        if (isFlowRateValid(recipeJson, conn, recipeJson.getString(ApiConstant.AUX_TUBING_SIZE_ + loopCount), recipeJson.getFloat(ApiConstant.FLOW_RATE_ + loopCount), ApiConstant.AUX_PUMP) == false
                        ) {
                            System.out.println("--------aux-------" + loopCount);
                            throw new InvalidFlowRateException(ApiConstant.INVALID_FLOW_RATE_ERROR_MESSAGE);
                        }
                    }

                    System.out.println("--------insideif2-------" + loopCount);
                    //Validate ABV count on behalf of pump function. When aux pump is on permeate line than only 1 abv is possible.
                    if (recipeJson.getInt(ApiConstant.TOTAL_ABV) > 1) {
                        System.out.println("--------abv-------");
                        throw new InvalidAbvException(ApiConstant.INVALID_ABV_COUNT_ERROR_MESSAGE);
                    }
                    // break because only one aux pump function can be permeate.
                    break;
                }
                loopCount++;

            }


            //Validation ABV type as for selected pump.
            if (
                // 1) KR2i valve only compatible with KR2i, KMPi valve with all pumps.
                    (recipeJson.getString(ApiConstant.ABV_TYPE_ + 1).equals(ApiConstant.KR2I) ||
                            recipeJson.getString(ApiConstant.ABV_TYPE_ + 2).equals(ApiConstant.KR2I)) &&
                            (recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.KMPI)) ||
                            // 2) if pump id KR2i or KF-FS-15 than only one ABV is allowed.
                            (
                                    (recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.KR2I) ||
                                            recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.KROSFLOFS15)) &&
                                            recipeJson.getInt(ApiConstant.TOTAL_ABV) > 1
                            )
            ) {
                throw new InvalidAbvException(ApiConstant.INVALID_ABV_TYPE_ERROR_MESSAGE);
            }

            // Validation for endpoint values

            loopCount = 1;
            totalSteps = recipeJson.getInt(ApiConstant.TOTAL_STEPS);
            while (loopCount <= totalSteps) {

                int step = loopCount;
                while (step > 0) {

                    // Check if end point type of current and previous steps are Permeate Weight.
                    if (
                            (step != loopCount) &&
                                    recipeJson.getString(ApiConstant.STEP + loopCount + ApiConstant._END_POINT_TYPE).equals(ApiConstant.PERMEATE_WEIGHT) &&
                                    recipeJson.getString(ApiConstant.STEP + step + ApiConstant._END_POINT_TYPE).equals(ApiConstant.PERMEATE_WEIGHT)
                    ) {

                        //Check if the value of permeate end points are valid.
                        if (
                                recipeJson.getFloat(ApiConstant.STEP + loopCount + ApiConstant._END_POINT_VALUE) <
                                        recipeJson.getFloat(ApiConstant.STEP + step + ApiConstant._END_POINT_VALUE)
                        ) {
                            throw new InvalidEndPointValueException(ApiConstant.INVALID_END_POINT_VALUE_ERROR_MESSAGE + loopCount);
                        }

                        // breaks because we only have to check from its previous permeate endpoint value to avoid extra loop.
                        break;

                    }

                    // Check if end point type of current and previous steps are Concentration Factor.
                    if (
                            (step != loopCount) &&
                                    recipeJson.getString(ApiConstant.STEP + loopCount + ApiConstant._END_POINT_TYPE).equals(ApiConstant.CONCENTRATION_FACTOR) &&
                                    recipeJson.getString(ApiConstant.STEP + step + ApiConstant._END_POINT_TYPE).equals(ApiConstant.CONCENTRATION_FACTOR)
                    ) {

                        //Check if the value of Concentration Factor end points are valid.
                        if (
                                recipeJson.getFloat(ApiConstant.STEP + loopCount + ApiConstant._END_POINT_VALUE) <
                                        recipeJson.getFloat(ApiConstant.STEP + step + ApiConstant._END_POINT_VALUE)
                        ) {
                            throw new InvalidEndPointValueException(ApiConstant.INVALID_END_POINT_VALUE_ERROR_MESSAGE + loopCount);
                        }

                        // breaks because we only have to check from its previous Concentration Factor endpoint value to avoid extra loop.
                        break;

                    }
                    step--;
                }

                loopCount++;

            }


            //Validate tubing size

            //


        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (InvalidFlowRateException e) {
            throw new InvalidFlowRateException(e.getMessage());
        } catch (InvalidAbvException e) {
            throw new InvalidAbvException(e.getMessage());
        } catch (InvalidEndPointValueException e) {
            throw new InvalidEndPointValueException(e.getMessage());
        }


        return true;
    }


    public static boolean isAuxEndPoint(String endPoint) {

        if (
                ApiConstant.UV.equalsIgnoreCase(endPoint) ||
                        ApiConstant.TURBIDITY.equalsIgnoreCase(endPoint) ||
                        ApiConstant.PH.equalsIgnoreCase(endPoint) ||
                        ApiConstant.CONDUCTIVITY.equalsIgnoreCase(endPoint) ||
                        ApiConstant.PROTEIN_CONCENTRATION.equalsIgnoreCase(endPoint) ||
                        ApiConstant.PERMEATE_TOTAL_ENDPOINT.equalsIgnoreCase(endPoint)

        ) {
            return true;
        }

        return false;
    }

    private static boolean isFeedScaleRequired(String modeName){

        if(
                ApiConstant.CLEANING.equalsIgnoreCase(modeName) ||
                ApiConstant.NWP.equalsIgnoreCase(modeName) ||
                ApiConstant.FLUSHING.equalsIgnoreCase(modeName) ||
                ApiConstant.FLUX_C.equalsIgnoreCase(modeName)
        ){
            return false;
        }

        return true;

    }

    /**
     * check all the device are connected which are required for particular recipe.
     *
     * @param recipeJson
     * @return
     * @throws HardwareValidationException
     */
    public static boolean isConnectionValid(JSONObject recipeJson, boolean isRunning) throws HardwareValidationException {

        JSONObject liveDataJson = new JSONObject();

        try {
            if (ComLib.get().getMainPump().getMotorModel() == null) {
                throw new HardwareValidationException(ApiConstant.COM_PORT_CONNECTION_ERROR_MESSAGE);
            }

            int loopCount = 0;
            //check aux pump type and number
            while (loopCount < recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length()) {

                JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount);
                String auxPumpFunction = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount).getString(ApiConstant.PUMP_FUNCTION);

                if (
                        !ComLib.get().getAuxPump()
                                .isAuxConnected(
                                        auxJson.getInt(ApiConstant.TYPE)
                                )
                ) {
                    if(isRunning){

                        RunManager.getInstance().recirculationPause(false);
                    }
                    BasicUtility.systemPrint(auxPumpFunction + ApiConstant.AUX_PUMP_CONNECTION_ERROR_MESSAGE, "");
                    throw new HardwareValidationException(auxPumpFunction + ApiConstant.AUX_PUMP_CONNECTION_ERROR_MESSAGE);
                }
                loopCount++;
            }

            if(!isRunning){

                if(recipeJson.getJSONArray(ApiConstant.ABV).length() == 2){

                    ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_VALVE, USBConfiguration.DO_RESET);

                }else{

                    ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_KFCONDUIT, USBConfiguration.DO_RESET);
                }

            }


            loopCount = 0;
            //check Konduit
            while (loopCount < recipeJson.getJSONArray(ApiConstant.END_POINTS).length()) {

                JSONObject endPointJson = recipeJson.getJSONArray(ApiConstant.END_POINTS).getJSONObject(loopCount);

                // It will only check connectivity of Konduit if aux endpoint is selected.
                if (isAuxEndPoint(endPointJson.getString(ApiConstant.END_POINT_TYPE))) {

                    if (recipeJson.has(ApiConstant.KF_KONDUIT_SETTING_ID) && !ComLib.get().getKonduitInfo().isKFConduitConnected()) {
                        if(isRunning){

                            RunManager.getInstance().recirculationPause(false);
                        }
                        throw new HardwareValidationException(ApiConstant.KF_KONDUIT_ERROR_MESSAGE);
                    }
                }

                loopCount++;
            }

            // check scale

            if (
                    !ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE) &&
                            (DeviceManager.getInstance().getFeedStartWt() == 0) &&
                            isFeedScaleRequired(recipeJson.getString(ApiConstant.MODE_NAME))
            ) {
                if(isRunning){

                    RunManager.getInstance().recirculationPause(false);
                }
                throw new HardwareValidationException(ApiConstant.FEED + ApiConstant.SCALE_CONNECTION_ERROR_MESSAGE);
            }

            if (!new KonduitUtil().isTotalizerMode() && !ComLib.get().getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)) {
                if(isRunning){
                    RunManager.getInstance().recirculationPause(false);
                }
                throw new HardwareValidationException(ApiConstant.PERMEATE + ApiConstant.SCALE_CONNECTION_ERROR_MESSAGE);
            }

        }catch (HardwareValidationException hardValEx){
            if(isRunning){
                liveDataJson = TrialManager.getInstance().setFinishedJson(liveDataJson);
                liveDataJson.put(ApiConstant.TYPE, ApiConstant.DISCONNECT_PAUSE);
                liveDataJson.put(ApiConstant.MESSAGE, hardValEx.getMessage());
                sLog.d("", liveDataJson);
                DeviceManager.getInstance().broadcastLiveData(liveDataJson);
            }
            throw new HardwareValidationException(hardValEx.getMessage());
        }

        return true;

    }

//    private static boolean preRunValidator(JSONObject recipeJson) {
//
//        int loopCount;
//        String[] subMode = recipeJson.getString(ApiConstant.MODE_NAME).split(ApiConstant.FORWARD_SLASH);
//        int totalSubMode = subMode.length;
//
//        double totalWt = 0;
//        double[] permTrueWt = new double[totalSubMode];
//        double feedTrueWt;
//        double[] permEndWt = new double[totalSubMode];
//        double permHoldUp = recipeJson.getDouble(ApiConstant.PERM_TUBE_HOLDUP);
//        double feedHoldup = 0;
//        double permModeStartWt;
//        double feedModeStartWt;
//        double totalDiaWt = 0;
//        double dv = 0;
//        double cf = 0;
//
//
//        if (subMode[0].equalsIgnoreCase(ApiConstant.CFC_MODE)) {
//            totalWt = ComLib.get()
//                    .getScaleInfo()
//                    .scaleReadingID(ScaleID.FEED_SCALE) *
//                    recipeJson.getJSONArray(ApiConstant.END_POINTS)
//                            .getJSONObject(0)
//                            .getDouble(ApiConstant.END_POINT_VALUE)
//            ;
//        }
//
//        loopCount = 0;
//        while (loopCount < totalSubMode) {
//
//            JSONArray endPointJsonArray = recipeJson.getJSONArray(ApiConstant.END_POINTS);
//
//
//            switch (subMode[loopCount]) {
//
//                case ApiConstant.CFC_MODE:
//
//                    if (ApiConstant.CONCENTRATION_FACTOR
//                            .equalsIgnoreCase(endPointJsonArray
//                                    .getJSONObject(loopCount)
//                                    .getString(ApiConstant.END_POINT_TYPE)
//                            )
//                    ) {
//
//
//                        feedTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
//                        permTrueWt[loopCount] = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
//
//                        cf = (permTrueWt[loopCount] + feedTrueWt + permHoldUp + feedHoldup - totalDiaWt) / feedTrueWt;
//
//
//                    }
//
//
//                case ApiConstant.C_MODE:
//
//                    if (ApiConstant.CONCENTRATION_FACTOR
//                            .equalsIgnoreCase(endPointJsonArray
//                                    .getJSONObject(loopCount)
//                                    .getString(ApiConstant.END_POINT_TYPE)
//                            )
//                    ) {
//
////                        totalWt = ComLib.get()
////                                .getScaleInfo()
////                                .scaleReadingID(ScaleID.FEED_SCALE) *
////                                endPointJsonArray.getJSONObject(loopCount).getDouble(ApiConstant.END_POINT_VALUE) ;
//
//                        feedTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
//                        permTrueWt[loopCount] = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
//
//                        double cf2 = (permTrueWt[loopCount] + feedTrueWt + permHoldUp + feedHoldup - totalDiaWt) / feedTrueWt;
//
//                        permTrueWt[loopCount] = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
//
//                        if (cf2 > cf) {
//                            cf = cf2;
//                        } else {
//
//                        }
//
//                    }
//
//                case ApiConstant.D_MODE:
//
//
////                    dv = (permEndWt[loopCount] - permModeStartWt) / feedModeStartWt;
//
//
//            }
//
//
//        }
//
//        return true;
//    }


    public static boolean isRecipeValidForRun(JSONObject recipeJson, JSONObject runJson) throws HardwareValidationException {

        String pumpName = null;
        double feedStartWt = 0;

        pumpName = recipeJson.getString(ApiConstant.PUMP_NAME);


        // checks whether all the required devices are connected or not.
        if (isConnectionValid(recipeJson, false)) {

            BasicUtility.systemPrint("isRecipeValidForRun : Checking pump type :  rec_pump : " + pumpName + " : dev_pump : ", BasicUtility.getInstance().getConnectedPumpName());
            //check main pump type
            if (!pumpName.equalsIgnoreCase(BasicUtility.getInstance().getConnectedPumpName())) {

                BasicUtility.systemPrint("inside", "pump error");
//            RunModeManager.get().stop();
                throw new HardwareValidationException(ApiConstant.MAIN_PUMP_VALIDATION_ERROR_MESSAGE);
            }


            if(runJson != null){

                if (
                        ApiConstant.C_MODE.equalsIgnoreCase(recipeJson.getString(ApiConstant.MODE_NAME)) &&
                                (!ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE) || runJson.getBoolean(ApiConstant.IS_FEED_SCALE_OVERRIDE))
                ) {
                    feedStartWt = runJson.getDouble(ApiConstant.FEED_START_WEIGHT);
                } else {

                    feedStartWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
                }


                //check weather feed scale weight is valid for run

                if (
//                    ApiConstant.YES.equalsIgnoreCase(recipeJson.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION)) &&
                        feedStartWt < (recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP) + runJson.getDouble(ApiConstant.FEED_HOLD_UP))
                ) {
                    BasicUtility.systemPrint("isRecipeValidForRun : "+ApiConstant.FEED_SCALE_VALUE_IS_NOT_VALID +" :  total_perm_tube_hold_up : "+recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP),"feed_start_wt : "+feedStartWt);
                    throw new HardwareValidationException(ApiConstant.FEED_SCALE_VALUE_IS_NOT_VALID);
                }

            }

        }


        return true;

    }


    public static boolean isTrialRunExist(String trialId) {
        PreparedStatement checkTrialRunExistPS = null;
        ResultSet checkTrialRunExistRS = null;
        Connection conn = DbConnectionManager.getInstance().getConnection();
        try {
            BasicUtility.systemPrint("isTrialRunExist : GET_TRIAL_RUN_SETTING_QUERY : trialID : "+trialId);
            checkTrialRunExistPS = conn.prepareStatement(DbConstant.GET_TRIAL_RUN_SETTING_QUERY);

            checkTrialRunExistPS.setString(1, trialId);
            checkTrialRunExistRS = checkTrialRunExistPS.executeQuery();

            if (checkTrialRunExistRS.next()) {
                return true;
            }


        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbConnectionManager.closeDBConnection(checkTrialRunExistRS, checkTrialRunExistPS, conn);
        }

        return false;

    }


}
