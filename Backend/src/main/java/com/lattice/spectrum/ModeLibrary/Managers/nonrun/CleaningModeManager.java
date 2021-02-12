/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 19/7/19 1:45 PM
 * Modified : 19/7/19 1:45 PM
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
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeMsg;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.RampManager;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

public class CleaningModeManager extends iModeManager {
    /**
     *
     */
    private static CleaningModeManager inst;

    public static CleaningModeManager get() {
        if (inst == null) {
            inst = new CleaningModeManager();
        }
        return inst;
    }

    private CleaningModeManager() {
    }

    private final ModeListener rampListener = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            superCallback.callback(id, obj);
            switch (id) {
                case RAMP_UP_DONE:
                    startStage();
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
                        stageID++;
                        if(stageID==2){
                            superCallback.callback(ModeEvent.ALL_MODE_FINISHED,prop.getSuperID());
                            stop();
                            return;
                        }{
                            superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
                        }
                        prepareNextIteration();
                    }
                    break;
                case PermeateWeight:
                    if (prop.getNonRunModes(stageID).getEndPointValue() <= ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE)) {
                            superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID);
                            stageID++;
                            if(stageID==2){
                                superCallback.callback(ModeEvent.ALL_MODE_FINISHED,prop.getSuperID());
                                stop();
                                return;
                            }else {
                                superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
                            }
                            prepareNextIteration();

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
                    stageID++;
                    if (stageID == 2) {
                        superCallback.callback(ModeEvent.ALL_MODE_FINISHED,prop.getSuperID());
                        stop();
                        return;
                    }else{
                        superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
                    }
                    prepareNextIteration();

                }
            }
        }
    };

    private void startStage() {
        switch (prop.getNonRunModes(stageID).getEndPointParam()) {
            case RetentateClean:
                if (prop.getPermPump() != null) {
                    sLog.d(CleaningModeManager.this, "Permeate Stop");
                    ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0, prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
                }
                if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
                    flowMeterEndPointListener.startRxListening();
                    endPointListener.stopRxListening();
                }else {
                    flowMeterEndPointListener.stopRxListening();
                    endPointListener.startRxListening();
                }
                break;
            case PermeateClean:
                if (prop.getPermPump() != null) {
                    sLog.d(CleaningModeManager.this, "Permeate Started");
                    ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), prop.getPermAuxPump().getFlowRate(), prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
                }
                if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
                    flowMeterEndPointListener.startRxListening();
                    endPointListener.stopRxListening();
                }else {
                    flowMeterEndPointListener.stopRxListening();
                    endPointListener.startRxListening();
                }
                break;
            default:
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_PARAMETER);
                break;
        }
        FlowManager.get().start(prop.getFlowProp(), null,prop.getTotalizerMode(), superCallback);
    }

    @Override
    public iModeManager setup() {
        //        valve length always going to be 1
//
        stageID = 0;
        prepareNextIteration();
        superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
//
        return this;
    }

    private void prepareNextIteration() {

        switch (prop.getNonRunModes(stageID).getEndPointParam()) {
            case PermeateClean:
                stop();
                superCallback.callback(ModeEvent.PROMT_USER, ModeMsg.OPEN_RETENTATE_LINE);
                break;
            case RetentateClean:
//                superCallback.callback(ModeEvent.PROMT_USER, ModeMsg.CLAMP_PERMEATE_LINE);
                resume();
                break;
        }
    }

    @Override
    public iModeManager resume() {
        RampManager.get().start(prop.getFlowProp(), rampListener,prop);
        return this;
    }

    @Override
    public iModeManager pause() {
        if (prop.getPermPump() != null) {
            sLog.d(CleaningModeManager.this, "Permeate Stop");
            ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0, prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
        }
        FlowManager.get().stop();
        flowMeterEndPointListener.stopRxListening();
        endPointListener.stopRxListening();
        return this;
    }

    @Override
    public iModeManager stop() {
        pause();
        return this;
    }
}
