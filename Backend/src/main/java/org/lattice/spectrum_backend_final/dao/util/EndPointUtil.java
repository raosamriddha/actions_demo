package org.lattice.spectrum_backend_final.dao.util;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowMeterManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.*;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.*;

import java.sql.SQLException;

public class EndPointUtil {

    private static EndPointUtil endPointUtil;

    public static EndPointUtil getInstance() {

        synchronized (EndPointUtil.class) {
            if (endPointUtil == null) {
                endPointUtil = new EndPointUtil();
            }
        }

        return endPointUtil;
    }

    private EndPointUtil() {
    }


    public JSONArray filterAuxEndPoints(JSONArray endPointJsonArray) throws SQLException {

        SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();

        int loopCount;

        if (systemSetting.getIsAUXkonduit() == 0) {

            loopCount = 0;
            while (loopCount < endPointJsonArray.length()) {
                JSONObject endPointJson = endPointJsonArray.getJSONObject(loopCount);

                String endPoint = endPointJson.getString(ApiConstant.END_POINT_NAME);

                if (
                        ApiConstant.PH.equalsIgnoreCase(endPoint) ||
                                ApiConstant.TURBIDITY.equalsIgnoreCase(endPoint) ||
                                ApiConstant.PERMEATE_TOTAL_ENDPOINT.equalsIgnoreCase(endPoint)||
                                ApiConstant.PROTEIN_CONCENTRATION.equalsIgnoreCase(endPoint)
                ) {
                    endPointJsonArray.remove(loopCount);
                    loopCount--;
                }
                loopCount++;
            }
        } else {

            loopCount = 0;
            while (loopCount < endPointJsonArray.length()) {

                JSONObject endPointJson = endPointJsonArray.getJSONObject(loopCount);

                String endPoint = endPointJson.getString(ApiConstant.END_POINT_NAME);

                switch (endPoint) {
                    case ApiConstant.UV:
                        endPointJsonArray.remove(loopCount);
                        loopCount--;
                        break;

                    case ApiConstant.PH:
                        if (systemSetting.getPhMin() == null || systemSetting.getPhMax() == null) {
                            endPointJsonArray.remove(loopCount);
                            loopCount--;
                        }
                        break;

                    case ApiConstant.TURBIDITY:
                        if (systemSetting.getTurbidityMax() == null || systemSetting.getTurbidityMin() == null) {
                            endPointJsonArray.remove(loopCount);
                            loopCount--;
                        }
                        break;

                    case ApiConstant.PERMEATE_TOTAL_ENDPOINT:
                        if (systemSetting.getTotalizerMin() == null || systemSetting.getTotalizerMax() == null) {
                            endPointJsonArray.remove(loopCount);
                            loopCount--;
                        }
                        break;

                    case ApiConstant.PROTEIN_CONCENTRATION:
                        if (systemSetting.getProteinConcMin() == null || systemSetting.getProteinConcMax() == null) {
                            endPointJsonArray.remove(loopCount);
                            loopCount--;
                        }
                        break;
                }

                loopCount++;

            }


        }

        return endPointJsonArray;
    }


