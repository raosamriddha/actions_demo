package org.lattice.spectrum_backend_final.dao.util;

import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.auxPumpSettingsGlobal;
import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.mainPumpGlobal;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.AuxPumpSetting;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.SystemSettingsManager;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;

public class Converter {

    /**
     * Stores system setting instance.
     */
    private static SystemSetting systemSetting;

    /**
     * Set regional format to ENGLISH for DecimalFormat.
     */
    public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);

    /**
     * Used to set decimal places.
     */
    private static DecimalFormat decimalFormat = new DecimalFormat(ApiConstant.LIVE_DATA_DECIMAL_FORMAT_PATTERN, symbols);

    public static SystemSetting getSystemSetting() {
        return systemSetting;
    }

    public static void setSystemSetting() throws SQLException {
        systemSetting = new SystemSettingsManager().fetchSystemSettings();
    }


    /**
     * @param value
     * @param convertedTo
     * @return
     */
    public static double unitConverter(double value, String convertedTo) {

        switch (convertedTo) {

            case ApiConstant.M_BAR:
                value /= 0.0145038;
                break;

            case ApiConstant.BAR:
                value /= 14.5038;
                break;

            case ApiConstant.KILOGRAM:
                value /= 1000;
                break;

            case ApiConstant.LITER:
                value /= 1000;
                break;

            case ApiConstant.INCH:
                value *= 2.54;
                break;

            case ApiConstant.MILLIMETER:
                value *= 10;
                break;

        }

        return Double.parseDouble(decimalFormat.format(value));

    }


    /**
     * System setting keys to pressure unit name mapper.
     *
     * @param key Takes system setting unit keys.
     * @return Name corresponding to system setting key.
     */
    public static String systemSettingPressureMapper(int key) {

        switch (key) {
            case 1:
                return ApiConstant.PSI;
            case 2:
                return ApiConstant.M_BAR;
            case 3:
                return ApiConstant.BAR;
            default:
                return ApiConstant.INVALID;
        }

    }

    /**
     * System setting keys to weight unit name mapper.
     *
     * @param key Takes system setting unit keys.
     * @return Name corresponding to system setting key.
     */
    public static String systemSettingWeightMapper(int key) {

        switch (key) {
            case 1:
                return ApiConstant.GRAM;
            case 2:
                return ApiConstant.KILOGRAM;
            default:
                return ApiConstant.INVALID;
        }

    }

