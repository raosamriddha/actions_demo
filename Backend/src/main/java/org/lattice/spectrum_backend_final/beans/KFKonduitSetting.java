package org.lattice.spectrum_backend_final.beans;

public class KFKonduitSetting {

	private Integer trialRunSettingId;
	private String ch1KFactor;
	private String ch2KFactor;
	private String digital_username;
	private String digital_password;

	public KFKonduitSetting() {
	}

	public KFKonduitSetting(Integer trialRunSettingId, String ch1KFactor, String ch2KFactor, String digital_username, String digital_password) {
		this.trialRunSettingId = trialRunSettingId;
		this.ch1KFactor = ch1KFactor;
		this.ch2KFactor = ch2KFactor;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public Integer getTrialRunSettingId() {
		return trialRunSettingId;
	}

	public void setTrialRunSettingId(Integer trialRunSettingId) {
		this.trialRunSettingId = trialRunSettingId;
	}

	public String getCh1KFactor() {
		return ch1KFactor;
	}

	public void setCh1KFactor(String ch1kFactor) {
		ch1KFactor = ch1kFactor;
	}

	public String getCh2KFactor() {
		return ch2KFactor;
	}

	public void setCh2KFactor(String ch2kFactor) {
		ch2KFactor = ch2kFactor;
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
		return "KFKonduitSetting [trialRunSettingId=" + trialRunSettingId + ", ch1KFactor=" + ch1KFactor
				+ ", ch2KFactor=" + ch2KFactor + ", digital_username=" + digital_username + ", digital_password="
				+ digital_password + "]";
	}
	
}