    public double getXValue(String endPoint, int channelId) throws SQLException {

        SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();

        double uvX = 0;

        switch (endPoint) {
            case ApiConstant.PH:
                if(channelId == 1){
                    uvX = (systemSetting.getPhMax() - systemSetting.getPhMin()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }else{
                    uvX = (systemSetting.getPhMax_2() - systemSetting.getPhMin_2()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }
                break;
            case ApiConstant.TURBIDITY:
                if(channelId == 1){
                    uvX = (systemSetting.getTurbidityMax() - systemSetting.getTurbidityMin()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }else{
                    uvX = (systemSetting.getTurbidityMax_2() - systemSetting.getTurbidityMin_2()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }
                break;
            case ApiConstant.TOTALIZER:
                if(channelId == 1){
                    uvX = (systemSetting.getTotalizerMax() - systemSetting.getTotalizerMin()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }else{
                    uvX = (systemSetting.getTotalizerMax_2() - systemSetting.getTotalizerMin_2()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }
                break;
             case ApiConstant.PROTEIN_CONCENTRATION:
                if(channelId == 1){
                    uvX = (systemSetting.getProteinConcMax() - systemSetting.getProteinConcMin()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }else{
                    uvX = (systemSetting.getProteinConcMax_2() - systemSetting.getProteinConcMin_2()) / (systemSetting.getUvMax() - systemSetting.getUvMin());
                }
                break;

            default:
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        }

        return uvX;

    }

    public double getYValue(String endPoint, int channelId) throws SQLException {

        SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();
        double uvY;

        switch (endPoint) {
            case ApiConstant.PH:
                if(channelId == 1){
                    uvY = systemSetting.getPhMin();

                }else{

                    uvY = systemSetting.getPhMin_2();
                }
                break;
            case ApiConstant.TURBIDITY:
                if(channelId == 1){
                    uvY = systemSetting.getTurbidityMin();

                }else{

                    uvY = systemSetting.getTurbidityMin_2();
                }
                break;
            case ApiConstant.TOTALIZER:
                if(channelId == 1){
                    uvY = systemSetting.getTotalizerMin();

                }else{

                    uvY = systemSetting.getTotalizerMin_2();
                }
                break;
            case ApiConstant.PROTEIN_CONCENTRATION:
                if(channelId == 1){
                    uvY = systemSetting.getProteinConcMin();

                }else{

                    uvY = systemSetting.getProteinConcMin_2();
                }
                break;

            default:
                uvY = 0;

        }

        return uvY;

    }

    public double getEndPointDeviceValue(String endPoint) throws SQLException {
        JSONObject recipeJson = DeviceManager.getInstance().getRecipeJson();
        ModeProp modeProp = DeviceManager.getInstance().getModeProp();
        if(modeProp == null || recipeJson == null){
            BasicUtility.systemPrint("getEndPointDeviceValue: ","NULL");
            return 0;
        }
        switch (endPoint){
            case ApiConstant.PERMEATE_WEIGHT:
                return ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
            case ApiConstant.DIAFILTRATION_VOLUME:
                return modeProp.getDV();
            case ApiConstant.UV:
            case ApiConstant.PH:
            case ApiConstant.TURBIDITY:
            case ApiConstant.PROTEIN_CONCENTRATION:
                return Double.parseDouble(new KonduitUtil().getKonduitReading(1));
            case ApiConstant.PERMEATE_TOTAL_ENDPOINT:
                return FlowMeterManager.get().getVolume(0);
            case ApiConstant.CONDUCTIVITY:
                double kFactor = 1;
                if (
                        recipeJson.has(ApiConstant.K_FACTOR_CH_1) &&
                                !ApiConstant.BLANK_QUOTE.equals(recipeJson.getString(ApiConstant.K_FACTOR_CH_1))
                ) {
                    kFactor = recipeJson.getDouble(ApiConstant.K_FACTOR_CH_1);
                }
                return kFactor * ComLib.get().getKonduitInfo().getProbeConductivity_mS(0);
            case ApiConstant.CONCENTRATION_FACTOR:
                return modeProp.getCF();
            case ApiConstant.TMP:
                return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.TMP_CHANNEL);
            default:
                return 0;
        }
    }


    public NotesDescription setEndPoint(NotesDescription notesDescription, int subModeStep) throws SQLException {
        String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);

        if(ApiConstant.VACUUM_MODE.equals(modeName)){
            ModeProp modeProp = DeviceManager.getInstance().getModeProp();
            String endPointName = BasicUtility.enumToStringConversion(modeProp.getRunModes()[subModeStep].getEndPointTarget());
            notesDescription.setEndPointName(endPointName);
            notesDescription.setEndPointValue(getEndPointDeviceValue(endPointName));

        }else{
            EndPointSetting[] endPointSetting = EndPointSettingManager.getInstance().getEndPointSettings();
            notesDescription.setEndPointName(endPointSetting[subModeStep].getEndPointType());
            notesDescription.setEndPointValue(
                    (
                            notesDescription.getStatus() == 0 &&
                                    ApiConstant.DIAFILTRATION_VOLUME.equals(notesDescription.getEndPointName())) ?
                            endPointSetting[subModeStep].getEndPointValue():
                            getEndPointDeviceValue(endPointSetting[subModeStep].getEndPointType())
            );
        }
        return notesDescription;
    }
    public NotesDescSetup setTargetStep(NotesDescSetup notesDescSetup, int subModeStep) throws SQLException {
        String modeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);

        if(
                ApiConstant.FLUX_C.equalsIgnoreCase(modeName) ||
                ApiConstant.FLUX_CV.equalsIgnoreCase(modeName)
        ){
            notesDescSetup.setTmp(getEndPointDeviceValue(ApiConstant.TMP));
            notesDescSetup.setDuration(TrialDurationManager.getInstance().getDurationFormatted());
            notesDescSetup.setFlowRate(DeviceManager.getInstance().getMainPumpFlowRate());

        }else if(ApiConstant.NWP.equalsIgnoreCase(modeName)){

            notesDescSetup.setTmp(getEndPointDeviceValue(ApiConstant.TMP));
            notesDescSetup.setDuration(TrialDurationManager.getInstance().getDurationFormatted());

        }else if(ApiConstant.CLEANING.equalsIgnoreCase(modeName) ||ApiConstant.FLUSHING.equalsIgnoreCase(modeName)){
            JSONObject targetStepJson = DeviceManager.getInstance().getRecipeJson().getJSONArray(ApiConstant.TARGET_STEP).getJSONObject(subModeStep);
            if(!ApiConstant.BLANK_QUOTE.equalsIgnoreCase(targetStepJson.getString(ApiConstant.COLUMN_PERMEATE_WEIGHT))){
                notesDescSetup.setPermeateWeight(getEndPointDeviceValue(ApiConstant.PERMEATE_WEIGHT));
            }else if(!ApiConstant.BLANK_QUOTE.equalsIgnoreCase(targetStepJson.getString(ApiConstant.PERMEATE_TOTAL_KEY))){
                notesDescSetup.setPermeateTotal(getEndPointDeviceValue(ApiConstant.PERMEATE_TOTAL_ENDPOINT));
            }else{
                notesDescSetup.setDuration(TrialDurationManager.getInstance().getDurationFormatted());
            }
        }

        return notesDescSetup;
    }


}
