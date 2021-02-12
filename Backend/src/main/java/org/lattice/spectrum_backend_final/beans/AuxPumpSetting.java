package org.lattice.spectrum_backend_final.beans;

public class AuxPumpSetting {

	private Integer trialRunSettingId;
	private Integer type;
	private String auxPumpType;
	private String speed;
	private String flowRate;
	private String tubingSize;
	private String digital_username;
	private String digital_password;

	public AuxPumpSetting() {
	}

	public AuxPumpSetting(Integer trialRunSettingId, Integer type, String auxPumpType, String speed, String flowRate, String tubingSize, String digital_username, String digital_password) {
		this.trialRunSettingId = trialRunSettingId;
		this.type = type;
		this.auxPumpType = auxPumpType;
		this.speed = speed;
		this.flowRate = flowRate;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAuxPumpType() {
		return auxPumpType;
	}

	public void setAuxPumpType(String auxPumpType) {
		this.auxPumpType = auxPumpType;
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
		return "AuxPumpSetting [trialRunSettingId=" + trialRunSettingId + ", type=" + type + ", auxPumpType=" + auxPumpType
				+ ", speed=" + speed + ", flowRate=" + flowRate + ", tubingSize=" + tubingSize + "]";
	}

}
