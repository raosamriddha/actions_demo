/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 11/7/19 12:19 PM
 * Modified : 11/7/19 12:19 PM
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
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ValveProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;

/**
 * used to ramp up main pump this is start
 * will generate RampUpDone event
 */
public class RampManager {

    /**
     *
     */
    private static RampManager rampManager;
    private ModeListener superCallback;
    private FlowProp prop;
    private ModeProp modeProp;
    private boolean calculateDeltaP = false;
    private Double deltaPValue = 0.0;
    private double startTime;
    private double stopTime;

    public static RampManager get() {
        if (rampManager == null) {
            rampManager = new RampManager();
        }
        return rampManager;
    }

    private RampManager() {
    }

    private final RxListener rampListener = new RxListener(ComLib.get().getMainPump()) {

        @Override
        public void OnReceive() {

            if (ufx.time() < stopTime) {
                ComLib.get().getMainPump().setFlow_mlpm(prop.getMainPumpFlowRate() * (ufx.time() - startTime) / prop.getMainPumpRampDuration(), prop.getMainPumpTubeFlowConversion(), prop.getMainPumpSpeedFactor());
            } else {
                stop();
                superCallback.callback(ModeEvent.RAMP_UP_DONE);
            }
            if(calculateDeltaP){
                double deltaP = ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.FEED_CHANNEL)-ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.RETENTATE_CHANNEL);
                if(Math.abs(deltaPValue-deltaP)<= 0.5){
                    modeProp.getFlowProp().setMainPumpFlowRate(ComLib.get().getMainPump().getFlowRate(prop.getMainPumpTubeFlowConversion(), prop.getMainPumpSpeedFactor()));
                    stop();
                    superCallback.callback(ModeEvent.RAMP_UP_DONE);
                    return;
                }
            }
        }
    };

    public void start(FlowProp prop, ModeListener callback, ModeProp modeProp) {
        this.modeProp = modeProp;
        if (prop == null || prop.getMainPumpRampDuration() == 0) {
            sLog.d(this, "Not using Ramp up");
            superCallback.callback(ModeEvent.RAMP_UP_DONE);
        } else {
            this.superCallback = callback;
            this.prop = prop;
            this.startTime = ufx.time();
            this.stopTime = ufx.time() + prop.getMainPumpRampDuration();
            rampListener.startRxListening();
            if(modeProp.getValveProps()!=null) {
                for (ValveProp v : modeProp.getValveProps()) {
                    if (v != null) {
                        if (v.isAutoMode()) {
                            if (v.getTarget() == PressureTarget.DELTA) {
                                calculateDeltaP = true;
                                deltaPValue = v.getOperatingPressure();
                            }
                        }
                    }
                }
            }
        }
    }

    public void stop() {
        rampListener.stopRxListening();
        calculateDeltaP = false;
    }
}