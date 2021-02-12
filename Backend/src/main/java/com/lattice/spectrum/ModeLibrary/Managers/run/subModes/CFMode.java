/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 18/7/19 2:20 PM
 * Modified : 18/7/19 2:16 PM
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
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

public class CFMode extends iModeManager {

    private static CFMode singleInstance;// = new ModeCalculator();

    private CFMode() {
    }

    public static CFMode get() {
        if (singleInstance == null) {
            singleInstance = new CFMode();
        }
        return singleInstance;
    }

    private boolean isFeedToEmptyDetected;
    private double currentModePercent=0.0;
    private double startCF=1;

    /**
     *
     */
    private final RxListener scaleEndPointListener = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            switch (subProp.getEndPointTarget()) {
                case PermeateWeight:
                    if (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) >= (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                        sLog.d(CFMode.this, "Final permeate weight reached");
//                        updateModValue();
                        stopRxListening();
                        ModeCalculator.get().updateModStart();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE)+prop.getPermHoldUp()-ModeCalculator.get().getPermTrueStartWt())*100.0/(subProp.getEndPointValue()-ModeCalculator.get().getPermTrueStartWt());
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    break;
                case ConcentrationFactor:
                    double cf = prop.getCF(); //(ComLib.get().getScaleInfo().totalScaleReading_gm() + prop.getPermHoldUp() - prop.getTotalDiaWt()) / (ComLib.get().getScaleInfo().totalScaleReading_gm() - ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE));
                    if (cf >= subProp.getEndPointValue()) {
                        sLog.d(CFMode.this, "Final CF reached");
//                        updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (cf-startCF)*100.0/(subProp.getEndPointValue()-startCF);
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
            switch (subProp.getEndPointTarget()){
                case UV:
                    if (isEndPointReached(ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) - subProp.getEndPointValue())) {
                        sLog.d(CFMode.this, "Final UV:" + subProp.getProbeID() + " reached");
//                    updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + konduitPercent /prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case Turbidity:
                case Protein:
                case pH:
                    double uvCalculated= ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID())*prop.getKonduit(KonduitChannel.CHANNEL1).getXValue()+prop.getKonduit(KonduitChannel.CHANNEL1).getYValue();
                    setKonduitValue(subProp.getProbeID(),uvCalculated,prop.getKonduit(subProp.getProbeID()).getType());
                    if (isEndPointReached(uvCalculated - subProp.getEndPointValue())) {
                        sLog.d(CFMode.this, "Final "+subProp.getEndPointTarget()+" " + subProp.getProbeID() + " reached");
//                    updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + konduitPercent /prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case PermeateVolume:
                    if (FlowMeterManager.get().getVolume(0) >= (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                        sLog.d(CFMode.this, "Final permeate volume reached");
//                        updateModValue();
                        stopRxListening();
                        ModeCalculator.get().updateModStart();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (FlowMeterManager.get().getVolume(0)+prop.getPermHoldUp()-ModeCalculator.get().getPermTrueStartWt())*100.0/(subProp.getEndPointValue()-ModeCalculator.get().getPermTrueStartWt());
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + currentModePercent/prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case ConcentrationFactor:
                    double cf = prop.getCF(); //(ComLib.get().getScaleInfo().totalScaleReading_gm() + prop.getPermHoldUp() - prop.getTotalDiaWt()) / (ComLib.get().getScaleInfo().totalScaleReading_gm() - ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE));
                    if (cf >= subProp.getEndPointValue()) {
                        sLog.d(CFMode.this, "Final CF reached");
//                        updateModValue();
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (cf-startCF)*100.0/(subProp.getEndPointValue()-startCF);
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + currentModePercent/prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
            }

        }
    };

    private ModeListener feedEmptyListener = new ModeListener() {
        @Override
        public void callback(ModeEvent id, Object... obj) {
            superCallback.callback(id, obj);
            if (id == ModeEvent.FEED_TO_EMPTY_DETECTED /*&& subProp.isFeedToEmptyEnable()*/) {
                sLog.d(CFMode.this, "Feed To Empty Detected");
                isFeedToEmptyDetected = true;
//                updateModValue();
//                superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                if(subProp.getEndPointValue() != Integer.MAX_VALUE)
                FeedWtManger.get().stop();
                switch (subProp.getEndPointTarget()) {
                    case PermeateWeight:
                        if (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) < (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                            scaleEndPointListener.startRxListening();
                        } else {
//                            updateModValue();
                            ModeCalculator.get().updateModStart();
                            superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                        }
                        break;
                    case PermeateVolume:
                        if (FlowMeterManager.get().getVolume(0) < (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                            uvListener.startRxListening();
                        } else {
                            ModeCalculator.get().updateModStart();
                            superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                        }
                    case ConcentrationFactor:
                        double cf = prop.getCF();//(ComLib.get().getScaleInfo().totalScaleReading_gm() + prop.getPermHoldUp() - prop.getTotalDiaWt()) / ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
                        if (cf < subProp.getEndPointValue()) {
                            if(prop.getTotalizerMode()==TotalizerMode.ENDPOINT){
                                uvListener.startRxListening();
                            }else {
                                scaleEndPointListener.startRxListening();
                            }
                        } else {
//                            updateModValue();
                            ModeCalculator.get().updateModStart();
                            superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                        }
                        break;
                    case UV:
                        if (!isEndPointReached(ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) - subProp.getEndPointValue())) {
                            uvListener.startRxListening();
                        } else {
//                            updateModValue();
                            ModeCalculator.get().updateModStart();
                            superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                        }
                        break;
                    case Turbidity:
                    case Protein:
                    case pH:
                        double uvCalculated= ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID())*prop.getKonduit(KonduitChannel.CHANNEL1).getXValue()+prop.getKonduit(KonduitChannel.CHANNEL1).getYValue();
                        if (!isEndPointReached(uvCalculated - subProp.getEndPointValue())) {
                            uvListener.startRxListening();
                        } else {
//                            updateModValue();
                            ModeCalculator.get().updateModStart();
                            superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                        }
                        break;
                    default:
                        superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
                }

            }
        }
    };

    @Override
    public iModeManager setup() {
        lastDiff =null;
        isFeedToEmptyDetected = false;
        currentModePercent = 0.0;
        double permModEndWt;
        if (subProp.isFeedToEmptyEnable()) {
            sLog.d(this, "Running till feed to Empty");
            switch (subProp.getEndPointTarget()) {
                case ConcentrationFactor:
                case PermeateWeight:
                case UV:
                case pH:
                case PermeateVolume:
                case Turbidity:
                    break;
                default:
                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
            }
        } else {
            switch (subProp.getEndPointTarget()) {
                case ConcentrationFactor:
//                    permModEndWt = prop.getFeedModStartWt() * (subProp.getEndPointValue() - 1) + prop.getTotalDiaWt();
//                    sLog.d(this, "Feed Wt: Cur:" + prop.getFeedModStartWt() + " Final: " + prop.getFeedModStartWt());
//                    sLog.d(this, "Perm Wt: Cur:" + prop.getPermModStartWt() + " Final: " + permModEndWt);
//                scaleEndPointListener.startRxListening();
                    break;
                case PermeateWeight:
//                    permModEndWt = subProp.getEndPointValue();
//                    sLog.d(this, "Feed Wt: Cur:" + prop.getFeedModStartWt() + " Final: " + prop.getFeedModStartWt());
//                    sLog.d(this, "Perm Wt: Cur:" + prop.getPermModStartWt() + " Final: " + permModEndWt);
//                scaleEndPointListener.startRxListening();
                    break;
                case Turbidity:
                case Protein:
                case pH:
                case UV:
                    sLog.d(this, "UV Cur:" + ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) + " UV Final: " + subProp.getEndPointValue());
//                uvListener.startRxListening();
                    break;
                default:
                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
            }
        }
        startCF = prop.getCF();
//        FeedWtManger.get().init(prop.getAuxProp(prop.getSubRunModeProp(stageID).getAuxPumpID()), feedEmptyListener, prop.getFeedModStartWt(), prop.getSubRunModeProp(stageID).getAuxPumpID());
        return this;
    }

