package org.lattice.spectrum_backend_final.beans;

public class TubingDetails {


	private String tubeName;

	private Double idIn;

	private Double idMm;

	private String rpmMinMax;

	private String flowRate;

	private String mainPump;

	private String auxPump;
	
	private String abvType;

	public String getTubeName() {
		return tubeName;
	}

	public void setTubeName(String tubeName) {
		this.tubeName = tubeName;
	}

	public Double getIdIn() {
		return idIn;
	}

	public void setIdIn(Double idIn) {
		this.idIn = idIn;
	}

	public Double getIdMm() {
		return idMm;
	}

	public void setIdMm(Double idMm) {
		this.idMm = idMm;
	}

	public String getRpmMinMax() {
		return rpmMinMax;
	}

	public void setRpmMinMax(String rpmMinMax) {
		this.rpmMinMax = rpmMinMax;
	}

	public String getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}

	public String getMainPump() {
		return mainPump;
	}

	public void setMainPump(String mainPump) {
		this.mainPump = mainPump;
	}

	public String getAuxPump() {
		return auxPump;
	}

	public void setAuxPump(String auxPump) {
		this.auxPump = auxPump;
	}

	public String getAbvType() {
		return abvType;
	}

	public void setAbvType(String abvType) {
		this.abvType = abvType;
	}
	
	public TubingDetails() {
		super();
	}

	public TubingDetails(String tubeName, Double idIn, Double idMm, String rpmMinMax, String flowRate, String mainPump,
			String auxPump, String abvType) {
		super();
		this.tubeName = tubeName;
		this.idIn = idIn;
		this.idMm = idMm;
		this.rpmMinMax = rpmMinMax;
		this.flowRate = flowRate;
		this.mainPump = mainPump;
		this.auxPump = auxPump;
		this.abvType = abvType;
	}
	
	
}
