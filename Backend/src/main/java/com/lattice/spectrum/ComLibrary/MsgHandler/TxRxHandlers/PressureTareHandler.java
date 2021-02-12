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

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * handles tare command and response
 *
 * @author Anuj Pathak
 */
public class PressureTareHandler extends TxRxModuleHandler {
    @Override
    public void reset() {

    }

    public PressureTareHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, START_PRESSURE_TARE_ID, START_PRESSURE_TARE_LENGTH, PRESSURE_TARE_DONE_ID, PRESSURE_TARE_DONE_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        if (enableLog) {
            sLog.d(this, "Taring Done");
        }
    }

    /**
     * init pressure taring process
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult start() {
        return TransmitPacket(bytes);
    }

}
