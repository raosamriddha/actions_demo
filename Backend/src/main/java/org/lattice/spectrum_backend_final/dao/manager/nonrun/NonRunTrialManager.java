package org.lattice.spectrum_backend_final.dao.manager.nonrun;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.nonrun.NonRunModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.Managers.run.RunModeManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.*;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.*;
import org.lattice.spectrum_backend_final.dao.util.*;

import java.util.Arrays;

public class NonRunTrialManager {

    private static NonRunTrialManager nonRunTrialManager;

    private NonRunTrialManager() {
    }


    public static NonRunTrialManager getInstance() {

        synchronized (NonRunTrialManager.class) {
            if (nonRunTrialManager == null) {
                nonRunTrialManager = new NonRunTrialManager();
            }
        }

        return nonRunTrialManager;
    }


    // starts Flux C, Flux CV, NWP, Cleaning, Flushing modes
    public void startNonRunMode(JSONObject runJson, JSONObject recipeJson) throws Exception {

        OperationMode opModeName = (OperationMode) BasicUtility.stringToEnumConversion(recipeJson.getString(ApiConstant.MODE_NAME));


        // saving log to file
        Logger.setTrialRunOutputLogToFile(opModeName, true);


        JSONObject alarmJson = AlarmManager.getInstance()
                .fetchAlarmSetting(runJson.getString(ApiConstant.TRIAL_ID));
        KonduitUtil konduitUtil = new KonduitUtil();

        sLog.d(this, recipeJson);

        ModeProp nonRun = new ModeProp();
        AuxProp[] auxProp = null;
        ValveProp[] valveProp = null;
        AlarmProp alarmProp = null;
        FlowProp flowProp = null;
        RecirculationProp recirculationProp = null;
        int loopCount;

        NonRunModeManager nonRunModeManager = NonRunModeManager.get();

        boolean isFeedEmpty = true;
        double feedHoldUp = 0.0;


        int totalAuxPump = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length();
        int totalAbv = recipeJson.getJSONArray(ApiConstant.ABV).length();

        String modeName = recipeJson.getString(ApiConstant.MODE_NAME);


        DeviceManager.getInstance().setModeType(0);


        // todo dummyModeLister need to implement
        ModeListener dummyModeListener = new ModeListener() {
            @Override
            public void callback(ModeEvent id, Object... obj) {
                sLog.d("DummyCallback", "Event Reported [ID:" + id + ", Data:" + Arrays.toString(obj));

                try {
                    TrialManager.getInstance().setDummyListenerLiveData(id, alarmJson, obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        if (!DbConnectionManager.getInstance().isDebuggingMode) {

            //check either recipe is valid for run or not.
            ValidationManager.isRecipeValidForRun(recipeJson, runJson);
        }

        if(konduitUtil.isTotalizerMode()){
            nonRun.setTotalizerMode(TotalizerMode.OBSERVER);
        }else{
            nonRun.setTotalizerMode(TotalizerMode.NONE);
        }

        nonRun.setSuperID(opModeName);


        if (ApiConstant.NO.equalsIgnoreCase(runJson.getString(ApiConstant.IS_FEED_EMPTY))) {
            isFeedEmpty = false;
        }

        if (ApiConstant.NWP.equalsIgnoreCase(modeName)) {

            //set recirculation property
            recirculationProp = Translator.getInstance().translateRecirculationProp(recipeJson);
        }


        feedHoldUp = runJson.getDouble(ApiConstant.FEED_HOLD_UP);


        nonRun.setPermHoldUp(recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));


        // set flow property
        flowProp = Translator.getInstance().translateFlowProp(recipeJson);


        //setting aux pump property
        auxProp = Translator.getInstance().translateAuxProp(totalAuxPump, recipeJson);


        loopCount = 0;

        while (loopCount < totalAuxPump) {

            JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP)
                    .getJSONObject(loopCount);

            if (
                    auxJson.getString(ApiConstant.PUMP_FUNCTION)
                            .contains(ApiConstant.PERMEATE)
            ) {

                BasicUtility.systemPrint("permeate aux id loopcount non run", loopCount);
                //check for permeate pump is there or not in recipe
                nonRun.setPermPump(new PermPumpProp(
                        auxJson.getInt(ApiConstant.TYPE),
                        ApiConstant.YES.equals(recipeJson.getString(ApiConstant.PERMEATE_STOP_FIRST))));

            }
            loopCount++;

        }


        //setting ABV property
        valveProp = Translator.getInstance().translateValveProp(recipeJson, totalAbv);

        // set alarm property

        if (alarmJson != null) {

            alarmProp = AlarmManager.getInstance().setAlarm(alarmJson);
        }

        if(
                ApiConstant.FLUX_C.equals(recipeJson.getString(ApiConstant.MODE_NAME)) ||
                ApiConstant.FLUX_CV.equals(recipeJson.getString(ApiConstant.MODE_NAME))
        ){
                if (!ApiConstant.BLANK_QUOTE.equalsIgnoreCase(runJson.getString(ApiConstant.FEED_START_WEIGHT))) {
                    nonRun.setFeedStartWt(runJson.getDouble(ApiConstant.FEED_START_WEIGHT));
                }
        }

        nonRun.setFeedHoldUpEmpty(isFeedEmpty);
        nonRun.setFeedHoldUp(feedHoldUp);
        nonRun.setRecirculationProp(recirculationProp);
        nonRun.setFlowProp(flowProp);
        nonRun.setAuxPumps(auxProp);
        nonRun.setValves(valveProp);
        nonRun.setNonRunModes(
                Translator.getInstance()
                        .getNonRunModeProp(recipeJson, nonRun)
        );
        nonRun.setKonduit(new KonduitUtil().getKonduitProp());
        nonRun.setAlarmProp(alarmProp);


        DeviceManager.getInstance().setModeProp(nonRun);

        sLog.d(this, nonRun);


        //set system setting once for trial.
        Converter.setSystemSetting();


        if (!DbConnectionManager.getInstance().isDebuggingMode) {

            nonRunModeManager
                    .setProp(nonRun)
                    .setSuperCallback(dummyModeListener)
                    .setup();


            DeviceManager.getInstance().connectSocket();
        }

        RunManager.getInstance().updateTrialStartTime(
                TrialManager.getInstance().getTrialRunSettingId(),
                BasicUtility.getInstance().getCurrentTimestamp()
        );

        //setting flowrate for nwp and flux mode
        if(BasicUtility.getInstance().isNwpOrFluxMode()){
            DeviceManager.getInstance().setFlowRateCount(1);
        }
    }


}
