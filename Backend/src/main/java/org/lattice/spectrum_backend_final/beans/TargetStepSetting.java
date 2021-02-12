package org.lattice.spectrum_backend_final.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

public class TargetStepSetting {

    @JsonProperty(ApiConstant.TARGET_STEP_ID)
    private long targetStepSettingId;

    @JsonProperty(ApiConstant.TRIAL_MASTER_ID)
    private long trialMasterId;

    @JsonProperty(ApiConstant.TMP)
    private String tmp;

    @JsonProperty(ApiConstant.DURATION)
    private String duration;

    @JsonProperty(ApiConstant.FLOW_RATE)
    private String flowRate;

    @JsonProperty(ApiConstant.PERM_WT)
    private String permeateWeight;

    @JsonProperty(ApiConstant.IS_ACTIVE)
    private String isActive;

    @JsonProperty(ApiConstant.PERMEATE_TOTAL_KEY)
    private String permeateTotal;

    public TargetStepSetting() {
    }

    public TargetStepSetting(long targetStepSettingId, long trialMasterId, String tmp, String duration, String flowRate, String permeateWeight, String isActive, String permeateTotal) {
        this.targetStepSettingId = targetStepSettingId;
        this.trialMasterId = trialMasterId;
        this.tmp = tmp;
        this.duration = duration;
        this.flowRate = flowRate;
        this.permeateWeight = permeateWeight;
        this.isActive = isActive;
        this.permeateTotal = permeateTotal;
    }

    public long getTargetStepSettingId() {
        return targetStepSettingId;
    }

    public void setTargetStepSettingId(long targetStepSettingId) {
        this.targetStepSettingId = targetStepSettingId;
    }

    public long getTrialMasterId() {
        return trialMasterId;
    }

    public void setTrialMasterId(long trialMasterId) {
        this.trialMasterId = trialMasterId;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(String flowRate) {
        this.flowRate = flowRate;
    }

    public String getPermeateWeight() {
        return permeateWeight;
    }

    public void setPermeateWeight(String permeateWeight) {
        this.permeateWeight = permeateWeight;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getPermeateTotal() {
        return permeateTotal;
    }

    public void setPermeateTotal(String permeateTotal) {
        this.permeateTotal = permeateTotal;
    }

    @Override
    public String toString() {
        return "TargetStepSetting{" +
                "targetStepSettingId=" + targetStepSettingId +
                ", trialMasterId=" + trialMasterId +
                ", tmp='" + tmp + '\'' +
                ", duration='" + duration + '\'' +
                ", flowRate='" + flowRate + '\'' +
                ", permeateWeight='" + permeateWeight + '\'' +
                ", isActive='" + isActive + '\'' +
                ", permeateTotal='" + permeateTotal + '\'' +
                '}';
    }
}
