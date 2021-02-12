package org.lattice.spectrum_backend_final.beans;

public class NotesDescription {

    private long trialLogId;
    private String subModeName;
    /**
     * sub mode step
     */
    private int step;
    private String endPointName;
    private double endPointValue;
    /**
     * 0 for end and 1 for start
     */
    private int status;


    public NotesDescription() {
    }

    public NotesDescription(long trialLogId, String modeName, int step, String endPointName, double endPointValue, int status) {
        this.trialLogId = trialLogId;
        this.subModeName = modeName;
        this.step = step;
        this.endPointName = endPointName;
        this.endPointValue = endPointValue;
        this.status = status;
    }

    public long getTrialLogId() {
        return trialLogId;
    }

    public void setTrialLogId(long trialLogId) {
        this.trialLogId = trialLogId;
    }

    public String getSubModeName() {
        return subModeName;
    }

    public void setSubModeName(String subModeName) {
        this.subModeName = subModeName;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }

    public double getEndPointValue() {
        return endPointValue;
    }

    public void setEndPointValue(double endPointValue) {
        this.endPointValue = endPointValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotesDescription{" +
                "trialLogId=" + trialLogId +
                ", modeName='" + subModeName + '\'' +
                ", step=" + step +
                ", endPointName='" + endPointName + '\'' +
                ", endPointValue=" + endPointValue +
                ", status=" + status +
                '}';
    }
}

