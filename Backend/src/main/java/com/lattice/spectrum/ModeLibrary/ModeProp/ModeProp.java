/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/5/19 10:43 AM
 * Modified : 25/4/19 1:06 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

public class ModeProp {

    /**
     * SuperMode Type
     */
    private OperationMode superID;
    //
    //
    //
    private Double feedStartWt;
    private Double permStartWt;
    /**
     * SubMode property as array
     */
    private RunSubModeProp[] runModes;
    /**
     * nonRun step prop as array
     */
    private NonRunModeProp[] nonRunModes;
    /**
     * calculated permeate hold up value
     */
    private double permHoldUp;

    /**
     * calculated feed hold up value
     */
    private double feedHoldUp;

    /**
     * calculated feed hold up value
     */
    private boolean isFeedEmpty;
    /**
     * aux pump setting if any
     */
    private AuxProp[] auxPumps;

    /**
     * valve setting if any
     */
    private ValveProp[] valves;
    private KonduitProp[] konduit;
    private double curCF=1;
    private double curDV;
    private double curKonduitCh1=0;
    private KonduitType curKonduitCh1Type;

    private double curKonduitCh2=0;
    private KonduitType curKonduitCh2Type;

    private RecirculationProp pressureProp;
    /**
     * Permeate Pump Settings if used
     */
    private PermPumpProp permPump;
    /**
     * Alarm settings
     */
    private AlarmProp alarmProp;
    /**
     * Target Flow Control
     */
    private FlowProp flowProp;

    private Double subModePercentComplete=0.0;
    private boolean isFeedConnected =false;

    private TotalizerMode totalizerMode;

    private boolean overrideValue = false;

    private double feedOffset = 0;

    private boolean secondValveInUse = false;

    /**
     * @return
     */
    public OperationMode getSuperID() {
        return superID;
    }

    public Double getFeedStartWt() {
        return feedStartWt;
    }

    public double getFeedOffset() {
        return feedOffset;
    }

    public void setFeedOffset(double feedOffset) {
        this.feedOffset = feedOffset;
    }

    public Double getPremStartWt() {
        return permStartWt;
    }

    public void setFeedStartWt(Double feedStartWt) {
        this.feedStartWt = feedStartWt;
    }

    public void setPermStartWt(Double permStartWt) {
        this.permStartWt = permStartWt;
    }

    public NonRunModeProp[] getNonRunModes() {
        return nonRunModes;
    }

    public int getSubModeCount() {
        return runModes != null ? runModes.length : 0;
    }

    public RunSubModeProp getSubRunModeProp(int modeStage) {
        return runModes[modeStage];
    }

    public NonRunModeProp getSubNonRunModeProp(int modeStage) {
        return nonRunModes[modeStage];
    }

    public NonRunModeProp getNonRunModes(int stageID) {
        return nonRunModes[stageID];
    }

    public double getPermHoldUp() {
        return permHoldUp;
    }

    public boolean isFeedEmpty() {
        return isFeedEmpty;
    }

    public double getFeedHoldUp() {
        return feedHoldUp;
    }

    public double getFeedHoldUpForStage1() {
        if (isFeedEmpty) {
            return feedHoldUp;
        } else {
            return 0.0;
        }
    }

    public AuxProp getAuxProp(int id) {
        for (int i = 0; i < auxPumps.length; i++) {
            if(auxPumps[i].getPumpID()==id){
                return auxPumps[i];
            }
        }
        return null;
    }

    public ValveProp getValveProp(int id) {
        return valves[id];
    }

    public PermPumpProp getPermPump() {
        return permPump;
    }

    public AuxProp getPermAuxPump() {
        return permPump != null ? getAuxProp(permPump.getPumpID()) : null;
    }

    public RunSubModeProp[] getRunModes() {
        return runModes;
    }

    public AuxProp[] getAuxPumps() {
        return auxPumps;
    }

    public ValveProp[] getValveProps() {
        return valves;
    }

    public AlarmProp getAlarmProp() {
        return alarmProp;
    }

    public FlowProp getFlowProp() {
        return flowProp;
    }

    public AlarmProp getAlarms() {
        return alarmProp;
    }

    public void setSuperID(OperationMode superID) {
        this.superID = superID;
    }

