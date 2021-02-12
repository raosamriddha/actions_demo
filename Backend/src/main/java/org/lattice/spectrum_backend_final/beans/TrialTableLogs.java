package org.lattice.spectrum_backend_final.beans;

public class TrialTableLogs {

	private String trialId;
	private Integer trialLiveDataId;
	private String timestamp;
	private String feedPressure;
	private String retentatePressure;
	private String permeatePressure;
	private String tmp;
	private Integer mainPumpSpeed;
	private String mainPumpFlowRate;
	private String auxPump1FlowRate;
	private String auxPump2FlowRate;
	private String conductivity1;
	private String conductivity2;
	private String temperature1;
	private String temperature2;
	private String feedScale;
	private String mPermeate;
	private String totalPermWeight;
	private String diaFiltrationVol1;
	private String diaFiltrationVol2;
	private String concentrationFactor;
	private String deltaP;
	private String feedFlowRate;
	private String permeateFlowRate;
	private String retentateFlowRate;
	private String vt;
	private String shear;
	private String flux;
	private String kFactorCh1;
	private String kFactorCh2;
	private String abv1;
	private String abv2;
	private String tcf;
	private String waterFlux20Degree;
	private String waterPerm;
	private String nwp;
	private String konduitCh1;
	private String konduitCh2;
	private Integer konduitCh1Type;
	private Integer konduitCh2Type;
	private int isPaused;
	private String permeateTotal;
	private String permTotalWithHoldUp;
	private String duration;

	public TrialTableLogs() {
		super();
	}

	public TrialTableLogs(String trialId, Integer trialLiveDataId, String timestamp, String feedPressure,
			String retentatePressure, String permeatePressure, String tmp, Integer mainPumpSpeed,
			String mainPumpFlowRate, String auxPump1FlowRate, String auxPump2FlowRate, String conductivity1,
			String conductivity2, String temperature1, String temperature2, String feedScale, String mPermeate,
			String totalPermWeight, String diaFiltrationVol1, String diaFiltrationVol2, String concentrationFactor,
			String deltaP, String feedFlowRate, String permeateFlowRate, String retentateFlowRate, String vt,
			String shear, String flux, String kFactorCh1, String kFactorCh2, String abv1, String abv2, String tcf,
			String waterFlux20Degree, String waterPerm, String nwp, String konduitCh1, String konduitCh2,
			Integer konduitCh1Type, Integer konduitCh2Type, int isPaused, String permeateTotal,
			String permTotalWithHoldUp, String duration) {
		super();
		this.trialId = trialId;
		this.trialLiveDataId = trialLiveDataId;
		this.timestamp = timestamp;
		this.feedPressure = feedPressure;
		this.retentatePressure = retentatePressure;
		this.permeatePressure = permeatePressure;
		this.tmp = tmp;
		this.mainPumpSpeed = mainPumpSpeed;
		this.mainPumpFlowRate = mainPumpFlowRate;
		this.auxPump1FlowRate = auxPump1FlowRate;
		this.auxPump2FlowRate = auxPump2FlowRate;
		this.conductivity1 = conductivity1;
		this.conductivity2 = conductivity2;
		this.temperature1 = temperature1;
		this.temperature2 = temperature2;
		this.feedScale = feedScale;
		this.mPermeate = mPermeate;
		this.totalPermWeight = totalPermWeight;
		this.diaFiltrationVol1 = diaFiltrationVol1;
		this.diaFiltrationVol2 = diaFiltrationVol2;
		this.concentrationFactor = concentrationFactor;
		this.deltaP = deltaP;
		this.feedFlowRate = feedFlowRate;
		this.permeateFlowRate = permeateFlowRate;
		this.retentateFlowRate = retentateFlowRate;
		this.vt = vt;
		this.shear = shear;
		this.flux = flux;
		this.kFactorCh1 = kFactorCh1;
		this.kFactorCh2 = kFactorCh2;
		this.abv1 = abv1;
		this.abv2 = abv2;
		this.tcf = tcf;
		this.waterFlux20Degree = waterFlux20Degree;
		this.waterPerm = waterPerm;
		this.nwp = nwp;
		this.konduitCh1 = konduitCh1;
		this.konduitCh2 = konduitCh2;
		this.konduitCh1Type = konduitCh1Type;
		this.konduitCh2Type = konduitCh2Type;
		this.isPaused = isPaused;
		this.permeateTotal = permeateTotal;
		this.permTotalWithHoldUp = permTotalWithHoldUp;
		this.duration = duration;
	}

