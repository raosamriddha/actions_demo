package org.lattice.spectrum_backend_final.beans;

import javax.validation.constraints.NotNull;

public class ManualTrialDetails {

	private long trialDetailsId;
	@NotNull
	private String trialId;
	private Integer feedHoldUpWt;
	private int mode;
	private int isFeedHoldUpEmpty;
	private Long alarmId;
	private ManualTrialAlarms manualTrialAlarms;
	private Long createdOn;
	private Long updatedOn;
	private String digital_username;
	private String digital_password;

	public ManualTrialDetails() {
		super();
	}

	public ManualTrialDetails(long trialDetailsId, String trialId, Integer feedHoldUpWt, int mode, int isFeedHoldUpEmpty, Long alarmId, ManualTrialAlarms manualTrialAlarms, Long createdOn, Long updatedOn, String digital_username, String digital_password) {
		this.trialDetailsId = trialDetailsId;
		this.trialId = trialId;
		this.feedHoldUpWt = feedHoldUpWt;
		this.mode = mode;
		this.isFeedHoldUpEmpty = isFeedHoldUpEmpty;
		this.alarmId = alarmId;
		this.manualTrialAlarms = manualTrialAlarms;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public long getTrialDetailsId() {
		return trialDetailsId;
	}

	public void setTrialDetailsId(long trialDetailsId) {
		this.trialDetailsId = trialDetailsId;
	}

	public String getTrialId() {
		return trialId;
	}

	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}

	public Integer getFeedHoldUpWt() {
		return feedHoldUpWt;
	}

	public void setFeedHoldUpWt(Integer feedHoldUpWt) {
		this.feedHoldUpWt = feedHoldUpWt;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getIsFeedHoldUpEmpty() {
		return isFeedHoldUpEmpty;
	}

	public void setIsFeedHoldUpEmpty(int isFeedHoldUpEmpty) {
		this.isFeedHoldUpEmpty = isFeedHoldUpEmpty;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public ManualTrialAlarms getManualTrialAlarms() {
		return manualTrialAlarms;
	}

	public void setManualTrialAlarms(ManualTrialAlarms manualTrialAlarms) {
		this.manualTrialAlarms = manualTrialAlarms;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Long updatedOn) {
		this.updatedOn = updatedOn;
	}

}
