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
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowMeterManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeCalculator;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

public class CMode extends iModeManager {

    private static CMode singleInstance;// = new ModeCalculator();

    private CMode() {
    }

    public static CMode get() {
        if (singleInstance == null) {
            singleInstance = new CMode();
        }
        return singleInstance;
    }

    private double currentModePercent=0.0;
    private double startCF=1;

    private final RxListener uvListener = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double totalModePercent;
            switch (subProp.getEndPointTarget()){
                case UV:
                    if (isEndPointReached(ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) - subProp.getEndPointValue())) {
                        sLog.d(CMode.this, "Final UV:" + subProp.getProbeID() + " reached");
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
                        sLog.d(CMode.this, "Final "+subProp.getEndPointTarget()+" " + subProp.getProbeID() + " reached");
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
                        sLog.d(CMode.this, "Final permeate volume reached");
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (FlowMeterManager.get().getVolume(0)+prop.getPermHoldUp()-ModeCalculator.get().getPermTrueStartWt())*100.0/(subProp.getEndPointValue()-ModeCalculator.get().getPermTrueStartWt());
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    totalModePercent = (stageID*100.0/prop.getRunModes().length) + currentModePercent/prop.getRunModes().length;
                    prop.setSubModePercentComplete(totalModePercent);
                    break;
                case ConcentrationFactor:
                    double cf = prop.getCF();//(ComLib.get().getScaleInfo().totalScaleReading_gm() + prop.getPermHoldUp() - prop.getTotalDiaWt()) / (ComLib.get().getScaleInfo().totalScaleReading_gm() - ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE));
                    sLog.d(CMode.this, "CF = " + cf);
                    if (cf >= subProp.getEndPointValue()) {
                        sLog.d(CMode.this, "Final CF reached");
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

    private final RxListener scaleEndPointListener = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            sLog.d(CMode.this, subProp.getEndPointValue());
            switch (subProp.getEndPointTarget()) {
                case PermeateWeight:
                    if (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) >= (subProp.getEndPointValue() - prop.getPermHoldUp())) {
                        sLog.d(CMode.this, "Final permeate weight reached");
                        ModeCalculator.get().updateModStart();
                        stopRxListening();
                        superCallback.callback(ModeEvent.ENDPOINT_REACHED, subProp.getMode());
                    }
                    currentModePercent = (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE)+prop.getPermHoldUp()-ModeCalculator.get().getPermTrueStartWt())*100.0/(subProp.getEndPointValue()-ModeCalculator.get().getPermTrueStartWt());
                    currentModePercent = ufx.boundInRange(currentModePercent,0,100);
                    break;
                case ConcentrationFactor:
                    double cf = prop.getCF();//(ComLib.get().getScaleInfo().totalScaleReading_gm() + prop.getPermHoldUp() - prop.getTotalDiaWt()) / (ComLib.get().getScaleInfo().totalScaleReading_gm() - ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE));
                    sLog.d(CMode.this, "CF = " + cf);
                    if (cf >= subProp.getEndPointValue()) {
                        sLog.d(CMode.this, "Final CF reached");
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

    @Override
    public iModeManager setup() {
        currentModePercent =0.0;
        lastDiff =null;
        double feedModEndWt;
        double permModEndWt;
        switch (subProp.getEndPointTarget()) {
            case ConcentrationFactor:
//                feedModEndWt = ((prop.getFeedModStartWt() + prop.getPermModStartWt() - prop.getTotalDiaWt()) / subProp.getEndPointValue());
//                permModEndWt = prop.getPermModStartWt() + (prop.getFeedModStartWt() - feedModEndWt);
//                sLog.d(this, "Feed Wt: Cur:" + prop.getFeedModStartWt() + " Final: " + feedModEndWt);
//                sLog.d(this, "Perm Wt: Cur:" + prop.getPermModStartWt() + " Final: " + permModEndWt);
//                scaleEndPointListener.startRxListening();
                break;
            case PermeateWeight:
//                permModEndWt = subProp.getEndPointValue();
//                feedModEndWt = prop.getFeedModStartWt() - (permModEndWt - prop.getPermModStartWt());
//                sLog.d(this, "Feed Wt: Cur:" + prop.getFeedModStartWt() + " Final: " + feedModEndWt);
//                sLog.d(this, "Perm Wt: Cur:" + prop.getPermModStartWt() + " Final: " + permModEndWt);
//                scaleEndPointListener.startRxListening();
                break;
            case pH:
            case Protein:
            case Turbidity:
            case UV:
//                uvListener.startRxListening();
                sLog.d(this, "UV Cur:" + ComLib.get().getKonduitInfo().getProbeUV_AU(subProp.getProbeID()) + " UV Final: " + subProp.getEndPointValue());
                break;
            default:
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.INVALID_ENDPOINT);
        }
        startCF = prop.getCF();
        return this;
    }

    @Override
    public iModeManager pause() {
        scaleEndPointListener.stopRxListening();
        uvListener.stopRxListening();
        return this;
    }

    @Override
    public iModeManager resume() {
        sLog.d(this, "end Point Value " + subProp.getEndPointValue());
        switch (subProp.getEndPointTarget()) {
            case ConcentrationFactor:
            case PermeateWeight:
                if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
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
            case pH:
            case Protein:
            case Turbidity:
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
        return this;
    }

    @Override
    public iModeManager stop() {
//        remove Listeners
        scaleEndPointListener.stopRxListening();
        uvListener.stopRxListening();
//        stop components in possession
//        super mode callback
        sLog.d(this, "Done");
        superCallback.callback(ModeEvent.SUB_MODE_FINISHED, subProp.getMode(), stageID);
        return this;
    }

}