	public String getTrialId() {
		return trialId;
	}

	public void setTrialId(String trialId) {
		this.trialId = trialId;
	}

	public Integer getTrialLiveDataId() {
		return trialLiveDataId;
	}

	public void setTrialLiveDataId(Integer trialLiveDataId) {
		this.trialLiveDataId = trialLiveDataId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFeedPressure() {
		return feedPressure;
	}

	public void setFeedPressure(String feedPressure) {
		this.feedPressure = feedPressure;
	}

	public String getRetentatePressure() {
		return retentatePressure;
	}

	public void setRetentatePressure(String retentatePressure) {
		this.retentatePressure = retentatePressure;
	}

	public String getPermeatePressure() {
		return permeatePressure;
	}

	public void setPermeatePressure(String permeatePressure) {
		this.permeatePressure = permeatePressure;
	}

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public Integer getMainPumpSpeed() {
		return mainPumpSpeed;
	}

	public void setMainPumpSpeed(Integer mainPumpSpeed) {
		this.mainPumpSpeed = mainPumpSpeed;
	}

	public String getMainPumpFlowRate() {
		return mainPumpFlowRate;
	}

	public void setMainPumpFlowRate(String mainPumpFlowRate) {
		this.mainPumpFlowRate = mainPumpFlowRate;
	}

	public String getAuxPump1FlowRate() {
		return auxPump1FlowRate;
	}

	public void setAuxPump1FlowRate(String auxPump1FlowRate) {
		this.auxPump1FlowRate = auxPump1FlowRate;
	}

	public String getAuxPump2FlowRate() {
		return auxPump2FlowRate;
	}

	public void setAuxPump2FlowRate(String auxPump2FlowRate) {
		this.auxPump2FlowRate = auxPump2FlowRate;
	}

	public String getFeedScale() {
		return feedScale;
	}

	public void setFeedScale(String feedScale) {
		this.feedScale = feedScale;
	}

	public String getMPermeate() {
		return mPermeate;
	}

	public void setMPermeate(String mPermeate) {
		this.mPermeate = mPermeate;
	}

	public String getTotalPermWeight() {
		return totalPermWeight;
	}

	public void setTotalPermWeight(String totalPermWeight) {
		this.totalPermWeight = totalPermWeight;
	}

	public String getDiaFiltrationVol1() {
		return diaFiltrationVol1;
	}

	public void setDiaFiltrationVol1(String diaFiltrationVol1) {
		this.diaFiltrationVol1 = diaFiltrationVol1;
	}

	public String getDiaFiltrationVol2() {
		return diaFiltrationVol2;
	}

	public void setDiaFiltrationVol2(String diaFiltrationVol2) {
		this.diaFiltrationVol2 = diaFiltrationVol2;
	}

	public String getConcentrationFactor() {
		return concentrationFactor;
	}

	public void setConcentrationFactor(String concentrationFactor) {
		this.concentrationFactor = concentrationFactor;
	}

	public String getDeltaP() {
		return deltaP;
	}

	public void setDeltaP(String deltaP) {
		this.deltaP = deltaP;
	}

	public String getFeedFlowRate() {
		return feedFlowRate;
	}

	public void setFeedFlowRate(String feedFlowRate) {
		this.feedFlowRate = feedFlowRate;
	}

	public String getPermeateFlowRate() {
		return permeateFlowRate;
	}

	public void setPermeateFlowRate(String permeateFlowRate) {
		this.permeateFlowRate = permeateFlowRate;
	}

	public String getRetentateFlowRate() {
		return retentateFlowRate;
	}

	public void setRetentateFlowRate(String retentateFlowRate) {
		this.retentateFlowRate = retentateFlowRate;
	}

	public String getVt() {
		return vt;
	}

	public void setVt(String vt) {
		this.vt = vt;
	}

	public String getShear() {
		return shear;
	}

	public void setShear(String shear) {
		this.shear = shear;
	}

	public String getFlux() {
		return flux;
	}

	public void setFlux(String flux) {
		this.flux = flux;
	}

	public String getkFactorCh1() {
		return kFactorCh1;
	}

	public void setkFactorCh1(String kFactorCh1) {
		this.kFactorCh1 = kFactorCh1;
	}

	public String getkFactorCh2() {
		return kFactorCh2;
	}

	public void setkFactorCh2(String kFactorCh2) {
		this.kFactorCh2 = kFactorCh2;
	}

	public String getAbv1() {
		return abv1;
	}

	public void setAbv1(String abv1) {
		this.abv1 = abv1;
	}

	public String getAbv2() {
		return abv2;
	}

	public void setAbv2(String abv2) {
		this.abv2 = abv2;
	}

	public String getTcf() {
		return tcf;
	}

	public void setTcf(String tcf) {
		this.tcf = tcf;
	}

	public String getWaterFlux20Degree() {
		return waterFlux20Degree;
	}

	public void setWaterFlux20Degree(String waterFlux20Degree) {
		this.waterFlux20Degree = waterFlux20Degree;
	}

	public String getWaterPerm() {
		return waterPerm;
	}

	public void setWaterPerm(String waterPerm) {
		this.waterPerm = waterPerm;
	}

	public String getNwp() {
		return nwp;
	}

	public void setNwp(String nwp) {
		this.nwp = nwp;
	}

	public Integer getKonduitCh1Type() {
		return konduitCh1Type;
	}

	public void setKonduitCh1Type(Integer konduitCh1Type) {
		this.konduitCh1Type = konduitCh1Type;
	}

	public Integer getKonduitCh2Type() {
		return konduitCh2Type;
	}

	public void setKonduitCh2Type(Integer konduitCh2Type) {
		this.konduitCh2Type = konduitCh2Type;
	}

	public int getIsPaused() {
		return isPaused;
	}

	public void setIsPaused(int isPaused) {
		this.isPaused = isPaused;
	}

	public String getConductivity1() {
		return conductivity1;
	}

	public void setConductivity1(String conductivity1) {
		this.conductivity1 = conductivity1;
	}

	public String getConductivity2() {
		return conductivity2;
	}

	public void setConductivity2(String conductivity2) {
		this.conductivity2 = conductivity2;
	}

	public String getTemperature1() {
		return temperature1;
	}

	public void setTemperature1(String temperature1) {
		this.temperature1 = temperature1;
	}

	public String getTemperature2() {
		return temperature2;
	}

	public void setTemperature2(String temperature2) {
		this.temperature2 = temperature2;
	}

	public String getKonduitCh1() {
		return konduitCh1;
	}

	public void setKonduitCh1(String konduitCh1) {
		this.konduitCh1 = konduitCh1;
	}

	public String getKonduitCh2() {
		return konduitCh2;
	}

	public void setKonduitCh2(String konduitCh2) {
		this.konduitCh2 = konduitCh2;
	}

	public String getPermeateTotal() {
		return permeateTotal;
	}

	public void setPermeateTotal(String permeateTotal) {
		this.permeateTotal = permeateTotal;
	}

	public String getPermTotalWithHoldUp() {
		return permTotalWithHoldUp;
	}

	public void setPermTotalWithHoldUp(String permTotalWithHoldUp) {
		this.permTotalWithHoldUp = permTotalWithHoldUp;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
