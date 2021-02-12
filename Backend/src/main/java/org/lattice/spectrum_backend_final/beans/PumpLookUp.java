package org.lattice.spectrum_backend_final.beans;

public class PumpLookUp {

	private Integer pumpLookUpId;
	private String pumpType;
	private String pumpName;
	private Double minSpeed;
	private Integer maxSpeed;
	private String speedUnit;

	public PumpLookUp() {
	}

	public PumpLookUp(Integer pumpLookUpId, String pumpType, String pumpName, Double minSpeed, Integer maxSpeed,
			String speedUnit) {
		super();
		this.pumpLookUpId = pumpLookUpId;
		this.pumpType = pumpType;
		this.pumpName = pumpName;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.speedUnit = speedUnit;
	}

	public Integer getPumpLookUpId() {
		return pumpLookUpId;
	}

	public void setPumpLookUpId(Integer pumpLookUpId) {
		this.pumpLookUpId = pumpLookUpId;
	}

	public String getPumpType() {
		return pumpType;
	}

	public void setPumpType(String pumpType) {
		this.pumpType = pumpType;
	}

	public String getPumpName() {
		return pumpName;
	}

	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}

	public Double getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(Double minSpeed) {
		this.minSpeed = minSpeed;
	}

	public Integer getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Integer maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getSpeedUnit() {
		return speedUnit;
	}

	public void setSpeedUnit(String speedUnit) {
		this.speedUnit = speedUnit;
	}

}
