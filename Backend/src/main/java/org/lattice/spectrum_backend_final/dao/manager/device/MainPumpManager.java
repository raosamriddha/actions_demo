/*===========================================================================================
 = Copyright (c) 2021 Lattice Innovations.
 = Created  :    18/01/21, 5:09 PM
 = Modified :    18/01/21, 5:09 PM
 = Author   :    Rahul Kumar Maurya
 = All right reserved.
 ==========================================================================================*/

package org.lattice.spectrum_backend_final.dao.manager.device;

import com.lattice.spectrum.ComLibrary.ComLib;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.*;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

/**
 * Main pump manager to manage setting/configuration related to main pump.
 */
public class MainPumpManager {

    /**
     * Change main pump direction while trial is in paused state.
     * @param direction direction to be change.
     * @throws Exception throws exception if any issue occur while performing this action.
     */
    public void changeDirectionOnPause(String direction) throws Exception {

        try {
            String pumpName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.PUMP_NAME);
            int headCount = DeviceManager.getInstance().getRecipeJson().getInt(ApiConstant.HEAD_COUNT);
            double speedFactor = BasicUtility.speedFactor(pumpName, direction, headCount);
            Logger.info(this, "changeDirectionOnPause: Changing main pump direction to "+direction);
            DeviceManager.getInstance()
                    .getModeProp()
                    .getFlowProp()
                    .setMainPumpSpeedFactor(speedFactor);

        } catch (Exception e) {
            Logger.error(this, "changeDirectionOnPause", e);
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

    }

    /**
     * Changing Main pump flow rate while trial is in run state.
     * @param runSettingJson Run json having all the settings required to change flow rate.
     * @throws Exception throws exception if any issue occur while performing this action.
     */
    public void changePumpFlowRateOnRun(JSONObject runSettingJson) throws Exception {

        double mainPumpFlowRate = 0;
        double calibrationFactor = 1;
        String pumpName = null;
        try {
            Logger.debug(this, "changePumpFlowRateOnRun");
            if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(runSettingJson, false)) {
                mainPumpFlowRate = runSettingJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE);
                pumpName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.PUMP_NAME);

                calibrationFactor = BasicUtility.getInstance()
                        .getCalibrationFactor(DeviceManager.getInstance()
                                .getRecipeJson()
                                .getString(ApiConstant.PUMP_TUBING_SIZE), pumpName);

                mainPumpFlowRate = BasicUtility.getInstance().getConvertedFlowRate(
                        mainPumpFlowRate,
                        pumpName,
                        true
                );

                BasicUtility.systemPrint("calibrationfactor", calibrationFactor);
                if (TrialManager.getInstance().isPaused()) {
                    String direction = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.DIRECTION);
                    String tubingSize = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.PUMP_TUBING_SIZE);
                    int pumpHeadCount = DeviceManager.getInstance().getRecipeJson().getInt(ApiConstant.HEAD_COUNT);
                    double speedFactor = BasicUtility.speedFactor(pumpName, direction, pumpHeadCount);
                    double tubeFactor = BasicUtility.getTubeFlowConversion(pumpName,tubingSize);

                    BasicUtility.systemPrint("speedFactor1 : " + speedFactor + " : tubeFactor1 : " + tubeFactor,
                            ": flowrate1 : " + mainPumpFlowRate);
                    ComLib.get().getMainPump().setFlow_mlpm(
                            mainPumpFlowRate,
                            tubeFactor,
                            speedFactor / calibrationFactor);
                    DeviceManager.getInstance().resetSpeedAndFlowRateIndicator(runSettingJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE));

                } else {
                    DeviceManager.getInstance().getModeProp().getFlowProp().setMainPumpFlowRate(
                            mainPumpFlowRate / calibrationFactor
                    );
                }


            } else {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }

            // saving trial log
            if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                LogManager.getInstance().insertTrialLog(
                        BasicUtility.getInstance().getUserId(runSettingJson),
                        TrialManager.getInstance().getTrialRunSettingId(),
                        ApiConstant.PUMP_FLOW_RATE_CHANGE_ACTION,
                        ApiConstant.LOG_TYPE_TRIAL
                );
                //Adding digital signature notes
                if(runSettingJson.has(ApiConstant.DIGITAL_NOTES)){
                    new NotesManager().saveNotes(new Notes(runSettingJson.getString(ApiConstant.DIGITAL_NOTES)));
                }
            } else {
                throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }

        } catch (InvalidCredentialException ex) {
            throw new InvalidCredentialException(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }
}
