/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.comHandler;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListenerWithExceptions;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxOnlyModuleHandler;
import com.lattice.spectrum.ComLibrary.utility.sLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * handles communication over UART using usb to serial interface
 *
 * @author Anuj Pathak
 */
public class CommHandler {

    private static CommHandler ch;
    private Thread thread;
    private Thread threadHeartBeat;

    public static CommHandler get() {
        if (ch == null) {
            ch = new CommHandler();
        }
        return ch;
    }

    private boolean waitTimeout;
    private final ArrayList<Byte> fifo = new ArrayList<>();
    private final HashMap<Byte, RxOnlyModuleHandler> moduleHandlerMap = new HashMap<>();
    private SerialPort[] ports;
    private SerialPort comPort;
    private PortDisconnectListener disconnectListener;

    private CommHandler() {
        getPortList();
    }

    /**
     * can be used by ui to get list of port physically connected to computer
     *
     * @return returns list of ports connected to current system
     */
    public String[] getPortList() {
        ports = SerialPort.getCommPorts();
        StringBuilder sb = new StringBuilder();
        for (SerialPort p : ports) {
//            sb.append(p.getPortDescription()).append(" [").append(p.getDescriptivePortName()).append("]\n");
            sb.append(p.getSystemPortName()).append("\n");
        }
        return sb.toString().split("\n");
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
        for (SerialPort p : ports) {
            if (p.getSystemPortName().contains(portName)) {
                sLog.d(this, "Connecting To " + p.getPortDescription() + " [" + p.getSystemPortName() + "]");
                connectToDevice(p, timeout_millis, disconnectListener);
                return;
            }
        }
    }


    /**
     * try to connect with the descriptive device name specified by the parameter
     * @param timeout_millis     internal command send timeout
     * @param disconnectListener listener to be notified in case of disconnection
     * @throws Exception Device not found exception
     */
    public void connectToKMP(int timeout_millis, PortDisconnectListener disconnectListener) throws PortNotFoundException {
        for (SerialPort p : ports) {
            if (p.getPortDescription().toLowerCase().equals("kmp+")) {
                sLog.d(this, "Connecting To " + p.getPortDescription() + " [" + p.getSystemPortName() + "]");
                connectToDevice(p, timeout_millis, disconnectListener);
                return;
            }
        }
    }


    /**
     * try to connect with the descriptive device name specified by the parameter
     *
     * @param description        string name of device you want to connect to E.g. "KMP+"
     * @param timeout_millis     internal command send timeout
     * @param disconnectListener listener to be notified in case of disconnection
     * @throws Exception Device not found exception
     */
    public void connectToDevice(String description, int timeout_millis, PortDisconnectListener disconnectListener) throws PortNotFoundException{
        for (SerialPort p : ports) {
            if (p.getPortDescription().contains(description)) {
                sLog.d(this, "Connecting To " + p.getPortDescription() + " [" + p.getSystemPortName() + "]");
                connectToDevice(p, timeout_millis, disconnectListener);
                sLog.d(this, "Connected To " + p.getPortDescription() + " [" + p.getSystemPortName() + "]");
                return;
            }
        }
    }


    /**
     * disconnect function to remove serial port binding
     * can be called anytime
     */
    public void disconnect() {
        if (comPort != null) {
            comPort.removeDataListener();
            comPort.disablePortConfiguration();
            comPort.closePort();
            comPort = null;
        }
        fifo.clear();
        for (RxOnlyModuleHandler rxm : moduleHandlerMap.values()) {
            rxm.reset();
        }
        waitTimeout = false;
    }

    /**
     * try to connect with the descriptive device name specified by the parameter
     *
     * @param port               SerialPort instance
     * @param timeout_millis     internal command send timeout
     * @param disconnectListener listener to be notified in case of disconnection
     * @throws Exception Device not found exception
     */
    private synchronized void connectToDevice(SerialPort port, int timeout_millis, PortDisconnectListener disconnectListener) throws PortNotFoundException {
//        synchronized (portConnectLock) {
        if (comPort == null) {
            this.disconnectListener = disconnectListener;
            port.closePort();
            port.setComPortParameters(19200, 8, 1, 0);
            port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, timeout_millis*20, timeout_millis);
            port.addDataListener(new SerialPortDataListenerWithExceptions() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE && port.bytesAvailable() > 0) {
                        waitTimeout = true;
                        byte[] newData = new byte[port.bytesAvailable()];
                        port.readBytes(newData, newData.length);
                        for (byte b : newData) {
                            fifo.add(b);
                        }
                        processAllPacketsReceived();
                    }
                }

                @Override
                public void catchException(Exception e) {
                    sLog.d(this,e.getMessage());
                    waitTimeout = false;
                }
            });
            port.openPort();
//            to get sync
            byte[] sync = new byte[32];
            Arrays.fill(sync, (byte) 0x55);
            port.writeBytes(sync, sync.length);