    @Override
    public iModeManager pause() {
        if(subProp.getEndPointValue() != Integer.MAX_VALUE)
        FeedWtManger.get().stop();
        scaleEndPointListener.stopRxListening();
        uvListener.stopRxListening();
        return this;
    }

    @Override
    public iModeManager resume() {
        if (isFeedToEmptyDetected) {
            feedEmptyListener.callback(ModeEvent.FEED_TO_EMPTY_DETECTED);
        } else {
            if (!subProp.isFeedToEmptyEnable()) {
                switch (subProp.getEndPointTarget()) {
                    case ConcentrationFactor:
                    case PermeateWeight:
                        if(prop.getTotalizerMode()==TotalizerMode.ENDPOINT){
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
                    case Protein:
                    case pH:
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
                    default:
                        superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
                }
            }
            sLog.d(this, " feed Wt " + (ModeCalculator.get().getFeedTrueStartWt() - prop.getFeedHoldUpForStage1()));
            if(subProp.getEndPointValue() != Integer.MAX_VALUE)
            FeedWtManger.get().start(prop.getAuxProp(prop.getSubRunModeProp(stageID).getAuxPumpID()),
                    feedEmptyListener, ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE) - (stageID == 0 ? prop.getFeedHoldUpForStage1() : 0), prop.getSubRunModeProp(stageID).getAuxPumpID(),prop.getTotalizerMode());
        }
        return this;
    }

    @Override
    public iModeManager stop() {
        if(subProp.getEndPointValue() != Integer.MAX_VALUE)
        FeedWtManger.get().stop();
        scaleEndPointListener.stopRxListening();
        uvListener.stopRxListening();
        sLog.d(this, "DONE");
        superCallback.callback(ModeEvent.SUB_MODE_FINISHED, subProp.getMode(), stageID);
        return null;
    }
}
