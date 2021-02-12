/*===========================================================================================
 = Copyright (c) 2021 Lattice Innovations.
 = Created  :    18/01/21, 5:10 PM
 = Modified :    18/01/21, 5:10 PM
 = Author   :    Rahul Kumar Maurya
 = All right reserved.
 ==========================================================================================*/

package org.lattice.spectrum_backend_final.dao.manager.device;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

/**
 * Aux pump manager to manage setting/configuration related to Aux pumps.
 */
public class AuxPumpManager {

    /**
     * Change aux pump flow rate while trial is in run state.
     * @param flowRate Input valid flow rate need to update.
     * @return returns aux pump name.
     * @throws Exception throws exception if any issue occur while performing this action.
     */
    public String changeAuxFlowRateOnRun(double flowRate) throws Exception {

        String auxPumpName = null;

        try {

            int permAuxId = BasicUtility.getPermAuxPumpPortId(DeviceManager.getInstance().getRecipeJson());
            int permAuxIndex = BasicUtility.getPermAuxPumpIndex(DeviceManager.getInstance().getRecipeJson());
            BasicUtility.systemPrint("changeAuxFlowRateOnRun : permAuxId : "+ permAuxId + " : permAuxIndex : "+permAuxIndex);

            JSONObject auxJson = DeviceManager.getInstance()
                    .getRecipeJson()
                    .getJSONArray(ApiConstant.AUX_PUMP)
                    .getJSONObject(permAuxIndex);

            auxPumpName = auxJson.getString(ApiConstant.AUX_PUMP_TYPE);
            if (permAuxId == -1) {
                Logger.error("changeAuxFlowRateOnRun : "+ApiConstant.INVALID_AUX_PORT_ID_ERROR_MESSAGE);
                throw new TrialRunException(ApiConstant.INVALID_AUX_PORT_ID_ERROR_MESSAGE);
            }

            flowRate = BasicUtility.getInstance().getConvertedFlowRate(
                    flowRate,
                    auxPumpName,
                    true
            );

            DeviceManager.getInstance().getModeProp().getAuxProp(permAuxId).setFlowRate(flowRate);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
        return auxPumpName;
    }
}
