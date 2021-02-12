/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 12:23 PM
 * Modified : 9/7/19 12:23 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.AuxProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

import static com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID.FEED_SCALE;
import static com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID.PERMEATE_SCALE;

/**
 * used in D and CFC mode to maintain feed wt
 * detect feed to empty
 */
public class FeedWtManger {
    /**
     *
     */
    private static FeedWtManger feedWtManger;
    private double auxPumpFlowRate;
    private boolean startFlag;

    public static FeedWtManger get() {
        if (feedWtManger == null) {
            feedWtManger = new FeedWtManger();
        }
        return feedWtManger;
    }

    /**
     *
     */
    private int auxId;
    private AuxProp auxProp;
    private double feedStartWt;
    private ModeListener superCallback;


    public void start(AuxProp prop, ModeListener superCallback, double feedStartWt, int auxID, TotalizerMode totalizerMode) {
        this.auxProp = prop;
        this.superCallback = superCallback;
        this.feedStartWt = feedStartWt;
        this.auxId = auxID;
//
        if(totalizerMode == TotalizerMode.ENDPOINT){
            feedWtKonduitListener.startRxListening();
        }else {
            feedWtListener.startRxListening();
        }
        sLog.d(this, " Start Endpoint type " + totalizerMode);
        startFlag =false;
    }

    public void stop() {
        feedWtListener.stopRxListening();
        feedWtKonduitListener.stopRxListening();
        ComLib.get().getAuxPump().setAuxPumpRPM(auxId, auxProp.getTypeID(), 0);
        sLog.d(this, " Stop" );
    }

    private final RxListener feedWtListener = new RxListener(ComLib.get().getScaleInfo()) {

        private final double kp = 0.25;
        private final double kd = 0.25;
        private final double ki = 0.0125;
        private double errConstant, lastErr;
        private int feedEmptyCounter;
        private double lastTotalScaleWt;

        @Override
        public void reset() {
            errConstant = 0;
            lastErr = 0;
            feedEmptyCounter = 0;
            lastTotalScaleWt = ComLib.get().getScaleInfo().scaleReading_gm(FEED_SCALE) + ComLib.get().getScaleInfo().scaleReading_gm(PERMEATE_SCALE);
        }

        @Override
        public void OnReceive() {
            double fw = ComLib.get().getScaleInfo().scaleReading_gm(FEED_SCALE);
            if(fw<feedStartWt){
                startFlag=true;
            }
            if(startFlag) {
                double feedErr = (feedStartWt - fw);
                double errRate = feedErr - lastErr;
                lastErr = feedErr;
                errConstant += feedErr;
                auxPumpFlowRate = (kp * feedErr + kd * errRate + ki * errConstant) * 60;
                int auxPumpRPM = (int) ufx.boundInRange(auxPumpFlowRate * 100 / auxProp.getTubeFlowConversion(), 0, auxProp.getMaxRPM());
                ComLib.get().getAuxPump().setAuxPumpRPM(auxId, auxProp.getTypeID(), auxPumpRPM);
                sLog.d(this, " feed Wt " + feedStartWt);
                double totalScaleWt = ComLib.get().getScaleInfo().scaleReading_gm(FEED_SCALE) + ComLib.get().getScaleInfo().scaleReading_gm(PERMEATE_SCALE);
                sLog.d(FeedWtManger.this, auxPumpRPM + ">150"
                        + " & " + totalScaleWt + ">" + 1.12 * feedStartWt
                        + " & " + (totalScaleWt - lastTotalScaleWt) + ">" + (auxPumpRPM * auxProp.getTubeFlowConversion() / (400 * 60)));
                if ((auxPumpRPM > 0.5 * auxProp.getMaxRPM())
                        && ((totalScaleWt) > 1.12 * feedStartWt)
                        && ((totalScaleWt - lastTotalScaleWt) < auxPumpRPM * auxProp.getTubeFlowConversion() / (400 * 60))) {
                    feedEmptyCounter++;
                    if (feedEmptyCounter > 9) {
                        stop();
                        superCallback.callback(ModeEvent.FEED_TO_EMPTY_DETECTED);
                    }
                } else {
                    feedEmptyCounter = 0;
                }
                lastTotalScaleWt = totalScaleWt;
            }
        }
    };


    private final RxListener feedWtKonduitListener = new RxListener(ComLib.get().getKonduitInfo()) {

        private final double kp = 0.25;
        private final double kd = 0.25;
        private final double ki = 0.0125;
        private double errConstant, lastErr;
        private int feedEmptyCounter;
        private double lastTotalScaleWt;

        @Override
        public void reset() {
            errConstant = 0;
            lastErr = 0;
            feedEmptyCounter = 0;
            lastTotalScaleWt = ComLib.get().getScaleInfo().scaleReading_gm(FEED_SCALE) +FlowMeterManager.get().getVolume(0);
        }

        @Override
        public void OnReceive() {
            double fw = ComLib.get().getScaleInfo().scaleReading_gm(FEED_SCALE);
            if(fw<feedStartWt){
                startFlag=true;
            }
            if(startFlag) {
                double feedErr = (feedStartWt - fw);
                double errRate = feedErr - lastErr;
                lastErr = feedErr;
                errConstant += feedErr;
                auxPumpFlowRate = (kp * feedErr + kd * errRate + ki * errConstant) * 60;
                int auxPumpRPM = (int) ufx.boundInRange(auxPumpFlowRate * 100 / auxProp.getTubeFlowConversion(), 0, auxProp.getMaxRPM());
                ComLib.get().getAuxPump().setAuxPumpRPM(auxId, auxProp.getTypeID(), auxPumpRPM);
                sLog.d(this, " feed Wt " + feedStartWt);
                double totalScaleWt = ComLib.get().getScaleInfo().scaleReading_gm(FEED_SCALE) + +FlowMeterManager.get().getVolume(0);
                sLog.d(FeedWtManger.this, auxPumpRPM + ">150"
                        + " & " + totalScaleWt + ">" + 1.12 * feedStartWt
                        + " & " + (totalScaleWt - lastTotalScaleWt) + ">" + (auxPumpRPM * auxProp.getTubeFlowConversion() / (400 * 60)));
                if ((auxPumpRPM > 0.5 * auxProp.getMaxRPM())
                        && ((totalScaleWt) > 1.12 * feedStartWt)
                        && ((totalScaleWt - lastTotalScaleWt) < auxPumpRPM * auxProp.getTubeFlowConversion() / (400 * 60))) {
                    feedEmptyCounter++;
                    if (feedEmptyCounter > 9) {
                        stop();
                        superCallback.callback(ModeEvent.FEED_TO_EMPTY_DETECTED);
                    }
                } else {
                    feedEmptyCounter = 0;
                }
                lastTotalScaleWt = totalScaleWt;
            }
        }
    };

    public double getAuxPumpFlowRate() {
        return auxPumpFlowRate;
    }
}
