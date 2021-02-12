/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 22/09/19, 11:52 AM
 * Modified : 12/09/19, 10:23 AM
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
 * handles Heartbeat command and response
 *
 */
public class HeartBeatHandler extends TxRxModuleHandler {
    @Override
    public void reset() {

    }

    public HeartBeatHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, START_HEART_BEAT_ID, START_HEART_BEAT_LENGTH, START_HEART_BEAT_ID, START_HEART_BEAT_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
        }
    }

    /**
     * Send heartbeat packet
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult send() {
        return TransmitPacket(bytes);
    }

}
