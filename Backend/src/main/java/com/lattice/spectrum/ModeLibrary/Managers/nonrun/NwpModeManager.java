/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 5/7/19 10:04 AM
 * Modified : 5/7/19 10:04 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.nonrun;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.RampManager;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.FlowProp;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class NwpModeManager extends iModeManager {
    /**
     *
     */
    private static NwpModeManager inst;


    public static NwpModeManager get() {
        if (inst == null) {
            inst = new NwpModeManager();
        }
        return inst;
    }

    /**
     *
     */
    private NwpModeManager() {
    }

    /**
     *
     */
    private FlowProp flowProp;


    private ModeListener rampListener = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            superCallback.callback(id, obj);
            switch (id) {
                case RAMP_UP_DONE:
                    pressureObserver.startRxListening();
                    FlowManager.get().start(flowProp, prop.getPermAuxPump(),prop.getTotalizerMode(), superCallback);
                    if (prop.getPermPump() != null) {
                        sLog.d(NwpModeManager.this, "Using Permeate Pump");
                        ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), prop.getPermAuxPump().getFlowRate(), prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
                    }
                    break;
            }
        }

    };

    private final RxListener pressureObserver = new RxListener(ComLib.get().getPressureInfo()) {

        private int secInt;
        private int counter;
        private int secTick;
        private boolean firstFlowCheck;

        @Override
        public void reset() {
            counter = 0;
            secTick = 0;
            secInt = 10;
        }

        private double getCp() {
            switch (prop.getNonRunModes(stageID).getPressureTarget()) {
                case PERMEATE:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.PERMEATE_CHANNEL);
                case RETENTATE:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.RETENTATE_CHANNEL);
                case TMP:
                    return ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.TMP_CHANNEL);
                default:
                    return Double.MAX_VALUE;
            }
        }

        @Override
        public void OnReceive() {
            double cp = getCp();
            double tp = prop.getNonRunModes(stageID).getOperatingPressure();
            boolean pressureInRange = (Math.abs(cp - tp) < 0.3);
            if (counter > 0) {
                if (pressureInRange) {
                    counter++;
                    if (counter >= prop.getNonRunModes(stageID).getTargetDuration()) {
                        sLog.d(NwpModeManager.this, "Completed TP:" + tp + " CP:" + cp);
                        stageID++;
                        if (stageID >= prop.getNonRunModeCount()) {
//                            stop();
                            superCallback.callback(ModeEvent.SUB_MODE_FINISHED, (stageID-1));
                            superCallback.callback(ModeEvent.ALL_MODE_FINISHED, prop.getSuperID());
                        } else {
                            prepareNextIteration();
                            superCallback.callback(ModeEvent.SUB_MODE_FINISHED, (stageID-1));
                            superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
                        }
                    }
                } else {
                    sLog.d(NwpModeManager.this, "Reset Count TP:" + tp + " CP:" + cp);
                    counter = 0;
                }
                //superCallback.callback(ModeEvent.MODE_COUNTER, counter);
            } else {
                if (pressureInRange) {
                    counter = 1;
                    sLog.d(NwpModeManager.this, "Starting Count TP:" + tp + " CP:" + cp);
                }
            }
            controlPressureUsingFlow(tp, cp);
        }

        private void controlPressureUsingFlow(double tp, double cp) {
            secTick++;
            if (secTick >= secInt) {
                secTick = 0;
                double fl;
                // check if CP in range then don't change the flowrate
                if ((Math.abs(cp - tp) < 0.3)) {
                    secInt = 20;
                    fl = flowProp.getMainPumpFlowRate();
                } else if (cp > tp) {
                    fl = flowProp.getMainPumpFlowRate()*(1-(prop.getRecirculationProp().getRate()/600));
//                    flowProp.setMainPumpFlowRate(flowProp.getMainPumpFlowRate() - 1);
                } else {
                    if (firstFlowCheck) {
                        fl = flowProp.getMainPumpFlowRate() *(1+(prop.getRecirculationProp().getRate()/600));
//                      flowProp.setMainPumpFlowRate(flowProp.getMainPumpFlowRate() + 1);
                    } else {
                        firstFlowCheck = true;
                        fl = flowProp.getMainPumpFlowRate() / 2;
                    }
                }
                if (fl < 1) {
                    fl = 1;
                }
                flowProp.setMainPumpFlowRate(fl);
            }
        }
    };

    private void prepareNextIteration() {
        pressureObserver.reset();
    }

    @Override
    public iModeManager setup() {
//        valve length always going to be 1
//        pinch off valve
        if(prop.getRecirculationProp()==null){
            throw new RuntimeException("Recirculation prop require");
        }
        flowProp = prop.getFlowProp();
//
        stageID = 0;
        prepareNextIteration();
//
        ComLib.get().getValveInfo().setValvePosition(0, (int) prop.getValveProps()[0].getClosePosition(),0);
        superCallback.callback(ModeEvent.NWP_VALVE_DONE);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RampManager.get().start(flowProp, rampListener,prop);
        }).start();
        superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID);
        return this;
    }

    @Override
    public iModeManager pause() {
        pressureObserver.stopRxListening();
        FlowManager.get().stop();
        return this;
    }

    @Override
    public iModeManager resume() {
        ComLib.get().getValveInfo().setValvePosition(0, (int) prop.getValveProps()[0].getClosePosition(),0);
        superCallback.callback(ModeEvent.NWP_VALVE_DONE);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RampManager.get().start(flowProp, rampListener,prop);
        }).start();
        return this;
    }

    @Override
    public iModeManager stop() {
        FlowManager.get().stop();
        pressureObserver.stopRxListening();
//
        return this;
    }
}
