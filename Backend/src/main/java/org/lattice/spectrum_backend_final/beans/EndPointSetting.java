package org.lattice.spectrum_backend_final.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;


/**
 * Used to store and manipulate end_point.
 */
public class EndPointSetting {

    @JsonProperty(ApiConstant.END_POINT_ID)
    private long endPointSettingId;

    @JsonProperty(ApiConstant.END_POINT_TYPE)
    private String endPointType;

    @JsonProperty(ApiConstant.END_POINT_VALUE)
    private double endPointValue;

    @JsonProperty(ApiConstant.STEP_NUMBER)
    private int stepNo;

    @JsonProperty(ApiConstant.IS_ACTIVE)
    private String isActive;

    @JsonProperty(ApiConstant.TRIAL_MASTER_ID)
    private int trialMasterId;

    public EndPointSetting() {
    }


    public EndPointSetting(long endPointSettingId, String endPointType, double endPointValue, int stepNo, String isActive, int trialMasterId) {
        this.endPointSettingId = endPointSettingId;
        this.endPointType = endPointType;
        this.endPointValue = endPointValue;
        this.stepNo = stepNo;
        this.isActive = isActive;
        this.trialMasterId = trialMasterId;
    }

    public long getEndPointSettingId() {
        return endPointSettingId;
    }

    public void setEndPointSettingId(long endPointSettingId) {
        this.endPointSettingId = endPointSettingId;
    }

    public String getEndPointType() {
        return endPointType;
    }

    public void setEndPointType(String endPointType) {
        this.endPointType = endPointType;
    }

    public double getEndPointValue() {
        return endPointValue;
    }

    public void setEndPointValue(double endPointValue) {
        this.endPointValue = endPointValue;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getTrialMasterId() {
        return trialMasterId;
    }

    public void setTrialMasterId(int trialMasterId) {
        this.trialMasterId = trialMasterId;
    }

    @Override
    public String toString() {
        return "EndPointSetting{" +
                "endPointSettingId=" + endPointSettingId +
                ", endPointType='" + endPointType + '\'' +
                ", endPointValue=" + endPointValue +
                ", stepNo=" + stepNo +
                ", isActive='" + isActive + '\'' +
                ", trialMasterId=" + trialMasterId +
                '}';
    }
}
