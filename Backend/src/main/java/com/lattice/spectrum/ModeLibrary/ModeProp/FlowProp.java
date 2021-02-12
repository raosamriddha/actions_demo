/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 11:25 AM
 * Modified : 9/7/19 11:25 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

import com.lattice.spectrum.ModeLibrary.ModeProp.type.FlowControlMode;

public class FlowProp {

    private FlowControlMode flowControl;
    private double mainPumpFlowRate;
    private double mainPumpTubeFlowConversion;
    private double mainPumpSpeedFactor;
    private double slowForValveFactor;
    private double flowFactor;

    public FlowProp(FlowControlMode flowControl, double mainPumpFlowRate, double mainPumpTubeFlowConversion, double mainPumpSpeedFactor, int mainPumpRampDuration,double flowFactor) {
        this.flowControl = flowControl;
        this.mainPumpFlowRate = mainPumpFlowRate;
        this.mainPumpTubeFlowConversion = mainPumpTubeFlowConversion;
        this.mainPumpSpeedFactor = mainPumpSpeedFactor;
        this.mainPumpRampDuration = mainPumpRampDuration;
        this.slowForValveFactor = 1;
        this.flowFactor = flowFactor;
    }

    private int mainPumpRampDuration;

    public FlowControlMode getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(FlowControlMode flowControl) {
        this.flowControl = flowControl;
    }

    public double getMainPumpFlowRate() {
        return mainPumpFlowRate;
    }

    public void setMainPumpFlowRate(double mainPumpFlowRate) {
        this.mainPumpFlowRate = mainPumpFlowRate;
    }

    public double getMainPumpTubeFlowConversion() {
        return mainPumpTubeFlowConversion;
    }

    public void setMainPumpTubeFlowConversion(double mainPumpTubeFlowConversion) {
        this.mainPumpTubeFlowConversion = mainPumpTubeFlowConversion;
    }

    public double getMainPumpSpeedFactor() {
        return mainPumpSpeedFactor;
    }

    public void setMainPumpSpeedFactor(double mainPumpSpeedFactor) {
        this.mainPumpSpeedFactor = mainPumpSpeedFactor;
    }

    public int getMainPumpRampDuration() {
        return mainPumpRampDuration;
    }

    public void setMainPumpRampDuration(int mainPumpRampDuration) {
        this.mainPumpRampDuration = mainPumpRampDuration;
    }

    /** internal function should not be called by backend
     *
     * @return
     */
    public void setSlowForValveFactor(double slowForValveFactor) {
        this.slowForValveFactor = slowForValveFactor;
    }

    /** internal function should not be called by backend
     *
     * @return
     */
    public double getSlowForValveFactor() {
        return slowForValveFactor;
    }

    public double getFlowFactor() {
        return flowFactor;
    }

    public void setFlowFactor(double flowFactor) {
        this.flowFactor = flowFactor;
    }
}
