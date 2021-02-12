/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * handles tubing calibration commands and response
 *
 * @author Anuj Pathak
 */
public class TubingCalibrationHandler extends TxRxModuleHandler {

    private boolean calibrationResultValid;
    private boolean calibrationIntruppted;
    private boolean calibrationError;
    private int motorPositionChange;
    //
    private final transient byte[] reqPkt = {REQUEST_TUBING_CALIBRATION_RESULT_ID, REQUEST_TUBING_CALIBRATION_RESULT_LENGTH, 0};

    @Override
    public void reset() {
        calibrationIntruppted = false;
        calibrationResultValid = false;
        calibrationError = false;
        motorPositionChange = 0;
    }

    public TubingCalibrationHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, REQUEST_TUBING_CALIBRATION_ID, REQUEST_TUBING_CALIBRATION_LENGTH
                , REPORT_TUBING_CALIBRATION_RESULT_ID, REPORT_TUBING_CALIBRATION_RESULT_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        calibrationResultValid = (fifo.get(2) & 0x01) > 0;
        calibrationIntruppted = (fifo.get(2) & 0x02) > 0;
        calibrationError = (fifo.get(2) & 0x04) > 0;
        motorPositionChange = ufx.BytesToInt(fifo, 3, 4, true, false);
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * init calibration with the values provided
     *
     * @param speedInDegree speed in degree ( not RPM )
     * @param rotation      rmp
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestCalibration(int speedInDegree, int rotation, byte tubeID) {
        ufx.saveIntToBytes(bytes, 2, speedInDegree, 4, true);
        ufx.saveIntToBytes(bytes, 6, rotation, 2, true);
        bytes[8] = tubeID;
        return TransmitPacket(bytes);
    }

    /**
     * request calibration result
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestResult() {
        return TransmitPacket(reqPkt);
    }

    /**
     * @return true if result is valid
     */
    public boolean isCalibrationResultValid() {
        return calibrationResultValid;
    }

    /**
     * @return true if process is interrupted
     */
    public boolean isCalibrationIntruppted() {
        return calibrationIntruppted;
    }

    /**
     * @return true if calibration results in e
     */
    public boolean isCalibrationError() {
        return calibrationError;
    }

    /**
     * @return motor position change
     */
    public int getMotorPositionChange() {
        return motorPositionChange;
    }

}