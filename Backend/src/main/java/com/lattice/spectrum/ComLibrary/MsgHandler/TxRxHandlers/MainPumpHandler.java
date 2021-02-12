/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * decode main pump status and used to set main pump speed
 *
 * @author Anuj Pathak
 */
public class MainPumpHandler extends TxRxModuleHandler {

    private static final String[] modelIDS = {"KR2i",
            "KMPi",
            "KR2i with Quattro pump head",
            "KMPi with Quattro pump head"};

    private int motorStatus;
    private int interfaceProcessorStatus;
    private boolean pumpSoftwareChecksumError;
    private boolean mainProcessorInProgramming;
    private boolean pumpIsStarting;
    private boolean taringReadings;
    private boolean isPumpHeadReadingValid;
    private boolean isPumpHeadOpen;
    private int hardwareErrorID;
    private int motorSpeed;
    private int mainCpuSoftwareVersion;
    private String interfaceProcessorSoftwareVersion;
    private String MotorModel;

    @Override
    public void reset() {
        MotorModel = null;
        interfaceProcessorSoftwareVersion = null;
        motorStatus = 0;
        interfaceProcessorStatus = 0;
        pumpSoftwareChecksumError = false;
        mainProcessorInProgramming = false;
        pumpIsStarting = false;
        taringReadings = false;
        isPumpHeadReadingValid = false;
        isPumpHeadOpen = false;
        hardwareErrorID = 0;
        motorSpeed = 0;
        mainCpuSoftwareVersion = 0;
    }

    public MainPumpHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SET_MAIN_PUMP_SPEED_ID, SET_MAIN_PUMP_SPEED_LENGTH, MAIN_PUMP_STATUS_ID, MAIN_PUMP_STATUS_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        int temp = ufx.BytesToInt(fifo, 2, 2, true, false);
        motorStatus = temp & 0x07;
        //
        pumpSoftwareChecksumError = ((temp >> 3) & 0x01) == 1;
        //
        interfaceProcessorStatus = (temp >> 4) & 0x03;
        //
        mainProcessorInProgramming = ((temp >> 6) & 0x01) == 1;
        pumpIsStarting = ((temp >> 7) & 0x01) == 1;
        taringReadings = ((temp >> 8) & 0x01) == 1;
        isPumpHeadReadingValid = ((temp >> 9) & 0x01) == 1;
        isPumpHeadOpen = ((temp >> 10) & 0x01) == 1;
        //
        hardwareErrorID = fifo.get(4) & 0xFF;
        //
        motorSpeed = ufx.BytesToInt(fifo, 5, 4, true, true);
        mainCpuSoftwareVersion = ufx.BytesToInt(fifo, 9, 2, true, true);
        interfaceProcessorSoftwareVersion = new String(new byte[]{fifo.get(11), '.', fifo.get(12), '.', fifo.get(13)}, StandardCharsets.UTF_8);
        MotorModel = fifo.get(14) < 4 ? modelIDS[fifo.get(14)] : "Error Reading Model";
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * set main pump speed in speed in degree per seconds
     *
     * @param speed int range defined by type
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setSpeed(int speed) {
        ufx.saveIntToBytes(bytes, 2, speed, 4, true);
        return TransmitPacket(bytes);
    }

    /**
     * set flowrate in ml/m
     *
     * @param mlpm
     * @param tubeFactor
     * @param speedFactor
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setFlow_mlpm(double mlpm, double tubeFactor, double speedFactor) {
        ufx.saveIntToBytes(bytes, 2, (int) (mlpm  * speedFactor / tubeFactor), 4, true);
        return TransmitPacket(bytes);
    }

    /**
     * @return motor current speed
     */
    public int getMotorSpeed() {
        return motorSpeed;
    }

    /**
     * @return current motor status running/stopped etc
     */
    public int getMotorStatus() {
        return motorStatus;
    }

    /**
     * @return interface processor status
     */
    public int getInterfaceProcessorStatus() {
        return interfaceProcessorStatus;
    }

    /**
     * @return true is pump software is corrupted
     */
    public boolean isPumpSoftwareChecksumError() {
        return pumpSoftwareChecksumError;
    }

    /**
     * @return true if in programming mode
     */
    public boolean isMainProcessorInProgramming() {
        return mainProcessorInProgramming;
    }

    /**
     * @return true if starting
     */
    public boolean isPumpIsStarting() {
        return pumpIsStarting;
    }

    /**
     * @return true while traing process is running
     */
    public boolean isTaringReadings() {
        return taringReadings;
    }

    /**
     * @return true if it can successfully read head state
     */
    public boolean isPumpHeadReadingValid() {
        return isPumpHeadReadingValid;
    }

    /**
     * @return true if head is open and reading is valid
     */
    public boolean isPumpHeadOpen() {
        return isPumpHeadOpen;
    }

    /**
     * @return hardware e id
     */
    public int getHardwareErrorID() {
        return hardwareErrorID;
    }

    /**
     * @return main pump software version
     */
    public int getMainCpuSoftwareVersion() {
        return mainCpuSoftwareVersion;
    }

    /**
     * @return interface processor software version
     */
    public String getInterfaceProcessorSoftwareVersion() {
        return interfaceProcessorSoftwareVersion;
    }

    /**
     * @return main pump model id
     */
    public String getMotorModel() {
        return MotorModel;
    }

    public Double getFlowRate(double tubeFactor, double speedFactor){
        double degreeToFlow = 1;

        if (MotorModel.equals(modelIDS[0])) {

            degreeToFlow =  36;

        } else if (MotorModel.equals(modelIDS[1])) {
            degreeToFlow =   28.8;
        } else {
            degreeToFlow = 1;
        }
        return (((motorSpeed/degreeToFlow)*tubeFactor)/speedFactor);
    }
}
