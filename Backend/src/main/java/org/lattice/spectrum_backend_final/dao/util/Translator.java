package org.lattice.spectrum_backend_final.dao.util;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.ModeProp.*;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.EndPointSettingManager;
import org.lattice.spectrum_backend_final.dao.manager.TargetStepSettingManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Translator {

    private static Translator translator;

    public Translator() {
    }

    public static Translator getInstance() {

        synchronized (Translator.class) {
            if (translator == null) {
                translator = new Translator();
            }
        }

        return translator;
    }

    public FlowProp translateFlowProp(JSONObject recipeJsonObject) throws Exception {

        double flowRate = 0;
        String pumpName = null;

        BasicUtility.systemPrint("flowrate",recipeJsonObject.getString(ApiConstant.FLOW_RATE) +"------");

        if (
                !ApiConstant.BLANK_QUOTE.equalsIgnoreCase(recipeJsonObject.getString(ApiConstant.FLOW_RATE)) &&
                        recipeJsonObject.getString(ApiConstant.FLOW_RATE) != null
        ) {
            flowRate = recipeJsonObject.getDouble(ApiConstant.FLOW_RATE);
        }
        pumpName = recipeJsonObject.getString(ApiConstant.PUMP_NAME);
        flowRate = BasicUtility.getInstance().getConvertedFlowRate(flowRate, pumpName, true);

        FlowProp flowProp = new FlowProp(
                (FlowControlMode) BasicUtility.stringToEnumConversion(recipeJsonObject.getString(ApiConstant.FLOW_CONTROL)),
                flowRate,
                BasicUtility.getTubeFlowConversion(recipeJsonObject.getString(ApiConstant.PUMP_NAME), recipeJsonObject.getString(ApiConstant.PUMP_TUBING_SIZE)),
                BasicUtility.speedFactor(
                        recipeJsonObject.getString(ApiConstant.PUMP_NAME),
                        recipeJsonObject.getString(ApiConstant.DIRECTION),
                        recipeJsonObject.getInt(ApiConstant.HEAD_COUNT)
                ) / BasicUtility.getInstance()
                        .getCalibrationFactor(recipeJsonObject.getString(ApiConstant.PUMP_TUBING_SIZE) , pumpName),
                recipeJsonObject.getInt(ApiConstant.RAMP_UP_TIME),
                BasicUtility.getInstance().getFlowFactor(recipeJsonObject.getString(ApiConstant.PUMP_NAME))
        );


        return flowProp;
    }

    public RecirculationProp translateRecirculationProp(JSONObject recipeJsonObject) {

        RecirculationProp recirculationProp = null;

        if (ApiConstant.YES.equalsIgnoreCase(recipeJsonObject.getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL))) {
            recirculationProp = new RecirculationProp(recipeJsonObject.getDouble(ApiConstant.RECIRCULATION_RATE_PER_UNIT), BasicUtility.getInterval(recipeJsonObject.getString(ApiConstant.RECIRCULATION_PRESSURE_UNIT_VALUE)));
        }
        else if(!recipeJsonObject.getString(ApiConstant.DELTA_P).equals(ApiConstant.BLANK_QUOTE)){
            recirculationProp = new RecirculationProp(recipeJsonObject.getDouble(ApiConstant.DELTA_P_RATE), BasicUtility.getInterval(recipeJsonObject.getString(ApiConstant.DELTA_P_DURATION)));
        }

        return recirculationProp;
    }

    public ValveProp[] translateValveProp(JSONObject recipeJsonObject, int totalAbv) {

        double openPosition = 0, closePosition = 0;
        int startStepSize = 0;
        double opPressure = 0;
        HashMap<String, Double> tubeMap = null;
        HashMap<String, Double> permeateTubeMap = null;
        JSONObject minMaxRpmJson = null;
        String pumpName = null;

        ValveProp[] valveProp = new ValveProp[totalAbv];

        int loopCount = 0;


        // setting abv properties
        while (loopCount < totalAbv) {

            JSONObject abvJson = recipeJsonObject.getJSONArray(ApiConstant.ABV).getJSONObject(loopCount);

            double perClosed = 0;
            if (abvJson.getString(ApiConstant.PERCENT_CLOSED).equals("")) {
                perClosed = abvJson.getDouble(ApiConstant.START_POS);
            } else {
                perClosed = abvJson.getDouble(ApiConstant.PERCENT_CLOSED);
            }

            tubeMap = BasicUtility.getInstance()
                    .getTubePosition(
                            recipeJsonObject,
                            abvJson
                    );


            openPosition = tubeMap.get(ApiConstant.OPEN_POSITION);

            closePosition = tubeMap.get(ApiConstant.CLOSED_POSITION);

            startStepSize = tubeMap.get(ApiConstant.START_STEP_SIZE).intValue();

            if (!abvJson.getString(ApiConstant.OP_PRESSURE).equals("")) {
                opPressure = abvJson.getDouble(ApiConstant.OP_PRESSURE);
            }

            if(!recipeJsonObject.getString(ApiConstant.DELTA_P).equals(ApiConstant.BLANK_QUOTE)){
                valveProp[loopCount] = new ValveProp(PressureTarget.DELTA,
                        perClosed,
                        openPosition,
                        closePosition,
                        recipeJsonObject.getDouble(ApiConstant.DELTA_P),
                        ApiConstant.AUTO.equals(abvJson.getString(ApiConstant.ABV_MODE)),
                        startStepSize,
                        opPressure
                );
                pumpName = recipeJsonObject.getString(ApiConstant.PUMP_NAME);
                minMaxRpmJson = BasicUtility.getPumpMinMaxRpm(pumpName);
                valveProp[loopCount].setMinRPM(0);
                valveProp[loopCount].setMaxRPM(BasicUtility.getDegreePerSecondFromRpm(pumpName, minMaxRpmJson.getInt(ApiConstant.MAX_SPEED)));

            }else{
                valveProp[loopCount] = new ValveProp((PressureTarget) BasicUtility.stringToEnumConversion(abvJson.getString(ApiConstant.CONT_BASED_ON)),
                        perClosed,
                        openPosition,
                        closePosition,
                        opPressure,
                        ApiConstant.AUTO.equals(abvJson.getString(ApiConstant.ABV_MODE)),
                        startStepSize
                );
            }

            loopCount++;
        }

        return valveProp;

    }

    public AuxProp[] translateAuxProp(int totalAuxPump, JSONObject recipeJsonObject) {

        AuxProp[] auxProp = new AuxProp[totalAuxPump];
        int loopCount = 0;
        int auxPumpTypeId = -1;

        while (loopCount < totalAuxPump) {

            JSONObject auxJson = recipeJsonObject.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount);

            double flowRate = 0;

            if (ApiConstant.PERMEATE.equals(auxJson.getString(ApiConstant.PUMP_FUNCTION)) &&
                    !auxJson.getString(ApiConstant.FLOW_RATE).equals("")
            ) {
                flowRate = auxJson.getDouble(ApiConstant.FLOW_RATE);
            }

            if (DbConnectionManager.getInstance().isDebuggingMode) {

                auxPumpTypeId = 1;

            } else {
                auxPumpTypeId = BasicUtility.getInstance().getAuxPumpTypeIdByAuxName(auxJson.getString(ApiConstant.AUX_PUMP_TYPE));
            }

            auxProp[loopCount] = new AuxProp(
                    auxJson.getInt(ApiConstant.TYPE),
                    auxPumpTypeId,
                    BasicUtility.getPumpMinMaxRpm(auxJson.getString(ApiConstant.AUX_PUMP_TYPE)).getInt(ApiConstant.MAX_SPEED),
                    BasicUtility.getTubeFlowConversion(auxJson.getString(ApiConstant.AUX_PUMP_TYPE),
                            auxJson.getString(ApiConstant.AUX_TUBING_SIZE)),
                    BasicUtility.getInstance().getConvertedFlowRate(flowRate, auxJson.getString(ApiConstant.AUX_PUMP_TYPE), true)
            );

            loopCount++;
        }

        return auxProp;
    }


