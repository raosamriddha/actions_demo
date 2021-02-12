/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 11:11 AM
 * Modified : 9/7/19 11:11 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.AlarmProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.KonduitProp;

import static com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent.*;

/**
 * plug in class
 * used to check and generate alarm event
 */
public class AlarmManager {
    /**
     *
     */
    private static AlarmManager alarmManager;

    public static AlarmManager get() {
        if (alarmManager == null) {
            alarmManager = new AlarmManager();
        }
        return alarmManager;
    }

    /**
     *
     */
    private ModeListener superCallback;
    private AlarmProp prop;
    private KonduitProp konduitProp1, konduitProp2;

    public void start(AlarmProp prop, KonduitProp konduitProp1, KonduitProp konduitProp2, ModeListener callback) {
        this.superCallback = callback;
        this.prop = prop;
        this.konduitProp1 = konduitProp1;
        this.konduitProp2 = konduitProp2;
//
        if (prop != null) {
            if (prop.getFeedPressure() != null) {
                feedPressureAlarm.startRxListening();
                sLog.d(AlarmManager.this, "FeedPressure Alarms active");
            }
            if (prop.getPermPressure() != null) {
                permPressureAlarm.startRxListening();
                sLog.d(AlarmManager.this, "PermPressure Alarms active");
            }
            if (prop.getFeedWt() != null) {
                feedScaleAlarm.startRxListening();
                sLog.d(AlarmManager.this, "feed Wt Alarms active");
            }
            if (prop.getPermWt() != null) {
                permScaleAlarm.startRxListening();
                sLog.d(AlarmManager.this, "perm Wt Alarms active");
            }
            if (prop.getKonduit1() != null) {
                if (konduitProp1 != null) {
                    konduitChannel1Alarm.startRxListening();
                    sLog.d(AlarmManager.this, "Konduit1 Alarms active");
                } else {
                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.KONDUIT_NOT_DEFINE);
                }
            }
            if (prop.getKonduit2() != null) {
                if (konduitProp2 != null) {
                    konduitChannel2Alarm.startRxListening();
                    sLog.d(AlarmManager.this, "Konduit2 Alarms active");
                } else {
                    superCallback.callback(ModeEvent.EXCEPTION, ModeException.KONDUIT_NOT_DEFINE);
                }
            }
            if (prop.getFlowMeterVolumeCh1() != null) {
                flowMeterVolumeCh1Alarm.startRxListening();
                sLog.d(AlarmManager.this, "flow meter volume ch1 Alarms active");
            }
            if (prop.getFlowMeterVolumeCh2() != null) {
                flowMeterVolumeCh2Alarm.startRxListening();
                sLog.d(AlarmManager.this, "flow meter volume ch2 Alarms active");
            }
        }

    }

    public void stop() {
        feedPressureAlarm.stopRxListening();
        permPressureAlarm.stopRxListening();
        feedScaleAlarm.stopRxListening();
        permScaleAlarm.stopRxListening();
        konduitChannel1Alarm.stopRxListening();
        konduitChannel2Alarm.stopRxListening();
        flowMeterVolumeCh1Alarm.stopRxListening();
        flowMeterVolumeCh2Alarm.stopRxListening();
    }

    private final RxListener feedPressureAlarm = new RxListener(ComLib.get().getPressureInfo()) {
        @Override
        public void OnReceive() {
            double wt = ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.FEED_CHANNEL);
            if (prop.getFeedPressure().getHighStop() != null && wt >= prop.getFeedPressure().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, FEED_PRESSURE_ALARM_HIGH_STOP, wt);
            } else if (prop.getFeedPressure().getHighWarn() != null && wt >= prop.getFeedPressure().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, FEED_PRESSURE_ALARM_HIGH_ALARM, wt);
            } else if (prop.getFeedPressure().getLowStop() != null && wt <= prop.getFeedPressure().getLowStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, FEED_PRESSURE_ALARM_LOW_STOP, wt);
            } else if (prop.getFeedPressure().getLowWarn() != null && wt <= prop.getFeedPressure().getLowWarn()) {
                superCallback.callback(SOFT_FAULT, FEED_PRESSURE_ALARM_LOW_ALARM, wt);
            }
        }
    };

    private final RxListener permPressureAlarm = new RxListener(ComLib.get().getPressureInfo()) {
        @Override
        public void OnReceive() {
            double wt = ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.PERMEATE_CHANNEL);
            if (prop.getPermPressure().getHighStop() != null && wt >= prop.getPermPressure().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, PERMEATE_PRESSURE_ALARM_HIGH_STOP, wt);
            } else if (prop.getPermPressure().getHighWarn() != null && wt >= prop.getPermPressure().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, PERMEATE_PRESSURE_ALARM_HIGH_ALARM, wt);
            } else if (prop.getPermPressure().getLowStop() != null && wt <= prop.getPermPressure().getLowStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, PERMEATE_PRESSURE_ALARM_LOW_STOP, wt);
            } else if (prop.getPermPressure().getLowWarn() != null && wt <= prop.getPermPressure().getLowWarn()) {
                superCallback.callback(SOFT_FAULT, PERMEATE_PRESSURE_ALARM_LOW_ALARM, wt);
            }
        }
    };

    private final RxListener feedScaleAlarm = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            double wt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
            if (prop.getFeedWt().getHighStop() != null && wt >= prop.getFeedWt().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, FEED_WEIGHT_ALARM_HIGH_STOP, wt);
            } else if (prop.getFeedWt().getHighWarn() != null && wt >= prop.getFeedWt().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, FEED_WEIGHT_ALARM_HIGH_ALARM, wt);
            } else if (prop.getFeedWt().getLowStop() != null && wt <= prop.getFeedWt().getLowStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, FEED_WEIGHT_ALARM_LOW_STOP, wt);
            } else if (prop.getFeedWt().getLowWarn() != null && wt <= prop.getFeedWt().getLowWarn()) {
                superCallback.callback(SOFT_FAULT, FEED_WEIGHT_ALARM_LOW_ALARM, wt);
            }
        }
    };

    private final RxListener permScaleAlarm = new RxListener(ComLib.get().getScaleInfo()) {
        @Override
        public void OnReceive() {
            double wt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
            if (prop.getPermWt().getHighStop() != null && wt >= prop.getPermWt().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, PERMEATE_WEIGHT_ALARM_HIGH_STOP, wt);
            } else if (prop.getPermWt().getHighWarn() != null && wt >= prop.getPermWt().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, PERMEATE_WEIGHT_ALARM_HIGH_ALARM, wt);
            } else if (prop.getPermWt().getLowStop() != null && wt <= prop.getPermWt().getLowStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, PERMEATE_WEIGHT_ALARM_LOW_STOP, wt);
            } else if (prop.getPermWt().getLowWarn() != null && wt <= prop.getPermWt().getLowWarn()) {
                superCallback.callback(SOFT_FAULT, PERMEATE_WEIGHT_ALARM_LOW_ALARM, wt);
            }

        }
    };

    private final RxListener konduitChannel1Alarm = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double wt = ComLib.get().getKonduitInfo().getProbeUV_AU(0) * konduitProp1.getXValue() + konduitProp1.getYValue();
            if (prop.getKonduit1().getHighStop() != null && wt >= prop.getKonduit1().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, KONDUIT1_ALARM_HIGH_STOP, wt, konduitProp1.getType());
            } else if (prop.getKonduit1().getHighWarn() != null && wt >= prop.getKonduit1().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, KONDUIT1_ALARM_HIGH_ALARM, wt, konduitProp1.getType());
            }
        }
    };

    private final RxListener konduitChannel2Alarm = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double wt = ComLib.get().getKonduitInfo().getProbeUV_AU(1) * konduitProp2.getXValue() + konduitProp2.getYValue();
            if (prop.getKonduit2().getHighStop() != null && wt >= prop.getKonduit2().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, KONDUIT2_ALARM_HIGH_STOP, wt, konduitProp2.getType());
            } else if (prop.getKonduit2().getHighWarn() != null && wt >= prop.getKonduit2().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, KONDUIT2_ALARM_HIGH_ALARM, wt, konduitProp2.getType());
            }

        }
    };


    private final RxListener flowMeterVolumeCh1Alarm = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double wt = FlowMeterManager.get().getVolume(0);
            if (prop.getFlowMeterVolumeCh1().getHighStop() != null && wt >= prop.getFlowMeterVolumeCh1().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, FLOW_METER_CH1_ALARM_HIGH_STOP, wt);
            } else if (prop.getFlowMeterVolumeCh1().getHighWarn() != null && wt >= prop.getFlowMeterVolumeCh1().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, FLOW_METER_CH1_ALARM_HIGH_ALARM, wt);
            }

        }
    };


    private final RxListener flowMeterVolumeCh2Alarm = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double wt = FlowMeterManager.get().getVolume(1);
            if (prop.getFlowMeterVolumeCh2().getHighStop() != null && wt >= prop.getFlowMeterVolumeCh2().getHighStop()) {
                stopRxListening();
                superCallback.callback(HARD_FAULT, FLOW_METER_CH2_ALARM_HIGH_STOP, wt);
            } else if (prop.getFlowMeterVolumeCh2().getHighWarn() != null && wt >= prop.getFlowMeterVolumeCh2().getHighWarn()) {
                superCallback.callback(SOFT_FAULT, FLOW_METER_CH2_ALARM_HIGH_ALARM, wt);
            }

        }
    };


}
