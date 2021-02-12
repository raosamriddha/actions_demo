package org.lattice.spectrum_backend_final.beans;

public class ManualTrialAlarms {

	private Long alarmId;

	private Integer feedWtLow;
	private Integer feedWtHigh;
	private Integer feedWtLowStop;
	private Integer feedWtHighStop;

	private Integer permWtLow;
	private Integer permWtHigh;
	private Integer permWtLowStop;
	private Integer permWtHighStop;

	private Integer feedPressureLow;
	private Integer feedPressureHigh;
	private Integer feedPressureLowStop;
	private Integer feedPressureHighStop;

	private Integer permPressureLow;
	private Integer permPressureHigh;
	private Integer permPressureLowStop;
	private Integer permPressureHighStop;

	private Double uvCh1High;
	private Double uvCh2High;
	private Double uvCh1HighStop;
	private Double uvCh2HighStop;

	private Long createdOn;
	private Long updatedOn;

	private String digital_username;
	private String digital_password;
	
	public ManualTrialAlarms() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ManualTrialAlarms(Long alarmId, Integer feedWtLow, Integer feedWtHigh, Integer feedWtLowStop, Integer feedWtHighStop, Integer permWtLow, Integer permWtHigh, Integer permWtLowStop, Integer permWtHighStop, Integer feedPressureLow, Integer feedPressureHigh, Integer feedPressureLowStop, Integer feedPressureHighStop, Integer permPressureLow, Integer permPressureHigh, Integer permPressureLowStop, Integer permPressureHighStop, Double uvCh1High, Double uvCh2High, Double uvCh1HighStop, Double uvCh2HighStop, Long createdOn, Long updatedOn, String digital_username, String digital_password) {
		this.alarmId = alarmId;
		this.feedWtLow = feedWtLow;
		this.feedWtHigh = feedWtHigh;
		this.feedWtLowStop = feedWtLowStop;
		this.feedWtHighStop = feedWtHighStop;
		this.permWtLow = permWtLow;
		this.permWtHigh = permWtHigh;
		this.permWtLowStop = permWtLowStop;
		this.permWtHighStop = permWtHighStop;
		this.feedPressureLow = feedPressureLow;
		this.feedPressureHigh = feedPressureHigh;
		this.feedPressureLowStop = feedPressureLowStop;
		this.feedPressureHighStop = feedPressureHighStop;
		this.permPressureLow = permPressureLow;
		this.permPressureHigh = permPressureHigh;
		this.permPressureLowStop = permPressureLowStop;
		this.permPressureHighStop = permPressureHighStop;
		this.uvCh1High = uvCh1High;
		this.uvCh2High = uvCh2High;
		this.uvCh1HighStop = uvCh1HighStop;
		this.uvCh2HighStop = uvCh2HighStop;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.digital_username = digital_username;
		this.digital_password = digital_password;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public Integer getFeedWtLow() {
		return feedWtLow;
	}

	public void setFeedWtLow(Integer feedWtLow) {
		this.feedWtLow = feedWtLow;
	}

	public Integer getFeedWtHigh() {
		return feedWtHigh;
	}

	public void setFeedWtHigh(Integer feedWtHigh) {
		this.feedWtHigh = feedWtHigh;
	}

	public Integer getFeedWtLowStop() {
		return feedWtLowStop;
	}

	public void setFeedWtLowStop(Integer feedWtLowStop) {
		this.feedWtLowStop = feedWtLowStop;
	}

	public Integer getFeedWtHighStop() {
		return feedWtHighStop;
	}

	public void setFeedWtHighStop(Integer feedWtHighStop) {
		this.feedWtHighStop = feedWtHighStop;
	}

	public Integer getPermWtLow() {
		return permWtLow;
	}

	public void setPermWtLow(Integer permWtLow) {
		this.permWtLow = permWtLow;
	}

	public Integer getPermWtHigh() {
		return permWtHigh;
	}

	public void setPermWtHigh(Integer permWtHigh) {
		this.permWtHigh = permWtHigh;
	}

	public Integer getPermWtLowStop() {
		return permWtLowStop;
	}

	public void setPermWtLowStop(Integer permWtLowStop) {
		this.permWtLowStop = permWtLowStop;
	}

	public Integer getPermWtHighStop() {
		return permWtHighStop;
	}

	public void setPermWtHighStop(Integer permWtHighStop) {
		this.permWtHighStop = permWtHighStop;
	}

	public Integer getFeedPressureLow() {
		return feedPressureLow;
	}

	public void setFeedPressureLow(Integer feedPressureLow) {
		this.feedPressureLow = feedPressureLow;
	}

	public Integer getFeedPressureHigh() {
		return feedPressureHigh;
	}

	public void setFeedPressureHigh(Integer feedPressureHigh) {
		this.feedPressureHigh = feedPressureHigh;
	}

	public Integer getFeedPressureLowStop() {
		return feedPressureLowStop;
	}

	public void setFeedPressureLowStop(Integer feedPressureLowStop) {
		this.feedPressureLowStop = feedPressureLowStop;
	}

	public Integer getFeedPressureHighStop() {
		return feedPressureHighStop;
	}

	public void setFeedPressureHighStop(Integer feedPressureHighStop) {
		this.feedPressureHighStop = feedPressureHighStop;
	}

	public Integer getPermPressureLow() {
		return permPressureLow;
	}

	public void setPermPressureLow(Integer permPressureLow) {
		this.permPressureLow = permPressureLow;
	}

	public Integer getPermPressureHigh() {
		return permPressureHigh;
	}

	public void setPermPressureHigh(Integer permPressureHigh) {
		this.permPressureHigh = permPressureHigh;
	}

	public Integer getPermPressureLowStop() {
		return permPressureLowStop;
	}

	public void setPermPressureLowStop(Integer permPressureLowStop) {
		this.permPressureLowStop = permPressureLowStop;
	}

	public Integer getPermPressureHighStop() {
		return permPressureHighStop;
	}

	public void setPermPressureHighStop(Integer permPressureHighStop) {
		this.permPressureHighStop = permPressureHighStop;
	}

	public Double getUvCh1High() {
		return uvCh1High;
	}

	public void setUvCh1High(Double uvCh1High) {
		this.uvCh1High = uvCh1High;
	}

	public Double getUvCh2High() {
		return uvCh2High;
	}

	public void setUvCh2High(Double uvCh2High) {
		this.uvCh2High = uvCh2High;
	}

	public Double getUvCh1HighStop() {
		return uvCh1HighStop;
	}

	public void setUvCh1HighStop(Double uvCh1HighStop) {
		this.uvCh1HighStop = uvCh1HighStop;
	}

	public Double getUvCh2HighStop() {
		return uvCh2HighStop;
	}

	public void setUvCh2HighStop(Double uvCh2HighStop) {
		this.uvCh2HighStop = uvCh2HighStop;
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
}
