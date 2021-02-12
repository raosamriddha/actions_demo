/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 11:32 AM
 * Modified : 9/7/19 11:32 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.FlowProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.RecirculationProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ValveProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;

/**
 * controls current pressure
 * handle recirculation / Slow for Valve
 */
public class FluxPressureManager {
    /**
     *
     */
    private static FluxPressureManager pressureManager;
    private RecirculationProp recirculationProp;
    /**
     *
     */
    private int[] valveSetPos;
    private ValveProp[] valveProp;
    private FlowProp flowProp;
    private ModeListener superCallback;
    private boolean secondValve;
    private final RxListener pressureControlListener = new RxListener(ComLib.get().getPressureInfo()) {

        double recirculationSpeedFactor;
        int wantPositionWhole;
        int wantPositionFraction;
        int startStepSize;
        int stepSize;
        double changeCount;
        double signCount;
        boolean lastSign; // true -> positive and false -> negative
        boolean lastMoveSign;  // true -> positive and false -> negative
        double close_7_by_8;
        int allClose;
        double stepChange;
        boolean passedHalfPressure;
        boolean passed_7_by_8;
        boolean passedClosed;
        boolean passedWholePressure;
        boolean inControl;

        @Override
        public void reset() {
            recirculationSpeedFactor = 1;
            for (ValveProp prop : valveProp) {
                if(prop!=null) {
                    wantPositionWhole = (int) (prop.getOpenPosition() + prop.getSetPos() * ((prop.getClosePosition() - prop.getOpenPosition()) / 100));
                    wantPositionFraction = 0;
                    startStepSize = prop.getStepSize() * 256;
                    stepSize = prop.getStepSize() * 256;
                    changeCount = 4;
                    signCount = 4;
                    lastSign = true;
                    lastMoveSign = true;
                    close_7_by_8 = prop.getOpenPosition() + ((prop.getClosePosition() - prop.getOpenPosition()) * 7 / 8);
                    allClose = (int) prop.getClosePosition();
                    stepChange = 8;
                    passedHalfPressure = false;
                    passed_7_by_8 = false;
                    passedClosed = false;
                    passedWholePressure = false;
                    inControl = true;
                }
            }
        }

        private double getCp(int i) {
            switch (valveProp[i].getTarget()) {
                case PERMEATE:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.PERMEATE_CHANNEL);
                case RETENTATE:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.RETENTATE_CHANNEL);
                case TMP:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.TMP_CHANNEL);
                case FEED:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.FEED_CHANNEL);
                default:
                    return Double.MAX_VALUE;
            }
        }

        private void handleRecirculation(int factor) {
            if (recirculationProp != null) {
                recirculationSpeedFactor = recirculationSpeedFactor + factor * (recirculationProp.getRate() / (recirculationProp.getInterval() * 100.0));
                recirculationSpeedFactor = ufx.boundInRange(recirculationSpeedFactor, 0, 1);
                flowProp.setSlowForValveFactor(recirculationSpeedFactor);
                sLog.d(this, "Slow For Valve");
            }
        }


        @Override
        public void OnReceive() {
            if(count >=10) {
                int newWhole = 0;
                int newFraction = 0;
                double currentPressureError;
                boolean flagLastMoveSign = lastMoveSign;
                double wholeChange = 0xFFFF & (stepSize >> 8);
                double fractionChange = 0xFF & stepSize;
                for (int i = 0; i < valveProp.length; i++) {
                    if (valveProp[i] != null) {
                        if (valveProp[i].getTarget() != PressureTarget.PERMEATE) {
                            double currentPressure = getCp(i);
                            newWhole = wantPositionWhole;
                            newFraction = wantPositionFraction;
                            currentPressureError = valveProp[i].getOperatingPressure() - currentPressure;
                            if (changeCount != 0) --changeCount;
                            if (signCount != 0) --signCount;
                            sLog.d(this, "pressure " + currentPressure + "  i = " + i + " pressure current pos:- " + ComLib.get().getValveInfo().getValveCurPos(i));
                            if (recirculationSpeedFactor == 1) {
                                if (currentPressureError > valveProp[i].getOperatingPressure() *0.02) {
                                    inControl = true;
                                    if (!lastSign) {
                                        signCount = 4;
                                        lastSign = true;
                                    }
                                    boolean pressureOverHalf = currentPressureError < (valveProp[i].getOperatingPressure() / 2);
                                    boolean moreThan_7_by_8 = newWhole >= close_7_by_8;
                                    if (pressureOverHalf && moreThan_7_by_8 && (stepSize > (startStepSize / 4))) {
                                        stepChange = 4;
                                        changeCount = 4;
                                        stepSize = startStepSize / 4;
                                    }
                                    if (pressureOverHalf && !passedHalfPressure) {
                                        passedHalfPressure = true;
                                        int WholeStep = 0xFFFF & (stepSize >> 7);
                                        if (newWhole > WholeStep) newWhole -= WholeStep;
                                        else newWhole = 0;
                                        int MicroStep = 0xFF & (stepSize);
                                        if (newFraction > MicroStep) newFraction -= MicroStep;
                                        else newFraction = 0;
                                        stepSize = stepSize / 4;
                                        if (stepSize < 2) stepSize = 2;
                                        stepChange = 8;
                                        changeCount = 4;
                                    }
                                    if (signCount == 0 && changeCount == 0) {
                                        flagLastMoveSign = lastMoveSign;
                                        if (!flagLastMoveSign) {
                                            lastMoveSign = true;
                                            stepSize = stepSize / 2;
                                            if (stepSize < 2) stepSize = 2;
                                            stepChange = 8;
                                        } else {
                                            if (stepChange != 0) --stepChange;
                                            if (stepChange == 0) {
                                                stepChange = 8;
                                                stepSize *= 2;
                                                if (stepSize > startStepSize) stepSize = startStepSize;
                                            }
                                        }
                                        newWhole += 0xFFFF & (stepSize >> 8);
                                        newFraction += 0xFF & stepSize;
                                        if (newFraction > 0xFF) {
                                            newFraction -= 0x100;
                                            newWhole++;
                                        }
                                        if (newWhole > valveProp[i].getClosePosition())
                                            newWhole = (int) valveProp[i].getClosePosition();
                                        changeCount = 4;
                                        if (recirculationSpeedFactor == 1) {
                                            ComLib.get().getValveInfo().setValvePosition(i, newWhole, newFraction);
                                        } else {
                                            handleRecirculation(1);
                                        }
                                    }
                                    if (!passed_7_by_8 && wantPositionWhole <= close_7_by_8 && newWhole >= close_7_by_8) {
                                        stepSize /= 4;
                                        if (stepSize < 2) stepSize = 2;
                                        stepChange = 8;
                                        passed_7_by_8 = true;
                                        lastSign = false;
                                    }
                                    if (!passedClosed && wantPositionWhole < allClose && newWhole >= allClose) {
                                        int backout = (0xFFFF & (stepSize >> 8)) * 2;
                                        if (backout < 1) backout = 1;
                                        newWhole = allClose - backout;
                                        stepSize /= 2;
                                        if (stepSize < 2) stepSize = 2;
                                        stepChange = 8;
                                        passedClosed = true;
                                        lastSign = false;
                                        superCallback.callback(ModeEvent.ERROR, ModeError.UNABLE_TO_MAINTAIN_PRESSURE, ModeMsg.VALVE_FULL_CLOSE);
                                    }
                                } else if (currentPressureError < -valveProp[i].getOperatingPressure() *0.02) {
                                    if (!passedWholePressure) {
                                        passedWholePressure = true;
                                        int WholeStep = 0xFFFF & (stepSize >> 8);
                                        if (newWhole > WholeStep) newWhole -= WholeStep;
                                        else newWhole = 0;
                                    }
                                    if (lastSign) {
                                        lastSign = false;
                                        signCount = 4;
                                    }
                                    if (!passedHalfPressure) {
                                        signCount = 0;
                                        changeCount = 0;
                                        passedHalfPressure = true;
                                        if (newWhole > stepSize) {
                                            newWhole -= stepSize;
                                        }
                                    }
                                    if (signCount == 0 && changeCount == 0) {
                                        boolean signChange = lastMoveSign;
                                        if (signChange) {
                                            lastMoveSign = false;
                                            stepSize /= 2;
                                            if (stepSize < 2) stepSize = 2;
                                            stepChange = 8;
                                        } else {
                                            if (stepChange != 0) --stepChange;
                                            if (stepChange == 0) {
                                                stepChange = 8;
                                                stepSize *= 2;
                                                if (stepSize > startStepSize) stepSize = startStepSize;
                                            }
                                        }
                                        wholeChange = 0xFFFF & (stepSize >> 8);
                                        fractionChange = 0xFF & stepSize;
                                        newFraction -= fractionChange;
                                        if (newFraction < 0) {
                                            newFraction += 256;
                                            wholeChange++;
                                        }
                                        if (newWhole > wholeChange) newWhole -= wholeChange;
                                        else newWhole = 0;
                                        changeCount = 4;
                                        if (newWhole < valveProp[i].getOpenPosition())
                                            newWhole = (int) valveProp[i].getOpenPosition();
                                        inControl = newWhole != valveProp[i].getOpenPosition();
                                        if (!inControl) {
                                            ComLib.get().getValveInfo().setValvePosition(i, ((newWhole)), newFraction);
                                            superCallback.callback(ModeEvent.ERROR, ModeError.UNABLE_TO_MAINTAIN_PRESSURE, ModeMsg.VALVE_FULL_OPEN);
                                            handleRecirculation(-1);
                                        } else {
                                            ComLib.get().getValveInfo().setValvePosition(i, ((newWhole)), newFraction);
                                        }
                                    }
                                } else {
                                    inControl = true;
                                }
                            } else {
                                if (currentPressureError > valveProp[i].getOperatingPressure() *0.02) {
                                    handleRecirculation(1);
                                } else if (currentPressureError < -valveProp[i].getOperatingPressure() *0.02) {
                                    handleRecirculation(-1);
                                }
                            }
                            String s = "prop{" +
                                    "recirculationSpeedFactor=" + recirculationSpeedFactor +
                                    ", wantPositionWhole=" + wantPositionWhole +
                                    ", wantPositionFraction=" + wantPositionFraction +
                                    ", startStepSize=" + startStepSize +
                                    ", stepSize=" + stepSize +
                                    ", changeCount=" + changeCount +
                                    ", signCount=" + signCount +
                                    ", lastSign=" + lastSign +
                                    ", lastMoveSign=" + lastMoveSign +
                                    ", close_7_by_8=" + close_7_by_8 +
                                    ", allClose=" + allClose +
                                    ", stepChange=" + stepChange +
                                    ", passedHalfPressure=" + passedHalfPressure +
                                    ", passed_7_by_8=" + passed_7_by_8 +
                                    ", passedClosed=" + passedClosed +
                                    ", passedWholePressure=" + passedWholePressure +
                                    ", inControl=" + inControl +
                                    ", newWhole=" + newWhole +
                                    ", newFraction=" + newFraction +
                                    ", currentPressureError=" + currentPressureError +
                                    ", flagLastMoveSign=" + flagLastMoveSign +
                                    ", wholeChange=" + wholeChange +
                                    ", fractionChange=" + fractionChange +
                                    '}';
                            sLog.d(this, s);
                        }

                        wantPositionWhole = newWhole;
                        wantPositionFraction = newFraction;
                    }
                }
            }else{
                count++;
            }
        }

    };
    private int count=11;

    public static FluxPressureManager get() {
        if (pressureManager == null) {
            pressureManager = new FluxPressureManager();
        }
        return pressureManager;
    }

    public void start(ValveProp[] valveProp, RecirculationProp recirculationProp, FlowProp flowProp, ModeListener superCallback, boolean secondValveInUse) {
        this.superCallback = superCallback;
        if (valveProp != null) {
            this.recirculationProp = recirculationProp;
            this.valveProp = valveProp;
            this.flowProp = flowProp;
            valveSetPos = new int[valveProp.length];
            int i = 0;
            secondValve = secondValveInUse;
            for (ValveProp v : valveProp) {
                if(v!=null) {
                    valveSetPos[i] = (int) (v.getOpenPosition() + v.getSetPos() * ((v.getClosePosition() - v.getOpenPosition()) / 100));
                    ComLib.get().getValveInfo().setValvePosition(i, valveSetPos[i], 0);
                    if (v.isAutoMode()) {
                        if(v.getTarget()==PressureTarget.DELTA){
                            DeltaPressureManager.get().start(valveProp, recirculationProp, flowProp, superCallback,secondValveInUse);
                        }else {
                            pressureControlListener.startRxListening();
                        }
                        sLog.d(FluxPressureManager.this, valveSetPos[i] + " Starting Pressure Control");
                    }
                }
                i++;
            }
        }
    }

    public void stop() {
        DeltaPressureManager.get().stop();
        pressureControlListener.stopRxListening();
        ComLib.get().getValveInfo().setValvePosition(0, 1, 0);
        if(secondValve)
        ComLib.get().getValveInfo().setValvePosition(1, 1, 0);
    }

    public void reset() {
        pressureControlListener.stopRxListening();
        int i = 0;
        count=0;
        for (ValveProp v : valveProp) {
            if(v!=null) {
                if (v.isAutoMode()) {
                    valveSetPos[i] = (int) (v.getOpenPosition() + v.getSetPos() * ((v.getClosePosition() - v.getOpenPosition()) / 100));
                    ComLib.get().getValveInfo().setValvePosition(i, valveSetPos[i], 0);
                    if(v.getTarget()==PressureTarget.DELTA){
                        DeltaPressureManager.get().start(valveProp, recirculationProp, flowProp, superCallback,secondValve);
                    }else {
                        pressureControlListener.startRxListening();
                    }
                    sLog.d(FluxPressureManager.this, valveSetPos[i] + " Starting Pressure Control again");
                }
            }
            i++;
        }
    }
}
