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

public class TFDFMode extends iModeManager {

    private static TFDFMode singleInstance;// = new ModeCalculator();

    private TFDFMode() {
    }

    public static TFDFMode get() {
        if (singleInstance == null) {
            singleInstance = new TFDFMode();
        }
        return singleInstance;
    }

    @Override
    public iModeManager setup() {
        return null;
    }

    @Override
    public iModeManager pause() {
        return null;
    }

    @Override
    public iModeManager resume() {
        return null;
    }

    @Override
    public iModeManager stop() {
        return null;
    }
}
