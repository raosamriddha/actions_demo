/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 19/7/19 3:55 PM
 * Modified : 19/7/19 3:55 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.nonrun;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowMeterManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.PressureManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.RampManager;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

public class FlushingModeManager extends iModeManager {
    /**
     *
     */
    private static FlushingModeManager inst;

    public static FlushingModeManager get() {
        if (inst == null) {
            inst = new FlushingModeManager();
        }
        return inst;
    }

    private final ModeListener rampListener = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            superCallback.callback(id, obj);
            switch (id) {
                case RAMP_UP_DONE:
                    PressureManager.get().start(prop.getValveProps(), prop.getRecirculationProp(), prop.getFlowProp(), superCallback, prop.isSecondValveInUse());
                    if (prop.getPermPump() != null) {
                        sLog.d(FlushingModeManager.this, "Permeate Started");
                        ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), prop.getPermAuxPump().getFlowRate(), prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
                    }
                    FlowManager.get().start(prop.getFlowProp(),  prop.getPermAuxPump(),prop.getTotalizerMode(), superCallback);
                    if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
                        flowMeterEndPointListener.startRxListening();
                    }else {
                        endPointListener.startRxListening();
                    }
                    break;
            }
        }

    };
    private final RxListener endPointListener = new RxListener(ComLib.get().getScaleInfo()) {

        private int secTick;

        @Override
        public void reset() {
            secTick = 0;
        }

        @Override
        public void OnReceive() {
            switch (prop.getNonRunModes(stageID).getEndPointTarget()) {
                case Duration:
                    secTick++;
                    if (prop.getNonRunModes(stageID).getEndPointValue() <= secTick) {
                        superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID);
                        superCallback.callback(ModeEvent.ALL_MODE_FINISHED,prop.getSuperID());
                    }
                    break;
                case PermeateWeight:
                    if (prop.getNonRunModes(stageID).getEndPointValue() <= ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE)) {
                        superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID);
                        superCallback.callback(ModeEvent.ALL_MODE_FINISHED,prop.getSuperID());
                    }
                    break;
            }
        }
    };

    private final RxListener flowMeterEndPointListener = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            if (prop.getNonRunModes(stageID).getEndPointTarget() == EndPointTarget.PermeateVolume) {
                if (prop.getNonRunModes(stageID).getEndPointValue() <= FlowMeterManager.get().getVolume(0)) {
                    superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID);
                    superCallback.callback(ModeEvent.ALL_MODE_FINISHED,prop.getSuperID());
                }
            }
        }
    };
    private FlushingModeManager() {
    }

    @Override
    public iModeManager setup() {
        RampManager.get().start(prop.getFlowProp(), rampListener,prop);
        superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
        return this;
    }

    @Override
    public iModeManager resume() {
        RampManager.get().start(prop.getFlowProp(), rampListener,prop);
        return this;
    }

    @Override
    public iModeManager pause() {
        PressureManager.get().stop();
        if (prop.getPermPump() != null) {
            sLog.d(FlushingModeManager.this, "Permeate Stopped");
            ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0, prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
        }
        FlowManager.get().stop();
        endPointListener.stopRxListening();
        flowMeterEndPointListener.stopRxListening();
        return this;
    }

    @Override
    public iModeManager stop() {
        pause();
        return this;
    }
}
