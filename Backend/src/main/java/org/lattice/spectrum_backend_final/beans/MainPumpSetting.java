package org.lattice.spectrum_backend_final.beans;

public class MainPumpSetting {

	private Integer trialRunSettingId;
	private String pumpName;
	private String speed;
	private String flowRate;
	private String direction;
	private String tubingSize;
	private String digital_username;
	private String digital_password;

	public MainPumpSetting() {
	}

	public MainPumpSetting(Integer trialRunSettingId, String pumpName, String speed, String flowRate, String direction, String tubingSize, String digital_username, String digital_password) {
		this.trialRunSettingId = trialRunSettingId;
		this.pumpName = pumpName;
		this.speed = speed;
		this.flowRate = flowRate;
		this.direction = direction;
		this.tubingSize = tubingSize;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public Integer getTrialRunSettingId() {
		return trialRunSettingId;
	}

	public void setTrialRunSettingId(Integer trialRunSettingId) {
		this.trialRunSettingId = trialRunSettingId;
	}

	public String getPumpName() {
		return pumpName;
	}

	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getTubingSize() {
		return tubingSize;
	}

	public void setTubingSize(String tubingSize) {
		this.tubingSize = tubingSize;
	}

	public String getDigital_username() {
		return digital_username;
	}

	public void setDigital_username(String digital_username) {
		this.digital_username = digital_username;
	}

	public String getDigital_password() {
		return digital_password;
	}

	public void setDigital_password(String digital_password) {
		this.digital_password = digital_password;
	}

	@Override
	public String toString() {
		return "MainPumpSetting [trialRunSettingId=" + trialRunSettingId + ", pumpName=" + pumpName + ", speed=" + speed
				+ ", flowRate=" + flowRate + ", direction=" + direction + ", tubingSize=" + tubingSize + "]";
	}

}
