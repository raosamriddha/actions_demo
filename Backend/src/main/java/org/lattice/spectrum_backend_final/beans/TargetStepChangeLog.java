package org.lattice.spectrum_backend_final.beans;

public class TargetStepChangeLog {

    private long targetStepChangeLogId;
    private long trialLogId;
    private String oldTmp;
    private String newTmp;
    private String oldDuration;
    private String newDuration;
    private String oldPermeateWeight;
    private String newPermeateWeight;
    private String oldPermeateTotal;
    private String newPermeateTotal;
    private int stepNo;

    public TargetStepChangeLog() {
    }


    public TargetStepChangeLog(long targetStepChangeLogId, long trialLogId, String oldTmp, String newTmp, String oldDuration, String newDuration, String oldPermeateWeight, String newPermeateWeight, String oldPermeateTotal, String newPermeateTotal, int stepNo) {
        this.targetStepChangeLogId = targetStepChangeLogId;
        this.trialLogId = trialLogId;
        this.oldTmp = oldTmp;
        this.newTmp = newTmp;
        this.oldDuration = oldDuration;
        this.newDuration = newDuration;
        this.oldPermeateWeight = oldPermeateWeight;
        this.newPermeateWeight = newPermeateWeight;
        this.oldPermeateTotal = oldPermeateTotal;
        this.newPermeateTotal = newPermeateTotal;
        this.stepNo = stepNo;
    }

    public long getTargetStepChangeLogId() {
        return targetStepChangeLogId;
    }

    public void setTargetStepChangeLogId(long targetStepChangeLogId) {
        this.targetStepChangeLogId = targetStepChangeLogId;
    }

    public long getTrialLogId() {
        return trialLogId;
    }

    public void setTrialLogId(long trialLogId) {
        this.trialLogId = trialLogId;
    }

    public String getOldTmp() {
        return oldTmp;
    }

    public void setOldTmp(String oldTmp) {
        this.oldTmp = oldTmp;
    }

    public String getNewTmp() {
        return newTmp;
    }

    public void setNewTmp(String newTmp) {
        this.newTmp = newTmp;
    }

    public String getOldDuration() {
        return oldDuration;
    }

    public void setOldDuration(String oldDuration) {
        this.oldDuration = oldDuration;
    }

    public String getNewDuration() {
        return newDuration;
    }

    public void setNewDuration(String newDuration) {
        this.newDuration = newDuration;
    }

    public String getOldPermeateWeight() {
        return oldPermeateWeight;
    }

    public void setOldPermeateWeight(String oldPermeateWeight) {
        this.oldPermeateWeight = oldPermeateWeight;
    }

    public String getNewPermeateWeight() {
        return newPermeateWeight;
    }

    public void setNewPermeateWeight(String newPermeateWeight) {
        this.newPermeateWeight = newPermeateWeight;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    public String getOldPermeateTotal() {
        return oldPermeateTotal;
    }

    public void setOldPermeateTotal(String oldPermeateTotal) {
        this.oldPermeateTotal = oldPermeateTotal;
    }

    public String getNewPermeateTotal() {
        return newPermeateTotal;
    }

    public void setNewPermeateTotal(String newPermeateTotal) {
        this.newPermeateTotal = newPermeateTotal;
    }

    @Override
    public String toString() {
        return "TargetStepChangeLog{" +
                "targetStepChangeLogId=" + targetStepChangeLogId +
                ", trialLogId=" + trialLogId +
                ", oldTmp='" + oldTmp + '\'' +
                ", newTmp='" + newTmp + '\'' +
                ", oldDuration='" + oldDuration + '\'' +
                ", newDuration='" + newDuration + '\'' +
                ", oldPermeateWeight='" + oldPermeateWeight + '\'' +
                ", newPermeateWeight='" + newPermeateWeight + '\'' +
                ", oldPermeateTotal='" + oldPermeateTotal + '\'' +
                ", newPermeateTotal='" + newPermeateTotal + '\'' +
                ", stepNo=" + stepNo +
                '}';
    }
}