//

            comPort = port;
            threadHeartBeat = new Thread(() -> {
                ComLib.get().getPumpSerialNumberHandler().requestPumpNumber();
                while (comPort!=null) {
                    try {
                        ComLib.get().getHeartBeatHandler().send();
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "heartbeat");
            threadHeartBeat.start();

            ComLib.get().getAuxPumpList().requestList(0);
            sLog.d(this, "Connected To " + comPort.getPortDescription() + " [" + comPort.getSystemPortName() + "]");
        } else {
            throw new PortNotFoundException("Already connected on Port: " + comPort.getSystemPortName() + " Please Disconnect First");
        }
//        }
    }

    /**
     * @return true if currently connected to any port
     */
    public boolean isConnected() {
        return comPort != null;
    }

    public void startDisconnectionListener(){
        thread = new Thread(() -> {
            waitTimeout = true;
            while (waitTimeout) {
                waitTimeout = false;
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String portName = comPort.getDescriptivePortName();
            CommHandler.this.disconnect();
            disconnectListener.disconnected(portName);
        }, "SPC_DL");
        thread.start();
    }

    /**
     * decode stream
     */
    private synchronized void processAllPacketsReceived() {
        while (fifo.size() > 1) {
            RxOnlyModuleHandler moduleHandler = moduleHandlerMap.get(fifo.get(0));
            if (moduleHandler != null && moduleHandler.rxLength == fifo.get(1)) {
                if (fifo.size() > (fifo.get(1) & 0xFF) + 2) {
                    if (isChecksumValid()) {

                        moduleHandler.decode(fifo);
                        moduleHandler.cancelTimeout();

                        synchronized (moduleHandler.getRxListener()) {
                            for (int i = 0; i < moduleHandler.getRxListener().size(); i++) {
                                if (moduleHandler.getRxListener().get(i).isActive()) {
                                    moduleHandler.getRxListener().get(i).OnReceive();
                                }
                            }
                            for (int i = moduleHandler.getRxListener().size() - 1; i >= 0; i--) {
                                if (!moduleHandler.getRxListener().get(i).isActive()) {
                                    moduleHandler.getRxListener().remove(i);
                                }
                            }
                        }

                        fifo.subList(0, (fifo.get(1) & 0xFF) + 2).clear();
                    } else {
                        sLog.d(this, "checksum error " + (fifo.get(0) & 0xFF) + ":" + (fifo.get(1) & 0xFF));
                        fifo.remove(0);
                    }
                } else {
                    break;
                }
            } else {
                sLog.d(this, "possible pkt " + (fifo.get(0) & 0xFF) + ":" + (fifo.get(1) & 0xFF));
                fifo.remove(0);
            }
        }
    }

    /**
     * validate stream
     *
     * @return true if integral
     */
    private boolean isChecksumValid() {
        // cal checksum
        int ch = 0;
        for (int i = 0; i < fifo.get(1) + 2; i++) {
            ch += fifo.get(i);
        }
        return ((byte) (ch & 0xFF) == 0);
    }

    /**
     * create a hook of the receive handler passed to the rx stream
     *
     * @param moduleHandler receive handler instance
     */
    public void addModule(RxOnlyModuleHandler moduleHandler) {
        moduleHandlerMap.put(moduleHandler.rxID, moduleHandler);
    }

    /**
     * transmits byte array to currently connected port must connect to port prior to using this command
     *
     * @param bytes array of bytes
     * @return true if transmission successful
     */
    public CommTxResult transmitBytes(byte[] bytes) {
        try {
            if (comPort != null) {
                if (!comPort.isOpen()) {
                    if (!comPort.openPort()) {
                        sLog.e(this, "Unable to open port");
                        return CommTxResult.PORT_ERROR;
                    }
                }
                // calculate checksum
                int ch = -bytes[bytes.length - 1];
                for (byte b : bytes) {
                    ch += b;
                }
                bytes[bytes.length - 1] = (byte) ((0 - ch) & 0XFF);
                //
                if (comPort.writeBytes(bytes, bytes.length) == bytes.length) {
                    return CommTxResult.TX_SUCCESS;
                } else {
                    sLog.e(this, "Write Timeout");
                    sLog.e(this, "make sure device is connected");
                    return CommTxResult.TX_ERROR;
                }
            } else {
                sLog.e(this, "Device is not Connected");
                sLog.e(this, "please connect one first");
                return CommTxResult.DISCONNECTED;
            }
        }catch (NullPointerException e){
            sLog.d(this,e.toString());
            waitTimeout=false;
            return CommTxResult.TX_ERROR;
        }
    }

    /**
     * @return returns port name currently connected to
     */
    public String getConnectedPortName() {
        return comPort != null ? comPort.getSystemPortName() : "";
    }

    public String getComPortDescription() {
        return comPort.getPortDescription();
    }
}