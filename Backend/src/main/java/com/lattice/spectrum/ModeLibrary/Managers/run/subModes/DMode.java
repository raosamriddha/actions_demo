/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 18/7/19 2:20 PM
 * Modified : 18/7/19 2:17 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.run.subModes;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FeedWtManger;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowMeterManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeCalculator;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

import static com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget.Conductivity;
import static com.lattice.spectrum.ModeLibrary.ModeProp.type.EndPointTarget.UV;

public class DMode extends iModeManager {

    private static DMode singleInstance;// = new ModeCalculator();

    private DMode() {
    }

    public static DMode get() {
        if (singleInstance == null) {
            singleInstance = new DMode();
        }
        return singleInstance;
    }

    private double currentModePercent=0.0;

    /**
     *
     */
    private final RxListener scaleEndPointListener = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            sLog.d(DMode.this, prop.getDV() + "   " + (prop.getDV() >= subProp.getEndPointValue()));
            switch (subProp.getEndPointTarget()) {
                case PermeateWeight:
                    if (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) >= (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                        sLog.d(DMode.this, "Final permeate weight reached");
//                        updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE)+prop.getPermHoldUp()-ModeCalculator.get().getPermTrueStartWt())*100.0/(subProp.getEndPointValue()-ModeCalculator.get().getPermTrueStartWt());
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    break;
                case DiafiltrationVolume:
                    double dv = prop.getDV();//(ComLib.get().getScaleInfo().scaleReading_gm(PERMEATE_SCALE) + prop.getPermHoldUp() - prop.getPermModStartWt()) / ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
                    sLog.d(DMode.this, "DV = " + dv);
                    if (dv >= subProp.getEndPointValue()) {
                        sLog.d(DMode.this, "Final DV reached");
//                        updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = dv*100.0/subProp.getEndPointValue();
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    break;
            }
            double totalModePercent = (stageID*100.0/prop.getRunModes().length) + currentModePercent/prop.getRunModes().length;
            prop.setSubModePercentComplete(totalModePercent);
        }
    };
    /**
     *
     */
    private final RxListener uvListener = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double totalModePercent;
            switch (subProp.getEndPointTarget()) {
                case UV:
                    if (isEndPointReached(ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) - subProp.getEndPointValue())) {
                        sLog.d(DMode.this, "Final UV:" + subProp.getProbeID() + " reached");
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    totalModePercent = (stageID * 100.0 / prop.getRunModes().length) + konduitPercent / prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case Turbidity:
                case Protein:
                case pH:
                    double uvCalculated= ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID())*prop.getKonduit(KonduitChannel.CHANNEL1).getXValue()+prop.getKonduit(KonduitChannel.CHANNEL1).getYValue();
                    setKonduitValue(subProp.getProbeID(),uvCalculated,prop.getKonduit(subProp.getProbeID()).getType());
                    if (isEndPointReached(uvCalculated - subProp.getEndPointValue())) {
                        sLog.d(DMode.this, "Final "+subProp.getEndPointTarget()+" " + subProp.getProbeID() + " reached");
//                    updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + konduitPercent/prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case PermeateVolume:
                    if (FlowMeterManager.get().getVolume(0) >= (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                        sLog.d(DMode.this, "Final permeate volume reached");
//                        updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (FlowMeterManager.get().getVolume(0)+prop.getPermHoldUp()-ModeCalculator.get().getPermTrueStartWt())*100.0/(subProp.getEndPointValue()-ModeCalculator.get().getPermTrueStartWt());
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + currentModePercent/prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case DiafiltrationVolume:
                    double dv = prop.getDV();//(ComLib.get().getScaleInfo().scaleReading_gm(PERMEATE_SCALE) + prop.getPermHoldUp() - prop.getPermModStartWt()) / ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
                    sLog.d(DMode.this, "DV = " + dv);
                    if (dv >= subProp.getEndPointValue()) {
                        sLog.d(DMode.this, "Final DV reached");
//                        updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = dv*100.0/subProp.getEndPointValue();
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + currentModePercent/prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
            }

        }
    };
    /**
     *
     */
    private final RxListener conductivityListener = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            if (subProp.getEndPointTarget() == Conductivity) {
                if (isEndPointReached(ComLib.get().getKonduitInfo().getProbeConductivity_mS(subProp.getProbeID()) - subProp.getEndPointValue())) {
                    sLog.d(this, "Final Conductivity" + subProp.getProbeID() + " reached");
                    ModeCalculator.get().updateModStart();
                    stopRxListening();
                    superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                }
                double totalModePercent = (stageID*100.0/prop.getRunModes().length) + konduitPercent/prop.getRunModes().length;
                prop.setSubModePercentComplete(totalModePercent);
            }
        }
    };



    @Override
    public iModeManager setup() {
        lastDiff =null;
        currentModePercent =0.0;
//        if (subProp.isFeedToEmptyEnable()) {
//            sLog.d(this, "Running till feed to Empty");
//            switch (subProp.getEndPointTarget()) {
//                case DiafiltrationVolume:
//                case PermeateWeight:
//                case UV:
//                case Conductivity:
//                    break;
//                default:
//                    throw new Exception("Invalid Endpoint Target Type");
//            }
//        } else {
        switch (subProp.getEndPointTarget()) {
            case DiafiltrationVolume:
//                permModEndWt = prop.getFeedModStartWt() * subProp.getEndPointValue() + prop.getPermModStartWt();
//                sLog.d(this, "Feed Wt: Cur:" + prop.getFeedModStartWt() + " Final: " + prop.getFeedModStartWt());
//                sLog.d(this, "Perm Wt: Cur:" + prop.getPermModStartWt() + " Final: " + permModEndWt);
//                scaleEndPointListener.startRxListening();
                break;
            case PermeateWeight:
//                permModEndWt = subProp.getEndPointValue();
//                sLog.d(this, "Feed Wt: Cur:" + prop.getFeedModStartWt() + " Final: " + prop.getFeedModStartWt());
//                sLog.d(this, "Perm Wt: Cur:" + prop.getPermModStartWt() + " Final: " + permModEndWt);
//                scaleEndPointListener.startRxListening();
                break;
            case Turbidity:
            case Protein:
            case pH:
            case UV:
//                uvListener.startRxListening();
                sLog.d(this, "UV Cur:" + ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) + " UV Final: " + subProp.getEndPointValue());
                break;
            case Conductivity:
//                conductivityListener.startRxListening();
                sLog.d(this, "MHO Cur:" + ComLib.get().getKonduitInfo().getProbeConductivity_mS(subProp.getProbeID()) + " MHO Final: " + subProp.getEndPointValue());
                break;
            default:
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
        }
