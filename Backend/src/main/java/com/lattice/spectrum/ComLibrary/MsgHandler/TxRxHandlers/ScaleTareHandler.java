/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 3/5/19 11:42 AM
 * Modified : 25/4/19 1:05 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * handles tare command and response
 *
 * @author Anuj Pathak
 */
public class ScaleTareHandler extends TxRxModuleHandler {
    @Override
    public void reset() {

    }

    public ScaleTareHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, START_SCALE_TARE_ID, START_SCALE_TARE_LENGTH, SCALE_TARE_DONE_ID, SCALE_TARE_DONE_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        if (enableLog) {
            sLog.d(this, "Taring Done");
        }
    }

    /**
     * init SCALE taring process
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult start(boolean feedTare, boolean permeateTare) {
        bytes[2] = (byte) ((feedTare ? 0x01 : 0x00) | (permeateTare ? 0x02 : 0x00));
        return TransmitPacket(bytes);
    }

}
