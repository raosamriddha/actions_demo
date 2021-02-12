package org.lattice.spectrum_backend_final.beans;



public class TrialBuffer {

    //Time	Pinlet	Pretentate	Ppermeate	TMP	DP	Qinlet	Qpermeate	Qretentate	Mpermeate	Mfeed	Aux Pump 1	Aux Pump 2	LMH	VT	Pump	Temp	Conc	Diafilt	Shear	Notes	Cond	State
    //	psig	psig	psig	psig	psig	ml/min	mL/min	ml/min	g	g	mL/min	mL/min	(Filt. Flux)	mL/cm²	rpm	oC	Factor	Vols
    //																Measure

    private Long timestamp;
    private double feedPressure;
    private double retentatePressure;
    private double permeatePressure;
    private double tmp;
    private int mainPumpSpeed;
    private double mainPumpFlowRate;
    private double auxPump1FlowRate;
    private double auxPump2FlowRate;
    private Double conductivity1;
    private Double conductivity2;
    private Double temperature1;
    private Double temperature2;
    private double feedScale;
    private Double mPermeate;
    private Double totalPermWt;
    private double diaFiltrationVol1;
    private double diaFiltrationVol2;
    private double concentrationFactor ;
    private double deltaP;
    private double feedFlowRate;
    private double permeateFlowRate;
    private double retentateFlowRate;
    private double vt;
    private String shear;
    private String abv1;
    private String abv2;
    private double flux;
    private String tcf ;
    private String waterFlux20Degree ;
    private String waterPerm ;
    private String nwp ;
    private Double konduitCh1;
    private Double konduitCh2;
    private int konduitCh1Type;
    private int konduitCh2Type;
    private int isPaused;
    private int flowRateCount;
    private Double permeateTotal;
    private Double permeateTotalWithHoldup;
    private String duration;




//    public static TrialBuffer[] trialBuffer;


//    public static TrialBuffer[] getInstance() {
//
//        synchronized (TrialBuffer.class) {
//            if (trialBuffer == null) {
//                trialBuffer = new TrialBuffer[ApiConstant.BUFFER_SIZE];
//            }
//        }
//
//        return trialBuffer;
//    }


    public TrialBuffer() {
    }

