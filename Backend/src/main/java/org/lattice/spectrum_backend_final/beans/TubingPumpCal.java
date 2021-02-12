package org.lattice.spectrum_backend_final.beans;

public class TubingPumpCal {

	private String pumpName;
	private double flowRate;
	private String direction;
	private String tubingSize;
	private int duration;
	private int isPermConnected;
	private String digital_username;
	private String digital_password;
	private String digital_notes;
	public TubingPumpCal() {
	}

	public TubingPumpCal(String pumpName, double flowRate, String direction, String tubingSize, int duration,
						 int isPermConnected, String digital_username, String digital_password) {
		super();
		this.pumpName = pumpName;
		this.flowRate = flowRate;
		this.direction = direction;
		this.tubingSize = tubingSize;
		this.duration = duration;
		this.isPermConnected = isPermConnected;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public String getPumpName() {
		return pumpName;
	}

	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}

	public double getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(double flowRate) {
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getIsPermConnected() {
		return isPermConnected;
	}

	public void setIsPermConnected(int isPermConnected) {
		this.isPermConnected = isPermConnected;
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

	public String getDigital_notes() {
		return digital_notes;
	}

	public void setDigital_notes(String digital_notes) {
		this.digital_notes = digital_notes;
	}
}