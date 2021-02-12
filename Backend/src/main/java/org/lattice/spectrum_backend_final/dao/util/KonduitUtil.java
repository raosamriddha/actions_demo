package org.lattice.spectrum_backend_final.dao.util;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowMeterManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.KonduitProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.SystemSettingsManager;

import java.sql.SQLException;

public class KonduitUtil {

    public boolean isKonduitEndPoint(JSONObject endPointJson) {
        if (
                ApiConstant.TURBIDITY.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE)) ||
                        ApiConstant.PH.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE)) ||
                        ApiConstant.PROTEIN_CONCENTRATION.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE)) ||
                        ApiConstant.PERMEATE_TOTAL_ENDPOINT.equalsIgnoreCase(endPointJson.getString(ApiConstant.END_POINT_TYPE))
        ) {
            return true;
        }
        return false;
    }


    public KonduitType getKonduitCh1TypeFromSystemSetting() throws SQLException {
        SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();
        KonduitType konduitType = null;

        if (systemSetting.getIsAUXkonduit() == 0) {
            konduitType = KonduitType.UV;
        } else if (systemSetting.getPhMin() != null) {
            konduitType = KonduitType.pH;
        } else if (systemSetting.getTurbidityMin() != null) {
            konduitType = KonduitType.TURBIDITY;
        } else if (systemSetting.getTotalizerMin() != null) {
            konduitType = KonduitType.TOTALIZER;
        } else if (systemSetting.getProteinConcMin() != null) {
            konduitType = KonduitType.PROTEIN;
        }
        return konduitType;
    }

    public KonduitType getKonduitType(String endPoint) {
        switch (endPoint) {

            case ApiConstant.TURBIDITY:
                return KonduitType.TURBIDITY;

            case ApiConstant.PH:
                return KonduitType.pH;

            case ApiConstant.UV:
                return KonduitType.UV;

            case ApiConstant.PROTEIN_CONCENTRATION:
                return KonduitType.PROTEIN;

            case ApiConstant.PERMEATE_TOTAL_ENDPOINT:
                return KonduitType.TOTALIZER;

            default:
                return null;
        }
    }

    public boolean isTotalizerMode() {
        if (
                ComLib.get().getKonduitInfo().isKFConduitConnected() &&
                        DeviceManager.getInstance().getRecipeJson() != null &&
                        DeviceManager.getInstance().getRecipeJson().getInt(ApiConstant.END_POINT_CAL) == 1

        ) {
            return true;
        }
        return false;
    }

    public KonduitProp getKonduitChannelProp(int channel) throws SQLException {
        SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();
        KonduitType konduitType = null;
        KonduitProp konduitProp = null;
        String endPoint = null;
        Double xValue = 0.0;
        Double yValue = 0.0;

        if (channel == 1) {

            if (systemSetting.getPhMin() != null) {
                konduitType = KonduitType.pH;
                endPoint = ApiConstant.PH;
            } else if (systemSetting.getTurbidityMin() != null) {
                konduitType = KonduitType.TURBIDITY;
                endPoint = ApiConstant.TURBIDITY;
            } else if (systemSetting.getProteinConcMin() != null) {
                konduitType = KonduitType.PROTEIN;
                endPoint = ApiConstant.PROTEIN_CONCENTRATION;
            } else if (systemSetting.getTotalizerMin() != null) {
                konduitType = KonduitType.TOTALIZER;
                endPoint = ApiConstant.TOTALIZER;
            } else {
                return new KonduitProp(KonduitType.UV, KonduitChannel.CHANNEL1, 1d, 0d);
            }
            xValue = EndPointUtil.getInstance().getXValue(endPoint, 1);
            yValue = EndPointUtil.getInstance().getYValue(endPoint, 1);
            konduitProp = new KonduitProp(konduitType, KonduitChannel.CHANNEL1, xValue, yValue);

        } else {

            if (systemSetting.getPhMin_2() != null) {
                konduitType = KonduitType.pH;
                endPoint = ApiConstant.PH;
            } else if (systemSetting.getTurbidityMin_2() != null) {
                konduitType = KonduitType.TURBIDITY;
                endPoint = ApiConstant.TURBIDITY;
            } else if (systemSetting.getProteinConcMin_2() != null) {
                konduitType = KonduitType.PROTEIN;
                endPoint = ApiConstant.PROTEIN_CONCENTRATION;
            } else if (systemSetting.getTotalizerMin_2() != null) {
                konduitType = KonduitType.TOTALIZER;
                endPoint = ApiConstant.TOTALIZER;
            } else {
                return new KonduitProp(KonduitType.UV, KonduitChannel.CHANNEL2, 1d, 0d);
            }
            xValue = EndPointUtil.getInstance().getXValue(endPoint, 2);
            yValue = EndPointUtil.getInstance().getYValue(endPoint, 2);
            konduitProp = new KonduitProp(konduitType, KonduitChannel.CHANNEL2, xValue, yValue);
        }


        return konduitProp;
    }

    public double getTotalizerFlowrate() throws SQLException {
        double totalizerFlowRate ;
        totalizerFlowRate = ComLib.get().getKonduitInfo()
                .getProbeUV_AU(0) * EndPointUtil.getInstance()
                .getXValue(ApiConstant.TOTALIZER, 1) + EndPointUtil.getInstance()
                .getYValue(ApiConstant.TOTALIZER, 1);
        return totalizerFlowRate;
    }

    public KonduitProp[] getKonduitProp() throws SQLException {
        KonduitProp[] konduitProps = new KonduitProp[2];

        konduitProps[0] = getKonduitChannelProp(1);
        konduitProps[1] = getKonduitChannelProp(2);
        sLog.d(this, konduitProps);
        return konduitProps;
    }

    public String getKonduitTypeToDeviceText(KonduitType konduitType) {
        switch (konduitType) {
            case pH:
                return ApiConstant.PH;
            case PROTEIN:
                return ApiConstant.PROTEIN_CONCENTRATION_DEVICE_TEXT;
            case TOTALIZER:
                return ApiConstant.TOTALIZER_DEVICE_TEXT;
            case TURBIDITY:
                return ApiConstant.TURBIDITY_DEVICE_TEXT;
            case UV:
                return ApiConstant.UV;
            default:
                return ApiConstant.BLANK_QUOTE;
        }
    }

    public String getKonduitText(KonduitType konduitType) {
        switch (konduitType) {
            case pH:
                return ApiConstant.PH;
            case PROTEIN:
                return ApiConstant.PROTEIN_CONCENTRATION;
            case TOTALIZER:
                return ApiConstant.FLOW_METER;
            case TURBIDITY:
                return ApiConstant.TURBIDITY;
            case UV:
                return ApiConstant.UV;
            default:
                return ApiConstant.BLANK_QUOTE;
        }
    }

    public int getKonduitTypeId(KonduitType konduitType) {
        int konduitTypeId;
        switch (konduitType) {
            case pH:
                konduitTypeId = ApiConstant.CHANNEL_1_PH_TYPE;
                break;
            case UV:
                konduitTypeId = ApiConstant.CHANNEL_1_UV_TYPE;
                break;
            case TURBIDITY:
                konduitTypeId = ApiConstant.CHANNEL_1_TURBIDITY_TYPE;
                break;
            case PROTEIN:
                konduitTypeId = ApiConstant.CHANNEL_1_PROTEIN_CONC_TYPE;
                break;
            case TOTALIZER:
                konduitTypeId = ApiConstant.CHANNEL_1_TOTALIZER_TYPE;
                break;
            default:
                konduitTypeId = ApiConstant.CHANNEL_2_UV_TYPE;
        }
        return konduitTypeId;
    }

    private double getKonduitValue(String endPoint, int channelId, int probId) throws SQLException {
        return ComLib.get().getKonduitInfo()
                .getProbeUV_AU(probId) * EndPointUtil.getInstance()
                .getXValue(endPoint, channelId) + EndPointUtil.getInstance()
                .getYValue(endPoint, channelId);
    }

    private String getKonduitCalculatedValue(int channelId, SystemSetting systemSetting) throws SQLException {
        double konduitValue = 0;
        String konduitStringValue = "0";
        if (channelId == 1) {
            if (systemSetting.getPhMin() != null) {
                konduitValue = getKonduitValue(ApiConstant.PH, channelId, 0);
                konduitStringValue = DeviceManager.decimalFormatForPH.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh1Type(ApiConstant.CHANNEL_1_PH_TYPE);
            } else if (systemSetting.getTurbidityMin() != null) {
                konduitValue = getKonduitValue(ApiConstant.TURBIDITY, channelId, 0);
                konduitStringValue = DeviceManager.decimalFormat.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh1Type(ApiConstant.CHANNEL_1_TURBIDITY_TYPE);
            } else if (systemSetting.getTotalizerMin() != null) {
                konduitValue = getKonduitValue(ApiConstant.TOTALIZER, channelId, 0);
                konduitStringValue = String.valueOf(konduitValue);
                DeviceManager.getInstance().setKonduitCh1Type(ApiConstant.CHANNEL_1_TOTALIZER_TYPE);
            } else if (systemSetting.getProteinConcMin() != null) {
                konduitValue = getKonduitValue(ApiConstant.PROTEIN_CONCENTRATION, channelId, 0);
                konduitStringValue = DeviceManager.decimalFormat.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh1Type(ApiConstant.CHANNEL_1_PROTEIN_CONC_TYPE);
            }
        } else {
            if (systemSetting.getPhMin_2() != null) {
                konduitValue = getKonduitValue(ApiConstant.PH, channelId, 1);
                konduitStringValue = DeviceManager.decimalFormatForPH.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh2Type(ApiConstant.CHANNEL_2_PH_TYPE);
            } else if (systemSetting.getTurbidityMin_2() != null) {
                konduitValue = getKonduitValue(ApiConstant.TURBIDITY, channelId, 1);
                konduitStringValue = DeviceManager.decimalFormat.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh2Type(ApiConstant.CHANNEL_2_TURBIDITY_TYPE);
            } else if (systemSetting.getTotalizerMin_2() != null) {
                konduitValue = getKonduitValue(ApiConstant.TOTALIZER, channelId, 1);
                konduitStringValue = String.valueOf(konduitValue);
                DeviceManager.getInstance().setKonduitCh2Type(ApiConstant.CHANNEL_2_TOTALIZER_TYPE);
            } else if (systemSetting.getProteinConcMin_2() != null) {
                konduitValue = getKonduitValue(ApiConstant.PROTEIN_CONCENTRATION, channelId, 1);
                konduitStringValue = DeviceManager.decimalFormat.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh2Type(ApiConstant.CHANNEL_2_PROTEIN_CONC_TYPE);
            }
        }
        return konduitStringValue;
    }

    public String getKonduitReading(int ChannelId) throws SQLException {

        double konduitValue = 0;
        String konduitStringValue = "0";
        SystemSetting systemSetting = Converter.getSystemSetting();
        KonduitUtil konduitUtil = new KonduitUtil();

        if (ChannelId == 1) {
            if (systemSetting.getIsAUXkonduit() == 1) {
                // check if mode is auto
                if (DeviceManager.getInstance().getModeType() == 0) {
                    if (DeviceManager.getInstance().getModeProp().getTotalizerMode() == TotalizerMode.ENDPOINT) {
                        konduitValue = FlowMeterManager.get().getCurrentFlowRate(0);
                        konduitStringValue = String.valueOf(konduitValue);
                        DeviceManager.getInstance().setKonduitCh1Type(konduitUtil.getKonduitTypeId(DeviceManager.getInstance().getModeProp().getKonduit(0).getType()));
                    } else {
                        konduitStringValue = getKonduitCalculatedValue(1, systemSetting);
                    }
                }
                //check if mode is manual
                else if (DeviceManager.getInstance().getModeType() == 1) {
                    konduitStringValue = getKonduitCalculatedValue(1, systemSetting);
                }
            } else {
                konduitValue = ComLib.get().getKonduitInfo().getProbeUV_AU(0);
                konduitStringValue = DeviceManager.decimalFormatForUV.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh1Type(ApiConstant.CHANNEL_1_UV_TYPE);
            }
        } else {

            if (systemSetting.getIsAUXkonduit_2() == 1) {
                konduitStringValue = getKonduitCalculatedValue(2, systemSetting);
            } else {
                konduitValue = ComLib.get().getKonduitInfo().getProbeUV_AU(1);
                konduitStringValue = DeviceManager.decimalFormatForUV.format(konduitValue);
                DeviceManager.getInstance().setKonduitCh2Type(ApiConstant.CHANNEL_2_UV_TYPE);
            }
        }
        return konduitStringValue;
    }

}