/**
     * System setting keys to volume unit name mapper.
     *
     * @param key Takes system setting unit keys.
     * @return Name corresponding to system setting key.
     */

    public static String systemSettingVolumeMapper(int key) {

        switch (key) {
            case 1:
                return ApiConstant.MILLILITER;
            case 2:
                return ApiConstant.LITER;
            default:
                return ApiConstant.BLANK_QUOTE;
        }

    }

    public static String getAuxConverter(double value) throws SQLException {

        KonduitUtil konduitUtil = new KonduitUtil();
        KonduitType konduitType = konduitUtil.getKonduitCh1TypeFromSystemSetting();
        String totalizerUnit = null;
        if (systemSetting == null) {
            setSystemSetting();
        }
        if(systemSetting.getTotalizerUnit() == 0){
            totalizerUnit = ApiConstant.ML_MIN;
        }else{
            totalizerUnit = ApiConstant.LITER_PER_MIN;
            value /= 1000;
        }
        switch (konduitType){
            case pH:
                return DeviceManager.decimalFormatForPH.format(value);
            case UV:
                return DeviceManager.decimalFormatForUV.format(value)
                        +ApiConstant.BLANK_SPACE
                        +ApiConstant.UNIT_FOR_UV;
            case TURBIDITY:
                return DeviceManager.decimalFormat.format(value)
                        +ApiConstant.BLANK_SPACE
                        +ApiConstant.UNIT_FOR_TURBIDITY;
            case PROTEIN:
                return DeviceManager.decimalFormat.format(value)
                        +ApiConstant.BLANK_SPACE
                        +ApiConstant.UNIT_FOR_PROTEIN_CONCENTRATION;
            case TOTALIZER:
                return DeviceManager.decimalFormat.format(value)
                        +ApiConstant.BLANK_SPACE
                        +totalizerUnit;
            default:
                return value + ApiConstant.BLANK_QUOTE;
        }
    }


    public static String setDecimalPlace(double value, int decimalPlace) {

        decimalFormat.setMinimumFractionDigits(decimalPlace);
        decimalFormat.setMaximumFractionDigits(decimalPlace);

        return decimalFormat.format(value);
    }


    /**
     * Convert live data to system settings defined units.
     *
     * @param unConvertedLiveDataJson takes live data json.
     * @return Converted json according to system setting.
     */
    public static JSONObject convertLiveData(JSONObject unConvertedLiveDataJson) {

        JSONObject convertedLiveDataJson = new JSONObject(unConvertedLiveDataJson.toString());

        JSONObject recipeJson = DeviceManager.getInstance().getRecipeJson();
        String pressureUnit;
        String weightUnit;
        String volumeUnit;
        int pressureDecimalPlace;
        int weightDecimalPlace;
        int volumeDecimalPlace;
        int flowRateDecimalPlace;
        int loopCount;
        int totalAuxPump = 0;

        if (DeviceManager.getInstance().getModeType() == 0 && recipeJson != null) {

            totalAuxPump = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length();
        } else if (DeviceManager.getInstance().getModeType() == 1) {
            totalAuxPump = auxPumpSettingsGlobal.size();
        }


        try {

            if (systemSetting == null) {
                setSystemSetting();
            }

//            sLog.d("String", systemSetting);

            pressureUnit = systemSettingPressureMapper(systemSetting.getPressureUnit());
            pressureDecimalPlace = systemSetting.getDecimalPressure();

            if (!ApiConstant.PSI.equalsIgnoreCase(pressureUnit)) {

                convertedLiveDataJson.put(
                        ApiConstant.FEED_PRESSURE,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.FEED_PRESSURE), pressureUnit)
                );

                convertedLiveDataJson.put(
                        ApiConstant.PERMEATE_PRESSURE,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.PERMEATE_PRESSURE), pressureUnit)
                );

                convertedLiveDataJson.put(
                        ApiConstant.RETENTATE_PRESSURE,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.RETENTATE_PRESSURE), pressureUnit)
                );

                convertedLiveDataJson.put(
                        ApiConstant.TMP_PRESSURE,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.TMP_PRESSURE), pressureUnit)
                );
                convertedLiveDataJson.put(
                        ApiConstant.DELTA_P,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.DELTA_P), pressureUnit)
                );
            }

            // set decimal place for pressure.
            convertedLiveDataJson.put(
                    ApiConstant.FEED_PRESSURE,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.FEED_PRESSURE), pressureDecimalPlace)
            );
            convertedLiveDataJson.put(
                    ApiConstant.PERMEATE_PRESSURE,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.PERMEATE_PRESSURE), pressureDecimalPlace)
            );

            convertedLiveDataJson.put(
                    ApiConstant.RETENTATE_PRESSURE,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.RETENTATE_PRESSURE), pressureDecimalPlace)
            );

            if (convertedLiveDataJson.has(ApiConstant.TMP_PRESSURE)) {
                convertedLiveDataJson.put(
                        ApiConstant.TMP_PRESSURE,
                        setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.TMP_PRESSURE), pressureDecimalPlace)
                );
            } else {
                convertedLiveDataJson.put(
                        ApiConstant.TMP_PRESSURE,
                        setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.TMP), pressureDecimalPlace)
                );
            }

            convertedLiveDataJson.put(
                    ApiConstant.DELTA_P,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.DELTA_P), pressureDecimalPlace)
            );


            weightUnit = systemSettingWeightMapper(systemSetting.getWeightUnit());
            weightDecimalPlace = systemSetting.getDecimalWeight();

            if (!ApiConstant.GRAM.equalsIgnoreCase(weightUnit)) {

                convertedLiveDataJson.put(
                        ApiConstant.FEED_SCALE,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.FEED_SCALE), weightUnit)
                );

                convertedLiveDataJson.put(
                        ApiConstant.TOTAL_PERM_WEIGHT,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.TOTAL_PERM_WEIGHT), weightUnit)
                );

                convertedLiveDataJson.put(
                        ApiConstant.M_PERMEATE,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.M_PERMEATE), weightUnit)
                );


            }

            // set decimal places for weight
            convertedLiveDataJson.put(
                    ApiConstant.FEED_SCALE,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.FEED_SCALE), weightDecimalPlace)
            );

            convertedLiveDataJson.put(
                    ApiConstant.TOTAL_PERM_WEIGHT,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.TOTAL_PERM_WEIGHT), weightDecimalPlace)
            );
            convertedLiveDataJson.put(
                    ApiConstant.M_PERMEATE,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.M_PERMEATE), weightDecimalPlace)
            );


            volumeUnit = systemSettingVolumeMapper(systemSetting.getVolumeUnit());
            volumeDecimalPlace = systemSetting.getDecimalVolume();

            if (ApiConstant.LITER.equalsIgnoreCase(volumeUnit)) {

                convertedLiveDataJson.put(
                        ApiConstant.PERMEATE_TOTAL_KEY,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.PERMEATE_TOTAL_KEY), volumeUnit)
                );
                convertedLiveDataJson.put(
                        ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP,
                        unitConverter(unConvertedLiveDataJson.getDouble(ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP), volumeUnit)
                );


            }

            // set decimal places for volume
            convertedLiveDataJson.put(
                    ApiConstant.PERMEATE_TOTAL_KEY,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.PERMEATE_TOTAL_KEY), volumeDecimalPlace)
            );
            convertedLiveDataJson.put(
                    ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP,
                    setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP), volumeDecimalPlace)
            );




            flowRateDecimalPlace = systemSetting.getDecimalFlowRate();


            if ((DeviceManager.getInstance().getModeType() == 0) && recipeJson != null) {

                loopCount = 1;
                // If aux pump type is IP than convert its unit from ml/min to L/min
                while (loopCount <= totalAuxPump) {

                    JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount - 1);
                    double convertedFlowRate = convertedLiveDataJson.getDouble(ApiConstant.AUX_PUMP_ + loopCount + ApiConstant.UNDER_SCORE + ApiConstant.FLOW_RATE);

                    if (ApiConstant.IP.equalsIgnoreCase(auxJson.getString(ApiConstant.AUX_PUMP_TYPE))) {

                        convertedFlowRate /= 1000;

                    }
                    convertedLiveDataJson.put(
                            ApiConstant.AUX_PUMP_ + loopCount + ApiConstant.UNDER_SCORE + ApiConstant.FLOW_RATE,
                            DeviceManager.decimalFormat.format(convertedFlowRate));

                    loopCount++;

                }


                // if pump is KMPi or FS500 than convert ml/min to l/min.
                if (
                        ApiConstant.KMPI.equalsIgnoreCase(recipeJson.getString(ApiConstant.PUMP_NAME)) ||
                                ApiConstant.KROSFLOFS500.equalsIgnoreCase(recipeJson.getString(ApiConstant.PUMP_NAME))

                ) {
                    double convertedMainPumpFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE) / 1000);
                    double convertedFeedFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.FEED_FLOW_RATE) / 1000);
                    double convertedPermeateFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.PERMEATE_FLOW_RATE) / 1000);
                    double convertedRetentateFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.RETENTATE_FLOW_RATE) / 1000);

                    convertedLiveDataJson.put(
                            ApiConstant.MAIN_PUMP_FLOW_RATE, String.valueOf(convertedMainPumpFlowRate)
                    );
                    convertedLiveDataJson.put(
                            ApiConstant.FEED_FLOW_RATE, String.valueOf(convertedFeedFlowRate)
                    );
                    convertedLiveDataJson.put(
                            ApiConstant.PERMEATE_FLOW_RATE, String.valueOf(convertedPermeateFlowRate)
                    );
                    convertedLiveDataJson.put(
                            ApiConstant.RETENTATE_FLOW_RATE, String.valueOf(convertedRetentateFlowRate)
                    );

                    convertedLiveDataJson = setFlowRateDecimalPlaces(convertedLiveDataJson, flowRateDecimalPlace);
                    DeviceManager.getInstance().setSpeedAndFlowRateIndicator(
                            DeviceManager.decimalFormat.format(convertedMainPumpFlowRate) +
                                    ApiConstant.LITER_PER_MIN

                    );
                } else {

                    convertedLiveDataJson = setFlowRateDecimalPlaces(convertedLiveDataJson, flowRateDecimalPlace);
                    DeviceManager.getInstance().setSpeedAndFlowRateIndicator(
                            convertedLiveDataJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE) +
                                    ApiConstant.ML_MIN

                    );

                }
            } else if (DeviceManager.getInstance().getModeType() == 1) {
                if (totalAuxPump > 0) {
                    loopCount = 1;
                    // If aux pump type is IP than convert its unit from ml/min to L/min
                    // todo aux - done
                    while (loopCount <= totalAuxPump) {
                        AuxPumpSetting auxPumpSetting = auxPumpSettingsGlobal.get(loopCount - 1);
                        double convertedFlowRate = convertedLiveDataJson.getDouble(ApiConstant.AUX_PUMP_ + (auxPumpSetting.getType() + 1) + ApiConstant.UNDER_SCORE + ApiConstant.FLOW_RATE);
                        if (ApiConstant.IP.equalsIgnoreCase(auxPumpSetting.getAuxPumpType())) {
                            convertedFlowRate /= 1000;
                        }

                        convertedLiveDataJson.put(
                        		ApiConstant.AUX_PUMP_ + (auxPumpSetting.getType() + 1) + ApiConstant.UNDER_SCORE + ApiConstant.FLOW_RATE,
                                DeviceManager.decimalFormat.format(convertedFlowRate));
                        loopCount++;

                    }
                }


                // if pump is KMPi or FS500 than convert ml/min to l/min.
                if (mainPumpGlobal != null) {
                    if (
                            ApiConstant.KMPI.equalsIgnoreCase(mainPumpGlobal.getPumpName()) ||
                                    ApiConstant.KROSFLOFS500.equalsIgnoreCase(mainPumpGlobal.getPumpName())

                    ) {

                        double convertedMainPumpFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE) / 1000);
                        double convertedFeedFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.FEED_FLOW_RATE) / 1000);
                        double convertedPermeateFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.PERMEATE_FLOW_RATE) / 1000);
                        double convertedRetentateFlowRate = (unConvertedLiveDataJson.getDouble(ApiConstant.RETENTATE_FLOW_RATE) / 1000);
                        convertedLiveDataJson.put(
                                ApiConstant.MAIN_PUMP_FLOW_RATE, String.valueOf(convertedMainPumpFlowRate)
                        );
                        convertedLiveDataJson.put(
                                ApiConstant.FEED_FLOW_RATE, String.valueOf(convertedFeedFlowRate)
                        );
                        convertedLiveDataJson.put(
                                ApiConstant.PERMEATE_FLOW_RATE, String.valueOf(convertedPermeateFlowRate)
                        );
                        convertedLiveDataJson.put(
                                ApiConstant.RETENTATE_FLOW_RATE, String.valueOf(convertedRetentateFlowRate)
                        );


                        convertedLiveDataJson = setFlowRateDecimalPlaces(convertedLiveDataJson, flowRateDecimalPlace);
                        if (isSpeedSetToDevice()) {
                            DeviceManager.getInstance().resetFlowRateIndicator();
                        } else {

                            DeviceManager.getInstance().setSpeedAndFlowRateIndicator(
                                    DeviceManager.decimalFormat.format(convertedMainPumpFlowRate) +
                                            ApiConstant.LITER_PER_MIN
                            );
                        }
                    } else {

                        convertedLiveDataJson = setFlowRateDecimalPlaces(convertedLiveDataJson, flowRateDecimalPlace);
                        if (isSpeedSetToDevice()) {
                            DeviceManager.getInstance().resetFlowRateIndicator();
                        } else {
                            DeviceManager.getInstance().setSpeedAndFlowRateIndicator(
                                    convertedLiveDataJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE) +
                                            ApiConstant.ML_MIN
                            );
                        }

                    }
                }

            }
            try{

                if (systemSetting.getTotalizerMin() != null) {
                    double convertedTotalizerValue = unConvertedLiveDataJson.getDouble(ApiConstant.KONDUIT_CH_1);
                    if (systemSetting.getTotalizerUnit() == 1) {
                        convertedTotalizerValue /= 1000;
                    }
                    convertedLiveDataJson.put(
                            ApiConstant.KONDUIT_CH_1,
                            setDecimalPlace(convertedTotalizerValue, flowRateDecimalPlace)
                    );
                }
                if(systemSetting.getTotalizerMin_2() != null){
                    double convertedTotalizerValue = unConvertedLiveDataJson.getDouble(ApiConstant.KONDUIT_CH_2);
                    if (systemSetting.getTotalizerUnit_2() == 1) {
                        convertedTotalizerValue /= 1000;
                    }
                    convertedLiveDataJson.put(
                            ApiConstant.KONDUIT_CH_2,
                            setDecimalPlace(convertedTotalizerValue, flowRateDecimalPlace)
                    );

                }
            }catch (Exception ex){
                BasicUtility.systemPrint("Exception Handled : "+ ex.getMessage());
            }


            // Set N/A for not in use value
            convertedLiveDataJson = setUnUsedFieldToNA(convertedLiveDataJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertedLiveDataJson;
    }

    public static JSONObject setFlowRateDecimalPlaces(JSONObject convertedLiveDataJson, int flowRateDecimalPlace) {

        // set decimal format for flow rate.
        convertedLiveDataJson.put(
                ApiConstant.MAIN_PUMP_FLOW_RATE,
                setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE), flowRateDecimalPlace)
        );
        convertedLiveDataJson.put(
                ApiConstant.FEED_FLOW_RATE,
                setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.FEED_FLOW_RATE), flowRateDecimalPlace)
        );
        convertedLiveDataJson.put(
                ApiConstant.PERMEATE_FLOW_RATE,
                setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.PERMEATE_FLOW_RATE), flowRateDecimalPlace)
        );
        convertedLiveDataJson.put(
                ApiConstant.RETENTATE_FLOW_RATE,
                setDecimalPlace(convertedLiveDataJson.getDouble(ApiConstant.RETENTATE_FLOW_RATE), flowRateDecimalPlace)
        );
        convertedLiveDataJson.put(
                ApiConstant.AUX_PUMP_1_FLOW_RATE,
                DeviceManager.decimalFormat.format(convertedLiveDataJson.getDouble(ApiConstant.AUX_PUMP_1_FLOW_RATE))
        );
        convertedLiveDataJson.put(
                ApiConstant.AUX_PUMP_2_FLOW_RATE,
                DeviceManager.decimalFormat.format(convertedLiveDataJson.getDouble(ApiConstant.AUX_PUMP_2_FLOW_RATE))
        );




        return convertedLiveDataJson;

    }

    public static boolean isSpeedSetToDevice() {
        if (mainPumpGlobal.getSpeed() != null && !mainPumpGlobal.getSpeed().isEmpty()) {
            return true;
        }
        return false;
    }




    public static JSONObject setUnUsedFieldToNA(JSONObject convertedLiveDataJson){
        JSONObject recipeJson = DeviceManager.getInstance().getRecipeJson();
        if(DeviceManager.getInstance().getModeType() == 0 && recipeJson != null){
            if(systemSetting.getTotalizerMin() != null){
                if(
                        recipeJson.getInt(ApiConstant.END_POINT_CAL) == 1 &&
                                !ComLib.get().getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)
                ){
                    convertedLiveDataJson.put(
                            ApiConstant.M_PERMEATE,
                            ApiConstant.NOT_APPLICABLE
                    );
                    convertedLiveDataJson.put(
                            ApiConstant.TOTAL_PERM_WEIGHT,
                            ApiConstant.NOT_APPLICABLE
                    );
                }
            }else{
                convertedLiveDataJson.put(
                        ApiConstant.PERMEATE_TOTAL_KEY,
                        ApiConstant.NOT_APPLICABLE
                );
                convertedLiveDataJson.put(
                        ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP,
                        ApiConstant.NOT_APPLICABLE
                );
            }
        }
        return convertedLiveDataJson;
    }

}
