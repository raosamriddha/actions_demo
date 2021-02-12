/*===========================================================================================
 = Copyright (c) 2021 Lattice Innovations.
 = Created  :    18/01/21, 3:58 PM
 = Modified :    18/01/21, 3:58 PM
 = Author   :    Rahul Kumar Maurya
 = All right reserved.
 ==========================================================================================*/

package org.lattice.spectrum_backend_final.dao.manager.device;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.USBConfiguration;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ValveConnectorConfiguration;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.PressureManager;
import com.lattice.spectrum.ModeLibrary.Managers.nonrun.NonRunModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.Managers.run.RunModeManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.ValveProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.*;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.services.handler.ManualModeHandler;

import java.util.HashMap;

import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.abvSettingsGlobal;

/**
 * Abv manager to manage setting/configuration related to ABV.
 */
public class AbvManager {

    /**
     * This API is used to configure valve 2. As device is not able to identify the connected device
     * on valve 2 connector so, we have to configure it.
     *
     * @param typeId typeId determines which configuration is need to set.
     *               typeId can be 0 or 1. 0 for Valve and 1 for Konduit.
     */
    public void configureValve2Connector(int typeId) throws Exception {
        if (typeId == 0) {
            Logger.info(this, "configureValve2Connector : USE_FOR_VALVE");
            ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_VALVE, USBConfiguration.DO_RESET);
        } else if (typeId == 1) {
            Logger.info(this, "configureValve2Connector : USE_FOR_KONDUIT");
            ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_KFCONDUIT, USBConfiguration.DO_RESET);
        } else {
            Logger.error(this, "configureValve2Connector : " + ApiConstant.INVALID_CONFIGURATION_ID_ERROR_MESSAGE);
            throw new Exception(ApiConstant.INVALID_CONFIGURATION_ID_ERROR_MESSAGE);
        }
    }

    /**
     * Resets Valves/ABVs only which are in use.
     * @param abvSettings provide ABV setting to identify which ABV is in use.
     */
    private void resetValveManual(ABVSetting[] abvSettings) {
        int loopCount = 0;
        Integer valveId;
        boolean isSecondAbvInUse = false;
        Logger.debug(this, "resetValveManual : Resetting valve...", abvSettings);
        while(loopCount < abvSettings.length){
            if (abvSettings[loopCount] == null) {
                loopCount++;
                continue;
            }
            valveId = abvSettings[loopCount].getType();
            isSecondAbvInUse = isSecondAbvInUse(abvSettings[loopCount]);
            if (valveId != null){
                ComLib.get().getValveInfo().setValvePosition(valveId, 1, 0);
            }
            loopCount++;
        }
        PressureManager.get().stop(isSecondAbvInUse);
    }

    /**
     * Fully opens valve in case of emergency.
     *
     * @param isEmergencyValveOpen if false, then not an emergency.
     * @throws Exception throws Exception if any issue occurs while performing this action.
     */
    public void emergencyValveOpen(boolean isEmergencyValveOpen) throws Exception {

        try {
            Logger.info("emergencyValveOpen : Opening valve.");
            String modeName = null;
            if (DeviceManager.getInstance().getModeType() == 0) {
                modeName = DeviceManager.getInstance()
                        .getRecipeJson()
                        .getString(ApiConstant.MODE_NAME);
            } else {
                modeName = ApiConstant.MANUAL;
            }
            if (ApiConstant.MANUAL.equalsIgnoreCase(modeName)) {
                resetValveManual(ManualModeHandler.abvSettingsGlobal);

            } else if (BasicUtility.getInstance().isRunMode(modeName)) {
                RunModeManager.get().onPausePressure();

            } else {
                NonRunModeManager.get().onPausePressure();
            }
            // saving trial log
            if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                if (isEmergencyValveOpen) {

                    LogManager.getInstance().insertTrialLog(
                            DbConnectionManager.getInstance().getTokenManager().getUserId(),
                            TrialManager.getInstance().getTrialRunSettingId(),
                            ApiConstant.EMERGENCY_VALVE_OPEN_ACTION,
                            ApiConstant.LOG_TYPE_TRIAL
                    );
                }
            } else {
                throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }
        } catch (InvalidCredentialException ex) {
            ex.printStackTrace();
            throw new InvalidCredentialException(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }

    /**
     * This action Perform after emergency valve open. It Resumes valve position to its previous state.
     *
     * @param digitalSignatureJson digital verification in case of KF Comm 2C.
     * @param verifyDS             determines whether digital signature verification is required or not.
     * @throws Exception throws Exception if any issue occurs while performing this action.
     */
    public void resumePressureControl(JSONObject digitalSignatureJson, boolean verifyDS) throws Exception {

        try {
            String modeName = null;
            if (!verifyDS || DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {
                if (DeviceManager.getInstance().getModeType() == 0) {
                    modeName = DeviceManager.getInstance()
                            .getRecipeJson()
                            .getString(ApiConstant.MODE_NAME);
                }
                sLog.d("modeName : " + modeName);
                if (BasicUtility.getInstance().isRunMode(modeName)) {
                    RunModeManager.get().onResumePressure();
                } else {
                    NonRunModeManager.get().onResumePressure();
                }
                if (!verifyDS) {
                } else if (TrialManager.getInstance().getTrialRunSettingId() != 0) {
                    // saving trial log
                    LogManager.getInstance().insertTrialLog(
                            BasicUtility.getInstance().getUserId(digitalSignatureJson),
                            TrialManager.getInstance().getTrialRunSettingId(),
                            ApiConstant.RESUME_PRESSURE_CONTROL_ACTION,
                            ApiConstant.LOG_TYPE_TRIAL
                    );
                    //Adding digital signature notes
                    if (digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)) {
                        new NotesManager().saveNotes(new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
                    }
                } else {
                    throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }
            } else {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }


        } catch (InvalidCredentialException ex) {
            ex.printStackTrace();
            throw new InvalidCredentialException(ex.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }


    /**
     * Set ABV to a closing percent.
     *
     * @param abvJson Json object with abv settings.
     */
    public void setPercentClosed(JSONObject abvJson) {
        int totalAbv = 0;
        int loopCount = 0;
        int valveId = 0;
        try {
            totalAbv = DeviceManager.getInstance()
                    .getRecipeJson()
                    .getJSONArray(ApiConstant.ABV)
                    .length();
            double openPos = 0;
            double closePos = 0;
            int curPos;
            double percentClosed;
            HashMap<String, Double> tubePos = null;
            tubePos = BasicUtility.getInstance()
                    .getTubePosition(
                            DeviceManager.getInstance().getRecipeJson(),
                            abvJson
                    );
            openPos = tubePos.get(ApiConstant.OPEN_POSITION);
            closePos = tubePos.get(ApiConstant.CLOSED_POSITION);
            percentClosed = abvJson.getDouble(ApiConstant.PERCENT_CLOSED);
            curPos = (int) (((percentClosed * (closePos - openPos)) / 100) + openPos);
            loopCount = 0;
            while (loopCount < totalAbv) {
                if (abvJson.getString(ApiConstant.CONT_BASED_ON)
                        .equalsIgnoreCase(DeviceManager.getInstance()
                                .getRecipeJson()
                                .getJSONArray(ApiConstant.ABV)
                                .getJSONObject(loopCount)
                                .getString(ApiConstant.CONT_BASED_ON))) {

                    valveId = loopCount;
                }
                loopCount++;
            }

            BasicUtility.systemPrint("percentClosed------------" + percentClosed + "--------currentPos", curPos);

            ComLib.get().getValveInfo().setValvePosition(valveId, curPos, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check whether ABV 2 in use or not.
     * @param abvSetting provide ABV setting to check whether ABV 2 in use or not.
     * @return true if ABV 2 in use else false.
     */
    private boolean isSecondAbvInUse(ABVSetting abvSetting) {
        if (abvSetting.getType() != null) {
            if (abvSetting.getType() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets ABV pressure during trial run.
     *
     * @param abvSetting the setting need to set on ABV at trial run.
     * @throws Exception throws Exception if any issue occurs while performing this action.
     */
    public void abvPressureControlAtRun(ABVSetting[] abvSetting) throws Exception {

        sLog.d(this, abvSetting);
        BasicUtility.systemPrint("inside", "abvPressureControl");
        ValveProp valveProp = null;
        ValveProp[] valveProps = new ValveProp[2];
        HashMap<String, Double> tubePositionMap = null;
        boolean isSecondAbvInUse = false;
        int loopCount;

        JSONObject digitalSignatureJson = new JSONObject(abvSetting);

        // if trial manual
        if (DeviceManager.getInstance().getModeType() == 1) {

            loopCount = 0;
            while (loopCount < abvSetting.length) {
                sLog.d("abvPressureControlAtRun: ", abvSetting);
                if (abvSetting[loopCount] == null) {
                    loopCount++;
                    continue;
                }
                isSecondAbvInUse = isSecondAbvInUse(abvSetting[loopCount]);
                if (abvSetting[loopCount].getType() == null) {
                    valveProps[loopCount] = null;
                } else if (ApiConstant.AUTO.equalsIgnoreCase(abvSetting[loopCount].getAbvMode())) {
                    abvSettingsGlobal[loopCount] = abvSetting[loopCount];
                    tubePositionMap = BasicUtility.getInstance().getTubePosition(abvSetting[loopCount].getTubingSize(), abvSetting[loopCount].getAbvType());
                    valveProps[loopCount] = new ValveProp(
                            (PressureTarget) BasicUtility.stringToEnumConversion(abvSetting[loopCount].getAbvTarget()),
                            Double.parseDouble(abvSetting[loopCount].getPosition()),
                            tubePositionMap.get(ApiConstant.OPEN_POSITION),
                            tubePositionMap.get(ApiConstant.CLOSED_POSITION),
                            Double.parseDouble(abvSetting[loopCount].getOperatingPressure()),
                            true,
                            tubePositionMap.get(ApiConstant.START_STEP_SIZE).intValue()

                    );
                    PressureManager.get().stop(isSecondAbvInUse);
                } else {

                    tubePositionMap = BasicUtility.getInstance().getTubePosition(abvSetting[loopCount].getTubingSize(), abvSetting[loopCount].getAbvType());

                    abvSettingsGlobal[loopCount] = abvSetting[loopCount];

                    valveProps[loopCount] = new ValveProp(
                            null,
                            Double.parseDouble(abvSetting[loopCount].getPosition()),
                            tubePositionMap.get(ApiConstant.OPEN_POSITION),
                            tubePositionMap.get(ApiConstant.CLOSED_POSITION),
                            0,
                            false,
                            tubePositionMap.get(ApiConstant.START_STEP_SIZE).intValue()

                    );
                    PressureManager.get().stop(isSecondAbvInUse);
                }
                loopCount++;
            }

            PressureManager.get().start(
                    valveProps,
                    null,
                    null,
                    new ModeListener() {
                        @Override
                        public void callback(ModeEvent id, Object... obj) {
                            try {
                                TrialManager.getInstance().setDummyListenerLiveData(id, null, obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    isSecondAbvInUse

            );

            sLog.d(this, PressureManager.get());


        } else {


            valveProp = DeviceManager.getInstance().getModeProp().getValveProp(abvSetting[0].getType());

            String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);
            String abvMode = null;

            if (BasicUtility.getInstance().isRunMode(modeName) || ApiConstant.FLUSHING.equalsIgnoreCase(modeName)) {

                if (valveProp.isAutoMode()) {
                    abvMode = ApiConstant.AUTO;
                } else {
                    abvMode = ApiConstant.MANUAL;
                }

                if (ApiConstant.AUTO.equalsIgnoreCase(abvSetting[0].getAbvMode())) {

                    valveProp.setOperatingPressure(Double.parseDouble(abvSetting[0].getOperatingPressure()));
                    valveProp.setAutoMode(true);


                } else {

                    valveProp.setSetPos(Double.parseDouble(abvSetting[0].getPosition()));
                    valveProp.setAutoMode(false);
                    int setPos = (int) (valveProp.getOpenPosition() + valveProp.getSetPos() * ((valveProp.getClosePosition() - valveProp.getOpenPosition()) / 100));
                    ComLib.get().getValveInfo().setValvePosition(abvSetting[0].getType(), setPos, 0);

                }

                // if change in abv Mode
                if (!abvMode.equalsIgnoreCase(abvSetting[0].getAbvMode())
                ) {
                    sLog.d(this, "reset abv mode");
                    emergencyValveOpen(false);

                    Thread.sleep(1000);

                    resumePressureControl(digitalSignatureJson, false);
                }

                sLog.d(this, DeviceManager.getInstance().getModeProp());


            }
        }


    }

}
