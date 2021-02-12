package org.lattice.spectrum_backend_final.beans;

public class ABVSetting {

	private Integer trialRunSettingId;
	private Integer type;
	private String abvType;
	private String position;
	private String tubingSize;
	private String abvTarget;
	private String abvMode;
	private String operatingPressure;
	private String digital_username;
	private String digital_password;

	public ABVSetting() {
	}

	public ABVSetting(Integer trialRunSettingId, Integer type, String abvType, String position, String tubingSize, String digital_username, String digital_password) {
		this.trialRunSettingId = trialRunSettingId;
		this.type = type;
		this.abvType = abvType;
		this.position = position;
		this.tubingSize = tubingSize;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public ABVSetting(Integer trialRunSettingId, Integer type, String abvType, String position, String tubingSize, String abvTarget, String abvMode, String operatingPressure, String digital_username, String digital_password) {
		this.trialRunSettingId = trialRunSettingId;
		this.type = type;
		this.abvType = abvType;
		this.position = position;
		this.tubingSize = tubingSize;
		this.abvTarget = abvTarget;
		this.abvMode = abvMode;
		this.operatingPressure = operatingPressure;
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

	public String getAbvType() {
		return abvType;
	}

	public void setAbvType(String abvType) {
		this.abvType = abvType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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

	public String getOperatingPressure() {
		return operatingPressure;
	}

	public String getAbvMode() {
		return abvMode;
	}

	public void setAbvMode(String abvMode) {
		this.abvMode = abvMode;
	}

	public void setOperatingPressure(String operatingPressure) {
		this.operatingPressure = operatingPressure;
	}

	public String getDigital_password() {
		return digital_password;
	}

	public void setDigital_password(String digital_password) {
		this.digital_password = digital_password;
	}

	public String getAbvTarget() {
		return abvTarget;
	}

	public void setAbvTarget(String abvTarget) {
		this.abvTarget = abvTarget;
	}

	@Override
	public String toString() {
		return "ABVSetting [trialRunSettingId=" + trialRunSettingId + ", type=" + type + ", abvType=" + abvType + ", position="
				+ position + ", tubingSize=" + tubingSize + "]";
	}

	
}
