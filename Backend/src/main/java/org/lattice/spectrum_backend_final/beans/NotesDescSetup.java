package org.lattice.spectrum_backend_final.beans;

public class NotesDescSetup {

    private long trialLogId;
    private Double tmp;
    private String duration;
    private Double permeateWeight;
    private Double permeateTotal;
    private Double flowRate;
    private int step;
    private int status;
    private String modeName;

    public NotesDescSetup() {
    }

    public NotesDescSetup(long trialLogId, Double tmp, String duration, Double permeateWeight, Double permeateTotal, Double flowRate, int stepNo, int status, String modeName) {
        this.trialLogId = trialLogId;
        this.tmp = tmp;
        this.duration = duration;
        this.permeateWeight = permeateWeight;
        this.permeateTotal = permeateTotal;
        this.flowRate = flowRate;
        this.step = stepNo;
        this.status = status;
        this.modeName = modeName;
    }

    public long getTrialLogId() {
        return trialLogId;
    }

    public void setTrialLogId(long trialLogId) {
        this.trialLogId = trialLogId;
    }

    public Double getTmp() {
        return tmp;
    }

    public void setTmp(Double tmp) {
        this.tmp = tmp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPermeateWeight() {
        return permeateWeight;
    }

    public void setPermeateWeight(Double permeateWeight) {
        this.permeateWeight = permeateWeight;
    }

    public Double getPermeateTotal() {
        return permeateTotal;
    }

    public void setPermeateTotal(Double permeateTotal) {
        this.permeateTotal = permeateTotal;
    }

    public Double getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(Double flowRate) {
        this.flowRate = flowRate;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    @Override
    public String toString() {
        return "NotesDescSetup{" +
                "trialLogId=" + trialLogId +
                ", tmp=" + tmp +
                ", duration=" + duration +
                ", permeateWeight=" + permeateWeight +
                ", permeateTotal=" + permeateTotal +
                ", flowRate=" + flowRate +
                ", stepNo=" + step +
                ", status=" + status +
                ", modeName='" + modeName + '\'' +
                '}';
    }
}