//    public ModeProp translateRunSubModeProp(JSONObject recipeJsonObject, String[] subMode, int totalAuxPump, ModeProp modeProp) throws SQLException {
//
//        boolean isPermeatePumpValueSet = false;
//        int totalSubMode = recipeJsonObject.getJSONArray(ApiConstant.END_POINTS).length();
//        int loopCount = 0;
//        int diaCount = 1;
//
//        RunSubModeProp[] runSubModeProp = new RunSubModeProp[totalSubMode];
//
//        // set sub mode property
//        while (loopCount < totalSubMode) {
//
//            JSONObject endPointJson = recipeJsonObject.getJSONArray(ApiConstant.END_POINTS).getJSONObject(loopCount);
//
//            runSubModeProp[loopCount] = new RunSubModeProp();
//            runSubModeProp[loopCount].setMode((OperationMode) BasicUtility.stringToEnumConversion(subMode[loopCount]));
//            runSubModeProp[loopCount].setEndPointTarget((EndPointTarget) BasicUtility.stringToEnumConversion(endPointJson.getString(ApiConstant.END_POINT_TYPE)));
//            runSubModeProp[loopCount].setEndPointValue(endPointJson.getDouble(ApiConstant.END_POINT_VALUE));
//
//            if(
//                    ApiConstant.TURBIDITY.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE )) ||
//                    ApiConstant.PH.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE )) ||
//                    ApiConstant.FLOW_RATE.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE ))
//            ){
//
//                modeProp.setUvX(EndPointUtil.getInstance().getXValue(endPointJson.getString(ApiConstant.END_POINT_TYPE )));
//                modeProp.setUvY(EndPointUtil.getInstance().getYValue(endPointJson.getString(ApiConstant.END_POINT_TYPE )));
//
//            }
//
//
//            if (recipeJsonObject.has(ApiConstant.FEED_TO_EMPTY) && ApiConstant.YES.equalsIgnoreCase(recipeJsonObject.getString(ApiConstant.FEED_TO_EMPTY))) {
//                runSubModeProp[loopCount].setFeedToEmptyEnable(true);
//            }
//
//
//
//            // set sub mode aux pump id
//            for (int auxPumpCount = 0; auxPumpCount < totalAuxPump; auxPumpCount++) {
//
//                JSONObject auxJson = recipeJsonObject.getJSONArray(ApiConstant.AUX_PUMP)
//                        .getJSONObject(auxPumpCount);
//
//                if (subMode[loopCount].equals(ApiConstant.D_MODE) &&
//                        (auxJson.getString(ApiConstant.PUMP_FUNCTION)
//                                .equals(ApiConstant.DIAFILTRATION + ApiConstant.BLANK_SPACE + diaCount)
//                        )
//                ) {
//
//                    //setting aux pump port
//                    runSubModeProp[loopCount].setAuxPumpID(
//                            auxJson.getInt(ApiConstant.TYPE)
//                    );
//                    diaCount++;
//                    break;
//                } else if (subMode[loopCount].equals(ApiConstant.CFC_MODE) &&
//                        (auxJson.getString(ApiConstant.PUMP_FUNCTION)
//                                .equals(ApiConstant.CONSTANT_FEED)
//                        )
//                ) {
//
//                    //setting aux pump port
//                    runSubModeProp[loopCount].setAuxPumpID(
//                            auxJson.getInt(ApiConstant.TYPE)
//                    );
//                    break;
//                } else if (
//                        auxJson.getString(ApiConstant.PUMP_FUNCTION)
//                                .contains(ApiConstant.PERMEATE)
//                ) {
//
//                    //check for permeate pump is there or not in recipe
//
//                    if (!isPermeatePumpValueSet) {
//                        BasicUtility.systemPrint("loopcount in perm", loopCount);
//                        modeProp.setPermPump(new PermPumpProp(
//                                auxJson.getInt(ApiConstant.TYPE),
//                                ApiConstant.YES.equalsIgnoreCase(recipeJsonObject.getString(ApiConstant.PERMEATE_STOP_FIRST)))
//                        );
//                        isPermeatePumpValueSet = true;
//                    }
//
//                }
//
//            }
//
//            loopCount++;
//
//        }
//
//        modeProp.setRunModes(runSubModeProp);
//
//        return modeProp;
//
//    }

    public ModeProp translateRunSubModeProp(JSONObject recipeJsonObject, String[] subMode, int totalAuxPump, ModeProp modeProp) throws SQLException, IOException {

        boolean isPermeatePumpValueSet = false;
        int totalSubMode = recipeJsonObject.getJSONArray(ApiConstant.END_POINTS).length();
        int loopCount = 0;
        int diaCount = 1;

        JSONArray endPointJsonArray = EndPointSettingManager.getInstance().getEndPointSettingsJsonArray();

        sLog.d(this, endPointJsonArray);

        RunSubModeProp[] runSubModeProp = new RunSubModeProp[totalSubMode];

        // set sub mode property
        while (loopCount < totalSubMode) {

//            JSONObject endPointJson = recipeJsonObject.getJSONArray(ApiConstant.END_POINTS).getJSONObject(loopCount);
            JSONObject endPointJson = endPointJsonArray.getJSONObject(loopCount);
            double kFactor = 1;
            double endPointValue;

            runSubModeProp[loopCount] = new RunSubModeProp();
            runSubModeProp[loopCount].setMode((OperationMode) BasicUtility.stringToEnumConversion(subMode[loopCount]));
            runSubModeProp[loopCount].setEndPointTarget((EndPointTarget) BasicUtility.stringToEnumConversion(endPointJson.getString(ApiConstant.END_POINT_TYPE)));
            endPointValue = endPointJson.getDouble(ApiConstant.END_POINT_VALUE);
            if(ApiConstant.CONDUCTIVITY.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE ))){
                kFactor = recipeJsonObject.getDouble(ApiConstant.K_FACTOR_CH_1);
                endPointValue /= kFactor;
            }
            runSubModeProp[loopCount].setEndPointValue(endPointValue);

            if(ApiConstant.PERMEATE_TOTAL_ENDPOINT.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE))){
                modeProp.setTotalizerMode(TotalizerMode.ENDPOINT);
            }

