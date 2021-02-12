/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 5/7/19 9:42 AM
 * Modified : 4/7/19 2:08 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.run;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.*;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.subModes.*;
import com.lattice.spectrum.ModeLibrary.ModeProp.RunSubModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

import static com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent.*;

public class RunModeManager extends iModeManager {

    /**
     *
     */
    private static RunModeManager runModeManager;

    /**
     * @return singleton instance of run mode manager
     */
    public static RunModeManager get() {
        if (runModeManager == null) {
            runModeManager = new RunModeManager();
        }
        return runModeManager;
    }

    private RunModeManager() {
    }

    private boolean callbackAllowed = false;
    private final Integer curModeLock = 0;

    private final ModeListener runModeManagerCallback = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            synchronized (curModeLock) {
                superCallback.callback(id, obj);
                switch (id) {
                    case RAMP_UP_DONE:
                        try {
                            curModeManager.resume();
                        } catch (Exception e) {
                            e.printStackTrace();
                            superCallback.callback(ModeEvent.EXCEPTION, e);
                        }
                        FlowManager.get().start(prop.getFlowProp(), prop.getPermAuxPump(), prop.getTotalizerMode(), superCallback);
                        if (prop.getSuperID() != OperationMode.TubeCalib_MODE) {
                            AlarmManager.get().start(prop.getAlarmProp(), prop.getKonduit(KonduitChannel.CHANNEL1), prop.getKonduit(KonduitChannel.CHANNEL2), runModeManagerCallback);
                        }
                        PressureManager.get().start(prop.getValveProps(), prop.getRecirculationProp(), prop.getFlowProp(), superCallback, prop.isSecondValveInUse());
                        break;
                    case SUB_MODE_FINISHED:
                        stageID++;
                        if (stageID < prop.getSubModeCount()) {
                            sLog.d(RunModeManager.this, "Starting Next Stage");
                            RunSubModeProp tempProp = prop.getSubRunModeProp(stageID);
                            if (prop.getSubRunModeProp(stageID - 1).getMode() == OperationMode.D_MODE && tempProp.getMode() == OperationMode.D_MODE) {
//                            run dia2 pump at dia1 flow rate
                                ComLib.get().getAuxPump().setAuxPumpRPM(tempProp.getAuxPumpID(), prop.getAuxProp(tempProp.getAuxPumpID()).getTypeID(),
                                        (int) ufx.boundInRange(
                                                ((DMode) curModeManager).getAuxPumpFlowRate() * 100 / prop.getAuxProp(tempProp.getAuxPumpID()).getTubeFlowConversion(),
                                                0, prop.getAuxProp(tempProp.getAuxPumpID()).getMaxRPM()));
                            }
                            try {
                                if (ModeCalculator.get().endPointInvalid()) {
                                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.ENDPOINT_IS_NOT_FEASIBLE);
//                                return;
                                }
                                curModeManager = getMode(tempProp.getMode())
                                        .setSuperCallback(runModeManagerCallback)
                                        .setProp(prop)
                                        .setStage(stageID)
                                        .setup()
                                        .resume();
                                superCallback.callback(ModeEvent.SUB_MODE_STARTED, tempProp.getMode());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case DONE_FINAL_COLLECTION:
                        ModeCalculator.get().stop();
                        if (callbackAllowed)
                            superCallback.callback(ModeEvent.SUPER_MODE_FINISHED, prop.getSuperID());
                        superCallback.callback(ModeEvent.STOP);
                        break;
                    case ENDPOINT_REACHED:
                    case TC_ENDPOINT_REACHED:
                    case HARD_FAULT:
                        if (stageID == (prop.getSubModeCount() - 1)) {
                            if (id == ENDPOINT_REACHED || id == TC_ENDPOINT_REACHED) callbackAllowed = true;
                        }
                        if (stageID >= prop.getSubModeCount() - 1 || id == HARD_FAULT /*|| id == EXCEPTION*/) {
                            if(id== ENDPOINT_REACHED || id ==HARD_FAULT){
                                stopWithDelay();
                                Endmode.get()
                                        .setSuperCallback(runModeManagerCallback)
                                        .setup()
                                        .resume();
                            }else {
                                stop();
                            }
                        } else {
                            curModeManager.stop();
                        }
                        break;
                }
            }
        }
    };

    @Override
    public iModeManager setup() {
        callbackAllowed = false;
        if (prop != null) {
//
            if (prop.getTotalizerMode() != TotalizerMode.ENDPOINT) {
                if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)) {
                    this.superCallback.callback(ModeEvent.PERMEATE_SCALE_TARE_STARTED);
                    ComLib.get().getScaleTare().start(false, true);
                    double timeout = ufx.time() + 10;
                    while (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (timeout <= ufx.time()) {
                            this.superCallback.callback(ModeEvent.PERMEATE_SCALE_TARE_TIMEOUT);
                            return null;
                        }
                    }
                    this.superCallback.callback(ModeEvent.PERMEATE_SCALE_TARE_COMPLETED);
                } else {
                    this.superCallback.callback(ModeEvent.EXCEPTION, ModeException.PERMEATE_SCALE_MISSING);
                    return null;
                }
            }
            if (prop.getTotalizerMode() != TotalizerMode.NONE) {
                FlowMeterManager.get().start(prop);
            }

            if (prop.getFeedStartWt() == null) {
                prop.setFeedStartWt(ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE));
                prop.setFeedConnected(true);
            } else {
                prop.setFeedOffset(prop.getFeedStartWt() - ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE));
                if (prop.getSuperID() != OperationMode.C_MODE) {
                    prop.setFeedConnected(true);
                } else {
                    prop.setFeedConnected(false);
                }
            }


            ModeCalculator.get().setSuperCallback(runModeManagerCallback).setProp(prop).setStage(0).setup().resume();

            if (ModeCalculator.get().endPointInvalid()) {
                this.superCallback.callback(ModeEvent.EXCEPTION, ModeException.ENDPOINT_IS_NOT_FEASIBLE);
//                return null;
            }
