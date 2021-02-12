package org.lattice.spectrum_backend_final.beans;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

public class Notes {

    private int notesId;
    private String trialId;
    private String trialType;
    private String notes;
    private String postedBy;
    private String createdOn;
    private String modifiedOn;
    private String isActive;

    public Notes(){}

    public Notes(String notes){
        this.notes = notes;
    }

    public Notes(JSONObject notesJson){
        notes = notesJson.getString(ApiConstant.NOTES);
    }

    public Notes(int notesId, String trialId, String trialType, String notes, String postedBy, String createdOn, String modifiedOn, String isActive) {
        this.notesId = notesId;
        this.trialId = trialId;
        this.trialType = trialType;
        this.notes = notes;
        this.postedBy = postedBy;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.isActive = isActive;
    }

    public String getTrialId() {
        return trialId;
    }

    public void setTrialId(String trialId) {
        this.trialId = trialId;
    }

    public int getNotesId() {
        return notesId;
    }

    public void setNotesId(int notesId) {
        this.notesId = notesId;
    }

    public String getTrialType() {
        return trialType;
    }

    public void setTrialType(String trialType) {
        this.trialType = trialType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "notesId=" + notesId +
                ", trialId='" + trialId + '\'' +
                ", trialType='" + trialType + '\'' +
                ", notes='" + notes + '\'' +
                ", postedBy='" + postedBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", modifiedOn='" + modifiedOn + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