//            if( konduitUtil.isKonduitEndPoint(endPointJson)){
//
//                xValue = EndPointUtil.getInstance().getXValue(endPointJson.getString(ApiConstant.END_POINT_TYPE ), 1);
//                yValue = EndPointUtil.getInstance().getYValue(endPointJson.getString(ApiConstant.END_POINT_TYPE), 1);
//                konduitProp[0] = new KonduitProp(
//                        konduitUtil.getKonduitType(endPointJson.getString(ApiConstant.END_POINT_TYPE)),
//                                KonduitChannel.CHANNEL1,
//                                xValue,
//                                yValue
//                );
//
//            }


            if (recipeJsonObject.has(ApiConstant.FEED_TO_EMPTY) && ApiConstant.YES.equalsIgnoreCase(recipeJsonObject.getString(ApiConstant.FEED_TO_EMPTY))) {
                runSubModeProp[loopCount].setFeedToEmptyEnable(true);
            }



            // set sub mode aux pump id
            for (int auxPumpCount = 0; auxPumpCount < totalAuxPump; auxPumpCount++) {

                JSONObject auxJson = recipeJsonObject.getJSONArray(ApiConstant.AUX_PUMP)
                        .getJSONObject(auxPumpCount);

                if (subMode[loopCount].equals(ApiConstant.D_MODE) &&
                        (auxJson.getString(ApiConstant.PUMP_FUNCTION)
                                .equals(ApiConstant.DIAFILTRATION + ApiConstant.BLANK_SPACE + diaCount)
                        )
                ) {

                    //setting aux pump port
                    runSubModeProp[loopCount].setAuxPumpID(
                            auxJson.getInt(ApiConstant.TYPE)
                    );
                    diaCount++;
                    break;
                } else if (subMode[loopCount].equals(ApiConstant.CFC_MODE) &&
                        (auxJson.getString(ApiConstant.PUMP_FUNCTION)
                                .equals(ApiConstant.CONSTANT_FEED)
                        )
                ) {

                    //setting aux pump port
                    runSubModeProp[loopCount].setAuxPumpID(
                            auxJson.getInt(ApiConstant.TYPE)
                    );
                    break;
                } else if (
                        auxJson.getString(ApiConstant.PUMP_FUNCTION)
                                .contains(ApiConstant.PERMEATE)
                ) {

                    //check for permeate pump is there or not in recipe

                    if (!isPermeatePumpValueSet) {
                        BasicUtility.systemPrint("loopcount in perm", loopCount);
                        modeProp.setPermPump(new PermPumpProp(
                                auxJson.getInt(ApiConstant.TYPE),
                                ApiConstant.YES.equalsIgnoreCase(recipeJsonObject.getString(ApiConstant.PERMEATE_STOP_FIRST)))
                        );
                        isPermeatePumpValueSet = true;
                    }

                }

            }

            loopCount++;

        }