    public TrialBuffer(Long timestamp, double feedPressure, double retentatePressure, double permeatePressure, double tmp, int mainPumpSpeed, double mainPumpFlowRate, double auxPump1FlowRate, double auxPump2FlowRate, Double conductivity1, Double conductivity2, Double temperature1, Double temperature2, double feedScale, Double mPermeate, Double totalPermWt, double diaFiltrationVol1, double diaFiltrationVol2, double concentrationFactor, double deltaP, double feedFlowRate, double permeateFlowRate, double retentateFlowRate, double vt, String shear, String abv1, String abv2, double flux, String tcf, String waterFlux20Degree, String waterPerm, String nwp, Double konduitCh1, Double konduitCh2, int konduitCh1Type, int konduitCh2Type, int isPaused, int flowRateCount, Double permeateTotal, Double permeateTotalWithHoldup, String duration) {
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
        this.totalPermWt = totalPermWt;
        this.diaFiltrationVol1 = diaFiltrationVol1;
        this.diaFiltrationVol2 = diaFiltrationVol2;
        this.concentrationFactor = concentrationFactor;
        this.deltaP = deltaP;
        this.feedFlowRate = feedFlowRate;
        this.permeateFlowRate = permeateFlowRate;
        this.retentateFlowRate = retentateFlowRate;
        this.vt = vt;
        this.shear = shear;
        this.abv1 = abv1;
        this.abv2 = abv2;
        this.flux = flux;
        this.tcf = tcf;
        this.waterFlux20Degree = waterFlux20Degree;
        this.waterPerm = waterPerm;
        this.nwp = nwp;
        this.konduitCh1 = konduitCh1;
        this.konduitCh2 = konduitCh2;
        this.konduitCh1Type = konduitCh1Type;
        this.konduitCh2Type = konduitCh2Type;
        this.isPaused = isPaused;
        this.flowRateCount = flowRateCount;
        this.permeateTotal = permeateTotal;
        this.permeateTotalWithHoldup = permeateTotalWithHoldup;
        this.duration = duration;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public double getFeedPressure() {
        return feedPressure;
    }

    public void setFeedPressure(double feedPressure) {
        this.feedPressure = feedPressure;
    }

    public double getRetentatePressure() {
        return retentatePressure;
    }

    public void setRetentatePressure(double retentatePressure) {
        this.retentatePressure = retentatePressure;
    }

    public double getPermeatePressure() {
        return permeatePressure;
    }

    public void setPermeatePressure(double permeatePressure) {
        this.permeatePressure = permeatePressure;
    }

    public double getTmp() {
        return tmp;
    }

    public void setTmp(double tmp) {
        this.tmp = tmp;
    }

    public int getMainPumpSpeed() {
        return mainPumpSpeed;
    }

    public void setMainPumpSpeed(int mainPumpSpeed) {
        this.mainPumpSpeed = mainPumpSpeed;
    }

    public double getMainPumpFlowRate() {
        return mainPumpFlowRate;
    }

    public void setMainPumpFlowRate(double mainPumpFlowRate) {
        this.mainPumpFlowRate = mainPumpFlowRate;
    }

    public double getAuxPump1FlowRate() {
        return auxPump1FlowRate;
    }

    public void setAuxPump1FlowRate(double auxPump1FlowRate) {
        this.auxPump1FlowRate = auxPump1FlowRate;
    }

    public double getAuxPump2FlowRate() {
        return auxPump2FlowRate;
    }

    public void setAuxPump2FlowRate(double auxPump2FlowRate) {
        this.auxPump2FlowRate = auxPump2FlowRate;
    }

    public double getFeedScale() {
        return feedScale;
    }

    public void setFeedScale(double feedScale) {
        this.feedScale = feedScale;
    }

    public Double getmPermeate() {
        return mPermeate;
    }

    public void setmPermeate(Double mPermeate) {
        this.mPermeate = mPermeate;
    }

    public Double getTotalPermWt() {
        return totalPermWt;
    }

    public void setTotalPermWt(Double totalPermWt) {
        this.totalPermWt = totalPermWt;
    }

    public double getDiaFiltrationVol1() {
        return diaFiltrationVol1;
    }

    public void setDiaFiltrationVol1(double diaFiltrationVol1) {
        this.diaFiltrationVol1 = diaFiltrationVol1;
    }

    public double getDiaFiltrationVol2() {
        return diaFiltrationVol2;
    }

    public void setDiaFiltrationVol2(double diaFiltrationVol2) {
        this.diaFiltrationVol2 = diaFiltrationVol2;
    }

    public double getConcentrationFactor() {
        return concentrationFactor;
    }

    public void setConcentrationFactor(double concentrationFactor) {
        this.concentrationFactor = concentrationFactor;
    }

    public double getDeltaP() {
        return deltaP;
    }

    public void setDeltaP(double deltaP) {
        this.deltaP = deltaP;
    }

    public double getFeedFlowRate() {
        return feedFlowRate;
    }

    public void setFeedFlowRate(double feedFlowRate) {
        this.feedFlowRate = feedFlowRate;
    }

    public double getPermeateFlowRate() {
        return permeateFlowRate;
    }

    public void setPermeateFlowRate(double permeateFlowRate) {
        this.permeateFlowRate = permeateFlowRate;
    }

    public double getRetentateFlowRate() {
        return retentateFlowRate;
    }

    public void setRetentateFlowRate(double retentateFlowRate) {
        this.retentateFlowRate = retentateFlowRate;
    }

    public double getVt() {
        return vt;
    }

    public void setVt(double vt) {
        this.vt = vt;
    }

    public String getShear() {
        return shear;
    }

    public void setShear(String shear) {
        this.shear = shear;
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

    public double getFlux() {
        return flux;
    }

    public void setFlux(double flux) {
        this.flux = flux;
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

    public Double getConductivity1() {
        return conductivity1;
    }

    public void setConductivity1(Double conductivity1) {
        this.conductivity1 = conductivity1;
    }

    public Double getConductivity2() {
        return conductivity2;
    }

    public void setConductivity2(Double conductivity2) {
        this.conductivity2 = conductivity2;
    }

    public Double getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(Double temperature1) {
        this.temperature1 = temperature1;
    }

    public Double getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(Double temperature2) {
        this.temperature2 = temperature2;
    }

    public Double getKonduitCh1() {
        return konduitCh1;
    }

    public void setKonduitCh1(Double konduitCh1) {
        this.konduitCh1 = konduitCh1;
    }

    public Double getKonduitCh2() {
        return konduitCh2;
    }

    public void setKonduitCh2(Double konduitCh2) {
        this.konduitCh2 = konduitCh2;
    }

    public int getKonduitCh1Type() {
        return konduitCh1Type;
    }

    public void setKonduitCh1Type(int konduitCh1Type) {
        this.konduitCh1Type = konduitCh1Type;
    }

    public int getKonduitCh2Type() {
        return konduitCh2Type;
    }

    public void setKonduitCh2Type(int konduitCh2Type) {
        this.konduitCh2Type = konduitCh2Type;
    }

    public int getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(int isPaused) {
        this.isPaused = isPaused;
    }

    public int getFlowRateCount() {
        return flowRateCount;
    }

    public void setFlowRateCount(int flowRateCount) {
        this.flowRateCount = flowRateCount;
    }

    public Double getPermeateTotal() {
        return permeateTotal;
    }

    public void setPermeateTotal(Double permeateTotal) {
        this.permeateTotal = permeateTotal;
    }

    public Double getPermeateTotalWithHoldup() {
        return permeateTotalWithHoldup;
    }

    public void setPermeateTotalWithHoldup(Double permeateTotalWithHoldup) {
        this.permeateTotalWithHoldup = permeateTotalWithHoldup;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "TrialBuffer{" +
                "timestamp=" + timestamp +
                ", feedPressure=" + feedPressure +
                ", retentatePressure=" + retentatePressure +
                ", permeatePressure=" + permeatePressure +
                ", tmp=" + tmp +
                ", mainPumpSpeed=" + mainPumpSpeed +
                ", mainPumpFlowRate=" + mainPumpFlowRate +
                ", auxPump1FlowRate=" + auxPump1FlowRate +
                ", auxPump2FlowRate=" + auxPump2FlowRate +
                ", conductivity1=" + conductivity1 +
                ", conductivity2=" + conductivity2 +
                ", temperature1=" + temperature1 +
                ", temperature2=" + temperature2 +
                ", feedScale=" + feedScale +
                ", mPermeate=" + mPermeate +
                ", totalPermWt=" + totalPermWt +
                ", diaFiltrationVol1=" + diaFiltrationVol1 +
                ", diaFiltrationVol2=" + diaFiltrationVol2 +
                ", concentrationFactor=" + concentrationFactor +
                ", deltaP=" + deltaP +
                ", feedFlowRate=" + feedFlowRate +
                ", permeateFlowRate=" + permeateFlowRate +
                ", retentateFlowRate=" + retentateFlowRate +
                ", vt=" + vt +
                ", shear='" + shear + '\'' +
                ", abv1='" + abv1 + '\'' +
                ", abv2='" + abv2 + '\'' +
                ", flux=" + flux +
                ", tcf='" + tcf + '\'' +
                ", waterFlux20Degree='" + waterFlux20Degree + '\'' +
                ", waterPerm='" + waterPerm + '\'' +
                ", nwp='" + nwp + '\'' +
                ", konduitCh1=" + konduitCh1 +
                ", konduitCh2=" + konduitCh2 +
                ", konduitCh1Type=" + konduitCh1Type +
                ", konduitCh2Type=" + konduitCh2Type +
                ", isPaused=" + isPaused +
                ", flowRateCount=" + flowRateCount +
                ", permeateTotal=" + permeateTotal +
                ", permeateTotalWithHoldup=" + permeateTotalWithHoldup +
                ", duration='" + duration + '\'' +
                '}';
    }
}
