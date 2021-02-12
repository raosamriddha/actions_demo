/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 18/7/19 10:34 AM
 * Modified : 18/7/19 10:33 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.iModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;

/**
 * brain of all mode validate endpoint
 * calculate CF/DV depending on current mode
 */
public class ModeCalculator extends iModeManager {

    private static ModeCalculator calculator;// = new ModeCalculator();

    private ModeCalculator() {
    }

    public static ModeCalculator get() {
        if (calculator == null) {
            calculator = new ModeCalculator();
        }
        return calculator;
    }

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

    @Override
    public iModeManager setup() {
        calculateHoldup = true;
        permTrueWt = prop.getTotalizerMode()== TotalizerMode.ENDPOINT ? 0 :ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
        permTrueStartWt = permTrueWt;
        permHoldupWt = prop.getPermHoldUp();
        feedHoldupWt = prop.getFeedHoldUp();
        feedTrueWt = prop.getFeedStartWt();
        feedTrueStartWt = feedTrueWt;
        lastFeedWt = feedTrueWt;
//
        totalDiaWt = 0;
        prop.setPermStartWt(permTrueStartWt);
        return this;
    }

    @Override
    public iModeManager pause() {
        if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
            calculator_rxl_permeateVolume.stopRxListening();
        }else {
            calculator_rxl.stopRxListening();
        }
        return this;
    }

    @Override
    public iModeManager resume() {
        if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
            calculator_rxl_permeateVolume.startRxListening();
        }else {
            calculator_rxl.startRxListening();
        }
        return this;
    }

    @Override
    public iModeManager stop() {
        if(prop.getTotalizerMode()== TotalizerMode.ENDPOINT){
            calculator_rxl_permeateVolume.stopRxListening();
        }else {
            calculator_rxl.stopRxListening();
        }
        return this;
    }

    private final RxListener calculator_rxl = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)) {
                permTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
            } else {
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.PERMEATE_SCALE_MISSING);
            }

            if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE) &&   prop.isFeedConnected()) {
                feedTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE)+prop.getFeedOffset();
            } else {
                feedTrueWt = feedTrueStartWt - permTrueWt - permHoldupWt - feedHoldupWt - totalDiaWt;
            }

            prop.setPermStartWt(permTrueStartWt);
            lastFeedWt = feedTrueWt;
            double checkForZero=0;
            switch (subProp.getMode()) {
                case D_MODE:
                    checkForZero=(feedTrueStartWt + ((stageID == 0 && prop.isFeedEmpty()) ? 0 : feedHoldupWt));
                    if(checkForZero!=0){
                        prop.setDV((permTrueWt - permTrueStartWt + (stageID == 0 ? permHoldupWt : 0)) / (feedTrueStartWt + ((stageID == 0 && prop.isFeedEmpty()) ? 0 : feedHoldupWt)));
                    }
                    break;
                case CF_MODE:
                case C_MODE:
                    checkForZero = (feedTrueWt + feedHoldupWt);
                    if(checkForZero!=0) {
                        prop.setCF((permTrueWt + permHoldupWt + feedTrueWt + feedHoldupWt - totalDiaWt) / (feedTrueWt + feedHoldupWt));
                    }
                    break;
                case VACUUM_MODE:
                    if(stageID%2!=0) {
                        checkForZero= feedTrueStartWt + feedHoldupWt;
                        if(checkForZero!=0) {
                            prop.setDV((permTrueWt - permTrueStartWt) / (feedTrueStartWt + feedHoldupWt));
                        }
                    }else {
                        checkForZero = (feedTrueWt + feedHoldupWt);
                        if(checkForZero!=0) {
                            prop.setCF((permTrueWt + permHoldupWt + feedTrueWt + feedHoldupWt - totalDiaWt) / (feedTrueWt + feedHoldupWt));
                        }
                    }
                    break;
            }            sLog.d(ModeCalculator.this, calculator.toString());
            sLog.d(ModeCalculator.this, "CF = " + prop.getCF() + " DV = " + prop.getDV());
            sLog.d(this,"current percent "+ prop.getSubModePercentComplete());
        }
    };

    private final RxListener calculator_rxl_permeateVolume = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            if (ComLib.get().getKonduitInfo().isKFConduitConnected()) {
                permTrueWt = FlowMeterManager.get().getVolume(0);
            } else {
                superCallback.callback(ModeEvent.EXCEPTION, ModeException.KF_KONDUIT_MISSING);
            }

            if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE)&&   prop.isFeedConnected()) {
                feedTrueWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
            } else {
                feedTrueWt = feedTrueStartWt - permTrueWt - permHoldupWt - feedHoldupWt - totalDiaWt;
            }

            prop.setPermStartWt(permTrueStartWt);
            lastFeedWt = feedTrueWt;
