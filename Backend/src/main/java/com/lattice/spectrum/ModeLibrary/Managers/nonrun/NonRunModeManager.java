/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 5/7/19 9:42 AM
 * Modified : 4/7/19 2:08 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.nonrun;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.*;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.Managers.run.subModes.Endmode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

/**
 * just a dummy class to maintain symmetry between auto and non auto mode
 */
public class NonRunModeManager extends iModeManager {

    /**
     *
     */
    private static NonRunModeManager nonRunModeManager;

    /**
     * @return singleton instance of run mode manager
     */
    public static NonRunModeManager get() {
        if (nonRunModeManager == null) {
            nonRunModeManager = new NonRunModeManager();
        }
        return nonRunModeManager;
    }

    private NonRunModeManager() {
    }

    private final ModeListener nonRunModeManagerCallback = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            superCallback.callback(id, obj);
            switch (id) {
                case RAMP_UP_DONE:
                    AlarmManager.get().start(prop.getAlarmProp(), prop.getKonduit(KonduitChannel.CHANNEL1), prop.getKonduit(KonduitChannel.CHANNEL2), nonRunModeManagerCallback);
                    break;
                case HARD_FAULT:
                case ALL_MODE_FINISHED:
                    stopWithDelay();
                    Endmode.get()
                            .setSuperCallback(nonRunModeManagerCallback)
                            .setup()
                            .resume();
                    break;
                case DONE_FINAL_COLLECTION:
                    superCallback.callback(ModeEvent.SUPER_MODE_FINISHED,prop.getSuperID());
                    superCallback.callback(ModeEvent.STOP, prop.getSuperID());
            }
        }
    };

    @Override
    public iModeManager setup() {
        if (prop != null) {
            if(prop.getTotalizerMode()!= TotalizerMode.ENDPOINT) {
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


            if (prop.getFeedStartWt() == null) {
                prop.setFeedStartWt(ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE));
                prop.setFeedConnected(true);
            } else {
                prop.setFeedOffset(prop.getFeedStartWt() - ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE));
                if (prop.getSuperID() == OperationMode.FluxCV) {
                    prop.setFeedConnected(true);
                } else {
                    prop.setFeedConnected(false);
                }
            }
            if(prop.getTotalizerMode()!=TotalizerMode.NONE){
                FlowMeterManager.get().start(prop);
            }
            if (prop.getFeedStartWt() == null) {
                prop.setFeedStartWt(ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE));
            }
            stageID = 0;
            curModeManager = getMode(prop.getSuperID())
                    .setSuperCallback(nonRunModeManagerCallback)
                    .setProp(prop)
                    .setStage(stageID)
                    .setup();
            //superCallback.callback(ModeEvent.SUB_MODE_STARTED, 0);
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
            case FluxCF:
            case FluxCV:
                return FluxModeManager.get();
            case NWP:
                return NwpModeManager.get();
            case Cleaning:
                return CleaningModeManager.get();
            case Flushing:
                return FlushingModeManager.get();
            default:
                throw new IllegalArgumentException("Invalid Mode Type");
        }
    }

    @Override
    public iModeManager pause() {
        AlarmManager.get().stop();
        if(prop.getSuperID() == OperationMode.Cleaning){
            curModeManager.pause();
            this.superCallback.callback(ModeEvent.PAUSE);
        }else{
            onPause();
        }
        return this;
    }

    @Override
    public iModeManager resume() {
        curModeManager.resume();
        this.superCallback.callback(ModeEvent.RESUME);
        return this;
    }

    public void onPause(){
        int speed = ComLib.get().getMainPump().getMotorSpeed();
        AlarmManager.get().stop();
        curModeManager.pause();
        PressureManager.get().stop();
        ComLib.get().getMainPump().setSpeed(speed);
        ComLib.get().getValveInfo().setValvePosition(0, 1,0);
        ComLib.get().getValveInfo().setValvePosition(1, 1,0);
        this.superCallback.callback(ModeEvent.PAUSE);
    }

    @Override
    public iModeManager stop() {
        ComLib.get().getValveInfo().setValvePosition(0, 0,0);
        AlarmManager.get().stop();
        curModeManager.stop();
        superCallback.callback(ModeEvent.STOP, prop.getSuperID());
        if(prop.getTotalizerMode()!=TotalizerMode.NONE){
            FlowMeterManager.get().stop();
        }
        return this;
    }

    public void stopWithDelay() {
        ComLib.get().getValveInfo().setValvePosition(0, 0,0);
        AlarmManager.get().stop();
        curModeManager.stop();
        if(prop.getTotalizerMode()!=TotalizerMode.NONE){
            FlowMeterManager.get().stop();
        }
    }


    public void onPausePressure() {
        ComLib.get().getValveInfo().setValvePosition(0, 1,0);
        ComLib.get().getValveInfo().setValvePosition(1, 1,0);
        PressureManager.get().stop();
    }

    public void onResumePressure() {
        switch (prop.getSuperID()) {
            case FluxCF:
            case FluxCV:
            case Flushing:
                PressureManager.get().start(prop.getValveProps(), prop.getRecirculationProp(), prop.getFlowProp(), superCallback, prop.isSecondValveInUse());
                break;
            case NWP:
                ComLib.get().getValveInfo().setValvePosition(0, (int) prop.getValveProps()[0].getClosePosition(),0);
                break;
        }

    }

    public void onDisconnect(){
        int speed = ComLib.get().getMainPump().getMotorSpeed();
        AlarmManager.get().stop();
        curModeManager.pause();
        PressureManager.get().stop();
        ComLib.get().getMainPump().setSpeed(speed);
        ComLib.get().getValveInfo().setValvePosition(0, 1,0);
        ComLib.get().getValveInfo().setValvePosition(1, 1,0);
        this.superCallback.callback(ModeEvent.DISCONNECT_PAUSE);
    }
}
