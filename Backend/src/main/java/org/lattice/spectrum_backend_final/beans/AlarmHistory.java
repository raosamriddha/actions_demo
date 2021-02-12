package org.lattice.spectrum_backend_final.beans;

public class AlarmHistory {

	private int alarmHistoryId;
	private String modeName;
	private String alarmStopDesc;
	private String alarmStopValue;
	private String nature;
	private String createdOn;

	public AlarmHistory() {
	}

	public AlarmHistory(int alarmHistoryId, String modeName, String alarmStopDesc, String alarmStopValue, String nature,
			String createdOn) {
		super();
		this.alarmHistoryId = alarmHistoryId;
		this.modeName = modeName;
		this.alarmStopDesc = alarmStopDesc;
		this.alarmStopValue = alarmStopValue;
		this.nature = nature;
		this.createdOn = createdOn;
	}

	public int getAlarmHistoryId() {
		return alarmHistoryId;
	}

	public void setAlarmHistoryId(int alarmHistoryId) {
		this.alarmHistoryId = alarmHistoryId;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getAlarmStopDesc() {
		return alarmStopDesc;
	}

	public void setAlarmStopDesc(String alarmStopDesc) {
		this.alarmStopDesc = alarmStopDesc;
	}

	public String getAlarmStopValue() {
		return alarmStopValue;
	}

	public void setAlarmStopValue(String alarmStopValue) {
		this.alarmStopValue = alarmStopValue;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

}
