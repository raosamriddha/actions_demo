/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/5/19 10:42 AM
 * Modified : 28/5/19 10:42 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary;

import com.fazecast.jSerialComm.SerialPort;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxHandlers.ErrorReportHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxHandlers.KonduitInfoHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxHandlers.PressureInfoHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxHandlers.ScaleInfoHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.MsgHandler.TxHandlers.PressureUnitHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.TxHandlers.PumpSpeedTextHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.TxHandlers.ScaleTypeHandler;
import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers.*;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.PortDisconnectListener;
import com.lattice.spectrum.ComLibrary.comHandler.PortNotFoundException;
import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * Binder class to facilitate common entry point for all spectrum pump functions
 *
 * @author Anuj Pathak
 */
public class ComLib {

    private static ComLib comLib;

    public static ComLib get() {
        if (comLib == null) {
            comLib = new ComLib();
        }
        return comLib;
    }

    //
//
//
    private final MainPumpHandler mainPump;
    private final AuxPumpHandler auxPump;
    private final AuxPumpTypeListHandler auxPumpList;
    private final ScaleInfoHandler scaleInfo;
    private final ScaleTareHandler scaleTare;
    private final ScaleTypeListHandler scaleList;
    private final ScaleTypeHandler scaleType;
    private final PressureInfoHandler pressureInfo;
    private final PressureUnitHandler pressureUnit;
    private final PressureTareHandler pressureTare;
    private final PressureCalibrateHandler pressureCalibrate;
    private final KonduitInfoHandler konduitInfo;
    private final ValveInfoHandler valveInfo;
    private final PressureChannelGainHandler pressureChannelGain;
    private final LimitingValueHandler limitingValue;
    private final ErrorReportHandler errorReport;
    private final ErrorTextHandler errorText;
    private final TubingCalibrationHandler tubingCalibration;
    private final HeartBeatHandler heartBeatHandler;
    private final PumpSpeedTextHandler pumpSpeedTextHandler;
    private final ConfigureAuxiliaryHandler configureAuxiliary;
    private final PumpAlarmIndicatorHandler pumpAlarmIndicatorHandler;
    private final PumpKeyHandler pumpKeyHandler;
    private final PumpSerialNumberHandler pumpSerialNumberHandler;
    private final CommHandler commHandler;
    private String portName = "";
//    private final FlashModuleHandler flash;

    private ComLib() {
        // common handler
        commHandler = CommHandler.get();
        // frequent rx packets
        mainPump = new MainPumpHandler(false, commHandler);
        auxPump = new AuxPumpHandler(false, commHandler);
        scaleInfo = new ScaleInfoHandler(false, commHandler);
        pressureInfo = new PressureInfoHandler(false, commHandler);
        konduitInfo = new KonduitInfoHandler(false, commHandler);
        valveInfo = new ValveInfoHandler(false, commHandler);
        // special packets
        auxPumpList = new AuxPumpTypeListHandler(true, commHandler);
        scaleList = new ScaleTypeListHandler(true, commHandler);
        pressureTare = new PressureTareHandler(true, commHandler);
        pressureCalibrate = new PressureCalibrateHandler(false, commHandler);
        scaleTare = new ScaleTareHandler(true, commHandler);
        scaleType = new ScaleTypeHandler(true, commHandler);
        pressureChannelGain = new PressureChannelGainHandler(false, commHandler);
        limitingValue = new LimitingValueHandler(true, commHandler);
        errorReport = new ErrorReportHandler(true, commHandler);
        errorText = new ErrorTextHandler(true, commHandler);
        tubingCalibration = new TubingCalibrationHandler(true, commHandler);
        pressureUnit = new PressureUnitHandler(true, commHandler);
        configureAuxiliary = new ConfigureAuxiliaryHandler(true, commHandler);
        heartBeatHandler = new HeartBeatHandler(false,commHandler);
        pumpSpeedTextHandler = new PumpSpeedTextHandler(true,commHandler);
        pumpAlarmIndicatorHandler = new PumpAlarmIndicatorHandler(true,commHandler);
        pumpKeyHandler= new PumpKeyHandler(true,commHandler);
        pumpSerialNumberHandler = new PumpSerialNumberHandler(true, commHandler);
        //
//        flash = new FlashModuleHandler(true, commHandler);
        //
        new RxListener(getErrorReport()) {
            @Override
            public void OnReceive() {
                getErrorText().requestText((byte) getErrorReport().getErrorNumber(), (byte) 0);
            }
        }.startRxListening();

    }

