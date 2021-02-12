/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 10/6/19 4:48 PM
 * Modified : 10/6/19 4:48 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;

public class ValveProp {

    private int stepSize;
    private PressureTarget target;
    private double setPos;
    private double openPosition;
    private double closePosition;
    private boolean autoMode;
    private double operatingPressure;
    private double retentatePressure;
    private double maxRPM;
    private double minRPM;

    public double getOperatingPressure() {
        return operatingPressure;
    }

    public boolean isAutoMode() {
        return autoMode;
    }

    public ValveProp(PressureTarget target, double setPos, double openPosition, double closePosition, double operatingPressure, boolean autoMode,int stepSize) {
        this.target = target;
        this.setPos = setPos;
        this.openPosition = openPosition;
        this.closePosition = closePosition;
        this.operatingPressure = operatingPressure;
        this.autoMode = autoMode;
        this.stepSize = stepSize;
        this.retentatePressure = 0;
    }

    public ValveProp(PressureTarget target, double setPos, double openPosition, double closePosition, double operatingPressure, boolean autoMode,int stepSize,double retentatePressure) {
        this.target = target;
        this.setPos = setPos;
        this.openPosition = openPosition;
        this.closePosition = closePosition;
        this.operatingPressure = operatingPressure;
        this.autoMode = autoMode;
        this.stepSize = stepSize;
        this.retentatePressure =retentatePressure;
    }

    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }


    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setOperatingPressure(double operatingPressure) {
        this.operatingPressure = operatingPressure;
    }

    public PressureTarget getTarget() {
        return target;
    }

    public void setTarget(PressureTarget target) {
        this.target = target;
    }

    public double getSetPos() {
        return setPos;
    }

    public void setSetPos(double setPos) {
        this.setPos = setPos;
    }

    public double getOpenPosition() {
        return openPosition;
    }

    public void setOpenPosition(double openPosition) {
        this.openPosition = openPosition;
    }

    public double getClosePosition() {
        return closePosition;
    }

    public void setClosePosition(double closePosition) {
        this.closePosition = closePosition;
    }


    public double getRetentatePressure() {
        return retentatePressure;
    }

    public void setRetentatePressure(double retentatePressure) {
        this.retentatePressure = retentatePressure;
    }

    public double getMaxRPM() {
        return maxRPM;
    }

    public void setMaxRPM(double maxRPM) {
        this.maxRPM = maxRPM;
    }

    public double getMinRPM() {
        return minRPM;
    }

    public void setMinRPM(double minRPM) {
        this.minRPM = minRPM;
    }
}
