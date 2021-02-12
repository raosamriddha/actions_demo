/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 11:17 AM
 * Modified : 9/7/19 11:17 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp;

public class AlarmProp {

    private AlarmRange feedPressure;
    private AlarmRange permPressure;
    private AlarmRange feedWt;
    private AlarmRange permWt;
    private AlarmRange konduit1;
    private AlarmRange konduit2;
    private AlarmRange flowMeterVolumeCh1;
    private AlarmRange flowMeterVolumeCh2;


    public static class AlarmRange {
        Double highStop;
        Double highWarn;
        Double lowWarn;
        Double lowStop;

        public AlarmRange(Double highStop, Double highWarn, Double lowWarn, Double lowStop) {
            this.highStop = highStop;
            this.highWarn = highWarn;
            this.lowWarn = lowWarn;
            this.lowStop = lowStop;
        }

        public void setHighStop(Double highStop) {
            this.highStop = highStop;
        }

        public void setHighWarn(Double highWarn) {
            this.highWarn = highWarn;
        }

        public void setLowWarn(Double lowWarn) {
            this.lowWarn = lowWarn;
        }

        public void setLowStop(Double lowStop) {
            this.lowStop = lowStop;
        }

        public Double getHighStop() {
            return highStop;
        }

        public Double getHighWarn() {
            return highWarn;
        }

        public Double getLowWarn() {
            return lowWarn;
        }

        public Double getLowStop() {
            return lowStop;
        }
    }

    public AlarmRange getFeedPressure() {
        return feedPressure;
    }

    public AlarmRange getPermPressure() {
        return permPressure;
    }

    public AlarmRange getFeedWt() {
        return feedWt;
    }

    public AlarmRange getPermWt() {
        return permWt;
    }

    public AlarmRange getKonduit1() {
        return konduit1;
    }

    public AlarmRange getKonduit2() {
        return konduit2;
    }

    public void setFeedPressure(AlarmRange feedPressure) {
        this.feedPressure = feedPressure;
    }

    public void setPermPressure(AlarmRange permPressure) {
        this.permPressure = permPressure;
    }

    public void setFeedWt(AlarmRange feedWt) {
        this.feedWt = feedWt;
    }

    public void setPermWt(AlarmRange permWt) {
        this.permWt = permWt;
    }

    public void setKonduit1(AlarmRange konduit1) {
        this.konduit1 = konduit1;
    }

    public void setKonduit2(AlarmRange konduit2) {
        this.konduit2 = konduit2;
    }

    public AlarmRange getFlowMeterVolumeCh1() {
        return flowMeterVolumeCh1;
    }

    public void setFlowMeterVolumeCh1(AlarmRange flowMeterVolumeCh1) {
        this.flowMeterVolumeCh1 = flowMeterVolumeCh1;
    }

    public AlarmRange getFlowMeterVolumeCh2() {
        return flowMeterVolumeCh2;
    }

    public void setFlowMeterVolumeCh2(AlarmRange flowMeterVolumeCh2) {
        this.flowMeterVolumeCh2 = flowMeterVolumeCh2;
    }
}
