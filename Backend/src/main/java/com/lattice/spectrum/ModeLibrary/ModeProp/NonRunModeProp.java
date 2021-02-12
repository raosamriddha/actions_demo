/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 5/7/19 10:21 AM
 * Modified : 5/7/19 10:21 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointParam;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;

public class NonRunModeProp {

    private PressureTarget pressureTarget;
    private double operatingPressure;

    private double mainFlowRate;
    private int targetDuration;

    private int auxPumpID;

    private EndPointTarget endPointTarget;
    private EndPointParam endPointParam;

    public EndPointTarget getEndPointTarget() {
        return endPointTarget;
    }

    public NonRunModeProp setEndPointTarget(EndPointTarget endPointTarget) {
        this.endPointTarget = endPointTarget;
        return this;
    }

    public EndPointParam getEndPointParam() {
        return endPointParam;
    }

    public NonRunModeProp setEndPointParam(EndPointParam endPointParam) {
        this.endPointParam = endPointParam;
        return this;
    }

    public double getEndPointValue() {
        return endPointValue;
    }

    public NonRunModeProp setEndPointValue(double endPointValue) {
        this.endPointValue = endPointValue;
        return this;
    }

    private double endPointValue;
    /*
    public NonRunModeProp(PressureTarget pressureTarget, double operatingPressure, int targetDuration, double mainFlowRate, int auxPumpID) {
        this.pressureTarget = pressureTarget;
        this.operatingPressure = operatingPressure;
        this.targetDuration = targetDuration;
        this.mainFlowRate = mainFlowRate;
        this.auxPumpID = auxPumpID;
    }*/

    public NonRunModeProp setAuxPumpID(int auxPumpID) {
        this.auxPumpID = auxPumpID;
        return this;
    }

    public NonRunModeProp setPressureTarget(PressureTarget pressureTarget) {
        this.pressureTarget = pressureTarget;
        return this;
    }

    public NonRunModeProp setOperatingPressure(double operatingPressure) {
        this.operatingPressure = operatingPressure;
        return this;
    }

    public NonRunModeProp setTargetDuration(int targetDuration) {
        this.targetDuration = targetDuration;
        return this;
    }

    public NonRunModeProp setMainFlowRate(double mainFlowRate) {
        this.mainFlowRate = mainFlowRate;
        return this;
    }

    public int getAuxPumpID() {
        return auxPumpID;
    }

    public PressureTarget getPressureTarget() {
        return pressureTarget;
    }

    public double getOperatingPressure() {
        return operatingPressure;
    }

    public int getTargetDuration() {
        return targetDuration;
    }

    public double getMainFlowRate() {
        return mainFlowRate;
    }

}
