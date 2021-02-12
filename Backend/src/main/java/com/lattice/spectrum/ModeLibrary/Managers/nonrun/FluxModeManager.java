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
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.*;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.FlowProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ValveProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

public class FluxModeManager extends iModeManager {
    /**
     *
     */
    private static FluxModeManager inst;

    public static FluxModeManager get() {
        if (inst == null) {
            inst = new FluxModeManager();
        }
        return inst;
    }

    private FluxModeManager() {
    }

    /**
     *
     */
    private FlowProp flowProp;
    private ValveProp[] valveProp;
    private int subStageID;
    //
    private boolean calculateHoldup;
    private double lastFeedWt;
    //
    private double permTrueWt;
    private double permHoldupWt;
    private double feedHoldupWt;
    private double feedTrueWt;
    private double totalDiaWt;
    private double permTrueStartWt;
    private double feedTrueStartWt;


    private ModeListener rampListener = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            superCallback.callback(id, obj);
            switch (id) {
                case RAMP_UP_DONE:
                    FlowManager.get().start(flowProp, prop.getPermAuxPump(),prop.getTotalizerMode(), superCallback);
                    FluxPressureManager.get().start(valveProp, prop.getRecirculationProp(), flowProp, superCallback, prop.isSecondValveInUse());
                    pressureObserver.startRxListening();
                    if (prop.getSuperID() == OperationMode.FluxCV) {
                        FeedWtManger.get().start(
                                prop.getAuxProp(prop.getSubNonRunModeProp(stageID).getAuxPumpID()),
                                superCallback,
                                ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE) - (stageID == 0 ? prop.getFeedHoldUpForStage1() : 0),
                                prop.getSubNonRunModeProp(stageID).getAuxPumpID(),
                                prop.getTotalizerMode()
                        );
                    }
                    if (prop.getPermPump() != null) {
                        sLog.d(FluxModeManager.this, "Using Permeate Pump");
                        ComLib.get().getAuxPump().setAuxPump_mlpm(prop.getPermPump().getPumpID(), prop.getPermAuxPump().getTypeID(), prop.getPermAuxPump().getFlowRate(), prop.getPermAuxPump().getTubeFlowConversion(), prop.getPermAuxPump().getMaxRPM());
                    }
                    superCallback.callback(ModeEvent.MODE_COUNTER,stageID);
                    if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
                        calculator_rxl_flow_meter.startRxListening();
                    }else {
                        calculator_rxl.startRxListening();
                    }
                    break;
            }
        }

    };

    private final RxListener pressureObserver = new RxListener(ComLib.get().getPressureInfo()) {

        private int counter;

        @Override
        public void reset() {
            counter = 0;
        }

        private double getCp() {
            switch (valveProp[0].getTarget()) {
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
            double tp = valveProp[0].getOperatingPressure();
            boolean pressureInRange = Math.abs(cp - tp) < 0.3;
            if (counter > 0) {
                if (pressureInRange) {
                    counter++;
                    if (counter >= prop.getNonRunModes(subStageID).getTargetDuration()) {
                        sLog.d("Completed TP:" + tp + " CP:" + cp);
                        if (subStageID >= prop.getNonRunModeCount()-1) {
                            if (stageID >= prop.getNonRunModeCount()-1) {
//                                stop();
                                superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID, subStageID);
                                superCallback.callback(ModeEvent.ALL_MODE_FINISHED, prop.getSuperID());
                            } else {
                                superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID, subStageID);
                                stageID++;
                                subStageID = 0;
                                superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID, subStageID);
                                superCallback.callback(ModeEvent.MODE_COUNTER,stageID);
                                prepareNextIteration();
                                FluxPressureManager.get().reset();
                            }
                        } else {
                            superCallback.callback(ModeEvent.SUB_MODE_FINISHED, stageID, subStageID);
                            subStageID++;
                            superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID, subStageID);
                            prepareNextIteration();
                        }
                    }
                } else {
                    sLog.d("Reset Count TP:" + tp + " CP:" + cp);
                    counter = 0;
                }
                //superCallback.callback(ModeEvent.MODE_COUNTER, counter);
            } else {
                if (pressureInRange) {
                    counter = 1;
                    sLog.d("Starting Count TP:" + tp + " CP:" + cp);
                }
            }
        }

    };


    private final RxListener calculator_rxl = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)) {
                permTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
            } else {
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.PERMEATE_SCALE_MISSING);
            }

            if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE) && prop.isFeedConnected()) {
                feedTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE) + prop.getFeedOffset();
            } else {
                feedTrueWt = feedTrueStartWt - permTrueWt - permHoldupWt - feedHoldupWt - totalDiaWt;
            }

            lastFeedWt = feedTrueWt;

            prop.setCF((permTrueWt + permHoldupWt + feedTrueWt + feedHoldupWt - totalDiaWt) / (feedTrueWt + feedHoldupWt));
            sLog.d(this, FluxModeManager.this.toString());
            sLog.d(this, "CF = " + prop.getCF() + " DV = " + prop.getDV());
        }
    };


    private final RxListener calculator_rxl_flow_meter = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            if (ComLib.get().getKonduitInfo().isKFConduitConnected()) {
                permTrueWt = FlowMeterManager.get().getVolume(0);
            } else {
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.KF_KONDUIT_MISSING);
            }

            if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE)) {
                feedTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE) + prop.getFeedOffset();
            } else {
                feedTrueWt = feedTrueStartWt - permTrueWt - permHoldupWt - feedHoldupWt - totalDiaWt;
            }

            lastFeedWt = feedTrueWt;

            double checkForZero = (feedTrueWt + feedHoldupWt);
            if(checkForZero!=0) {
                prop.setCF((permTrueWt + permHoldupWt + feedTrueWt + feedHoldupWt - totalDiaWt) / (feedTrueWt + feedHoldupWt));
            }
            sLog.d(this, FluxModeManager.this.toString());
            sLog.d(this, "CF = " + prop.getCF() + " DV = " + prop.getDV());
        }
    };

    private void prepareNextIteration() {
        pressureObserver.reset();
        flowProp.setMainPumpFlowRate(prop.getNonRunModes(stageID).getMainFlowRate());
        valveProp[0].setTarget(prop.getNonRunModes(subStageID).getPressureTarget());
        valveProp[0].setOperatingPressure(prop.getNonRunModes(subStageID).getOperatingPressure());
    }

    @Override
    public iModeManager setup() {
//        valve length always going to be 1
        valveProp = prop.getValveProps();
//
        flowProp = prop.getFlowProp();
//
        stageID = 0;
        subStageID = 0;
        prepareNextIteration();
//
        RampManager.get().start(prop.getFlowProp(), rampListener,prop);
        calculateHoldup = true;
        permTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
        permTrueStartWt = permTrueWt;
        permHoldupWt = prop.getPermHoldUp();
        feedHoldupWt = prop.getFeedHoldUp();
        feedTrueWt = prop.getFeedStartWt();
        feedTrueStartWt = feedTrueWt;
        lastFeedWt = feedTrueWt;
//
        totalDiaWt = 0;
        superCallback.callback(ModeEvent.SUB_MODE_STARTED, stageID, subStageID);
        return this;
    }

    @Override
    public iModeManager pause() {
        FlowManager.get().stop();
        FluxPressureManager.get().stop();
        pressureObserver.stopRxListening();
        if (prop.getSuperID() == OperationMode.FluxCV) {
            FeedWtManger.get().stop();
        }
        calculator_rxl.stopRxListening();
        calculator_rxl_flow_meter.stopRxListening();
        return this;
    }

    @Override
    public iModeManager resume() {
        RampManager.get().start(flowProp, rampListener,prop);
        return this;
    }


    @Override
    public String toString() {
        return "FluxCalculator {" +
                "\n\tstageID=" + stageID +
                ",\n\tcalculateHoldup=" + calculateHoldup +
                ",\n\tpermTrueStartWt=" + permTrueStartWt +
                ",\n\tfeedTrueStartWt=" + feedTrueStartWt +
                ",\n\tlastFeedWt=" + lastFeedWt +
                ",\n\tpermTrueWt=" + permTrueWt +
                ",\n\tpermHoldupWt=" + permHoldupWt +
                ",\n\tfeedTrueWt=" + feedTrueWt +
                ",\n\tfeedHoldupWt=" + feedHoldupWt +
                ",\n\ttotalDiaWt=" + totalDiaWt +
                "\n}";
    }

    @Override
    public iModeManager stop() {
        FlowManager.get().stop();
        FluxPressureManager.get().stop();
        pressureObserver.stopRxListening();
        if (prop.getSuperID() == OperationMode.FluxCV) {
            FeedWtManger.get().stop();
        }
        calculator_rxl.stopRxListening();
        calculator_rxl_flow_meter.stopRxListening();
        return this;
    }

    public double getFeedTrueWt() {
        return feedTrueWt;
    }

}