    public void setRunModes(RunSubModeProp[] runModes) {
        this.runModes = runModes;
    }

    public void setNonRunModes(NonRunModeProp[] nonRunModes) {
        this.nonRunModes = nonRunModes;
    }

    public void setPermHoldUp(double permHoldUp) {
        this.permHoldUp = permHoldUp;
    }

    public void setFeedHoldUp(double feedHoldUp) {
        this.feedHoldUp = feedHoldUp;
    }

    public void setFeedHoldUpEmpty(boolean feedEmpty) {
        isFeedEmpty = feedEmpty;
    }

    public void setAuxPumps(AuxProp[] auxPumps) {
        this.auxPumps = auxPumps;
    }

    public void setValves(ValveProp[] valves) {
        this.valves = valves;
    }

    public RecirculationProp getRecirculationProp() {
        return pressureProp;
    }

    public void setRecirculationProp(RecirculationProp pressureProp) {
        this.pressureProp = pressureProp;
    }

    public void setPermPump(PermPumpProp permPump) {
        this.permPump = permPump;
    }

    public void setAlarmProp(AlarmProp alarmProp) {
        this.alarmProp = alarmProp;
    }

    public void setFlowProp(FlowProp flowProp) {
        this.flowProp = flowProp;
    }

    public int getValveCount() {
        return valves == null ? 0 : valves.length;
    }

    public int getNonRunModeCount() {
        return nonRunModes.length;
    }

    public void setDV(double dv) {
        this.curDV = dv;
    }

    public void setCF(double cf) {
        this.curCF = cf;
    }

    public double getDV() {
        return curDV;
    }

    public double getCF() {
        return curCF;
    }

    public Double getSubModePercentComplete() {
        return subModePercentComplete;
    }

    public void setSubModePercentComplete(Double subModePercentComplete) {
        this.subModePercentComplete = subModePercentComplete;
    }


    public boolean isFeedConnected() {
        return isFeedConnected;
    }

    public void setFeedConnected(boolean feedConnected) {
        isFeedConnected = feedConnected;
    }

    public double getCurKonduitCh1() {
        return curKonduitCh1;
    }

    public void setCurKonduitCh1(double curKonduitCh1) {
        this.curKonduitCh1 = curKonduitCh1;
    }

    public KonduitType getCurKonduitCh1Type() {
        return curKonduitCh1Type;
    }

    public void setCurKonduitCh1Type(KonduitType curKonduitCh1Type) {
        this.curKonduitCh1Type = curKonduitCh1Type;
    }

    public TotalizerMode getTotalizerMode() {
        return totalizerMode;
    }

    public void setTotalizerMode(TotalizerMode totalizerMode) {
        this.totalizerMode = totalizerMode;
    }

    public KonduitProp[] getKonduit() {
        return konduit;
    }

    public void setKonduit(KonduitProp[] konduit) {
        this.konduit = konduit;
    }

    public KonduitProp getKonduit(KonduitChannel konduitChannel){
        for (int i = 0; i < konduit.length; i++) {
            if(konduitChannel.getChannelID()==konduit[i].getChannelID()){
                return konduit[i];
            }
        }
        return null;
    }
    public KonduitProp getKonduit(int probeID){
        for (int i = 0; i < konduit.length; i++) {
            if(probeID==konduit[i].getChannelID()){
                return konduit[i];
            }
        }
        return null;
    }

    public double getCurKonduitCh2() {
        return curKonduitCh2;
    }

    public void setCurKonduitCh2(double curKonduitCh2) {
        this.curKonduitCh2 = curKonduitCh2;
    }

    public KonduitType getCurKonduitCh2Type() {
        return curKonduitCh2Type;
    }

    public void setCurKonduitCh2Type(KonduitType curKonduitCh2Type) {
        this.curKonduitCh2Type = curKonduitCh2Type;
    }

    public boolean isOverrideValue() {
        return overrideValue;
    }

    public void setOverrideValue(boolean overrideValue) {
        this.overrideValue = overrideValue;
    }

    public boolean isSecondValveInUse() {
        return secondValveInUse;
    }

    public void setSecondValveInUse(boolean secondValveInUse) {
        this.secondValveInUse = secondValveInUse;
    }
}