//        }
//        FeedWtManger.get().init(prop.getAuxProp(prop.getSubRunModeProp(stageID).getAuxPumpID()), superCallback, prop.getFeedModStartWt(), prop.getSubRunModeProp(stageID).getAuxPumpID());
        return this;
    }

    @Override
    public iModeManager pause() {
        if(subProp.getEndPointValue() != Integer.MAX_VALUE)
        FeedWtManger.get().stop();
        scaleEndPointListener.stopRxListening();
        conductivityListener.stopRxListening();
        uvListener.stopRxListening();
        return this;
    }

    @Override
    public iModeManager resume() {
        sLog.d(this, "end Point Value " + subProp.getEndPointValue());
        switch (subProp.getEndPointTarget()) {
            case DiafiltrationVolume:
            case PermeateWeight:
                if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT) {
                    if(prop.getKonduit(KonduitChannel.CHANNEL1)!=null) {
                        uvListener.startRxListening();
                    }else{
                        superCallback.callback(ModeEvent.EXCEPTION, ModeException.KONDUIT_NOT_DEFINE);
                        stop();
                        return this;
                    }
                }else {
                    scaleEndPointListener.startRxListening();
                }
                break;
            case Turbidity:
            case pH:
            case Protein:
            case PermeateVolume:
            case UV:
                if(prop.getKonduit(KonduitChannel.CHANNEL1)!=null) {
                    uvListener.startRxListening();
                }else{
                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.KONDUIT_NOT_DEFINE);
                    stop();
                    return this;
                }
                break;
            case Conductivity:
                conductivityListener.startRxListening();
                break;
            default:
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
        }
        sLog.d(this, "feed wt " + (ModeCalculator.get().getFeedTrueStartWt() - stageID == 0 ? prop.getFeedHoldUpForStage1() : 0));
        if(subProp.getEndPointValue() != Integer.MAX_VALUE)
        FeedWtManger.get().start(prop.getAuxProp(prop.getSubRunModeProp(stageID).getAuxPumpID()), superCallback, ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE) - (stageID == 0 ? prop.getFeedHoldUpForStage1() : 0), prop.getSubRunModeProp(stageID).getAuxPumpID(),prop.getTotalizerMode());
        return this;
    }

    @Override
    public iModeManager stop() {

        if(subProp.getEndPointValue() != Integer.MAX_VALUE)
        FeedWtManger.get().stop();
        scaleEndPointListener.stopRxListening();
        conductivityListener.stopRxListening();
        uvListener.stopRxListening();
        superCallback.callback(ModeEvent.SUB_MODE_FINISHED, subProp.getMode(), stageID);
        sLog.d(this, "Done");
        return this;
    }

    public double getAuxPumpFlowRate() {
        return FeedWtManger.get().getAuxPumpFlowRate();
    }
}
