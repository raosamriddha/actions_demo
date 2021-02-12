/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 16/5/19 12:27 PM
 * Modified : 25/4/19 1:05 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxModuleHandler;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureUnits;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * handles tare command and response
 *
 * @author Anuj Pathak
 */
public class PressureCalibrateHandler extends TxRxModuleHandler {

    private PressureUnits unit;
    private double scaleFactor;
    private byte result;
    private double value;

    @Override
    public void reset() {
        unit = null;
        scaleFactor = 0;
        result = 0;
        value = 0;
    }

    public PressureCalibrateHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, CALIBRATE_PRESSURE_ID, CALIBRATE_PRESSURE_LENGTH, CALIBRATE_PRESSURE_DONE_ID, CALIBRATE_PRESSURE_DONE_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        unit = PressureUnits.values()[fifo.get(2) & 0x03];
        scaleFactor = Math.pow(0.1, (fifo.get(2) >> 2) & 0x07);
        result = fifo.get(3);
        value = scaleFactor * ufx.BytesToInt(fifo, 4, 2, true, true);
        if (enableLog) {
            sLog.d(this, "Pressure Calibration Done");
            sLog.d(this);
        }
    }

    /**
     * set internal pressure calibration endPointValue
     *
     * @param channel_id 0,1,2 for feed, retentate, permeate respectively
     * @param Value      pressure endPointValue to be calibrated against should be adjusted against scale factor
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult calibrate(int channel_id, int Value) {
        bytes[2] = (byte) channel_id;
        ufx.saveIntToBytes(bytes, 3, Value, 2, true);
        return TransmitPacket(bytes);
    }

    public byte getResult() {
        return result;
    }

    public double getValue() {
        return value;
    }

    public PressureUnits getUnit() {
        return unit;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
