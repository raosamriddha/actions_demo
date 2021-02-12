/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 18/7/19 2:20 PM
 * Modified : 18/7/19 2:18 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.run.subModes;

import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;

public class VMode extends iModeManager {

    private static VMode singleInstance;// = new ModeCalculator();

    private VMode() {
    }

    public static VMode get() {
        if (singleInstance == null) {
            singleInstance = new VMode();
        }
        return singleInstance;
    }

    @Override
    public iModeManager setup() {
        return this;
    }

    @Override
    public iModeManager pause() {
        return this;
    }

    @Override
    public iModeManager resume() {
        return this;
    }

    @Override
    public iModeManager stop() {
        return this;
    }
}