//
            double checkForZero=0;
            switch (subProp.getMode()) {
                case D_MODE:
                    checkForZero=(feedTrueStartWt + ((stageID == 0 && prop.isFeedEmpty()) ? 0 : feedHoldupWt));
                    if(checkForZero!=0){
                        prop.setDV((permTrueWt - permTrueStartWt + (stageID == 0 ? permHoldupWt : 0)) / (feedTrueStartWt + ((stageID == 0 && prop.isFeedEmpty()) ? 0 : feedHoldupWt)));
                    }
                    break;
                case CF_MODE:
                case C_MODE:
                    checkForZero = (feedTrueWt + feedHoldupWt);
                    if(checkForZero!=0) {
                        prop.setCF((permTrueWt + permHoldupWt + feedTrueWt + feedHoldupWt - totalDiaWt) / (feedTrueWt + feedHoldupWt));
                    }
                    break;
                case VACUUM_MODE:
                    if(stageID%2!=0) {
                        checkForZero= feedTrueStartWt + feedHoldupWt;
                        if(checkForZero!=0) {
                            prop.setDV((permTrueWt - permTrueStartWt) / (feedTrueStartWt + feedHoldupWt));
                        }
                    }else {
                        checkForZero = (feedTrueWt + feedHoldupWt);
                        if(checkForZero!=0) {
                            prop.setCF((permTrueWt + permHoldupWt + feedTrueWt + feedHoldupWt - totalDiaWt) / (feedTrueWt + feedHoldupWt));
                        }
                    }
                    break;
            }
            sLog.d(ModeCalculator.this, calculator.toString());
            sLog.d(ModeCalculator.this, "CF = " + prop.getCF() + " DV = " + prop.getDV());
            sLog.d(this,"current percent "+ prop.getSubModePercentComplete());
        }
    };

    public void updateModStart() {
        if (stageID < prop.getSubModeCount() - 1) {
            if (subProp.getMode() == OperationMode.D_MODE || (stageID % 2 != 0 && subProp.getMode() == OperationMode.VACUUM_MODE)) {
                totalDiaWt += permTrueWt + feedTrueWt - permTrueStartWt - feedTrueStartWt;
                prop.setDV(0);
            }
            permTrueStartWt = permTrueWt;
            feedTrueStartWt = feedTrueWt;
//
            prop.setPermStartWt(permTrueStartWt);
            setStage(stageID + 1);
        }
    }

    public double getPermTrueStartWt() {
        return permTrueStartWt;
    }

    public double getPermTrueWt() {
        return permTrueWt;
    }

    public double getFeedTrueStartWt() {
        return feedTrueStartWt;
    }

    public double getFeedTrueWt() {
        return feedTrueWt;
    }

    @Override
    public String toString() {
        return "ModeCalculator {" +
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

    public boolean endPointInvalid() {
        double permTrueEndPointWt = -Double.MAX_VALUE;
        if (subProp.getEndPointTarget() != null) {
            switch (subProp.getEndPointTarget()) {
                case PermeateWeight:
                    permTrueEndPointWt = subProp.getEndPointValue() - prop.getPermHoldUp();
                    break;
                case DiafiltrationVolume:
                    permTrueEndPointWt = permTrueWt + (feedTrueStartWt + feedHoldupWt) * subProp.getEndPointValue();
                    break;
                case ConcentrationFactor:
                    switch (subProp.getMode()) {
                        case C_MODE:
                            permTrueEndPointWt = permTrueStartWt - prop.getPermHoldUp() - totalDiaWt + (feedTrueStartWt + feedHoldupWt) * (1 - (1 / subProp.getEndPointValue()));
                            break;
                        case CF_MODE:
                            permTrueEndPointWt = permTrueStartWt - prop.getPermHoldUp() + (feedTrueStartWt + feedHoldupWt) * (subProp.getEndPointValue() - 1);
                            break;
                    }
                    break;
                default:
                    return false;
            }
            sLog.d(this, "Calculated permScaleEndpoint=" + permTrueEndPointWt);
            return permTrueEndPointWt <= ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
        } else {
            return false;
        }
    }
}
