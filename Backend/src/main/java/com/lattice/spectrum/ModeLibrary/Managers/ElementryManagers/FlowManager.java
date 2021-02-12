/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 11:21 AM
 * Modified : 9/7/19 11:21 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.AuxProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.FlowProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

/**
 * main pump and aux pump flow rate manager
 */
public class FlowManager {
    /**
     *
     */
    private static FlowManager flowManager;

    private FlowManager() {
    }

    public static FlowManager get() {
        if (flowManager == null) {
            flowManager = new FlowManager();
        }
        return flowManager;
    }

    /**
     *
     */
    private ModeListener superCallback;
    private FlowProp prop;
    private AuxProp permPump;
    private double lastPermeateWt;
    private TotalizerMode totalizerMode;

    private final RxListener flowControlListener = new RxListener(ComLib.get().getScaleInfo()) {

        @Override
        public void OnReceive() {
            switch (prop.getFlowControl()) {
                case FEED:
                    sLog.d(FlowManager.this, "Feed Flow:" + (prop.getMainPumpFlowRate() * prop.getSlowForValveFactor()));
                    ComLib.get().getMainPump().setFlow_mlpm(prop.getMainPumpFlowRate() * prop.getSlowForValveFactor(), prop.getMainPumpTubeFlowConversion(), prop.getMainPumpSpeedFactor());
                    break;
                case RETENTATE:
                    if(totalizerMode==TotalizerMode.ENDPOINT) {
                        double curPermeateWt = FlowMeterManager.get().getVolume(0);
                        if (curPermeateWt > lastPermeateWt || lastPermeateWt < 0) {
                            sLog.d(FlowManager.this, "Retentate Flow using flowmeter:" + ((prop.getMainPumpFlowRate() + FlowMeterManager.get().getCurrentFlowRate(0) * 60.0 * prop.getFlowFactor()) * prop.getSlowForValveFactor()));
                            ComLib.get().getMainPump().setFlow_mlpm((prop.getMainPumpFlowRate() + FlowMeterManager.get().getCurrentFlowRate(0) * 60.0 * prop.getFlowFactor()) * prop.getSlowForValveFactor(), prop.getMainPumpTubeFlowConversion(), prop.getMainPumpSpeedFactor());
                            lastPermeateWt = curPermeateWt;
                        }
                    }else {
                        double curFlowRate = ComLib.get().getScaleInfo().scaleFlowRate_mlpm(ScaleID.PERMEATE_SCALE);
                        if (curFlowRate > 0) {
                            sLog.d(FlowManager.this, "Retentate Flow:" + ((prop.getMainPumpFlowRate() + curFlowRate * prop.getFlowFactor()) * prop.getSlowForValveFactor()));
                            ComLib.get().getMainPump().setFlow_mlpm((prop.getMainPumpFlowRate() + curFlowRate * prop.getFlowFactor()) * prop.getSlowForValveFactor(), prop.getMainPumpTubeFlowConversion(), prop.getMainPumpSpeedFactor());
                        } else {
                            sLog.d(FlowManager.this, "Retentate Flow:" + ((prop.getMainPumpFlowRate()) * prop.getSlowForValveFactor()));
                            ComLib.get().getMainPump().setFlow_mlpm((prop.getMainPumpFlowRate()) * prop.getSlowForValveFactor(), prop.getMainPumpTubeFlowConversion(), prop.getMainPumpSpeedFactor());
                        }
                    }
                    break;
            }
            if (permPump != null) {
                ComLib.get().getAuxPump().setAuxPump_mlpm(permPump.getPumpID(), permPump.getTypeID(), permPump.getFlowRate(), permPump.getTubeFlowConversion(), permPump.getMaxRPM());
            }
        }
    };

    public void start(FlowProp prop, AuxProp permPump, TotalizerMode totalizerMode, ModeListener callback) {
        this.superCallback = callback;
        this.prop = prop;
        this.permPump = permPump;
        this.totalizerMode = totalizerMode;
//
        lastPermeateWt = -1;
        flowControlListener.startRxListening();
        if (permPump != null) {
            sLog.d(FlowManager.this, "Using Permeate Pump");
            ComLib.get().getAuxPump().setAuxPump_mlpm(permPump.getPumpID(), permPump.getTypeID(), permPump.getFlowRate(), permPump.getTubeFlowConversion(), permPump.getMaxRPM());
        }
    }

    public void stop() {
        ComLib.get().getMainPump().setSpeed(0);
        flowControlListener.stopRxListening();
        if (permPump != null) {
            ComLib.get().getAuxPump().setAuxPump_mlpm(permPump.getPumpID(), permPump.getTypeID(), 0, permPump.getTubeFlowConversion(), permPump.getMaxRPM());
        }
    }
}
