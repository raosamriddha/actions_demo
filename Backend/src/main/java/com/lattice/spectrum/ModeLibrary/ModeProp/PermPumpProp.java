/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 12:10 PM
 * Modified : 9/7/19 12:10 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

public class PermPumpProp {

    private int pumpID;
    private boolean stopFirst;

    public PermPumpProp(int pumpID, boolean stopFirst) {
        this.pumpID = pumpID;
        this.stopFirst = stopFirst;
    }

    public void setStopFirst(boolean stopFirst) {
        this.stopFirst = stopFirst;
    }

    public void setPumpID(int pumpID) {
        this.pumpID = pumpID;
    }

    public boolean shouldStopFirst() {
        return stopFirst;
    }

    public int getPumpID() {
        return pumpID;
    }

}
