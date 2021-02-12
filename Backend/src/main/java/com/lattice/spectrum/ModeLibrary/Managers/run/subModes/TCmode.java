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
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;

import static com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent.SUB_MODE_FINISHED;

public class TCmode extends iModeManager {

    private static TCmode singleInstance;// = new ModeCalculator();
    private boolean timerStartFlag=true;

    private TCmode() {
    }

    public static TCmode get() {
        if (singleInstance == null) {
            singleInstance = new TCmode();
        }
        return singleInstance;
    }

    //
    // When your program starts up
    private double tStart;
    private double startWeight;
//    private double expectedVolume, actualVolume, calibrationFactor;

    private final RxListener scaleListener = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            if(ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE)>=1 ) {
                if (timerStartFlag) {
                    startWeight = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
                    tStart = ufx.time();
                    superCallback.callback(ModeEvent.TIMER_START);
                    timerStartFlag = false;
                } else {
                    if ((ufx.time() - tStart) > subProp.getTubeCalTimeout()) {
                        ComLib.get().getMainPump().setSpeed(0);
                        double expectedVolume = prop.getFlowProp().getMainPumpFlowRate() * (ufx.time() - tStart) / 60;
                        double actualVolume = Math.abs(ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) - startWeight);
                        double calibrationFactor = actualVolume / expectedVolume;
                        sLog.d(this, "Done:\nExpected Volume=" + expectedVolume + "\nMeasured Volume=" + actualVolume + "\nCalibration Factor=" + calibrationFactor);
                        stopRxListening();
                        superCallback.callback(ModeEvent.TC_ENDPOINT_REACHED, subProp.getMode(), expectedVolume, actualVolume);
                    }
                }
            }else{
                if(!timerStartFlag){
                    ComLib.get().getMainPump().setSpeed(0);
                    stopRxListening();
                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.TC_ERROR);
                }
            }

        }
    };

    @Override
    public iModeManager setup() {
        // init motor and wait for pressure reading
        startWeight = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
        timerStartFlag= true;
        return this;
    }

    @Override
    public iModeManager pause() {
        ComLib.get().getMainPump().setSpeed(0);
        scaleListener.stopRxListening();
        return this;
    }

    @Override
    public iModeManager resume() {
        ComLib.get().getMainPump().setFlow_mlpm(prop.getFlowProp().getMainPumpFlowRate(), prop.getFlowProp().getMainPumpTubeFlowConversion(), prop.getFlowProp().getMainPumpSpeedFactor());
        tStart = ufx.time();
        scaleListener.startRxListening();
        sLog.d(this, "Tubing Calibration Starts");
        return this;
    }

    @Override
    public iModeManager stop() {
        pause();
        // TODO hardcoded values to be generalized
        superCallback.callback(SUB_MODE_FINISHED, OperationMode.TubeCalib_MODE, 0);
        return this;
    }

}
