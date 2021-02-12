package org.lattice.spectrum_backend_final.beans;

public class EndPointChangeLog {

    private long endPointChangeLogId;
    private long trialLogId;
    private String endPointType;
    private double oldEndPointValue;
    private double newEndPointValue;
    private int stepNo;


    public EndPointChangeLog() {
    }

    public EndPointChangeLog(long endPointChangeLogId, long trialLogId, String endPointType, double oldEndPointValue, double newEndPointValue, int stepNo) {
        this.endPointChangeLogId = endPointChangeLogId;
        this.trialLogId = trialLogId;
        this.endPointType = endPointType;
        this.oldEndPointValue = oldEndPointValue;
        this.newEndPointValue = newEndPointValue;
        this.stepNo = stepNo;
    }

    public long getEndPointChangeLogId() {
        return endPointChangeLogId;
    }

    public void setEndPointChangeLogId(long endPointChangeLogId) {
        this.endPointChangeLogId = endPointChangeLogId;
    }

    public long getTrialLogId() {
        return trialLogId;
    }

    public void setTrialLogId(long trialLogId) {
        this.trialLogId = trialLogId;
    }

    public String getEndPointType() {
        return endPointType;
    }

    public void setEndPointType(String endPointType) {
        this.endPointType = endPointType;
    }

    public double getOldEndPointValue() {
        return oldEndPointValue;
    }

    public void setOldEndPointValue(double oldEndPointValue) {
        this.oldEndPointValue = oldEndPointValue;
    }

    public double getNewEndPointValue() {
        return newEndPointValue;
    }

    public void setNewEndPointValue(double newEndPointValue) {
        this.newEndPointValue = newEndPointValue;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

    @Override
    public String toString() {
        return "EndPointChangeLog{" +
                "endPointChangeLogId=" + endPointChangeLogId +
                ", trialLogId=" + trialLogId +
                ", endPointType='" + endPointType + '\'' +
                ", oldEndPointValue=" + oldEndPointValue +
                ", newEndPointValue=" + newEndPointValue +
                ", stepNo=" + stepNo +
                '}';
    }
}
