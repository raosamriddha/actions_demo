package org.lattice.spectrum_backend_final.beans;

public class TrialLog {

	private String trialId;
	private int trialMasterId;
	private int trialRunSettingId;
	private int trialStatus;
	private String trialStartTime;
	private String trialEndTime;
	private String recipeName;
	private String userName;

	public TrialLog() {
		super();
	}

	public TrialLog(String trialId, int trialMasterId, int trialRunSettingId, int trialStatus, String trialStartTime,
			String trialEndTime, String recipeName, String userName) {
		super();
		this.trialId = trialId;
		this.trialMasterId = trialMasterId;
		this.trialRunSettingId = trialRunSettingId;
		this.trialStatus = trialStatus;
		this.trialStartTime = trialStartTime;
		this.trialEndTime = trialEndTime;
		this.recipeName = recipeName;
		this.userName = userName;
	}

	public String getTrialId() {
		return trialId;
	}

	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}

	public int getTrialMasterId() {
		return trialMasterId;
	}

	public void setTrialMasterId(int trialMasterId) {
		this.trialMasterId = trialMasterId;
	}

	public int getTrialRunSettingId() {
		return trialRunSettingId;
	}

	public void setTrialRunSettingId(int trialRunSettingId) {
		this.trialRunSettingId = trialRunSettingId;
	}

	public int getTrialStatus() {
		return trialStatus;
	}

	public void setTrialStatus(int trialStatus) {
		this.trialStatus = trialStatus;
	}

	public String getTrialStartTime() {
		return trialStartTime;
	}

	public void setTrialStartTime(String trialStartTime) {
		this.trialStartTime = trialStartTime;
	}

	public String getTrialEndTime() {
		return trialEndTime;
	}

	public void setTrialEndTime(String trialEndTime) {
		this.trialEndTime = trialEndTime;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