//        konduitProp[1] = konduitUtil.getKonduitChannelProp(2);
        modeProp.setRunModes(runSubModeProp);

        return modeProp;

    }


    public NonRunModeProp[] translateFluxNonRunModeProp(JSONObject recipeJson) {

        int loopCount = 0;
        int totalTargetStep = recipeJson.getJSONArray(ApiConstant.TARGET_STEP).length();

        NonRunModeProp[] nonRunModeProp = new NonRunModeProp[totalTargetStep];
        int auxPumpId = 0;

        if (recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length() > 0) {

            auxPumpId = recipeJson.getJSONArray(ApiConstant.AUX_PUMP)
                    .getJSONObject(0)
                    .getInt(ApiConstant.TYPE);
        }

        while (loopCount < totalTargetStep) {

            JSONObject targetStepJson = recipeJson.getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(loopCount);

            double pumpFlowRate = BasicUtility.getInstance().getConvertedFlowRate(targetStepJson.getDouble(ApiConstant.FLOW_RATE), recipeJson.getString(ApiConstant.PUMP_NAME), true);

            nonRunModeProp[loopCount] = new NonRunModeProp().setPressureTarget(
                    (PressureTarget) BasicUtility.stringToEnumConversion(
                            ApiConstant.TMP
                    ))
                    .setOperatingPressure(targetStepJson.getDouble(ApiConstant.TMP))
                    .setTargetDuration(targetStepJson.getInt(ApiConstant.DURATION))
                    .setMainFlowRate(pumpFlowRate)
                    .setAuxPumpID(auxPumpId);


            loopCount++;
        }

        return nonRunModeProp;
    }

    public NonRunModeProp[] translateNwpNonRunModeProp(JSONObject recipeJson) throws IOException {

        int loopCount = 0;
        int totalTargetStep = recipeJson.getJSONArray(ApiConstant.TARGET_STEP).length();
        JSONArray targetStepJsonArray = TargetStepSettingManager.getInstance().getTargetStepSettingsJsonArray();

        NonRunModeProp[] nonRunModeProp = new NonRunModeProp[totalTargetStep];

        while (loopCount < totalTargetStep) {

            JSONObject targetStepJson = targetStepJsonArray.getJSONObject(loopCount);

            nonRunModeProp[loopCount] = new NonRunModeProp().setPressureTarget(
                    (PressureTarget) BasicUtility.stringToEnumConversion(
                            ApiConstant.TMP
                    ))
                    .setOperatingPressure(targetStepJson.getDouble(ApiConstant.TMP))
                    .setTargetDuration(targetStepJson.getInt(ApiConstant.DURATION));

            loopCount++;
        }

        return nonRunModeProp;
    }

    public NonRunModeProp[] translateCleaningNonRunModeProp(JSONObject recipeJson, ModeProp nonRun) throws IOException {

        int loopCount = 0;
        int totalTargetStep = recipeJson.getJSONArray(ApiConstant.TARGET_STEP).length();
        JSONArray targetStepJsonArray = TargetStepSettingManager.getInstance().getTargetStepSettingsJsonArray();

        NonRunModeProp[] nonRunModeProp = new NonRunModeProp[totalTargetStep];

        while (loopCount < totalTargetStep) {

            JSONObject targetStepJson = targetStepJsonArray.getJSONObject(loopCount);


            if(!targetStepJson.getString(ApiConstant.DURATION).equals(ApiConstant.BLANK_QUOTE)) {

                nonRunModeProp[loopCount] = new NonRunModeProp().setEndPointTarget(
                        (EndPointTarget) BasicUtility.stringToEnumConversion(
                                ApiConstant.DURATION
                        ))
                        .setEndPointValue(targetStepJson.getDouble(ApiConstant.DURATION));
            }
            else if(!targetStepJson.getString(ApiConstant.PERM_WT).equals(ApiConstant.BLANK_QUOTE)){

                nonRunModeProp[loopCount] = new NonRunModeProp().setEndPointTarget(
                        (EndPointTarget) BasicUtility.stringToEnumConversion(
                                ApiConstant.PERM_WT
                        ))
                        .setEndPointValue(targetStepJson.getDouble(ApiConstant.PERM_WT));

            }else if(!targetStepJson.getString(ApiConstant.PERMEATE_TOTAL_KEY).equals(ApiConstant.BLANK_QUOTE)){

                nonRunModeProp[loopCount] = new NonRunModeProp().setEndPointTarget(
                        (EndPointTarget) BasicUtility.stringToEnumConversion(
                                ApiConstant.PERMEATE_TOTAL_KEY
                        ))
                        .setEndPointValue(targetStepJson.getDouble(ApiConstant.PERMEATE_TOTAL_KEY));
                nonRun.setTotalizerMode(TotalizerMode.ENDPOINT);

            }else {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }

            if (loopCount == 0) {

                nonRunModeProp[loopCount].setEndPointParam(EndPointParam.RetentateClean);

            } else if (loopCount == 1) {

                nonRunModeProp[loopCount].setEndPointParam(EndPointParam.PermeateClean);
            }

            loopCount++;

        }

        return nonRunModeProp;
    }

    public NonRunModeProp[] translateFlushingNonRunModeProp(JSONObject recipeJson, ModeProp nonRun) throws IOException {

        int loopCount = 0;
        int totalTargetStep = recipeJson.getJSONArray(ApiConstant.TARGET_STEP).length();
        JSONArray targetStepJsonArray = TargetStepSettingManager.getInstance().getTargetStepSettingsJsonArray();

        NonRunModeProp[] nonRunModeProp = new NonRunModeProp[totalTargetStep];

        while (loopCount < totalTargetStep) {

            JSONObject targetStepJson = targetStepJsonArray.getJSONObject(loopCount);

            if(!targetStepJson.getString(ApiConstant.DURATION).equals(ApiConstant.BLANK_QUOTE)) {

                nonRunModeProp[loopCount] = new NonRunModeProp().setEndPointTarget(
                        (EndPointTarget) BasicUtility.stringToEnumConversion(
                                ApiConstant.DURATION
                        ))
                        .setEndPointValue(targetStepJson.getDouble(ApiConstant.DURATION));
            }
            else if(!targetStepJson.getString(ApiConstant.PERM_WT).equals(ApiConstant.BLANK_QUOTE)){

                nonRunModeProp[loopCount] = new NonRunModeProp().setEndPointTarget(
                        (EndPointTarget) BasicUtility.stringToEnumConversion(
                                ApiConstant.PERM_WT
                        ))
                        .setEndPointValue(targetStepJson.getDouble(ApiConstant.PERM_WT));

            }else if(!targetStepJson.getString(ApiConstant.PERMEATE_TOTAL_KEY).equals(ApiConstant.BLANK_QUOTE)){

                nonRunModeProp[loopCount] = new NonRunModeProp().setEndPointTarget(
                        (EndPointTarget) BasicUtility.stringToEnumConversion(
                                ApiConstant.PERMEATE_TOTAL_KEY
                        ))
                        .setEndPointValue(targetStepJson.getDouble(ApiConstant.PERMEATE_TOTAL_KEY));
                nonRun.setTotalizerMode(TotalizerMode.ENDPOINT);

            }else {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }

            loopCount++;

        }

        return nonRunModeProp;
    }



    public RunSubModeProp[] translateVacuumNonRunModeProp() {

        int loopCount = 0;
        int totalSubMode = 10000;
        RunSubModeProp[] runSubModeProp = new RunSubModeProp[totalSubMode];

        while (loopCount < totalSubMode) {

            if( loopCount % 2 == 0){

                runSubModeProp[loopCount] = new RunSubModeProp().setMode(OperationMode.CF_MODE)
                        .setEndPointTarget(EndPointTarget.ConcentrationFactor)
                        .setEndPointValue(Integer.MAX_VALUE);

            }else {

                runSubModeProp[loopCount] = new RunSubModeProp().setMode(OperationMode.D_MODE)
                        .setEndPointTarget(EndPointTarget.DiafiltrationVolume)
                        .setEndPointValue(Integer.MAX_VALUE);

            }
            loopCount++;

        }

        return runSubModeProp;
    }


    public NonRunModeProp[] getNonRunModeProp(JSONObject recipeJson, ModeProp nonRun) throws IOException {

        NonRunModeProp[] nonRunModeProp = null;

        String modeName = recipeJson.getString(ApiConstant.MODE_NAME);

        switch (modeName) {

            case ApiConstant.FLUX_C:
            case ApiConstant.FLUX_CV:
                nonRunModeProp = translateFluxNonRunModeProp(recipeJson);
                break;

            case ApiConstant.NWP:
                nonRunModeProp = translateNwpNonRunModeProp(recipeJson);
                break;

            case ApiConstant.CLEANING:
                nonRunModeProp = translateCleaningNonRunModeProp(recipeJson, nonRun);
                break;

            case ApiConstant.FLUSHING:
                nonRunModeProp = translateFlushingNonRunModeProp(recipeJson, nonRun);
                break;

            default:
                throw new IllegalArgumentException(ApiConstant.INVALID_MODE_ERROR_MESSAGE);

        }

        return nonRunModeProp;

    }
}
