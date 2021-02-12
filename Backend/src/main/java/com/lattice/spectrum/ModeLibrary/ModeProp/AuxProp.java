/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 10/6/19 4:49 PM
 * Modified : 10/6/19 4:49 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

public class AuxProp {

    private int pumpID;
    private int typeID;
    private int maxRPM;
    private double tubeFlowConversion;
    private double flowRate;

    public AuxProp(int pumpID, int typeID, int maxRPM, double tubeFlowConversion, double flowRate) {
        this.pumpID = pumpID;
        this.typeID = typeID;
        this.maxRPM = maxRPM;
        this.tubeFlowConversion = tubeFlowConversion;
        this.flowRate = flowRate;
    }

    public int getPumpID() {
        return pumpID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getMaxRPM() {
        return maxRPM;
    }

    public void setMaxRPM(int maxRPM) {
        this.maxRPM = maxRPM;
    }

    public double getTubeFlowConversion() {
        return tubeFlowConversion;
    }

    public void setTubeFlowConversion(double tubeFlowConversion) {
        this.tubeFlowConversion = tubeFlowConversion;
    }

    public double getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(double flowRate) {
        this.flowRate = flowRate;
    }
}
