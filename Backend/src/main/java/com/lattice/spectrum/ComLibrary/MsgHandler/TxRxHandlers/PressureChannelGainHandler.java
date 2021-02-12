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
 * handles pressure channel gain commands
 *
 * @author Anuj Pathak
 */
public class PressureChannelGainHandler extends TxRxModuleHandler {

    private int feedGain;
    private int permeateGain;
    private int retentateGain;
    private int feedZero;
    private int permeateZero;
    private int retentateZero;
    private final transient byte[] reqPkt = {REQUEST_PRESSURE_CHANNEL_GAIN_ID, REQUEST_PRESSURE_CHANNEL_GAIN_LENGTH, 0};

    @Override
    public void reset() {
        feedGain = 0;
        permeateGain = 0;
        retentateGain = 0;
        feedZero = 0;
        permeateZero = 0;
        retentateZero = 0;
    }

    public PressureChannelGainHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SET_PRESSURE_CHANNEL_GAIN_ID, SET_PRESSURE_CHANNEL_GAIN_LENGTH, PRESSURE_CHANNEL_GAIN_ID, PRESSURE_CHANNEL_GAIN_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        feedGain = ufx.BytesToInt(fifo, 2, 4, true, true);
        permeateGain = ufx.BytesToInt(fifo, 6, 4, true, true);
        retentateGain = ufx.BytesToInt(fifo, 10, 4, true, true);
        feedZero = ufx.BytesToInt(fifo, 14, 4, true, true);
        permeateZero = ufx.BytesToInt(fifo, 18, 4, true, true);
        retentateZero = ufx.BytesToInt(fifo, 22, 4, true, true);
        if (enableLog) {
            sLog.d(this, ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * set pressure channel gain values supplied by parameters
     * should result in reply
     *
     * @param feedGain      int endPointValue
     * @param permeateGain  int endPointValue
     * @param retentateGain int endPointValue
     * @param feedZero      int endPointValue
     * @param permeateZero  int endPointValue
     * @param retentateZero int endPointValue
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setValues(int feedGain, int permeateGain, int retentateGain, int feedZero, int permeateZero, int retentateZero) {
//        bytes[2] = (byte) 0xFF;
        int off = 0;
        ufx.saveIntToBytes(bytes, off + 2, feedGain, 4, true);
        ufx.saveIntToBytes(bytes, off + 6, permeateGain, 4, true);
        ufx.saveIntToBytes(bytes, off + 10, retentateGain, 4, true);
        ufx.saveIntToBytes(bytes, off + 14, feedZero, 4, true);
        ufx.saveIntToBytes(bytes, off + 18, permeateZero, 4, true);
        ufx.saveIntToBytes(bytes, off + 22, retentateZero, 4, true);

        return TransmitPacket(bytes);
    }

    /**
     * request gain channel values from pump
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestValues() {

        return TransmitPacket(reqPkt);
    }

    /**
     * @return feed gain endPointValue
     */
    public int getFeedGain() {
        return feedGain;
    }

    /**
     * @return permeate gain endPointValue
     */
    public int getPermeateGain() {
        return permeateGain;
    }

    /**
     * @return retentate gain endPointValue
     */
    public int getRetentateGain() {
        return retentateGain;
    }

    /**
     * @return feed zero endPointValue
     */
    public int getFeedZero() {
        return feedZero;
    }

    /**
     * @return permeate zero endPointValue
     */
    public int getPermeateZero() {
        return permeateZero;
    }

    /**
     * @return retentate zero endPointValue
     */
    public int getRetentateZero() {
        return retentateZero;
    }

    @Override
    public String toString() {
        sLog.d(this);
        return super.toString();
    }
}
