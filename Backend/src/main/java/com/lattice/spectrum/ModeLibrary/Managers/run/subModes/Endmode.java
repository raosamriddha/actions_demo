/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 18/7/19 2:20 PM
 * Modified : 18/7/19 2:18 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.run.subModes;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;

import static com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent.SUB_MODE_FINISHED;

public class Endmode extends iModeManager {

    private static Endmode singleInstance;// = new ModeCalculator();

    private Endmode() {
    }

    public static Endmode get() {
        if (singleInstance == null) {
            singleInstance = new Endmode();
        }
        return singleInstance;
    }

    private double tStart;

    private final RxListener mainPumpListener = new RxListener(ComLib.get().getMainPump()) {
        @Override
        public void OnReceive() {
            double value = (ufx.time() - tStart);
            superCallback.callback(ModeEvent.FINAL_COLLECTION_TIME, (int)(30-value));
            if (value > 30) {
                stopRxListening();
                sLog.d(this, "Done final collection");
                superCallback.callback(ModeEvent.DONE_FINAL_COLLECTION);
            }
        }
    };

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
        tStart = ufx.time();
        superCallback.callback(ModeEvent.START_FINAL_COLLECTION);
        mainPumpListener.startRxListening();
        superCallback.callback(ModeEvent.FINAL_COLLECTION_TIME, 30);
        sLog.d(this, "Starts final collection");
        return this;
    }

    @Override
    public iModeManager stop() {
        return this;
    }

}
