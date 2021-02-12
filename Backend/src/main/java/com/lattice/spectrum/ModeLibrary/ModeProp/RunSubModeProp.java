/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 30/5/19 3:55 PM
 * Modified : 30/5/19 3:55 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;

public class RunSubModeProp {
    /**
     * Sub Mode type
     */
    private OperationMode mode;
    /**
     * Endpoint value
     */
    private EndPointTarget endPointTarget;
    private double endPointValue;
    /**
     * only used if feed scale not connected
     * can be used in cascading modes to reduce computational error
     */
    private boolean feedToEmptyEnable;
    /**
     * endpoint dependent extra settings optional
     */
    private int probeID;
    private int TubeCalTimeout;
    /**
     * used in D or CFC mode
     */
    private int auxPumpID;

    /**
     * Non Run mode FluxCV FluxCF NWP Cleaning flushing
     */
    public RunSubModeProp setMode(OperationMode mode) {
        this.mode = mode;
        return this;
    }

    public RunSubModeProp setFeedToEmptyEnable(boolean feedToEmptyEnable) {
        this.feedToEmptyEnable = feedToEmptyEnable;
        return this;
    }

    public RunSubModeProp setEndPointTarget(EndPointTarget endPointTarget) {
        this.endPointTarget = endPointTarget;
        return this;
    }

    public RunSubModeProp setEndPointValue(double endPointValue) {
        this.endPointValue = endPointValue;
        return this;
    }

    public RunSubModeProp setProbeID(int probeID) {
        this.probeID = probeID;
        return this;
    }

    public RunSubModeProp setTubeCalTimeout(int tubeCalTimeout) {
        TubeCalTimeout = tubeCalTimeout;
        return this;
    }

    public RunSubModeProp setAuxPumpID(int auxPumpID) {
        this.auxPumpID = auxPumpID;
        return this;
    }

    public OperationMode getMode() {
        return mode;
    }

    public boolean isFeedToEmptyEnable() {
        return feedToEmptyEnable;
    }

    public EndPointTarget getEndPointTarget() {
        return endPointTarget;
    }

    public double getEndPointValue() {
        return endPointValue;
    }

    public int getProbeID() {
        return probeID;
    }

    public int getTubeCalTimeout() {
        return TubeCalTimeout;
    }

    public int getAuxPumpID() {
        return auxPumpID;
    }


}