//
            stageID = 0;
            curModeManager = getMode(prop.getSubRunModeProp(stageID).getMode())
                    .setSuperCallback(runModeManagerCallback)
                    .setProp(prop)
                    .setStage(stageID)
                    .setup();
            superCallback.callback(ModeEvent.SUB_MODE_STARTED, prop.getSubRunModeProp(stageID).getMode());
            RampManager.get().start(prop.getFlowProp(), runModeManagerCallback,prop);
//            FlowManager.get().init(prop, superCallback);
        } else {
            if (this.superCallback != null) {
                this.superCallback.callback(ModeEvent.PROP_NOT_SET);
            } else {
                throw new NullPointerException("Mode Listener not set");
            }
        }
        return this;
    }

    private iModeManager getMode(OperationMode value) {
        switch (value) {
            case TubeCalib_MODE:
                return TCmode.get();
            case C_MODE:
                return CMode.get();
            case D_MODE:
                return DMode.get();
            case CF_MODE:
                return CFMode.get();
            case VACUUM_MODE:
                return VMode.get();
            case HARVEST_MODE:
                return TFDFMode.get();
            default:
                throw new IllegalArgumentException("Invalid Mode Type");
        }
    }

    @Override
    public iModeManager pause() {
        onPause();
        return this;
    }

    @Override
    public iModeManager resume() {
//        rest managers will get started after rampup
//        FlowManager.get().init(prop, superCallback);
        callbackAllowed = false;
        RampManager.get().start(prop.getFlowProp(), runModeManagerCallback,prop);
        ModeCalculator.get().resume();
//        curModeManager.resume();
        this.superCallback.callback(ModeEvent.RESUME);
        return this;
    }

    @Override
    public iModeManager stop() {
        synchronized (curModeLock) {
//
            stageID = prop.getSubModeCount();
            for (int i = 0; i < prop.getValveCount(); i++) {
                if (prop.getValveProp(i).getTarget() == PressureTarget.PERMEATE) {
                    ComLib.get().getValveInfo().setValvePosition(i, (int) prop.getValveProp(i).getClosePosition(), 0);
                } else {
                    ComLib.get().getValveInfo().setValvePosition(i, (int) prop.getValveProp(i).getOpenPosition(), 0);
                }
            }
//
            curModeManager.stop();
            ModeCalculator.get().stop();
            if (prop.getSuperID() != OperationMode.TubeCalib_MODE) {
                AlarmManager.get().stop();
            }
            PressureManager.get().stop();
            FlowManager.get().stop();
            if (prop.getTotalizerMode() != TotalizerMode.NONE) {
                FlowMeterManager.get().stop();
            }
            if (prop.getPermPump() != null) {
//            prop.getPermAuxPump().setFlowRate(0);
                ComLib.get().getAuxPump().setAuxPumpRPM(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0);
            }
            if (prop.getPermPump() != null && prop.getPermPump().shouldStopFirst()) {
                new RxListener(ComLib.get().getAuxPump()) {
                    @Override
                    public void OnReceive() {
                        if (ComLib.get().getAuxPump().curAuxRPM(prop.getPermPump().getPumpID()) <= 3) {
                            sLog.d(RunModeManager.this, "Permeate Pump Stopped");
                            stopRxListening();
                            ComLib.get().getMainPump().setSpeed(0);
                            if (callbackAllowed)
                                RunModeManager.this.superCallback.callback(ModeEvent.SUPER_MODE_FINISHED, prop.getSuperID());
                        }
                    }
                }.startRxListening();
                sLog.d(this, "Stopping Permeate Pump");
            } else {
                ComLib.get().getMainPump().setSpeed(0);
                if (callbackAllowed)
                    this.superCallback.callback(ModeEvent.SUPER_MODE_FINISHED, prop.getSuperID());
            }
            this.superCallback.callback(ModeEvent.STOP);
        }
        return this;
    }

    public void stopWithDelay() {
        synchronized (curModeLock) {
//
            stageID = prop.getSubModeCount();
            for (int i = 0; i < prop.getValveCount(); i++) {
                if (prop.getValveProp(i).getTarget() == PressureTarget.PERMEATE) {
                    ComLib.get().getValveInfo().setValvePosition(i, (int) prop.getValveProp(i).getClosePosition(), 0);
                } else {
                    ComLib.get().getValveInfo().setValvePosition(i, (int) prop.getValveProp(i).getOpenPosition(), 0);
                }
            }
//
            curModeManager.stop();
            if (prop.getSuperID() != OperationMode.TubeCalib_MODE) {
                AlarmManager.get().stop();
            }
            PressureManager.get().stop();
            FlowManager.get().stop();
            if (prop.getTotalizerMode() != TotalizerMode.NONE) {
                FlowMeterManager.get().stop();
            }
            if (prop.getPermPump() != null) {
//            prop.getPermAuxPump().setFlowRate(0);
                ComLib.get().getAuxPump().setAuxPumpRPM(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0);
            }
            if (prop.getPermPump() != null && prop.getPermPump().shouldStopFirst()) {
                new RxListener(ComLib.get().getAuxPump()) {
                    @Override
                    public void OnReceive() {
                        if (ComLib.get().getAuxPump().curAuxRPM(prop.getPermPump().getPumpID()) <= 3) {
                            sLog.d(RunModeManager.this, "Permeate Pump Stopped");
                            stopRxListening();
                            ComLib.get().getMainPump().setSpeed(0);
                        }
                    }
                }.startRxListening();
                sLog.d(this, "Stopping Permeate Pump");
            } else {
                ComLib.get().getMainPump().setSpeed(0);
            }
        }
    }

    public void changeVacuumModeTarget() {
        ModeCalculator.get().updateModStart();
        if (stageID >= prop.getSubModeCount() - 1) {
            stop();
        } else {
            curModeManager.stop();
        }
    }

    public void onPause() {
        synchronized (curModeLock) {
            int speed = ComLib.get().getMainPump().getMotorSpeed();
            FlowManager.get().stop();
            PressureManager.get().stop();
            if (prop.getSuperID() != OperationMode.TubeCalib_MODE) {
                AlarmManager.get().stop();
            }
            if (prop.getPermPump() != null) {
                ComLib.get().getAuxPump().setAuxPumpRPM(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0);
            }
            ComLib.get().getMainPump().setSpeed(speed);
            curModeManager.pause();
            this.superCallback.callback(PAUSE);
        }
        ComLib.get().getValveInfo().setValvePosition(0, 1, 0);
        ComLib.get().getValveInfo().setValvePosition(1, 1, 0);
    }

    public void onPausePressure() {
        ComLib.get().getValveInfo().setValvePosition(0, 1, 0);
        ComLib.get().getValveInfo().setValvePosition(1, 1, 0);
        PressureManager.get().stop();
    }

    public void onResumePressure() {
        PressureManager.get().start(prop.getValveProps(), prop.getRecirculationProp(), prop.getFlowProp(), superCallback, prop.isSecondValveInUse());
    }

    public void onDisconnect() {
        synchronized (curModeLock) {
            int speed = ComLib.get().getMainPump().getMotorSpeed();
            FlowManager.get().stop();
            PressureManager.get().stop();
            if (prop.getSuperID() != OperationMode.TubeCalib_MODE) {
                AlarmManager.get().stop();
            }
            if (prop.getPermPump() != null) {
                ComLib.get().getAuxPump().setAuxPumpRPM(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), 0);
            }
            ComLib.get().getMainPump().setSpeed(speed);
            curModeManager.pause();
            this.superCallback.callback(ModeEvent.DISCONNECT_PAUSE);
        }
        ComLib.get().getValveInfo().setValvePosition(0, 1, 0);
        ComLib.get().getValveInfo().setValvePosition(1, 1, 0);
    }
}
