package org.lattice.spectrum_backend_final.beans;

import org.json.JSONArray;

public class TrialHistory {

	private int trialLogId;
	private String trialId;
	private String createdOn;
	private String userName;
	private String fullName;
	private String role;
	private String action;
	private JSONArray endPointsChangeLog;
	private JSONArray targetStepChangeLog;

	public TrialHistory() {
	}

	public TrialHistory(int trialLogId, String trialId, String createdOn, String userName, String fullName, String role,
			String action, JSONArray endPointsChangeLog, JSONArray targetStepChangeLog) {
		super();
		this.trialLogId = trialLogId;
		this.trialId = trialId;
		this.createdOn = createdOn;
		this.userName = userName;
		this.fullName = fullName;
		this.role = role;
		this.action = action;
		this.endPointsChangeLog = endPointsChangeLog;
		this.targetStepChangeLog = targetStepChangeLog;
	}

	public int getTrialLogId() {
		return trialLogId;
	}

	public void setTrialLogId(int trialLogId) {
		this.trialLogId = trialLogId;
	}

	public String getTrialId() {
		return trialId;
	}

	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public JSONArray getEndPointsChangeLog() {
		return endPointsChangeLog;
	}

	public void setEndPointsChangeLog(JSONArray endPointsChangeLog) {
		this.endPointsChangeLog = endPointsChangeLog;
	}

	public JSONArray getTargetStepChangeLog() {
		return targetStepChangeLog;
	}

	public void setTargetStepChangeLog(JSONArray targetStepChangeLog) {
		this.targetStepChangeLog = targetStepChangeLog;
	}

}