    /**
     * checks each com port and identify pump model
     *
     * @param pumpModel connects automatically to supplied pump model
     * @param pd        disconnect listener to be notify disconnection
     * @return true if pump found
     * @throws Exception Device not found exception
     */
    public boolean autoConnect(String pumpModel, PortDisconnectListener pd) throws PortNotFoundException {
        String[] ports = getAllPorts();
        if (isConnected()) {
            if(ComLib.get().getMainPump().getMotorModel()!=null && ComLib.get().getMainPump().getMotorModel().equals(pumpModel)){
                return true;
            }
            disconnect();
        }
        for (String p : ports) {
            sLog.d(this, "Checking " + p);
            connectToPortName(p, 100, pd);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (pumpModel.equals(mainPump.getMotorModel())) {
                portName = p;
                commHandler.startDisconnectionListener();
                sLog.d(this, "Checking Success");
                return true;
            }
            disconnect();
            sLog.d(this, "Checking Fails");
        }
        return false;
    }


    public boolean autoConnect(boolean reconnect, String portName, PortDisconnectListener pd) throws PortNotFoundException {
        if(reconnect){
            getAllPorts();
            if (isConnected()) {
                return true;
            }
            disconnect();
            connectToPortName( portName,100, pd);
            if(isConnected()){
                Long timeInmilli = System.currentTimeMillis();
                while(System.currentTimeMillis()<timeInmilli+2000 && mainPump.getMotorModel()==null){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (mainPump.getMotorModel()!=null) {
                    this.portName = commHandler.getComPortDescription();
                    commHandler.startDisconnectionListener();
                    return true;
                }
            }
        }
        getAllPorts();
        if (isConnected()) {
            return true;
        }
        disconnect();
        connectToKMP( 100, pd);
        if(isConnected()){
            Long timeInmilli = System.currentTimeMillis();
            while(System.currentTimeMillis()<timeInmilli+2000 && mainPump.getMotorModel()==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mainPump.getMotorModel()!=null) {
                this.portName = commHandler.getComPortDescription();
                commHandler.startDisconnectionListener();
                return true;
            }
        }
        String[] ports = getAllPorts();
        if (isConnected()) {
            if(ComLib.get().getMainPump().getMotorModel()!=null){
                return true;
            }
            disconnect();
        }
        for (String p : ports) {
            sLog.d(this, "Checking " + p);
            connectToPortName(p, 100, pd);
            Long timeInmilli = System.currentTimeMillis();
            while(System.currentTimeMillis()<timeInmilli+2000 && mainPump.getMotorModel()==null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (mainPump.getMotorModel()!=null) {
                this.portName = commHandler.getComPortDescription();
                commHandler.startDisconnectionListener();
                sLog.d(this, "Checking Success");
                return true;
            }
            disconnect();
            sLog.d(this, "Checking Fails");
        }
        throw new PortNotFoundException("Port Not Found");
    }


    /**
     * autoconnect to the com port which have port description KMP+
     * @param pd        disconnect listener to be notify disconnection
     * @return true if connected
     * @throws Exception Device not found exception
     */
    public boolean autoConnect( PortDisconnectListener pd) throws PortNotFoundException {
        getAllPorts();
        if (isConnected()) {
            return true;
        }
        disconnect();
        connectToKMP( 100, pd);
        if(isConnected()){
            commHandler.startDisconnectionListener();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


    /**
     * try to connect with the descriptive device name specified by the parameter
     *
     * @param pumpModel          string name of device you want to connect to E.g. "KMP+"
     * @param timeout_millis     internal command send timeout
     * @param disconnectListener listener to be notified in case of disconnection
     * @throws Exception Device not found exception
     */
    public void connectToPumpName(String pumpModel, int timeout_millis, PortDisconnectListener disconnectListener) throws PortNotFoundException {
        commHandler.connectToDevice(pumpModel, timeout_millis, disconnectListener);
    }

    /**
     * try to connect with the descriptive device name specified by the parameter
     *
     * @param portName           string name of port you want to connect to E.g. "COM1"
     * @param timeout_millis     internal command send timeout
     * @param disconnectListener listener to be notified in case of disconnection
     * @throws Exception Device not found exception
     */
    public void connectToPortName(String portName, int timeout_millis, PortDisconnectListener disconnectListener) throws PortNotFoundException {
        commHandler.connectToPortName(portName, timeout_millis, disconnectListener);
    }



    /**
     * try to connect
     * @param timeout_millis     internal command send timeout
     * @param disconnectListener listener to be notified in case of disconnection
     * @throws Exception Device not found exception
     */
    public void connectToKMP(int timeout_millis, PortDisconnectListener disconnectListener) throws PortNotFoundException {
        commHandler.connectToKMP(timeout_millis, disconnectListener);
    }

    /**
     * disconnect function to remove serial port binding
     * can be called anytime
     */
    public void disconnect() {
        commHandler.disconnect();
    }

    /**
     * @return true if currently connected to any port
     */
    public boolean isConnected() {
        return commHandler.isConnected();
    }

    /**
     * can be used by ui to get list of port physically connected to computer
     *
     * @return returns list of ports connected to current system
     */
    public String[] getAllPorts() {
        return commHandler.getPortList();
    }

    /**
     * @return returns port name currently connected to
     */
    public String getConnetecComPort() {
        return portName;
    }

    public AuxPumpHandler getAuxPump() {
        return auxPump;
    }

    public MainPumpHandler getMainPump() {
        return mainPump;
    }

    public ScaleInfoHandler getScaleInfo() {
        return scaleInfo;
    }

    public PressureInfoHandler getPressureInfo() {
        return pressureInfo;
    }

    public KonduitInfoHandler getKonduitInfo() {
        return konduitInfo;
    }

    public ValveInfoHandler getValveInfo() {
        return valveInfo;
    }

    public AuxPumpTypeListHandler getAuxPumpList() {
        return auxPumpList;
    }

    public ScaleTypeListHandler getScaleList() {
        return scaleList;
    }

    public PressureTareHandler getPressureTare() {
        return pressureTare;
    }

    public ScaleTypeHandler getScaleType() {
        return scaleType;
    }

    public PressureChannelGainHandler getPressureChannelGain() {
        return pressureChannelGain;
    }

    public LimitingValueHandler getLimitingValue() {
        return limitingValue;
    }

    public ErrorReportHandler getErrorReport() {
        return errorReport;
    }

    public ErrorTextHandler getErrorText() {
        return errorText;
    }

    public TubingCalibrationHandler getTubingCalibration() {
        return tubingCalibration;
    }

    public PressureUnitHandler getPressureUnit() {
        return pressureUnit;
    }

    public ConfigureAuxiliaryHandler getConfigureAuxiliary() {
        return configureAuxiliary;
    }

    public ScaleTareHandler getScaleTare() {
        return scaleTare;
    }

    public PressureCalibrateHandler getPressureCalibrate() {
        return pressureCalibrate;
    }

    public HeartBeatHandler getHeartBeatHandler() {
        return heartBeatHandler;
    }

    public PumpSpeedTextHandler getPumpSpeedTextHandler() {
        return pumpSpeedTextHandler;
    }

    public PumpAlarmIndicatorHandler getPumpAlarmIndicatorHandler() {
        return pumpAlarmIndicatorHandler;
    }
    public PumpKeyHandler getPumpKeyHandler() {
        return pumpKeyHandler;
    }

    public PumpSerialNumberHandler getPumpSerialNumberHandler() {
        return pumpSerialNumberHandler;
    }

    /*
    public void updateMainCpuFirmware(String path) throws FileNotFoundException {
//        flash.updateMainCPU(new BufferedReader(new FileReader(new File(path))));
    }*/

    public String getPortName() {
        return portName;
    }
    public boolean tryReconnect(PortDisconnectListener pd) throws PortNotFoundException{
        disconnect();
        getAllPorts();
        connectToKMP(100, pd);
        if(isConnected()){
            commHandler.startDisconnectionListener();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
